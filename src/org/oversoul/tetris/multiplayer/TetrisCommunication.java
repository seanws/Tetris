/**
 * TetrisCommunication.java
 * 
 * Created on 10-Mar-04
 */
package org.oversoul.tetris.multiplayer;

import java.util.Vector;

import org.oversoul.networking.Communication;
import org.oversoul.networking.NetworkApplication;
import org.oversoul.networking.Packet;
import org.oversoul.tetris.TetrisController;
import org.oversoul.tetris.TetrisPlayer;

/**
 * Provides methods for sending and receiving tetris network packets.
 * 
 * @author Chris Callendar (9902588)
 * @date   10-Mar-04, 5:46:36 PM
 */
public class TetrisCommunication implements NetworkApplication {

	private Communication communication = null;

	private TetrisController controller = null;

	/** Game setup ui. */
	private MultiplayerFrame mpFrame = null;

	/** Stores messages. */
	private Vector<Packet> messages = null;

	/**
	 * Constructor for TetrisCommunication.
	 */
	public TetrisCommunication(TetrisController controller) {
		this.controller = controller;
		this.messages = new Vector<Packet>();
	}

	/**
	 * Shuts down communication.
	 */
	public void shutdown() {
		if (communication != null) {
			communication.shutdown();	
			communication = null;
		}
	}

	/**
	 * @see org.oversoul.networking.NetworkApplication#errorMessage(String)
	 */
	public void errorMessage(String msg) {
		//System.out.println("ERROR: " + msg);
		Packet p = new TermPacket(ERROR_MSG, ERROR, msg);
		handlePacket(p);
	}
	
	/**
	 * @see org.oversoul.networking.NetworkApplication#systemMessage(String)
	 */
	public void systemMessage(String msg) {
		//System.out.println("SYSTEM: " + msg);
		Packet p = new InitPacket(MESSAGE, SYSTEM, msg);
		handlePacket(p);
		p = new GamePacket(GAME_MSG, SYSTEM, msg);
		handlePacket(p);
	}

	/**
	 * @see org.oversoul.networking.NetworkApplication#receiveMessage(byte[])
	 */
	public void receiveMessage(byte[] packet) {
		// check previous messages
		while (messages.size() > 0) {
			Packet p = messages.remove(0);
			handlePacket(p);
		}
		if ((packet != null) && (packet.length > 0)) {
			Packet p = null;
			byte type = packet[0];
			//System.out.println("Packet type: " + type);
			if (type >= GAME_START) {
				p = new GamePacket(packet);
			} else if (type >= DROPPED) {
				p = new TermPacket(packet);
			} else  if (type >= CONNECT) {
				p = new InitPacket(packet);
			}
			handlePacket(p);
		} else {
			String s = ((packet == null) ? "null" : "empty");
			System.err.println(s + " packet");	
		}
	}
	
	/**
	 * Receives a packet. Saves it if the ui isn't available to handle it.
	 * @param p 	packet.
	 */
	private void handlePacket(Packet p) {
		if (p != null) {
			System.out.println("Received: " + p);
			if (p instanceof InitPacket) {
				InitPacket ip = (InitPacket) p;
				if (mpFrame != null) {
					handleInitMessage(ip);
				} else {
					messages.add(p);	
				}
			} else if (p instanceof GamePacket) {
				GamePacket gp = (GamePacket) p;
				if (controller != null) {
					handleGameMessage(gp);
				} else {
					messages.add(p);	
				}
			} else if (p instanceof TermPacket) {
				TermPacket tp = (TermPacket) p;
				handleTerminationMessage(tp);
			}
		}
	}

