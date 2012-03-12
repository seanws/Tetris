/**
 * TetrisRootPane.java
 * 
 * Created on 25-Oct-2005
 */
package org.oversoul.tetris.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;

import org.oversoul.border.PanelBorder;
import org.oversoul.networking.Client;
import org.oversoul.networking.Server;
import org.oversoul.tetris.Actions;
import org.oversoul.tetris.IUIController;
import org.oversoul.tetris.TetrisColors;
import org.oversoul.tetris.TetrisConstants;
import org.oversoul.tetris.TetrisDefaults;
import org.oversoul.tetris.TetrisPanel;
import org.oversoul.tetris.TetrisPlayer;
import org.oversoul.tetris.highscores.Highscores;
import org.oversoul.tetris.highscores.HighscoresDialog;
import org.oversoul.tetris.multiplayer.ClientDialog;
import org.oversoul.tetris.multiplayer.IMultiplayerController;
import org.oversoul.tetris.multiplayer.MultiplayerFrame;
import org.oversoul.tetris.multiplayer.ServerDialog;
import org.oversoul.tetris.multiplayer.TetrisCommunication;
import org.oversoul.tetris.util.Util;

/**
 * This is the root pane that contains all the tetris components.
 * 
 * @author 		ccallendar
 * @date		25-Oct-2005, 6:49:16 PM
 * @version 	1.0
 */
public class TetrisRootPane extends JRootPane {

	private static final long serialVersionUID = 1;
	private static final String EMPTY = "";
	private static final String ZERO = "0";
	public static final String TEXT_START = "Start Game";
	public static final String TEXT_PAUSE = "Pause Game";
	public static final String TEXT_RESUME = "Resume Game";
	private static final String TEXT_RESTART = "Restart Game";
	private static final String TEXT_END = "End Game";
	
	
	private boolean appletMode = false;
	private Frame parentFrame = null;
	private Component gameComponent = null;
	private Component previewComponent = null;
	private TetrisActionListener handler = null;
	private TetrisPanel tetrisPanel = null;
	
	private JFrame blockColorChooser = null;
	private JFrame panelColorChooser = null;
	private MultiplayerFrame mpFrame = null;
	private BlockDistributionDialog blockDistributionDialog = null;
	private boolean blockDistributionDialogShown = false;
	
	private Hashtable<PanelBorder, JPanel> panelBorders = null;
	private Hashtable<String, JMenuItem> lnfMenuItems = null;
	
	private Vector<JMenuItem> startingLevelMenus = null;
	private Vector<JMenuItem> startingLinesMenus = null;
	private Vector<JMenuItem> gameSpeedMenus = null;
	
	private File lastImportDir = null;
	
	private ScorePanel scorePanel = null;
	private ScorePanel opponentScorePanel = null;
	private StatsPanel statsPanel = null;
	private StatsPanel opponentStatsPanel = null;
	
	private JFileChooser importChooser = null;
	private JMenuItem blockDistMenuItem = null;
	private JLabel messageLabel1 = null;
	private JLabel messageLabel2 = null;
	private JButton startButton = null;
	private JSlider jSlider = null;
	private JSlider jSlider1 = null;	
	
	private Color bgColor = null;
	private Color labelTextColor = null;
	
	/**
	 * Constructor for TetrisRootPane.  Does not call initialize(), 
	 * it must be called for the sub components to be loaded.
	 * @param mode The mode
	 */
	public TetrisRootPane(boolean appletMode) {
		super();
		this.appletMode = appletMode;
		this.tetrisPanel = new TetrisPanel();
		
		this.panelBorders = new Hashtable<PanelBorder, JPanel>();
		this.lnfMenuItems = new Hashtable<String, JMenuItem>();
		this.startingLevelMenus = new Vector<JMenuItem>();
		this.startingLinesMenus = new Vector<JMenuItem>();
		this.gameSpeedMenus = new Vector<JMenuItem>();
		
		this.bgColor = new Color(240, 240, 235);
		this.labelTextColor = new Color(0, 0, 32);
	}
	
	////////////////////////////////////////////////
	// Event Handler Methods
	////////////////////////////////////////////////

	
	/**
	 * Disposes the dialogs.
	 */
	public void dispose() {
		if (blockColorChooser != null) {
			blockColorChooser.dispose();
			blockColorChooser = null;
		}
		if (panelColorChooser != null) {
			panelColorChooser.dispose();
			panelColorChooser = null;
		}
		if (mpFrame != null) {
			mpFrame.dispose();
			mpFrame = null;
		}		
	}

	/**
	 * Displays an error dialog (JOptionPane).
	 * @param e	Exception
	 */
	public void error(Exception e) {
		error("Exception: ", e);
	}

