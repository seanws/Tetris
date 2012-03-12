/**
 * TetrisAudio.java
 * 
 * Created on Sep 28, 2004
 */
package org.oversoul.tetris;

import java.io.File;
import java.security.AccessControlException;

import org.oversoul.audio.Audio;
import org.oversoul.audio.AudioListener;
import org.oversoul.audio.AudioUI;
import org.oversoul.audio.MidiFile;
import org.oversoul.audio.SampledAudioFile;
import org.oversoul.tetris.ui.AudioFileFilter;
import org.oversoul.tetris.util.Configuration;

/**
 * Controls the audio for the Tetris Game.
 * @author ccallendar
 */
public class TetrisAudio implements Cloneable {
	
	public static final String EMPTY = "";
	public static final String TRUE = "true";

	public static final int PLAY_TETRIS = 0;
	public static final int PLAY_LEVELUP = 1;
	public static final int PLAY_GAMEOVER = 2;
	public static final int PLAY_HIGHSCORE = 3;
	public static final int PLAY_PENALTY = 4;
	public static final int PLAY_BACKGROUND = 5;
	
	public static final String GAME_SOUNDS = "audio.gamesounds";
	public static final String TETRIS = "audio.tetris";
	public static final String TETRIS_PATH = "audio.tetris.path";
	public static final String LEVELUP = "audio.levelup";
	public static final String LEVELUP_PATH = "audio.levelup.path";
	public static final String GAMEOVER = "audio.gameover";
	public static final String GAMEOVER_PATH = "audio.gameover.path";
	public static final String HIGHSCORE = "audio.highscore";
	public static final String HIGHSCORE_PATH = "audio.highscore.path";
	public static final String PENALTY = "audio.penalty";
	public static final String PENALTY_PATH = "audio.penalty.path";

	public static final String BG_MUSIC = "audio.bg.music";
	public static final String BG_PATH = "audio.bg.path";
	public static final String BG_LOOP = "audio.bg.loop";
	public static final String BG_VOLUME = "audio.bg.volume";
	

	// game sounds
	public boolean gameSounds = false;
	public boolean tetris = false;
	public boolean levelUp = false;
	public boolean gameOver = false;
	public boolean highscore = false;
	public boolean penalty = false;
	private Audio tetrisAudio = null;
	private Audio levelUpAudio = null;
	private Audio gameOverAudio = null;
	private Audio highscoreAudio = null;
	private Audio penaltyAudio = null;

	// background music
	public boolean background = false;
	private Audio backgroundAudio = null;
	public boolean loop = false;
	public int volume = 50;
	
	/**
	 * Constructor for TetrisAudio.
	 */
	public TetrisAudio() {
	}

