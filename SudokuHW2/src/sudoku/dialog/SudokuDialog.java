package sudoku.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import sudoku.model.Board;
import sudoku.model.Solve;

/**
 * A dialog template for playing simple Sudoku games.
 * You need to write code for three callback methods:
 * newClicked(int), numberClicked(int) and boardClicked(int,int).
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_SIZE = new Dimension(310, 430);

    private final static String IMAGE_DIR = "/image/";

    /** Sudoku board. */
    private Board board;

    /** Special panel to display a Sudoku board. */
    private BoardPanel boardPanel;

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel("");
    
    /** Buttons for the ToolBar. */
    JButton newGame, solveGame, checkSolve, giveHint;
    
    /** Menu items for dropdown menu. */
    JMenuItem menuItemNewGame, menuItemCheckSolve, menuItemSolve, menuItemGiveHint;
    
    /** Buttons for bonus problems. */
    JButton undo, redo;
    
    private int toggle;

    /** Create a new dialog. */
    public SudokuDialog() {
    	this(DEFAULT_SIZE);
    }
    
    /** Create a new dialog of the given screen dimension. */
    public SudokuDialog(Dimension dim) {
        super("Sudoku");
        setSize(dim);
        board = new Board(9);
        boardPanel = new BoardPanel(board, this::boardClicked);
        configureUI();
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        //setResizable(false);
    }

    /**
     * Callback to be invoked when a square of the board is clicked.
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    int[] val = {-1, -1, -1};
    private void boardClicked(int x, int y) {
    	boardPanel.highlight(x, y);
    	val[0] = x;
    	val[1] = y;
    	boardPanel.repaint();
    	
    	if(toggle == 1)
    		showMessage(String.format("Board clicked: x = %d, y = %d , Possible numbers = %s",  x, y, board.hint(x, y)));
    	else
    		showMessage(String.format("Board clicked: x = %d, y = %d",  x, y));
    }
    
    /**
     * Callback to be invoked when a number button is clicked.
     * @param number Clicked number (1-9), or 0 for "X".
     */
    private void numberClicked(int number) {
    	if(val[0] != -1 && val[1] != -1) {
    		val[2] = number;
    		board.checkBoard(val[2], val[0], val[1]);
            val[0] = -1;
            val[1] = -1;
            boardPanel.repaint();
        	showMessage("Number clicked: " + number);
    	}
    	else {
    		/**
    		 * plays sound
    		 */
    		 try { 
    			 AudioInputStream s = AudioSystem.getAudioInputStream(new File("ahem_x.wav"));
    			 Clip c = AudioSystem.getClip();
    			 
    			 c.open(s);
    			 c.start();
    			 
    			 while(!c.isRunning())
    				 Thread.sleep(10);
    			 while (c.isRunning())
    				 Thread.sleep(10);
    			 
    			 c.close();
    			 
    		 } catch (Exception e) {
    			 e.printStackTrace();
    		 }
    	   }
    	/**
    	 * checks board
    	 */
    	if(board.checkSolved()) {
    		int selectedOption = JOptionPane.showConfirmDialog(msgBar, "Congratulations!! Start new game?", "Solved!", JOptionPane.YES_NO_OPTION);
    		if(selectedOption == JOptionPane.YES_OPTION) {
		        newClicked(board.size);
    		}
    		else {
    			System.exit(0);
    		}
    	}
    }
    
    /**
     * Callback to be invoked when a new button is clicked.
     * If the current game is over, start a new game of the given size;
     * otherwise, prompt the user for a confirmation and then proceed
     * accordingly.
     * @param size Requested puzzle size, either 4 or 9.
     */
    private void newClicked(int size) {
    	if(!board.checkSolved()) {
    		int selectedOption = JOptionPane.showConfirmDialog(null, "Start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
	    	if(selectedOption == JOptionPane.YES_OPTION) {
		        this.board = new Board(size);
		        this.boardPanel.setBoard(board);
		        boardPanel.repaint();
    		}
    	}
    	else {
    		this.board = new Board(size);
    		this.boardPanel.setBoard(board);
    		boardPanel.repaint();
    	}
        
        showMessage("New clicked: " + size);
    }

    /**
     * Display the given string in the message bar.
     * @param msg Message to be displayed.
     */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }

    /** Configure the UI. */
    private void configureUI() {
    	setIconImage(createImageIcon("sudoku.png").getImage());
        setLayout(new BorderLayout());
        
    	JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        
        JPanel North = new JPanel();
        North.setLayout(new BorderLayout());
        
        JPanel menu = createMenu();
        menu.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        add(menu, BorderLayout.NORTH);
        
        JPanel toolBar = createToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        North.add(toolBar, BorderLayout.NORTH);
        
        JPanel buttons = makeControlPanel();
        // boarder: top, left, bottom, right
        buttons.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        center.add(buttons, BorderLayout.NORTH);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        board.setLayout(new GridLayout(1,1));
        board.add(boardPanel);
        center.add(board, BorderLayout.CENTER);
        
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        center.add(msgBar, BorderLayout.SOUTH);
        
        add(center, BorderLayout.CENTER);
        add(North, BorderLayout.NORTH);
        
        setupListeners();
    }
      
    /** Create a control panel consisting of new and number buttons. */
    private JPanel makeControlPanel() {
    	JPanel newButtons = new JPanel(new FlowLayout());
        JButton new4Button = new JButton("New (4x4)");
        for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
        	button.setFocusPainted(false);
            button.addActionListener(e -> {
                newClicked(e.getSource() == new4Button ? 4 : 9);
            });
            newButtons.add(button);
    	}
    	newButtons.setAlignmentX(LEFT_ALIGNMENT);
        
    	/** buttons labeled 1, 2, ..., 9, and X.
    	 * 
    	 */
    	JPanel numberButtons = new JPanel(new FlowLayout());
    	int maxNumber = board.size() + 1;
    	for (int i = 1; i <= maxNumber; i++) {
            int number = i % maxNumber;
            JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
            button.setFocusPainted(false);
            button.setMargin(new Insets(0,2,0,2));
            button.addActionListener(e -> numberClicked(number));
    		numberButtons.add(button);
    	}
    	numberButtons.setAlignmentX(LEFT_ALIGNMENT);

    	JPanel content = new JPanel();
    	content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(newButtons);
        content.add(numberButtons);
        return content;
    }
    
    /** Create toolbar consisting of new game, end game, and solveable buttons. */
    private JPanel createToolBar() {
    	JPanel panel = new JPanel();
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        panel.add(toolBar, BorderLayout.PAGE_START);
    	
		newGame = new JButton(createImageIcon("play.png"));
		newGame.setToolTipText("Play new game");
		toolBar.add(newGame);
		
		checkSolve = new JButton(createImageIcon("checkmark.png"));
		checkSolve.setToolTipText("Checks if current board is solveable");
		toolBar.add(checkSolve);
		
		solveGame = new JButton(createImageIcon("solution.jpg"));
		solveGame.setToolTipText("Solves the current game");
		toolBar.add(solveGame);
		
		giveHint = new JButton(createImageIcon("check.png"));
		giveHint.setToolTipText("Enable/Disable Hint");
		toolBar.add(giveHint);
		
        return panel;
    }
    
    private JPanel createMenu() {
    	JPanel menus = new JPanel();;
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription("Game menu");
		menuBar.add(menu);
		
		menuItemNewGame = new JMenuItem("New Game", KeyEvent.VK_N);
		menuItemNewGame.setIcon(createImageIcon("new_game.png"));
		menuItemNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItemNewGame.getAccessibleContext().setAccessibleDescription("Play a new game");
		
		menuItemCheckSolve = new JMenuItem("Check Game", KeyEvent.VK_C);
		menuItemCheckSolve.setIcon(createImageIcon("checkmark.png"));
		menuItemCheckSolve.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		menuItemCheckSolve.getAccessibleContext().setAccessibleDescription("Check if game is solveable");
		
		menuItemSolve = new JMenuItem("Solve Game", KeyEvent.VK_S);
		menuItemSolve.setIcon(createImageIcon("solution.jpg"));
		menuItemSolve.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menuItemSolve.getAccessibleContext().setAccessibleDescription("Solve game");
		
		menuItemGiveHint = new JMenuItem("Enable/Disable Hints", KeyEvent.VK_H);
		menuItemGiveHint.setIcon(createImageIcon("check.png"));
		menuItemGiveHint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		menuItemGiveHint.getAccessibleContext().setAccessibleDescription("Enable/Disable Hints");
		
		menu.add(menuItemNewGame);
		menu.add(menuItemCheckSolve);
		menu.add(menuItemSolve);
		menu.add(menuItemGiveHint);
		setJMenuBar(menuBar);
		setVisible(true);
		return menus;
    }
    
	private void setupListeners() {
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Object[] sizes = {"Quit",9,4};
					int size = JOptionPane.showOptionDialog(null, "Select size: ", "New Game", 
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, sizes, null);
					if(size == 2)  {
						board = new Board(4);
				        boardPanel.setBoard(board);
				        boardPanel.repaint();
					}
					if(size == 1)  {
						board = new Board(9);
				        boardPanel.setBoard(board);
				        boardPanel.repaint();
					}
					if(size == 0)
						System.exit(0);
				}
			}
		});
		
		checkSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Do you want to check if the board is solveable?", "Solveable", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Solve solver = new Solve(board, board.getSize());
					if (solver.isSolvable())
						showMessage("Is solvable");
					else 
						showMessage("Not solvable");
				}
			}
		});
		
		solveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Solve the game?", "Scrub", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Solve solver = new Solve(board, board.getSize());
					if (solver.isSolvable()) {
						solver.solve();
						boardPanel.repaint();
						if (board.checkSolved()) {
							int selectedOption = JOptionPane.showConfirmDialog(msgBar,
									"Game is solved. Start new game?", "Solved!", JOptionPane.YES_NO_OPTION);
							if (selectedOption == JOptionPane.YES_OPTION) {
								newClicked(board.size);
							}
						}
					}
					else 
						showMessage("Board can't be solved with current configuration");
				}
			}
		});
		
		giveHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				Object[] a = {"Enable", "Disable"};
				int input = JOptionPane.showOptionDialog(null, "Enable/Disable Hints?", "Hints", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, a, null);
				if (input == JOptionPane.YES_OPTION)
					toggle = 1;
				else {
					toggle = 0;
				}
			}
		});
		
		menuItemNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Object[] sizes = {"Quit",9,4};
					int size = JOptionPane.showOptionDialog(null, "Select size: ", "New Game", 
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, sizes, null);
					if(size == 2)  {
						board = new Board(4);
				        boardPanel.setBoard(board);
				        boardPanel.repaint();
					}
					if(size == 1)  {
						board = new Board(9);
				        boardPanel.setBoard(board);
				        boardPanel.repaint();
					}
					if(size == 0)
						System.exit(0);
				}
			}
		});
		
		menuItemCheckSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Do you want to check if the board is solveable?", "Solveable", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Solve solver = new Solve(board, board.getSize());
					if (solver.isSolvable())
						showMessage("Is solvable");
					else 
						showMessage("Not solvable");
				}
			}
		});
		
		menuItemSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				int input = JOptionPane.showConfirmDialog(null, "Solve the game?", "Scrub", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					Solve solver = new Solve(board, board.getSize());
					if (solver.isSolvable()) {
						solver.solve();
						boardPanel.repaint();
						if (board.checkSolved()) {
							int selectedOption = JOptionPane.showConfirmDialog(msgBar,
									"Game is solved. Start new game?", "Solved!", JOptionPane.YES_NO_OPTION);
							if (selectedOption == JOptionPane.YES_OPTION) {
								newClicked(board.size);
							}
						}
					}
					else 
						showMessage("Board can't be solved with current configuration");
				}
			}
		});
		
		menuItemGiveHint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				Object[] a = {"Enable", "Disable"};
				int input = JOptionPane.showOptionDialog(null, "Enable/Disable Hints?", "Hints", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, a, null);
				if (input == JOptionPane.YES_OPTION)
					toggle = 1;
				else {
					toggle = 0;
				}
			}
		});
	}

    /** Create an image icon from the given image file. */
    private ImageIcon createImageIcon(String filename) {
        URL imageUrl = getClass().getResource(IMAGE_DIR + filename);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        return null;
    }

    public static void main(String[] args) {
        new SudokuDialog();
    }
}
