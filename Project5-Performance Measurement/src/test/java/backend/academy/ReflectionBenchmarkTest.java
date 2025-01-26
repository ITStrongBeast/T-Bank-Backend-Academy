package backend.academy;

import org.junit.jupiter.api.Test;

class ReflectionBenchmarkTest {

    @Test
    void testMainMethodExecution() {
        Thread mainThread = new Thread(() -> {
            try {
                ReflectionBenchmark.main(new String[]{});
            } catch (Exception e) {
                throw new RuntimeException("Main method threw an exception", e);
            }
        });

        mainThread.start();
    }
}
