package sudoku.model;

public class Solve {
	Board board;
	int size;
	
	public Solve(Board b, int s){
		board = b;
		size = s;
		
	}
	
	public boolean solve(){
		return solve(0, 0);
	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean solve(int x, int y){
		int xN, yN; //new x and y
		if(x == size-1){
			xN = 0; 
			yN = y + 1;
		}
		else{
			xN = x + 1;
			yN = y;
		}
		
		if(board.checkSolved()) 	return true;
		else{
			for (int i = 1; i <= size; ++i){
				int[] ca = {i,x,y};
				if (board.checkValidCoordinates(i, x, y)){
					board.setCoordinates(i, x, y);
					if(solve(xN, yN)) return true;
					else{
						ca[0] = 0;
						ca[1] = x;
						ca[2] = y;
						board.setCoordinates(i, x, y);
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
}
