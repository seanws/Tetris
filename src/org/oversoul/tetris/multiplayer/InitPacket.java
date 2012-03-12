/**
 * InitPacket.java
 * 
 * Created on 11-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import org.oversoul.networking.*;
import org.oversoul.tetris.TetrisConstants;

/**
 * Holds the information about a tetris initialization message.
 * 
 * @author Chris Callendar (9902588)
 * @date   11-Mar-04, 9:09:40 PM
 */
public class InitPacket extends Packet {
	
	/**
	 * Constructor for a new InitPacket.
	 * @param type	packet type.
	 * @param num	player number.
	 */
	public InitPacket(byte type, int num) {
		super(type);
		// initialize the size of the data portion
		switch (type) {
			case REFUSE :
			case MESSAGE :
			case READY :
			case NOT_READY :
			case START :
				data = new byte[1];
				break;
			case CONNECT :
			case ACCEPT :
				data = new byte[15];
				break;
			case SETTINGS :
				data = new byte[5];
				break;
			default :
				data = new byte[1];
				System.out.println("WARNING: unknown type: " + type);
		}
		data[NUM] = (byte) num;
	}

	/**
	 * Constructor for InitPacket.
	 * @param packet
	 */
	public InitPacket(byte[] packet) {
		super(packet);
	}
	/**
	 * Constructor for InitPacket.
	 * @param type	packet type.
	 * @param num	player number.
	 * @param str	string to append to data.
	 */
	public InitPacket(byte type, int num, String str) {
		super(type);
		byte[] s = str.getBytes();
		data = new byte[s.length+1];
		data[NUM] = (byte)num;
		System.arraycopy(s, 0, data, NUM+1, s.length);
	}

