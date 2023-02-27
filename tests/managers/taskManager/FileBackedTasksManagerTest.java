package managers.taskManager;

import enums.TaskStatus;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTests<FileBackedTasksManager> {

    final private String PATH_TO_FILE = "saveFile.csv";

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager();
    }

    @Test
    public void savingAndLoadingToFileWithEmptyHistory() {
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

        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File(PATH_TO_FILE));

        returnHashMaps();
        assertTrue(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToFileWithEpicWithNoSubtasks() {
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

        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File(PATH_TO_FILE));

        assertTrue(fileBackedTasksManagerLoaded.getAllSubtasksInOneEpic(3).isEmpty());
        assertFalse(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToFileWithEmptyTasks() {
        createTasksEpicsSubtasksForManager();
        taskManager.deleteAllTasks();
        taskManager.updateEpic(epicsForTest.get(0));

        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File(PATH_TO_FILE));

        assertTrue(fileBackedTasksManagerLoaded.getAllTasks().isEmpty());
        assertTrue(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenWrongFile() {
        String pathname = "save.csv";
        Executable executable = () -> {
            FileBackedTasksManager.loadFromFile(
                    new File(pathname));
        };

        final ManagerLoadException e = assertThrows(
                ManagerLoadException.class,
                executable
        );

        assertEquals("Не удалось считать файл!", e.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenTryingToSaveFile() {
        File file = new File(PATH_TO_FILE);
        file.setReadOnly();

        Executable executable = () -> taskManager.save();
        final ManagerSaveException e = assertThrows(
                ManagerSaveException.class,
                executable
        );

        assertEquals("Не удалось сделать запись в файл!", e.getMessage());

        file.setWritable(true);
    }
}