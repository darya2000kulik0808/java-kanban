package task;

import enums.TaskStatus;
import enums.TaskCategory;

import java.time.LocalDateTime;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final TaskCategory taskCategory = TaskCategory.EPIC;

    public Epic(String name, String description,
                TaskStatus status) {

        super(name, description, status);
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

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
