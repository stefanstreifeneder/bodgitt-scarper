package suncertify.test;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import suncertify.db.InterfaceClient_Admin;
import suncertify.gui.SavedConfiguration;

/**
 * 
 * This class tests each method of the public interface. There are five
 * interfaces, which working together as a public interface.<br>
 * The interfaces are:<br>
 * <br>
 * <code>InterfaceClient_ReadOnly</code><br>
 * <code>findByFilter</code> - searches for Records by criteria<br>
 * <code>getRecord</code> - searches for a Records by Record number<br>
 * <code>getDeleted</code> - returns all Record numbers of deleted Records<br>
 * <code>getBooked</code> - returns all Record numbers of rented Records<br>
 * <code>getLocked</code> - returns all Record numbers of locked Records<br>
 * <br>
 * <code>InterfaceClient_LockPermission</code><br>
 * <code>setRecordLocked</code> - locks a Record<br>
 * <code>setRecordUnlocked</code> - unlocks a Record<br>
 * <br>
 * <code>InterfaceClient_Buyer</code><br>
 * <code>reserveRecord</code> - rents a Record<br>
 * <code>releaseRecord</code> - releases a Record<br>
 * <br>
 * <code>InterfaceClient_Seller</code><br>
 * <code>addRecord</code> - adds a Records<br>
 * <code>modifyRecord</code> - modifies a Records<br>
 * <code>removeRecord</code> - removes a Record<br>
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class Z_R_Test_Meths {
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_R_Test_Meths(){
		// 
	}

	/**
	 * 
	 * Starts the program.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(final String[] args) {
		
		
		InterfaceClient_Admin connection = null;
		//InterfaceClient_Buyer connection = null;
		//InterfaceClient_Seller connection = null;
		try {
			String ip = SavedConfiguration.getSavedConfiguration().getParameter(
					SavedConfiguration.SERVER_IP_ADDRESS);
			String port = SavedConfiguration.getSavedConfiguration().getParameter(
					SavedConfiguration.SERVER_PORT);
			// new enter ip and port
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
	    	// end new enter ip and port			
			connection = suncertify.sockets.admin.SocketConnector_Admin.getRemote(
					ip, port);
		} 
		catch (final UnknownHostException e) {
			System.out.println("Z_R_Test_Meths/main UHExc: " + e.toString() + connection );
		} catch (final IOException e) {
			System.out.println("Z_R_Test_Meths/main IOExc: " + e.toString() );
		}catch (final Exception e) {
			System.out.println("Z_R_Test_Meths/main IOExc: " + e.toString() );
		}
		
		
		
		//_____________________  InterfaceClient_ReadOnly ______________________
		
		/**
		//------------------------- findByRecord --------------------------		
		//Please check:
		//null as method argument
		//a null String within valid Strings
		//no UTF-8
		//to many String arguments
		
		long[] collRecs = null;
		String[] criteria = 
				{"D", "", "s", "", "", "", "", "", "s"};
		try {			
			if(connection != null){
				collRecs =  connection.findByFilter(criteria);
				//collRecs =  connection.findByFilter(null);
				System.out.println("Z_R_Test_Meths/findByFilter "
						+ "length: " + collRecs.length);
				for(int i = 0; i < collRecs.length; i++ ){
					Record r = null;
					try {
						r = connection.getRecord(collRecs[i]);
					} catch (RecordNotFoundException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						continue;
					}
					System.out.println("Z_R_Test_Meths/findByFilter "
							+ "length: " + collRecs.length
							+ " - rec: " + r);
				}
			}
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Z_R_Test_Meths/findByFilter Exc: " 
				+ e.toString());					
		}catch (IOException e) {
			System.out.println("Z_R_Test_Meths/findByFilter Exc: " 
				+ e.toString());
		} 
		
		if(collRecs != null){
			System.out.println("Z_R_Test_Meths/findByFilter collRecs: " 
					+ collRecs.length);
		}else{
			System.out.println("Test_db_All/main collRecs is NULL!");
		}
		*/
				
		
		//--------------------------- getRecord ---------------------------
		//Be aware You never get deleted Records of the application.
		//If You want to see deleted Records, You have to set comments
		//in the method readRecord(long recNo) of class data.
		//Please check:
		//wrong number (-1, 1000000)
		//deleted Record 
		//GUI - no Records
		/**
		Record rec = null;
		try {
			if(connection != null){
				rec = connection.getRecord(2444);
			}
		} 
		catch (IOException e2) {
			System.out.println("Z_R_Test_Meths, getRecord, Exc: "
					+ e2.toString());
		} 				
		catch (RecordNotFoundException e2) {
			System.out.println("Z_R_Test_Meths, getRecord, Exc: " 
					+ e2.toString());
		}
		System.out.println("Test_db_All/main getRecord, rec: " + rec);
		*/
		
		
		
		//--------------------------- getLocked ---------------------------
		// Returns the Record number of all locked Records.
		// Should be applied only in a network environment.
		// Local always 0.		
		// Please check:
		// returns null or an initialized set even with size 0
		/**
		Set<Long> lockedRecs = null;
		try {
			if (connection != null) {
				lockedRecs = connection.getLocked();
				System.out.println("TestMethsPublic1, getLocked, size: " + lockedRecs.size());

				for (Long recNo : lockedRecs) {
					System.out.println("TestMethsPublic2, getLocked, recNo: " + recNo);
				}
				
				lockedRecs.remove(Long.valueOf(2));
				
			}
		} catch (IOException e2) {
			System.out.println("TestMethsPublic3, getLocked, Exc: " 
				+ e2.toString());
		}
		if (lockedRecs != null) {
			System.out.println("TestMethsPublic4, getLocked, size: " + lockedRecs.size());
		}
		*/	
		
		
		//--------------------------- getAllocatedMemory ---------------------------
		//Returns the size of the database by counting the bytes.
		/**
		long dbSize = -1;
		try {
			if(connection != null){
				dbSize = connection.getAllocatedMemory();
			}
		} 
		catch (IOException e2) {
			System.out.println("Z_R_Test_Meths, getRecord, Exc: "
					+ e2.toString());
		}
		System.out.println("Test_db_All/main getRecord, rec: " + dbSize);
		*/
		
		
		//--------------------------- getAllValidRecords ---------------------------
		// Returns a list with all valid Records.
		/**
		List<Record> listValidRecords = null;
		try {
			if (connection != null) {
				listValidRecords = connection.getAllValidRecords();
				System.out.println("Z_R_Test_Meths 1, getBooked, size: " 
						+ listValidRecords.size());

				for (Record recNo : listValidRecords) {
					System.out.println("Z_R_Test_Meths 2, getBooked, recNo: " + recNo);
				}
			}
		} catch (IOException e2) {
			System.out.println("Z_R_Test_Meths 3, getBooked, Exc: " 
				+ e2.toString());
		}
		if (listValidRecords != null) {
			System.out.println("Z_R_Test_Meths 4, getBooked, size: " 
					+ listValidRecords.size());
		}
		*/
		
		
		//_____________________  InterfaceClient_LockPermission  ______________________

		
		//------------------------ setRecordLocked ---------------------------
		// Locks a Record to delete or update it.
		// Please check:
		// wrong Record number
		// deleted Record
		// call twice		
		// reserved Record (remote)
		// locked Record (remote)
		// alive with sleep() (remote)
		// alive with sleep() another is waiting (remote)		
		/**
		long recNo = 2;
		long cookie = 0;
		try {
			System.out.println("Z_R_Test_Meths, setRecordLocked, Start,"
					+ "recNo: " + recNo);
			if (connection != null) {
				cookie = connection.setRecordLocked(recNo);
				//connection.setRecordLocked(recNo);
				// Thread.sleep(20000);
//				TimeUnit.SECONDS.sleep(20);
			}
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths 1, setRecordLocked, Exc: " 
				+ e.toString());
//			try {
//				TimeUnit.SECONDS.sleep(20);
//			} catch (InterruptedException e1) {
//				System.out.println("Z_R_Test_Meths 2, setRecordLocked, Exc: " 
//						+ e1.toString());
//			}
		}
		System.out.println("Z_R_Test_Meths 3, setRecordLocked, "
				+ "recNo: " + recNo
				+ " - cookie: " + cookie);
		
		*/				
		
		//------------------------ setRecordUnlocked ---------------------------
		// Unlocks a Record.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 		
		// remove wrong cookie 
		// update wrong cookie 
		// wrong client (remote)
		// reserved Record (remote)
		// locked Record (remote)
		/**				
		long cookie = 1l;
		boolean ok = false;
		int recNum = 3;
		try {
			if (connection != null) {
				cookie = connection.setRecordLocked(recNum);				
				//Thread.sleep(40000);				
				connection.removeRecord(recNum, cookie);
				//Thread.sleep(40000);	
				Record rec = new Record();
//				rec.setName("Herr Straub");
//				rec.setNumberOfStaff(123456);//1234567
//				rec.setHourlyChargeRate("$333.33");
//				rec.setOwner("12345678");
//				connection.modifyRecord(rec, recNum, cookie);
//				 connection.reserveRecord(1, 7771777, cookie);
//				 connection.releaseRecord(1, cookie);				
				//Thread.sleep(10000);
				ok = connection.setRecordUnlocked(recNum, 448816);
				System.out.println("Test_db_All/main " + "Vor sleep, setUnLocked" + ok);
				Thread.sleep(40000);
			}
		} catch (IOException e1) {
			System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
				+ e1.toString());
		} catch (RecordNotFoundException e1) {
			System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
				+ e1.toString());
		} catch (SecurityException e) {
			System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
				+ e.toString());
			try {
				Thread.sleep(40000);
				try {
					if(connection != null){
						ok = connection.setRecordUnlocked(recNum, cookie);
					}					
				}catch (Exception ex) {
					System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
						+ ex.toString());
				}
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
					+ e1.toString());
			}
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, setUnlocked, Exc: " 
				+ e.toString());
		}
		System.out.println("Test_db_All/main unlock: " + ok);
		*/		
		
		
		
		
		//_____________________  InterfaceClient_Buyer  ______________________
						
		
		//-------------------------- reserveRecord -----------------------------
		// Rents a Record and enters the Record number.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 
		// wrong cookie 
		// wrong client 
		/**
		long cookie = 0l;
		boolean okReserve = false;
		boolean okUnlock = false;
		long recNo = 7;
		try {
			
			if(connection != null){
				cookie = connection.setRecordLocked(recNo);
				//Reserve = connection.reserveRecord(recNum, recordID, cookie);
				okReserve = connection.reserveRecord(recNo, 333888888, cookie);   				
				//Thread.sleep(20000);		
				okUnlock = connection.setRecordUnlocked(recNo, cookie);
			}
			Thread.sleep(20000);
		}catch (Exception e) {
			System.out.println("Z_R_Test_Meths, reserveRecord, Exc: " 
				+ e.toString());
		} 		
		System.out.println("Z_R_Test_Meths, serveRecord: " + okReserve
								+ " - unlocked: " + okUnlock);
		*/
		
		//------------------------ releaseRecord ---------------------------
		// Releases a Record of rented state.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 
		// wrong cookie locked Record (remote)
		// wrong client locked Record (remote)
		/**
		long cookie = 264286l;
		boolean ok = false;
		boolean okUnlock = false;
		long recNo = 2;
		try {			
			if(connection != null){
				
				cookie = connection.setRecordLocked(recNo);
				connection.reserveRecord(recNo, 7878, cookie);
				connection.setRecordUnlocked(recNo, cookie);				
				Thread.sleep(10000);
				
				cookie = connection.setRecordLocked(recNo);				
				//connection.modifyRelease(4, cookie);
				ok = connection.releaseRecord(recNo, -4);
				
				//okUnlock = connection.setRecordUnlocked(recNo, cookie);
			}
			Thread.sleep(20000);
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, releaseRecord, Exc: " 
				+ e.toString());
		} 
		System.out.println("Z_R_Test_Meths, releaseRecord: " + ok
				+ " - unlocked: " + okUnlock);
		*/
		
		
		
		
		
		
		//_____________________  InterfaceClient_Seller  ______________________
		
		
		
		//-------------------------- removeRecord -----------------------------
		// Marks a Record deleted.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 
		// wrong cookie locked Record (remote)
		// wrong client locked Record (remote)
		/**
		long cookie = -1;
		boolean ok = false;
		boolean okUnlock = false;
		long recNo = 1;
		try {
			if (connection != null) {
				cookie = connection.setRecordLocked(recNo);
				ok = connection.removeRecord(recNo, cookie);
				
				//new
				Thread.sleep(10000);				
				
				//okUnlock = connection.setRecordUnlocked(recNo, cookie);
			}
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, removeRecord, Exc: " 
				+ e.toString());
		} 
		System.out.println("Z_R_Test_Meths, removeRecord: " + ok
				+ " - unlocked: " + okUnlock);
		*/
						
				
		//-------------------------- modifyRecord -----------------------------
		// Modifies a Record.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 
		// wrong cookie locked Record (remote)
		// wrong client locked Record (remote)
		// wrong entries
		/**
		Record rec = new Record();
		boolean ok = false;
		boolean okUnlock = false;
		long cookie = 0l;
		long recNo = 4;
		try {
			rec.setRecNo(1222);
//			rec.setFlag("lkhj");
			rec.setName("Modify"
					//+ "1234567890"
					+ "2234567890"
					+ "3234567890"
					+ "12"
					//+ ""
					);
			rec.setCity("M-City"
					//+ "1234567890"
					+ "2234567890"
					+ "3234567890"
					+ "4234567890"
					+ "5234567890"
					+ "6234567890"
					+ "1234"					
					//+ ""
					);
			rec.setTypesOfWork("M-Types"
					//+ "1234567890"
					+ "2234567890"
					+ "3234567890"
					+ "4234567890"
					+ "5234567890"
					+ "6234567890"
					+ "1234"
					//+ ""
					);
			rec.setNumberOfStaff(123456);// 1234567
			rec.setHourlyChargeRate("$333.35");
			//rec.setOwner("0");

			if (connection != null) {
				//cookie = connection.setRecordLocked(recNo);
				ok = connection.modifyRecord(rec, recNo, -1);
				//okUnlock = connection.setRecordUnlocked(recNo, cookie);
			}
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, modify, Exc: " + e.toString());
		} 
		try {
			if(connection != null){
				System.out.println("Z_R_Test_Meths, modify, ok: " + ok 
						+ "\n- rec: " + rec.toString()
						+ "\n- db rec: " + connection.getRecord(recNo)
						+ "\n- unlock: " + okUnlock);
			}
			System.out.println("Z_R_Test_Meths, modify, End, connection is NULL!");
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, modify, Exc: " + e.toString());
		} 
		*/		
				
		//-------------------------- addRecord -----------------------------
		// Modifies a Record.
		// Please check:
		// wrong Record number 
		// wrong entries
		/**
		//Overloaded Constructor
		long recNoDB = 0;
		long recNoReq = 1;
		Record rec = new Record(recNoReq,
				"overloaded Constructor",
				"munich",
				"all",
				6,
				"$333.33",
				"");
		
		
		try {
//			rec.setRecNo(recNoReq);
//			rec.setName("Add"
//					//+ "1234567890"
////					+ "2234567890" + "3234567890" + "12"
////					+ ""
//			);
//			rec.setCity("ACity"
//					//+ "1234567890"
////					+ "2234567890" + "3234567890" + "4234567890" 
////					+ "5234567890" + "6234567890" + "1234"
//					//+ ""
//			);
//			rec.setTypesOfWork("ATypes"
//					//+ "1234567890"
////					+ "2234567890" + "3234567890" + "4234567890" 
////					+ "5234567890" + "6234567890" + "1234"
//					//+ ""
//			);
//			rec.setNumberOfStaff(123456);// 0, 1234567
//			rec.setHourlyChargeRate("$333.38");
//			rec.setOwner("");//0, 123456789
	
			if (connection != null) {
				recNoDB = connection.addRecord(rec);
			}
		} catch (Exception e) {
			System.out.println("Z_R_Test_Meths, addRecord, Exc: " 
							+ e.toString());
		}
		System.out.println("Z_R_Test_Meths, addRecord, recNo requested-DB: " + recNoReq + "-" + recNoDB + " - rec: "
				+ rec.toString());
		
		*/
	}// end main()
}

