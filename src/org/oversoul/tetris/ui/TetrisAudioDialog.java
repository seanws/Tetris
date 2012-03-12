/**
 * TetrisAudioDialog.java
 * 
 * Created on Sep 28, 2004
 */
package org.oversoul.tetris.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.oversoul.audio.Audio;
import org.oversoul.audio.AudioListener;
import org.oversoul.audio.AudioUI;
import org.oversoul.audio.MidiFile;
import org.oversoul.audio.SampledAudioFile;
import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisAudio;
import org.oversoul.tetris.TetrisPanel;
import org.oversoul.tetris.util.Util;

/**
 * Displays the audio dialog which lets the player choose audio clips to play 
 * during a tetris game.
 * @author ccallendar
 */
public class TetrisAudioDialog extends JDialog implements ActionListener,AudioUI {

	private static final long serialVersionUID = 1;

	private IUIController uiController = null;
	private TetrisRootPane rootPane = null;
	private TetrisAudio audio = null;
	private Audio currentSong = null;
	private JRadioButton currentButton = null;
	private AudioListener currentListener = null;

	private ImageIcon playIcon;
	private ImageIcon playIconOver;
	private ImageIcon playIconDisabled;
	private ImageIcon stopIcon;
	private ImageIcon stopIconOver;
	private Cursor CURSOR_HAND;
	
	//TODO too many fields!
	private JFileChooser audioChooser = null;
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
	private JCheckBox jCheckBox = null;
	private JCheckBox jCheckBox1 = null;
	private JCheckBox jCheckBox2 = null;
	private JCheckBox jCheckBox3 = null;
	private JCheckBox jCheckBox4 = null;
	private JCheckBox jCheckBox5 = null;
	private JCheckBox jCheckBox6 = null;
	private JCheckBox jCheckBox7 = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel7 = null;
	private JTextField jTextField = null;
	private JTextField jTextField1 = null;
	private JTextField jTextField2 = null;
	private JTextField jTextField3 = null;
	private JTextField jTextField4 = null;
	private JTextField jTextField5 = null;
	private JSlider jSlider = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
	private JButton jButton5 = null;
	private JButton jButton6 = null;
	private JButton jButton7 = null;
	private JRadioButton jRadioButton = null;
	private JRadioButton jRadioButton1 = null;
	private JRadioButton jRadioButton2 = null;
	private JRadioButton jRadioButton3 = null;
	private JRadioButton jRadioButton4 = null;
	private JRadioButton jRadioButton5 = null;
	
	/**
	 * TetrisAudioDialog constructor.
	 * @param controller
	 */
	public TetrisAudioDialog(Frame parent, TetrisRootPane rootPane, IUIController uiController) {
		super(parent);
		this.rootPane = rootPane;
		this.uiController = uiController;
		if (uiController.getPlayer().getAudio() == null) {
			uiController.getPlayer().setAudio(new TetrisAudio());
		}
		this.audio = uiController.getPlayer().getAudio();
		
		CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);
		playIcon = Util.getIconResource("/images/play.gif");
		playIconOver = Util.getIconResource("/images/play_over.gif");
		playIconDisabled = Util.getIconResource("/images/play_disabled.gif");
		stopIcon = Util.getIconResource("/images/stop_down.gif");
		stopIconOver = Util.getIconResource("/images/stop_over.gif");
	
