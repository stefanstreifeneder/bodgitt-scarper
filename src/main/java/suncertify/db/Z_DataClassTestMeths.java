package suncertify.db;

import suncertify.gui.SavedConfiguration;

/**
 * The class test each single method of the class
 * <code>Data</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class Z_DataClassTestMeths {
	
	
	/**
	 * No-arg constructor.
	 */
	private Z_DataClassTestMeths(){
		//
	}
	
	/**
	 * Starts the program.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(final String[] args){
		
		final String pathDatabaseCurDir = 
				SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.DATABASE_LOCATION);
		String password = SavedConfiguration.getSavedConfiguration().getParameter(
				SavedConfiguration.PASSWORD);
		
		try {
			
			final Data db = new Data(pathDatabaseCurDir, password);
			
			// readRecord()
			//---------------------
			
			long recNo = 
			1;
			//-1;
			String[] rec = db.readRecord(recNo);
			for(int k = 0; k < rec.length; k++){
				System.out.println("DataTest, db.getDvd, "
						//+ "count: " + count
						+ " - [" + k + "]: " + rec[k]);
			}
				
			
			// getRecords()
			//------------------------------------------------
			/**
			final List<String[]> set =  db.getRecords();
			for(final String[] rec : set){				
				for(int j = 0; j < rec.length; j++){
					System.out.print("DataTest, count: " 
							+ " - " + rec[j] + "\n");
				}
			}
			*/
			
			// findByCriteria()
			//------------------------------------------------
			/**
			String[] criteria = 
					//{null};
					{""};
					//{};
					//null;
					//{"", "", "", "1"};//Staff
					//{"", "", "", "a"};//Staff
					//{"", "", "", "", "$"};//Rate
					//{"", "", "", "", "8"};//Rate
					//{"", "", "", "", "a"};//Rate
					//{"", "", "", "", "", "s"};//Owner
					//{"H", "", "", "", "", "", null};//null
			//{"", "", "", "", "", ""};//null
			final long[] recNos =  db.findByCriteria(criteria);
			System.out.println("Z_DataClassTestMeths, findBy, size: "
					+ recNos.length);
			for(int i = 0; i < recNos.length; i++){	
				String[] rec = db.readRecord(recNos[i]);
				System.out.print("Record: ");
				for(int j = 0; j < rec.length; j++){
					System.out.print(rec[j] + " ");
				}
				System.out.print("\n");
			}
			*/
			
			// lockRecord()
			//------------------------------------------------
			/**
			long recNo = 3;// does not exist
			//long recNo = 1;//deleted
			long cookie = -1;
			cookie = db.lockRecord(recNo);
			//cookie = db.lockRecord(recNo);//Deadlock
			System.out.println("Z_DataClassTestMeths, lockRecord, "
					+ "cookie: " + cookie);
			*/
			
			
			
			// unlock()
			//------------------------------------------------
			/**
			long recNo = 3;// does not exist
			//long recNo = 1;//deleted
			long cookie = -1;
			cookie = db.lockRecord(recNo);
			//db.unlock(recNo, cookie);
			db.unlock(recNo, -1);
			System.out.println("Z_DataClassTestMeths, unlock, "
					+ "cookie: " + cookie);
			*/
			
			
			//update lock problems
			//-----------------------------------------------
			/**
			long cookie = -1;
			long recNo = 3;
			String[] rec = db.readRecord(recNo);
			rec[1] = "Stevo";  
			cookie = db.lockRecord(recNo);
			db.updateRecord(recNo, rec, -1);
			db.unlock(recNo, cookie);
			for(int i = 0; i < rec.length; i++) {
				System.out.println(rec[i] + ";");
			}
			*/
			
			
			//update input problems
			//-----------------------------------------------
			/**
			long cookie = -1;
			long recNo = 3;
			String[] rec = db.readRecord(recNo);
			//rec[0] = "";//flag
			rec[1] = "12345678901234567890123456789012";  
			rec[2] = "12345678901234567890123456789012"
					+ "12345678901234567890123456789012";   
			rec[3] =  "12345678901234567890123456789012"
					+ "12345678901234567890123456789012";    
			rec[4] = "123456";  
			rec[5] = "$0.01";  
			rec[6] = "12345678";  
			//rec[7] = "";//record number 
			cookie = db.lockRecord(recNo);
			db.updateRecord(recNo, rec, cookie);
			db.unlock(recNo, cookie);
			for(int i = 0; i < rec.length; i++) {
				System.out.println(rec[i] + ";");
			}
			*/
			
			
			//create
			//-----------------------------------------------
			/**
			String[] rec = new String[8];
			rec[0] = "";//flag
			rec[1] = "12345678901234567890123456789012";  
			rec[2] = "12345678901234567890123456789012"
					+ "12345678901234567890123456789012";   
			rec[3] =  "12345678901234567890123456789012"
					+ "12345678901234567890123456789012";    
			rec[4] = "123456";  
			rec[5] = "$0.01";  
			rec[6] = "12345678";  
			rec[7] = "2";//record number
			long recNo = db.createRecord(rec);
			System.out.println("DataTest, recNo: " + recNo);
			*/
			
				
			// remove 
			//-----------------------------------------------
			/**
			long cookie = -1;
			long recNo = 3;
			String[] rec = db.readRecord(recNo);
			cookie = db.lockRecord(recNo);
			db.deleteRecord(recNo, -1);
			db.unlock(recNo, cookie);
			for(int i = 0; i < rec.length; i++) {
				System.out.println(rec[i] + ";");
			}
			*/
			
			
		} catch (final Exception e) {
			//e.printStackTrace();
			System.out.println("Z_DataClassTestMeths, ends program"
					+ " due to: " + e.toString());
			System.exit(0);
		}
	}
}
