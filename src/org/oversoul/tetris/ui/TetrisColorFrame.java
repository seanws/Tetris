/**
 * TetrisColorDialog.java
 * 
 * Created on 21-Feb-04
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.oversoul.border.GradientIcon;
import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.Block;
import org.oversoul.tetris.BlockType;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisBoard;
import org.oversoul.tetris.TetrisColorScheme;
import org.oversoul.tetris.TetrisColors;
import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisDefaults;
import org.oversoul.tetris.TetrisPanel;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.util.Util;

/**
 * The Color Chooser frame that allows you to change the colors of each 
 * block in Tetris.
 * 
 * @author Chris Callendar (9902588)
 * @date   21-Feb-04, 7:27:19 PM
 */
public class TetrisColorFrame extends JFrame implements ActionListener, ChangeListener, MouseListener {

	private static final long serialVersionUID = 1;

	private final Font FONT_TITLE = new Font("Arial", Font.BOLD, 12);
	private final Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
	//private final Border BORDER_SELECTED = BorderFactory.createLineBorder(Color.YELLOW, 1);
	private final Dimension PREVIEW_DIMENSION = new Dimension(100, 50);
	
	private final String ACTION_FIGURE = "Figure";
	
	private TetrisRootPane rootPane = null;
	private IUIController uiController = null;
	
	private JRadioButton selected = null;

	private JColorChooser colorChooser = null;
	private TetrisColors blockColors = null;
	private TetrisPanel panelColors = null;
	private Hashtable<String, TetrisColorScheme> colorSchemes = null;
	private Hashtable<JRadioButton, Block> figureMap = null;
	private Hashtable<JRadioButton, Color> panelMap = null;
	private boolean ignoreColorChange = false;
	private boolean ignoreComboChange = false;
	private PanelBorder panelBorder = null;
	private ImageIcon recentColorIcon = null;
	private int recentColorIndex = 0;
	private JLabel[] recentColorLabels = null;
	private boolean ignoreRecentColorChange = false;

	// preview boards
	private TetrisBoard previewZ 	= null;
	private TetrisBoard previewS 	= null;
	private TetrisBoard previewL 	= null;
	private TetrisBoard previewR 	= null;
	private TetrisBoard previewLine = null;
	private TetrisBoard previewTri 	= null;
	private TetrisBoard previewSq	= null;

	// TODO too many fields
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private JPanel jPanel8 = null;
	private JPanel jPanel9 = null;
	private JPanel jPanel10 = null;
    private JRadioButton zRadioButton = null;
    private JRadioButton sRadioButton = null;
    private JRadioButton leftRadioButton = null;
    private JRadioButton rightRadioButton = null;
    private JRadioButton lineRadioButton = null;
    private JRadioButton triangleRadioButton = null;
    private JRadioButton squareRadioButton = null;
	private JRadioButton firstColorRadioButton = null;
	private JRadioButton secondColorRadioButton = null;
	private JRadioButton titleColorRadioButton = null;
	private JRadioButton borderColorRadioButton = null;
	private JRadioButton horizontalGradientRadioButton = null;
	private JRadioButton verticalGradientRadioButton = null;
	private JRadioButton flashColorRadioButton = null;
	private JCheckBox jCheckBox = null;
	private ButtonGroup directionButtonGroup = null;
	private ButtonGroup colorsButtonGroup = null;
	private JButton jButton = null;
	private JButton jButton7 = null;
	private JButton jButton8 = null;
	private JButton jButton10 = null;
	private JButton jButton11 = null;
    private JComboBox jComboBox = null;

	/**
	 * Constructor for TetrisColorDialog.
	 * @throws HeadlessException
	 */
	public TetrisColorFrame(TetrisRootPane rootPane, 
							IUIController uiController) throws HeadlessException {
		super();
		this.rootPane = rootPane;
		this.uiController = uiController;
		this.figureMap = new Hashtable<JRadioButton, Block>();
		this.panelMap = new Hashtable<JRadioButton, Color>();
		
		this.blockColors = (TetrisColors)uiController.getPlayer().getColors().clone();
		this.panelColors = (TetrisPanel)uiController.getPlayer().getPanel().clone();
		this.colorSchemes = uiController.getConfiguration().loadColorSchemes();
		
		previewZ = new TetrisBoard(5, 4, blockColors);
		previewS = new TetrisBoard(5, 4, blockColors);
		previewL = new TetrisBoard(5, 4, blockColors);
		previewR = new TetrisBoard(5, 4, blockColors);
		previewLine = new TetrisBoard(5, 4, blockColors);
		previewTri = new TetrisBoard(5, 4, blockColors);
		previewSq = new TetrisBoard(5, 4, blockColors);

		recentColorIcon = Util.getIconResource("/images/recent_color.gif");					

		initialize();

		this.addWindowListener(new WindowClosingDisposer(this));
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		if (rootPane != null) {
			rootPane.requestFocus();
		}
		super.dispose();
	}

