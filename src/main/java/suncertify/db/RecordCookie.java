package suncertify.db;

/**
 * The class possesses two properties. One of them
 * stores the counted byte position in the database file
 * and the other indicates whether the Record is valid ("")
 * or marked deleted ("d").
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RecordCookie {
	
	/**
	 * Stores the location in the database, where the
	 * Record data starts.
	 */
	private final long locationInFile;
	
	/**
	 * Stores, whether the Record is valid or marked
	 * deleted.
	 */
	String flag;
	
	/**
	 * Creates an object of the class and initializes its
	 * properties.
	 * 
	 * @param posInFile Location in the database.
	 * @param flagStr Stores, whether the Record is valid
	 * or marked deleted.
	 */
	public RecordCookie(final long posInFile, final String flagStr){
		this.locationInFile = posInFile;
		this.flag = flagStr;
	}
	
	/**
	 * Returns the location in the database.
	 * 
	 * @return long - the counted byte position in the database.
	 */
	public long getLocationInFile(){
		return this.locationInFile;
	}
	
	/**
	 * Returns whether the Record is valid ("") or marked deleted ("d").
	 * 
	 * @return String which stores whether the Record is valid
	 * or marked deleted.
	 */
	public String getFlagString(){
		return this.flag;
	}
	
	
	/**
	 * Sets the value, whether the Record is valid or 
	 * marked deleted.
	 * <br>
	 * Please use:
	 * <br> "" - valid
	 * <br> "d" - deleted
	 * @param flagStr Indicates whether the Record is deleted.
	 */
	public void setFlagString(final String flagStr){
		this.flag = flagStr;
	}

}
