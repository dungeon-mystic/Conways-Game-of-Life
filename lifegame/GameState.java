package lifegame;

import java.lang.Thread.State;

/**
 * GameState 
 * 
 * Represents the current state of the lifegame.
 * @author stirling.joyner, Lisa Hemphill (team 2)
 *
 */
public interface GameState {
	
	/**
	 * The game's behavior when the play/pause button is clicked.
	 * @return the state the game changes to.
	 */
	public void switchState();
	/**
	 * What the game does when it tries to advance a turn.
	 */
	public Thread act(Thread thread);
	
	public boolean isPlaying();

}
