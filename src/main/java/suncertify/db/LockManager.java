package suncertify.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class is the main part of the locking system.
 * <br>
 * <br>
 * Note that since this class should only be used by one class the access 
 * level is reduced to default.
 * <br>
 * <br>
 * The class provides a method called <code>eraseLock</code>,
 * which is part of the enhanced version.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
class LockManager {
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.db.LockManager</code>.
	 */
	private static final Logger log = 
			LoggerControl.getLoggerBS(
					Logger.getLogger("suncertify.db.LockManager"), Level.ALL);	
	
	/**
	 * Locks code of this class within the locking procedure.
	 */
	private static Lock lock = new ReentrantLock();
	
	/**
	 * A new condition, if a Record is locked or released.
	 */
	private static Condition lockReleased  = lock.newCondition();	

	/**
	 * Stores the Record number and the according 
	 * <code>suncertify.db.LockCookieObject</code>
	 * object to manage lock and unlock operations.
	 */
	private static Map<Long, LockCookieObject> mapLocks = 
			new HashMap<Long, LockCookieObject>();

	/**
	 * Stores the value of the lock cookie, which is incremented
	 * each time a lock on a Record is created.
	 */
	private static long cookieStatic = 0;	
	
	/**
	 * Default constructor. 
	 * 
	 */
	LockManager(){
		LockManager.log.entering("LockManager", "LockManager");
		LockManager.log.exiting("LockManager", "LockManager");
	}
	
	/**
	 * Locks a Record to update, delete or unlock it.
	 * Returns a lock cookie, which must be used,
	 * if the Record will be updated, deleted or unlocked.
	 * <br> Beware the method does not care how many locks 
	 * a client holds at the same time. 
	 * 
	 * 
	 * @param recNo The Record number of the Record, its lock
	 * should be acquired.
	 *  
	 * @return long - The lock cookie, which must be used to update
	 * or delete a Record.
	 * 
	 * @throws RecordNotFoundException Thrown if the Record number
	 * does not exist or the Record is marked deleted.
	 */
	@SuppressWarnings("static-method")
	long lockRecord(final long recNo) throws RecordNotFoundException {
		final long lockCookie = ++cookieStatic;
		LockManager.lock.lock();
		try {
			
			// If the method must expect, that a client tries to
			// hold more than one lock at the same time, which should be 
			// avoided strictly, the following loop will be an appropriate
			// aid, but it hits performance. 
			// 
			// Restricts a client to one lock at the same time.
//			for(final Map.Entry<Long, LockCookieObject> map : mapLocks.entrySet()){
//				if (map.getValue().getClient().equals(Thread.currentThread())) {
//					throw new SecurityException("You are NOT allowed " 
//							+ "to lock more than ONE Record!");
//				}
//			}
			
			while (LockManager.mapLocks.containsKey(Long.valueOf(recNo))) {
				try {
					 
					// Test
//					System.out.println("LockManager, BEFORE AWAIT, "
////							+ " Time: " + new Date()
//							//+ " - client: " + Thread.currentThread().getName()
//							+ " - recNo: " + recNo
//							+ " - cookie: " + cookieStatic
//							+ " - map: " + mapLocks.size());					 
					 
					 LockManager.lockReleased.await();
				} catch (final InterruptedException e) {
					LockManager.log.severe("LockManager, lockRecord, during a call" 
									+ "to await: " + e.getLocalizedMessage());
					Thread.currentThread().interrupt();
				}
				
				// Test
//				System.out.println("LockManager, AFTER AWAIT, "
////						+ " Time: " + new Date()
//						//+ " - client: " + Thread.currentThread().getName()
//						+ " - recNo: " + recNo
//						+ " - cookie: " + cookieStatic
//						+ " - map: " + mapLocks.size());
			}
			
			// Tests, whether the data set was deleted of the former client.
			// Throws a RecordNotFoundException, if the data set was
			// marked deleted. 
			Data.checkRecordExists(recNo);
			LockManager.mapLocks.put(Long.valueOf(recNo), new LockCookieObject(
					lockCookie, Thread.currentThread()));
		} finally {
			LockManager.lock.unlock();
		} // end await
		return lockCookie;
	}
	
	
	
