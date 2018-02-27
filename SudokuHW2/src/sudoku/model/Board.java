package sudoku.model;

/** An abstraction of Sudoku puzzle. */
public class Board {

    /** Size of this board (number of columns/rows). */
    public final int size;
	private int[][] b;

    /** Create a new board of the given size. */
    public Board(int size) {
        this.size = size;
        b = new int[size][size];
    }

    /** Return the size of this board. */
    public int size() {
    	return size;
    }

	public int[][] checkBoard(int n, int x, int y) { 	// updates board and checks valid entry
		int sqrt = (int) Math.sqrt(b.length);
		int subx = x / sqrt;
		int suby = y / sqrt;
		int boundx = (subx +1) * sqrt;
		int boundy = (suby +1) * sqrt;
		
		if (n > 0 && n <= b.length) {
			if (x >= 0 && x <= b.length) {
				if (y >= 0 && y <= b.length) {
					
					for (int i = 0; i < b.length; i++) {			// checks rules 
						if (b[x][i] == n) {	 					// rows
							System.out.println("Invalid input... Number in same row");
							return b;
						}
						
						if (b[i][y] == n) {						// columns
							System.out.println("Invalid Input... Number in same column");
							return b;
						}
					}
					
					for (int i = subx * sqrt; i < boundx; i++) {			// subgrids
						for (int j = suby * sqrt; j < boundy; j++) {
							if (b[i][j] == n) {
								System.out.println("Invalid Input.. Number in same Sub-Grid");
								return b;
							}
						}
					}
					
					b[x][y] = n;
					//System.out.println("Updated!");
				}
			}
		}
		return b;
	}
	
	public boolean checkSolved() {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (b[i][j] == 0)
					return false;
			}
		}
		return true;
	}
	
	public int[][] getBoard() {
		return b;
	}
}
