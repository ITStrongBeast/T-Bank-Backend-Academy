package backend.academy.samples.finders;

import backend.academy.finders.URLFileFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class URLFileFinderTest {

    private static final String INVALID_URL = "http://example.com/invalid";

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @InjectMocks
    private URLFileFinder urlFileFinder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindFilesWithNon200StatusCodeShouldThrowIOException() throws IOException, InterruptedException {
        urlFileFinder = new URLFileFinder(INVALID_URL);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(404);
        assertThatThrownBy(() -> {
            Stream<Path> ignored = urlFileFinder.findFiles();
        }).isInstanceOf(IOException.class)
            .hasMessageContaining(INVALID_URL + " with status: 404");
    }
}
