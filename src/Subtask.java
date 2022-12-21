public class Subtask extends Task{
    int idEpic;

    public Subtask(String nameTask, String descriptionTask, String statusTask, int idEpic) {
        super(nameTask, descriptionTask, statusTask);
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}' + "\n";
    }
}
