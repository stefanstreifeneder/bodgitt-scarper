package suncertify.db;

import java.io.IOException;

/**
 * Public interface, which supports Record locking. The lock is necessary for
 * delete, update, rent, release and unlock operations. 
 *  
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public interface InterfaceClient_LockPermission {

	/**
	 * Locks a Record so that it can only be updated, deleted, rented or
	 * released of a rented state by this client. Returned value is a cookie
	 * that must be used, when the Record is unlocked, updated, deleted, rented
	 * or released of a rented state. If the specified Record is already locked
	 * by a different client, the current thread gives up the CPU and consumes
	 * no CPU cycles until the Record is unlocked by the other client.
	 * <br> Beware the method does not care how many locks a client holds at 
	 * the same time.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return The lock cookie, which has to be used, if a Record will be modified,
	 * deleted, rented or released of rented state.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 */
	public long setRecordLocked(long recNo) throws IOException, 
							RecordNotFoundException;

	/**
	 * Releases the lock of a Record. 
	 * <br>
	 * <br> The method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another client
	 * <br> - the lock cookie does not match
	 * 
	 * 
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param lockCookie
	 *            Is created by calling setRecordLocked().
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 */
	public void setRecordUnlocked(long recNo, long lockCookie)
			throws IOException, SecurityException, RecordNotFoundException;

}
