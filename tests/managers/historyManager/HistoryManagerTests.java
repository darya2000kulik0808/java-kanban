package managers.historyManager;

import enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryManagerTests {

    private HistoryManager historyManagerForTest;
    private ArrayList<Task> tasksForTest;

    public void createTasksForTest() {
        tasksForTest = new ArrayList<>();
        tasksForTest.add(new Task("name1", "description1", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T14:30:00"), 10));//0
        tasksForTest.add(new Task("name2", "description2", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T14:40:00"), 20));//1
        tasksForTest.add(new Task("name3", "description3", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T14:50:00"), 25));//2
        tasksForTest.add(new Epic("name4", "description4", TaskStatus.NEW));//3
        tasksForTest.add(new Epic("name5", "description5", TaskStatus.NEW));//4
        tasksForTest.add(new Task("name6", "description6", TaskStatus.NEW,
                LocalDateTime.parse("2023-02-26T15:20:00"), 30));//5

        for (int i = 0; i < tasksForTest.size(); i++) {
            tasksForTest.get(i).setId(i);
        }
    }

    @BeforeEach
    public void beforeEach() {
        historyManagerForTest = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTask() {
        createTasksForTest();
        //history: 0, 1, 2, 3, 4, 5
        ArrayList<Task> rightOrder = tasksForTest;

        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }

        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldAddTaskWhenRepeatedTask() {
        createTasksForTest();
        ArrayList<Task> rightOrder = new ArrayList<>();
        rightOrder.add(tasksForTest.get(1));
        rightOrder.add(tasksForTest.get(2));
        rightOrder.add(tasksForTest.get(3));
        rightOrder.add(tasksForTest.get(0));
        rightOrder.add(tasksForTest.get(4));
        rightOrder.add(tasksForTest.get(5));

        //history: 0, 1, 2, 3, 4, 5
        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }

        historyManagerForTest.add(tasksForTest.get(0));
        historyManagerForTest.add(tasksForTest.get(4));
        historyManagerForTest.add(tasksForTest.get(5));

        //history:  1, 2, 3, 0, 4, 5
        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldRemoveTask() {
        createTasksForTest();
        ArrayList<Task> rightOrder = new ArrayList<>();
        rightOrder.add(tasksForTest.get(0));
        rightOrder.add(tasksForTest.get(2));
        rightOrder.add(tasksForTest.get(3));
        rightOrder.add(tasksForTest.get(4));
        rightOrder.add(tasksForTest.get(5));

        //history: 0, 1, 2, 3, 4, 5
        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }
        //history: 0, 2, 3, 4, 5
        historyManagerForTest.remove(1);

        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldRemoveFromBeginning() {
        createTasksForTest();
        ArrayList<Task> rightOrder = new ArrayList<>();
        rightOrder.add(tasksForTest.get(1));
        rightOrder.add(tasksForTest.get(2));
        rightOrder.add(tasksForTest.get(3));
        rightOrder.add(tasksForTest.get(4));
        rightOrder.add(tasksForTest.get(5));

        //history: 0, 1, 2, 3, 4, 5
        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }
        //history: 1, 2, 3, 4, 5
        historyManagerForTest.remove(0);

        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldRemoveFromMiddle() {
        createTasksForTest();
        ArrayList<Task> rightOrder = new ArrayList<>();
        rightOrder.add(tasksForTest.get(0));
        rightOrder.add(tasksForTest.get(1));
        rightOrder.add(tasksForTest.get(4));
        rightOrder.add(tasksForTest.get(5));

        //history: 0, 1, 2, 3, 4, 5
        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }

        //history: 0, 1, 4, 5
        historyManagerForTest.remove(2);
        historyManagerForTest.remove(3);
        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldRemoveFromEnding() {
        createTasksForTest();
        ArrayList<Task> rightOrder = new ArrayList<>();
        rightOrder.add(tasksForTest.get(0));
        rightOrder.add(tasksForTest.get(1));
        rightOrder.add(tasksForTest.get(2));
        rightOrder.add(tasksForTest.get(3));

        //history: 0, 1, 2, 3, 4, 5
        for (Task task : tasksForTest) {
            historyManagerForTest.add(task);
        }

        //history: 0, 1, 2, 3
        historyManagerForTest.remove(4);
        historyManagerForTest.remove(5);
        assertArrayEquals(rightOrder.toArray(), historyManagerForTest.getHistory().toArray());
    }

    @Test
    public void shouldReturnEmptyArrayOfHistoryWhenEmpty() {
        assertTrue(historyManagerForTest.getHistory().isEmpty());
    }
}
