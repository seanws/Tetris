/**
 * TetrisPlayer.java
 * 
 * Created on 17-Mar-04
 */
package org.oversoul.tetris;

import java.awt.Point;

import org.oversoul.tetris.util.Configuration;



/**
 * Holds information about a tetris player.
 * 
 * @author Chris Callendar (9902588)
 * @date   17-Mar-04, 10:07:54 PM
 */
public class TetrisPlayer {

	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String EMPTY = "";
	private static final String LAST = "player.last";
	private static final String WINS = "stats.wins";
	private static final String LOSSES = "stats.losses";
	private static final String GAMESPEED = "options.gamespeed";
	private static final String STARTLEVEL = "options.startinglevel";
	private static final String STARTLINES = "options.startinglines";
	private static final String FIXEDLEVEL = "options.fixedlevel";
	private static final String FIXEDLINES = "options.fixedlines";
	private static final String FIXEDSPEED = "options.fixedspeed";
	private static final String PENALTIES = "options.penalties";
	private static final String BLOCKDIST = "options.blockdistribution";
	private static final String TETRISES = "stats.tetristotal";
	private static final String PREVIEW = "options.preview";
	private static final String HOST_ADDRESS = "multiplayer.host.address";
	private static final String HOST_PORT = "multiplayer.host.port";
	private static final String CLIENT_PORT = "multiplayer.client.port";
	private static final String LOCATION_X = "location.x";
	private static final String LOCATION_Y = "location.y";
	private static final String LOOK_AND_FEEL = "lookandfeel.name";

	private String name = EMPTY;
	
	private boolean needsReloading = false;
	
	private int playerNumber = 0;
	
	// startup options
	private int startingLevel = 0;
	private int startingLines = 0;
	private int gameSpeed = TetrisConstants.SPEED_NORM;
	private boolean fixedLevel = false;
	private boolean fixedLines = false;
	private boolean fixedSpeed = false;
	private boolean penalties = false;
	private boolean preview = true;
	private boolean showBlockDistribution = false;
	private int team = 1;
	
	// multiplayer in game options
	private boolean ready = false;
	private boolean accepted = false;
	private boolean gameOver = false;
	
	// in game score variables
	private int score = 0;
	private int wscore = 0;
	private int lines = 0;
	private int level = 0;
	private int inPenaltyLines = 0;
	private int outPenaltyLines = 0;
	private int tetrisCount = 0;
	private int tetrisTotal = 0;
	private int triples = 0;
	private int doubles = 0;
	private int singles = 0;
	private String gameTime = "0:00";
	
	// history options
	private int wins = 0;
	private int losses = 0;
	
	// player keys
	private TetrisControls controls = null;
	
	// player block colors
	private TetrisColors colors = null;
	
	// player panel colors
	private TetrisPanel panel = null;
	
	// player sounds
	private TetrisAudio audio = null;
	
	// multiplayer options
	private String hostAddress = "";
	private int hostPort = TetrisDefaults.DEFAULT_PORT; 
	private int clientPort = TetrisDefaults.DEFAULT_PORT;
	
	private Point location = null;
	
	private String lnfName = TetrisDefaults.LOOK_AND_FEEL;
	
	/**
	 * Constructor for TetrisPlayer.
	 */
	public TetrisPlayer() {
		this(EMPTY);
	}

	/**
	 * Constructor for TetrisPlayer.
	 */
	public TetrisPlayer(String playerName) {
		this.name = playerName;
		resetAll();
	}
	
	public boolean getNeedsReloading() {
		return needsReloading;
	}
	
	public void setNeedsReloading(boolean needsReloading) {
		this.needsReloading = needsReloading;
	}
	
	/**
	 * Resets all the fields except the player name & number (and colors and keys).
	 */
	public void resetAll() {
		//System.out.println("* Resetting player: " + name);
		//this.name = EMPTY;
		startingLevel = startingLines = score = wscore = lines = level = tetrisCount = tetrisTotal = triples = doubles = singles = 0;
		fixedLevel = fixedLines = fixedSpeed = penalties = ready = accepted = false;
		preview = true;
		wins = losses = inPenaltyLines = outPenaltyLines = 0;
		gameSpeed = TetrisConstants.SPEED_NORM;
		location = new Point(200, 120);
		gameTime = "0:00";
	}
	