	/**
	 * Handles an initialization message.
	 * @param p			the incoming init packet.
	 */
	private void handleInitMessage(InitPacket p) {
		switch (p.getType()) {
			case CONNECT :
				mpFrame.handleConnect(p.getPlayerNumber(), 
									  p.getLevel(), p.isFixedLevel(),  
									  p.getLines(), p.isFixedLines(), 
									  p.getSpeed(), p.isFixedSpeed(),  
									  p.isPenalties(), 
									  p.getWins(), p.getLosses(), 
									  p.getTetrisTotal(),
									  p.getVersion(), p.getName());
				break;
			case ACCEPT :
				mpFrame.handleAccept(p.getPlayerNumber(), 
									 p.getLevel(), p.isFixedLevel(),  
									 p.getLines(), p.isFixedLines(), 
									 p.getSpeed(), p.isFixedSpeed(),  
									 p.isPenalties(), 
									 p.getWins(), p.getLosses(), 
									 p.getTetrisTotal(),
									 p.getVersion(), p.getName());
				break;
			case REFUSE :
				mpFrame.handleRefuse(ERROR, p.getReason());
				break;
			case SETTINGS :
				mpFrame.changeSettings(p.getLevel(), p.isFixedLevel(),  
									   p.getLines(), p.isFixedLines(),
									   p.getSpeed(), p.isFixedSpeed(),  
									   p.isPenalties());
				break;
			case MESSAGE :
				mpFrame.addMessage(p.getPlayerNumber(), p.getMessage());
				break;
			case READY :
				mpFrame.playerReady();
				break;
			case NOT_READY :
				mpFrame.playerNotReady();
				break;
			case START :
				mpFrame.startGame();
				break;	
		}
	}

	/**
	 * Handles a termination message.  This method also checks the underlying 
	 * communication object by writing a few bytes to see if the socket is still
	 * connected.  If the connection has been lost then the handler or client will
	 * be shutdown.
	 * @param p	the incoming packet.
	 */
	private void handleTerminationMessage(TermPacket p) {
		boolean handled = true;
		switch (p.getType()) {
			case DROPPED :
				if (mpFrame != null) {
					mpFrame.dropPlayer(p.getPlayerNumber(), p.getReason());
				}
				break;
			case CANCEL :
				if (mpFrame != null) {
					mpFrame.dropPlayer(p.getPlayerNumber(), null);
				}
				if (controller != null) {
					controller.handleMultiplayerQuit();	
				}
				break;
			case QUIT :
				if (mpFrame != null) {
					mpFrame.dropPlayer(p.getPlayerNumber(), null);	
				}
				if (controller != null) {
					controller.handleMultiplayerQuit();	
				}
				break;
			case ERROR_MSG :
				if (mpFrame != null) {
					mpFrame.addMessage(p.getPlayerNumber(), p.getErrorMessage());
					mpFrame.dropPlayer(p.getPlayerNumber(), p.getErrorMessage());	
				}
				if (controller != null) {
					controller.handleMultiplayerError(p.getPlayerNumber(), 
						p.getErrorMessage());
				}
				break;
			default :
				handled = false;
		}
		if (handled && (communication != null)) {
			boolean okay = communication.checkConnection();
			if (!okay) {
				communication.shutdown();
			}
		}
	}

	/**
	 * Handles a game message.
	 * @param p			the incoming packet.
	 */
	private void handleGameMessage(GamePacket p) {
		switch (p.getType()) {
			case SIMPLE_SCORE :
				controller.handleMultiplayerSimpleScore(p.getPlayerNumber(), 
									p.getScore(), p.getWeightedScore());
				break;
			case FULL_SCORE :
				controller.handleMultiplayerFullScore(p.getPlayerNumber(), p.getScore(), 
					p.getWeightedScore(),p.getLines(), p.getLevel(), p.getTetrisCount(), p.getPenaltyLines());
				break;
			case GAME_START :
				controller.handleMultiplayerStart();
				break;
			case PAUSE :
				controller.handleMultiplayerPause();
				break;
			case RESUME :
				controller.handleMultiplayerResume();
				break;
			case RESTART :
				controller.handleMultiplayerRestart();
				break;
			case END_GAME :
				controller.handleMultiplayerEnd();
				break;
			case GAME_OVER :
				controller.handleMultiplayerGameOver(p.getPlayerNumber(), p.getScore(), 
									p.getWeightedScore(), p.getLines(), p.getLevel(), p.getTetrisCount());
				break;
			case GAME_MSG :
				controller.setMessageText(p.getMessage(), false);
				break;
		}
	}

	//////////////////////////////////////////////////////////////////////
	// SEND Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Sends a packet using the Communication object.
	 * @param packet	the packet to send.
	 */
	private void send(Packet packet) {
		System.out.println("Sending: " + packet);
		if (communication != null) {
			communication.send(packet.toByteArray());	
		} else {
			System.err.println("Connection has been closed.");	
		}
	}

