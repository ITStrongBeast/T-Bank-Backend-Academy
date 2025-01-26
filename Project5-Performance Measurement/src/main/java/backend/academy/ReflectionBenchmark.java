package backend.academy;

import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.NANOSECONDS) @State(Scope.Thread)
public class ReflectionBenchmark {

    private Student student;
    private Method method;
    private MethodHandle methodHandle;
    private NameGetter lambdaGetter;

    @Setup public void setup() throws Throwable {
        student = new Student("George", "Golenkov");
        String name = "name";

        method = Student.class.getMethod(name);
        method.setAccessible(true);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(Student.class, name, MethodType.methodType(String.class));

        CallSite callSite = LambdaMetafactory.metafactory(lookup, "getName", MethodType.methodType(NameGetter.class),
            MethodType.methodType(String.class, Student.class), methodHandle,
            MethodType.methodType(String.class, Student.class));
        lambdaGetter = (NameGetter) callSite.getTarget().invokeExact();
    }

    @Benchmark public void directAccess(Blackhole bh) {
        String name = student.name();
        bh.consume(name);
    }

    @Benchmark public void reflection(Blackhole bh) throws InvocationTargetException, IllegalAccessException {
        String name = (String) method.invoke(student);
        bh.consume(name);
    }

    @Benchmark public void methodHandle(Blackhole bh) throws Throwable {
        String name = (String) methodHandle.invoke(student);
        bh.consume(name);
    }

    @Benchmark public void lambdaMetafactory(Blackhole bh) {
        String name = lambdaGetter.getName(student);
        bh.consume(name);
    }

    @SuppressWarnings({"MagicNumber", "UncommentedMain"})
    public static void main(String[] args) throws RunnerException, IOException {
        Options options =
            new OptionsBuilder().include(ReflectionBenchmark.class.getSimpleName()).shouldFailOnError(true)
                .shouldDoGC(true).mode(Mode.AverageTime).timeUnit(TimeUnit.NANOSECONDS).forks(1).warmupForks(1)
                .warmupIterations(5).warmupTime(TimeValue.seconds(5)).measurementIterations(5)
                .measurementTime(TimeValue.seconds(5)).build();

        new Runner(options).run();
    }

    record Student(String name, String surname) {
    }

    @FunctionalInterface interface NameGetter {
        String getName(Student student);
    }
}
