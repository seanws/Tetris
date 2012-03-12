/**
 * HighscoreEntry.java
 * 
 * Created on 16-Feb-04
 */
package org.oversoul.tetris.highscores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisPlayer;

/**
 * The HighscoreEntry class represents an entry in the Highscores class for Tetris.
 * 
 * @author Chris Callendar (9902588)
 * @date   16-Feb-04, 1:16:41 PM
 */
public class HighscoreEntry implements Comparable, Comparator {

	protected static final String DELIM = "\t";

	/** The game speed for this entry.	 */
	private int gameSpeed = TetrisConstants.SPEED_NORM;

	/** The person's name for this entry. */
	private String name = "";
	
	/** The score. */
	private int score = -1;
	
	/** The number of lines. */
	private int lines = -1;
	
	/** The level. */
	private int level = -1;
	
	/** The tetris count. */
	private int tetrisCount = -1;
	
	/** The date of the high score. */
	private Date date = null;
	
	/** If no name has been given. */
	private boolean hasName = false;
	
	/** Date formatter. */
	private SimpleDateFormat DATE = new SimpleDateFormat("MM/dd/yy");

	/**
	 * Constructor for HighscoreEntry.
	 * @param player The high scoring tetris player.
	 */
	public HighscoreEntry(TetrisPlayer player) {
		this.gameSpeed = player.getGameSpeed();
		setName(player.getName());
		this.score = player.getScore();
		this.lines = player.getLines();
		this.level = player.getLevel();
		this.tetrisCount = player.getTetrisCount();
		this.date = new Date();
	}

	/**
	 * Constructor for HighscoreEntry.
	 * @param gameSpeed game speed.
	 * @param name	Player's name.
	 * @param score Player's score.
	 * @param lines Player's lines.
	 * @param level Player's level. 
	 * @param tetrisCount the Player's tetris count.
	 */
	public HighscoreEntry(int gameSpeed,
						  String name,
						  int score,
						  int lines,
						  int level,
						  int tetrisCount) {
		this.gameSpeed = gameSpeed;
		setName(name);
		this.score = score;
		this.lines = lines;
		this.level = level;
		this.tetrisCount = tetrisCount;
		this.date = null;
	}
	
	/**
	 * Compares two high scores based on score, level, and lines (in that order).
	 * @param o	HighscoreEntry object to compare to this.
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object o) {
		return compare(this, o);
	}

	/**
	 * Compares two high scores based on score, level, and lines (in that order).
	 * @param o1	First HighscoreEntry object
	 * @param o2	Second HighscoreEntry object
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		int rv = 0;
		if ((o1 instanceof HighscoreEntry) && (o2 instanceof HighscoreEntry)) {
			HighscoreEntry hs1 = (HighscoreEntry) o1;
			HighscoreEntry hs2 = (HighscoreEntry) o2;
			rv = hs1.getScore() - hs2.getScore();
			if (rv == 0) {
				rv = hs1.getLevel() - hs2.getLevel();
			}
			if (rv == 0) {
				rv = hs1.getLines() - hs2.getLines();
			}
			if (rv == 0) {
				rv = hs1.getTetrisCount() - hs2.getTetrisCount();						
			}
			if ((rv == 0) && (hs1.getDate() != null) && (hs2.getDate() != null)) {
				rv = hs1.getDate().compareTo(hs2.getDate());	
			}
		}
		return rv;
	}

	/**
	 * Compares two high scores and determines if this entry is
	 * higher in score/level/lines than hs.
	 * @param hs	high score to compare against.
	 * @return boolean if this is > hs.
	 */
	public boolean isAbove(HighscoreEntry hs) {
		return (this.compareTo(hs) >= 0);
	}


	/**
	 * Returns a String representation of the high score.
	 * Each highscore variable is separated by a tab.
	 * @return String
	 */
	@Override
	public String toString() {
		String str;
		String nm = name;
		if (!hasName)
			nm = " ";
		str = gameSpeed + DELIM + nm + DELIM + getDateString() + DELIM;
		str += score + DELIM + level + DELIM + lines + DELIM + tetrisCount;
		return str;
	}

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Gets the game speed for this highscore.
	 * @return int game speed
	 */
	public int getGameSpeed() {
		return gameSpeed;
	}

	/**
	 * Sets the game speed for this highscore.
	 * @param gameSpeed
	 */
	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		if ((name != null) && (name.length() > 0)) {
			this.name = name;
			if (!name.equals(" "))
				this.hasName = true;	
		}
	}

	/**
	 * Returns the score.
	 * @return int
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the score.
	 * @param score The score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Returns the lines.
	 * @return int
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * Sets the lines.
	 * @param lines The lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	/**
	 * Returns the level.
	 * @return int
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 * @param level The level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the tetris countr.
	 * @return int
	 */
	public int getTetrisCount() {
		return tetrisCount;
	}

	/**
	 * Sets the tetris count.
	 * @param tetrisCount
	 */
	public void setTetrisCount(int tetrisCount) {
		this.tetrisCount = tetrisCount;
	}

	/**
	 * Returns the needName.
	 * @return boolean
	 */
	public boolean hasName() {
		return hasName;
	}

	/**
	 * Returns the date.
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the date String.
	 * @return String date.
	 */
	public String getDateString() {
		String dateString = " ";
		if (date != null) {
			dateString = DATE.format(date); 	
		}
		return dateString;
	}

	/**
	 * Sets the date.
	 * @param dateString date.
	 */
	public void setDate(String dateString) {
		if ((dateString != null) && (dateString.length() > 0)) {
			try {
				this.date = DATE.parse(dateString);
			} catch (ParseException pe) {
				this.date = null;	
			}
		} else {
			this.date = null;
		}
	}

}
