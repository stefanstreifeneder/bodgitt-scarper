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
 * An object of the class represents the remote server.
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
 * @see RecordDatabaseSocketRequest
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RecordDatabaseSocketServer extends Thread {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.server.RecordDatabaseSocketServer</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.sockets.server.RecordDatabaseSocketServer"), 
			Level.ALL);
	
	/**
	 * The path to the database.
	 */
	private final String dbLocation;

	/**
	 * The password of the database.
	 */
	private final String password;
	
	/**
	 * Stores port number.
	 */
	private int port = 3000;

	/**
	 * The server.
	 */
	ServerSocket serverSocket;

	/**
	 * Creates a socket server, listening on the specified port, accessing the
	 * specified database file. Private access, because instantiation of
	 * an object of this class is controlled by the method <code>register</code>.
	 *
	 * @param dbLocationParam
	 *            The location of the client file on disk.
	 * @param portParam
	 *            The port to listen on.
	 * @param passWord The password of the database
	 */
	private RecordDatabaseSocketServer(final String dbLocationParam, 
			final int portParam, final String passWord) {
		this.log.entering("RecordDatabaseSocketServer", 
				"RecordDatabaseSocketServer",
				new Object[] { dbLocationParam, Integer.valueOf(portParam) });
		this.dbLocation = dbLocationParam;
		this.port = portParam;
		this.password = passWord;
		this.log.exiting("RecordDatabaseSocketServer", 
				"RecordDatabaseSocketServer");
	}	
	
	/**
	 * To start the server as a command line client.
	 * 
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(final String[] args){
//		RecordDatabaseSocketServer.register(System.getProperty(
//				"user.dir") + File.separator + "db-2x2.db", 3000);
		Logger.getAnonymousLogger().severe("RecordDatabaseSocketServer, main,"
				+ " server is started, file: " + "user.dir" 
				+ File.separator +"db-2x2.db"
				+ " - port: " + 3000);
	}

	/**
	 * Returns and starts the registered socket server, listening on the 
	 * specified port, accessing the specified data file. 
	 *
	 * @param dbLocation
	 *            The location of the client file on disk.
	 * @param port
	 *            The port to listen on.
	 * @param password The password of the database.
	 * @return RecordDatabaseSocketServer - The server.
	 */
	public static RecordDatabaseSocketServer register(final String dbLocation, 
			final int port, final String password) {
		final RecordDatabaseSocketServer recordDatabaseSocketServer = new RecordDatabaseSocketServer(dbLocation, port,
				password);
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
				RecordDatabaseSocketRequest request =
						new RecordDatabaseSocketRequest(
								this.dbLocation, clientSocket, this.password);
				request.start();
				// only logging data
				final Date d = new Date(System.currentTimeMillis());
				final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
				final String formattedDate = sdf.format(d);
				this.log.info("RecordDatabaseServerSocket, listenForConnections, " + " client: "
						+ clientSocket.toString() + "- at: " + formattedDate);
			}
		} catch (final Exception ex) {
			this.log.severe("RecordDatabaseSocketSever, listenForConnections, Exception, " 
					+ "server closed: "
					+ this.serverSocket.isClosed() + " - Exception: " 
					+ ex.getLocalizedMessage());

		} finally {
			this.log.severe("RecordDatabaseSocketSever, listenForConnections, " 
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
			this.log.severe("RecordDatabaseSocketServer, " + "closeClientsAndServer, server closed: "
					+ this.serverSocket.isClosed());
		} catch (final IOException ex) {
			this.log.warning("RecordDatabaseSocketServer, " + "closeClientsAndServer, server, Exception: "
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
			this.log.severe("RecordDatabaseSocketServer, run, " + ioe.getLocalizedMessage());
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
