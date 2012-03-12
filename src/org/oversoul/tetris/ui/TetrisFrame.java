/**
 * TetrisFrame.java
 * 
 * Created on 25-Oct-2005
 */
package org.oversoul.tetris.ui;

import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisController;
import org.oversoul.tetris.util.Util;

public class TetrisFrame extends JFrame {

	private static final long serialVersionUID = 1;
	
	private TetrisRootPane rootPane = null;
	private IUIController controller = null;
	private TetrisActionListener listener = null;
	
	/**
	 * Main entry point.
	 * @param args command line args.
	 */
	public static void main(String[] args) {
		new TetrisFrame();
	}	
	
	public TetrisFrame() throws HeadlessException {
		super();
		this.rootPane = new TetrisRootPane(false);
		this.controller = new TetrisController(this, rootPane, getWorkingDirectory());
		this.listener = new TetrisActionListener(controller, this, rootPane);
		
		rootPane.setActionListener(listener);
		rootPane.setGameComponents(controller.getGameComponent(), controller.getPreviewComponent());
		rootPane.initialize();
		initialize();
		
		controller.initializeUI();		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		// kill sound threads!
		System.exit(0);	
	}
	
	public void initialize() {
		this.setContentPane(rootPane);
		this.setSize(560, 562);
		this.setLocation(controller.getPlayer().getLocation());
		this.addComponentListener(listener);
		this.addWindowFocusListener(listener);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Tetris v" + controller.getVersion().toString());
		this.setIconImage(Util.getImageResource("/images/tetris16.gif"));

		this.pack();
		this.setVisible(true);
		
		if (controller.getPlayer().isShowBlockDistribution()) {
			rootPane.displayBlockDistributionDialog(controller.getPlayer().getColors(), controller.getBlockCounts());
		}
	}
	
	private File getWorkingDirectory() {
		File file = null;
		try {
			file = Util.findWorkingDirectory();
			System.out.println("TetrisFrame working directory: " + file.getCanonicalPath());
		} catch (Exception ex) {
			System.err.println("Error finding working directory: " + ex.getMessage());
		}
		return file;
	}

}
