package httpTaskServer;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import forGson.GsonGetter;
import managers.taskManager.FileBackedTasksManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final FileBackedTasksManager httpTaskManager = new FileBackedTasksManager();
    private static final Gson gson = GsonGetter.getGson();
    private static final Response response = new Response();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::handleGetPrioritizedTasks);
        httpServer.createContext("/tasks/history", this::handleGetHistory);
        httpServer.createContext("/tasks/task", this::handleTasksOperations);
        httpServer.createContext("/tasks/subtask", this::handleSubtasksOperations);
        httpServer.createContext("/tasks/epic", this::handleEpicsOperations);
        httpServer.createContext("/tasks/subtask/epic", this::handleGetAllSubtasksForEpic);

        httpServer.start();

        System.out.println("HTTP-сервер запущен! Порт: " + PORT);
    }

    private void handleTasksOperations(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Optional<String> query = Optional.ofNullable(exchange.getRequestURI().getQuery());

        switch (method) {
            case "GET":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("GET tasks/task?id={id}");
                    handleGetTaskById(exchange);
                } else {
                    System.out.println("GET tasks/task");
                    handleGetTasks(exchange);
                }
                return;
            case "POST":
                System.out.println("POST tasks/task");
                handlePostTask(exchange);
                return;
            case "DELETE":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("DELETE tasks/task?id={id}");
                    handleDeleteTaskById(exchange);
                } else {
                    System.out.println("DELETE tasks/task");
                    handleDeleteTasks(exchange);
                }
        }
    }

    private void handleSubtasksOperations(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Optional<String> query = Optional.ofNullable(exchange.getRequestURI().getQuery());

        switch (method) {
            case "GET":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("GET tasks/subtask?id={id}");
                    handleGetSubtaskById(exchange);
                } else {
                    System.out.println("GET tasks/subtask");
                    handleGetSubtasks(exchange);
                }
                return;
            case "POST":
                System.out.println("POST tasks/subtask");
                handlePostSubtask(exchange);
                return;
            case "DELETE":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("DELETE tasks/subtask?id={id}");
                    handleDeleteSubtaskById(exchange);
                } else {
                    System.out.println("DELETE tasks/subtask");
                    handleDeleteSubtasks(exchange);
                }
        }
    }

    private void handleEpicsOperations(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Optional<String> query = Optional.ofNullable(exchange.getRequestURI().getQuery());

        switch (method) {
            case "GET":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("GET tasks/epic?id={id}");
                    handleGetEpicById(exchange);
                } else {
                    System.out.println("GET tasks/epic");
                    handleGetEpics(exchange);
                }
                return;
            case "POST":
                System.out.println("POST tasks/epic");
                handlePostEpic(exchange);
                return;
            case "DELETE":
                if (query.isPresent() && (query.get().startsWith("id"))) {
                    System.out.println("DELETE tasks/epic?id={id}");
                    handleDeleteEpicById(exchange);
                } else {
                    System.out.println("DELETE tasks/epic");
                    handleDeleteEpics(exchange);
                }
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllEpics().isEmpty()) {
                for (Integer id : httpTaskManager.getAllEpics().keySet()) {
                    if (taskId == id) {
                        httpTaskManager.deleteEpicById(id);
                        response.writeResponse(exchange,
                                "Супер-задача с индетификатором " + taskId + " успешно удалена!",
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Супер-задача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список супер-задач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор супер-задачи!",
                    400);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllSubtasks().isEmpty()) {
                for (Integer id : httpTaskManager.getAllSubtasks().keySet()) {
                    if (taskId == id) {
                        httpTaskManager.deleteSubtaskById(id);
                        response.writeResponse(exchange,
                                "Подзадача с индетификатором " + taskId + " успешно удалена!",
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Подзадача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список подзадач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор подзадачи!",
                    400);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllTasks().isEmpty()) {
                for (Integer id : httpTaskManager.getAllTasks().keySet()) {
                    if (taskId == id) {
                        httpTaskManager.deleteTaskById(id);
                        response.writeResponse(exchange,
                                "Задача с индетификатором " + taskId + " успешно удалена!",
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Задача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список задач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор задачи!",
                    400);
        }
    }

    private void handleDeleteEpics(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllEpics().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно удалить список супер-задач, так как пока нет никаких супер-задач!",
                    404);//no content
        } else {
            httpTaskManager.deleteAllEpics();
            response.writeResponse(exchange,
                    "Супер-задачи успешно удалены!",
                    200);
        }
    }

    private void handleDeleteSubtasks(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllSubtasks().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно удалить список подзадач, так как пока нет никаких подзадач!",
                    404);//no content
        } else {
            httpTaskManager.deleteAllSubtasks();
            response.writeResponse(exchange,
                    "Подзадачи успешно удалены!",
                    200);
        }
    }

    private void handleDeleteTasks(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllTasks().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно удалить список задач, так как пока нет никаких задач!",
                    404);//no content
        } else {
            httpTaskManager.deleteAllTasks();
            response.writeResponse(exchange,
                    "Задачи успешно удалены!",
                    200);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String status = jsonObject.get("status").getAsString();

            Epic epic = new Epic(name, description, TaskStatus.valueOf(status));

            if (epic.getName().isEmpty() || epic.getName().isBlank()) {
                response.writeResponse(exchange,
                        "Название супер-задачи не может быть пустым.",
                        400);
            } else if (epic.getDescription().isEmpty() || epic.getDescription().isBlank()) {
                response.writeResponse(exchange,
                        "Описание супер-задачи не может быть пустым.",
                        400);
            } else {
                for (Integer id : httpTaskManager.getAllEpics().keySet()) {
                    if (epic.getId() == id) {

                        httpTaskManager.updateEpic(epic);
                        response.writeResponse(exchange,
                                "Супер-задача успешно обновлена!",
                                201);
                        return;
                    }
                }
                httpTaskManager.createEpic(epic);
                response.writeResponse(exchange,
                        "Супер-задача успешно добавлена!",
                        201);

            }
        } catch (JsonSyntaxException exception) {
            response.writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String status = jsonObject.get("status").getAsString();
            String startTime = jsonObject.get("startTime").getAsString();
            long duration = jsonObject.get("duration").getAsLong();
            int idEpic = jsonObject.get("idEpic").getAsInt();

            Subtask subtask = new Subtask(name, description, TaskStatus.valueOf(status),
                    LocalDateTime.parse(startTime), duration, idEpic);

            if (subtask.getName().isEmpty() || subtask.getName().isBlank()) {
                response.writeResponse(exchange,
                        "Название подзадачи не может быть пустым.",
                        400);
            } else if (subtask.getDescription().isEmpty() || subtask.getDescription().isBlank()) {
                response.writeResponse(exchange,
                        "Описание подзадачи не может быть пустым.",
                        400);
            } else if (subtask.getDuration() == 0) {
                response.writeResponse(exchange,
                        "Длительность выполнения подзадачи не может равняться 0. " +
                                "Вы же не Флеш в конце концов...",
                        400);
            } else if (httpTaskManager.isIntersectionByTime(subtask)) {
                response.writeResponse(exchange,
                        "Дата и время начала выполнения подзадачи не может" +
                                " пересекаться с другими задачами и подзадачами." +
                                " Даже если вы сам Гай Юлий Цезарь.",
                        400);
            } else if (httpTaskManager.getAllEpics().isEmpty()) {
                response.writeResponse(exchange,
                        "Супер-задачи с идентификатором " + subtask.getIdEpic() +
                                " не существует, так как список супер-задач пуст!",
                        400);
            } else {
                if (httpTaskManager.getAllEpics().containsKey(subtask.getIdEpic())) {
                    if (!httpTaskManager.getAllSubtasks().isEmpty()) {
                        for (Integer id : httpTaskManager.getAllSubtasks().keySet()) {
                            if (subtask.getId() == id) {
                                httpTaskManager.updateSubtask(subtask);
                                response.writeResponse(exchange,
                                        "Подзадача успешно обновлена!",
                                        201);
                                return;
                            }
                        }
                    }
                    httpTaskManager.createSubtask(subtask);
                    response.writeResponse(exchange,
                            "Подзадача успешно добавлена!",
                            201);
                }
                response.writeResponse(exchange,
                        "Супер-задачи с идентификатором " + subtask.getIdEpic() + " не существует!",
                        404);
            }
        } catch (JsonSyntaxException exception) {
            response.writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String status = jsonObject.get("status").getAsString();
            String startTime = jsonObject.get("startTime").getAsString();
            long duration = jsonObject.get("duration").getAsLong();

            Task task = new Task(name, description, TaskStatus.valueOf(status),
                    LocalDateTime.parse(startTime), duration);

            if (task.getName().isEmpty() || task.getName().isBlank()) {
                response.writeResponse(exchange,
                        "Название задачи не может быть пустым.",
                        400);
            } else if (task.getDescription().isEmpty() || task.getDescription().isBlank()) {
                response.writeResponse(exchange,
                        "Описание задачи не может быть пустым.",
                        400);
            } else if (task.getDuration() == 0) {
                response.writeResponse(exchange,
                        "Длительность выполнения задачи не может равняться 0. " +
                                "Вы же не Флеш в конце концов...",
                        400);
            } else if (httpTaskManager.isIntersectionByTime(task)) {
                response.writeResponse(exchange,
                        "Дата и время начала выполнения задачи не может" +
                                " пересекаться с другими задачами и подзадачами." +
                                " Даже если вы сам Гай Юлий Цезарь.",
                        400);
            } else {
                if (!httpTaskManager.getAllTasks().isEmpty()) {
                    for (Integer id : httpTaskManager.getAllTasks().keySet()) {
                        if (task.getId() == id) {
                            httpTaskManager.updateTask(task);
                            response.writeResponse(exchange,
                                    "Задача успешно обновлена!",
                                    201);
                            return;
                        }
                    }
                }
                httpTaskManager.createTask(task);
                response.writeResponse(exchange,
                        "Задача успешно добавлена!",
                        201);
            }
        } catch (JsonSyntaxException exception) {
            response.writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void handleGetAllSubtasksForEpic(HttpExchange exchange) throws IOException {
        System.out.println("GET tasks/subtask/epic?id={id}");
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllEpics().isEmpty()) {
                for (Integer id : httpTaskManager.getAllEpics().keySet()) {
                    if (taskId == id) {
                        if (!httpTaskManager.getAllSubtasksInOneEpic(id).isEmpty()) {
                            String subtasksForEpic = gson.toJson(httpTaskManager.getAllSubtasksInOneEpic(id));
                            response.writeResponse(exchange,
                                    subtasksForEpic,
                                    200);
                            return;
                        }
                        response.writeResponse(exchange,
                                "Для супер-задачи с индетификатором " + taskId + " не найдены под задачи!",
                                404);
                    }
                }
                response.writeResponse(exchange,
                        "Супер-задача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список супер-задач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор супер-задачи!",
                    400);
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllEpics().isEmpty()) {
                for (Integer id : httpTaskManager.getAllEpics().keySet()) {
                    if (taskId == id) {
                        String epic = gson.toJson(httpTaskManager.getEpicById(id));
                        response.writeResponse(exchange,
                                epic,
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Супер-задача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список супер-задач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор супер-задачи!",
                    400);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();

            if (!httpTaskManager.getAllSubtasks().isEmpty()) {
                for (Integer id : httpTaskManager.getAllSubtasks().keySet()) {
                    if (taskId == id) {
                        String subtask = gson.toJson(httpTaskManager.getSubtaskById(id));
                        response.writeResponse(exchange,
                                subtask,
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Подзадача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список подзадач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор подзадачи!",
                    400);
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> idOptional = getId(exchange);
        if (idOptional.isPresent()) {
            int taskId = idOptional.get();
            if (!httpTaskManager.getAllTasks().isEmpty()) {
                for (Integer id : httpTaskManager.getAllTasks().keySet()) {
                    if (taskId == id) {
                        String task = gson.toJson(httpTaskManager.getTaskById(id));
                        response.writeResponse(exchange,
                                task,
                                200);
                        return;
                    }
                }
                response.writeResponse(exchange,
                        "Задача с индетификатором " + taskId + " не найдена!",
                        404);
            }
            response.writeResponse(exchange,
                    "Список задач пуст!",
                    400);
        } else {
            response.writeResponse(exchange,
                    "Неверный идентфикатор задачи!",
                    400);
        }
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        System.out.println("GET tasks");
        if (httpTaskManager.getPrioritizedTasks().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно получить задачи по времени начала, так как этот список пока пуст!",
                    404);
        } else {
            response.writeResponse(exchange,
                    gson.toJson(httpTaskManager.getPrioritizedTasks()),
                    200);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        System.out.println("GET tasks/history");
        if (httpTaskManager.getHistoryManager().getHistory().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно получить историю просмотра, так как история пока пуста!",
                    404);
        } else {
            response.writeResponse(exchange,
                    gson.toJson(httpTaskManager.getHistoryManager().getHistory()),
                    200);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllEpics().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно получить список супер-задач, так как пока нет никаких супер-задач!",
                    404);
        } else {
            String allEpics = gson.toJson(httpTaskManager.getAllEpics());
            response.writeResponse(exchange,
                    allEpics,
                    200);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllSubtasks().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно получить список подзадач, так как пока нет никаких подзадач!",
                    404);
        } else {
            response.writeResponse(exchange,
                    gson.toJson(httpTaskManager.getAllSubtasks()),
                    200);
        }

    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        if (httpTaskManager.getAllTasks().isEmpty()) {
            response.writeResponse(exchange,
                    "Невозможно получить список задач, так как пока нет никаких задач!",
                    404);
        } else {
            response.writeResponse(exchange,
                    gson.toJson(httpTaskManager.getAllTasks()),
                    200);
        }
    }

    private Optional<Integer> getId(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String[] idFromPath = query.split("=");
        try {
            return Optional.of(Integer.parseInt(idFromPath[1]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public void stop(int delay) {
        System.out.println("Завершаем работу сервера!");
        httpServer.stop(0);
    }
}
