package suncertify.test;

import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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


/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roel De Nijs
 * (see: https://coderanch.com/t/484954/certification/
 * Test-business-service).<br> 
 * I revised the original class according to my public interfaces:
 * <br> <code>suncertify.db.InterfaceClient_ReadOnly</code>
 * <br> <code>suncertify.db.InterfaceClient_LockPermission</code>
 * <br> <code>suncertify.db.InterfaceClient_Buyer</code>
 * <br> <code>suncertify.db.InterfaceClient_Seller</code>
 * <br> <code>suncertify.db.InterfaceClient_Admin</code>
 * <br>
 * <br> The class starts the inner classes, which are subtype 
 * <code>Thread</code>, in the method <code>startTests</code>
 * in a loop.
 * <br> The <code>run</code> method of the inner classes
 * are starting another loop. Within an
 * iteration the program establishes a connection to the server
 * and calls methods of the public interface. Referring
 * to the iterations the program connects to the server
 * some thousand times at least.
 * <br> 
 * <br> The inner classes proceed functionalities accordingly to
 * the public interface provided to an Admin client.
 * <br>
 * <br> This program should only used in a network environment.
 * The access is done via ipaddress and port.
 * <br> This program is not appropriate to run local.<br>
 * The setting of the connection data (ipaddress and port) will be done
 * in the static initializer.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class Z_R_TestAdminManyCons {
	
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
	 * <code>GetMemoryThread</code>.
	 */
	static int countGetMemeoryStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetMemoryThread</code> has reached the end.
	 */
	static int countGetMemoryEnd = 0;
	
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
	 * Stores the number of loops have been run
	 * before displaying messages on the command line.
	 */
	static long loopsToNextDisplay = 0;
	
	/**
	 * Stores the number loops,
	 * done
	 * of each thread.
	 */
	static long numberOfInternalLoops = 0;
	
	
	/**
	 * Stores the criteria.
	 */
	static String[] criteriaExists = {""};
	
	/**
	 * Stores the ipaddress of the server.
	 */
	static String ip = 
			SavedConfiguration.getSavedConfiguration().getParameter(
					SavedConfiguration.SERVER_IP_ADDRESS);
	
	/**
	 * Stores the port number of the server.
	 */
	static String port = 
			SavedConfiguration.getSavedConfiguration().getParameter(
					SavedConfiguration.SERVER_PORT);	
	
	/**
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;


	/**
	 * Private noarg-constructor.
	 */
	private Z_R_TestAdminManyCons(){
		// 
	}
 
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {    	
    	iterations = 100;
    	numberOfInternalLoops = 50;
    	loopsToNextDisplay = 100;
    	
    	//iterations
    	boolean iterateIsEntered = true;
    	while(iterateIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_R_TestAdminManyCons starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"100");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestAdminManyCons - please enter the number of iterations!", 
    					 		"Z_TestAdminLocal (ADMIN)",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			iterations = Long.parseLong(inputIterations);    			
    			iterateIsEntered = false;
    		}
    	}     	
    	boolean innerLoopIsEntered = true;
    	while(innerLoopIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_R_TestAdminManyCons starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"50");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestAdminManyCons - please enter the number of iterations!", 
    					 		"Z_R_TestAdminManyCons",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			numberOfInternalLoops = Long.parseLong(inputIterations);    			
    			innerLoopIsEntered = false;
    		}
    	}     	
    	boolean loopsAreEntered = true;
    	while(loopsAreEntered){
    		final String displayIterations = JOptionPane.showInputDialog(null,
        			"Z_R_TestAdminManyCons starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"1000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestAdminManyCons - please enter the number of iterations!", 
    					 		"Z_R_TestAdminManyCons",
    					JOptionPane.YES_NO_OPTION);	
    			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
    				System.exit(0);
    			}    			
    		} else{
    			loopsToNextDisplay = Long.parseLong(displayIterations);
    			loopsAreEntered = false;
    		}
    	}
		
		// enter ip and port
		final String input = JOptionPane.showInputDialog(
		null, "Z_R_TestAdminManyCons" 
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
    			"Z_R_TestAdminManyCons\n"
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
				+ "\nUpdate-1: D_A_RX_UP_1_ + count; Smalville; 2; Roofing; 75; "
				+ "\nUpdate-2: H_A_RX_UP_2_ + count; Myville; 23; Roofing; 85; "
				+ "\nCreate: F_A_RX_CR_ + count; Myville; 14; Heating; 85; "
				+ "\nRent-1-ID: 9999"  
				+ "\nRent-2-ID: 1234"
				+ "\n"
				+ "\n");
    	
    	System.out.println("Z_R_TestAdminManyCons has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour input: " + inputAll);
    	
		if (inputAll == null) {
			System.exit(0);			
		}else if (inputAll.equals("")) {
			criteriaExists = new String[]{null};		
		} else{ 
			criteriaExists = inputAll.split(";");
		}
		
		final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestAdminManyCons starts."
				 		+ "\nRole: ADMIN" 
				 		+ "\nIP: " + ip + " - " 
				 		+ "Port: " + port
				 		+ "\nIterations: " + iterations*numberOfInternalLoops
				 		+ "\nLoops before display message: " 
				 		+ loopsToNextDisplay
				 		, 
				 		"Z_R_TestAdminManyCons",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		
		try{
			new Z_R_TestAdminManyCons().startTests();
		}catch(final OutOfMemoryError e){ final Date d = new Date(System.currentTimeMillis());
			final SimpleDateFormat sdf = 
			new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			final String formattedDate = sdf.format(d);
			System.out.println("Z_R_TestAdminManyCons, OOMError: " + formattedDate);
			e.printStackTrace();
		}          
    }
 
    /**
     * Starts the threads in a loop.
     */
    private void startTests() {
        final List<Thread> threads = new ArrayList<Thread>();
        try {
        	
            // create book-threads
        	// Please do not enlarge over 2000 due to local ports
        	// 'Address already in use'
            for (int i = 0; i < iterations; i++) {            	
            	countRuns++;
            	threads.add(new Thread(new BookThread()));
            	threads.add(new Thread(new ReleaseThread()));
            	threads.add(new Thread(new UpdatingRandomRecordThread()));
            	threads.add(new Thread(new UpdatingRecord1Thread()));
            	threads.add(new Thread(new CreatingRecordThread()));
            	threads.add(new Thread(new DeletingRecord1Thread()));
            	threads.add(new Thread(new FindingRecordsThread()));
            	threads.add(new Thread(new FindingByRecordsThread()));
            	threads.add(new Thread(new GetMemoryThread()));
                threads.add(new Thread(new GetLockedThread()));
                threads.add(new Thread(new GetValidRecsThread()));
                
            }//end for
            
            // random order
            Collections.shuffle(threads);
            // start threads
            for (final Thread thread : threads) {
                thread.start();
                thread.join();
            }
            //ensures the last information will be visible on the command line.
            Thread.sleep(1000); 
        } catch (final Exception e) {
            System.out.println(e);
        }     
        final Date d = new Date(System.currentTimeMillis());
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		final String formattedDate = sdf.format(d);
        System.out.println("Z_R_TestAdminManyCons (ADMIN) has run to the end."
        		+ "\nDone, Runs: " + countRuns + " - " 
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
        		+ "\n- getValid start: " + countGetValidStart
        		+ " - getValid end: " + countGetValidEnd
        		+ " -\n " + formattedDate);
        
        final JOptionPane alert = new JOptionPane("Z_R_TestAdminManyCons (ADMIN) has "
        		+ "run to the end."
        		+ "\nDone, Runs: " + countRuns + " - " 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd
        		+ "\n- release start: " + countReleaseStart
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
        		+ " - release end: " + countReleaseEnd
        		+ " -\n " + formattedDate
         		
         		, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
 		final JDialog dialog = alert.createDialog(null, 
 				"Z_R_TestAdminManyCons");
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
    private class BookThread implements Runnable {    	
    	
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
        	
            int i = 0;
            while (i <= numberOfInternalLoops) {          	
            	
            	i++;
            	countRentStart++;     
            	final int recNo = (int)(Math.random()* 28) + 1;
            	long cookie = 0;
            	boolean ok = false;
            	
            	InterfaceClient_Admin data = null;
            	try {            		
            		data = 
            			suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);            		
            		cookie = data.setRecordLocked(recNo);					
					ok = data.reserveRecord(recNo, 10 + countRentStart, cookie);	
            	            	
            	}catch(final Exception e){
            		if( !((e instanceof SecurityException)
                			^ (e instanceof RecordNotFoundException))) {
            			System.out.println("Z_R_TestAdminManyCons count: " 
                        		+ countRentStart + " - " + e.getMessage());
            			if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - BOOKING",
                					countRuns, countRentEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - BOOKING",
                					countRuns, countRentEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - BOOKING",
                					countRuns, countRentEnd);
                		}
            		}            		
            	} finally{
            		try {
            			if(data != null){
            				data.setRecordUnlocked(recNo, cookie);
            			}						
					} catch (final Exception e) {
						if( !((e instanceof SecurityException)
		            			^ (e instanceof RecordNotFoundException))) {
							System.out.println("Z_R_TestAdminManyCons - "
									+ "BOOK Unlock count: " 
	                        		+ countRentStart + " - " + e.getMessage());
						}
					} 
            	}
            	countRentEnd++;
            	if ((countRentEnd % loopsToNextDisplay) == 0) {
            		System.out.println("Z_R_TestAdminManyCons "
            				+ "- Rent Many Connect run END, rent-start: " 
    						+ countRentStart + " - rent-end: " + countRentEnd
    						+ " - loops: " + countRuns + " - ok: " + ok);
           	 	}      		
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
    private class ReleaseThread implements Runnable {
    	
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
        	int i = 0;
            while (i <= numberOfInternalLoops) {
            	countReleaseStart++;
            	i++;
            	final int recNo = (int)(Math.random()* (28 - 1 + 1)) + 1;
            	long cookie = 0;
            	boolean ok = false;
            	InterfaceClient_Admin data = null;
            	try {
            		
            		data = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
            		            		
					cookie = data.setRecordLocked(recNo);
					ok = data.releaseRecord(recNo, cookie);
            	}catch(final Exception e){
            		if( !((e instanceof SecurityException)
                			^ (e instanceof RecordNotFoundException))) {
            			System.out.println("Z_R_TestAdminManyCons - RELEASE - " 
                				+ countRuns + " - " + e.getMessage());
            			if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - RELEASE",
                					countRuns, countReleaseEnd);
                		}
            		}            		
            	}finally{
            		try {
            			if(data != null){
            				data.setRecordUnlocked(recNo, cookie);
            			}
					} catch (final Exception e) {
						if( !((e instanceof SecurityException)
		            			^ (e instanceof RecordNotFoundException))) {
							System.out.println(
									"Z_R_TestAdminManyCons - RELEASE unlock - " 
	                				+ countRuns + " - " + e.getMessage());
						}
					} 
            	}
            	countReleaseEnd++;     
            	if ((countReleaseEnd % loopsToNextDisplay) == 0) {
            		System.out.println("Z_R_TestAdminManyCons - RELEASE"
            				+ " Release Many Connect run END, release-start: " 
            				+ countReleaseStart
                    		+ " - release-end: " + countReleaseEnd
                    		+ " - loops: " + countRuns
                    		+ " - ok: " + ok 
                    		+ " - recNo: " + recNo);
            	}
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
    		int i = 0;
            while (i <= numberOfInternalLoops) {              	
            	i++;
            	countUp1Start++;
        		final Record room = new Record();
    			room.setName("D_A_RX_UP_1_" + countUp1Start);
    			room.setCity("Smallville");
    			room.setNumberOfStaff(2);
    			room.setTypesOfWork("all");
    			room.setHourlyChargeRate("$150.00");
    			room.setOwner("11");
    			final int recNo = (int)(Math.random()* 28) + 1;
    			boolean ok = false;
    			long cookie = 0;
    			InterfaceClient_Admin client = null;
            	try {
            		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
    				
    				cookie = client.setRecordLocked(recNo);
    				ok = client.modifyRecord(room, recNo, cookie);
    			} catch (final Exception e) {
    				if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
    					System.out.println("Z_R_TestAdminManyCons -"
    							+ " U 1 count: " 
        						+ countUp1End + " - " + e.getLocalizedMessage());
    					if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 1",
                					countRuns, countUp1End);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 1",
                					countRuns, countUp1End);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 1",
                					countRuns, countUp1End);
                		}
    				}    				
    			}finally{
                	try {
                		if( client != null){
                			client.setRecordUnlocked(recNo, cookie);            			
                		}					
    				} catch (final Exception e) {
    					if( !((e instanceof SecurityException)
    	            			^ (e instanceof RecordNotFoundException))) {
    						System.out.println("Z_R_TestAdminManyCons - "
    								+ "U 1 exc: " 
        							+ e.getLocalizedMessage());
    					}    					
    				}
                }
    			countUp1End++;
    			if ((countUp1End % loopsToNextDisplay) == 0) {
    				System.out.println("Z_R_TestAdminManyCons "
    						+ "- UP 1 Many Connect END, start run: " + countUp1Start 
    						+ " - run end: " + countUp1End 
    						+ " - loops: " + countRuns
    						+ " - ok: " + ok);
            	}    			
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
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countUp2Start++;
        		final Record room = new Record();
                room.setName("H_A_RX_UP_2_" + countUp2Start);
                room.setCity("Myville");
                room.setNumberOfStaff(2);
                room.setTypesOfWork("all");
                room.setHourlyChargeRate("$150.00");
                room.setOwner("");            
                final int recNo = (int)(Math.random()* (28 - 1 + 1)) + 1;
                long cookie = 0;
                boolean updateOk = false;
                InterfaceClient_Admin client = null;
            	try {
            		
            		client = 
            				suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            						Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);   	    		
                	cookie = client.setRecordLocked(recNo);
                	updateOk = client.modifyRecord(room, recNo, cookie);
                } catch (final Exception e) {
                	if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
                		System.out.println("U 2 Many Connect " + e);
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 2",
                					countRuns, countUp2End);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 2",
                					countRuns, countUp2End);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - UPDATE 2",
                					countRuns, countUp2End);
                		}
                	}    				
                }finally{
                	try {
                		if( client != null){
                			client.setRecordUnlocked(recNo, cookie);
                		}					
    				} catch (final Exception e) {
    					if( !((e instanceof SecurityException)
    	            			^ (e instanceof RecordNotFoundException))) {
    						System.out.println("Z_R_TestAdminManyCons - "
    								+ "U 2 Many Connect " 
        							+ e.getLocalizedMessage());
    					}
    				}
                }
                countUp2End++;
                if ((countUp2End % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - UP 2 , "
                			+ "start: " + countUp2Start
            				+ " - run end: "+ countUp2End 
            				+ " - loops: " + countRuns
            				+ " - ok: " + updateOk);
            	}
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
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countCreateStart++;
        		final Record room = new Record();
                room.setName("F_A_R1_CR_" + countCreateStart);
                room.setCity("Create");
                room.setTypesOfWork("Something");
                room.setNumberOfStaff(8);
                room.setHourlyChargeRate("$950.00");            
                room.setRecNo((int)(Math.random()* 28) + 1);
                long recNo = 0;
                InterfaceClient_Admin client = null;
            	try {
            		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                	
                	recNo = client.addRecord(room);
                } catch (final Exception e) {
                	if( !(e instanceof DuplicateKeyException)) {
                		System.out.println("Z_R_TestAdminManyCons - Create Connect: " 
        						+ e.getLocalizedMessage());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - CREATE",
                					countRuns, countCreateEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - CREATE",
                					countRuns, countCreateEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - CREATE",
                					countRuns, countCreateEnd);
                		}
                	}    				
                }
                countCreateEnd++;
                if ((countCreateEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - "
                			+ "CREATE  Many Connect ENDE run, start: " + countCreateStart 
            				+ " - run end: "+ countCreateEnd 
            				+ " - loops: " + countRuns
            				+ " - recNo: " + recNo);
            	}
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
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countDelStart++;
        		boolean delOk = false;
            	long cookie = 0; 
            	final long recNo = 
            			(long)this.ran.nextInt(20) + 1;
            	InterfaceClient_Admin client = null;
            	try {
            		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                	
                	cookie = client.setRecordLocked(recNo);
    				delOk = client.removeRecord(recNo, cookie);
                } catch (final Exception e) {
                	if( !((e instanceof SecurityException)
	            			^ (e instanceof RecordNotFoundException))) {
                		System.out.println("Z_R_TestAdminManyCons - "
                				+ "Delete Many Connect: " 
        						+ e.getLocalizedMessage());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - DELETE",
                					countRuns, countDelEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - DELETE",
                					countRuns, countDelEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - DELETE",
                					countRuns, countDelEnd);
                		}
                	}    				
                }finally{
                	try {
                		if( client != null){
                			client.setRecordUnlocked(recNo, cookie);
                		}					
    				} catch (final Exception e) {
    					if( !((e instanceof SecurityException)
    	            			^ (e instanceof RecordNotFoundException))) {
    						System.out.println("Z_R_TestAdminManyCons - "
    								+ "Delete Connect: " 
        							+ e.getLocalizedMessage());
    					}    					
    				}
                }
                countDelEnd++;
                if ((countDelEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - "
                			+ "Delete Many Connect END run, start: " + countDelStart 
            				+ " - run end: "+ countDelEnd 
            				+ " - loops: " + countRuns
            				+ " - recNo: " + recNo
            				+ " - del: " + delOk);
            	}                
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
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countGetRecStart++;
        		Record r = null;
    			InterfaceClient_Admin client = null;
            	try {
            		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                	
                	r =  client.getRecord((long)this.ran.nextInt(28) + 1);  
                	r.toString();
                } catch (final Exception e) {
                	if( !(e instanceof RecordNotFoundException)) {
                		System.out.println("Z_R_TestAdminManyCons - "
                				+ "FindingRecords Many Connect: " 
        						+ e.getLocalizedMessage());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}                		
                	}    				
                }
                countGetRecEnd++;
                if ((countGetRecEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - "
                			+ "FindingRecords Many Connect END run, start: " 
                			+ countGetRecStart
            				+ " - run end: "+ countGetRecEnd 
            				+ " - loops: " + countRuns
            				+ " - rec: " + r);		
            	}                	
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
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	long[] results = new long[]{};
    			InterfaceClient_Admin client = null;
            	try {        		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                   results = client.findByFilter(criteriaExists);
                } catch (final Exception e) {
                	if( !(e instanceof RecordNotFoundException)) {
                		System.out.println("Z_R_TestAdminManyCons - FindingByRecords Many Connect: " 
                    			+ e.toString());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		} else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
                					Z_R_TestAdminManyCons.port, 
                					"Z_R_TestAdminManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		}               		
                	}
                }
                countFindByEnd++;
                if ((countFindByEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - FindBy,"
                			+ " start: " + countFindByStart
            				+ " - run end: "+ countFindByEnd 
            				+ " - loops: " + countRuns
            				+ " - results: " + results.length
            				);
            	}                
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
    private class GetMemoryThread extends Thread {
 
        /**
         * No-arg constructor to create an object of this class.
         */
        public GetMemoryThread() {
			//  
		}
        
        /**
    	 * Overridden <code>run</code> method. Finds Record numbers
    	 * of deleted Records.
    	 */
		@Override
		public void run() {
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countGetMemeoryStart++;
    			long memory = -1;
    			InterfaceClient_Admin client = null;
            	try {        		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                	
                   memory = client.getAllocatedMemory();
                } catch (final Exception e) {                	
                	System.out.println("Z_R_TestAdminManyCons - GetDelRecords Many Connect: " 
                			+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET DELETED",
            					countRuns, countGetMemoryEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET DELETED",
            					countRuns, countGetMemoryEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET DELETED",
            					countRuns, countGetMemoryEnd);
            		}  
                }
                countGetMemoryEnd++;
                if ((countGetMemoryEnd % loopsToNextDisplay) == 0) {
                	 System.out.println("Z_R_TestAdminManyCons - GET MEMEORY,"
                	 		+ " start: " + countGetMemeoryStart
             				+ " - run end: "+ countGetMemoryEnd 
             				+ " - loops: " + countRuns
             				+ " - results: " + memory
             				);	
            	}               		
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
    	 * Overridden <code>run</code> method. Finds Record numbers
    	 * of locked Records.
    	 */
		@Override
		public void run() {
    		int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
            	countGetLoStart++;
    			Set<Long> results = new HashSet<Long>();	
    			InterfaceClient_Admin client = null;
            	try {        		
            		client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
                	
                   results = client.getLocked();
                   results.size();
                } catch (final Exception e) {
                	System.out.println("Z_R_TestAdminManyCons - "
                			+ "GetLocked Many Connect: " 
                			+ e.getLocalizedMessage());
            		if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
            					Z_R_TestAdminManyCons.port, 
            					"Z_R_TestAdminManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		} 
                }
                countGetLoEnd++;
                if ((countGetLoEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestAdminManyCons - GET LOCKED, "
                			+ "start: " + countGetLoStart
            				+ " - run end: "+ countGetLoEnd 
            				+ " - loops: " + countRuns
            				+ " - results: " + results.size()
            				);	
            	}                		
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
			int i = 0;
            while (i <= numberOfInternalLoops) { 
            	i++;
				List<Record> result = new ArrayList<Record>();
				InterfaceClient_Admin client = null;				
	            try{
	            	countGetValidStart++;     
	            	client = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
	            				Z_R_TestAdminManyCons.ip, Z_R_TestAdminManyCons.port);
	               result = client.getAllValidRecords();
	            } catch (final Exception e) {            	
	            	System.out.println("Z_R_TestAdminManyCons - GetValidRecsThread - " 
	            			+ e.getLocalizedMessage());
	            	if(e instanceof ConnectException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
	        					Z_R_TestAdminManyCons.port, 
	        					"Z_R_TestAdminOneCon - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}else if(e instanceof BindException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
	        					Z_R_TestAdminManyCons.port, 
	        					"Z_R_TestAdminOneCon - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}else if(e instanceof SocketException){
	        			displayDialog(e.getMessage(), Z_R_TestAdminManyCons.ip, 
	        					Z_R_TestAdminManyCons.port, 
	        					"Z_R_TestAdminOneCon - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}  
	            	
	            }
	            countGetValidEnd++;
				if ((countGetValidEnd % loopsToNextDisplay) == 0) {
					 System.out.println("Z_R_TestAdminManyCons - GetValidRecsThread, start: " 
							 	+ countGetValidStart
		        				+ " - run end: "+ countGetValidEnd 
		        				+ " - loops: " + countRuns
		        				+ " - results: " + result.size()
		        				);
				}   
            }
        }
    }
    
            
    
    /**
     * It displays a dialog to exit the application by a call to 
     * 'System.exit(0)' or continues.
     * 
     * @param exception The exception message.
     * @param ipDisplay The ipaddress of the server.
     * @param portDisplay The port number of the server.
     * @param operation Describes the proceeded operation.
     * @param iterStart The counted iteration at the start of the thread. 
     * @param iterEnd The counted iteration at the start of the thread.
     */
    static void displayDialog(final String exception, 
    		final String ipDisplay, final String portDisplay, final String operation,
    		final int iterStart, final int iterEnd){
    	final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestAdminManyCons"
						+ "\n" + operation
				 		+ "\nException: " + exception 
				 		+ "\nIP: " + ipDisplay + " - " 
				 		+ "Port: " + portDisplay
				 		+ "\nloops start: " + iterStart
				 		+ "\nloops end: " + iterEnd  
				 		+ "\nTry again or exit:"
				 		, 
				 		"Z_R_TestAdminManyCons",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    }
}