	public void resetGame() {
		score = wscore = lines = tetrisCount = triples = doubles = singles = 0;
		gameTime = "0:00";
		ready = accepted = false;
		inPenaltyLines = outPenaltyLines = 0;		
		level = startingLevel;
	}

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////	

	/**
	 * Returns the playerNumber.
	 * @return int
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * Sets the playerNumber.
	 * @param playerNumber The playerNumber to set
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the starting settings. 
	 * @param startingLevel
	 * @param fixedLevel
	 * @param startingLines
	 * @param fixedLines
	 * @param gameSpeed
	 * @param fixedSpeed
	 * @param penalties
	 */
	public void setSettings(int startingLevel, boolean fixedLevel,
							int startingLines, boolean fixedLines, 
							int gameSpeed, boolean fixedSpeed, 
							boolean penalties) {
		this.startingLevel = startingLevel;
		this.fixedLevel = fixedLevel;
		this.startingLines = startingLines;
		this.fixedLines = fixedLines;
		this.gameSpeed = gameSpeed;
		this.fixedSpeed = fixedSpeed;
		this.penalties = penalties;							
	}


	/**
	 * Gets the game speed.
	 * @return int game speed
	 */
	public int getGameSpeed() {
		return gameSpeed;
	}

	/**
	 * Sets the game speed.
	 * @param gameSpeed game speed
	 */
	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	/**
	 * Returns if the speed is fixed.
	 * @return boolean if the speed is fixed.
	 */
	public boolean isFixedSpeed() {
		return fixedSpeed;
	}

	/**
	 * Sets if the speed is fixed.
	 * @param fixedSpeed
	 */
	public void setFixedSpeed(boolean fixedSpeed) {
		this.fixedSpeed = fixedSpeed;
	}
	
	/**
	 * Sets the fixed starting level, lines, game speed, and penalties.
	 * @param fixedLevel
	 * @param fixedLines
	 * @param fixedSpeed
	 * @param penalties
	 */
	public void setBooleans(boolean fixedLevel, boolean fixedLines, boolean fixedSpeed, boolean penalties) {
		this.fixedLevel = fixedLevel;
		this.fixedLines = fixedLines;
		this.fixedSpeed = fixedSpeed;
		this.penalties = penalties;
	}

	/**
	 * Returns the startingLevel.
	 * @return int
	 */
	public int getStartingLevel() {
		return startingLevel;
	}

	/**
	 * Sets the startingLevel.
	 * @param startingLevel The startingLevel to set
	 */
	public void setStartingLevel(int startingLevel) {
		this.startingLevel = startingLevel;
	}

	/**
	 * Returns the startingLines.
	 * @return int
	 */
	public int getStartingLines() {
		return startingLines;
	}

	/**
	 * Sets the startingLines.
	 * @param startingLines The startingLines to set
	 */
	public void setStartingLines(int startingLines) {
		this.startingLines = startingLines;
	}

	/**
	 * Returns the fixedLevel.
	 * @return boolean
	 */
	public boolean isFixedLevel() {
		return fixedLevel;
	}

	/**
	 * Sets the fixedLevel.
	 * @param fixedLevel The fixedLevel to set
	 */
	public void setFixedLevel(boolean fixedLevel) {
		this.fixedLevel = fixedLevel;
	}

	/**
	 * Returns the fixedLines.
	 * @return boolean
	 */
	public boolean isFixedLines() {
		return fixedLines;
	}

	/**
	 * Sets the fixedLines.
	 * @param fixedLines The fixedLines to set
	 */
	public void setFixedLines(boolean fixedLines) {
		this.fixedLines = fixedLines;
	}

	/**
	 * Returns the penalties.
	 * @return boolean
	 */
	public boolean isPenalties() {
		return penalties;
	}

	/**
	 * Sets the penalties.
	 * @param penalties The penalties to set
	 */
	public void setPenalties(boolean penalties) {
		this.penalties = penalties;
	}
	
