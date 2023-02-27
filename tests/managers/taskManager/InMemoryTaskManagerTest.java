package managers.taskManager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTests<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    public void normalTaskHashMap() {
        super.returnHashMaps();
    }

    public void emptyTaskHashMap() {
        super.returnEmptyHashMaps();
    }

    public void creatingTaskEpicSubtask() {
        super.createTaskEpicSubtask();
    }

    public void makeHashMapsEmpty() {
        super.emptyHashMaps();
    }

    public void deletingById() {
        super.deleteById();
    }

    public void gettingById() {
        super.getById();
    }

    public void updatingTasksEpicsSubtasks() {
        super.updateTasksSubtasksEpics();
    }

    public void gettingAllSubtasksForOneEpic() {
        super.getAllSubtasksForOneEpicByIf();
    }
}
