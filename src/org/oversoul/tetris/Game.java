/**
 * Game.java
 *
 * February 11, 2004
 */

package org.oversoul.tetris;

import java.awt.event.KeyEvent;
import java.util.Random;

import org.oversoul.tetris.highscores.HighscoreEntry;
import org.oversoul.tetris.highscores.Highscores;
import org.oversoul.tetris.util.Util;

/**
 * The Tetris game. This class controls all events in the game and
 * handles all the game logics. The game is started through user
 * interaction with the graphical game component provided by this 
 * class.
 *
 * @author Chris Callendar (9902588)
 */
public class Game {

    /** The main square board. This board is used for the game itself. */
    private TetrisBoard board = null;

    /**
     * The preview square board. This board is used to display a 
     * preview of the figures.
     */
    private TetrisBoard previewBoard = null;

    /**
     * The figures used on both boards. All figures are reutilized in 
     * order to avoid creating new objects while the game is running.
     * Special care has to be taken when the preview figure and the
     * current figure refers to the same object.
     */
    private Block[] figures = null;

    /**
     * The game controller.
     */
    private IGameController controller = null;

    /**
     * The thread that runs the game. When this variable is set to 
     * null, the game thread will terminate.
     */
    private GameThread thread = null;
    
    /** 
     * The game speed that is used if a highscore occurs.
     * It is the slowest game speed used over a game. 
     */
    private int minGameSpeed = TetrisConstants.SPEED_NORM;

	/** Holds the high scores. */
	private Highscores highscores = null;
	private int lowestScore = 0;
	private boolean onHighscoresList = false;

	/** If highscores are allowed. */
	private boolean highscoreAllowed = true;
	private boolean gameOver = false;

    /** The current figure. The figure will be updated.    */
    private Block figure = null;

    /** The next figure.     */
    private Block nextFigure = null;
    
    /** The rotation of the next figure.     */
    private int nextRotation = 0;

    /**
     * The move lock flag. If this flag is set, the current figure
     * cannot be moved. This flag is set when a figure is moved all 
     * the way down, and reset when a new figure is displayed.
     */
    private boolean moveLock = false;
    
    /** The random number generator. */
    private Random random = null;
    
    private int lastFigure = -1;
    private int figureRepeats = 0;

	/** the number of consecutive tetrises (bonus points). */
	private int consecutiveTetrises = 0;

	private int cheatKey = KeyEvent.VK_BACK_QUOTE;
	
	private long gameStartTime = 0;
	private int gameSeconds = 0;
	
	private int[] blockCounts = null;

	private boolean DEBUG = false;

    /**
     * Creates a new Tetris game. The square board will be given
     * the default size of 10x20.
     */
    public Game(IGameController controller, Highscores highscores) {
        this(controller, highscores, 10, 20);
    }

    /**
     * Creates a new Tetris game. The square board will be given
     * the specified size.
     * @param width     the width of the square board (in positions)
     * @param height    the height of the square board (in positions)
     */
    public Game(IGameController controller, Highscores highscores, int width, int height) {
    	this.controller = controller;
		this.highscores = highscores;
		this.random = new Random();
		
		// initialize the figures
		initializeFigures();
    	
    	previewBoard = new TetrisBoard(5, 5, getPlayer().getColors());
        board = new TetrisBoard(width, height, getPlayer().getColors());
        board.setMessage("Press start");
        thread = new GameThread();
		blockCounts = new int[] { 0,0,0,0,0,0,0 };
    }

	/** Initializes the figures.	 */
	public void initializeFigures() {
		int i = 0;
		BlockType[] types = BlockType.values();
	    figures = new Block[types.length];
	    final TetrisColors colors = getPlayer().getColors();
	    for(BlockType type : types) {
	    	figures[i++] = new Block(colors.getColor(type), type);
	    }
		for (i = 0; i < figures.length; i++) {
			TetrisBoard s = new TetrisBoard(5, 5, colors);
			figures[i].attach(s, true);
			figures[i].detach();
			s = null;
		}
	}


    /**
     * Kills the game running thread.
     * After calling this method, no further methods in this class
     * should be called. Neither should the component returned
     * earlier be trusted upon.
     */
    public void quit() {
        thread = null;
    }

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Returns the board.
	 * @return TetrisBoard
	 */
	public TetrisBoard getBoard() {
		return board;
	}

	/**
	 * Returns the previewBoard.
	 * @return TetrisBoard
	 */
	public TetrisBoard getPreviewBoard() {
		return previewBoard;
	}

	/**
	 * Returns the highscores.
	 * @return Highscores
	 */
	public Highscores getHighScores() {
		return highscores;
	}

