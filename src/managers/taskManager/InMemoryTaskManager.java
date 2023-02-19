package managers.taskManager;

import managers.Managers;
import managers.historyManager.HistoryManager;
import enums.TaskStatus;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int hashMapId;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        setHashMapId();
    }

    private void setHashMapId(){
        if (tasks.isEmpty() && subtasks.isEmpty() && epics.isEmpty()){
            hashMapId = 0;
        } else {
            hashMapId = 0;
            if(!tasks.isEmpty()){
                for(Task task: tasks.values()){
                    if (hashMapId < task.getId()){
                        hashMapId = task.getId() + 1;
                    }
                }
            }

            if(!subtasks.isEmpty()){
                for(Subtask subtask: subtasks.values()){
                    if (hashMapId < subtask.getId()){
                        hashMapId = subtask.getId() + 1;
                    }
                }
            }

            if(!epics.isEmpty()){
                for(Epic epic: epics.values()){
                    if (hashMapId < epic.getId()){
                        hashMapId = epic.getId() + 1;
                    }
                }
            }
        }
    }

    @Override
    public HistoryManager getHistoryManager() {
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
        for(Integer idTask: tasks.keySet()){
              historyManager.remove(idTask);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for(Integer idTSubtask: subtasks.keySet()){
            historyManager.remove(idTSubtask);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for(Integer idEpic: epics.keySet()){
            historyManager.remove(idEpic);
        }
        epics.clear();
    }

    @Override
    public void deleteTaskById(int idTask) {
        historyManager.remove(idTask);
        tasks.remove(idTask);
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        historyManager.remove(idSubtask);
        subtasks.remove(idSubtask);
    }

    @Override
    public void deleteEpicById(int idEpic) {
        historyManager.remove(idEpic);
        epics.remove(idEpic);
        for (Subtask subtask : subtasks.values()) {
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
        ArrayList<Subtask> subtasksForEpic = new ArrayList<>();

        for(Subtask subtask: subtasks.values()){
            if(subtask.getIdEpic() == idEpic){
                subtasksForEpic.add(subtask);
            }
        }

        return subtasksForEpic;
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
    public void updateEpic(Epic epic) {
        int newSubtasks = 0;
        int doneSubtasks = 0;

        if (!getAllSubtasksInOneEpic(epic.getId()).isEmpty()) {
            for (Subtask subtask : getAllSubtasksInOneEpic(epic.getId())) {
                if (subtask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                }

                if (subtask.getStatus().equals(TaskStatus.NEW)) {
                    newSubtasks++;
                }

                if (subtask.getStatus().equals(TaskStatus.DONE)) {
                    doneSubtasks++;
                }
            }

            if (newSubtasks == getAllSubtasksInOneEpic(epic.getId()).size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (doneSubtasks == getAllSubtasksInOneEpic(epic.getId()).size()) {
                epic.setStatus(TaskStatus.DONE);
            }
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
        epics.put(epic.getId(), epic);
    }
}
