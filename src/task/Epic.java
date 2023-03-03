package task;

import enums.TaskStatus;
import enums.TaskCategory;

public class Epic extends Task {

    public Epic(String name, String description,
                TaskStatus status) {

        super(name, description, status);
        setTaskCategory(TaskCategory.EPIC);
    }

    @Override
    public String toString() {
        String result = "Task.Epic{" +
                "id = " + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}' + "\n";

        return result;
    }
}
