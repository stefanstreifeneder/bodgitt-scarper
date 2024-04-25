package suncertify.sockets.server;

/**
 * An object of this class is used in 
 * <code>suncertify.sockets.server.ExitControl</code>. It cares
 * to release the right locks, when a client cancels connection in network mode.
 * <br>
 * An object of this class reflects a lock operation, if the method
 * <code>lockRecord</code> of the interface 
 * <code>suncertify.db.DBAccess</code> was called.
 * <br>
 * An object of this class has three properties: <br>
 * recNo - type <code>java.lang.Long</code>, the Record number <br>
 * cookie - type <code>java.lang.Long</code>, the value for update and deleting operations <br>
 * client - type <code>java.net.Socket</code>, represents the client, who has the lock
 * 
 * @see ExitControl
 * @author stefan.streifeneder@gmx.de
 */
public class ExitCookie {

	/**
	 * Stores the Record number.
	 */
	private final Long recNo;

	/**
	 * Stores the necessary lock cookie for update, deleting and unlock
	 * operations.
	 */
	private final Long cookie;

	/**
	 * Stores the client.
	 */
	private final Thread client;

	/**
	 * Constructs an object of this class. 
	 * 
	 * @param recNoParam
	 *            The Record number of the locked Record.
	 * @param cookieParam
	 *            The lock cookie for update, delete and unlock operation.
	 * @param clientParam
	 *            The client, represented by a Thread object.
	 */
	public ExitCookie(final Long recNoParam, final Long cookieParam, 
			final Thread clientParam) {
		this.recNo = recNoParam;
		this.cookie = cookieParam;
		this.client = clientParam;
	}

	/**
	 * Returns the Record number.
	 * 
	 * @return Long - Record number of the locked Record.
	 */
	public Long getRecNo() {
		return this.recNo;
	}

	/**
	 * Returns the cookie.
	 * 
	 * @return Long - The cookie for update, delete and unlock operation.
	 */
	public Long getCookie() {
		return this.cookie;
	}

	/**
	 * Returns the client.
	 * 
	 * @return Thread - The client, represented by a <code>java.lang.Thread</code> object.
	 */
	public Thread getClient() {
		return this.client;
	}

	/**
	 * Overrides the <code>toString</code> method of <code>java.lang.Object</code>.
	 * 
	 * @return String - A String representation of an object of this class.
	 */
	@Override
	public String toString() {
		return "ExitCookie: recNo: " + this.recNo + " - client: " + this.client + " - cookie: " + this.cookie;
	}
}
