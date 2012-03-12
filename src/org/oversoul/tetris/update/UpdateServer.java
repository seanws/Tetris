/**
 * FileServer.java
 * 
 * Created on 23-Jan-05
 */
package org.oversoul.tetris.update;

import java.net.URL;
import java.net.URLDecoder;

import org.oversoul.networking.NetworkApplication;
import org.oversoul.networking.Server;
import org.oversoul.util.Version;

/**
 * The FileServer class runs a server which allows connections
 * to update to a new version of Tetris.
 * 
 * @author 		nyef
 * @date		23-Jan-05, 12:34:13 PM
 * @version 	1.0
 */
public class UpdateServer implements NetworkApplication {

	private Version version = null;
	private Server server = null;

	/**
	 * Constructor for FileServer.java
	 */
	public UpdateServer(Version version) {
		this.version = version;
	}

	public static void main(String[] args) {
		try {
			URL propsFile = UpdateServer.class.getResource("/version.properties");
			String file = URLDecoder.decode(propsFile.getFile(), "US-ASCII");
			Version version = new Version(file);
			UpdateServer updateServer = new UpdateServer(version);
			updateServer.startServer();
		} catch (Exception ex) {
			ex.printStackTrace();	
		}
	}

	/**
	 * Starts the update server.
	 */
	private void startServer() {
		try {
			server = new Server(version.getPort(), this);
			server.start();
			Thread.sleep(100);	// pause to let server start
		} catch (Exception ex) {
			ex.printStackTrace();
			stopServer();	
		}
	}
	
	/**
	 * Stops the server.
	 */
	private void stopServer() {
		if ((server != null) && server.isRunning()) {
			server.shutdown();
			server = null;	
		}
	}

	/**
	 * @see org.oversoul.networking.NetworkApplication#errorMessage(java.lang.String)
	 */
	public void errorMessage(String msg) {

	}

	/**
	 * @see org.oversoul.networking.NetworkApplication#receiveMessage(byte[])
	 */
	public void receiveMessage(byte[] packet) {

	}

	/**
	 * @see org.oversoul.networking.NetworkApplication#systemMessage(java.lang.String)
	 */
	public void systemMessage(String msg) {

	}

}
