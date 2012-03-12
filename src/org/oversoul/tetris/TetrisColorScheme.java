/**
 * TetrisColorScheme.java
 * 
 * Created on 8-Nov-04
 */
package org.oversoul.tetris;

import org.oversoul.tetris.util.Configuration;

/**
 * Holds the name, block, and panel colors of a color scheme.
 * 
 * @author 		nyef
 * @date		8-Nov-04, 10:32:23 PM
 * @version 	1.0
 */
public class TetrisColorScheme {
	
	public static final String COLOR_SCHEME = "color.scheme.";
	public static final String COLOR_SCHEME_NAME = "color.scheme.name.";
	
	private String name = null;
	private TetrisColors blockColors = null;
	private TetrisPanel panelColors = null;

	/**
	 * Constructor for TetrisColorScheme.java
	 * @param name Color scheme name
	 */
	public TetrisColorScheme(String name) {
		this.name = name;
	}

	/**
	 * Constructor for TetrisColorScheme.java
	 * @param name Color scheme name
	 * @param blockColors block colors
	 * @param panelColors panel border colors
	 */
	public TetrisColorScheme(String name, TetrisColors blockColors, TetrisPanel panelColors) {
		this.name = name;
		this.blockColors = blockColors;
		this.panelColors = panelColors;
	}
	
	/**
	 * Loads a color scheme.
	 * @param config
	 * @return boolean If the Color scheme was loaded.
	 */
	public boolean loadColorScheme(Configuration config) {
		String key = COLOR_SCHEME + name + ".";
		if (config.getValue(COLOR_SCHEME_NAME + name) != null) {
			blockColors = new TetrisColors();
			blockColors.bg 		= config.getColor(key + blockColors.BOARD_BG, blockColors.bg);
			blockColors.text 	= config.getColor(key + blockColors.BOARD_TEXT, blockColors.text);
			blockColors.left 	= config.getColor(key + blockColors.FIG_LEFT, blockColors.left);
			blockColors.line 	= config.getColor(key + blockColors.FIG_LINE, blockColors.line);
			blockColors.right 	= config.getColor(key + blockColors.FIG_RIGHT, blockColors.right);
			blockColors.s 		= config.getColor(key + blockColors.FIG_S, blockColors.s);
			blockColors.square 	= config.getColor(key + blockColors.FIG_SQUARE, blockColors.square);
			blockColors.tri 	= config.getColor(key + blockColors.FIG_TRI, blockColors.tri);
			blockColors.z 		= config.getColor(key + blockColors.FIG_Z, blockColors.z);
			blockColors.flash 	= config.getColor(key + blockColors.FLASH, blockColors.flash);
			panelColors = new TetrisPanel();
			panelColors.setFirstColor(config.getColor(key + panelColors.FIRST, panelColors.getFirstColor()));
			panelColors.setSecondColor(config.getColor(key + panelColors.SECOND, panelColors.getSecondColor()));
			panelColors.setTitleColor(config.getColor(key + panelColors.TITLE, panelColors.getTitleColor()));
			panelColors.setLineColor(config.getColor(key + panelColors.LINE, panelColors.getLineColor()));
			panelColors.setDirection(Integer.parseInt(config.getValue(key + panelColors.DIRECTION, ""+panelColors.getDirection())));
			panelColors.setRoundedCorners("true".equals(config.getValue(key + panelColors.ROUNDED_CORNERS, ""+panelColors.isRoundedCorners())));
			return true;
		}
		return false;
	}
	
	/**
	 * Saves the current color scheme.
	 * @param config
	 */
	public void saveColorScheme(Configuration config) {
		String key = COLOR_SCHEME + name + ".";
		config.setValue(COLOR_SCHEME_NAME + name, name);
		if (blockColors != null) {
			config.setColor(key + blockColors.BOARD_BG, blockColors.bg);
			config.setColor(key + blockColors.BOARD_TEXT, blockColors.text);
			config.setColor(key + blockColors.FIG_LEFT, blockColors.left);
			config.setColor(key + blockColors.FIG_LINE, blockColors.line);
			config.setColor(key + blockColors.FIG_RIGHT, blockColors.right);
			config.setColor(key + blockColors.FIG_S, blockColors.s);
			config.setColor(key + blockColors.FIG_SQUARE, blockColors.square);
			config.setColor(key + blockColors.FIG_TRI, blockColors.tri);
			config.setColor(key + blockColors.FIG_Z, blockColors.z);
			config.setColor(key + blockColors.FLASH, blockColors.flash);
		}
		if (panelColors != null) {
			config.setColor(key + panelColors.FIRST, panelColors.getFirstColor());
			config.setColor(key + panelColors.SECOND, panelColors.getSecondColor());
			config.setColor(key + panelColors.TITLE, panelColors.getTitleColor());
			config.setColor(key + panelColors.LINE, panelColors.getLineColor());
			config.setValue(key + panelColors.DIRECTION, ""+panelColors.getDirection());
			config.setValue(key + panelColors.ROUNDED_CORNERS, ""+panelColors.isRoundedCorners());
		}
	}
	
	/**
	 * Deletes the color scheme and resets to the default.
	 * @param config
	 */
	public void removeColorScheme(Configuration config) {
		if (config.getValue(COLOR_SCHEME_NAME + name) != null)  {
			String key = COLOR_SCHEME + name + ".";
			config.remove(COLOR_SCHEME_NAME + name);
			config.removeKeys(key);
		}
		blockColors.reset();
		panelColors.reset();
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Gets the color scheme name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the color scheme name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the block colors.
	 * @return TetrisColors
	 */
	public TetrisColors getBlockColors() {
		return blockColors;
	}

	/**
	 * Sets the block colors.
	 * @param blockColors
	 */
	public void setBlockColors(TetrisColors blockColors) {
		this.blockColors = blockColors;
	}

	/**
	 * Gets the panel colors.
	 * @return TetrisPanel
	 */
	public TetrisPanel getPanelColors() {
		return panelColors;
	}

	/**
	 * Sets the panel colors.
	 * @param panelColors
	 */
	public void setPanelColors(TetrisPanel panelColors) {
		this.panelColors = panelColors;
	}

}
