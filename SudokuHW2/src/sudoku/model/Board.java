package sudoku.model;

import java.util.ArrayList;

import sudoku.model.DoublyLinkedList;

/** An abstraction of Sudoku puzzle. */
public class Board {

    /** Size of this board (number of columns/rows). */
    public final int size;
	private int[][] b;
	private boolean[][] br;
	
	/** Linked list used for undo/redo. */
	DoublyLinkedList head = null;
	DoublyLinkedList current = null;
	DoublyLinkedList tail = null;
	
	boolean start;
	int toggle = 0;

    /** Create a new board of the given size. */
    public Board(int size) {
    	start = true;
    	this.size = size;
        b = new int[size][size];
        br = new boolean[size][size];
        partialFill();
        start = false;
       //solver.solve();
    }

    /** Return the size of this board. */
    public int size() {
    	return size;
    }
    
    public ArrayList<Integer> hint(int x, int y) {
    	ArrayList<Integer> hint = new ArrayList<Integer>();
    	for (int n = 1; n <= size; n++) {
    		if (checkValidCoordinates(n, x, y)) {
    			hint.add(n);
    		}
    	}
    	return hint;
    }
    
    /** Partially fills in the starting board. */
    public void partialFill() {
    	int counter = 0;
    	int pre;
    	if (size == 9) {pre = 17;}
    	else { pre = 3;}
    	
    	while( counter != pre) {
    		int n = (int) (Math.random() * size) + 1;
    		int x = (int) (Math.random() * size) + 1;
    		int y = (int) (Math.random() * size) + 1;
    		for (; x < size; x++) {
    			for (; y < size; y++) {
    				if (b[x][y] == 0) {
    					if (br[x][y] != true) {
    						if (checkValidCoordinates(n, x, y)) {
    							setCoordinates(n, x, y);
        						br[x][y] = true;
        						counter++;
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
	/** updates board and checks valid entry */
	public int[][] checkBoard(int n, int x, int y) {
		if (n == 0) {
			b[x][y] = 0;
			return b;
		}
		
		if (n > 0 && n <= b.length) {
			if (x >= 0 && x <= b.length) {
				if (y >= 0 && y <= b.length) {
					/** checks rules */
					if(checkValidCoordinates(n, x, y)) {
						if (br[x][y] == false) {
							setCoordinates(n, x, y);
							//System.out.println("Updated!");
						}
					}
				}
			}
		}
		return b;
	}
	
	/** Checks if coordinates are valid. */
	public boolean checkValidCoordinates(int n, int x, int y) {
		for (int i = 0; i < b.length; i++) {
			if (b[i][y] == n) {
				//System.out.println("Invalid Input... Number in same column");
				return false;
			}
		}
		for (int i = 0; i < b.length; i++) {
			if (b[x][i] == n) {
				//System.out.println("Invalid input... Number in same row");
				return false;
			}
		}
		for (int i = 0; i < b.length; i++) {
			if (b[x][i] == n) {
				//System.out.println("Invalid input... Number in same row");
				return false;
			}
		}
		int sqrt = (int) Math.sqrt(b.length);
		int subx = x / sqrt;
		int suby = y / sqrt;
		int boundx = (subx +1) * sqrt;
		int boundy = (suby +1) * sqrt;
		for (int i = subx * sqrt; i < boundx; i++) {
			for (int j = suby * sqrt; j < boundy; j++) {
				if (b[i][j] == n) {
					//System.out.println("Invalid Input.. Number in same Sub-Grid");
					return false;
				}
			}
		}
		return true;
	}
	
	/** Sets the new coordinates of the board. */
	public void setCoordinates(int n, int x, int y) {
		if(!start) {
			add(x, y, b[x][y], n);
			//System.out.println("x==" + current.x + " y==" + current.y +" added" + current.n);
		}
		b[x][y] = n;
	}
	
	/** checks if board is solved */
	public boolean checkSolved() {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (b[i][j] == 0)
					return false;
			}
		}
		return true;
	}
	
	public void add(int x, int y, int n, int n2) { 
        DoublyLinkedList tmp = new DoublyLinkedList(x, y, n, n2, null, current);
        if(current != null) {current.next = tmp;}
        tail = tmp;
        current = tmp;
        if(head == null) { head = tmp;}
    }
	
	/** Undo previous action. */
	public void undo() {
		b[current.x][current.y] = current.n;
		if(current.prev != null){
            current = current.prev;
        }
	}
	
	/** Redo previous action. */
	public void redo() {
		b[current.x][current.y] = current.n2;
		if (current.next != null) {
			current = current.next;
		}

	}
	
	public void setBoardArray(int[][] a) {
		for (int i = 0; i < size; i++) 
			for (int j = 0; j < size; j++)
				b[i][j] = a[i][j];
	}
	
	public int[][] getBoard() {
		return b;
	}
	
	public boolean[][] getBr() {
		return br;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setToggle(int i) {
		this.toggle = i;
	}
	
	public boolean isHint() {
		return toggle == 1;
	}
}
