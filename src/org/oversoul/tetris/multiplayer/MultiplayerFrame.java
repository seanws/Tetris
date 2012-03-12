/**
 * MultiplayerFrame.java
 * 
 * Created on 6-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.ui.TetrisRootPane;
import org.oversoul.tetris.ui.WindowClosingDisposer;
import org.oversoul.tetris.util.Util;
import org.oversoul.util.Version;

/**
 * The frame that allows two players to communicate and start a 
 * tetris game.
 * 
 * @author Chris Callendar (9902588)
 * @date   6-Mar-04, 9:52:52 AM
 */
public class MultiplayerFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1;
	private static final int HOST = 1;
	private static final String EMPTY = "";
	private static final String READY = "  Ready  ";
	private static final String NOT_READY = "Not Ready";

	private IMultiplayerController mpController = null;
	private IUIController uiController = null;
	private TetrisCommunication communication = null;
	private TetrisRootPane rootPane = null;

    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JPanel jPanel2 = null;
    private JPanel jPanel3 = null;
    private JCheckBox jCheckBox = null;
    private JCheckBox jCheckBox1 = null;
    private JCheckBox jCheckBox2 = null;
    private JCheckBox jCheckBox3 = null;
    private JButton jButton = null;
    private JButton jButton1 = null;
    private JPanel jPanel6 = null;
    private JPanel jPanel7 = null;
    private JScrollPane jScrollPane = null;
    private JList jList = null;
    private JPanel jPanel9 = null;
    private JTextField jTextField2 = null;
    private JTable jTable = null;

	/**
	 * Constructor for MultiplayerFrame.
	 * @throws HeadlessException
	 */
	public MultiplayerFrame(TetrisRootPane rootPane, 
							IMultiplayerController mpController, 
							IUIController uiController,
							TetrisCommunication communication) throws HeadlessException {
		super();
		this.rootPane = rootPane;
		this.mpController = mpController;
		this.uiController = uiController;
		this.communication = communication;
		this.communication.setMultiplayerFrame(this);
		this.addWindowListener(new WindowClosingDisposer(this));
		initialize();
		
		getPlayer().setReady(false);
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		int num = getPlayer().getPlayerNumber();
		if (communication != null) {
			communication.sendCancel(num);
			communication.shutdown();
			communication = null;
		}
		mpController.setMultiplayers(getPlayer(), null);
		// save preferences
		getPlayer().setFixedLevel(getFixedStartingLevel());
		getPlayer().setFixedLines(getFixedStartingLines());
		getPlayer().setPenalties(getPenalties());				
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		model.updatePlayer(getPlayer());
		super.dispose();
	}

	//////////////////////////////////////////////////////////////////////
	// ACTION Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Cancel")) {
			dispose();	
		} else if (action.equals("Ready")) {
			handleReady();
		} else if (action.equals("Start")) {
			handleStart();
		} else if (action.equals("FixedStartingLevels") ||
				   action.equals("FixedStartingLines") ||
				   action.equals("FixedGameSpeed") || 
				   action.equals("Penalties")) {
			fixStartingOptions();
		} else if (action.equals("LevelChange")) {
			levelChange();	
		} else if (action.equals("LinesChange")) {
			linesChange();	
		} else if (action.equals("SpeedChange")) {
			speedChange();
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER) {
			int num = getPlayer().getPlayerNumber();
			Object src = e.getSource();
			if (src instanceof JTextField) {
				JTextField tf = (JTextField) src;
				String s = tf.getText();
				if (s.length() > 0) {
					if (tf.equals(getJTextField2())) {
						String msg = s;
						String name = getPlayer().getName();
						if (name.length() > 0) {
							msg = name + ": " + s;
						}
						addMessage(num, msg);
						communication.sendText(num, msg);
						tf.setText(EMPTY);
					}
				}
			}
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}
	
	//////////////////////////////////////////////////////////////////////
	// CUSTOM Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Sends a connect message using the communication object.
	 * This should be called after this Frame is initialized.
	 */
	public void sendConnectMessage() {
		communication.sendConnect(getPlayer());	
	}
	
	/**
	 * Adds a new player to the table.
	 * @param p	the player to add.
	 * @return boolean if added.
	 */
	private boolean addPlayer(TetrisPlayer p) {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		System.out.println("Adding opponent '" + p.getName() + "'");
		return model.addPlayer(p);
	}

	/**
	 * Drops a player from the game.
	 * @param playerNum	player number.
	 * @param reason	reason for leaving.
	 */
	public void dropPlayer(int playerNum, String reason) {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		model.removePlayer(getOpponent());
		String s = getOpponent().getName() + " has left the game";
		if (reason != null) {
			s += ": " + reason;	
		}
		addMessage(playerNum, s);
		getStartButton().setEnabled(false);
		getPlayer().setReady(true);
	}

	/**
	 * Adds a message to the list.
	 * @param type	The type of message: -1=error,0=default,1=host,2=client.
	 * @param msg	The message to add the to JList.
	 */
	public void addMessage(int type, String msg) {
		JList list = getJList();
		DefaultListModel model = (DefaultListModel) list.getModel();
		MessageListCellRenderer renderer = (MessageListCellRenderer) list.getCellRenderer();
		renderer.setColor(model.getSize(), type);
		if (model.getSize() >= 20) {
			model.remove(0);
			renderer.removeMessage(0);
		}
		model.addElement(msg.trim());
		list.repaint();
		int last = model.getSize() - 1;
		list.scrollRectToVisible(list.getCellBounds(last, last));
		list.ensureIndexIsVisible(last);
	}
	
	/**
	 * Changes the player's starting level with the given number.
	 * @param playerNum	the player's number whose starting level has changed.
	 * @param level	The new starting level of the player.
	 */
	public void changeLevel(int playerNum, int level) {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		getOpponent().setStartingLevel(level);
		model.updateTable(getOpponent());
	}	

	/**
	 * Changes the player's starting lines with the given number.
	 * @param playerNum	the player's number whose starting level has changed.
	 * @param lines	The new starting lines of the player.
	 */
	public void changeLines(int playerNum, int lines) {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		getOpponent().setStartingLines(lines);
		model.updateTable(getOpponent());
	}	

	/**
	 * Changes the game speed.
	 * @param playerNum	the player's number whose game speed has changed.
	 * @param speed The new game speed of the player.
	 */
	public void changeSpeed(int playerNum, int speed) {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		getOpponent().setGameSpeed(speed);
		model.updateTable(getOpponent());
	}	

	/**
	 * Changes the settings.
	 * @param level	starting level.
	 * @param fixedLevel	fixed starting level.
	 * @param lines	starting lines.
	 * @param fixedLines	fixed starting lines.
	 * @param gameSpeed the game speed
	 * @param fixedSpeed if the game speed is fixed.
	 * @param penalties		penalties on.
	 */
	public void changeSettings(int level, boolean fixedLevel,
							   int lines, boolean fixedLines, 
							   int gameSpeed, boolean fixedSpeed,
							   boolean penalties) {
		getOpponent().setSettings(level, fixedLevel, lines, fixedLines, gameSpeed, fixedSpeed, penalties);
		getPlayer().setBooleans(fixedLevel, fixedLines, fixedSpeed, penalties);
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		model.updateTable(getOpponent());
		getFixedLevelCheckBox().setSelected(fixedLevel);
		getFixedLinesCheckBox().setSelected(fixedLines);
		getFixedSpeedCheckBox().setSelected(fixedSpeed);
		getPenaltiesCheckBox().setSelected(penalties);		
	}

	/**
	 * Sets a player to be ready or unready.  Allows the game to start.
	 */
	public void playerReady() {
		if (!getPlayer().isReady()) {
			getStartButton().setEnabled(true);
			getPlayer().setReady(true);
		}
	}

	/**
	 * Sets a player to be not ready. Doesn't allow the game to start.
	 */
	public void playerNotReady() {
		if (getPlayer().isReady()) {
			getStartButton().setEnabled(false);
			getPlayer().setReady(false);
		}
	}
	
	/**
	 * Starts the game by giving focus to the GameFrame and hiding itself.
	 */
	public void startGame() {
		communication.sendGameStart(getPlayer().getPlayerNumber());
		getPlayer().setReady(false);
		getStartButton().setText(READY);			
		mpController.setMultiplayers(getPlayer(), getOpponent());
		mpController.handleMultiplayerStart();
	}

	/**
	 * Handles a connect message.
	 */
	public synchronized void handleConnect(int playerNum, 
								int level, boolean fixedLevel,  
								int lines,  boolean fixedLines,
								int gameSpeed, boolean fixedSpeed, 
							  	boolean penalties, 
							  	int wins, int losses, 
							  	int tetrisTotal,
							  	String versionString, String name) {

		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		if (!getVersion().equals(versionString)) {
			String errMsg = "Incorrect Tetris Version (Server="+getVersion().toString()+", Client="+versionString+".";
			communication.sendRefuse(errMsg);
			addMessage(0, errMsg);
		} else if (!model.isGameFull()) {
			getOpponent().setName(name);
			getOpponent().setSettings(level, fixedLevel, lines, fixedLines, gameSpeed, fixedSpeed, penalties);
			getOpponent().setWins(wins);
			getOpponent().setLosses(losses);
			getOpponent().setTetrisTotal(tetrisTotal);
		
			addPlayer(getOpponent());
			changeLevel(playerNum, level);
			changeLines(playerNum, lines);
			changeSpeed(playerNum, gameSpeed);
			mpController.updatePlayerStats();

			getPlayer().setReady(false);
			// added successfully
			communication.sendAccept(getPlayer());
						
			// must wait for ready
			getStartButton().setEnabled(false);
			addMessage(playerNum, name + " has joined the game.");
			model.updateTable(getPlayer());
		} else {
			// error
			communication.sendRefuse("Max players reached.");
			addMessage(0, "Max players reached, client couldn't join.");
		}	
	}

	/**
	 * Handles an accept message.
	 */
	public void handleAccept(int playerNum, 
							 int level, boolean fixedLevel,  
							 int lines, boolean fixedLines, 
							 int gameSpeed, boolean fixedSpeed,
							 boolean penalties, 
							 int wins, int losses, 
							 int tetrisTotal,
							 String versionString, String name) {
		getPlayer().setAccepted(true);
		getOpponent().setName(name);
		getOpponent().setSettings(level, fixedLevel, lines, fixedLines, gameSpeed, fixedSpeed, penalties);
		getOpponent().setWins(wins);
		getOpponent().setLosses(losses);
		getOpponent().setTetrisTotal(tetrisTotal);
		changeSettings(level, fixedLevel, lines, fixedLines, gameSpeed, fixedSpeed, penalties);
		boolean added = addPlayer(getOpponent());
		if (added) {
			addMessage(playerNum, name + " has joined the game.");
			mpController.updatePlayerStats();
		} else {
			System.err.println("Error accepting player " + name + ", game is full.");	
		}
	}

	/**
	 * Handles a refusal message.
	 */
	public void handleRefuse(int type, String reason) {
		if (!getPlayer().isAccepted()) {
			addMessage(type, reason);
			communication.shutdown();
			getStartButton().setEnabled(false);
		}
	}

	/**
	 * Sends a ready message.
	 */
	private void handleReady() {
		if (!getPlayer().isReady()) {
			communication.sendReady(getPlayer().getPlayerNumber());
			getPlayer().setReady(true);
			getStartButton().setText(NOT_READY);
		} else {
			communication.sendNotReady(getPlayer().getPlayerNumber());
			getPlayer().setReady(false);
			getStartButton().setText(READY);
		}		
	}

	/**
	 * Sends a start message and brings the game frame into focus.
	 */
	private void handleStart() {
		if (getPlayer().isReady()) {
			getPlayer().setReady(false);
			getStartButton().setEnabled(false);
			mpController.setMultiplayers(getPlayer(), getOpponent());
			mpController.waitForOpponent();
			communication.sendStart(getPlayer().getPlayerNumber());
		}
	}
	
	/**
	 * Fixes the starting levels or lines so they are the same.
	 * Also sends a settings change message.
	 */
	private void fixStartingOptions() {
		boolean fixedLevel = getFixedStartingLevel();
		boolean fixedLines = getFixedStartingLines();
		boolean fixedSpeed = getFixedGameSpeed();
		boolean penalties = getPenalties();
		getPlayer().setBooleans(fixedLevel, fixedLines, fixedSpeed, penalties);
		
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		model.updatePlayer(getPlayer());
		if (getPlayer().isFixedLevel()) {
			model.setAllStartingLevel(getPlayer().getStartingLevel());	
		}
		if (getPlayer().isFixedLines()) {
			model.setAllStartingLines(getPlayer().getStartingLines());	
		}
		if (getPlayer().isFixedSpeed()) {
			model.setAllGameSpeed(getPlayer().getGameSpeed());
		}
		communication.sendSettingsChange(getPlayer());
	}

	/**
	 * Handles a level change event.
	 */
	private void levelChange() {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		int before = getPlayer().getStartingLevel();
		int level = model.getStartingLevel(getPlayer());
		if (level != before) {
			communication.sendSettingsChange(getPlayer());
			if (getPlayer().isFixedLevel()) {
				model.setAllStartingLevel(level);
			}
		}
	}

	/**
	 * Handles a line change event.
	 */
	private void linesChange() {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		int before = getPlayer().getStartingLines();
		int lines = model.getStartingLines(getPlayer());
		if (lines != before) {
			communication.sendSettingsChange(getPlayer());
			if (getPlayer().isFixedLines()) {
				model.setAllStartingLines(lines);
			}
		}	
	}

	/**
	 * Handles a game speed change event.
	 */
	private void speedChange() {
		PlayerTableModel model = (PlayerTableModel) getJTable().getModel();
		int before = getPlayer().getGameSpeed();
		int speed = model.getGameSpeed(getPlayer());
		if (speed != before) {
			communication.sendSettingsChange(getPlayer());
			if (getPlayer().isFixedSpeed()) {
				model.setAllGameSpeed(speed);
			}
		}	
	}

	/**
	 * Configures the GUI and the game.
	 */
	private void configure() {
		// configure the players table
		JTable table = getJTable();
		PlayerTableModel model = new PlayerTableModel(getPlayer());
		table.setModel(model);

		JTableHeader header = table.getTableHeader();
		header.setForeground(java.awt.Color.BLACK);
		header.setReorderingAllowed(false);
		header.setPreferredSize(new Dimension(370, 18));

		int height = model.getRowCount() * table.getRowHeight();
		table.setPreferredSize(new Dimension(370, height));
		
		height += 25;
		getJPanel7().setPreferredSize(new Dimension(376, height));
		
		TableColumnModel columns = table.getColumnModel();
		TableColumn col = columns.getColumn(PlayerTableModel.NUM);
		col.setPreferredWidth(25);
		col.setResizable(false);
		
		JTextField nameTF = new JTextField();
		TableColumn nameCol = columns.getColumn(PlayerTableModel.NAME);
		nameCol.setCellEditor(new DefaultCellEditor(nameTF));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Tetris player");
        nameCol.setCellRenderer(renderer);
		nameCol.setPreferredWidth(210);

		JComboBox levels = new JComboBox();
		for (int j = 0; j <= 10; j++) {
			levels.addItem(EMPTY+j);	
		}
		levels.addActionListener(this);
		levels.setActionCommand("LevelChange");
		TableColumn levelCol = columns.getColumn(PlayerTableModel.LEVEL);
		levelCol.setCellEditor(new DefaultCellEditor(levels));
        renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Choose starting level");
        levelCol.setCellRenderer(renderer);
		levelCol.setPreferredWidth(40);

		JComboBox lines = new JComboBox();
		for (int j = 0; j <= 15; j++) {
			lines.addItem(EMPTY+j);	
		}
		lines.addActionListener(this);
		lines.setActionCommand("LinesChange");
		TableColumn linesCol = columns.getColumn(PlayerTableModel.LINES);
		linesCol.setCellEditor(new DefaultCellEditor(lines));
        renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Choose starting lines");
        linesCol.setCellRenderer(renderer);
		linesCol.setPreferredWidth(40);

		JComboBox speed = new JComboBox();
		speed.addItem(Util.getGameSpeedString(TetrisConstants.SPEED_SLOW));	
		speed.addItem(Util.getGameSpeedString(TetrisConstants.SPEED_NORM));	
		speed.addItem(Util.getGameSpeedString(TetrisConstants.SPEED_FAST));	
		speed.addActionListener(this);
		speed.setActionCommand("SpeedChange");
		TableColumn speedCol = columns.getColumn(PlayerTableModel.SPEED);
		speedCol.setCellEditor(new DefaultCellEditor(speed));
		renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Choose game speed");
		speedCol.setCellRenderer(renderer);
		speedCol.setPreferredWidth(65);

		if (getPlayer().getPlayerNumber() == HOST) {
			getPenaltiesCheckBox().setEnabled(true);
			getFixedLevelCheckBox().setEnabled(true);			
			getFixedLinesCheckBox().setEnabled(true);
			getFixedSpeedCheckBox().setEnabled(true);	
			getStartButton().setEnabled(false);
		} else {
			getPenaltiesCheckBox().setEnabled(false);
			getFixedLevelCheckBox().setEnabled(false);			
			getFixedLinesCheckBox().setEnabled(false);
			getFixedSpeedCheckBox().setEnabled(false);			
			getStartButton().setText(READY);
			getStartButton().setActionCommand("Ready");
			getStartButton().setMnemonic(java.awt.event.KeyEvent.VK_R);
		}			

		int row = getPlayer().getPlayerNumber() - 1;
		table.setRowSelectionInterval(row, row);
		
		// configure the messages list
		JList list = getJList();
		list.setCellRenderer(new MessageListCellRenderer());
		
		levelChange();
		linesChange();
		speedChange();
		model.updateTable(getPlayer());
	}
	
	//////////////////////////////////////////////////////////////////////
	// GUI Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getRootContentPane());
		this.setLocation(rootPane.getX()+90, rootPane.getY()+60);	
		this.setSize(new Dimension(408,434));
		configure();
		this.setIconImage(Util.getImageResource("/images/tetris16_2.gif"));
		this.setTitle("Multiplayer Game");
		this.setResizable(false);
		this.pack();
		getJTextField2().requestFocusInWindow();
		this.setVisible(true);		
	}
	/**
	 * This method initializes jContentPane
	 * @return JPanel
	 */
	private JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
			jContentPane.add(getJPanel(), null);
			jContentPane.setPreferredSize(new Dimension(400, 410));
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * @return JPanel
	 */
	private JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel6(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel3(), null);
			jPanel.setPreferredSize(new Dimension(390, 400));
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1
	 * @return JPanel
	 */
	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));

			JPanel jp = new JPanel(new GridLayout(2, 2, 0, 0));
			jp.add(getFixedLevelCheckBox(), null);
			jp.add(getFixedLinesCheckBox(), null);
			jp.add(getFixedSpeedCheckBox(), null);
			jp.add(getPenaltiesCheckBox(), null);
			jp.setPreferredSize(new Dimension(388, 40));
			
			jPanel1.add(jp, null);
			jPanel1.setPreferredSize(new Dimension(390,67));
			jPanel1.setBorder(getPlayer().getPanel().getPanelBorder("Game Settings"));
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2
	 * @return JPanel
	 */
	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
			jPanel2.add(getJScrollPane(), null);
			jPanel2.add(getJPanel9(), null);
			jPanel2.setPreferredSize(new Dimension(390,200));
			jPanel2.setBorder(getPlayer().getPanel().getPanelBorder("Messages"));
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3
	 * @return JPanel
	 */
	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 1);
			jPanel3.setLayout(flow);
			jPanel3.add(getStartButton(), null);
			jPanel3.add(getCloseButton(), null);
			jPanel3.setPreferredSize(new Dimension(390,35));
		}
		return jPanel3;
	}
	/**
	 * This method initializes jCheckBox1
	 * @return JCheckBox
	 */
	private JCheckBox getFixedLevelCheckBox() {
		if(jCheckBox1 == null) {
			jCheckBox1 = new JCheckBox("Fixed starting level", getPlayer().isFixedLevel());
			jCheckBox1.addActionListener(this);
			jCheckBox1.setActionCommand("FixedStartingLevels");
		}
		return jCheckBox1;
	}
	/**
	 * This method initializes jCheckBox2
	 * @return JCheckBox
	 */
	private JCheckBox getFixedLinesCheckBox() {
		if(jCheckBox2 == null) {
			jCheckBox2 = new JCheckBox("Fixed starting lines", getPlayer().isFixedLines());
			jCheckBox2.addActionListener(this);
			jCheckBox2.setActionCommand("FixedStartingLines");
		}
		return jCheckBox2;
	}
	/**
	 * This method initializes jCheckBox3
	 * @return JCheckBox
	 */
	private JCheckBox getFixedSpeedCheckBox() {
		if(jCheckBox3 == null) {
			jCheckBox3 = new JCheckBox("Fixed game speed", getPlayer().isFixedSpeed());
			jCheckBox3.addActionListener(this);
			jCheckBox3.setActionCommand("FixedGameSpeed");
		}
		return jCheckBox3;
	}
	/**
	 * This method initializes jCheckBox
	 * @return JCheckBox
	 */
	private JCheckBox getPenaltiesCheckBox() {
		if(jCheckBox == null) {
			jCheckBox = new JCheckBox("Penalties", getPlayer().isPenalties());
			jCheckBox.addActionListener(this);
			jCheckBox.setActionCommand("Penalties");
		}
		return jCheckBox;
	}
	/**
	 * This method initializes jButton
	 * @return JButton
	 */
	private JButton getCloseButton() {
		if(jButton == null) {
			jButton = new JButton("Close Connection");
			jButton.addActionListener(this);
			jButton.setActionCommand("Cancel");
			jButton.setMnemonic(java.awt.event.KeyEvent.VK_C);
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1
	 * @return JButton
	 */
	private JButton getStartButton() {
		if(jButton1 == null) {
			jButton1 = new JButton("Start Game");
			jButton1.addActionListener(this);
			jButton1.setActionCommand("Start");
			jButton1.setMnemonic(java.awt.event.KeyEvent.VK_S);
		}
		return jButton1;
	}
	/**
	 * This method initializes jPanel6
	 * @return JPanel
	 */
	private JPanel getJPanel6() {
		if(jPanel6 == null) {
			jPanel6 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 2, 2);
			jPanel6.setLayout(flow);
			jPanel6.add(getJPanel7(), null);
			jPanel6.setPreferredSize(new Dimension(390,95));
			jPanel6.setBorder(getPlayer().getPanel().getPanelBorder("Players"));
		}
		return jPanel6;
	}
	/**
	 * This method initializes jPanel7
	 * @return JPanel
	 */
	private JPanel getJPanel7() {
		if(jPanel7 == null) {
			jPanel7 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			jPanel7.add(getJTable(), null);
			JTable table = getJTable();
			jPanel7.add(table.getTableHeader(), null);
			jPanel7.add(table, null);
			jPanel7.setPreferredSize(new Dimension(376,60));
			jPanel7.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		return jPanel7;
	}
	/**
	 * This method initializes jScrollPane
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJList());
			jScrollPane.setPreferredSize(new Dimension(380,150));
			jScrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jList
	 * @return JList
	 */
	private JList getJList() {
		if(jList == null) {
			jList = new JList(new DefaultListModel());
			jList.setBackground(java.awt.Color.BLACK);
			jList.setForeground(java.awt.Color.WHITE);
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jList;
	}
	/**
	 * This method initializes jPanel9
	 * @return JPanel
	 */
	private JPanel getJPanel9() {
		if(jPanel9 == null) {
			jPanel9 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 0, 0);
			jPanel9.setLayout(flow);
			jPanel9.add(getJTextField2(), null);
			jPanel9.setPreferredSize(new Dimension(380,20));
		}
		return jPanel9;
	}
	/**
	 * This method initializes jTextField2
	 * @return JTextField
	 */
	private JTextField getJTextField2() {
		if(jTextField2 == null) {
			jTextField2 = new JTextField();
			jTextField2.setPreferredSize(new Dimension(380,20));
			jTextField2.addKeyListener(this);
			
		}
		return jTextField2;
	}
	/**
	 * This method initializes jTable
	 * @return JTable
	 */
	private JTable getJTable() {
		if(jTable == null) {
			jTable = new JTable();
			jTable.setPreferredSize(new Dimension(375,50));
			jTable.setCellSelectionEnabled(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setIntercellSpacing(new Dimension(2,2));
			jTable.setRequestFocusEnabled(true);
			jTable.setSelectionBackground(Color.black);
			jTable.setSelectionForeground(Color.cyan);
			jTable.setShowGrid(true);
			jTable.setShowVerticalLines(true);
			jTable.setShowHorizontalLines(true);
			jTable.setFont(new Font("Default", Font.BOLD, 13));
			jTable.setGridColor(Color.BLUE);
			jTable.setRowHeight(20);
			jTable.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
			jTable.addKeyListener(this);
		}
		return jTable;
	}
	
	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Returns if the starting levels are fixed.
	 * @return boolean fixed starting levels.
	 */
	private boolean getFixedStartingLevel() {
		return getFixedLevelCheckBox().isSelected();
	}
	/**
	 * Returns if the starting lines are fixed.
	 * @return boolean fixed starting lines.
	 */
	private boolean getFixedStartingLines() {
		return getFixedLinesCheckBox().isSelected();
	}
	/**
	 * Returns if the game speed is fixed.
	 * @return boolean fixed game speed.
	 */
	private boolean getFixedGameSpeed() {
		return getFixedSpeedCheckBox().isSelected();
	}
	/**
	 * Returns if penalties are on.
	 * @return boolean if penalties are on.
	 */
	private boolean getPenalties() {
		return getPenaltiesCheckBox().isSelected();
	}

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////
	/**
	 * Returns the communication.
	 * @return TetrisCommunication
	 */
	public TetrisCommunication getCommunication() {
		return communication;
	}

	/**
	 * Sets the communication.
	 * @param communication The communication to set
	 */
	public void setCommunication(TetrisCommunication communication) {
		this.communication = communication;
	}
	
	public TetrisPlayer getPlayer() {
		return uiController.getPlayer();
	}
	
	public TetrisPlayer getOpponent() {
		return uiController.getOpponent();
	}
	
	public Version getVersion() {
		return uiController.getVersion();
	}

}
