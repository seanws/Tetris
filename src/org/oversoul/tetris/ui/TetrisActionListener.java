/**
 * TetrisListener.java
 * 
 * Created on 10-Mar-04
 */
package org.oversoul.tetris.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.oversoul.tetris.Actions;
import org.oversoul.tetris.IDebugger;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisController;
import org.oversoul.tetris.multiplayer.IMultiplayerController;
import org.oversoul.tetris.util.Util;
import org.oversoul.util.Version;

/**
 * A listener class for catching menu, key, and window events.
 * 
 * @author Chris Callendar (9902588)
 * @date 10-Mar-04, 2:35:58 PM
 */
public class TetrisActionListener implements ActionListener, MenuListener,
		KeyListener, WindowFocusListener, WindowListener, ComponentListener,
		ChangeListener {

	private IUIController controller = null;

	private Component component = null;

	private TetrisRootPane ui = null;
	
	/**
	 * Constructor for TetrisListener.
	 */
	public TetrisActionListener(IUIController controller,
			Component component, TetrisRootPane ui) {
		this.controller = controller;
		this.component = component;
		this.ui = ui;
	}

	// //////////////////////////////////////////////////////////////////////
	// ActionListener Methods
	// //////////////////////////////////////////////////////////////////////

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		Object src = e.getSource();
		String name = "";
		if (src instanceof Component) {
			Component comp = (Component) src;
			name = comp.getName();
		}

		boolean focus = true;
		Actions action = Actions.valueOf(cmd);
		switch (action) {
		case START :
			controller.doStartGame();	// no multiplayer 
			break;
		case PAUSE :
			controller.sendGamePause();
			break;
		case RESUME :
			controller.sendGameResume();
			break;
		case RESTART :
			controller.sendGameRestart();
			break;
		case END :
			controller.sendGameEnd();
			break;
		case EXIT :
			controller.dispose();
			break;
		case FILE_IMPORT :
			ui.displayImportChooser(controller);
			break;
		case HIGHSCORES :
			ui.displayHighscores(controller.getHighscores(), 
					controller.getPlayer().getGameSpeed(), 0);
			break;
		case MULTIPLAYER_CREATE :
			controller.sendGamePause();
			ui.createGame(controller.getCommunication(), (IMultiplayerController)controller, controller);
			focus = false;
			break;
		case MULTIPLAYER_JOIN :
			controller.sendGamePause();
			ui.joinGame(controller.getCommunication(), (IMultiplayerController)controller, controller);
			focus = false;
			break;
		case CHANGE_PLAYER:
			controller.sendGamePause();
			ui.displayPlayerChangeDialog(controller);
			break;
		case SOUNDS:
			ui.displayAudioDialog(controller);
			break;
		case CONTROLS:
			ui.displayControlsDialog(controller);
			break;
		case COLORS:
			ui.displayColorsFrame(controller);
			focus = false;
			break;
		case BLOCK_DISTRIBUTION:
			ui.displayBlockDistributionDialog(controller.getPlayer().getColors(), 
					controller.getBlockCounts());
			break;
		case LOOKANDFEEL:
			controller.setLookAndFeel(name, component);
			break;
		case STARTING_LEVEL:
			int startingLevel = Util.parseInt(name, TetrisConstants.MIN_LEVEL);
			controller.setStartingLevel(startingLevel);
			ui.setStartingLevel(startingLevel);
			break;
		case STARTING_LINES:
			int startingLines = Util.parseInt(name, TetrisConstants.MIN_LINES);
			controller.setStartingLines(startingLines);
			ui.setStartingLines(startingLines);
			break;
		case GAME_SPEED:
			int gameSpeed = Util.getGameSpeed(name);
			controller.setGameSpeed(gameSpeed);
			ui.setGameSpeed(gameSpeed);
			break;
		case SCORING:
			ui.displayScoringDialog();
			break;
		case ABOUT:
			ui.displayAboutDialog(controller.getVersion().toString(), 
					controller.getVersion().getDateString());
			break;
		case DEBUG_LOAD_VERSION :
			debugLoadVersion();
			break;
		case DEBUG_LOAD_PROPERTIES :
			debugLoadProperties();
			break;
		case DEBUG_SAVE_PROPERTIES :
			debugSaveProperties();
			break;
		case DEBUG_LOAD_HIGHSCORES :
			debugLoadHighscores();
			break;
		case DEBUG_SAVE_HIGHSCORES :
			debugSaveHighscores();
			break;
		}
		if (focus) {
			ui.requestFocus();
		}
	}

	// //////////////////////////////////////////////////////////////////////
	// ChangeListener Methods
	// //////////////////////////////////////////////////////////////////////

	private void debugLoadVersion() {
		Debugger debugger = new Debugger();
		Version version = ((TetrisController)controller).getIO().loadVersion(debugger);
		String msg = "Version: v" + version.toString() + ", Date: " + version.getDateString();
		msg += "\n" + debugger.toString();
		JOptionPane.showMessageDialog(component, msg, "Version", JOptionPane.INFORMATION_MESSAGE);
		debugger.clear();
	}

	private void debugLoadProperties() {
		Debugger debugger = new Debugger();
		((TetrisController)controller).getIO().loadProperties(debugger);
		String msg = debugger.toString();
		JOptionPane.showMessageDialog(component, msg, "Load Properties", JOptionPane.INFORMATION_MESSAGE);
		debugger.clear();
	}
	
	private void debugSaveProperties() {
		Debugger debugger = new Debugger();
		((TetrisController)controller).getIO().saveProperties(debugger, controller.getConfiguration());
		String msg = debugger.toString();
		JOptionPane.showMessageDialog(component, msg, "Save Properties", JOptionPane.INFORMATION_MESSAGE);
		debugger.clear();
	}

	private void debugLoadHighscores() {
		Debugger debugger = new Debugger();
		((TetrisController)controller).getIO().loadHighscores(debugger);
		String msg = debugger.toString();
		JOptionPane.showMessageDialog(component, msg, "Load Highscores", JOptionPane.INFORMATION_MESSAGE);
		debugger.clear();
	}

	private void debugSaveHighscores() {
		Debugger debugger = new Debugger();
		controller.getHighscores().setChanged(true);
		((TetrisController)controller).getIO().saveHighscores(debugger, controller.getHighscores());
		String msg = debugger.toString();
		JOptionPane.showMessageDialog(component, msg, "Save Highscores", JOptionPane.INFORMATION_MESSAGE);
		debugger.clear();
	}

	/**
	 * Game speed slider change event listener.
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			String name = source.getName();
			if (Actions.GAME_SPEED.name().equals(name)) {
				int gameSpeed = source.getValue();
				controller.setGameSpeed(gameSpeed);
				ui.setGameSpeed(gameSpeed);
			} else if (Actions.STARTING_LEVEL.name().equals(name)) {
				int level = source.getValue();
				controller.setStartingLevel(level);
				ui.setStartingLevel(level);
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////
	// MenuListener Methods
	// //////////////////////////////////////////////////////////////////////

	/** Not implemented. */
	public void menuCanceled(MenuEvent e) {
	}

	/** Not implemented. */
	public void menuDeselected(MenuEvent e) {
	}

	/**
	 * @see javax.swing.event.MenuListener#menuSelected(MenuEvent)
	 */
	public void menuSelected(MenuEvent e) {
		controller.sendGamePause();
	}

	// //////////////////////////////////////////////////////////////////////
	// KeyListener Methods
	// //////////////////////////////////////////////////////////////////////

	/**
	 * Forwards the key event on to the controller.
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		// Workaround for when Enter or Space is pressed and a button is focused
		if ((code == KeyEvent.VK_ENTER) || (code == KeyEvent.VK_SPACE)) {
			if (controller.getPlayer().getControls().containsKeyCode(code)) {
				controller.handleKeyEvent(e);
				e.consume();
			}
		}
		//check for CTRL-# keys which change the starting level
		else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
			if ((code >= KeyEvent.VK_0) && (code <= KeyEvent.VK_9)) {
				int level = code - KeyEvent.VK_0;
				if (level == 0) {
					level = 10;	
				}
				ui.setStartingLevel(level);
			} else if ((code == KeyEvent.VK_PLUS) || (code == KeyEvent.VK_EQUALS)) {
				ui.setGameSpeed(controller.getPlayer().getGameSpeed()+1);
			} else if (code == KeyEvent.VK_MINUS) {
				ui.setGameSpeed(controller.getPlayer().getGameSpeed()-1);
			}
		}
	}

	/** Not implemented. */
	public void keyReleased(KeyEvent e) {
	}

	/** Not implemented. */
	public void keyTyped(KeyEvent e) {
	}

	// //////////////////////////////////////////////////////////////////////
	// WindowFocusListener Methods
	// //////////////////////////////////////////////////////////////////////

	/**
	 * @see java.awt.event.WindowFocusListener#windowGainedFocus(WindowEvent)
	 */
	public void windowGainedFocus(WindowEvent e) {
		ui.showBlockDistributionDialog();
	}

	/**
	 * @see java.awt.event.WindowFocusListener#windowLostFocus(WindowEvent)
	 */
	public void windowLostFocus(WindowEvent e) {
		controller.sendGamePause();
	}

	// //////////////////////////////////////////////////////////////////////
	// WindowListener Methods
	// //////////////////////////////////////////////////////////////////////

	/** Not implemented. */
	public void windowActivated(WindowEvent e) {
	}

	/** Not implemented. */
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosing(WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		controller.dispose();
	}

	/** Not implemented. */
	public void windowDeactivated(WindowEvent e) {
	}

	/** Not implemented. */
	public void windowDeiconified(WindowEvent e) {
	}

	/** Not implemented. */
	public void windowIconified(WindowEvent e) {
	}

	/** Not implemented. */
	public void windowOpened(WindowEvent e) {
	}

	// //////////////////////////////////////////////////////////////////////
	// ComponentListener Methods
	// //////////////////////////////////////////////////////////////////////

	/**
	 * Keeps the block distribution dialog docked to the side of the frame.
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		if (ui != null) {
			ui.updateBlockDistributionDialogLocation();
		}
	}

	/** Not implemented. */
	public void componentHidden(ComponentEvent e) {
	}

	/** Not implemented. */
	public void componentResized(ComponentEvent e) {
	}

	/** Not implemented. */
	public void componentShown(ComponentEvent e) {
	}

	class Debugger implements IDebugger {
		private StringBuffer buffer = new StringBuffer();

		public void debug(Exception e) {
			debug(e.getMessage());
		}
		
		public void debug(String msg) {
			buffer.append(msg + "\n");
		}
		
		public void debug(String msg, Exception e) {
			debug(msg + " " + e.getMessage());
		}
		
		@Override
		public String toString() {
			return buffer.toString();
		}
		
		public void clear() {
			buffer = new StringBuffer();
		}
		
	}
}
