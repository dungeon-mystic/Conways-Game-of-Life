package lifegame;
/**
 * RunningState 
 * 
 * Defines the game's behavior when it is in the running state (i.e. after the play button has been pressed)
 * This class is a singleton.
 * @author stirling.joyner, Lisa Hemphill
 *
 */
public class RunningState implements GameState {

	/**
	 * The grid from the game
	 */
	private GridBrain grid; 
	/**
	 * Empty constructor
	 */
	public RunningState(GridBrain grid) {
		this.grid = grid;
	}
		
	public Thread act(Thread thread) {
		Thread NewThread = new Thread(){
			private boolean isRunning = true;
			public void run(){
				while(isRunning){
					grid.run();
					try{
						sleep(300);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			public void interrupt(){ isRunning = false; }
		};
		NewThread.start();
		return NewThread;
	}

	public void switchState() {
		grid.getNewState(new PausedState(grid));
	}

	/**
	 * Returns true because it is playing
	 */
	public boolean isPlaying() {
		return true;
	}
}
