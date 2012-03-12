/**
 * PlayerTableModel.java
 * 
 * Created on 16-Feb-04
 */
package org.oversoul.tetris.multiplayer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.util.Util;

/**
 * Controls the players Table.
 * 
 * @author Chris Callendar (9902588)
 * @date   16-Feb-04, 8:58:12 PM
 */
public class PlayerTableModel extends AbstractTableModel implements TableModelListener {

	private static final long serialVersionUID = 1;
	private static final String EMPTY = "";

	public static final int MAX_PLAYERS = 2;
	public static final int HOST = 0;
	public static final int CLIENT = 1;
	// headers
	public static final int NUM = 0;
	public static final int NAME = 1;
	public static final int LEVEL = 2;
	public static final int LINES = 3;
	public static final int SPEED = 4;
	public static final int TEAM = 5;

	public String[] columnNames = {"#", "Name", "Level", "Lines", "Speed"};
	public Object[][] data = { { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,} };
	
	private TetrisPlayer player = null;
	private int playerNumber = HOST;
	private int players = 0;
	
	/**
	 * Constructor for PlayerTableModel.
	 */
	public PlayerTableModel(TetrisPlayer player) {
		this.player = player;
		this.playerNumber = player.getPlayerNumber() - 1;
		this.players = 1;
		this.data = new String[MAX_PLAYERS][columnNames.length];
		for (int i = 0; i < data.length; i++) {
			data[i][NUM] = " " + (i+1) + ". ";
			if (i == playerNumber) {
				data[i][NAME] = player.getName();
				data[i][LEVEL] = EMPTY+player.getStartingLevel();
				data[i][LINES] = EMPTY+player.getStartingLines();
				data[i][SPEED] = Util.getGameSpeedString(player.getGameSpeed());
				//data[i][TEAM] = EMPTY+player.getTeam();
			} else {
				data[i][NAME] = EMPTY;
				data[i][LEVEL] = "0";
				data[i][LINES] = "0";
				data[i][SPEED] = Util.getGameSpeedString(TetrisConstants.SPEED_NORM);
				//data[i][TEAM] = EMPTY;
			}
		}
	}

