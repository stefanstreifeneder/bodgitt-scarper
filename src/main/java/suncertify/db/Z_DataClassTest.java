package suncertify.db;
 
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import suncertify.gui.SavedConfiguration;
 
/**
 * The class is a derivative of a class, which is published on www.coderanch.com
 * and was created by Roberto Pirillo originally (
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).
 * <br>
 * I revised the original class according to the interface
 * <code>suncertify.db.DBAccess</code>.
 * <br>
 * <br>
 * Attention: The <code>java.util.logging.Level</code> of the
 * <code>java.util.Logger</code> must be set to <code>Level.OFF</code>.<br>
 * <br>
 * The design of Roberto Pirillo's original class stays (almost) the same:<br>
 * - one connection object used by all threads initialized in the static
 * initializer<br>
 * - one loop, which creates multiple threads<br>
 * - each thread represents a functionality according to the interface
 * <code>suncertify.db.DBAccess</code><br>
 * - unlock is done in a finally block<br>
 * <br>
 * <br>
 * The path to the database is obtained by the class
 * <code>suncertify.gui.SavedConfiguration</code>, which reads out the
 * connection data of the file suncertify.properties. 
 * <br>
 * <br>
 * There are another testing classes available see <code>suncertify.test</code>.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class Z_DataClassTest {
	
	
	/**
	 * Counts the starts of the method <code>run</code> of the inner class
	 * <code>UpdatingRandomRecordThread</code>.
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
	 * <code>DeletingRecordThread</code>.
	 */
	static int countDelStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>DeletingRecordThread</code> has reached the end.
	 */
	static int countDelEnd = 0;	
	
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
	 * <code>FindingByFilterRecordsThread</code>.
	 */
	static int countFindByFilterStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>FindingByFilterRecordsThread</code> has reached the end.
	 */
	static int countFindByFilterEnd = 0;
	
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
	 * Counts the iterations in the method <code>startTest</code>.
	 */
	static int countRuns = 0;
	
	/**
	 * Stores the criteria used to find matching criteria.
	 */
	static String[] criteriaExists = {""};
	
	/**
	 * Stores the number of iterations until to the next break.
	 */
	static long countIterToNextDisplay = 0;
	
	/**
	 * Set the highest used Record number.
	 */
	static int sizeRecNos = 0;
	
	/**
	 * An object to test the class. Usually the creation of an object 
	 * of class <code>Data</code> is reserved for the class 
	 * <code>RecordDatabase</code>.
	 */
	static Data data = null;
	
	/**
	 * Stores whether the find by criteria operation should run or not.
	 */
	static boolean runFindBy = true; 

	/**
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;
	
	/**
	 * The path to the database file in the currently working directory.
	 */
	private static String pathDatabaseCurDir = "";
	
	/**
	 * Stores the adjustment for a run concerning
	 * how to build the Record number and how to lock a Record.
	 */
	private static String mode = "";
	

	/**
	 * Static initializer to settle the access to the database file.
	 */
    static {
    	boolean idIsEntered = true;
    	while(idIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_DataClassTest starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"100000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else if (inputIterations.equals("")) {  			
    				
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_DataClassTest - please enter the number of iterations!", 
    					 		"Z_DataClassTest",
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
        			"Z_DataClassTest."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"10000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_DataClassTest - please enter the number of iterations!", 
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
    	
    	boolean maxSizeOfRecnos = true;
    	while(maxSizeOfRecnos){
    		final String displayIterations = JOptionPane.showInputDialog(null,
        			"Z_DataClassTest"
    				 		+ "\nPlease enter the number of the highest Record number."
    				 		+ "\nThe number will be used within add, update and delete"
    				 		+ "\noperations." , 
    				 		"28");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_DataClassTest - please enter the number of "
    					+ "the hihgest Record number!", 
    					 		"Z_DataClassTest",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			sizeRecNos = Integer.parseInt(displayIterations);
    			maxSizeOfRecnos = false;
    		}
    	}
    	
    	// Find by criteria    	
    	final int k = JOptionPane.showConfirmDialog(null, 
				"Z_DataClassTest "
				 		+ "run operation find by criteria?"
				 		, 
				 		"Z_DataClassTest",
				JOptionPane.YES_NO_OPTION);
    	
    	String inputAll = null;
		if (k == JOptionPane.NO_OPTION || k == JOptionPane.CLOSED_OPTION) {
			runFindBy = false;
		}else{
			inputAll = JOptionPane.showInputDialog(null,
	    			"Z_DataClassTest\n"
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
					+ "\nUpdate-1: D_D_UP_1_ + count; Houseville; Drywall; 1; $211.99"
					+ "\nUpdate-2: H_D_UP_2_ + count; Dorfen; Heating; 23, $85.00"
					+ "\nCreate: F_D_CR_ + count; Myville; 14; Heating; 85; "
					+ "\n"
					+ "\n");
	    	
			if (inputAll == null) {
				System.exit(0);			
			}else if (inputAll.equals("")) {
				criteriaExists = new String[]{null};		
			} else{ 
				criteriaExists = inputAll.split(";");
			}
		}
		
		System.out.println("Z_DataClassTest has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + countIterToNextDisplay
    			+ "\nYour input: " + inputAll);
		
		
		pathDatabaseCurDir = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.DATABASE_LOCATION);
		
		String password = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.PASSWORD);
		
		
		
		final int n = JOptionPane.showConfirmDialog(null, 
				"Z_DataClassTest "
				 		+ "starts."
				 		+ "\nPath: " + pathDatabaseCurDir
				 		+ "\nIterations: " + iterations
				 		, 
				 		"Z_DataClassTest",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    	
    	try {
			data = new Data(pathDatabaseCurDir, password);
		} catch (final FileNotFoundException e) {
			System.out.println("Z_DataClassTest, static initializr, Exce: "
					+ e.getMessage());
		} catch (final IOException e) {
			System.out.println("Z_DataClassTest, static initializr, Exce: "
					+ e.getMessage());
		}
    }
    
    /**
	 * Private noarg-constructor.
	 */
	private Z_DataClassTest(){
		// 
	}
 
    /**
     * The main method to start the test program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
    	try{
    		new Z_DataClassTest().startTests();
	    }catch(final OutOfMemoryError e){ 
	    	final Date d = new Date(System.currentTimeMillis());
			final SimpleDateFormat sdf = 
				new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			final String formattedDate = sdf.format(d);
			System.out.println("Z_DataClassTest, OOMError: " + formattedDate);
			e.printStackTrace();
			System.exit(-1);
	    }
    }
 
    /**
     * Starts the threads in a loop.
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
                
                final Thread deletingRecord = new DeletingRecordThread();
                deletingRecord.start();
            	
				final Thread findingRecords = new FindingRecordsThread();
				findingRecords.start();
            	
				final Thread getAllRecsRecords = new GetAllValidRecordsThread();
				getAllRecsRecords.start();
				
				if(runFindBy){
	                final Thread findingByRecords = new FindingByRecordsThread();
	                findingByRecords.start();     
				}
            }
            
            //ensures run client will be visible on the command line.
            Thread.sleep(1000);
            
        } catch (final Exception e) {
            System.out.println("start " + e.getMessage());
        }
        
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
        System.out.println("Z_DataClassTest, "
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
        		+ "\n- getAllValid start: " + countGetAllRecsStart
        		+ " - getAllValid end: " + countGetAllRecsEnd
        		+ "\nMode: " + mode
        		+ "\nDate: " + formattedDate);
        
        
        final JOptionPane alert = new JOptionPane("Z_DataClassTest has run to the end"
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
        		+ "\n- getAllValid start: " + countGetAllRecsStart
        		+ " - getAllValid end: " + countGetAllRecsEnd
        		+ "\nMode: " + mode
        		+ "\nDate: " + formattedDate
        		
        		, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		final JDialog dialog = alert.createDialog(null, "B & S - Z_DataClassTest");
		dialog.setVisible(true);
		System.exit(0);
    }
    
    
    /**
     * An object of this class is a <code>Thread</code> and
     * in its <code>run</code> method it updates a Record.
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
        	final Record room = new Record(1, "D_D_UP_1_" + countRuns,
        			"Houseville", "Drywall", 1,"$211.99", "");
			final String[] record = {
					new String(new byte[]{0, 0}),
					room.getName(),
					room.getCity(),
					room.getTypesOfWork(),
					Integer.toString(room.getNumberOfStaff()),
					room.getHourlyChargeRate(),
					room.getOwner()
			};

			final int recNo =  (int)(Math.random()* sizeRecNos) + 1;
			long cookie = 0;
			try {
				cookie = data.lockRecord(recNo);
				data.updateRecord(recNo, record, cookie);
			} catch (final Exception e) {
				
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println(" U 1 run: " 
							+ countUp1End + " - " + e.getLocalizedMessage());
				}
			}finally{
            	try {
					data.unlock(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))){
						System.out.println("U 1 " + e.getCause());
					}
				}
            }
			Z_DataClassTest.countUp1End++;
			if ((countUp1End % countIterToNextDisplay) == 0) {
				System.out.println("Z_DataClassTest - UP 1 END, start run: " + countUp1Start 
						+ " - run end: " + countUp1End 
						+ " - loops: " + countRuns
						+ " - recNo: " + recNo);
			}			
        }//run end
    }
 
    /**
     * An object of this class is a <code>Thread</code> and
     * in its <code>run</code> method it updates a Record.
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
        	final Record room = new Record(
        			1, "H_D_UP_2_ + count", "Dorfen", "Heating", 23, "$85.00" , "");
            final String[] record = {
					new String(new byte[]{0, 0}),
					room.getName(),
					room.getCity(),
					room.getTypesOfWork(),
					Integer.toString(room.getNumberOfStaff()),
					room.getHourlyChargeRate(),
					room.getOwner()
			};            
            final int recNo = (int)(Math.random()* sizeRecNos) + 1;            
            long cookie = 0;
            try {            	
            	cookie = data.lockRecord(recNo);
            	data.updateRecord(recNo, record, cookie);
            } catch (final Exception e) {
            	
            	if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
            		System.out.println("U 1 " + e.getLocalizedMessage());
            	}            	
            }finally{
            	try {
					data.unlock(recNo, cookie);
				} catch (final Exception e) {
					
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("U 2 " + e.getLocalizedMessage());
					}
				} 
            }
            Z_DataClassTest.countUp2End++;
            if ((countUp2End % countIterToNextDisplay) == 0) {
            	System.out.println("Z_DataClassTest - UP 2 END, start: " + countUp2Start
        				+ " - run end: "+ countUp2End 
        				+ " - loops: " + countRuns
						+ " - recNo: " + recNo);
            }
        }
    }
 
    /**
     * An object of this class is a <code>Thread</code> and
     * in its <code>run</code> method it creates a Record.
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
        	final int recNoRequest = (int)(Math.random()* sizeRecNos) + 1;
        	final String name = "Hund_CA" + countCreateStart;
        	final Record room = new Record(recNoRequest, name,
        			"Myville", "Plumbing", 2,"$150.00", "");
            final String[] record = {
					room.getFlag(),
            		room.getName(),
					room.getCity(),
					room.getTypesOfWork(),
					Integer.toString(room.getNumberOfStaff()),
					room.getHourlyChargeRate(),
					room.getOwner(),
					Long.toString(room.getRecNo())
			};
            
            long recNo = 0;
            try {
            	recNo = data.createRecord(record);
            } catch (final Exception e) {
            	
            	if( !(e instanceof DuplicateKeyException)) {
            		e.printStackTrace();
            		System.out.println("Create: " + e.getLocalizedMessage());
            	}
            }
            countCreateEnd++;
            if ((countCreateEnd % countIterToNextDisplay) == 0) {
            	System.out.println("Z_DataClassTest - CREATE  "
            			+ "ENDE run, start: " + countCreateStart 
        				+ " - run end: "+ countCreateEnd 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo);
            }
        }
    }
 
    /**
     * An object of this class is a <code>Thread</code> and
     * in its <code>run</code> method it deletes a Record.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class DeletingRecordThread extends Thread { 
    	
    	/**
    	 * To produce a random number.
    	 */
    	Random ran = new Random();
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public DeletingRecordThread() {
			// 
		}
    	
    	/**
    	 * Overridden <code>run</code> method. Deletes a Record.
    	 */
        @Override
		public void run() {
        	countDelStart++;
        	long cookie = 0; 
        	final long recNo = this.ran.nextInt(sizeRecNos);
        	
            try {
            	
            	cookie = data.lockRecord(recNo);
				data.deleteRecord(recNo, cookie);
            } catch (final Exception e) {
            	if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))
            			) {
            		System.out.println("Delete 1: " + e.getLocalizedMessage());
            		e.printStackTrace();
            		System.exit(-1);            		
            	}
            }finally{
            	try {
					data.unlock(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Delete 2: " + e.getLocalizedMessage());
					}
				} 
            }
            Z_DataClassTest.countDelEnd++;
            if ((countDelEnd % countIterToNextDisplay) == 0) {
            	System.out.println("Z_DataClassTest - Delete ENDE run, start: " + countDelStart 
        				+ " - run end: "+ countDelEnd 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo);
            }
        }
    }
    
 
    /**
     * An object of this class is a <code>java.lang.Thread</code> and
     * in its <code>run</code> method it reads Records by Record number.
     * 
     * @author stefan.streifeneder@gmx.de
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
    	 */
		@Override
		public void run() {
			countGetRecStart++;
			String[] rec =  {""};
			String name = "is deleted";
            try {              	
            	rec =  data.readRecord((long)this.ran.nextInt(sizeRecNos) + 1);            	
            	if(rec.length != 0){
            		name = rec[1].toString();
            	}
            } catch (final Exception e) {
            	if( !((e instanceof RecordNotFoundException)
            			^ (e instanceof IOException))) {
            		e.printStackTrace();
            		System.out.println("FindingRecords: " 
            			+ e.getLocalizedMessage());
            	}
            }
            Z_DataClassTest.countGetRecEnd++;
            if ((countGetRecEnd % countIterToNextDisplay) == 0) {
            	System.out.println("Z_DataClassTest - Find, ENDE run, start: " + countGetRecStart
        				+ " - run end: "+ countGetRecEnd 
        				+ " - loops: " + countRuns
        				+ " - rec: " + name
        						);
            }
        }
    }
    
    /**
     * An object of this class is a <code>java.lang.Thread</code> and
     * in its <code>run</code> method it finds Records by criteria.
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
                results = data.findByCriteria(criteriaExists);
            } catch (final Exception e) {
            	if(!(e instanceof RecordNotFoundException 
            			| 
            			!(e instanceof EOFException))) {
            		System.out.println("FindingByRecords: " + e.getLocalizedMessage());
            	}
            }
            countFindByEnd++;
            if((countFindByEnd % countIterToNextDisplay) == 0){
				System.out.println("Z_DataClassTest - FindBy, ENDE run, start: " 
						+ countFindByStart + " - countFindByEnd: " + countFindByEnd
						+ " - loops: " + countRuns
						+ " - size: " + results.length
						);
			}
        }
    }
    
    /**
     * An object of this class is a <code>java.lang.Thread</code> and
     * in its <code>run</code> method calls for all valid Records.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class GetAllValidRecordsThread extends Thread {
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
        public GetAllValidRecordsThread() {
			//  
		}
        /**
    	 * Overridden <code>run</code> method. Finds Records by criteria
    	 * and reads the first Record of the result.
    	 */
		@Override
		public void run() {
			List<String[]> results = new ArrayList<String[]>();
            try{
            	countGetAllRecsStart++;
                results = data.getRecords();
            } catch (final Exception e) {
            	if(!(e instanceof RecordNotFoundException 
            			| 
            			!(e instanceof EOFException))) {
            		System.out.println("FindingByRecords: " + e.getLocalizedMessage());
            	}
            }
            countGetAllRecsEnd++;
            if((countGetAllRecsEnd % countIterToNextDisplay) == 0){
				System.out.println("Z_DataClassTest - GetAllValid, ENDE run, start: " 
						+ countGetAllRecsStart + " - countGetAllRecsEnd: " 
						+ countGetAllRecsEnd
						+ " - loops: " + countRuns
						+ " - size: " + results.size()
						);
			}
        }
    }
}

