package sudoku.model;

public class DoublyLinkedList {
 
    DoublyLinkedList next;
    DoublyLinkedList prev;
    int x, y, n, n2;
     
    public DoublyLinkedList(int x, int y, int n, int n2, DoublyLinkedList next, DoublyLinkedList prev) {
        this.x = x;
        this.y = y;
        this.n = n;
        this.n2 = n2;
        this.next = next;
        this.prev = prev;
    }
}
