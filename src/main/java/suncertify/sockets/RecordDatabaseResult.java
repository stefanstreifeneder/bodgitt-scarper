package suncertify.sockets;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import suncertify.db.Record;

/**
 * An object of the class is used in a socket implementation 
 * and represents a respond of the server. The object is transmitted
 * to a a client. It implements the interface 
 * <code>java.io.Serializable</code>. The <code>Logger</code> object
 * is marked by the modifier 'transient'.
 * <br>
 * <br> Beware that a possible thrown exception, which was caused by 
 * a request of a client, will be wrapped within a response. The exception 
 * is not thrown over the Internet. It is stored in the property 
 * <code>exception</code>, which is of type <code>java.lang.Exception</code> 
 * and transmitted as a property of this class.
 * <br>
 * <br> If the application should run in a network environment as a standalone
 * Java application, it is recommended to extract the different 
 * roles: Buyer and Seller. In this case a Buyer client does not need 
 * certain properties.
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RecordDatabaseResult implements Serializable {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 32L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.sockets.RecordDatabaseResult</code>.
	 */
	private transient Logger log = Logger.getLogger(
			"suncertify.sockets.RecordDatabaseResult");

	/**
	 * The Record number.
	 */
	private long recNo;

	/**
	 * An internal reference to a Record object.
	 */
	private Record record;

	/**
	 * The exception member. When an exception occurs, the original exception is
	 * returned in the result.
	 */
	private Exception exception;

	/**
	 * The boolean result returned to the client.
	 */
	private boolean booleanResult;

	/**
	 * Stores the value of the cookie.
	 */
	private long cookie;	
	
	/**
	 * Stores Record numbers that match specified criteria.
	 * It is initialized to avoid a 
	 * <code>java.lang.NullPointerException</code>
	 * due to the method <code>toString</code>, which contains
	 * a call to the static field <code>length</code>.
	 */
	private long[] matchingRecords = new long[]{};

	/**
	 * Stores Record numbers of all locked Records.
	 */
	private Set<Long> listLocked;
	
	
	/**
	 * The size of the database due to counted
	 * bytes.
	 */
	private long memory;
	
	/**
	 * A list with valid Records.
	 */
	private List<Record> setValidRecords;

	/**
	 * Noarg constructor, which should be used, if one of the commands 
	 * <code>SocketCommand.ADD</code>, 
	 * <code>SocketCommand.LOCK_RECORD</code> or
	 * <code>SocketCommand.GET_VALID_RECORDS</code> is proceeded.
	 * The instance variables <code>cookie</code> or <code>recNo</code>
	 * has to be initialized by the provided setter methods.
	 * <br> The empty constructor conforms with JavaBean requirements 
	 * as well.
	 */
	public RecordDatabaseResult() {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	/**
	 * Overloaded constructor to return the boolean outcome,
	 * if a release, rent, modify or remove operation
	 * was proceeded.
	 * 
	 * @param b Indicates whether the operation
	 * has terminated successfully.
	 */
	public RecordDatabaseResult(final boolean b) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.booleanResult = b;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	/**
	 * Overloaded constructor to return the outcome
	 * of a search by Record number operation, which is a Record.
	 * 
	 * @param rec The Record accordingly to a Record number.
	 */
	public RecordDatabaseResult(final Record rec) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.record = rec;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	
	/**
	 * Overloaded constructor to return the outcome of a find by 
	 * criteria operation, which is an array, which elements
	 * are the Record numbers.
	 * 
	 * 
	 * @param recNos An array, which contains the matching Record numbers.
	 */
	public RecordDatabaseResult(final long[] recNos) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.matchingRecords = recNos;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	/**
	 * Overloaded constructor to return a set with all locked
	 * Record numbers.
	 * 
	 * @param lockedRecs A set, which stores all locked Record numbers.
	 */
	public RecordDatabaseResult(final Set<Long> lockedRecs) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.listLocked = lockedRecs;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	/**
	 * Overloaded constructor to return a list with all valid
	 * Record.
	 * 
	 * @param validRecs A list, which stores all 
	 * valid Record.
	 */
	public RecordDatabaseResult(final List<Record> validRecs) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.setValidRecords = validRecs;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}
	
	/**
	 * Overloaded constructor to return an exception,
	 * if it is thrown while a command is proceeded. 
	 * 
	 * @param exc The exception, which is thrown caused
	 * while a command is proceeded.
	 */
	public RecordDatabaseResult(final Exception exc) {
		this.log.entering("RecordDatabaseResult", "RecordDatabaseResult");		
		this.exception = exc;		
		this.log.exiting("RecordDatabaseResult", "RecordDatabaseResult");
	}

	/**
	 * Returns the Record number.
	 * 
	 * @return long - The Record number.
	 */
	public long getRecNo() {
		return this.recNo;
	}

	/**
	 * Returns the value of the variable <code>booleanResult</code>,
	 * which is of type boolean.
	 *
	 * @return boolean - Value of the boolean value.
	 */
	public boolean getBoolean() {
		this.log.entering("RecordDatabaseResult", "getBoolean");
		this.log.exiting("RecordDatabaseResult", "getBoolean", Boolean.valueOf(this.booleanResult));
		return this.booleanResult;
	}

	/**
	 * Returns the variable <code>record</code>,
	 * which is of type <code>Record</code>.
	 *
	 * @return Record - The Record object of the return value.
	 */
	public Record getRecord() {
		this.log.entering("RecordDatabaseResult", "getRecord");
		this.log.exiting("RecordDatabaseResult", "getRecord", this.record);
		return this.record;
	}

	/**
	 * Returns the exception value of the object.
	 *
	 * @return Exception - The <code>java.lang.Exception</code> object of the return
	 *         value.
	 */
	public Exception getException() {
		this.log.entering("RecordDatabaseResult", "getException");
		this.log.exiting("RecordDatabaseResult", "getException", this.exception);
		return this.exception;
	}

	/**
	 * Returns a list of all searched Records.
	 * 
	 * @return List - All searched Records.
	 */
	public long[] getFindBy() {
		this.log.entering("RecordDatabaseResult", "getCollection");
		this.log.exiting("RecordDatabaseResult", "getCollection", 
				this.matchingRecords);
		return this.matchingRecords;
	}

	/**
	 * Returns the value of the lock cookie.
	 * 
	 * @return long - Represents the lock cookie.
	 */
	public long getCookie() {
		this.log.entering("RecordDatabaseResult", "getCookie");
		this.log.exiting("RecordDatabaseResult", "getCookie", Long.valueOf(this.cookie));
		return this.cookie;
	}

	/**
	 * Returns a set of all Record numbers, which
	 * are locked.
	 * 
	 * @return Set - All locked Record numbers.
	 */
	public Set<Long> getListLocked() {
		return this.listLocked;
	}
	
	
	/**
	 * Returns the number of counted bytes of the 
	 * database.
	 * 
	 * @return long - the amount of counted bytes.
	 */
	public long getMemory(){
		return this.memory;
	}
	
	/**
	 * Returns a list, which contains all valid Records.
	 * 
	 * @return List - contains all valid Records.
	 */
	public List<Record> getValidsRecords(){
		return this.setValidRecords;
	}

	/**
	 * Sets the Record number.
	 * 
	 * @param recNoParam
	 *            The Record number.
	 */
	public void setRecNo(final long recNoParam) {
		this.recNo = recNoParam;
	}

	/**
	 * Sets the property <code>cookie</code>.
	 * 
	 * @param cookieParam
	 *            The lock cookie.
	 */
	public void setCookie(final long cookieParam) {
		this.cookie = cookieParam;
	}
	
	
	/**
	 * Sets the property <code>memory</code>, which stores
	 * the size of the database.
	 * 
	 * @param sizeOfDb The counted bytes of the database.
	 */
	public void setMemory(long sizeOfDb){
		this.memory = sizeOfDb;
	}

	/**
	 * Creates a <code>java.lang.String</code> representation
	 * of an object of this class.
	 *
	 * @return String - A string representing this DvdCommand.
	 */
	
	@Override
	public String toString() {
		return
				"RecordDatabaseResult:[" 
				+ "recNo: " + this.recNo + "; " 
				+ "Record: " + this.record + "; "
				+ "Exception: " + this.exception + "; " 
				+ "booleanResult: " + this.booleanResult + "; " 
				+ "findBy-Array-length: " + this.matchingRecords.length + "; " + "]";
				//"NOT IN MY HOUSE - R";//In real world recommended
	}
	

	/**
	 * Deserializes an object of this class. It is never
	 * called directly.
	 * 
	 * @param in
	 *            The stream to read client from in order to restore the object
	 * @throws IOException
	 *             if I/O errors occur.
	 * @throws ClassNotFoundException
	 *             If the class for an object being restored cannot be found.
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.log = Logger.getLogger("sampleproject.db");
		in.defaultReadObject();
	}
}
