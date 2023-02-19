package task;

import statusName.StatusName;
import taskCategory.TaskCategory;

public class Task {

    private final TaskCategory taskCategory = TaskCategory.TASK;
    private int id;
    private String name;
    private String description;
    private StatusName status;

    public Task(String name, String description, StatusName status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StatusName getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(StatusName status) {
        this.status = status;
    }

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }
}
