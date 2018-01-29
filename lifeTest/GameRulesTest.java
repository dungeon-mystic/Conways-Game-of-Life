package lifeTest;
import lifegame.Cell;
import lifegame.GridBrain;
import static org.junit.Assert.*;

import javax.swing.JButton;

import org.junit.Test;
/**
 * GameRulesTest
 * 
 * Tests the rules (game dynamics) for the lifegame.
 * 
 * @author Stirling Joyner
 * Software Development, Team 2
 * March 16, 2016
 */
public class GameRulesTest {

	/**
	 * Fills the given grid with dead cells
	 * @param grid
	 */
	private void populate(Cell[][] grid){
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++) {
				Cell cell =  new Cell(new JButton()); // create the test cell
				grid[i][j] = cell; // place the cell in the grid
			}
	}
	/**
	 * Tests that a single cell does what its supposed to. 
	 */
	@Test
	public void singleCellLifeTest() {
		/* ********* set up *********** */
		Cell cell = new Cell(new JButton());
		/* ************* test that the cell begins its life dead. ***************/
		assertFalse("cell is initially alive, should start play dead.", cell.isAlive());
		
		/* ******** test that a cell can be killed or brought to life. **********/
		cell.kill();
		assertFalse("Cell.kill() should have killed this cell.", cell.isAlive());
		cell.makeAlive();
		assertTrue("Cell.makeAlive() should have lived this cell, but it is dead.", cell.isAlive());
		cell.toggleLife();
		assertFalse("Cell.toggleLife() should have killed this cell.", cell.isAlive());
		cell.toggleLife();
		assertTrue("Cell.toggleLife() should have lived this cell, but it is dead.", cell.isAlive());
	}
	

	/**
	 * Test that a single live cell will die if it has fewer than 2 neighbors.
	 */
	@Test 
	public void LiveCellDiesWhenLonely(){
		Cell testCell = new Cell(new JButton());
		testCell.makeAlive();
		assertFalse("A live cell is supposed to die when it has 0 neighbors.", testCell.act(0)); //test with 0 neighbors
		assertFalse("A live cell is supposed to die when it has 1 neighbor.", testCell.act(1)); //test with 1 neighbors
	}
	
	/**
	 * Test that a single live cell will remain alive if it has two or three live neighbors.
	 */
	@Test
	public void LiveCellLivesWithFriends() {
		Cell testCell = new Cell(new JButton());
		testCell.makeAlive();
		// test with 2 live neighbors
		assertTrue("A live cell is supposed to live when it has 2 live neighbors.", testCell.act(2));
		// test with 3 live neighbors
		assertTrue("A live cell is supposed to live when it has 3 live neighbors.", testCell.act(3));
	}
	
	/**
	 * Test that a living cell can die of over-population.
	 */
	@Test
	public void LiveCellDiesOfCrowding() {
		Cell testCell = new Cell(new JButton());
		testCell.makeAlive();
		for(int i = 4; i < 9; i++) {
			assertFalse("Cell should die with 4+ live neighbors. numNeighbors: " + i, testCell.act(i));
		}
	}
	
	/**
	 * Test to see if neighbors just beyond reach are factoring into neighbor count 
	 */
	
	// test corner test middle test 
	// test middle on a 3X3
	@Test 
	public void NeighborsWorksJustRightTest(){
		Cell[][] grid = new Cell[5][5]; // create a test grid of cells to test.
		populate(grid); // fill the grid with dead cells.
		GridBrain gridBrain = new GridBrain(grid, null); 
		grid[2][2].makeAlive();
		grid[1][2].makeAlive();
		grid[2][1].makeAlive();

		assertTrue("Cell should have 2 neighbors ",gridBrain.neighborCount(2, 2)==2); 
		
		
	}
	/**
	 * Test to see if a cell on the edge can properly count its neighbors. 
	 */
	@Test 
	public void NeighborEdgeTest(){
		Cell[][] grid = new Cell[5][5]; // create a test grid of cells to test.
		populate(grid); // fill the grid with dead cells.
		GridBrain gridBrain = new GridBrain(grid, null); 
		grid[3][1].makeAlive();
		grid[2][0].makeAlive();
		
		assertTrue("Cell should have one live neighbor on the edge",gridBrain.neighborCount(3, 1)==1); 
		
	}
	
	/**
	 *Test to see if a cell in the middle of the array counts as neighbors cells just one beyond.  
	 */
	@Test
	public void NeighborsJustTooFarAwayTest(){
		Cell[][] grid = new Cell[5][5]; // create a test grid of cells to test.
		populate(grid); // fill the grid with dead cells.
		GridBrain gridBrain = new GridBrain(grid, null); 
		grid[2][2].makeAlive();
		grid[2][0].makeAlive();
		grid[4][2].makeAlive();
		grid[2][4].makeAlive();

		assertTrue("Cell should have 0 live neighbor as Cells should be just out of reach",gridBrain.neighborCount(2, 2)==0); 

		
	}
	
	
	/**
	 * Test that a dead cell will become alive if there are three live neighbors.
	 */
	@Test
	public void DeadCellResurrectsWithThreeNeighbors() {
		Cell testCell = new Cell(new JButton());
		assertFalse("A cell should be initialized to dead.", testCell.isAlive());
		assertTrue("A dead cell is supposed to live when it has 3 live neighbors.", testCell.act(3));
	}
}
