package suncertify.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import suncertify.gui.ExceptionDialog;

/**
 * db-name: scjddb
 * password: isabella
 * 
 * DB 
 * RECNO
 * NAME
 * CITY
 * TYPES
 * STAFF
 * RATE
 * OWNER
 * 
 * The following db scheme:
 * RECNO int
 * NAME varchar(32)
 * CITY varchar(64)
 * TYPES varchar(64)
 * STAFF varchar(6)
 * RATE varchar(8)
 * OWNER varchar(8)
 * 
 * 
 * 
 * 
 * The class supplies access to a certain kind of database file and 
 * is able to manipulate the content of the database. The class expects
 * the database to store all data sets one after the other. Due to the
 * memory management of the database an object of type 
 * <code>java.io.RandomAccessFile</code> is used to access the 
 * database file, instead to create an connection by using
 * <code>java.sql.Connection</code> and SQL commands. The class
 * controls the database by the class collection 
 * <code>recordNumbers</code> of type <code>java.util.HashMap</code>.
 * <br>
 * <br>
 * Note that since this class should only be used by one class the access 
 * level is reduced to default.
 * <br>
 * <br>
 * The class implements the interface <code>suncertify.db.DBAccess</code>. 
 * The interface determines how the database file should be accessed (see 
 * the public methods).
 * <br>
 * <br>
 * The static collection <code>recordNumbers</code> will be loaded by 
 * using an object of type <code>suncertify.db.DatabaseReader</code>, which 
 * is used by the constructor one time at the beginning of a session. During 
 * that session the class collection will be maintained by the class and 
 * not by further reloading <code>recordNumbers</code> like at the 
 * beginning of a session. The class provides another method called 
 * <code>loadDB</code> to load the database, which enables to choose
 * between either to use an object of class <code>DatabaseReader</code> like
 * the constructor does it or to use another method, which is called
 * <code>loadRecordNumners</code>.   
 * <br>
 * <br>
 * The class provides a package private 
 * method <code>getRecords</code>, which propagates the collection
 * called <code>recordNumbers</code>. The propagating method is actually 
 * a facade to the private method <code>getRecordList</code>, which 
 * works like <code>loadRecordNumners</code> without filling the class 
 * collection.
 * <br>
 * <br>
 * The underlying database stores data sets one after the other. 
 * Therefore the data of the first Record are stored always at the same 
 * position, the same applied to the second, the third and so on. The 
 * database stores the data sets without a field for an unique ID to 
 * identify a Record. The ID is represented by a Record 
 * number, which is created within this class. The Record 
 * numbers will be stored in the class collection of 
 * type <code>HashMap</code>, whereby the Record number 
 * is the key and  the value is represented by an object of type 
 * <code>RecordCookie</code>. An object of <code>RecordCookie</code> 
 * possesses two properties, the first property represents the 
 * counted byte position in the database file. The second property
 * is of type <code>String</code>, which indicates by an empty String
 * it is a valid Record or by "d" it is a deleted marked Record.
 * <br>
 * <br> Afterwards the Record numbers are bound to a space in the 
 * database file and they will never change, the first data set will always 
 * have the Record number 1, the second Record has number 2 and so on 
 * independent whether they are marked as deleted or not.
 * <br> 
 * The class forces to use space of deleted marked Records at first place, 
 * if a new Record should be added to the database file. A new Record will 
 * stored  directly after the last one, if there is no deleted Record and 
 * the Record number is the sum of all Records plus one. 
 * <br>
 * <br>
 * To use the methods <code>deleteRecord</code> and 
 * <code>updateRecord</code>, first a call to the method 
 * <code>lockRecord</code> must be done. The method 
 * <code>lockRecord</code> creates a lock cookie of type 
 * <code>long</code>. The lock cookie must be used as a method argument 
 * of the methods <code>deleteRecord</code>, <code>updateRecord</code> 
 * and <code>unlock</code>.
 * <br>
 * <br>
 * You have to keep a certain order of method calls.<br>
 * Update:
 * <blockquote><pre>
 * long lockCookie = dataInstance.lockRecord(recordNumber);
 * dataInstance.updateRecord(recordNumber, data, lockCookie);
 * dataInstance.unLock(recordNumber, lockCookie);
 * </pre></blockquote>
 * Delete:
 * <blockquote><pre>
 * long lockCookie = dataInstance.lockRecord(recordNumber);
 * dataInstance.deleteRecord(recordNumber, lockCookie);
 * dataInstance.unLock(recordNumber, lockCookie);
 * </pre></blockquote>
 * <br> Beware the locking system does not care how many locks 
 * a client holds at the same time. 
 * 
 * 
 * 
 * @see RecordDatabaseAdapter
 * @see DatabaseReader
 * @see LockManager
 * @author stefan.streifeneder@gmx.de
 */
class Data implements DBAccess{
	
	/**
	 * The Logger instance. All log messages from this class are routed 
	 * through this member. The Logger namespace is 
	 * <code>suncertify.db.Data</code>. Note the 
	 * <code>java.util.logging.Logger</code> instance is customized by 
	 * the class <code>suncertify.db.Data</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.db.Data"), Level.ALL);

    /**
	 * A static structure that keeps track of the locked Records.
	 */
	private final LockManager lockingManager;

	/**
     * The physical file on disk containing the Records.
     */
    private static RandomAccessFile database = null;

    /**
     * Contains an index for the primary key (Record number) 
     * to file location, which is a property of the object
     * <code>RecordCookie</code>.
     */
    private static Map<Long, RecordCookie> recordNumbers
            = new HashMap<Long, RecordCookie>();

