package suncertify.gui.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;

/**
 * If the application runs as a server, the class ensures safely exiting the
 * application. The class is subtype of class <code>java.lang.Thread</code>.
 * Its <code>run</code> method will be executed, when the
 * application is shut down running as a server.
 * 
 * 
 * @see ServerWindow
 * @author stefan.streifeneder@gmx.de
 */
public class CleanExit extends Thread {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.server.CleanExit</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.server.CleanExit"), Level.ALL);
		
	/**
	 * Stores the location of the database file.
	 */
	private static String location = "";
	

	/**
	 * Creates an object of the class. The instance executes at the end 
	 * of the session. 
	 * <br> 
	 * Beware the class variable <code>location</code> has to be initialized
	 * by the setter method called <code>setLocation</code>.   
	 * 
	 * @param loc Path to the database file.
	 */
	public CleanExit(String loc) {
		this.log.entering("CleanExit", "CleanExit");
		location = loc;
		this.log.exiting("CleanExit", "CleanExit");
	}

	/**
	 * This method will be executed by the JVM when the application is shutdown.
	 * It ensures that the database is in a clean state for shutdown (that is,
	 * there are no outstanding writes occurring).
	 */
	@Override
	public void run() {
		this.log.entering("CleanExit", "run");
		try {
			this.log.severe("CleanExit, run, RecordDatabase calls saveExit" + location);
			//new RecordDatabase(location, "").saveExit();
		} catch (Exception e) {
			this.log.log(Level.SEVERE, "Failed to lock database before exiting", e);
		}
		this.log.exiting("CleanExit", "run");
	}
}
