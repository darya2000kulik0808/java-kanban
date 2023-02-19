package task;

import statusName.StatusName;
import taskCategory.TaskCategory;

import java.util.ArrayList;

public class Epic extends Task {

    private final TaskCategory taskCategory = TaskCategory.EPIC;

    public Epic(String name, String description,
                StatusName status) {

        super(name, description, status);
    }

    @Override
    public String toString() {
        String result = "Task.Epic{" +
                "id = " + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}' + "\n";

        return result;
    }

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }
}
