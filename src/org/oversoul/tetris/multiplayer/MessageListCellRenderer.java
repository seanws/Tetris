/**
 * MessageListCellRenderer.java
 * 
 * Created on 8-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Makes the messages (entries in the list) in different colors.
 * 
 * @author Chris Callendar (9902588)
 * @date   8-Mar-04, 9:34:18 PM
 */
public class MessageListCellRenderer implements ListCellRenderer {

	public static final int ERROR = -1;
	public static final int DEFAULT = 0;
	public static final int HOST = 1;
	public static final int CLIENT = 2;

	private final Color defaultColor 	= Color.WHITE;
	private final Color hostColor		= Color.CYAN;
	private final Color clientColor		= Color.GREEN;
	private final Color errorColor		= Color.RED;
	private final Color background 		= Color.BLACK;
	private final Color selectedBg		= new Color(0, 0, 156);
	private final Font normal			= new Font("Default", Font.PLAIN, 12);

	private Hashtable<Integer, JLabel> messages = null;
	private Hashtable<Integer, Color> colors = null;

	/**
	 * Constructor for MessageListCellRenderer.
	 */
	public MessageListCellRenderer() {
		messages = new Hashtable<Integer, JLabel>();
		colors = new Hashtable<Integer, Color>();
	}
	
	/**
	 * Removes a message at the given index.
	 * @param index
	 */
	public void removeMessage(int index) {
		messages.remove(index);
	}

	/**
	 * Sets a color for the given index.
	 * @param index	The index of the list to color.	
	 * @param type	The type of entry.
	 */
	public void setColor(int index, int type) {
		Color color = defaultColor;
		switch (type) {
			case HOST :
				color = hostColor;
				break;
			case CLIENT :
				color = clientColor;
				break;
			case ERROR :
				color = errorColor;
				break;
		}
		if (messages.containsKey(index)) {
			JLabel label = messages.get(index);
			label.setForeground(color);
		} else {
			colors.put(index, color);			
		}
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list,
												  Object value,
												  int index,
												  boolean isSelected,
												  boolean cellHasFocus) {

		JLabel label = null;
		Color color = defaultColor;
		if (colors.contains(index)) {
			color = colors.get(index);
		}
		if (messages.containsKey(index)) {
			label = messages.get(index);				
		} else {
			label = new JLabel(value.toString());
			messages.put(index, label);
		}
		label.setFont(normal);
		label.setForeground(color);

		if (!isSelected) {
			label.setBackground(background);
		} else {
			label.setBackground(selectedBg);
		}
		
		return label;
	}

}
