/**
 * Util.java
 * 
 * Created on 14-Mar-04
 */
package org.oversoul.tetris.util;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.oversoul.tetris.TetrisConstants;

/**
 * Utility class.
 * 
 * @author Chris Callendar (9902588)
 * @date 14-Mar-04, 4:43:55 PM
 */
public final class Util {

	private static final String SPC = " ";

	private static final String EMPTY = "";

	private static final String ZERO = "0";

	private static final String NUM = "#";

	private static final String[] SPEEDS = new String[] { "Slow", "Normal", "Fast" };

	/**
	 * Prints out the stack trace for the given exception to a String Writer.
	 * @param ex
	 * @return String
	 */
	public static String getStackTrace(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}

	/**
	 * Gets the base working directory from which the given class was run.
	 * This also works if the class is inside a jar file.
	 * @param cls The class that is running
	 * @return The base directory or null if not found
	 */
	public static File getBaseDirectory(Class cls) {
		File base = null;
		String name = cls.getName();
		int upDirs = name.split("\\.").length;
		name = "/" + name.replace('.', '/') + ".class";
		URL url = cls.getResource(name);
		if (url != null) {
			String path = null;
			try {
				path = URLDecoder.decode(url.getPath(), "UTF-8");
			} catch (Exception ex) {}
			if (path != null) {
				// check for jar file
				int jar = path.toLowerCase().lastIndexOf(".jar");
				if (jar != -1) {
					path = path.substring(0, jar + 4);
					upDirs = 1;
				}
				if (path.toLowerCase().startsWith("file:/")) {
					path = path.substring(6);
				}
				base = new File(path);
				for (int i = 0; i < upDirs; i++) {
					if (base.getParentFile() != null) {
						base = base.getParentFile();
					}
				}
			}
		}
		return base;
	}

	/**
	 * Gets the working directory.
	 * @return File the working directory
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static File findWorkingDirectory() throws FileNotFoundException, IOException {
		File workingDir = null;
		workingDir = Util.getBaseDirectory(Util.class);
		if (workingDir == null) {
			File delete = File.createTempFile("delete", ".tmp");
			workingDir = delete.getParentFile();
			delete.delete();
		}
		if (workingDir == null) {
			throw new FileNotFoundException("Couldn't find a working directory.");
		}
		return workingDir;
	}
	
	/**
	 * Attempts to find the File for the given resource.
	 * 
	 * @param resource The relative path of the resource.
	 * @return File or null if not found
	 */
	public static File findFileResource(String resource) {
		File file = null;
		URL url = Util.class.getResource(resource);
		if (url != null) {
			try {
				String path = URLDecoder.decode(url.getFile(), "UTF-8");
				file = new File(path);
				if (!file.exists()) {
					file = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				file = null;
			}
		}
		return file;
	}	

	/**
	 * Reads a resource text file (from a jar file).
	 * @param resourceName the name of the resource (relative to the jar file)
	 * @return String[] the lines of the text file
	 */
	public static String[] readTextFromJar(String resourceName) throws Exception {
		Vector<String> lines = new Vector<String>();
		InputStream is = Util.class.getResourceAsStream(resourceName);
		if (is != null) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}
		String[] linesArray = new String[lines.size()];
		linesArray = lines.toArray(linesArray);
		return linesArray;
	}

	/**
	 * Reads a read file in and returns a String array of the lines 
	 * in the text file.
	 * @param is the input stream
	 * @return String[] the lines in the file
	 * @throws Exception
	 */
	public static String[] readTextFile(InputStream is) throws Exception {
		if (is == null) 
			throw new Exception("Input stream is null.");

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Vector<String> lines = new Vector<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		String[] linesArray = new String[lines.size()];
		linesArray = lines.toArray(linesArray);
		return linesArray;
	}
	
	/**
	 * Gets a resource from disk. If it doesn't exist null is returned.
	 * @param directory	The directory
	 * @param resource	The resource to load
	 * @return String 	The absolute path.
	 */
	public static String getResource(File directory, String resource) {
		File file = new File(directory, resource);
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		return null;
	}

	/**
	 * Loads an icon image from file. Returns null if the image doesn't exist.
	 * 
	 * @param directory	The directory to look in
	 * @param imageName	The image name
	 * @return IconImage the image
	 */
	public static ImageIcon getIcon(File directory, String imageName) {
		ImageIcon icon = null;
		File file = new File(directory, imageName);
		if (file.exists()) {
			icon = new ImageIcon(file.getAbsolutePath());
		}
		return icon;
	}

	/**
	 * Loads an image from file. Returns null if the image doesn't exist.
	 * 
	 * @param directory	The directory to look in
	 * @param imageName	The image name
	 * @return IconImage the image
	 */
	public static Image getImage(File directory, String imageName) {
		Image image = null;
		File file = new File(directory, imageName);
		if (file.exists()) {
			try {
				image = ImageIO.read(file);
			} catch (IOException ioe) {
				image = null;
			}
		}
		return image;
	}

	/**
	 * Loads an icon image from a resource. Returns null if the image doesn't exist.
	 * @param resource	The image resource name
	 * @return IconImage the image
	 */
	public static ImageIcon getIconResource(String resource) {
		ImageIcon icon = null;
		Image img = getImageResource(resource);
		if (img != null) {
			icon = new ImageIcon(img);
		}
		return icon;
	}
	
