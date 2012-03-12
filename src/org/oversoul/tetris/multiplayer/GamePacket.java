/**
 * GamePacket.java
 * 
 * Created on 14-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import org.oversoul.networking.NetUtil;
import org.oversoul.networking.Packet;

/**
 * 
 * 
 * @author Chris Callendar (9902588)
 * @date   14-Mar-04, 4:29:17 PM
 */
public class GamePacket extends Packet {

	//////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////

	/**
	 * Constructor for GamePacket.
	 * @param type	packet type.
	 * @param num	player number.
	 */
	public GamePacket(byte type, int num) {
		super(type);
		switch (type) {
			case GAME_START :
			case PAUSE :
			case RESUME :
			case RESTART :
			case END_GAME :
			case GAME_MSG :
				this.data = new byte[1];
				break;
			case GAME_OVER :
				this.data = new byte[8];
				break;
			case SIMPLE_SCORE :
				this.data = new byte[4];
				break;
			case FULL_SCORE :
				this.data = new byte[9];
				break;
		}
		this.data[NUM] = (byte) num;
	}

	/**
	 * Constructor for GamePacket.
	 * @param packet
	 */
	public GamePacket(byte[] packet) {
		super(packet);
	}

	/**
	 * Constructor for GamePacket.
	 * @param type	packet type.
	 * @param num	player number.
	 * @param str	string to append to data.
	 */
	public GamePacket(byte type, int num, String str) {
		super(type);
		byte[] s = str.getBytes();
		data = new byte[s.length+1];
		data[NUM] = (byte) num;
		System.arraycopy(s, 0, data, NUM+1, s.length);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "[" + type + "]: " + getPlayerNumber() + " ";
		switch (type) {
			case GAME_START :
			case PAUSE :
			case END_GAME :
			case RESTART :
				break;
			case GAME_MSG :
				s += "'" + getErrorMessage() + "'";
				break;
			case GAME_OVER :
				s += getScore() +" "+ getWeightedScore() +" "+ getLines() +" "+ getLevel() +" "+ getTetrisCount();
				break;
			case SIMPLE_SCORE :
				s += getScore() +" "+ getWeightedScore();
				break;
			case FULL_SCORE :
				s += getScore() +" "+ getWeightedScore() +" "+ getLines() +" "+ 
					 getLevel() +" "+ getTetrisCount() +" "+ getPenaltyLines();
				break;
		}
		return s;
	}

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Returns the playerNumber.
	 * @return int
	 */
	public int getPlayerNumber() {
		int playerNumber = 0;
		if (data.length > NUM) {
			playerNumber = data[NUM];	
		}
		return playerNumber;
	}
	/**
	 * Sets the playerNumber.
	 * @param playerNumber The playerNumber to set
	 */
	public void setPlayerNumber(int playerNumber) {
		if (data.length > NUM) {
			data[NUM] = (byte) playerNumber;	
		}
	}
	/**
	 * Returns a String.
	 * @return String
	 */
	private String getString(int pos) {
		String str = "";
		if (data.length > pos) {
			byte[] b = new byte[data.length-pos];
			System.arraycopy(data, pos, b, 0, b.length);
			str = new String(b);
		}
		return str.trim();
	}
	/**
	 * Adds the String onto the end of the data byte array.
	 * @param str
	 */
	public void setString(String str) {
		byte[] b = str.getBytes();
		byte[] dest = new byte[data.length + b.length];
		System.arraycopy(data, 0, dest, 0, data.length);
		System.arraycopy(b, 0, dest, data.length, b.length);
		this.data = dest;
	}

	/**
	 * Gets the error message.
	 * @return String	error message.
	 */
	public String getErrorMessage() {
		return getString(STR);
	}
	/**
	 * Gets the message.
	 * @return String	message.
	 */
	public String getMessage() {
		return getString(STR);
	}

