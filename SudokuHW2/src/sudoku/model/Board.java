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

	public String checkBoard(int n, int x, int y) { 	// updates board and checks valid entry
		int sqrt = (int) Math.sqrt(b.length);
		int subx = x / sqrt;
		int suby = y / sqrt;
		int boundx = (subx +1) * sqrt;
		int boundy = (suby +1) * sqrt;
		
		if (n > 0 && n <= b.length) {
			if (x >= 0 && x <= b.length) {
				if (y >= 0 && y <= b.length) {
					
					for (int i = 0; i < b.length; i++) {			// checks rules 
						if (b[x][i] == n) {	 					// column
							return  "Invalid Input... Number in same column";
						}
						
						if (b[i][y] == n) {						// rows
							return "Invalid input... Number in same row";
						}
					}
					
					for (int i = subx * sqrt; i < boundx; i++) {			// subgrids
						for (int j = suby * sqrt; j < boundy; j++) {
							if (b[i][j] == n) {
								return "Invalid Input.. Number in same Sub-Grid";
							}
						}
					}
					
					b[x][y] = n;
					return "Updated!";
				}
			}
		}
		return "";
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
