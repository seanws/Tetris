/**
 * PropertiesFileFilter.java
 * 
 * Created on 8-Nov-04
 */
package org.oversoul.tetris.ui;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

/**
 * Filter for property files ending with the <code>.properties</code> extension.
 * 
 * @author 		nyef
 * @date		8-Nov-04, 9:12:28 PM
 * @version 	1.0
 */
public class PropertiesFileFilter extends FileFilter implements FilenameFilter {

	private static final String EXT = ".properties";
	private static final String DESC = "Property files (*.properties)";

	/**
	 * Constructor for PropertiesFileFilter.java
	 */
	public PropertiesFileFilter() {
	}

	/**
	 * Accepts files that have the <code>.properties</code> file extension.
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			return f.getName().endsWith(EXT);
		}
		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return DESC;
	}

	/**
	 * Accepts files that have the <code>.properties</code> file extension.
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		if (name != null) {
			return name.toLowerCase().endsWith(EXT);	
		}
		return false;
	}

}
