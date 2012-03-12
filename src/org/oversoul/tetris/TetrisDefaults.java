/**
 * DefaultColors.java
 * 
 * Created on 21-Feb-04
 */
package org.oversoul.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 * Tetris defaults.
 * 
 * @author Chris Callendar (9902588)
 * @date   21-Feb-04, 11:36:27 PM
 */
public class TetrisDefaults {

	// if debug mode
	public static final boolean DEBUG = true;
	
	// Multiplayer defaults
	public static final int DEFAULT_PORT = 1357;
	
	// default fonts
	public static final Font FONT_PANEL = new Font("Arial", Font.BOLD, 12);
	public static final Font FONT_PLAIN = new Font("Arial", Font.PLAIN, 12);
	public static final Font FONT_14 = new Font("Arial", Font.BOLD, 14);
	public static final Font FONT_MENU_BOLD = new Font("Tahoma", Font.BOLD, 11);
	public static final Font FONT_MENU_PLAIN = new Font("Tahoma", Font.PLAIN, 11);
	public static final Font FONT_LABEL_BOLD = new Font("Arial", Font.BOLD, 11);
	public static final Font FONT_LABEL_SMALL = new Font("Arial", Font.PLAIN, 11);


	public static final int PANEL_HEIGHT = 20;
	public static final Color DEFAULT_BACKGROUND = new Color(224, 223, 227);

	// default colors
	public static final Color BGColor 		= new Color(0, 0, 0);
	public static final Color TEXTColor		= new Color(255, 255, 255);
	public static final Color ZColor 		= new Color(0, 173, 0);
	public static final Color SColor 		= new Color(91, 95, 255);
	public static final Color LEFTColor 	= new Color(173, 173, 8);
	public static final Color RIGHTColor 	= new Color(173, 0, 173);
	public static final Color LINEColor 	= new Color(173, 0, 0);
	public static final Color TRIANGLEColor = new Color(235, 141, 49);
	public static final Color SQUAREColor 	= new Color(0, 173, 173);
	public static final Color FLASHColor	= new Color(204, 156, 156);
	
	// default color strings
	public static final String BG 			= "#000000";
	public static final String TEXT			= "#FFFFFF";
	public static final String Z 			= "#00AD00";
	public static final String S 			= "#5B5FFF";
	public static final String LEFT 		= "#ADAD08";
	public static final String RIGHT 		= "#AD00AD";
	public static final String LINE 		= "#AD0000";
	public static final String TRIANGLE	 	= "#EB8D31";
	public static final String SQUARE 		= "#00ADAD";
	
	// default controls
	public static final int MOVE_LEFT 	= KeyEvent.VK_LEFT;
	public static final int MOVE_RIGHT 	= KeyEvent.VK_RIGHT;
	public static final int ROTATE 		= KeyEvent.VK_UP;
	public static final int DOWN		= KeyEvent.VK_DOWN;
	public static final int DROP		= KeyEvent.VK_SPACE;
	public static final int LEVELUP		= KeyEvent.VK_S;
	public static final int PAUSE		= KeyEvent.VK_P;
	public static final int PREVIEW		= KeyEvent.VK_N;
	
	public static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	// gradient colors (first, second, text, line)
	// hue=0, saturation=255
	public static final Color[] GRADIENT_RED = new Color[] { new Color(255, 219, 219), 
															 new Color(255, 71, 71),
															 new Color(190, 0, 0),
															 new Color(232, 0, 0) };
															 
	public static final Color[] GRADIENT_BLUE = new Color[] { new Color(221, 232, 252), 
															  new Color(121, 150, 205), 
															  new Color(55, 84, 135), 
															  new Color(99, 99, 132) };
	// hue=80, saturation=150
	public static final Color[] GRADIENT_GREEN = new Color[] { new Color(226, 249, 229),
															   new Color(108, 228, 121),
															   new Color(53, 150, 40),
															   new Color(64, 184, 48) };
	// hue=20, saturation=255
	public static final Color[] GRADIENT_ORANGE = new Color[] { new Color(255, 236, 219),
																new Color(255, 158, 71),
																new Color(190, 89, 0),
																new Color(232, 109, 0) };

																
}
