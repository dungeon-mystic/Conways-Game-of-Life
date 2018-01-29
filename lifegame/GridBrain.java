package lifegame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JButton;

/**
 * GridBrain 
 * 
 * Handles the functionality of the grid in Conway's game of life.
 * Manages all of its cells and ensures that each one acts.
 * Updates the grid after each turn.
 * 
 * @author Stirling Joyner, Nathaniel Whitfield 
 * Software Development, Team 2
 * March 16, 2016
 */
public class GridBrain {
	/**
	 * True when the game is in test mode
	 */
	@SuppressWarnings("unused")
	private boolean test;
	
	/**
	 * The internal representation of the grid
	 */
	private Cell[][] grid;
	
	public int iteration;
	
	private GridScreen screen;
	
	public GameState state;
	
	private int speed;
	
	private class  FileFormatException extends RuntimeException {}


			
	
	/**
	 * Constructor. Fills internal with dead cells.
	 * @param grid, Grid of Cells
	 * @param screen, GUI screen with components 
	 */
	public GridBrain(Cell[][] grid, GridScreen screen){
		this.grid = grid;
		iteration = 0;
		this.screen = screen;
		state = new PausedState(this);
		speed = 25;
	}
	
	/**
	 * Constructor. To be used if the user selects a file instead
	 * of specifying a grid size.
	 * Parse the given file, use the information therein to create the internal Cell[][] grid.
	 * NOTE: to sync GridBrain's Cell[][] with the caller's Cell[][],
	 * the client must call GridBrain.getGrid() to get the 
	 * array.
	 * @param filename the name of the file to load this grid from.
	 * @throws FileNotFoundException 
	 */
	public GridBrain(String filename, GridScreen screen) throws FileNotFoundException {
		Scanner file = new Scanner(new FileInputStream(filename));
		
		
		// the first line of the file
		file.nextLine();
		//The second line of the file
		String secondLine = file.nextLine();
		// the second line of the file should begin with "I ".
		if (secondLine.substring(0,2).equals("I ")) {
			// parse the file into the iteration number
			try {
				iteration = Integer.parseInt(secondLine.substring(2));
			} catch (NumberFormatException e) { // if there's a problem with the parsing, throw an exception
				file.close();
				throw new FileFormatException();
			}
		}
		else {
			file.close();
			throw new FileFormatException(); 
		}
		String thirdLine= file.nextLine();
		/**
		 * The third line indicates the height and width of the grid. This is given in the format "H [number] W [number]".
		 * Those numbers have to be parsed into ints. We will use a StringTokenizer to break up thirdLine into four separate
		 * strings, using spaces as the delimiter. We will check the first and third tokens to make sure they are the correct
		 * letters. We will attempt to parse the numbers as ints. If any of these conditions fail, we throw a FileFormatException.
		 */
		// create a StringTokenizer to break up the second line into separate data (width of grid, height, letters that precede them)
		StringTokenizer tokens = new StringTokenizer(thirdLine);
		int width;
		int height;
		try {
			// if the height isn't preceded by an H, this is a bad file.
			if (!tokens.nextToken().equals("W")) { file.close(); throw new FileFormatException(); }
			// parse in the height
			height = Integer.parseInt(tokens.nextToken());
			// if the width isn't preceded by a W, this is a bad file.
			if (!tokens.nextToken().equals("H")) { file.close(); throw new FileFormatException(); }
			// parse in the width
			width = Integer.parseInt(tokens.nextToken());
		} catch (NumberFormatException e) {
			file.close();
			throw new FileFormatException();
		}
		grid = new Cell[width][height];
		populate();  //fills grid with dead cells.
		
		// use the file to set the appropriate life value
		for (int i = 0; i < height; i++) {
			// this gets the next line of the matrix containing 0s and 1s representing the dead and alive cells
			String cellRow = file.nextLine();  
			for (int j = 0; j < width; j++) {
				if(cellRow.charAt(j) == '1'){
					grid[i][j].makeAlive();
				}
			}
		}
		file.close();
		state = new PausedState(this);
		speed = 25;
		this.screen = screen;
	}

	// For testing purposes only
	public GridBrain() {
		speed = 25;
	}

	public void run() {
		int opSpeed = Math.abs(speed-50);
		if (opSpeed !=50){
			try {
				Thread.sleep((50+ (opSpeed*10))); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act();
		}
	}

	/**
	 * Advance the grid by one turn.
	 */
	public void act() {
		// a new grid where the moves for the next turn will be stored
		boolean[][] newGrid = new boolean[grid.length][grid[0].length];
		// run through the grid.
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// the cell's next move. True if it lives, false if it dies.
				newGrid[i][j] = grid[i][j].act(neighborCount(i,j));
			}
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(newGrid[i][j]) grid[i][j].makeAlive();
				else grid[i][j].kill();
			}
		}
		iteration++;
		screen.setIteration(iteration);
	}
	
	
	/**
	 * returns whether the cell at the specified coordinates is alive.
	 * @param row index into the first dimension of the internal array
	 * @param column index into the second dimension of the internal array
	 * @return whether the cell at the specified coordinates is alive
	 */
	public boolean isAlive(int row, int column) {
		return grid[row][column].isAlive();
	}
	

	/**
	 * Clears the grid by killing each cell 
	 */
	public void resetGrid() {
		for (int i = 0; i < grid.length; i++) 
			for (int j = 0; j < grid[i].length; j++) 
				grid[i][j].kill();		
	}
	
	public void setSpeed(int sp) {
		speed = sp;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Fills the given grid with dead cells
	 * @param grid
	 */
	public void populate(){
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++) {
				Cell cell =  new Cell(new JButton()); // create the test cell
				grid[i][j] = cell; // place the cell in the grid
			}
	}
	
	/*------test mode only methods-----*/
	
	
	/**
	 * Sets the grid to test mode.
	 */
	public void test() { test = true; }
	
	/**
	 * Counts the neighbors this cell has
	 * @param x The x value
	 * @param y The y value
	 * @return neighbors The number of neighbors this cell has
	 */
	public int neighborCount(int x, int y){
		int neighbors = 0;
		for(int i = x-1; i < x+2; i++){
			for(int j = y-1; j < y+2; j++){
				if(i >= 0 && j >=0 && i < grid.length && j < grid[0].length){
					if(grid[i][j].isAlive()){
						if(i == x && j == y){
							// this is the one we are looking at so no increase for liveCount; 
						}else{
							neighbors++; 
						}
					}
				}
			}
		}
		return neighbors;
	}
	
	/**
	 * Sets the current state to the given one 
	 * @param state
	 */
	public void getNewState(GameState state){
		this.state = state;
	}
	
	/**
	 * 
	 * @return an int[][] representation of the internal cell grid.
	 */
	public int[][] getIntGrid(){
		int[][] numGrid = new int[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j].isAlive()) numGrid[i][j] = 1;
				else numGrid[i][j] = 0;
			}
		}
		return numGrid;
	}
	
	/**
	 * @return the internal grid of Cell[][]
	 */
	public Cell[][] getCellGrid() {
		return grid;
	}
	
	
}
