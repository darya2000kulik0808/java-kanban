package managers.taskManager;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import managers.historyManager.HistoryManager;
import enums.TaskStatus;
import enums.TaskCategory;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager();

        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Epic> epics = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic1 = new ArrayList<>();
        ArrayList<Subtask> subtasksEpic2 = new ArrayList<>();

        //Проверка создания задач, эпиков и подзадач
        tasks.add(new Task("Моя первая задача", "Описание задачи номер 1", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T00:00:01"), 80));
        tasks.add(new Task("Задача номер 2", "Описание второй задачи", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T00:01:01"), 60));

        epics.add(new Epic("New Task.Epic 1", "Description of epic 1", TaskStatus.NEW));
        epics.add(new Epic("New epic 2", "Description 2", TaskStatus.NEW));


        for (Task task : tasks) {
            fileBackedTasksManager1.createTask(task);
        }

        for (Epic epic : epics) {
            fileBackedTasksManager1.createEpic(epic);
        }

        subtasksEpic1.add(new Subtask("Подзадача один эпика один", "Описание подзадачи",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T14:00:00"), 30, epics.get(0).getId()));
        subtasksEpic1.add(new Subtask("Подзадача 2 эпика один", "Описание подзадачи 2",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T14:10:00"), 10, epics.get(0).getId()));

        subtasksEpic2.add(new Subtask("Task.Subtask name 2.2", "Description 2.2",
                TaskStatus.NEW, LocalDateTime.parse("2023-02-26T14:15:00"), 5, epics.get(1).getId()));

        for (Subtask subtask : subtasksEpic1) {
            fileBackedTasksManager1.createSubtask(subtask);
        }

        for (Subtask subtask : subtasksEpic2) {
            fileBackedTasksManager1.createSubtask(subtask);
        }

        fileBackedTasksManager1.getTaskById(0);
        fileBackedTasksManager1.getEpicById(2);
        fileBackedTasksManager1.getSubtaskById(4);
        fileBackedTasksManager1.getTaskById(1);
        fileBackedTasksManager1.getEpicById(2);
        fileBackedTasksManager1.getEpicById(3);
        fileBackedTasksManager1.getTaskById(0);
        fileBackedTasksManager1.getSubtaskById(5);
        fileBackedTasksManager1.getSubtaskById(6);
        fileBackedTasksManager1.getTaskById(1);

        subtasksEpic1.get(0).setStatus(TaskStatus.IN_PROGRESS);
        subtasksEpic1.get(1).setStatus(TaskStatus.DONE);

        subtasksEpic2.get(0).setStatus(TaskStatus.DONE);

        fileBackedTasksManager1.updateSubtask(subtasksEpic1.get(0));
        fileBackedTasksManager1.updateSubtask(subtasksEpic1.get(1));

        fileBackedTasksManager1.updateEpic(epics.get(0));
        fileBackedTasksManager1.updateEpic(epics.get(1));


        System.out.println(fileBackedTasksManager1.getAllTasks());
        System.out.println(fileBackedTasksManager1.getAllEpics());
        System.out.println(fileBackedTasksManager1.getAllSubtasks());
        System.out.println(fileBackedTasksManager1.getHistoryManager().getHistory());

        System.out.println("\n");

        FileBackedTasksManager fileBackedTasksManager =
                FileBackedTasksManager.loadFromFile(
                        new File("C:\\Users\\Lenovo\\IdeaProjects\\java-kanban\\saveFile.csv"));

        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println(fileBackedTasksManager.getAllEpics());
        System.out.println(fileBackedTasksManager.getAllSubtasks());
        System.out.println(fileBackedTasksManager.getHistoryManager().getHistory());
    }

    public FileBackedTasksManager(){
        super();
    }

    public static FileBackedTasksManager loadFromFile(File file){
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getName()))){
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
            ArrayList<String> list = new ArrayList<>();

            while(fileReader.ready()){
                list.add(fileReader.readLine());
            }

            list.remove(0);//Удаление заголовка
            List<Integer> idsHistory = new ArrayList<>();

            if(!list.get(list.size()-1).isEmpty()) {
                idsHistory = historyFromString(list.get(list.size() - 1));
                list.remove(list.size()-1); //Удаление истории из списка
            }

            list.remove(list.size()-1); //удаление пустой строки

            for(String string: list){
                if(fileBackedTasksManager.fromString(string) instanceof Subtask){
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createSubtask(((Subtask) fileBackedTasksManager.fromString(string)));

                } else if(fileBackedTasksManager.fromString(string) instanceof Epic){
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createEpic ((Epic) fileBackedTasksManager.fromString(string));

                } else if(fileBackedTasksManager.fromString(string) instanceof Task){
                    fileBackedTasksManager.checkHashMapId(fileBackedTasksManager.fromString(string));
                    fileBackedTasksManager.createTask(fileBackedTasksManager.fromString(string));
                }
            }

            if(!idsHistory.isEmpty()) {
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
            throw  new ManagerLoadException("Не удалось считать файл!");
        }
    }

    public void checkHashMapId(Task task){
        if((this.getHashMapId() == 0) && (task.getId() > this.getHashMapId())){
            this.setHashMapId(task.getId());
        }
    }


    public void save() {
        try (Writer fileWriter = new FileWriter("saveFile.csv")){
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");

            for(Task task: this.getAllTasks().values()){
                fileWriter.write(toString(task) + "\n");
            }

            for(Epic epic: this.getAllEpics().values()){
                fileWriter.write(toString(epic) + "\n");
            }

            for(Subtask subtask: this.getAllSubtasks().values()){
                fileWriter.write(toString(subtask) + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e){
            throw new ManagerSaveException("Не удалось сделать запись в файл!");
        }
    }

    public String toString(Task task){
        String taskToString = "";
        switch (task.getTaskCategory()){
            case TASK:
                taskToString = task.getId() + "," + task.getTaskCategory() + "," +  task.getName() + "," +
                    task.getStatus().toString() + "," +  task.getDescription() + "," +  task.getStartTime()
                        + "," +  task.getDuration();
                break;
            case EPIC:
                Epic epic = (Epic) task;
                taskToString = epic.getId() + "," +  epic.getTaskCategory() + "," +  epic.getName() + "," +
                    epic.getStatus().toString() + "," +  epic.getDescription() + "," +  epic.getStartTime()
                        + "," +  epic.getDuration();
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                taskToString = subtask.getId() + "," +  subtask.getTaskCategory() + "," +  subtask.getName() + "," +
                    subtask.getStatus().toString() + "," +  subtask.getDescription() + ","
                        +  subtask.getStartTime() + "," +  subtask.getDuration() + "," +  subtask.getIdEpic();
        }

       return  taskToString;
    }

    static String historyToString(HistoryManager manager){
       String output;
       ArrayList<String> id = new ArrayList<>();
       for(Task task: manager.getHistory()){
            id.add(String.valueOf(task.getId()));
       }
       output = String.join(",", id);
       return output;
    }

    public Task fromString(String value){
        String[] fromString = value.split(",");
        switch (TaskCategory.valueOf(fromString[1])){
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

    static List<Integer> historyFromString(String value){
        String[] fromString = value.split(",");
        List<Integer> ids = new ArrayList<>();
        for(String id: fromString){
            ids.add(Integer.parseInt(id));
        }
        return  ids;
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
