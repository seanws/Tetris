/**
 * GameFrame.java
 * 
 * Created on 12-Feb-04
 */
package org.oversoul.tetris;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.oversoul.tetris.highscores.Highscores;
import org.oversoul.tetris.multiplayer.IMultiplayerController;
import org.oversoul.tetris.multiplayer.TetrisCommunication;
import org.oversoul.tetris.ui.TetrisRootPane;
import org.oversoul.tetris.util.Configuration;
import org.oversoul.util.Version;

/**
 * The Main Swing UI for Tetris.
 * 
 * @author Chris Callendar (9902588)
 * @date   12-Feb-04, 7:51:31 PM
 */
public class TetrisController implements IMultiplayerController, IGameController, IUIController, IDebugger {

	/** The Input/Output class for loading and saving property files, highscores, version. */
	private TetrisIO io = null;

	/** The Game object that handles the gameplay. */
	private Game game = null;
	
	/** The main UI component. */
	private TetrisRootPane ui = null;
	
	/** The component - either a JFrame or a JApplet. */
	private Component component = null;
	
	/** The default tetris player. */
	private TetrisPlayer player = null;
	
	/** The default opponent player. */
	private TetrisPlayer opponent = null;
	
	/** Tetris Configuration. */
	private Configuration config = null;
	
	/** The Tetris version. */
	private Version version = null;
	
	/** The tetris highscores. */
	private Highscores highscores = null;

	/** The NetworkApplcation communications object. */
	private TetrisCommunication communication = null;

	private boolean multiplayer = false;
	
	//private boolean appletMode = false;
		
	/**
	 * Constructor for the Tetris controller.
	 * @param component the Component (JFrame or Applet)
	 */
	public TetrisController(Component component, TetrisRootPane ui, File workingDir) {
		super();
		this.component = component;
		this.ui = ui;
		this.io = new TetrisIO(workingDir);
		this.player = new TetrisPlayer("");
		this.opponent = new TetrisPlayer("Opponent");
		//this.appletMode = ui.isAppletMode();
		
		this.version = io.loadVersion(this);
		this.config = io.loadProperties(this);
		this.highscores = io.loadHighscores(this);

		loadPlayer();
		this.game = new Game(this, highscores);
		
		initializeActions(player.getControls());
	}

	
	public void debug(String msg) {
		if (TetrisDefaults.DEBUG) {
			System.out.println(msg);
		}
	}
	
	public void debug(Exception e) {
		debug("** Exception: ", e);
	}

	public void debug(String prefix, Exception e) {
		debug(prefix + e.getMessage());
	}
	
	protected void error(Exception e) {
		error("Exception: ", e);
	}

	protected void error(String text, Exception e) {
		if (ui != null) {
			ui.error(text, e);
		}
	}
	
	protected void message(String msg) {
		if (ui != null) {
			ui.message(msg);
		}
	}
	
