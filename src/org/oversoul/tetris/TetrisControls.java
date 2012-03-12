/**
 * TetrisControls.java
 * 
 * Created on 22-Sep-04
 */
package org.oversoul.tetris;

import org.oversoul.tetris.util.Configuration;

/**
 * Stores the tetris player's game controls. 
 * 
 * @author 		nyef
 * @date		22-Sep-04, 8:41:21 PM
 * @version 	1.0
 */
public class TetrisControls implements Cloneable {

	public int left		= TetrisDefaults.MOVE_LEFT;
	public int right 	= TetrisDefaults.MOVE_RIGHT;
	public int down		= TetrisDefaults.DOWN;
	public int drop		= TetrisDefaults.DROP;
	public int rotate  	= TetrisDefaults.ROTATE;
	public int pause	= TetrisDefaults.PAUSE;
	public int preview	= TetrisDefaults.PREVIEW;
	public int levelUp	= TetrisDefaults.LEVELUP;
	
	public final String KEY_LEFT 	= "left";
	public final String KEY_RIGHT 	= "right";
	public final String KEY_DOWN 	= "down";
	public final String KEY_DROP	= "drop";
	public final String KEY_ROTATE 	= "rotate";
	public final String KEY_PAUSE 	= "pause";
	public final String KEY_PREVIEW = "preview";
	public final String KEY_LEVELUP	= "levelup";


	/**
	 * Constructor for TetrisControls.
	 */
	public TetrisControls() {
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()  {
		TetrisControls c = new TetrisControls();
		c.left = left;
		c.right = right;
		c.down = down;
		c.drop = drop;
		c.rotate = rotate;
		c.pause = pause;
		c.preview = preview;
		c.levelUp = levelUp;
		return c;
	}
	
	/**
	 * Loads the game control keys.
	 * @param name player name
	 * @param config
	 */
	public void load(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			left = config.getControl(name, KEY_LEFT, left);
			right = config.getControl(name, KEY_RIGHT, right);	
			down = config.getControl(name, KEY_DOWN, down);	
			drop = config.getControl(name, KEY_DROP, drop);	
			rotate = config.getControl(name, KEY_ROTATE, rotate);	
			pause = config.getControl(name, KEY_PAUSE, pause);	
			preview = config.getControl(name, KEY_PREVIEW, preview);	
			levelUp = config.getControl(name, KEY_LEVELUP, levelUp);
		}
	}
	
	/**
	 * Saves the game control keys.
	 * @param name player name
	 * @param config
	 */
	public void save(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			config.setControl(name, KEY_LEFT, left);	
			config.setControl(name, KEY_RIGHT, right);	
			config.setControl(name, KEY_DOWN, down);	
			config.setControl(name, KEY_DROP, drop);	
			config.setControl(name, KEY_ROTATE, rotate);	
			config.setControl(name, KEY_PAUSE, pause);	
			config.setControl(name, KEY_PREVIEW, preview);	
			config.setControl(name, KEY_LEVELUP, levelUp);
		}
	}

	/**
	 * Checks if the given key code is one of the 8 currently
	 * mapped controls.
	 * @param code the key code to check
	 * @return boolean
	 */
	public boolean containsKeyCode(int code) {
		if ((code == left) || (code == right) || 
			(code == rotate) || (code == drop) || 
			(code == down) || (code == pause) || 
			(code == levelUp) || (code == preview)) {
			return true;			
		}
		return false;
	}

}
