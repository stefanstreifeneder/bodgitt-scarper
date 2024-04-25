package suncertify.db;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.gui.ExceptionDialog;
import suncertify.gui.SavedConfiguration;

/**
 * The class reads the database file (db-2x2.db). It reads
 * the whole file included the scheme.
 * <br>
 * <br>
 * <br> The scheme is set by the assignment:
 * <br> 
 * <br> Start of file
 * <br> 4 byte numeric, magic cookie value identifies this as a data file
 * <br> 4 byte numeric, offset to start of record zero
 * <br> 2 byte numeric, number of fields in each record
 * <br> 
 * <br> Schema description section.
 * <br> 
 * <br> Repeated for each field in a record:
 * <br> 2 byte numeric, length in bytes of field name
 * <br> n bytes (defined by previous entry), field name
 * <br> 2 byte numeric, field length in bytes
 * <br> end of repeating block
 * <br> 
 * <br> Data section. (offset into file equal to 
 * "offset to start of record zero" value)
 * <br> 
 * <br> Repeat to end of file:
 * <br> 2 byte flag. 00 implies valid record, 0x8000 implies deleted record
 * <br> 
 * <br> Record containing fields in order specified in schema section,
 * no separators between fields, each field fixed length at 
 * maximum specified in schema information
 * <br> 
 * <br> End of file  
 * <br>
 * <br>
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and was created by Roberto Pirillo originally
 * ( https://coderanch.com/t/445690/certification/Database-file-reader ).
 * <br>
 * <br>
 * The loading procedure is started by a call to the method
 * <code>loadData</code>. The class provides getter methods to obtain the 
 * evaluated values.
 * <br>
 * <br> 
 * The class stores the content of the database in an object
 * of type <code>java.io.RandomAccessFile</code>. 
 * <br> 
 * To locate a Record the static collection stores the Record 
 * number as key and the location is stored by an object of
 * type <code>RecordCookie</code>, which possesses an property to store
 * the location in the file.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class DatabaseReader {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.db.DatabaseReader</code>. Note the 
	 * <code>java.util.Logger</code>
	 * instance is customized by the class <code>suncertify.db.LoggerControl</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.db.DatabaseReader"), Level.ALL);
	
	/**
	 * A <code>java.util.Logger</code> marked 'static' to track the 
	 * static methods.
	 */
	private final static Logger logStatic =  LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.db.DatabaseReader"), Level.ALL);

	/**
	 * Length of the space for the magic cookie, which is 4.
	 */
	private static final int MAGIC_COOKIE_BYTES = 4;

	/**
	 * Length of the space for the byte length of a Record, which is 4.
	 */
	private static final int RECORD_LENGTH_BYTES = 4;

	/**
	 * Length of space to store how many fields a Record has, which is 2.
	 */
	private static final int NUMBER_OF_FIELDS_BYTES = 2;

	/**
	 * Length of space to store the name of a field of the Record, 
	 * which is 2.
	 */
	private static final int FIELD_NAME_BYTES = 2;

	/**
	 * Length of field in the database file, which is 2.
	 */
	private static final int FIELD_LENGTH_BYTES = 2;

	/**
	 * Byte length to store the value, which indicates an valid or 
	 * an unvalid Record, which is 2.
	 */
	private static final int RECORD_FLAG_BYTES = 2;

	/**
	 * The value, which indicates a deleted Record, which is 0x8000.
	 */
	private static final int DELETED = 0x8000;
	
	/**
	 * Magic cookie of 'db-2x2.db' is 514.
	 */
	private static final int MAGIC_COOKIE_DB_2x2 = 514;

	/**
	 * The name of the encoding, which is "US-ASCII".
	 */
	private static final String ENCODING = "US-ASCII";

	/**
	 * The array, which stores the bytes, which represent the magic cookie.
	 */
	private final byte[] magicCookieByteArray = 
			new byte[DatabaseReader.MAGIC_COOKIE_BYTES];

	/**
	 * Stores the length of a Record.
	 */
	private final byte[] recordLengthByteArray = new byte[RECORD_LENGTH_BYTES];

	/**
	 * Stores the number of fields.
	 */
	private final byte[] numberOfFieldsByteArray = 
			new byte[DatabaseReader.NUMBER_OF_FIELDS_BYTES];

	/**
	 * Stores all Record numbers and the according offsets in the database file,
	 * which is a property of the object <code>RecordCookie</code>.
	 */
	private final Map<Long, RecordCookie> recordNumbers = 
								new HashMap<Long, RecordCookie>();

	/**
	 * The file, which stores the content of the database file.
	 */
	private RandomAccessFile database;

	/**
	 * Stores the Record number. Starts with 1.
	 */
	private long recordNumber = 1;

	/**
	 * The offset of a Record in the database file.
	 */
	private long offsetPlus = 0;

	/**
	 * The first four bytes of the database file, represent a special number to
	 * indicate this is a database file with the known format.
	 */
	private int magicCookie;
	
	/**
	 * Stores the path to database.
	 */
	private String pathToDB;

	/**
	 * Constructs an object of this class with the path to the database file.
	 * 
	 * @param dbPath
	 *            The path to the database file.
	 */
	public DatabaseReader(final String dbPath){
		this.log.entering("DatabaseReader", "DatabaseReader", dbPath);
		this.pathToDB = dbPath;
		this.log.exiting("DatabaseReader", "DatabaseReader");
	}

	/**
	 * Utility method to read an int value by free eligible amount of bytes.
	 * Reads the first byte and pulls the evaluated value as a binary number
	 * to the start and appends the next value as a binary number to the
	 * the number before.
	 * 
	 * @param byteArray
	 *            The byte array, which should be read.
	 * @return int - The value of the bytes to read.
	 */
	public static int getValue(final byte[] byteArray) {
		DatabaseReader.logStatic.entering("DatabaseReader", "getValue", 
				byteArray);
		int value = 0;
		final int byteArrayLength = byteArray.length;
		for (int i = 0; i < byteArrayLength; i++) {
			// cares for the position to start reading
			final int shift = (byteArrayLength - 1 - i) * 8;
			// 0x000000FF= 255, cares for overflow
			value += (byteArray[i] & 0x000000FF) << shift;
		}
		DatabaseReader.logStatic.exiting("DatabaseReader", "getValue",
				Integer.valueOf(value));
		return value;
	}

	/**
	 * Starts the loading procedure to initialize all the members.
	 * 
	 * @throws IOException
	 *             If transmission problems occur.
	 */
	public void loadData() throws IOException {
		this.log.entering("DatabaseReader", "loadData");
		this.log.exiting("DatabaseReader", "loadData");
		this.loadDB();
	}

	/**
	 * Returns the file, which stores the data of the database file.
	 * 
	 * @return RandomAccessFile - Stores the client and supplies random access to
	 *         an arbitrary Record.
	 */
	public RandomAccessFile getFile() {
		this.log.entering("DatabaseReader", "getFile");
		this.log.exiting("DatabaseReader", "getFile");
		return this.database;
	}

	/**
	 * Returns a map, whereby key is the Record number and value is the offset,
	 * which is stored in the object of type <code>RecordCookie</code>.
	 * 
	 * @return Map - All Record numbers with the according offset.
	 */
	public Map<Long, RecordCookie> getRecordNumbersMap() {
		this.log.entering("DatabaseReader", "getRecordNumbersMap");
		this.log.exiting("DatabaseReader", "getRecordNumbersMap");
		return this.recordNumbers;
	}

	/**
	 * Initializes the members by reading the physical database file. If the magic
	 * cookie does not have the decimal value of 514, a dialog will be
	 * displayed.
	 * <br>
	 * If the method is used by a server within a network environment and a path
	 * to an inappropriate database is assigned, finally it is possible a dialog
	 * requests to stop the program. That exit will be done by a call to
	 * 'Runtime.getRuntime().halt()' instead to 'System.exit()', which could be
	 * not efficient enough in such a case to stop the JVM. This call omits
	 * the call to 'Runtime.getRuntime().addShutdownHook()'.
	 * 
	 * @throws IOException
	 *             If transmission problems occur.
	 */
	private void loadDB() throws IOException {
		this.log.entering("DatabaseReader", "loadDB");
		this.database = new RandomAccessFile(this.pathToDB, "rw");	
		this.database.read(this.magicCookieByteArray);
		// Assignment:
		// 4 byte numeric, magic cookie value identifies this as a client file
		this.magicCookie = getValue(this.magicCookieByteArray);
		//test magic cookie
		if (this.magicCookie != DatabaseReader.MAGIC_COOKIE_DB_2x2) {			
			final String input = JOptionPane.showInputDialog(
			null, "B & S - Magic Cookie Tester\n\n" 
					+ "Magic Cookie ("
					+ this.magicCookie	    					 
					+ ") of the "
					+ "\nfile("
					+ this.pathToDB
					+ ")"
					+ "\ndoes not match with expected "
					+ "Magic Cookie(" 
					+ DatabaseReader.MAGIC_COOKIE_DB_2x2 + ")." 					
					+ "\n\nBeware the Program can crash!"
					+ "\nIt is recommended to exit or to enter the path "
					+ "\nto an appropriate database."
					+ "\n"
					+ "\nIf You get the following error message:"
					+ "\n-  ATTENTION java.io.EOFEception  -"
					+ "\nit indicates You are using an unvalid database."
					+ "\n" 
					+ "\nYou have tried to connected to: \n"
					+ this.pathToDB
					+ "\n\nPlease enter the path to the database:",
					this.pathToDB);
			
			if(input != null){
				 if (!new File(input).isFile()) {
					ExceptionDialog.handleException("DatabaseReader: "
							+ "This is not a file!\n" + input);					
					
					final int k = JOptionPane.showConfirmDialog(null, 
							"If you are running a server,"
							+ "\n"
							+ "\nyou HAVE TO PRESS (Y)es "
							+ "\n"
							+ "\nto stop the program, because"
							+ "\nyou want to use an UNVALID database."
							+ "\nOtherwise you will have a "
							+ "\nPROGRAM CRASH."
							+ "\n"
							+ "\nIf you are running a local client press"
							+ "\n"
							+ "\n(N)o or close the dialog by its symbol"
							+ "\n"
							+ "\nto restart the connecting procedure.", 
							 		"DB Reader, DB UNVALID - SERVER",
							 			JOptionPane.YES_NO_OPTION);
					if(k  == JOptionPane.YES_NO_OPTION) {
						//System.exit(0);// may be not efficient 
						// Beware this call avoids 'Runtime.getRuntime().addShutdownHook()'
						Runtime.getRuntime().halt(1);
					}
					throw new IOException("This is not a file:\n"
							+ input);
				}
				 
				this.pathToDB = input;
				final int nB = JOptionPane.showConfirmDialog(null, 
						"B & S - Magic Cookie Tester - Store Service"
						 		+ "\n\nDo You want to store the connection"
						 		+ " data in the file 'suncertify.properties':" 
						 		+ "\n\n" + this.pathToDB
						 		, 
						 		"B & S - Magic Cookie Tester",
						JOptionPane.YES_NO_OPTION);
		    	if (nB == JOptionPane.OK_OPTION){
		    		final SavedConfiguration config = 
		    				SavedConfiguration.getSavedConfiguration();
					config.setParameter(SavedConfiguration.DATABASE_LOCATION, 
							this.pathToDB);
				}
		    	this.loadData();
			}else{
				//System.exit(0);// may be not efficient
				Runtime.getRuntime().halt(1);
			}
		}			

		this.database.read(this.recordLengthByteArray);
		this.database.read(this.numberOfFieldsByteArray);
		final int offset = getValue(this.recordLengthByteArray);
		final int numberOfFields = getValue(this.numberOfFieldsByteArray);

		final String[] fieldNames = new String[numberOfFields];
		final int[] fieldLengths = new int[numberOfFields];

		for (int i = 0; i < numberOfFields; i++) {

			// length of the name of the column header
			final byte nameLengthByteArray[] = new byte[DatabaseReader.FIELD_NAME_BYTES];
			this.database.read(nameLengthByteArray);
			final int nameLength = getValue(nameLengthByteArray);

			// name of the column header
			final byte[] fieldNameByteArray = new byte[nameLength];
			this.database.read(fieldNameByteArray);
			fieldNames[i] = new String(fieldNameByteArray, 
					DatabaseReader.ENCODING);

			// length of the maximum entry in a cell of these column
			final byte[] fieldLength = new byte[
			                                    DatabaseReader.FIELD_LENGTH_BYTES];
			this.database.read(fieldLength);
			fieldLengths[i] = getValue(fieldLength);
		}

		RecordCookie rc = null;
		
		// endless loop until database is at the end of the file
		while (true) {
			final byte[] flagByteArray = new byte[
			                                      DatabaseReader.RECORD_FLAG_BYTES];
			final int eof = this.database.read(flagByteArray);
			if (eof == -1) {
				break;
			}
			final int flag = getValue(flagByteArray);
			for (int i = 0; i < numberOfFields; i++) {
				final byte[] buffer = new byte[fieldLengths[i]];
				this.database.read(buffer);
			}
			rc = new RecordCookie(offset + this.offsetPlus, "");
			if (flag == DatabaseReader.DELETED) {
				rc.setFlagString("d");
			}
			this.recordNumbers.put(Long.valueOf(this.recordNumber), rc);
			this.recordNumber++;			
			this.offsetPlus += Record.RECORD_LENGTH;
		}		
		this.log.exiting("DatabaseReader", "getRecordNumbersMap");
	}
}