	/**
	 * Gets the current player.
	 * @return TetrisPlayer
	 */
	public TetrisPlayer getPlayer() {
		return controller.getPlayer();
	}
	
	/**
	 * Gets the block distribution.
	 * @return int[]
	 */
	public int[] getBlockCounts() {
		return blockCounts;	
	}

	//////////////////////////////////////////////////////////////////////
	// HANDLE INCOMING Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * @see org.oversoul.tetris.IGameController#handleGameStart()
	 */
	public synchronized void handleGameStart() {
		doStartGame();
	}
	
	/**
	 * Pauses the game.
	 * @see org.oversoul.tetris.IGameController#handleGamePause()
	 */
	public synchronized void handleGamePause() {
		if ((thread != null) && !thread.isPaused()) {
			doPauseGame();
		}
	}

	/**
 	 * Resumes the game.
	 * @see org.oversoul.tetris.IGameController#handleGameResume()
	 */	
	public synchronized void handleGameResume() {
		if (isPaused()) {
			doResumeGame();
		}		
	}
	
	/**
	 * @see org.oversoul.tetris.IGameController#handleGameOver()
	 */
	public void handleGameOver() {
		doGameOver();
	}
	
	/**
	 * Sets the number of incoming penalty lines.
	 * @param penaltyLines The number of penalty lines to set
	 */
	public synchronized void handlePenalty(int penaltyLines) {
		getPlayer().addIncomingPenaltyLines(penaltyLines);
	}
	
	/**
	 * Handles a change in the game speed.
	 * The highscores are based on the slowest game speed
	 * that occurs during a game.
	 */
	public synchronized void handleGameSpeedChange() {
		int oldMin = minGameSpeed;
		int newSpeed = getPlayer().getGameSpeed();
		minGameSpeed = Math.min(minGameSpeed, newSpeed);
		if (oldMin != minGameSpeed) {
			lowestScore = highscores.getLowestVisibleScore(minGameSpeed);
			onHighscoresList = false;
		}
		thread.adjustSpeed(newSpeed);
	}
   
	///////////////////////////////////////////////////////////////////////////
	// OUTGOING MESSAGES
	///////////////////////////////////////////////////////////////////////////
    
    private void sendPauseGame() {
		if (nextFigure == null) {
			doStartGame();
			controller.handleGameStart();
		} else if (thread != null) {
			if (!thread.isPaused()) {
				doPauseGame();
				controller.handleGamePause();
			} else {
				doResumeGame();
				controller.handleGameResume();
			}
		}
    }
    
    private void sendGameOver() {
    	doGameOver();
    	controller.handleGameOver();
    }
    
    private void sendHighscore(int pos) {
		controller.displayHighscores(highscores, minGameSpeed, pos);
    }
    
    private void sendSimpleScore() {
		controller.handleGameSimpleScore();
    }
    
    private void sendFullScore() {
		controller.handleGameFullScore();
    }
    
    private void sendBlockCounts() {
    	controller.updateBlockCounts(blockCounts);	
    }
	   
	///////////////////////////////////////////////////////////////////////////
	// GAME METHODS
	///////////////////////////////////////////////////////////////////////////
    
	/**
	 * Handles a game start event. Both the main and preview square
	 * boards will be reset, and all other game parameters will be
	 * reset. Finally the game thread will be launched.
	 */
    private void doStartGame() {
		gameOver = false;
		thread.setPaused(true);
		
		blockCounts = new int[] { 0,0,0,0,0,0,0 };
		
		// Reset score and figures
		getPlayer().resetGame();
		minGameSpeed = getPlayer().getGameSpeed();
		lowestScore = highscores.getLowestVisibleScore(minGameSpeed);
		onHighscoresList = false;

		// detach any attached blocks
		for (int i = 0; i < figures.length; i++) {
			figures[i].detach();
		}
		figure = null;
		nextFigure = randomFigure();
		nextFigure.rotateRandom();
		nextRotation = nextFigure.getRotation();  

		// Reset components
		board.hideCursor();
		board.setMessage(null);
		board.clear();
		// add any starting lines
		board.addStartingLines(getPlayer().getStartingLines());

		gameStartTime = System.currentTimeMillis();

		// Start game thread
		thread.reset(getPlayer().getGameSpeed());
	}

