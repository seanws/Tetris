/**
 * Configuration.java
 */
package org.oversoul.tetris.util;

import org.oversoul.tetris.TetrisColorScheme;
import org.oversoul.tetris.TetrisPlayer;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Configuration is a simple extension of the Properties class.  The major difference is that
 * it saves the properties in sorted order by key. 
 * This class provides static methods for simplifying the reading of configuration parameters. It also 
 * provides some methods for transforming string values into more useful objects.
 * 
 * @author Chris Callendar (9902588)
 */
public class Configuration {

	public static final String CONTROL		= "control.";
	public static final String COLOR		= ".color.";
	public static final String PLAYERNAME	= "player.name.";

    /** The internal configuration property values.  */
    private Properties config = null;
    
	/** If a the properties object has changed. */
	private boolean modified = false;

	/**
	 * Initializes an empty configuration.
	 */
	public Configuration() {
		this.config = new Properties();
	}
	
	/**
	 * Reads the properties from the given file.
	 * @param props	the properties.
	 */
	public void load(Properties props) {
		this.config = props;
	}
	
	/**
	 * Writes the configuration out to a file if any changes have been made.
	 * @param configFile	the file to write to.
	 * @throws FileNotFoundException couldn't write to the file.
	 * @throws IOException	could write to the file.
	 */
	public synchronized void save(File configFile) throws FileNotFoundException, IOException {
		if (!configFile.getParentFile().exists()) {
			configFile.getParentFile().mkdirs();
		}
		
		if (modified) {
			String header = "##########################\r\n";
			header += 		"#  TETRIS CONFIGURATION  #\r\n";
			header += 		"##########################\r\n\r\n";
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(configFile, false));
			//config.store(out, header);
			out.write(header.getBytes());
			
			// sort the keys
			String[] keys = new String[config.size()];
			Enumeration en = config.keys(); 
			int i = 0;
			while (en.hasMoreElements() && (i < keys.length)) {
				keys[i++] = (String) en.nextElement();	
			}
			Arrays.sort(keys);
			for (i = 0; i < keys.length; i++) {
				String key = keys[i];
				String value = config.getProperty(key);
				value = escape(value);
				String string = key + "=" + value + "\r\n";
				out.write(string.getBytes());
			}
			out.close();
			
			modified = false;
		}
	}

    /**
     * Escapes special characters.
     * @param value	value to escape.
     * @return String new escaped String.
     */
    private String escape(String value) {
        int len = value.length();
        StringBuffer buffer = new StringBuffer(len+1);

		// special chars: "=:\t\r\n\f#!"
        for(int i = 0; i < len; i++) {
            char c = value.charAt(i);
            switch(c) {
                case '\\':
                case '\t':
                case '\n':
                case '\r':
                case '\f':
            	case '=' :
            	case ':' :
            	case '!' :
            	case '#' : 
            		buffer.append('\\');
            		break;
            }
            buffer.append(c);
        }
        return buffer.toString();
    }

	/**
	 * Deletes a config entry.
	 * @param key the key to delete.
	 */
	public void remove(String key) {
		config.remove(key);
	}

    /**
     * Returns a configuration parameter value.
     * @param key  the configuration parameter key
     * @return String the configuration parameter value, or null if not set
     */
    public String getValue(String key) {
        if (config.containsKey(key)) {
            return config.get(key).toString();
        }
        return null;
    }
 
	/**
	 * Returns a configuration parameter value.
	 * @param key  the configuration parameter key
	 * @param def  the default value.
	 * @return String the configuration parameter value, or the default if not set
	 */
	public String getValue(String key, String def) {
		if (config.containsKey(key)) {
			Object obj = config.get(key);
			return (obj != null ? obj.toString() : def);
		}
		return def;
	}
    
    /**
     * Returns a configuration parameter value. If the configuration
     * parameter is not set, a default value will be returned instead.
     * @param name the player name.
     * @param key  the configuration parameter key
     * @param def  the default value to use
     * @return String the configuration parameter value, or the default value if not set
     */
    public String getValue(String name, String key, String def) {
        return getValue(name + "." + key, def);
    }

	/**
	 * Sets a configuration parameter value.
	 * @param key       the configuration parameter key
	 * @param value     the configuration parameter value
	 */
	public void setValue(String key, String value) {
		config.put(key, value);
		modified = true;
	}

    /**
     * Sets a configuration parameter value.
     * @param name		the player's name.
     * @param key       the configuration parameter key
     * @param value     the configuration parameter value
     */
    public void setValue(String name, String key, String value) {
        setValue(name + "." + key, value);
    }

    /**
     * Returns the color configured for the specified name and key. 
     * The color value must be specified in hexadecimal web format, 
     * i.e. in the "#RRGGBB" format. If the default color isn't in a valid format, 
     * white will be returned.
     * @param name		the player name.
     * @param key       the configuration parameter key
     * @param def       the default value
     * @return Color the color specified in the configuration, or a default color value
     */
    public Color getColor(String name, String key, Color def) {
        return getColor(name + COLOR + key, def);
        
    }
    
	/**
     * Returns the color configured for the specified key. 
     * The color value must be specified in hexadecimal web format, 
     * i.e. in the "#RRGGBB" format. If the default color isn't in a valid format, 
     * white will be returned.
	 * @param key       the configuration parameter key
	 * @param def       the default color.
	 * @return Color the color specified in the configuration, or a default color value
	 */
    public Color getColor(String key, Color def) {
    	String value = getValue(key);
    	if (value == null) {
    		return def;
		}
        Color color = Util.stringToColor(value);
	    return (color != null ? color : Color.WHITE);
    }
   
	/**
	 * Sets a color.
	 * @param name	player name.
	 * @param key	color key.
	 * @param color color object.
	 */
    public void setColor(String name, String key, Color color) {
    	setValue(name + COLOR + key, Util.colorToString(color));
    }
	/**
	 * Sets a color.
	 * @param key	color key.
	 * @param color color object.
	 */
	public void setColor(String key, Color color) {
		setValue(key, Util.colorToString(color));
	}
	/**
	 * Sets a color.
	 * @param name	player name.
	 * @param key	color key.
	 * @param color color String.
	 */
	public void setColor(String name, String key, String color) {
		setValue(name + COLOR + key, color);
	}
    
	/**
	 * Returns the config.
	 * @return Properties
	 */
	public Properties getConfiguration() {
		return config;
	}

	/**
	 * Gets a control.
	 * @param name player name.
	 * @param key  control key.
	 * @param def  default key.
	 */
	public int getControl(String name, String key, int def) {
		int control = def;
		String val = getValue(name, CONTROL + key, ""+def);
		try {
			control = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			control = def;
		}
		return control;
	}
	
	/**
	 * Sets a control key.
	 * @param name player name
	 * @param key  config key.
	 * @param control the control to set.
	 */
	public void setControl(String name, String key, int control) {
		setValue(name, CONTROL + key, "" + control);
	}

	/**
	 * Removes all entries starting with the given name
	 * @param name
	 */
	public void removeKeys(String name) {
		if ((name != null) && (name.length() > 0)) {
			Iterator iter = config.keySet().iterator();
			Vector<String> keys = new Vector<String>();
			while (iter.hasNext()) {
				String key = (String)iter.next();
				if (key.startsWith(name)) {
					keys.add(key);
				}
			}
			for (int i = 0; i < keys.size(); i++) {
				String key = keys.get(i);
				config.remove(key);
			}
		}
	}
	
	/**
	 * Removes a tetris player.
	 * @param name player to remove.
	 */
	public void removePlayer(String name) {
		removeKeys(name);
		config.remove(PLAYERNAME + name);	
	}

	/**
	 * Gets the names of all the color schemes in this configuration.
	 * @return Hashtable a map of color scheme names to the color schemes.
	 */
	public Hashtable<String, TetrisColorScheme> loadColorSchemes() {
		Hashtable<String, TetrisColorScheme> schemes = new Hashtable<String, TetrisColorScheme>();
		if ((config != null) && (config.size() > 0)) {
			Iterator iter = config.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String)iter.next();
				if (key.startsWith(TetrisColorScheme.COLOR_SCHEME_NAME)) {
					String schemeName = key.substring(TetrisColorScheme.COLOR_SCHEME_NAME.length());
					TetrisColorScheme cs = new TetrisColorScheme(schemeName);
					cs.loadColorScheme(this);
					schemes.put(schemeName, cs);
				}
			}
		}
		return schemes;
	}
	
	/**
	 * Loads the players for this configuration.
	 * @return Hashtable
	 */
	public Hashtable<String, TetrisPlayer> loadPlayers() {
		Hashtable<String, TetrisPlayer> players = new Hashtable<String, TetrisPlayer>();
		if ((config != null) && (config.size() > 0)) {
			Iterator iter = config.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String)iter.next();
				if (key.startsWith(PLAYERNAME)) {
					String name = key.substring(PLAYERNAME.length());
					if (!players.containsKey(name)) {
						TetrisPlayer p = new TetrisPlayer();
						p.load(name, this);
						players.put(name, p);
					}
				}
			}
		}
		return players;
	}
		
}