	/**
	 * Gets the team number.
	 * @return int
	 */
	public int getTeam() {
		return team;	
	}
	
	/**
	 * Sets the team number.
	 * @param team
	 */
	public void setTeam(int team) {
		this.team = team;	
	}

	/**
	 * Sets the simple score.
	 * @param score
	 * @param wscore
	 */
	public void setSimpleScore(int score, int wscore) {
		this.score = score;
		this.wscore = wscore;
	}
	/**
	 * Sets all the score variables.
	 * @param score
	 * @param wscore
	 * @param lines
	 * @param level
	 * @param tetrisCount
	 * @param inPenaltyLines
	 */
	public void setFullScore(int score, int wscore, int lines, int level, int tetrisCount, int inPenaltyLines) {
		this.score = score;
		this.wscore = wscore;
		this.lines = lines;
		this.level = level;
		this.tetrisCount = tetrisCount;
		this.tetrisTotal += tetrisCount;
		this.inPenaltyLines = inPenaltyLines;
	}

	/**
	 * Returns the score.
	 * @return int
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Sets the score.
	 * @param score The score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * Adds to the score.
	 * @param scoreToAdd
	 */
	public void addScore(int scoreToAdd) {
		this.score += scoreToAdd;
	}
	/**
	 * Returns the wscore.
	 * @return int
	 */
	public int getWeightedScore() {
		return wscore;
	}
	/**
	 * Sets the wscore.
	 * @param wscore The wscore to set
	 */
	public void setWeightedScore(int wscore) {
		this.wscore = wscore;
	}
	/**
	 * Returns the lines.
	 * @return int
	 */
	public int getLines() {
		return lines;
	}
	/**
	 * Sets the lines.
	 * @param lines The lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}
	/**
	 * Adds to the lines.
	 * @param linesToAdd
	 */
	public void addLines(int linesToAdd) {
		this.lines += linesToAdd;
	}
	/**
	 * Returns the level.
	 * @return int
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * Sets the level.
	 * @param level The level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * Level up.
	 */
	public void levelUp() {
		if (audio != null) {
			audio.play(TetrisAudio.PLAY_LEVELUP);
		}
		this.level++;
	}
	/**
	 * Gets the tetris count.
	 * @return int
	 */
	public int getTetrisCount() {
		return tetrisCount;
	}
	/**
	 * Sets the tetris countr.
	 * @param tetrisCount
	 */
	public void setTetrisCount(int tetrisCount) {
		this.tetrisCount = tetrisCount;
	}
	/**
	 * Adds a tetris.
	 */
	public void tetris() {
		if (audio != null) {
			audio.play(TetrisAudio.PLAY_TETRIS);
		}
		tetrisCount++;
		tetrisTotal++;
	}
	/**
	 * Gets the tetris total.
	 * @return int
	 */
	public int getTetrisTotal() {
		return tetrisTotal;
	}
	/**
	 * Sets the tetris total.
	 * @param tetrisTotal
	 */
	public void setTetrisTotal(int tetrisTotal) {
		this.tetrisTotal = tetrisTotal;
	}
	
	public int getTriples() {
		return triples;
	}
	public void addTriple() {
		triples++;	
	}
	public int getDoubles() {
		return doubles;
	}
	public void addDouble() {
		doubles++;
	}
	public int getSingles() {
		return singles; 
	}
	public void addSingle() {
		singles++;
	} 
	
	public void setGameTime(String gameTime) {
		this.gameTime = gameTime;
	}
	public String getGameTime() {
		return gameTime;
	}
	
	/**
	 * Plays the penalty sound and resets the penalty lines.
	 */
	public void penalty() {
		if (audio != null) {
			audio.play(TetrisAudio.PLAY_PENALTY);	
		}
		setIncomingPenaltyLines(0);
	}
	/**
	 * Returns the incoming penalty lines.
	 * @return int
	 */
	public int getIncomingPenaltyLines() {
		return inPenaltyLines;
	}
	/**
	 * Sets the incoming penalty lines.
	 * @param inPenaltyLines The incoming penalty lines to set
	 */
	public void setIncomingPenaltyLines(int inPenaltyLines) {
		this.inPenaltyLines = inPenaltyLines;
	}
	/**
	 * Adds incoming penalty lines.
	 * @param lines
	 */
	public void addIncomingPenaltyLines(int lines) {
		this.inPenaltyLines += lines;
	}

