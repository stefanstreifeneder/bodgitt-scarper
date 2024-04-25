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

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.SavedConfiguration;
import suncertify.gui.StartMonitor;
 
/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roberto Perillo(
 * https://coderanch.com/t/427863/java-developer-OCMJD/certification/
 * Tests-Data-class-locking-mechanism).<br>
 * I revised the original class according to my public interfaces
 * <code>suncertify.db.InterfaceClient_ReadOnly</code>,
 * <code>suncertify.db.InterfaceClient_LockPermission</code>,
 * <code>suncertify.db.InterfaceClient_Buyer</code>.<br>
 * <br>
 * The class can only applied in a network environment. 
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
 * @author stefan.streifeneder@gmx.de
 */
public class Z_R_TestBuyerOneCon {
	
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
	static int countGetDelStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetValidRecsThread</code> has reached the end.
	 */
	static int countGetDelEnd = 0;
	
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
	 * class <code>BookThread</code> at the start.
	 */
	static int countRent_2_Start = 1;	
	
	/**
	 * Counts each run of the method <code>run</code> of the inner
	 * class <code>BookThread</code> at the end.
	 */
	static int countRent_2_End = 1;
	
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
	static InterfaceClient_Buyer client = null;
	
	/**
	 * Stores the number of loops have been run
	 * before displaying messages on the command line.
	 */
	static long loopsToNesxtDisplay = 0;
	
	/**
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;
	
	/**
	 * Stores the criteria.
	 */
	static String[] criteriaExists = {""};
	
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
	private static String progressText = "Z_R_TestBuyerOneCon"
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
        			"Z_R_TestBuyerOneCon starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"10000");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestBuyerOneCon - please enter the number of iterations!", 
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
        			"Z_R_TestBuyerOneCon starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"1000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestBuyerOneCon - please enter the number of iterations!", 
    					 		"Z_R_TestBuyerOneCon (ADMIN)",
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
		null, "Z_R_TestBuyerOneCon (BUYER)" 
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
    			"Z_R_TestBuyerOneCon\n"
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
				+ "\nRent-1-ID: 6 + count"  
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
    	
    	
    	System.out.println("Z_R_TestBuyerOneCon has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour bumber of iterations: " + iterations
    			+ "\nIterations to display: " + loopsToNesxtDisplay
    			+ "\nYour input: " + inputAll);
    	
