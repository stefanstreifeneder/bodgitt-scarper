package suncertify.sockets.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;

/**
 * An object of the class represents the remote enhanced server.
 * The class is a subclass of class <code>java.lang.Thread</code>. An object
 * of the class is instantiated and started in the static marked 
 * method <code>register</code>.
 * <br>
 * <br>
 * To close the server the class supplies the method 
 * <code>closeServer</code>.<br> 
 * <br> 
 * For tests the class supplies the method <code>main</code> to start
 * the server.
 * 
 * @see SocketRequestEnhanced
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketServerEnhanced extends Thread {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.server.SocketServerEnhanced</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.sockets.server.SocketServerEnhanced"), 
			Level.ALL);
	
	/**
	 * The path to the database.
	 */
	private String dbLocation;

	/**
	 * The password of the database.
	 */
	private String password;

	/**
	 * Stores port number.
	 */
	private int port = 3000;
	
	/**
	 * True, if exit control is enabled.
	 */
	private boolean exitControl = false;

	/**
	 * The server socket.
	 */
	ServerSocket serverSocket;
	
	/**
	 * Constructs an enhanced server version, which unlocks Records locked by clients,
	 * who are offline. Calls the other overloaded constructor.
	 *
	 * @param dbLocationParam
	 *            The location of the client file on disk.
	 * @param portParam
	 *            The port to listen on.
	 * @param lockExitCtrl True, if locks were released, 
	 * if a client goes online.
	 * a search by criteria.
	 * @param passWord The password of the database.
	 * 
	 */
	private SocketServerEnhanced(final String dbLocationParam
			, final int portParam
			, final boolean lockExitCtrl
			, final String passWord) {
		this.log.entering("SocketServerEnhanced", 
				"SocketServerEnhanced",
							new Object[] { dbLocationParam, Integer.valueOf(portParam) });
		this.dbLocation = dbLocationParam;
		this.port = portParam;
		this.password = passWord;		
		this.exitControl = lockExitCtrl;
		this.log.exiting("SocketServerEnhanced", 
				"SocketServerEnhanced");
	}
	
	
	/**
	 * To start the server as a command line client.
	 * 
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(final String[] args){
//		SocketServerEnhanced.register(System.getProperty(
//				"user.dir")+ File.separator +"db-2x2.db", 3000, true);
		Logger.getAnonymousLogger().severe("SocketServerEnhanced, main,"
				+ " server is started, file: " + "user.dir" 
				+ File.separator +"db-2x2.db"
				+ " - port: " + 3000);
	}
	
	
	/**
	 * Returns and starts the enhanced version of the registered socket server, 
	 * listening on the specified port, accessing the
	 * specified data file. 
	 * 
	 * 
	 * @param dbLocation
	 *            The location of the client file on disk.
	 * @param port
	 *            The port to listen on.
	 * will be updated or deleted.
	 * @param isExitControl True, if locks were released, 
	 * if a client goes online.
	 * @param passWord The password of the database.
	 * 
	 * @return RecordDatabaseSocketServer - The enhanced server object.
	 */
	public static SocketServerEnhanced register(final String dbLocation
			, final int port
			, final boolean isExitControl
			, final String passWord) {
		
		final SocketServerEnhanced recordDatabaseSocketServer = 
				new SocketServerEnhanced(dbLocation
						, port
						, isExitControl
						, passWord);		
		recordDatabaseSocketServer.start();
		return recordDatabaseSocketServer;
	}

	/**
	 * Listens for new client connections, starts a thread to handle the
	 * requests.
	 * 
	 */
	private void listenForConnections() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			Socket clientSocket = null;
			while (true) {
				clientSocket = this.serverSocket.accept();
				SocketRequestEnhanced requestAlt = null;				
				requestAlt = new SocketRequestEnhanced(
										this.dbLocation, clientSocket, 
														this.exitControl, this.password);
				requestAlt.start();
				
				// only logging data
				final Date d = new Date(System.currentTimeMillis());
				final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				final String formattedDate = sdf.format(d);
				this.log.info("SocketServerEnhanced, listenForConnections, " + " client: "
						+ clientSocket.toString() + "- at: " + formattedDate);
			}
		} catch (final Exception ex) {
			this.log.severe("SocketServerEnhanced, listenForConnections, Exception, " 
					+ "server closed: "
					+ this.serverSocket.isClosed() + " - Exception: " 
					+ ex.getLocalizedMessage());

		} finally {
			this.log.severe("SocketServerEnhanced, listenForConnections, " 
					+ "finally,  server closed: "
					+ this.serverSocket.isClosed());
			
			// if we come here, we know, this is the end of the session
			// therefore will close the socket
			if (this.serverSocket != null) {
				if (!this.serverSocket.isClosed()) {
					this.closeServer();
				}
			}
		}
	}

	/**
	 * Closes the <code>java.net.ServerSocket</code> object.
	 */
	public void closeServer() {
		try {
			this.serverSocket.close();
			this.log.severe("SocketServerEnhanced, " + "closeClientsAndServer, server closed: "
					+ this.serverSocket.isClosed());
		} catch (final IOException ex) {
			this.log.warning("SocketServerEnhanced, " + "closeClientsAndServer, server, Exception: "
					+ ex.getLocalizedMessage());
		}
	}

	/**
	 * Listens for connections, handling any errors.
	 */
	@Override
	public void run() {
		try {
			listenForConnections();
		} catch (final Exception ioe) {
			this.log.severe("SocketServerEnhanced, run, " + ioe.getLocalizedMessage());
			// An BindException caused by an already used port is handled
			// in suncertify.gui.ConfigOptions.validateAll
			JOptionPane.showMessageDialog(null, "Server exits!" 
					+ "\nYou have to start the application again."
					+ "\nPlease give attention to the error message:" 
					+ "\n" + ioe.getLocalizedMessage());
			System.exit(0);
		}
	}
}
