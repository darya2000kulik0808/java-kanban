package task;

import statusName.StatusName;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String name, String description, StatusName status, int idEpic) {
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
}
