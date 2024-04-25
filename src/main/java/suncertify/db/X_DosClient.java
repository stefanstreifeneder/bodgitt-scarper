package suncertify.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import suncertify.gui.SavedConfiguration;

/**
 *  The main method in the X_DosClient class allows for a number of functions:
 *  <ul>
 *      <li> Displays all Records in the database.</li>
 *      <li> Adds a Record to the database. </li>
 *      <li> Modifies an existing Record in the database. </li>
 *      <li> Removes a Record from the database. </li>
 *      <li> Rents a Record. </li>
 *      <li> Returns a rented Record. </li>
 *      <li> Generates some dummy Records. </li>
 * </ul>
 *
 * This simple X_DosClient is for testing.
 * Currently the system output is displayed to a command window.
 * System input is also entered in a command window.
 * <br>
 * <br> The path to the database is obtained by the class
 * <code>suncertify.gui.SavedConfiguration</code>, which reads out the
 * connection data of the file suncertify.properties. 
 * 
 */
public class X_DosClient {
    /**
     * Main menu prompt for screen display.
     */
    private static final String DIRECTIONS
            = "Make Your choice:"
            		+ "\n1 for printing existing records"
            		+ "\n2 for adding records"
            		+ "\n3 for modify"
            		+ "\n4 for remove"
            		+ "\n5 rent"
            		+ "\n6 to return a rental"
            		+ "\n7 to generate some dummy Records"
            		+ "\n8 to quit"
            		
            		+ "\nStart entry:";

    /**
     * Prompt displayed on screen when user is about to create a new Record.
     */
    private static final String ENTER_RECORD_MESSAGE
            = "Enter an Record number, name, city, types, staff(d), rate($ddd.dd), owner id"
            		+ "\nEntries (press return):";
    
    /**
     * Test class- no object instantiation necessary. 
     */
    private X_DosClient(){
    	
    }

