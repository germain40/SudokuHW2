package sudoku.model;

public class DoublyLinkedList {
 
    private Node head;
    private Node current;
    private Node tail;
    private int size;
    public int x, y, n;
     
    public DoublyLinkedList() {
        size = 0;
    }
    
    public int size() { return size; }

    public void add(int x, int y, int n) {   
        Node tmp = new Node(x, y, n, null, current);
        if(current != null) {current.next = tmp;}
        tail = tmp;
        current = tmp;
        if(head == null) { head = tmp;}
        size++;
    }
    
   public void redo(){
    	if(current.next != null){
            current = current.next;
        }
    }
    
	public void undo(){
        if(current.prev != null){
            current = current.prev;
        }
    }
    
    private class Node {
        int x, y, n;
        Node next;
        Node prev;
 
        public Node(int x, int y, int n, Node next, Node prev) {
            this.x = x;
            this.y = y;
            this.n = n;
            this.next = next;
            this.prev = prev;
        }
    }
}
