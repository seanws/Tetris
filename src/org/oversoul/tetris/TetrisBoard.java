/**
 * TetrisBoard.java
 * 
 * February 11, 2004
 */

package org.oversoul.tetris;

import java.awt.Color;
import java.awt.Component;

import org.oversoul.tetris.ui.TetrisBoardComponent;

/**
 * A Tetris square board. The board is rectangular and contains a grid
 * of colored squares. The board is considered to be constrained to
 * both sides (left and right), and to the bottom. There is no 
 * constraint to the top of the board, although colors assigned to 
 * positions above the board are not saved.
 *
 * @author Chris Callendar (9902588)
 */
public class TetrisBoard extends Object {

	/** The board width (in squares).     */
	private int width = 0;

	/** The board height (in squares).     */
	private int height = 0;

	/** The tetris colors. */	
	private TetrisColors colors = null;

	/**
	 * The square board color matrix. This matrix (or grid) contains
	 * a color entry for each square in the board. The matrix is 
	 * indexed by the vertical, and then the horizontal coordinate.
	 */
	private Color[][] matrix = null;

	/**
	 * An optional board message. The board message can be set at any
	 * time, printing it on top of the board.
	 */
	private String message = null;

	/**
	 * The number of lines removed. This counter is increased each 
	 * time a line is removed from the board.
	 */
	private int removedLines = 0;
	
	/**
	 * The graphical sqare board component. This graphical 
	 * representation is created upon the first call to 
	 * getComponent().
	 */
	private TetrisBoardComponent component = null;
	
	/**
	 * Creates a new square board with the specified size. The square
	 * board will initially be empty.
	 * @param width     the width of the board (in squares)
	 * @param height    the height of the board (in squares)
	 * @param colors	the tetris colors.
	 */
	public TetrisBoard(int width, int height, TetrisColors colors) {
		this.width = width;
		this.height = height;
		this.colors = colors;
		this.matrix = new Color[height][width];
		clear();
	}

	/**
	 * Hides the cursor.
	 */
	public void hideCursor() {
		if (component != null) {
			component.hideCursor();	
		}
	}
	/**
	 * Shows the cursor.
	 */
	public void showCursor() {
		if (component != null) {
			component.showCursor();	
		}
	}