	/**
	 * Sends a connect message.
	 * @param player
	 */
	public void sendConnect(TetrisPlayer player) {
		InitPacket p = new InitPacket(CONNECT, player.getPlayerNumber());
		p.setLevel(player.getStartingLevel());
		p.setLines(player.getStartingLines());
		p.setSpeed(player.getGameSpeed());
		p.setBooleans(player.isFixedLevel(), player.isFixedLines(), player.isFixedSpeed(), player.isPenalties());
		p.setWins(player.getWins());
		p.setLosses(player.getLosses());
		p.setTetrisTotal(player.getTetrisTotal());
		p.setVersion(controller.getVersion().getMajor(), controller.getVersion().getMinor());
		p.setName(player.getName());
		send(p);
	}	
	/**
	 * Sends an accept message.
	 * @param player
	 */
	public void sendAccept(TetrisPlayer player) {
		InitPacket p = new InitPacket(ACCEPT, player.getPlayerNumber());
		p.setLevel(player.getStartingLevel());
		p.setLines(player.getStartingLines());
		p.setSpeed(player.getGameSpeed());
		p.setBooleans(player.isFixedLevel(), player.isFixedLines(), player.isFixedSpeed(), player.isPenalties());
		p.setWins(player.getWins());
		p.setLosses(player.getLosses());
		p.setTetrisTotal(player.getTetrisTotal());
		p.setVersion(controller.getVersion().getMajor(), controller.getVersion().getMinor());
		p.setName(player.getName());
		send(p);
	}	
	/**
	 * Sends a refuse message.
	 * @param reason	the refusal reason.
	 */
	public void sendRefuse(String reason) {
		InitPacket p = new InitPacket(REFUSE, 0, reason);
		send(p);
	}
	/**
	 * Sends a settings change message.
	 * @param player
	 */
	public void sendSettingsChange(TetrisPlayer player) {
		InitPacket p = new InitPacket(SETTINGS, player.getPlayerNumber());
		p.setLevel(player.getStartingLevel());
		p.setLines(player.getStartingLines());
		p.setSpeed(player.getGameSpeed());
		p.setBooleans(player.isFixedLevel(), player.isFixedLines(), player.isFixedSpeed(), player.isPenalties());
		send(p);
	}
	/**
	 * Sends a text message.
	 * @param num	player number.
	 * @param msg	The message to send.
	 */
	public void sendText(int num, String msg) {
		InitPacket p = new InitPacket(MESSAGE, num, msg);
		send(p);
	}	
	/**
	 * Sends a cancel (quit) message.
	 * @param num	the player number.	
	 */
	public void sendCancel(int num) {
		TermPacket p = new TermPacket(CANCEL, num);
		send(p);
	}
	/**
	 * Sends a ready message.
	 * @param num	the player number.	
	 */
	public void sendReady(int num) {
		InitPacket p = new InitPacket(READY, num);
		send(p);
	}
	/**
	 * Sends a not ready message.
	 * @param num	the player number.	
	 */
	public void sendNotReady(int num) {
		InitPacket p = new InitPacket(NOT_READY, num);
		send(p);
	}
	/**
	 * Sends a start message.
	 * @param num	the player number.	
	 */
	public void sendStart(int num) {
		InitPacket p = new InitPacket(START, num);
		send(p);
	}


	//////////////////////////////////////////////////////////////////////
	// GAME Methods
	//////////////////////////////////////////////////////////////////////	
	
	/**
	 * Sends a game start message.
	 * @param num	player number.
	 */
	public void sendGameStart(int num) {
		GamePacket p = new GamePacket(GAME_START, num);
		send(p);	
	}
	/**
	 * Sends a pause game message.
	 * @param num	player number.
	 */
	public void sendPause(int num) {
		GamePacket p = new GamePacket(PAUSE, num);
		send(p);	
	}
	/**
	 * Sends a resume game message.
	 * @param num	player number.
	 */
	public void sendResume(int num) {
		GamePacket p = new GamePacket(RESUME, num);
		send(p);
	}
	/**
	 * Sends a restart message.
	 * @param num	player number.
	 */
	public void sendRestart(int num) {
		GamePacket p = new GamePacket(RESTART, num);
		send(p);	
	}
	/**
	 * Sends a restart message.
	 * @param num	player number.
	 */
	public void sendEnd(int num) {
		GamePacket p = new GamePacket(END_GAME, num);
		send(p);	
	}
	/**
	 * Sends a quit game message.
	 * @param num	player number.
	 */
	public void sendQuit(int num) {
		TermPacket p = new TermPacket(QUIT, num);
		send(p);	
	}
	/**
	 * Sends a game over message.
	 * @param player
	 */
	public void sendGameOver(TetrisPlayer player) {
		GamePacket p = new GamePacket(GAME_OVER, player.getPlayerNumber());
		p.setScore(player.getScore());
		p.setWeightedScore(100);
		p.setLines(player.getLines());
		p.setLevel(player.getLevel());
		p.setTetrisCount(player.getTetrisCount());
		send(p);	
	}
	/**
	 * Sends an error message.
	 * @param num	player number.
	 */
	public void sendError(int num, String error) {
		GamePacket p = new GamePacket(ERROR_MSG, num, error);
		send(p);	
	}
	
