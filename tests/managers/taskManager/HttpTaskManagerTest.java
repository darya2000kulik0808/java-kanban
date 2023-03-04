package managers.taskManager;

import enums.TaskStatus;
import exceptions.kvTaskClientExceptions.KVTaskClientGetApiTokenException;
import kvServer.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTests<HttpTaskManager> {
    KVServer kvServer;
    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager("http://localhost:8078");

    }

    @AfterEach
    public void afterEach(){
        kvServer.stop(0);
    }

    @Test
    public void savingAndLoadingToServerWithEmptyHistory() {
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

        HttpTaskManager httpTaskManager =
                new HttpTaskManager("http://localhost:8078").loadFromServer("http://localhost:8078");

        returnHashMaps();
        assertTrue(httpTaskManager.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToServerWithEpicWithNoSubtasks() {
        createTasksEpicsSubtasksForManager();
        tasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        tasksForTest.get(1).setName("Meow");
        taskManager.deleteSubtaskById(6);
        taskManager.deleteSubtaskById(7);
        taskManager.getTaskById(0);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.updateTask(tasksForTest.get(0));
        taskManager.updateTask(tasksForTest.get(1));
        taskManager.updateEpic(epicsForTest.get(0));

        HttpTaskManager httpTaskManager =
                new HttpTaskManager("http://localhost:8078").loadFromServer("http://localhost:8078");

        assertTrue(httpTaskManager.getAllSubtasksInOneEpic(3).isEmpty());
        assertFalse(httpTaskManager.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToServerWithEmptyTasks() {
        createTasksEpicsSubtasksForManager();
        taskManager.deleteAllTasks();
        taskManager.updateEpic(epicsForTest.get(0));

        HttpTaskManager httpTaskManager =
                new HttpTaskManager("http://localhost:8078").loadFromServer("http://localhost:8078");

        assertTrue(httpTaskManager.getAllTasks().isEmpty());
        assertTrue(httpTaskManager.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenWrongAddress() {
        Executable executable = () -> {
            new HttpTaskManager("http://localhost:8079").loadFromServer("http://localhost:8079");
        };

        final KVTaskClientGetApiTokenException exception = assertThrows(
                KVTaskClientGetApiTokenException.class,
                executable
        );

        assertEquals("Не удалось получить ApiToken. Не удалось послать запрос" +
                " и получить корректный ответ.", exception.getMessage());

    }
}
