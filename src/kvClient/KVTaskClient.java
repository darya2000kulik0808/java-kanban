package kvClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private  HttpRequest request;
    private final HttpClient  client = HttpClient.newHttpClient();;
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();;
    private HttpResponse<String> response;
    private String url;
    private URI uri;

    public KVTaskClient(String url) {
        this.url = url;
        API_TOKEN = "DEBUG";
    }

    public void getApiToken(){
        String newUrl = url + "/register";
        uri = URI.create(newUrl);
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            response = client.send(request, handler);
            if (response.statusCode() == 200) {
                API_TOKEN = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String json){
        String newUrl = url + "/save/" + key + "?API_TOKEN=" + API_TOKEN;
        uri = URI.create(newUrl);
        final HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key){
        String loadedFromServer = "";
        String newUrl = url + "/load/" + key + "?API_TOKEN=" + API_TOKEN;
        uri = URI.create(newUrl);
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            response = client.send(request, handler);
            if (response.statusCode() == 200) {
                loadedFromServer = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
            return loadedFromServer;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
