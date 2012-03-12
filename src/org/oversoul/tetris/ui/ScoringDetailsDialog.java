/**
 * ScoringDetailsDialog.java
 * 
 * Created on 1-Apr-05
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisDefaults;

/**
 * Displays the scoring details for Tetris.
 * 
 * @author 		nyef
 * @date		1-Apr-05, 7:23:37 PM
 * @version 	1.0
 */
public class ScoringDetailsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1;
	private static final String EMPTY = "";

	private TetrisRootPane rootPane;

	private JButton jButton;
	private JPanel jPanel;
	private JTable jTable;

	/**
	 * Constructor for ScoringDetailsDialog.java
	 * @throws java.awt.HeadlessException
	 */
	public ScoringDetailsDialog(Frame parent, TetrisRootPane rootPane) throws HeadlessException {
		super(parent);
		this.rootPane = rootPane;
		
		initialize();
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("OK")) {
			dispose();
		}
	}

	/**
	 * Initializes this dialog.
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(368, 295);
		this.setName("ScoringDetailsDialog");
		this.setModal(true);
		this.setTitle("Tetris Scoring Details");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(rootPane.getTopLeft().x+120, rootPane.getTopLeft().y+120);
		this.setBackground(Color.WHITE);
		this.pack();
		getOKButton().requestFocusInWindow();
		this.setVisible(true);
	}
	
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 6));
			jPanel.setPreferredSize(new Dimension(320, 260));
			jPanel.setBackground(Color.WHITE);
			
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setBorder(rootPane.getTetrisPanel().getPanelBorder("Tetris Scoring Details", Color.WHITE, Color.BLACK));
			panel.setLayout(box);
			panel.add(getJTable());
			panel.add(new JLabel(" "));
			jPanel.add(panel);

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			buttonPanel.setPreferredSize(new Dimension(300, 35));
			buttonPanel.add(getOKButton());
			buttonPanel.setBackground(Color.WHITE);
			jPanel.add(buttonPanel);
		}
		return jPanel;
	}
	private JButton getOKButton() {
		if (jButton == null) {
			jButton = new JButton("    OK    ");
			jButton.addActionListener(this);
			jButton.setActionCommand("OK");
		}
		return jButton;
	}
	private JTable getJTable() {
		if (jTable == null) {
			DefaultTableModel model = new DefaultTableModel(11, 2);
			model.setValueAt(EMPTY, 0, 0);
			model.setValueAt(EMPTY, 0, 1);
			model.setValueAt("   Block landed:", 1, 0);
			model.setValueAt(EMPTY+TetrisConstants.BLOCK_LANDED, 1, 1);
			model.setValueAt("   Single line:", 2, 0);
			model.setValueAt(EMPTY+TetrisConstants.ONE_LINE, 2, 1);
			model.setValueAt("   Two lines:", 3, 0);
			model.setValueAt(EMPTY+TetrisConstants.TWO_LINES, 3, 1);
			model.setValueAt("   Three lines:", 4, 0);
			model.setValueAt(EMPTY+TetrisConstants.THREE_LINES, 4, 1);
			model.setValueAt("   Four lines:", 5, 0);
			model.setValueAt(EMPTY+TetrisConstants.FOUR_LINES, 5, 1);
			model.setValueAt("   Consecutive tetrises bonus:  ", 6, 0);
			model.setValueAt(EMPTY+TetrisConstants.TETRIS_BONUS, 6, 1);
			model.setValueAt("   3 blocks left bonus:", 7, 0);
			model.setValueAt(EMPTY+TetrisConstants.NO_BLOCKS_BONUS, 7, 1);
			model.setValueAt("   2 blocks left bonus:", 8, 0);
			model.setValueAt(EMPTY+(TetrisConstants.NO_BLOCKS_BONUS*2), 8, 1);
			model.setValueAt("   1 blocks left bonus:", 9, 0);
			model.setValueAt(EMPTY+(TetrisConstants.NO_BLOCKS_BONUS*3), 9, 1);
			model.setValueAt("   0 blocks left bonus:", 10, 0);
			model.setValueAt(EMPTY+TetrisConstants.MAX_BLOCKS_BONUS, 10, 1);

			jTable = new JTable(model);
			TableColumnModel columns = jTable.getColumnModel();
			TableColumn col = columns.getColumn(0);
			col.setPreferredWidth(250);
			col.setResizable(false);
			col = columns.getColumn(1);
			col.setPreferredWidth(50);
			col.setResizable(false);

			jTable.setRowSelectionAllowed(true);			
			jTable.setColumnSelectionAllowed(false);
			jTable.setBackground(Color.BLACK);
			jTable.setForeground(Color.WHITE);
			jTable.setFont(TetrisDefaults.FONT_14);
			jTable.setShowGrid(false);
		}
		return jTable;
	}
	
}
