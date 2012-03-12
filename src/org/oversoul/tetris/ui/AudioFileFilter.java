/**
 * AudioFileFilter.java
 * 
 * Created on 28-Sep-04
 */
package org.oversoul.tetris.ui;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

/**
 * Filename filter for midi and sampled audio files.
 * 
 * @author 		nyef
 * @date		28-Sep-04, 8:42:06 PM
 * @version 	1.0
 */
public class AudioFileFilter extends FileFilter implements FilenameFilter, java.io.FileFilter {

	private static final String WAV = ".wav";
	private static final String AU = ".au";
	private static final String AIF = ".aif";
	private static final String AIFF = ".aiff";
	private static final String MID = ".mid";
	private static final String MIDI = ".midi";

	/**
	 * Constructor for AudioFileFilter.java
	 * 
	 */
	public AudioFileFilter() {
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			return (isSampledAudioFile(f.getName()) || isMidiFile(f.getName()));
		}
		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Audio Files";
	}

	/**
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		return (isSampledAudioFile(name) || isMidiFile(name));
	}
	
	/**
	 * Compares the filename with the allowed extensions.
	 * @param name the filename or path.
	 * @return boolean
	 */
	public static boolean isSampledAudioFile(String name) {
		if (name != null) {
			name = name.toLowerCase();
			if (name.endsWith(WAV) || name.endsWith(AU) || name.endsWith(AIF) || name.endsWith(AIFF))
				return true;
		}
		return false;
	}

	/**
	 * Compares the filename with the allowed extensions.
	 * @param name the filename or path.
	 * @return boolean
	 */
	public static boolean isMidiFile(String name) {
		if (name != null) {
			name = name.toLowerCase();
			if (name.endsWith(MID) || name.endsWith(MIDI))
				return true;
		}
		return false;
	}

}