    /**
     * Cares for persistence of the class collection.
     */
    private static ReadWriteLock recordNumbersLock
            = new ReentrantReadWriteLock();
    
    /**
	 * Stores the size of the database. 
	 */
	private static long dbSize;

	/**
	 * Stores the path to the database, which is accessed recently.
	 */
	private static String recentPathToDB;

	/**
	 * The offset (70) to start the search within
	 * the database. The first 70 bytes are
	 * used to describe the scheme of the database.
	 */
	private final static int DB_SCHEME_SIZE = 70;	
	
	/**
	 * The connection to the database.
	 */
	private static Connection con;
	
	/**
	 * The statement to conduct a query to the database.
	 */
	private static Statement stmt;

    /**
     * Constructor that accepts the database path as a parameter.
     * <br>
     * All instances of this class share the same database.
     * <br>
     * An object of type <code>suncertify.db.DatabaseReader</code> is used
     * to load the database.
     * 
     * @param nameDB The name of the database.
     * 
     * @param password The password to access the database.
     *
     * @throws FileNotFoundException if the database file cannot be found.
     * @throws IOException if the database file cannot be written to.
     */
    @SuppressWarnings("resource")
	Data(final String nameDB, final String password)
            throws FileNotFoundException, IOException {
        this.log.entering("Data", "Data", new Object[] {nameDB, password});
        this.lockingManager = new LockManager();
        Data.recentPathToDB = nameDB + password;
        String url = "jdbc:mysql://localhost/" + nameDB + "?" +
                "user=root&password=" + password;
        //test
        System.out.println("Data, Data, dbName: " + nameDB + " - password: "
        		+ password);
        //=============
        //String url = "jdbc:mysql://localhost/scjddb?user=root&password=isabella";
        //=============
        
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		String query1 = "select * from VENDORS";		
		ResultSet rs1 = null;
		String xs = "";		
		try {
			stmt = con.createStatement();
			rs1 = stmt.executeQuery(query1);
			while (rs1.next()) {
				xs = rs1.getString("RECNO");
				Data.recordNumbers.put(Long.valueOf(xs), null);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		this.log.exiting("Data", "Data");
    }
    
    
    
    /**
	 * Reads a Record by Record number. If the Record is marked deleted 
	 * or the Record number does not exists a 
	 * <code>RecordNotFoundException</code> will be thrown.<br>
	 * <br>
	 * The returned elements of the <code>java.lang.String</code> array 
	 * are:<br>
	 * [0] flag<br>
	 * [1] name<br>
	 * [2] city<br>
	 * [3] typesOfWork <br>
	 * [4] numberOfStaff<br>
	 * [5] rate<br>
	 * [6] idOwner<br>
	 * [7] recNo<br>
	 * <br>
	 * <br>
	 * Remark: <code>java.lang.RuntimeException</code><br>
	 * The method wraps an <code>java.io.IOException</code> within a 
	 * <code>RuntimeException</code> to keep the signature of the 
	 * interface <code>DBAccess</code>. The <code>RuntimeException</code> 
	 * takes the exception as a constructor argument. 
	 * 
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return String[] - Each field represents a property of the Record.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is marked
	 *             deleted.
	 */
    @Override
	public String[] readRecord(final long recNo) throws 
										RecordNotFoundException {
    	String[] recNew = null;
    	Data.recordNumbersLock.readLock().lock();
        try {
        	if(!Data.recordNumbers.containsKey(Long.valueOf(recNo))) {
        		throw new RecordNotFoundException("Data: This Record number does "
        				+ "not exists: " + recNo);
        	}
            try {            	
            	recNew = retrieveRecord(recNo);
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        } finally {
        	Data.recordNumbersLock.readLock().unlock();
            this.log.exiting("Data", "readRecord");
        }        
        return recNew;
	}

    
    /**
	 * Returns an array of Record numbers that match the specified
	 * <code>criteria</code>. Field n in the database file is described 
	 * by <code>criteria[n]</code>. 
	 * <br> <b>A null value in <code>criteria[n]</code> matches any field 
	 * value.</b> This means, if an element of the method argument is null
	 * all valid Records will be returned.
	 * <br> A non-null value in <code>criteria[n]</code> matches any field 
	 * value that begins with <code>criteria[n]</code>. (For example, "Fred" 
	 * matches "Fred" or "Freddy"). The method behaves case insensitive.
	 * <br>
	 * <br> 
	 * The fields of the method argument <code>criteria</code> must
	 * have the following order:
	 * <br> [0]flag
	 * <br> [1]name
	 * <br> [2]city
	 * <br> [3]types
	 * <br> [4]staff
	 * <br> [5]rate
	 * <br> [6]owner
	 * <br>
	 * <br>
	 * If You want to search for just one field of a data set e.g. for 'types'
	 * You have to pass {"", "", "", types}.
	 * <br> All array elements beyond of the sixth element will be disregarded.
	 * <br> If the method argument is null a 
	 * <code>java.lang.NullpointerException</code> will be thrown.
	 * <br> If all array elements are empty Strings all valid Records will
	 * be returned.
	 * 
	 * 
	 * 
	 * @param criteria
	 *            The database will be investigated according to the 
	 *            criteria.
	 * @return long[] - An array of Record numbers, which matches the 
	 * 				criteria.
	 */
	@SuppressWarnings("resource")
	@Override
	public long[] findByCriteria(final String[] criteria) {
		final Set<Long> returnValue = new HashSet<Long>();		
		switch (criteria.length) {
		case 1:			
			if(!criteria[0].isEmpty()) {
				String query1 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs1 = null;	
				try {
					stmt = con.createStatement();
					rs1 = stmt.executeQuery(query1);
					while (rs1.next()) {
						returnValue.add(Long.valueOf(rs1.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}			
			}
			break;
		case 2:
			
			if(!criteria[0].isEmpty()) {
				String query2 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs2 = null;	
				try {
					stmt = con.createStatement();
					rs2 = stmt.executeQuery(query2);
					while (rs2.next()) {
						returnValue.add(Long.valueOf(rs2.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(criteria[1] != null) {
				String querycity = "select * from VENDORS"
						+ "		where CITY='" + criteria[1] + "'";		
				ResultSet rscity = null;	
				try {
					stmt = con.createStatement();
					rscity = stmt.executeQuery(querycity);
					while (rscity.next()) {
						returnValue.add(Long.valueOf(rscity.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			break;
		case 3:
			
			if(!criteria[0].isEmpty()) {
				String query3 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs3 = null;	
				try {
					stmt = con.createStatement();
					rs3 = stmt.executeQuery(query3);
					while (rs3.next()) {
						returnValue.add(Long.valueOf(rs3.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[1].isEmpty()) {
				String querycity3 = "select * from VENDORS"
						+ "		where CITY='" + criteria[1] + "'";		
				ResultSet rscity3 = null;	
				try {
					stmt = con.createStatement();
					rscity3 = stmt.executeQuery(querycity3);
					while (rscity3.next()) {
						returnValue.add(Long.valueOf(rscity3.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[2].isEmpty()) {
				String querytypes = "select * from VENDORS"
						+ "		where TYPES='" + criteria[2] + "'";		
				ResultSet rstypes = null;	
				try {
					stmt = con.createStatement();
					rstypes = stmt.executeQuery(querytypes);
					while (rstypes.next()) {
						returnValue.add(Long.valueOf(rstypes.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			break;
		case 4:
			
			if(!criteria[0].isEmpty()) {
				String query4 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs4 = null;	
				try {
					stmt = con.createStatement();
					rs4 = stmt.executeQuery(query4);
					while (rs4.next()) {
						returnValue.add(Long.valueOf(rs4.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[1].isEmpty()) {
				String querycity4 = "select * from VENDORS"
						+ "		where CITY='" + criteria[1] + "'";		
				ResultSet rscity4 = null;	
				try {
					stmt = con.createStatement();
					rscity4 = stmt.executeQuery(querycity4);
					while (rscity4.next()) {
						returnValue.add(Long.valueOf(rscity4.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[2].isEmpty()) {
				String querytypes4 = "select * from VENDORS"
						+ "		where TYPES='" + criteria[2] + "'";		
				ResultSet rstypes4 = null;	
				try {
					stmt = con.createStatement();
					rstypes4 = stmt.executeQuery(querytypes4);
					while (rstypes4.next()) {
						returnValue.add(Long.valueOf(rstypes4.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[3].isEmpty()) {
				String queryStaff = "select * from VENDORS"
						+ "		where STAFF='" + criteria[3] + "'";		
				ResultSet rsStaff = null;	
				try {
					stmt = con.createStatement();
					rsStaff = stmt.executeQuery(queryStaff);
					while (rsStaff.next()) {
						returnValue.add(Long.valueOf(rsStaff.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			break;
		case 5:
			
			if(!criteria[0].isEmpty()) {
				String query5 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs5 = null;	
				try {
					stmt = con.createStatement();
					rs5 = stmt.executeQuery(query5);
					while (rs5.next()) {
						returnValue.add(Long.valueOf(rs5.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[1].isEmpty()) {
				String querycity5 = "select * from VENDORS"
						+ "		where CITY='" + criteria[1] + "'";		
				ResultSet rscity5 = null;	
				try {
					stmt = con.createStatement();
					rscity5 = stmt.executeQuery(querycity5);
					while (rscity5.next()) {
						returnValue.add(Long.valueOf(rscity5.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[2].isEmpty()) {
				String querytypes5 = "select * from VENDORS"
						+ "		where TYPES='" + criteria[2] + "'";		
				ResultSet rstypes5 = null;	
				try {
					stmt = con.createStatement();
					rstypes5 = stmt.executeQuery(querytypes5);
					while (rstypes5.next()) {
						returnValue.add(Long.valueOf(rstypes5.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			if(!criteria[3].isEmpty()) {
				String queryStaff5 = "select * from VENDORS"
						+ "		where STAFF='" + criteria[3] + "'";		
				ResultSet rsStaff5 = null;	
				try {
					stmt = con.createStatement();
					rsStaff5 = stmt.executeQuery(queryStaff5);
					while (rsStaff5.next()) {
						returnValue.add(Long.valueOf(rsStaff5.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			
			if(!criteria[4].isEmpty()) {
				String queryRate = "select * from VENDORS"
						+ "		where RATE='" + criteria[4] + "'";		
				ResultSet rsRate = null;	
				try {
					stmt = con.createStatement();
					rsRate = stmt.executeQuery(queryRate);
					while (rsRate.next()) {
						returnValue.add(Long.valueOf(rsRate.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}
			
			
			break;
		case 6:
			
			if(!criteria[0].isEmpty()) {
				String query6 = "select * from VENDORS"
						+ "		where NAME='" + criteria[0] + "'";		
				ResultSet rs6 = null;	
				try {
					stmt = con.createStatement();
					rs6 = stmt.executeQuery(query6);
					while (rs6.next()) {
						returnValue.add(Long.valueOf(rs6.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}		
			
			if(!criteria[1].isEmpty()) {
				String querycity6 = "select * from VENDORS"
						+ "		where CITY='" + criteria[1] + "'";		
				ResultSet rscity6 = null;	
				try {
					stmt = con.createStatement();
					rscity6 = stmt.executeQuery(querycity6);
					while (rscity6.next()) {
						returnValue.add(Long.valueOf(rscity6.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}			
			
			if(!criteria[2].isEmpty()) {
				String querytypes6 = "select * from VENDORS"
						+ "		where TYPES='" + criteria[2] + "'";		
				ResultSet rstypes6 = null;	
				try {
					stmt = con.createStatement();
					rstypes6 = stmt.executeQuery(querytypes6);
					while (rstypes6.next()) {
						returnValue.add(Long.valueOf(rstypes6.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}			
			
			if(!criteria[3].isEmpty()) {
				String queryStaff6 = "select * from VENDORS"
						+ "		where STAFF='" + criteria[3] + "'";		
				ResultSet rsStaff6 = null;	
				try {
					stmt = con.createStatement();
					rsStaff6 = stmt.executeQuery(queryStaff6);
					while (rsStaff6.next()) {
						returnValue.add(Long.valueOf(rsStaff6.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}			
			
			if(!criteria[4].isEmpty()) {
				String queryRate6 = "select * from VENDORS"
						+ "		where RATE='" + criteria[4] + "'";		
				ResultSet rsRate6 = null;	
				try {
					stmt = con.createStatement();
					rsRate6 = stmt.executeQuery(queryRate6);
					while (rsRate6.next()) {
						returnValue.add(Long.valueOf(rsRate6.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}			
			
			if(!criteria[5].isEmpty()) {
				String queryOwner = "select * from VENDORS"
						+ "		where OWNER='" + criteria[5] + "'";		
				ResultSet rsOwner = null;	
				try {
					stmt = con.createStatement();
					rsOwner = stmt.executeQuery(queryOwner);
					while (rsOwner.next()) {
						returnValue.add(Long.valueOf(rsOwner.getString("RECNO")));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);					
				}
			}			
			break;		
		default:
			break;
		}
		return Data.getLongArray(returnValue);
	}


	/**
	 * Creates a new Record in the database. Inserts the given data
	 * and returns the Record number.
	 * <br> 
	 * <br> The method forces to use Record numbers of deleted Records
	 * at first choice, but it is possible to choose between deleted
	 * Record numbers, if there are more than one. If there are no
	 * deleted Records, the new Record number will be always the amount
	 * of all Records plus one. 
	 * <br> If a Record number is requested, which does not exists, 
	 * always the lowest Record number of the deleted Records will be 
	 * used or the number of all Records plus one will be the Record 
	 * number.
	 * <br>
	 * <br> The elements of the <code>java.lang.String</code> array, 
	 * which is used as the method argument must have the following 
	 * order:<br>
	 * [0] - name, 32 byte (UTF-8)<br>
	 * [1] - city, 64 byte (UTF-8)<br>
	 * [2] - types, 64 byte (UTF-8)<br>
	 * [3] - staff, 6 byte (1 - 999999)<br>
	 * [4] - rate, 8 byte ($dddd.dd)<br>
	 * [5] - owner, 8 byte (only digits or nothing)<br>
	 * [6] - Record number, (1 - 99999999)
	 * <br>
	 * <br>
	 * If a Record number is requested, which is already in use of a 
	 * valid Record a <code>suncertify.db.DuplicateKeyException</code> 
	 * is thrown. Additional a <code>DuplicateKeyException</code> will be 
	 * thrown, if the Record is locked. In a case a Record is marked 
	 * deleted, but still not unlocked a new Record would be created,
	 * which is locked by another client.
	 * <br>
	 * <br>Remark: <code>java.lang.RuntimeException</code><br>
	 * The method wraps some exceptions, which might be thrown within 
	 * a <code>RuntimeException</code> to keep the signature of the 
	 * interface <code>DBAccess</code>. The <code>RuntimeException</code> 
	 * takes the actually thrown exception as a constructor argument. 
	 * <br> Based on calls on an object of type 
	 * <code>java.util.Map</code> and on an object of type an 
	 * <code>java.lang.RandomAccessFile</code> an
	 * <code>java.io.IOException</code> could be thrown. 
	 * <br> Additional the method may throw an 
	 * <code>java.lang.IllegalArgumentException</code>. Such an 
	 * exception indicates the format of the database is not kept and 
	 * therefore the creation of the new Record will be denied. 
	 * <br> Further more the method may throw an 
	 * <code>java.lang.ArrayIndexOutOfBoundsException</code>, if the 
	 * method argument's length is less than seven.
	 *  
	 * 
	 * @param data
	 *            A String[] which carries all fields of the new Record.
	 * @return long - The Record number for the created Record.
	 * @throws DuplicateKeyException
	 *             Thrown, if the Record number is already occupied
	 *             or the Record is locked.
	 */
	@Override
	public long createRecord(final String[] data) throws 
										DuplicateKeyException {
		DataFieldValidation.validateFields(data);		
		try {
			// Beware the String array 'data' is assigned by 
			// reference, therefore the method 'persistRecord'
			// is able to assign new values to the fields. 
			// It is possible a new Record number, which is 
			// stored in 'data[7]', will be assigned.
			this.persistRecordAdd(data, Long.valueOf(data[7]));			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Long.parseLong(data[7]);
	}


	/**
	 * Modifies the fields of a Record. 
	 * <br>
	 * <br> The elements of the <code>java.lang.String</code> array, which is 
	 * used as the method argument must have the following order:<br>
	 * [0] name<br>
	 * [1] city<br>
	 * [2] types<br>
	 * [3] staff<br>
	 * [4] rate<br>
	 * [5] owner<br>
	 * <br>
	 * <br>
	 * Remark: <code>java.lang.RuntimeException</code><br>
	 * The method wraps some exceptions, which might be thrown within 
	 * a <code>RuntimeException</code> to keep the signature of the interface 
	 * <code>DBAccess</code>. The <code>RuntimeException</code> takes the 
	 * actually thrown exception as a constructor argument.
	 * <br> An <code>IOException</code> could be thrown, because the method 
	 * calls the private method <code>persistRecord</code> and this private 
	 * method uses a <code>java.lang.RandomAccessFile</code> object to write 
	 * to the database, which might throw an <code>java.io.IOException</code>. 
	 * <br> If the format of the database is hurt an 
	 * <code>java.lang.IllegalArgumentException</code> will be thrown.
	 * <br>
	 * <br> The method may throw a <code>java.lang.SecurityException</code> 
	 * in cases there are locking problems
	 * like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * <br>
	 * <br>
	 * The method throws an 
	 * <code>java.lang.ArrayIndexOutOfBoundsException</code>, if the 
	 * method argument's length is less than six.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param data
	 *            The new values of the Record.
	 * @param lockCookie
	 *            Is created by calling <code>lockRecord</code>. 
	 *            Guarantees persistence.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 */
	@Override
	public void updateRecord(final long recNo, 
			final String[] data, final long lockCookie)
					throws SecurityException, RecordNotFoundException{
		DataFieldValidation.validateFields(data);		
		Data.checkRecordExists(recNo);
		LockManager.checkLockStatus(recNo, lockCookie);
		try{
	        this.persistRecord(data, Long.valueOf(recNo));
		}catch(final Exception ioe){			
			if(ioe instanceof IOException ){
				throw new RuntimeException(ioe);
			}else if(ioe instanceof DuplicateKeyException ){
				// Can not happen, but the method 'persistRecord'
				// possible throws such an exception, but only
				// while creating a new Record.
			}			
		}
	}


	/**
	 * Deletes a Record, making the Record number and associated disk 
	 * storage available for reuse. Marks the field <code>flag</code> of 
	 * the Record deleted with 0x8000 in two bytes at the beginning of 
	 * the Record's data.
	 * <br>
	 * This method writes directly to the database file.<br>
	 * <br>
	 * Remark: <code>java.lang.RuntimeException</code><br>
	 * The method wraps an <code>java.io.IOException</code> within a 
	 * <code>RuntimeException</code> to keep the signature of the 
	 * interface <code>DBAccess</code>. The <code>RuntimeException</code> 
	 * takes the exception as a constructor argument. 
	 * <br> 
	 * <br> Additional the method may throw a 
	 * <code>java.lang.SecurityException</code> in cases there are 
	 * locking problems like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another client
	 * <br> - the lock cookie does not match
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param lockCookie
	 *            Is created by calling the method <code>lockRecord</code>. 
	 *            Guarantees persistence.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record does not exist or is deleted.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 */
	@Override
	public void deleteRecord(final long recNo, final long lockCookie) 
							throws RecordNotFoundException, SecurityException {
		Data.checkRecordExists(recNo);
		LockManager.checkLockStatus(recNo, lockCookie);
		String query1 = "delete from VENDORS where RECNO='" + recNo + "'";		
		int rs1 = -1;		
		try {
			@SuppressWarnings("resource")
			PreparedStatement stmtP = con.prepareStatement(query1);
			rs1 = stmtP.executeUpdate();
		} catch (SQLException e) {
			throw new SecurityException("Data: " + e.getMessage() 
					+ " - rows(DML): " + rs1);
		}
		
		try {
			Data.recordNumbers.remove(Long.valueOf(recNo));
		}catch(Exception e) {
			throw new RecordNotFoundException(e.getMessage());
		}
		
	}


	/**
	 * Locks a Record so that it can only be updated or deleted by the
	 * client, who holds the lock. Returned value is a cookie that must 
	 * be used when the Record is unlocked, updated, or deleted. 
	 * <br> The method in first place checks whether the Record exists.
	 * Subsequently it uses an object of type 
	 * <code>suncertify.db.LockManager</code> to lock a Record.
	 * 
	 * 
	 * @param recNo
	 *            To identify the Record.
	 * @return long - The lock cookie.
	 * @throws RecordNotFoundException
	 *             Thrown when a Record can not been identified or is 
	 *             marked deleted.
	 */
	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException {
		Data.checkRecordExists(recNo);
		return this.lockingManager.lockRecord(recNo);
	}

	/**
	 * Releases the lock on a Record. 
	 * <br> The method uses an object of type <code>suncertify.db.LockManager</code>
	 * to unlock a Record.
	 * <br>
	 * <br> The method may throw a <code>java.lang.SecurityException</code> in 
	 * cases there are locking problems like:
	 * <br> - the Record is not locked
	 * <br> - the Record is locked by another one
	 * <br> - the lock cookie does not match
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @param cookie
	 *            Is created by calling <code>lockRecord</code>. Is needed to
	 *            acquire the lock for updates and deleting operations.
	 * @throws SecurityException
	 *             Thrown if there are locking problems.
	 */
	@Override
	public void unlock(final long recNo, final long cookie) 
										throws SecurityException {
		this.lockingManager.unlock(recNo, cookie);		
	}


	/**
	 * Locks the database so that no other client can modify it. This might be
	 * used if we were doing emergency maintenance, or if we were shutting down
	 * the database.
	 *
	 * @param databaseLocked true if the database should be locked.
	 */
	void setDatabaseClosed(final boolean databaseLocked) {
	    this.log.entering("Data", "setDatabaseLocked", 
	    		Boolean.valueOf(databaseLocked));
	    // Implementation details: any modification (create/update/delete) to
	    // the database locks <code>recordNumbersLock</code> first, since the
	    // modification might change the recordNumbers collection. Therefore
	    // all we need do is lock this mutual exclusion lock, and no other
	    // thread can modify it.
	    if (databaseLocked) {
	    	Data.recordNumbersLock.writeLock().lock();
	    } else {
	    	Data.recordNumbersLock.writeLock().unlock();
	    }
	    this.log.exiting("Data", "setDatabaseLocked");
	}
	
		
	/**
     * Gets the store's inventory.
     * All of the Records in the system.
     * 
     * @return A collection of all found Records.
     * @throws IOException Indicates there is a problem accessing the data.
     */
	List<String[]> getRecords() throws IOException{
		return this.getRecordList();
	}
		
		
	/**
	 * The method is a special feature and belongs to the enhanced part.
	 * It provides the possibility to change the database during a 
	 * session. The functionality is provided in two
	 * different ways, which will be chosen by a dialog. On the one side
	 * it is using the method <code>loadRecordNumbers</code>, which jumps 
	 * to the 70. byte position of the database to start reading. On the 
	 * other side the method uses an object of class <code>DatabaseReader</code>, 
	 * which reads also the magic cookie and the scheme.
	 * 
	 * @param dbPathDB
	 *            The path to the database file.
	 * @throws IOException
	 *             Thrown if transmission problems occur.
	 */
	 void loadDB(final String dbPathDB) throws IOException {
		 final Object[] options = { "Use DatabaseReader", // 0
					"CANCEL", // 1
					"Start reading at 70th byte"// 2
			};
		 final int n = JOptionPane.showOptionDialog(null,
				 "You will "
					 		+ "reload another or the same database file! "
					 		+ "Press Yes, if You want to run "
					 		+ "\nthe DatabaseReader, which reads the "
					 		+ "database up from the start included "
					 		+ "\nthe magic cookie and the scheme. Otherwise "
					 		+ "the database will be read up "
					 		+ "\nfrom the 70th byte, where the first data "
					 		+ "set starts."
							+ "\n"
							+ "\nIf You get the following error message:"
							+ "\n-  ATTENTION java.io.EOFEception  -"
							+ "\nit indicates You are using an unvalid database."
							+ "\n" ,
					"B&S Reload Database", JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
		 if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			 return;
		 }else if (n == JOptionPane.OK_OPTION) {
			 // Class DatabaseReader checks the magic cookie
		     // and reads the scheme.
			 DatabaseReader reader = 
		    			new DatabaseReader(dbPathDB);
			 reader.loadData();
			 Data.database = reader.getFile();
			 Data.dbSize = Data.database.length();
			 Data.recordNumbers = reader.getRecordNumbersMap();
		 }else if (n == JOptionPane.OK_CANCEL_OPTION) {
			 // Reads up from the 70th byte.
			 try {
				 Data.database = new RandomAccessFile(dbPathDB, "rw");
				 Data.dbSize = Data.database.length();
				 Data.recordNumbers = new HashMap<Long, RecordCookie>();
				 this.loadRecordNumbers();
			 }catch(Exception e) {					 
					ExceptionDialog.handleException("Data-loadDB: "
							+ "CONNECTING TO THE DATABASE FAILED!"
							+ "\n"
							+ "\nUnvalid database:"
							+ "\n" + dbPathDB
							+ "\n"
							+ "\nException: " + e.getMessage()
							+ "\n"
							+ "\nTHE PROGRAMM CONNCECTS TO THE LAST ACCESSED"
							+ "\nDATABASE:"
							+ "\n" + Data.recentPathToDB
							+ "\n"
							+ "\nBEWARE NO UPDATES TO STORED PATHS ARE DONE! TO STORE"
							+ "\nTHE PATH, PLEASE 'Reload DB' AGAIN but THIS TIME"
							+ "\n 'Use DatabaseReader'"
							+ "\n");
					Data.database = new RandomAccessFile(Data.recentPathToDB, "rw");
					Data.dbSize = Data.database.length();
					Data.recordNumbers = new HashMap<Long, RecordCookie>();
					this.loadRecordNumbers();
					
			 }
		 }
	}


	/**
	 * This method retrieves a data set from the database.
	 * <br>
	 * <br>
	 * The returned <code>String</code> array has the following order:
	 * <br> - flag
	 * <br> - name
	 * <br> - city
	 * <br> - types
	 * <br> - staff
	 * <br> - rate
	 * <br> - owner
	 * <br> - Record number
	 *
	 * @param recNo The Record number.
	 * @return The requested Record.
	 * @throws IOException Indicates there is a problem accessing the data.
	 */
	@SuppressWarnings({ "static-method", "resource" })
	private String[] retrieveRecord(long recNo) throws IOException {
		String query1 = "select * from VENDORS"
				+ "		where RECNO='" + recNo + "'";		
		ResultSet rs1 = null;
		String xs = "";
		String xs2 = "";
		String xs3 = "";
		String xs4 = "";
		String xs5 = "";
		String xs6 = "";
		String xs7 = "";		
		try {
			stmt = con.createStatement();
			rs1 = stmt.executeQuery(query1);
			while (rs1.next()) {
				xs = rs1.getString("RECNO");
				xs2 = rs1.getString("NAME");
				xs3 = rs1.getString("CITY");
				xs4 = rs1.getString("TYPES");
				xs5 = rs1.getString("STAFF");
				xs6 = rs1.getString("RATE");
				xs7 = rs1.getString("OWNER");
			}
		} catch (SQLException e) {
			throw new IOException(e);
		}		
		return new String[] {"", xs2, xs3, xs4, xs5, xs6, xs7, xs};		
	}
	
	
    /**
     * The method modifies a data set or creates a new data set
     * determined by the method argument.
     *
     * @param record The Record to store
     * @param recNo The Record number.
     * @throws IOException Indicates there is a problem accessing the data file
     */
    @SuppressWarnings({ "static-method", "resource" })
	private void persistRecord(final String[] record, Long recNo) 
			throws IOException {
    	
    	if(!Data.recordNumbers.containsKey(recNo)){
			throw new IOException("Data: This Record number(" 
					+ recNo + ") does not exist");
		}
    	if(!LockManager.getLockedSet().contains(recNo)){
			throw new IOException("The Record"
					+ "(" + recNo + ") is not locked.");
		}		
       		
    	String query1 = "update VENDORS set NAME='" + record[1] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rs1 = -1;
		try {
			PreparedStatement stmtP = con.prepareStatement(query1);
			rs1 = stmtP.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rs1 +
					" - " + e.getMessage());
		}      
		
		String city = "update VENDORS set CITY='" + record[2] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rscity = -1;
		try {
			PreparedStatement stmtcity = con.prepareStatement(city);
			rscity = stmtcity.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rscity +
					" - " + e.getMessage());
		} 
		
		String querytypes = "update VENDORS set TYPES='" + record[3] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rstypes = -1;
		try {
			PreparedStatement stmttypes = con.prepareStatement(querytypes);
			rstypes = stmttypes.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rstypes +
					" - " + e.getMessage());
		} 
		
		String querystaff = "update VENDORS set STAFF='" + record[4] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rsstaff = -1;
		try {
			PreparedStatement stmtstaff = con.prepareStatement(querystaff);
			rsstaff = stmtstaff.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rsstaff +
					" - " + e.getMessage());
		} 
		
		String queryrate = "update VENDORS set RATE='" + record[5] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rsrate = -1;
		try {
			PreparedStatement stmtrate = con.prepareStatement(queryrate);
			rsrate = stmtrate.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rsrate +
					" - " + e.getMessage());
		} 
		
		String queryowner = "update VENDORS set OWNER='" + record[6] + "' "
    			+ "where RECNO='" + recNo + "'";
		int rsowner = -1;
		try {
			PreparedStatement stmtowner = con.prepareStatement(queryowner);
			rsowner = stmtowner.executeUpdate();			
		} catch (SQLException e) {
			throw new IOException("Data, persistUpdate, rows: " + rsowner +
					" - " + e.getMessage());
		} 
    	
        
    }
    
    
    /**
     * The method modifies a data set or creates a new data set
     * determined by the method argument.
     *
     * @param record The Record to store
     * @param recNo The Record number.
     * @throws IOException Indicates there is a problem accessing the data file
     * @throws DuplicateKeyException Is thrown if the Record number is occupied
     * of another Record or the Record is locked. 
     */
    @SuppressWarnings({ "static-method", "resource" })
	private void persistRecordAdd(final String[] record, Long recNo) 
    									throws IOException, DuplicateKeyException {
    	
    	if(Data.recordNumbers.containsKey(recNo)){
			throw new DuplicateKeyException("Data: This Record number(" 
					+ recNo + ") is occupied");
		}
    	if(LockManager.getLockedSet().contains(recNo)){
			throw new DuplicateKeyException("The Record"
					+ "(" + recNo + ") is marked deleted but it is still "
							+ "locked.");
		}
    	
    	Long recordNo = Long.valueOf(-1);
    	long count = 1;   	
    	Set<Long> recNos = Data.recordNumbers.keySet();
    	List<Long> liRecNos = new ArrayList<Long>(recNos);
    	Collections.sort(liRecNos);
    	
    	for (Long recNoLi : liRecNos) {
			if( recNoLi != Long.valueOf(count)) {
				recordNo = Long.valueOf(count);
				break;
			}
			++count;
			recordNo = Long.valueOf(count);
		}
       		
    	record[7] = recordNo.toString();
    	Data.recordNumbers.put(recordNo, new RecordCookie(
    			1, ""));
		
		String query1 = "insert into VENDORS"
				+ "	values("
				+ recordNo + ","
				+ "'" + record[1] + "',"
        		+ "'" + record[2] + "',"
                + "'" + record[3] + "',"
        		+ record[4] + ","
                + "'" + record[5] + "',"
                + "'" + record[6] + "'"
				+ ")";
		
		int rs1 = -1;
		try {
			PreparedStatement stmtP = con.prepareStatement(query1);
			rs1 = stmtP.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new IOException("Data, persistAdd, rows: " + rs1 +
					" - " + e.getMessage());
		}       
    }
		
		
    /**
     * Gets the store's inventory - all of the Records in the system.
     * <br>
     * It does not fill the class collection <code>recordNumbers</code>,
     * but it calls the method <code>retrieveRecord</code> to obtain
     * all valid Records.
     *
     * @return A collection of all found Records.
     * @throws IOException Indicates there is a problem accessing the data.
     */
    @SuppressWarnings("resource")
	private List<String[]> getRecordList() 
			throws IOException {		
	    this.log.entering("Data", "getRecordList");
	    final List<String[]> listRecordAsArray = new ArrayList<String[]>();
	    Map<Long, String[]> map = new HashMap<Long, String[]>();
	    String query1 = "select * from VENDORS";		
		ResultSet rs1 = null;
		String xs = "";
		String xs2 = "";
		String xs3 = "";
		String xs4 = "";
		String xs5 = "";
		String xs6 = "";
		String xs7 = "";
		
		try {
			stmt = con.createStatement();
			rs1 = stmt.executeQuery(query1);
			while (rs1.next()) {
				xs = rs1.getString("RECNO");
				xs2 = rs1.getString("NAME");
				xs3 = rs1.getString("CITY");
				xs4 = rs1.getString("TYPES");
				xs5 = rs1.getString("STAFF");
				xs6 = rs1.getString("RATE");
				xs7 = rs1.getString("OWNER");				
				Data.recordNumbers.put(Long.valueOf(xs), null);				
				map.put(Long.valueOf(xs), new String[] {
						"", xs2, xs3, xs4, xs5, xs6, xs7, xs});
			}			
			Map<Long, String[]> treeMap = new TreeMap<Long, String[]>(map);			
			for(final Map.Entry<Long, String[]> ma : 
				treeMap.entrySet()){
				listRecordAsArray.add(ma.getValue());
			}
			
		} catch (SQLException e) {
			throw new IOException("Data, getRecordList, " 
					+ e.getMessage());
		}	   
	    return listRecordAsArray;
	}
    
    
    /**
     * Loads the class collection <code>recordNumbers</code>
     * by reading the database.
     * 
     * @throws IOException Thrown, if there problems to read the
     * database.
     */
    private void loadRecordNumbers() throws IOException{
    	Data.recordNumbersLock.writeLock().lock();
	    try {
	    	long recNo = 1;
	    	for (long locationInFile = Data.DB_SCHEME_SIZE;
	                locationInFile < Data.getDbSize();
	                locationInFile += Record.RECORD_LENGTH) {	        	
	            final String[] record = retrieveRecord(recNo);
	            if (record == null) {
	            	Data.recordNumbers.put(Long.valueOf(recNo), new RecordCookie(
	            				locationInFile, "d"));
	            } else {
	            	Data.recordNumbers.put(Long.valueOf(recNo), new RecordCookie(
                				locationInFile, ""));	                
	            }         
	            recNo++;
	        }
	    } finally {
	    	Data.recordNumbersLock.writeLock().unlock();
	    }	
    }
	

	/**
	 * Throws an <code>RecordNotFoundException</code>,
	 * if Record is deleted or does not exists.
	 * 
	 * @param recNo The Record number to identify the Record.
	 * 
	 * @throws RecordNotFoundException
	 *             Thrown when a Record does not exist or is deleted. 
	 */
	static void checkRecordExists(final long recNo) 
			throws RecordNotFoundException{
		Data.recordNumbersLock.readLock().lock();
		try{
			if(!Data.recordNumbers.containsKey(Long.valueOf(recNo))){
				throw new RecordNotFoundException("Data, checkRecordExists, "
						+ "Record(" + recNo + ") does not exists");
			}
		}finally{
			Data.recordNumbersLock.readLock().unlock();
		}
	}
	

	/**
	 * Returns a set, which contains all Record numbers
	 * of locked Records. The set is created and maintained of the
	 * class <code>suncertify.db.LockManager</code>.
	 * 
	 * @return Set - A set with all Record numbers of locked Records.
	 */
	static Set<Long> getLockedSet() {
		return LockManager.getLockedSet();
	}
	
	
	/**
	 * Returns the number of counted bytes of the database file.
	 * 
	 * @return Returns the number of counted bytes of the 
	 * database file.
	 */
	static long getDbSize(){
		return Data.dbSize; 
	}


	/**
	 * Returns a <code>java.lang.String</code>, which consists of white spaces and 
	 * the length is determined by the method argument. 
	 * 
	 * @param whiteSpaces
	 *            How many white spaces.
	 * @return String - A <code>java.lang.String</code> with the number of 
	 * determined
	 *         white spaces.
	 */
	static String getWhiteSpaces(final int whiteSpaces) {
		final byte[] bArr = new byte[whiteSpaces];
		for (int i = 0; i < bArr.length; i++) {
			bArr[i] = (byte)32;// (32)byte value of a white space
		}
		return new String(bArr);
	}
	
	
	/**
	 * Takes a set of reference type <code>java.lang.Long</code> and 
	 * returns it in an array of primitive type long.
	 * 
	 * @param set A set, which elements are of types 
	 * <code>java.lang.Long</code>.
	 * 
	 * @return Returns an array of primitive type long.
	 */
	private static long[] getLongArray(final Set<Long> set){	
		final long []recNosArr = new long[set.size()];
		int i = 0;
		for(final Long l : set){
			recNosArr[i++] = l.longValue();
		}
		Arrays.sort(recNosArr);		
		return recNosArr;
	}
}