	/**
	 * Checks if a specified square is empty, i.e. if it is not 
	 * marked with a color. If the square is outside the board, 
	 * false will be returned in all cases except when the square is 
	 * directly above the board.
	 *
	 * @param x	the horizontal position (0 <= x < width)
	 * @param y the vertical position (0 <= y < height)
	 * @return true if the square is emtpy, or false otherwise
	 */
	public boolean isSquareEmpty(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return x >= 0 && x < width && y < 0;
		}
		return matrix[y][x] == null;
	}

	/**
	 * Checks if a specified line is empty, i.e. only contains 
	 * empty squares. If the line is outside the board, false will
	 * always be returned.
	 *
	 * @param y	the vertical position (0 <= y < height)
	 * @return true if the whole line is empty, or false otherwise
	 */
	public boolean isLineEmpty(int y) {
		if (y < 0 || y >= height) {
			return false;
		}
		for (int x = 0; x < width; x++) {
			if (matrix[y][x] != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a specified line is full, i.e. only contains no empty
	 * squares. If the line is outside the board, true will always be 
	 * returned.
	 * 
	 * @param y	the vertical position (0 <= y < height)
	 * @return true if the whole line is full, or false otherwise
	 */
	public boolean isLineFull(int y) {
		if (y < 0 || y >= height) {
			return true;
		}
		for (int x = 0; x < width; x++) {
			if (matrix[y][x] == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the board contains any full lines.
	 * @return true if there are full lines on the board, or false otherwise
	 */
	public boolean hasFullLines() {
		for (int y = height - 1; y >= 0; y--) {
			if (isLineFull(y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a graphical component to draw the board. The component 
	 * returned will automatically be updated when changes are made to
	 * this board. Multiple calls to this method will return the same
	 * component, as a square board can only have a single graphical
	 * representation.
	 * @return a graphical component that draws this board
	 */
	public Component getComponent() {
		if (component == null) {
			component = new TetrisBoardComponent(this, width, height);
		}
		return component;
	}

	/**
	 * Returns the board height (in squares). This method returns, 
	 * i.e, the number of vertical squares that fit on the board.
	 * @return the board height in squares
	 */
	public int getBoardHeight() {
		return height;
	}

	/**
	 * Returns the board width (in squares). This method returns, i.e,
	 * the number of horizontal squares that fit on the board.
	 * @return the board width in squares
	 */
	public int getBoardWidth() {
		return width;
	}

	/**
	 * Returns the number of lines removed since the last clear().
	 * @return the number of lines removed since the last clear call
	 */
	public int getRemovedLines() {
		return removedLines;
	}

	/**
	 * Returns the color of an individual square on the board. If the 
	 * square is empty or outside the board, null will be returned.
	 * @param x	the horizontal position (0 <= x < width)
	 * @param y	the vertical position (0 <= y < height)
	 * @return the square color, or null for none
	 */
	public Color getSquareColor(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return null;
		}
		return matrix[y][x];
	}

	/**
	 * Changes the color of an individual square on the board. The 
	 * square will be marked as in need of a repaint, but the 
	 * graphical component will NOT be repainted until the update() 
	 * method is called.
	 * @param x		the horizontal position (0 <= x < width)
	 * @param y		the vertical position (0 <= y < height)
	 * @param color	the new square color, or null for empty
	 */
	public void setSquareColor(int x, int y, Color color) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return;
		}
		matrix[y][x] = color;
		if (component != null) {
			component.invalidateSquare(x, y);
		}
	}

	/**
	 * Sets a message to display on the square board. This is supposed 
	 * to be used when the board is not being used for active drawing, 
	 * as it slows down the drawing considerably.
	 * @param message  a message to display, or null to remove a previous message
	 */
	public void setMessage(String message) {
		this.message = message;
		if (component != null) {
			component.redrawAll();
		}
	}

	/**
	 * Returns the message.
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Clears the board, i.e. removes all the colored squares. As 
	 * side-effects, the number of removed lines will be reset to 
	 * zero, and the component will be repainted immediately.
	 */
	public void clear() {
		removedLines = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.matrix[y][x] = null;
			}
		}
		if (component != null) {
			component.redrawAll();
		}
	}

	/**
	 * Removes all full lines. All lines above a removed line will be 
	 * moved downward one step, and a new empty line will be added at 
	 * the top. After removing all full lines, the component will be 
	 * repainted.
	 * @param level	The current level
	 * @see TetrisBoard#hasFullLines()
	 * @return int	the number of lines removed.
	 */
	public int removeFullLines(int level) {
		boolean repaint = false;

		//TODO add preference to turn flashing on/off?
		
		if (level <= 8) {
			// paint full lines a different color
			for (int y = height - 1; y >= 0; y--) {
				if (isLineFull(y)) {
					flashLine(y);
					repaint = true;
				}
			}
			// Repaint if necessary
			if (repaint && (component != null)) {
				component.redrawAll();
				int sleep = (10 - level) * 5;
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {}
			}
		}		
		repaint = false;		
		
		// Remove full lines
		int removed = 0;
		for (int y = height - 1; y >= 0; y--) {
			if (isLineFull(y)) {
				removeLine(y);
				repaint = true;
				removed++;
				y++;
			}
		}
		removedLines += removed;

		// Repaint if necessary
		if (repaint && (component != null)) {
			component.redrawAll();
		}
		
		return removed;
	}

	/**
	 * Momentarily changes the color of the line that is being removed.
	 * @param y	the row.
	 */
	private void flashLine(int y) {
		if ((y < 0) || (y >= height)) {
			return;
		}
		for (int x = 0; x < width; x++) {
			matrix[y][x] = colors.flash;
		}
	}

	/**
	 * Removes a single line. All lines above are moved down one step, 
	 * and a new empty line is added at the top. No repainting will be 
	 * done after removing the line.
	 * @param y	the vertical position (0 <= y < height)
	 */
	private void removeLine(int y) {
		if (y < 0 || y >= height) {
			return;
		}
		for (; y > 0; y--) {
			for (int x = 0; x < width; x++) {
				matrix[y][x] = matrix[y - 1][x];
			}
		}
		for (int x = 0; x < width; x++) {
			matrix[0][x] = null;
		}
	}

	/**
	 * Updates the graphical component. Any squares previously changed 
	 * will be repainted by this method.
	 */
	public void update() {
		if (component == null) {
			getComponent();	
		}
		component.redraw();
	}

	/**
	 * Calculates the weighted score.  The highest block is inverted since 
	 * the index starts from 0 at the top of the board.  The max value is 100.  
	 * @return int	weighted score.
	 */
	public int calculateWeightedScore() {
		int wScore = 0;
		int[] count = countBlocks();
		int blocks = count[0];
		int highest = count[1];
		double d = (highest * Math.PI) / ((double)height*2);
		d = Math.pow(Math.sin(d), 2) * 100;
		if (blocks < 100) {
			d = d * (blocks / 100.0);
		}
		wScore = (int) d;
		return wScore;
	}

	/**
	 * Adds incomplete lines to the bottom of the board.  
	 * @param linesToAdd	The number of lines to add.
	 */
	public void addStartingLines(int linesToAdd) {
		if ((linesToAdd > 0) && (linesToAdd < height)) {
			Color[][] lines = randomLines(linesToAdd);
			// add new incomplete lines
			for (int i = (height-linesToAdd); i < height; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] = lines[i-height+linesToAdd][j];
				}
			}
			if (component != null) {
				component.redrawAll();
			}
		}
	}

	/**
	 * Adds incomplete lines to the bottom of the board.  All the blocks
	 * are shifted up.
	 * @param linesToAdd	The number of lines to add (between 1 and height-1).
	 * @return boolean if successful, otherwise game over.
	 */
	public boolean addIncompleteLines(int linesToAdd) {
		if (linesToAdd <= 0) {
			return true;
		}
		boolean added = false;
		int[] count = countBlocks();
		int highest = count[1];
		if (linesToAdd <= (height-highest)) {
			Color[][] lines = randomLines(linesToAdd);
			// shift everything up
			for (int i = 0; i < (height-linesToAdd); i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] = matrix[i+linesToAdd][j];
				}
			}
			// add new incomplete lines
			for (int i = (height-linesToAdd); i < height; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] = lines[i-height+linesToAdd][j];
				}
			}		
			if (component != null) {
				component.redrawAll();
			}
			added = true;
		}
		return added;
	}

	/** 
	 * Counts the number of blocks and finds the highest blocks and
	 * returns them in an array.  The first number is the number of blocks,
	 * and the second is the highest block.
	 * @return int[] first is the number of blocks, second is highest block.
	 */
	public int[] countBlocks() {
		int blocks = 0;
		int highest = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (matrix[i][j] != null) {
					blocks++;
					int h = height - i;
					if (h > highest)
						highest = h;
				}
			}
		}
		return new int[]{blocks, highest};		
	}

	/**
	 * Generates a random incomplete line containing between 5 and 8 squares
	 * of random colors and in random order.
	 * @param num	the number of lines.
	 * @return Color[] one line.
	 */
	private Color[][] randomLines(int num) {
		Color[][] lines = new Color[num][width];	
		for (int i = 0; i < num; i++) {
			int fill = (int) (Math.random() * 4.0) + 5;	// # from 5 -> 8
			int colored = 0;		
			while (colored < fill) {
				int pos = (int)(Math.random() * 10);		// # from 0 -> 9
				BlockType[] types = BlockType.values();
				int index = (int)(Math.random() * types.length);
				BlockType type = types[index];
				Color c = getBlockColor(type);
				if (lines[i][pos] == null) {
					lines[i][pos] = c;
					colored++;
				}
			}
		}
		return lines;
	}

	/**
	 * Prints out the matrix using 0 for no block and 1 for block.
	 */
	protected void sprintBoard(boolean ignoreEmpty) {
		for (int i = 0; i < matrix.length; i++) {
			if (!ignoreEmpty || !isLineEmpty(i)) {
				System.out.print((i+1)+": ");
				for (int j = 0; j < matrix[i].length; j++) {
					String s = "0 ";
					if (matrix[i][j] != null)
						s = "1 ";
					System.out.print(s);
				}
				System.out.println();
			}
		}
		System.out.println();
	}

	/**
	 * Gets the tetris colors.
	 * @return TetrisColors
	 */
	public TetrisColors getColors() {
		return colors;
	}

	/**
	 * Sets the colors.
	 * @param colors
	 */
	public void setColors(TetrisColors colors) {
		this.colors = colors;
	}

	/**
	 * Gets the color for the figure type.
	 * @param type	The figure type.
	 * @return Color object or null if not defined.
	 */
	private Color getBlockColor(BlockType type) {
		switch (type) {
			case SQUARE :
				return colors.square;
			case LINE :
				return colors.line;
			case S :
				return colors.s;
			case Z :
				return colors.z;
			case RIGHT :
				return colors.right;
			case LEFT :
				return colors.left;
			case TRIANGLE :
				return colors.tri;
			default :
				System.err.println("No figure constant: " + type);
		}
		return null;
	}

}
