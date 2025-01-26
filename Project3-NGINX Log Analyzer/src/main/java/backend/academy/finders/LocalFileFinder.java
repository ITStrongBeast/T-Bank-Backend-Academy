package backend.academy.finders;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LocalFileFinder implements Finders {
    private final String pathPattern;

    public LocalFileFinder(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    @Override
    public Stream<Path> findFiles() throws IOException {
        Path path = Paths.get(pathPattern);
        Path rootDir = path.getParent();
        String pattern = path.getFileName().toString();
        if (rootDir == null) {
            rootDir = Paths.get(".");
        }

        final PathMatcher matcher = rootDir.getFileSystem().getPathMatcher("glob:" + pattern);

        List<Path> matchedFiles = new ArrayList<>();

        Files.walkFileTree(rootDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (matcher.matches(file.getFileName())) {
                    matchedFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return matchedFiles.stream();
    }
}
