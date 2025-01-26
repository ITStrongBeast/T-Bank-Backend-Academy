package backend.academy.parsers;

import backend.academy.LogRecord;
import java.io.IOException;

public interface Parsers extends AutoCloseable {
    LogRecord nextLog() throws IOException;

    boolean hasNextLog() throws IOException;
}
