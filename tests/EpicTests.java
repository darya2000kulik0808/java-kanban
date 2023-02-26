import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.TaskStatus;
import task.Epic;
import task.Subtask;
import managers.taskManager.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTests {

    private Epic epicForTest;
    private ArrayList<Subtask> subtasksForEpic;
    private InMemoryTaskManager inMemoryTaskManagerForTest;
    public void createSubtasksForEpic(TaskStatus status1, TaskStatus status2,
                                      TaskStatus status3, TaskStatus status4, int  idEpic){
        subtasksForEpic = new ArrayList<>();
        subtasksForEpic.add(new Subtask("TestSubtaskName1", "TestSubtaskDescription1", status1,
                LocalDateTime.parse("2023-02-26T14:30:00"), 10, idEpic));
        subtasksForEpic.add(new Subtask("TestSubtaskName2", "TestSubtaskDescription2", status2,
                LocalDateTime.parse("2023-02-26T14:41:00"), 20, idEpic));
        subtasksForEpic.add(new Subtask("TestSubtaskName3", "TestSubtaskDescription3", status3,
                LocalDateTime.parse("2023-02-26T15:01:00"), 15, idEpic));
        subtasksForEpic.add(new Subtask("TestSubtaskName4", "TestSubtaskDescription4", status4,
                LocalDateTime.parse("2023-02-26T15:16:00"), 5,idEpic));
    }
    @BeforeEach
    public void beforeEach(){
        inMemoryTaskManagerForTest = new InMemoryTaskManager();
        epicForTest = new Epic("TestEpicName", "TestEpicDescription",
                TaskStatus.NEW);
        inMemoryTaskManagerForTest.createEpic(epicForTest);
    }

    @Test
    public void shouldReturnTaskStatusNewWhenNoSubtasks(){
        assertEquals(TaskStatus.NEW, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не NEW");
    }

    @Test
    public void shouldReturnTaskStatusNewWhenAllSubtasksAreNew(){
        createSubtasksForEpic(TaskStatus.NEW, TaskStatus.NEW, TaskStatus.NEW, TaskStatus.NEW, epicForTest.getId());

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(TaskStatus.NEW, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не NEW");
    }

    @Test
    public void shouldReturnTaskStatusDoneWhenAllSubtasksAreDone(){
        createSubtasksForEpic(TaskStatus.DONE, TaskStatus.DONE, TaskStatus.DONE, TaskStatus.DONE, epicForTest.getId());

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(TaskStatus.DONE, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не DONE");
    }

    @Test
    public void shouldReturnTaskStatusInProgressWhenSubtasksAreBothDoneAndNew(){
        createSubtasksForEpic(TaskStatus.NEW, TaskStatus.NEW, TaskStatus.DONE, TaskStatus.DONE, epicForTest.getId());

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    public void shouldReturnTaskStatusInProgressWhenAllSubtasksAreInProgress(){
        createSubtasksForEpic(TaskStatus.IN_PROGRESS, TaskStatus.IN_PROGRESS,
                              TaskStatus.IN_PROGRESS, TaskStatus.IN_PROGRESS, epicForTest.getId());

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    public void shouldReturnTaskStatusInProgressWhenAllSubtasksHaveMixedStatuses(){
        createSubtasksForEpic(TaskStatus.IN_PROGRESS, TaskStatus.DONE,
                TaskStatus.NEW, TaskStatus.IN_PROGRESS, epicForTest.getId());

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManagerForTest.getEpicById(epicForTest.getId()).getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @Test
    public void shouldReturnRightTimeAndDuration(){
        createSubtasksForEpic(TaskStatus.IN_PROGRESS, TaskStatus.DONE,
                TaskStatus.NEW, TaskStatus.IN_PROGRESS, epicForTest.getId());

        long allDuration = 0;

        for(Subtask subtask: subtasksForEpic){
            inMemoryTaskManagerForTest.createSubtask(subtask);
            allDuration = allDuration + subtask.getDuration();
        }

        inMemoryTaskManagerForTest.updateEpic(epicForTest);
        assertEquals(allDuration, epicForTest.getDuration());
        assertEquals(subtasksForEpic.get(0).getStartTime(), epicForTest.getStartTime());
        assertEquals(subtasksForEpic.get(3).getEndTime(), epicForTest.getEndTime());
    }
}
