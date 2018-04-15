package sudoku.model;

public class DoublyLinkedList {
 
    private Node head;
    private Node current;
    private Node tail;
    private int size;
     
    public DoublyLinkedList() {
        size = 0;
    }
    
    public int size() { return size; }

    public void add(Board element) {   
        Node tmp = new Node(element, null, current);
        if(current != null) {current.next = tmp;}
        tail = tmp;
        current = tmp;
        if(head == null) { head = tmp;}
        size++;
    }
    
    public Board redo(){
    	if(current.next != null){
            current = current.next;
        }
    	return current.element;
    }
    
	public Board undo(){
        if(current.prev != null){
            current = current.prev;
        }
        return current.element;
    }
    
    private class Node {
        Board element;
        Node next;
        Node prev;
 
        public Node(Board element, Node next, Node prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
