package managers;

import managers.historyManager.HistoryManager;
import managers.historyManager.InMemoryHistoryManager;
import managers.taskManager.*;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
