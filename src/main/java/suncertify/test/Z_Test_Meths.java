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
public class Z_Test_Meths {
	
	/**
	 * Private noarg-constructor.
	 */
	private Z_Test_Meths(){
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
			
			//local
			String ip = SavedConfiguration.getSavedConfiguration().getParameter(
					SavedConfiguration.DATABASE_LOCATION);
			
			// enter ip and port
			final String input = JOptionPane.showInputDialog(
			null, "Z_Test_Meths" 
			+ "\n\nYou will be connected to: \n"
			+ ip
			+ "\n\nPlease enter the path:",
			ip);
			if(input == null){
				System.exit(0);
			}
	    	// enter ip and port			
			connection = suncertify.direct.admin.DirectConnector_Admin.getLocal(ip, "");
		} 
		catch (final UnknownHostException e) {
			System.out.println("Z_Test_Meths/main UHExc: " + e.toString() + connection );
		} catch (final IOException e) {
			System.out.println("Z_Test_Meths/main IOExc: " + e.toString() );
		}catch (final Exception e) {
			System.out.println("Z_Test_Meths/main IOExc: " + e.toString() );
		}
		
		
		
		
		//_____________________  InterfaceClient_ReadOnly ______________________
		
		
		//------------------------- findByRecord --------------------------		
		//Please check:
		//null as method argument
		//a null String within valid Strings
		//no UTF-8
		//to many String arguments
//		
//		long[] collRecs = null;
//		String[] criteria = 
//				//{null};
//				//{""};
//				//{};
//				//null;
//				//{"", "", "", "1"};//Staff
//				//{"", "", "", "a"};//Staff
//				//{"", "", "", "", "$"};//Rate
//				//{"", "", "", "", "8"};//Rate
//				//{"", "", "", "", "a"};//Rate
//				//{"", "", "", "", "", "s"};//Owner
//				{"H", "", "", "", "", "", null};//null
//		try {			
//			if(connection != null){
//				collRecs =  connection.findByFilter(criteria);
//				//collRecs =  connection.findByFilter(null);
//				System.out.println("Z_Test_Meths/findByFilter "
//						+ "length: " + collRecs.length);
//				for(int i = 0; i < collRecs.length; i++ ){
//					Record r = null;
//					try {
//						r = connection.getRecord(collRecs[i]);
//					} catch (RecordNotFoundException e) {
//						//e.printStackTrace();
//						continue;
//					}
//					System.out.println("Z_Test_Meths/findByFilter "
//							+ "length: " + collRecs.length
//							+ " - rec: " + r);
//				}
//			}
//		}catch (ArrayIndexOutOfBoundsException e) {
//			System.out.println("Z_Test_Meths/findByFilter Exc: " 
//				+ e.toString());					
//		}catch (IOException e) {
//			System.out.println("Z_Test_Meths/findByFilter Exc: " 
//				+ e.toString());
//		} 
//		
//		if(collRecs != null){
//			System.out.println("Z_Test_Meths/findByFilter collRecs: " 
//					+ collRecs.length);
//		}else{
//			System.out.println("Z_Test_Meths/main collRecs is NULL!");
//		}
//		
				
		
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
				rec = connection.getRecord(1);
			}
		} 
		catch (IOException e2) {
			System.out.println("Z_Test_Meths, getRecord, Exc: "
					+ e2.toString());
		} 				
		catch (RecordNotFoundException e2) {
			System.out.println("Z_Test_Meths, getRecord, Exc: " 
					+ e2.toString());
		}
		System.out.println("Z_Test_Meths/main getRecord, rec: " + rec);
		*/
		
		
		
		//--------------------------- getLocked ---------------------------
		// Returns the Record number of all rented Records.
		// Should be applied only in a networkenviroment.
		// Local always 0.		
		// Please check:
		// returns null or an initialized set even with size 0
		/**
		Set<Long> rentedRecs = null;
		try {
			if (connection != null) {
				
				try {
					connection.setRecordLocked(3);
				} catch (RecordNotFoundException e) {
					System.out.println("Z_Test_Meths, getLocked, Exc: " 
							+ e.toString());
				}
				
				rentedRecs = connection.getLocked();
				System.out.println("TestMethsPublic1, getLocked, size: " + rentedRecs.size());

				for (Long recNo : rentedRecs) {
					System.out.println("Z_Test_Meths-2, getLocked, recNo: " + recNo);
				}
				
				rentedRecs.remove(Long.valueOf(2));
				
			}
		} catch (IOException e2) {
			System.out.println("Z_Test_Meths-3, getLocked, Exc: " 
				+ e2.toString());
		}
		if (rentedRecs != null) {
			System.out.println("Z_Test_Meths-4, getLocked, size: " + rentedRecs.size());
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
			System.out.println("Z_Test_Meths, getAllocatedMemory, Exc: "
					+ e2.toString());
		}
		System.out.println("Z_Test_Meths/main getAllocatedMemory, rec: " + dbSize);
		*/
		
		
		//--------------------------- getAllValidRecords ---------------------------
		// Returns a list with all valid Records.
		/**
		List<Record> listValidRecords = null;
		try {
			if (connection != null) {
				listValidRecords = connection.getAllValidRecords();
				System.out.println("Z_Test_Meths-1, getAllValidRecords, size: " 
						+ listValidRecords.size());

				for (Record recNo : listValidRecords) {
					System.out.println("Z_Test_Meths-2, getAllValidRecords, recNo: " 
							+ recNo);
				}
			}
		} catch (IOException e2) {
			System.out.println("Z_Test_Meths-3, getAllValidRecords, Exc: " 
				+ e2.toString());
		}
		if (listValidRecords != null) {
			System.out.println("Z_Test_Meths-4, getAllValidRecords, size: " 
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
		long recNo = -1;
		long cookie = 0;
		try {
			System.out.println("Z_Test_Meths, setRecordLocked, Start,"
					+ "recNo: " + recNo);
			if (connection != null) {
				cookie = connection.setRecordLocked(recNo);
				//connection.setRecordLocked(recNo);
				// Thread.sleep(20000);
//				TimeUnit.SECONDS.sleep(20);
			}
		} catch (Exception e) {
			System.out.println("Z_Test_Meths-1, setRecordLocked, Exc: " 
				+ e.toString());
//			try {
//				TimeUnit.SECONDS.sleep(20);
//			} catch (InterruptedException e1) {
//				System.out.println("Z_Test_Meths-2, setRecordLocked, Exc: " 
//						+ e1.toString());
//			}
		}
		System.out.println("Z_Test_Meths-3, setRecordLocked, "
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
		int recNum = 3;
		try {
			if (connection != null) {
				//cookie = connection.setRecordLocked(recNum);
				//Thread.sleep(10000);
				connection.setRecordUnlocked(recNum, 448816);
				System.out.println("Z_Test_Meths/main " + "Vor sleep, setUnLocked");
				Thread.sleep(40000);
			}
		} catch (IOException e1) {
			System.out.println("Z_Test_Meths-1, setUnlocked, Exc: " 
				+ e1.toString());
		} catch (RecordNotFoundException e1) {
			System.out.println("Z_Test_Meths-2, setUnlocked, Exc: " 
				+ e1.toString());
		} catch (SecurityException e) {
			System.out.println("Z_Test_Meths-3, setUnlocked, Exc: " 
				+ e.toString());
			try {
				Thread.sleep(40000);
				try {
					if(connection != null){
						connection.setRecordUnlocked(recNum, cookie);
					}					
				}catch (Exception ex) {
					System.out.println("Z_Test_Meths, setUnlocked, Exc: " 
						+ ex.toString());
				}
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				System.out.println("Z_Test_Meths, setUnlocked, Exc: " 
					+ e1.toString());
			}
		} catch (Exception e) {
			System.out.println("Z_Test_Meths, setUnlocked, Exc: " 
				+ e.toString());
		}
		System.out.println("Z_Test_Meths/main unlock: ");
		*/		
		
		
		
		
		//_____________________  InterfaceClient_Buyer  ______________________
						
		
		//-------------------------- reserveRecord -----------------------------
		// Rents a Record and enters the Record number.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie 
		// booked Record
		/**
		long cookie = 0l;
		boolean okReserve = false;
		long recNo = 4;
		try {
			
			if(connection != null){
				cookie = connection.setRecordLocked(recNo);
				//Reserve = connection.reserveRecord(recNum, recordID, cookie);
				okReserve = connection.reserveRecord(recNo, 12345678, -1);   				
				//Thread.sleep(20000);		
				connection.setRecordUnlocked(recNo, cookie);
			}
		}catch (Exception e) {
			System.out.println("Z_Test_Meths, reserveRecord, Exc: " 
				+ e.toString());
		} 		
		System.out.println("Z_Test_Meths, serveRecord: " + okReserve);
		*/
		
		//------------------------ releaseRecord ---------------------------
		// Releases a Record of rented state.
		// Please check:
		// wrong Record number 
		// deleted Record 
		// wrong cookie
		/**
		long cookie = -1;
		boolean ok = false;
		long recNo = 2;
		try {			
			if(connection != null){
				
//				cookie = connection.setRecordLocked(recNo);
//				connection.reserveRecord(recNo, 7878, cookie);
//				connection.setRecordUnlocked(recNo, cookie);				
//				Thread.sleep(10000);
				
				//cookie = connection.setRecordLocked(recNo);				
				//connection.modifyRelease(4, cookie);
				//ok = connection.releaseRecord(recNo, -4);
				ok = connection.releaseRecord(recNo, cookie);
				//okUnlock = connection.setRecordUnlocked(recNo, cookie);
			}
		} catch (Exception e) {
			System.out.println("Z_Test_Meths, releaseRecord, Exc: " 
				+ e.toString());
		} 
		System.out.println("Z_Test_Meths, releaseRecord: " + ok);
		*/
		
		
		
		
		
	}// end main()
}

