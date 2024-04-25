package suncertify.db;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Public interface, which supports read and search functionalities.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public interface InterfaceClient_ReadOnly {

	/**
	 * Returns an array of Record numbers that match the specified
	 * <code>criteria</code>.  Field n in the database file is described by
	 * <code>criteria[n]</code>. A null value in <code>criteria[n]</code>
	 * (or all elements are empty Strings) matches any field value. A non-null 
	 * value in <code>criteria[n]</code>
	 * matches any field value that begins with <code>criteria[n]</code>. (For
	 * example, "Fred" matches "Fred" or "Freddy".)
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
	 * 
	 * @param criteria
	 *            Elements of a Record, which matches to Record's elements.
	 * @return long[] - Returns an array, which contains the
	 *         matching Record numbers.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */
	public long[] findByFilter(String[] criteria)throws IOException;	
	

	/**
	 * Reads a Record by Record number. If the Record is marked deleted 
	 * or the Record number does not exists a 
	 * <code>RecordNotFoundException</code> will be thrown.<br>
	 * 
	 * @param recNo
	 *            The Record number of the searched Record.
	 * @return Record - The searched Record.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or the Record is
	 *             marked deleted.
	 */
	public Record getRecord(long recNo) throws IOException, 
											RecordNotFoundException;
	
	/**
	 * Returns the size of the database. Due to the memory
	 * system of the underlying database it makes sense to provide
	 * such a method, because you can not delete physical memory
	 * space, but you can have a lot of deleted Records, which are
	 * not visible to a client.
	 * 
	 * @return long - the size of the database.
	 * @throws IOException
	 *             Indicates there is a problem to access the data. 
	 */
	public long getAllocatedMemory() throws IOException;	
	
	/**
	 * Returns a set of all Record numbers of locked Records.
	 * 
	 * @return Set - Record numbers of all locked Records.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */
	public Set<Long> getLocked() throws IOException;
	
	
	/**
	 * Returns a list, which elements are valid Records.
	 * If there is high traffic and the content of the table should 
	 * be refreshed the method findByFilter of the interface 
	 * <code>InterfaceClient_ReadOnly</code> will be insufficient, 
	 * because it returns Record numbers, which will be used to obtain 
	 * Record objects in another procedure. Within the described 
	 * mechanism it is possible some Records according to the Record 
	 * numbers, which were returned due to the call to findByFilter 
	 * are already deleted, this leads to a 
	 * <code>RecordNotFoundException</code> and the content of the 
	 * table can never refreshed. Instead the method 
	 * <code>getAllValidRecords</code> will be called, which is 
	 * robust against high traffic.
	 * 
	 * @return List - all valid Records.
	 * 
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */public List<Record> getAllValidRecords() throws IOException;
	
	
	
}
