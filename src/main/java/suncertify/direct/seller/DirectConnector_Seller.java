package suncertify.direct.seller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Seller;
import suncertify.db.LoggerControl;
import suncertify.db.RecordDatabase;

/**
 * A <code>suncertify.direct.seller.DirectConnector_Seller</code> 
 * is used in cases where a Seller client wants to make a local 
 * connection to the database file. The classes uses the factory design 
 * pattern to create an object to connect to the database.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class DirectConnector_Seller {
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.direct.seller.DirectConnector_Seller</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		DirectConnector_Seller.log = LoggerControl
				.getLoggerBS(Logger.getLogger("suncertify.direct.seller.DirectConnector_Seller"), Level.ALL);
	}

	/**
	 * This is an utility class. It only exists for other classes to call
	 * it's static methods.
	 */
	private DirectConnector_Seller() {
		// unused
	}

	/**
	 * Static method that gets a database handle. The
	 * <code>suncertify.db.InterfaceClient_Seller</code> accesses the database file in a local
	 * way. The method argument is the path to the database file.
	 *
	 * @param dbLocation
	 *            The path to the database on disk.
	 * @param password The password of the database.
	 * @return InterfaceClient_Seller - Access to the database file.
	 * @throws IOException
	 *             Thrown if there are transmission problems.
	 * 
	 */
	public static InterfaceClient_Seller getLocal(String dbLocation,
			String password) throws IOException {
		InterfaceClient_Seller db = new RecordDatabase(dbLocation, password);
		return db;
	}
	
	
	/**
	 * Static method that gets a database handle, which resides in the currently working 
	 * directory. The <code>suncertify.db.InterfaceClient_Seller</code> 
	 * accesses the database file in a local
	 * way. 
	 * 
	 * @return InterfaceClient_Seller - Access to the database file.
	 * @throws IOException
	 *             Thrown if there are transmission problems.
	 * 
	 */
	public static InterfaceClient_Seller getLocal() throws IOException {
		DirectConnector_Seller.log.entering("DirectConnector_Seller", "getLocal");
		InterfaceClient_Seller db = new RecordDatabase();
		DirectConnector_Seller.log.exiting("DirectConnector_Seller", "getLocal", db);
		return db;
	}
}
