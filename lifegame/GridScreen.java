package lifegame;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * GridScreen
 * Creates the JFrame and sets up the GUI portion of the grid and its screen 
 *  
 * @author Noah Cameron, Lisa Hemphill 
 * Software Development, Team 2
 * March 22, 2016
 */
@SuppressWarnings("serial")
public class GridScreen extends JFrame{

	/**
	 * Basic JFrame to hold everything 
	 */
	public JFrame frmGameOfLife;

	/**
	 * Will hold the given width of the grid  
	 */
	private int width;

	/**
	 * Will hold the given height of the grid  
	 */
	private int height;

	/**
	 * 2D array of cells (forming a grid)
	 */
	private Cell[][] grid;

	/**
	 * Instance of the brain of the game 
	 */
	private GridBrain brain;

	/**
	 * Boolean to show the current state of the game
	 */
	private boolean isPaused;

	/**
	 * Iteration Count
	 */
	public JLabel lblIteration;


	/**
	 * Constructor. This constructor is called if the user
	 * entered a grid size on start instead of selecting a 
	 * file to load from.
	 * @param width the width of the grid
	 * @param height the height of the grid
	 */
	public GridScreen(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new Cell[width][height];
		brain = new GridBrain(grid, this);
		brain.populate();
		isPaused = true;
		initialize();

	}

	/**
	 * Constructor. This constructor is called if the user
	 * selected a file to load from instead of entering
	 * a gridsize.
	 * @param filename the name of the file to load from
	 */
	public GridScreen(String filename) throws FileNotFoundException {
		brain = new GridBrain(filename, this);
		grid = brain.getCellGrid();
		width = grid.length;
		height = grid[0].length;
		isPaused = true;

		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
		    e.printStackTrace();
		}
		frmGameOfLife = new JFrame();
		frmGameOfLife.setTitle("Game of Life");
		frmGameOfLife.setBounds(100, 100, 750, 562);
		frmGameOfLife.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGameOfLife.getContentPane().setLayout(null);

		// sets the grid panel to correct size
		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 520, 520);
		frmGameOfLife.getContentPane().add(panel);
		panel.setLayout(new GridLayout(width, height)); 

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(584, 460, 117, 25);
		frmGameOfLife.getContentPane().add(btnSave);

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// opens graph window
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							if (isPaused) {
								SaveScreen ss = new SaveScreen(brain);
								ss.frmSave.setVisible(true);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		final JButton btnPlaypause = new JButton("Play");
		btnPlaypause.setBounds(584, 60, 117, 25);
		frmGameOfLife.getContentPane().add(btnPlaypause);

		btnPlaypause.addActionListener(new ActionListener () {
			private Thread thread = null;
			public void actionPerformed(ActionEvent e) {
				isPaused = !isPaused;
				playPauseText(btnPlaypause);
				brain.state.switchState();
				thread = brain.state.act(thread);
			}						
		});

		JButton btnStep = new JButton("Step");
		btnStep.setBounds(584, 120, 117, 25);
		frmGameOfLife.getContentPane().add(btnStep);


		btnStep.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				if (!brain.state.isPlaying()) {
					try {
						int oldSpeed = brain.getSpeed();
						brain.setSpeed(50);
						brain.run();
						brain.setSpeed(oldSpeed);
					} catch (Exception ie) {
						System.out.println("Exception in grid screen: step button");
						ie.printStackTrace();
					}	
				}
			}						
		});

		final JSlider slider = new JSlider();

		slider.setValue(25);
		slider.setMaximum(50);
		slider.setBounds(572, 286, 150, 16);
		frmGameOfLife.getContentPane().add(slider);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!slider.getValueIsAdjusting()) {
					int speed = (int)slider.getValue();
					brain.setSpeed(speed);
				}
			}

		});


		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(584, 180, 117, 25);
		frmGameOfLife.getContentPane().add(btnReset);

		btnReset.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				if (!brain.state.isPlaying()) {
					try {
						brain.resetGrid();
						brain.iteration = 0;
						setIteration(0);
						slider.setValue(25);		
					}catch (Exception ie) {
						System.out.println("Exception in grid screen: reset button");
					}	
				}
			}

		});





		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setBounds(619, 314, 70, 15);
		frmGameOfLife.getContentPane().add(lblSpeed);


		lblIteration = new JLabel("Iteration: " + brain.iteration);
		lblIteration.setBounds(572, 389, 117, 16);
		frmGameOfLife.getContentPane().add(lblIteration);

		// adds the buttons to the grid
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				JButton btnNewButton = grid[i][j].getButton();
				btnNewButton.addActionListener(grid[i][j]);
				panel.add(grid[i][j].getButton());

			}
		}

	}

	/**
	 * Toggles the text on the Play/Pause button
	 * @param button
	 */
	public void playPauseText(JButton button){
		if(isPaused) button.setText("Play");
		else button.setText("Pause");
	}

	/**
	 * Updates the iteration number on the label  
	 * @param it, iteration number
	 */
	public void setIteration(int it) {
		lblIteration.setText("Iteration: " + it);
	}

}
