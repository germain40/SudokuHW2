public class solve {
	Board board;
	int size;
	
	public solve(Board b, int s){
		board = b;
		size = s;
		
	}
	//MISSING JAVADOC
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
		

		if(board.isSolved()) 	return true;
		else{
			for (int i = 1; i <= size; ++i){
				int[] ca = {x,y,i};
				if (board.validCoordinates(ca)){
					board.setCoordinates(ca);
					if(solve(xN, yN)) return true;
					else{
						ca[0] = x;
						ca[1] = y;
						ca[2] = 0;
						board.setCoordinates(ca);
					}
				}
				
			}
		}
		return false;
		
	}
	public boolean isSolvable(){
		Board tempBoard = new Board();
		tempBoard.setBoardArray(board.getBoard());
		solve tempsolve = new solve(tempBoard, tempBoard.getSize());
		return tempsolve.solve();
	}
}
