/**
 * ServerDialog.java
 * 
 * Created on 9-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.oversoul.networking.NetworkApplication;
import org.oversoul.networking.Server;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.ui.WindowClosingDisposer;

/**
 * Dialog for starting up a server on a specified port.
 * 
 * @author Chris Callendar (9902588)
 * @date   9-Mar-04, 2:34:30 PM
 */
public class ServerDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1;

	private TetrisPlayer player = null;
	private NetworkApplication netApp = null;
	private Server server = null;

    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    private JButton jButton = null;
    private JButton jButton1 = null;
    private JPanel jPanel2 = null;
    private JPanel jPanel4 = null;
    private JPanel jPanel5 = null;
    private JLabel jLabel = null;
    private JLabel jLabel2 = null;
    private JLabel jLabel3 = null;
    private JTextField jTextField = null;
    private JTextField jTextField1 = null;
	
	/**
	 * Constructor for ServerDialog.
	 * @param parent
	 * @param player
	 * @param netApp
	 * @throws HeadlessException
	 */
	public ServerDialog(Frame parent, 
						TetrisPlayer player,
						NetworkApplication netApp) throws HeadlessException {
		super(parent);
		this.player = player;
		this.netApp = netApp;
		initialize();
	}

	/**
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		try {
			player.setHostPort(Integer.parseInt(getHostPortTextField().getText()));
		} catch (NumberFormatException nfe) {}
		super.dispose();
	}

	/**
	 * Saves the default values and hides the dialog.
	 */
	public void done() {
		try {
			player.setHostPort(Integer.parseInt(getHostPortTextField().getText()));
		} catch (NumberFormatException nfe) {}
		setVisible(false);
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
			stopServer("");
			dispose();	
		} else if (action.equals("Start")) {
			startServer();
		} else if (action.equals("Stop")) {
			stopServer("");
		}
	}

	/**
	 * Starts the server.
	 */
	private void startServer() {
		int port = player.getHostPort();
		try {
			port = Integer.parseInt(getHostPortTextField().getText());
			player.setHostPort(port);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Please enter the port to listen on.", "Error", JOptionPane.ERROR_MESSAGE);
			getHostPortTextField().requestFocus();
			return;
		}
		
		JButton button = getStartButton();
		button.setText("Stop Server");
		button.setActionCommand("Stop");
		updateStatus("Starting server on port " + port + ".");
		try {
			server = new Server(port, netApp);
			updateStatus("Server started on port " + port + ".");
			server.start();
			paintAll(getGraphics());
			Thread.sleep(100);	// pause to let server start
		} catch (Exception e) {
			e.printStackTrace();
			stopServer(e.getMessage());
		}
		if ((server != null) && server.isConnected()) {
			done();	
		}
	}


	/**
	 * Stops the server.
	 * @param msg	Message to display.
	 */
	private void stopServer(String msg) {
		if ((server != null) && server.isRunning()) {
			server.shutdown();
			server = null;	
		}
		JButton button = getStartButton();
		button.setText("Start Server");
		button.setActionCommand("Start");
		String s = "Server stopped";
		if (msg.length() > 0) {
			s += ": " + msg;	
		}
		s +=  ".";
		updateStatus(s);
	}


	/**
	 * Updates the status message.
	 * @param msg	The status message to display.
	 */
	private void updateStatus(String msg) {
		JLabel label = getJLabel3();
		label.setText(msg);
		label.repaint();
	}

	//////////////////////////////////////////////////////////////////////
	// GUI Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * This method initializes this dialog.
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getRootContentPane());
		this.setTitle("Tetris Server");
		this.addWindowListener(new WindowClosingDisposer(this));
		this.setSize(298, 180);
		if (getParent() != null) {
			this.setLocation(getParent().getX()+120, getParent().getY()+80);
		} else {
			this.setLocation(100,100);
		}
		this.setModal(true);
		this.setResizable(false);
		this.pack();
		this.getStartButton().requestFocusInWindow();
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * @return JPanel
	 */
	private JPanel getRootContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 3, 3);
			jContentPane.setLayout(flow);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel1(), null);
			jContentPane.setPreferredSize(new Dimension(290, 160));
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel
	 * @return JPanel
	 */
	private JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 0);
			jPanel.setLayout(flow);
			jPanel.add(getJPanel4(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel5(), null);
			jPanel.setPreferredSize(new Dimension(285,115));
			jPanel.setBorder(player.getPanel().getPanelBorder("Server Settings"));
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
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 1, 1);
			jPanel1.setLayout(flow);
			jPanel1.add(getStartButton(), null);
			jPanel1.add(getJButton(), null);
			jPanel1.setPreferredSize(new Dimension(290,35));
		}
		return jPanel1;
	}
	/**
	 * This method initializes jButton
	 * @return JButton
	 */
	private JButton getJButton() {
		if(jButton == null) {
			jButton = new JButton();
			jButton.setText("Cancel");
			jButton.setActionCommand("Cancel");
			jButton.addActionListener(this);
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
			jButton1 = new JButton();
			jButton1.setText("Start Server");
			jButton1.setActionCommand("Start");
			jButton1.addActionListener(this);
			jButton1.setMnemonic(java.awt.event.KeyEvent.VK_S);
		}
		return jButton1;
	}
	/**
	 * This method initializes jPanel2
	 * @return JPanel
	 */
	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 2);
			jPanel2.setLayout(flow);
			jPanel2.add(getJLabel(), null);
			jPanel2.add(getNameTextField(), null);
			jPanel2.setPreferredSize(new Dimension(280,28));
			jPanel2.setToolTipText("Current tetris player");
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel4
	 * @return JPanel
	 */
	private JPanel getJPanel4() {
		if(jPanel4 == null) {
			jPanel4 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 2);
			jPanel4.setLayout(flow);
			jPanel4.add(getJLabel2(), null);
			jPanel4.add(getHostPortTextField(), null);
			jPanel4.setPreferredSize(new Dimension(280,28));
			jPanel4.setToolTipText("Enter a port number between 1-65535");
		}
		return jPanel4;
	}
	/**
	 * Method getJPanel5.
	 * @return Component
	 */
	private Component getJPanel5() {
		if(jPanel5 == null) {
			jPanel5 = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 2);
			jPanel5.setLayout(flow);
			jPanel5.add(getJLabel3(), null);
			jPanel5.setPreferredSize(new Dimension(275,25));
			jPanel5.setToolTipText("Server status");
			jPanel5.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		return jPanel5;
	}
	/**
	 * This method initializes jLabel
	 * @return JLabel
	 */
	private JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new JLabel("Current player:  ");
			jLabel.setPreferredSize(new Dimension(80,15));
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel2
	 * @return JLabel
	 */
	private JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new JLabel("Host Port:  ");
			jLabel2.setPreferredSize(new Dimension(80,15));
			jLabel2.setToolTipText("");
		}
		return jLabel2;
	}
	/**
	 * This method initializes jLabel3
	 * @return JLabel
	 */
	private JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new JLabel("");
			jLabel3.setPreferredSize(new Dimension(268, 15));
			
		}
		return jLabel3;
	}
	/**
	 * This method initializes jTextField
	 * @return JTextField
	 */
	private JTextField getNameTextField() {
		if(jTextField == null) {
			jTextField = new JTextField(player.getName());
			jTextField.setPreferredSize(new Dimension(160,20));
			jTextField.setEnabled(false);
		}
		return jTextField;
	}
	/**
	 * This method initializes jTextField1
	 * @return JTextField
	 */
	private JTextField getHostPortTextField() {
		if(jTextField1 == null) {
			jTextField1 = new JTextField(""+player.getHostPort());
			jTextField1.setPreferredSize(new Dimension(50,20));
		}
		return jTextField1;
	}
	
	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the server.
	 * @return Server
	 */
	public Server getServer() {
		return server;
	}

}
