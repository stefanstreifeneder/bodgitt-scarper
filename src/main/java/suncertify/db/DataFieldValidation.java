package suncertify.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to validate a Record's value, which should 
 * be entered in the database.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class DataFieldValidation {
	
	/**
	 * A <code>java.util.Logger</code> marked 'static' to track the 
	 * static methods.
	 */
	private final static Logger logStatic =  LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.db.DataFieldValidation"), Level.ALL);
	
	/**
	 * Byte value of zero (48).
	 */
	private final static int ASCI_0_BYTE_VALUE = 48;
	
	/**
	 * Byte value of white space (32).
	 */
	private final static int ASCI_WhiteSpace = 32;
	
	/**
	 * Highest byte value, if UTF-8 (127).
	 */
	private final static int BYTE_VALUE_HIGHEST_UTF_8 = 127;
	
	/**
	 * Byte value of the dollar currency (36).
	 */
	private final static int DOLLAR_BYTE_VALUE = 36;
	
	/**
	 * Byte value of a dot (46).
	 */
	private final static int DOT_BYTE_VALUE = 46;
	
	
	/**
	 * Private Constructor, because only the static methods will be called.
	 */
	private DataFieldValidation(){
		// unused
	}
	
	
	/**
	 * This method validates the elements of an entry, which should be written
	 * to the database file.<br>
	 * Utility method to keep the format of the database file.<br>
	 * <br>
	 * An <code>java.lang.IllegalArgumentException</code> will be thrown, if 
	 * the scheme of the database file is hurt.<br>
	 * <br>
	 * Eventually the method argument will be used  by the method 
	 * <code>persistRecord</code> of class <code>Data</code>, which expects
	 * all fields of the Record in the database, which includes the flag to
	 * indicate, whether the Record is valid or marked deleted. Afterwards
	 * the flag is set by the system and not by a client and only valid
	 * Records will be entered this way, the flag lefts unconcerned.
	 * Accordingly the method starts testing with the second field of the method
	 * argument's array.
	 * <br> The elements of the array will be tested in the following order:
	 * <br> rec[1] = name<br>
	 * rec[2] = city<br>
	 * rec[3] = services<br>
	 * rec[4] = staff count<br>
	 * rec[5] = rate<br>
	 * rec[6] = owner
	 * 
	 * @param rec The values of a Record.
	 */
	public static void validateFields(final String[] rec) {
		DataFieldValidation.logStatic.entering("DataFieldValidation", 
				"validateFields", rec);
		// Checks the first three elements of the Record,
		// which are representing the name, city and types
		// due to UTF-8
		for (int i = 1; i < 4; i++) {
			if (!testUTF(rec[i].getBytes())) {
				switch (i) {
					case 1:
						throw new IllegalArgumentException(
								"Name: Character set - Please use only UTF-8");
					case 2:
						throw new IllegalArgumentException(
								"City: " + "Character set - Please use only UTF-8");
					case 3:
						throw new IllegalArgumentException(
								"Types: " + "Character set - Please use only UTF-8");
					default:
						break;
				}
			}
		}
		
		final byte[] bArr = rec[5].getBytes();
		if(bArr[0] != DOLLAR_BYTE_VALUE){
			throw new IllegalArgumentException(
					"You have to use the '$' currency.");
		}else if(bArr[1] == ASCI_0_BYTE_VALUE && bArr[2] != DOT_BYTE_VALUE){
			throw new IllegalArgumentException(
					"Hourly Rate wrong pattern: NO - $0xxx.xx");
		}
		
		if (rec[1].getBytes().length > Record.NAME_LENGTH || 
				rec[1].getBytes().length < 1 || rec[1].startsWith(" ")) {
			throw new IllegalArgumentException(
					"'Name': 1 - 32 Signs and do not start with a whitespace");
		}else if (rec[2].getBytes().length > Record.CITY_LENGTH || 
				rec[2].getBytes().length < 1 || rec[2].startsWith(" ")) {
			throw new IllegalArgumentException(
					"'City': 1 - 64 Signs and do not start with a whitespace");
		}else if (rec[3].getBytes().length > Record.TYPES_OF_WORK_LENGTH || 
				rec[3].getBytes().length < 1 || rec[3].startsWith(" ")) {		
			throw new IllegalArgumentException(
					"'Kinds': 1 - 64 Signs and do not start with a whitespace");
		}else if (Integer.parseInt(rec[4]) == 0 || rec[4].startsWith(" ")) {
			throw new IllegalArgumentException(
					"'Staff': wrong Input! Pattern: " + "1-999999");
		}else if (!rec[4].matches("\\d{1,6}")) {
			throw new IllegalArgumentException(
					"'Staff': wrong Input! Pattern: " + "1-999999");
		}else if (!rec[5].matches("\\p{Sc}\\d{1,4}\\.\\d\\d")) {
			throw new IllegalArgumentException(
					"Hourly Rate wrong Input! Pattern: " + "$xxxx.xx");
		}else if (!rec[6].matches("\\d{0,8}")) {
			throw new IllegalArgumentException(
					"'ID Number' wrong! Pattern: " + "xxxxxxxx");
		}else if (rec[6].equals("0")) {
			throw new IllegalArgumentException(
					"'ID Number' wrong! Pattern: " + "1-99999999");
		}
		DataFieldValidation.logStatic.exiting("DataFieldValidation", 
				"validateFields");
	}

	/**
	 * Checks to confirm UTF-8.
	 * 
	 * @param bArr
	 *            The byte array to be checked.
	 * @return boolean - True, if the <code>byte</code> array is accordingly
	 * to UTF-8.
	 */
	private static boolean testUTF(final byte[] bArr) {
		DataFieldValidation.logStatic.entering("DataFieldValidation", 
				"testUTF", bArr);
		for (int i = 0; i < bArr.length; i++) {
			if (bArr[i] < ASCI_WhiteSpace || bArr[i] > BYTE_VALUE_HIGHEST_UTF_8
					) {
				return false;
			}
		}
		DataFieldValidation.logStatic.exiting("DataFieldValidation", 
				"testUTF");
		return true;
	}
}
