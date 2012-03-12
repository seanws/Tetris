/**
 * TetrisConstants.java
 * 
 * Created on 28-Oct-04
 */
package org.oversoul.tetris;

import java.awt.Cursor;

/**
 * Contains constants used in Tetris.
 * 
 * @author 		nyef
 * @date		28-Oct-04, 11:14:01 PM
 * @version 	1.0
 */
public class TetrisConstants {

	// game speeds
	public static final int SPEED_SLOW = 0;
	public static final int SPEED_NORM = 1;
	public static final int SPEED_FAST = 2;
	
	public static final int MIN_LEVEL = 0;
	public static final int MAX_LEVEL = 10;
	
	public static final int MIN_LINES = 0;
	public static final int MAX_LINES = 15;
	
	// Tetris Points
	public static final int BLOCK_LANDED = 5;
	public static final int ONE_LINE = 15;
	public static final int TWO_LINES = 35;
	public static final int THREE_LINES = 70;
	public static final int FOUR_LINES = 115;
	public static final int TETRIS_BONUS = 60;
	public static final int NO_BLOCKS_BONUS = 10;
	public static final int MAX_BLOCKS_BONUS = 40;

	public static final Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
	
}
