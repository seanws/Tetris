/**
 * WindowClosingDisposer.java
 * 
 * Created on 25-Oct-2005
 */
package org.oversoul.tetris.ui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Overrides the windowClosing method and calls the dispose
 * method of the given Window.
 * 
 * @author 		ccallendar
 * @date		25-Oct-2005, 10:01:54 AM
 * @version 	1.0
 */
public class WindowClosingDisposer extends WindowAdapter {

	private Window window = null;
	
	public WindowClosingDisposer(Window window) {
		super();
		this.window = window;
	}
	
	/**
	 * Calls the dispose method of the Window.
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		if (window != null) {
			window.dispose();
		}
	}

}
