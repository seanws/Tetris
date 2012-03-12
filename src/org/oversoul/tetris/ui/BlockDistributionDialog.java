/**
 * BlockDistributionFrame.java
 * 
 * Created on 27-Feb-05
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.oversoul.tetris.Block;
import org.oversoul.tetris.BlockType;
import org.oversoul.tetris.TetrisBoard;
import org.oversoul.tetris.TetrisColors;

/**
 * Displays the block distribution for a game.
 * 
 * @author 		nyef
 * @date		27-Feb-05, 4:39:31 PM
 * @version 	1.0
 */
public class BlockDistributionDialog extends JDialog {
	
	private static final long serialVersionUID = 1;

	private final String ZERO = "0";
	private final String EMPTY = "";	
	
	private final Font FONT = new Font("Arial", Font.BOLD, 14);
	
	private TetrisRootPane rootPane = null;
	private Block[] figures = null;	
	private TetrisColors blockColors = null;
	//private int[] blockCounts = null;
	
	// preview boards
	private TetrisBoard previewZ 	= null;
	private TetrisBoard previewS 	= null;
	private TetrisBoard previewL 	= null;
	private TetrisBoard previewR 	= null;
	private TetrisBoard previewLine = null;
	private TetrisBoard previewTri 	= null;
	private TetrisBoard previewSq	= null;	
			
	private JPanel jContentPanel;
	private JPanel jPanel;
	private JLabel jLabel;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;

	/**
	 * Constructor for BlockDistributionDialog
	 * @throws java.awt.HeadlessException
	 */
	public BlockDistributionDialog(TetrisRootPane rootPane, TetrisColors blockColors, int[] blockCounts) throws HeadlessException {
		super();
		this.rootPane = rootPane;
		this.blockColors = blockColors;
		
		previewZ = new TetrisBoard(5, 4, blockColors);
		previewS = new TetrisBoard(5, 4, blockColors);
		previewL = new TetrisBoard(5, 4, blockColors);
		previewR = new TetrisBoard(5, 4, blockColors);
		previewLine = new TetrisBoard(5, 4, blockColors);
		previewTri = new TetrisBoard(5, 4, blockColors);
		previewSq = new TetrisBoard(5, 4, blockColors);
				
		initialize();
		updateBlockCounts(blockCounts);
				
		this.addWindowListener(new WindowClosingDisposer(this));	
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		rootPane.setBlockDistributionDialogShown(false);
		super.dispose();
	}
	
	/**
	 * Updates the block counts.
	 * @param counts
	 */
	public void updateBlockCounts(int[] counts) {
		getZLabel().setText(EMPTY+counts[BlockType.Z.ordinal()]);
		getSLabel().setText(EMPTY+counts[BlockType.S.ordinal()]);
		getLeftLabel().setText(EMPTY+counts[BlockType.LEFT.ordinal()]);
		getRightLabel().setText(EMPTY+counts[BlockType.RIGHT.ordinal()]);
		getLineLabel().setText(EMPTY+counts[BlockType.LINE.ordinal()]);
		getTriangleLabel().setText(EMPTY+counts[BlockType.TRIANGLE.ordinal()]);
		getSquareLabel().setText(EMPTY+counts[BlockType.SQUARE.ordinal()]);
	}

	/**
	 * Resets the preview components.
	 */
	public void updateColors(TetrisColors blockColors) {
		this.blockColors = blockColors;
		previewZ.clear();
		previewS.clear();
		previewL.clear();
		previewR.clear();
		previewLine.clear();
		previewTri.clear();
		previewSq.clear();
		initFigures();
		addFigures();
	}
	
	/**
	 * Initializes the figures array. 
	 */
	private void initFigures() {
		BlockType[] types = BlockType.values();
		figures = new Block[types.length];
		int i = 0;
		for (BlockType type : types) {
			figures[i++] = new Block(blockColors.getColor(type), type);
		}
	}
	
	/**
	 * Adds the figures (images) to the list.
	 */
	private void addFigures() {
		if (figures == null) {
			initFigures();	
		}
		figures[BlockType.Z.ordinal()].attach(previewZ, true);
		figures[BlockType.S.ordinal()].attach(previewS, true);
		figures[BlockType.LEFT.ordinal()].attach(previewL, true);
		figures[BlockType.RIGHT.ordinal()].attach(previewR, true);
		figures[BlockType.LINE.ordinal()].attach(previewLine, true);
		figures[BlockType.TRIANGLE.ordinal()].attach(previewTri, true);
		figures[BlockType.SQUARE.ordinal()].attach(previewSq, true);
	}
	
	/**
	 * Initializes this frame.
	 */
	private void initialize() {
		this.setContentPane(getRootContentPane());
		this.setTitle("Blocks");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(100, 300);
		this.setResizable(false);
		this.setFocusable(false);
		this.setFocusableWindowState(false);
		this.setModal(false);
		this.pack();
		setDialogLocation();
		initFigures();
		addFigures();
		this.setVisible(true);		
	}
	/**
	 * Sets the dialog's location to be beside the game frame.
	 * Defaults to the right side, if not enough room will use the left side.
	 */
	public void setDialogLocation() {
		int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		Point tl = rootPane.getTopLeft();
		int x = tl.x + rootPane.getParentWidth();
		if ((x + this.getWidth()) > screenWidth) {
			x = tl.x - this.getWidth();
			if (x < 0) {
				x = 0;	
			}
		}
		this.setLocation(x, tl.y);
	}

