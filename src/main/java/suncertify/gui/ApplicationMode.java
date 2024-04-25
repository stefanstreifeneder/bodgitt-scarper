package suncertify.gui;

/**
 * Specifies the modes the application can run in.<br>
 * These are:<br>
 * LOCAL_CLIENT - local access to the database file - cmd: "alone"<br>
 * NETWORK_CLIENT - accesses the database file in a network 
 * environment - cmd: ""<br>
 * SERVER - server in a network environment - cmd: "server"
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public enum ApplicationMode {

	/**
	 * Application will be a local client - no network access.
	 */
	LOCAL_CLIENT,

	/**
	 * The server application.
	 */
	SERVER,

	/**
	 * A networked client via Server Socket. This is used when the user has not
	 * specified any command line parameters when starting the application, so
	 * we know that we are going to be making a network connection.
	 */
	NETWORK_CLIENT;

	/**
	 * Constructor, which is never called directly.
	 */
	ApplicationMode() {
		// never called directly
	}
}
