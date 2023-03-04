package httpTaskServer;

import org.junit.jupiter.api.*;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpTaskServerTest {
    private static HttpTaskServer httpTaskServer;
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final ArrayList<Epic> epics = new ArrayList<>();
    private static final ArrayList<Subtask> subtasks = new ArrayList<>();
    private final String url = "http://localhost:8080";
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final static String jsonStringTask =
            "{\n" +
                    "\"name\": \"Моя первая задача\",\n" +
                    "\"description\": \"Описание задачи номер 1\",\n" +
                    "\"status\": \"NEW\",\n" +
                    "\"startTime\": \"2023-02-26T00:00:01\",\n" +
                    "\"duration\": 80\n" +
                    "}";
    private final static String jsonStringSubtask =
            "{\n" +
                    "\"name\": \"Моя первая задача\",\n" +
                    "\"description\": \"Описание задачи номер 1\",\n" +
                    "\"status\": \"NEW\",\n" +
                    "\"startTime\": \"2023-02-28T00:00:01\",\n" +
                    "\"duration\": 80,\n" +
                    "\"idEpic\": 0\n" +
                    "}";

    private final static String jsonEpicString =
            "{\n" +
                    "\"name\": \"My Epic\",\n" +
                    "\"description\": \"Описание задачи номер 1\",\n" +
                    "\"status\": \"NEW\"\n" +
                    "}";


    @BeforeAll
    public static void beforeAll() throws IOException {
        httpTaskServer = new HttpTaskServer();
    }

    @AfterAll
    public static void afterAll() {
        httpTaskServer.stop(0);
    }

    @Test
    @Order(1)
    public void testPostRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response2 = client.send(postRequest(jsonEpicString, "epic"), handler);

        assertEquals("Супер-задача успешно добавлена!", response2.body());
        assertEquals(201, response2.statusCode());

        HttpResponse<String> response1 = client.send(postRequest(jsonStringTask, "task"), handler);

        assertEquals("Задача успешно добавлена!", response1.body());
        assertEquals(201, response1.statusCode());


        HttpResponse<String> response3 = client.send(postRequest(jsonStringSubtask, "subtask"), handler);

        assertEquals("Подзадача успешно добавлена!", response3.body());
        assertEquals(201, response3.statusCode());
    }

    @Test
    @Order(2)
    public void testGetRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getRequest("task"), handler);
        HttpResponse<String> response2 = client.send(getRequest("epic"), handler);
        HttpResponse<String> response3 = client.send(getRequest("subtask"), handler);

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
        assertEquals(200, response3.statusCode());
    }

    @Test
    @Order(3)
    public void testGetByIdRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getByIdRequest("task", 1), handler);
        HttpResponse<String> response2 = client.send(getByIdRequest("epic", 0), handler);
        HttpResponse<String> response3 = client.send(getByIdRequest("subtask", 2), handler);

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
        assertEquals(200, response3.statusCode());
    }

    @Test
    @Order(4)
    public void testGetSubtasksForEpicByIdRequests() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getSubtasksForEpicByIdRequest(0), handler);
        assertEquals(200, response1.statusCode());
    }

    @Test
    @Order(5)
    public void testGetByIdWithATypoRequests() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getByIdWithATypoRequest("task", "c"), handler);
        HttpResponse<String> response2 = client.send(getByIdWithATypoRequest("epic", "p"), handler);
        HttpResponse<String> response3 = client.send(getByIdWithATypoRequest("subtask", "p"), handler);

        assertEquals("Неверный идентфикатор задачи!", response1.body());
        assertEquals(400, response1.statusCode());
        assertEquals("Неверный идентфикатор супер-задачи!", response2.body());
        assertEquals(400, response2.statusCode());
        assertEquals("Неверный идентфикатор подзадачи!", response3.body());
        assertEquals(400, response3.statusCode());
    }

    @Test
    @Order(6)
    public void testGetSubtasksForEpicByIdWithATypoRequests() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getSubtasksForEpicByIdWithTypoRequest("c"), handler);

        assertEquals("Неверный идентфикатор супер-задачи!", response1.body());
        assertEquals(400, response1.statusCode());
    }

    @Test
    @Order(7)
    public void testGetByIdRequestsWithWrongId() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getByIdRequest("task", 12), handler);
        HttpResponse<String> response2 = client.send(getByIdRequest("epic", 13), handler);
        HttpResponse<String> response3 = client.send(getByIdRequest("subtask", 14), handler);

        assertEquals("Задача с индетификатором 12 не найдена!", response1.body());
        assertEquals(404, response1.statusCode());
        assertEquals("Супер-задача с индетификатором 13 не найдена!", response2.body());
        assertEquals(404, response2.statusCode());
        assertEquals("Подзадача с индетификатором 14 не найдена!", response3.body());
        assertEquals(404, response3.statusCode());
    }

    @Test
    @Order(8)
    public void testGetPrioritizedTasksRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getPrioritizedTasksRequest(), handler);

        assertEquals(200, response1.statusCode());
    }

    @Test
    @Order(9)
    public void testGetHistoryRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getHistoryRequest(), handler);

        assertEquals(200, response1.statusCode());
    }

    @Test
    @Order(10)
    public void testDeleteByIdRequestsWithWrongId() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(deleteByIdRequest("task", 12), handler);
        HttpResponse<String> response2 = client.send(deleteByIdRequest("epic", 13), handler);
        HttpResponse<String> response3 = client.send(deleteByIdRequest("subtask", 14), handler);

        assertEquals("Задача с индетификатором 12 не найдена!", response1.body());
        assertEquals(404, response1.statusCode());
        assertEquals("Супер-задача с индетификатором 13 не найдена!", response2.body());
        assertEquals(404, response2.statusCode());
        assertEquals("Подзадача с индетификатором 14 не найдена!", response3.body());
        assertEquals(404, response3.statusCode());
    }

    @Test
    @Order(11)
    public void testDeleteByIdWithATypoRequests() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(deleteByIdWithATypoRequest("task", "c"), handler);
        HttpResponse<String> response2 = client.send(deleteByIdWithATypoRequest("epic", "p"), handler);
        HttpResponse<String> response3 = client.send(deleteByIdWithATypoRequest("subtask", "p"), handler);

        assertEquals("Неверный идентфикатор задачи!", response1.body());
        assertEquals(400, response1.statusCode());
        assertEquals("Неверный идентфикатор супер-задачи!", response2.body());
        assertEquals(400, response2.statusCode());
        assertEquals("Неверный идентфикатор подзадачи!", response3.body());
        assertEquals(400, response3.statusCode());
    }

    @Test
    @Order(12)
    public void testDeleteByIdRequestsWithNormalConditions() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(deleteByIdRequest("task", 1), handler);

        assertEquals("Задача с индетификатором 1 успешно удалена!", response1.body());
        assertEquals(200, response1.statusCode());

        HttpResponse<String> response3 = client.send(deleteByIdRequest("subtask", 2), handler);

        assertEquals("Подзадача с индетификатором 2 успешно удалена!", response3.body());
        assertEquals(200, response3.statusCode());

        HttpResponse<String> response2 = client.send(deleteByIdRequest("epic", 0), handler);

        assertEquals("Супер-задача с индетификатором 0 успешно удалена!", response2.body());
        assertEquals(200, response2.statusCode());
    }

    @Test
    @Order(13)
    public void testGetRequestsWithNoData() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getRequest("task"), handler);
        HttpResponse<String> response2 = client.send(getRequest("epic"), handler);
        HttpResponse<String> response3 = client.send(getRequest("subtask"), handler);

        assertEquals("Невозможно получить список задач, так как пока нет никаких задач!",
                response1.body());
        assertEquals(404, response1.statusCode());
        assertEquals("Невозможно получить список супер-задач, так как пока нет никаких супер-задач!",
                response2.body());
        assertEquals(404, response2.statusCode());
        assertEquals("Невозможно получить список подзадач, так как пока нет никаких подзадач!",
                response3.body());
        assertEquals(404, response3.statusCode());
    }

    @Test
    @Order(14)
    public void testGetByIdRequestsWithNoData() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getByIdRequest("task", 0), handler);
        HttpResponse<String> response2 = client.send(getByIdRequest("epic", 2), handler);
        HttpResponse<String> response3 = client.send(getByIdRequest("subtask", 4), handler);

        assertEquals("Список задач пуст!", response1.body());
        assertEquals(400, response1.statusCode());
        assertEquals("Список супер-задач пуст!", response2.body());
        assertEquals(400, response2.statusCode());
        assertEquals("Список подзадач пуст!", response3.body());
        assertEquals(400, response3.statusCode());
    }

    @Test
    @Order(15)
    public void testGetByIdWithATypoRequestsWithNoData() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getByIdWithATypoRequest("task", "c"), handler);
        HttpResponse<String> response2 = client.send(getByIdWithATypoRequest("epic", "p"), handler);
        HttpResponse<String> response3 = client.send(getByIdWithATypoRequest("subtask", "p"), handler);

        assertEquals("Неверный идентфикатор задачи!", response1.body());
        assertEquals(400, response1.statusCode());
        assertEquals("Неверный идентфикатор супер-задачи!", response2.body());
        assertEquals(400, response2.statusCode());
        assertEquals("Неверный идентфикатор подзадачи!", response3.body());
        assertEquals(400, response3.statusCode());
    }

    @Test
    @Order(16)
    public void testGetPrioritizedTasksRequestsWithNoData() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getPrioritizedTasksRequest(), handler);

        assertEquals("Невозможно получить задачи по времени начала, так как этот список пока пуст!",
                response1.body());
        assertEquals(404, response1.statusCode());
    }

    @Test
    @Order(17)
    public void testGetHistoryRequestsWithNoData() throws IOException, InterruptedException {
        HttpResponse<String> response1 = client.send(getHistoryRequest(), handler);

        assertEquals("Невозможно получить историю просмотра, так как история пока пуста!", response1.body());
        assertEquals(404, response1.statusCode());
    }


    //region Requests
    public HttpRequest postRequest(String task, String type) {
        //  String string = GsonGetter.getGson().toJson(task);
        String newUrl = url + "/tasks/" + type;
        URI uri = URI.create(newUrl);
        final HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(task);
        return HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getRequest(String type) {
        String newUrl = url + "/tasks/" + type;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getByIdRequest(String type, int id) {
        String newUrl = url + "/tasks/" + type + "?id=" + id;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getByIdWithATypoRequest(String type, String typo) {
        String newUrl = url + "/tasks/" + type + "?id=" + typo;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getHistoryRequest() {
        String newUrl = url + "/tasks/history";
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getPrioritizedTasksRequest() {
        String newUrl = url + "/tasks";
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest deleteRequest(String type) {
        String newUrl = url + "/tasks/" + type;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest deleteByIdRequest(String type, int id) {
        String newUrl = url + "/tasks/" + type + "?id=" + id;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest deleteByIdWithATypoRequest(String type, String typo) {
        String newUrl = url + "/tasks/" + type + "?id=" + typo;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getSubtasksForEpicByIdRequest(int id) {
        String newUrl = url + "/tasks/subtask/epic?id=" + id;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public HttpRequest getSubtasksForEpicByIdWithTypoRequest(String typo) {
        String newUrl = url + "/tasks/subtask/epic?id=" + typo;
        URI uri = URI.create(newUrl);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
    //endregion
}
