package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskInEpics;

    public Epic(String name, String description,
                String status, ArrayList<Subtask> subtaskInEpic) {

        super(name, description, status);
        this.subtaskInEpics = subtaskInEpic;
    }

    @Override
    public String toString() {
        String result = "Task.Epic{" +
                "subtaskInEpic=";

        for (Subtask subtask: subtaskInEpics) {
            result = result + subtask.toString();
        }

        result = result + ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}' + "\n";

        return result;
    }

    public ArrayList<Subtask> getSubtaskInEpics() {
        return subtaskInEpics;
    }

    public void setSubtaskInEpics(ArrayList<Subtask> subtaskInEpics) {
        this.subtaskInEpics = subtaskInEpics;
    }
}
