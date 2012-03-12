/**
 * ImportDialog.java
 * 
 * Created on 8-Nov-04
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.oversoul.tetris.TetrisColorScheme;
import org.oversoul.tetris.TetrisColors;
import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisPanel;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.util.Configuration;
import org.oversoul.tetris.util.Util;

/**
 * Import dialog for importing tetris players and color schemes.
 * 
 * @author 		nyef
 * @date		8-Nov-04, 9:21:16 PM
 * @version 	1.0
 */
public class ImportDialog extends JDialog implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1;

	private final ImageIcon RIGHT1;
	private final ImageIcon RIGHT1_OVER;
	private final ImageIcon RIGHT2;
	private final ImageIcon RIGHT2_OVER;
	private final ImageIcon LEFT1;
	private final ImageIcon LEFT1_OVER;
	private final ImageIcon LEFT2;
	private final ImageIcon LEFT2_OVER;
	
	private final String EMPTY = "";
	private final String PLAYERS_LEFT = "PlayersLeftList";
	private final String PLAYERS_RIGHT = "PlayersRightList";
	private final String SCHEMES_LEFT = "SchemesLeftList";
	private final String SCHEMES_RIGHT = "SchemesLeftList";

	private Configuration config = null;
	private TetrisRootPane rootPane = null;
	private TetrisPlayer player = null;
	private Configuration loadedConfig = null;
	
	private Hashtable players = null;
	private Hashtable colorSchemes = null;
	private TetrisPanel defaultPanel = null;
	private boolean ignoreListEvents = false;

	// TODO way too many field vars dude!
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
	private JPanel jPanel11 = null;
	private JPanel jPanel12 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	private JList jList = null;
	private JList jList1 = null;
	private JList jList2 = null;
	private JList jList3 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
	private JButton jButton5 = null;
	private JButton jButton6 = null;
	private JButton jButton7 = null;
	private JButton jButton8 = null;
	private JButton jButton9 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JLabel jLabel8 = null;
	private JLabel jLabel9 = null;
	private JLabel jLabel21 = null;
	private JLabel jLabel22 = null;
	private JLabel jLabel23 = null;
	private JLabel jLabel24 = null;	
	private JLabel jLabel25 = null;
	private JLabel jLabel26 = null;	
	private JCheckBox jCheckBox = null;

	/**
	 * Constructor for ImportDialog.java
	 * @throws HeadlessException
	 */
	public ImportDialog(Frame parent, 
						TetrisRootPane rootPane, 
						TetrisPlayer player, 
						Configuration config, 
						File selectedFile) throws HeadlessException {
		super(parent);
		this.rootPane = rootPane;
		this.player = player;
		this.config = config;
		this.defaultPanel = new TetrisPanel();

		RIGHT1 = Util.getIconResource("/images/right1.gif");
		RIGHT1_OVER = Util.getIconResource("/images/right1_over.gif");
		RIGHT2 = Util.getIconResource("/images/right2.gif");
		RIGHT2_OVER = Util.getIconResource("/images/right2_over.gif");
		LEFT1 = Util.getIconResource("/images/left1.gif");
		LEFT1_OVER = Util.getIconResource("/images/left1_over.gif");
		LEFT2 = Util.getIconResource("/images/left2.gif");
		LEFT2_OVER = Util.getIconResource("/images/left2_over.gif");

		boolean ok = readPropertiesFile(selectedFile);
		if (ok) {
			initialize();
		} else {
			dispose();
		}
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (!ignoreListEvents) {
			JList list = (JList)e.getSource();
			String name = list.getName();
			int[] selIndices = list.getSelectedIndices();
			int count = 0;
			String selValue = null;
			int selIndex = 0;
			if (selIndices != null) {
				count = selIndices.length;
				selValue = (String)list.getSelectedValue();
				selIndex = list.getSelectedIndex();
			}
			
			if (PLAYERS_LEFT.equals(name) || PLAYERS_RIGHT.equals(name)) {
				if (count == 1) {
					setPlayerStats(selValue);
					ignoreListEvents = true;
					getPlayersLeftList().clearSelection();
					getPlayersRightList().clearSelection();
					list.setSelectedIndex(selIndex);
					ignoreListEvents = false;
				} else {
					clearPlayerStats();				
				}
			} else if (SCHEMES_LEFT.equals(name) || SCHEMES_RIGHT.equals(name)) {
				if (count == 1) {
					setColorSchemes(selValue);
					ignoreListEvents = true;
					getSchemesLeftList().clearSelection();
					getSchemesRightList().clearSelection();
					list.setSelectedIndex(selIndex);
					ignoreListEvents = false;
				} else {
					clearColorSchemes();	
				}
			}
		}
	}
	
	/**
	 * Sets the player stats labels.
	 * @param playerName
	 */
	private void setPlayerStats(String playerName) {
		if (players.containsKey(playerName)) {		
			TetrisPlayer player = (TetrisPlayer)players.get(playerName);
			jLabel21.setText(EMPTY+player.getWins());
			jLabel22.setText(EMPTY+player.getLosses());
			jLabel23.setText(player.getWinningPercentage() + "%");
			jLabel24.setText(EMPTY+player.getTetrisTotal());
			jLabel25.setText(EMPTY+player.getStartingLevel());
			jLabel26.setText(Util.getGameSpeedString(player.getGameSpeed()));
		}
	}	
	/**
	 * Clears the player stats labels.
	 */
	private void clearPlayerStats() {
		jLabel21.setText(EMPTY);
		jLabel22.setText(EMPTY);
		jLabel23.setText(EMPTY);
		jLabel24.setText(EMPTY);
		jLabel25.setText(EMPTY);
		jLabel26.setText(EMPTY);
	}
	
	/**
	 * Sets the block and panel colors for the 12 labels.
	 * @param schemeName
	 */
	private void setColorSchemes(String schemeName) {
		if (colorSchemes.containsKey(schemeName)) {
			TetrisColorScheme cs = (TetrisColorScheme)colorSchemes.get(schemeName);
			TetrisColors colors = cs.getBlockColors();
			TetrisPanel panel = cs.getPanelColors();
			jLabel2.setBackground(colors.z);
			jLabel3.setBackground(colors.s);
			jLabel4.setBackground(colors.left);
			jLabel5.setBackground(colors.right);
			jLabel6.setBackground(colors.line);
			jLabel7.setBackground(colors.tri);
			jLabel8.setBackground(colors.square);
			jLabel9.setBackground(colors.flash);
			jPanel12.setBorder(panel.getPanelBorder("Preview"));
			jPanel12.repaint();					
		}
	}	
	/**
	 * Clears the color scheme labels.
	 */
	private void clearColorSchemes() {
		Color bgcolor = getRootContentPane().getBackground();
		jLabel2.setBackground(bgcolor);
		jLabel3.setBackground(bgcolor);
		jLabel4.setBackground(bgcolor);
		jLabel5.setBackground(bgcolor);
		jLabel6.setBackground(bgcolor);
		jLabel7.setBackground(bgcolor);
		jLabel8.setBackground(bgcolor);
		jLabel9.setBackground(bgcolor);
		jPanel12.setBorder(defaultPanel.getPanelBorder("Preview"));
		jPanel12.repaint();		
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if ("OK".equals(action)) {
			doImport();
			dispose();
		} else if ("Cancel".equals(action)) {
			dispose();
		} else if ("AddPlayers".equals(action)) {
			addPlayers();
		} else if ("AddAllPlayers".equals(action)) {
			addAllPlayers();
		} else if ("RemovePlayers".equals(action)) {
			removePlayers();
		} else if ("RemoveAllPlayers".equals(action)) {
			removeAllPlayers();
		} else if ("AddSchemes".equals(action)) {
			addSchemes();
		} else if ("AddAllSchemes".equals(action)) {
			addAllSchemes();
		} else if ("RemoveSchemes".equals(action)) {
			removeSchemes();
		} else if ("RemoveAllSchemes".equals(action)) {
			removeAllSchemes();
		}
	}


	/**
	 * Imports the selected players and color schemes.
	 */
	private void doImport() {
		boolean overwrite = getJCheckBox().isSelected();
		Hashtable currentPlayers = config.loadPlayers();
		DefaultListModel model = (DefaultListModel)getPlayersRightList().getModel();
		if (model.size() > 0) {
			for (int i = 0; i < model.size(); i++) {
				String key = (String)model.getElementAt(i);
				if (players.containsKey(key)) {
					TetrisPlayer p = (TetrisPlayer)players.get(key);
					if (overwrite || !currentPlayers.containsKey(key)) {
						player.setNeedsReloading(currentPlayers.containsKey(key));
						p.saveAll(config);
					} else {
						int choice = JOptionPane.showConfirmDialog(this, "Overwrite properties for player '" + key + "'?", "Overwrite?", JOptionPane.YES_NO_OPTION);
						if (choice == JOptionPane.YES_OPTION) {
							player.setNeedsReloading(true);
							p.saveAll(config);
						}
					}
				} else {
					System.out.println("Couldn't import player '" + key + "'.");	
				}
			}
		}
		
		Hashtable currentSchemes = config.loadColorSchemes();
		model = (DefaultListModel)getSchemesRightList().getModel();
		if (model.size() > 0) {
			for (int i = 0; i < model.size(); i++) {
				String key = (String)model.getElementAt(i);
				if (colorSchemes.containsKey(key)) {
					TetrisColorScheme cs = (TetrisColorScheme)colorSchemes.get(key);
					if (overwrite || !currentSchemes.containsKey(key)) {
						cs.saveColorScheme(config);
					} else {
						int choice = JOptionPane.showConfirmDialog(this, "Overwrite color scheme '" + key + "'?", "Overwrite?", JOptionPane.YES_NO_OPTION);
						if (choice == JOptionPane.YES_OPTION) {
							cs.saveColorScheme(config);
						}	
					}
				} else {
					System.out.println("Couldn't import color scheme '" + key + "'.");	
				}
			}
		}
	}

	/**
	 * Adds all the players from the left list to the right.
	 */
	private void addAllPlayers() {
		int[] indices = moveAllListItems((DefaultListModel)getPlayersLeftList().getModel(), 
						 (DefaultListModel)getPlayersRightList().getModel());
		getPlayersRightList().setSelectedIndices(indices);
	}

	/**
	 * Removes all the players from the right list to the left.
	 */
	private void removeAllPlayers() {
		int[] indices = moveAllListItems((DefaultListModel)getPlayersRightList().getModel(), 
						 (DefaultListModel)getPlayersLeftList().getModel());
		getPlayersLeftList().setSelectedIndices(indices);
	}

	/**
	 * Adds all the color schemes from the left list to the right.
	 */
	private void addAllSchemes() {
		int[] indices = moveAllListItems((DefaultListModel)getSchemesLeftList().getModel(), 
						 (DefaultListModel)getSchemesRightList().getModel());
		getSchemesRightList().setSelectedIndices(indices);
	}

	/**
	 * Removes all the color schemes from the right list to the left.
	 */
	private void removeAllSchemes() {
		int[] indices = moveAllListItems((DefaultListModel)getSchemesRightList().getModel(), 
						 (DefaultListModel)getSchemesLeftList().getModel());
		getSchemesLeftList().setSelectedIndices(indices);
	}

	/**
	 * Moves all the list items from one list to the other.
	 * @param fromModel the model where the items are being removed from.
	 * @param toModel	the model where the items are being moved to.
	 */
	private int[] moveAllListItems(DefaultListModel fromModel, DefaultListModel toModel) {
		int[] indices = new int[] { -1 };
		if (fromModel.size() > 0) {
			String[] names = new String[fromModel.size()];
			for (int i = 0; i < names.length; i++) {
				names[i] = (String)fromModel.remove(0);	
			}
			Arrays.sort(names);
			indices = new int[names.length];
			for (int i = 0; i < names.length; i++) {
				indices[i] = toModel.getSize();
				toModel.addElement(names[i]);	
			}
		}
		return indices;
	}

	/**
	 * Adds players to the right list and removes from the left.
	 */
	private void addPlayers() {
		int[] indices = moveListItems(getPlayersLeftList().getSelectedValues(),
					  (DefaultListModel)getPlayersLeftList().getModel(),
					  (DefaultListModel)getPlayersRightList().getModel());
		getPlayersRightList().setSelectedIndices(indices);
	}

	/**
	 * Removes players from the right list and adds to the left.
	 */
	private void removePlayers() {
		int[] indices = moveListItems(getPlayersRightList().getSelectedValues(),
					  (DefaultListModel)getPlayersRightList().getModel(),
					  (DefaultListModel)getPlayersLeftList().getModel());
		getPlayersLeftList().setSelectedIndices(indices);
	}

	/**
	 * Adds color schemes to the right list and removes from the left.
	 */
	private void addSchemes() {
		int[] indices = moveListItems(getSchemesLeftList().getSelectedValues(), 
					  (DefaultListModel)getSchemesLeftList().getModel(),
					  (DefaultListModel)getSchemesRightList().getModel());
		getSchemesRightList().setSelectedIndices(indices);
	}

	/**
	 * Removes color schemes from the right list and adds to the left.
	 */
	private void removeSchemes() {
		int[] indices = moveListItems(getSchemesRightList().getSelectedValues(), 
					  (DefaultListModel)getSchemesRightList().getModel(),
					  (DefaultListModel)getSchemesLeftList().getModel());
		getSchemesLeftList().setSelectedIndices(indices);
	}
	
	/**
	 * Moves list items from one list to another.
	 * @param vals		the values to move (Strings).
	 * @param fromModel the ListModel where the items are being moved from.
	 * @param toModel	the ListModel where the items are being moved to.
	 * @return int[] 	the indices of the moved items in the to list.
	 */
	private int[] moveListItems(Object[] vals, DefaultListModel fromModel, DefaultListModel toModel) {
		int[] indices = new int[] { -1 };
		if ((vals != null) && (vals.length > 0)) {
			indices = new int[vals.length];
			for (int i = 0; i < vals.length; i++) {
				boolean removed = fromModel.removeElement(vals[i]);
				if (removed) {
					indices[i] = toModel.getSize();
					toModel.addElement(vals[i]);
				} else {
					indices[i] = -1;	
				}
			}
		}
		return indices;
	}

	/**
	 * Reads in the properties file.
	 * @param file properties file
	 * @return boolean if properties file was successfully read.
	 */
	private boolean readPropertiesFile(File file) {
		try {
			if (file.exists()) {
				loadedConfig = new Configuration();
				Properties props = new Properties();
				props.load(new BufferedInputStream(new FileInputStream(file)));
				loadedConfig.load(props);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, "Error loading properties file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * Loads the left list with the player names and color schemes.
	 */
	private void loadLeftList() {
		players = loadedConfig.loadPlayers();
		DefaultListModel listModel = new DefaultListModel();
		if (players.size() > 0) {
			String[] names = new String[players.size()];
			Iterator iter = players.keySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				names[i++] = (String)iter.next();
			}
			Arrays.sort(names);
			for (i = 0; i < names.length; i++) {
				listModel.addElement(names[i]);	
			}
		}
		getPlayersLeftList().setModel(listModel);
		getPlayersRightList().setModel(new DefaultListModel());
		 
		colorSchemes = loadedConfig.loadColorSchemes();
		listModel = new DefaultListModel();
		if (colorSchemes.size() > 0) {
			String[] names = new String[colorSchemes.size()];
			Iterator iter = colorSchemes.keySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				names[i++] = (String)iter.next();
			}
			Arrays.sort(names);
			for (i = 0; i < names.length; i++) {
				listModel.addElement(names[i]);	
			}
		}
		getSchemesLeftList().setModel(listModel);
		getSchemesRightList().setModel(new DefaultListModel());
	}

	//////////////////////////////////////////////////////////////////////
	// AUTOGENERATED GUI Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Initialies this dialog.
	 */
	private void initialize() {
		this.setContentPane(getRootContentPane());
		this.setSize(478, 420);
		this.setModal(true);
		this.setTitle("Import Tetris Properties");
		this.setLocation(rootPane.getTopLeft().x+40, rootPane.getTopLeft().y+80);
		this.setResizable(false);
		loadLeftList();
		this.pack();
		getOKButton().requestFocusInWindow();
		this.setVisible(true);			
	}

	/**
	 * This method initialies jPanel
	 * @return JPanel
	 */
	private JPanel getRootContentPane() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			jPanel.setPreferredSize(new Dimension(470, 410));
			jPanel.add(getJPanel5());
			jPanel.add(getJPanel4());
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * @return JPanel
	 */
	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 0);
			jPanel1.setLayout(flow);
			jPanel1.add(getJScrollPane(), null);
			jPanel1.setPreferredSize(new Dimension(140,120));
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2
	 * @return JPanel
	 */
	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.add(getJLabel(), null);
			jPanel2.add(getAddPlayersButton(), null);
			jPanel2.add(getAddAllPlayersButton(), null);
			jPanel2.add(getRemovePlayersButton(), null);
			jPanel2.add(getRemoveAllPlayersButton(), null);
			jPanel2.setPreferredSize(new Dimension(30,120));
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
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 0);
			jPanel3.setLayout(flow);
			jPanel3.add(getJScrollPane1(), null);
			jPanel3.setPreferredSize(new Dimension(140,120));
		}
		return jPanel3;
	}
	/**
	 * This method initializes jPanel4
	 * @return JPanel
	 */
	private JPanel getJPanel4() {
		if(jPanel4 == null) {
			jPanel4 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 2, 0);
			jPanel4.setLayout(flow);
			jPanel4.add(getOKButton(), null);
			jPanel4.add(getCancelButton(), null);
			jPanel4.setPreferredSize(new Dimension(350,30));
		}
		return jPanel4;
	}
	/**
	 * This method initializes jPanel6
	 * @return JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 1));
			jPanel6.add(getJCheckBox());
			jPanel6.setPreferredSize(new Dimension(390, 20));
		}
		return jPanel6;	
	}
	/**
	 * Initializes jCheckBox
	 * @return JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox("Overwrite existing players and color schemes?", false);			
		}
		return jCheckBox;	
	}
	/**
	 * This method initializes jButton
	 * @return JButton
	 */
	private JButton getOKButton() {
		if(jButton == null) {
			jButton = new JButton("OK");
			jButton.setMnemonic(KeyEvent.VK_O);
			jButton.setActionCommand("OK");
			jButton.addActionListener(this);
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1
	 * @return JButton
	 */
	private JButton getCancelButton() {
		if(jButton1 == null) {
			jButton1 = new JButton("Cancel");
			jButton1.setMnemonic(KeyEvent.VK_C);
			jButton1.setActionCommand("Cancel");
			jButton1.addActionListener(this);
		}
		return jButton1;
	}
	/**
	 * This method initializes jScrollPane
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPlayersLeftList());
			jScrollPane.setPreferredSize(new Dimension(140,120));
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jScrollPane1
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getPlayersRightList());
			jScrollPane1.setPreferredSize(new Dimension(140,120));
		}
		return jScrollPane1;
	}
	/**
	 * This method initializes jButton2
	 * @return JButton
	 */
	private JButton getAddPlayersButton() {
		if(jButton2 == null) {
			jButton2 = new JButton(RIGHT1);
			jButton2.setActionCommand("AddPlayers");
			jButton2.addActionListener(this);
			jButton2.setToolTipText("Add selected players");
			jButton2.setPreferredSize(new Dimension(22,20));
			jButton2.setRolloverIcon(RIGHT1_OVER);
			jButton2.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton2;
	}
	/**
	 * This method initializes jButton6
	 * @return JButton
	 */
	private JButton getAddAllPlayersButton() {
		if(jButton6 == null) {
			jButton6 = new JButton(RIGHT2);
			jButton6.setActionCommand("AddAllPlayers");
			jButton6.addActionListener(this);
			jButton6.setToolTipText("Add all players");
			jButton6.setPreferredSize(new Dimension(22,20));
			jButton6.setRolloverIcon(RIGHT2_OVER);
			jButton6.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton6;
	}
	/**
	 * This method initializes jButton3
	 * @return JButton
	 */
	private JButton getRemovePlayersButton() {
		if(jButton3 == null) {
			jButton3 = new JButton(LEFT1);
			jButton3.setActionCommand("RemovePlayers");
			jButton3.addActionListener(this);
			jButton3.setToolTipText("Remove selected players");
			jButton3.setPreferredSize(new Dimension(22,20));
			jButton3.setRolloverIcon(LEFT1_OVER);
			jButton3.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton3;
	}
	/**
	 * This method initializes jButton7
	 * @return JButton
	 */
	private JButton getRemoveAllPlayersButton() {
		if(jButton7 == null) {
			jButton7 = new JButton(LEFT2);
			jButton7.setActionCommand("RemoveAllPlayers");
			jButton7.addActionListener(this);
			jButton7.setToolTipText("Remove all players");
			jButton7.setPreferredSize(new Dimension(22,20));
			jButton7.setRolloverIcon(LEFT2_OVER);
			jButton7.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton7;
	}
	/**
	 * This method initializes jList
	 * @return JList
	 */
	private JList getPlayersLeftList() {
		if(jList == null) {
			jList = new JList(new DefaultListModel());
			jList.setName(PLAYERS_LEFT);
			jList.addListSelectionListener(this);
		}
		return jList;
	}
	/**
	 * This method initializes jList1
	 * @return JList
	 */
	private JList getPlayersRightList() {
		if(jList1 == null) {
			jList1 = new JList(new DefaultListModel());
			jList1.setName(PLAYERS_RIGHT);
			jList1.addListSelectionListener(this);
		}
		return jList1;
	}
	/**
	 * This method initializes jPanel5
	 * @return JPanel
	 */
	private JPanel getJPanel5() {
		if(jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
			
			final Font font = new Font("Arial", Font.BOLD, 11);
			final Dimension dim = new Dimension(170, 18);
			final Dimension dim2 = new Dimension(450, 18);
			JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
			JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			labelPanel.setPreferredSize(dim2);
			JLabel lab = new JLabel(" Available Players:");
			lab.setPreferredSize(dim);
			lab.setFont(font);
			labelPanel.add(lab);
			lab = new JLabel(" Players To Add:");
			lab.setPreferredSize(dim);
			lab.setFont(font);
			labelPanel.add(lab);

			jp.add(labelPanel);
			jp.add(getJPanel1());
			jp.add(getJPanel2());
			jp.add(getJPanel3());
			jp.add(getJPanel11());
			jp.setPreferredSize(new Dimension(454, 140));
			jPanel5.add(jp);
			
			jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
			labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			labelPanel.setPreferredSize(dim2);
			lab = new JLabel(" Available Color Schemes:");
			lab.setPreferredSize(dim);
			lab.setFont(font);
			labelPanel.add(lab);
			lab = new JLabel(" Color Schemes To Add:");
			lab.setPreferredSize(dim);
			lab.setFont(font);
			labelPanel.add(lab);

			jp.add(labelPanel);
			jp.add(getJPanel7());
			jp.add(getJPanel8());
			jp.add(getJPanel9());
			jp.add(getJPanel10());
			jp.setPreferredSize(new Dimension(454, 170));
			jPanel5.add(jp);
			
			jPanel5.add(getJPanel6());
			jPanel5.setPreferredSize(new Dimension(460,370));
			jPanel5.setBorder(rootPane.getTetrisPanel().getPanelBorder("Choose properties to import:"));
		}
		return jPanel5;
	}
	/**
	 * This method initializes jLabel
	 * @return JLabel
	 */
	private JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new JLabel();
			jLabel.setPreferredSize(new Dimension(21,3));
		}
		return jLabel;
	}
	/**
	 * This method initializes jPanel7
	 * @return JPanel
	 */
	private JPanel getJPanel7() {
		if(jPanel7 == null) {
			jPanel7 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 0);
			jPanel7.setLayout(flow);
			jPanel7.add(getJScrollPane2(), null);
			jPanel7.setPreferredSize(new Dimension(140,150));
		}
		return jPanel7;
	}
	/**
	 * This method initializes jPanel8
	 * @return JPanel
	 */
	private JPanel getJPanel8() {
		if(jPanel8 == null) {
			jPanel8 = new JPanel();
			jPanel8.add(getJLabel1(), null);
			jPanel8.add(getAddSchemesButton(), null);
			jPanel8.add(getAddAllSchemesButton(), null);
			jPanel8.add(getRemoveSchemesButton(), null);
			jPanel8.add(getRemoveAllSchemesButton(), null);
			jPanel8.setPreferredSize(new Dimension(30,150));
		}
		return jPanel8;
	}
	/**
	 * This method initializes jPanel9
	 * @return JPanel
	 */
	private JPanel getJPanel9() {
		if(jPanel9 == null) {
			jPanel9 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 0);
			jPanel9.setLayout(flow);
			jPanel9.add(getJScrollPane3(), null);
			jPanel9.setPreferredSize(new Dimension(140,150));
		}
		return jPanel9;
	}
	/**
	 * This method initializes jScrollPane2
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if(jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getSchemesLeftList());
			jScrollPane2.setPreferredSize(new Dimension(140,150));
		}
		return jScrollPane2;
	}
	/**
	 * This method initializes jScrollPane3
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if(jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(getSchemesRightList());
			jScrollPane3.setPreferredSize(new Dimension(140,150));
		}
		return jScrollPane3;
	}
	/**
	 * This method initializes jList2
	 * @return JList
	 */
	private JList getSchemesLeftList() {
		if(jList2 == null) {
			jList2 = new JList(new DefaultListModel());
			jList2.setName(SCHEMES_LEFT);
			jList2.addListSelectionListener(this);
		}
		return jList2;
	}
	/**
	 * This method initializes jList3
	 * @return JList
	 */
	private JList getSchemesRightList() {
		if(jList3 == null) {
			jList3 = new JList(new DefaultListModel());
			jList3.setName(SCHEMES_RIGHT);
			jList3.addListSelectionListener(this);
		}
		return jList3;
	}
	/**
	 * This method initializes jLabel1
	 * @return JLabel
	 */
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setPreferredSize(new Dimension(21, 17));
		}
		return jLabel1;
	}
	/**
	 * This method initializes jButton4
	 * @return JButton
	 */
	private JButton getAddSchemesButton() {
		if(jButton4 == null) {
			jButton4 = new JButton(RIGHT1);
			jButton4.setActionCommand("AddSchemes");
			jButton4.addActionListener(this);
			jButton4.setToolTipText("Add selected color schemes");
			jButton4.setRolloverIcon(RIGHT1_OVER);
			jButton4.setPreferredSize(new Dimension(22, 20));
			jButton4.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton4;
	}
	/**
	 * This method initializes jButton8
	 * @return JButton
	 */
	private JButton getAddAllSchemesButton() {
		if(jButton8 == null) {
			jButton8 = new JButton(RIGHT2);
			jButton8.setActionCommand("AddAllSchemes");
			jButton8.addActionListener(this);
			jButton8.setToolTipText("Add all color schemes");
			jButton8.setRolloverIcon(RIGHT2_OVER);
			jButton8.setPreferredSize(new Dimension(22, 20));
			jButton8.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton8;
	}
	/**
	 * This method initializes jButton5
	 * @return JButton
	 */
	private JButton getRemoveSchemesButton() {
		if(jButton5 == null) {
			jButton5 = new JButton(LEFT1);
			jButton5.setActionCommand("RemoveSchemes");
			jButton5.addActionListener(this);
			jButton5.setToolTipText("Remove selected color schemes");
			jButton5.setRolloverIcon(LEFT1_OVER);
			jButton5.setPreferredSize(new Dimension(22, 20));
			jButton5.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton5;
	}
	/**
	 * This method initializes jButton9
	 * @return JButton
	 */
	private JButton getRemoveAllSchemesButton() {
		if(jButton9 == null) {
			jButton9 = new JButton(LEFT2);
			jButton9.setActionCommand("RemoveAllSchemes");
			jButton9.addActionListener(this);
			jButton9.setToolTipText("Remove all color schemes");
			jButton9.setRolloverIcon(LEFT2_OVER);
			jButton9.setPreferredSize(new Dimension(22, 20));
			jButton9.setCursor(TetrisConstants.CURSOR_HAND);
		}
		return jButton9;
	}
	/**
	 * This method initializes jPanel10
	 * @return JPanel
	 */
	private JPanel getJPanel10() {
		if(jPanel10 == null) {
			jPanel10 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 0);
			jPanel10.setLayout(flow);
			jPanel10.setPreferredSize(new Dimension(130,150));
			
			Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
			JPanel gridPanel = new JPanel(new GridLayout(4, 2, 2, 2));
			jLabel2 = new JLabel(); jLabel2.setToolTipText("Z block color"); 
			jLabel3 = new JLabel(); jLabel3.setToolTipText("S block color"); 
			jLabel4 = new JLabel(); jLabel4.setToolTipText("Left block color");
			jLabel5 = new JLabel(); jLabel5.setToolTipText("Right block color");
			jLabel6 = new JLabel(); jLabel6.setToolTipText("Line block color");
			jLabel7 = new JLabel(); jLabel7.setToolTipText("Triangle block color");
			jLabel8 = new JLabel(); jLabel8.setToolTipText("Square block color");
			jLabel9 = new JLabel(); jLabel9.setToolTipText("Flash line color");
			JLabel[] labels = new JLabel[] { jLabel2, jLabel3, jLabel4, jLabel5, 
											 jLabel6, jLabel7, jLabel8, jLabel9 }; 
			for (int i = 0; i < labels.length; i++) {
				labels[i].setOpaque(true); 
				labels[i].setBorder(border);
				labels[i].setPreferredSize(new Dimension(50,25));
				gridPanel.add(labels[i]);
			}
			
			jPanel12 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));			
			jPanel12.setPreferredSize(new Dimension(120, 140));
			jPanel12.setBorder(defaultPanel.getPanelBorder("Preview"));
			jPanel12.add(gridPanel);			
			jPanel10.add(jPanel12);
		}
		return jPanel10;
	}
	/**
	 * This method initializes jPanel11
	 * @return JPanel
	 */
	private JPanel getJPanel11() {
		if(jPanel11 == null) {
			jPanel11 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 2, 0);
			jPanel11.setLayout(flow);
			jPanel11.setPreferredSize(new Dimension(130,120));
			
			JPanel gridPanel = new JPanel(new GridLayout(6, 1, 2, 2));
			JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			JLabel label = new JLabel(" Wins: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel21 = new JLabel());
			gridPanel.add(jp); 
			jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			label = new JLabel(" Losses: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel22 = new JLabel());
			gridPanel.add(jp); 
			jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			label = new JLabel(" Percentage: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel23 = new JLabel());
			gridPanel.add(jp); 
			jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			label = new JLabel(" Tetris Total: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel24 = new JLabel());
			gridPanel.add(jp); 
			jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			label = new JLabel(" Starting Level: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel25 = new JLabel());
			gridPanel.add(jp); 
			jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
			label = new JLabel(" Game Speed: ");
			label.setPreferredSize(new Dimension(80, 20));
			jp.add(label);
			jp.add(jLabel26 = new JLabel());
			gridPanel.add(jp); 
			gridPanel.setPreferredSize(new Dimension(124, 120));
			Color blue = new Color(127, 157, 185);
			gridPanel.setBorder(BorderFactory.createLineBorder(blue, 1));
			
			jPanel11.add(gridPanel);
		}
		return jPanel11;
	}

}//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