	public void dispose() {
		//TODO this is done in MultiplayerFrame.dispose() ?
		if (communication != null) {
			communication.sendQuit(player.getPlayerNumber());
			communication.shutdown();
		}
		if (game != null) {
			game.quit();
			game = null;
		}

		player.setShowBlockDistribtion(ui.isBlockDistributionDialogVisible());
		player.setLocation(ui.getTopLeft());
		player.save(config);
		io.saveProperties(this, config);
		if (ui != null) {
			ui.dispose();
			ui = null;
		}
		if (component instanceof Frame) {
			((Frame)component).dispose();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// INITIALIZE METHODS
	//////////////////////////////////////////////////////////////////////

	/**
	 * Loads the current player from the configuration.
	 * Also updates the look and feel if necessary, and sets 
	 * the player's TetrisPanel in the ui.
	 */
	private void loadPlayer() {
		String lnf = UIManager.getLookAndFeel().getName();
		player.load(config);
		ui.setTetrisPanel(player.getPanel());
		if (!player.getLookAndFeel().equals(lnf)) {
			try {
				setLookAndFeel(player.getLookAndFeel(), component);
			} catch (Exception e) {
				error(e);
			}
		}
	}

	private void initializeActions(TetrisControls controls) {
		registerAction(controls.down, controls.KEY_DOWN);
		registerAction(controls.drop, controls.KEY_DROP);
		registerAction(controls.left, controls.KEY_LEFT);
		registerAction(controls.levelUp, controls.KEY_LEVELUP);
		registerAction(controls.pause, controls.KEY_PAUSE);
		registerAction(controls.preview, controls.KEY_PREVIEW);
		registerAction(controls.right, controls.KEY_RIGHT);
		registerAction(controls.rotate, controls.KEY_ROTATE);
	}

	private void registerAction(final int keyCode, String name) {
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);
		Action action = new AbstractAction(name) {
			private static final long serialVersionUID = 1;
			public void actionPerformed(ActionEvent e) {
				Component comp = (e.getSource() instanceof Component ? (Component)e.getSource() : null);
				KeyEvent ke = new KeyEvent(comp, e.getID(), e.getWhen(), e.getModifiers(), keyCode, '!');
				game.handleKeyEvent(ke);
			};
		};
		ui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
		ui.getActionMap().put(name, action);
	}
	
	//////////////////////////////////////////////////////////////////////
	// IGameController Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * @see org.oversoul.tetris.IGameController#handleGameStart()
	 */
	public void handleGameStart() {
		sendMultiplayerStart();
		doStartGame();
	}

	/**
	 * @see org.oversoul.tetris.IGameController#handleGamePause()
	 */
	public void handleGamePause() {
		sendMultiplayerPause();
		doPauseGame();
	}

	/**
	 * @see org.oversoul.tetris.IGameController#handleGameResume()
	 */
	public void handleGameResume() {
		sendMultiplayerPause();		
		doResumeGame();
	}

	/**
	 * Handles game over.  Sends the game over message.
	 */
	public void handleGameOver() {
		sendMultiplayerGameOver();
		doGameOver();
	}
	
	/**
	 * @see org.oversoul.tetris.IGameController#handleGameSimpleScore()
	 */
	public void handleGameSimpleScore() {
		sendMultiplayerSimpleScore();
		ui.updateSimpleScore(player);
	}

	/**
	 * @see org.oversoul.tetris.IGameController#handleGameFullScore()
	 */
	public void handleGameFullScore() {
		sendMultiplayerFullScore();
		ui.updateFullScore(player);
	}
	
	/**
	 * @see org.oversoul.tetris.IGameController#displayHighscores(org.oversoul.tetris.highscores.Highscores, int, int)
	 */
	public void displayHighscores(Highscores highscores, int gameSpeed, int position) {
		if (position > 0) {
			io.saveHighscores(this, highscores);
		}
		ui.displayHighscores(highscores, gameSpeed, position);
	}
	
	/**
	 * Updates the block distribution.
	 */
	public void updateBlockCounts(int[] blockCounts) {
		ui.updateBlockCounts(blockCounts);
	}
	
	//////////////////////////////////////////////////////////////////////
	// OUTGOING MESSAGES
	//////////////////////////////////////////////////////////////////////

	/**
	 * Sends a game start message and starts the game.
	 */
	private void sendGameStart() {
		sendMultiplayerStart();
		doStartGame();
	}
	
	/**
	 * Sends a game pause message and pauses the game.
	 */
	public void sendGamePause() {
		if (!game.isPaused()) {
			sendMultiplayerPause();
			game.handleGamePause();
			doPauseGame();
		}
	}
	
	/**
	 * Sends a game resume message and resumes the game.
	 */
	public void sendGameResume() {
		if (game.isPaused()) {
			sendMultiplayerResume();
			game.handleGameResume();
			doResumeGame();
		}
	}
	
	/**
	 * Sends the game restart message and either waits for the opponent
	 * to respond (if multiplayer game) or simple restarts the game.
	 */
	public void sendGameRestart() {

		if (multiplayer) {		
			game.handleGamePause();
			sendMultiplayerRestart();
			waitForOpponent();
		} else {
			game.handleGameStart();
			doRestartGame();
		}
	}

	/**
	 * Sends the game end message ends the game.
	 */
	public void sendGameEnd() {
		sendMultiplayerEnd();
		game.handleGameOver();
		doGameOver();
	}	
	
	//////////////////////////////////////////////////////////////////////
	// EVENT HANDLER METHODS
	//////////////////////////////////////////////////////////////////////

	public void doStartGame() {
		resetScores();
		ui.setButtonText(TetrisRootPane.TEXT_PAUSE, Actions.PAUSE);
		setMessageText("Game started.", false);
		game.handleGameStart();
	}

	/**
	 * Handles pausing the game frame.
	 */
	private void doPauseGame() {
		ui.setButtonText(TetrisRootPane.TEXT_RESUME, Actions.RESUME);
		setMessageText("Game paused.", false);
	}
	
	/**
	 * Handles resuming the game frame.
	 */
	private void doResumeGame() {
		ui.setButtonText(TetrisRootPane.TEXT_PAUSE, Actions.PAUSE);
		setMessageText("Game resumed.", false);		
	}
	
	/**
	 * Restarts the game.
	 */
	private void doRestartGame() {
		resetScores();
		ui.setButtonText(TetrisRootPane.TEXT_PAUSE, Actions.PAUSE);
		setMessageText("Game restarted.", false);
		ui.updateTetrisTotal(player.getTetrisTotal());
		ui.updateOpponentTetrisTotal(opponent.getTetrisTotal());
	}

	/**
	 * Ends a game.
	 */
	private void doGameOver() {
		player.setWeightedScore(100);
		player.setOutgoingPenaltyLines(0);
		player.setIncomingPenaltyLines(0);
		ui.setButtonText(TetrisRootPane.TEXT_START, Actions.START);
		ui.updateSimpleScore(player);
		updatePlayerStats();
		ui.getStartButton().requestFocusInWindow();
	}

	
	private void setMultiplayer(boolean multiplayer) {
		this.multiplayer = multiplayer;	
		ui.setMultiplayer(multiplayer);
	}

	//////////////////////////////////////////////////////////////////////
	// IMultiplayerController Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates the player stats labels.
	 * @see org.oversoul.tetris.multiplayer.IMultiplayerController#updatePlayerStats()
	 */
	public void updatePlayerStats() {
		ui.updateStats(player);		
	}
	
	/**
	 * Sets the initial game variables.
	 * @param player
	 * @param opponent
	 */
	public void setMultiplayers(TetrisPlayer player, TetrisPlayer opponent) {
		this.player = player;
		if (opponent == null) {
			setMultiplayer(false);	
			resetOpponentStats();
		}
	}

	/**
	 * Starts the multiplayer countdown before starting a game.
	 */
	public void waitForOpponent() {
		game.getBoard().setMessage("Waiting...");
		game.getBoard().getComponent().repaint();
	}
	
	/**
	 * Handles the starting of a new multiplayer game.
	 */
	public void handleMultiplayerStart() {
		setMultiplayer(true);
		ui.setStartingValues(player.getStartingLevel(), player.getStartingLines(), player.getGameSpeed());
		ui.setVisible(true);
		ui.requestFocusInWindow();
		game.handleGameStart();
		doStartGame();
	}
	
	public void handleMultiplayerPause() {
		game.handleGamePause();
		doPauseGame();
	}
	
	public void handleMultiplayerResume() {
		game.handleGameResume();
		doResumeGame();	
	}
	
	public void handleMultiplayerRestart() {
		game.handleGamePause();
		doPauseGame();
		
		String msg = opponent.getName() + " has requested a restart.\nDo you accept?";
		int choice = JOptionPane.showConfirmDialog(component, msg, "Restart?", 
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			sendGameStart();
		} else {
			// continue without restart
			game.handleGameResume();
			doResumeGame();
			component.requestFocusInWindow();	
		}
		component.requestFocus();
	}

	/**
	 * Called when a player has left the multiplayer game.
	 */
	public void handleMultiplayerQuit() {
		setMultiplayer(false);
		String s = opponent.getName() + " has left the game";
		setMessageText(s, true);
		resetOpponentStats();
	}

	/**
	 * Handles a game over.
	 * @param playerNum	player number.
	 * @param score		opponent's score.
	 * @param wscore	opponent's weighted score.
	 * @param lines		opponent's lines.
	 * @param level		opponent's level.
	 * @param tetrisCount opponent's tetris count.
	 */
	public void handleMultiplayerGameOver(int playerNum, int score, int wscore,
										  int lines, int level, int tetrisCount) {
		game.handleGamePause();

		handleMultiplayerFullScore(playerNum, score, wscore, lines, level, tetrisCount, 0);
		player.won();
		opponent.lost();
		setMessageText("You won!", false);
		
		String[] options = new String[] {"New Game", "Keep Playing", "End Game"};
		int choice = JOptionPane.showOptionDialog(component, "You won!", "Winner", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
					null, options, options[0]);
		if (choice == JOptionPane.YES_OPTION) {
			sendMultiplayerRestart();
		} else if (choice == JOptionPane.NO_OPTION) {
			sendMultiplayerQuit();
			game.handleGameResume();
			component.requestFocusInWindow();
		} else {
			game.handleGameOver();
		}
		updatePlayerStats();
		ui.updateOpponentStats(opponent);
	}
	
	/**
	 * Handles the end of a multiplayer game. 
	 * @see org.oversoul.tetris.multiplayer.IMultiplayerController#handleMultiplayerEnd()
	 */
	public void handleMultiplayerEnd() {
		game.handleGamePause();

		String msg = opponent.getName() + " has ended the game.";
		setMessageText(msg, false);
		msg += "\nContinue playing?";
		int choice = JOptionPane.showConfirmDialog(component, msg, "Game ended", 
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			sendMultiplayerQuit();
			game.handleGameResume();
			updatePlayerStats();
			doResumeGame();
		} else {
			game.handleGameOver();
			doGameOver();
		}
		ui.updateOpponentStats(opponent);
	}
	
	/**
	 * Handles an error in a multiplayer game.
	 * @param playerNum	player number.
	 * @param error		error message.
	 */
	public void handleMultiplayerError(int playerNum, String error) {
		setMultiplayer(false);
		setMessageText(error, true);
		resetOpponentStats();
	}
	

	/**
	 * Updates the opponents score and weighted score.
	 * @param playerNum	player number.
	 * @param score		opponent's score.
	 * @param wscore	opponent's weighted score.
	 */
	public void handleMultiplayerSimpleScore(int playerNum, int score, int wscore) {
		opponent.setSimpleScore(score, wscore);
		ui.updateOpponentSimpleScore(opponent);
	}

	/**
	 * Updates the opponents complete score.
	 * @param playerNum	player number.
	 * @param score		opponent's score.
	 * @param wscore	opponent's weighted score.
	 * @param lines		opponent's lines.
	 * @param level		opponent's level.
	 * @param tetrisCount opponent's tetris count.
	 * @param plines	number of penalty lines (0-3).
	 */
	public void handleMultiplayerFullScore(int playerNum, int score, int wscore,
										int lines, int level, int tetrisCount, int plines) {
		opponent.setFullScore(score, wscore, lines, level, tetrisCount, plines);
		ui.updateOpponentFullScore(opponent);
		if (plines > 0) {
			game.handlePenalty(plines);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// OUTGOING MULTIPLAYER Methods
	//////////////////////////////////////////////////////////////////////
	
	private void sendMultiplayerStart() {
		if ((communication != null) && multiplayer) {
			communication.sendStart(player.getPlayerNumber());
		}
	}
	
	private void sendMultiplayerPause() {
		if ((communication != null) && multiplayer) {
			communication.sendPause(player.getPlayerNumber());
		}
	}
	
	private void sendMultiplayerResume() {
		if ((communication != null) && multiplayer) {
			communication.sendResume(player.getPlayerNumber());
		}
	}
	
	private void sendMultiplayerRestart() {
		if ((communication != null) && multiplayer) {
			communication.sendRestart(player.getPlayerNumber());
		}
	}

	private void sendMultiplayerEnd() {
		if ((communication != null) && multiplayer) {
			communication.sendEnd(player.getPlayerNumber());
		}
	}

	private void sendMultiplayerGameOver() {
		if ((communication != null) && multiplayer) {
			player.lost();
			opponent.won();
			communication.sendGameOver(player);
		}
	}
	
	private void sendMultiplayerQuit() {
		if (communication != null) {
			communication.sendQuit(player.getPlayerNumber());
		}
		ui.dropPlayer(opponent.getPlayerNumber());
		handleMultiplayerQuit();
	}
	
	private void sendMultiplayerSimpleScore() {
		if ((communication != null) && multiplayer) {
			communication.sendSimpleScore(player);	
		}	
	}
	
	private void sendMultiplayerFullScore() {
		if ((communication != null) && multiplayer) {
			communication.sendFullScore(player);	
		}	
	}
	
	public void resetScores() {
		player.resetGame();
		opponent.resetGame();
		ui.updateAllScores(player, opponent);
	}
	
	/** 
	 * Resets the opponent stats.
	 */
	public void resetOpponentStats() {
		opponent.resetAll();
		ui.updateOpponentStats(opponent);
	}
	
	/**
	 * Adds a message to the UI.  If it is an error message it will be 
	 * painted in red.
	 */
	public void setMessageText(String message, boolean isErrorMsg) {
		ui.setMessageText(message, isErrorMsg);
	}
	
	//////////////////////////////////////////////////////////////////////
	// UIInterface Methods
	//////////////////////////////////////////////////////////////////////
	
	public TetrisPlayer getPlayer() {
		return player;
	}
	
		
	public Configuration getConfiguration() {
		return config;	
	}

	public Version getVersion() {
		return version;
	}

	/**
	 * Reloads the current player from the config file.
	 * Then the ui is updated to reflect the new player (colors etc).
	 */
	public void reloadPlayer() {
		String oldName = player.getName();
		loadPlayer();
		if (!player.getName().equals(oldName)) {
			ui.updateFullScore(player);
			ui.updateStats(player);
			player.getPanel().setNeedRepaint(true);
			ui.repaintPanelBorders();
			player.savePanel(config);
			player.getPanel().setNeedRepaint(false);
			
			// re-initialize blocks				
			game.initializeFigures();
			game.getBoard().setColors(getPlayer().getColors());
			ui.updateBlockColors(getPlayer().getColors());
			
			ui.setStartingValues(player.getStartingLevel(), player.getStartingLines(), player.getGameSpeed());
			player.setNeedsReloading(false);
		}
	}
	
	public TetrisPlayer getOpponent() {
		return opponent;
	}
	
	public TetrisCommunication getCommunication() {
		if (communication == null) {
			communication = new TetrisCommunication(this);
		}
		return communication;
	}
	
	public Highscores getHighscores() {
		return highscores;
	}
	
	public void setStartingLevel(int startingLevel) {
		player.setStartingLevel(startingLevel);
		opponent.setStartingLevel(startingLevel);
	}

	public void setStartingLines(int startingLines) {
		player.setStartingLines(startingLines);
		opponent.setStartingLines(startingLines);
	}

	public void setGameSpeed(int gameSpeed) {
		player.setGameSpeed(gameSpeed);
		opponent.setGameSpeed(gameSpeed);
	}
	/**
	 * Sets the look and feel.  If the component is not null then 
	 * the component is refreshed and the new UI will show up.
	 * @param lnfName The look and feel class name
	 * @param comp	   The optional visual component/window to update
	 */
	public void setLookAndFeel(String lnfName, Component comp) {
		debug("Changing the look and feel to " + lnfName + ".");
		try {
			UIManager.setLookAndFeel(lnfName);
			if (comp != null) {
				SwingUtilities.updateComponentTreeUI(comp);
				if (comp instanceof Window) {
					((Window)comp).pack();
				}
			}
			player.setLookAndFeel(lnfName);
			ui.setLookAndFeel(lnfName);
		} catch (Exception ex) {
			debug("Error loading look and feel: " + ex);
			error("Error loading look and feel: ", ex);
			ui.disableLookAndFeel(lnfName);
		}
	}
	
	public int[] getBlockCounts() {
		return game.getBlockCounts();
	}
	
	public Component getGameComponent() {
		return game.getBoard().getComponent();
	}
	
	public Component getPreviewComponent() {
		return game.getPreviewBoard().getComponent();
	}
	
	public void handleKeyEvent(KeyEvent ke) {
		game.handleKeyEvent(ke);
	}
	
	/**
	 * This method will initialize the UI with the current player.
	 */
	public void initializeUI() {
		ui.updateStats(player);
		ui.setLookAndFeel(player.getLookAndFeel());
		ui.setStartingValues(player.getStartingLevel(), player.getStartingLines(), player.getGameSpeed());
	}

	public TetrisIO getIO() {
		return io;
	}
		

}
