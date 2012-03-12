/**
 * FilePacket.java
 * 
 * Created on 24-Jan-05
 */
package org.oversoul.tetris.update;

import org.oversoul.networking.NetUtil;
import org.oversoul.networking.Packet;
import org.oversoul.tetris.multiplayer.TetrisCommunication;

/**
 * Holds the information about tetris update messages.
 * 
 * @author 		nyef
 * @date		24-Jan-05, 4:00:41 PM
 * @version 	1.0
 */
public class UpdatePacket extends Packet {

	/**
	 * Constructor for FilePacket.java
	 * @param type
	 */
	public UpdatePacket(byte type) {
		super(type);
		// initialize the size of the data portion
		switch (type) {
			case CHECK_UPDATE :
			case NO_UPDATE :
			case AVAIL_UPDATE :
			case GET_UPDATE :
				data = new byte[2];
				break;
			case UPDATE_FILES :
			case SEND_FILES :
			case DONE_UPDATE :
				data = new byte[6];
				break;
			case FILE :
				data = new byte[14];
				break;
			case RECV_FILE :
			case RESEND_FILE :
				data = new byte[12];
				break;
			case UPDATE_ERROR :
				data = new byte[0];
				break;
			default :
				data = new byte[0];
				System.out.println("WARNING: unknown type: " + type);
		}		
	}
	
