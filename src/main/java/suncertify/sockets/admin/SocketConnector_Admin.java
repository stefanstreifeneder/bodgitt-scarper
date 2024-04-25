package suncertify.sockets.admin;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;

/**
 * A <code>suncertify.sockets.admin.SocketConnector_Admin</code> is used 
 * in cases where the Admin client wants to make a network connection.
 * The classes uses the factory design pattern to create an object 
 * to connect to the database.
 *
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketConnector_Admin {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.admin.SocketConnector_Admin</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		SocketConnector_Admin.log = 
				LoggerControl.getLoggerBS(
						Logger.getLogger("suncertify.sockets.admin.SocketConnector_Admin"), 
						Level.ALL);
	}

	/**
	 * Since this is a utility class (it only exists for other classes to call
	 * it's static methods), stops users creating unneeded instances of this
	 * class by creating a private constructor.
	 */
	private SocketConnector_Admin() {
		//
	}

	/**
	 * Static method to create a connection to the database. The
	 * <code>suncertify.db.InterfaceClient_Admin</code> is a remote object.
	 *
	 * @param hostname
	 *            The ipaddress of the host machine.
	 * @param port
	 *            The socket port the server is listening on.
	 * @return InterfaceClient_Admin - A connection to the database.
	 * @throws UnknownHostException
	 *             When the host is unreachable or cannot be resolved.
	 * @throws IOException
	 *             If a communication error occurs trying to connect to the
	 *             host.
	 */
	public static InterfaceClient_Admin getRemote(String hostname, String port)
			throws UnknownHostException, IOException {
		SocketConnector_Admin.log.entering("RecordConnector", "getRemote", new Object[] { hostname, port });
		InterfaceClient_Admin db = new SocketClient_Admin(hostname, port);
		SocketConnector_Admin.log.exiting("RecordConnector", "getLocal", db);
		return db;
	}
}