	/**
	 * Useful debugging method for seeing the packet bytes.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "[" + type + "]: " + getPlayerNumber() + " ";
		switch (type) {
			case REFUSE :
			case MESSAGE :
				s += "'" + getString(STR) + "'";
				break;
			case READY :
			case START :
				break;
			case CONNECT :
			case ACCEPT :
				s += getLevel()+" "+getLines()+" "+getSpeed()+" "+NetUtil.toBinary(data[BOOLEANS])+" ";
				s += getWins() +" "+getLosses()+" "+getTetrisTotal()+" v"+getVersion()+" '"+getString(NAME)+"'";
				break;
			case SETTINGS :
				s += getLevel()+" "+getLines()+" "+getSpeed()+" "+NetUtil.toBinary(data[BOOLEANS]);
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
	 * Sets the name.
	 * @param name	the name to set.
	 */
	public void setName(String name) {
		setString(name);	
	}
	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return getString(NAME);
	}
	/**
	 * Returns the reason.
	 * @return String
	 */
	public String getReason() {
		return getString(STR);
	}
	/**
	 * Sets the reason.
	 * @param reason the reason.
	 */
	public void setReason(String reason) {
		setString(reason);
	}
	/**
	 * Returns the message.
	 * @return String
	 */
	public String getMessage() {
		return getString(STR);
	}
	/**
	 * Sets the message.
	 * @param msg	the message to set.
	 */
	public void setMessage(String msg) {
		setString(msg);
	}
	/**
	 * Returns the starting level.
	 * @return int
	 */
	public int getLevel() {
		int level = 0;
		if (data.length > LEVEL) {
			level = data[LEVEL];		
		}
		return level;
	}
	/**
	 * Sets the starting level.
	 * @param level The starting level to set
	 */
	public void setLevel(int level) {
		if (data.length > LEVEL) {
			data[LEVEL] = (byte) level;	
		}
	}
	/**
	 * Returns the starting lines.
	 * @return int
	 */
	public int getLines() {
		int lines = 0;
		if (data.length > LINES) {
			lines = data[LINES];		
		}
		return lines;
	}	
	/**
	 * Sets the starting lines.
	 * @param lines The starting lines to set
	 */
	public void setLines(int lines) {
		if (data.length > LINES) {
			data[LINES] = (byte) lines;	
		}
	}
	/**
	 * Returns the game speed.
	 * @return int
	 */
	public int getSpeed() {
		int speed = TetrisConstants.SPEED_NORM;
		if (data.length > SPEED) {
			speed = data[SPEED];		
		}
		return speed;
	}	
	/**
	 * Sets the game speed.
	 * @param speed The game speed to set
	 */
	public void setSpeed(int speed) {
		if (data.length > SPEED) {
			data[SPEED] = (byte) speed;	
		}
	}
	/**
	 * Sets the 4 bits corresponding to the booleans.
	 * @param fixedLevel
	 * @param fixedLines
	 * @param fixedSpeed
	 * @param penalties
	 */
	public void setBooleans(boolean fixedLevel, 
							boolean fixedLines, 
							boolean fixedSpeed,
							boolean penalties) {
		if (data.length > BOOLEANS) {
			NetUtil.bit2byte(data, BOOLEANS, fixedLevel, FIXED_LEVEL_BIT);
			NetUtil.bit2byte(data, BOOLEANS, fixedLines, FIXED_LINES_BIT);
			NetUtil.bit2byte(data, BOOLEANS, fixedSpeed, FIXED_SPEED_BIT);
			NetUtil.bit2byte(data, BOOLEANS, penalties, PENALTIES_BIT);
		}
	}
	/**
	 * Returns the fixedLevel.
	 * @return boolean
	 */
	public boolean isFixedLevel() {
		boolean fixedLevel = false;
		if (data.length > BOOLEANS) {
			fixedLevel = NetUtil.byte2bit(data[BOOLEANS], FIXED_LEVEL_BIT);
		}
		return fixedLevel;
	}
	/**
	 * Returns the fixedLines.
	 * @return boolean
	 */
	public boolean isFixedLines() {
		boolean fixedLines = false;
		if (data.length > BOOLEANS) {
			fixedLines = NetUtil.byte2bit(data[BOOLEANS], FIXED_LINES_BIT);
		}
		return fixedLines;
	}
	/**
	 * Returns if the game speed is fixed.
	 * @return boolean
	 */
	public boolean isFixedSpeed() {
		boolean fixedSpeed = false;
		if (data.length > BOOLEANS) {
			fixedSpeed = NetUtil.byte2bit(data[BOOLEANS], FIXED_SPEED_BIT);
		}
		return fixedSpeed;
	}
	/**
	 * Returns the penalties.
	 * @return boolean
	 */
	public boolean isPenalties() {
		boolean penalties = false;
		if (data.length > BOOLEANS) {
			penalties = NetUtil.byte2bit(data[BOOLEANS], PENALTIES_BIT);
		}
		return penalties;
	}
	/**
	 * Returns the wins.
	 * @return int wins.
	 */
	public int getWins() {
		int wins = 0;
		if (data.length > (WINS+1)) {
			wins = NetUtil.bytes2short(data, WINS);		
		}
		return wins;
	}	
	/**
	 * Sets the wins.
	 * @param wins The wins to set
	 */
	public void setWins(int wins) {
		if (data.length > WINS) {
			NetUtil.short2bytes(data, WINS, (short)wins);
		}
	}
	/**
	 * Returns the losses.
	 * @return int losses.
	 */
	public int getLosses() {
		int losses = 0;
		if (data.length > LOSSES) {
			losses = NetUtil.bytes2short(data, LOSSES);		
		}
		return losses;
	}	
	/**
	 * Sets the losses.
	 * @param losses The losses to set
	 */
	public void setLosses(int losses) {
		if (data.length > LOSSES) {
			NetUtil.short2bytes(data, LOSSES, (short)losses);
		}
	}
	/**
	 * Returns the tetris total.
	 * @return int tetris total.
	 */
	public int getTetrisTotal() {
		int tetrises = 0;
		if (data.length > TETRISES) {
			tetrises = NetUtil.bytes2int(data, TETRISES);		
		}
		return tetrises;
	}	
	/**
	 * Sets the tetris total.
	 * @param tetrises The tetris total to set
	 */
	public void setTetrisTotal(int tetrises) {
		if (data.length > TETRISES) {
			NetUtil.int2bytes(data, TETRISES, tetrises);
		}
	}
	/**
	 * Returns the version.
	 * @return String version.
	 */
	public String getVersion() {
		byte major = 0;
		byte minor = 0;
		if (data.length > VERSION) {
			major = data[VERSION];
		}
		if (data.length > (VERSION+1)) {
			minor = data[VERSION+1];	
		}
		return new String(major + "." + minor);
	}	
	/**
	 * Sets the version number.
	 * @param major the major version number
	 * @param minor the minor version number
	 */
	public void setVersion(byte major, byte minor) {
		if (data.length > VERSION) {
			data[VERSION] = major;
		}
		if (data.length > (VERSION+1)) {
			data[VERSION+1] = minor;	
		}
	}

	//////////////////////////////////////////////////////////////////////
	// CONSTANTS 
	//////////////////////////////////////////////////////////////////////

	// connect/initialization phase
	private static final byte CONNECT 	= TetrisCommunication.CONNECT;
	private static final byte ACCEPT 	= TetrisCommunication.ACCEPT;
	private static final byte REFUSE 	= TetrisCommunication.REFUSE;
	private static final byte SETTINGS	= TetrisCommunication.SETTINGS;
	private static final byte MESSAGE	= TetrisCommunication.MESSAGE;
	private static final byte READY		= TetrisCommunication.READY;
	private static final byte NOT_READY = TetrisCommunication.NOT_READY;
	private static final byte START		= TetrisCommunication.START;

	// indices
	private static final int NUM 		= 0;
	private static final int STR 		= 1;
	private static final int LEVEL 		= 1;
	private static final int LINES	 	= 2;
	private static final int SPEED	 	= 3;
	private static final int BOOLEANS	= 4;
	private static final int WINS		= 5;
	private static final int LOSSES		= 7;
	private static final int TETRISES	= 9;
	private static final int VERSION	= 13;
	private static final int NAME		= 15;

	private static final int FIXED_LEVEL_BIT = 0;
	private static final int FIXED_LINES_BIT = 1;
	private static final int FIXED_SPEED_BIT = 2;
	private static final int PENALTIES_BIT	 = 3;
}
