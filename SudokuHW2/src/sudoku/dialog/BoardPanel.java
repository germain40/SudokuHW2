package sudoku.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import sudoku.model.Board;

/**
 * A special panel class to display a Sudoku board modeled by the
 * {@link sudoku.model.Board} class. You need to write code for
 * the paint() method.
 *
 * @see sudoku.model.Board
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
    
	public interface ClickListener {
		
		/** Callback to notify clicking of a square. 
		 * 
		 * @param x 0-based column index of the clicked square
		 * @param y 0-based row index of the clicked square
		 */
		void clicked(int x, int y);
	}
	
    /** Background color of the board. */
	private static final Color boardColor = new Color(247, 223, 150);

    /** Board to be displayed. */
    private Board board;

    /** Width and height of a square in pixels. */
    private int squareSize;
    
    /** Used for hint display. */
    private boolean start;

    /** Create a new board panel to display the given board. */
    public BoardPanel(Board board, ClickListener listener) {
        this.board = board;
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int xy = locateSquaree(e.getX(), e.getY());
            	if (xy >= 0) {
            		listener.clicked(xy / 100, xy % 100);
            	}
            }
        });
    }

    /** Set the board to be displayed. */
    public void setBoard(Board board) {
    	this.board = board;
    }
    /** for highlight method needs cordinates */
    int h[] = {-1, -1};
    public void highlight(int x, int y) {
    	h[0] = x;
    	h[1] = y;
    }
    
    /**
     * Given a screen coordinate, return the indexes of the corresponding square
     * or -1 if there is no square.
     * The indexes are encoded and returned as x*100 + y, 
     * where x and y are 0-based column/row indexes.
     */
    private int locateSquaree(int x, int y) {
    	if (x < 0 || x > board.size * squareSize
    			|| y < 0 || y > board.size * squareSize) {
    		return -1;
    	}
    	int xx = x / squareSize;
    	int yy = y / squareSize;
    	return xx * 100 + yy;
    }

    /** Draw the associated board. */
    @Override
    public void paint(Graphics g) {
        super.paint(g); 

        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        
        /** square size */
        Dimension dim = getSize();
        squareSize = Math.min(dim.width, dim.height) / board.size;

        g.setColor(boardColor);
        g.fillRect(0, 0, squareSize * board.size, squareSize * board.size);
        
        /** color pre-filled numbers */
        boolean[][] pre = board.getBr();
        for (int i = 0; i < board.size; i++) {
        	for (int j = 0; j < board.size; j++) {
        		if (pre[i][j]) {
        			g.setColor(Color.CYAN);
        			g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
        		}
        	}
        }
        
        /** highlights square */
        if(h[0] != -1 && h[1] != -1) {
        	g.setColor(Color.MAGENTA);
        	g.fillRect(h[0]*squareSize, h[1]*squareSize, squareSize, squareSize);
        	h[0] = -1;
        	h[1] = -1;
        }
        
        if (board.isHint()) {
        	 g.setColor(Color.BLACK);
        	 int[] dist = new int[4];
        	 if(board.size == 9){
     			dist[0] = 9; //how far apart x
     			dist[1] = 6; //starting x
     			dist[2] = 12; //starting y
     			dist[3] = 9; // how far apart y
     		}
     		else if(board.size == 4){
     			dist[0] = 34; //how far apart x
     			dist[1] = 20; //starting x
     			dist[2] = 26; //starting y
     			dist[3] = 34; // how far apart y
     		}
        	int[][] b = board.getBoard();
        	for (int i = 0; i < board.size; i++) {
            	for (int j = 0; j < board.size; j++) {
    				if (b[i][j] != 0) {
    					g.drawString(Integer.toString(b[i][j]), (i * squareSize) + (squareSize / 2), (j * squareSize) + (squareSize / 2));
    					g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
    				}
    				else {
    					int x = i;
    					int y = j;
    					if(b[y][x] == 0) {
							for (int k = 0; k < (int) Math.sqrt(b.length); k++) {
								for (int m = 0; m < (int) Math.sqrt(b.length); m++) {
									int n = m * (int) Math.sqrt(b.length) + (k + 1);
									if (board.checkValidCoordinates(n, y, x))
										g.drawString(Integer.toString(n), (squareSize) * (j) + (k * dist[0]) + dist[1],
												(squareSize) * (i) + dist[2] + (m * dist[3]));
								}
							}
							g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
    					}
    				}
            	}
            }
        }

        /** draw squares */
        Color c = Color.black;
        g.setColor(c);
        int sqrt = (int) Math.sqrt(board.size);

        for (int i = 0; i < board.size; i++) {
        	for (int j = 0; j < board.size; j++) {
        		g.drawRect(i * squareSize, j * squareSize, squareSize, squareSize); //(x, y, width, height)
        	}
        }
        for (int i = 0; i <= squareSize * board.size; i += squareSize) {
        	g.drawLine(0, i, squareSize * board.size, i);
        	g.drawLine(i, 0, i, squareSize * board.size);
        	if ((i/squareSize) % sqrt == 0) {
        		g.drawLine(0, i+1, squareSize * board.size, i+1);
        		g.drawLine(i+1, 0, i+1, squareSize * board.size);
        		g.drawLine(0, i-1, squareSize * board.size, i-1);
        		g.drawLine(i-1, 0, i-1, squareSize * board.size);
        	}
        }
        
        /** draws numbers */
        int b[][] = board.getBoard();
        
        for (int i = 0; i < board.size; i++) {
        	for (int j = 0; j < board.size; j++) {
        		if (b[i][j] != 0)
        		g.drawString(Integer.toString(b[i][j]), (i * squareSize) + (squareSize / 2), (j * squareSize) + (squareSize / 2));
        	}
        }
    }
}