	/**
	 * Returns the outgoing penalty lines.
	 * @return int
	 */
	public int getOutgoingPenaltyLines() {
		return outPenaltyLines;
	}
	/**
	 * Sets the outgoing penalty lines.
	 * @param outPenaltyLines The incoming penalty lines to set
	 */
	public void setOutgoingPenaltyLines(int outPenaltyLines) {
		this.outPenaltyLines = outPenaltyLines;
	}
	/**
	 * Adds outgoing penalty lines.
	 * @param lines
	 */
	public void addOutgoingPenaltyLines(int lines) {
		this.outPenaltyLines += lines;
	}

	/** Increases the wins by one. */
	public void won() {
		wins++;	
	}
	/** Increases the losses by one. */
	public void lost() {
		losses++;	
	}
	/**
	 * Returns the wins.
	 * @return int
	 */
	public int getWins() {
		return wins;
	}
	/**
	 * Sets the wins.
	 * @param wins The wins to set
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}
	/**
	 * Returns the losses.
	 * @return int
	 */
	public int getLosses() {
		return losses;
	}
	/**
	 * Sets the losses.
	 * @param losses The losses to set
	 */
	public void setLosses(int losses) {
		this.losses = losses;
	}
	/**
	 * Sets the wins and losses.
	 * @param wins
	 * @param losses
	 */
	public void setStats(int wins, int losses) {
		this.wins = wins;
		this.losses = losses;
	}
	/**
	 * Resets the stats to zero.
	 */
	public void clearStats() {
		this.wins = 0;
		this.losses = 0;
		this.tetrisCount = 0;
		this.tetrisTotal = 0;
	}

	/**
	 * Returns if preview is on.
	 * @return boolean
	 */
	public boolean isPreview() {
		return preview;
	}

	/**
	 * Sets if preview is on.
	 * @param b
	 */
	public void setPreview(boolean b) {
		preview = b;
	}

	/**
	 * Gets the winning percentage.
	 * @return String winning percentage.
	 */
	public String getWinningPercentage() {
		String s = "0";
		if ((losses > 0) && (wins > 0)) {
			int i = (int)(100D * wins) / (losses+wins);
			s = EMPTY + i;
		} else if (wins == 0) {
			s = "0";
		} else {
			s = "100";	
		}
		return s;
	}

	/**
	 * Returns the ready.
	 * @return boolean
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Sets the ready.
	 * @param ready The ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * Returns the accepted.
	 * @return boolean
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * Sets the accepted.
	 * @param accepted The accepted to set
	 */
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	/**
	 * Returns the gameOver.
	 * @return boolean
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Sets the gameOver.
	 * @param gameOver The gameOver to set
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/**
	 * Returns the tetris controls object.
	 * @return TetrisControls
	 */
	public TetrisControls getControls() {
		return controls;
	}

	/**
	 * Sets the game controls.
	 * @param controls
	 */
	public void setControls(TetrisControls controls) {
		this.controls = controls;
	}

	/**
	 * Returns the game colors.
	 * @return TetrisColors
	 */
	public TetrisColors getColors() {
		return colors;
	}

	/**
	 * Sets the game colors.
	 * @param colors
	 */
	public void setColors(TetrisColors colors) {
		this.colors = colors;
	}
	
	/**
	 * Gets the tetris panel.
	 * @return TetrisPanel
	 */
	public TetrisPanel getPanel() {
		return panel;
	}

	/**
	 * Sets the tetris panel. 
	 * @param panel
	 */
	public void setPanel(TetrisPanel panel) {
		this.panel = panel;
	}

	/**
	 * Gets the tetris sounds.
	 * @return TetrisAudio
	 */
	public TetrisAudio getAudio() {
		return audio;
	}
	
