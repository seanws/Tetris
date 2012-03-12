/**
 * HighscoresTableModel.java
 * 
 * Created on 16-Feb-04
 */
package org.oversoul.tetris.highscores;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * The Table Model for the Tetris Highscores table.
 * 
 * @author Chris Callendar (9902588)
 * @date   16-Feb-04, 8:58:12 PM
 */
public class HighscoresTableModel extends AbstractTableModel implements TableModelListener {

	private static final long serialVersionUID = 1;
	private static final String EMPTY = "";

	public static final int POS = 0;
	public static final int NAME = 1;
	public static final int DATE = 2;
	public static final int SCORE = 3;
	public static final int LEVEL = 4;
	public static final int LINES = 5;
	public static final int TETRIS = 6;

	public String[] columnNames = {EMPTY, "Name", "Date", "Score", "Level", "Lines", "Tetrises"};
	public Object[][] data = { { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY} };
	
	//private Highscores highscores = null;
	//private int gameSpeed = TetrisConstants.SPEED_NORM;
	
	/**
	 * Constructor for HighscoresTableModel.
	 * @param highscores
	 */
	public HighscoresTableModel(Highscores highscores, int gameSpeed) {
		//this.highscores = highscores;
		//this.gameSpeed = gameSpeed;
		int visibleEntries = Math.min(highscores.getCount(gameSpeed), highscores.getVisibleEntries());
		this.data = new String[visibleEntries][columnNames.length];
		for (int i = 0; i < data.length; i++) {
			HighscoreEntry hs = highscores.getHighScore(gameSpeed, i);
			data[i][POS] = EMPTY + (i+1);
			data[i][NAME] = hs.getName();
			data[i][DATE] = hs.getDateString();
			int score = hs.getScore();
			int level = hs.getLevel();
			int lines = hs.getLines();
			int tc = hs.getTetrisCount();
			data[i][SCORE] = (score != -1 ? EMPTY+score : EMPTY);
			data[i][LEVEL] = (level != -1 ? EMPTY+level : EMPTY);
			data[i][LINES] = (lines != -1 ? EMPTY+lines : EMPTY);
			data[i][TETRIS] = (tc != -1 ? EMPTY+tc : EMPTY);
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
     * Returns whether the cell is editable.  No cells are editable for the highscores table.
     * @param row		Row.
     * @param column	Column.
     * @return boolean if editable.
     */
	@Override
   public boolean isCellEditable(int row, int column) { 
    	return false;
    }
    
}
