package managers.taskManager;

import enums.TaskStatus;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTests<T extends TaskManager> {
    ArrayList<Task> tasksForTest;
    ArrayList<Subtask> subtasksForTest;
    ArrayList<Epic> epicsForTest;
    T taskManager;

    public void createTasksEpicsSubtasksForManager() {
        createTasksForTest();
        createEpicsForTest();

        for (Task task : tasksForTest) {
            taskManager.createTask(task);
        }

        for (Epic epic : epicsForTest) {
            taskManager.createEpic(epic);
        }

        createSubtasksForTest();

        for (Subtask subtask : subtasksForTest) {
            taskManager.createSubtask(subtask);
        }
    }

    @Test
    public void createTaskEpicSubtask() {
        createTasksEpicsSubtasksForManager();
        assertNotNull(taskManager.getTaskById(0));
        assertNotNull(taskManager.getEpicById(4));
        assertNotNull(taskManager.getSubtaskById(10));
    }

    @Test
    public void returnHashMaps() {
        createTasksEpicsSubtasksForManager();
        assertNotNull(taskManager.getAllEpics());
        assertNotNull(taskManager.getAllTasks());
        assertNotNull(taskManager.getAllSubtasks());
    }

    @Test
    public void returnEmptyHashMaps() {
        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    public void emptyHashMaps() {
        createTasksEpicsSubtasksForManager();

        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();

        returnEmptyHashMaps();
    }

    @Test
    public void deleteById() {
        createTasksEpicsSubtasksForManager();
        Task task = taskManager.getTaskById(0);
        Epic epic = taskManager.getEpicById(4);
        Subtask subtask1 = taskManager.getSubtaskById(8);
        Subtask subtask2 = taskManager.getSubtaskById(9);

        taskManager.deleteTaskById(0);
        taskManager.deleteEpicById(4);

        assertFalse(taskManager.getAllTasks().containsValue(task));
        assertFalse(taskManager.getAllEpics().containsValue(epic));
        assertFalse(taskManager.getAllSubtasks().containsValue(subtask1));
        assertFalse(taskManager.getAllSubtasks().containsValue(subtask2));
    }

    @Test
    public void getById() {
        createTasksEpicsSubtasksForManager();

        assertNotNull(taskManager.getTaskById(1));
        assertNotNull(taskManager.getEpicById(5));
        assertNotNull(taskManager.getSubtaskById(9));
        assertEquals(tasksForTest.get(1), taskManager.getTaskById(1));
        assertEquals(epicsForTest.get(2), taskManager.getEpicById(5));
        assertEquals(subtasksForTest.get(3), taskManager.getSubtaskById(9));
    }

    @Test
    public void updateTasksSubtasksEpics() {
        createTasksEpicsSubtasksForManager();
        tasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        tasksForTest.get(1).setName("Meow");
        subtasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        subtasksForTest.get(1).setStatus(TaskStatus.DONE);
        taskManager.updateTask(tasksForTest.get(0));
        taskManager.updateTask(tasksForTest.get(1));
        taskManager.updateSubtask(subtasksForTest.get(0));
        taskManager.updateSubtask(subtasksForTest.get(1));
        taskManager.updateEpic(epicsForTest.get(0));

        assertEquals(tasksForTest.get(0), taskManager.getTaskById(0));
        assertEquals(tasksForTest.get(1), taskManager.getTaskById(1));
        assertEquals(subtasksForTest.get(0), taskManager.getSubtaskById(6));
        assertEquals(subtasksForTest.get(1), taskManager.getSubtaskById(7));
        assertEquals(epicsForTest.get(0), taskManager.getEpicById(3));
    }

    @Test
    public void getAllSubtasksForOneEpicByIf() {
        createTasksEpicsSubtasksForManager();
        ArrayList<Subtask> subtasksForOneEpic = new ArrayList<>();
        subtasksForOneEpic.add(subtasksForTest.get(0));
        subtasksForOneEpic.add(subtasksForTest.get(1));

        assertNotNull(taskManager.getAllSubtasksInOneEpic(3));
        assertArrayEquals(subtasksForOneEpic.toArray(), taskManager.getAllSubtasksInOneEpic(3).toArray());
    }

    private void createTasksForTest() {
        tasksForTest = new ArrayList<>();
        tasksForTest.add(new Task("name1", "description1",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T14:30:00"), 10));//0
        tasksForTest.add(new Task("name2", "description2",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T14:41:00"), 20));//1
        tasksForTest.add(new Task("name3", "description3",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T15:01:00"), 25));//2
    }

    private void createEpicsForTest() {
        epicsForTest = new ArrayList<>();
        epicsForTest.add(new Epic("epicName1", "epicDescription1", TaskStatus.NEW));//3
        epicsForTest.add(new Epic("epicName2", "epicDescription2", TaskStatus.NEW));//4
        epicsForTest.add(new Epic("epicName3", "epicDescription3", TaskStatus.NEW));//5
    }

    private void createSubtasksForTest() {
        subtasksForTest = new ArrayList<>();
        subtasksForTest.add(
                new Subtask("subtaskName1", "subtaskDescription1", TaskStatus.NEW,
                        LocalDateTime.parse("2023-02-26T15:26:00"), 5, epicsForTest.get(0).getId()));//6
        subtasksForTest.add(
                new Subtask("subtaskName2", "subtaskDescription2", TaskStatus.NEW,
                        LocalDateTime.parse("2023-02-26T15:31:00"), 5, epicsForTest.get(0).getId()));//7
        subtasksForTest.add(
                new Subtask("subtaskName3", "subtaskDescription3", TaskStatus.NEW,
                        LocalDateTime.parse("2023-02-26T15:36:00"), 25, epicsForTest.get(1).getId()));//8
        subtasksForTest.add(
                new Subtask("subtaskName4", "subtaskDescription4", TaskStatus.NEW,
                        LocalDateTime.parse("2023-02-26T16:01:00"), 15, epicsForTest.get(1).getId()));//9
        subtasksForTest.add(
                new Subtask("subtaskName5", "subtaskDescription5", TaskStatus.NEW,
                        LocalDateTime.parse("2023-02-26T16:16:00"), 10, epicsForTest.get(2).getId()));//10
    }
}