	/**
	 * Displays an error dialog (JOptionPane).
	 * @param text text to prefix the exception message
	 * @param e	Exception
	 */
	public void error(String text, Exception e) {
		JOptionPane.showMessageDialog(this, text + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays a message dialog (JOptionPane).
	 * @param msg	Message to display.
	 */
	public void message(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Enables key events.
	 */
	protected void enableKeyEvents() {
		enableEvents(KeyEvent.KEY_EVENT_MASK);
	}
	
	/**
	 * Disables key events.
	 */
	protected void disableKeyEvents() {
		disableEvents(KeyEvent.KEY_EVENT_MASK);
	}
	
	////////////////////////////////////////////////
	// UI Methods
	////////////////////////////////////////////////

	/**
	 * Initializes this component.  
	 * Should call setActionListener() and setGameComponents() 
	 * before calling this method.
	 */
	public void initialize() {
		setOpaque(true);
		setLayout(new BorderLayout(0, 0));
		setBorder(BorderFactory.createEmptyBorder(0, 1, 1, 1));
		
		createMenuBar(this, BorderLayout.PAGE_START);
		createLeftPanel(this, BorderLayout.LINE_START);
		createMiddlePanel(this, BorderLayout.CENTER);
		createRightPanel(this, BorderLayout.LINE_END);
		createBottomPanel(this, BorderLayout.PAGE_END);
		setPreferredSize(new Dimension(562, 530));
		
		this.setDefaultButton(getStartButton());
		this.setFocusable(true);
		this.enableKeyEvents();
		this.addKeyListener(handler);
		this.requestFocusInWindow();
		
		long start = System.currentTimeMillis();
		setColors(this.getComponents());
		System.out.println("Time: "+ (System.currentTimeMillis() - start));
	}
	
	private void setColors(Component[] components) {
		if (components == null)
			return;
		for (Component comp : components) {
			if ((comp instanceof JMenuBar) || (comp instanceof JMenu) || 
				(comp instanceof JMenuItem) || (comp instanceof JSlider)) {
				comp.setBackground(bgColor);
			} else if (comp instanceof JLabel) {
				comp.setBackground(bgColor);
				comp.setForeground(labelTextColor);				
			} else if (comp instanceof JPanel) {
				JPanel panel = (JPanel)comp;
				comp.setBackground(bgColor);
				if (panel.getBorder() instanceof PanelBorder) {
					((PanelBorder)panel.getBorder()).setInnerBackground(bgColor);
				}
			}
//			if (comp instanceof JMenuItem) {
//				setColors(((JMenuItem)comp).getComponents());
//			} else if (comp instanceof JMenu) {
//				setColors(((JMenu)comp).getMenuComponents());
//			} else 
			if (comp instanceof Container) {
				setColors(((Container)comp).getComponents());
			}
		}
	}
	
	////////////////////////////////////////////////
	// MENU Methods
	////////////////////////////////////////////////
	
	private void createMenuBar(JComponent comp, Object constraint) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(554, 21));
		menuBar.setMinimumSize(new Dimension(554, 21));
		menuBar.setMaximumSize(new Dimension(554, 25));
		createGameMenu(menuBar);
		createMultiplayerMenu(menuBar);
		createOptionsMenu(menuBar);
		createHelpMenu(menuBar);

		if (TetrisDefaults.DEBUG) {
			createDebugMenu(menuBar);
		}
		
		comp.add(menuBar, constraint);
	}

	private void createGameMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.addMenuListener(handler);

		JMenuItem menuItem = new JMenuItem("Start New Game", KeyEvent.VK_S);
		menuItem.setActionCommand(Actions.START.name());
		menuItem.addActionListener(handler);
		menu.add(menuItem);
		menu.add(new JSeparator());
		
		if (!appletMode) {
			menuItem = new JMenuItem("Import Tetris Properties...", KeyEvent.VK_I);
			menuItem.setActionCommand(Actions.FILE_IMPORT.name());
			menuItem.addActionListener(handler);
			menu.add(menuItem);
			menu.add(new JSeparator());
		}
		
		menuItem = new JMenuItem("High Scores", KeyEvent.VK_H);
		menuItem.setActionCommand(Actions.HIGHSCORES.name());
		menuItem.addActionListener(handler);
		menu.add(menuItem);
		menu.add(new JSeparator());
		
		if (!appletMode) {
			menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
			menuItem.setActionCommand(Actions.EXIT.name());
			menuItem.addActionListener(handler);
			menu.add(menuItem);
		}
		menuBar.add(menu);
	}