	/**
	 * Gets the background color of the recent color label which fired the mouse event
	 * and sets the color chooser to that color.
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		JLabel label = (JLabel)e.getSource();
		if (label != null) {
			ignoreRecentColorChange = true;
			colorChooser.setColor(label.getBackground());	
			ignoreRecentColorChange = false;
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("SaveColors")) {
			saveColors();
			dispose();
		} else if ("Cancel".equals(action)) {
			dispose();
		} else if ("DefaultColors".equals(action)) {
			defaultColors();
		} else if ("SaveColorScheme".equals(action)) {
			saveColorScheme();
		} else if ("LoadColorScheme".equals(action)) {
			loadColorScheme();
		} else if ("DeleteColorScheme".equals(action)) {
			deleteColorScheme();
		} else if (ACTION_FIGURE.equals(action)) {
			setSelectedBlock((JRadioButton)e.getSource());
		} else if ("FirstColor".equals(action)) {
			setSelected((JRadioButton)e.getSource(), panelColors.getFirstColor());
		} else if ("SecondColor".equals(action)) {
			setSelected((JRadioButton)e.getSource(), panelColors.getSecondColor());
		} else if ("TitleColor".equals(action)) {
			setSelected((JRadioButton)e.getSource(), panelColors.getTitleColor());
		} else if ("BorderColor".equals(action)) {
			setSelected((JRadioButton)e.getSource(), panelColors.getLineColor());
		} else if ("Horizontal".equals(action)) {
			panelBorder.setGradientDirection(PanelBorder.DIRECTION_LEFT_RIGHT);
			getJPanel8().repaint();
		} else if ("Vertical".equals(action)) {
			panelBorder.setGradientDirection(PanelBorder.DIRECTION_TOP_BOTTOM);
			getJPanel8().repaint();
		} else if ("RoundedCorners".equals(action)) {
			panelBorder.setRoundedCorners(getRoundedCornersCheckBox().isSelected());
			getJPanel8().repaint();
		} else if ("Flash".equals(action)) {
			setSelected((JRadioButton)e.getSource(), getFlashColorButton().getBackground());	
		}
	}

	/**
	 * @see event.ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		Color newColor = colorChooser.getColor();
		if (!ignoreRecentColorChange) {
			recentColorLabels[recentColorIndex].setBackground(newColor);
			recentColorIndex = (recentColorIndex+1) % recentColorLabels.length;
		}
		if (!ignoreColorChange && (selected != null)) {
			boolean updated = true;
			if (figureMap.containsKey(selected)) {
				Block block = figureMap.get(selected);
				TetrisBoard board = null;
				switch (block.getType()) {
				case Z :
					board = previewZ;
					break;
				case S :
					board = previewS;
					break;
				case LEFT :
					board = previewL;
					break;
				case RIGHT :
					board = previewR;
					break;
				case LINE :
					board = previewLine;
					break;
				case TRIANGLE : 
					board = previewTri;
					break;
				case SQUARE :
					board = previewSq;
					break;				
				}
				if (board != null) {
					board.clear();
					block = new Block(newColor, block.getType());
					block.attach(board, true);
					figureMap.put(selected, block);
				}
			} else if (selected.equals(getFirstColorButton())) {
				panelColors.setFirstColor(newColor);
				panelBorder.setFirstColor(newColor);
				getFirstColorButton().setBackground(newColor);
				getFirstColorButton().setForeground(getButtonForegroundColor(newColor));
				getJPanel8().repaint();
			} else if (selected.equals(getSecondColorButton())) {
				panelColors.setSecondColor(newColor);
				panelBorder.setSecondColor(newColor);
				getSecondColorButton().setBackground(newColor);
				getSecondColorButton().setForeground(getButtonForegroundColor(newColor));
				getJPanel8().repaint();
			} else if (selected.equals(getTitleColorButton())) {
				panelColors.setTitleColor(newColor);	
				panelBorder.setTitleColor(newColor);
				getTitleColorButton().setBackground(newColor);
				getTitleColorButton().setForeground(getButtonForegroundColor(newColor));
				getJPanel8().repaint();
			} else if (selected.equals(getBorderColorButton())) {
				panelColors.setLineColor(newColor);	
				panelBorder.setLineColor(newColor);
				getBorderColorButton().setBackground(newColor);
				getBorderColorButton().setForeground(getButtonForegroundColor(newColor));
				getJPanel8().repaint();
			} else if (selected.equals(getFlashColorButton())) {
				blockColors.flash = newColor;
				getFlashColorButton().setBackground(newColor);
				getFlashColorButton().setForeground(getButtonForegroundColor(newColor));
			} else {
				updated = false;
			}
			if (updated) {
				getJComboBox().setSelectedIndex(0);
			}
		}
	}
	
	private void setSelectedBlock(JRadioButton button) {
		String name = button.getName();
		BlockType type = BlockType.valueOf(name);
		Color selectedColor = blockColors.getColor(type);
		setSelected(button, selectedColor);
	}
	
	private void setSelected(JRadioButton button, Color color) {
		selected = button;
		ignoreColorChange = true;
		colorChooser.setColor(color);
		ignoreColorChange = false;
	}
	
	/**
	 * Restores the color choices before any changes.
	 */
	private void saveColors() {
		panelColors.setDirection((getHorizontalRadioButton().isSelected() ? PanelBorder.DIRECTION_LEFT_RIGHT : PanelBorder.DIRECTION_TOP_BOTTOM));
		panelColors.setRoundedCorners(getRoundedCornersCheckBox().isSelected());
		panelColors.setNeedRepaint(true);
		TetrisPlayer player = uiController.getPlayer();
		player.setPanel(panelColors);
		player.savePanel(uiController.getConfiguration());
		player.setColors(blockColors);
		player.saveColors(uiController.getConfiguration());

		rootPane.setTetrisPanel(panelColors);
		rootPane.repaintPanelBorders();
		
		uiController.reloadPlayer();
	}

