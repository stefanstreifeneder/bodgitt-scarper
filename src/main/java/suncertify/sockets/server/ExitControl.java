package suncertify.sockets.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;
import suncertify.db.RecordNotFoundException;

/**
 * The class handles the situation, when a client goes offline
 * and did not release a lock on a Record. It stores all clients,
 * who hold a lock on a Record, the according Record number
 * and the lock cookie in a list. The three values are stored
 * by an object of 
 * type <code>suncertify.sockets.server.ExitCookie</code>,
 * which represents an element of the list.
 * <br>
 * <br>
 * Note that since this class should only be used by one class the access 
 * level is reduced to default.
 * 
 * 
 * @see ExitCookie
 * @see RecordDatabaseSocketRequest
 * @author stefan.streifeneder@gmx.de
 */
class ExitControl {
	
	/**
	 * The Logger instance. The Logger namespace is 
	 * <code>suncertify.sockets.server.ExitControl</code>.
	 */
	private static Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify..sockets.server.ExitControl"),
			Level.ALL);

	/**
	 * Ensures that many users can read collections and write to them.
	 */
	private static ReadWriteLock lockReadWrite = 
								new ReentrantReadWriteLock();
	
	/**
	 * The read lock.
	 */
	private final static Lock r = lockReadWrite.readLock();

	/**
	 * The write lock.
	 */
	private final static Lock w = lockReadWrite.writeLock();

	/**
	 * Stores all clients, who have locks on Records as an object of type
	 * <code>suncertify.sockets.server.ExitCookie</code>.
	 */
	private static List<ExitCookie> listClients = new ArrayList<ExitCookie>();
	

	/**
	 * Noarg constructor to create an object of this class.
	 */
	ExitControl() {

	}

	/**
	 * Stores the method arguments in an object of type
	 * <code>suncertify.sockets.server.ExitCookie</code>,
	 * which will be stored in the class list <code>listClients</code>.
	 * 
	 * @param recNo
	 *            The Record number of the locked Record.
	 * @param cookie
	 *            The cookie, which has to be used during update, delete
	 *            operation and to release the lock of the Record.
	 * @param client
	 *            Represents the client.
	 */
	@SuppressWarnings("static-method")
	void fillList(final Long recNo, final Long cookie, final Thread client) {
		ExitControl.log.entering("ExitControl", "fillList", new Object[] { 
				recNo, cookie, client });
		
		ExitControl.w.lock();	
		try{
			ExitControl.log.finer(
					"ExitControl, fillList, before add listClients: " 
								+ Integer.valueOf(ExitControl.listClients.size()));
			ExitControl.listClients.add(new ExitCookie(recNo, cookie, client));
		}finally{
			ExitControl.w.unlock();
		}
		ExitControl.log.exiting("ExitControl", "fillList");
	}

	/**
	 * Handles the situation, if a client closes connection without releasing
	 * the lock he holds on a Record. 
	 * 
	 * @param client
	 *            Type <code>java.lang.Thread</code>, represents the client.
	 * @param db
	 *            To access the database file.
	 */
	@SuppressWarnings("static-method")
	void irregularExit(final Thread client, final InterfaceClient_Admin db) {
		ExitControl.log.entering("ExitControl", "irregularExit", client);
		ExitControl.r.lock();	
		try{
			if (ExitControl.listClients.isEmpty()) {
				ExitControl.log.fine("ExitControl, irregularExit, stops, "
						+ "listClients: 0");
				return;
			}
		}finally{
			ExitControl.r.unlock();
		}
		ExitCookie cookie = null;
		ExitControl.r.lock();	
		try{
			ExitControl.log.fine("ExitControl, irregularExit, listClients: " 
					+ ExitControl.listClients.size());
			for (final ExitCookie cookieToAdd : ExitControl.listClients) {
				if (client.equals(cookieToAdd.getClient())) {
					cookie = cookieToAdd;
				}
			}
			if (cookie == null) {
				return;
			}
		}finally{
			ExitControl.r.unlock();
		}
		
		ExitControl.w.lock();	
		try{
			final Long recNoCookie = cookie.getRecNo();
			final Long cookieCookie = cookie.getCookie();
			try {
				db.setRecordUnlocked(recNoCookie.longValue(), 
						cookieCookie.longValue());
				ExitControl.listClients.remove(cookie);

				ExitControl.log.info("ExitControl, irregularExit, unlock, " 
						+ " - recNo: " + recNoCookie + " - cookie: "
						+ cookieCookie);
			} catch (final SecurityException e) {
				ExitControl.log.log(Level.SEVERE, "SecurityException - " 
						+ e.getMessage());
			} 
			catch (final IOException e) {
				ExitControl.log.log(Level.SEVERE, "IOException - " 
						+ e.getMessage());
			} catch (final RecordNotFoundException e) {
				ExitControl.log.log(Level.SEVERE, "RecordNotFoundException - " 
						+ e.getMessage());
			}
		}finally{
			ExitControl.w.unlock();
		}		
		ExitControl.log.exiting("ExitControl", "irregularExit");
	}

	/**
	 * Handles the call of a client to unlock a Record. 
	 * 
	 * @param recNo
	 *            The Record number of the unlocked Record.
	 * @param cookie
	 *            The lock cookie, which is needed for delete, update and unlock
	 *            operations.
	 * @param client
	 *            Represents the client.
	 */
	@SuppressWarnings("static-method")
	void regularExit(final Long recNo, final Long cookie, final Thread client) {
		ExitControl.log.entering("ExitControl", "regularExit", new Object[] {
				recNo, cookie, client });
		ExitControl.w.lock();	
		try{
			ExitControl.listClients.remove(new ExitCookie(
					recNo, cookie, client));
		}finally{
			ExitControl.w.unlock();
		}
		ExitControl.log.exiting("ExitControl", "regularExit");
	}
}
