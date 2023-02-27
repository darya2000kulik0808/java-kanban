package managers.taskManager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTests<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }
}
