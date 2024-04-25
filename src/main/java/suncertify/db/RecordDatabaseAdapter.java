package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class works as an adapter between class 
 * <code>suncertify.db.Data</code> and class
 * <code>suncertify.db.RecordDatabase</code>.<br>
 * <br>
 * <br>
 * Note that since this class should only be used by one class the access 
 * level is reduced to default.
 * <br>
 * <br>
 * The class has a <code>suncertify.db.Data</code> 
 * object, which provides physical access 
 * to the database. 
 * <br>
 * <br>
 * The class <code>suncertify.db.Data</code> uses standard 
 * API classes to built a Record's data, while class 
 * <code>suncertify.db.RecordDatabase</code> uses a 
 * 'Data Transfer Object ' (DTO), which is an 
 * object of type <code>suncertify.db.Record</code> to represent 
 * a data set of the database file. 
 * This class in cooperation with the class 
 * <code>suncertify.db.RecordFormatter</code> accomplishes the 
 * transformation from pojo to an object of type 
 * <code>suncertify.db.Record</code>.
 * 
 * 
 * @see Data
 * @see RecordDatabase
 * @author stefan.streifeneder@gmx.de
 */
class RecordDatabaseAdapter {

	/**
	 * The reference to the class that handles physical access to 
	 * the database.
	 */
	private final Data database;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.RecordDatabaseAdapter</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.db.RecordDatabaseAdapter"), Level.ALL);

	/**
	 * Constructor that takes the path of the database file as a parameter. 
	 * 
	 * @param nameDB The name of the database. 
	 * @param password The password of the database.
	 * 
	 * @throws FileNotFoundException
	 *             Signals that an attempt to open the file denoted by a
	 *             specified pathname has failed.
	 * @throws IOException
	 *             Indicates there is a problem to access the database file.
	 */
	RecordDatabaseAdapter(final String nameDB, final String password) throws IOException {
		this.database = new Data(nameDB, password);
	}

	/**
	 * Creates a new Record in the database.
	 * Inserts the given Record data, and returns the record number of the new
	 * Record.<br>
	 * <br> 
	 * The method uses the <code>suncertify.db.RecordFormatter</code> to convert the
	 * pojo of class <code>suncertify.db.Data</code> into an object of 
	 * type <code>suncertify.db.Record</code>. 
	 * 
	 * @param rec
	 *            The Record, which will be added.
	 * @return long - The Record number of the added Record.
	 * @throws IllegalArgumentException
	 *             Indicates format problems.
	 * @throws DuplicateKeyException
	 *             The Record number is already occupied or the Record is locked.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 */
	long addRecord(final Record rec) throws DuplicateKeyException, 
	IOException,
			IllegalArgumentException {
		this.log.entering("RecordDatabaseAdapter", "addRecord", rec);
		long recNo;
		
		try {
			recNo = this.database.createRecord(
					RecordFormatter.recToStringArr(rec));
		} catch (final RuntimeException e) {
			if (e.getCause() instanceof IOException) {
				throw new IOException(e.getMessage());
			}else if (e instanceof IllegalArgumentException) {
				throw new IllegalArgumentException(e.getMessage());
			}else if (e instanceof ArrayIndexOutOfBoundsException) {
				throw new IllegalArgumentException("The Record's elements "
							+ "are not initialized correctly.");
			} 			
			this.log.info("RecordDatabaseAdapter, addRecord, "
						+ "RuntimeException:" + e.toString());
			throw new RuntimeException(e);
		}
		this.log.exiting("RecordDatabaseAdapter", "addRecord");
		return recNo;
	}
	
	/**
	 * Returns an array of Record numbers that match the specified
	 * <code>criteria</code>. 
	 * 
	 * @param criteria An array that contains criteria to investigate
	 * the database.
	 * @return long[] - an array of Record numbers that match the 
	 * specified criteria.
	 * @throws IOException Thrown, if transmission problems occur.
	 */
	long[] find(final String[] criteria)throws IOException{
		long[] recNos;
		try {			
			recNos = this.database.findByCriteria(criteria);
		} catch (final RuntimeException re) {
			if (re.getCause() instanceof IOException) {
				throw new IOException(re.getMessage());
			}
			this.log.info("RecordDatabaseAdapter, findByCriteria, " 
			+ "RuntimeException:" + re.toString());
			throw new RuntimeException(re);
		}
		this.log.exiting("RecordDatabaseAdapter", "findRecord");			
		return recNos;
	}

