package managers.taskManager;

import com.google.gson.reflect.TypeToken;
import forGson.GsonGetter;
import kvClient.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;

    public HttpTaskManager(String path) {
        super();
        kvTaskClient = new KVTaskClient(path);
    }

    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    public HttpTaskManager loadFromServer(String path) {
        HttpTaskManager httpTaskManager = new HttpTaskManager(path);

        String tasks = httpTaskManager.getKvTaskClient().load("tasks");
        String subtasks = httpTaskManager.getKvTaskClient().load("subtasks");
        String epics = httpTaskManager.getKvTaskClient().load("epics");
        String history = httpTaskManager.getKvTaskClient().load("history");

        if (!tasks.isEmpty()) {
            Type tasksMapTypes = new TypeToken<HashMap<Integer, Task>>() {
            }.getType();
            HashMap<Integer, Task> tasksMap = GsonGetter.getGson().fromJson(tasks, tasksMapTypes);
            for (Task task : tasksMap.values()) {
                httpTaskManager.checkHashMapId(task);
                httpTaskManager.createTask(task);
            }
        }

        if (!epics.isEmpty()) {
            Type epicsMapTypes = new TypeToken<HashMap<Integer, Epic>>() {
            }.getType();
            HashMap<Integer, Epic> epicMap = GsonGetter.getGson().fromJson(epics, epicsMapTypes);
            for (Epic epic : epicMap.values()) {
                httpTaskManager.checkHashMapId(epic);
                httpTaskManager.createEpic(epic);
            }

            if (!subtasks.isEmpty()) {
                Type subtasksMapTypes = new TypeToken<HashMap<Integer, Subtask>>() {
                }.getType();
                HashMap<Integer, Subtask> subtasksMap = GsonGetter.getGson().fromJson(subtasks, subtasksMapTypes);
                for (Subtask subtask : subtasksMap.values()) {
                    httpTaskManager.checkHashMapId(subtask);
                    httpTaskManager.createSubtask(subtask);
                }
            }
        }

        if (!history.isEmpty()) {
            Type historyListTypes = new TypeToken<ArrayList<Task>>() {
            }.getType();
            ArrayList<Task> historyList = GsonGetter.getGson().fromJson(history, historyListTypes);
            for (Task task : historyList) {
                httpTaskManager.getHistoryManager().add(task);
            }
        }

        return httpTaskManager;
    }

    @Override
    public void save() {
        String tasks = GsonGetter.getGson().toJson(getAllTasks());
        String subtasks = GsonGetter.getGson().toJson(getAllSubtasks());
        String epics = GsonGetter.getGson().toJson(getAllEpics());
        String history = GsonGetter.getGson().toJson(getHistoryManager().getHistory());

        kvTaskClient.put("tasks", tasks);
        kvTaskClient.put("subtasks", subtasks);
        kvTaskClient.put("epics", epics);
        kvTaskClient.put("history", history);
    }
}


