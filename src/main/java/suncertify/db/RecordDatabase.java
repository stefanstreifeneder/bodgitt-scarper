package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * A <code>suncertify.db.RecordDatabase</code> object provides access to 
 * all Records in the system and all the operations that act upon 
 * the Records.
 * <br>
 * The class implements the interface 
 * <code>suncertify.db.InterfaceClient_Admin</code>, which is subtype of  
 * all public interfaces.
 * <br>
 * These interfaces are:
 * <br> <code>suncertify.db.InterfaceClient_ReadOnly</code>
 * <br> <code>suncertify.db.InterfaceClient_LockPermission</code>
 * <br> <code>suncertify.db.InterfaceClient_Buyer</code>
 * <br> <code>suncertify.db.InterfaceClient_Seller</code>
 * <br>
 * <br>
 * This is the only class that a programmer should use to access or to modify 
 * Records. This class is the facade of the database to the outside world.
 * <br>
 * The class has a <code>suncertify.db.RecordDatabaseAdapter</code> object, 
 * which transforms a <code>suncertify.db.Record</code> object in standard 
 * java API types accordingly to the interface <code>DBAccess</code>.
 * <br>
 * <br>
 * There are methods, which are not part of the public interface:
 * <br> <code>saveExit</code> - the method should be used either of the 
 * default server and the enhanced server at the end of a session. Finally
 * the class collection of class <code>Data</code> will be locked.
 * <br> <code>reloadDB</code> - enables a client to change
 * the database within a running session.
 * <br> <code>eraseLock</code> - it releases the lock on a Record
 * by Record number.
 * 
 * 
 * @see InterfaceClient_Admin
 * @see RecordDatabaseAdapter
 * @see Data 
 * @author stefan.streifeneder@gmx.de
 * 
 */
public class RecordDatabase implements InterfaceClient_Admin {

	/**
	 * A reference to <code>suncertify.db.RecordDatabaseAdapter</code>.
	 */
	private final RecordDatabaseAdapter databaseAdapter;

	/**
	 * Constructor that assumes the database is in the current working
	 * directory. Calls the overloaded constructor.
	 * 
	 * @throws FileNotFoundException
	 *             Signals that an attempt to open the file denoted by the
	 *             default pathname has failed.
	 * @throws IOException
	 *             Indicates there is a problem to access the database.
	 */
	public RecordDatabase() throws FileNotFoundException, IOException {
		this("","");
		System.out.println("RecordDatabase, noarg");
	}

	/**
	 * Constructor that takes the path of the database file as a parameter.
	 * @param nameDB The name of the database.
	 * @param password The password of the database.
	 * 
	 * @throws FileNotFoundException
	 *             Signals that an attempt to open the file denoted by a
	 *             specified pathname has failed.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */
	public RecordDatabase(final String nameDB, final String password) 
			throws IOException {
		this.databaseAdapter = new RecordDatabaseAdapter(nameDB, password);
	}

