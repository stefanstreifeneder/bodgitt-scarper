package suncertify.db;

/**
 * An Interface to get access to the database file. It is only implemented by
 * class <code>Data</code>.
 */
public interface DBAccess {

	/**
	 * Reads a Record from the database file. Returns an array where each
	 * element is a Record value.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return String[] - Each element represents a field of the Record.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 */
	public String[] readRecord(long recNo) throws RecordNotFoundException;

	/**
	 * Modifies the fields of a Record. The new value for field n appears in
	 * <code>data[n]</code>. Throws <code>SecurityException</code> if the Record
	 * is locked with a cookie other than the lockCookie or is not locked.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param data
	 *            A <code>String</code> array, which carries all fields of the
	 *            Record.
	 * @param lockCookie
	 *            Is needed to unlock the Record after the procedure.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown there are locking problems.
	 */
	public void updateRecord(long recNo, String[] data, long lockCookie)
			throws RecordNotFoundException, SecurityException;

	/**
	 * Deletes a Record, making the Record number and associated disk storage
	 * available for reuse. Marks the field <code>flag</code> of the Record deleted with
	 * 0x8000 in two bytes at the beginning of the Record.<br>
	 * Throws <code>SecurityException</code> if the Record is locked with a
	 * cookie other than <code>lockCookie</code> or is not locked.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param lockCookie
	 *            Is needed to unlock the Record after the procedure.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 */
	public void deleteRecord(long recNo, long lockCookie) throws RecordNotFoundException, SecurityException;

	/**
	 * Returns an array of Record numbers that match the specified criteria.
	 * Field n in the database file is described by <code>criteria[n]</code>. A
	 * null value in <code>criteria[n]</code> matches any field value. A
	 * non-null value in <code>criteria[n]</code> matches any field value that
	 * begins with <code>criteria[n]</code>. (For example, "Fred" matches "Fred"
	 * or "Freddy".)
	 * 
	 * @param criteria
	 *            Each <code>String</code> represents a firld of a Record.
	 * @return long[] - An array of Record numbers, which matches the criteria.
	 */
	public long[] findByCriteria(String[] criteria);

	/**
	 * Creates a new Record in the database (possibly reusing a deleted entry).
	 * Inserts the given <code>data</code>, and returns the Record number of the
	 * new Record.
	 * 
	 * @param data
	 *            A String[] which carries all fields of the new Record.
	 * @return long - The Record number for the created Record.
	 * @throws DuplicateKeyException
	 *             Thrown if the Record number is already occupied.
	 */
	public long createRecord(String[] data) throws DuplicateKeyException;

	/**
	 * Locks a Record so that it can only be updated or deleted by this client.
	 * Returned value is a cookie that must be used when the Record is unlocked,
	 * updated, or deleted. If the specified record is already locked by a
	 * different client, the current thread gives up the CPU and consumes no CPU
	 * cycles until the Record is unlocked.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return long - The value of the lockCookie.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is deleted.
	 */
	public long lockRecord(long recNo) throws RecordNotFoundException;

	/**
	 * Releases the lock on a Record. Cookie must be the cookie returned when
	 * the Record was locked, otherwise a <code>SecurityException</code> is
	 * thrown.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param cookie
	 *            Is created by calling <code>lockRecord</code>. Is needed to
	 *            acquire the lock of the Record for updates and deleting
	 *            operations.
	 * @throws SecurityException
	 *             Thrown if the are locking problems.
	 */
	public void unlock(long recNo, long cookie) throws SecurityException;
}