	/**
	 * Handles a game over event. This will stop the game thread,
	 * reset all figures and print a game over message.
	 */
	private void doGameOver() {
		// Stop game thread
		thread.setPaused(true);
		if (!gameOver) {
			board.showCursor();
	
			// Reset figures
			if (figure != null) {
				figure.detach();
			}
			figure = null;
			if (nextFigure != null) {
				nextFigure.detach();
			}
			nextFigure = null;
	
			// Handle components
			board.setMessage("Game Over");
	
			gameOver = true;
			boolean highscore = doHighScore();
	
			TetrisAudio audio = getPlayer().getAudio();
			if (!highscore && (audio != null)) {
				audio.play(TetrisAudio.PLAY_GAMEOVER);
			}
		}
	}

	/**
	 * Adds the current score to the high scores if it is high enough.
	 * Displays the high scores dialog.
	 */
	private boolean doHighScore() {
		boolean highscore = false;
		TetrisAudio audio = getPlayer().getAudio();
		if (highscoreAllowed && gameOver) {
			HighscoreEntry entry = new HighscoreEntry(getPlayer());
			int pos = highscores.addHighScore(entry, minGameSpeed);
			if (pos > 0) {
				if (pos <= highscores.getVisibleEntries()) {
					if (audio != null) {
						audio.play(TetrisAudio.PLAY_HIGHSCORE);
					}
					highscore = true;
					sendHighscore(pos);
				}
			}
		}
		return highscore;
	}


    /**
     * Handles a game pause event. This will pause the game thread and
     * print a pause message on the game board.
     */
    private void doPauseGame() {
        thread.setPaused(true);
        board.showCursor();
       	board.setMessage("Paused");
    }

    /**
     * Handles a game resume event. This will resume the game thread 
     * and remove any messages on the game board.
     */
    private void doResumeGame() {
        board.setMessage(null);
        board.hideCursor();
       	thread.setPaused(false);
       	gameOver = false;
    }
    
    
    /**
     * Handles a penalty - shifts the blocks up by the given number
     * of lines.  This is for multiplayer only.
     */
    private void doPenalty() {
    	if (getPlayer().isPenalties() && (getPlayer().getIncomingPenaltyLines() > 0)) {
	        boolean added = false;
	        synchronized (this) {
	    		added = board.addIncompleteLines(getPlayer().getIncomingPenaltyLines());
				getPlayer().penalty();
	        }
	    	if (!added) {
	    		sendGameOver();	
	    	}
    	}
    }

    /**
     * Handles a figure start event. This will move the next figure
     * to the current figure position, while also creating a new 
     * preview figure. If the figure cannot be introduced onto the
     * game board, a game over event will be launched.
     */
    private void handleFigureStart() {
        int rotation;
		
		if (nextFigure == null) {
	        nextFigure = randomFigure();
    	    nextFigure.rotateRandom();
        	nextRotation = nextFigure.getRotation();  
		}

        // Move next figure to current
        figure = nextFigure;
        moveLock = false;
        rotation = nextRotation;
        nextFigure = randomFigure();
        nextFigure.rotateRandom(); 
        nextRotation = nextFigure.getRotation(); 
        blockCounts[figure.getType().ordinal()] += 1;
        
        // update the block counts
		sendBlockCounts();

		// Handle figure preview
		if (getPlayer().isPreview()) {
			previewBoard.clear();
			nextFigure.attach(previewBoard, true);
			nextFigure.detach();
		}

        // Attach figure to game board
        figure.setRotation(rotation);
        if (!figure.attach(board, false)) {
            previewBoard.clear();
            figure.attach(previewBoard, true);
            figure.detach();
            sendGameOver();
        }
    }

