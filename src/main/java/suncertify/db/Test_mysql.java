package suncertify.db;

import java.io.IOException;
import java.util.List;

/**
 * @author stefan
 *
 */
public class Test_mysql {
	
	/**
	 * Starts the program.
	 * 
	 * @param arg Command line arguments.
	 */
	@SuppressWarnings("null")
	public static void main(String[] arg) {
		
		InterfaceClient_Admin client = null;
		try {
			client = suncertify.direct.admin.DirectConnector_Admin.getLocal();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {			
			List<Record> li = client.getAllValidRecords();
			for(Record r : li) {
				System.out.println("Test_mysql, rec: " + r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
//		try {
//			System.out.println("Test_mysql, rec: " + client.getRecord(4));
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (RecordNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
//		try {
//			long recNoDel = 42;
//			//long cookie = 5555555;//TEST!!!!!!!!!!!!!!!!!!!!!!!
//			long cookie = client.setRecordLocked(recNoDel);
//			boolean delOk = client.removeRecord(recNoDel, cookie);
//			client.setRecordUnlocked(recNoDel, cookie);
//			System.out.println("Test_mysql, recNo: " + 1 + " - cookie: " + cookie);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (RecordNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
		
//		long rno;
//		try {
//			rno = client.addRecord(new Record( 
//					((long)44),
//					"Isa", 
//					"Munich", 
//					"all",
//					2, 
//					"$2222.22", 
//					""));
//			System.out.println("Test_mysql, recNo: " + rno);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (DuplicateKeyException e) {
//			e.printStackTrace();
//		}
		
		
//		long recNo = 2;
//		boolean updateOk = false;
//		long cookie = -1;
//		try {
//			cookie = client.setRecordLocked(recNo);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		} catch (RecordNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			updateOk = client.modifyRecord(new Record( 
//					recNo,
//					"Elisabetta", 
//					"Capiago", 
//					"less",
//					2, 
//					"$2.22", 
//					""), recNo, cookie);
//			
//			client.setRecordUnlocked(recNo, cookie);
//		} catch (SecurityException e1) {
//			e1.printStackTrace();
//		} catch (IllegalArgumentException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//		} catch (RecordNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		
//		System.out.println("Test_mysql, recNo: " + recNo
//				+ " - cookie: " + cookie + " - up: " + updateOk);
//		
		
		
		
//		try {
//			System.out.println("Test_mysql, rec: " + client.getRecord(88));
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (RecordNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
//		try {
//			System.out.println("Test_mysql, rec: " + client.getRecord(2));
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (RecordNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
		try {			
			List<Record> li = client.getAllValidRecords();
			for(Record r : li) {
				System.out.println("Test_mysql, rec: " + r);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
