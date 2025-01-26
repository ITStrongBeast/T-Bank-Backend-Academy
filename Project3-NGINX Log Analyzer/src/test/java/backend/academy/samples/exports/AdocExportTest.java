package backend.academy.samples.exports;

import backend.academy.Analyzer;
import backend.academy.exports.AdocExport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AdocExportTest {

    private AdocExport adocExport;
    private Analyzer mockAnalyzer;

    @BeforeEach
    public void setUp() {
        adocExport = new AdocExport("test-adoc-export");
        mockAnalyzer = Mockito.mock(Analyzer.class);
    }

    @Test
    public void testWrite_shouldGenerateCorrectContent() throws IOException {
        when(mockAnalyzer.from()).thenReturn(null);
        when(mockAnalyzer.to()).thenReturn(null);
        when(mockAnalyzer.numberOfRequests()).thenReturn(100L);
        when(mockAnalyzer.getTotalSize()).thenReturn(2000L);
        when(mockAnalyzer.getPercentile()).thenReturn(1500);

        TreeMap<Integer, String> statusMap = new TreeMap<>();
        statusMap.put(1, "200");
        statusMap.put(2, "404");

        TreeMap<Integer, String> agentMap = new TreeMap<>();
        agentMap.put(80, "Mozilla");
        agentMap.put(20, "Chrome");

        TreeMap<Integer, String> typeRequestMap = new TreeMap<>();
        typeRequestMap.put(70, "GET");
        typeRequestMap.put(30, "POST");

        TreeMap<Integer, String> requestMap = new TreeMap<>();
        requestMap.put(60, "/index.html");
        requestMap.put(40, "/contact.html");

        when(mockAnalyzer.getStatus()).thenReturn(statusMap);
        when(mockAnalyzer.getAgent()).thenReturn(agentMap);
        when(mockAnalyzer.getTypeRequest()).thenReturn(typeRequestMap);
        when(mockAnalyzer.getRequest()).thenReturn(requestMap);
        adocExport.write(mockAnalyzer);

        String content = Files.readString(Path.of("test-adoc-export.adoc"));

        assertThat(content).contains("== Анализ логов ==")
            .contains("= Общая информация =")
            .contains("= Коды ответа =")
            .contains("= Запрашиваемые ресурсы =")
            .contains("= Агенты запросов =")
            .contains("= Типы запросов =");

        assertThat(content).contains("| Начальная дата                 |              |")
            .contains("| Конечная дата                  |              |")
            .contains("| Количество запросов            |          100 |")
            .contains("| Средний размер ответа          |        2000b |")
            .contains("| 95p размера ответа             |        1500b |");

        assertThat(content).contains("| Код | Имя                       | Количество |")
            .contains("| 200 | OK                        |          1 |")
            .contains("| 404 | Not Found                 |          2 |");

        assertThat(content).contains("| Агент                     | Количество |")
            .contains("| Chrome                    |         20 |")
            .contains("| Mozilla                   |         80 |");

        assertThat(content).contains("| Тип                       | Количество |")
            .contains("| POST                      |         30 |")
            .contains("| GET                       |         70 |");
    }
}