	/**
	 * Resets the preview components.
	 */
	private void resetPreviews() {
		previewZ.clear();
		previewS.clear();
		previewL.clear();
		previewR.clear();
		previewLine.clear();
		previewTri.clear();
		previewSq.clear();
		initFigures();
	}

	/**
 	* Resets the panel back to default colors.
 	*/
	private void resetPanel() {
		panelColors = new TetrisPanel();
		panelColors.setNeedRepaint(true);
		updatePanelColors();
	}

	/**
	 * Restores the default color choices.
	 */
	private void defaultColors() {
		getJComboBox().setSelectedItem("");
		blockColors = new TetrisColors();
		resetPreviews();
		resetPanel();
	}
	
	/**
	 * Updates the colors on the radio buttons and sample panel.
	 */
	private void updatePanelColors() {
		if (panelColors.isNeedRepaint()) {
			panelBorder.setPanelProperties(panelColors.getFirstColor(), panelColors.getSecondColor(), panelColors.getTitleColor(), panelColors.getLineColor(), panelColors.getDirection(), panelColors.isRoundedCorners());
			getJPanel8().repaint();
			
			getFirstColorButton().setBackground(panelColors.getFirstColor());
			getFirstColorButton().setForeground(getButtonForegroundColor(panelColors.getFirstColor()));
			getSecondColorButton().setBackground(panelColors.getSecondColor());
			getSecondColorButton().setForeground(getButtonForegroundColor(panelColors.getSecondColor()));
			getTitleColorButton().setBackground(panelColors.getTitleColor());
			getTitleColorButton().setForeground(getButtonForegroundColor(panelColors.getTitleColor()));
			getBorderColorButton().setBackground(panelColors.getLineColor());
			getBorderColorButton().setForeground(getButtonForegroundColor(panelColors.getLineColor()));
			getRoundedCornersCheckBox().setSelected(panelColors.isRoundedCorners());

			panelColors.setNeedRepaint(false);
		}
	}

	/**
	 * Loads the selected color scheme.
	 */
	private void loadColorScheme() {
		String schemeName = (String)getJComboBox().getSelectedItem();
		if (!ignoreComboChange && (schemeName.length() > 0) && colorSchemes.containsKey(schemeName)) {
			TetrisColorScheme scheme = colorSchemes.get(schemeName);
			if (!blockColors.equals(scheme.getBlockColors())) {
				blockColors = scheme.getBlockColors();
				resetPreviews();
			}
			if (!panelColors.equals(scheme.getPanelColors())) {
				panelColors = scheme.getPanelColors();
				panelColors.setNeedRepaint(true);
				updatePanelColors();
			}
		}
	}

