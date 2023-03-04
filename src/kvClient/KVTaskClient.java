package kvClient;

import exceptions.kvTaskClientExceptions.KVTaskClientGetApiTokenException;
import exceptions.kvTaskClientExceptions.KVTaskClientLoadException;
import exceptions.kvTaskClientExceptions.KVTaskClientSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        getApiToken();
    }

    public void getApiToken() {
        String newUrl = url + "/register";
        URI uri = URI.create(newUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                apiToken = response.body();
            } else {
                throw new KVTaskClientGetApiTokenException("Не удалось получить ApiToken. " +
                        "Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientGetApiTokenException("Не удалось получить ApiToken. Не удалось послать запрос" +
                    " и получить корректный ответ.");
        }
    }

    public void put(String key, String json) {
        String newUrl = url + "/save/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(newUrl);
        final HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientSaveException("Не удалось сохранить информацию на сервер. Не удалось послать запрос" +
                    " и получить корректный ответ.");
        }
    }

    public String load(String key) {
        String loadedFromServer = "";
        String newUrl = url + "/load/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(newUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                loadedFromServer = response.body();
            } else {
                throw new KVTaskClientLoadException("Не удалось загрузить информацию с сервера.  " +
                        "Сервер вернул код состояния: " + response.statusCode());
            }
            return loadedFromServer;
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientLoadException("Не удалось загрузить информацию с сервера. Не удалось послать запрос" +
                    " и получить корректный ответ.");
        }
    }
}
