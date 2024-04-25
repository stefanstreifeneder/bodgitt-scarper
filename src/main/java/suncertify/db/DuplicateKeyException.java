package suncertify.db;

import java.util.logging.Logger;

/**
 * The exception is thrown within the creation of a new Record. It is thrown, 
 * if a Record number is requested, which is already in use of a valid Record a
 * <code>suncertify.db.DuplicateKeyException</code> is thrown. Additional a
 * <code>DuplicateKeyException</code> will be thrown, if the Record is locked.
 * In a case a Record is marked deleted, but still not unlocked a new Record
 * would be created, which is locked by another client.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class DuplicateKeyException extends Exception {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 11L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.DuplicateKeyException</code>.
	 */
	private transient Logger log = Logger.getLogger("suncertify.db.DuplicateKeyException");

	/**
	 * Constructs a <code>suncertify.db.DuplicateKeyException</code> with no detailed message
	 * with a call to the overloaded constructor with a default message.
	 * <br> The empty constructor conforms with JavaBean requirements 
	 * as well.
	 */
	public DuplicateKeyException() {
		this("A DuplicateKeyException occured!");
		this.log.entering("DuplicateKeyException", "DuplicateKeyException");
		this.log.exiting("DuplicateKeyException", "DuplicateKeyException");
	}

	/**
	 * Constructs a <code>suncertify.db.DuplicateKeyException</code> with the specified
	 * detailed message.
	 * 
	 * @param description
	 *            Gives more details.
	 */
	public DuplicateKeyException(String description) {
		super(description);
		this.log.entering("DuplicateKeyException", "DuplicateKeyException", description);
		this.log.exiting("DuplicateKeyException", "DuplicateKeyException");
	}
}