	/**
	 * Releases the lock on a Record.
	 * <br> 
	 * <br> The method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param cookie
	 *            Is created by calling <code>lockRecord</code>. Is needed to
	 *            acquire the lock for updates and deleting operations.
	 * @throws SecurityException
	 *             Thrown, if there are locking problems.
	 */
	@SuppressWarnings("static-method")
	void unlock(final long recNo, final long cookie) throws SecurityException {
		LockManager.checkLockStatus(recNo, cookie);
		LockManager.lock.lock();
		try {
			LockManager.lockReleased.signal();
			LockManager.mapLocks.remove(Long.valueOf(recNo));
		} finally {
			LockManager.lock.unlock();
		}
	}
	
	
	/**
	 * Returns a copy of a set, which stores all Record numbers of 
	 * locked Records. 
	 * 
	 * @return Set - A set with all Record numbers of locked Records.
	 */
	static Set<Long> getLockedSet() {
		// If no HashSet object will be created, based on
		// a call to 'return getLockedObjects().keySet()' the following
		// exception will be thrown in a network environment:
		//
		// java.io.WriteAbortedException: writing aborted; 
		// java.io.NotSerializableException: java.util.HashMap$KeySet
		final Set<Long> set = new HashSet<Long>();
		set.addAll(getLockedObjects().keySet());
		return set;
	}
	
	/**
	 * The method may throw a <code>java.lang.SecurityException</code> 
	 * in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * 
	 * @param recNo The Record number to identify the Record.
	 * @param lockCookie The lock cookie.
	 * @throws SecurityException is thrown, if the Record is 
	 * not locked, if the cookie is wrong or the client
	 * does not match.
	 */
    static void checkLockStatus(final long recNo, 
    		final long lockCookie) throws SecurityException{			
    	final Long recNoLong = Long.valueOf(recNo);
		LockManager.lock.lock();
		try {
			final LockCookieObject cookieStored = mapLocks.isEmpty() ? null :
					mapLocks.get(recNoLong);
			if (cookieStored == null) {
				throw new SecurityException("Unlock Operation failed! " 
								+ " Record: " 
								+ Long.toString(recNo) 
								+ " - is NOT locked!");
			} else if (lockCookie != cookieStored.getLockCookie()) {
				throw new SecurityException("Unlock Operation failed! " 
						+ "LockCookie: " + Long.toString(lockCookie)
						+ " does not match with recNo: " + Long.toString(recNo));
			} else if (!cookieStored.getClient().equals(Thread.currentThread())) {
				throw new SecurityException("Unlock Operation failed! " 
						+ "Record number: " + recNo
						+ " - an other client possesses the lock!" 
						+ " Client: " + Thread.currentThread());
			}
		} finally {
			LockManager.lock.unlock();
		}
	}
	
	/**
	 * Releases the lock on the Record represented
	 * by the method argument. 
	 * <br>
	 * It is part of the enhanced version. It is not part of the
	 * public interface. 
	 * 
	 * @param recNo The Record number, which will be released
	 * of locks.
	 */
	static void eraseLock(Long recNo){
		LockManager.lock.lock();
		try {
			LockManager.lockReleased.signal();
			mapLocks.remove(recNo);
		} finally {
			LockManager.lock.unlock();
		}
	}

	/**
	 * Returns a copy of a map, which stores all Record numbers of locked 
	 * Records and the according <code>suncertify.db.LockCookieObject</code>
	 * object.
	 * 
	 * @return Map - Key is the Record number, value is an object of type
	 *         <code>suncertify.db.LockCookieObject</code>.
	 */
	private static Map<Long, LockCookieObject> getLockedObjects() {	
		final Map<Long, LockCookieObject> mapFin = 
				new HashMap<Long, LockCookieObject>();
		LockManager.lock.lock();
		try{
			mapFin.putAll(LockManager.mapLocks);			
		}finally{
			LockManager.lock.unlock();
		}
		return mapFin;
	}
}