	/**
	 * Gets the score (2 bytes) from the data array and return the int.
	 * @return int the score.
	 */
	public int getScore() {
		int score = 0;
		if (data.length > (SCORE+1)) {
			score = NetUtil.bytes2short(data, SCORE);	
		}
		return score;
	}
	/**
	 * Sets the score using 2 bytes (short).
	 * @param score	the score to set
	 */
	public void setScore(int score) {
		if (data.length > (SCORE+1)) {
			NetUtil.short2bytes(data, SCORE, (short) score);	
		}
	}
	/**
	 * Gets the weighted score (1 byte).
	 * @return int	 weighted score from 0 to 100.
	 */
	public int getWeightedScore() {
		int wscore = 0;
		if (data.length > WSCORE) {
			wscore = data[WSCORE];
		}
		return wscore;
	}
	/**
	 * Sets the weighted score.
	 * @param wscore	the weighted score to set
	 */
	public void setWeightedScore(int wscore) {
		if (data.length > WSCORE) {
			data[WSCORE] = (byte) wscore;
		}
	}
	/**
	 * Gets the lines (2 bytes) from the data array and return the int.
	 * @return int the lines.
	 */
	public int getLines() {
		int lines = 0;
		if (data.length > (LINES+1)) {
			lines = NetUtil.bytes2short(data, LINES);	
		}
		return lines;
	}
	/**
	 * Sets the lines using 2 bytes (short).
	 * @param lines the lines to set
	 */
	public void setLines(int lines) {
		if (data.length > (LINES+1)) {
			NetUtil.short2bytes(data, LINES, (short) lines);	
		}
	}
	/**
	 * Gets the level (1 byte).
	 * @return int	 level score from 0 to 10.
	 */
	public int getLevel() {
		int level = 0;
		if (data.length > LEVEL) {
			level = data[LEVEL];
		}
		return level;
	}
	/**
	 * Sets the level.
	 * @param level	the level.
	 */
	public void setLevel(int level) {
		if (data.length > LEVEL) {
			data[LEVEL] = (byte) level;
		}
	}
	/**
	 * Gets the tetris count (1 byte).
	 * @return int	 tetris count
	 */
	public int getTetrisCount() {
		int tc = 0;
		if (data.length > TETRISES) {
			tc = data[TETRISES];
		}
		return tc;
	}
	/**
	 * Sets the tetris count.
	 * @param tetrisCount the tetris count.
	 */
	public void setTetrisCount(int tetrisCount) {
		if (data.length > TETRISES) {
			data[TETRISES] = (byte)tetrisCount;
		}
	}
	/**
	 * Gets the penalty lines (0-4).
	 * @return int	 the number of penalty lines (0-4).
	 */
	public int getPenaltyLines() {
		int plines = 0;
		if (data.length > PENALTIES) {
			plines = data[PENALTIES];
		}
		return plines;
	}
	/**
	 * Sets the number of penalty lines.
	 * @param plines	the number of penalty lines.
	 */
	public void setPenaltyLines(int plines) {
		if (data.length > PENALTIES) {
			data[PENALTIES] = (byte) plines;
		}
	}
	

	//////////////////////////////////////////////////////////////////////
	// CONSTANTS
	//////////////////////////////////////////////////////////////////////
	
	// game messages
	private static final byte GAME_START	= TetrisCommunication.GAME_START;
	private static final byte PAUSE			= TetrisCommunication.PAUSE;
	private static final byte RESUME		= TetrisCommunication.RESUME;
	private static final byte RESTART		= TetrisCommunication.RESTART;
	private static final byte END_GAME		= TetrisCommunication.END_GAME;
	private static final byte GAME_OVER		= TetrisCommunication.GAME_OVER;
	private static final byte GAME_MSG		= TetrisCommunication.GAME_MSG;
	
	// game scoring messages
	private static final byte SIMPLE_SCORE	= TetrisCommunication.SIMPLE_SCORE;
	private static final byte FULL_SCORE	= TetrisCommunication.FULL_SCORE;
	
	// array indices
	private static final int NUM 		= 0;
	private static final int SCORE 		= 1;
	private static final int STR		= 1;
	private static final int WSCORE		= 3;
	private static final int LINES	 	= 4;
	private static final int LEVEL		= 6;
	private static final int TETRISES	= 7;
	private static final int PENALTIES 	= 8;
	
}
