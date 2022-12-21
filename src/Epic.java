import java.util.ArrayList;

public class Epic extends Task{

    ArrayList<Subtask> subtaskInEpicArrayList;

    public Epic(String nameTask, String descriptionTask,
                String statusTask, ArrayList<Subtask> subtaskInEpicArrayList) {

        super(nameTask, descriptionTask, statusTask);
        this.subtaskInEpicArrayList = subtaskInEpicArrayList;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "subtaskInEpicArrayList=";

        for (Subtask subtask: subtaskInEpicArrayList) {
            result = result + subtask.toString();
        }

        result = result + ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}' + "\n";

        return result;
    }
}
