package task;

import enums.TaskStatus;
import enums.TaskCategory;

public class Subtask extends Task {
    private int idEpic;
    private final TaskCategory taskCategory = TaskCategory.SUBTASK;

    public Subtask(String name, String description, TaskStatus status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
                "id=" + getId() +
                ", idEpic=" + idEpic +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}' + "\n";
    }

    public int getIdEpic() {
        return idEpic;
    }

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }
}
