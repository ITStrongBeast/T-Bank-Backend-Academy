package backend.academy.samples;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import backend.academy.Analyzer;
import backend.academy.LogRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.util.TreeMap;

public class AnalyzerTest {

    private Analyzer analyzer;

    @BeforeEach
    public void setUp() {
        analyzer = spy(new Analyzer());
    }

    @Test
    public void testAnalysisWithValidLogRecordShouldAddStatistics() {
        LogRecord log = Mockito.mock(LogRecord.class);

        when(log.timeLocal()).thenReturn(LocalDateTime.now().minusHours(1));
        when(log.bodyBytesSent()).thenReturn(100);
        when(log.typeRequest()).thenReturn("GET");
        when(log.httpUserAgent()).thenReturn("Mozilla");
        when(log.request()).thenReturn("/index.html");
        when(log.status()).thenReturn("200");

        analyzer.analysis(log);

        assertThat(analyzer.numberOfRequests()).isEqualTo(1);
        assertThat(analyzer.getTotalSize()).isEqualTo(100);
        assertThat(analyzer.getPercentile()).isEqualTo(100);

        TreeMap<Integer, String> typeRequest = analyzer.getTypeRequest();
        assertThat(typeRequest).containsEntry(1, "GET");

        TreeMap<Integer, String> agent = analyzer.getAgent();
        assertThat(agent).containsEntry(1, "Mozilla");

        TreeMap<Integer, String> request = analyzer.getRequest();
        assertThat(request).containsEntry(1, "/index.html");

        TreeMap<Integer, String> status = analyzer.getStatus();
        assertThat(status).containsEntry(1, "200");
    }

    @Test
    public void testAnalysisWithInvalidLogRecordShouldNotAddStatistics() {
        LogRecord log = Mockito.mock(LogRecord.class);

        analyzer.filterField("agent");
        analyzer.filterValue("Chrome");

        when(log.timeLocal()).thenReturn(LocalDateTime.now().minusHours(1));
        when(log.httpUserAgent()).thenReturn("Mozilla");

        analyzer.analysis(log);

        assertThat(analyzer.numberOfRequests()).isEqualTo(0);
        assertThat(analyzer.getTotalSize()).isEqualTo(0);
    }

    @Test
    public void testGetPercentileWithMultipleLogRecords() {
        LogRecord log1 = Mockito.mock(LogRecord.class);
        LogRecord log2 = Mockito.mock(LogRecord.class);
        LogRecord log3 = Mockito.mock(LogRecord.class);

        when(log1.timeLocal()).thenReturn(LocalDateTime.now().minusHours(1));
        when(log1.bodyBytesSent()).thenReturn(50);
        when(log2.timeLocal()).thenReturn(LocalDateTime.now().minusHours(2));
        when(log2.bodyBytesSent()).thenReturn(150);
        when(log3.timeLocal()).thenReturn(LocalDateTime.now().minusHours(3));
        when(log3.bodyBytesSent()).thenReturn(100);

        analyzer.analysis(log1);
        analyzer.analysis(log2);
        analyzer.analysis(log3);

        int expectedPercentile = 150;
        assertThat(analyzer.getPercentile()).isEqualTo(expectedPercentile);
    }

    @Test
    public void testSortingOrderInStatMaps() {
        LogRecord log1 = Mockito.mock(LogRecord.class);
        LogRecord log2 = Mockito.mock(LogRecord.class);
        LogRecord log3 = Mockito.mock(LogRecord.class);

        when(log1.timeLocal()).thenReturn(LocalDateTime.now().minusHours(1));
        when(log1.typeRequest()).thenReturn("GET");
        when(log1.httpUserAgent()).thenReturn("Mozilla");
        when(log1.request()).thenReturn("/index.html");
        when(log1.status()).thenReturn("200");

        when(log3.timeLocal()).thenReturn(LocalDateTime.now().minusHours(1));
        when(log3.typeRequest()).thenReturn("GET");
        when(log3.httpUserAgent()).thenReturn("Mozilla");
        when(log3.request()).thenReturn("/index.html");
        when(log3.status()).thenReturn("200");

        when(log2.timeLocal()).thenReturn(LocalDateTime.now().minusHours(2));
        when(log2.typeRequest()).thenReturn("POST");
        when(log2.httpUserAgent()).thenReturn("Chrome");
        when(log2.request()).thenReturn("/submit");
        when(log2.status()).thenReturn("404");

        analyzer.analysis(log1);
        analyzer.analysis(log2);
        analyzer.analysis(log3);

        TreeMap<Integer, String> typeRequest = analyzer.getTypeRequest();
        assertThat(typeRequest.firstEntry().getValue()).isEqualTo("GET");
        assertThat(typeRequest.lastEntry().getValue()).isEqualTo("POST");

        TreeMap<Integer, String> status = analyzer.getStatus();
        assertThat(status.firstEntry().getValue()).isEqualTo("200");
        assertThat(status.lastEntry().getValue()).isEqualTo("404");
    }
}
