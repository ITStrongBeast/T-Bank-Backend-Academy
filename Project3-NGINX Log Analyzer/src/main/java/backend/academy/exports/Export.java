package backend.academy.exports;

import backend.academy.Analyzer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("MultipleStringLiterals")
public abstract class Export implements Exports {
    private final String file;

    protected Export(String file) {
        this.file = file;
    }

    @Override
    public void write(Analyzer analyzer) {
        String startDate = analyzer.from() == null ? "" : analyzer.from().toString();
        String endDate = analyzer.to() == null ? "" : analyzer.to().toString();

        StringBuilder content = new StringBuilder();
        generateMetrix(startDate, endDate, analyzer.numberOfRequests(), analyzer.getTotalSize() + "b",
            analyzer.getPercentile() + "b", content);

        generateReport(analyzer, content);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content.toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void generateReport(Analyzer analyzer, StringBuilder content) {
        generateResources(content, analyzer.getRequest());
        generateStatus(content, analyzer.getStatus());
        generateAgent(content, analyzer.getAgent());
        generateTypeRequest(content, analyzer.getTypeRequest());
    }

    private void generateStatus(StringBuilder content, TreeMap<Integer, String> status) {
        writeStatus(content);
        content.append(String.format("| %-3s | %-25s | %10s |", "Код", "Имя", "Количество")).append(LS);
        content.append("|:---:|:-------------------------:|:----------:|").append(LS);
        for (Map.Entry<Integer, String> response : status.entrySet()) {
            content.append(String.format("| %-3s | %-25s | %,10d |", response.getValue(),
                codes.get(response.getValue()), response.getKey())).append(LS);
        }
        content.append(LS);
    }

    private void generate(StringBuilder content, TreeMap<Integer, String> resources) {
        content.append("|:-------------------------:|:----------:|").append(LS);
        for (Map.Entry<Integer, String> resource : resources.entrySet()) {
            content.append(String.format("| %-25s | %,10d |", resource.getValue(), resource.getKey())).append(LS);
        }
        content.append(LS);
    }

    private void generateResources(StringBuilder content, TreeMap<Integer, String> resources) {
        writeResources(content);
        content.append(String.format("| %-25s | %10s |", "Ресурс", "Количество")).append(LS);
        generate(content, resources);
    }

    private void generateAgent(StringBuilder content, TreeMap<Integer, String> resources) {
        writeAgent(content);
        content.append(String.format("| %-25s | %10s |", "Агент", "Количество")).append(LS);
        generate(content, resources);
    }

    private void generateTypeRequest(StringBuilder content, TreeMap<Integer, String> resources) {
        writeTypeRequest(content);
        content.append(String.format("| %-25s | %10s |", "Тип", "Количество")).append(LS);
        generate(content, resources);
    }

    private void generateMetrix(
        String startDate, String endDate, long totalRequests,
        String averageResponseSize, String percentile95ResponseSize, StringBuilder content
    ) {
        writeMetrix(content);

        content.append(String.format("| %-30s | %12s |", "Метрика", "Значение")).append(LS);
        content.append("|:------------------------------:|:------------:|").append(LS);
        content.append(String.format("| %-30s | %12s |", "Начальная дата", startDate)).append(LS);
        content.append(String.format("| %-30s | %12s |", "Конечная дата", endDate)).append(LS);
        content.append(String.format("| %-30s | %,12d |", "Количество запросов", totalRequests)).append(LS);
        content.append(String.format("| %-30s | %12s |", "Средний размер ответа", averageResponseSize)).append(LS);
        content.append(String.format("| %-30s | %12s |", "95p размера ответа", percentile95ResponseSize));
        content.append(LS).append(LS);
    }

    protected abstract void writeStatus(StringBuilder content);

    protected abstract void writeResources(StringBuilder content);

    protected abstract void writeAgent(StringBuilder content);

    protected abstract void writeTypeRequest(StringBuilder content);

    protected abstract void writeMetrix(StringBuilder content);
}