	/**
	 * Reads a Record by Record number. If the Record is marked deleted 
	 * a <code>RecordNotFoundException</code> will be thrown.<br>
	 * Uses static methods of the class <code>suncertify.db.RecordFormatter</code> 
	 * to convert the <code>String</code> array object in to a <code>Record</code> 
	 * object.
	 * 
	 * 
	 * @param recNo
	 *            The Record number of the searched Record.
	 * @return Record - A Record, represents an home improvement contractor's
	 *         offer.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 */
	Record getRecord(final long recNo) throws RecordNotFoundException, IOException {
		this.log.entering("RecordDatabaseAdapter", "getRecord", Long.valueOf(recNo));
		try {
			this.log.exiting("RecordDatabaseAdapter", "getRecord");
			return RecordFormatter.stringArrToRecord(
								this.database.readRecord(recNo));
		} catch (final RuntimeException re) {
			if (re.getCause() instanceof IOException) {
				throw new IOException(re.getMessage());
			}
			this.log.info("RecordDatabaseAdapter, getRecord, recNo: " 
					+ recNo
					+ ", Exc: " + re.getLocalizedMessage() );
			throw new RuntimeException(re);
		}
	}

	/**
	 * Modifies the fields of a Record.<br>
	 * Throws a <code>java.lang.SecurityException</code>, if there are locking 
	 * problems and throws an <code>java.lang.IllegalArgumentException</code>, 
	 * if the entries do not keep the format of the database file.
	 * The class <code>suncertify.db.RecordFormatter</code> transforms
	 * the values of the <code>Record</code> object into an array of
	 * <code>String</code> objects. 
	 * 
	 * 
	 * @param rec
	 *            The modified Record.
	 * @param recNo
	 *            The Record number of the Record.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - True if update was successful.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there locking problems.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 * @throws IllegalArgumentException
	 *             Thrown if the format of the database file is not kept.
	 */
	boolean modifyRecord(final Record rec, final long recNo, 
			final long lockCookie)throws RecordNotFoundException, 
				SecurityException, IOException, IllegalArgumentException {
		this.log.entering("RecordDatabaseAdapter", "modifyRecord",
				new Object[] { Long.valueOf(recNo), Long.valueOf(lockCookie) });
		try {
			this.database.updateRecord(recNo,
					RecordFormatter.recToStringArrNoRecNo(rec), lockCookie);
		} catch (final RuntimeException re) {
			if (re.getCause() instanceof IOException) {
				throw new IOException(re.getLocalizedMessage());
			}else if (re instanceof IllegalArgumentException) {
				throw new IllegalArgumentException(re.getMessage());
			} else if (re instanceof SecurityException) {
				throw new SecurityException(re.getLocalizedMessage());
			}
			this.log.info("RecordDatabaseAdapter, modifyRecord, "
						+ "RuntimeException:" + re.toString());
			throw new RuntimeException(re);
		}
		this.log.exiting("RecordDatabaseAdapter", "modifyRecord", rec);
		return true;
	}

	/**
	 * Deletes a Record's data, making the Record number and associated disk
	 * storage available for reuse. 
	 * 
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be removed.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - True if deletion was successful.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 */
	boolean removeRecord(final long recNo, final long lockCookie) throws RecordNotFoundException, 
														SecurityException, IOException {
		this.log.entering("RecordDatabaseAdapter", "removeRecord",
				new Object[] { Long.valueOf(recNo), Long.valueOf(lockCookie) });
		try {
			this.database.deleteRecord(recNo, lockCookie);
		} catch (final RuntimeException re) {
			if (re.getCause() instanceof IOException) {
				throw new IOException(re.getLocalizedMessage());
			} else if (re instanceof SecurityException) {
				throw new SecurityException(re.getLocalizedMessage());
			}
			this.log.info("RecordDatabaseAdapter, removeRecord, "
						+ "RuntimeException:" + re.toString());
			throw new RuntimeException(re);
		}
		this.log.exiting("RecordDatabase", "removeRecord", Boolean.valueOf(true));
		return true;
	}

	/**
	 * Reserves a Record. The ID of the client will be entered in the
	 * database.<br>
	 * The field 'ID Owner' has to be blank, otherwise
	 * false will be returned.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be reserved.
	 * @param idOwnerParam
	 *            The ID of the owner.
	 * @param cookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - True, if reservation was successful.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 * @throws IOException
	 *             Thrown if problems occur to access the database file.
	 */
	boolean reserveRecord(final long recNo, final int idOwnerParam, final long cookie)
			throws RecordNotFoundException, SecurityException, IOException {
		this.log.entering("RecordDatabaseAdapter", "reserveRecord",
				new Object[] { Long.valueOf(recNo), Integer.valueOf(idOwnerParam), 
						Long.valueOf(cookie) });
		boolean rent = false;
		final Record rec = this.getRecord(recNo);
		if (rec.getOwner().length() == 0) {
			rec.setOwner(Integer.valueOf(idOwnerParam).toString());
			this.modifyRecord(rec, recNo, cookie);
			rent = true;
		}
		this.log.severe("RecordDatabasAdapater, reserveRecord, rent: " + rent
				+ " - recNo: " + recNo
				+ " - Client-Thread: " + Thread.currentThread().getId()
				+ " - Client-ID: " + idOwnerParam);
		this.log.exiting("RecordDatabaseAdapter", "reserveRecord", 
				Boolean.valueOf(rent));
		return rent;
	}
	

