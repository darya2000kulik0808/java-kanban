import task.Task;
import task.Epic;
import task.Subtask;

import taskManager.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        ArrayList<String> statusName = new ArrayList<>();

        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Epic> epics = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic1 = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic2 = new ArrayList<>();

        statusName.add("NEW"); //0
        statusName.add("IN_PROGRESS"); //1
        statusName.add("DONE"); //2

        //Проверка создания задач, эпиков и подзадач
        tasks.add(new Task("Моя первая задача", "Описание задачи номер 1", statusName.get(0)));
        tasks.add(new Task("Задача номер 2", "Описание второй задачи", statusName.get(0)));

        epics.add(new Epic("New Task.Epic 1", "Description of epic 1", statusName.get(0), subtasksEpic1));
        epics.add(new Epic("New epic 2", "Description 2", statusName.get(0), subtasksEpic2));

        for(Task task: tasks) {
            taskManager.createTask(task);
        }

        for(Epic epic: epics) {
            taskManager.createEpic(epic);
        }

        subtasksEpic1.add(new Subtask("Подзадача один эпика один", "Описание подзадачи",
                statusName.get(0), epics.get(0).getId()));
        subtasksEpic1.add(new Subtask("Подзадача 2 эпика один", "Описание подзадачи 2",
                statusName.get(0), epics.get(0).getId()));

        subtasksEpic2.add(new Subtask("Task.Subtask name 2.2", "Description 2.2",
                statusName.get(0), epics.get(1).getId()));

        for (Subtask subtask: subtasksEpic1) {
            taskManager.createSubtask(subtask);
        }

        for (Subtask subtask: subtasksEpic2) {
            taskManager.createSubtask(subtask);
        }

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        //Проверка обновления статусов эпиков с изменением статуов подзадач
        System.out.println("\n");
        subtasksEpic1.get(0).setStatus(statusName.get(1));
        subtasksEpic1.get(1).setStatus(statusName.get(2));

        subtasksEpic2.get(0).setStatus(statusName.get(2));

        taskManager.updateSubtask(subtasksEpic1.get(0));
        taskManager.updateSubtask(subtasksEpic1.get(1));

        taskManager.updateEpic(epics.get(0), subtasksEpic1);
        taskManager.updateEpic(epics.get(1), subtasksEpic2);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        //Проверка удаления одного эпика и подзадачи
        System.out.println("\n");
        taskManager.deleteEpicById(3);

        subtasksEpic1.remove(0);
        taskManager.deleteSubtaskById(4);
        taskManager.updateEpic(epics.get(0), subtasksEpic1);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        //Проверка удаления из хэш-таблиц
        System.out.println("\n");
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

    }
}
