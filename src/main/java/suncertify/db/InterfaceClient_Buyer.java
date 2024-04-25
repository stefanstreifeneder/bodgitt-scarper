package suncertify.db;

import java.io.IOException;

/**
 * The interface enables a Buyer client to rent a Record 
 * (enter ID in the database) and to release a Record of a rented state 
 * (erase ID in the database).
 * <br>
 * The interface is subtype of <code>suncertify.db.InterfaceClient_ReadOnly</code> 
 * to get and to search for Records. Additional the interface is also subtype of 
 * <code>suncertify.db.InterfaceClient_LockPermission</code> to
 * set a lock on a Record to rent or to release it.
 * <br>
 * <br>
 * <br>
 * To rent a Record please keep the following order of method calls:<br>
 * <blockquote><pre>
 * long lockCookie = interfaceClient_Buyer.setRecordLocked(recordNumber);
 * boolean isBooked = interfaceClient_Buyer.reserveRecord(recordNumber, id, lockCookie);
 * interfaceClient_Buyer.setRecordUnLocked(recordNumber, lockCookie);
 * </pre></blockquote>
 * To release a Record of a rented state, please keep the following order of
 * method calls:<br>
 *  <blockquote><pre>
 * long lockCookie = interfaceClient_Buyer.setRecordLocked(recordNumber);
 * boolean isReleased = interfaceClient_Buyer.releaseRecord(recordNumber, lockCookie);
 * interfaceClient_Buyer.setRecordUnLocked(recordNumber, lockCookie);
 * </pre></blockquote>
 * 
 * @see InterfaceClient_ReadOnly
 * @see InterfaceClient_LockPermission
 * @author stefan.streifeneder@gmx.de
 *
 */
public interface InterfaceClient_Buyer extends 
		InterfaceClient_ReadOnly, InterfaceClient_LockPermission{

	/**
	 * It releases a booked Record of a rented state. 
	 * The ID of an Buyer client will be erased in the database.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param lockCookie
	 *            The cookie, which will be created, if a Record is locked.
	 * @return boolean - Indicates whether the Record could be released of a
	 *         rented state.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 */
	public boolean releaseRecord(long recNo, long lockCookie)
			throws IOException, RecordNotFoundException, SecurityException;

	/**
	 * Reserves a Record. The ID of the home owner will be entered in the
	 * database file.<br>
	 * The field 'ID Owner' has to be blank, otherwise
	 * false will be returned.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param idOwner
	 *            The ID of the owner.
	 * @param cookie
	 *            The cookie, which will be created, if a Record will be locked.
	 * @return boolean - Indicates whether the Record could be rented.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 * @throws SecurityException
	 *             Thrown if there locking problems.
	 * @throws IllegalArgumentException 
	 * 			   Thrown if the Record number does not confirm the format
	 * 			   of the database file.
	 */
	public boolean reserveRecord(long recNo, int idOwner, long cookie)
			throws IOException, RecordNotFoundException, SecurityException,
								IllegalArgumentException;
}
