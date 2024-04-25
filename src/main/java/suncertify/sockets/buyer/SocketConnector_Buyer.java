package suncertify.sockets.buyer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.LoggerControl;

/**
 * A <code>suncertify.sockets.buyer.SocketConnector_Buyer</code> 
 * is used in cases where the Buyer client wants to make a network connection.
 * The classes uses the factory design pattern to create an object 
 * to connect to the database.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketConnector_Buyer {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.buyer.SocketConnector_Buyer</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		SocketConnector_Buyer.log = 
				LoggerControl.getLoggerBS(Logger.getLogger(
						"suncertify.sockets.buyer.SocketConnector_Buyer"), 
						Level.ALL);
	}

	/**
	 * Since this is an utility class (it only exists for other classes to call
	 * it's static methods), stops users creating unneeded instances of this
	 * class by creating a private constructor.
	 */
	private SocketConnector_Buyer() {
		//unused
	}

	/**
	 * Static method to create a connection to the database.
	 *
	 * @param hostname
	 *            The ipddress of the host machine.
	 * @param port
	 *            The socket port the server is listening on.
	 * @return InterfaceClient_Buyer - A connection to the database.
	 * @throws UnknownHostException
	 *             When the host is unreachable or cannot be resolved.
	 * @throws IOException
	 *             If a communication error occurs trying to connect to the
	 *             host.
	 */
	public static InterfaceClient_Buyer getRemote(String hostname, String port)
			throws UnknownHostException, IOException {
		SocketConnector_Buyer.log.entering("RecordConnector", "getRemote", new Object[] { hostname, port });
		InterfaceClient_Buyer db = new SocketClient_Buyer(hostname, port);
		SocketConnector_Buyer.log.exiting("RecordConnector", "getLocal", db);
		return db;
	}
}
