package backend.academy.samples.parsers;

import backend.academy.LogRecord;
import backend.academy.parsers.LogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LogParserTest {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);

    private LogParser logParser;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() throws IOException {
        Path tempFile = Files.createTempFile(tempDir, "log", ".txt");
        Files.writeString(tempFile,
            "127.0.0.1 - admin [12/Dec/2021:10:15:30 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla\"\n");
        logParser = new LogParser(tempFile);
    }

    @Test
    public void testParse_validLogLine_shouldReturnLogRecord() {
        String logLine =
            "127.0.0.1 - admin [12/Dec/2021:10:15:30 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla\"";
        LocalDateTime expectedDate = LocalDateTime.parse("12/Dec/2021:10:15:30", FORMATTER);

        LogRecord logRecord = logParser.parse(logLine);

        assertThat(logRecord.remoteAddr()).isEqualTo("127.0.0.1");
        assertThat(logRecord.remoteUser()).isEqualTo("admin");
        assertThat(logRecord.timeLocal()).isEqualTo(expectedDate);
        assertThat(logRecord.typeRequest()).isEqualTo("GET");
        assertThat(logRecord.request()).isEqualTo("/index.html");
        assertThat(logRecord.protoRequest()).isEqualTo("HTTP/1.1");
        assertThat(logRecord.status()).isEqualTo("200");
        assertThat(logRecord.bodyBytesSent()).isEqualTo(1234);
        assertThat(logRecord.httpReferer()).isEqualTo("-");
        assertThat(logRecord.httpUserAgent()).isEqualTo("Mozilla");
    }

    @Test
    public void testParse_invalidLogLine_shouldThrowIllegalArgumentException() {
        String invalidLogLine = "invalid log format line";

        assertThatThrownBy(() -> logParser.parse(invalidLogLine))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Строка лога не соответствует ожидаемому формату");
    }

    @Test
    public void testParse_invalidDateFormat_shouldThrowIllegalArgumentException() {
        String logLineWithInvalidDate =
            "127.0.0.1 - admin [99/Dec/2021:10:15:30 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla\"";

        assertThatThrownBy(() -> logParser.parse(logLineWithInvalidDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Неверный формат даты");
    }

    @Test
    public void testNextLog_validLogInFile_shouldReturnLogRecord() throws IOException {
        assertThat(logParser.hasNextLog()).isTrue();

        LogRecord logRecord = logParser.nextLog();
        LocalDateTime expectedDate = LocalDateTime.parse("12/Dec/2021:10:15:30", FORMATTER);

        assertThat(logRecord.remoteAddr()).isEqualTo("127.0.0.1");
        assertThat(logRecord.remoteUser()).isEqualTo("admin");
        assertThat(logRecord.timeLocal()).isEqualTo(expectedDate);
        assertThat(logRecord.typeRequest()).isEqualTo("GET");
        assertThat(logRecord.request()).isEqualTo("/index.html");
    }
}