	/**
	 * Loads the audio.
	 * @param name player name
	 * @param config
	 */
	public void load(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			gameSounds = TRUE.equals(config.getValue(name, GAME_SOUNDS, EMPTY+gameSounds));
			tetris = TRUE.equals(config.getValue(name, TETRIS, EMPTY+tetris));
			setTetrisAudio(config.getValue(name, TETRIS_PATH, EMPTY));
			levelUp = TRUE.equals(config.getValue(name, LEVELUP, EMPTY+levelUp));
			setLevelUpAudio(config.getValue(name, LEVELUP_PATH, EMPTY));
			gameOver = TRUE.equals(config.getValue(name, GAMEOVER, EMPTY+gameOver));
			setGameOverAudio(config.getValue(name, GAMEOVER_PATH, EMPTY));
			highscore = TRUE.equals(config.getValue(name, HIGHSCORE, EMPTY+highscore));
			setHighscoreAudio(config.getValue(name, HIGHSCORE_PATH, EMPTY));
			penalty = TRUE.equals(config.getValue(name, PENALTY, EMPTY+penalty));
			setPenaltyAudio(config.getValue(name, PENALTY_PATH, EMPTY));

			background = TRUE.equals(config.getValue(name, BG_MUSIC, EMPTY+background));
			loop = TRUE.equals(config.getValue(name, BG_LOOP, EMPTY+loop));
			setBackgroundAudio(config.getValue(name, BG_PATH, EMPTY), loop);
			String vol = config.getValue(name, BG_VOLUME, EMPTY+volume);
			try {
				volume = Integer.parseInt(vol);
			} catch (NumberFormatException ignore) {}
		}		
	}
	
	/**
	 * Saves the audio.
	 * @param name player name
	 * @param config
	 */
	public void save(String name, Configuration config) {
		if ((name != null) && (name.length() > 0)) {
			config.setValue(name, GAME_SOUNDS, EMPTY+gameSounds);
			config.setValue(name, TETRIS, EMPTY+tetris);
			config.setValue(name, TETRIS_PATH, getTetrisAudioPath());
			config.setValue(name, LEVELUP, EMPTY+levelUp);
			config.setValue(name, LEVELUP_PATH, getLevelUpAudioPath());
			config.setValue(name, GAMEOVER, EMPTY+gameOver);
			config.setValue(name, GAMEOVER_PATH, getGameOverAudioPath());
			config.setValue(name, HIGHSCORE, EMPTY+highscore);
			config.setValue(name, HIGHSCORE_PATH, getHighscoreAudioPath());
			config.setValue(name, PENALTY, EMPTY+penalty);
			config.setValue(name, PENALTY_PATH, getPenaltyAudioPath());

			config.setValue(name, BG_MUSIC, EMPTY+background);
			config.setValue(name, BG_PATH, getBackgroundAudioPath());
			config.setValue(name, BG_LOOP, EMPTY+loop);
			config.setValue(name, BG_VOLUME, EMPTY+volume);
		}		
	}

	/**
	 * Gets the gameOverAudio audio path.
	 * @return String
	 */
	public String getTetrisAudioPath() {
		if ((tetrisAudio != null) && (tetrisAudio.getPath() != null)) {
			return tetrisAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the tetris audio.
	 * @param path
	 */
	public void setTetrisAudio(String path) {
		File file = new File(path);
		tetrisAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isSampledAudioFile(path)) {
				tetrisAudio = new SampledAudioFile(path);
			} else if (AudioFileFilter.isMidiFile(path)) {
				tetrisAudio = new MidiFile(path);
			}
		}
	}

	/**
	 * Gets the gameOverAudio audio path.
	 * @return String
	 */
	public String getLevelUpAudioPath() {
		if ((levelUpAudio != null) && (levelUpAudio.getPath() != null)) {
			return levelUpAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the level up audio.
	 * @param path
	 */
	public void setLevelUpAudio(String path) {
		File file = new File(path);
		levelUpAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isSampledAudioFile(path)) {
				levelUpAudio = new SampledAudioFile(path);
			} else if (AudioFileFilter.isMidiFile(path)) {
				levelUpAudio = new MidiFile(path);
			}
		}
	}

	/**
	 * Gets the gameOverAudio audio path.
	 * @return String
	 */
	public String getGameOverAudioPath() {
		if ((gameOverAudio != null) && (gameOverAudio.getPath() != null)) {
			return gameOverAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the gameover audio.
	 * @param path
	 */
	public void setGameOverAudio(String path) {
		File file = new File(path);
		gameOverAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isSampledAudioFile(path)) {
				gameOverAudio = new SampledAudioFile(path);
			} else if (AudioFileFilter.isMidiFile(path)) {
				gameOverAudio = new MidiFile(path);
			}
		}
	}

	/**
	 * Gets the highscore audio path.
	 * @return String
	 */
	public String getHighscoreAudioPath() {
		if ((highscoreAudio != null) && (highscoreAudio.getPath() != null)) {
			return highscoreAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the highscore audio.
	 * @param path
	 */
	public void setHighscoreAudio(String path) {
		File file = new File(path);
		highscoreAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isSampledAudioFile(path)) {
				highscoreAudio = new SampledAudioFile(path);
			} else if (AudioFileFilter.isMidiFile(path)) {
				highscoreAudio = new MidiFile(path);
			}
		}
	}

	/**
	 * Gets the penalty audio path.
	 * @return String
	 */
	public String getPenaltyAudioPath() {
		if ((penaltyAudio != null) && (penaltyAudio.getPath() != null)) {
			return penaltyAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the penalty audio.
	 * @param path
	 */
	public void setPenaltyAudio(String path) {
		File file = new File(path);
		penaltyAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isSampledAudioFile(path)) {
				penaltyAudio = new SampledAudioFile(path);
			} else if (AudioFileFilter.isMidiFile(path)) {
				penaltyAudio = new MidiFile(path);
			}
		}
	}

	/**
	 * Gets the background audio path.
	 * @return String
	 */
	public String getBackgroundAudioPath() {
		if ((backgroundAudio != null) && (backgroundAudio.getPath() != null)) {
			return backgroundAudio.getPath();
		} 
		return ""; 
	}
	/**
	 * Sets the background audio. 
	 * @param path
	 * @param loop if the background music should loop
	 */
	public void setBackgroundAudio(String path, boolean loop) {
		File file = new File(path);
		backgroundAudio = null;
		if (exists(file)) {
			if (AudioFileFilter.isMidiFile(path)) {
				backgroundAudio = new MidiFile(path, loop);
			} else if (AudioFileFilter.isSampledAudioFile(path)) {
				backgroundAudio = new SampledAudioFile(path, loop);
			}
		}
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		TetrisAudio audio = new TetrisAudio();
		audio.gameSounds = this.gameSounds;
		audio.levelUp = this.levelUp;
		audio.gameOver = this.gameOver;
		audio.highscore = this.highscore;
		audio.penalty = this.penalty;
		audio.setTetrisAudio(this.getTetrisAudioPath());
		audio.setLevelUpAudio(this.getLevelUpAudioPath());
		audio.setGameOverAudio(this.getGameOverAudioPath());
		audio.setHighscoreAudio(this.getHighscoreAudioPath());
		audio.setPenaltyAudio(this.getPenaltyAudioPath());
		audio.background = this.background;
		audio.setBackgroundAudio(this.getBackgroundAudioPath(), this.loop);
		audio.loop = this.loop;
		audio.volume = this.volume;
		return audio;
	}
	
	@Override
	public String toString() {
		return gameSounds + "," + levelUp + "," + gameOver + "," + highscore + "," + 
				penalty + "," + background + "," + loop + "," + volume;
	}
	
	/**
	 * Plays the given sound.
	 * @param sound
	 */
	public void play(int sound) {
		switch (sound) {
			case PLAY_TETRIS :
				play(tetris && gameSounds, tetrisAudio);
				break;
			case PLAY_LEVELUP :
				play(levelUp && gameSounds, levelUpAudio);
				break;
			case PLAY_GAMEOVER : 
				play(gameOver && gameSounds, gameOverAudio);
				break;
			case PLAY_HIGHSCORE :
				play(highscore && gameSounds, highscoreAudio);
				break;
			case PLAY_PENALTY :
				play(penalty && gameSounds, penaltyAudio);
				break;
			case PLAY_BACKGROUND :
				play(background, backgroundAudio);
				break;	
		}
	}
	
	/**
	 * Plays the sound if it is allowed.
	 * @param on
	 * @param audio
	 */
	private void play(boolean on, Audio audio) {
		if (on && (audio != null)) {
			new TetrisAudioThread(audio);	
		}
	}
	
	private boolean exists(File file) {
		try {
			return file.exists();
		} catch (AccessControlException ace) {}
		return false;
	}
	
	/**
	 * Plays an audio file and then kills itself once the song is done.
	 * 
	 * @author 		ccallendar
	 * @date		20-Nov-04, 2:14:11 PM
	 * @version 	1.0
	 */
	private class TetrisAudioThread implements Runnable, AudioUI {
		
		private volatile Thread thread = null;	
		
		private Audio audio = null;
		
		//TODO is this needed?  It is never used.
		protected AudioListener listener = null;
		
		public TetrisAudioThread(Audio audio) {
			this.audio = audio;	
			this.listener = new AudioListener(this, audio);
			thread = new Thread(this);
			thread.start();
		}
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				audio.play();
				while (thread != null) {
					Thread.sleep(100);	
				}
			} catch (Exception ex) {}
			
		}

		/**
		 * @see org.oversoul.audio.AudioUI#audioStopped()
		 */
		public void audioStopped() {
			thread = null;
		}

	}

}
