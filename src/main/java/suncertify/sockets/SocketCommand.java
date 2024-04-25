package suncertify.sockets;

/**
 * The enumerated list of possible commands we can send from the client to the
 * server.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public enum SocketCommand {

	/** Indicates that the command object has not been set. */
	UNSPECIFIED,
	
	/** Find matching Records by criteria action. */
	FIND, 
	
	/** Rent a Record. */
	RENT, 
	
	/** Release a Record. */
	RELEASE, 
	
	/** Update a Record. */
	MODIFY,
	
	/** Create a new Record. */
	ADD, 
	
	/** Delete a Record. */
	REMOVE, 
	
	/** Retrieve a single Record from the database by Record number. */
	GET_RECORD, 
	
	/** Lock a Record. */
	LOCK_RECORD, 
	
	/** Release a Lock on a Record. */
	UNLOCK_RECORD,
	
	/** Return all locked Record numbers. */
	GET_LOCKS,
	
	/** Return all deleted Record Numbers */
	GET_MEMORY,
	
	/** Return all valid Records in a set.*/
	GET_VALID_RECORDS;	
	

	/**
	 * Constructor, which is never called directly.
	 */
	SocketCommand() {
		// never called directly
	}
}