    /**
     * Entry point for the non-graphical user interface. Prompts the user for 
     * actions to be performed. It should call
     * appropriate methods for each user action, however all actions are
     * performed within this one monolithic method.
     *
     * @param args command line arguments, which are ignored.
     * @throws IOException if there is a problem accessing the data file.
     */
    public static void main(final String args[]) throws IOException {
    	final String path = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.DATABASE_LOCATION);
    	final String password = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.PASSWORD);
        final InterfaceClient_Admin db = new RecordDatabase(path, password);
        boolean quitApp = false;
        long recNo = 0;
        String name = null;
        String city = null;
        String types = null;
        int staff = 0;
        String rate = null;
        String idOwner = null;
        Record record = null;
        final BufferedReader in
                = new BufferedReader(new InputStreamReader(System.in));

        while (quitApp == false) {
            System.out.println(DIRECTIONS);
            final String input = in.readLine();
            
            try{
            
            switch(Integer.parseInt(input)) {
                case(1):
                	long dbSize = db.getAllocatedMemory();
	                List<Record> l = db.getAllValidRecords();
                	System.out.println("DB-Size: " + dbSize
                			+ "\nRecords: " + l.size());
                	for(Record r : l){
                		System.out.println("Record: " + r);
                	}
                    break;
                case(2):
                    System.out.println(ENTER_RECORD_MESSAGE);

                    recNo = Long.parseLong(in.readLine());
                    name = in.readLine();
                    city = in.readLine();
                    types = in.readLine();
                    staff = Integer.parseInt(in.readLine());
                    rate = in.readLine();
                    idOwner = in.readLine();

                    record = new Record();
                    record.setRecNo(recNo);
                    record.setName(name);
                    record.setCity(city);
                    record.setTypesOfWork(types);
                    record.setNumberOfStaff(staff);
                    record.setHourlyChargeRate(rate);
                    record.setOwner(idOwner);

                    db.addRecord(record);
                    break;
                case(3):
                    System.out.println(ENTER_RECORD_MESSAGE);

                    recNo = Long.parseLong(in.readLine());
                    name = in.readLine();
                    city = in.readLine();
                    types = in.readLine();
                    staff = Integer.parseInt(in.readLine());
                    rate = in.readLine();
                    idOwner = in.readLine();

                    record = new Record();
                    record.setRecNo(recNo);
                    record.setName(name);
                    record.setCity(city);
                    record.setTypesOfWork(types);
                    record.setNumberOfStaff(staff);
                    record.setHourlyChargeRate(rate);
                    record.setOwner(idOwner);

                    final long cookie = db.setRecordLocked(recNo);                   
                    
                    if (db.modifyRecord(record, recNo, cookie)) {
                        System.out.println("recod modified: " + record);
                    } else {
                        System.out.println("record not modified."
                                + "May not be unique: " + record);
                    }
                    db.setRecordUnlocked(recNo, cookie);
                    break;
                case(4):
                        System.out.print("to remove item with Record number: " );
                        recNo = Long.parseLong(in.readLine());
                        
                        final long cookieDel = db.setRecordLocked(recNo);
                        
                        if (db.removeRecord(recNo, cookieDel)) {
                            System.out.println("item removed!");
                        } else {
                            System.out.println("item not removed."
                                    +   "Check that Record number is correct.");
                        }
                        db.setRecordUnlocked(recNo, cookieDel);
                        break;
                case(5):
                    System.out.print("rent Record with Record number: " );
                    recNo = Long.parseLong(in.readLine());
                    
                    final long cookieRes = db.setRecordLocked(recNo);
                    try {
                        if (db.reserveRecord(recNo, 6666, cookieRes)) {
                            record = db.getRecord(recNo);
                            System.out.println("item rented" + record.toString());
                            
					} else {
						System.out.println("item not rented." + "Check that Record number"
								+ " is correct"
								+ "and that there are available copies.");
					}
                        
                    } finally {
                        db.setRecordUnlocked(recNo, cookieRes);
                    }
                    break;
                case(6):
                    System.out.print("return Record with Record number: " );
                    recNo = Long.parseLong(in.readLine());
                    
                    
                    final long cookieRel = db.setRecordLocked(recNo);
                    
                    try {
                        if (db.releaseRecord(recNo, cookieRel)) {
                            record = db.getRecord(recNo);
                            System.out.println("item returned: " + record.toString());
                        }
                    }finally {
                    	db.setRecordUnlocked(recNo, cookieRel);
                    }
                    break;
                
                case(7):
                	
                	
					record = new Record();
					record.setRecNo(1);
					record.setName("1-name");
					record.setCity("1-city");
					record.setTypesOfWork("1-types");
					record.setNumberOfStaff(1);
					record.setHourlyChargeRate("$111.11");
					record.setOwner("1");
					db.addRecord(record);

					record = new Record();
					record.setRecNo(2);
					record.setName("2-name");
					record.setCity("2-city");
					record.setTypesOfWork("2-types");
					record.setNumberOfStaff(2);
					record.setHourlyChargeRate("$222.11");
					record.setOwner("2");
                    db.addRecord(record);

                    record = new Record();
                    record.setRecNo(3);
					record.setName("3-name");
					record.setCity("3-city");
					record.setTypesOfWork("3-types");
					record.setNumberOfStaff(3);
					record.setHourlyChargeRate("$333.11");
					record.setOwner("3");
                    db.addRecord(record);

                    record = new Record();
                    record.setRecNo(4);
					record.setName("4-name");
					record.setCity("4-city");
					record.setTypesOfWork("4-types");
					record.setNumberOfStaff(1);
					record.setHourlyChargeRate("$444.11");
					record.setOwner("4");
                    db.addRecord(record);

                    record = new Record();
                    record.setRecNo(5);
					record.setName("5-name");
					record.setCity("5-city");
					record.setTypesOfWork("5-types");
					record.setNumberOfStaff(5);
					record.setHourlyChargeRate("$5.11");
					record.setOwner("5");
                    db.addRecord(record);

                    record = new Record();
                    record.setRecNo(6);
					record.setName("6-name");
					record.setCity("6-city");
					record.setTypesOfWork("6-types");
					record.setNumberOfStaff(6);
					record.setHourlyChargeRate("$666.11");
					record.setOwner("6");
                    db.addRecord(record);

                    record = new Record();
                    record.setRecNo(7);
					record.setName("7-name");
					record.setCity("7-city");
					record.setTypesOfWork("7-types");
					record.setNumberOfStaff(7);
					record.setHourlyChargeRate("$777.11");
					record.setOwner("7");
                    db.addRecord(record);
                    
                break;
			case(8):
                    System.out.println("Thanks for working with B&S.");
                    quitApp = true;
                    break;
			default:
				break;
            }
            
            }catch(final Exception e){
            	System.out.println("X_DosClient: " + e.toString());
            }
        }
    }
}
