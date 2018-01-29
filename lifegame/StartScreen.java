package lifegame;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * StartScreen
 * Creates the JFrame and sets up the GUI portion of the initial starting screen
 *  
 * @author Noah Cameron, Lisa Hemphill 
 * Software Development, Team 2
 * March 22, 2016
 */
@SuppressWarnings("serial")
public class StartScreen extends JFrame {

	public JFrame frmStartScreen;
	public JTextField x_text;
	public JTextField y_text;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreen window = new StartScreen();
					window.frmStartScreen.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try { 
			// Thanks, stackoverflow
	    	UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
	 	} catch (Exception e) {
	            e.printStackTrace();
	 	}
		frmStartScreen = new JFrame();
		frmStartScreen.setTitle("Start Screen");
		frmStartScreen.setBounds(100, 100, 337, 430);
		frmStartScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStartScreen.getContentPane().setLayout(null);
		
		// Text field for height of graph 
		y_text = new JTextField(5);
		y_text.setColumns(10);
		y_text.setBounds(174, 41, 48, 19);
		frmStartScreen.getContentPane().add(y_text);
		y_text.setText("50");
		
		JLabel lblSize = new JLabel("Please enter desired grid size:");
		lblSize.setBounds(56, 25, 225, 15);
		frmStartScreen.getContentPane().add(lblSize);
		
		// Text field for width of graph 
		x_text = new JTextField(5);
		x_text.setBounds(106, 41, 48, 19);
		frmStartScreen.getContentPane().add(x_text);
		x_text.setColumns(10);
		x_text.setText("50");
		
		// Label for "x" between the two text fields 
		JLabel lblX = new JLabel("x");
		lblX.setBounds(160, 43, 17, 15);
		frmStartScreen.getContentPane().add(lblX);
		
		// Button for start
		JButton btnGo = new JButton("Start");
		btnGo.setBounds(105, 352, 117, 25);
		frmStartScreen.getContentPane().add(btnGo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(49, 125, 239, 184);
		frmStartScreen.getContentPane().add(scrollPane);
		
		final JList<String> list = new JList<String>();
		list.setVisibleRowCount(10);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		populateList(list); // fill this list with filenames.
		scrollPane.setViewportView(list);
		
		
		
		JLabel lblOrSelectA = new JLabel("Or select a saved game from this list:");
		lblOrSelectA.setBounds(31, 98, 275, 15);
		frmStartScreen.getContentPane().add(lblOrSelectA);
		
		// Action listener for start button
		// parses the width and height from text fields, closes start screen, and opens graph window
		btnGo.addActionListener(new ActionListener () {
			int width = Integer.parseInt(x_text.getText());
			int height = Integer.parseInt(y_text.getText());
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// get the item from the saved games menu that the user selected (if any)
				final String selection = list.getSelectedValue();
				// if the user has selected a saved game from the list,
				if (!(selection == null)) {
					frmStartScreen.dispose(); // close start screen
					// open graph window
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								GridScreen hg = new GridScreen(selection);
								hg.frmGameOfLife.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				// if there is no selected item in the list, run the game with the values entered in the xy fields
				else {
					
					try {
						width = Integer.parseInt(x_text.getText());
						height = Integer.parseInt(y_text.getText());
					} catch (Exception ie) {
						width = 50;
						height = 50;
					}
				
					// Prevents the user from asking for grids larger than 100x100
					if (width > 100) width = 100;
					if (height > 100) height = 100;
					
					
					
					frmStartScreen.dispose(); // closes start screen
					
					// opens graph window
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								GridScreen hg = new GridScreen(width, height);
								hg.frmGameOfLife.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		
		
		
		
		
		
	}
	/**
	 * populate the list of saved games
	 * POSTCONDITION: the list is now populated with saved files and is immutable.
	 */
	private void populateList(JList<String> list) {
		Scanner savedGames = null; // this scanner will be used to add file names to the list
		try {
			// get the saved games file
			savedGames = new Scanner(new FileInputStream("savedGames.txt"));
		} catch (FileNotFoundException f) {
			// make the savedGames file
			try {
			new File("savedGames.txt").createNewFile();
			} catch (IOException io){
				System.out.println("Problem creating saved games list file.");
			}
			return;
		}


		// an array to hold the names of all the saved files. To be passed to list.
		// run through the filenames array and savedGames.txt, adding names of files
		// to filenames.
		ArrayList<String> filenames = new ArrayList<String>();
		while(savedGames.hasNextLine()) {
			String name = savedGames.nextLine();
			File f = new File(name);
			if(f.exists() && !f.isDirectory()) {
				filenames.add(name);
			}
		}
		String[] files = new String[filenames.size()];
		for(int i = 0; i < files.length; i++){
			files[i] = filenames.get(i);
		}
		savedGames.close();
		list.setListData(files);
		
		
	}
}