	/**
	 * Loads the selected color scheme.
	 */
	private void deleteColorScheme() {
		String schemeName = (String)getJComboBox().getSelectedItem();
		if (schemeName.length() > 0) {
			int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the '" + schemeName + "' color scheme?", "Delete color scheme...", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				if (colorSchemes.containsKey(schemeName)) {
					TetrisColorScheme cs = colorSchemes.remove(schemeName);
					cs.removeColorScheme(uiController.getConfiguration());
				}
				getJComboBox().removeItem(schemeName);
				defaultColors();
			}
		}
	}
	
	private void saveColorScheme() {
		String schemeName = JOptionPane.showInputDialog(this, "Enter a name for this color scheme.", "Save color scheme", JOptionPane.OK_CANCEL_OPTION);
		if ((schemeName != null) && (schemeName.length() > 0)) {
			schemeName = schemeName.trim();
			boolean save = true;
			schemeName.replace(' ', '_');
			schemeName.replace('.', '_');
			if (colorSchemes.containsKey(schemeName)) {
				save = JOptionPane.showConfirmDialog(this, "Overwrite existing color scheme called '" + schemeName + "'?", "Overwrite?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			} else {
				getJComboBox().addItem(schemeName);
			}
			if (save) {
				TetrisPanel tp = (TetrisPanel)panelColors.clone();
				tp.setDirection((getHorizontalRadioButton().isSelected() ? PanelBorder.DIRECTION_LEFT_RIGHT : PanelBorder.DIRECTION_TOP_BOTTOM));
				tp.setRoundedCorners(getRoundedCornersCheckBox().isSelected());				
				TetrisColorScheme cs = new TetrisColorScheme(schemeName, (TetrisColors)blockColors.clone(), tp);
				cs.saveColorScheme(uiController.getConfiguration());
				colorSchemes.put(schemeName, cs);
			}
			ignoreComboChange = true;
			getJComboBox().setSelectedItem(schemeName);
			ignoreComboChange = false;
		}
	}
	
	/**
	 * Adds the current color schemes to the combo box. 
	 */
	private void initColorSchemes() {
		getJComboBox().addItem("");
		if (colorSchemes.size() > 0) {
			String[] names = new String[colorSchemes.size()];
			names = colorSchemes.keySet().toArray(names);
			if (names.length > 1) {
				Arrays.sort(names);
			}
			for (int i = 0; i < names.length; i++) {
				getJComboBox().addItem(names[i]);			
			}
		}
		getJComboBox().setSelectedIndex(0);
	}

	/**
	 * Gets a color for the button text.  Returns white for dark backgrounds
	 * or black for light backgrounds.
	 * @param bgColor
	 * @return Color
	 */
	private Color getButtonForegroundColor(Color bgColor) {
		Color fgColor = Color.BLACK;
		int sum = bgColor.getRed() + bgColor.getGreen() + bgColor.getBlue();
		if (sum < 360) {
			fgColor = Color.WHITE;	
		}
		return fgColor;	
	}

	/**
	 * Determines and sets the best location for this frame.
	 */
	private void calculateLocation() {
		//int gfw = gameFrame.getWidth();
		int gfx = rootPane.getTopLeft().x, gfy = rootPane.getTopLeft().y;
		int width = this.getSize().width;
		int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int x = gfx;
		int y = gfy + 100;		
		if ((gfx + width) < screenWidth) {
			this.setLocation(x, y);
		} else {
			x = screenWidth - width - 10;
			this.setLocation(x, y);
		}
	}
	
	private void initializePanelSet() {
		panelMap.put(getFirstColorButton(), panelColors.getFirstColor());
		panelMap.put(getSecondColorButton(), panelColors.getSecondColor());
		panelMap.put(getTitleColorButton(), panelColors.getTitleColor());
		panelMap.put(getBorderColorButton(), panelColors.getLineColor());
	}
	
	
	/**
	 * Initializes the figures array. 
	 */
	private void initFigures() {
		figureMap.clear();
		createBlock(blockColors.z, BlockType.Z, previewZ, getZRadioButton());
		createBlock(blockColors.s, BlockType.S, previewS, getSRadioButton());
		createBlock(blockColors.left, BlockType.LEFT, previewL, getLeftRadioButton());
		createBlock(blockColors.right, BlockType.RIGHT, previewR, getRightRadioButton());
		createBlock(blockColors.line, BlockType.LINE, previewLine, getLineRadioButton());
		createBlock(blockColors.tri, BlockType.TRIANGLE, previewTri, getTriangleRadioButton());
		createBlock(blockColors.square, BlockType.SQUARE, previewSq, getSquareRadioButton());
	}
	
	private void createBlock(Color c, BlockType type, TetrisBoard board, JRadioButton btn) {
		Block block = new Block(c, type);
		block.attach(board, true);
		figureMap.put(btn, block);
	}
	
	////////////////////////////////////////////////////////////////////////
	// AUTOGENERATED Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * This method initializes this Frame.
	 */
	private void initialize() {
        this.setContentPane(getRootContentPane());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(743, 409);
		this.setIconImage(Util.getImageResource("/images/tetris16_3.gif"));
		this.setTitle("Tetris Color Chooser");
        this.setResizable(true);
		this.pack();
		calculateLocation();
        initFigures();
		initializePanelSet();
		initColorSchemes();
		getSaveButton().requestFocusInWindow();
		this.setVisible(true);
	}

	private JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel1(), null);
			jContentPane.setPreferredSize(new Dimension(740, 380));
		}
		return jContentPane;
	}

	private JPanel getJPanel5() {
		if(jPanel5 == null) {
			jPanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			jPanel5.add(getJPanel4(), null);
			jPanel5.add(getJPanel3(), null);
			jPanel5.setPreferredSize(new Dimension(480,350));
		}
		return jPanel5;
	}

	private JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel5(), null);
			jPanel.add(getJPanel7(), null);
			jPanel.setPreferredSize(new Dimension(740, 350));
		}
		return jPanel;
	}

	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
			JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0));
			JLabel colorSchemeLabel = new JLabel("Load Color Scheme: ");
			colorSchemeLabel.setFont(FONT_TITLE);
			jp.add(colorSchemeLabel, null);
			jp.add(getJComboBox(), null);
			jp.setPreferredSize(new Dimension(335, 30));
			c.gridx = 0;
			c.gridy = 0;
			jPanel3.add(jp, c);

			jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0));
			jp.add(getSaveSchemeButton(), null);
			jp.add(getJButton11(), null);
			jp.setPreferredSize(new Dimension(335, 30));
			c.gridx = 0;
			c.gridy = 1;
			jPanel3.add(jp, c);
			
			c.gridx = 1;
			c.gridy = 0;
			c.gridheight = 2;
			jPanel3.add(getJPanel9(), c);
			jPanel3.setPreferredSize(new Dimension(460, 100));
		}
		return jPanel3;
	}

	private JButton getSaveButton() {
		if(jButton7 == null) {
			jButton7 = new JButton("Save Colors");
			jButton7.addActionListener(this);
			jButton7.setMnemonic(KeyEvent.VK_S);
			jButton7.setActionCommand("SaveColors");
		}
		return jButton7;
	}

	private JButton getCancelButton() {
		if(jButton8 == null) {
			jButton8 = new JButton(" Cancel ");
			jButton8.addActionListener(this);
			jButton8.setActionCommand("Cancel");
		}
		return jButton8;
	}

	private JButton getDefaultColorsButton() {
		if(jButton == null) {
			jButton = new JButton("Defaults Colors");
			jButton.addActionListener(this);
			jButton.setActionCommand("DefaultColors");
		}
		return jButton;
	}

	private JButton getSaveSchemeButton() {
		if(jButton10 == null) {
			jButton10 = new JButton("Save Scheme", 
						Util.getIconResource("/images/save.gif"));
			jButton10.setActionCommand("SaveColorScheme");
			jButton10.setToolTipText("Save the current color scheme");
			jButton10.addActionListener(this);
		}
		return jButton10;
	}

	private JButton getJButton11() {
		if(jButton11 == null) {
			jButton11 = new JButton("Delete Scheme", 
						Util.getIconResource("/images/delete.gif"));
			jButton11.setActionCommand("DeleteColorScheme");
			jButton11.setToolTipText("Delete selected color scheme");
			jButton11.addActionListener(this);
		}
		return jButton11;
	}

	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel(new GridLayout(7, 1, 0, 0));
			jPanel2.add(getZRadioButton(), null);
			jPanel2.add(getSRadioButton(), null);
			jPanel2.add(getLeftRadioButton(), null);
			jPanel2.add(getRightRadioButton(), null);
			jPanel2.add(getLineRadioButton(), null);
			jPanel2.add(getTriangleRadioButton(), null);
			jPanel2.add(getSquareRadioButton(), null);
			jPanel2.setPreferredSize(new Dimension(100,350));
			jPanel2.setBackground(Color.BLACK);
		}
		return jPanel2;
	}

	private JRadioButton getZRadioButton() {
		if(zRadioButton == null) {
			zRadioButton = new JRadioButton();
			zRadioButton.setActionCommand(ACTION_FIGURE);
			zRadioButton.addActionListener(this);
			zRadioButton.setName(BlockType.Z.name());
			zRadioButton.setCursor(CURSOR_HAND);
			zRadioButton.add(previewZ.getComponent());
			zRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			zRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(zRadioButton);
		}
		return zRadioButton;
	}

	private JRadioButton getSRadioButton() {
		if(sRadioButton == null) {
			sRadioButton = new JRadioButton();
			sRadioButton.setActionCommand(ACTION_FIGURE);
			sRadioButton.addActionListener(this);
			sRadioButton.setName(BlockType.S.name());
			sRadioButton.setCursor(CURSOR_HAND);
			sRadioButton.add(previewS.getComponent());
			sRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			sRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(sRadioButton);
		}
		return sRadioButton;
	}

	private JRadioButton getLeftRadioButton() {
		if(leftRadioButton == null) {
			leftRadioButton = new JRadioButton();
			leftRadioButton.setActionCommand(ACTION_FIGURE);
			leftRadioButton.addActionListener(this);
			leftRadioButton.setName(BlockType.LEFT.name());
			leftRadioButton.setCursor(CURSOR_HAND);
			leftRadioButton.add(previewL.getComponent());
			leftRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			leftRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(leftRadioButton);
		}
		return leftRadioButton;
	}

	private JRadioButton getRightRadioButton() {
		if(rightRadioButton == null) {
			rightRadioButton = new JRadioButton();
			rightRadioButton.setActionCommand(ACTION_FIGURE);
			rightRadioButton.addActionListener(this);
			rightRadioButton.setName(BlockType.RIGHT.name());
			rightRadioButton.setCursor(CURSOR_HAND);
			rightRadioButton.add(previewR.getComponent());
			rightRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			rightRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(rightRadioButton);
		}
		return rightRadioButton;
	}

	private JRadioButton getLineRadioButton() {
		if(lineRadioButton == null) {
			lineRadioButton = new JRadioButton();
			lineRadioButton.setActionCommand(ACTION_FIGURE);
			lineRadioButton.addActionListener(this);
			lineRadioButton.setName(BlockType.LINE.name());
			lineRadioButton.setCursor(CURSOR_HAND);
			lineRadioButton.add(previewLine.getComponent());
			lineRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			lineRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(lineRadioButton);
		}
		return lineRadioButton;
	}

	private JRadioButton getTriangleRadioButton() {
		if(triangleRadioButton == null) {
			triangleRadioButton = new JRadioButton();
			triangleRadioButton.setActionCommand(ACTION_FIGURE);
			triangleRadioButton.addActionListener(this);
			triangleRadioButton.setName(BlockType.TRIANGLE.name());
			triangleRadioButton.setCursor(CURSOR_HAND);
			triangleRadioButton.add(previewTri.getComponent());
			triangleRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			triangleRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(triangleRadioButton);
		}
		return triangleRadioButton;
	}

	private JRadioButton getSquareRadioButton() {
		if(squareRadioButton == null) {
			squareRadioButton = new JRadioButton();
			squareRadioButton.setActionCommand(ACTION_FIGURE);
			squareRadioButton.addActionListener(this);
			squareRadioButton.setName(BlockType.SQUARE.name());
			squareRadioButton.setCursor(CURSOR_HAND);
			squareRadioButton.add(previewSq.getComponent());
			squareRadioButton.setPreferredSize(PREVIEW_DIMENSION);
			squareRadioButton.setBackground(Color.BLACK);
			getColorsButtonGroup().add(squareRadioButton);
		}
		return squareRadioButton;
	}

	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jPanel7 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
			jPanel7.setPreferredSize(new Dimension(150, 350));
			JLabel label = new JLabel("Panel Colors:");
			label.setFont(FONT_TITLE);
			label.setPreferredSize(new Dimension(120, 20));
			jPanel7.add(label);
			jPanel7.add(getJPanel6());
			
			JPanel dirPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
			dirPanel.setPreferredSize(new Dimension(100, 35));
			dirPanel.add(getVerticalRadioButton());
			dirPanel.add(getHorizontalRadioButton());
			
			jPanel7.add(dirPanel);
			jPanel7.add(getJPanel8());
		}
		return jPanel7;	
	}	

	private JPanel getJPanel8() {
		if(jPanel8 == null) {
			jPanel8 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
			panelBorder = panelColors.getPanelBorder("Sample Panel", jPanel8.getBackground(), Color.WHITE);
			jPanel8.setBorder(panelBorder);
			jPanel8.setPreferredSize(new Dimension(110,100));
		}
		return jPanel8;
	}

	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jPanel9 = new JPanel(new GridLayout(2, 1, 0, 1));
			JPanel panel = new JPanel(new GridLayout(1, 1, 0, 0));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			panel.add(getFlashColorButton());
			jPanel9.add(panel);
			jPanel9.add(getRecentColorsPanel());
			jPanel9.setPreferredSize(new Dimension(120, 60));
		}
		return jPanel9;	
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
			jPanel1.setPreferredSize(new Dimension(730, 30));
			jPanel1.add(getSaveButton());
			jPanel1.add(getCancelButton());
			jPanel1.add(getDefaultColorsButton());
		}
		return jPanel1;
	}

	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel(new GridLayout(5, 1, 0, 0));
			jPanel6.add(getFirstColorButton());
			jPanel6.add(getSecondColorButton());
			jPanel6.add(getTitleColorButton());
			jPanel6.add(getBorderColorButton());
			jPanel6.add(getRoundedCornersCheckBox());
			jPanel6.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
		return jPanel6;
	}

	private JPanel getJPanel4() {
		if(jPanel4 == null) {
			jPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
			jPanel4.setPreferredSize(new Dimension(480,260));
			colorChooser = new JColorChooser(Color.BLACK);
			colorChooser.setPreviewPanel(new JPanel());
			colorChooser.getSelectionModel().addChangeListener(this);
			jPanel4.add(colorChooser, null);
		}
		return jPanel4;
	}

	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setPreferredSize(new Dimension(150,20));
			jComboBox.setToolTipText("Load Color Scheme");
			jComboBox.setActionCommand("LoadColorScheme");			
			jComboBox.addActionListener(this);
		}
		return jComboBox;
	}

	private JRadioButton getHorizontalRadioButton() {
		if (horizontalGradientRadioButton == null) {
			Color[] norm = TetrisDefaults.GRADIENT_BLUE;
			Color[] roll = TetrisDefaults.GRADIENT_GREEN;
			Color[] sel = TetrisDefaults.GRADIENT_ORANGE;
			norm[0] = norm[0].brighter();
			roll[0] = roll[0].brighter();
			sel[0] = sel[0].brighter();
			
			// horizontal gradient
			GradientIcon icon = new GradientIcon(35, 25, norm[0], norm[1]);
			icon.setBorder(norm[3], 1);
			icon.setGradientDirection(GradientIcon.DIRECTION_LEFT_RIGHT);

			GradientIcon rolloverIcon = new GradientIcon(35, 25, roll[0], roll[1]);
			rolloverIcon.setBorder(roll[3], 2);
			rolloverIcon.setGradientDirection(GradientIcon.DIRECTION_LEFT_RIGHT);

			GradientIcon selIcon = new GradientIcon(35, 25, sel[0], sel[1]);
			selIcon.setBorder(sel[3], 2);
			selIcon.setGradientDirection(GradientIcon.DIRECTION_LEFT_RIGHT);

			horizontalGradientRadioButton = new JRadioButton(icon, false);
			horizontalGradientRadioButton.setRolloverEnabled(true);
			horizontalGradientRadioButton.setRolloverIcon(rolloverIcon);
			horizontalGradientRadioButton.setSelectedIcon(selIcon);
			horizontalGradientRadioButton.setRolloverSelectedIcon(rolloverIcon);
			horizontalGradientRadioButton.setToolTipText("Horizontal gradient");
			horizontalGradientRadioButton.setCursor(CURSOR_HAND);
			getDirectionButtonGroup().add(horizontalGradientRadioButton);
			horizontalGradientRadioButton.addActionListener(this);
			horizontalGradientRadioButton.setActionCommand("Horizontal");
		}
		return horizontalGradientRadioButton;	
	}

	private JRadioButton getVerticalRadioButton() {
		if (verticalGradientRadioButton == null) {
			Color[] norm = TetrisDefaults.GRADIENT_BLUE;
			Color[] roll = TetrisDefaults.GRADIENT_GREEN;
			Color[] sel = TetrisDefaults.GRADIENT_ORANGE;
			norm[0] = norm[0].brighter();
			roll[0] = roll[0].brighter();
			sel[0] = sel[0].brighter();
			
			// horizontal gradient
			GradientIcon icon = new GradientIcon(35, 25, norm[0], norm[1]);
			icon.setBorder(norm[3], 1);
			icon.setGradientDirection(GradientIcon.DIRECTION_TOP_BOTTOM);

			GradientIcon rolloverIcon = new GradientIcon(35, 25, roll[0], roll[1]);
			rolloverIcon.setBorder(roll[3], 2);
			rolloverIcon.setGradientDirection(GradientIcon.DIRECTION_TOP_BOTTOM);

			GradientIcon selIcon = new GradientIcon(35, 25, sel[0], sel[1]);
			selIcon.setBorder(sel[3], 2);
			selIcon.setGradientDirection(GradientIcon.DIRECTION_TOP_BOTTOM);

			verticalGradientRadioButton = new JRadioButton(icon, true);
			verticalGradientRadioButton.setRolloverEnabled(true);
			verticalGradientRadioButton.setRolloverIcon(rolloverIcon);
			verticalGradientRadioButton.setSelectedIcon(selIcon);
			verticalGradientRadioButton.setRolloverSelectedIcon(rolloverIcon);
			verticalGradientRadioButton.setToolTipText("Vertical gradient");
			verticalGradientRadioButton.setCursor(CURSOR_HAND);
			getDirectionButtonGroup().add(verticalGradientRadioButton);
			verticalGradientRadioButton.addActionListener(this);
			verticalGradientRadioButton.setActionCommand("Vertical");
		}
		return verticalGradientRadioButton;	
	}

	private JRadioButton getFirstColorButton() {
		if (firstColorRadioButton == null) {
			firstColorRadioButton = new JRadioButton("Color #1");
			firstColorRadioButton.setBackground(panelColors.getFirstColor());
			firstColorRadioButton.setForeground(getButtonForegroundColor(panelColors.getFirstColor()));
			firstColorRadioButton.addActionListener(this);
			firstColorRadioButton.setActionCommand("FirstColor");
			firstColorRadioButton.setCursor(CURSOR_HAND);
			firstColorRadioButton.setFocusPainted(false);
			firstColorRadioButton.setFont(FONT_TITLE);
			getColorsButtonGroup().add(firstColorRadioButton);
		}
		return firstColorRadioButton;	
	}

	private JRadioButton getSecondColorButton() {
		if (secondColorRadioButton == null) {
			secondColorRadioButton = new JRadioButton("Color #2");
			secondColorRadioButton.setBackground(panelColors.getSecondColor());
			secondColorRadioButton.setForeground(getButtonForegroundColor(panelColors.getSecondColor()));
			secondColorRadioButton.addActionListener(this);
			secondColorRadioButton.setActionCommand("SecondColor");
			secondColorRadioButton.setCursor(CURSOR_HAND);
			secondColorRadioButton.setFocusPainted(false);
			secondColorRadioButton.setFont(FONT_TITLE);
			getColorsButtonGroup().add(secondColorRadioButton);
		}
		return secondColorRadioButton;	
	}

	private JRadioButton getTitleColorButton() {
		if (titleColorRadioButton == null) {
			titleColorRadioButton = new JRadioButton("Title Text");
			titleColorRadioButton.setBackground(panelColors.getTitleColor());
			titleColorRadioButton.setForeground(getButtonForegroundColor(panelColors.getTitleColor()));
			titleColorRadioButton.addActionListener(this);
			titleColorRadioButton.setActionCommand("TitleColor");
			titleColorRadioButton.setCursor(CURSOR_HAND);
			titleColorRadioButton.setFocusPainted(false);
			titleColorRadioButton.setFont(FONT_TITLE);
			getColorsButtonGroup().add(titleColorRadioButton);
		}
		return titleColorRadioButton;	
	}

	private JRadioButton getBorderColorButton() {
		if (borderColorRadioButton == null) {
			borderColorRadioButton = new JRadioButton("Border");
			borderColorRadioButton.setBackground(panelColors.getLineColor());
			borderColorRadioButton.setForeground(getButtonForegroundColor(panelColors.getLineColor()));
			borderColorRadioButton.addActionListener(this);
			borderColorRadioButton.setActionCommand("BorderColor");
			borderColorRadioButton.setCursor(CURSOR_HAND);
			borderColorRadioButton.setFocusPainted(false);
			borderColorRadioButton.setFont(FONT_TITLE);
			getColorsButtonGroup().add(borderColorRadioButton);
		}
		return borderColorRadioButton;	
	}

	private ButtonGroup getDirectionButtonGroup() {
		if (directionButtonGroup == null) {
			directionButtonGroup = new ButtonGroup();
		}
		return directionButtonGroup;	
	}	

	private ButtonGroup getColorsButtonGroup() {
		if (colorsButtonGroup == null) {
			colorsButtonGroup = new ButtonGroup();
		}
		return colorsButtonGroup;	
	}	

	private JCheckBox getRoundedCornersCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox("Rounded corners", uiController.getPlayer().getPanel().isRoundedCorners());
			jCheckBox.setActionCommand("RoundedCorners");
			jCheckBox.addActionListener(this);
			jCheckBox.setForeground(Color.WHITE);
			jCheckBox.setBackground(Color.BLACK);
			jCheckBox.setFont(FONT_TITLE);
			jCheckBox.setCursor(CURSOR_HAND);
		}
		return jCheckBox;	
	}

	private JRadioButton getFlashColorButton() {
		if (flashColorRadioButton == null) {
			flashColorRadioButton = new JRadioButton("Flash line color", false);
			flashColorRadioButton.setBackground(blockColors.flash);
			flashColorRadioButton.setForeground(getButtonForegroundColor(blockColors.flash));
			flashColorRadioButton.addActionListener(this);
			flashColorRadioButton.setActionCommand("Flash");
			flashColorRadioButton.setCursor(CURSOR_HAND);
			flashColorRadioButton.setFont(FONT_TITLE);
			flashColorRadioButton.setToolTipText("Choose a color for completed lines to flash before they are removed");
			getColorsButtonGroup().add(flashColorRadioButton);
		}
		return flashColorRadioButton;	
	}

	private JPanel getRecentColorsPanel() {
		if (jPanel10 == null) {
			jPanel10 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
			
			jPanel10.add(new JLabel("Recent:"));
			final int recent = 4;
			JPanel gridPanel = new JPanel(new GridLayout(1, recent, 0, 0));
			gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			final Dimension dim = new Dimension(17,16);
			final MatteBorder border = BorderFactory.createMatteBorder(0, 1, 0, 0, Color.WHITE);
			recentColorLabels = new JLabel[recent];
			for (int i = 0; i < recent; i++) {
				JLabel label = new JLabel(recentColorIcon);
				label.setOpaque(true);
				label.setCursor(TetrisConstants.CURSOR_HAND);
				label.addMouseListener(this);
				label.setPreferredSize(dim);
				label.setBorder(border);
				label.setToolTipText("Choose a recent color");
				recentColorLabels[i] = label;	
				gridPanel.add(label);
			}
			
			jPanel10.add(gridPanel);
			jPanel10.setToolTipText("Choose a recent color");
			jPanel10.setPreferredSize(new Dimension(120, 30));
			
		}
		return jPanel10;
	}

}