	/**
	 * Sets the tetris sounds.
	 * @param audio
	 */
	public void setAudio(TetrisAudio audio) {
		this.audio = audio;
	}

	/**
	 * Gets the default host address.
	 * @return String
	 */
	public String getHostAddress() {
		return hostAddress;
	}
	/**
	 * Sets the default host address.
	 * @param address
	 */
	public void setHostAddress(String address) {
		hostAddress = address;
	}
	/**
	 * Gets the default server port.
	 * @return int
	 */
	public int getHostPort() {
		return hostPort;
	}
	/**
	 * Sets the default server port.
	 * @param port
	 */
	public void setHostPort(int port) {
		hostPort = port;
	}
	/**
	 * Gets the default client port.
	 * @return int
	 */
	public int getClientPort() {
		return clientPort;
	}
	/**
	 * Sets the default client port. 
	 * @param port
	 */
	public void setClientPort(int port) {
		clientPort = port;
	}

	/**
	 * Gets the window location.
	 * @return Point
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Sets the window location.
	 * @param location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	
	/**
	 * Sets if the block distribution window is shown.
	 * @param showBlockDistribution
	 */
	public void setShowBlockDistribtion(boolean showBlockDistribution) {
		this.showBlockDistribution = showBlockDistribution;
	}
	
	/**
	 * Gets if the block distribution window is shown.
	 * @return boolean
	 */
	public boolean isShowBlockDistribution() {
		return showBlockDistribution;
	}

	/**
	 * Gets the current look and feel name.
	 * @return String
	 */
	public String getLookAndFeel() {
		return lnfName;
	}
	
