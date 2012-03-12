/**
 * ScorePanel.java
 * 
 * Created on 14-Feb-05
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.oversoul.tetris.TetrisDefaults;
import org.oversoul.tetris.TetrisPlayer;


/**
 * ScorePanel class displays the score, lines, level and other scoring options.
 * 
 * @author 		nyef
 * @date		14-Feb-05, 3:37:25 PM
 * @version 	1.0
 */
public class ScorePanel extends JPanel {

	private static final long serialVersionUID = 1;
	
	private static final String ZERO = "0";
	//private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final String TEXT_GAME_OVER = "Game Over";
	private static final Dimension DIM_LABELS = new Dimension(44, 15);
	private static final Dimension DIM_LABELS2 = new Dimension(50, 15);
	private static final Dimension DIM_VALUES = new Dimension(22, 15);

	private boolean showExtras = false;

	private JPanel scorePanel = null;
	private JPanel extrasPanel = null;
	private JPanel progressPanel = null;
	
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
	private JLabel jLabel10 = null;
	private JLabel jLabel11 = null;
	private JLabel jLabel12 = null;
	private JLabel jLabel13 = null;
	private JLabel jLabel14 = null;
	private JLabel jLabel15 = null;
	
	private JProgressBar jProgressBar = null;

	/**
	 * Constructor for ScorePanel.java
	 * @param showExtras
	 */
	public ScorePanel(boolean showExtras) {
		super();
		this.showExtras = showExtras;
		initialize();
	}
	
	//////////////////////////////////////////////////////////////////////
	// PUBLIC Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates the simple scores labels.
	 * @param player
	 */
	public void updateSimpleScores(TetrisPlayer player) {
		getScoreLabel().setText(player.getScore()+SPACE);
		getGameTimeLabel().setText(player.getGameTime()+SPACE);
		updateWeightedScore(player.getWeightedScore());
	}
	
	/**
	 * Updates all the labels.
	 * @param player
	 */
	public void updateFullScores(TetrisPlayer player) {
		getScoreLabel().setText(player.getScore()+SPACE);
		updateWeightedScore(player.getWeightedScore());
		getLinesLabel().setText(player.getLines()+SPACE);
		getLevelLabel().setText(player.getLevel()+SPACE);
		getGameTimeLabel().setText(player.getGameTime()+SPACE);
		if (showExtras) {
			getSinglesLabel().setText(player.getSingles()+SPACE);
			getDoublesLabel().setText(player.getDoubles()+SPACE);
			getTriplesLabel().setText(player.getTriples()+SPACE);
			getTetrisCountLabel().setText(player.getTetrisCount()+SPACE);
		}	
	}
	
	/**
	 * Sets the level label.
	 * @param level
	 */
	public void setLevelLabel(int level) {
		getLevelLabel().setText(level+SPACE);	
	}
	/**
	 * Sets the lines label.
	 * @param lines
	 */
	public void setLinesLabel(int lines) {
		getLinesLabel().setText(lines+SPACE);	
	}
	
	//////////////////////////////////////////////////////////////////////
	// PRIVATE & PROTECTED Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Updates the progress bar.
	 */
	private void updateWeightedScore(int percent) {
		getProgressBar().setValue(percent);
		String s = percent + "%";
		if (percent == 100) {
			s = TEXT_GAME_OVER;
		}
		getProgressBar().setString(s);
	}
	
