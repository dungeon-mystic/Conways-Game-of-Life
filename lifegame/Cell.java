package lifegame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cell 
 * 
 * A representation of a single cell in Conway's Game of Life. 
 * 
 * @author Stirling Joyner, Lisa Hemphill
 * Software Development, Team 2
 * March 16, 2016
 */
public class Cell implements ActionListener{

	/**
	 * Whether this cell is alive or dead.
	 */
	private boolean isAlive;

	/**
	 * JButton to change color when clicked
	 */
	private JButton button;

	/**
	 * The color of a dead cell
	 */
	
	private final Color DEAD_COLOR = Color.WHITE;
	/**
	 * the color of a living cell
	 */
	private final Color ALIVE_COLOR = Color.DARK_GRAY;
	
	/**
	 * Constructor. Initialize this cell to dead.
	 * @param btnNewButton 
	 * @param grid, the grid this cell is inside of.
	 * @param row, the row in the grid where this cell is located
	 * @param column, the column in the grid where this cell is located
	 */
	public Cell(JButton button){
		this.button = button;
		//button.setOpaque(true);		
		kill();
	}

	

	/**
	 * the cell takes one turn based on the rules of Conway's Game of Life.
	 * @param the number of live neighbors this cell has.
	 * @return true if the cell will be alive next turn, false if it will be dead.
	 */
	public boolean act(int liveNeighbors){
		// if the cell is alive
		if (isAlive){
			// if this cell has fewer than two or more than three live neighbors
			if (liveNeighbors < 2 || liveNeighbors > 3) {
				//die of underpopulation (case 1) or overpopulation (case 2)
				return false;
			}
			
			/*
			 * If the live cell has two or three live neighbors, it continues to
			 * live.
			 */
			assert liveNeighbors == 2 || liveNeighbors == 3;
			return true;
		}
		// else if the cell is dead and it has exactly three live neighbors,
		else if (liveNeighbors == 3) {
			return true;
			
		}
		return false;
	}
	
	/**
	 * @return true if the cell is dead false if alive 
	 */
	public boolean isDead(){
		return !isAlive; 
	
	}
	/**
	 * @return whether this cell is alive or dead.
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * Toggle this cell between life and death
	 */
	public void toggleLife() {
		isAlive = !isAlive;
		if(isAlive) button.setBackground(ALIVE_COLOR);
		else button.setBackground(DEAD_COLOR);
	}
	/**
	 * Kill this cell.
	 * POSTCONDITION: this cell will be in a dead state. isAlive will be false
	 * and the cell's color will be the appropriate color for a dead cell.
	 */
	public void kill() {
		isAlive = false;
		button.setBackground(DEAD_COLOR);
	}
	/**
	 * Make this cell alive.
	 * POSTCONDITION: this cell will be in an alive state. isAlive will be true
	 * and the cell's color will be the appropriate color for a living cell.
	 */
	public void makeAlive() {
		isAlive = true;
		button.setBackground(ALIVE_COLOR);
	}
	/**
	 * Switch this cell's color to the opposite of what it was.
	 */
	public void toggleColor(){
		if(isAlive) button.setBackground(ALIVE_COLOR);
		else button.setBackground(DEAD_COLOR);
	}
	
	public JButton getButton() {
		return button;
	}

	/**
	 * When this cell's button is pressed, it switches from dead to 
	 * alive or vice versa.
	 */
	public void actionPerformed(ActionEvent e) {
		this.toggleLife();
	}
	

}
