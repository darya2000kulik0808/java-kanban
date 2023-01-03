package managers.taskManager;

import managers.Managers;
import managers.historyManager.HistoryManager;

import statusName.StatusName;

import task.Task;
import task.Epic;
import task.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager{

    private HistoryManager historyManager;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    int hashMapId;

    public InMemoryTaskManager() {

        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();

        hashMapId = 0;

    }

    @Override
    public HistoryManager getHistoryManager(){
        return historyManager;
    }
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }
    @Override
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }
    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public Task getTaskById(int idTask) {
        historyManager.add(tasks.get(idTask));
        return tasks.get(idTask);
    }
    @Override
    public Subtask getSubtaskById(int idSubtask) {
        historyManager.add(subtasks.get(idSubtask));
        return subtasks.get(idSubtask);
    }
    @Override
    public Epic getEpicById(int idEpic) {
        historyManager.add(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }
    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void  deleteTaskById(int idTask) {
        tasks.remove(idTask);
    }
    @Override
    public void  deleteSubtaskById(int idSubtask) {
        subtasks.remove(idSubtask);
    }
    @Override
    public void  deleteEpicById(int idEpic) {
        epics.remove(idEpic);
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getIdEpic() == idEpic) {
                deleteSubtaskById(subtask.getId());
            }
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(hashMapId);
        tasks.put(hashMapId, task);
        hashMapId++;
    }
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(hashMapId);
        subtasks.put(hashMapId, subtask);
        hashMapId++;
    }
    @Override
    public void createEpic(Epic epic) {
        epic.setId(hashMapId);
        epics.put(hashMapId, epic);
        hashMapId++;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksInOneEpic(int idEpic) {
        return epics.get(idEpic).getSubtaskInEpics();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
    @Override
    public void updateEpic(Epic epic, ArrayList<Subtask> subtaskInEpicArrayList) {
        int newSubtasks = 0;
        int doneSubtasks = 0;

        if(!epic.getSubtaskInEpics().isEmpty()) {
            for (Subtask subtask : epic.getSubtaskInEpics()) {
                if (subtask.getStatus().equals(StatusName.IN_PROGRESS)) {
                    epic.setStatus(StatusName.IN_PROGRESS);
                }

                if (subtask.getStatus().equals(StatusName.NEW)) {
                    newSubtasks++;
                }

                if (subtask.getStatus().equals(StatusName.DONE)){
                    doneSubtasks++;
                }
            }

            if (newSubtasks == epic.getSubtaskInEpics().size()) {
                epic.setStatus(StatusName.NEW);
            } else if (doneSubtasks == epic.getSubtaskInEpics().size()) {
                epic.setStatus(StatusName.DONE);
            }
        } else {
            epic.setSubtaskInEpics(subtaskInEpicArrayList);
            epic.setStatus(StatusName.NEW);
        }
        epics.put(epic.getId(), epic);
    }
}
