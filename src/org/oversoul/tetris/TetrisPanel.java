/**
 * TetrisPanel.java
 * 
 * Created on 30-Sep-04
 */
package org.oversoul.tetris;

import java.awt.Color;

import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.util.Configuration;

/**
 * Stores information about the tetris panel gradient colors.
 * 
 * @author 		nyef
 * @date		30-Sep-04, 9:25:35 AM
 * @version 	1.0
 */
public class TetrisPanel implements Cloneable {

	public static final String EMPTY = "";
	public static final String TRUE = "true";
	
	public final String FIRST 			= "panel.first";
	public final String SECOND 			= "panel.second";
	public final String TITLE 			= "panel.title";
	public final String LINE 			= "panel.line";
	public final String DIRECTION 		= "panel.direction";
	public final String ROUNDED_CORNERS = "panel.roundedcorners";

	protected Color firstColor;
	protected Color secondColor;
	protected Color titleColor;
	protected Color lineColor;
	protected int direction;
	protected boolean roundedCorners;
	protected boolean needRepaint;

	/**
	 * Constructor for TetrisPanel.
	 */
	public TetrisPanel() {
		reset();
	}

	/**
	 * Constructor for TetrisPanel.
	 * @param firstColor
	 * @param secondColor
	 * @param titleColor
	 * @param lineColor
	 */
	public TetrisPanel(Color firstColor, Color secondColor, Color titleColor, Color lineColor) {
		this(firstColor, secondColor, titleColor, lineColor, PanelBorder.DIRECTION_TOP_BOTTOM, false);
	}

