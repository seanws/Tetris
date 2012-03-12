/**
 * Version.java
 * 
 * Created on 23-Jan-05
 */
package org.oversoul.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Loads the version information.
 * 
 * @author 		nyef
 * @date		23-Jan-05, 5:38:46 PM
 * @version 	1.0
 */
public class Version {

	private byte major = 1;
	private byte minor = 0;
	private byte build = 0;
	private Date date = null;
	private String dateString = "";
	
	private int port = 7331;
	
	/**
	 * Sets the default version (1.0.0) and date (today).
	 */
	public Version() {
		this.major = 1;
		this.minor = 0;
		this.build = 0;
		this.date = new Date();
		this.dateString = DateFormat.getDateInstance(DateFormat.LONG).format(this.date);
	}

	/**
	 * Creates a new Version file from the given properties.
	 * @param props
	 * @throws Exception
	 */
	public Version(Properties props) throws Exception {
		loadVersionProperties((props == null ? new Properties() : props));
	}
	
	/**
	 * Constructor for Version.java
	 */
	public Version(String versionFile) throws Exception {
		Properties props = new Properties();
		props.load(new BufferedInputStream(new FileInputStream(versionFile)));
		loadVersionProperties(props);
	}

	/**
	 * Loads the version properties.
	 */
	private void loadVersionProperties(Properties props) throws Exception {
		String maj = props.getProperty("version.major", "1");
		String min = props.getProperty("version.minor", "0");
		String bld = props.getProperty("version.build", "0");
		try {
			this.major = Byte.parseByte(maj);
			this.minor = Byte.parseByte(min);
			this.build = Byte.parseByte(bld);
		} catch (NumberFormatException ex) {
			this.major = 1;
			this.minor = 0;
			this.build = 0;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		try {
			this.date = sdf.parse(props.getProperty("version.date", "01/01/2005"));
			this.dateString = df.format(date);			
		} catch (Exception ex) {
			System.err.println("Error with version date: " + ex.getMessage());
			this.date = null;
			this.dateString = "";
		}
		
		try {
			this.port = Integer.parseInt(props.getProperty("server.port", "7331"));	
		} catch (NumberFormatException nfe) {
			this.port = 7331;
		}			
	}

	/**
	 * Gets the version date.
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Gets the version date String.
	 * @return String
	 */
	public String getDateString() {
		return dateString;
	}

	/**
	 * Gets the major version.
	 * @return byte
	 */
	public byte getMajor() {
		return major;
	}

	/**
	 * Gets the minor version.
	 * @return byte
	 */
	public byte getMinor() {
		return minor;
	}

	/**
	 * Gets the build version number.
	 * @return byte
	 */
	public byte getBuild() {
		return build;	
	}

	/**
	 * Gets the update server port.
	 * @return int
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getMajor() + "." + getMinor() + "." + getBuild();	
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj instanceof Version) {
			Version v = (Version)obj;
			boolean sameString = this.toString().equals(v.toString());
			boolean sameDate = (this.getDate() != null) && this.getDate().equals(v.getDate());
			equal = sameString && sameDate;
		} else if (obj instanceof String) {
			String s = (String)obj;
			equal = this.toString().equals(s);	
		}
		return equal;
	}

}
