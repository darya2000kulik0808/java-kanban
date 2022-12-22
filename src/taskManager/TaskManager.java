package taskManager;

import task.Task;
import task.Epic;
import task.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    int hashMapId;
    private ArrayList<String> statusName;

    public TaskManager() {

        statusName = new ArrayList<>();
        statusName.add("NEW"); //0
        statusName.add("IN_PROGRESS"); //1
        statusName.add("DONE"); //2

        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();

        hashMapId = 0;

    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }
    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public Task getTaskById(int idTask) {
        return tasks.get(idTask);
    }
    public Subtask getSubtaskById(int idSubtask) {
        return subtasks.get(idSubtask);
    }
    public Epic getEpicById(int idEpic) {
        return epics.get(idEpic);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }
    public void deleteAllSubtasks() {
        subtasks.clear();
    }
    public void deleteAllEpics() {
        epics.clear();
    }

    public void  deleteTaskById(int idTask) {
        tasks.remove(idTask);
    }
    public void  deleteSubtaskById(int idSubtask) {
        subtasks.remove(idSubtask);
    }
    public void  deleteEpicById(int idEpic) {
        epics.remove(idEpic);
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getIdEpic() == idEpic) {
                deleteSubtaskById(subtask.getId());
            }
        }
    }

    public void createTask(Task task) {
        task.setId(hashMapId);
        tasks.put(hashMapId, task);
        hashMapId++;
    }
    public void createSubtask(Subtask subtask) {
        subtask.setId(hashMapId);
        subtasks.put(hashMapId, subtask);
        hashMapId++;
    }
    public void createEpic(Epic epic) {
        epic.setId(hashMapId);
        epics.put(hashMapId, epic);
        hashMapId++;
    }

    public ArrayList<Subtask> getAllSubtasksInOneEpic(int idEpic) {
        return epics.get(idEpic).getSubtaskInEpics();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
    public void updateEpic(Epic epic, ArrayList<Subtask> subtaskInEpicArrayList) {
        int newSubtasks = 0;
        int doneSubtasks = 0;

        if(!epic.getSubtaskInEpics().isEmpty()) {
            for (Subtask subtask : epic.getSubtaskInEpics()) {
                if (subtask.getStatus().equals(statusName.get(1))) {
                    epic.setStatus(statusName.get(1));
                }

                if (subtask.getStatus().equals(statusName.get(0))) {
                    newSubtasks++;
                }

                if (subtask.getStatus().equals(statusName.get(2))) {
                    doneSubtasks++;
                }
            }

            if (newSubtasks == epic.getSubtaskInEpics().size()) {
                epic.setStatus(statusName.get(0));
            } else if (doneSubtasks == epic.getSubtaskInEpics().size()) {
                epic.setStatus(statusName.get(2));
            }
        } else {
            epic.setSubtaskInEpics(subtaskInEpicArrayList);
            epic.setStatus(statusName.get(0));
        }
        epics.put(epic.getId(), epic);
    }
}
