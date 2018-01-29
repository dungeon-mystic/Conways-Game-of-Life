package lifeTest;
import lifegame.Cell;
import lifegame.GridBrain;
import lifegame.GridScreen;
import lifegame.StartScreen;

import static org.junit.Assert.*;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.Test;
/**
 * GUITest
 * 
 * Tests the GUI for the lifegame.
 * 
 * @author Noah Cameron
 * Software Development, Team 2
 * March 25, 2016
 */
public class GUITest {

	@Test
	public void checkNullityOfStartScreenComponents() {
		StartScreen screen = new StartScreen();
		assertNotNull("Screen is null", screen);
		
		JFrame frame = screen.frmStartScreen;
		assertNotNull("Frame is null", frame);
		
		JTextField x = screen.x_text;
		assertNotNull("x field is null", x);

		JTextField y = screen.y_text;
		assertNotNull("y field is null", y);
	}
	
	@Test
	public void checkNullityOfGridScreenComponents() {
		GridScreen screen = new GridScreen(50, 50);
		assertNotNull("Screen is null", screen);
		
		JFrame frame = screen.frmGameOfLife;
		assertNotNull("Frame is null", frame);
		
		JLabel iteration = screen.lblIteration;
		assertNotNull("Iteration label is null", iteration);
			
	}
	
	@Test
	public void checkDefaultXYTextFields() {
		StartScreen screen = new StartScreen();
		JTextField x = screen.x_text;
		assertEquals(x.getText(), "50");
		JTextField y = screen.y_text;
		assertEquals(y.getText(), "50");
	}
	
	
	@Test
	public void speedButtonInitial() {
		GridBrain brain = new GridBrain(); 
		assertEquals(brain.getSpeed(), 25);
	}
	@Test
	public void speedButtonChange() {
		GridBrain brain = new GridBrain(); 
		assertEquals(brain.getSpeed(), 25);
		brain.setSpeed(45);
		assertEquals(brain.getSpeed(), 45);

	}
	
	
}