	/**
	 * Constructor for TetrisPanel.
	 * @param firstColor
	 * @param secondColor
	 * @param titleColor
	 * @param lineColor
	 * @param direction
	 * @param roundedCorners
	 */
	public TetrisPanel(Color firstColor, Color secondColor, Color titleColor, Color lineColor, int direction, boolean roundedCorners) {
		this.firstColor = firstColor;
		this.secondColor = secondColor;
		this.titleColor = titleColor;
		this.lineColor = lineColor;
		this.direction = direction;
		this.roundedCorners = roundedCorners;
	}


	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		TetrisPanel p = new TetrisPanel();
		p.setFirstColor(getFirstColor());
		p.setSecondColor(getSecondColor());
		p.setTitleColor(getTitleColor());
		p.setLineColor(getLineColor());
		p.setDirection(getDirection());
		p.setRoundedCorners(isRoundedCorners());
		return p;
	}

	/** 
	 * Loads the panel colors, gradient direction, and rounded corners.
	 * @param name player name
	 * @param config
	 */
	public void load(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			Color[] defs = TetrisDefaults.GRADIENT_BLUE;
			setFirstColor(config.getColor(name, FIRST, defs[0]));
			setSecondColor(config.getColor(name, SECOND, defs[1]));
			setTitleColor(config.getColor(name, TITLE, defs[2]));
			setLineColor(config.getColor(name, LINE, defs[3]));
			setDirection(Integer.parseInt(config.getValue(name, DIRECTION, EMPTY+PanelBorder.DIRECTION_TOP_BOTTOM)));
			setRoundedCorners(TRUE.equals(config.getValue(name, ROUNDED_CORNERS, "false")));
			setNeedRepaint(false);
		}
	}
	
	/** 
	 * Saves the panel colors, gradient direction, and rounded corners.
	 * @param name player name
	 * @param config
	 */
	public void save(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			config.setColor(name, FIRST, getFirstColor());
			config.setColor(name, SECOND, getSecondColor());
			config.setColor(name, TITLE, getTitleColor());
			config.setColor(name, LINE, getLineColor());
			config.setValue(name, DIRECTION, EMPTY+getDirection());
			config.setValue(name, ROUNDED_CORNERS, EMPTY+isRoundedCorners());
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Gets the first gradient color.
	 * @return Color
	 */
	public Color getFirstColor() {
		return firstColor;
	}

	/**
	 * Sets the first gradient color.
	 * @param color
	 */
	public void setFirstColor(Color color) {
		if (!firstColor.equals(color)) {
			needRepaint = true;
		}
		firstColor = color;
	}

	/**
	 * Gets the second gradient color.
	 * @return Color
	 */
	public Color getSecondColor() {
		return secondColor;
	}

	/**
	 * Sets the second gradient color.
	 * @param color
	 */
	public void setSecondColor(Color color) {
		if (!secondColor.equals(color)) {
			needRepaint = true;
		}
		secondColor = color;
	}

	/**
	 * Gets the title color.
	 * @return Color
	 */
	public Color getTitleColor() {
		return titleColor;
	}

	/**
	 * Sets the title color.
	 * @param color
	 */
	public void setTitleColor(Color color) {
		if(!titleColor.equals(color)) {
			needRepaint = true;
		}
		titleColor = color;
	}

	/**
	 * Gets the line color.
	 * @return Color
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * Sets the line color.
	 * @param color
	 */
	public void setLineColor(Color color) {
		if (!lineColor.equals(color)) {
			needRepaint = true;
		}
		lineColor = color;
	}

	/**
	 * Gets the direction of the gradient.
	 * @return int
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the gradient direction.
	 * @param direction
	 */
	public void setDirection(int direction) {
		if (direction != this.direction) {
			needRepaint = true;	
		}
		this.direction = direction;
	}

	/**
	 * Returns if the panel's corners are rounded.
	 * @return boolean
	 */
	public boolean isRoundedCorners() {
		return roundedCorners;
	}

	/**
	 * Sets if the panel's corners are rounded.
	 * @param roundedCorners
	 */
	public void setRoundedCorners(boolean roundedCorners) {
		if (roundedCorners != this.roundedCorners) {
			needRepaint = true;	
		}
		this.roundedCorners = roundedCorners;
	}

	/**
	 * If the panel colors have changed.
	 * @return boolean
	 */
	public boolean isNeedRepaint() {
		return needRepaint;
	}

	/**
	 * Need to repaint the border.
	 * @param needRepaint
	 */
	public void setNeedRepaint(boolean needRepaint) {
		this.needRepaint = needRepaint;
	}
	
	/**
	 * Gets a PanelBorder with the properties in TetrisPanel.
	 * @param title	the title of the panel.
	 * @return PanelBorder
	 */	
	public PanelBorder getPanelBorder(String title) {
		return getPanelBorder(title, null, null);	
	}

	/**
	 * Gets a PanelBorder with the properties in TetrisPanel.
	 * @param title	the title of the panel.
	 * @param color	the inner and outer colors for the border.
	 * @return PanelBorder
	 */	
	public PanelBorder getPanelBorder(String title, Color color) {
		return getPanelBorder(title, color, color);	
	}

	/**
	 * Gets a PanelBorder with the properties in TetrisPanel.
	 * @param title	the title of the panel.
	 * @param outerColor outer color
	 * @param innerColor inside color
	 * @return PanelBorder
	 */	
	public PanelBorder getPanelBorder(String title, Color outerColor, Color innerColor) {
		PanelBorder border = new PanelBorder(getLineColor(), 1, TetrisDefaults.PANEL_HEIGHT);
		border.setTitle(title, TetrisDefaults.FONT_PANEL, getTitleColor());
		border.setColors(getFirstColor(), getSecondColor());
		border.setRoundedCorners(isRoundedCorners());
		border.setGradientDirection(getDirection());
		if ((outerColor != null) && (innerColor != null)) {
			border.setBackground(outerColor, innerColor);
		}
		return border;	
	}	
	
	/**
	 * Sets the colors, direction, and rounded corners to the defaults.
	 */
	public void reset() {
		firstColor = TetrisDefaults.GRADIENT_BLUE[0];
		secondColor = TetrisDefaults.GRADIENT_BLUE[1];
		titleColor = TetrisDefaults.GRADIENT_BLUE[2];
		lineColor = TetrisDefaults.GRADIENT_BLUE[3];
		direction = PanelBorder.DIRECTION_TOP_BOTTOM;
		roundedCorners = false;
		needRepaint = false;		
	}

}
