/**
 * TetrisApplet.java
 * 
 * Created on 20-Oct-2005
 */
package org.oversoul.tetris.applet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.oversoul.border.PanelBorder;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisController;
import org.oversoul.tetris.ui.TetrisActionListener;
import org.oversoul.tetris.ui.TetrisRootPane;
import org.oversoul.tetris.util.Util;

public class TetrisApplet extends JApplet {

	private static final long serialVersionUID = 1;

	private TetrisRootPane rootPane = null;
	private IUIController controller = null;
	private TetrisActionListener listener = null;
	
	//private TetrisController controller = null;
	
	public TetrisApplet() throws HeadlessException {
		super();
	}

	/**
	 * Creates the UI and initializes the game.
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		System.out.println("Initializing Tetris...");
	    //Execute a job on the event-dispatching thread
	    try {
	        SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createUI();
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("Error creating UI");
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void start() {
		System.out.println("Starting Tetris...");
		this.setVisible(true);
	}
	
	@Override
	public void stop() {
		System.out.println("Stopping Tetris...");
		if (controller != null) {
			controller.dispose();
			controller = null;
		}
	}
	
	/**
	 * Creates the tetris GameFrame.
	 */
	private void createUI() {
		this.rootPane = new TetrisRootPane(true);
		this.controller = new TetrisController(this, rootPane, getWorkingDirectory());
		this.listener = new TetrisActionListener(controller, this, rootPane);
		
		rootPane.setActionListener(listener);
		rootPane.setGameComponents(controller.getGameComponent(), controller.getPreviewComponent());
		rootPane.initialize();
		
		PanelBorder border = controller.getPlayer().getPanel().getPanelBorder("Tetris v" + controller.getVersion());
		border.setRoundedCorners(true);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setBackground(getBackgroundColor(border.getLineColor()));
		panel.setBorder(border);
		panel.setPreferredSize(new Dimension(564, 551));
		panel.add(rootPane);
		rootPane.addPanelBorder(border, panel);
		
		this.setContentPane(panel);
		this.setSize(564, 550);
		
		controller.initializeUI();
	}
	
	private File getWorkingDirectory() {
		File file = null;
		try {
			String serverRoot = getParameter("serverRoot", "C:/");
			String filePath = getCodeBase().getFile();
			if (filePath.startsWith("file:/")) {
				filePath = filePath.substring(6);
			}					
			file = new File(filePath);
			if (!file.exists()) {
				file = new File(serverRoot, filePath);
			}
			System.out.println("Applet working directory: " + file.getCanonicalPath());
		} catch (Exception ex) {
			System.err.println("Error finding working directory: " + ex.getMessage());
		}
		return file;
	}
	
	private Color getBackgroundColor(Color def) {
		String s = getParameter("bgcolor");
		Color c = Util.stringToColor(s);
		return (c != null ? c : def);
	}
	
	private String getParameter(String name, String def) {
		String s = getParameter(name);
		return (s != null ? s : def);
	}
}
