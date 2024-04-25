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

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.SavedConfiguration;
/**
 * The class is a derivative of a class, which is published on 
 * www.coderanch.com and originally was created by Roel De Nijs
 * (see: https://coderanch.com/t/484954/certification/
 * Test-business-service).<br> 
 * I revised the original class according to my public interfaces
 * <code>suncertify.db.InterfaceClient_ReadOnly</code>,
 * <code>suncertify.db.InterfaceClient_LockPermission</code> and
 * <code>suncertify.db.InterfaceClient_Buyer</code>.<br>
 * The inner class <code>BookThread</code> books Records and
 * the inner class <code>ReleaseThread</code> releases Records of 
 * booked state. In the method <code>startTests</code> a loop
 * creates objects of the inner classes. Another loop
 * starts the <code>Thread</code> and calls <code>join</code>
 * with the thread objects.<br>
 * <br>
 * This program should only used in a network environment.
 * The access should be done via ipaddress and port.<br>
 * This program is not appropriate to run local.<br>
 * The setting of the connection data (ipaddress and port) will be done
 * in the static initializer.<br>
 * <br>
 * Each thread creates its own connection to the server.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class Z_R_TestBuyerManyCons {
	
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
	static int countGetDelStart = 0;
	
	/**
	 * Counts, if the method <code>run</code> of the inner class
	 * <code>GetMemoryThread</code> has reached the end.
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
	 * Stores the number of iterations.
	 */
	private static long iterations = 0;
	
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
	 * Private noarg-constructor.
	 */
	private Z_R_TestBuyerManyCons(){
		// 
	}
 
    /**
     * Starts the program.
     * 
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
    	
    	iterations = 100;
    	loopsToNextDisplay = 100;
    	numberOfInternalLoops = 50;
    	
    	//iterations
    	boolean iterateIsEntered = true;
    	while(iterateIsEntered){
    		final String inputIterations = JOptionPane.showInputDialog(null,
        			"Z_R_TestBuyerManyCons starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"100");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestBuyerManyCons - please enter the number of iterations!", 
    					 		"Z_R_TestBuyerManyCons",
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
        			"Z_R_TestBuyerManyCons starts."
    				 		+ "\nPlease enter the number of iterations." , 
    				 		"50");
    		if (inputIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (inputIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestBuyerManyCons - please enter the number of iterations!", 
    					 		"Z_R_TestBuyerManyCons",
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
        			"Z_R_TestBuyerManyCons starts."
    				 		+ "\nPlease enter the number of iterations to the next display." , 
    				 		"1000");
    		if (displayIterations == null) {
    			System.exit(0);
    		}else 
    			
    			if (displayIterations.equals("")) {
    			
    			final int n = JOptionPane.showConfirmDialog(null, 
    					"Z_R_TestBuyerManyCons - please enter the number of iterations!", 
    					 		"Z_R_TestBuyerManyCons",
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
		null, "Z_R_TestBuyerManyCons" 
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
    			"Z_R_TestBuyerManyCons\n"
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
    	
    	
    	System.out.println("Z_R_TestBuyerManyCons has started."
    			+ "\nPlease wait. Additional information should"
    			+ "\nbe displayed in the next 30 seconds."
    			+ "\nYour input: " + inputAll 
         		+ "\nIP: " + ip + " - " 
         		+ "Port: " + port
         		+ "\nIterations: " + iterations*numberOfInternalLoops);
    	
    	
    	final int n = JOptionPane.showConfirmDialog(null, 
				"Z_R_TestBuyerManyCons "
		    	 		+ "starts."
		    	 		+ "\nRole: BUYER" 
		         		+ "\nIP: " + ip + " - " 
		         		+ "Port: " + port
		         		+ "\nIterations: " + iterations*numberOfInternalLoops
				 		, 
				 		"Z_R_TestBuyerManyCons",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    	
        new Z_R_TestBuyerManyCons().startTests();
    }
 
    /**
     * Starts the threads in a loop.
     */
    private void startTests() {
        final List<Thread> threads = new ArrayList<Thread>();
        try {
        	for (int i = 0; i < iterations; i++) {       	
            	countRuns++;
            	threads.add(new Thread(new BookThread()));
                threads.add(new Thread(new ReleaseThread()));
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
        System.out.println("Z_R_TestBuyerManyCons has run to the end."
        		+ "\nDone, Runs: " + countRuns + " - " 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd
        		+ "\n- find start: " + countGetRecStart
        		+ " - find end: " + countGetRecEnd
        		+ "\n- findBy start: " + countFindByStart
        		+ " - findBy end: " + countFindByEnd
        		+ " -\n " + formattedDate);
        
        final JOptionPane alert = new JOptionPane("Z_R_TestBuyerManyCons "
        		+ "has run to the end."
        		+ "\nDone, Runs: " + countRuns*10 + " - " 
        		+ "\n- rent start: " + countRentStart
        		+ " - rent end: " + countRentEnd
        		+ "\n- release start: " + countReleaseStart
        		+ " - release end: " + countReleaseEnd
        		+ "\n- find start: " + countGetRecStart
        		+ " - find end: " + countGetRecEnd
        		+ "\n- findBy start: " + countFindByStart
        		+ " - findBy end: " + countFindByEnd
        		+ " -\n " + formattedDate
         		
         		, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
 		final JDialog dialog = alert.createDialog(null, 
 				"B & S - Z_R_TestBuyerManyCons");
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
            	InterfaceClient_Buyer data = null;
            	try {            		
            		data = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
            		cookie = data.setRecordLocked(recNo);					
					ok = data.reserveRecord(recNo, 10 + countRentStart, cookie);            	
            	}catch(final Exception e){
            		if( !((e instanceof SecurityException)
                			^ (e instanceof RecordNotFoundException))) {
            			System.out.println("Z_R_TestBuyerManyCons - BOOK: " 
                				+ countRentStart + " - " + e.getMessage());
            			if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - BOOKING",
                					countRuns, countRentEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - BOOKING",
                					countRuns, countRentEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - BOOKING",
                					countRuns, countRentEnd);
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
							System.out.println("Z_R_TestBuyerManyCons - BOOKING"
									+ " Unlock, count: " 
	                				+ countRentStart + " - " + e.getMessage());
						}
					}
            	}
            	countRentEnd++;
            	if ((countRentEnd % loopsToNextDisplay) == 0) {
            		System.out.println("Z_R_TestBuyerManyCons - BOOKING, rent-start: " 
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
            	InterfaceClient_Buyer data = null;
            	try {
            		data = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
					cookie = data.setRecordLocked(recNo);
					ok = data.releaseRecord(recNo, cookie);
            	}catch(final Exception e){
            		if( !((e instanceof SecurityException)
                			^ (e instanceof RecordNotFoundException))) {
            			System.out.println("Z_R_TestBuyerManyCons - RELEASE: " 
                			+ countRuns + " - " + e.getMessage());
            			if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - RELEASE",
                					countRuns, countReleaseEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - RELEASE",
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
							System.out.println("Z_R_TestBuyerManyCons - RELEASE, "
									+ "Unlock, count: " 
		                			+ countRuns + " - " + e.getMessage());
						}
					}
            	}
            	countReleaseEnd++;   
            	if ((countReleaseEnd % loopsToNextDisplay) == 0) {
            		System.out.println("Z_R_TestBuyerManyCons - RELEASE, "
            				+ "release-start: " + countReleaseStart
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
    			InterfaceClient_Buyer client = null;
            	try {
            		
            		client = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
                	
                	r =  client.getRecord((long)this.ran.nextInt(28) + 1);  
                	r.toString();
                } catch (final Exception e) {
                	if( !(e instanceof RecordNotFoundException)) {
                		System.out.println("Z_R_TestBuyerManyCons - FindingRecords"
                				+ ": " 
        						+ e.getLocalizedMessage());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FindingRecords",
                					countRuns, countGetRecEnd);
                		}
                	}
                }
                countGetRecEnd++;
                if ((countGetRecEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestBuyerManyCons - FindingRecords, "
                			+ "start: " + countGetRecStart
            				+ " - run end: "+ countGetRecEnd 
            				+ " - loops: " + countRuns
            				+ " - rec: ");
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
    			InterfaceClient_Buyer client = null;
            	try {        		
            		client = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
                   results = client.findByFilter(criteriaExists);
                } catch (final Exception e) {
                	if( !(e instanceof RecordNotFoundException)) {
                		System.out.println("Z_R_TestBuyerManyCons - FIND BY CRITERIA"
                				+ ": " 
                    			+ e.getLocalizedMessage());
                		if(e instanceof ConnectException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		}else if(e instanceof BindException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		} else if(e instanceof SocketException){
                			displayDialog(e.getMessage(), ip, port, 
                					"Z_R_TestBuyerManyCons - FIND BY CRITERIA",
                					countRuns, countFindByEnd);
                		}
                	}
                }
                countFindByEnd++;
                if ((countFindByEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestBuyerManyCons - FIND BY CRITERIA, "
                			+ "start: " + countFindByStart
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
            	countGetDelStart++;
    			long memory = -1;
    			InterfaceClient_Buyer client = null;
            	try {        		
            		client = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
                	
            		memory = client.getAllocatedMemory();
            		
                } catch (final Exception e) {
                	System.out.println("Z_R_TestBuyerManyCons - GET MEMORY: " 
                			+ e.getLocalizedMessage());
        			if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET MEMORY",
            					countRuns, countGetDelEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET MEMORY",
            					countRuns, countGetDelEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET MEMORY",
            					countRuns, countGetDelEnd);
            		} 
                }
                countGetDelEnd++;
                if ((countGetDelEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestBuyerManyCons - GET MEMORY, start: " 
                			+ countGetDelStart
            				+ " - run end: "+ countGetDelEnd 
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
    			InterfaceClient_Buyer client = null;
            	try {        		
            		client = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
            				ip, port);
                	
                   results = client.getLocked();
                   results.size();
                } catch (final Exception e) {
                	System.out.println("Z_R_TestBuyerManyCons - GET LOCKED: " 
                			+ e.getLocalizedMessage());
                	
                	//new
                	if(e instanceof ConnectException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		}else if(e instanceof BindException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		}else if(e instanceof SocketException){
            			displayDialog(e.getMessage(), ip, port, 
            					"Z_R_TestBuyerManyCons - GET LOCKED",
            					countRuns, countGetLoEnd);
            		} 

                	
                }
                countGetLoEnd++;
                if ((countRentEnd % loopsToNextDisplay) == 0) {
                	System.out.println("Z_R_TestBuyerManyCons - GET LOCKED, start: " 
                			+ countGetLoStart
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
				InterfaceClient_Buyer client = null;
	            try{
	            	countGetValidStart++;     
	            	client = suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
	            				ip, port);
	               result = client.getAllValidRecords();
	            } catch (final Exception e) {            	
	            	System.out.println("Z_R_TestBuyerManyCons - GetValidRecsThread - " 
	            			+ e.getLocalizedMessage());
	            	if(e instanceof ConnectException){
	        			displayDialog(e.getMessage(), ip, port, 
	        					"Z_R_TestBuyerManyCons - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}else if(e instanceof BindException){
	        			displayDialog(e.getMessage(), ip, port, 
	        					"Z_R_TestBuyerManyCons - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}else if(e instanceof SocketException){
	        			displayDialog(e.getMessage(), ip, port, 
	        					"Z_R_TestBuyerManyCons - GetValidRecsThread",
	        					countRuns, countGetValidEnd);
	        		}  
	            	
	            }
	            countGetValidEnd++;
				if ((countGetValidEnd % loopsToNextDisplay) == 0) {
					 System.out.println("Z_R_TestBuyerManyCons - GetValidRecsThread, start: " 
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
				"Z_R_TestBuyerManyCons"
						+ "\n" + operation
				 		+ "\nException: " + exception 
				 		+ "\nIP: " + ipDisplay + " - " 
				 		+ "Port: " + portDisplay
				 		+ "\nloops start: " + iterStart
				 		+ "\nloops end: " + iterEnd  
				 		+ "\nTry again or exit:"
				 		, 
				 		"Z_R_TestBuyerManyCons",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
    }
}

