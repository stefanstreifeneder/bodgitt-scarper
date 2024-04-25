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

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.direct.buyer.DirectConnector_Buyer;
import suncertify.gui.SavedConfiguration;

/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roberto Perillo(
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).<br>
 * I revised the original class according to my public interfaces
 * <code>suncertify.db.InterfaceClient_ReadOnly</code>,
 * <code>suncertify.db.InterfaceClient_LockPermission</code> and
 * <code>suncertify.db.InterfaceClient_Buyer</code>.
 * <br>
 * <br>
 *  The class accesses the database by using an Buyer client.
 * <br>
 * <br>
 * The class addresses the methods <code>reserveRecord</code>
 * and <code>releaseRecord</code> of the public interface
 * <code>suncertify.db.InterfaceClient_Buyer</code>. It does not use 
 * methods of the public interface
 * <code>suncertify.db.InterfaceClient_Seller</code>.
 * <br>
 * <br>
 * The design of the original program stays (almost) the same:<br>
 * - one connection object used by all threads initialized in the 
 * static initializer.<br>
 * - one loop, which creates multiple threads<br>
 * - each thread represents a functionality according to the public
 * interfaces<br>
 * - unlock is done in a finally block
 * <br>
 * <br>
 * Attention: Please set the Logger Level to OFF, otherwise
 * memory problems can arise.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class Z_TestBuyerLocal {
	
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.test.TestRecordDatabaseLocalAdmin</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.test.Z_TestBuyerLocal"), 
			Level.ALL);	
	
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
	 * <code>GetDeletedThread</code>.
	 */
	static int countGetAllRecsStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetDeletedThread</code> has reached the end.
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
	static InterfaceClient_Buyer client = null;


	/**
	 * Stores the number of iterations until to the next view.
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
    	//new
    	boolean idIsEntered = true;
    	while(idIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_TestBuyerLocal starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"50000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestBuyerLocal - please enter the number of iterations!", 
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
        			"Z_TestBuyerLocal starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"10000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestBuyerLocal - please enter the number of iterations!", 
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
		null, "Z_TestBuyerLocal" 
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
    			"Z_TestBuyerLocal\n"
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
				+ "\nRent-1-ID: 88888"  
				+ "\nRent-2-ID: 2345"
				+ "\n"
				+ "\n");
    	
    	if (inputAll == null) {
			System.exit(0);			
		}else if (inputAll.equals("")) {
			criteriaExists = new String[]{null};		
		} else{ 
			criteriaExists = inputAll.split(";");
		}
    	
    	System.out.println("Z_TestBuyerLocal has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + countIterToNextDisplay
    			+ "\nYour input: " + inputAll);
    	
    	
    	final int n = JOptionPane.showConfirmDialog(null, 
				"Z_TestBuyerLocal (BUYER) "
				 		+ "starts."
				 		+ "\nRole: BUYER" 
				 		+ "\nPath: " + pathDatabaseCurDir
				 		+ "\nIterations: " + iterations
				 		+ "\nLoops until next display: " + countIterToNextDisplay
		    			+ "\nYour input: " + inputAll, 
				 		"Z_TestBuyerLocal (BUYER)",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    	try {
    		//Client initialization:
    		client = DirectConnector_Buyer.getLocal(pathDatabaseCurDir, "");
		} catch (final FileNotFoundException e) {
			System.out.println("Z_TestBuyerLocal, static initializr, Exce: "
					+ e.getMessage());
		} catch (final IOException e) {
			System.out.println("Z_TestBuyerLocal, static initializr, Exce: "
					+ e.getMessage());
		}
    }
	
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_TestBuyerLocal(){
		// 
	}
 
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
    	try{
    		new Z_TestBuyerLocal().startTests();
    	}catch(final OutOfMemoryError e){ final Date d = new Date(System.currentTimeMillis());
        	final SimpleDateFormat sdf = 
        			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        	final String formattedDate = sdf.format(d);
        	System.out.println("Z_TestBuyerLocal, OOMError: " + formattedDate);
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
            	
            	final Thread rentRandom = new RentRandomRecordThread();
            	rentRandom.start();           	
            	
            	final Thread rentRecord1 = new RentRecord1Thread();
            	rentRecord1.start();          	
            	
                final Thread releaseRecord = new ReleaseRecordThread();
                releaseRecord.start();               
                
                final Thread release1Record = new ReleaseRecord1Thread();
                release1Record.start();           	
            	
				final Thread findingRecords = new FindingRecordsThread();
				findingRecords.start();			
                
                final Thread findingByRecords = new FindingByRecordsThread();
                findingByRecords.start();
            	
            	final Thread getLoRecords = new GetLockedThread();
            	getLoRecords.start();
            	
            	final Thread getAllRecords = new GetAllRecordsThread();
            	getAllRecords.start();
            }
            //ensures messages will be visible on the command line.
            Thread.sleep(1000);            
        } catch (final Exception e) {
            System.out.println("start " + e.getMessage());
        } 
        
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
        
		System.out.println("Z_TestBuyerLocal has run to the end."
					+ "\nRole: BUYER" 
					+ "\niterations: " + iterations
					+ "\nbreaks: " + countIterToNextDisplay 
					+ "\nDone, Loops: " + countRuns 
					+ "\n- up 1 start: " + countRent1Start
					+ " - up 1 end: " + countRent1End
					+ "\n- up 2 start: " + countRent2Start
					+ " - up 2 end: " + countRent2End
					+ "\n- create start: " + countRelease1Start
					+ " - create end: " + countRelease1End
					+ "\n- delete start: " + countRelease2Start
					+ " - delete end: " + countRelease2End
					+ "\n- find start: " + countGetRecStart
					+ " - find end: " + countGetRecEnd
					+ "\n- findBy start: " + countFindByStart
					+ " - findBy end: " + countFindByEnd  
					+ "\n- getDeleted start: " + countGetAllRecsStart
					+ " - getDeleted end: " + countGetAllRecsEnd   
					+ "\n- getLocked start: " + countGetLoStart
					+ " - getLocked end: " + countGetLoEnd 
					+ "\n- getRented start: " + countGetRentStart
					+ " - getRented end: " + countGetRentEnd
					+ "\n- " + formattedDate);
		
		final JOptionPane alert = new JOptionPane("Z_TestBuyerLocal has "
					+ "run to the end." 
					+ "\nRole: BUYER" 
					+ "\niterations: " + iterations
					+ "\nbreaks: " + countIterToNextDisplay
					+ "\nDone, Loops: " + countRuns 
					+ "\n- up 1 start: " + countRent1Start + " - up 1 end: " + countRent1End 
					+ "\n- up 2 start: " + countRent2Start + " - up 2 end: " + countRent2End
					+ "\n- create start: " + countRelease1Start + " - create end: " + countRelease1End
					+ "\n- delete start: " + countRelease2Start + " - delete end: " + countRelease2End
					+ "\n- find start: " + countGetRecStart + " - find end: " + countGetRecEnd 
					+ "\n- findBy start: " + countFindByStart + " - findBy end: " + countFindByEnd 
					+ "\n- getDeleted start: " + countGetAllRecsStart + " - getDeleted end: " + countGetAllRecsEnd 
					+ "\n- getLocked start: " + countGetLoStart + " - getLocked end: " + countGetLoEnd 
					+ "\n- getRented start: " + countGetRentStart 
					+ " - getRented end: " + countGetRentEnd 
					+ "\n- " + formattedDate
			, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		
		final JDialog dialog = alert.createDialog(null, "B & S - "
					+ "Z_TestBuyerLocal");
		dialog.setVisible(true);
		System.exit(0);// Must if graphics in use
        
    }
    
    
    /**
     * In its <code>run</code> method it rents a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class RentRandomRecordThread extends Thread { 
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public RentRandomRecordThread() {
			// 
		}
		/**
    	 * Overridden <code>run</code> method. Rents a Record.
    	 */
    	@Override
		public void run() {
    		countRent1Start++;
			final int recNo = (int)(Math.random()* 28) + 1;
			boolean ok = false;
			long cookie = 0;
			try {
				
				cookie = client.setRecordLocked(recNo);
				ok = client.reserveRecord(recNo, 88888, cookie);
			} catch (final Exception e) {
				Z_TestBuyerLocal.this.log.severe("Rent 1, run: " + countRent1End 
						+ " - modify: " + ok
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
			}finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestBuyerLocal.this.log.severe("Rent 1, run: " 
								+ countRent1End + "Exc unlock: " 
								+ e.getLocalizedMessage());
				} 
            }
			countRent1End++;
			if ((countRent1End % countIterToNextDisplay) == 0) {
				System.out.println(
						"Z_TestBuyerLocal - UP 1 END, start run: " + countRent1Start 
						+ " - run end: " + countRent1End 
						+ " - loops: " + countRuns
						+ " - ok: " + ok);
			}			
        }//run end
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
            	updateOk = client.reserveRecord(recNo, 2345, cookie);
            } catch (final Exception e) {
            	Z_TestBuyerLocal.this.log.severe("Rent 2, run: " + countRent2End 
						+ " - modify: " + updateOk
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestBuyerLocal.this.log.severe("Rent 2, run: " 
							+ countRent2End + "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countRent2End++;
            if ((countRent2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - UP 2 END, start: " + countRent2Start
        				+ " - run end: "+ countRent2End 
        				+ " - loops: " + countRuns
        				+ " - ok: " + updateOk);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it releases a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class ReleaseRecordThread extends Thread { 
    	
    	/**
         * No-arg constructor to create an object of this class.
         */
        public ReleaseRecordThread() {
			//
		}
		/**
         * Overridden <code>run</code> method. Releases a Record.
         */
        @Override
		public void run() {
        	countRelease1Start++;
            final long recNo = ((int)(Math.random()* 28) + 1);
            boolean okRelease = false;
            final boolean okUnlock = false;
            long lockCookie = 0;
            try {
            	lockCookie = client.setRecordLocked(recNo);
            	okRelease = client.releaseRecord(recNo, lockCookie);
            } catch (final Exception e) {
            	Z_TestBuyerLocal.this.log.severe("Release 1, run: "            			
						+ countRelease1End 
						+ " - recNo: " + recNo + " - Exc: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, lockCookie);
				} catch (final Exception e) {
					Z_TestBuyerLocal.this.log.severe("Release 1, run: " 
							+ countRelease1End + "Exc unlock: " 
							+ e.getLocalizedMessage());
				} 
            }
            countRelease1End++;
            if ((countRelease1End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - Release 1  ENDE run, start: " + countRelease1Start 
        				+ " - run end: "+ countRelease1End 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo
        				+ " - rel: " + okRelease
        				+ " - unLock: " + okUnlock);
            }            
        }
    }
 
    /**
     * In its <code>run</code> method it releases a Record.
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
            	Z_TestBuyerLocal.this.log.severe("Release 2, run: "            			
						+ countRelease2End 
						+ " - recNo: " + recNo + " - Exc: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, lockCookie);
				} catch (final Exception e) {
					Z_TestBuyerLocal.this.log.severe("Release 2, run: " 
							+ countRelease2End + "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countRelease2End++;
            if ((countRelease2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - Releases 2 ENDE run, start: " + countRelease2Start 
        				+ " - run end: "+ countRelease2End 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo
        				+ " - del: " + okRelease
        				+ " - unl: " + okUnlock);
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
            } catch (final Exception e) {
            	Z_TestBuyerLocal.this.log.severe("Find, run: " 
						+ countGetRecEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetRecEnd++;
            if ((countGetRecEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - Find, ENDE run, start: " + countGetRecStart
        				+ " - run end: "+ countGetRecEnd 
        				+ " - loops: " + countRuns
        				+ " - rec: " + r);
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
            	Z_TestBuyerLocal.this.log.severe("FindBy, run: " 
						+ countFindByEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countFindByEnd++;
            if ((countFindByEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - FindBy, ENDE run, start: " + countFindByStart
        				+ " - run end: "+ countFindByEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.length
            			);
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
            	Z_TestBuyerLocal.this.log.severe("Z_TestAdminLocal - "
            			+ "Find, run: " 
						+ countGetRecEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetAllRecsEnd++;
            if ((countGetAllRecsEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - "
            			+ "Get All Records, start: " + countGetAllRecsStart
        				+ " - run end: "+ countGetAllRecsEnd 
        				+ " - loops: " + countRuns
        				+ " - size: " + li.size());
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
            	Z_TestBuyerLocal.this.log.severe("GetLocked, run: " 
						+ countGetLoEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetLoEnd++;
            if ((countGetLoEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestBuyerLocal - GetLocked, ENDE run, start: " + countGetLoStart
        				+ " - run end: "+ countGetLoEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.size()
        				);
            }            
        }
    }
}

