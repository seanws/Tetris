/**
 * TermPacket.java
 * 
 * Created on 11-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import org.oversoul.networking.*;

/**
 * Holds the information about a tetris termination message.
 * 
 * @author Chris Callendar (9902588)
 * @date   11-Mar-04, 9:09:40 PM
 */
public class TermPacket extends Packet {
	
	/**
	 * Constructor for a new TermPacket.
	 * @param type	packet type.
	 * @param num	player number.
	 */
	public TermPacket(byte type, int num) {
		super(type);
		// initialize the size of the data portion
		switch (type) {
			case DROPPED :
			case CANCEL :
			case QUIT :
			case ERROR_MSG :
				data = new byte[1];
				break;
		}
		data[NUM] = (byte) num;
	}

	/**
	 * Constructor for TermPacket.
	 * @param packet
	 */
	public TermPacket(byte[] packet) {
		super(packet);
	}
	/**
	 * Constructor for TermPacket.
	 * @param type	packet type.
	 * @param num	player number.
	 * @param str	string to append to data.
	 */
	public TermPacket(byte type, int num, String str) {
		super(type);
		byte[] s = str.getBytes();
		data = new byte[s.length+1];
		data[NUM] = (byte)num;
		System.arraycopy(s, 0, data, NUM+1, s.length);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "[" + type + "]: " + getPlayerNumber() + " ";
		switch (type) {
			case ERROR_MSG :
			case DROPPED :
				s += "'" + getString(STR) + "'";
				break;
			case CANCEL :
			case QUIT :
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
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return getString(STR);
	}
	/**
	 * Sets the name.
	 * @param name	the name to set.
	 */
	public void setName(String name) {
		setString(name);	
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
	 * Gets the error message.
	 * @return String	error message.
	 */
	public String getErrorMessage() {
		return getString(STR);
	}
	/**
	 * Sets the error message.
	 * @param error the error message.
	 */
	public void setErrorMessage(String error) {
		setString(error);
	}

	//////////////////////////////////////////////////////////////////////
	// CONSTANTS 
	//////////////////////////////////////////////////////////////////////

	// connect/initialization phase
	private static final byte DROPPED	= TetrisCommunication.DROPPED;
	private static final byte CANCEL	= TetrisCommunication.CANCEL;
	private static final byte QUIT		= TetrisCommunication.QUIT;
	private static final byte ERROR_MSG	= TetrisCommunication.ERROR_MSG;

	// indices
	private static final int NUM 		= 0;
	private static final int STR 		= 1;

}
