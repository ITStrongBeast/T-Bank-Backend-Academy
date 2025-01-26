package backend.academy.samples.finders;

import backend.academy.finders.LocalFileFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocalFileFinderTest {

    @TempDir
    Path tempDir;

    private LocalFileFinder fileFinder;

    @BeforeEach
    public void setUp() throws IOException {
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));
        Files.createFile(tempDir.resolve("image.jpg"));
        Files.createFile(tempDir.resolve("document.pdf"));
        Files.createFile(tempDir.resolve("notes.docx"));
    }

    @Test
    public void testFindFilesWithInvalidPathShouldThrowIOException() {
        fileFinder = new LocalFileFinder("glob:invalid_path_pattern/*.txt");

        assertThatThrownBy(() -> {
            Stream<Path> ignored = fileFinder.findFiles();
        }).isInstanceOf(Exception.class);
    }
}
