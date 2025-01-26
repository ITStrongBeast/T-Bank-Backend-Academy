package backend.academy.parsers;

import backend.academy.LogRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements Parsers {
    private final BufferedReader reader;

    @SuppressWarnings("MultipleStringLiterals")
    private static final String LOG_REGEX =
        "(\\S+) " + "(\\S+) " + "(\\S+) " + "\\[(.+?)\\] " + "\"(\\S+) (\\S+) (\\S+)\" "
            + "(\\d{3}) " + "(\\d+) " + "\"([^\"]*)\" " + "\"([^\"]*)\"";
    private static final Pattern PATTERN = Pattern.compile(LOG_REGEX);

    @SuppressWarnings("MagicNumber")
    public LogRecord parse(String logLine) throws IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        Matcher matcher = PATTERN.matcher(logLine);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Строка лога не соответствует ожидаемому формату.");
        }
        String timeLocalStr = matcher.group(4).split("\\s+")[0];
        LocalDateTime timeLocal;

        try {
            timeLocal = LocalDateTime.parse(timeLocalStr, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат даты: " + timeLocalStr, e);
        }

        return new LogRecord(
            matcher.group(1),
            matcher.group(3),
            timeLocal,
            matcher.group(5),
            matcher.group(6),
            matcher.group(7),
            matcher.group(8),
            Integer.parseInt(matcher.group(9)),
            matcher.group(10),
            matcher.group(11).split(" ")[0]
        );
    }

    public LogParser(Path path) throws IOException {
        reader = Files.newBufferedReader(path);
    }

    @Override
    public LogRecord nextLog() throws IOException {
        return parse(reader.readLine());
    }

    @Override
    public boolean hasNextLog() throws IOException {
        return reader.ready();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