	/**
	 * Releases a Record of a rented state. The ID of the client will be 
	 * erased in the database. Returns true even the Record was not booked
	 * before.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be released.
	 * @param cookie
	 *            Is created by acquiring the lock of the Record.
	 * @return boolean - Indicates, whether operation completed or failed.
	 * @throws SecurityException
	 *             Thrown, if there are locking problems.
	 * @throws IOException
	 *             Thrown, if transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If the Record can not been found or is marked deleted.
	 */
	boolean releaseRecord(final long recNo, final long cookie) 
			throws SecurityException, IOException, RecordNotFoundException {
		this.log.entering("RecordDatabaseAdapter", "releaseRecord", 
				Long.valueOf(recNo));
		final Record rec = this.getRecord(recNo);
		rec.setOwner("");
		this.modifyRecord(rec, recNo, cookie);
		this.log.severe("RecordDatabasAdapater, releaseRecord, release: " + true
				+ " - recNo: " + recNo + " - client: " + Thread.currentThread().getId());
		this.log.exiting("RecordDatabaseAdapter", "releaseRecord", Boolean.valueOf(true));
		return true;	
	}

	/**
	 * Locks a Record so that it can only be updated or deleted by this client.
	 * Calls the appropriate method of class <code>Data</code>.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be locked.
	 * @return long - The lock cookie, which must be used to update, delete or
	 *         unlock a Record.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 */
	long setRecordLocked(final long recNo) throws RecordNotFoundException{
		this.log.entering("RecordDatabaseAdapter", "setRecordLocked", Long.valueOf(recNo));
		this.log.exiting("RecordDatabaseAdapter", "setRecordLocked");
		return this.database.lockRecord(recNo);
	}

	/**
	 * Releases the lock on a Record. 
	 * 
	 * @param recNo
	 *            The Record number of the Record, which lock should be
	 *            released.
	 * @param lockCookie
	 *            Is created by acquiring the lock of the Record.
	 * @throws SecurityException
	 *             Happens when locking problems occur.
	 */
	void setRecordUnlocked(final long recNo, final long lockCookie) throws SecurityException {
		this.log.entering("RecordDatabaseAdapter", "setRecordUnlocked",
				new Object[] { Long.valueOf(recNo), Long.valueOf(lockCookie) });
		this.database.unlock(recNo, lockCookie);
		this.log.exiting("RecordDatabaseAdapter", "setRecordUnlocked");
	}

	/**
	 * Returns a set, which stores the Record numbers of all locked 
	 * Records. 
	 * 
	 * @return Set - All Record numbers of locked Records.
	 */
	@SuppressWarnings({ "static-method" })
	Set<Long> getLockedSet() {		
		return Data.getLockedSet();
	}

	/**
	 * The method closes the database.
	 * The method is used only in a network environment and
	 * is called only by the server.
	 * It is not a part of the public interface.<br>
	 * The method manages the situation, if the server shuts down.
	 * 
	 */
	void saveExit() {
		this.log.entering("RecordDatabaseAdapter", "saveExit");
		if (this.database != null) {
			this.database.setDatabaseClosed(true);
		}		
		this.log.exiting("RecordDatabaseAdapter", "saveExit");
	}

	/**
	 * The method loads a new or the same database file.
	 * It is not part of the public interface. 
	 * The method argument is the path on the local disk.
	 * <br> Throws an <code>java.io.IOException</code>, if there is a locked
	 * Record.
	 * 
	 * @param path String representation of the path to the database file.
	 * @throws IOException Thrown, if the database
	 * file could not be accessed or there is a locked Record.
	 */
	void reloadDB(final String path) throws IOException{
		this.log.entering("RecordDatabaseAdapter", "reloadDB", path);
		if(!this.getLockedSet().isEmpty()){
			throw new IOException("You can not change the database File,"
					+ " because there is at least one locked Record.");
		}
		this.database.loadDB(path);
		this.log.exiting("RecordDatabaseAdapter", "reloadDB");
	}
	
	
	/**
	 * Returns a list, which contains all valid Records.
	 * 
	 * @return List - contains all valid Records.
	 * @throws IOException thrown, if the database can not
	 * been accessed successfully.
	 */
	List<Record> getAllRecords() throws IOException{
		List<Record> returnSet = new ArrayList<Record>();
		for(String[] recArr : this.database.getRecords()){
			returnSet.add(new Record(Long.parseLong(recArr[7]),
					recArr[1], recArr[2], recArr[3],
				Integer.parseInt(recArr[4]), 
				recArr[5], recArr[6]));
		}
		return returnSet;
	}
	
	
	/**
	 * Returns the number of counted bytes of the database file.
	 * In this case included the scheme, which occupies the
	 * first 70 bytes.
	 * 
	 * @return Returns the number of counted bytes of the 
	 * database file.
	 */
	@SuppressWarnings("static-method")
	long getSizeOfDB(){
		return Data.getDbSize();
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
	@SuppressWarnings("static-method")
	void eraseClientsLock(Long recNo){
		LockManager.eraseLock(recNo);
	}
}
