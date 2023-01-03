package managers.historyManager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> taskHistory;

    public InMemoryHistoryManager(){
        this.taskHistory = new ArrayList<>();
    }

    public void add(Task task){

        if((taskHistory.size() < 10) || taskHistory.isEmpty()) {
            taskHistory.add(task);
        } else if(taskHistory.size() == 10) {
            taskHistory.remove(0);
            taskHistory.add(task);
        }

    }
    public List<Task> getHistory(){
        return taskHistory;
    }
}
