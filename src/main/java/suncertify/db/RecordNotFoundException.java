package suncertify.db;

import java.util.logging.Logger;

/**
 * Thrown when a Record can not been identified by the Record number
 * or is marked deleted. Afterwards an object of this class should be able
 * to be send via the Internet, it is necessary to mark the instance
 * variable of type <code>java.util.logging.Logger</code> 'transient'.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class RecordNotFoundException extends Exception {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 15L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.RecordNotFoundException</code>.
	 *  Afterwards an object of this class should be able
	 * to be send via the Internet, it is necessary to mark the instance
	 * variable of type <code>java.util.logging.Logger</code> 'transient'.
	 * 
	 */
	private transient Logger log = Logger.getLogger(
			"suncertify.db.RecordNotFoundException");

	/**
	 * Constructs a RecordNotFoundException with no detailed message and with a
	 * call to the overloaded Constructor with a default message.
	 * <br> The empty constructor conforms with JavaBean requirements 
	 * as well.
	 */
	public RecordNotFoundException() {
		this("This Record-Number does NOT exist!");
		this.log.info("RecordNotFoundException, RecordNotFoundException");
	}

	/**
	 * Constructs a RecordNotFoundException with the specified detailed message.
	 * 
	 * @param description
	 *            A message to describe the exception.
	 */
	public RecordNotFoundException(String description) {
		super(description);
	}
}