		initialize();
	}
	
	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		if (currentSong != null) {
			currentSong.close();
			currentSong = null;	
		}
		if (currentListener != null) {
			currentListener = null;	
		}
		super.dispose();
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("OK")) {
			saveAudio();
			dispose();
		} else if (action.equals("Cancel")) {
			dispose();				
		} else if (action.equals("GameSoundsCB")) {
			enableAllSounds(getGameSoundsCheckBox());
		} else if (action.equals("TetrisCB")) {
			enableSound(getTetrisCheckBox(), getTetrisLabel(), getTetrisTextField(), getChooseTetrisButton(), getPlayTetrisButton());
		} else if (action.equals("LevelUpCB")) {
			enableSound(getLevelUpCheckBox(), getLevelUpLabel(), getLevelUpTextField(), getChooseLevelUpButton(), getPlayLevelUpButton());
		} else if (action.equals("GameOverCB")) {
			enableSound(getGameOverCheckBox(), getGameOverLabel(), getGameOverTextField(), getChooseGameOverButton(), getPlayGameOverButton());
		} else if (action.equals("HighscoreCB")) {
			enableSound(getHighscoreCheckBox(), getHighscoreLabel(), getHighscoreTextField(), getChooseHighscoreButton(), getPlayHighscoreButton());
		} else if (action.equals("PenaltyCB")) {
			enableSound(getPenaltyCheckBox(), getPenaltyLabel(), getPenaltyTextField(), getChoosePenaltyButton(), getPlayPenaltyButton());
		} else if (action.equals("BackgroundCB")) {
			enableBackgroundSounds(getBackgroundCheckBox());
		} else if (action.equals("ChooseTetris")) {
			handleAudioChooser(getTetrisTextField());
		} else if (action.equals("ChooseLevelUp")) {
			handleAudioChooser(getLevelUpTextField());
		} else if (action.equals("ChooseGameOver")) {
			handleAudioChooser(getGameOverTextField());
		} else if (action.equals("ChooseHighscore")) {
			handleAudioChooser(getHighscoreTextField());
		} else if (action.equals("ChoosePenalty")) {
			handleAudioChooser(getPenaltyTextField());
		} else if (action.equals("ChooseBackground")) {
			handleAudioChooser(getBackgroundTextField());
		} else if ("PlayTetris".equals(action)) {
			playSong(getPlayTetrisButton(), getTetrisTextField().getText(), -1);
		} else if ("PlayLevelUp".equals(action)) {
			playSong(getPlayLevelUpButton(), getLevelUpTextField().getText(), -1);
		} else if ("PlayGameOver".equals(action)) {
			playSong(getPlayGameOverButton(), getGameOverTextField().getText(), -1);
		} else if ("PlayHighscore".equals(action)) {
			playSong(getPlayHighscoreButton(), getHighscoreTextField().getText(), -1);
		} else if ("PlayPenalty".equals(action)) {
			playSong(getPlayPenaltyButton(), getPenaltyTextField().getText(), -1);
		} else if ("PlayBackground".equals(action)) {
			playSong(getPlayBackgroundButton(), getBackgroundTextField().getText(), getVolumeControlSlider().getValue());
		}
	}
	
	
	/**
	 * @see org.oversoul.audio.AudioUI#audioStopped()
	 */
	public void audioStopped() {
		if (currentButton != null) {
			currentButton.setSelected(false);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// ACTION HANDLER METHODS 
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Plays the song.
	 * @param playButton the play button
	 * @param path the audio file
	 * @param volume the volume or -1 if leave as it was
	 */
	private void playSong(JRadioButton playButton, String path, int volume) {
		boolean play = playButton.isSelected();
		// stop and close the current song
		if (currentSong != null) {
			currentSong.close();
			currentSong = null;	
		}
		if ((currentButton != null) && !playButton.equals(currentButton)) {
			currentButton.setSelected(false);
		}

		// play the new song
		if (play) {
			if (path.length() > 0) {
				if (AudioFileFilter.isMidiFile(path)) {
					currentSong = new MidiFile(path);
				} else if (AudioFileFilter.isSampledAudioFile(path)) {
					currentSong = new SampledAudioFile(path);
				} else {
					currentSong = null;	
				}
				if (currentSong != null) {
					if (volume >= 0)
						currentSong.setVolume(volume);	
					currentButton = playButton;
					currentListener = new AudioListener(this, currentSong);
					currentListener.play();
				}
			}
		}
	}
	
	/**
	 * Opens a file chooser dialog to select the desired audio file.  Then sets the seleceted
	 * filename and path in the textfield.
	 * @param field
	 */
	private void handleAudioChooser(JTextField field) {
		if (audioChooser == null) {
			audioChooser = new JFileChooser();
			audioChooser.setApproveButtonMnemonic(KeyEvent.VK_ENTER);
			audioChooser.setApproveButtonText("OK");
			audioChooser.setDialogTitle("Audio Chooser");
			audioChooser.setFileFilter(new AudioFileFilter());
			audioChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		File file = new File(field.getText());
		if (file.exists()) {
			audioChooser.setSelectedFile(file);
		}
		
		int choice = audioChooser.showOpenDialog(this);
		if(choice == JFileChooser.APPROVE_OPTION) {
			field.setText(audioChooser.getSelectedFile().getAbsolutePath());
		}
	}
	/**
	 * Enables or disables background sounds.
	 * @param box the checkbox
	 */
	private void enableBackgroundSounds(JCheckBox box) {
		boolean enabled = box.isSelected();
		enableSound(box, getBackgroundLabel(), getBackgroundTextField(), getChooseBackgroundButton(), getPlayBackgroundButton());
		getLoopCheckBox().setEnabled(enabled);
		getVolumeLabel().setEnabled(enabled);
		getVolumeControlSlider().setEnabled(enabled);
	}
	
	/**
	 * Enables or disables all game sounds.
	 * @param box the checkbox.
	 */
	private void enableAllSounds(JCheckBox box) {
		boolean enabled = box.isSelected();
		enableSound(getTetrisCheckBox(), enabled, getTetrisLabel(), getTetrisTextField(), getChooseTetrisButton(), getPlayTetrisButton());
		enableSound(getLevelUpCheckBox(), enabled, getLevelUpLabel(), getLevelUpTextField(), getChooseLevelUpButton(), getPlayLevelUpButton());
		enableSound(getGameOverCheckBox(), enabled, getGameOverLabel(), getGameOverTextField(), getChooseGameOverButton(), getPlayGameOverButton());
		enableSound(getHighscoreCheckBox(), enabled, getHighscoreLabel(), getHighscoreTextField(), getChooseHighscoreButton(), getPlayHighscoreButton());
		enableSound(getPenaltyCheckBox(), enabled, getPenaltyLabel(), getPenaltyTextField(), getChoosePenaltyButton(), getPlayPenaltyButton());
	}
	
	/**
	 * Enables or disables a label, textfield, and button.
	 * @param box	a checkbox
	 * @param label a label
	 * @param field a textfield 
	 * @param button a button.
	 * @param playButton the play button.
	 */
	private void enableSound(JCheckBox box, JLabel label, JTextField field, JButton button, JRadioButton playButton) {
		enableSound(box, box.isEnabled(), label, field, button, playButton);
	}

	/**
	 * Enables or disables a label, textfield, and button.
	 * @param box	a checkbox
	 * @param enabled if the checkbox should be enabled
	 * @param label a label
	 * @param field a textfield 
	 * @param button a button
	 * @param playButton the play button.
	 */
	private void enableSound(JCheckBox box, boolean enabled, JLabel label, JTextField field, JButton button, JRadioButton playButton) {
		box.setEnabled(enabled);
		boolean checked = enabled && box.isSelected();
		label.setForeground((checked ? Color.BLACK : Color.GRAY));		
		field.setEnabled(checked);
		button.setEnabled(checked);
		playButton.setEnabled(checked);
	}

	/**
	 * Saves the audio selections.
	 */
	private void saveAudio() {
		audio.gameSounds = getGameSoundsCheckBox().isSelected();
		if (audio.gameSounds) {
			audio.tetris = getTetrisCheckBox().isSelected();
			if (audio.tetris) {
				audio.setTetrisAudio(getTetrisTextField().getText());
			}
			audio.levelUp = getLevelUpCheckBox().isSelected();
			if (audio.levelUp) {
				audio.setLevelUpAudio(getLevelUpTextField().getText());
			}
			audio.gameOver = getGameOverCheckBox().isSelected();
			if (audio.gameOver) {
				audio.setGameOverAudio(getGameOverTextField().getText());
			}
			audio.highscore = getHighscoreCheckBox().isSelected();
			if (audio.highscore) {
				audio.setHighscoreAudio(getHighscoreTextField().getText());
			}
			audio.penalty = getPenaltyCheckBox().isSelected();
			if (audio.penalty) {
				audio.setPenaltyAudio(getPenaltyTextField().getText());
			}
		}			
		audio.background = getBackgroundCheckBox().isSelected();
		if (audio.background) {
			audio.loop = getLoopCheckBox().isSelected();
			audio.setBackgroundAudio(getBackgroundTextField().getText(), audio.loop);
			audio.volume = getVolumeControlSlider().getValue();
		}
			 
		uiController.getPlayer().setAudio(audio);
		uiController.getPlayer().saveAudio(uiController.getConfiguration());
	}
	
	/**
	 * Enables or disables the checkboxes, labels, textfields, and buttons. 
	 */
	private void loadSounds() {
		enableAllSounds(getGameSoundsCheckBox());
		enableBackgroundSounds(getBackgroundCheckBox());
	}

	//////////////////////////////////////////////////////////////////////////////////
	// AUTO-GENERATED METHODS
	//////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(508, 405);
		this.setName("AudioChooser");
		this.setTitle("Tetris Audio Chooser");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setModal(true);
		this.setLocation(rootPane.getTopLeft().x+25, rootPane.getTopLeft().y+80);
		loadSounds();
        this.pack();
        getOKButton().requestFocusInWindow();
		this.setVisible(true);
	}
	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new javax.swing.JPanel();
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel3(), null);
			jPanel.add(getJPanel11(), null);
			jPanel.setPreferredSize(new Dimension(500, 380));
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
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 2, 2);
			jPanel1.setLayout(flow);
			jPanel1.add(getJPanel2(), null);
			jPanel1.add(getJPanel5(), null);
			jPanel1.add(getJPanel6(), null);
			jPanel1.add(getJPanel7(), null);
			jPanel1.add(getJPanel8(), null);
			jPanel1.add(getJPanel9(), null);
			TetrisPanel panel = uiController.getPlayer().getPanel();
			PanelBorder border = panel.getPanelBorder("Game Sounds", jPanel1.getBackground());
			jPanel1.setBorder(border);
			jPanel1.setPreferredSize(new Dimension(490,220));
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
			java.awt.FlowLayout flow = new java.awt.FlowLayout();
			flow.setAlignment(FlowLayout.LEFT);
			jPanel2.setLayout(flow);
			jPanel2.add(getGameSoundsCheckBox(), null);
			jPanel2.setPreferredSize(new Dimension(480,30));
		}
		return jPanel2;
	}
	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getGameSoundsCheckBox() {
		if(jCheckBox == null) {
			jCheckBox = new javax.swing.JCheckBox();
			jCheckBox.setActionCommand("GameSoundsCB");
			jCheckBox.setText(" Enable game sounds");
			jCheckBox.addActionListener(this);
			jCheckBox.setSelected(audio.gameSounds);
		}
		return jCheckBox;
	}
	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new javax.swing.JPanel();
			FlowLayout layFlowLayout8 = new FlowLayout();
			layFlowLayout8.setVgap(2);
			jPanel3.setLayout(layFlowLayout8);
			jPanel3.add(getJPanel4(), null);
			jPanel3.add(getJPanel10(), null);
			jPanel3.setPreferredSize(new Dimension(490,108));
			TetrisPanel panel = uiController.getPlayer().getPanel();
			PanelBorder border = panel.getPanelBorder("Background Music", jPanel3.getBackground());
			jPanel3.setBorder(border);
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
			java.awt.FlowLayout layFlowLayout2 = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			jPanel4.setLayout(layFlowLayout2);
			jPanel4.add(getBackgroundCheckBox(), null);
			jPanel4.add(getBackgroundLabel(), null);
			jPanel4.add(getBackgroundTextField(), null);
			jPanel4.add(getChooseBackgroundButton(), null);
			jPanel4.add(getPlayBackgroundButton(), null);
			jPanel4.setPreferredSize(new Dimension(480,30));
		}
		return jPanel4;
	}
	/**
	 * This method initializes jCheckBox1
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getBackgroundCheckBox() {
		if(jCheckBox1 == null) {
			jCheckBox1 = new javax.swing.JCheckBox();
			jCheckBox1.setActionCommand("BackgroundCB");
			jCheckBox1.addActionListener(this);
			jCheckBox1.setSelected(audio.background);
		}
		return jCheckBox1;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getBackgroundLabel() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("Background song:");
			jLabel1.setPreferredSize(new Dimension(100,15));
			jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel1;
	}
	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel5() {
		if(jPanel5 == null) {
			jPanel5 = new javax.swing.JPanel();
			FlowLayout layFlowLayout3 = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			jPanel5.setLayout(layFlowLayout3);
			jPanel5.add(getTetrisCheckBox(), null);
			jPanel5.add(getTetrisLabel(), null);
			jPanel5.add(getTetrisTextField(), null);
			jPanel5.add(getChooseTetrisButton(), null);
			jPanel5.add(getPlayTetrisButton(), null);
			jPanel5.setPreferredSize(new Dimension(480,30));
		}
		return jPanel5;
	}
	/**
	 * This method initializes jCheckBox2
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getTetrisCheckBox() {
		if(jCheckBox2 == null) {
			jCheckBox2 = new javax.swing.JCheckBox();
			jCheckBox2.setActionCommand("TetrisCB");
			jCheckBox2.addActionListener(this);
			jCheckBox2.setSelected(audio.tetris);
		}
		return jCheckBox2;
	}
	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTetrisTextField() {
		if(jTextField == null) {
			jTextField = new javax.swing.JTextField();
			jTextField.setPreferredSize(new Dimension(270,21));
			jTextField.setText(audio.getTetrisAudioPath());
		}
		return jTextField;
	}
	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChooseTetrisButton() {
		if(jButton == null) {
			jButton = new javax.swing.JButton();
			jButton.setText("...");
			jButton.setActionCommand("ChooseTetris");
			jButton.addActionListener(this);
		}
		return jButton;
	}
	/**
	 * This method initializes jLabel2
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getTetrisLabel() {
		if(jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
			jLabel2.setText("Tetris:");
			jLabel2.setPreferredSize(new Dimension(80,15));
			jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel2;
	}
	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel6() {
		if(jPanel6 == null) {
			jPanel6 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout4 = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			jPanel6.setLayout(layFlowLayout4);
			jPanel6.add(getLevelUpCheckBox(), null);
			jPanel6.add(getLevelUpLabel(), null);
			jPanel6.add(getLevelUpTextField(), null);
			jPanel6.add(getChooseLevelUpButton(), null);
			jPanel6.add(getPlayLevelUpButton(), null);
			jPanel6.setPreferredSize(new Dimension(480,30));
		}
		return jPanel6;
	}
	/**
	 * This method initializes jCheckBox3
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getLevelUpCheckBox() {
		if(jCheckBox3 == null) {
			jCheckBox3 = new javax.swing.JCheckBox();
			jCheckBox3.setActionCommand("LevelUpCB");
			jCheckBox3.addActionListener(this);
			jCheckBox3.setSelected(audio.levelUp);
		}
		return jCheckBox3;
	}
	/**
	 * This method initializes jLabel3
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLevelUpLabel() {
		if(jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("Level up:");
			jLabel3.setPreferredSize(new Dimension(80,15));
			jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel3;
	}
	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getLevelUpTextField() {
		if(jTextField1 == null) {
			jTextField1 = new javax.swing.JTextField();
			jTextField1.setPreferredSize(new Dimension(270,21));
			jTextField1.setText(audio.getLevelUpAudioPath());
		}
		return jTextField1;
	}
	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChooseLevelUpButton() {
		if(jButton1 == null) {
			jButton1 = new javax.swing.JButton();
			jButton1.setText("...");
			jButton1.setActionCommand("ChooseLevelUp");
			jButton1.addActionListener(this);
		}
		return jButton1;
	}
	/**
	 * This method initializes jPanel7
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel7() {
		if(jPanel7 == null) {
			jPanel7 = new javax.swing.JPanel();
			FlowLayout layFlowLayout5 = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			layFlowLayout5.setVgap(2);
			jPanel7.setLayout(layFlowLayout5);
			jPanel7.add(getGameOverCheckBox(), null);
			jPanel7.add(getGameOverLabel(), null);
			jPanel7.add(getGameOverTextField(), null);
			jPanel7.add(getChooseGameOverButton(), null);
			jPanel7.add(getPlayGameOverButton(), null);
			jPanel7.setPreferredSize(new Dimension(480,30));
		}
		return jPanel7;
	}
	/**
	 * This method initializes jCheckBox4
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getGameOverCheckBox() {
		if(jCheckBox4 == null) {
			jCheckBox4 = new javax.swing.JCheckBox();
			jCheckBox4.setActionCommand("GameOverCB");
			jCheckBox4.addActionListener(this);
			jCheckBox4.setSelected(audio.gameOver);
		}
		return jCheckBox4;
	}
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getGameOverLabel() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Game over:");
			jLabel4.setPreferredSize(new Dimension(80,15));
			jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel4;
	}
	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getGameOverTextField() {
		if(jTextField2 == null) {
			jTextField2 = new javax.swing.JTextField();
			jTextField2.setPreferredSize(new Dimension(270,21));
			jTextField2.setText(audio.getGameOverAudioPath());
		}
		return jTextField2;
	}
	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChooseGameOverButton() {
		if(jButton2 == null) {
			jButton2 = new javax.swing.JButton();
			jButton2.setText("...");
			jButton2.setActionCommand("ChooseGameOver");
			jButton2.addActionListener(this);
		}
		return jButton2;
	}
	/**
	 * This method initializes jPanel8
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel8() {
		if(jPanel8 == null) {
			jPanel8 = new javax.swing.JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			jPanel8.setLayout(flow);
			jPanel8.add(getHighscoreCheckBox(), null);
			jPanel8.add(getHighscoreLabel(), null);
			jPanel8.add(getHighscoreTextField(), null);
			jPanel8.add(getChooseHighscoreButton(), null);
			jPanel8.add(getPlayHighscoreButton(), null);
			jPanel8.setPreferredSize(new Dimension(480,30));
		}
		return jPanel8;
	}
	/**
	 * This method initializes jCheckBox5
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getHighscoreCheckBox() {
		if(jCheckBox5 == null) {
			jCheckBox5 = new javax.swing.JCheckBox();
			jCheckBox5.setActionCommand("HighscoreCB");
			jCheckBox5.addActionListener(this);
			jCheckBox5.setSelected(audio.highscore);
		}
		return jCheckBox5;
	}
	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getHighscoreLabel() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Highscore:");
			jLabel5.setPreferredSize(new Dimension(80,15));
			jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel5;
	}
	/**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getHighscoreTextField() {
		if(jTextField3 == null) {
			jTextField3 = new javax.swing.JTextField();
			jTextField3.setPreferredSize(new Dimension(270,21));
			jTextField3.setText(audio.getHighscoreAudioPath());
		}
		return jTextField3;
	}
	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChooseHighscoreButton() {
		if(jButton3 == null) {
			jButton3 = new javax.swing.JButton();
			jButton3.setText("...");
			jButton3.setActionCommand("ChooseHighscore");
			jButton3.addActionListener(this);
		}
		return jButton3;
	}
	/**
	 * This method initializes jPanel9
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel9() {
		if(jPanel9 == null) {
			jPanel9 = new javax.swing.JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.RIGHT, 2, 2);
			jPanel9.setLayout(flow);
			jPanel9.add(getPenaltyCheckBox(), null);
			jPanel9.add(getPenaltyLabel(), null);
			jPanel9.add(getPenaltyTextField(), null);
			jPanel9.add(getChoosePenaltyButton(), null);
			jPanel9.add(getPlayPenaltyButton(), null);
			jPanel9.setPreferredSize(new Dimension(480,30));
		}
		return jPanel9;
	}
	/**
	 * This method initializes jCheckBox6
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getPenaltyCheckBox() {
		if(jCheckBox6 == null) {
			jCheckBox6 = new javax.swing.JCheckBox();
			jCheckBox6.setActionCommand("PenaltyCB");
			jCheckBox6.addActionListener(this);
			jCheckBox6.setSelected(audio.penalty);
		}
		return jCheckBox6;
	}
	/**
	 * This method initializes jLabel6
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getPenaltyLabel() {
		if(jLabel6 == null) {
			jLabel6 = new javax.swing.JLabel();
			jLabel6.setText("Penalty lines:");
			jLabel6.setPreferredSize(new Dimension(80,15));
			jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel6;
	}
	/**
	 * This method initializes jTextField4
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getPenaltyTextField() {
		if(jTextField4 == null) {
			jTextField4 = new javax.swing.JTextField();
			jTextField4.setPreferredSize(new Dimension(270,21));
			jTextField4.setText(audio.getPenaltyAudioPath());
		}
		return jTextField4;
	}
	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChoosePenaltyButton() {
		if(jButton4 == null) {
			jButton4 = new javax.swing.JButton();
			jButton4.setText("...");
			jButton4.setActionCommand("ChoosePenalty");
			jButton4.addActionListener(this);
		}
		return jButton4;
	}
	/**
	 * This method initializes jPanel10
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel10() {
		if(jPanel10 == null) {
			jPanel10 = new javax.swing.JPanel();
			FlowLayout layFlowLayout10 = new FlowLayout(FlowLayout.CENTER, 2, 2);
			jPanel10.setLayout(layFlowLayout10);
			jPanel10.add(getLoopCheckBox(), null);
			jPanel10.add(getVolumeLabel(), null);
			jPanel10.add(getVolumeControlSlider(), null);
			jPanel10.setPreferredSize(new Dimension(480,48));
		}
		return jPanel10;
	}
	/**
	 * This method initializes jTextField5
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getBackgroundTextField() {
		if(jTextField5 == null) {
			jTextField5 = new javax.swing.JTextField();
			jTextField5.setPreferredSize(new Dimension(270,21));
			jTextField5.setText(audio.getBackgroundAudioPath());
		}
		return jTextField5;
	}
	/**
	 * This method initializes jButton5
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getChooseBackgroundButton() {
		if(jButton5 == null) {
			jButton5 = new javax.swing.JButton();
			jButton5.setText("...");
			jButton5.setActionCommand("ChooseBackground");
			jButton5.addActionListener(this);
		}
		return jButton5;
	}
	/**
	 * This method initializes jCheckBox7
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getLoopCheckBox() {
		if(jCheckBox7 == null) {
			jCheckBox7 = new javax.swing.JCheckBox();
			jCheckBox7.setToolTipText("Continuously play the background music");
			jCheckBox7.setText(" Loop");
			jCheckBox7.setSelected(audio.loop);
		}
		return jCheckBox7;
	}
	/**
	 * This method initializes jLabel7
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getVolumeLabel() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText("      Volume:");
			jLabel7.setPreferredSize(new Dimension(80,15));
			jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return jLabel7;
	}
	/**
	 * This method initializes jSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private javax.swing.JSlider getVolumeControlSlider() {
		if(jSlider == null) {
			jSlider = new javax.swing.JSlider();
			jSlider.setSnapToTicks(true);
			jSlider.setPaintTicks(true);
			jSlider.setPaintLabels(true);
			jSlider.setName("volumeControl");
			jSlider.setMajorTickSpacing(20);
			jSlider.setMinorTickSpacing(5);
			jSlider.setToolTipText("Volume for background music");
			jSlider.setPreferredSize(new Dimension(250,47));
			jSlider.setValue(audio.volume);
		}
		return jSlider;
	}
	/**
	 * This method initializes jPanel11
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel11() {
		if(jPanel11 == null) {
			jPanel11 = new javax.swing.JPanel();
			java.awt.FlowLayout layFlowLayout11 = new java.awt.FlowLayout();
			layFlowLayout11.setVgap(1);
			jPanel11.setLayout(layFlowLayout11);
			jPanel11.add(getOKButton(), null);
			jPanel11.add(getCancelButton(), null);
			jPanel11.setPreferredSize(new Dimension(440,27));
		}
		return jPanel11;
	}
	/**
	 * This method initializes jButton6
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getOKButton() {
		if(jButton6 == null) {
			jButton6 = new javax.swing.JButton();
			jButton6.setText("OK");
			jButton6.setActionCommand("OK");
			jButton6.setMnemonic(KeyEvent.VK_ENTER);
			jButton6.addActionListener(this);
		}
		return jButton6;
	}
	/**
	 * This method initializes jButton7
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getCancelButton() {
		if(jButton7 == null) {
			jButton7 = new javax.swing.JButton();
			jButton7.setText("Cancel");
			jButton7.setActionCommand("Cancel");
			jButton7.setMnemonic(KeyEvent.VK_ESCAPE);
			jButton7.addActionListener(this);
		}
		return jButton7;
	}
	/**
	 * Gets the play tetris sound button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayTetrisButton() {
		if (jRadioButton == null) {
			jRadioButton = new javax.swing.JRadioButton(playIcon);
			jRadioButton.setRolloverEnabled(true);
			jRadioButton.setRolloverIcon(playIconOver);
			jRadioButton.setRolloverSelectedIcon(stopIconOver);
			jRadioButton.setSelectedIcon(stopIcon);
			jRadioButton.setDisabledIcon(playIconDisabled);
			jRadioButton.addActionListener(this);
			jRadioButton.setActionCommand("PlayTetris");
			jRadioButton.setCursor(CURSOR_HAND);
		}
		return jRadioButton;	
	}
	/**
	 * Gets the play level up sound button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayLevelUpButton() {
		if (jRadioButton1 == null) {
			jRadioButton1 = new javax.swing.JRadioButton(playIcon);
			jRadioButton1.setRolloverEnabled(true);
			jRadioButton1.setRolloverIcon(playIconOver);
			jRadioButton1.setRolloverSelectedIcon(stopIconOver);
			jRadioButton1.setSelectedIcon(stopIcon);
			jRadioButton1.setDisabledIcon(playIconDisabled);
			jRadioButton1.addActionListener(this);
			jRadioButton1.setActionCommand("PlayLevelUp");
			jRadioButton1.setCursor(CURSOR_HAND);
		}
		return jRadioButton1;	
	}
	/**
	 * Gets the play game over sound button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayGameOverButton() {
		if (jRadioButton2 == null) {
			jRadioButton2 = new javax.swing.JRadioButton(playIcon);
			jRadioButton2.setRolloverEnabled(true);
			jRadioButton2.setRolloverIcon(playIconOver);
			jRadioButton2.setRolloverSelectedIcon(stopIconOver);
			jRadioButton2.setSelectedIcon(stopIcon);
			jRadioButton2.setDisabledIcon(playIconDisabled);
			jRadioButton2.addActionListener(this);
			jRadioButton2.setActionCommand("PlayGameOver");
			jRadioButton2.setCursor(CURSOR_HAND);
		}
		return jRadioButton2;	
	}
	/**
	 * Gets the play tetris sound button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayHighscoreButton() {
		if (jRadioButton3 == null) {
			jRadioButton3 = new javax.swing.JRadioButton(playIcon);
			jRadioButton3.setRolloverEnabled(true);
			jRadioButton3.setRolloverIcon(playIconOver);
			jRadioButton3.setRolloverSelectedIcon(stopIconOver);
			jRadioButton3.setSelectedIcon(stopIcon);
			jRadioButton3.setDisabledIcon(playIconDisabled);
			jRadioButton3.addActionListener(this);
			jRadioButton3.setActionCommand("PlayHighscore");
			jRadioButton3.setCursor(CURSOR_HAND);
		}
		return jRadioButton3;	
	}
	/**
	 * Gets the play tetris sound button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayPenaltyButton() {
		if (jRadioButton4 == null) {
			jRadioButton4 = new javax.swing.JRadioButton(playIcon);
			jRadioButton4.setRolloverEnabled(true);
			jRadioButton4.setRolloverIcon(playIconOver);
			jRadioButton4.setRolloverSelectedIcon(stopIconOver);
			jRadioButton4.setSelectedIcon(stopIcon);
			jRadioButton4.setDisabledIcon(playIconDisabled);
			jRadioButton4.addActionListener(this);
			jRadioButton4.setActionCommand("PlayPenalty");
			jRadioButton4.setCursor(CURSOR_HAND);
		}
		return jRadioButton4;	
	}
	/**
	 * Gets the play background music button.
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPlayBackgroundButton() {
		if (jRadioButton5 == null) {
			jRadioButton5 = new javax.swing.JRadioButton(playIcon);
			jRadioButton5.setRolloverEnabled(true);
			jRadioButton5.setRolloverIcon(playIconOver);
			jRadioButton5.setRolloverSelectedIcon(stopIconOver);
			jRadioButton5.setSelectedIcon(stopIcon);
			jRadioButton5.setDisabledIcon(playIconDisabled);
			jRadioButton5.addActionListener(this);
			jRadioButton5.setActionCommand("PlayBackground");
			jRadioButton5.setCursor(CURSOR_HAND);
		}
		return jRadioButton5;	
	}

}
