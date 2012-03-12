/**
 * TetrisControlsDialog.java
 * 
 * Created on 22-Feb-04
 */
package org.oversoul.tetris.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisControls;
import org.oversoul.tetris.TetrisPanel;

/**
 * Lets the user choose which keys to use for Tetris gameplay.
 * 
 * @author Chris Callendar (9902588)
 * @date   22-Feb-04, 1:05:43 AM
 */
public class TetrisControlsDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1;

	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int DROP = 3;
	private static final int ROTATE = 4;
	private static final int PAUSE = 5;
	private static final int PREVIEW = 6;
	private static final int LEVELUP = 7;
	private static final String EMPTY = "";

	private TetrisRootPane rootPane;
	private IUIController uiController = null;
	private TetrisControls currentControls = null;
	private ArrayList<JTextField> textFields = null;
	private boolean fullyLoaded = false;
	
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
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JTextField leftKeyTextField = null;
	private JTextField rightKeyTextField = null;
	private JTextField downKeyTextField = null;
	private JTextField dropKeyTextField = null;
	private JTextField rotateKeyTextField = null;
	private JTextField pauseKeyTextField = null;
	private JTextField previewKeyTextField = null;
	private JTextField levelUpKeyTextField = null;

	/**
	 * Constructor for TetrisControlsDialog.
	 * @param controller
	 * @throws HeadlessException
	 */
	public TetrisControlsDialog(Frame parent, TetrisRootPane rootPane, IUIController uiController) throws HeadlessException {
		super(parent);
		this.rootPane = rootPane;
		this.uiController = uiController;
		this.textFields = new ArrayList<JTextField>(8);
		this.currentControls = (TetrisControls)uiController.getPlayer().getControls().clone();
		initialize();
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	//////////////////////////////////////////////////////////////////////
	// ACTION / KEYEVENT Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Save")) {
			if (saveControls()) {
				dispose();
			}
		} else if (action.equals("Cancel")) {
			dispose();
		} else if (action.equals("DefaultKeys")) {
			restoreDefaultKeys();	
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (fullyLoaded) {
			Object obj = e.getSource();
			if (obj instanceof JTextField) {
				JTextField textField = (JTextField) obj;
				textField.setText(EMPTY);
			}
		}
	}
	/**
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (fullyLoaded) {
			int keyCode = e.getKeyCode();
	        Object obj = e.getSource();
	        if (obj instanceof JTextField) {
				JTextField textField = (JTextField) obj;
				handleKeyEvent(keyCode, textField, true);
			}
		}
	}
	/**
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/** 
	 * Handles a key event.  Checks for duplicate key controls.
	 * @param keyCode		The int key code.
	 * @param textField		The text field which the event happened on.
	 * @param checkDuplicates checks for duplicate entries.
	 */
	private void handleKeyEvent(int keyCode, JTextField textField, boolean checkDuplicates) {
		// remove possible duplicate key code
		if (checkDuplicates) {
			if (keyCode == currentControls.left) {
				getLeftKeyTextField().setText(EMPTY);
				currentControls.left = -1;
			} else if (keyCode == currentControls.right) {
				getRightKeyTextField().setText(EMPTY);
				currentControls.right = -1;
			} else if (keyCode == currentControls.down) {
				getDownKeyTextField().setText(EMPTY);
				currentControls.down = -1;
			} else if (keyCode == currentControls.drop) {
				getDropKeyTextField().setText(EMPTY);
				currentControls.drop = -1;
			} else if (keyCode == currentControls.rotate) {
				getRotateKeyTextField().setText(EMPTY);
				currentControls.rotate = -1;
			} else if (keyCode == currentControls.pause) {
				getPauseKeyTextField().setText(EMPTY);
				currentControls.pause = -1;
			} else if (keyCode == currentControls.preview) {
				getPreviewKeyTextField().setText(EMPTY);
				currentControls.preview = -1;
			} else if (keyCode == currentControls.levelUp) {
				getLevelUpKeyTextField().setText(EMPTY);
				currentControls.levelUp = -1;
			}
		}
		// assign the new keycode
		int tfCode = Integer.parseInt(textField.getName());
		switch (tfCode) {
			case LEFT :
				currentControls.left = keyCode;
				break;	
			case RIGHT :
				currentControls.right = keyCode;
				break;	
			case DOWN :
				currentControls.down = keyCode;
				break;	
			case DROP :
				currentControls.drop = keyCode;
				break;	
			case ROTATE :
				currentControls.rotate = keyCode;
				break;	
			case PAUSE :
				currentControls.pause = keyCode;
				break;	
			case PREVIEW :
				currentControls.preview = keyCode;
				break;	
			case LEVELUP :
				currentControls.levelUp = keyCode;
				break;	
		}
		textField.setText(KeyEvent.getKeyText(keyCode));
		textField.setBackground(java.awt.Color.WHITE);
	}
	
	/**
	 * Restores the default keys.
	 */
	private void restoreDefaultKeys() {
		currentControls = new TetrisControls();
		loadControls();
	}
	
	//////////////////////////////////////////////////////////////////////
	// LOAD/SAVE Methods
	//////////////////////////////////////////////////////////////////////

	/** 
	 * Loads the current control keys.
	 */
	private void loadControls() {
		handleKeyEvent(currentControls.left, leftKeyTextField, false);
		handleKeyEvent(currentControls.right, rightKeyTextField, false);
		handleKeyEvent(currentControls.down, downKeyTextField, false);
		handleKeyEvent(currentControls.drop, dropKeyTextField, false);
		handleKeyEvent(currentControls.rotate, rotateKeyTextField, false);
		handleKeyEvent(currentControls.pause, pauseKeyTextField, false);
		handleKeyEvent(currentControls.preview, previewKeyTextField, false);
		handleKeyEvent(currentControls.levelUp, levelUpKeyTextField, false);
	}

	/** 
	 * Saves the current control keys.
	 * @return boolean if saved.
	 */
	private boolean saveControls() {
		for (int i = 0; i < textFields.size(); i++) {
			JTextField tf = textFields.get(i);
			if (tf.getText().length() == 0) {
				tf.requestFocus();
				tf.setBackground(java.awt.Color.YELLOW);
				return false;	
			}	
		}
		uiController.getPlayer().setControls(currentControls);
		uiController.getPlayer().saveControls(uiController.getConfiguration());
		return true;
	}
	
	/**
	 * Adds the key textfields to an array list.
	 */
	private void addTextFields() {
		textFields.add(LEFT, getLeftKeyTextField());
		textFields.add(RIGHT, getRightKeyTextField());
		textFields.add(DOWN, getDownKeyTextField());
		textFields.add(DROP, getDropKeyTextField());
		textFields.add(ROTATE, getRotateKeyTextField());
		textFields.add(PAUSE, getPauseKeyTextField());
		textFields.add(PREVIEW, getPreviewKeyTextField());
		textFields.add(LEVELUP, getLevelUpKeyTextField());	
	}

	//////////////////////////////////////////////////////////////////////
	// GUI Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
        this.setContentPane(getRootContentPane());
        this.setSize(298, 344);
        this.setModal(true);
       	this.setLocation(rootPane.getTopLeft().x+135, rootPane.getTopLeft().y+80);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        loadControls();
        addTextFields();
        this.setResizable(false);
        this.setTitle("Tetris Game Controls");
        this.pack();
        getJButton().requestFocusInWindow();
        this.fullyLoaded = true;
		this.setVisible(true);
	}	

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

			JPanel innerPanel = new JPanel();
			BoxLayout box = new BoxLayout(innerPanel,  javax.swing.BoxLayout.Y_AXIS);
			innerPanel.setLayout(box);
			innerPanel.add(getJPanel(), null);
			innerPanel.add(getJPanel1(), null);
			innerPanel.add(getJPanel2(), null);
			innerPanel.add(getJPanel3(), null);
			innerPanel.add(getJPanel4(), null);
			innerPanel.add(getJPanel5(), null);
			innerPanel.add(getJPanel6(), null);
			innerPanel.add(getJPanel7(), null);
			innerPanel.setPreferredSize(new Dimension(275, 265));
			
			TetrisPanel panel = rootPane.getTetrisPanel();
			PanelBorder panelBorder = panel.getPanelBorder("Tetris Keys", jContentPane.getBackground());
			innerPanel.setBorder(panelBorder);
			
			jContentPane.add(innerPanel, null);
			jContentPane.add(getJPanel8(), null);
			jContentPane.setPreferredSize(new Dimension(290, 310));
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_10 = new java.awt.FlowLayout();
			layFlowLayout_10.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel.setLayout(layFlowLayout_10);
			jPanel.add(getJLabel(), null);
			jPanel.add(getLeftKeyTextField(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_11 = new java.awt.FlowLayout();
			layFlowLayout_11.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel1.setLayout(layFlowLayout_11);
			jPanel1.add(getJLabel1(), null);
			jPanel1.add(getRightKeyTextField(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_12 = new java.awt.FlowLayout();
			layFlowLayout_12.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel2.setLayout(layFlowLayout_12);
			jPanel2.add(getJLabel2(), null);
			jPanel2.add(getDownKeyTextField(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_13 = new java.awt.FlowLayout();
			layFlowLayout_13.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel3.setLayout(layFlowLayout_13);
			jPanel3.add(getJLabel3(), null);
			jPanel3.add(getDropKeyTextField(), null);
		}
		return jPanel3;
	}
	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel4() {
		if(jPanel4 == null) {
			jPanel4 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_14 = new java.awt.FlowLayout();
			layFlowLayout_14.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel4.setLayout(layFlowLayout_14);
			jPanel4.add(getJLabel4(), null);
			jPanel4.add(getRotateKeyTextField(), null);
		}
		return jPanel4;
	}
	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel5() {
		if(jPanel5 == null) {
			jPanel5 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_15 = new java.awt.FlowLayout();
			layFlowLayout_15.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel5.setLayout(layFlowLayout_15);
			jPanel5.add(getJLabel5(), null);
			jPanel5.add(getPauseKeyTextField(), null);
		}
		return jPanel5;
	}
	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel6() {
		if(jPanel6 == null) {
			jPanel6 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_16 = new java.awt.FlowLayout();
			layFlowLayout_16.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel6.setLayout(layFlowLayout_16);
			jPanel6.add(getJLabel6(), null);
			jPanel6.add(getPreviewKeyTextField(), null);
		}
		return jPanel6;
	}
	/**
	 * This method initializes jPanel7
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel7() {
		if(jPanel7 == null) {
			jPanel7 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_17 = new java.awt.FlowLayout();
			layFlowLayout_17.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel7.setLayout(layFlowLayout_17);
			jPanel7.add(getJLabel7(), null);
			jPanel7.add(getLevelUpKeyTextField(), null);
		}
		return jPanel7;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText(" Move block left: ");
			jLabel.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText(" Move block right: ");
			jLabel1.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel1;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText(" Move block down: ");
			jLabel2.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel2;
	}
	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText(" Drop block: ");
			jLabel3.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel3;
	}
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText(" Rotate block: ");
			jLabel4.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel4;
	}
	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText(" Pause game: ");
			jLabel5.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel5;
	}
	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText(" Show/Hide preview: ");
			jLabel6.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel6;
	}
	/**
	 * This method initializes jPanel8
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel8() {
		if(jPanel8 == null) {
			jPanel8 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout_18 = new java.awt.FlowLayout();
			layFlowLayout_18.setVgap(2);
			jPanel8.setLayout(layFlowLayout_18);
			jPanel8.add(getJButton(), null);
			jPanel8.add(getJButton1(), null);
			jPanel8.add(getJButton2(), null);
			//jPanel8.setPreferredSize(new java.awt.Dimension(43,30));
		}
		return jPanel8;
	}
	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText(" Increase Level: ");
			jLabel7.setPreferredSize(new java.awt.Dimension(150,15));
		}
		return jLabel7;
	}
	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton() {
		if(jButton == null) {
			jButton = new javax.swing.JButton();
			jButton.setText("  Save  ");
			jButton.addActionListener(this);
			jButton.setActionCommand("Save");
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton1() {
		if(jButton1 == null) {
			jButton1 = new javax.swing.JButton();
			jButton1.setText(" Cancel ");
			jButton1.addActionListener(this);
			jButton1.setActionCommand("Cancel");
		}
		return jButton1;
	}
	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton2() {
		if(jButton2 == null) {
			jButton2 = new javax.swing.JButton();
			jButton2.setText("Default Keys");
			jButton2.setActionCommand("DefaultKeys");
			jButton2.addActionListener(this);
		}
		return jButton2;
	}
	/**
	 * This method initializes leftKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getLeftKeyTextField() {
		if(leftKeyTextField == null) {
			leftKeyTextField = new javax.swing.JTextField();
			leftKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			leftKeyTextField.setName(EMPTY+LEFT);
			leftKeyTextField.setText(KeyEvent.getKeyText(currentControls.left));
			leftKeyTextField.addKeyListener(this);
		}
		return leftKeyTextField;
	}
	/**
	 * This method initializes rightKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getRightKeyTextField() {
		if(rightKeyTextField == null) {
			rightKeyTextField = new javax.swing.JTextField();
			rightKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			rightKeyTextField.setName(EMPTY+RIGHT);
			rightKeyTextField.addKeyListener(this);
		}
		return rightKeyTextField;
	}
	/**
	 * This method initializes downKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getDownKeyTextField() {
		if(downKeyTextField == null) {
			downKeyTextField = new javax.swing.JTextField();
			downKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			downKeyTextField.setName(EMPTY+DOWN);
			downKeyTextField.addKeyListener(this);
		}
		return downKeyTextField;
	}
	/**
	 * This method initializes dropKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getDropKeyTextField() {
		if(dropKeyTextField == null) {
			dropKeyTextField = new javax.swing.JTextField();
			dropKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			dropKeyTextField.setName(EMPTY+DROP);
			dropKeyTextField.addKeyListener(this);
		}
		return dropKeyTextField;
	}
	/**
	 * This method initializes rotateKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getRotateKeyTextField() {
		if(rotateKeyTextField == null) {
			rotateKeyTextField = new javax.swing.JTextField();
			rotateKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			rotateKeyTextField.setName(EMPTY+ROTATE);
			rotateKeyTextField.addKeyListener(this);
		}
		return rotateKeyTextField;
	}
	/**
	 * This method initializes pauseKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getPauseKeyTextField() {
		if(pauseKeyTextField == null) {
			pauseKeyTextField = new javax.swing.JTextField();
			pauseKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			pauseKeyTextField.setName(EMPTY+PAUSE);
			pauseKeyTextField.addKeyListener(this);
		}
		return pauseKeyTextField;
	}
	/**
	 * This method initializes previewKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getPreviewKeyTextField() {
		if(previewKeyTextField == null) {
			previewKeyTextField = new javax.swing.JTextField();
			previewKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			previewKeyTextField.setName(EMPTY+PREVIEW);
			previewKeyTextField.addKeyListener(this);
		}
		return previewKeyTextField;
	}
	/**
	 * This method initializes levelUpKeyTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getLevelUpKeyTextField() {
		if(levelUpKeyTextField == null) {
			levelUpKeyTextField = new javax.swing.JTextField();
			levelUpKeyTextField.setPreferredSize(new java.awt.Dimension(100,20));
			levelUpKeyTextField.setName(EMPTY+LEVELUP);
			levelUpKeyTextField.addKeyListener(this);
		}
		return levelUpKeyTextField;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="0,0"
