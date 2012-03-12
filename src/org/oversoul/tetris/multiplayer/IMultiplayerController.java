/**
 * MultiplayerInterface.java
 * 
 * Created on 4-Nov-04
 */
package org.oversoul.tetris.multiplayer;

import org.oversoul.tetris.TetrisPlayer;

/**
 * Interface for the tetris multiplayer methods.
 * 
 * @author 		nyef
 * @date		4-Nov-04, 8:03:09 PM
 * @version 	1.0
 */
public interface IMultiplayerController {

	/**
	 * Handles the starting of a new multiplayer game.
	 */
	public void handleMultiplayerStart();

	/**
	 * Handles the pausing of a multiplayer game.
	 */
	public void handleMultiplayerPause();

	/**
	 * Handles the resuming of a multiplayer game.
	 */
	public void handleMultiplayerResume();
	
	/**
	 * Handles the restarting of a multiplayer game.
	 */
	public void handleMultiplayerRestart();
	
	/**
	 * Handles the end of a multiplayer game.
	 */
	public void handleMultiplayerEnd();

	/**
	 * Handles a multiplayer game over.
	 * @param playerNum	player number.
	 * @param score		opponent's score.
	 * @param wscore	opponent's weighted score.
	 * @param lines		opponent's lines.
	 * @param level		opponent's level.
	 * @param tetrisCount opponent's tetris count.
	 */
	public void handleMultiplayerGameOver(int playerNum, int score, int wscore,
										  int lines, int level, int tetrisCount);
	
	/**
	 * Called when a player has left the multiplayer game.
	 */
	public void handleMultiplayerQuit();

	/**
	 * Sets the players.
	 * @param player
	 * @param opponent
	 */
	public void setMultiplayers(TetrisPlayer player, TetrisPlayer opponent);
	
	/**
	 * Updates the player stats.
	 */
	public void updatePlayerStats();
	
	/**
	 * Starts the multiplayer countdown before starting a game.
	 */
	public void waitForOpponent();
		
}
