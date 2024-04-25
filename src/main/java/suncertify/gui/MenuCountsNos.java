package suncertify.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;

/**
 * The class is an utility class, which stores statistic numbers 
 * as properties. 
 * <br>
 * It possesses four properties to store statistic data.
 * <br>
 * These properties are:
 * <br> <code>allValidRecs</code> - all valid Records
 * <br> <code>allRented</code> - all booked Records
 * <br> <code>allLocked</code> - all locked Records
 * <br> <code>dbSize</code> - the size of the database 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuCountsNos {
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.MenuCountsNos</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuCountsNos"), Level.ALL);
	
	/**
	 * Stores the number of all valid Records.
	 */
	private int allValidRecs;
	
	/**
	 * Stores the number of all rented Records.
	 */
	private int allRented;

	/**
	 * Stores size of the whole database.
	 */
	private int dbSize;

	/**
	 * Stores the number of all locked Records.
	 */
	private int allLocked;
	
	/**
	 * Overloaded constructor, which creates an object
	 * of the class and assigns the values
	 * of the constructor's arguments.
	 * 
	 * @param recsAll Stores the number of all valid Records.
	 * @param recsRented Stores the number of all rented Records.
	 * @param recsLocked Stores the number of all rented Records.
	 * @param sizeDB Stores the size of the database.
	 */
	public MenuCountsNos(int recsAll, 
			int recsRented,
			int recsLocked,
			int sizeDB){	
		this.log.entering("MenuCountsNos", "MenuCountsNos", new Object[]{
				Integer.valueOf(recsAll), 
				Integer.valueOf(recsLocked),
				Integer.valueOf(sizeDB),
				Integer.valueOf(recsRented)
		});
		
		this.allValidRecs = recsAll;
		this.allRented = recsRented;
		this.allLocked = recsLocked;
		this.dbSize = sizeDB; 	
		
		this.log.exiting("MenuCountsNos", "MenuCountsNos");
	}	
	
	
	/**
	 * It sets the properties of the class.
	 * 
	 * @param recsAll Sets the number of all valid Records.
	 * @param recsLocked Sets the number of all locked Records.
	 * @param recsRented Sets the number of all rented Records.
	 * @param sizeDB Sets the size of the database.
	 */
	public void setNos(int recsAll, 
			int recsLocked,
			int recsRented,
			int sizeDB){
		this.allValidRecs = recsAll; 
		this.allLocked = recsLocked;
		this.allRented = recsRented;
		this.dbSize = sizeDB;
	}	
	
	/**
	 * Returns the properties as an <code>int</code>
	 * array.
	 * <br> The array has the following elements:
	 * <br> - the number of all Records
	 * <br> - the number of all locked Records
	 * <br> - the number of all rented Records
	 * <br> - the size of the database
	 * 
	 * @return int - all statistic numbers.
	 */
	public int[] getNos(){
		return new int[]{ this.allValidRecs, 
		 this.allLocked,
		 this.allRented,
		this.dbSize };
	}
}
