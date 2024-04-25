package suncertify.db;

import java.io.IOException;

/**
 * Public interfaces, which enables update, delete and add operations. This
 * interface reflects the necessary functionalities a home improvement 
 * contractor (seller) must have.<br>
 * <br>
 * You have to keep a certain order of method calls.<br>
 * Update:<br>
 * <blockquote><pre>
 * long lockCookie = interfaceClient_Seller.setRecordLocked(recordNumber);
 * boolean isUpdated = interfaceClient_Seller.modifyRecord(recordNumber, updatedRecord, lockCookie);
 * interfaceClient_Seller.setRecordUnLocked(recordNumber, lockCookie);
 * </pre></blockquote>
 * Delete:<br>
 * <blockquote><pre>
 * long lockCookie = interfaceClient_Seller.setRecordLocked(recordNumber);
 * boolean isDeleted = interfaceClient_Seller.deleteRecord(recordNumber, lockCookie);
 * interfaceClient_Seller.setRecordUnLocked(recordNumber, lockCookie);
 *  </pre></blockquote>
 * 
 * @see InterfaceClient_ReadOnly
 * @see InterfaceClient_LockPermission
 * @author stefan.streifeneder@gmx.de
 *
 */
public interface InterfaceClient_Seller extends InterfaceClient_ReadOnly
				, InterfaceClient_LockPermission{

	/**
	 * Creates a new Record in the database (possibly reusing a deleted entry).
	 * Inserts the given Record, and returns the Record number of the new
	 * <code>suncertify.db.Record</code>.
	 * <br> 
	 * <br> The method forces to use Record numbers of deleted Records
	 * at first choice, but it is possible to choose between deleted
	 * Record numbers, if there are more than one. If there are no
	 * deleted Records, the new Record number will be always the amount
	 * of all Records plus one. 
	 * <br> If a Record number is requested, which does not exists, always the
	 * lowest Record number of the deleted Records will be used or
	 * the number of all Records plus one will be the Record number. 
	 * <br>
	 * <br>
	 * If a Record number is requested, which is already in use of a 
	 * valid Record a <code>suncertify.db.DuplicateKeyException</code> 
	 * is thrown. Additional a <code>DuplicateKeyException</code> will be 
	 * thrown, if the Record is locked. In a case a Record is marked 
	 * deleted, but still not unlocked a new Record would be created,
	 * which is locked by another client.
	 * 
	 * 
	 * @param rec
	 *            The Record, which should be added.
	 * @return long - The Record number of the added Record.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws IllegalArgumentException
	 *             Thrown to indicate that a method has been passed an illegal
	 *             or inappropriate argument.
	 * @throws DuplicateKeyException
	 *             Thrown if the Record number is already occupied or the 
	 *             Record is marked deleted but it is still locked.
	 */
	public long addRecord(Record rec)
			throws IOException, IllegalArgumentException, DuplicateKeyException;

	/**
	 * Modifies the fields of a Record. 
	 * <br>
	 * <br> You have to keep the format of a Record or an
	 * <code>java.lang.IllegalArgumentException</code> is thrown.
	 * <br>
	 * <br> The method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * 
	 * @param record
	 *            The Record with the modified values.
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param lockCookie
	 *            Is created by calling the method <code>setRecordLocked</code>
	 *            of <code>suncertify.db.InterfaceClient_LockPermission</code>.
	 * @return boolean - Indicates, whether the Record has been modified.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws IllegalArgumentException
	 *             Thrown if the format of the database is not kept.
	 */
	public boolean modifyRecord(Record record, long recNo, long lockCookie)
			throws IOException, RecordNotFoundException, SecurityException, 
													IllegalArgumentException;

	/**
	 * Deletes a Record, making the Record number and associated disk storage
	 * available for reuse.<br>
	 * If a Record is deleted its physical space maintains for ever
	 * or will be reused of a new Record.
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
	 * @param lockCookie
	 *            Is created by calling the method <code>setRecordLocked</code>
	 *            of <code>suncertify.db.InterfaceClient_LockPermission</code>.
	 * @return boolean - Indicates, whether the remove operation accomplished or
	 *         not.
	 * @throws IOException
	 *             Indicates there is a problem to access the database file.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 * @throws SecurityException
	 *             Thrown if the Record is locked with a cookie other than the
	 *             lockCookie or is not locked.
	 */
	public boolean removeRecord(long recNo, long lockCookie)
			throws IOException, RecordNotFoundException, SecurityException;

}
