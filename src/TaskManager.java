import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> taskHashMap;
    HashMap<Integer, Epic> epicHashMap;
    HashMap<Integer, Subtask> subtaskHashMap;

    int hashMapId;
    private ArrayList<String> statusName;

    public TaskManager() {

        statusName = new ArrayList<>();
        statusName.add("NEW"); //0
        statusName.add("IN_PROGRESS"); //1
        statusName.add("DONE"); //2

        this.taskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
        this.subtaskHashMap = new HashMap<>();

        hashMapId = 0;

    }

    public HashMap<Integer, Task> getAllTasks() {
        return  taskHashMap;
    }
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtaskHashMap;
    }
    public HashMap<Integer, Epic> getAllEpics() {
        return epicHashMap;
    }

    public Task getTaskById(int idTask) {
        return taskHashMap.get(idTask);
    }
    public Subtask getSubtaskById(int idSubtask) {
        return subtaskHashMap.get(idSubtask);
    }
    public Epic getEpicById(int idEpic) {
        return epicHashMap.get(idEpic);
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }
    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
    }
    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    public void  deleteTaskById(int idTask) {
        taskHashMap.remove(idTask);
    }
    public void  deleteSubtaskById(int idSubtask) {
        subtaskHashMap.remove(idSubtask);
    }
    public void  deleteEpicById(int idEpic) {
        epicHashMap.remove(idEpic);
        for (Subtask subtask: subtaskHashMap.values()) {
            if (subtask.idEpic == idEpic) {
                deleteSubtaskById(subtask.getIdTask());
            }
        }
    }

    public void createTask(Task task) {
        task.setIdTask(hashMapId);
        taskHashMap.put(hashMapId, task);
        hashMapId++;
    }
    public void createSubtask(Subtask subtask) {
        subtask.setIdTask(hashMapId);
        subtaskHashMap.put(hashMapId, subtask);
        hashMapId++;
    }
    public void createEpic(Epic epic) {
        epic.setIdTask(hashMapId);
        epicHashMap.put(hashMapId, epic);
        hashMapId++;
    }

    public ArrayList<Subtask> getAllSubtasksInOneEpic(int idEpic) {
        return epicHashMap.get(idEpic).subtaskInEpicArrayList;
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getIdTask(), task);
    }
    public void updateSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getIdTask(), subtask);
    }
    public void updateEpic(Epic epic, ArrayList<Subtask> subtaskInEpicArrayList) {
        int newSubtasks = 0;
        int doneSubtasks = 0;

        if(!epic.subtaskInEpicArrayList.isEmpty()) {
            for (Subtask subtask : epic.subtaskInEpicArrayList) {
                if (subtask.statusTask.equals(statusName.get(1))) {
                    epic.statusTask = statusName.get(1);
                }

                if (subtask.statusTask.equals(statusName.get(0))) {
                    newSubtasks++;
                }

                if (subtask.statusTask.equals(statusName.get(2))) {
                    doneSubtasks++;
                }
            }

            if (newSubtasks == epic.subtaskInEpicArrayList.size()) {
                epic.statusTask = statusName.get(0);
            } else if (doneSubtasks == epic.subtaskInEpicArrayList.size()) {
                epic.statusTask = statusName.get(2);
            }
        } else {
            epic.subtaskInEpicArrayList = subtaskInEpicArrayList;
            epic.statusTask = statusName.get(0);
        }
        epicHashMap.put(epic.getIdTask(), epic);
    }
}