	/**
	 * Gets the root content panel.
	 * @return JPanel
	 */
	private JPanel getRootContentPane() {
		if (jContentPanel == null) {
			jContentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			jContentPanel.setPreferredSize(new Dimension(100, 280));
			jContentPanel.add(getJPanel());
			jContentPanel.setBackground(Color.BLACK);
		}
		return jContentPanel;	
	}
	/**
	 * This method initializes jPanel.
	 * @return JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel(new GridLayout(7, 1, 0, 0));
			jPanel.add(createPanel(previewZ, getZLabel()));
			jPanel.add(createPanel(previewS, getSLabel()));
			jPanel.add(createPanel(previewL, getLeftLabel()));
			jPanel.add(createPanel(previewR, getRightLabel()));
			jPanel.add(createPanel(previewLine, getLineLabel()));
			jPanel.add(createPanel(previewTri, getTriangleLabel()));
			jPanel.add(createPanel(previewSq, getSquareLabel()));
			
			jPanel.setPreferredSize(new Dimension(100, 275));	
			jPanel.setBackground(Color.BLACK);
		}
		return jPanel;
	}
	/**
	 * Creates a JPanel and adds the tetris board and label.
	 * @param board
	 * @param label
	 * @return JPanel
	 */
	private JPanel createPanel(TetrisBoard board, JLabel label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setPreferredSize(new Dimension(100, 40));
		
		JPanel previewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		previewPanel.setPreferredSize(new Dimension(60, 40));
		previewPanel.add(board.getComponent());
		previewPanel.setBackground(Color.BLACK);
		
		panel.add(previewPanel);
		panel.add(label);
		panel.setBackground(Color.BLACK);
		return panel;	
	}
	
	/**
	 * Gets the Z Block label.
	 * @return JLabel
	 */
	private JLabel getZLabel() {
		if (jLabel == null) {
			jLabel = new JLabel(ZERO, JLabel.RIGHT);
			jLabel.setPreferredSize(new Dimension(40, 40));
			jLabel.setBackground(Color.BLACK);
			jLabel.setForeground(Color.WHITE);
			jLabel.setOpaque(true);
			jLabel.setAlignmentY(0.5F);
			jLabel.setFont(FONT);
		}
		return jLabel;	
	}
	/**
	 * Gets the S Block label.
	 * @return JLabel
	 */
	private JLabel getSLabel() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel2.setPreferredSize(new Dimension(40, 40));
			jLabel2.setBackground(Color.BLACK);
			jLabel2.setForeground(Color.WHITE);
			jLabel2.setOpaque(true);
			jLabel2.setAlignmentY(0.5F);
			jLabel2.setFont(FONT);
		}
		return jLabel2;	
	}
	/**
	 * Gets the left block label.
	 * @return JLabel
	 */
	private JLabel getLeftLabel() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel3.setPreferredSize(new Dimension(40, 40));
			jLabel3.setBackground(Color.BLACK);
			jLabel3.setForeground(Color.WHITE);
			jLabel3.setOpaque(true);
			jLabel3.setAlignmentY(0.5F);
			jLabel3.setFont(FONT);
		}
		return jLabel3;	
	}
	/**
	 * Gets the right block label.
	 * @return JLabel
	 */
	private JLabel getRightLabel() {
		if (jLabel4 == null) {
			jLabel4 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel4.setPreferredSize(new Dimension(40, 40));
			jLabel4.setBackground(Color.BLACK);
			jLabel4.setForeground(Color.WHITE);
			jLabel4.setOpaque(true);
			jLabel4.setAlignmentY(0.5F);
			jLabel4.setFont(FONT);
		}
		return jLabel4;	
	}
	/**
	 * Gets the line block label.
	 * @return JLabel
	 */
	private JLabel getLineLabel() {
		if (jLabel5 == null) {
			jLabel5 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel5.setPreferredSize(new Dimension(40, 40));
			jLabel5.setBackground(Color.BLACK);
			jLabel5.setForeground(Color.WHITE);
			jLabel5.setOpaque(true);
			jLabel5.setAlignmentY(0.5F);
			jLabel5.setFont(FONT);
		}
		return jLabel5;	
	}
	/**
	 * Gets the triangle Block label.
	 * @return JLabel
	 */
	private JLabel getTriangleLabel() {
		if (jLabel6 == null) {
			jLabel6 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel6.setPreferredSize(new Dimension(40, 40));
			jLabel6.setBackground(Color.BLACK);
			jLabel6.setForeground(Color.WHITE);
			jLabel6.setOpaque(true);
			jLabel6.setAlignmentY(0.5F);
			jLabel6.setFont(FONT);
		}
		return jLabel6;	
	}
	/**
	 * Gets the square block label.
	 * @return JLabel
	 */
	private JLabel getSquareLabel() {
		if (jLabel7 == null) {
			jLabel7 = new JLabel(ZERO, JLabel.RIGHT);
			jLabel7.setPreferredSize(new Dimension(40, 40));
			jLabel7.setBackground(Color.BLACK);
			jLabel7.setForeground(Color.WHITE);
			jLabel7.setOpaque(true);
			jLabel7.setAlignmentY(0.5F);
			jLabel7.setFont(FONT);
		}
		return jLabel7;	
	}

}
