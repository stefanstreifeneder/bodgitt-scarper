package suncertify.direct.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;
import suncertify.db.RecordDatabase;

/**
 * A <code>suncertify.direct.admin.DirectConnector_Admin</code> is used in 
 * cases where the client is an Admin Client, who wants to make a local 
 * connection to the database file. The classes uses the factory design 
 * pattern to create an object to connect to the database.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class DirectConnector_Admin {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.direct.admin.DirectConnector_Admin</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		DirectConnector_Admin.log = LoggerControl
				.getLoggerBS(Logger.getLogger("suncertify.direct.admin.DirectConnector_Admin"), Level.ALL);
	}

	/**
	 * This is an utility class. It only exists for other classes to call
	 * it's static methods.
	 */
	private DirectConnector_Admin() {
		// unused
	}

	/**
	 * Static method that gets a database handle. The
	 * <code>suncertify.db.InterfaceClient_Admin</code> is a local object.
	 * The method argument is the path to the database file.
	 *
	 * @param dbLocation
	 *            The path to the database on disk.
	 * @param password The password of the database.
	 * @return InterfaceClient_Admin - Access to the database file.
	 * @throws IOException
	 *             Thrown if transmission problems occur. <br>
	 * 
	 */
	public static InterfaceClient_Admin getLocal(String dbLocation, String password) throws IOException {
		DirectConnector_Admin.log.entering("DirectConnector_Admin", "getLocal", dbLocation);
		InterfaceClient_Admin db = new RecordDatabase(dbLocation, password);
		DirectConnector_Admin.log.exiting("DirectConnector_Admin", "getLocal", db);
		return db;
	}
		
	/**
	 * Static method that gets a database handle. It assumes the database file
	 * resides in the currently working  directory. The 
	 * <code>suncertify.db.InterfaceClient_Admin</code> accesses the database 
	 * file in a local way. 
	 * 
	 * @return InterfaceClient_Admin - Access to the database file.
	 * @throws IOException
	 *             Thrown if there are transmission problems.
	 * 
	 */
	public static InterfaceClient_Admin getLocal() throws IOException {
		DirectConnector_Admin.log.entering("DirectConnector_Admin", "getLocal");
		InterfaceClient_Admin db = new RecordDatabase();
		DirectConnector_Admin.log.exiting("DirectConnector_Admin", "getLocal", db);
		return db;
	}
}
