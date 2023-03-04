package managers.taskManager;

import enums.TaskCategory;
import enums.TaskStatus;
import exceptions.managerExceptions.ManagerLoadException;
import exceptions.managerExceptions.ManagerSaveException;
import managers.historyManager.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static String path;

    public FileBackedTasksManager() {
        super();
    }

    public FileBackedTasksManager(String path) {
        super();
        FileBackedTasksManager.path = path;
    }

    public static FileBackedTasksManager loadFromFile() {
        File file = new File(path);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getName()))) {
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
            ArrayList<String> list = new ArrayList<>();

            while (fileReader.ready()) {
                list.add(fileReader.readLine());
            }

            list.remove(0);//Удаление заголовка
            List<Integer> idsHistory = new ArrayList<>();

            if (!list.get(list.size() - 1).isEmpty()) {
                idsHistory = historyFromString(list.get(list.size() - 1));
                list.remove(list.size() - 1); //Удаление истории из списка
            }

            list.remove(list.size() - 1); //удаление пустой строки

            for (String string : list) {
                if (fileBackedTasksManager.fromString(string) instanceof Subtask) {
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createSubtask(((Subtask) fileBackedTasksManager.fromString(string)));

                } else if (fileBackedTasksManager.fromString(string) instanceof Epic) {
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createEpic((Epic) fileBackedTasksManager.fromString(string));

                } else if (fileBackedTasksManager.fromString(string) instanceof Task) {
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createTask(fileBackedTasksManager.fromString(string));
                }
            }

            if (!idsHistory.isEmpty()) {
                for (Integer id : idsHistory) {
                    if (fileBackedTasksManager.getAllTasks().containsKey(id)) {
                        fileBackedTasksManager.getHistoryManager()
                                .add(fileBackedTasksManager.getTaskById(id));
                    } else if (fileBackedTasksManager.getAllSubtasks().containsKey(id)) {
                        fileBackedTasksManager.getHistoryManager()
                                .add(fileBackedTasksManager.getSubtaskById(id));
                    } else if (fileBackedTasksManager.getAllEpics().containsKey(id)) {
                        fileBackedTasksManager.getHistoryManager()
                                .add(fileBackedTasksManager.getEpicById(id));
                    }
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerLoadException("Не удалось считать файл!");
        }
    }

    public void checkHashMapId(Task task) {
        if ((getHashMapId() == 0) && (task.getId() > getHashMapId())) {
            setHashMapId(task.getId());
        }
    }


    public void save() {
        String pathToFile = "saveFile.csv";
        try (Writer fileWriter = new FileWriter(pathToFile)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");

            for (Task task : this.getAllTasks().values()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic : this.getAllEpics().values()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (Subtask subtask : this.getAllSubtasks().values()) {
                fileWriter.write(toString(subtask) + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сделать запись в файл!");
        }
    }

    public String toString(Task task) {
        String taskToString = "";
        switch (task.getTaskCategory()) {
            case TASK:
                taskToString = task.getId() + "," + task.getTaskCategory() + "," + task.getName() + "," +
                        task.getStatus().toString() + "," + task.getDescription() + "," + task.getStartTime()
                        + "," + task.getDuration();
                break;
            case EPIC:
                Epic epic = (Epic) task;
                taskToString = epic.getId() + "," + epic.getTaskCategory() + "," + epic.getName() + "," +
                        epic.getStatus().toString() + "," + epic.getDescription() + "," + epic.getStartTime()
                        + "," + epic.getDuration();
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                taskToString = subtask.getId() + "," + subtask.getTaskCategory() + "," + subtask.getName() + "," +
                        subtask.getStatus().toString() + "," + subtask.getDescription() + ","
                        + subtask.getStartTime() + "," + subtask.getDuration() + "," + subtask.getIdEpic();
        }

        return taskToString;
    }

    static String historyToString(HistoryManager manager) {
        String output;
        ArrayList<String> id = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            id.add(String.valueOf(task.getId()));
        }
        output = String.join(",", id);
        return output;
    }

    public Task fromString(String value) {
        String[] fromString = value.split(",");
        switch (TaskCategory.valueOf(fromString[1])) {
            case TASK:
                Task task = new Task(fromString[2], fromString[4], TaskStatus.valueOf(fromString[3]),
                        LocalDateTime.parse(fromString[5]), Long.parseLong(fromString[6]));
                task.setId(Integer.parseInt(fromString[0]));
                return task;
            case SUBTASK:
                Subtask subtask = new Subtask(fromString[2], fromString[4], TaskStatus.valueOf(fromString[3]),
                        LocalDateTime.parse(fromString[5]), Long.parseLong(fromString[6]), Integer.parseInt(fromString[7]));
                subtask.setId(Integer.parseInt(fromString[0]));
                return subtask;
            case EPIC:
                Epic epic = new Epic(fromString[2], fromString[4], TaskStatus.valueOf(fromString[3]));
                epic.setId(Integer.parseInt(fromString[0]));
                return epic;
            default:
                return new Task("error", "error", TaskStatus.NEW,
                        LocalDateTime.parse("2000-01-01T00:00:00"), 0);
        }
    }

    static List<Integer> historyFromString(String value) {
        String[] fromString = value.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String id : fromString) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }
}
