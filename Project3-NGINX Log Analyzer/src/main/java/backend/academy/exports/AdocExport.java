package backend.academy.exports;

public class AdocExport extends Export {

    public AdocExport(String name) {
        super(name + ".adoc");
    }

    @Override
    protected void writeStatus(StringBuilder content) {
        content.append("= Коды ответа =").append(LS);
    }

    @Override
    protected void writeResources(StringBuilder content) {
        content.append("= Запрашиваемые ресурсы =").append(LS);
    }

    @Override
    protected void writeAgent(StringBuilder content) {
        content.append("= Агенты запросов =").append(LS);
    }

    @Override
    protected void writeTypeRequest(StringBuilder content) {
        content.append("= Типы запросов =").append(LS);
    }

    @Override
    protected void writeMetrix(StringBuilder content) {
        content.append("== Анализ логов ==").append(LS).append(LS);
        content.append("= Общая информация =").append(LS);
    }
}
