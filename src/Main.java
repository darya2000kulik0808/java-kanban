import managers.Managers;
import managers.taskManager.TaskManager;
import statusName.StatusName;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Epic> epics = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic1 = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic2 = new ArrayList<>();

        //Проверка создания задач, эпиков и подзадач
        tasks.add(new Task("Моя первая задача", "Описание задачи номер 1", StatusName.NEW));
        tasks.add(new Task("Задача номер 2", "Описание второй задачи", StatusName.NEW));

        epics.add(new Epic("New Task.Epic 1", "Description of epic 1", StatusName.NEW, subtasksEpic1));
        epics.add(new Epic("New epic 2", "Description 2", StatusName.NEW, subtasksEpic2));

        for (Task task : tasks) {
            taskManager.createTask(task);
        }

        for (Epic epic : epics) {
            taskManager.createEpic(epic);
        }

        subtasksEpic1.add(new Subtask("Подзадача один эпика один", "Описание подзадачи",
                StatusName.NEW, epics.get(0).getId()));
        subtasksEpic1.add(new Subtask("Подзадача 2 эпика один", "Описание подзадачи 2",
                StatusName.NEW, epics.get(0).getId()));

        subtasksEpic2.add(new Subtask("Task.Subtask name 2.2", "Description 2.2",
                StatusName.NEW, epics.get(1).getId()));

        for (Subtask subtask : subtasksEpic1) {
            taskManager.createSubtask(subtask);
        }

        for (Subtask subtask : subtasksEpic2) {
            taskManager.createSubtask(subtask);
        }

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        //Проверка сохранения и обновления истории
        System.out.println("\nПроверка истории просмотров \n");
        taskManager.getTaskById(0);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(4);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(0);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getTaskById(1);

        System.out.println(taskManager.getHistoryManager().getHistory());

        taskManager.getTaskById(1);

        System.out.println(taskManager.getHistoryManager().getHistory());

        //Проверка обновления статусов эпиков с изменением статуов подзадач
        System.out.println("\nПроверка обновления статусов эпиков с изменением статуов подзадач\n");
        subtasksEpic1.get(0).setStatus(StatusName.IN_PROGRESS);
        subtasksEpic1.get(1).setStatus(StatusName.DONE);

        subtasksEpic2.get(0).setStatus(StatusName.DONE);

        taskManager.updateSubtask(subtasksEpic1.get(0));
        taskManager.updateSubtask(subtasksEpic1.get(1));

        taskManager.updateEpic(epics.get(0), subtasksEpic1);
        taskManager.updateEpic(epics.get(1), subtasksEpic2);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        //Проверка удаления одного эпика и подзадачи
        System.out.println("\nПроверка обновления статусов эпиков с изменением статуов подзадач и историей\n");
        taskManager.deleteEpicById(3);

        subtasksEpic1.remove(0);
        taskManager.deleteSubtaskById(4);
        taskManager.updateEpic(epics.get(0), subtasksEpic1);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        System.out.println(taskManager.getHistoryManager().getHistory());

        //Проверка удаления из хэш-таблиц
        System.out.println("\nПроверка удаления из хэш-таблиц и истории\n");
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistoryManager().getHistory());
    }
}