    /**
     * Handles a figure landed event. This will check that the figure
     * is completely visible, or a game over event will be launched.
     * After this control, any full lines will be removed. If no full
     * lines could be removed, a figure start event is launched 
     * directly.
     */
    private void handleFigureLanded() {
    	
    	int scoreToAdd = 0;
    	
        // Check and detach figure
        if (figure.isAllVisible()) {
			scoreToAdd += TetrisConstants.BLOCK_LANDED;
        } else {
            sendGameOver();
            return;
        }
        figure.detach();
        figure = null;

        // Check for full lines or create new figure
        boolean hasFull = board.hasFullLines();
        if (hasFull) {
        	int removed = board.removeFullLines(getPlayer().getLevel());
			getPlayer().setOutgoingPenaltyLines(removed - 1);
			
			switch (removed) {
			case 1:
				scoreToAdd += TetrisConstants.ONE_LINE;
				getPlayer().addSingle();
				break;	
			case 2:
				scoreToAdd += TetrisConstants.TWO_LINES;
				getPlayer().addDouble();
				break;	
			case 3:
				scoreToAdd += TetrisConstants.THREE_LINES;
				getPlayer().addTriple();
				break;	
			case 4:
				scoreToAdd += TetrisConstants.FOUR_LINES;
				// add a bonus for consecutive tetrises
				scoreToAdd += (consecutiveTetrises*TetrisConstants.TETRIS_BONUS);	
				consecutiveTetrises++;	
				getPlayer().tetris();
				break;
			}

			if (removed != 4) {
				consecutiveTetrises = 0;
			}
			
			// count the blocks on the board and award a bonus for having very few (< 4)
			int blocks = board.countBlocks()[0];
			int bonus = TetrisConstants.MAX_BLOCKS_BONUS - (blocks * TetrisConstants.NO_BLOCKS_BONUS);
			if (bonus > 0) {
				scoreToAdd += bonus;
			}
			
            getPlayer().addLines(removed);
            int newLevel = getPlayer().getLines() / TetrisConstants.MAX_LEVEL;
            if ((getPlayer().getLevel() < TetrisConstants.MAX_LEVEL) && (newLevel > getPlayer().getLevel())) {
				getPlayer().levelUp();
				thread.adjustSpeed(getPlayer().getGameSpeed());
            }
        } else {
			consecutiveTetrises = 0;
        }
        
        if (!onHighscoresList && (getPlayer().getScore() > lowestScore)) {
        	onHighscoresList = true;
        	getPlayer().getAudio().play(TetrisAudio.PLAY_HIGHSCORE);	
        }
        
	    int wScore = board.calculateWeightedScore();
    	if (getPlayer().getLevel() <= 4) {
	    	wScore = (int)(wScore * ((getPlayer().getLevel()+6)/10.0));
    	}
	    if (wScore != getPlayer().getWeightedScore()) {
	     	getPlayer().setWeightedScore(wScore);
	    }
	    
	    getPlayer().addScore(scoreToAdd);
		
		doPenalty();	
        if (!hasFull) {
			sendSimpleScore();
            handleFigureStart();
        } else {
        	sendFullScore();
        }
    }

    /**
     * Handles a timer event. This will normally move the figure down
     * one step, but when a figure has landed or isn't ready other 
     * events will be launched. This method is synchronized to avoid 
     * race conditions with other asynchronous events (keyboard and
     * mouse).
     */
    private synchronized void handleTimer() {
        if (figure == null) {
            handleFigureStart();
        } else if (figure.hasLanded()) {
            handleFigureLanded();
        } else {
            figure.moveDown();
        }
    }

    /**
     * Handles a keyboard event. This will result in different actions
     * being taken, depending on the key pressed. In some cases, other
     * events will be launched. This method is synchronized to avoid 
     * race conditions with other asynchronous events (timer and 
     * mouse).
     * @param e	the key event
     */
    public synchronized void handleKeyEvent(KeyEvent e) {
        int code = e.getKeyCode();
        if ((figure == null) || moveLock || thread.isPaused()) {
        	if (code == getPlayer().getControls().pause) {
       			sendPauseGame();
        	}
        } else if (code == getPlayer().getControls().left) {
            figure.moveLeft();
        } else if (code == getPlayer().getControls().right) {
            figure.moveRight();
        } else if (code == getPlayer().getControls().rotate) {
            if (e.isControlDown()) {
                figure.rotateRandom();  
            } else if (e.isShiftDown()) { 
                figure.rotateClockwise();
            } else {
                figure.rotateCounterClockwise();
            }
        } else if (code == getPlayer().getControls().drop) {
            figure.moveAllWayDown();
            moveLock = true;
        } else if (code == getPlayer().getControls().down) {
        	figure.moveDown();
        } else if (code == getPlayer().getControls().pause) {
      		sendPauseGame();
        } else if (code == cheatKey) {
        	getPlayer().setOutgoingPenaltyLines(2);
        	sendFullScore();
        } else if (code == getPlayer().getControls().levelUp) {
            if (getPlayer().getLevel() <= 9) {
                getPlayer().levelUp();
                thread.adjustSpeed(getPlayer().getGameSpeed());
				sendFullScore();
            }
        } else if (code == getPlayer().getControls().preview) {
            getPlayer().setPreview(!getPlayer().isPreview());
            if (getPlayer().isPreview() && (figure != nextFigure)) {
                nextFigure.attach(previewBoard, true);
                nextFigure.detach(); 
            } else {
                previewBoard.clear();
            }
		}
    }

    /**
     * Returns a random figure. The figures come from the figures
     * array, and will not be initialized.  This method will never return the 
     * same Block more than three times in a row.
     * @return a random figure
     */
    private Block randomFigure() {
        int rand = random.nextInt(figures.length);
        // make sure to never get more than 2 blocks in a row (except for lines)
        while ((rand == lastFigure) && ((lastFigure != BlockType.LINE.ordinal()) && (figureRepeats >= 1))) {
        	rand = random.nextInt(figures.length);
        }
		figureRepeats = ((lastFigure == rand) ? figureRepeats+1 : 0);
        lastFigure = rand;
        return figures[rand];
    }