	/**
	 * Useful debugging method for seeing the packet bytes.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "[" + type + "]: ";
		switch (type) {
			case CHECK_UPDATE :
			case NO_UPDATE :
			case AVAIL_UPDATE :
			case GET_UPDATE :
				s += getVersion();
				break;
			case UPDATE_FILES :
			case SEND_FILES :
			case DONE_UPDATE :
				s += getFiles()+" "+getSize();
				break;
			case FILE :
				s += getFiles()+" "+getSize()+getFileNumber()+" "+getBlockNumber();
				s += " "+getBlocks()+" "+getFilenameChars()+" "+getFilename();
				break;
			case RECV_FILE :
			case RESEND_FILE :
				s += getFiles()+" "+getSize()+getFileNumber()+" "+getBlockNumber()+" "+getBlocks();
				break;
			case UPDATE_ERROR :
				s += "'" + getString(UPDATE_ERROR) + "'";
				break;
		}
		return s;
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
	
	/**
	 * Returns a String.
	 * @return String
	 */
	private String getString(int pos) {
		return getString(pos, data.length-pos);
	}
	/**
	 * Returns a string from the start position with the given length.
	 * @param startPos	the starting position of the string.
	 * @param length	the length of the string.
	 * @return String
	 */
	private String getString(int startPos, int length) {
		String str = "";
		if ((data.length > startPos) && (length > 0)) {
			int endPos = startPos + length;
			if (endPos > data.length) {
				endPos = data.length;
			}
			byte[] b = new byte[endPos-startPos];
			System.arraycopy(data, startPos, b, 0, b.length);
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
	 * Returns the update error message.
	 * @return String
	 */
	public String getUpdateError() {
		return getString(UPDATE_ERROR);
	}
	/**
	 * Sets the update error message.
	 * @param msg	the message to set.
	 */
	public void setUpdateError(String msg) {
		setString(msg);
	}
	
	/**
	 * Gets the number of files.
	 * @return int number of files
	 */
	public int getFiles() {
		int files = 0;
		if (data.length > FILES) {
			files = NetUtil.bytes2short(data, FILES);	
		}
		return files;
	}
	/**
	 * Sets the number of files.
	 * @param files number of files
	 */
	public void setFiles(int files) {
		if (data.length > FILES) {
			NetUtil.short2bytes(data, FILES, (short)files);	
		}
	}
	/**
	 * Gets the file size(s).
	 * @return int
	 */
	public int getSize() {
		int size = 0;
		if (data.length > SIZE) {
			size = NetUtil.bytes2int(data, SIZE);		
		}
		return size;	
	}
	/**
	 * Sets the file size(s).
	 * @param size
	 */
	public void setSize(int size) {
		if (data.length > SIZE) {
			NetUtil.int2bytes(data, SIZE, size);
		}
	}
	
	/**
	 * Gets the file number.
	 * @return int
	 */
	public int getFileNumber() {
		int fileNumber = 0;
		if (data.length > FILE_NUM) {
			fileNumber = NetUtil.bytes2short(data, FILE_NUM);	
		}
		return fileNumber;	
	}
	/**
	 * Sets the file number.
	 * @param fileNumber
	 */
	public void setFileNumber(int fileNumber) {
		if (data.length > FILE_NUM) {
			NetUtil.short2bytes(data, FILE_NUM, (short)fileNumber);
		}
	}
	
	/**
	 * Gets the block number.
	 * @return int
	 */
	public int getBlockNumber() {
		int blockNum = 0;
		if (data.length > BLOCK_NUM) {
			blockNum = NetUtil.bytes2short(data, BLOCK_NUM);
		}
		return blockNum;
	}
	/**
	 * Sets the block number.
	 * @param blockNumber
	 */
	public void setBlockNumber(int blockNumber) {
		if (data.length > BLOCK_NUM) {
			NetUtil.short2bytes(data, BLOCK_NUM, (short)blockNumber);
		}
	}
	
	/**
	 * Gets the number of blocks.
	 * @return int
	 */
	public int getBlocks() {
		int blocks = 0;
		if (data.length > BLOCKS) {
			blocks = NetUtil.bytes2short(data, BLOCKS);
		}
		return blocks; 
	}
	/**
	 * Sets the number of blocks.
	 * @param blocks
	 */
	public void setBlocks(int blocks) {
		if (data.length > BLOCKS) {
			NetUtil.short2bytes(data, BLOCKS, (short)blocks);
		}
	}
	
	/**
	 * Gets the number of characters in the filename.
	 * @return int
	 */
	public int getFilenameChars() {
		int chars = 0;
		if (data.length > FILENAME_CHARS) {
			chars = NetUtil.bytes2short(data, FILENAME_CHARS);
		}
		return chars;
	}
	/**
	 * Gets the filename.
	 * @return
	 */
	public String getFilename() {
		String filename = "";
		if (data.length > FILENAME) {
			filename = getString(FILENAME, getFilenameChars());
		}
		return filename;	
	}
	/**
	 * Sets the filename, filename length, and file data.
	 * @param filename
	 * @param fileData
	 */
	public void setFile(String filename, byte[] fileData) {
		byte[] b = filename.getBytes();
		if (b.length > 0) {
			NetUtil.short2bytes(data, FILENAME_CHARS, (short)b.length);
			byte[] dest = new byte[data.length + b.length + fileData.length];
			System.arraycopy(data, 0, dest, 0, data.length);
			System.arraycopy(b, 0, dest, data.length, b.length);
			this.data = dest;
		}
	}
	/**
	 * 
	 * @return
	 */
	public byte[] getFileData() {
		return null;	
	}
	
	//////////////////////////////////////////////////////////////////////
	// CONSTANTS 
	//////////////////////////////////////////////////////////////////////

	// connect/initialization phase
	private static final byte CHECK_UPDATE	= TetrisCommunication.CHECK_UPDATE;
	private static final byte NO_UPDATE		= TetrisCommunication.NO_UPDATE;
	private static final byte AVAIL_UPDATE 	= TetrisCommunication.AVAIL_UPDATE;
	private static final byte GET_UPDATE		= TetrisCommunication.GET_UPDATE;
	private static final byte UPDATE_FILES	= TetrisCommunication.UPDATE_FILES;
	private static final byte SEND_FILES		= TetrisCommunication.SEND_FILES;
	private static final byte FILE			= TetrisCommunication.FILE;
	private static final byte RECV_FILE		= TetrisCommunication.RECV_FILE;
	private static final byte RESEND_FILE	= TetrisCommunication.RESEND_FILE;
	private static final byte DONE_UPDATE	= TetrisCommunication.DONE_UPDATE;
	private static final byte UPDATE_ERROR	= TetrisCommunication.UPDATE_ERROR;

	// indices
	private static final int VERSION		= 0;
	private static final int FILES 			= 0;
	protected static final int ERROR_MSG		= 0;
	private static final int SIZE	 		= 2;
	private static final int FILE_NUM		= 6;
	private static final int BLOCK_NUM		= 8;
	private static final int BLOCKS			= 10;
	private static final int FILENAME_CHARS	= 12;
	private static final int FILENAME		= 14;
	protected static final int DATA			= 14;
	
}