	/**
	 * @see javax.swing.event.TableModelListener#tableChanged(TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
	}
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}
    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
	@Override
    public String getColumnName(int col) {
    	String name = EMPTY;
    	if (col < columnNames.length) {
    		name = columnNames[col];	
    	} 
        return name;
    }
	/**
	 * Returns the columnNames.
	 * @return String[]
	 */
	public String[] getColumnNames() {
		return columnNames;
	}
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.length;
	}
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = new String(EMPTY);
		if ((rowIndex >= 0) && (rowIndex < data.length) && 
			(columnIndex >= 0) && (columnIndex < data[rowIndex].length)) {
			obj = data[rowIndex][columnIndex];		
		}
		return obj;
	}
	/**
	 * Updates the value at row/col.
	 * @param value	New value.
	 * @param row	Row.
	 * @param col	Column.
	 */
	@Override
    public void setValueAt(Object value, int row, int col) {
        if ((value != null) && (row < data.length) && (col < data[row].length)) {
        	data[row][col] = value;	
        	fireTableCellUpdated(row, col);
        }
    }
    
    /**
     * JTable uses this method to determine the default renderer/editor for each cell.  
     * If we didn't implement this method, then the last column would contain text ("true"/"false"),
     * rather than a check box.
     * @param col	Column number.
     * @return Class
     */
	@Override
    public Class<? extends Object> getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }
    
    /**
     * Returns whether the cell is editable. Makes the checkbox column editable.
     * @param row		Row.
     * @param column	Column.
     * @return boolean if editable.
     */
	@Override
    public boolean isCellEditable(int row, int column) { 
    	boolean edit = false;
         if ((row >= 0) && (row < data.length)) {
         	switch (column) { 
         	case LEVEL :
         		if ((row == playerNumber) && (!isFixedLevel() || (playerNumber == HOST))) {
					edit = true;
         		}
         		break;
         	case LINES :
         		if ((row == playerNumber) && (!isFixedLines() || (playerNumber == HOST))) {
					edit = true;
         		}
         		break;
         	case SPEED :
				if ((row == playerNumber) && (!isFixedSpeed() || (playerNumber == HOST))) {
					edit = true;
				}
				break;
         	}         		
        }
		return edit;
    }
    
    // Custom methods
    
    /**
     * Gets the name for a player.
     * @param player	The player number - assoc. with the row in the table.
     * @return String player's name.
     */
    public String getName(int player) {
    	int row = player - 1;
    	Object val = getValueAt(row, NAME);
    	String name = EMPTY;
    	if ((val != null) && (val instanceof String)) {
			name = (String) val;
		}
    	return name;
    }
  
 	/**
	 * Sets the player's name.
	 * @param p		The player to set the name of.
	 */
	public void setName(TetrisPlayer p) {
    	int row = p.getPlayerNumber() - 1;
    	if (row >= 0) {
	    	setValueAt(p.getName(), row, NAME);
    	} else {
    		System.err.println("** setName(): INVALID ROW: " + row);	
    	}
	}

   /**
     * Gets the player's starting level.
     * @param p	The player.
     * @return int Starting level (defaults to 0).
     */
    public int getStartingLevel(TetrisPlayer p) {
    	int row = p.getPlayerNumber() - 1;
    	int level = 0;
    	if (row >= 0) {
	    	Object val = getValueAt(row, LEVEL);
	    	if ((val != null) && (val instanceof String)) {
	    		String s = (String) val;
	    		if (s.length() > 0) {
		    		level = Integer.parseInt((String) val);	
		    		p.setStartingLevel(level);
	    		}
	    	}
    	} else {
    		System.err.println("** getStartingLevel(): INVALID ROW: " + row);	
	   	}
    	return level;
    }

 	/**
	 * Sets all the levels.
	 * @param level
	 */
	public void setAllStartingLevel(int level) {
		for (int i = 0; i < getRowCount(); i++) {
			setValueAt(EMPTY+level, i, LEVEL);	
		}
	}

   /**
     * Gets the player's starting lines.
     * @param p	The player.
     * @return int Starting lines (defaults to 0).
     */
    public int getStartingLines(TetrisPlayer p) {
    	int row = p.getPlayerNumber() - 1;
    	int lines = 0;
		if (row >= 0) {
	    	Object val = getValueAt(row, LINES);
	    	if ((val != null) && (val instanceof String)) {
	    		String s = (String) val;
	    		if (s.length() > 0) {
		    		lines = Integer.parseInt((String) val);	
		    		p.setStartingLines(lines);
	    		}
	    	}
    	} else {
    		System.err.println("** getStartingLines(): INVALID ROW: " + row);	
		}
    	return lines;
    }

 	/**
	 * Sets all the lines.
	 * @param lines	the starting lines.
	 */
	public void setAllStartingLines(int lines) {
		for (int i = 0; i < getRowCount(); i++) {
			setValueAt(EMPTY+lines, i, LINES);	
		}
	}

	/**
   	 * Gets the player's game speed.
   	 * @param p	The player.
   	 * @return int game speed (defaults to normal speed).
   	 */
  	public int getGameSpeed(TetrisPlayer p) {
	  	int row = p.getPlayerNumber() - 1;
	  	int speed = TetrisConstants.SPEED_NORM;
	  	if (row >= 0) {
		  	Object val = getValueAt(row, SPEED);
		  	if ((val != null) && (val instanceof String)) {
			  	String s = (String) val;
			  	speed = Util.getGameSpeed(s);
				p.setGameSpeed(speed);
			}
	  	} else {
			System.err.println("** getGameSpeed(): INVALID ROW: " + row);	
	  	}
		return speed;
	}

	/**
	 * Forces all the players to have the same game speed.
	 * @param gameSpeed	the game speed.
	 */
	public void setAllGameSpeed(int gameSpeed) {
		final String speedString = Util.getGameSpeedString(gameSpeed);
		for (int i = 0; i < getRowCount(); i++) {
			setValueAt(speedString, i, SPEED);	
		}
	}

	/**
	 * Returns the fixedLevel.
	 * @return boolean
	 */
	public boolean isFixedLevel() {
		return player.isFixedLevel();
	}
	/**
	 * Returns the fixedLines.
	 * @return boolean
	 */
	public boolean isFixedLines() {
		return player.isFixedLines();
	}
	/**
	 * Returns if the game speed is fixed.
	 * @return boolean
	 */
	public boolean isFixedSpeed() {
		return player.isFixedSpeed();
	}

	/**
	 * Adds a player to the table.  Returns true if successfully added.
	 * @param opponent	the opponent player.
	 * @return boolean if added.
	 */
	public boolean addPlayer(TetrisPlayer opponent) {
		boolean added = false;
		if (players < MAX_PLAYERS) {
			players++;
			setName(opponent);
			added = true;
		} else {
			System.err.println("Game is full.");
		}
		return added;
	}

	/**
	 * Removes a player.
	 * @param p		player to remove.
	 */
	public void removePlayer(TetrisPlayer p) {
		int row = p.getPlayerNumber() - 1;
		if (row >= 0) {
			setValueAt(EMPTY, row, NAME);
			setValueAt("0", row, LEVEL);
			setValueAt("0", row, LINES);
			setValueAt(Util.getGameSpeedString(TetrisConstants.SPEED_NORM), row, SPEED);
			if (playerNumber != HOST) {
				player.setFixedLevel(false);
				player.setFixedLines(false);
				player.setFixedSpeed(false);
			} else {
				if (isFixedLevel()) {
					setValueAt(EMPTY+player.getStartingLevel(), row, LEVEL);
				}
				if (isFixedLines()) {
					setValueAt(EMPTY+player.getStartingLines(), row, LINES);
				}
				if (isFixedSpeed()) {
					setValueAt(Util.getGameSpeedString(player.getGameSpeed()), row, SPEED);	
				}
			}
			players--;
		}
	}

	/**
	 * Gets the opponent player's number.
	 * @return int player number of opponent.
	 */
	public int getOpponentPlayerNumber() {
		int num = 0;
		if (playerNumber == HOST) {
			num = CLIENT+1;	
		} else {
			num = HOST+1;
		}
		return num;
	}

	/**
	 * Updates the player with the values from the table.
	 * @param p	the player to update.
	 */
	public void updatePlayer(TetrisPlayer p) {
		p.setName(getName(p.getPlayerNumber()));
		getStartingLevel(p);
		getStartingLines(p);
		getGameSpeed(p);
	}
	
	/**
	 * Sets the player values into the table.
	 * @param p	player.
	 */
	public void updateTable(TetrisPlayer p) {
		int row = p.getPlayerNumber() - 1;
		if (row >= 0) {
			setValueAt(p.getName(), row, NAME);
			setValueAt(EMPTY+p.getStartingLevel(), row, LEVEL);
			setValueAt(EMPTY+p.getStartingLines(), row, LINES);
			setValueAt(Util.getGameSpeedString(p.getGameSpeed()), row, SPEED);
			if (p.isFixedLevel()) {
				setAllStartingLevel(p.getStartingLevel());	
				player.setStartingLevel(p.getStartingLevel());
			}
			if (p.isFixedLines()) {
				setAllStartingLines(p.getStartingLines());	
				player.setStartingLines(p.getStartingLines());
			}
			if (p.isFixedSpeed()) {
				setAllGameSpeed(p.getGameSpeed());
				player.setGameSpeed(p.getGameSpeed());	
			}
		}
	}

	/**
	 * Returns if the game is full.
	 * @return boolean if the game is full.
	 */
	public boolean isGameFull() {
		return (players >= MAX_PLAYERS);
	}

}
