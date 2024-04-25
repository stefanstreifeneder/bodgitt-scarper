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

import suncertify.db.InterfaceClient_Seller;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.direct.seller.DirectConnector_Seller;
import suncertify.gui.SavedConfiguration;


/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roberto Perillo(
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).<br>
 * I revised the original class according to my public interfaces
 * <code>suncertify.db.InterfaceClient_ReadOnly</code>,
 * <code>suncertify.db.InterfaceClient_LockPermission</code> and
 * <code>suncertify.db.InterfaceClient_Seller</code>.
 * <br>
 * <br>
 * The class accesses the database by using an Seller client.
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
public class Z_TestSellerLocal {
	
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.test.TestRecordDatabaseLocalAdmin</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.test.Z_TestSellerLocal"), 
			Level.ALL);	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>UpdateingRandomRecordThread</code>.
	 */
	static int countUp1Start = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>UpdatingRandomRecordThread</code> has reached the end.
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
	 * <code>GetAllRecordsThread</code>.
	 */
	static int countGetAllRecsStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetAllRecordsThread</code> has reached the end.
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
	static InterfaceClient_Seller client = null;


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
        			"Z_TestSellerLocal starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"50000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestSellerLocal - please enter the number of iterations!", 
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
        			"Z_TestSellerLocal starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"10000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_TestSellerLocal - please enter the number of iterations!", 
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
		null, "Z_TestSellerLocal" 
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
    			"Z_TestSellerLocal\n"
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
				+ "\nUpdate-1: P_S_UP_1_ + count; Smalville; Painting; 9; 47.00; "
				+ "\nUpdate-2: B_S_UP_2_ + count; Myville; Drywall; 41; 69.00; "
				+ "\nCreate: S_S_CR_ + count; Myville; Plumbing; 55; 59.00; "
				+ "\n"
				+ "\n");
    	
    	
    	
		if (inputAll == null) {
			System.exit(0);			
		}else if (inputAll.equals("")) {
			criteriaExists = new String[]{null};		
		} else{ 
			criteriaExists = inputAll.split(";");
		}
		
		
		System.out.println("Z_TestSellerLocal has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + countIterToNextDisplay
    			+ "\nYour input: " + inputAll);
		
		final int n = JOptionPane.showConfirmDialog(null, 
				"Z_TestSellerLocal (SELLER) "
				 		+ "starts."
				 		+ "\nRole: SELLER" 
				 		+ "\nPath: " + pathDatabaseCurDir
				 		+ "\nIterations: " + iterations
		    			+ "\nIterations to display: " 
				 		+ countIterToNextDisplay
		    			+ "\nYour input: " + inputAll, 
				 		"Z_TestSellerLocal (SELLER)",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    	try {
    		client = DirectConnector_Seller.getLocal(pathDatabaseCurDir, "");
		} catch (final FileNotFoundException e) {
			System.out.println("Z_TestSellerLocal, static initializr, Exce: "
					+ e.getMessage());
		} catch (final IOException e) {
			System.out.println("Z_TestSellerLocal, static initializr, Exce: "
					+ e.getMessage());
		}
    }
	
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_TestSellerLocal(){
		// 
	}
 
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
    	try{
    		new Z_TestSellerLocal().startTests();
    	}catch(final OutOfMemoryError e){ 
    		final Date d = new Date(System.currentTimeMillis());
    		final SimpleDateFormat sdf = 
    			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
    		final String formattedDate = sdf.format(d);
    		System.out.println("Z_TestSellerLocal, OOMError: " + formattedDate);
    		e.printStackTrace();
    	}
    }
 
    /**
     * Starts the threads in a loop.
     * Please call <code>join</code>, if You run the program in
     * remote mode.
     */
    private void startTests() {
    	
        try {
        	for (int i = 0; i < iterations; i++) {
            	
            	countRuns++;
            	
            	final Thread updatingRandom = new UpdatingRandomRecordThread();
            	updatingRandom.start();           	
            	
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
            	
            	final Thread getAllResRecords = new GetAllRecordsThread();
            	getAllResRecords.start();
            	
            }
            //ensures messages will be visible on the command line.
            Thread.sleep(1000);            
        } catch (final Exception e) {
            System.out.println("start " + e.getMessage());
        }  
        
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
		
		System.out.println("Z_TestSellerLocal has run to the end."
        		+ "\nRole: SELLER" 
        		+ "Done, Loops: " + countRuns 
        		+ "\n- up 1 start: " + countUp1Start
        		+ " - up 1 end: " + countUp1End
        		+ "\n- up 2 start: " + countUp2Start
        		+ " - up 2 end: " + countUp2End
        		+ "\n- create start: " + countCreateStart
        		+ " - create end: " + countCreateEnd
        		+ "\n- delete start: " + countDelStart
        		+ " - delete end: " + countDelEnd
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
        
        
		final JOptionPane alert = new JOptionPane("Z_TestSellerLocal "
					+ "has run to the end."
					+ "\nRole: SELLER"
					+ "\nDone, Loops: " + countRuns 
					+ "\n- up 1 start: " + countUp1Start
					+ " - up 1 end: " + countUp1End
					+ "\n- up 2 start: " + countUp2Start
					+ " - up 2 end: " + countUp2End
					+ "\n- create start: " + countCreateStart
					+ " - create end: " + countCreateEnd
					+ "\n- delete start: " + countDelStart
					+ " - delete end: " + countDelEnd
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
					+ "\n- " + formattedDate					
					, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);			
		final JDialog dialog = alert.createDialog(null, "B & S - "
					+ "Z_TestSellerLocal");
		dialog.setVisible(true);
		System.exit(0);//Must if graphics in use
    }
    
    
    /**
     * In its <code>run</code> method it updates a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class UpdatingRandomRecordThread extends Thread { 
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public UpdatingRandomRecordThread() {
			// 
		}
		/**
    	 * Overridden <code>run</code> method. Updates a Record.
    	 */
    	@Override
		public void run() {
    		countUp1Start++;
			final Record room = new Record();
			room.setName("P_S_UP_1_-" + countRuns);
			room.setCity("Munich");
			room.setNumberOfStaff(9);
			room.setTypesOfWork("Painting");
			room.setHourlyChargeRate("$47.00");
			room.setOwner("");
			final int recNo = (int)(Math.random()* 28) + 1;
			boolean ok = false;
			long cookie = 0;
			try {
				cookie = client.setRecordLocked(recNo);
				ok = client.modifyRecord(room, recNo, cookie);
			} catch (final Exception e) {
				Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal "
						+ "U 1, run: " + countUp1End 
						+ " - modify: " + ok
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
			}finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
							+ " U 1, run: " 
								+ countUp1End + "Exc 2: " 
								+ e.getLocalizedMessage());
				} 
            }
			countUp1End++;
			if ((countUp1End % countIterToNextDisplay) == 0) {
				System.out.println(
						"Z_TestSellerLocal - UP 1 END, start run: " + countUp1Start 
						+ " - run end: " + countUp1End 
						+ " - loops: " + countRuns
						+ " - ok: " + ok);
				}			
        }//run end
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
            room.setName("B_S_UP_2_" + countRuns);
            room.setCity("Berlin");
            room.setNumberOfStaff(41);
            room.setTypesOfWork("Drywall");
            room.setHourlyChargeRate("$69.00");
            room.setOwner("");            
            final int recNo = (int)(Math.random()* (28 - 1 + 1)) + 1;
            long cookie = 0;
            boolean updateOk = false;
            try {            	
            	cookie = client.setRecordLocked(recNo);
            	updateOk = client.modifyRecord(room, recNo, cookie);
            } catch (final Exception e) {
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
            			+ " U 2, run: " + countUp2End 
						+ " - modify: " + updateOk
						+ " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
							+ " U 2, run: " 
							+ countUp2End + "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countUp2End++;
            if ((countUp2End % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - UP 2 END, start: " + countUp2Start
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
            room.setName("S_S_CR_" + countCreateStart);
            room.setCity("Hamburg");
            room.setTypesOfWork("Plumbing");
            room.setNumberOfStaff(55);
            room.setHourlyChargeRate("$59.00"); 
            room.setRecNo((int)(Math.random()* 28) + 1);
            long recNo = 0;
            try {
            	recNo = client.addRecord(room);
            } catch (final Exception e) {
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
            			+ " Create, run: "            			
						+ countCreateEnd 
						+ " - recNo: " + recNo + " - Exc: " 
						+ e.getLocalizedMessage());
            }
            countCreateEnd++;
            if ((countCreateEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - CREATE  ENDE run, start: " + countCreateStart 
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
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
            			+ " Delete, run: " 
						+ countDelEnd + " - Exc 1: " 
						+ e.getLocalizedMessage());
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
							+ " Delete, run: " 
							+ countDelEnd 
							+ " - delete: " + delOk
							+ "Exc 2: " 
							+ e.getLocalizedMessage());
				} 
            }
            countDelEnd++;
            if ((countDelEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - "
            			+ "Delete ENDE run, start: " + countDelStart 
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
			Record r = null;
            try {  
            	r =  client.getRecord((long)this.ran.nextInt(28) + 1);  
            	r.toString();
            } catch (final Exception e) {
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal "
            			+ "Find, run: " 
						+ countGetRecEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetRecEnd++;
            if ((countGetRecEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - Find, ENDE run, start: " + countGetRecStart
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
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal "
            			+ "FindBy, run: " 
						+ countFindByEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countFindByEnd++;
            if ((countFindByEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - FindBy, ENDE run, start: " + countFindByStart
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
            	Z_TestSellerLocal.this.log.severe("Z_TestAdminLocal - "
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
            	Z_TestSellerLocal.this.log.severe("Z_TestSellerLocal"
            			+ " GetLocked, run: " 
						+ countGetLoEnd + "Exc: " 
						+ e.getLocalizedMessage());
            }
            countGetLoEnd++;
            if ((countGetLoEnd % countIterToNextDisplay) == 0) {
            	System.out.println(
            			"Z_TestSellerLocal - "
            			+ "GetLocked, ENDE run, start: " + countGetLoStart
        				+ " - run end: "+ countGetLoEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.size()
        				);
            }            
        }
    }
}


