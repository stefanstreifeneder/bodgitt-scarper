package suncertify.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class converts a Record object in a appropriate <code>java.lang.String</code>
 * array and vice versa.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class RecordFormatter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.RecordFormatter</code>.
	 */
	private static Logger log;

	/**
	 * Static initializer to customize the logger.
	 */
	static {
		RecordFormatter.log = LoggerControl.getLoggerBS(
				Logger.getLogger("suncertify.db.RecordFormatter"), Level.ALL);
	}

	/**
	 * Since this is a utility class (it only exists for other classes to call
	 * it's static methods), lets stop users creating unneeded instances of this
	 * class by creating a private constructor.
	 * 
	 */
	private RecordFormatter() {
		// unused
	}

	/**
	 * 
	 * Returns a <code>java.lang.String</code> array representing the fields 
	 * of a Record except the <code>flag</code> included the 
	 * Record number.<br>
	 * The scheme of the returned array looks like this:<br>
	 * [0] - name<br>
	 * [1] - city<br>
	 * [2] - types of work<br>
	 * [3] - number of staff<br>
	 * [4] - hourly rate<br>
	 * [5] - owner id<br>
	 * [6] - Record number<br>
	 * 
	 * 
	 * @param rec
	 *            The Record, which value will be stored in the array.
	 * @return String [] - The Record as an <code>java.lang.String</code> array.
	 */
	public static String[] recToStringArr(final Record rec) {
		RecordFormatter.log.entering("RecordFormatter", 
				"recordToStringArr", rec.toString());
		final String[] recordStringArray = new String[8];		
		recordStringArray[0] = rec.getFlag();
		recordStringArray[1] = rec.getName();
		recordStringArray[2] = rec.getCity();
		recordStringArray[3] = rec.getTypesOfWork();
		recordStringArray[4] = Integer.toString(rec.getNumberOfStaff());
		recordStringArray[5] = rec.getHourlyChargeRate();
		recordStringArray[6] = rec.getOwner();
		recordStringArray[7] = Long.toString(rec.getRecNo());		
		RecordFormatter.log.exiting("RecordFormatter", "recordToStringArr", 
				recordStringArray);
		return recordStringArray;
	}
	
	/**
	 * Converts a Record to a <code>java.lang.String</code> array. 
	 * Returns a <code>java.lang.String</code> representing the fields 
	 * of a Record except the <code>flag</code> and the 
	 * Record number.<br>
	 * The scheme of the returned array looks like this:<br>
	 * [0] - name<br>
	 * [1] - city<br>
	 * [2] - types of work<br>
	 * [3] - number of staff<br>
	 * [4] - hourly rate<br>
	 * [5] - owner id<br>
	 * 
	 * @param rec
	 *            The Record, which values will be stored in the array.
	 * @return String [] - The Record as an <code>java.lang.String</code> array.
	 */
	public static String[] recToStringArrNoRecNo(final Record rec) {
		RecordFormatter.log.entering("RecordFormatter", 
				"recordToStringArr", rec.toString());
		final String[] recordStringArray = new String[7];
		recordStringArray[0] = rec.getFlag();
		recordStringArray[1] = rec.getName();
		recordStringArray[2] = rec.getCity();
		recordStringArray[3] = rec.getTypesOfWork();
		recordStringArray[4] = Integer.toString(rec.getNumberOfStaff());
		recordStringArray[5] = rec.getHourlyChargeRate();
		recordStringArray[6] = rec.getOwner();
		RecordFormatter.log.exiting("RecordFormatter", "recordToStringArr", recordStringArray);
		return recordStringArray;
	}	

	/**
	 * Returns an object of type Record. 
	 * <br>
	 * <br> The scheme of the method argument must look like this:<br>
	 * [0] - Record number<br>
	 * [1] - name<br>
	 * [2] - city<br>
	 * [3] - types of work<br>
	 * [4] - number of staff<br>
	 * [5] - hourly rate<br>
	 * [6] - owner id<br>
	 * 
	 * @param recordStrings
	 *            <code>java.lang.String</code> array, which represents the values of a
	 *            Record.
	 * @return Record - The Record object.
	 */
	public static Record stringArrToRecord(final String[] recordStrings) {
		RecordFormatter.log.entering("RecordFormatter", 
				"stringRecArrToRecord", recordStrings);
		return new Record(Long.parseLong(recordStrings[7]),
				recordStrings[1], recordStrings[2], recordStrings[3],
				Integer.parseInt(recordStrings[4]), 
				recordStrings[5], recordStrings[6]);
	}
}