	/**
	 * Creates a new Record in the database and returns the Record number 
	 * of the new <code>suncertify.db.Record</code>.
	 * <br> 
	 * <br> The method forces to use Record numbers of deleted Records
	 * at first choice, but it is possible to choose between deleted
	 * Record numbers, if there are more than one. If there are no
	 * deleted Records, the new Record number will be always the amount
	 * of all Records plus one. 
	 * <br> If a Record number is requested, which does not exists, always the
	 * lowest Record number of the deleted Records will be assigned or
	 * the number of all Records plus one will be the Record number,
	 * if there are no deleted Records. 
	 * <br>
	 * <br>
	 * If a Record number is requested, which is already in use of a 
	 * valid Record a <code>suncertify.db.DuplicateKeyException</code> 
	 * is thrown. Additional a <code>DuplicateKeyException</code> will be 
	 * thrown, if the Record is locked. In a case a Record is marked 
	 * deleted, but still not unlocked a new Record would be created,
	 * which is locked by another client.
	 * 
	 * @param rec
	 *            The Record, which will be added.
	 * @return long - The assigned Record number.
	 * @throws IllegalArgumentException
	 *             Indicates format problems.
	 * @throws DuplicateKeyException
	 *             The Record number is already occupied by a valid Record
	 *             or the Record number is marked deleted but it
	 *             is still locked.
	 * @throws IOException
	 *             Indicates there is a problem to access the database file.
	 */
	@Override
	public long addRecord(final Record rec)
			throws DuplicateKeyException, IllegalArgumentException, IOException{
		return this.databaseAdapter.addRecord(rec);
	}

	
	/**
	 * Returns an array of Record numbers that match the specified
	 * <code>criteria</code>.  Field n in the database file is described by
	 * <code>criteria[n]</code>. A null value in <code>criteria[n]</code>
	 * or all elements are empty Strings matches any field value. A non-null 
	 * value in <code>criteria[n]</code>
	 * matches any field value that begins with <code>criteria[n]</code>. For
	 * example, "Fred" matches "Fred" or "Freddy".  The method behaves case 
	 * insensitive. 
	 * <br>
	 * <br>
	 * The elements of the <code>java.lang.String</code> array, which is used
	 * as the method argument must have the following order:<br>
	 * [0] name<br>
	 * [1] city<br>
	 * [2] types<br>
	 * [3] staff<br>
	 * [4] rate<br>
	 * [5] owner<br>  
	 * 
	 * @param criteria
	 *            Elements of a Record, which matches to Record's elements.
	 * @return Collection - A list of matching Records.
	 * @throws IOException
	 *             Indicates there is a problem to access the database.
	 */
	@Override
	public long[] findByFilter(final String[] criteria) throws	
	IOException {
		return this.databaseAdapter.find(criteria);
	}

	/**
	 * Reads a Record by Record number. 
	 * 
	 * @param recNo
	 *            The Record number of the searched Record.
	 * @return Record - The Record according to the Record number.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws IOException
	 *             Indicates there is a problem to access the database.
	 */
	@Override
	public Record getRecord(final long recNo) throws RecordNotFoundException, 
		IOException {
		return this.databaseAdapter.getRecord(recNo);

	}