	/**
	 * Creates and sends a GamePacket object containing the player number,
	 * score, and weighted score.
	 * @param player
	 */
	public void sendSimpleScore(TetrisPlayer player) {
		GamePacket p = new GamePacket(SIMPLE_SCORE, player.getPlayerNumber());
		p.setScore(player.getScore());
		p.setWeightedScore(player.getWeightedScore());
		send(p);
	}

	/**
	 * Creates and sends a GamePacket object containing the full scoring information.
	 * This includes the player number, score, weighted score, lines, level, 
	 * and any penalty lines.  
	 * @param player the player.
	 */
	public void sendFullScore(TetrisPlayer player) {
		GamePacket p = new GamePacket(FULL_SCORE, player.getPlayerNumber());
		p.setScore(player.getScore());
		p.setWeightedScore(player.getWeightedScore());
		p.setLines(player.getLines());
		p.setLevel(player.getLevel());
		p.setTetrisCount(player.getTetrisCount());
		p.setPenaltyLines(player.getOutgoingPenaltyLines());
		send(p);
		player.setOutgoingPenaltyLines(0);
	}
	

	//////////////////////////////////////////////////////////////////////
	// GETTER/SETTER Methods
	//////////////////////////////////////////////////////////////////////

	/**
	 * Returns the communication.
	 * @return Communication
	 */
	public Communication getCommunication() {
		return communication;
	}

	/**
	 * Sets the communication.
	 * @param communication The communication to set
	 */
	public void setCommunication(Communication communication) {
		this.communication = communication;
	}

	/**
	 * Sets the multiplayer frame.
	 * @param mpFrame
	 */
	public void setMultiplayerFrame(MultiplayerFrame mpFrame) {
		this.mpFrame = mpFrame;	
	}

	//////////////////////////////////////////////////////////////////////
	// CONSTANTS 
	//////////////////////////////////////////////////////////////////////

	// message types
	private static final int ERROR 	= MessageListCellRenderer.ERROR;
	private static final int SYSTEM = MessageListCellRenderer.DEFAULT;

	// connect/initialization phase
	public static final byte CONNECT 		= 0;	// client  -> server
	public static final byte ACCEPT 		= 1;	// client <-  server
	public static final byte REFUSE 		= 2;	// client <-  server
	public static final byte SETTINGS		= 3;	// client <-> server
	public static final byte MESSAGE		= 4;	// client <-> server
	public static final byte READY			= 5;	// client  -> server
	public static final byte NOT_READY		= 6;	// client  -> server
	public static final byte START			= 7;	// client <-  server

	// termination messages
	public static final byte DROPPED		= 10;	
	public static final byte CANCEL			= 11;	
	public static final byte QUIT			= 12;	
	public static final byte ERROR_MSG		= 13;
	
	// game messages
	public static final byte GAME_START		= 20;
	public static final byte PAUSE			= 21;
	public static final byte RESUME			= 22;
	public static final byte RESTART		= 23;
	public static final byte END_GAME		= 24;
	public static final byte GAME_OVER		= 25;
	public static final byte GAME_MSG		= 26;
	
	// game scoring messages
	public static final byte SIMPLE_SCORE	= 30;
	public static final byte FULL_SCORE		= 31;

	// update messages
	public static final byte CHECK_UPDATE	= 50;
	public static final byte NO_UPDATE		= 51;
	public static final byte AVAIL_UPDATE 	= 52;
	public static final byte GET_UPDATE		= 53;
	public static final byte UPDATE_FILES	= 54;
	public static final byte SEND_FILES		= 55;
	public static final byte FILE			= 56;
	public static final byte RECV_FILE		= 57;
	public static final byte RESEND_FILE	= 58;
	public static final byte DONE_UPDATE	= 59;
	public static final byte UPDATE_ERROR	= 60;
		
}
