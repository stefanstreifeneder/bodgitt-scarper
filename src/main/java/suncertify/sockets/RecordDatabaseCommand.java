package suncertify.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Logger;

import suncertify.db.Record;

/**
 * An object of the class is used in a socket implementation 
 * and represents a request of a client. The object is transmitted
 * to a server. It implements the interface 
 * <code>java.io.Serializable</code>. The <code>Logger</code> object
 * is marked by the modifier 'transient'.
 * <br>
 * <br> It has a property to store the command ( 
 * <code>suncertify.sockets.SocketCommand</code> ) and
 * further properties to store necessary data to accomplish the request.<br>
 * The class is part of the protocol, which is used to exchange messages
 * between the server and clients.<br>
 * The server responds with an object of type 
 * <code>suncertify.sockets.RecordDatabaseResult</code>.<br>
 * <br>
 * If the application should run as a Java standalone application in a 
 * network environment, it is recommended to extract the different 
 * roles: Buyer and Seller. In this case both clients does not need 
 * certain properties.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RecordDatabaseCommand implements Serializable {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 31L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.sockets.RecordDatabaseCommand</code>.
	 */
	private transient Logger log = Logger.getLogger(
			"suncertify.sockets.RecordDatabaseCommand");

	/**
	 * The type of action this command object represents.
	 */
	private SocketCommand commandId;

	/**
	 * Stores the Record Number.
	 */
	private long recNo;

	/**
	 * An internal reference to a Record object.
	 */
	private Record record;

	/**
	 * Represents the fields of a Record.
	 */
	private String[] criteria;

	/**
	 * The lock cookie.
	 */
	private long cookie;

	/**
	 * The ID of the owner.
	 */
	private int idOwner;

	/**
	 * Noarg constructor to keep Java Beans convention.
	 */
	public RecordDatabaseCommand() {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor to proceed a locking operation or to get
	 * a Record by Record number.
	 * 
	 * @param idCmd The socket command to know what to do.
	 * @param recNoPara The Record number of the Record.
	 */
	public RecordDatabaseCommand(final SocketCommand idCmd, final long recNoPara) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = idCmd;
		this.recNo = recNoPara;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	
	/**
	 * Overloaded constructor to proceed an add operation.
	 * 
	 * @param sc The socket command to know what to do.
	 * @param rec The Record to add.
	 */
	public RecordDatabaseCommand(final SocketCommand sc, final Record rec) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.record = rec;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor to proceed a find operation by given
	 * parameters, which are reflected by the <code>String</code>
	 * arguments of the method argument <code>cri</code>.
	 *  
	 * @param sc The socket command to know what to do.
	 * @param cri Elements are accordingly to the values of a Record.
	 */
	public RecordDatabaseCommand(final SocketCommand sc, final String[] cri) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.criteria = cri;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor to proceed a modify operation.
	 * 
	 * 
	 * @param sc The socket command to know what to do.
	 * @param rec The modified Record.
	 * @param recNoPara The Record number of the Record.
	 * @param lCookie Is created by acquiring the lock of the Record.
	 */
	public RecordDatabaseCommand(final SocketCommand sc, final Record rec, final long recNoPara,
			final long lCookie) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.record = rec;
		this.recNo = recNoPara;
		this.cookie = lCookie;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor to delete a Record.
	 * 
	 * 
	 * @param sc The socket command to know what to do.
	 * @param recNoPara The Record number of the Record.
	 * @param lCookie Is created by acquiring the lock of the Record.
	 */
	public RecordDatabaseCommand(final SocketCommand sc, final long recNoPara,
			final long lCookie) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.recNo = recNoPara;
		this.cookie = lCookie;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor to proceed a book operation.
	 * 
	 * 
	 * @param sc The socket command to know what to do.
	 * @param recNoPara The Record number of the Record.
	 * @param id The id of the client.
	 * @param lCookie Is created by acquiring the lock of the Record.
	 */
	public RecordDatabaseCommand(final SocketCommand sc, final long recNoPara,
			final int id, final long lCookie) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.recNo = recNoPara;
		this.idOwner = id;
		this.cookie = lCookie;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}
	
	/**
	 * Overloaded constructor, which is used in three cases:
	 * <br> - the database size is requested
	 * <br> - Record numbers of all locked Records should be returned
	 * <br> - all valid Records should be returned
	 * 
	 * 
	 * @param sc The socket command to know what to do.
	 */
	public RecordDatabaseCommand(final SocketCommand sc) {
		this.log.entering("RecordDatabaseCommand", "RecordDatabaseCommand");
		this.commandId = sc;
		this.log.exiting("RecordDatabaseCommand", "RecordDatabaseCommand");
	}


	/**
	 * Returns the Record number.
	 * 
	 * @return long - Represents the Record number.
	 */
	public long getRecNo() {
		return this.recNo;
	}

	/**
	 * Returns the lock cookie.
	 * 
	 * @return long - Represents the lock cookie.
	 */
	public long getCookie() {
		this.log.entering("RecordDatabaseCommand", "getCookie");
		this.log.exiting("RecordDatabaseCommand", "getCookie", Long.valueOf(this.cookie));
		return this.cookie;
	}

	/**
	 * Gets the query that was used for searching.
	 *
	 * @return String[] - The elements of a Record which should be searched.
	 */
	public String[] getCriteria() {
		this.log.entering("RecordDatabaseCommand", "getRegex");
		this.log.exiting("RecordDatabaseCommand", "getRegex", this.criteria);
		return this.criteria;
	}

	/**
	 * Returns the ID of the owner.
	 * 
	 * @return int - The ID of the owner.
	 */
	public int getIdOwner() {
		return this.idOwner;
	}

	/**
	 * Retrieves the command id specified for this object.
	 *
	 * @return SocketCommand - Type enum, which represents the command.
	 */
	public SocketCommand getCommandId() {
		this.log.entering("RecordDatabaseCommand", "getCommandId");
		this.log.exiting("RecordDatabaseCommand", "getCommandId", this.commandId);
		return this.commandId;
	}

	/**
	 * Gets the Record object stored as property 
	 * of this class.
	 *
	 * @return Record - An object of type Record.
	 */
	public Record getRecord() {
		this.log.entering("RecordDatabaseCommand", "getRecord");
		this.log.exiting("RecordDatabaseCommand", "getRecord", this.record);
		return this.record;
	}

	/**
	 * Creates a <code>java.lang.String</code> representing this command for debugging and
	 * logging purposes.
	 *
	 * @return String - Representing this RecordDatabaseCommand.
	 */
	@Override
	public String toString() {
		return 
				"recordDatabaseCommand[" + "SocketCommand: " + this.commandId + ", " + "recNo: " + this.recNo + ", "
				+ "record: " + this.record + ", " + "cookie: " + this.cookie + ", " + "id owner: " + this.idOwner + ", "
				+ "Regex: " + this.criteria + "]"
				//"NOT IN MY HOUSE - C"//In real world recommended
				;
	}

	/**
	 * Deserializes an object of this class. It is never
	 * called directly.
	 * 
	 * @param in
	 *            The stream to read client from in order to restore the object
	 * @throws IOException
	 *             If I/O errors occur.
	 * @throws ClassNotFoundException
	 *             If the class for an object being restored cannot be found.
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.log = Logger.getLogger("suncertify.sockets");
		in.defaultReadObject();
	}
}
