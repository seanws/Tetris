/**
 * HighscoresDialog.java
 * 
 * Created on 16-Feb-04
 */
package org.oversoul.tetris.highscores;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.ui.TetrisRootPane;
import org.oversoul.tetris.ui.WindowClosingDisposer;
import org.oversoul.tetris.util.Util;

/**
 * Dialog for displaying the Tetris highscores.
 * 
 * @author Chris Callendar (9902588)
 * @date   16-Feb-04, 7:13:27 PM
 */
public class HighscoresDialog extends JDialog implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 1;

	private static final Color HEADER = new Color(249, 250, 253);
	private static final Font TABLE_FONT = new Font("Tahoma", Font.PLAIN, 14);

	private TetrisRootPane rootPane = null;
	private Highscores highscores = null;
	private int currentGameSpeed = TetrisConstants.SPEED_NORM;
	private int newEntryPosition = -1;
	//private int focussedRow = -1;
	private boolean allowClearScores = true;

    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JPanel jPanel2 = null;
    private JPanel jPanel3 = null;
    private JTable jTable = null;
    private JTable jTable1 = null;
    private JTable jTable2 = null;
    private JButton jButton = null;
    private JButton jButton1 = null;
    private JTabbedPane jTabbedPane = null;
    
	/**
	 * Constructor for HighscoresDialog.
	 * @param parent			The parent frame, or null.
	 * @param rootPane			The root pane.
	 * @param highscores		High scores list.
	 * @param gameSpeed			The game speed (slow, normal, fast)
	 * @param newEntryPosition	The position of new entry, or 0 for no new entry.
	 * @param allowClearScores	If the highscores can be cleared.
	 * @throws HeadlessException
	 */
	public HighscoresDialog(Frame parent, TetrisRootPane rootPane, Highscores highscores, 
							int gameSpeed, int newEntryPosition, boolean allowClearScores) throws HeadlessException {
		super(parent);
		this.rootPane = rootPane;
		this.highscores = highscores;
		this.currentGameSpeed = gameSpeed;
		this.newEntryPosition = newEntryPosition - 1;
		this.allowClearScores = allowClearScores;
		initialize();
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		// TODO if highscores change somehow - need to save?
		super.dispose();
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if ("ClearScores".equals(action)) {
			clearHighScores();
		} else if ("CloseWindow".equals(action)) {
			dispose();	
		}
	}
	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		int selectedTab = getJTabbedPane().getSelectedIndex();
		if ((selectedTab >= TetrisConstants.SPEED_SLOW) && (selectedTab <= TetrisConstants.SPEED_FAST)) {
			currentGameSpeed = selectedTab; 
		}
	}

	/**
	 * Clears the high scores.  Prompts the user.
	 */
	private void clearHighScores() {
		String msg = "Are you sure you want to clear the high scores?";
		String[] options = new String[] { "Clear All", Util.getGameSpeedString(currentGameSpeed)+" Speed Only", "Cancel" };
		int choice = JOptionPane.showOptionDialog(this, msg, "Clearing high scores...", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]); 
		if (choice == JOptionPane.YES_OPTION) {		
			highscores.clearAllHighScores();
			addHighScores();
		} else if (choice == JOptionPane.NO_OPTION) {
			highscores.clearHighScores(currentGameSpeed);
			addHighScores();
		}
	}

	/**
	 * Adds the high scores to the table.
	 */
	private void addHighScores() {
		if (highscores != null) {
			JTable[] tables = new JTable[Highscores.GAME_SPEEDS];
			tables[0] = getSlowTable();
			tables[1] = getNormalTable();
			tables[2] = getFastTable();
			for (int i = 0; i < tables.length; i++) {
				JTable table = tables[i];
				TableModel model = new HighscoresTableModel(highscores, i);
				table.setModel(model);
				DefaultTableCellRenderer render = new DefaultTableCellRenderer();
				render.setHorizontalAlignment(SwingConstants.CENTER);
				TableColumnModel columns = table.getColumnModel();
				TableColumn col = columns.getColumn(HighscoresTableModel.POS);
				col.setPreferredWidth(25);
				col.setResizable(false);
				col = columns.getColumn(HighscoresTableModel.NAME);
				JTextField nameTF = new JTextField();
				col.setCellEditor(new DefaultCellEditor(nameTF));
				col.setPreferredWidth(140);
				col = columns.getColumn(HighscoresTableModel.DATE);
				col.setPreferredWidth(65);
				col.setCellRenderer(render);
				col = columns.getColumn(HighscoresTableModel.SCORE);
				col.setPreferredWidth(50);
				col.setCellRenderer(render);
				col = columns.getColumn(HighscoresTableModel.LEVEL);
				col.setPreferredWidth(50);
				col.setCellRenderer(render);
				col = columns.getColumn(HighscoresTableModel.LINES);
				col.setPreferredWidth(50);
				col.setCellRenderer(render);
				col = columns.getColumn(HighscoresTableModel.TETRIS);
				col.setPreferredWidth(60);
				col.setCellRenderer(render);
				JTableHeader header = table.getTableHeader();
				header.setBackground(HEADER);
				header.setForeground(Color.BLACK);
				header.setReorderingAllowed(false);
				header.setPreferredSize(new Dimension(442, 18));
				header.setMinimumSize(new Dimension(300, 18));
				
				table.clearSelection();
				if ((newEntryPosition >= 0) && (newEntryPosition < table.getRowCount())) {
					table.setRowSelectionInterval(newEntryPosition, newEntryPosition);
					table.changeSelection(newEntryPosition, HighscoresTableModel.NAME, false, false);
				}
			}
		}	
	}

	//////////////////////////////////////////////////////////////////////
	// GUI Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.addWindowListener(new WindowClosingDisposer(this));

		this.setContentPane(getRootContentPane());
		this.setSize(468, 340);
		this.setLocation(rootPane.getTopLeft().x+80, rootPane.getTopLeft().y+80);	
		// modal seems to cause this dialog to freeze if a key is pressed
		// while it is loading...
		this.setModal(false);
		this.setTitle("Highscores");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addHighScores();
		this.pack();
		this.getRootPane().setDefaultButton(getCloseButton());
		getCloseButton().requestFocus();		
		this.setVisible(true);
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			jContentPane.add(getJTabbedPane(), null);
			jContentPane.add(getJPanel1(), null);
			jContentPane.setPreferredSize(new Dimension(458, 310));
		}
		return jContentPane;
	}

	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.add(getCloseButton(), null);
			jPanel1.add(getClearButton(), null);
			jPanel1.setPreferredSize(new Dimension(300,40));
		}
		return jPanel1;
	}

	private JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			JTable table = getSlowTable();
			jPanel.add(table.getTableHeader(), null);
			jPanel.add(table, null);
			jPanel.setBorder(rootPane.getTetrisPanel().getPanelBorder("High Scores: Slow Speed", getRootContentPane().getBackground(), Color.WHITE));
			jPanel.setPreferredSize(new Dimension(440,240));
			jPanel.setBackground(Color.WHITE);
		}
		return jPanel;
	}

	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			JTable table = getNormalTable();
			jPanel2.add(table.getTableHeader(), null);
			jPanel2.add(table, null);
			jPanel2.setBorder(rootPane.getTetrisPanel().getPanelBorder("High Scores: Normal Speed", getRootContentPane().getBackground(), Color.WHITE));
			jPanel2.setPreferredSize(new Dimension(440,240));
			jPanel2.setBackground(Color.WHITE);
		}
		return jPanel2;
	}

	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			JTable table = getFastTable();
			jPanel3.add(table.getTableHeader(), null);
			jPanel3.add(table, null);
			jPanel3.setBorder(rootPane.getTetrisPanel().getPanelBorder("High Scores: Fast Speed", getRootContentPane().getBackground(), Color.WHITE));
			jPanel3.setPreferredSize(new Dimension(440,240));
			jPanel3.setBackground(Color.WHITE);
		}
		return jPanel3;
	}

	private JTable getSlowTable() {
		if(jTable == null) {
			jTable = new JTable();
			jTable.setGridColor(Color.WHITE);
			jTable.setBackground(Color.WHITE);
			jTable.setSelectionBackground(new Color(0, 0, 175));
			jTable.setSelectionForeground(Color.WHITE);
			jTable.setIntercellSpacing(new Dimension(0,0));
			jTable.setShowGrid(false);
			jTable.setShowHorizontalLines(true);
			jTable.setShowVerticalLines(false);
			jTable.setFont(TABLE_FONT);
			jTable.setRowHeight(18);
			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setPreferredSize(new Dimension(440, 228));
		}
		return jTable;
	}

	private JTable getNormalTable() {
		if(jTable1 == null) {
			jTable1 = new JTable();
			jTable1.setGridColor(Color.WHITE);
			jTable1.setBackground(Color.WHITE);
			jTable1.setSelectionBackground(new Color(0, 0, 175));
			jTable1.setSelectionForeground(Color.WHITE);
			jTable1.setIntercellSpacing(new Dimension(0,0));
			jTable1.setShowGrid(false);
			jTable1.setShowHorizontalLines(true);
			jTable1.setShowVerticalLines(false);
			jTable1.setFont(TABLE_FONT);
			jTable1.setRowHeight(18);
			jTable1.setCellSelectionEnabled(false);
			jTable1.setRowSelectionAllowed(true);
			jTable1.setColumnSelectionAllowed(false);
			jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setPreferredSize(new Dimension(440, 228));
		}
		return jTable1;
	}

	private JTable getFastTable() {
		if(jTable2 == null) {
			jTable2 = new JTable();
			jTable2.setGridColor(Color.WHITE);
			jTable2.setBackground(Color.WHITE);
			jTable2.setSelectionBackground(new Color(0, 0, 175));
			jTable2.setSelectionForeground(Color.WHITE);
			jTable2.setIntercellSpacing(new Dimension(0,0));
			jTable2.setShowGrid(false);
			jTable2.setShowHorizontalLines(true);
			jTable2.setShowVerticalLines(false);
			jTable2.setFont(TABLE_FONT);
			jTable2.setRowHeight(18);
			jTable2.setCellSelectionEnabled(false);
			jTable2.setRowSelectionAllowed(true);
			jTable2.setColumnSelectionAllowed(false);
			jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setPreferredSize(new Dimension(440, 228));
		}
		return jTable2;
	}
	
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Slow Speed", getJPanel());
			jTabbedPane.addTab("Normal Speed", getJPanel2());
			jTabbedPane.addTab("Fast Speed", getJPanel3());
			jTabbedPane.setMnemonicAt(0, KeyEvent.VK_S);
			jTabbedPane.setMnemonicAt(1, KeyEvent.VK_N);
			jTabbedPane.setMnemonicAt(2, KeyEvent.VK_F);
			jTabbedPane.setSelectedIndex(currentGameSpeed);
			jTabbedPane.setPreferredSize(new Dimension(450, 255));
			jTabbedPane.addChangeListener(this);
		}
		return jTabbedPane;	
	}

	private JButton getClearButton() {
		if(jButton == null) {
			jButton = new JButton("Clear Scores");
			jButton.setMnemonic(KeyEvent.VK_C);
			jButton.setActionCommand("ClearScores");
			jButton.addActionListener(this);
			jButton.setEnabled(allowClearScores);
		}
		return jButton;
	}

	private JButton getCloseButton() {
		if(jButton1 == null) {
			jButton1 = new JButton("Close Window");
			jButton1.setMnemonic(KeyEvent.VK_W);
			jButton1.setActionCommand("CloseWindow");
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

}
