/**
 * ReloadPlayerListener.java
 * 
 * Created on 30-Oct-2005
 */
package org.oversoul.tetris;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.oversoul.tetris.highscores.Highscores;
import org.oversoul.tetris.multiplayer.TetrisCommunication;
import org.oversoul.tetris.util.Configuration;
import org.oversoul.util.Version;

/**
 * The interface that the Tetris UI components will see.
 * 
 * @author 		ccallendar
 * @date		30-Oct-2005, 6:18:05 PM
 * @version 	1.0
 */
public interface IUIController {
	
	/** Disposes the controller. */
	void dispose();
	
	/** Starts the game (no multiplayer messages are sent). */
	void doStartGame();
	
	/** Sends a game pause message and pauses the game. */
	void sendGamePause();
	
	/** Sends a game resume message and resumes the game. */
	void sendGameResume();
	
	/** Sends a game restart message and [possibly] restarts the game. */
	void sendGameRestart();
	
	/** Sends a game end message and ends the game. */
	void sendGameEnd();

	/** 
	 * Get the Tetris configuration.
	 * @return Configuration
	 */
	Configuration getConfiguration();
	
	/**
	 * Gets the current TetrisPlayer
	 * @return TetrisPlayer the current player
	 */
	TetrisPlayer getPlayer();
	
	/**
	 * Gets the opponent TetrisPlayer
	 * @return TetrisPlayer the current opponent
	 */
	TetrisPlayer getOpponent();
	
	/**
	 * Gets the current version of Tetris.
	 * @return Version the current version
	 */
	Version getVersion();
	
	/**
	 * Gets the highscores object.
	 * @return Highscores
	 */
	Highscores getHighscores();

	/**
	 * Gets the TetrisCommunication object used for sending and 
	 * receiving network packets/messages.
	 * @return TetrisCommunication
	 */
	TetrisCommunication getCommunication();
	
	/**
	 * Gets the counts for each block type. 
	 * @return int[] the counts for each block type
	 */
	int[] getBlockCounts();
	
	/**
	 * Returns the Game {@link java.awt.Component}
	 * @return Component the game Component
	 */
	Component getGameComponent();
	
	/**
	 * Returns the preview {@link java.awt.Component}
	 * @return Component the preview Component
	 */
	Component getPreviewComponent();
	
	/**
	 * Sets the look and feel and updates the UI (if component is not null).
	 * @param lnfClassName	The class name of the new look and feel
	 * @param component		The component or window to update
	 */
	void setLookAndFeel(String lnfClassName, Component component);
	
	/**
	 * Updates the current player to use the given starting level.
	 * @param startingLevel the level to start on next game
	 */
	void setStartingLevel(int startingLevel);
	
	/**
	 * Updates the current player to use the given number of starting lines.
	 * @param startingLines the number of lines to start with next game
	 */
	void setStartingLines(int startingLines);
	
	/**
	 * Updates the current player to use the given game speed.
	 * @param gameSpeed the game speed to start on next game
	 */
	void setGameSpeed(int gameSpeed);
	
	/**
	 * Reloads the current player from the Configuration
	 * and updates the UI.
	 */
	void reloadPlayer();
	
	/**
	 * Initializes the UI for the current player.
	 * This involves setting the player's stats and colors.
	 */
	void initializeUI();
	
	/**
	 * Handles the KeyEvent.
	 * @param ke
	 */
	void handleKeyEvent(KeyEvent ke);
	
}
