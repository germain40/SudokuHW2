package sudoku.model;

public class Solve {
	Board board;
	int size;
	
	public Solve(Board b, int s){
		this.board = b;
		this.size = s;
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean solve(){
		XY xy = new XY (0,0);
		int grid[][] = board.getBoard();
		
		if (checkNums(grid, xy)) {
			board.setBoardArray(grid);
			return true;
		} else {
			for (int n = 1; n <= size; n++) {
				if (board.checkValidCoordinates(n, xy.x, xy.y)) {
					grid[xy.x][xy.y] = n;
					if(solve()) {
						return true;
					} else {
						grid[xy.x][xy.y] = 0;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isSolvable(){
		Board tempBoard = new Board(size);
		tempBoard.setBoardArray(board.getBoard());
		Solve tempsolve = new Solve(tempBoard, tempBoard.size);
		return tempsolve.solve();
	}
	
	private static boolean checkNums(int[][] g, XY rc) {
		for (rc.x = 0; rc.x < g.length; rc.x++) {
			for (rc.y =0; rc.y < g.length; rc.y++) {
				if (g[rc.x][rc.y] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static class XY {
        int x;
        int y;

        XY(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
