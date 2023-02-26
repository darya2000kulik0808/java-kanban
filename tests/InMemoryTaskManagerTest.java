import managers.taskManager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTests<InMemoryTaskManager>{
    InMemoryTaskManager inMemoryTaskManagerForTest;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManagerForTest = new InMemoryTaskManager();
    }

    @Test
    public void normalTaskHashMap(){
        super.returnHashMaps(inMemoryTaskManagerForTest);
    }

    @Test
    public void emptyTaskHashMap(){
        super.returnEmptyHashMaps(inMemoryTaskManagerForTest);
    }
    @Test
    public void creatingTaskEpicSubtask(){
        super.createTaskEpicSubtask(inMemoryTaskManagerForTest);
    }

    @Test
    public void makeHashMapsEmpty(){
        super.emptyHashMaps(inMemoryTaskManagerForTest);
    }

    @Test
    public void deletingById(){
        super.deleteById(inMemoryTaskManagerForTest);
    }

    @Test
    public void gettingById(){
        super.getById(inMemoryTaskManagerForTest);
    }

    @Test
    public void updatingTasksEpicsSubtasks(){
        super.updateTasksSubtasksEpics(inMemoryTaskManagerForTest);
    }

    @Test
    public void gettingAllSubtasksForOneEpic(){
        super.getAllSubtasksForOneEpicByIf(inMemoryTaskManagerForTest);
    }
}