	/**
	 * Loads an image resource and returns the image. It uses an input stream to
	 * read in the image, this seems to work better when extracting resources
	 * from jar files.
	 * 
	 * @param resource The resource (relative to the base class directory).
	 * @return Image
	 */
	public static Image getImageResource(String resource) {
		Image img = null;
		InputStream is = Util.class.getResourceAsStream(resource);
		if (is != null) {
			BufferedInputStream input = new BufferedInputStream(is);
			try {
				img = ImageIO.read(input);
			} catch (IOException ex) {
			} finally {
				try {
					input.close();
				} catch (Exception ex) {
				}
			}
		}
		return img;
	}

	/**
	 * Parses a web color string. If the color value couldn't be parsed
	 * correctly, null will be returned.
	 * @param value	the color value to parse
	 * @return the color represented by the string, or null if the string was malformed
	 */
	public static Color stringToColor(String value) {
		if ((value == null) || !value.startsWith(NUM)) {
			return null;
		}
		try {
			return new Color(Integer.parseInt(value.substring(1), 16));
		} catch (NumberFormatException ignore) {
			return null;
		}
	}

	/**
	 * Converts a color into a String like #RRGGBB.
	 * @param color	Color to convert into a String.
	 * @return String color String.
	 */
	public static String colorToString(Color color) {
		String colorStr = Integer.toHexString(color.getRGB());
		colorStr = NUM + colorStr.substring(2);
		return colorStr;
	}

	/**
	 * Inverts the given color.
	 * @param color the color to invert
	 * @return Color the inverted color
	 */
	public static Color invertColor(Color color) {
		int red = 255 - color.getRed();
		int green = 255 - color.getGreen();
		int blue = 255 - color.getBlue();
		int diff = (red - color.getRed()) + (green - color.getGreen()) + (blue - color.getBlue());
		if (diff < 20) {
			return Color.BLACK;
		}
		return new Color(red, green, blue);
	}

	/**
	 * Returns the sum of the Red, Green, and Blue values (0-255 each).
	 * @param color
	 * @return int between (0 - 765)
	 */
	public static int getRGB(Color color) {
		return (color.getRed() + color.getBlue() + color.getGreen());
	}
	
	/**
	 * Returns the lighter of the two colors based on RGB.
	 * @param one	The first color
	 * @param two	The second color
	 * @return Color the lighter color
	 */
	public static Color lighterColor(Color one, Color two) {
		return (getRGB(one) >= getRGB(two) ? one : two);
	}

	/**
	 * Returns the darker of the two colors based on RGB.
	 * @param one	The first color
	 * @param two	The second color
	 * @return Color the darker color
	 */
	public static Color darkerColor(Color one, Color two) {
		return (getRGB(one) <= getRGB(two) ? one : two);
	}

	/**
	 * Returns white for a dark background or black for a light background.
	 * @param bgColor
	 * @return Color
	 */
	public static Color textColor(Color bgColor) {
		return (getRGB(bgColor) > 383 ? Color.BLACK : Color.WHITE);
	}

	/**
	 * Gets the String game speed: {Slow, Normal, Fast}.
	 * 
	 * @param gameSpeed
	 * @return String game speed string.
	 */
	public static String getGameSpeedString(int gameSpeed) {
		if ((gameSpeed < TetrisConstants.SPEED_SLOW) || (gameSpeed > TetrisConstants.SPEED_FAST)) {
			gameSpeed = TetrisConstants.SPEED_NORM;
		}
		return SPEEDS[gameSpeed];
	}

	/**
	 * Gets the game speed: {0, 1, 2}.
	 * 
	 * @param speedString
	 * @return int game speed integer.
	 */
	public static int getGameSpeed(String speedString) {
		for (int i = 0; i < SPEEDS.length; i++) {
			if (SPEEDS[i].equals(speedString))
				return i;
		}
		return TetrisConstants.SPEED_NORM;
	}

	/**
	 * Pads an integer with zeroes to make it a certain length and returns the
	 * String.
	 * 
	 * @param i   the integer
	 * @param len the length of the String
	 * @return String
	 */
	public static String padZeroes(int i, int len) {
		String s = EMPTY + i;
		for (int j = s.length(); j < len; j++) {
			s = ZERO + s;
		}
		return s;
	}

	/**
	 * Pads the right of a String with spaces to make it a certain length and
	 * returns the String.
	 * 
	 * @param str the string to pad
	 * @param len the total length of the String after padding
	 * @return String
	 */
	public static String padSpaces(String str, int len) {
		return Util.padSpaces(str, len, true);
	}

	/**
	 * Pads a String with spaces to make it a certain length and returns the
	 * String.
	 * 
	 * @param str the string to pad
	 * @param len the total length of the String after padding
	 * @param padRight if the spaces should be on the right
	 * @return String
	 */
	public static String padSpaces(String str, int len, boolean padRight) {
		String s = str;
		for (int j = s.length(); j < len; j++) {
			s = (padRight ? s + SPC : SPC + s);
		}
		return s;
	}

	/**
	 * Parses the given string into an integer and returns it.
	 * If the string can't be parsed the default int is returned.
	 * @param str	The string to parse
	 * @param def	The default int to return in case of an error
	 * @return int
	 */
	public static int parseInt(String str, int def) {
		int i = def;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {}
		return i;
	}

}