	/**
	 * Returns if the game is paused.
	 * @return boolean if paused.
	 */
	public boolean isPaused() {
		return (thread != null) && thread.isPaused();	
	}
	/**
	 * Returns if the game is over.
	 * @return boolean if game over.
	 */
	public boolean isGameOver() {
		return isPaused() && (nextFigure == null);	
	}
	
	/**
	 * Gets the game playing time [hrs:]mins:secs.
	 * @return String game time String
	 */
	private String getGameTime() {
		String timeString = "";
		long time = System.currentTimeMillis() - gameStartTime;
		int seconds = Math.round(time / 1000);
		int hrs = seconds / 3600;
		int hrsRem = seconds % 3600;
		int mins = hrsRem / 60;
		int secs = hrsRem % 60;
		
		if (hrs > 0) {
			timeString = hrs + ":" + Util.padZeroes(mins, 2) + ":" + Util.padZeroes(secs, 2);	
		} else {
			timeString = mins + ":" + Util.padZeroes(secs, 2);
		}
		return timeString;
	}

    /**
     * The game time thread. This thread makes sure that the timer
     * events are launched appropriately, making the current figure 
     * fall. This thread can be reused across games, but should be set
     * to paused state when no game is running.
     */
    private class GameThread extends Thread {
    	
    	private final int[] SLOW = new int[] { 600,500,400,350,300,250,200,150,125,100, 75 };
    	private final int[] NORM = new int[] { 500,400,350,300,250,200,150,125,100, 75, 50 };
    	private final int[] FAST = new int[] { 400,300,250,200,175,150,125,100, 75, 50, 35 };

        /** The game pause flag. Set to true while the game should pause. */
        private boolean paused = true;

        /**
         * The time interval (milliseconds) between each block movement.
         * This includes the execution time and the sleep before.
         * This number will be lowered as the game progresses.
         */
        private int timeInterval = 500;
        
        /** The minimum sleep time. */
        private final int MIN_SLEEP_TIME = 30;
        
        /** 
         * Constructor for GameThread.
         */
        public GameThread() {
        }

        /**
         * Resets the game thread. This will adjust the speed and 
         * start the game thread if not previously started.
         * @param speed the game speed - slow, normal, or fast
         */
        public void reset(int speed) {
            adjustSpeed(speed);
            setPaused(false);
            if (!isAlive()) {
                this.start();
            }
        }

        /**
         * Checks if the thread is paused.
         * @return true if the thread is paused, or false otherwise
         */
        public boolean isPaused() {
            return paused;
        }

        /**
         * Sets the thread pause flag.
         * @param paused     the new paused flag value
         */
        public void setPaused(boolean paused) {
            this.paused = paused;
        }

        /**
         * Adjusts the game speed according to the current level and speed. 
         * @param speed the game speed - slow, normal, or fast
         */
        public void adjustSpeed(int speed) {
        	/* old way - adjust sleep time by formula
            sleepTime = (4500 / (getPlayer().getLevel() + 5)) - 250;
			f (sleepTime < 50) {
				sleepTime = 50;
			}
            */

            // new way - use predefined times in ms
            int level = getPlayer().getLevel();
            switch(speed) {
	            case TetrisConstants.SPEED_SLOW :
					timeInterval = SLOW[level];
	            	break;
	            case TetrisConstants.SPEED_FAST :
					timeInterval = FAST[level];
	            	break;
	            case TetrisConstants.SPEED_NORM :
	            default:
					timeInterval = NORM[level];
            }
        }

        /**
         * Runs the game.
         */
        @Override
        public void run() {
            while (thread == this) {
            	long executionTime = System.currentTimeMillis();
            	
                // Make the time step
                handleTimer();
            	
            	// update the game time
				long time = System.currentTimeMillis() - gameStartTime;
				int seconds = Math.round(time / 1000);
				if (seconds > gameSeconds) {
					gameSeconds = seconds;
					getPlayer().setGameTime(getGameTime());
				}

				executionTime = System.currentTimeMillis() - executionTime;
				
				if ((executionTime > 0) && DEBUG)
					System.out.println("Execution time = " + executionTime);

                // Sleep for the remainder of the time interval
				long sleepTime = Math.max((timeInterval - executionTime), MIN_SLEEP_TIME);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignore) {}

                // Sleep if paused
                while (paused && thread == this) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
       }
    }

}
