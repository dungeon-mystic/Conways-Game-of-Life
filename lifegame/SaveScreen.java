package lifegame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
/**
 * SaveScreen 
 * 
 * Creates the screen to save the game 
 * @author Noah Cameron, Stirling Joyner, Nathaniel Whitfield
 *
 */
public class SaveScreen {

	public JFrame frmSave;
	private JTextField textField;
	private GridBrain brain;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaveScreen window = new SaveScreen();
					window.frmSave.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public SaveScreen(GridBrain brain) {
		initialize();
		this.brain = brain;
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
		frmSave = new JFrame();
		frmSave.setTitle("Save");
		//frmSave.getContentPane().setBackground(UIManager.getColor("Button.disabledToolBarBorderBackground"));
		//frmSave.setBackground(UIManager.getColor("Button.darkShadow"));
		frmSave.setBounds(100, 100, 406, 164);
		frmSave.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSave.getContentPane().setLayout(null);
		
		JLabel lblPleaseEnterA = new JLabel("Please enter a name to save your game with: ");
		lblPleaseEnterA.setBounds(35, 27, 335, 15);
		frmSave.getContentPane().add(lblPleaseEnterA);
		
		textField = new JTextField();
		//textField.setBackground(UIManager.getColor("Button.background"));
		textField.setBounds(50, 52, 306, 19);
		frmSave.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		//btnSave.setForeground(Color.WHITE);
		////btnSave.setFont(new Font("Dialog", Font.BOLD, 12));
		////btnSave.setBackground(Color.DARK_GRAY);
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean overwriting = false;
				String gameName = textField.getText();

				gameName.trim();

				//Default Save
				if(gameName.equals("")){
					gameName = "gameSave";
				}
					
					
					BufferedWriter bw = null;
				      try {

					     //Specify the file name and path here
						 File file = new File(gameName);
	
						 /* This logic will make sure that the file 
						  * gets created if it is not present at the
						  * specified location*/
						  if (file.exists()) {
							  overwriting = true;
						  }
						  file.createNewFile();
	
						  FileWriter fw = new FileWriter(file, false);
						  bw = new BufferedWriter(fw);
						  int[][] grid = brain.getIntGrid();
						  String gridWrite = "";
						  for(int i = 0; i < grid.length; i++){
							  for(int j = 0; j < grid[0].length; j++){
								  gridWrite += grid[i][j];
							  }
							  gridWrite += "\n";
						  }
						  bw.write(gameName + "\nI " + brain.iteration + "\nW " + grid.length + " H " + grid[0].length +"\n" + gridWrite);		  

				      } catch (IOException ioe) {
					   ioe.printStackTrace();
					}
					finally
					{ 
					   try{
					      if(bw!=null)
						 bw.close();
					   }catch(Exception ex){
					       System.out.println("Error in closing the BufferedWriter"+ex);
					    }
					}

				    //Now that the new file has been created and written to, the saveGames file should be appended
				      gameName += "\n";
				      if(!overwriting){
				      try {
				    	  
				    	    Files.write(Paths.get("savedGames.txt"), gameName.getBytes(), StandardOpenOption.APPEND);
				    	}catch (IOException b) {
				    	    //exception handling left as an exercise for the reader
				    	}
				      }
				      
				      frmSave.dispose();
				      
				
			}
		});
		btnSave.setBounds(144, 97, 117, 25);
		frmSave.getContentPane().add(btnSave);
	}
}