	private void createOptionsMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		
		JMenuItem menuItem = new JMenuItem("Change Player", KeyEvent.VK_P);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.CHANGE_PLAYER.name());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Sounds", KeyEvent.VK_S);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.SOUNDS.name());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Controls", KeyEvent.VK_C);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.CONTROLS.name());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Colors", KeyEvent.VK_O);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.COLORS.name());
		menu.add(menuItem);
		
		if (!appletMode) {
			menu.add(getBlockDistributionMenuItem());
		}
		menu.add(new JSeparator());
		
		createStartingLevelMenu(menu);
		createStartingLinesMenu(menu);
		createGameSpeedMenu(menu);
		menu.add(new JSeparator());
		
		JMenu subMenu = new JMenu("Choose the Look and Feel...");
		subMenu.setMnemonic(KeyEvent.VK_F);
		menu.add(subMenu);
		subMenu.add(createLookAndFeelMenuItem("Metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
		subMenu.add(createLookAndFeelMenuItem("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
		subMenu.add(createLookAndFeelMenuItem("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
		//subMenu.add(createLookAndFeelMenuItem("PlasticWindows", "com.jgoodies.looks.windows.WindowsLookAndFeel"));
		subMenu.add(createLookAndFeelMenuItem("Plastic", "com.jgoodies.looks.plastic.PlasticLookAndFeel"));
		//subMenu.add(createLookAndFeelMenuItem("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"));
		//subMenu.add(createLookAndFeelMenuItem("Plastic XP", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel"));
		subMenu.add(createLookAndFeelMenuItem("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
		
		menu.addMenuListener(handler);
		menuBar.add(menu);
	}
	
	private JMenuItem getBlockDistributionMenuItem() {
		if (blockDistMenuItem == null) {
			blockDistMenuItem = new JCheckBoxMenuItem("Block Distribution", false);
			blockDistMenuItem.setMnemonic(KeyEvent.VK_D);
			blockDistMenuItem.addActionListener(handler);
			blockDistMenuItem.setActionCommand(Actions.BLOCK_DISTRIBUTION.name());
		}
		return blockDistMenuItem;
	}
		
	private void createHelpMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem menuItem = new JMenuItem("Tetris Scoring", KeyEvent.VK_S);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.SCORING.name());
		menu.add(menuItem);
		menu.add(new JSeparator());
		
		menuItem = new JMenuItem("About Tetris", KeyEvent.VK_A);
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.ABOUT.name());
		menu.add(menuItem);
		
		menu.addMenuListener(handler);
		menuBar.add(menu);
	}

	private void createStartingLevelMenu(JMenu parentMenu) {
		JMenu menu = new JMenu("Starting Level...");
		menu.setMnemonic(KeyEvent.VK_L);
		String actionCmd = Actions.STARTING_LEVEL.name();
		menu.add(createMenuItem(ZERO, actionCmd, KeyEvent.VK_0, startingLevelMenus));
		menu.add(createMenuItem("1", actionCmd, KeyEvent.VK_1, startingLevelMenus));
		menu.add(createMenuItem("2", actionCmd, KeyEvent.VK_2, startingLevelMenus));
		menu.add(createMenuItem("3", actionCmd, KeyEvent.VK_3, startingLevelMenus));
		menu.add(createMenuItem("4", actionCmd, KeyEvent.VK_4, startingLevelMenus));
		menu.add(createMenuItem("5", actionCmd, KeyEvent.VK_5, startingLevelMenus));
		menu.add(createMenuItem("6", actionCmd, KeyEvent.VK_6, startingLevelMenus));
		menu.add(createMenuItem("7", actionCmd, KeyEvent.VK_7, startingLevelMenus));
		menu.add(createMenuItem("8", actionCmd, KeyEvent.VK_8, startingLevelMenus));
		menu.add(createMenuItem("9", actionCmd, KeyEvent.VK_9, startingLevelMenus));
		menu.add(createMenuItem("10", actionCmd, KeyEvent.VK_UNDEFINED, startingLevelMenus));
		parentMenu.add(menu);
	}

	private void createStartingLinesMenu(JMenu parentMenu) {
		JMenu menu = new JMenu("Starting Lines...");
		//menu.setMnemonic(KeyEvent.VK_I);
		String actionCmd = Actions.STARTING_LINES.name();
		menu.add(createMenuItem(ZERO, actionCmd, KeyEvent.VK_0, startingLinesMenus));
		menu.add(createMenuItem("1", actionCmd, KeyEvent.VK_1, startingLinesMenus));
		menu.add(createMenuItem("2", actionCmd, KeyEvent.VK_2, startingLinesMenus));
		menu.add(createMenuItem("3", actionCmd, KeyEvent.VK_3, startingLinesMenus));
		menu.add(createMenuItem("4", actionCmd, KeyEvent.VK_4, startingLinesMenus));
		menu.add(createMenuItem("5", actionCmd, KeyEvent.VK_5, startingLinesMenus));
		menu.add(createMenuItem("6", actionCmd, KeyEvent.VK_6, startingLinesMenus));
		menu.add(createMenuItem("7", actionCmd, KeyEvent.VK_7, startingLinesMenus));
		menu.add(createMenuItem("8", actionCmd, KeyEvent.VK_8, startingLinesMenus));
		menu.add(createMenuItem("9", actionCmd, KeyEvent.VK_9, startingLinesMenus));
		menu.add(createMenuItem("10", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		menu.add(createMenuItem("11", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		menu.add(createMenuItem("12", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		menu.add(createMenuItem("13", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		menu.add(createMenuItem("14", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		menu.add(createMenuItem("15", actionCmd, KeyEvent.VK_UNDEFINED, startingLinesMenus));
		parentMenu.add(menu);
	}

	private void createGameSpeedMenu(JMenu parentMenu) {
		JMenu menu = new JMenu("Game Speed...");
		menu.setMnemonic(KeyEvent.VK_G);
		String actionCmd = Actions.GAME_SPEED.name();
		menu.add(createMenuItem(Util.getGameSpeedString(TetrisConstants.SPEED_SLOW), 
				actionCmd, KeyEvent.VK_S, gameSpeedMenus));
		menu.add(createMenuItem(Util.getGameSpeedString(TetrisConstants.SPEED_NORM), 
				actionCmd, KeyEvent.VK_N, gameSpeedMenus));
		menu.add(createMenuItem(Util.getGameSpeedString(TetrisConstants.SPEED_FAST), 
				actionCmd, KeyEvent.VK_F, gameSpeedMenus));
		parentMenu.add(menu);
	}

	private void createMultiplayerMenu(JMenuBar menuBar) {
		if (!appletMode) {
			JMenu menu = new JMenu("Multiplayer");
			menu.setMnemonic(KeyEvent.VK_M);
	
			JMenuItem menuItem = new JMenuItem("Create Game", KeyEvent.VK_C);
			menuItem.addActionListener(handler);
			menuItem.setActionCommand(Actions.MULTIPLAYER_CREATE.name());
			menu.add(menuItem);
	
			menuItem = new JMenuItem("Join Game", KeyEvent.VK_J);
			menuItem.addActionListener(handler);
			menuItem.setActionCommand(Actions.MULTIPLAYER_JOIN.name());
			menu.add(menuItem);
	
			menu.addMenuListener(handler);
			menuBar.add(menu);
		}
	}
	
	private void createDebugMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Debug");
		
		JMenuItem menuItem = new JMenuItem("Load Version");
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.DEBUG_LOAD_VERSION.name());
		menu.add(menuItem);
		menu.add(new JSeparator());
		
		menuItem = new JMenuItem("Load Properties");
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.DEBUG_LOAD_PROPERTIES.name());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save Properties");
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.DEBUG_SAVE_PROPERTIES.name());
		menu.add(menuItem);
		menu.add(new JSeparator());
		
		menuItem = new JMenuItem("Load Highscores");
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.DEBUG_LOAD_HIGHSCORES.name());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save Highscores");
		menuItem.addActionListener(handler);
		menuItem.setActionCommand(Actions.DEBUG_SAVE_HIGHSCORES.name());
		menu.add(menuItem);

		menuBar.add(menu);
	}
	
	/**
	 * This method creates and returns a new menu item.
	 */
	private JMenuItem createMenuItem(String text, String actionCmd, int mnemonic, Vector<JMenuItem> menuList) {
		JMenuItem menuItem = new JMenuItem(text, mnemonic);
		menuItem.setName(text);
		menuItem.setActionCommand(actionCmd);
		menuItem.addActionListener(handler);
		menuList.add(menuItem);
		return menuItem;
	}

	/**
	 * This method creates a new menu item for the given look and feel.
	 * @param menuText	The menu text
	 * @param lnfClassName	The class name of the look and feel
	 * @return JMenuItem
	 */
	private JMenuItem createLookAndFeelMenuItem(String menuText, String lnfClassName) {
		JMenuItem menuItem = new JMenuItem(menuText);
		menuItem.setName(lnfClassName);
		menuItem.addActionListener(handler); 
		menuItem.setActionCommand(Actions.LOOKANDFEEL.name());
		lnfMenuItems.put(lnfClassName, menuItem);
		return menuItem;
	}

	/**
	 * Sets the menu item color to be highlighted or black.
	 * @param on	If the menu item is being highlighted 
	 */
	private void colorMenuItem(JMenuItem menuItem, boolean on) {
		if (menuItem != null) {
			Font font = (on ? TetrisDefaults.FONT_MENU_BOLD : TetrisDefaults.FONT_MENU_PLAIN);
			Color fgcolor = (on ? Color.WHITE : Color.BLACK);
			Color bgcolor = (on ? Color.RED : Color.WHITE);
			menuItem.setFont(font);
			menuItem.setForeground(fgcolor);
			menuItem.setBackground(bgcolor);
		}
	}
	
	/////////////////////////////////////////////////
	// PANELS & OTHER UI METHODS
	/////////////////////////////////////////////////

	private void createLeftPanel(JComponent comp, Object constraint) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		
		JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		subPanel.add(getStatsPanel(), null);

		JPanel btnPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		btnPanel.setPreferredSize(new Dimension(144, 25));
		btnPanel.setMaximumSize(new Dimension(144, 25));

		JButton button = new JButton("Change Player");
		button.setActionCommand(Actions.CHANGE_PLAYER.name());
		button.addActionListener(handler);
		button.setMnemonic(KeyEvent.VK_P);
		btnPanel.add(button, null);

		subPanel.add(btnPanel, null);
		subPanel.setPreferredSize(new Dimension(148,150));
		createPanelBorder(subPanel, "Stats");			
		panel.add(subPanel, null);
		
		JPanel opponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		opponentPanel.add(getOpponentStatsPanel(), null);
		createPanelBorder(opponentPanel, "Opponent's Stats");
		opponentPanel.setPreferredSize(new Dimension(148,120));
		opponentPanel.setVisible(false);
		panel.add(opponentPanel, null);
		
		createButtonsPanel(panel);

		panel.setPreferredSize(new Dimension(150,450));
		comp.add(panel, constraint);
	}

	private void createButtonsPanel(JPanel parent) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		JPanel buttonsPanel = new JPanel(new GridLayout((appletMode ? 3 : 6), 1, 0, 0));
		
		buttonsPanel.add(getStartButton(), null);
		
		JButton button = new JButton(TEXT_RESTART);
		button.setActionCommand(Actions.RESTART.name());
		button.addActionListener(handler);
		button.setMnemonic(java.awt.event.KeyEvent.VK_R);
		buttonsPanel.add(button);
		
		button = new JButton(TEXT_END);
		button.setActionCommand(Actions.END.name());
		button.addActionListener(handler);
		button.setMnemonic(java.awt.event.KeyEvent.VK_E);
		buttonsPanel.add(button);

		if (!appletMode) {
			button = new JButton("Create Game");
			button.setActionCommand(Actions.MULTIPLAYER_CREATE.name());
			button.addActionListener(handler);
			button.setMnemonic(java.awt.event.KeyEvent.VK_C);
			buttonsPanel.add(button);
			
			button = new JButton("Join Game");
			button.setActionCommand(Actions.MULTIPLAYER_JOIN.name());
			button.addActionListener(handler);
			button.setMnemonic(java.awt.event.KeyEvent.VK_J);
			buttonsPanel.add(button);
			
			button = new JButton("Exit Game");
			button.setActionCommand(Actions.EXIT.name());
			button.addActionListener(handler);
			button.setMnemonic(java.awt.event.KeyEvent.VK_X);
			buttonsPanel.add(button);
			buttonsPanel.setPreferredSize(new Dimension(143,156));
			panel.setPreferredSize(new Dimension(148,180));
		} else {
			buttonsPanel.setPreferredSize(new Dimension(143,78));
			panel.setPreferredSize(new Dimension(148,102));
		}
		panel.add(buttonsPanel);
		createPanelBorder(panel, "Game Options");
		parent.add(panel);
	}
	
	private void createMiddlePanel(JComponent comp, Object constraint) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 2));
		panel.setPreferredSize(new Dimension(252,450));

		JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
		subPanel.setPreferredSize(new Dimension(248,450));
		subPanel.add(gameComponent);
		createPanelBorder(subPanel, "Main");
		
		panel.add(subPanel);		
		comp.add(panel, constraint);
	}
	
	private void createRightPanel(JComponent comp, Object constraint) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));

		JPanel previewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		previewPanel.setPreferredSize(new Dimension(148,155));
		createPanelBorder(previewPanel, "Preview");
		previewPanel.add(previewComponent);
		panel.add(previewPanel, null);

		panel.add(getScorePanel(), null);
		panel.add(getOpponentScorePanel(), null);
		panel.setPreferredSize(new Dimension(150,450));
		comp.add(panel, constraint);
	}
	
	private void createBottomPanel(JComponent comp, Object constraint) {
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		leftPanel.add(getStartingLevelSlider(), null);
		leftPanel.setPreferredSize(new Dimension(149, 42));
		createPanelBorder(leftPanel, null);
		panel.add(leftPanel, BorderLayout.LINE_START);
		
		JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
		messagePanel.add(getMessageLabel1(), null);
		messagePanel.add(getMessageLabel2(), null);
		messagePanel.setPreferredSize(new Dimension(248,42));
		createPanelBorder(messagePanel, null);
		panel.add(messagePanel, BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
		rightPanel.add(getGameSpeedSlider(), null);
		rightPanel.setPreferredSize(new Dimension(149, 42));
		createPanelBorder(rightPanel, null);
		panel.add(rightPanel, BorderLayout.LINE_END);
		
		panel.setPreferredSize(new Dimension(550,55));
		comp.add(panel, constraint);
	}
	
	/**
	 * This method initializes jSlider1 - the starting level slider.
	 * @return JSlider
	 */
	private JSlider getStartingLevelSlider() {
		if (jSlider1 == null) {
			jSlider1 = new JSlider(JSlider.HORIZONTAL);
			jSlider1.setName(Actions.STARTING_LEVEL.name());
			jSlider1.setMinimum(TetrisConstants.MIN_LEVEL);
			jSlider1.setMaximum(TetrisConstants.MAX_LEVEL);
			jSlider1.setValue(TetrisConstants.MIN_LEVEL);
			jSlider1.setMajorTickSpacing(1);
			jSlider1.setSnapToTicks(true);
			jSlider1.setPaintTicks(true);
			
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			for (int i = TetrisConstants.MIN_LEVEL; i <= TetrisConstants.MAX_LEVEL; i++) {
				labelTable.put(i, new JLabel(EMPTY+i));
				i++;	// even numbers only
			}
			jSlider1.setLabelTable(labelTable);
			jSlider1.setPaintLabels(true);
			jSlider1.setCursor(TetrisConstants.CURSOR_HAND);
			jSlider1.setFocusable(false);
			jSlider1.setToolTipText("Starting Level");
			jSlider1.addChangeListener(handler);
			jSlider1.setPreferredSize(new Dimension(130, 44));
			jSlider1.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
		}
		return jSlider1;	
	}	

	/**
	 * This method initializes jSlider - the game speed slider.
	 * @return JSlider
	 */
	private JSlider getGameSpeedSlider() {
		if (jSlider == null) {
			jSlider = new JSlider(JSlider.HORIZONTAL);
			jSlider.setName(Actions.GAME_SPEED.name());
			jSlider.setMinimum(TetrisConstants.SPEED_SLOW);
			jSlider.setMaximum(TetrisConstants.SPEED_FAST);
			jSlider.setValue(TetrisConstants.SPEED_NORM);
			jSlider.setMajorTickSpacing(1);
			jSlider.setSnapToTicks(true);
			jSlider.setPaintTicks(true);
			
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			for (int i = TetrisConstants.SPEED_SLOW; i <= TetrisConstants.SPEED_FAST; i++) {
				labelTable.put(i, new JLabel(Util.getGameSpeedString(i)));
			}
			jSlider.setLabelTable(labelTable);
			jSlider.setPaintLabels(true);
			jSlider.setCursor(TetrisConstants.CURSOR_HAND);
			jSlider.setFocusable(false);
			jSlider.setToolTipText("Game Speed");
			jSlider.addChangeListener(handler);
			jSlider.setPreferredSize(new Dimension(130, 44));
			jSlider.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
		}
		return jSlider;	
	}
	
	private JLabel getMessageLabel1() {
		if (messageLabel1 == null) {
			messageLabel1 = new JLabel(EMPTY);
			messageLabel1.setToolTipText(EMPTY);
			messageLabel1.setPreferredSize(new Dimension(250,20));
			messageLabel1.setFont(TetrisDefaults.FONT_PLAIN);
			//messageLabel1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		}
		return messageLabel1;
	}
	
	private JLabel getMessageLabel2() {
		if (messageLabel2 == null) {
			messageLabel2 = new JLabel(EMPTY);
			messageLabel2.setToolTipText(EMPTY);
			messageLabel2.setPreferredSize(new Dimension(250,20));
			messageLabel2.setFont(TetrisDefaults.FONT_PLAIN);
			//messageLabel2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		}
		return messageLabel2;
	}
	
	private void createPanelBorder(JPanel panel, String title) {
		PanelBorder panelBorder;
		if (title != null) {
			panelBorder = tetrisPanel.getPanelBorder(title, bgColor);
		} else {
			panelBorder = new PanelBorder(tetrisPanel.getLineColor(), 1, 0);
			panelBorder.setRoundedCorners(tetrisPanel.isRoundedCorners());
		}
		panelBorders.put(panelBorder, panel);
		panel.setBorder(panelBorder);
		
	}
	

	protected void setStartingLevel(int level) {
		for (JMenuItem menuItem : startingLevelMenus) {
			colorMenuItem(menuItem, false);
		}
		colorMenuItem(startingLevelMenus.get(level), true);
		getStartingLevelSlider().setValue(level);
	}
	
	protected void setStartingLines(int lines) {
		for (JMenuItem menuItem : startingLinesMenus) {
			colorMenuItem(menuItem, false);
		}
		colorMenuItem(startingLinesMenus.get(lines), true);
	}	
	
	protected void setGameSpeed(int newGameSpeed) {
		for (JMenuItem menuItem : gameSpeedMenus) {
			colorMenuItem(menuItem, false);
		}
		colorMenuItem(gameSpeedMenus.get(newGameSpeed), true);
		getGameSpeedSlider().setValue(newGameSpeed);
	}
	
	/**
	 * Displays a file chooser to let the user select a tetris properties file
	 * to import.
	 */
	protected void displayImportChooser(IUIController controller) {
		if (importChooser == null) {
			importChooser = new JFileChooser();
			importChooser.setApproveButtonMnemonic(KeyEvent.VK_O);
			importChooser.setFileFilter(new PropertiesFileFilter());
			importChooser.setDialogTitle("Choose a tetris properties file to import");
		}
		if (lastImportDir != null) { 
			importChooser.setCurrentDirectory(lastImportDir);
		}
		int choice = importChooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			lastImportDir = importChooser.getCurrentDirectory();
			controller.getPlayer().save(controller.getConfiguration());	
			new ImportDialog(getParentFrame(), this, controller.getPlayer(), 
					controller.getConfiguration(), importChooser.getSelectedFile());
			if (controller.getPlayer().getNeedsReloading()) {
				controller.reloadPlayer();	
			}
		}
	}

	/**
	 * Changes the current player's name.
	 */
	protected void displayPlayerChangeDialog(IUIController uiController) {
		uiController.getPlayer().save(uiController.getConfiguration());
		new PlayerChangeDialog(getParentFrame(), this, uiController, !appletMode);
		uiController.reloadPlayer();
	}
	
	/**
	 * Displays the high scores.
	 * @param highscores	The high scores to display.
	 * @param gameSpeed		The current game speed.
	 * @param position		The zero based position of the new entry (-1 if not added).
	 */
	public void displayHighscores(Highscores highscores, int gameSpeed, int position) {
		disableKeyEvents();
		new HighscoresDialog(getParentFrame(), this, highscores, gameSpeed, position, !appletMode);
		enableKeyEvents();
	}

	/**
	 * Displays the color chooser dialog.
	 */
	protected void displayControlsDialog(IUIController uiController) {
		new TetrisControlsDialog(getParentFrame(), this, uiController);
	}

	/**
	 * Displays the block color chooser dialog.
	 */
	protected void displayColorsFrame(IUIController uiController) {
		if (blockColorChooser == null) {
			blockColorChooser = new TetrisColorFrame(this, uiController);
		} else {
			if (blockColorChooser.isDisplayable() && blockColorChooser.isFocusable()) {
				blockColorChooser.requestFocus();
			} else {
				blockColorChooser.dispose();
				blockColorChooser = new TetrisColorFrame(this, uiController);
			}
		}
	}

	/**
	 * Displays the audio dialog.
	 */
	protected void displayAudioDialog(IUIController uiController) {
		new TetrisAudioDialog(getParentFrame(), this, uiController);
	}

	/**
	 * Displays the block distribution dialog.
	 */
	protected void displayBlockDistributionDialog(TetrisColors colors, int[] blockCounts) {
		if (blockDistributionDialogShown) {
			blockDistributionDialog.setVisible(false);
			setBlockDistributionDialogShown(false);		
		} else {
			if (blockDistributionDialog == null) {
				blockDistributionDialog = new BlockDistributionDialog(this, colors, blockCounts);
				setBlockDistributionDialogShown(true);	
			} else {
				blockDistributionDialog.setDialogLocation();
				blockDistributionDialog.setVisible(true);
				setBlockDistributionDialogShown(true);	
			}
		}
		requestFocus();
	}
	
	/**
	 * Displays the about dialog.
	 */
	protected void displayAboutDialog(String version, String date) {
		String message = "TETRIS\nChris Callendar\n\nVersion " + version+ "\n" + date;
		String title = "About Tetris";
		Object[] options = { "OK" };
		Icon icon = Util.getIconResource("/images/cc.gif");
		JOptionPane.showOptionDialog(this, message, title, JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);
	}
	
	/**
	 * Displays the dialog which shows the scoring breakdown.
	 */
	protected void displayScoringDialog() {
		new ScoringDetailsDialog(getParentFrame(), this);
	}

	/**
	 * Gets the parent frame or null if the parent container isn't a Frame.
	 */
	private Frame getParentFrame() {
		if (parentFrame == null) {
			Container parent = getParent();
			while (parent != null) {
				if (parent instanceof Frame) {
					parentFrame = (Frame)parent;
					break;
				}
				parent = parent.getParent();
			}
		}
		return parentFrame;
	}
	
	/**
	 * Creates a new multiplayer game.
	 */
	protected void createGame(TetrisCommunication communication, IMultiplayerController multiplayerController,  
			IUIController uiController) {
		
		if ((communication != null) && (mpFrame != null) && (mpFrame.getCommunication() != null)) {
			mpFrame.setVisible(true);
			mpFrame.requestFocus();
		} else {
			uiController.getPlayer().setPlayerNumber(1);
			uiController.getOpponent().setPlayerNumber(2);
			Server server = null;
			try {
				ServerDialog sDialog = new ServerDialog(getParentFrame(), uiController.getPlayer(), communication);
				server = sDialog.getServer();
				communication.setCommunication(server);
				sDialog.dispose();
				if ((server != null) && server.isRunning()) {
					mpFrame = new MultiplayerFrame(this, multiplayerController, uiController, communication);
					mpFrame.addMessage(0, "Server started on port " + uiController.getPlayer().getHostPort() + ".");
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (server != null) {
					server.shutdown();
				}
			}
		}
	}

	/**
	 * Joins a multiplayer game.
	 */
	protected void joinGame(TetrisCommunication communication, 
			IMultiplayerController multiplayerController, IUIController uiController) {
		
		if ((communication != null) && (mpFrame != null) && (mpFrame.getCommunication() != null)) {
			mpFrame.setVisible(true);
			mpFrame.requestFocus();
		} else {
			uiController.getPlayer().setPlayerNumber(2);
			uiController.getOpponent().setPlayerNumber(1);
			Client client = null;
			try {
				ClientDialog cDialog = new ClientDialog(getParentFrame(), uiController.getPlayer(), communication);
				client = cDialog.getClient();
				communication.setCommunication(client);
				cDialog.dispose();
				if (client != null) {
					mpFrame = new MultiplayerFrame(this, multiplayerController, uiController, communication);
					mpFrame.sendConnectMessage();
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (client != null)
					client.shutdown();
			}
		}
	}	

	/////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	/////////////////////////////////////////////////////////////

	public boolean isAppletMode() {
		return appletMode;
	}
	
	/**
	 * Gets the top left corner of the parent container.
	 * @return Point the top left corner of the parent container.
	 */
	public Point getTopLeft() {
		Frame frame = getParentFrame();
		if (frame != null) {
			return frame.getLocation();
		}
		return getLocationOnScreen(); 
	}
	
	/**
	 * Returns the width of the parent component if it isn't null,
	 * otherwise the width of this is returned.
	 * @return int width
	 */
	public int getParentWidth() {
		Frame frame = getParentFrame();
		if (frame != null) {
			return frame.getWidth();
		}
		return this.getWidth();
	}
	
	/**
	 * Returns the tetris panel used in the UI.
	 * @return TetrisPanel
	 */
	public TetrisPanel getTetrisPanel() {
		return tetrisPanel;
	}

	/**
	 * Sets the tetris panel.  
	 * This should be called (for the first time) before initialize() is called.
	 */
	public void setTetrisPanel(TetrisPanel panel) {
		this.tetrisPanel = panel;
	}	
	
	/**
	 * Sets the action handler and listener which deals with all the events.  
	 * This should be called before initialize() is called.
	 */
	public void setActionListener(TetrisActionListener handler) {
		this.handler = handler;
	}
	
	/**
	 * Sets the game board and preview board components.
	 */
	public void setGameComponents(Component gameComponent, Component previewComponent) {
		this.gameComponent = gameComponent;
		this.previewComponent = previewComponent;
	}
	
	/**
	 * Sets the starting level, lines, and game speed - colors the menu items
	 * and adjusts the sliders.
	 */
	public void setStartingValues(int startingLevel, int startingLines, int gameSpeed) {
		setStartingLevel(startingLevel);
		setStartingLines(startingLines);
		setGameSpeed(gameSpeed);
	}
	
	/**
	 * Colors the selected look and feel JMenuItem
	 * and uncolors the previously selected lnf item.
	 * @param lnfObject the look and feel object
	 */
	public void setLookAndFeel(String lnfName) {
		for (JMenuItem menuItem : lnfMenuItems.values()) {
			colorMenuItem(menuItem, false);
		}
		if (lnfMenuItems.containsKey(lnfName)) {
			colorMenuItem(lnfMenuItems.get(lnfName), true);
			//TODO bgcolor of labels differs from panel bgcolor
		}
		
		JPanel test = new JPanel();
		bgColor = test.getBackground();
		setColors(this.getComponents());
	}
	
	/**
	 * Disables the menu item (and uncolors it) associated with the
	 * given look and feel.
	 * @param lnfName
	 */
	public void disableLookAndFeel(String lnfName) {
		if (lnfMenuItems.containsKey(lnfName)) {
			colorMenuItem(lnfMenuItems.get(lnfName), false);
			lnfMenuItems.get(lnfName).setEnabled(false);
		}
	}

		
	/**
	 * This method initializes the start button.
	 * @return JButton
	 */
	public JButton getStartButton() {
		if(startButton == null) {
			startButton = new JButton(TEXT_START);
			startButton.setActionCommand(Actions.START.name());
			startButton.addActionListener(handler);
			startButton.setMnemonic(java.awt.event.KeyEvent.VK_S);
			startButton.addKeyListener(handler);
		}
		return startButton;
	}

	/**
	 * This method initializes the player's score panel.
	 * @return ScorePanel
	 */
	public ScorePanel getScorePanel() {
		if (scorePanel == null) {
			scorePanel = new ScorePanel(true);
			createPanelBorder(scorePanel, "Your Score");
			scorePanel.setPreferredSize(new Dimension(148,164));
		}
		return scorePanel;	
	}

	/**
	 * This method initializes the opponent's score panel.
	 * @return ScorePanel
	 */
	public ScorePanel getOpponentScorePanel() {
		if (opponentScorePanel == null) {
			opponentScorePanel = new ScorePanel(false);
			createPanelBorder(opponentScorePanel, "Opponent's Score");
			opponentScorePanel.setPreferredSize(new Dimension(148,125));
			opponentScorePanel.setVisible(false);
		}
		return opponentScorePanel;	
	}

	/**
	 * Initializes this player's stats panel.
	 * @return StatsPanel
	 */
	public StatsPanel getStatsPanel() {
		if (statsPanel == null) {
			statsPanel = new StatsPanel();
		}
		return statsPanel;
	}
	
	/**
	 * Initializes the opponent's stats panel.
	 * @return StatsPanel
	 */
	public StatsPanel getOpponentStatsPanel() {
		if (opponentStatsPanel == null) {
			opponentStatsPanel = new StatsPanel();
		}
		return opponentStatsPanel;
	}
	
	/**
	 * Updates the text on the start/pause button.
	 * @param text	the text for the button.
	 * @param action the action
	 */
	public void setButtonText(String text, Actions action) {
		getStartButton().setText(text);
		getStartButton().setActionCommand(action.name());
	}

	/**
	 * Updates the message label.
	 * @param text	the text for the label.
	 */
	public void setMessageText(String text, boolean error) {
		JLabel label1 = getMessageLabel1();
		JLabel label2 = getMessageLabel2();
		// shift previous message down
		String prev = label1.getText().trim();
		if ((prev.length() > 0) && (!text.equals(prev))) {
			label2.setText(label1.getText());
			label2.setForeground(label1.getForeground());
			label2.setToolTipText(label1.getToolTipText());
		}
		if (text.length() > 45) {
			label1.setToolTipText(text);
			text = text.substring(0, 35) + "...";
		}
		label1.setText(" " + text);
		if (error) {
			label1.setForeground(Color.RED);
		} else {
			label1.setForeground(Color.BLACK);
		}
	}	

	/**
	 * Shows or hides the opponent's score and stats panels.
	 */
	public void setMultiplayer(boolean multiplayer) {
		getOpponentScorePanel().setVisible(multiplayer);
		getOpponentStatsPanel().setVisible(multiplayer);
	}
	
	public void dropPlayer(int playerNum) {
		if (mpFrame != null) {
			mpFrame.dropPlayer(playerNum, null);
		}
	}
	
	/**
	 * Repaints the panel borders.
	 */
	public void repaintPanelBorders() {
		if (tetrisPanel.isNeedRepaint()) {
			Iterator iter = panelBorders.keySet().iterator();
			while (iter.hasNext()) {
				PanelBorder panelBorder = (PanelBorder)iter.next();
				panelBorder.setPanelProperties(tetrisPanel.getFirstColor(), tetrisPanel.getSecondColor(), 
						tetrisPanel.getTitleColor(), tetrisPanel.getLineColor(), 
						tetrisPanel.getDirection(), tetrisPanel.isRoundedCorners());
				JPanel jPanel = panelBorders.get(panelBorder);
				panelBorder.setBackground(panelBorder.getOuterBackground(), jPanel.getBackground());
				jPanel.repaint();
			}
		}
	}
	
	public void updateAllScores(TetrisPlayer player, TetrisPlayer opponent) {
		updateFullScore(player);
		updateOpponentFullScore(opponent);
	}
	
	public void updateFullScore(TetrisPlayer player) {
		getScorePanel().updateFullScores(player);		
	}
	
	public void updateSimpleScore(TetrisPlayer player) {
		getScorePanel().updateSimpleScores(player);
	}
	
	public void updateStats(TetrisPlayer player) {
		getStatsPanel().updateStats(player);
	}
	
	public void updateOpponentFullScore(TetrisPlayer opponent) {
		if (getOpponentScorePanel().isVisible()) {
			getOpponentScorePanel().updateFullScores(opponent);
		}
	}
	
	public void updateOpponentSimpleScore(TetrisPlayer opponent) {
		if (getOpponentScorePanel().isVisible()) {
			getOpponentScorePanel().updateSimpleScores(opponent);
		}
	}
	
	public void updateOpponentStats(TetrisPlayer opponent) {
		getOpponentStatsPanel().updateStats(opponent);
	}

	public void updateTetrisTotal(int tetrisTotal) {
		getStatsPanel().updateTetrisTotal(tetrisTotal);
	}
	
	public void updateOpponentTetrisTotal(int tetrisTotal) {
		getOpponentStatsPanel().updateTetrisTotal(tetrisTotal);
	}

	/**
	 * Checks if the block distribution dialog is visible.
	 * @return boolean
	 */
	public boolean isBlockDistributionDialogVisible() {
		return ((blockDistributionDialog != null) && blockDistributionDialog.isVisible());
	}
	
	/**
	 * Updates the location of the block distribution dialog to keep it docked with
	 * the frame.
	 */
	public void updateBlockDistributionDialogLocation() {
		if (isBlockDistributionDialogVisible()) {
			blockDistributionDialog.setDialogLocation();
		}
	}
	
	/**
	 * Sets the boolean if the block distribution dialog is shown.
	 * Does not actually display or hide the dialog.
	 * @param shown
	 */
	public void setBlockDistributionDialogShown(boolean shown) {
		blockDistributionDialogShown = shown;
		getBlockDistributionMenuItem().setSelected(shown);		
	}


	/**
	 * Shows the block distribution dialog.
	 */
	public void showBlockDistributionDialog() {
		if (blockDistributionDialogShown && (blockDistributionDialog != null)) {
			blockDistributionDialog.setVisible(true);
			this.requestFocus();
		}
	}

	/**
	 * Updates the block counts.
	 * @param blockCounts the counts of each block type
	 */
	public void updateBlockCounts(int[] blockCounts) {
		if (isBlockDistributionDialogVisible()) {
			blockDistributionDialog.updateBlockCounts(blockCounts);
		}
	}

	/**
	 * Updates the colors of the blocks shown in the block distribution dialog.
	 * @param colors the new block colors
	 */
	public void updateBlockColors(TetrisColors colors) {
		if (isBlockDistributionDialogVisible()) {
			blockDistributionDialog.updateColors(colors);
		}
	}

	@Override
	public void requestFocus() {
		gameComponent.requestFocus();
		super.requestFocus();
	}
	
	/**
	 * Adds a panel border to the list of borders. 
	 * This way when the user changes the panel border colors
	 * the given border will be updated too.
	 * @param border the border.
	 * @param panel	 the associated JPanel.
	 */
	public void addPanelBorder(PanelBorder border, JPanel panel) {
		panelBorders.put(border, panel);
	}
	
}
