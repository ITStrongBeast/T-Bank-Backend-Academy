package backend.academy.samples;

import backend.academy.LogReport;
import backend.academy.exceptions.ArgumentsException;
import backend.academy.exports.AdocExport;
import backend.academy.exports.MarkdownExport;
import backend.academy.finders.LocalFileFinder;
import backend.academy.finders.URLFileFinder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogReportTest {
    @Test
    public void testInvalidArguments() {
        String[] args = {"--incorrect", "path"};
        assertThrows(ArgumentsException.class, () -> new LogReport(args));
    }

    @Test
    public void testLocalFileFinderInitialization()
        throws ArgumentsException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--path", "/local/path/to/logs"};
        LogReport logReport = new LogReport(args);
        Field privateField = LogReport.class.getDeclaredField("finder");
        privateField.setAccessible(true);
        assertInstanceOf(LocalFileFinder.class, privateField.get(logReport));
    }

    @Test
    public void testURLFileFinderInitialization()
        throws ArgumentsException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--path", "http://example.com/logs"};
        LogReport logReport = new LogReport(args);
        Field privateField = LogReport.class.getDeclaredField("finder");
        privateField.setAccessible(true);
        assertInstanceOf(URLFileFinder.class, privateField.get(logReport));
    }

    @Test
    public void testMarkdownExportInitialization()
        throws ArgumentsException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--path", "/local/path/to/logs", "--format", "markdown"};
        LogReport logReport = new LogReport(args);
        Field privateField = LogReport.class.getDeclaredField("writer");
        privateField.setAccessible(true);
        assertInstanceOf(MarkdownExport.class, privateField.get(logReport));
    }

    @Test
    public void testDefaultAdocExportInitialization()
        throws ArgumentsException, NoSuchFieldException, IllegalAccessException {
        String[] args = {"--path", "/local/path/to/logs"};
        LogReport logReport = new LogReport(args);
        Field privateField = LogReport.class.getDeclaredField("writer");
        privateField.setAccessible(true);
        assertInstanceOf(AdocExport.class, privateField.get(logReport));
    }

    @Test
    public void testAnalysisProcess() throws ArgumentsException, IOException, InterruptedException {
        String[] args = {"--path", "/local/path/to/logs"};
        LogReport logReport = spy(new LogReport(args));

        doNothing().when(logReport).analysis();
        logReport.analysis();
        verify(logReport, times(1)).analysis();
    }
}