	/**
	 * Initializes this panel.
	 */
	private void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		this.add(getScoresPanel());
		this.add(getExtrasPanel());
		this.add(getProgressPanel());
	}
	
	/**
	 * This method initializes the scorePanel
	 * @return JPanel
	 */
	private JPanel getScoresPanel() {
		if (scorePanel == null) {
			scorePanel = new JPanel(new GridLayout(4, 2, 0, 0));
			scorePanel.add(getScoreLabelLabel(), null);
			scorePanel.add(getScoreLabel(), null);
			scorePanel.add(getLinesLabelLabel(), null);
			scorePanel.add(getLinesLabel(), null);
			scorePanel.add(getLinesLabellabel(), null);
			scorePanel.add(getLevelLabel(), null);
			scorePanel.add(getGameTimeLabelLabel(), null);
			scorePanel.add(getGameTimeLabel(), null);
			scorePanel.setPreferredSize(new Dimension(146, 72));
		}
		return scorePanel;
	}
	/**
	 * This method initialies the extrasPanel
	 * @return JPanel
	 */
	private JPanel getExtrasPanel() {
		if (extrasPanel == null) {
			extrasPanel = new JPanel(new GridLayout(2, 2, 0, 0));
			
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.add(getSinglesLabelLabel(), null);
			panel.add(getSinglesLabel(), null);
			extrasPanel.add(panel);
			
			panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			panel.add(getDoublesLabelLabel(), null);
			panel.add(getDoublesLabel(), null);
			extrasPanel.add(panel);
			
			panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.add(getTriplesLabelLabel(), null);
			panel.add(getTriplesLabel(), null);
			extrasPanel.add(panel);
			
			panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			panel.add(getTetrisCountLabelLabel(), null);
			panel.add(getTetrisCountLabel(), null);
			extrasPanel.add(panel);
			
			extrasPanel.setPreferredSize(new Dimension(146, 38));
			extrasPanel.setVisible(showExtras);
			extrasPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		}
		return extrasPanel;
	}
	/**
	 * This method initializes jLabel
	 * @return JLabel
	 */
	private JLabel getScoreLabelLabel() {
		if (jLabel == null) {
			jLabel = new JLabel(" Score: ");
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 * @return JLabel
	 */
	private JLabel getScoreLabel() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel(ZERO);
			//jLabel1.setPreferredSize(new Dimension(40,15));
			jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel1;
	}
	/**
	 * This method initializes jLabel2
	 * @return JLabel
	 */
	private JLabel getLinesLabelLabel() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel(" Lines: ");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jLabel3
	 * @return JLabel
	 */
	private JLabel getLinesLabel() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel(ZERO);
			jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel3;
	}
	/**
	 * This method initializes jLabel4
	 * @return JLabel
	 */
	private JLabel getLinesLabellabel() {
		if (jLabel4 == null) {
			jLabel4 = new JLabel(" Level: ");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jLabel5
	 * @return JLabel
	 */
	private JLabel getLevelLabel() {
		if (jLabel5 == null) {
			jLabel5 = new JLabel(ZERO);
			jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel5;
	}
	/**
	 * This method initializes jLabel6
	 * @return JLabel
	 */
	private JLabel getTetrisCountLabelLabel() {
		if (jLabel6 == null) {
			jLabel6 = new JLabel("  Tetrises:");
			jLabel6.setPreferredSize(DIM_LABELS2);
			jLabel6.setFont(TetrisDefaults.FONT_LABEL_SMALL);
		}
		return jLabel6;
	}
	/**
	 * This method initializes jLabel35
	 * @return JLabel
	 */
	private JLabel getTetrisCountLabel() {
		if (jLabel7 == null) {
			jLabel7 = new JLabel(ZERO);
			jLabel7.setPreferredSize(DIM_VALUES);
			jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel7;
	}
	/**
	 * This method initializes jLabel8.
	 * @return JLabel
	 */
	private JLabel getTriplesLabelLabel() {
		if (jLabel8 == null) {
			jLabel8 = new JLabel(" Triples:");
			jLabel8.setPreferredSize(DIM_LABELS);
			jLabel8.setFont(TetrisDefaults.FONT_LABEL_SMALL);
		}
		return jLabel8;
	}
	/**
	 * This method initializes jLabel9.
	 * @return JLabel
	 */
	private JLabel getDoublesLabelLabel() {
		if (jLabel9 == null) {
			jLabel9 = new JLabel("  Doubles:");
			jLabel9.setPreferredSize(DIM_LABELS2);
			jLabel9.setFont(TetrisDefaults.FONT_LABEL_SMALL);
		}
		return jLabel9;
	}
	/**
	 * This method initializes jLabel10.
	 * @return JLabel
	 */
	private JLabel getSinglesLabelLabel() {
		if (jLabel10 == null) {
			jLabel10 = new JLabel(" Singles:");
			jLabel10.setPreferredSize(DIM_LABELS);
			jLabel10.setFont(TetrisDefaults.FONT_LABEL_SMALL);
		}
		return jLabel10;
	}
	/**
	 * This method initializes jLabel41.
	 * @return JLabel
	 */
	private JLabel getGameTimeLabelLabel() {
		if (jLabel11 == null) {
			jLabel11 = new JLabel(" Timer: ");
			jLabel11.setVisible(showExtras);
		}
		return jLabel11;
	}
	/**
	 * This method initializes the triples (three lines) label.
	 * @return JLabel
	 */
	private JLabel getTriplesLabel() {
		if (jLabel12 == null) {
			jLabel12 = new JLabel(ZERO);
			jLabel12.setPreferredSize(DIM_VALUES);
			jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel12;
	}
	/**
	 * This method initializes the doubles (2 lines) label.
	 * @return JLabel
	 */
	private JLabel getDoublesLabel() {
		if (jLabel13 == null) {
			jLabel13 = new JLabel(ZERO);
			jLabel13.setPreferredSize(DIM_VALUES);
			jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel13;
	}
	/**
	 * This method initializes the singles (single line) label.
	 * @return JLabel
	 */
	private JLabel getSinglesLabel() {
		if (jLabel14 == null) {
			jLabel14 = new JLabel(ZERO);
			jLabel14.setPreferredSize(DIM_VALUES);
			jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return jLabel14;
	}
	/**
	 * This method initializes the game playing time label.
	 * @return JLabel
	 */
	private JLabel getGameTimeLabel() {
		if (jLabel15 == null) {
			jLabel15 = new JLabel("");
			jLabel15.setPreferredSize(DIM_VALUES);
			jLabel15.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel15.setVisible(showExtras);
		}
		return jLabel15;
	}
	/**
	 * This method initializes progressPanel
	 * @return JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			progressPanel = new JPanel(new GridLayout(1, 1, 0, 0));
			progressPanel.add(getProgressBar(), null);
			progressPanel.setPreferredSize(new Dimension(144,25));
		}
		return progressPanel;
	}
	/**
	 * This method initializes jProgressBar
	 * @return JProgressBar
	 */
	private JProgressBar getProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
			jProgressBar.setMinimum(0);
			jProgressBar.setMaximum(100);
			jProgressBar.setToolTipText("How close this player is to being game over");
			jProgressBar.setPreferredSize(new Dimension(95, 18));
		}
		return jProgressBar;
	}

}
