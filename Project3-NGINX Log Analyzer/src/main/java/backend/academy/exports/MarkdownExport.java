package backend.academy.exports;

public class MarkdownExport extends Export {

    public MarkdownExport(String name) {
        super(name + ".md");
    }

    @Override
    protected void writeStatus(StringBuilder content) {
        append(content, "### Коды ответа");
    }

    @Override
    protected void writeResources(StringBuilder content) {
        append(content, "### Запрашиваемые ресурсы");
    }

    @Override
    protected void writeAgent(StringBuilder content) {
        append(content, "### Агенты запросов");
    }

    @Override
    protected void writeTypeRequest(StringBuilder content) {
        append(content, "### Типы запросов");
    }

    @Override
    protected void writeMetrix(StringBuilder content) {
        content.append("# Анализ логов").append(LS).append(LS);
        append(content, "### Общая информация");
    }

    private void append(StringBuilder content, String add) {
        content.append(add).append(LS);
    }
}
