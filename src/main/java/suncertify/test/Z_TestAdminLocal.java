package suncertify.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.direct.admin.DirectConnector_Admin;
import suncertify.gui.SavedConfiguration;


/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roberto Perillo(
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).<br>
 * I revised the original class according to my public interfaces
 * <code>suncertify.db.InterfaceClient_ReadOnly</code>,
 * <code>suncertify.db.InterfaceClient_LockPermission</code>,
 * <code>suncertify.db.InterfaceClient_Seller</code> and
 * <code>suncertify.db.InterfaceClient_Admin</code>.
 * <br>
 * <br>
 * The class accesses the database by using an Admin client.
 * <br>
 * <br>
 * The class addresses the methods of the public interface
 * <code>suncertify.db.InterfaceClient_Seller</code>.
 * <br>
 * <br>
 * The design of the original program stays (almost) the same:<br>
 * - one connection object used by all threads initialized in the 
 * static initializer.<br>
 * - one loop, which creates multiple threads<br>
 * - each thread represents a functionality according to to my public
 * interfaces<br>
 * - unlock is done in a finally block
 * <br>
 * <br>
 * Attention: Please set the Logger Level to OFF, otherwise
 * memory problems can arise.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class Z_TestAdminLocal {
	
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.test.TestRecordDatabaseLocalAdmin</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.test.Z_TestAdminLocal"), 
			Level.ALL);	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>UpdateingRandomRecordThread</code>.
	 */
	static int countUp1Start = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>Updating_1_RecordThread</code> has reached the end.
	 */
	static int countUp1End = 0;
	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>UpdatingRecord1Thread</code>.
	 */
	static int countUp2Start = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>UpdatingRecord1Thread</code> has reached the end.
	 */
	static int countUp2End = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>CreatingRecordThread</code>.
	 */
	static int countCreateEnd = 0;
	
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>CreatingRecordThread</code> has reached the end.
	 */
	static int countCreateStart = 0;
	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>DeletingRecord1Thread</code>.
	 */
	static int countDelEnd = 0;
	
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>DeletingRecord1Thread</code> has reached the end.
	 */
	static int countDelStart = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>RentRandomRecordThread</code>.
	 */
	static int countRent1Start = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>RentRandomRecordThread</code> has reached the end.
	 */
	static int countRent1End = 0;
	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>RentRecord1Thread</code>.
	 */
	static int countRent2Start = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>RentRecord1Thread</code> has reached the end.
	 */
	static int countRent2End = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>ReleaseRecordThread</code>.
	 */
	static int countRelease1End = 0;
	
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>ReleaseRecordThread</code> has reached the end.
	 */
	static int countRelease1Start = 0;
	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>ReleaseRecord1Thread</code>.
	 */
	static int countRelease2End = 0;
	
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>ReleaseRecord1Thread</code> has reached the end.
	 */
	static int countRelease2Start = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>FindingRecordsThread</code>.
	 */
	static int countGetRecStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>FindingRecordsThread</code> has reached the end.
	 */
	static int countGetRecEnd = 0;

	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>FindingByRecordsThread</code>.
	 */
	static int countFindByStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>FindingByRecordsThread</code> has reached the end.
	 */
	static int countFindByEnd = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>FindingByReturnListThread</code>.
	 */
	static int countFindByRetListStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>FindingByReturnListThread</code> has reached the end.
	 */
	static int countFindByRetListEnd = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>GetValidRecordsThread</code>.
	 */
	static int countGetAllRecsStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetValidRecordsThread</code> has reached the end.
	 */
	static int countGetAllRecsEnd = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>GetLockedThread</code>.
	 */
	static int countGetLoStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetLockedThread</code> has reached the end.
	 */
	static int countGetLoEnd = 0;
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>GetRentedThread</code>.
	 */
	static int countGetRentStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetRentedThread</code> has reached the end.
	 */
	static int countGetRentEnd = 0;
	
	/**
	 * Counts the iterations in the method <code>startTest</code>.
	 */
	static int countRuns = 0;
	
	/**
	 * Stores the criteria.
	 */
	static String[] criteriaExists = {""};
	
	/**
	 * Client enabled with all functionalities.
	 */
	static InterfaceClient_Admin client = null;
	
	/**
	 * Stores the number of iterations until to the next break.
	 */
	static long countIterToNextDisplay = 0;

	/**
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;
	
	/**
	 * The path to the database file in the currently working directory.
	 */
	private static String pathDatabaseCurDir = "";
	
	/**
	 * Static initializer to settle the access to the database file.
	 */
	static {
    	boolean idIsEntered = true;
    	while(idIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_TestAdminLocal starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"50000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestAdminLocal - please enter the number of iterations!", 
    					 		"Z_TestAdminLocal (ADMIN)",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			iterations = Long.parseLong(inputIterations);    			
    			idIsEntered = false;
    		}
    	}     	
    	boolean loopsAreEntered = true;
    	while(loopsAreEntered){
    		final String displayIterations = JOptionPane.showInputDialog(null,
        			"Z_TestAdminLocal starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"10000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestAdminLocal - please enter the number of iterations!", 
    					 		"Z_TestAdminLocal (ADMIN)",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			countIterToNextDisplay = Long.parseLong(displayIterations);
    			loopsAreEntered = false;
    		}
    	}
    	
    	
    	pathDatabaseCurDir = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.DATABASE_LOCATION);
    	
    	
    	// enter path
		final String input = JOptionPane.showInputDialog(
		null, "Z_TestAdminLocal" 
		+ "\n\nYou will be connected to: \n\n"
		+ pathDatabaseCurDir
		+ "\n\nPlease enter the path to the database:",
		pathDatabaseCurDir);
		if(input != null){
			pathDatabaseCurDir = input;
		}else{
			System.exit(0);
		}
		// end enter path
    	
    	
    	final String inputAll = JOptionPane.showInputDialog(null,
    			"Z_TestAdminLocal\n"
				+ "\nSearch for any field of a Record!"
				+ "\nSplit by using ';'." 
				+ "\n"
				+ "\nExample: search for all companies with 4 staff members" 
				+ "\nInput:       ';;;4(;)(;)'"
				+ "\n"
				+ "\nIf You search for certain rates, You have to omitt '$'."
				+ "\nExample: search for all companies with rates of $ 90.00" 
				+ "\nInput:       ';;;;90(;)'"
				+ "\n"
				+ "\nIf You want to search for all Records -" 
				+ "\nInput:       ''       (none- leave the field blank)"
				+ "\n"
				+ "\n"
				+ "\nInputs done by the program:"
				+ "\nUpdate: H_A_UP_2_ + count; Myville; 23; Roofing; 85; "
				+ "\nCreate: F_A_CR_ + count; Myville; 14; Heating; 85; "
				+ "\nRent-1-ID: 9999"  
				+ "\nRent-2-ID: 1234"
				+ "\n"
				+ "\n");    	
    	
		if (inputAll == null) {
			System.exit(0);			
		}else if (inputAll.equals("")) {
			criteriaExists = new String[]{null};		
		} else{ 
			criteriaExists = inputAll.split(";");
		}
		
		
		System.out.println("Z_TestAdminLocal has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + countIterToNextDisplay
    			+ "\nYour input: " + inputAll);
		
		final int n = JOptionPane.showConfirmDialog(null, 
				"Z_TestAdminLocal (ADMIN) "
				 		+ "starts."
				 		+ "\nRole: ADMIN" 
				 		+ "\nPath: " + pathDatabaseCurDir
				 		+ "\nIterations: " + iterations
				 		+ "\nLoops until next display: " + countIterToNextDisplay, 
				 		"Z_TestAdminLocal (ADMIN)",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		
		try {
    		client = DirectConnector_Admin.getLocal(pathDatabaseCurDir, "");
		} catch (final FileNotFoundException e) {
			System.out.println("Z_TestAdminLocal, static initializr, Exce: "
					+ e.getMessage());
		} catch (final IOException e) {
			System.out.println("Z_TestAdminLocal, static initializr, Exce: "
					+ e.getMessage());
		}
    }
	
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_TestAdminLocal(){
	}
 
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
    	try{
    		new Z_TestAdminLocal().startTests();
    	}catch(final OutOfMemoryError e){ final Date d = new Date(System.currentTimeMillis());
        	final SimpleDateFormat sdf = 
        			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        	final String formattedDate = sdf.format(d);
        	System.out.println("Z_TestAdminLocal, OOMError: " + formattedDate);
    		e.printStackTrace();
    	}
    }
 
    /**
     * Starts the threads in a loop.
     */
    private void startTests() {
        try {
        	for (int i = 0; i < iterations; i++) {
        		
            	countRuns++;          	
            	
            	final Thread updatingRecord1 = new UpdatingRecord1Thread();
            	updatingRecord1.start();          	
            	
                final Thread creatingRecord = new CreatingRecordThread();
                creatingRecord.start();               
                
                final Thread deletingRecord = new DeletingRecord1Thread();
                deletingRecord.start();           	
            	
				final Thread findingRecords = new FindingRecordsThread();
				findingRecords.start();			
                
                final Thread findingByRecords = new FindingByRecordsThread();
                findingByRecords.start();
            	
            	final Thread getLoRecords = new GetLockedThread();
            	getLoRecords.start();          	
            	
            	final Thread rentRecord1 = new RentRecord1Thread();
            	rentRecord1.start();              
                
                final Thread release1Record = new ReleaseRecord1Thread();
                release1Record.start();              
                
                final Thread getAllRecs = new GetAllRecordsThread();
                getAllRecs.start();
            }
            //ensures messages will be visible on the command line.
            Thread.sleep(1000);            
        } catch (final Exception e) {
            System.out.println("start " + e.getMessage());
        }   
        
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
		
		System.out.println("Z_TestAdminLocal has run to the end." 
					+ "\nRole: ADMIN" + "Done, Loops: " + countRuns 
					+ "\nBreaks: " + countIterToNextDisplay
					+ "\n- up 1 start: " + countUp1Start + " - up 1 end: " + countUp1End
					+ "\n- up 2 start: " + countUp2Start + " - up 2 end: " + countUp2End 
					+ "\n- create start: " + countCreateStart + " - create end: " + countCreateEnd 
					+ "\n- delete start: " + countDelStart + " - delete end: " + countDelEnd 
					+ "\n- find start: " + countGetRecStart + " - find end: " + countGetRecEnd 
					+ "\n- findBy start: " + countFindByStart + " - findBy end: " + countFindByEnd
					+ "\n- get all Records start: " + countGetAllRecsStart 
					+ " - getDeleted end: " + countGetAllRecsEnd
					+ "\n- getLocked start: " + countGetLoStart + " - getLocked end: " + countGetLoEnd
					+ "\n- getRented start: " + countGetRentStart + " - getRented end: " + countGetRentEnd
					+ "\n- rent1 start: " + countRent1Start + " - rent1 end: " + countRent1End 
					+ "\n- rent2 start: " + countRent2Start + " - rent2 end: " + countRent2End 
					+ "\n- release1 start: " + countRelease1Start + " - release1 end: " + countRelease1End 
					+ "\n- release2 start: " + countRelease2Start 
					+ " - release2 end: " + countRelease2End 
					+ "\n- " + formattedDate);

		final JOptionPane alert = new JOptionPane("Z_TestAdminLocal has run to the end." 
					+ "\nRole: ADMIN" + " Done, Loops: " + countRuns 
					+ "\nBreaks: " + countIterToNextDisplay
					+ "\n- up 1 start: " + countUp1Start + " - up 1 end: " + countUp1End
					+ "\n- up 2 start: " + countUp2Start + " - up 2 end: " + countUp2End 
					+ "\n- create start: " + countCreateStart + " - create end: " + countCreateEnd 
					+ "\n- delete start: " + countDelStart + " - delete end: " + countDelEnd 
					+ "\n- find start: " + countGetRecStart + " - find end: " + countGetRecEnd 
					+ "\n- findBy start: " + countFindByStart + " - findBy end: " + countFindByEnd
					+ "\n- get all Records start: " + countGetAllRecsStart + " - getDeleted end: " 
					+ countGetAllRecsEnd
					+ "\n- getLocked start: " + countGetLoStart + " - getLocked end: " + countGetLoEnd
					+ "\n- getRented start: " + countGetRentStart + " - getRented end: " + countGetRentEnd
					+ "\n- rent1 start: " + countRent1Start + " - rent1 end: " + countRent1End 
					+ "\n- rent2 start: " + countRent2Start + " - rent2 end: " + countRent2End 
					+ "\n- release1 start: " + countRelease1Start + " - release1 end: " + countRelease1End 
					+ "\n- release2 start: " + countRelease2Start + " - release2 end: " 
					+ countRelease2End
					+ "\n- " + formattedDate

			, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);

		final JDialog dialog = alert.createDialog(null, "B & S - " 
					+ "Z_TestAdminLocal");
		dialog.setVisible(true);
		System.exit(0);// Must if graphics in use
    }
 
    /**
     * In its <code>run</code> method it updates a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class UpdatingRecord1Thread extends Thread { 
    	
        /**
         * No-arg constructor to create an object of this class.
         */
        public UpdatingRecord1Thread() {
			//
		}
        
        /**
         * Overridden <code>run</code> method. Updates a Record.
         */
		@Override
		public void run() {
			countUp2Start++;			
        	final Record room = new Record();
            room.setName("H_A_UP_2_" + countRuns);
            room.setCity("Myville");
            room.setTypesOfWork("Heating");
            room.setNumberOfStaff(99);
            room.setHourlyChargeRate("$150.00");
            room.setOwner("");            
            final int recNo = (int)(Math.random()* (28 - 1 + 1)) + 1;
            long cookie = 0;
            boolean updateOk = false;
            try {            	
            	cookie = client.setRecordLocked(recNo);
            	updateOk = client.modifyRecord(room, recNo, cookie);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Update, run: " + countUp2End 
						+ " - modify: " + updateOk
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Update, run: " 
							+ countUp2End + "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countUp2End++;
            if ((countUp2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Update, start: " + countUp2Start
        				+ " - run end: "+ countUp2End 
        				+ " - loops: " + countRuns
        				+ " - ok: " + updateOk);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it creates a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class CreatingRecordThread extends Thread { 
    	
    	/**
         * No-arg constructor to create an object of this class.
         */
        public CreatingRecordThread() {
			//
		}
		/**
         * Overridden <code>run</code> method. Creates a Record.
         */
        @Override
		public void run() {
        	countCreateStart++;
        	final Record room = new Record();
            room.setName("F_A_CA_" + countCreateStart);
            room.setCity("Xanadu");
            room.setNumberOfStaff(777);
            room.setTypesOfWork("all");
            room.setHourlyChargeRate("$99.00"); 
            room.setRecNo((int)(Math.random()* 20) + 1);
            long recNo = 0;
            try {
            	recNo = client.addRecord(room);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Create, run: "            			
						+ countCreateEnd 
						+ " - recNo: " + recNo + " - Exc: " 
						+ e.getLocalizedMessage());
            }
            countCreateEnd++;
            if ((countCreateEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Create, start: " + countCreateStart 
        				+ " - run end: "+ countCreateEnd 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it deletes a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class DeletingRecord1Thread extends Thread { 
    	
    	/**
    	 * To produce a random number.
    	 */
    	Random ran = new Random();
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public DeletingRecord1Thread() {
			// 
		}
    	
		/**
    	 * Overridden <code>run</code> method. Deletes a Record.
    	 */
        @Override
		public void run() {
        	countDelStart++;
        	boolean delOk = false;
        	long cookie = 0; 
        	final long recNo = (long)this.ran.nextInt(20) + 1;
            try {            	
            	cookie = client.setRecordLocked(recNo);
				delOk = client.removeRecord(recNo, cookie);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Delete, run: " 
						+ countDelEnd + " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Delete, run: " 
							+ countDelEnd 
							+ " - delete: " + delOk
							+ "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countDelEnd++;
            if ((countDelEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Delete, start: " + countDelStart 
        				+ " - run end: "+ countDelEnd 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo
        				+ " - del: " + delOk);
            }            
        }
    }
    
 
    /**
     * In its <code>run</code> method it reads Records by Record number.
     *
     */
    private class FindingRecordsThread extends Thread { 
    	
    	/**
    	 * To produce a random number.
    	 */
    	Random ran = new Random();
    	
        /**
         * No-arg constructor to create an object of this class.
         */
        public FindingRecordsThread() {
			// 
		}
        
        /**
         * Overridden <code>run</code> method. Reads Records by Record number.
         *
         */
		@Override
		public void run() {
			countGetRecStart++;
			Record r = new Record();
            try {  
            	r =  client.getRecord((long)this.ran.nextInt(28) + 1);  
            	r.toString();
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Find, run: " 
						+ countGetRecEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetRecEnd++;
            if ((countGetRecEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Find, start: " + countGetRecStart
        				+ " - run end: "+ countGetRecEnd 
        				+ " - loops: " + countRuns
        				+ " - rec: " + r);
            }            
        }
    }
    
    
    
    /**
     * In its <code>run</code> method it reads all valid Records.
     *
     */
    private class GetAllRecordsThread extends Thread {
    	
        /**
         * No-arg constructor to create an object of this class.
         */
        public GetAllRecordsThread() {
			// 
		}
        
        /**
         * Overridden <code>run</code> method. Reads all valid 
         * Records.
         *
         */
		@Override
		public void run() {
			countGetAllRecsStart++;
			List<Record> li = new ArrayList<Record>();
            try {  
            	li = client.getAllValidRecords();
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Find, run: " 
						+ countGetRecEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetAllRecsEnd++;
            if ((countGetAllRecsEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Get All Records, start: " + countGetAllRecsStart
        				+ " - run end: "+ countGetAllRecsEnd 
        				+ " - loops: " + countRuns
        				+ " - size: " + li.size());
            }            
        }
    }
    
    
    /**
     * In its <code>run</code> method it finds Records by criteria
     * and reads the first Record of the result.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class FindingByRecordsThread extends Thread {
 
        /**
         * No-arg constructor to create an object of this class.
         */
        public FindingByRecordsThread() {
			//  
		}
        
        /**
    	 * Overridden <code>run</code> method. Finds Records by criteria
    	 * and reads the first Record of the result.
    	 */
		@Override
		public void run() {
			long[] results = new long[]{};
            try{
            	countFindByStart++;
               results = client.findByFilter(criteriaExists);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "FindBy, run: " 
						+ countFindByEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countFindByEnd++;
            if ((countFindByEnd % 10000) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "FindBy, start: " + countFindByStart
        				+ " - run end: "+ countFindByEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.length
            			);
            }
            
        }
    }
    
    /**
     * In its <code>run</code> method it searches for all locked Records.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class GetLockedThread extends Thread {
 
        /**
         * No-arg constructor to create an object of this class.
         */
        public GetLockedThread() {
			//  
		}
        
        /**
    	 * Overridden <code>run</code> method. Finds Record numbers
    	 * of locked Records.
    	 */
		@Override
		public void run() {
			Set<Long> results = new HashSet<Long>();	
            try{
				countGetLoStart++;
				results = client.getLocked();
				results.size();
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "GetLocked, run: " 
						+ countGetLoEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetLoEnd++;
            if ((countGetLoEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "GetLocked, ENDE run, start: " + countGetLoStart
        				+ " - run end: "+ countGetLoEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.size()
        				);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it rents a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class RentRecord1Thread extends Thread { 
    	
        /**
         * No-arg constructor to create an object of this class.
         */
        public RentRecord1Thread() {
			//
		}
        
        /**
         * Overridden <code>run</code> method. Rents a Record.
         */
		@Override
		public void run() {
			countRent2Start++;          
            final int recNo = (int)(Math.random()* (28 - 1 + 1)) + 1;
            long cookie = 0;
            boolean updateOk = false;
            try {            	
            	cookie = client.setRecordLocked(recNo);
            	updateOk = client.reserveRecord(recNo, 11111, cookie);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Rent 2, run: " + countRent2End 
						+ " - modify: " + updateOk
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
							+ "Rent 2, run: " 
							+ countRent2End + "Exc unlock: " 
							+ e.getLocalizedMessage());
				} 
            }
            countRent2End++;
            if ((countRent2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Rent 2 END, start: " + countRent2Start
        				+ " - run end: "+ countRent2End 
        				+ " - loops: " + countRuns
        				+ " - ok: " + updateOk);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it Releases a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class ReleaseRecord1Thread extends Thread {
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public ReleaseRecord1Thread() {
			// 
		}
    	
		/**
    	 * Overridden <code>run</code> method. Releases a Record.
    	 */
        @Override
		public void run() {
        	countRelease2Start++;
        	final long recNo = ((int)(Math.random()* 28) + 1);
            boolean okRelease = false;
            final boolean okUnlock = false;
            long lockCookie = 0;
            try {
            	lockCookie = client.setRecordLocked(recNo);
            	okRelease = client.releaseRecord(recNo, lockCookie);
            } catch (final Exception e) {
            	Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Release 2, run: "            			
						+ countRelease2End 
						+ " - recNo: " + recNo + " - Exc: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, lockCookie);
				} catch (final Exception e) {
					Z_TestAdminLocal.this.log.severe("Z_TestAdminLocal - "
							+ "Release 2, run: " 
							+ countRelease2End + "Exc unlock: " 
							+ e.getLocalizedMessage());
				} 
            }
            countRelease2End++;
            if ((countRelease2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestAdminLocal - "
            			+ "Release ENDE run, start: " + countRelease2Start 
        				+ " - run end: "+ countRelease2End 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo
        				+ " - del: " + okRelease
        				+ " - unl: " + okUnlock);
            }            
        }
    }
}

