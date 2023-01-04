package managers.taskManager;

import managers.historyManager.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    HistoryManager getHistoryManager();

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Subtask> getAllSubtasks();

    HashMap<Integer, Epic> getAllEpics();

    Task getTaskById(int idTask);

    Subtask getSubtaskById(int idSubtask);

    Epic getEpicById(int idEpic);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    void deleteTaskById(int idTask);

    void deleteSubtaskById(int idSubtask);

    void deleteEpicById(int idEpic);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    ArrayList<Subtask> getAllSubtasksInOneEpic(int idEpic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic, ArrayList<Subtask> subtaskInEpicArrayList);
}
