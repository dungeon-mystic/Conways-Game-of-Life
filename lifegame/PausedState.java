package lifegame;
import java.lang.Thread.State;

/**
 * PausedState 
 * 
 * Defines the game's behavior when it is in the paused state (i.e. after the pause button has been pressed)
 * This class is a singleton.
 * 
 * @author Stirling Joyner
 * Software Development, Team 2
 * March 16, 2016
 */
public class PausedState implements GameState {
	
	/**
	 * The grid from the game
	 */
	private GridBrain grid;
	
	/**
	 * Empty constructor.
	 */
	public PausedState(GridBrain grid) {
		this.grid = grid;
	}
	
	public Thread act(Thread thread) {
		thread.interrupt();
		return thread;
	}

	
	public void switchState() {
		// return a RunningState
		grid.getNewState(new RunningState(grid));
	}
	
	/**
	 * Returns false because it is paused
	 */
	public boolean isPlaying() {
		return false;
	}


}
