package suncertify.db;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import suncertify.gui.ExceptionDialog;
import suncertify.gui.SavedConfiguration;

/**
 * Reads the database and stores the result in a text file.
 * The text file is called 'dbText.txt' and will be stored in 
 * the currently working directory. 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class X_ReadDB {
	
	
	/**
	 * Noarg-constructor, which is never called.
	 */
	private X_ReadDB(){
		
	}

    /**
     * Starts the program.
     * 
     * @param args Command arguments.
     */
    public static void main(final String[] args) {
        try{

			String pathDatabaseCurDir = SavedConfiguration.getSavedConfiguration()
					.getParameter(SavedConfiguration.DATABASE_LOCATION);
			
			// enter path
			final String input = JOptionPane.showInputDialog(
			null, "X_ReadDB" 
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
			
			final FileInputStream in = new FileInputStream(pathDatabaseCurDir);
			final DataInputStream dis = new DataInputStream(in);

			// Physical file to store
			final String path = System.getProperty("user.dir");
			final File file = new File(path + "\\dbText.txt");
			final FileWriter fw = new FileWriter(file); // create an actual file & a
													
			if (!file.isFile()) {			
				try {
					file.createNewFile();
				} catch (IOException ex) {
					
					ExceptionDialog
							.handleException("X_ReadDB: " 
									+ ex.getMessage());
					fw.close();
					dis.close();
					return;
				}
			}									

			// Determines how many bytes will be read.			
			int byteCountString = 0;
			
			final String inputAll = JOptionPane.showInputDialog(null,
					"DB: " + pathDatabaseCurDir
					+ "\nPhysical File: " + path + "\\dbText.txt"
					+ "\nDetermines how many bytes will be read."
					+ "\nMeasured Seize: " + new File(pathDatabaseCurDir).length()
					+ "\n"
					+ "\n",
					Long.valueOf(new File(pathDatabaseCurDir).length()));
			if (inputAll == null) {
				System.exit(0);
			} 
			byteCountString = Integer.parseInt(inputAll);
			
			final byte header[] = new byte[byteCountString];

			dis.read(header);
			System.out.println(header.length);

			System.out.println("-----------header-----------------");
			int count = 0;
			for (int i = 0; i < header.length; i++) {

				System.out.println("header[" + count + "] = " + (char) header[i] + "  " 
								+ header[i]);
				
				fw.write("header[" + count + "] = " + (char) header[i] //+ "  " 
								+ header[i] + "  " + "\r\n");
				
				
				final String s1 = "header[" + count + "] = " + (char) header[i] + "  " + header[i];
				System.out.println("gepackter String:" + s1);
				count++;
			} // end for
			fw.flush(); // flush before closing
			fw.close(); // close file when done
			dis.close();

        }catch(final Exception e){e.printStackTrace();}
    }
}