    	final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestBuyerOneCon (BUYER) "
				 		+ "starts."
				 		+ "\nRole: ADMIN" 
				 		+ "\nIP: " + ip + " - " 
				 		+ "Port: " + port
				 		+ "\nIterations: " + iterations
				 		+ "\nLoops before display message: " 
				 		+ loopsToNesxtDisplay
		    			+ "\nYour input: " + inputAll
				 		, 
				 		"Z_R_TestBuyerOneCon",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}	
		
		boolean conFailed = false;	
		try {
			client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
							ip, port);
		}  catch (final UnknownHostException e1) {
			System.out.println("Z_R_TestBuyerOneCon, main, "
					+ "Exception: " + e1.getLocalizedMessage());
			conFailed = true;
		} catch (final IOException e1) {
			System.out.println("Z_R_TestBuyerOneCon, main, "
					+ "Exception: " + e1.getLocalizedMessage());
			conFailed = true;
		}
		if(conFailed){
			JOptionPane.showMessageDialog(null,
					"ATTENTION: Z_R_TestBuyerOneCon, connection failed!"
					+ "\nProgram stops!");
			System.exit(0);
		}
    }
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_R_TestBuyerOneCon(){
		// 
	}
 
	
	
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String [] args) {
        try{
        	 new Z_R_TestBuyerOneCon().startTests();
    	}catch(final OutOfMemoryError e){ final Date d = new Date(System.currentTimeMillis());
    		final SimpleDateFormat sdf = 
    			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
    		final String formattedDate = sdf.format(d);
    		System.out.println("Z_R_TestBuyerOneCon, OOMError: " + formattedDate);
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
            	
            	final Thread book_2_Thread = new BookThread_2();
            	book_2_Thread.start();
            	book_2_Thread.join();
            	
            	final Thread releaseThread = new ReleaseThread();
            	releaseThread.start();
            	releaseThread.join();  
            	
            	final Thread findingRecords = new FindingRecordsThread();
				findingRecords.start();
            	findingRecords.join();				
                
                final Thread findingByRecords = new FindingByRecordsThread();
                findingByRecords.start();
            	findingByRecords.join();
            	
            	final Thread getDelRecords = new GetValidRecsThread();
            	getDelRecords.start();
            	getDelRecords.join();
            	
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
        System.out.println("Z_R_TestBuyerOneCon has run to the end."
        		+ "\nConnection: " + client.toString()
        		+ "Done, Loops: " + countRuns 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd 
        		+ "\n- rent start: " + countRent_2_Start
        		+ " - rent end: " + countRent_2_End
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd
        		+ "\n- find start: " + countGetRecStart
        		+ " - find end: " + countGetRecEnd
        		+ "\n- findBy start: " + countFindByStart
        		+ " - findBy end: " + countFindByEnd 
        		+ "\n- getDeleted start: " + countGetDelStart
        		+ " - getDeleted end: " + countGetDelEnd   
        		+ "\n- getLocked start: " + countGetLoStart
        		+ " - getLocked end: " + countGetLoEnd 
        		+ "\n- getRented start: " + countGetRentStart
        		+ " - getRented end: " + countGetRentEnd
        		+ "\n- " + formattedDate);
        
        
        final JOptionPane alert = new JOptionPane("Z_R_TestBuyerOneCon has run to the end."
        		+ "\nConnection: " + client.toString()
        		+ "\nDone, Loops: " + countRuns 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd 
        		+ "\n- rent start: " + countRent_2_Start
        		+ " - rent end: " + countRent_2_End
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd 
        		+ "\n- find start: " + countGetRecStart
        		+ " - find end: " + countGetRecEnd
        		+ "\n- findBy start: " + countFindByStart
        		+ " - findBy end: " + countFindByEnd 
        		+ "\n- getDeleted start: " + countGetDelStart
        		+ " - getDeleted end: " + countGetDelEnd   
        		+ "\n- getLocked start: " + countGetLoStart
        		+ " - getLocked end: " + countGetLoEnd 
        		+ "\n- getRented start: " + countGetRentStart
        		+ " - getRented end: " + countGetRentEnd 
        		+ "\n- " + formattedDate
        		
        		, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		final JDialog dialog = alert.createDialog(null, 
				"B & S - Z_R_TestBuyerOneCon");
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
				ok = client.reserveRecord(recNo, 6 + countRentStart
						, cookie);
			} catch (final Exception e) {
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println("Z_R_TestBuyerOneCon - BOOKING 1: " + countRentStart 
							+ " - " + e.getMessage());
					if(e instanceof ConnectException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestBuyerOneCon - BOOKING 1",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof BindException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestBuyerOneCon - BOOKING 1",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof SocketException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip,
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestBuyerOneCon - BOOKING 1",
	        					countRuns, countRentEnd);
	        		}
				}
			} finally {
				try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestBuyerOneCon - BOOKING 1,"
								+ " unlock: " 
								+ countRentStart + " - " + e.getMessage());
					}
				}
			}
			countRentEnd++;
			if ((countRentEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - BOOKING 1, rent-start: " 
						+ countRentStart + " - rent-end: " + countRentEnd
						+ " - loops: " + countRuns + " - ok: " + ok);
			}
        }
    } 
    
    /**
     * An object of this class is a <code>Thread</code>.
     * In its <code>run</code> method a Record will be booked.
     * 
     * @author stefan.streifeneder@gmx.de
     *
     */
    private class BookThread_2 extends Thread {    	
    	
    	/**
    	 * No-arg constructor to create an object of this class.
    	 */
    	public BookThread_2() {
    		//
      	}
 
    	/**
    	 * Overridden <code>run</code> method. Rents a Record.
    	 */
        @Override
        public void run() {
			countRent_2_Start++;
			final int recNo = (int) (Math.random() * 28) + 1;
			long cookie = 0;
			boolean ok = false;
			try {
				cookie = client.setRecordLocked(recNo);
				ok = client.reserveRecord(recNo, 2 + countRent_2_Start, cookie);
			} catch (final Exception e) {
				if( !((e instanceof SecurityException)
            			^ (e instanceof RecordNotFoundException))) {
					System.out.println("Z_R_TestBuyerOneCon - BOOKING 2: " 
            			+ countRent_2_Start 
							+ " - " + e.getMessage());
					if(e instanceof ConnectException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestBuyerOneCon - BOOKING 2",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof BindException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestAdminOneCon - BOOKING 2",
	        					countRuns, countRentEnd);
	        		}else if(e instanceof SocketException){
	        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip,
	        					Z_R_TestBuyerOneCon.this.port, 
	        					"Z_R_TestAdminOneCon - BOOKING 2",
	        					countRuns, countRentEnd);
	        		}
				}
			} finally {
				try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e) {
					if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestBuyerOneCon - BOOKING 2: " 
								+ countRent_2_Start + " - " + e.getMessage());
					}
				}
			}
			countRent_2_End++;
			if ((countRent_2_End % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - BOOKING 2, rent-start: " 
						+ countRent_2_Start + " - rent-end: " + countRent_2_End
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
					System.out.println("Z_R_TestBuyerOneCon - RELEASE: " + countRuns 
							+ " - " + e.getMessage());
					if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - RELEASE",
            					countRuns, countReleaseEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - RELEASE",
            					countRuns, countReleaseEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - RELEASE",
            					countRuns, countReleaseEnd);
            		}
				}
			} finally {
				try {
					client.setRecordUnlocked(recNo, cookie);
				} catch (final Exception e1) {
					if( !((e1 instanceof SecurityException)
	            			^ (e1 instanceof RecordNotFoundException))) {
						System.out.println("Z_R_TestBuyerOneCon - RELEASE: " 
	            			+ countRuns + " - " + e1.getMessage());
					}
					
				}
			}
			countReleaseEnd++;
			if ((countReleaseEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - RELEASE, "
						+ "release-start: " + countReleaseStart + " - release-end: "
						+ countReleaseEnd + " - loops: " 
						+ countRuns + " - ok: " + ok 
						+ " - recNo: " + recNo);
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
            		System.out.println("Z_R_TestBuyerOneCon - FindingRecords: " 
            				+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingRecords",
            					countRuns, countGetRecEnd);
            		} 
            	}				
            }
            countGetRecEnd++;
			if ((countGetRecEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - FindingRecords,"
						+ " start: " + countGetRecStart
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
            		System.out.println("Z_R_TestBuyerOneCon - FindingByRecords: " 
            				+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		} else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
            					Z_R_TestBuyerOneCon.this.port, 
            					"Z_R_TestBuyerOneCon - FindingByRecords",
            					countRuns, countFindByEnd);
            		}
            	}            	
            }
            countFindByEnd++;
			if ((countFindByEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - FindingByRecords, start: " + countFindByStart
        				+ " - run end: "+ countFindByEnd 
        				+ " - loops: " + countRuns
        				+ " - results: " + results.length
        				);
			}
        }
    }
    
    
    /**
     * An object of this class is a <code>Thread</code>.
     * In its <code>run</code> method searches for all deleted Records.
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
    	 * Overridden <code>run</code> method. Finds Records by criteria
    	 * and reads the first Record of the result.
    	 */
		@Override
		public void run() {
			List<Record> result = new ArrayList<Record>();
            try{
            	countGetDelStart++;                
                result = client.getAllValidRecords();
            } catch (final Exception e) {            	
            	System.out.println("Z_R_TestBuyerOneCon - GetDelRecords: " + e.getLocalizedMessage());
            	
            	if(e instanceof ConnectException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetDelRecords",
        					countRuns, countGetDelEnd);
        		}else if(e instanceof BindException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetDelRecords",
        					countRuns, countGetDelEnd);
        		}else if(e instanceof SocketException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetDelRecords",
        					countRuns, countGetDelEnd);
        		} 
            }
            countGetDelEnd++;
			if ((countGetDelEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - GetDelRecords, "
						+ "start: " + countGetDelStart
        				+ " - run end: "+ countGetDelEnd 
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
            	System.out.println("GetLocked: " 
            			+ e.getLocalizedMessage());
            	if(e instanceof ConnectException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		}else if(e instanceof BindException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		}else if(e instanceof SocketException){
        			displayDialog(e.getMessage(), Z_R_TestBuyerOneCon.this.ip, 
        					Z_R_TestBuyerOneCon.this.port, 
        					"Z_R_TestBuyerOneCon - GetLocked",
        					countRuns, countGetLoEnd);
        		} 
            }
            countGetLoEnd++;
			if ((countGetLoEnd % loopsToNesxtDisplay) == 0) {
				System.out.println("Z_R_TestBuyerOneCon - GetLocked, "
						+ "start: " + countGetLoStart
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
				"Z_R_TestBuyerOneCon - GetLocked"
						+ "\n" + operation
				 		+ "\nException: " + exception 
				 		+ "\nIP: " + ip + " - " 
				 		+ "Port: " + port
				 		+ "\nloops start: " + iterStart
				 		+ "\nloops end: " + iterEnd  
				 		+ "\nTry again or exit:"
				 		, 
				 		"Z_R_TestBuyerOneCon - GetLocked",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    }
}

