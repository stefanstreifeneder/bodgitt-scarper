package suncertify.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import suncertify.db.LoggerControl;
import suncertify.db.Record;

/**
 * An object of the class is subtype of 
 * <code>javax.swing.table.AbstractTableModel</code>.<br>
 * <br>
 * New data will be added by an object of type Record.<br>
 * <br>
 * The class possesses a map, which uses the row number
 * as key and the Record number as value. Based on the
 * map the class provides a method, which returns the Record number
 * of the according row number.
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class RecordTableModel extends AbstractTableModel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2015L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.RecordTableModel</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.RecordTableModel"), Level.OFF);

	/**
	 * An array of <code>java.lang.String</code> objects representing the table headers.
	 */
	private String[] headerNames = { "Name", "City", "Kinds", "Staff", 
			"Rate", "ID Owner" };

	/**
	 * Stores a Record.
	 */
	private String[] recordAsStringArr;

	/**
	 * Holds all data sets displayed in the main table.
	 */
	private List<String[]> listRecordsAsStringArrs = 
			new ArrayList<String[]>();

	/**
	 * Stores all line numbers with their according Record number. Key is the
	 * line number (begins with 0), value is the Record number (begins with the
	 * lowest valid Record number).
	 */
	private Map<Integer, Long> mapIndexRecNo = new HashMap<Integer, Long>();

	/**
	 * No-arg Constructor to construct an object.
	 */
	public RecordTableModel() {
		this.log.entering("RecordTableModel", "RecordTableModel");
		this.log.exiting("RecordTableModel", "RecordTableModel");
	}

	/**
	 * Returns the column count of the table.
	 *
	 * @return int - The number or columns in the table.
	 */
	@Override
	public int getColumnCount() {
		return this.headerNames.length;
	}

	/**
	 * Returns the number of rows in the table.
	 *
	 * @return int - The number of rows in the table.
	 */
	@Override
	public int getRowCount() {
		return this.listRecordsAsStringArrs.size();
	}

	/**
	 * Gets a value from a specified index in the table.
	 *
	 * @param row
	 *            An <code>int</code> representing the row index.
	 * @param column
	 *            An <code>int</code> representing the column index.
	 * @return Object - The object located at the specified row and column.
	 */
	@Override
	public Object getValueAt(int row, int column) {
		String[] rowValues = this.listRecordsAsStringArrs.get(row);
		return rowValues[column];
	}

	/**
	 * Sets the cell value at a specified index.
	 *
	 * @param obj
	 *            The object that is placed in the table cell.
	 * @param row
	 *            The row index.
	 * @param column
	 *            The column index.
	 */
	@Override
	public void setValueAt(Object obj, int row, int column) {
		Object[] rowValues = this.listRecordsAsStringArrs.get(row);
		rowValues[column] = obj;
	}

	/**
	 * Returns the name of a column at a given column index.
	 *
	 * @param column
	 *            The specified column index.
	 * @return String - Containing the column name.
	 */
	@Override
	public String getColumnName(int column) {
		return this.headerNames[column];
	}

	/**
	 * All cells are not editable by default, therefore the method returns
	 * always false.
	 *
	 * @param row
	 *            Specified row index.
	 * @param column
	 *            Specified column index.
	 * @return boolean - Indicating if a cell is editable.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		this.log.info(
				"RecordTableModel, isCellEditable, row: " 
						+ Integer.valueOf(row) + " - column: " 
						+ Integer.valueOf(column));
		return false;
	}

	/**
	 * Returns the Record number of the Record in the specified line number.
	 * 
	 * @param index
	 *            The line number.
	 * @return long - The Record number.
	 */
	public long getRecNo(Integer index) {
		this.log.entering("RecordTableModel", "getRecNo", index);
		int indexLoc = 1;
		if (index == null) {
			indexLoc = 1;
		} else {
			indexLoc = index.intValue();
		}
		Long recNo = this.mapIndexRecNo.get(Integer.valueOf(indexLoc));
		if (recNo == null) {
			recNo = Long.valueOf(1);
		}
		this.log.entering("RecordTableModel", "getRecNo", recNo);
		return recNo.longValue();
	}

	/**
	 * Returns a map, key is the row number and 
	 * value is the according Record number.
	 * 
	 * @return Map - Key is line number and value is the Record number.
	 */
	public Map<Integer, Long> getRowRecNoMap() {
		return this.mapIndexRecNo;
	}

	/**
	 * Adds a Record object to the table. Used outside of this class. Calls the
	 * overloaded private method <code>addRecord</code>.
	 *
	 * @param rec
	 *            The Record object to add to the table.
	 */
	public void addRecord(Record rec) {		
		addRecord(rec.getName(), rec.getCity(), rec.getTypesOfWork(), 
				rec.getNumberOfStaff(),
				rec.getHourlyChargeRate(), rec.getOwner());
		this.mapIndexRecNo.put(
				Integer.valueOf(this.listRecordsAsStringArrs.indexOf(
						this.recordAsStringArr)),
				Long.valueOf(rec.getRecNo()));
	}

	/**
	 * Adds a row, which represents a Record, to the table.
	 * 
	 * @param name
	 *            Name of the contractor for home improving contractors.
	 * @param city
	 *            Where the contractor is located.
	 * @param typesOfWork
	 *            Which kind of works the contractor offers.
	 * @param numberOfStaff
	 *            How many stuff members the contractor has.
	 * @param hourlyChargeRate
	 *            The rate per hour.
	 * @param resaverationStatus
	 *            ID of a client or empty.
	 */
	private void addRecord(String name, String city, 
			String typesOfWork, int numberOfStaff, 
			String hourlyChargeRate,
			String resaverationStatus) {
		
		String[] temp = { name, city, typesOfWork, 
				Integer.toString(numberOfStaff), hourlyChargeRate, 
					resaverationStatus };
		try {
			this.listRecordsAsStringArrs.add(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.recordAsStringArr = temp;
	}
}
