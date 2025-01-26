package backend.academy;

import backend.academy.exceptions.ArgumentsException;
import backend.academy.exports.AdocExport;
import backend.academy.exports.Exports;
import backend.academy.exports.MarkdownExport;
import backend.academy.finders.Finders;
import backend.academy.finders.LocalFileFinder;
import backend.academy.finders.URLFileFinder;
import backend.academy.parsers.LogParser;
import backend.academy.parsers.Parsers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@SuppressWarnings("MultipleStringLiterals")
public class LogReport {
    private static final String URL_REGEX =
        "^(https?|ftp)://[\\w.-]+(?:\\.[\\w.-]+)+[/#?]?.*$";
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    private static final Pattern PATTERN = Pattern.compile(URL_REGEX);

    private final Finders finder;
    private final Analyzer analyzer = new Analyzer();
    private Exports writer = new AdocExport("analysis");

    private final Map<String, Consumer<String>> settings = new HashMap<>(Map.of(
        "--format", s -> this.writer = Objects.equals(s, "markdown")
            ? new MarkdownExport("analysis") : new AdocExport("analysis"),
        "--from", s -> this.analyzer.from(LocalDateTime.parse(s, FORMATTER)),
        "--to", s -> this.analyzer.to(LocalDateTime.parse(s, FORMATTER)),
        "--filter-field", this.analyzer::filterField,
        "--filter-value", this.analyzer::filterValue
    ));

    public LogReport(String[] args) throws ArgumentsException {
        if (args.length < 2 || !Objects.equals(args[0], "--path")) {
            throw new ArgumentsException("Вы ввели некорректный путь до лог-файлов");
        }

        this.finder = PATTERN.matcher(args[1]).matches() ? new URLFileFinder(args[1]) : new LocalFileFinder(args[1]);

        for (int i = 2; i < args.length - 1; i += 2) {
            Consumer<String> consumer = settings.get(args[i]);
            if (consumer == null) {
                throw new ArgumentsException("Вы ввели некорректные аргументы");
            }
            consumer.accept(args[i + 1]);
        }
    }

    public void analysis() throws IOException, InterruptedException {
        finder.findFiles().forEach(
            s -> {
                try (Parsers parser = new LogParser(s)) {
                    while (parser.hasNextLog()) {
                        analyzer.analysis(parser.nextLog());
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        );
        writer.write(analyzer);
    }
}
