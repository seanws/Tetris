/**
 * PlayerChangeDialog.java
 * 
 * Created on 4-Apr-04
 */
package org.oversoul.tetris.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisPanel;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.util.Util;

/**
 * Player change dialog.  Lets the user change the current tetris player, delete a player,
 * or create a new player.
 * 
 * @author Chris Callendar (9902588)
 * @date   4-Apr-04, 2:26:59 PM
 */
public class PlayerChangeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1;

	private static final String[] BAD_NAMES = new String[] { "multiplayer", "player", "color" };

	private IUIController uiController = null;
	private TetrisRootPane rootPane = null;
	private Hashtable<String, TetrisPlayer> names = null;
	private String currentPlayerName = null;
	private String startingPlayerName = null;
	private boolean allowDeletePlayer = true;

	private StatsPanel statsPanel = null;
    private JPanel jContentPane = null;
    private JPanel jPanel = null;
	private JPanel jPanel1 = null;
    private JComboBox jComboBox = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;

	/**
	 * PlayerChangeDialog constructor.
	 */
	public PlayerChangeDialog(Frame parent, TetrisRootPane rootPane, IUIController uiController, boolean allowDeletePlayer) {
		super(parent);
		this.rootPane = rootPane;
		this.uiController = uiController;
		this.allowDeletePlayer = allowDeletePlayer;
		this.currentPlayerName = uiController.getPlayer().getName();
		this.startingPlayerName = currentPlayerName;
		this.names = new Hashtable<String, TetrisPlayer>();
		setModal(true);
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
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("NameChange")) {
			handleNameChange();
		} else if (action.equals("OK")) {
			saveNameChange();
		} else if (action.equals("Cancel")) {
			setLastPlayer(startingPlayerName);
			dispose();
		} else if (action.equals("NewPlayer")) {
			addNewPlayer();	
		} else if (action.equals("Delete")) {
			deletePlayer();
		} else if (action.equals("ClearStats")) {
			clearStats();
		}
	}

	/**
	 * Resets the player's stats.
	 */
	private void clearStats() {
		String name = (String) getPlayersComboBox().getSelectedItem();
		if (names.containsKey(name)) {
			int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear " + name + "'s stats?", 
								"Clearing player stats", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				TetrisPlayer p = names.get(name);
				p.clearStats();
				p.save(uiController.getConfiguration());
				updateStats(p);
			}
		}
	}

	/**
	 * Deletes the selected player.
	 */
	private void deletePlayer() {
		String name = (String) getPlayersComboBox().getSelectedItem();
		if ((name != null) && (name.length() > 0)) {
			int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete player '" + name + "'?", "Confirm...", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				uiController.getConfiguration().removePlayer(name);
				names.remove(name);
				getPlayersComboBox().removeItem(name);
				boolean delStartingPlayer = startingPlayerName.equals(name);
				if ((names.size() == 0) || delStartingPlayer) {
					TetrisPlayer tp = new TetrisPlayer("Player1");
					if (delStartingPlayer) {
						startingPlayerName = tp.getName();	
					}
					names.put(tp.getName(), tp);
					getPlayersComboBox().addItem(tp.getName());
					updateStats(tp);
				}
				getPlayersComboBox().setSelectedIndex(0);
			}
		}
	}

	/**
	 * Adds a new player to the list.
	 */
	private void addNewPlayer() {
		String name = JOptionPane.showInputDialog(this, "Enter a new player name:", "New Player", JOptionPane.INFORMATION_MESSAGE);
		if ((name != null) && (name.length() > 0)) {
			if (name.length() > 25) {
				name = name.substring(0, 25);
			}
			name = name.replace('.', '_');
			name = name.replace(' ', '_');

			for (int i = 0; i < BAD_NAMES.length; i++) {
				if (name.equals(BAD_NAMES[i]))
					return;
			}

			TetrisPlayer newPlayer = new TetrisPlayer(name);
			newPlayer.resetAll();
			newPlayer.save(uiController.getConfiguration());
			// load the new player to set all the defaults
			newPlayer.load(name, uiController.getConfiguration());
			if (!names.containsKey(name)) {
				names.put(name, newPlayer);
				getPlayersComboBox().addItem(name);
			}
			getPlayersComboBox().setSelectedItem(name);
			updateStats(newPlayer);
		}
	}

	/**
	 * Handles a name change from the combo box.
	 */
	private void handleNameChange() {
		String name = (String) getPlayersComboBox().getSelectedItem();
		if ((name != null) && names.containsKey(name)) {
			TetrisPlayer selPlayer = names.get(name);
			updateStats(selPlayer);
			changeBorder(selPlayer.getPanel());
		}
	}
	
	/**
	 * Changes the tetris panel colors and combobox colors.
	 * @param currPanel
	 */
	private void changeBorder(TetrisPanel currPanel) {
		if (currPanel != null) {
			PanelBorder border = currPanel.getPanelBorder("Stats", getStatsPanel().getBackground(), Color.WHITE);
			getStatsPanel().setBorder(border);
			
			Color bg = Util.lighterColor(currPanel.getFirstColor(), currPanel.getSecondColor());
			getPlayersComboBox().setBackground(bg);
			getPlayersComboBox().setForeground(Util.textColor(bg));
		}
	}
	
	/**
	 * Saves the name change info.
	 */
	private void saveNameChange() {
		String name = (String) getPlayersComboBox().getSelectedItem();
		setLastPlayer(name);
		dispose();
	}
	
	/**
	 * Saves the last player.
	 * @param name
	 */
	private void setLastPlayer(String name) {
		if ((name != null) && names.containsKey(name)) {
			TetrisPlayer player = names.get(name);
			player.saveLast(uiController.getConfiguration());
		}
	}

	/**
	 * Reads the players in from the configuration object.
	 */
	private void readPlayers() {
		names = uiController.getConfiguration().loadPlayers();
		int sel = -1;
		TetrisPlayer selPlayer = null;
		String[] nms = new String[names.size()];
		nms = names.keySet().toArray(nms);
		if ((nms != null) && (nms.length > 1)) {
			Arrays.sort(nms);
		}
		for (int i = 0; i < nms.length; i++) {
			String name = nms[i];
			if (name.equals(currentPlayerName)) {
				sel = i;
				selPlayer = names.get(name);
			}
			getPlayersComboBox().addItem(name);
		}
		
		getPlayersComboBox().setSelectedIndex(sel);
		updateStats(selPlayer);
	}

	/**
	 * Updates the wins and losses for a player.
	 * @param p	the player.
	 */
	private void updateStats(TetrisPlayer p) {
		if (p != null) {
			getStatsPanel().updateStats(p);
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
        this.setContentPane(getRootContentPane());
        this.setSize(198, 240);
        this.setModal(true);
        this.setTitle("Player Change");
        readPlayers();
       	this.setLocation(rootPane.getTopLeft().x+185, rootPane.getTopLeft().y+100);
        this.setResizable(false);
        this.pack();
		getOKButton().requestFocusInWindow();
		this.setVisible(true);			
	}

	private JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			jContentPane.add(new JLabel("  "), BorderLayout.WEST);
			jContentPane.add(getStatsPanel(), BorderLayout.CENTER);
			jContentPane.add(new JLabel("  "), BorderLayout.EAST);
			jContentPane.add(getOKCancelPanel(), BorderLayout.SOUTH);
			jContentPane.setPreferredSize(new Dimension(190, 210));
		}
		return jContentPane;
	}

	private JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getPlayersComboBox(), null);
			jPanel.add(getNewPlayerButton(), null);
			jPanel.add(getDeleteButton(), null);
			jPanel.add(getClearButton(), null);
			jPanel.setPreferredSize(new Dimension(160, 60));
		}
		return jPanel;
	}
	
	private StatsPanel getStatsPanel() {
		if (statsPanel == null) {
			statsPanel = new StatsPanel();
		}
		return statsPanel;
	}

	private JComboBox getPlayersComboBox() {
		if(jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setActionCommand("NameChange");
			jComboBox.addActionListener(this);
			jComboBox.setPreferredSize(new Dimension(167, 20));
			Color bg = Util.lighterColor(rootPane.getTetrisPanel().getFirstColor(), rootPane.getTetrisPanel().getSecondColor());
			jComboBox.setBackground(bg);
			jComboBox.setForeground(Util.textColor(bg));
		}
		return jComboBox;
	}

	private JPanel getOKCancelPanel() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.add(getOKButton(), null);
			jPanel1.add(getCancelButton(), null);
		}
		return jPanel1;
	}

	private JButton getOKButton() {
		if(jButton == null) {
			jButton = new JButton("    OK    ");
			jButton.addActionListener(this);
			jButton.setActionCommand("OK");
			jButton.setMnemonic(KeyEvent.VK_O);
		}
		return jButton;
	}

	private JButton getCancelButton() {
		if(jButton1 == null) {
			jButton1 = new JButton("Cancel");
			jButton1.setActionCommand("Cancel");
			jButton1.addActionListener(this);
			jButton1.setMnemonic(KeyEvent.VK_C);
		}
		return jButton1;
	}

	private JButton getNewPlayerButton() {
		if(jButton2 == null) {
			jButton2 = new JButton("New");
			jButton2.setActionCommand("NewPlayer");
			jButton2.setToolTipText("Create a new player");
			jButton2.addActionListener(this);
		}
		return jButton2;
	}
	
	private JButton getDeleteButton() {
		if(jButton3 == null) {
			jButton3 = new JButton(Util.getIconResource("/images/delete.gif"));
			jButton3.setActionCommand("Delete");
			jButton3.setToolTipText("Delete the selected player");
			jButton3.addActionListener(this);
			jButton3.setEnabled(allowDeletePlayer);
		}
		return jButton3;
	}
	
	private JButton getClearButton() {
		if(jButton4 == null) {
			jButton4 = new JButton("Clear");
			jButton4.setActionCommand("ClearStats");
			jButton4.setToolTipText("Clear the stats for the selected player");
			jButton4.addActionListener(this);
			jButton4.setEnabled(allowDeletePlayer);
		}
		return jButton4;
	}

}
