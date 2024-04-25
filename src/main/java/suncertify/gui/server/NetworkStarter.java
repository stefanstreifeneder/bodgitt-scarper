package suncertify.gui.server;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

import suncertify.db.LoggerControl;
import suncertify.sockets.server.RecordDatabaseSocketServer;

/**
 * 
 * Starts the server that accepts connections over sockets. 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class NetworkStarter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.server.NetworkStarter</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.server.NetworkStarter"),
			Level.ALL);

	/**
	 * An error code to be passed back to the operating system itself if the
	 * port number provided is invalid.
	 */
	public static final int ERR_CODE_INVALID_PORT_NUMBER = -1;

	/**
	 * Status messages about the server, whether he is running or not.
	 */
	private static final String SERVER_RUNNING = "Socket: Server running.";

	/**
	 * Stores the port number.
	 */
	private int port;

	/**
	 * A reference to the server.
	 */
	RecordDatabaseSocketServer socketServer = null;

	/**
	 * Creates a new instance of the class.
	 * Will be called of the class
	 * <code>suncertify.gui.server.ServerWindow</code>.
	 * <br> The constructor calls the static method <code>register</code>
	 * of the class 
	 * <code>suncertify.sockets.server.RecordDatabaseSocketServer</code>,
	 * which instantiates an object of type <code>java.net.ServerSocket</code>.
	 *
	 * @param dbLocation
	 *            The location of the client file on a local hard drive.
	 * @param portPara
	 *            The port number the socket server will listen on.
	 * @param status
	 *            A label on the server GUI we can update with our status.
	 * @param passWord The password of the database.
	 */
	public NetworkStarter(
			final String dbLocation, 
			final String portPara, 
			final JLabel status,
			final String passWord) {
		
		this.log.entering("NetworkStarter", "NetworkStarter",
				new Object[] { dbLocation, portPara, status });
		try {
			this.port = Integer.parseInt(portPara);
			this.log.info("Socket: Starting Server on port " + portPara);
			status.setForeground(Color.CYAN);
			this.socketServer = 
					RecordDatabaseSocketServer.register(dbLocation, this.port, passWord);
			this.log.info("Socket - Server started.");
			status.setText(NetworkStarter.SERVER_RUNNING);
		} catch (final NumberFormatException e) {
			this.log.log(Level.SEVERE, "Wrong port number", e);
			// this should never happen, since we are taking pains to ensure
			// that only numbers can be entered into the text field. But
			// just in case ...
			System.exit(ERR_CODE_INVALID_PORT_NUMBER);
		}
	}

	/**
	 * Returned object represents the server. The object is
	 * used at the end of a session to close the socket.
	 * 
	 * @return RecordDatabaseSocketServer - The server.
	 */
	public RecordDatabaseSocketServer getSocketServer() {
		return this.socketServer;
	}
}
