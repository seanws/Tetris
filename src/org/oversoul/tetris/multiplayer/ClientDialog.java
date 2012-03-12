/**
 * ClientDialog.java
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
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.oversoul.networking.Client;
import org.oversoul.networking.NetworkApplication;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.ui.WindowClosingDisposer;

/**
 * A dialog for connecting to a server at an address and port number.
 * 
 * @author Chris Callendar (9902588)
 * @date   9-Mar-04, 2:34:30 PM
 */
public class ClientDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1;

	private TetrisPlayer player = null;
	private NetworkApplication netApp = null;
	private Client client = null;

	private JPanel rootContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField jTextField = null;
	private JTextField jTextField1 = null;
	private JTextField jTextField2 = null;
     
	/**
	 * Constructor for ClientDialog.
	 * @param parent
	 * @param player
	 * @param netApp
	 * @throws HeadlessException
	 */
	public ClientDialog(Frame parent, 
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
		done();
		super.dispose();
	}

	/**
	 * Saves the default values and hides the dialog.
	 */
	public void done() {
		player.setHostAddress(getHostAddressTextField().getText());
		try {
			player.setClientPort(Integer.parseInt(getPortTextField().getText()));
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
			stopConnect("");
			dispose();	
		} else if (action.equals("Connect")) {
			connect();
		} else if (action.equals("Stop")) {
			stopConnect("");	
		}
	}

	/**
	 * Tries to connect to the given host.
	 */
	private void connect() {
		String address = getHostAddressTextField().getText().trim();
		if (address.length() == 0) {
			address = "localhost";
		}
		
		player.setHostAddress(address);
		int port = player.getClientPort();
		try {
			port = Integer.parseInt(getPortTextField().getText().trim());
			player.setClientPort(port);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Please enter the destination port.", "Error", JOptionPane.ERROR_MESSAGE);
			getPortTextField().requestFocus();
			return;
		}
		if (address.length() > 0) {
			String msg = address + ":" + port;
			try {
				connecting(msg);
				client = new Client(address, port, netApp);
				if (!client.failedToConnect()) {
					connected(msg);
					Thread.sleep(100);	// let the client startup
					done();	
				} else {
					stopConnect(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				stopConnect(msg);
			}
		}
	}

	/**
	 * Method connected.
	 * @param msg	The message to display.
	 */
	private void connected(String msg) {
		updateStatus("Connected to '" + msg + "'.");
		getConnectButton().setText("Disconnect");
		getConnectButton().setActionCommand("Stop");
		paintAll(getGraphics());
	}


	/**
	 * Sets connecting text values.
	 * @param msg	The message to display.
	 */
	private void connecting(String msg) {
		updateStatus("Connecting to '" + msg + "'...");
		getConnectButton().setText("Stop");
		getConnectButton().setActionCommand("Stop");
		paintAll(getGraphics());
	}


	/**
	 * Stops the connection.
	 * @param msg	The message to display.
	 */
	private void stopConnect(String msg) {
		if (client != null) {
			client.shutdown();
			client = null;	
		}
		String s = "Failed to connect";
		if (msg.length() > 0) 
			s += " to '" + msg + "'";
		s += ".";
		updateStatus(s);
		getConnectButton().setText("Connect To Server");
		getConnectButton().setActionCommand("Connect");
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
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getRootContentPane());
		this.setTitle("Tetris Client");
		this.addWindowListener(new WindowClosingDisposer(this));
		this.setSize(298, 200);
		if (getParent() != null) {
			this.setLocation(getParent().getX()+120, getParent().getY()+80);
		} else {
			this.setLocation(500,100);
		}
		this.setModal(true);
		this.setResizable(false);
		this.pack();
		getConnectButton().requestFocusInWindow();
		this.setVisible(true);
	}

	/**
	 * This method initializes rootContentPane
	 * @return JPanel
	 */
	private JPanel getRootContentPane() {
		if(rootContentPane == null) {
			rootContentPane = new JPanel();
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 3, 3);
			rootContentPane.setLayout(flow);
			rootContentPane.add(getJPanel(), null);
			rootContentPane.add(getJPanel1(), null);
			rootContentPane.setPreferredSize(new Dimension(290, 175));
		}
		return rootContentPane;
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
			jPanel.add(getJPanel3(), null);
			jPanel.add(getJPanel4(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel5(), null);
			jPanel.setPreferredSize(new Dimension(285,140));
			jPanel.setBorder(player.getPanel().getPanelBorder("Connection Settings"));
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
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 1);
			jPanel1.setLayout(flow);
			jPanel1.add(getConnectButton(), null);
			jPanel1.add(getCancelButton(), null);
			jPanel1.setPreferredSize(new Dimension(285,35));
		}
		return jPanel1;
	}
	/**
	 * This method initializes jButton
	 * @return JButton
	 */
	private JButton getCancelButton() {
		if(jButton == null) {
			jButton = new JButton();
			jButton.setText("Cancel");
			jButton.setActionCommand("Cancel");
			jButton.addActionListener(this);
			jButton.setMnemonic(KeyEvent.VK_C);
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1
	 * @return JButton
	 */
	private JButton getConnectButton() {
		if(jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Connect To Server");
			jButton1.setActionCommand("Connect");
			jButton1.addActionListener(this);
			jButton1.setMnemonic(KeyEvent.VK_S);
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
			jPanel2.setToolTipText("The current tetris player");
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
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 2);
			jPanel3.setLayout(flow);
			jPanel3.add(getJLabel1(), null);
			jPanel3.add(getHostAddressTextField(), null);
			jPanel3.setPreferredSize(new Dimension(280,28));
			jPanel3.setToolTipText("Enter the host IP address or host name");
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
			FlowLayout flow = new FlowLayout(FlowLayout.LEFT, 2, 2);
			jPanel4.setLayout(flow);
			jPanel4.add(getJLabel2(), null);
			jPanel4.add(getPortTextField(), null);
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
			FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 2, 2);
			jPanel5.setLayout(flow);
			jPanel5.add(getJLabel3(), null);
			jPanel5.setPreferredSize(new Dimension(275,25));
			jPanel5.setToolTipText("Connection status");
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
			jLabel.setPreferredSize(new Dimension(100,15));
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 * @return JLabel
	 */
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel("Host Address:  ");
			jLabel1.setPreferredSize(new Dimension(100,15));
			jLabel1.setToolTipText("The IP address of the host machine, or the domain name");
		}
		return jLabel1;
	}
	/**
	 * This method initializes jLabel2
	 * @return JLabel
	 */
	private JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new JLabel("Host Port:  ");
			jLabel2.setPreferredSize(new Dimension(100,15));
			jLabel2.setToolTipText("The port number that the tetris server is running on (1-65535)");
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
			jLabel3.setPreferredSize(new Dimension(268,15));
			
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
	private JTextField getHostAddressTextField() {
		if(jTextField1 == null) {
			jTextField1 = new JTextField(player.getHostAddress());
			jTextField1.setPreferredSize(new Dimension(160,20));
		}
		return jTextField1;
	}
	/**
	 * This method initializes jTextField2
	 * @return JTextField
	 */
	private JTextField getPortTextField() {
		if(jTextField2 == null) {
			jTextField2 = new JTextField(""+player.getClientPort());
			jTextField2.setPreferredSize(new Dimension(50,20));
		}
		return jTextField2;
	}
	
	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the client.
	 * @return Client
	 */
	public Client getClient() {
		return client;
	}

}
