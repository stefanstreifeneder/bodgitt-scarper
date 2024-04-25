package suncertify.db;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * A Record object is a representation of a data set, which depicts an 
 * offer of a home improvement contractor.<br>
 * <br>
 * All fields are stored in the database file except the Record number.<br>
 * <br>
 * The part of the database file has the following scheme:<br>
 * 2 byte - <code>flag</code> - indicates a valid (both bytes have the value 0)
 * or a deleted Record (both bytes have together the value 0x8000)<br>
 * 32 byte - <code>name</code> - the name of the home improvement contractor<br>
 * 64 byte - <code>city</code> - in which city the company is located<br>
 * 64 byte - <code>typesOfWork</code> - which services the company offers (e.g.
 * painting)<br>
 * 6 byte - <code>numberOfStaff</code> - the number of the employees<br>
 * 8 byte - <code>hourlyChargeRate</code> - the charge for a hour<br>
 * 8 byte - <code>owner</code> - the ID of a client, who rented the service<br>
 * <br>
 * A Record is equal, if the Record number is equal.
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class Record implements Serializable{

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 14L;

	/**
	 * The maximum length of the flag, which is 2.
	 */
	public static final int FLAG_LENGTH = 2;

	/**
	 * The maximum length of the name, which is 32.
	 */
	public static final int NAME_LENGTH = 32;

	/**
	 * The maximum length of the city, which is 64.
	 */
	public static final int CITY_LENGTH = 64;

	/**
	 * The maximum length of the types of work, which is 64.
	 */
	public static final int TYPES_OF_WORK_LENGTH = 64;

	/**
	 * The maximum length of the number of staff, which is 6.
	 */
	public static final int NUMBER_OF_STAFF_LENGTH = 6;

	/**
	 * The maximum length of the hourly rate, which is 8.
	 */
	public static final int HOURLY_CHARGE_RATE_LENGTH = 8;

	/**
	 * The maximum length of the ID number of the owner, which is 8.
	 */
	public static final int OWNER_LENGTH = 8;

	/**
	 * The size (184 bytes) of a complete Record in the database. Calculated by
	 * adding all fields together. 
	 */
	public static final int RECORD_LENGTH = 
			+ Record.FLAG_LENGTH 
			+ Record.NAME_LENGTH 
			+ Record.CITY_LENGTH
			+ Record.TYPES_OF_WORK_LENGTH 
			+ Record.NUMBER_OF_STAFF_LENGTH 
			+ Record.HOURLY_CHARGE_RATE_LENGTH
			+ Record.OWNER_LENGTH;
	
	/**
	 * The number of the Record's fields(6) in the database,
	 * without the flag.
	 */
	public static final int RECORD_FIELDS_WITHOUT_FLAG = 6;
	
	/**
	 * The number of fields(7) of a Record in the database without
	 * the flag but with the Record number.
	 */
	public static final int RECORD_FIELDS_WITHOUT_FLAG_PLUS_RECNO = 7;
	
	/**
	 * The number of all fields(8) inclusive the flag of a Record in 
	 * the database and the Record number.
	 */
	public static final int RECORD_FIELDS_ALL_PLUS_RECNO = 8;
	
	/**
	 * Default value (-1) of a Record's Record number, whereby
	 * the Record number is not appropriate for the underlying
	 * database.
	 *  
	 */
	public static final long RECNO_DEFAULT_NOT_EXIST = -1;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. Have to be marked transient, else a a
	 * <code>java.io.NotSerializableException</code> will be thrown. The Logger
	 * namespace is <code>suncertify.db.Record</code>.
	 */
	private transient Logger log = Logger.getLogger("suncertify.db.Record");

	/**
	 * Stores the value of a byte used by <code>flag</code>.
	 */
	private final static byte flagByte = 0;
	
	/**
	 * The byte array, which stores the two bytes to
	 * initialize the <code>flag</code>.
	 */
	private final static byte[] flagByteArray = {flagByte, flagByte};
	
	/**
	 * The flag <code>java.lang.String</code>, which indicates
	 * whether a valid Record (00) or a deleted Record (0x8000).
	 * The property is not accessible. It will be set automatically 
	 * by the program. A new Record can not be marked deleted.
	 */
	private String flag = new String(flagByteArray);

	/**
	 * Stores the Record number of the Record.
	 */
	private long recNo = Record.RECNO_DEFAULT_NOT_EXIST;

	/**
	 * Stores the name of the home improvement contractor.
	 */
	private String name = "Name of Company";

	/**
	 * Stores the city of the home improvement contractor.
	 */
	private String city = "Name of location";

	/**
	 * Stores the types of work of the home improvement contractor.
	 */
	private String typesOfWork = "Types of work";

	/**
	 * Stores the number of staff of the home improvement contractor.
	 */
	private int numberOfStaff = -1;

	/**
	 * Stores the hourly rate of the home improvement contractor.
	 */
	private String hourlyChargeRate = "$0.0";

	/**
	 * Stores the  number of the owner, who booked a Record.
	 */
	private String owner = "";
	
	
	/**
	 * A noarg constructor to create an object of the class.
	 * Beware the default values are not appropriate to the
	 * underlying database, please use the setter methods to initialize
	 * the properties. The field <code>flag</code> will be initialized
	 * in the constructor appropriate for a valid Record.
	 * <br> The properties, which have to be initialized are:
	 * <br> - <code>recNo</code>
	 * <br> - <code>name</code>
	 * <br> - <code>city</code>
	 * <br> - <code>typesOfWork</code>
	 * <br> - <code>numberOfStaff</code>
	 * <br> - <code>hourlyChargeRate</code>
	 * <br> The empty constructor conforms with JavaBean requirements 
	 * as well.
	 */
	public Record() {
		this.log.entering("Record", "Record");
		this.log.exiting("Record", "Record");
	}
	
	/**
	 * An overloaded constructor to create an object of the class,
	 * which initializes the properties by the constructor arguments.
	 * The field <code>flag</code> will be initialized
	 * in the constructor appropriate for a valid Record.
	 *  
	 * @param recNoPara The Record number.
	 * @param namePara The name of the home improvement contractor.
	 * @param cityPara The city where to find the contractor.
	 * @param services The services offered by the contractor.
	 * @param staffCount The number of employees.
	 * @param hourRate The rate for one hour.
	 * @param ownerId The id of a home owner.
	 * 
	 */
	public Record(final long recNoPara,
					final String namePara,
					final String cityPara,
					final String services,
					final int staffCount,
					final String hourRate,
					final String ownerId) {
		this.log.entering("Record", "Record");
		this.recNo = recNoPara;		
		this.name = namePara;
		this.city = cityPara;
		this.typesOfWork = services;
		this.numberOfStaff = staffCount;
		this.hourlyChargeRate = hourRate;
		this.owner = ownerId;
		this.log.exiting("Record", "Record");
	}

	/**
	 * Returns the <code>flag</code> of the Record.
	 * 
	 * @return String - The <code>flag</code> which stores whether the Record is
	 *         deleted or not.
	 */
	public String getFlag() {
		this.log.entering("Record", "getFlag");
		this.log.exiting("Record", "getFlag", this.flag);
		return this.flag;
	}

	/**
	 * Returns the Record number of this Record.
	 * 
	 * @return long - The Record number.
	 */
	public long getRecNo() {
		this.log.entering("Record", "getRecNo");
		this.log.exiting("Record", "getRecNo", Long.valueOf(this.recNo));
		return this.recNo;
	}

	/**
	 * Sets the Record number of the Record.
	 * 
	 * @param recNoParam
	 *            The Record number.
	 */
	public void setRecNo(final long recNoParam) {
		this.log.entering("Record", "setRecNo", Long.valueOf(recNoParam));
		this.recNo = recNoParam;
		this.log.exiting("Record", "setRecNo", Long.valueOf(this.recNo));
	}

	/**
	 * Returns the name of the home improvement contractor.
	 * 
	 * @return String - The name of the home improvement contractor.
	 */
	public String getName() {
		this.log.entering("Record", "getName");
		this.log.exiting("Record", "getName", this.name);
		return this.name;
	}

	/**
	 * Sets the name of the home improvement contractor.
	 * 
	 * @param nameParam
	 *            The name of the home improvement contractor.
	 */
	public void setName(final String nameParam) {
		this.log.entering("Record", "setName", nameParam);
		this.name = nameParam;
		this.log.exiting("Record", "setName", this.name);
	}

	/**
	 * Returns the ID number of the owner, who reserved the Record.
	 * 
	 * @return long - The ID number of the owner.
	 */
	public String getOwner() {
		this.log.entering("Record", "getOwner");
		this.log.exiting("Record", "getOwner", this.owner);
		return this.owner;
	}

	/**
	 * Sets the ID number of the owner, who reserved the Record.
	 * 
	 * @param ownerParam
	 *            The ID of the owner.
	 */
	public void setOwner(final String ownerParam) {
		this.log.entering("Record", "setOwner", ownerParam);
		this.owner = ownerParam;
		this.log.exiting("Record", "setOwner", this.owner);
	}

	/**
	 * Returns the element city of the Record.
	 * 
	 * @return String - The city, where the home improvement contractor resides.
	 */
	public String getCity() {
		this.log.entering("Record", "getCity");
		this.log.exiting("Record", "getCity", this.city);
		return this.city;
	}

	/**
	 * Sets the element city of the Record.
	 * 
	 * @param cityParam
	 *            The home city of the contractor.
	 */
	public void setCity(final String cityParam) {
		this.log.entering("Record", "setCity", cityParam);
		this.city = cityParam;
		this.log.exiting("Record", "setCity", this.city);
	}

	/**
	 * Returns the types of work of the home improvement contractor.
	 * 
	 * @return String - The types of work.
	 */
	public String getTypesOfWork() {
		this.log.entering("Record", "getTypesOfWork");
		this.log.exiting("Record", "getTypesOfWork", this.typesOfWork);
		return this.typesOfWork;
	}

	/**
	 * Sets the types of work of the home improvement contractor.
	 * 
	 * @param typesOfWorkParam
	 *            Which types of work the contractor offers.
	 */
	public void setTypesOfWork(final String typesOfWorkParam) {
		this.log.entering("Record", "setTypesOfWork", typesOfWorkParam);
		this.typesOfWork = typesOfWorkParam;
		this.log.exiting("Record", "setTypesOfWork", this.typesOfWork);
	}

	/**
	 * Returns the number of staff of the home improvement contractor.
	 * 
	 * @return int - The number of staff.
	 */
	public int getNumberOfStaff() {
		this.log.entering("Record", "getNumberOfStaff");
		this.log.exiting("Record", "getNumberOfStaff", Integer.valueOf(this.numberOfStaff));
		return this.numberOfStaff;
	}

	/**
	 * Sets the number of staff of the home improvement contractor.
	 * 
	 * @param numberOfStaffPara
	 *            How many employees.
	 */
	public void setNumberOfStaff(final int numberOfStaffPara) {
		this.log.entering("Record", "setNumberOfStaff", Integer.valueOf(this.numberOfStaff));
		this.numberOfStaff = numberOfStaffPara;
		this.log.exiting("Record", "setNumberOfStaff", Integer.valueOf(this.numberOfStaff));
	}

	/**
	 * Returns the hourly rate of the home improvement contractor.
	 * 
	 * @return String - The hourly rate ($(d)(d)(d)d.dd).
	 */
	public String getHourlyChargeRate() {
		this.log.entering("Record", "getHourlyChargeRate");
		this.log.exiting("Record", "getHourlyChargeRate", this.hourlyChargeRate);
		return this.hourlyChargeRate;
	}

	/**
	 * Sets the hourly rate of the Record ($(d)(d)(d)d.dd).
	 * 
	 * @param hourlyChargeRateParam
	 *            Charge for each hour.
	 */
	public void setHourlyChargeRate(final String hourlyChargeRateParam) {
		this.log.entering("Record", "setHourlyChargeRate", this.hourlyChargeRate);
		this.hourlyChargeRate = hourlyChargeRateParam;
		this.log.exiting("Record", "setHourlyChargeRate", this.hourlyChargeRate);
	}

	/**
	 * Checks whether two Record objects are the same by comparing their Record
	 * numbers. Since a Record number is unique, if two Record
	 * numbers are the same, the two Record must be the same object.
	 * 
	 * @param aRecord
	 *            The object, which will be tested against an object of this
	 *            class.
	 * @return boolean - Indicates, whether the objects are equal or not.
	 */
	@Override
	public boolean equals(final Object aRecord) {
		if (!(aRecord instanceof Record)) {
			return false;
		}
		final Record otherRecord = (Record) aRecord;
		return (this.recNo == RECNO_DEFAULT_NOT_EXIST) ? 
				(otherRecord.getRecNo() == RECNO_DEFAULT_NOT_EXIST) : 
					this.recNo == otherRecord.getRecNo();
	}

	/**
	 * Returns a hash code for this Record.
	 * 
	 * @return int - Returns the hash code value.
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(this.recNo).hashCode();
	}

	/**
	 * Returns a <code>java.lang.String</code> representation of the Record.
	 * 
	 * @return String - A Record with all fields inclusive flag and record
	 *         number.
	 */
	@Override
	public String toString() {
		final String retVal = "[" + this.flag + "; " + this.recNo + "; " + this.name + "; " + this.city + "; "
				+ this.typesOfWork + "; " + this.numberOfStaff + "; " 
				+ this.hourlyChargeRate + "; " + this.owner + "]";

		return retVal;
	}

	/**
	 * Deserializes an object of this class. It is never
	 * called directly.
	 * 
	 * @param in
	 *            The stream to read client from in order to restore the object.
	 * @throws IOException
	 *             if I/O errors occur.
	 * @throws ClassNotFoundException
	 *             If the class for an object being restored cannot be found.
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.log = Logger.getLogger("suncertify.db.Record");
		in.defaultReadObject();
	}
}
