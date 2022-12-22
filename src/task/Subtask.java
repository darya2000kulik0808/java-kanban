package task;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String name, String description, String status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
                "idEpic=" + idEpic +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}' + "\n";
    }

    public int getIdEpic() {
        return idEpic;
    }
}
