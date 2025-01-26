package backend.academy.finders;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Finders {
    Stream<Path> findFiles() throws IOException, InterruptedException;
}