	/**
	 * Modifies the fields of a Record.<br>
	 * <br>
	 * The method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are locking 
	 * problems like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * <br>
	 * <br> Additional an <code>java.lang.IllegalArgumentException</code>
	 * will be thrown, if the entries do not keep the format of the 
	 * database file.
	 * 
	 * @param rec
	 *            The modified Record.
	 * @param recNo
	 *            The Record number of the Record.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - Indicates the update of the Record succeeded.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 * @throws IllegalArgumentException
	 *             Thrown if the format of the database file is not kept.
	 */
	@Override
	public boolean modifyRecord(final Record rec, final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException, IOException {
		return this.databaseAdapter.modifyRecord(rec, recNo, lockCookie);
	}

	/**
	 * Releases a Record of a rented state.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be released.
	 * @param lockCookie
	 *            The cookie, which will be created, if a Record is locked.
	 * @return boolean - Indicates operation completed or failed.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws RecordNotFoundException
	 *             Thrown if the Record could not been found or is marked
	 *             deleted.
	 * @throws IOException
	 *             Thrown if transmission problems occur.
	 */
	@Override
	public boolean releaseRecord(final long recNo, final long lockCookie)
			throws SecurityException, RecordNotFoundException, IOException {
		return this.databaseAdapter.releaseRecord(recNo, lockCookie);
	}

	/**
	 * Deletes a Record's data, making the Record number and associated disk
	 * storage available for reuse. Marks the field <code>flag</code> of the 
	 * Record deleted with 0x8000 in two bytes at the beginning of the Record's 
	 * data. <br>
	 * If the delete operation has accomplished, the space will be still
	 * available and the elements set to 'DELETED'. Deleted Records are
	 * not displayed in the table of the main window. They keep their Record 
	 * number.
	 * <br>
	 * <br> The method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be removed.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - Indicates deletion succeeded.
	 * @throws RecordNotFoundException
	 *             Thrown if the Record could not been found or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 */
	@Override
	public boolean removeRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException, IOException {
		return this.databaseAdapter.removeRecord(recNo, lockCookie);
	}

	/**
	 * Books a Record. The ID of the home owner will be entered in the
	 * database file.<br>
	 * The field 'ID Owner' has to be blank, otherwise
	 * false will be returned.
	 * 
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be reserved.
	 * @param idOwner
	 *            The ID of the client.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - Indicates operation completed or failed.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there locking problems.
	 * @throws IOException
	 *             Thrown if problem occur to access the database file.
	 * @throws IllegalArgumentException
	 *             Thrown if the Record number does not confirm the format of
	 *             the database file.
	 */
	@Override
	public boolean reserveRecord(final long recNo, final int idOwner, final long lockCookie)
			throws RecordNotFoundException, SecurityException, IOException,
							IllegalArgumentException{
		return this.databaseAdapter.reserveRecord(recNo, idOwner, lockCookie);
	}

	/**
	 * Locks a Record so that it can only be updated, deleted, rented or
	 * released by this client.
	 * 
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be locked.
	 * @return long - The lock cookie.
	 * @throws RecordNotFoundException
	 *             Thrown, if there are locking problems.
	 */
	@Override
	public long setRecordLocked(final long recNo) throws RecordNotFoundException, 
				SecurityException {
		return this.databaseAdapter.setRecordLocked(recNo);
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
	 * 
	 * @param recNo
	 *            The Record number of the Record, which lock should be
	 *            released.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @throws SecurityException
	 *             Happens when the cookie is not appropriate.
	 */
	@Override
	public void setRecordUnlocked(final long recNo, final long lockCookie) throws SecurityException {
		this.databaseAdapter.setRecordUnlocked(recNo, lockCookie);
	}

	/**
	 * Returns all Record numbers of locked Records.
	 * 
	 * @return Set - The Record numbers of all locked Records.
	 */
	@Override
	public Set<Long> getLocked() {
		return this.databaseAdapter.getLockedSet();
	}	
	
	
	/**
	 * Returns the number of counted bytes of the database file.
	 * In this case included the scheme, which occupies the
	 * first 70 bytes. Due to the memory
	 * system of the underlying database it makes sense to provide
	 * such a method, because you can not delete physical memory
	 * space, but you can have a lot of deleted Records, which are
	 * not visible to a client.
	 * 
	 * @return Returns the number of counted bytes of the 
	 * database file.
	 */
	@Override
	public long getAllocatedMemory() {
		return Data.getDbSize();
	}

	/**
	 * Returns a list, which contains all valid Records.
	 * 
	 * @return Returns the number of counted bytes of the 
	 * database file.
	 */
	@Override
	public List<Record> getAllValidRecords() throws IOException {
		return this.databaseAdapter.getAllRecords();
	}

	/**
	 * The method closes the database.
	 * The method is used only in a network environment and
	 * is called only at the server side.
	 * The method is not part of the public interface and is not inherited.
	 * The method manages the situation, if the server shuts down.
	 * 
	 */
	public void saveExit() {
		this.databaseAdapter.saveExit();
	}
	
	/**
	 * The method loads a new or the same database file. Beware
	 * it is not part of the public interface, therefore it is not
	 * inherited. The method is used, if the database is 
	 * accessed locally. The method argument is the path to the database 
	 * file on the local disk.
	 * <br> Throws an <code>java.io.IOException</code>, if there is a locked
	 * Record.
	 * 
	 * @param path String representation of the path to the database file.
	 * @throws IOException Thrown, if the database
	 * file could not be accessed or there is a locked Record.
	 */
	public void reloadDB(final String path) throws IOException{
		this.databaseAdapter.reloadDB(path);
	}
	
	/**
	 * Releases the lock on the Record represented
	 * by the method argument. 
	 * <br>
	 * It is part of the enhanced version. It is not part of the
	 * public interface. It should only be used of the
	 * enhanced server program.
	 * 
	 * @param recNo The Record number, which will be released
	 * of locks.
	 */
	public void eraseLock(Long recNo){
		this.databaseAdapter.eraseClientsLock(recNo);
	}
}
