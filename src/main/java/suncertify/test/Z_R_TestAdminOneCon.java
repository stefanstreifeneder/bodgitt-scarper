package suncertify.test;
 
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import suncertify.db.DuplicateKeyException;
import suncertify.db.InterfaceClient_Admin;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.SavedConfiguration;
import suncertify.gui.StartMonitor;
 
/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roberto Perillo(
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).<br>
 * I revised the original class according to my public interfaces:
 * <br> <code>suncertify.db.InterfaceClient_ReadOnly</code>
 * <br> <code>suncertify.db.InterfaceClient_LockPermission</code>
 * <br> <code>suncertify.db.InterfaceClient_Buyer</code>
 * <br> <code>suncertify.db.InterfaceClient_Seller</code> 
 * <br> <code>suncertify.db.InterfaceClient_Admin</code>
 * <br>
 * <br> The class can only applied in a network environment. 
 * <br>
 * The design of the original program stays (almost) the same:<br>
 * - one connection object used by all threads initialized in the 
 * static initializer.<br>
 * - one loop, which creates multiple threads<br>
 * - each thread represents a functionality according to to my public
 * interfaces<br>
 * - unlock is done in a finally block<br>
 * <br>
 * <br>
 * Changes of the original design<br>
 * Each <code>Thread</code> object must call <code>join</code> otherwise a 
 * <code>java.io.StreamCorruptedException</code> will be thrown at the server
 * side and an <code>java.io.EOFException</code> at the client side.
 * 
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class Z_R_TestAdminOneCon {
	
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
	 * <code>GetValidRecsThread</code>.
	 */
	static int countGetValidStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetValidRecsThread</code> has reached the end.
	 */
	static int countGetValidEnd = 0;
	
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
	 * Counts the iterations done in the loop of the method
	 * <code>startTests</code>.
	 */
	static int countRuns = 1;	
	
	/**
	 * Counts each call of the method <code>run</code> of the inner
	 * class <code>BookThread</code> at the start.
	 */
	static int countRentStart = 1;	
	
	/**
	 * Counts each run of the method <code>run</code> of the inner
	 * class <code>BookThread</code> at the end.
	 */
	static int countRentEnd = 1;
	
	/**
	 * Counts each call of the method <code>run</code> of the inner
	 * class <code>ReleaseThread</code> at the start.
	 */
	static int countReleaseStart = 1;
	
	/**
	 * Counts each run of the method <code>run</code> of the inner
	 * class <code>ReleaseThread</code> at the end.
	 */
	static int countReleaseEnd = 1;	
		
	/**
	 * Client enabled in special to create, update, delete 
	 * a Record.
	 */
	static InterfaceClient_Admin client = null;
	
	/**
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;
	
	/**
	 * Stores the criteria.
	 */
	static String[] criteriaExists = {""};
	
	/**
	 * Stores the number of loops have been run
	 * before displaying messages on the command line.
	 */
	static long loopsToNesxtDisplay = 0;
	
	/**
	 * The ipaddress of the server.
	 */
	final String ip = "";
	
	/**
	 * The port number of the server.
	 */
	final String port = "";
	
	/**
	 * Displays an introduction to the monitor
	 * of progress.
	 */
	private static String progressText = "Z_R_TestAdminOneCon"
			+ "\n2 clicks - stops the program"
			+ "\nwheel rotation - moves the dialog"
			+ "\nProgress(||||||||||): ";

	/**
	 * Static initializer to settle the access to the database file.
	 */
	static {
    	boolean idIsEntered = true;
    	while(idIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_R_TestAdminOneCon starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"10000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestAdminOneCon - please enter the number of iterations!", 
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
        			"Z_R_TestAdminOneCon starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"1000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestAdminOneCon - please enter the number of iterations!", 
    					 		"Z_TestAdminLocal (ADMIN)",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			loopsToNesxtDisplay = Long.parseLong(displayIterations);
    			loopsAreEntered = false;
    		}
    	}    	
		
		String ip = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.SERVER_IP_ADDRESS);
		String port = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.SERVER_PORT);		
		
		// enter ip and port
		final String input = JOptionPane.showInputDialog(
		null, "Z_R_TestAdminOneCon" 
		+ "\nYou will be connected to: \n"
		+ ip + ";" + port
		+ "\n\nYou have to use a semicolon (;) to spilt ipaddress "
		+ "and the port number."
		+ "\n\nPlease enter the ipaddress and port number:",
		ip + ";" + port);
		if(input != null){
				final String[] tokens = input.split(";");
				ip = tokens[0];
				port = tokens[1];
		}else{
			System.exit(0);
		}
    	// end enter ip and port
		
		
		final String inputAll = JOptionPane.showInputDialog(null,
    			"Z_R_TestAdminOneCon\n"
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
				+ "\nUpdate-1: D_A_R1_UP_1_ + count; Smalville; 2; Roofing; 75; "
				+ "\nUpdate-2: H_A_R1_UP_2_ + count; Myville; 23; Roofing; 85; "
				+ "\nCreate: F_A_R1_CR_ + count; Myville; 14; Heating; 85; "
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
		
		System.out.println("Z_R_TestAdminOneCon has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + loopsToNesxtDisplay
    			+ "\nYour input: " + inputAll);
		
		final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestAdminOneCon (ADMIN) "
				 		+ "starts."
				 		+ "\nRole: ADMIN" 
				 		+ "\nIP: " + ip + " - " 
				 		+ "Port: " + port
				 		+ "\nIterations: " + iterations
				 		+ "\nLoops before display message: " 
				 		+ loopsToNesxtDisplay
		    			+ "\nYour input: " + inputAll
				 		, 
				 		"Z_R_TestAdminOneCon",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		
		
		boolean conFailed = false;	
		try {
			client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
							ip, port);
		}  catch (final UnknownHostException e1) {
			System.out.println("Z_R_TestAdminOneCon, main, "
					+ "Exception: " + e1.getLocalizedMessage());
			conFailed = true;
		} catch (final IOException e1) {
			System.out.println("Z_R_TestAdminOneCon, main, "
					+ "Exception: " + e1.getLocalizedMessage());
			conFailed = true;
		}
		if(conFailed){
			JOptionPane.showMessageDialog(null,
					"ATTENTION: Z_R_TestAdminOneCon, connection failed!"
					+ "\nProgram stops!");
			System.exit(0);
		}
		
    }
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_R_TestAdminOneCon(){
		// 
	}
 
	
	
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
    	try{
    		new Z_R_TestAdminOneCon().startTests();
    	}catch(final OutOfMemoryError e){ final Date d = new Date(System.currentTimeMillis());
    		final SimpleDateFormat sdf = 
    			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
    		final String formattedDate = sdf.format(d);
    		System.out.println("Z_R_TestAdminOneCon, OOMError: " + formattedDate);
    		e.printStackTrace();
    	}        
    }
 
    /**
     * Starts the threads in a loop.
     */
    private void startTests() {
    	StartMonitor.disposeStartMonitor();
    	StartMonitor.setTestPane(progressText);
    	StartMonitor.setVisi();
    	final long tenthOfIterations = iterations/10;
		String bars = "|";
        try {
        	for (int i = 0; i < iterations; i++) {
        		if(countRuns % tenthOfIterations == 0){
        			StartMonitor.setTestPane(progressText + bars);
        			StartMonitor.setVisi();
        			bars += "|";
        		}
        		
            	countRuns++;
            	
            	final Thread bookThread = new BookThread();
            	bookThread.start();
            	bookThread.join();  
            	
            	final Thread releaseThread = new ReleaseThread();
            	releaseThread.start();
            	releaseThread.join();              	
            	
            	final Thread updatingRandom = new UpdatingRandomRecordThread();
            	updatingRandom.start();
            	updatingRandom.join();            	
            	
            	final Thread updatingRecord1 = new UpdatingRecord1Thread();
            	updatingRecord1.start();
            	updatingRecord1.join();            	
            	
                final Thread creatingRecord = new CreatingRecordThread();
                creatingRecord.start();
            	creatingRecord.join();                
                 
                final Thread deletingRecord = new DeletingRecord1Thread();
                deletingRecord.start();
            	deletingRecord.join();            	
            	
				final Thread findingRecords = new FindingRecordsThread();
				findingRecords.start();
            	findingRecords.join();				
                
                final Thread findingByRecords = new FindingByRecordsThread();
                findingByRecords.start();
            	findingByRecords.join();
            	
            	final Thread getDValidRecs = new GetValidRecsThread();
            	getDValidRecs.start();
            	getDValidRecs.join();
            	
            	final Thread getLoRecords = new GetLockedThread();
            	getLoRecords.start();
            	getLoRecords.join();            	
            }
            //ensures run client will be visible on the command line.
            Thread.sleep(1000);            
        } catch (final Exception e) {
            System.out.println("start " + e.getMessage());
        }
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
        System.out.println("Z_R_TestAdminOneCon has run to the end."
        		+ "\nConnection: " + client.toString()
        		+ "Done, Loops: " + countRuns 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd 
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
        		+ "\n- getDeleted start: " + countGetValidStart
        		+ " - getDeleted end: " + countGetValidEnd   
        		+ "\n- getLocked start: " + countGetLoStart
        		+ " - getLocked end: " + countGetLoEnd 
        		+ "\n- getRented start: " + countGetRentStart
        		+ " - getRented end: " + countGetRentEnd 
        		+ "\n- " + formattedDate);
        
        
        final JOptionPane alert = new JOptionPane("Z_R_TestAdminOneCon has run to the end."
        		+ "\nConnection: " + client.toString()
        		+ "\nDone, Loops: " + countRuns 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd 
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
        		+ "\n- getDeleted start: " + countGetValidStart
        		+ " - getDeleted end: " + countGetValidEnd   
        		+ "\n- getLocked start: " + countGetLoStart
        		+ " - getLocked end: " + countGetLoEnd 
        		+ "\n- getRented start: " + countGetRentStart
        		+ " - getRented end: " + countGetRentEnd
        		+ "\n- " + formattedDate
        		
        		, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		final JDialog dialog = alert.createDialog(null, "B & S - Z_R_TestAdminOneCon");
		dialog.setVisible(true);
		System.exit(0);//Must if graphics in use        
    }
    
    /**
     * An object of this class is a <code>Thread</code>.
     * In its <code>run</code> method a Record will be booked.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class BookThread extends Thread {    	
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public BookThread() {
    		//
      	}
 
    	/**
    	 * Overridden <code>run</code> method. Rents a Record.
    	 */
        @Override
        public void run() {
			countRentStart++;
			final int recNo = (int) (Math.random() * 28) + 1;
			long cookie = 0;
			boolean ok = false;
			try {
				cookie = client.setRecordLocked(recNo);
				ok = client.reserveRecord(recNo, 6 + countRentStart, cookie);
			} catch (final Exception e) {
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println("Z_R_TestAdminOneCon BOOK, count: " + countRentStart 
							+ " - " + e.getMessage());
					//new
					if(e instanceof ConnectException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
	        					Z_R_TestAdminOneCon.this.port, 
	        					"Z_R_TestAdminOneCon - BOOKING",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof BindException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
	        					Z_R_TestAdminOneCon.this.port, 
	        					"Z_R_TestAdminOneCon - BOOKING",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof SocketException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip,
	        					Z_R_TestAdminOneCon.this.port, 
	        					"Z_R_TestAdminOneCon - BOOKING",
	        					countRuns, countRentEnd);
	        		}
					
				}
			} finally {
				try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestAdminOneCon BOOK: " 
	            			+ countRentStart 
								+ " - " + e.getMessage());
					}
				}
			}
			countRentEnd++;
			if ((countRentEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - Rent run END, rent-start: " 
						+ countRentStart + " - rent-end: " + countRentEnd
						+ " - loops: " + countRuns + " - ok: " + ok);
			}
        }
    } 
    
    
    
    /**
     * An object of this class is a <code>Thread</code>.
     * In its <code>run</code> method a Record will be released of rented state.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class ReleaseThread extends Thread {
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
		public ReleaseThread() {
			//
	    	}
 
    	/**
    	 * Overridden <code>run</code> method. Releases a Record.
    	 */
        @Override
        public void run() {        	
			countReleaseStart++;
			final int recNo = (int) (Math.random() * 28) + 1;
			long cookie = 0;
			boolean ok = false;
			try {
				cookie = client.setRecordLocked(recNo);
				ok = client.releaseRecord(recNo, cookie);
			} catch (final Exception e) {
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println("Z_R_TestAdminOneCon - RELEASE, count: " 
            			+ countRuns + " - " + e.getMessage());
				}
			} finally {
				try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e1) {
					if( !((e1 instanceof SecurityException)
	            			^ (e1 instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestAdminOneCon - RELEASE,"
								+ " count: " 
								+ countRuns + " - " + e1.getMessage());
						
						if(e1 instanceof ConnectException){
                			displayDialog(e1.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e1 instanceof BindException){
                			displayDialog(e1.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e1 instanceof SocketException){
                			displayDialog(e1.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - RELEASE",
                					countRuns, countReleaseEnd);
                		}
						
					}
					
				}
			}
			countReleaseEnd++;
			if ((countReleaseEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - RELEASE run END, release-start: " 
						+ countReleaseStart + " - release-end: "
						+ countReleaseEnd + " - loops: " 
						+ countRuns + " - ok: " + ok 
						+ " - recNo: " + recNo);
			}			
        }
    } 
    
    
    /**
     * An object of this class is a <code>Thread</code>.
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
			final Record recordUpdate = new Record();
			recordUpdate.setName("D_A_R1_UP_1_" + countRuns);
			recordUpdate.setCity("Smallville");
			recordUpdate.setNumberOfStaff(2);
			recordUpdate.setTypesOfWork("Roofing");
			recordUpdate.setHourlyChargeRate("$75.00");
			recordUpdate.setOwner("");
			final int recNo = (int)(Math.random()* 28) + 1;
			boolean ok = false;
			long cookie = 0;
			try {
				cookie = client.setRecordLocked(recNo);				
				ok = client.modifyRecord(recordUpdate, recNo, cookie);
			} catch (final Exception e) {
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println("Z_R_TestAdminOneCon - U 1 run: " 
							+ countUp1End + " - " + e.getLocalizedMessage());
				}				
			}finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestAdminOneCon - UPDATE 1 " 
	            			+ e.getLocalizedMessage());
						if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 1",
                					countRuns, countUp1End);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 1",
                					countRuns, countUp1End);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 1",
                					countRuns, countUp1End);
                		}						
					}					
				} 
            }
			countUp1End++;
			if ((countUp1End % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - UPDATE 1, start run: " 
						+ countUp1Start 
						+ " - run end: " + countUp1End 
						+ " - loops: " + countRuns
						+ " - ok: " + ok);
			}			
        }//run end
    }
 
    /**
     * An object of this class is a <code>Thread</code>.
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
            room.setName("H_A_R1_UP_2_" + countRuns);
            room.setCity("Myville");
            room.setNumberOfStaff(23);
            room.setTypesOfWork("Roofing");
            room.setHourlyChargeRate("$85.00");
            room.setOwner("");            
            final int recNo = (int)(Math.random()* 28 ) + 1;
            long cookie = 0;
            boolean updateOk = false;
            try {            	
            	cookie = client.setRecordLocked(recNo);
            	updateOk = client.modifyRecord(room, recNo, cookie);
            } catch (final Exception e) {
            	if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
            		System.out.println("Z_R_TestAdminOneCon - UPDATE 2 - " + e);
            	}
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestAdminOneCon - UPDATE 2 - " 
	            			+ e.getLocalizedMessage());
						if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 2",
                					countRuns, countUp2End);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 2",
                					countRuns, countUp2End);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - UPDATE 2",
                					countRuns, countUp2End);
                		}
					}					
				} 
            }
            countUp2End++;
			if ((countUp2End % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - UPDATE 2, start: " + countUp2Start
        				+ " - run end: "+ countUp2End 
        				+ " - loops: " + countRuns
        				+ " - ok: " + updateOk);
			}            
        }
    }
 
    /**
     * An object of this class is a <code>Thread</code>.
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
        	final Record rec = new Record();
            rec.setName("F_A_R1_CR_" + countCreateStart);
            rec.setCity("Myville");
            rec.setNumberOfStaff(14);
            rec.setTypesOfWork("Heating");
            rec.setHourlyChargeRate("$85.00"); 
            rec.setRecNo((int)(Math.random()* 28) + 1);
            long recNo = 0;
            try {
            	recNo = client.addRecord(rec);
            } catch (final Exception e) {
            	if( !(e instanceof DuplicateKeyException)) {
            		System.out.println("Z_R_TestAdminOneCon - CREATE - " 
            				+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - CREATE",
            					countRuns, countCreateEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - CREATE",
            					countRuns, countCreateEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - CREATE",
            					countRuns, countCreateEnd);
            		}            		
            	}				
            }
            countCreateEnd++;
			if ((countCreateEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - CREATE, start: " 
						+ countCreateStart 
        				+ " - run end: "+ countCreateEnd 
        				+ " - loops: " + countRuns
        				+ " - recNo: " + recNo);
			}            
        }
    }
 
    /**
     * An object of this class is a <code>Thread</code>.
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
            	if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
            		System.out.println("Z_R_TestAdminOneCon - DELETE - " 
            			+ e.getLocalizedMessage());
            	}				
            }finally{
            	try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestAdminOneCon - DELETE - " 
	            			+ e.getLocalizedMessage());
						if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - DELETE",
                					countRuns, countDelEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - DELETE",
                					countRuns, countDelEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
                					Z_R_TestAdminOneCon.this.port, 
                					"Z_R_TestAdminOneCon - DELETE",
                					countRuns, countDelEnd);
                		}
					}
				} 
            }
            countDelEnd++;
			if ((countDelEnd % loopsToNesxtDisplay) == 0) {
				 System.out.println("Z_R_TestAdminOneCon - DELETE, start: " + countDelStart 
	        				+ " - run end: "+ countDelEnd 
	        				+ " - loops: " + countRuns
	        				+ " - recNo: " + recNo
	        				+ " - del: " + delOk);
			}           
        }
    }
    
 
    /**
     * An object of this class is a <code>Thread</code>.
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
            } catch (final Exception e) {
            	if( !(e instanceof RecordNotFoundException)) {
            		System.out.println("Z_R_TestAdminOneCon - FindingRecords: " 
            				+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		}     
            	}
            }
            countGetRecEnd++;
			if ((countGetRecEnd % loopsToNesxtDisplay) == 0) {
				 System.out.println("Z_R_TestAdminOneCon - FindingRecords, start: " + countGetRecStart
	        				+ " - run end: "+ countGetRecEnd 
	        				+ " - loops: " + countRuns
	        				+ " - rec: " + r);
			}           
        }
    }
    
    /**
     * An object of this class is a <code>Thread</code>.
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
            	if( !(e instanceof RecordNotFoundException)) {
            		System.out.println("Z_R_TestAdminOneCon - "
            				+ "FindingByRecords - " 
            				+ e.toString());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		} else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
            					Z_R_TestAdminOneCon.this.port, 
            					"Z_R_TestAdminOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		}
            	}            	
            }
            countFindByEnd++;
			if ((countFindByEnd % loopsToNesxtDisplay) == 0) {
				 System.out.println("Z_R_TestAdminOneCon - FindingByRecords, start: " + countFindByStart
	        				+ " - run end: "+ countFindByEnd 
	        				+ " - loops: " + countRuns
	        				+ " - results: " + results.length
	        				);
			}           
        }
    }
    
    
    /**
     * An object of this class is a <code>Thread</code>.
     * In its <code>run</code> method searches for all valid Records.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class GetValidRecsThread extends Thread {
 
        /**
         * No-arg constructor to create an object of this class.
         */
        public GetValidRecsThread() {
			//  
		}
        
        /**
    	 * Overridden <code>run</code> method. Finds all valid Records.
    	 */
		@Override
		public void run() {
			List<Record> result = new ArrayList<Record>();
            try{
            	countGetValidStart++;                
               result = client.getAllValidRecords();
            } catch (final Exception e) {            	
            	System.out.println("Z_R_TestAdminOneCon - GetValidRecsThread - " 
            			+ e.getLocalizedMessage());
            	if(e instanceof ConnectException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetValidRecsThread",
        					countRuns, countGetValidEnd);
        		}else if(e instanceof BindException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetValidRecsThread",
        					countRuns, countGetValidEnd);
        		}else if(e instanceof SocketException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetValidRecsThread",
        					countRuns, countGetValidEnd);
        		}  
            	
            }
            countGetValidEnd++;
			if ((countGetValidEnd % loopsToNesxtDisplay) == 0) {
				 System.out.println("Z_R_TestAdminOneCon - GetValidRecsThread, start: " 
						 	+ countGetValidStart
	        				+ " - run end: "+ countGetValidEnd 
	        				+ " - loops: " + countRuns
	        				+ " - results: " + result.size()
	        				);
			}           
        }
    }
    
    /**
     * An object of this class is a <code>Thread</code>.
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
    	 * Overridden <code>run</code> method. Finds Records by criteria
    	 * and reads the first Record of the result.
    	 */
		@Override
		public void run() {
			Set<Long> results = new HashSet<Long>();	
            try{
            	countGetLoStart++;
                
               results = client.getLocked();
               results.size();
            } catch (final Exception e) {
            	System.out.println("Z_R_TestAdminOneCon - GetLocked - " 
            			+ e.getLocalizedMessage());
            	if(e instanceof ConnectException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		}else if(e instanceof BindException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		}else if(e instanceof SocketException){
        			displayDialog(e.getMessage(), Z_R_TestAdminOneCon.this.ip, 
        					Z_R_TestAdminOneCon.this.port, 
        					"Z_R_TestAdminOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		}
            }
            countGetLoEnd++;
			if ((countGetLoEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestAdminOneCon - GetLocked, start: " 
						+ countGetLoStart
        				+ " - run end: "+ countGetLoEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.size()
        				);
			}            
        }
    }
    
    /**
     * It displays a dialog to exit the application by a call to 
     * 'System.exit(0)' or continues.
     * 
     * @param exception The exception message.
     * @param ip The ipaddress of the server.
     * @param port The port number of the server.
     * @param operation Describes the proceeded operation.
     * @param iterStart The counted iteration at the start of the thread. 
     * @param iterEnd The counted iteration at the start of the thread.
     */
    static void displayDialog(final String exception, 
    		final String ip, final String port, final String operation,
    		final int iterStart, final int iterEnd){
    	final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestAdminOneCon"
						+ "\n" + operation
				 		+ "\nException: " + exception 
				 		+ "\nIP: " + ip + " - " 
				 		+ "Port: " + port
				 		+ "\nloops start: " + iterStart
				 		+ "\nloops end: " + iterEnd  
				 		+ "\nTry again or exit:"
				 		, 
				 		"Z_R_TestAdminOneCon",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    }
}

