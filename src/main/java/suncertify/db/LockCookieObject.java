package suncertify.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An object of the class represents a lock event. The event happens,
 * when the method <code>lockRecord</code> of class 
 * <code>suncertify.db.LockManager</code> is called.
 * <br>
 * <br>
 * An instance of the class has two properties: 
 * <br>
 * <code>lockCookie</code> - is an identification to update or to 
 * delete a Record
 * <br> 
 * <code>client</code> - object represents a client.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class LockCookieObject {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.LockCookieObject</code>. Note the <code>java.util.Logger</code>
	 * instance is customized by the class <code>suncertify.db.LoggerControl</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.db.LockCookieObject"), Level.ALL);

	/**
	 * Stores the value of the lock cookie.
	 */
	private final long lockCookie;

	/**
	 * Represents a client.
	 */
	private final Thread client;	
	
	/**
	 * Creates an object the class.
	 * 
	 * 
	 * @param lockCookieParam
	 *            The lock cookie.
	 * @param t Representing the client.
	 */
	public LockCookieObject(final long lockCookieParam, final Thread t) {
		this.log.entering("LockCookieObject", "LockCookieObject",
				new Object[] {Long.valueOf(lockCookieParam), t});
		this.lockCookie = lockCookieParam;
		this.client = t;
		this.log.exiting("LockCookieObject", "LockCookieObject");
	}

	/**
	 * Returns the lock cookie.
	 * 
	 * @return long - The lock cookie.
	 */
	public long getLockCookie() {
		return this.lockCookie;
	}

	/**
	 * Returns the client.
	 * 
	 * @return Thread - The client.
	 */
	public Thread getClient() {
		return this.client;
	}

	/**
	 * Overrides the <code>equal</code> method of <code>java.lang.Object</code> to
	 * recognize objects as equal.
	 * 
	 * @param o
	 *            The object, which will be tested against an object of this
	 *            class.
	 * @return boolean - Indicates, whether the objects are equal or not.
	 */
	@Override
	public boolean equals(final Object o) {
		this.log.entering("LockCookieObject", "equals", o);
		boolean equal = false;
		if (o instanceof LockCookieObject) {
			if (((LockCookieObject) o).getLockCookie() == this.lockCookie) {
				if (((LockCookieObject) o).getClient().equals(this.client)) {
					equal = true;
				}
			}
		} else {
			equal = false;
		}
		this.log.exiting("LockCookieObject", "equals", Boolean.valueOf(equal));
		return equal;
	}

	/**
	 * Overrides the <code>hashCode</code> method of <code>java.lang.Object</code>.
	 * 
	 * @return int - Returns a hash code value for the object.
	 */
	@Override
	public int hashCode() {
		this.log.entering("LockCookieObject", "hashCode");
		final String num = Long.valueOf(this.lockCookie).toString();
		final int hash = Integer.parseInt(num);
		this.log.entering("LockCookieObject", "hashCode", Integer.valueOf(hash));
		return hash;
	}

	/**
	 * Overrides the <code>toString</code> method of <code>java.lang.Object</code>.
	 * 
	 * @return String - A String representation of an object of this class.
	 */
	@Override
	public String toString() {
		return "LockCookieObject, toString, lockCookie: " 
				+ this.lockCookie + " - thread: "
				+ this.client.toString();
	}
}