	/**
	 * Sets the look and feel name.
	 * @param lnfName
	 */
	public void setLookAndFeel(String lnfName) {
		this.lnfName = lnfName;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	// SAVE & LOAD METHODS
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads the last player.
	 */
	public void load(Configuration config) {
		load(config.getValue(LAST), config);
	}
	
	/**
	 * Loads the player's configuration.
	 */
	public void load(String name, Configuration config) {
		if ((name == null) || (name.length() == 0)) {
			name = "Player1";
		}
		setName(name);
		if (config.getValue(Configuration.PLAYERNAME + name) != null) {
			setWins(Integer.parseInt(config.getValue(name, WINS, "0")));
			setLosses(Integer.parseInt(config.getValue(name, LOSSES, "0")));
			setGameSpeed(Integer.parseInt(config.getValue(name, GAMESPEED, "1")));
			setStartingLevel(Integer.parseInt(config.getValue(name, STARTLEVEL, "0")));
			setStartingLines(Integer.parseInt(config.getValue(name, STARTLINES, "0")));
			setFixedLevel(TRUE.equals(config.getValue(name, FIXEDLEVEL, FALSE)));
			setFixedLines(TRUE.equals(config.getValue(name, FIXEDLINES, FALSE)));
			setFixedSpeed(TRUE.equals(config.getValue(name, FIXEDSPEED, FALSE)));
			setPenalties(TRUE.equals(config.getValue(name, PENALTIES, FALSE)));
			setTetrisTotal(Integer.parseInt(config.getValue(name, TETRISES, "0")));
			setPreview(TRUE.equals(config.getValue(name, PREVIEW, TRUE)));
			setHostAddress(config.getValue(name, HOST_ADDRESS, EMPTY));
			setHostPort(Integer.parseInt(config.getValue(name, HOST_PORT, EMPTY+TetrisDefaults.DEFAULT_PORT)));
			setClientPort(Integer.parseInt(config.getValue(name, CLIENT_PORT, EMPTY+TetrisDefaults.DEFAULT_PORT)));
			Point p = new Point();
			p.x = Integer.parseInt(config.getValue(name, LOCATION_X, EMPTY+250));
			p.y = Integer.parseInt(config.getValue(name, LOCATION_Y, EMPTY+100));
			setLocation(p);
			setShowBlockDistribtion(TRUE.equals(config.getValue(name, BLOCKDIST, FALSE)));
			setLookAndFeel(config.getValue(name, LOOK_AND_FEEL, TetrisDefaults.LOOK_AND_FEEL));
		}
		loadControls(config);
		loadColors(config);
		loadAudio(config);
		loadPanel(config);
	}

	/**
	 * Saves the player's name as the last player.
	 */
	public void saveLast(Configuration config) {
		String name = getName();
		if ((name == null) || (name.length() == 0)) {
			name = "Player1";
		}
		name = name.replace(' ', '_');
		name = name.replace('.', '_');
		config.setValue(LAST, name);
		config.setValue(Configuration.PLAYERNAME + name, name);
	}
	
	/**
	 * Saves the player's configuration.
	 */
	public void save(Configuration config) {
		saveLast(config);
		config.setValue(name, WINS, EMPTY+getWins());
		config.setValue(name, LOSSES, EMPTY+getLosses());
		config.setValue(name, GAMESPEED, EMPTY+getGameSpeed());
		config.setValue(name, STARTLEVEL, EMPTY+getStartingLevel());
		config.setValue(name, STARTLINES, EMPTY+getStartingLines());
		config.setValue(name, FIXEDLEVEL, EMPTY+isFixedLevel());
		config.setValue(name, FIXEDLINES, EMPTY+isFixedLines());
		config.setValue(name, FIXEDSPEED, EMPTY+isFixedSpeed());
		config.setValue(name, PENALTIES, EMPTY+isPenalties());
		config.setValue(name, TETRISES, EMPTY+getTetrisTotal());
		config.setValue(name, PREVIEW, EMPTY+isPreview());
		config.setValue(name, HOST_ADDRESS, getHostAddress());
		config.setValue(name, HOST_PORT, EMPTY+getHostPort());
		config.setValue(name, CLIENT_PORT, EMPTY+getClientPort());
		config.setValue(name, LOCATION_X, EMPTY+getLocation().x);
		config.setValue(name, LOCATION_Y, EMPTY+getLocation().y);
		config.setValue(name, BLOCKDIST, EMPTY+isShowBlockDistribution());
		config.setValue(name, LOOK_AND_FEEL, lnfName);
	}

	/**
	 * Saves all the player properties.
	 * @param config
	 */
	public void saveAll(Configuration config) {
		save(config);
		saveAudio(config);
		saveColors(config);
		saveControls(config);
		savePanel(config);	
	}
	
	/**
	 * Loads the game control keys.
	 */
	public void loadControls(Configuration config) {
		if (controls == null) {
			controls = new TetrisControls();
		}
		controls.load(getName(), config);
	}
	
	/**
	 * Saves the game control keys.
	 */
	public void saveControls(Configuration config) {
		if (controls == null) {
			controls = new TetrisControls();
		}
		controls.save(getName(), config);
	}
	
	/**
	 * Loads the colors.
	 * @param config
	 */
	public void loadColors(Configuration config) {
		if (colors == null) {
			colors = new TetrisColors();
		}
		colors.load(getName(), config);
	}

	/**
	 * Saves the colors.
	 * @param config
	 */
	public void saveColors(Configuration config) {
		if (colors == null) {
			colors = new TetrisColors();
		}
		colors.save(getName(), config);
	}
	
	/**
	 * Loads the audio.
	 * @param config
	 */
	public void loadAudio(Configuration config) {
		if (audio == null) {
			audio = new TetrisAudio();
		}
		audio.load(getName(), config);
	}
	
	/**
	 * Saves the audio.
	 * @param config
	 */
	public void saveAudio(Configuration config) {
		if (audio == null) {
			audio = new TetrisAudio();
		}
		audio.save(getName(), config);
	}

	/** 
	 * Loads the panel colors, gradient direction, and rounded corners.
	 * @param config
	 */
	public void loadPanel(Configuration config) {
		if (panel == null) {
			panel = new TetrisPanel();	
		}	
		panel.load(getName(), config);
	}
	
	/** 
	 * Saves the panel colors, gradient direction, and rounded corners.
	 * @param config
	 */
	public void savePanel(Configuration config) {
		if (panel == null) {
			panel = new TetrisPanel();	
		}	
		panel.save(getName(), config);
	}

}
