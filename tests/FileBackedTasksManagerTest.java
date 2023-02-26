import enums.TaskStatus;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import managers.taskManager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTests<FileBackedTasksManager>{
    FileBackedTasksManager fileBackedTasksManagerForTest;

    @BeforeEach
    public void beforeEach() {
        fileBackedTasksManagerForTest = new FileBackedTasksManager();
    }

    @Test
    public void normalTaskHashMap(){
        super.returnHashMaps(fileBackedTasksManagerForTest);
    }

    @Test
    public void emptyTaskHashMap(){
        super.returnEmptyHashMaps(fileBackedTasksManagerForTest);
    }

    @Test
    public void creatingTaskEpicSubtask(){
        super.createTaskEpicSubtask(fileBackedTasksManagerForTest);
    }

    @Test
    public void makeHashMapsEmpty(){
        super.emptyHashMaps(fileBackedTasksManagerForTest);
    }

    @Test
    public void deletingById(){
        super.deleteById(fileBackedTasksManagerForTest);
    }

    @Test
    public void gettingById(){
        super.getById(fileBackedTasksManagerForTest);
    }

    @Test
    public void updatingTasksEpicsSubtasks(){
        super.updateTasksSubtasksEpics(fileBackedTasksManagerForTest);
    }

    @Test
    public void gettingAllSubtasksForOneEpic() {
        super.getAllSubtasksForOneEpicByIf(fileBackedTasksManagerForTest);
    }

    @Test
    public void savingAndLoadingToFileWithEmptyHistory(){
        createTasksEpicsSubtasksForManager(fileBackedTasksManagerForTest);

        tasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        tasksForTest.get(1).setName("Meow");
        subtasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        subtasksForTest.get(1).setStatus(TaskStatus.DONE);

        fileBackedTasksManagerForTest.updateTask(tasksForTest.get(0));
        fileBackedTasksManagerForTest.updateTask(tasksForTest.get(1));
        fileBackedTasksManagerForTest.updateSubtask(subtasksForTest.get(0));
        fileBackedTasksManagerForTest.updateSubtask(subtasksForTest.get(1));
        fileBackedTasksManagerForTest.updateEpic(epicsForTest.get(0));

        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\saveFile.csv"));

        returnHashMaps(fileBackedTasksManagerLoaded);
        assertTrue(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToFileWithEpicWithNoSubtasks(){
        createTasksEpicsSubtasksForManager(fileBackedTasksManagerForTest);

        tasksForTest.get(0).setStatus(TaskStatus.IN_PROGRESS);
        tasksForTest.get(1).setName("Meow");

        fileBackedTasksManagerForTest.deleteSubtaskById(6);
        fileBackedTasksManagerForTest.deleteSubtaskById(7);

        fileBackedTasksManagerForTest.getTaskById(0);
        fileBackedTasksManagerForTest.getEpicById(3);
        fileBackedTasksManagerForTest.getTaskById(1);

        fileBackedTasksManagerForTest.updateTask(tasksForTest.get(0));
        fileBackedTasksManagerForTest.updateTask(tasksForTest.get(1));
        fileBackedTasksManagerForTest.updateEpic(epicsForTest.get(0));


        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\saveFile.csv"));

        assertTrue(fileBackedTasksManagerLoaded.getAllSubtasksInOneEpic(3).isEmpty());
        assertFalse(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void savingAndLoadingToFileWithEmptyTasks(){
        createTasksEpicsSubtasksForManager(fileBackedTasksManagerForTest);

        fileBackedTasksManagerForTest.deleteAllTasks();

        fileBackedTasksManagerForTest.updateEpic(epicsForTest.get(0));

        FileBackedTasksManager fileBackedTasksManagerLoaded =
                FileBackedTasksManager.loadFromFile(
                        new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\saveFile.csv"));

        assertTrue(fileBackedTasksManagerLoaded.getAllTasks().isEmpty());
        assertTrue(fileBackedTasksManagerLoaded.getHistoryManager().getHistory().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenWrongFile(){
        final ManagerLoadException e = assertThrows(
                ManagerLoadException.class,
                () -> FileBackedTasksManager.loadFromFile(
                        new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\save.csv"))
        );

        assertEquals("Не удалось считать файл!", e.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenTryingToSaveFile(){
        File file = new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\saveFile.csv");
        file.setReadOnly();

        final ManagerSaveException e = assertThrows(
                ManagerSaveException.class,
                () -> fileBackedTasksManagerForTest.save()
        );

        assertEquals("Не удалось сделать запись в файл!", e.getMessage());
        file.setWritable(true);
    }
}