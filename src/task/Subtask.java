package task;

import enums.TaskStatus;
import enums.TaskCategory;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String description, TaskStatus status, LocalDateTime startTime, long duration, int idEpic) {
        super(name, description, status, startTime,  duration);
        this.idEpic = idEpic;
        setTaskCategory(TaskCategory.SUBTASK);
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
                "id=" + getId() +
                ", idEpic=" + idEpic +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                '}' + "\n";
    }

    public int getIdEpic() {
        return idEpic;
    }
}
