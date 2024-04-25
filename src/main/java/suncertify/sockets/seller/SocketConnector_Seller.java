package suncertify.sockets.seller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Seller;
import suncertify.db.LoggerControl;

/**
 * A <code>suncertify.sockets.seller.SocketConnector_Seller</code> 
 * is used in cases where the Seller client wants to make a 
 * network connection. This class generates an object to
 * access the database in a network environment. The classes uses 
 * the factory design pattern to create an object to connect to 
 * the database.
 *
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketConnector_Seller {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.seller.SocketConnector_Seller</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		SocketConnector_Seller.log = 
				LoggerControl.getLoggerBS(
						Logger.getLogger(
								"suncertify.sockets.seller.SocketConnector_Seller"), 
						Level.ALL);
	}

	/**
	 * Since this is an utility class (it only exists for other classes to call
	 * it's static methods), stops users creating unneeded instances of this
	 * class by creating a private constructor.
	 */
	private SocketConnector_Seller() {
		//
	}

	/**
	 * Static method to create a connection to the database. The
	 * InterfaceClient_Seller is a remote object.
	 *
	 * @param hostIpaddress
	 *            The ipaddress of the host machine.
	 * @param port
	 *            The socket port the server is listening on.
	 * @return InterfaceClient_Seller - A connection to the database.
	 * @throws UnknownHostException
	 *             When the host is unreachable or cannot be resolved.
	 * @throws IOException
	 *             If a communication error occurs trying to connect to the
	 *             host.
	 */
	public static InterfaceClient_Seller getRemote(String hostIpaddress, String port)
			throws UnknownHostException, IOException {
		SocketConnector_Seller.log.entering("RecordConnector", "getRemote", new Object[] { hostIpaddress, port });
		InterfaceClient_Seller db = new SocketClient_Seller(hostIpaddress, port);
		SocketConnector_Seller.log.exiting("RecordConnector", "getLocal", db);
		return db;
	}
}
