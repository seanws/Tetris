/**
 * Highscores.java
 * 
 * Created on 16-Feb-04
 */
package org.oversoul.tetris.highscores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.oversoul.tetris.TetrisConstants;

/**
 * Stores the high scores in a LinkedList. 
 * 
 * @author Chris Callendar (9902588)
 * @date   16-Feb-04, 1:14:23 PM
 */
public class Highscores {

	protected static final int GAME_SPEEDS = 3;

	private Vector<LinkedList<HighscoreEntry>> allScores = null;

	/** The maximum number of entries in the list. */
	private int maxEntries = 20;
	
	/** The number of entries to display. */
	private int visibleEntries = 10;
	
	/** If the high scores have been changed. */
	private boolean changed = false;

	/**
	 * Constructor for Highscores.
	 */
	public Highscores() {
		this(20, 10);
	}

	/**
	 * Constructor for Highscores.
	 * @param maxEntries	Maximum number of high scores.
	 * @param visibleEntries number of entries to display
	 */
	public Highscores(int maxEntries, int visibleEntries) {
		this.allScores = new Vector<LinkedList<HighscoreEntry>>(GAME_SPEEDS);
		this.maxEntries = maxEntries;
		this.visibleEntries = Math.min(maxEntries, visibleEntries);
		init();
	}

	/**
	 * Initializes the high scores linked list.
	 */
	private void init() {
		allScores.clear();
		for (int i = 0; i < GAME_SPEEDS; i++) {
			 LinkedList<HighscoreEntry> scores = new LinkedList<HighscoreEntry>();
			for (int j = maxEntries; j > 0; j--) {
				HighscoreEntry hs = new HighscoreEntry(i, "", -1, -1, -1, -1);
				scores.add(hs);
			}
			allScores.add(i, scores);	
		}
	}
	
	/**
	 * Clears the high scores for the current game speed.
	 * @param gameSpeed
	 */
	public void clearHighScores(int gameSpeed) {
		LinkedList<HighscoreEntry> scores = allScores.get(gameSpeed);
		scores.clear();
		for (int i = maxEntries; i > 0; i--) {
			HighscoreEntry hs = new HighscoreEntry(gameSpeed, "", -1, -1, -1, -1);
			scores.add(hs);
		}
		changed = true;
	}

	/**
	 * Clears all the high scores.
	 */
	public void clearAllHighScores() {
		for (int i = 0; i < allScores.size(); i++) {
			clearHighScores(i);
		}
		changed = true;
	}
	
	/**
	 * Adds a high score entry if it makes the cut.
	 * @param entry	The HighscoreEntry object.
	 * @param gameSpeed the game speed
	 * @return int The position that this entry was added or -1 if not added.
	 */
	public int addHighScore(HighscoreEntry entry, int gameSpeed) {
		int added = -1;
		LinkedList<HighscoreEntry> scores = allScores.get(gameSpeed);
		for (int i = 0; i < scores.size(); i++) {
			HighscoreEntry current = scores.get(i);
			if (entry.isAbove(current)) {
				scores.add(i, entry);	
				changed = true;
				added = (i+1);
				break;
			}
		}
		if ((added == -1) && (scores.size() < maxEntries)) {
			scores.add(entry);
			changed = true;
			added = scores.size();
		}
		// remove any excess entries
		while (scores.size() > maxEntries) {
			scores.removeLast();
			changed = true;
		}

		return added;
	}
	
	/**
	 * Gets an array of High Score entries from the highest to the lowest.
	 * @param gameSpeed
	 * @return HighscoreEntry[]
	 */
	public HighscoreEntry[] getHighScores(int gameSpeed) {
		LinkedList scores = allScores.get(gameSpeed);
		HighscoreEntry[] hs = new HighscoreEntry[scores.size()];
		for (int i = 0; i < hs.length; i++) {
			hs[i] = (HighscoreEntry) scores.get(i);
		}
		return hs;	
	}

	/**
	 * Gets a High Score entry.
	 * @param gameSpeed the game speed.
	 * @param pos	The entry to get or null if invalid.
	 * @return HighscoreEntry or null.
	 */
	public HighscoreEntry getHighScore(int gameSpeed, int pos) {
		HighscoreEntry entry = null;
		LinkedList scores = allScores.get(gameSpeed);
		if ((pos >= 0) && (pos < scores.size())) {
			entry = (HighscoreEntry) scores.get(pos);	
		} else {
			System.out.println("Invalid high score: " + pos);	
		}
		return entry;
	}
	
