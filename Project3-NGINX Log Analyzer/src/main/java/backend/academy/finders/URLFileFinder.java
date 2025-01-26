package backend.academy.finders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class URLFileFinder implements Finders {
    private final String urlPattern;

    public URLFileFinder(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public Stream<Path> findFiles() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlPattern))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException(urlPattern + " with status: " + response.statusCode());
        }

        List<String> fileUrls = parseFileUrls(response.body());
        return fileUrls.stream().map(Paths::get);
    }

    private List<String> parseFileUrls(String jsonResponse) {
        return List.of(jsonResponse.replaceAll("[\\[\\]\"]", "").split(","));
    }
}
