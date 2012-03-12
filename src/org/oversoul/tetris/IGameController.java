/**
 * GameInterface.java
 * 
 * Created on 4-Nov-04
 */
package org.oversoul.tetris;

import org.oversoul.tetris.highscores.Highscores;


/**
 * Interface for game methods like start, pause, resume, and game over.
 * 
 * @author 		nyef
 * @date		4-Nov-04, 8:15:20 PM
 * @version 	1.0
 */
public interface IGameController {

	/** Starts a game. */
	void handleGameStart();

	/** Pauses a game. */	
	void handleGamePause();

	/** Resume a game. */	
	void handleGameResume();
	
	/** Handles game over. */
	void handleGameOver();

	/** Gets the tetris player. */
	TetrisPlayer getPlayer();
	
	/** 
	 * Opens up the highscores dialog for the given game speed.
	 * @param highscores the highscores
	 * @param gameSpeed	 the game speed for the highscore
	 * @param position	 the position of the new highscore (or 0 if no highscore)
	 */
	void displayHighscores(Highscores highscores, int gameSpeed, int position);
	
	/** Updates the score, weighted score, and game time. */
	void handleGameSimpleScore();
	
	/** Updates the score, level, lines, tetrises, weighted score, game time. */
	void handleGameFullScore();
	
	/** 
	 * Updates the block counts for each type of block.
	 * @param blockCounts the counts for each block type. 
	 */
	void updateBlockCounts(int[] blockCounts);
	
}
