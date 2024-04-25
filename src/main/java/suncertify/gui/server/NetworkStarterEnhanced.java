package suncertify.gui.server;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

import suncertify.db.LoggerControl;
import suncertify.sockets.server.SocketServerEnhanced;

/**
 * 
 * Starts the enhanced server that accepts connections over sockets. 
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class NetworkStarterEnhanced {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.server.NetworkStarterEnhanced</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.server.NetworkStarterEnhanced"),
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
	 * A reference to the enhanced server.
	 */
	SocketServerEnhanced serverEnhanced = null;
	
	/**
	 * 
	 * Constructor to create an enhanced server version.
	 * <br> The constructor calls the static method <code>register</code>
	 * of the class 
	 * <code>suncertify.sockets.server.SocketServerEnhanced</code>,
	 * which instantiates an object of type <code>java.net.ServerSocket</code>.
	 *
	 * @param dbLocation
	 *            The location of the client file on a local hard drive.
	 * @param portPara
	 *            The port number the socket server will listen on.
	 * @param status
	 *            A label on the server GUI we can update with our status.
	 * problems.
	 * @param runExitLockCtrl Locks will be released automatically,
	 * if a client disconnects to the server.
	 * @param passWord The password of the database.
	 */
	public NetworkStarterEnhanced(
			final String dbLocation, 
			final String portPara, 
			final JLabel status, 
			final boolean runExitLockCtrl,
			final String passWord) {
		
		this.log.entering("NetworkStarterEnhanced", "NetworkStarterEnhanced",
				new Object[] { dbLocation, portPara, status });
		try {
			this.port = Integer.parseInt(portPara);
			this.log.info("Socket: Starting Server on port " + portPara);
			status.setForeground(Color.CYAN);
			this.serverEnhanced = 
					SocketServerEnhanced.register(dbLocation
							, this.port
							, runExitLockCtrl, passWord);

			this.log.info("Socket - Server started.");
			status.setText(NetworkStarterEnhanced.SERVER_RUNNING);
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
	 * @return SocketServerEnhanced - The server.
	 */
	public SocketServerEnhanced getServerEnhancedObject() {
		return this.serverEnhanced;
	}	
}
