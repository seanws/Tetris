/**
 * StatsPanel.java
 * 
 * Created on 15-Feb-05
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.oversoul.tetris.TetrisDefaults;
import org.oversoul.tetris.TetrisPlayer;


/**
 * The StatsPanel class displays the player's name, wins, losses, 
 * winning percentage and tetris totals.
 * 
 * @author 		nyef
 * @date		15-Feb-05, 9:52:08 AM
 * @version 	1.0
 */
public class StatsPanel extends JPanel {

	private static final long serialVersionUID = 1;

	private static final String ZERO = "0";
	private static final String EMPTY = "";

	private String playerName = null;

	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JLabel jLabel = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JLabel jLabel8 = null;
	private JLabel jLabel9 = null;
	private JLabel jLabel10 = null;
	
	/**
	 * Constructor for StatsPanel.java
	 */
	public StatsPanel() {
		super();
		initialize();
	}
	
	//////////////////////////////////////////////////////////////////////
	// PUBLIC Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates the stats labels.
	 * @param player
	 */
	public void updateStats(TetrisPlayer player) {
		if (!player.getName().equals(playerName)) {
			playerName = player.getName();
			getNameLabel().setText(playerName);
		}
		getWinsLabel().setText(EMPTY+player.getWins());
		getLossesLabel().setText(EMPTY+player.getLosses());
		getPercentageLabel().setText(EMPTY+player.getWinningPercentage());
		getTetrisTotalLabel().setText(EMPTY+player.getTetrisTotal());
	}
		
	/**
	 * Updates the tetris total label.
	 * @param tetrisTotal
	 */
	public void updateTetrisTotal(int tetrisTotal) {
		getTetrisTotalLabel().setText(EMPTY+tetrisTotal);	
	}
	
	//////////////////////////////////////////////////////////////////////
	// PRIVATE & PROTECTED Methods
	//////////////////////////////////////////////////////////////////////
	
	private void initialize() {
		this.setLayout(new GridLayout(5, 1, 1, 0));
		this.add(getNameLabel(), null);
		this.add(getWinsPanel(), null);
		this.add(getLossesPanel(), null);
		this.add(getPercentagePanel(), null);
		this.add(getTetrisTotalPanel(), null);
		this.setPreferredSize(new Dimension(146,100));
	}
	/**
	 * This method initializes jPanel2
	 * @return JPanel
	 */
	private JPanel getWinsPanel() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 2));
			jPanel2.add(getJLabel2(), null);
			jPanel2.add(getWinsLabel(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3
	 * @return JPanel
	 */
	private JPanel getLossesPanel() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 2));
			jPanel3.add(getJLabel4(), null);
			jPanel3.add(getLossesLabel(), null);
		}
		return jPanel3;
	}
	/**
	 * This method initializes jPanel4
	 * @return JPanel
	 */
	private JPanel getPercentagePanel() {
		if(jPanel4 == null) {
			jPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 2));
			jPanel4.add(getJLabel6(), null);
			jPanel4.add(getPercentageLabel(), null);
			jPanel4.add(getJLabel8(), null);
		}
		return jPanel4;
	}	
	/**
	 * This method initializes jPanel5
	 * @return JPanel
	 */
	private JPanel getTetrisTotalPanel() {
		if(jPanel5 == null) {
			jPanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 2));
			jPanel5.add(getJLabel9(), null);
			jPanel5.add(getTetrisTotalLabel(), null);
		}
		return jPanel5;
	}	

	private JLabel getNameLabel() {
		if (jLabel == null) {
			jLabel = new JLabel(EMPTY);
			jLabel.setPreferredSize(new Dimension(135,20));
			jLabel.setToolTipText("Player Name");
			jLabel.setFont(TetrisDefaults.FONT_14);
			jLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return jLabel;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel("Wins:");
			jLabel2.setHorizontalAlignment(SwingConstants.LEFT);
			jLabel2.setPreferredSize(new Dimension(70,15));
		}
		return jLabel2;
	}

	private JLabel getWinsLabel() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel(ZERO);
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel3.setPreferredSize(new Dimension(50,15));
		}
		return jLabel3;
	}

	private JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new JLabel("Losses:");
			jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
			jLabel4.setPreferredSize(new Dimension(70,15));
		}
		return jLabel4;
	}

	private JLabel getLossesLabel() {
		if (jLabel5 == null) {
			jLabel5 = new JLabel(ZERO);
			jLabel5.setPreferredSize(new Dimension(50,15));
			jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel5;
	}

	private JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new JLabel("Percentage:");
			jLabel6.setPreferredSize(new Dimension(70,15));
		}
		return jLabel6;
	}

	private JLabel getPercentageLabel() {
		if(jLabel7 == null) {
			jLabel7 = new JLabel(ZERO);
			jLabel7.setPreferredSize(new Dimension(38,15));
			jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel7;
	}

	private JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new JLabel("%");
			jLabel8.setPreferredSize(new Dimension(12,15));
			jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel8;
	}

	private JLabel getJLabel9() {
		if (jLabel9 == null) {
			jLabel9 = new JLabel("Tetris Total:");
			jLabel9.setHorizontalAlignment(SwingConstants.LEFT);
			jLabel9.setPreferredSize(new Dimension(70,15));
		}
		return jLabel9;
	}

	private JLabel getTetrisTotalLabel() {
		if (jLabel10 == null) {
			jLabel10 = new JLabel(ZERO);
			jLabel10.setPreferredSize(new Dimension(50,15));
			jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel10;
	}
	
}
