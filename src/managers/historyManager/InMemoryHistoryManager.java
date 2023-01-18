package managers.historyManager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> likedTaskHistory;
    private final HashMap<Integer, Node<Task>> taskHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new HashMap<>();
        this.likedTaskHistory = new CustomLinkedList<>();
    }

    @Override
    public void add(Task task) {

        if(taskHistory.containsKey(task.getId())){
            likedTaskHistory.removeNode(taskHistory.get(task.getId()));
            taskHistory.remove(task.getId());
        }

        Node<Task> lastLinked = likedTaskHistory.linkLast(task);
        taskHistory.put(task.getId(), lastLinked);
    }

    @Override
    public void remove(int id){
        if(taskHistory.containsKey(id)) {
            likedTaskHistory.removeNode(taskHistory.get(id));
            taskHistory.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {

        return likedTaskHistory.getTasks();
    }

    class CustomLinkedList<T> {
        private  Node<T> head;
        private  Node<T> tail;
        private int size = 0;

        public  int getSize(){
            return this.size;
        }

        Node<T> linkLast(T element){
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail != null)
                oldTail.setNext(newNode);
            else
                head = newNode;
            size++;

            return newNode;
        }

        ArrayList<T> getTasks(){
            ArrayList<T> taskOutput = new ArrayList<>();
            Node<T> node = head;

           for(int i = 0; i< size; i++){
                taskOutput.add(node.getData());
                node = node.getNext();
            }

            return taskOutput;
        }

        void removeNode(Node<T> node) {
            if(node != null) {
                if (node.getNext() == null) {
                    tail = node.getPrev();
                } else {
                    node.getNext().setPrev( node.getPrev());
                }
                if (node.getPrev() == null){
                    head = node.getNext();
                } else {
                    node.getPrev().setNext(node.getNext());
                }
                size--;
            }
        }
    }

}
