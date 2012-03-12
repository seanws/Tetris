/**
 * TetrisColors.java
 * 
 * Created on 22-Sep-04
 */
package org.oversoul.tetris;

import java.awt.Color;

import org.oversoul.tetris.util.Configuration;

/**
 * Holds the tetris game and block colors.
 * 
 * @author 		nyef
 * @date		22-Sep-04, 8:42:36 PM
 * @version 	1.0
 */
public class TetrisColors implements Cloneable {

	public Color bg 	= null;
	public Color text 	= null;
	public Color z 		= null;
	public Color s 		= null;
	public Color left	= null;
	public Color right	= null;
	public Color line	= null;
	public Color tri	= null;
	public Color square	= null;

	public Color flash  = null;

	public final String BOARD_BG 	= "board.background";
	public final String BOARD_TEXT	= "board.message";
	public final String FIG_LEFT 	= "figure.left";
	public final String FIG_LINE 	= "figure.line";
	public final String FIG_RIGHT 	= "figure.right";
	public final String FIG_S 		= "figure.s";
	public final String FIG_SQUARE 	= "figure.square";
	public final String FIG_TRI		= "figure.triangle";
	public final String FIG_Z 		= "figure.z";
	public final String FLASH		= "flash.line";	

	/**
	 * Constructor for TetrisColors.  Initializes the colors to the defaults.
	 */
	public TetrisColors() {
		reset();	
	}
	
	/**
	 * Gets the color for the given block type.
	 * @param type The block type
	 * @return the color
	 */
	public Color getColor(BlockType type) {
		switch (type) {
		case Z : 
			return z;
		case S :
			return s;
		case LEFT :
			return left;
		case RIGHT : 
			return right;
		case LINE :
			return line;
		case TRIANGLE :
			return tri;
		case SQUARE :
			return square;
		default :
			return null;
		}
	}
	
	/**
	 * Sets the color for the given type.
	 * @param type	the type of block
	 * @param c		the color to set
	 */
	public void setColor(BlockType type, Color c) {
		switch (type) {
		case Z : 
			z = c;
			break;
		case S :
			s = c;
			break;
		case LEFT :
			left = c;
			break;
		case RIGHT : 
			right = c;
			break;
		case LINE :
			line = c;
			break;
		case TRIANGLE :
			tri = c;
			break;
		case SQUARE :
			square = c;
			break;
		}		
	}
	
	/**
	 * Sets the current colors to be the defaults.
	 */
	public void reset() {
		bg = TetrisDefaults.BGColor;
		text = TetrisDefaults.TEXTColor;
		z = TetrisDefaults.ZColor;
		s = TetrisDefaults.SColor;
		left = TetrisDefaults.LEFTColor;
		right = TetrisDefaults.RIGHTColor;
		line = TetrisDefaults.LINEColor;
		tri = TetrisDefaults.TRIANGLEColor;
		square = TetrisDefaults.SQUAREColor;
		flash = TetrisDefaults.FLASHColor;
	}
	
	/** 
	 * Clones this color scheme. 
	 * @return Object TetrisColor
	 */
	@Override
	public Object clone() {
		TetrisColors c = new TetrisColors();
		c.bg = new Color(bg.getRGB());
		c.text = new Color(text.getRGB());
		c.z = new Color(z.getRGB());
		c.s = new Color(s.getRGB());
		c.left = new Color(left.getRGB());
		c.right = new Color(right.getRGB());
		c.line = new Color(line.getRGB());
		c.tri = new Color(tri.getRGB());
		c.square = new Color(square.getRGB());
		c.flash = new Color(flash.getRGB());
		return c;
	}
	
	/**
	 * Loads the colors.
	 * @param name player name
	 * @param config
	 */
	public void load(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			bg = config.getColor(name, BOARD_BG, TetrisDefaults.BGColor);
			text = config.getColor(name, BOARD_TEXT, TetrisDefaults.TEXTColor);
			left = config.getColor(name, FIG_LEFT, TetrisDefaults.LEFTColor);
			line = config.getColor(name, FIG_LINE, TetrisDefaults.LINEColor);
			right = config.getColor(name, FIG_RIGHT, TetrisDefaults.RIGHTColor);
			s = config.getColor(name, FIG_S, TetrisDefaults.SColor);
			square = config.getColor(name, FIG_SQUARE, TetrisDefaults.SQUAREColor);
			tri = config.getColor(name, FIG_TRI, TetrisDefaults.TRIANGLEColor);
			z = config.getColor(name, FIG_Z, TetrisDefaults.ZColor);
			flash = config.getColor(name, FLASH, TetrisDefaults.FLASHColor);
		}
	}

	/**
	 * Saves the colors.
	 * @param name player name
	 * @param config
	 */
	public void save(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			config.setColor(name, BOARD_BG, bg);
			config.setColor(name, BOARD_TEXT, text);
			config.setColor(name, FIG_LEFT, left);
			config.setColor(name, FIG_LINE, line);
			config.setColor(name, FIG_RIGHT, right);
			config.setColor(name, FIG_S, s);
			config.setColor(name, FIG_SQUARE, square);
			config.setColor(name, FIG_TRI, tri);
			config.setColor(name, FIG_Z, z);
			config.setColor(name, FLASH, flash);
		}
	}
	
}
