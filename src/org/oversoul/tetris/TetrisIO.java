/**
 * TetrisLoader.java
 * 
 * Created on 25-Oct-2005
 */
package org.oversoul.tetris;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.oversoul.tetris.highscores.Highscores;
import org.oversoul.tetris.util.Configuration;
import org.oversoul.tetris.util.Util;
import org.oversoul.util.Version;

/**
 * This is the class that loads tetris.  It contains the main method.
 * It reads in the properties files, the version file, and the highscores file
 * (if they exist) and sets up the tetris configuration.
 * 
 * @author 		ccallendar
 * @date		25-Oct-2005, 11:00:44 AM
 * @version 	1.0
 */
public class TetrisIO {

	private static final String VERSION_FILE = "/version.properties";
	private static final String CONFIG_FILE = "/config/tetris.properties";
	private static final String HIGHSCORES_FILE = "/config/highscores.lst";
	
	private File workingDir = null;

	/**
	 * Initializes this class with the given working directory.
	 * The working directory must be present for saving files. 
	 * @param workingDir the working directory or null if no saving of files
	 */
	public TetrisIO(File workingDir) {
		this.workingDir = workingDir;
	}
	
	/**
	 * Loads the version properties from version.properties file.
	 * @return Version
	 */
	public Version loadVersion(IDebugger debugger) {
		Version version = new Version();
		String msg = "Loading version... ";
		try {
			InputStream is = TetrisIO.class.getResourceAsStream(VERSION_FILE);
			if (is != null) {
				Properties props = new Properties();
				props.load(is);
				version = new Version(props);
			}
			debugger.debug(msg + "v" + version + ", done.");
		} catch (Exception e) {
			debugger.debug(msg);
			debugger.debug("Error loading version: ", e);
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * Loads the properties from file.
	 */
	public Configuration loadProperties(IDebugger debugger) {
		Configuration config = new Configuration();
		String msg = "Loading configuration... ";
		try {
			InputStream is = TetrisIO.class.getResourceAsStream(CONFIG_FILE);
			if ((is == null) && (workingDir != null)) {
				File file = new File(workingDir, CONFIG_FILE);
				is = new BufferedInputStream(new FileInputStream(file));
			}
			if (is == null)
				throw new Exception("Input stream is null.");

			Properties props = new Properties();
			props.load(is);
			config.load(props);
			debugger.debug(msg + "done.");
		} catch (Exception e) {
			debugger.debug(msg);
			debugger.debug("Error loading properties: ", e);
			e.printStackTrace();
		}
		return config;
	}

	/**
	 * Loads the highscores file.
	 * @return Highscores
	 */
	public Highscores loadHighscores(IDebugger debugger) {
		Highscores highscores = new Highscores();
		String msg = "Loading highscores... ";
		try {
			InputStream is = TetrisIO.class.getResourceAsStream(HIGHSCORES_FILE);
			if ((is == null) && (workingDir != null)) {
				File file = new File(workingDir, HIGHSCORES_FILE);
				is = new BufferedInputStream(new FileInputStream(file));
			}
			
			highscores.load(Util.readTextFile(is));
			debugger.debug(msg + "done.");
		} catch (Exception e) {
			debugger.debug(msg);
			debugger.debug("Error loading highscores: ", e);
			e.printStackTrace();
		}
		return highscores;
	}

	
	/**
	 * Saves the properties to a file.
	 */
	public void saveProperties(IDebugger debugger, Configuration config) {
		String msg = "Saving properties... ";
		try {
			if (workingDir != null) {
				File file = new File(workingDir, CONFIG_FILE);
				msg += "(" + file.getCanonicalPath() + ")";
				config.save(file);
				debugger.debug(msg + " done");
			}
		} catch (Exception e) {
			debugger.debug(msg);
			debugger.debug("Error saving properties: ", e);
			//e.printStackTrace();
		}
	}
	
	/**
	 * Saves the highscores.
	 * @param highscores the highscores to save
	 */
	public void saveHighscores(IDebugger debugger, Highscores highscores) {
		String msg = "Saving highscores... ";
		try {
			if (workingDir != null) {
				File file = new File(workingDir, HIGHSCORES_FILE);
				msg +=  "(" + file.getCanonicalPath() + ") ";
				highscores.save(file);
				debugger.debug(msg + "done.");
			}
		} catch (Exception e) {
			debugger.debug(msg);
			debugger.debug("Error saving highscores: ", e);
			//e.printStackTrace();
		}
	}
	
	
}