	/**
	 * Gets the lowest score in the highscore list.
	 * @param gameSpeed
	 * @return int lowest score
	 */
	public int getLowestScore(int gameSpeed) {
		int lowestScore = 0;
		LinkedList scores = allScores.get(gameSpeed);
		if ((scores.size() > 0) && (scores.size() == maxEntries)) {
			HighscoreEntry lowest = (HighscoreEntry)scores.getLast();
			lowestScore = lowest.getScore();
		}
		return lowestScore;	
	}

	/**
	 * Gets the lowest score in the highscore list.
	 * @param gameSpeed
	 * @return int lowest score
	 */
	public int getLowestVisibleScore(int gameSpeed) {
		int lowestScore = 0;
		LinkedList scores = allScores.get(gameSpeed);
		if ((scores != null) && (scores.size() > 0) && (scores.size() >= visibleEntries)) {
			HighscoreEntry lowest = (HighscoreEntry)scores.get(visibleEntries-1);
			lowestScore = lowest.getScore();
		}
		return lowestScore;	
	}
		
	/**
	 * Returns the number of high scores.
	 * @param gameSpeed
	 * @return int number of high scores
	 */
	public int getCount(int gameSpeed) {
		LinkedList scores = allScores.get(gameSpeed);
		return (scores != null ? scores.size() : 0);	
	}
	
	/**
	 * Gets the visible highscore entries.
	 * @return int visible entries
	 */
	public int getVisibleEntries() {
		return visibleEntries;	
	}

	/**
	 * Gets the maximum highscore entries.
	 * @return int max entries
	 */
	public int getMaxEntries() {
		return maxEntries;	
	}
	
	/**
	 * This should only be used for debugging purposes.
	 * @param changed
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * Saves the high scores list to disk.
	 * @param file	The file to save to.
	 * @throws Exception couldn't save.
	 */
	public synchronized void save(File file) throws Exception {
		if (changed) {
			changed = false;
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			for (int i = 0; i < allScores.size(); i++) {
				LinkedList scores = allScores.get(i);
				for (int j = 0; j < scores.size(); j++) {
					HighscoreEntry entry = (HighscoreEntry) scores.get(j);
					out.println(entry.toString());
				}
				out.println();
			}
			out.close();
		}
	}
	
	/**
	 * Loads the highscores from the given lines of a the highscores file.
	 * @param lines the lines read in from the highscores text file
	 */
	public void load(String[] lines) throws Exception {
		for (LinkedList<HighscoreEntry> ll : allScores) {
			ll.clear();
		}

		if ((lines != null) && (lines.length > 0)) {
			for (String line : lines) {
				HighscoreEntry entry = parseHighScore(line);
				addHighScore(entry, entry.getGameSpeed());
			}
			changed = false;
		} else {
			init();
			changed = true;	
		}
	}

	/**
	 * Parse the String into a HighScore entry.
	 * @param str	The String to parse.
	 * @return HighscoreEntry
	 */
	private HighscoreEntry parseHighScore(String str) {
		HighscoreEntry entry = null;
		StringTokenizer toke = new StringTokenizer(str, HighscoreEntry.DELIM);
		int gameSpeed = TetrisConstants.SPEED_NORM;
		String name = "";
		String date = "";
		int score = -1;
		int level = -1;
		int lines = -1;
		int tetrisCount = -1;
		if (toke.countTokens() >= 6) {
			String firstToken = toke.nextToken();
			
			// check for game speed
			boolean foundGameSpeed = false;
			if (firstToken.length() == 1) {
				try {
					gameSpeed = Integer.parseInt(firstToken);
					foundGameSpeed = true;
				} catch (NumberFormatException nfe) {}
			}
			if (foundGameSpeed) {
				name = toke.nextToken();
			} else {
				name = firstToken;
			}
			date = toke.nextToken();
			try {
				score = Integer.parseInt(toke.nextToken());
				level = Integer.parseInt(toke.nextToken());
				lines = Integer.parseInt(toke.nextToken());
				if (toke.hasMoreTokens())
					tetrisCount = Integer.parseInt(toke.nextToken());
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();	
			}
		} else {
			//System.out.println("Invalid number of tokens: " + toke.countTokens());	
		}
		entry = new HighscoreEntry(gameSpeed, name, score, lines, level, tetrisCount);
		entry.setDate(date);
		return entry;
	}

}
