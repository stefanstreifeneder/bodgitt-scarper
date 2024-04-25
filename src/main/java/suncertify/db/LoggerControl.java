package suncertify.db;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * The class customizes a <code>java.util.logging.Logger</code> object.
 * <br>
 * <br> The adjustment is done by a dialog, which is created in the 
 * static initializer.
 * <br>
 * <br> Additional the class provides the possibility to store the log
 * messages in a text file called '%uBodgittScarper_Log.txt'.
 * <br>
 * <br> Beware to use logger messages costs performance.  
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class LoggerControl{
	
	/**
	 * Stores the level of the logger instance.
	 */
	private static Level logLevel;

	/**
	 * Adjusts whether the log messages will be
	 * overridden by each start or appended,
	 * if log files will be used.
	 */
	 private static boolean append = false;
	 
	 /**
	  * If true, the application creates logging files.
	  */
	 private static boolean createLogFiles = false;

	/**
	 * Stores all log messages, if log files will be used.
	 * The file is stored in the currently working directory
	 * and is called "%uBodgittScarper_Log.txt".
	 */
	 private static FileHandler file = null;	 
	 
	 
	 /**
	  * Static initializer, which displays a dialog
	  * to just the level of the logging messages
	  * and choose whether logging messages will be stored.
	  * If logging messages should be stored, it is possible
	  * to choose, whether old messages will be overridden
	  * or new messages will be appended to the old one.
	  */
//	 static{
//		 
//		 final String message = 
//		 	"The application is able to monitor almost all actions, "
//		 	+ "because this is 'a learning exercise' "
//			+ "\napplication. Usually 'problem diagnostic' is the "
//			+ "'main target' of log messages."
//			+ "\nBefore You can start the application You have to "
//			+ "fix on which level logging messages"
//			+ "\nshould be gathered. The logging messages will be "
//			+ "displayed at the command line."
//			+ "\nAdditional logging messages could be stored in a file. "
//			+ "The file is called:"
//			+ "\n'(d)BodgittScarper_Log.txt'"
//			+ "\nand  will be placed in the current working directory "
//			+ "like 'runme.jar'. You can choose,"
//			+ "\nwether old messages will be overridden ('Level - "
//			+ "Create/Override Log files') or old"
//			+ "\nmessages will be saved und new messages will be "
//			+ "appended "
//			+ "\n('Level - append LOG message')."
//			+ "\n"
//			+ "\nBe aware: logger messages cost memory, especially if "
//			+ "they were stored in a file!"
//			+ "\n"
//			+ "\nTo open the command line: Windows 8 - Windows > System  "
//			+ "or Mac - Terminal"
//			+ "\n"
//			+ "\nLogger Level:";
//		 
//		 if(logLevel == null){
//			 
//			 //test
//			 System.out.println("" + append);
//			 			 
//				final String[] opt = new String[25];
//		    	opt[0] = Level.OFF.toString();
//		    	opt[1] = Level.SEVERE.toString();
//		    	opt[2] = Level.WARNING.toString();
//		    	opt[3] = Level.INFO.toString();
//		    	opt[4] = Level.CONFIG.toString();
//		    	opt[5] = Level.FINE.toString();
//		    	opt[6] = Level.FINER.toString();
//		    	opt[7] = Level.FINEST.toString();
//		    	opt[8] = Level.ALL.toString();
//		    	opt[9] = Level.SEVERE.toString() + " - Create/Override Log files";
//		    	opt[10] = Level.WARNING.toString() + " - Create/Override Log files";
//		    	opt[11] = Level.INFO.toString() + " - Create/Override Log files";
//		    	opt[12] = Level.CONFIG.toString() + " - Create/Override Log files";
//		    	opt[13] = Level.FINE.toString() + " - Create/Override Log files";
//		    	opt[14] = Level.FINER.toString() + " - Create/Override Log files";
//		    	opt[15] = Level.FINEST.toString() + " - Create/Override Log files";
//		    	opt[16] = Level.ALL.toString() + " - Create/Override Log files";
//		    	opt[17] = Level.SEVERE.toString() + " - " + "append LOG message";
//		    	opt[18] = Level.WARNING.toString() + " - " + "append LOG message";
//		    	opt[19] = Level.INFO.toString() + " - " + "append LOG message";
//		    	opt[20] = Level.CONFIG.toString() + " - " + "append LOG message";
//		    	opt[21] = Level.FINE.toString() + " - " + "append LOG message";
//		    	opt[22] = Level.FINER.toString() + " - " + "append LOG message";
//		    	opt[23] = Level.FINEST.toString() + " - " + "append LOG message";
//		    	opt[24] = Level.ALL.toString() + " - " + "append LOG message";
//		    	
//		    	
//		    	//test
//		    	//final String input = "OFF";
//		    	//original
//				final String input = (String) JOptionPane
//						.showInputDialog(null,
//								message,
//								"Bodgitt & Scarper - Logger Message Dialog", 
//								JOptionPane.INFORMATION_MESSAGE, 
//								null, 							
//								opt, 
//								opt[0]);
//				
//				if (input == null) {
//					System.exit(0);
//				}else if (input.equals("OFF")) {
//					logLevel = Level.OFF;
//				} else if (input.equals("SEVERE")) {
//					logLevel = Level.SEVERE;
//				} else if (input.equals("WARNING")) {
//					logLevel = Level.WARNING;
//				} else if (input.equals("INFO")) {
//					logLevel = Level.INFO;
//				} else if (input.equals("CONFIG")) {
//					logLevel = Level.CONFIG;
//				} else if (input.equals("FINE")) {
//					logLevel = Level.FINE;
//				} else if (input.equals("FINER")) {
//					logLevel = Level.FINER;
//				} else if (input.equals("FINEST")) {
//					logLevel = Level.FINEST;
//				} else if (input.equals("ALL")) {
//					logLevel = Level.ALL;
//				}else if (input.equals("SEVERE - Create/Override Log files")) {
//					logLevel = Level.SEVERE;
//					createLogFiles = true;
//				} else if (input.equals("WARNING - Create/Override Log files")) {
//					logLevel = Level.WARNING;
//					createLogFiles = true;
//				} else if (input.equals("INFO - Create/Override Log files")) {
//					logLevel = Level.INFO;
//					createLogFiles = true;
//				} else if (input.equals("CONFIG - Create/Override Log files")) {
//					logLevel = Level.CONFIG;
//					createLogFiles = true;
//				} else if (input.equals("FINE - Create/Override Log files")) {
//					logLevel = Level.FINE;
//					createLogFiles = true;
//				} else if (input.equals("FINER - Create/Override Log files")) {
//					logLevel = Level.FINER;
//					createLogFiles = true;
//				} else if (input.equals("FINEST - Create/Override Log files")) {
//					logLevel = Level.FINEST;
//					createLogFiles = true;
//				} else if (input.equals("ALL - Create/Override Log files")) {
//					logLevel = Level.ALL;
//					createLogFiles = true;
//				}else if (input.equals("SEVERE - append LOG message")) {
//					logLevel = Level.SEVERE;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("WARNING - append LOG message")) {
//					logLevel = Level.WARNING;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("INFO - append LOG message")) {
//					logLevel = Level.INFO;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("CONFIG - append LOG message")) {
//					logLevel = Level.CONFIG;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("FINE - append LOG message")) {
//					logLevel = Level.FINE;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("FINER - append LOG message")) {
//					logLevel = Level.FINER;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("FINEST - append LOG message")) {
//					logLevel = Level.FINEST;
//					createLogFiles = true;
//					append = true;
//				} else if (input.equals("ALL - append LOG message")) {
//					logLevel = Level.ALL;
//					createLogFiles = true;
//					append = true;
//				}
//			}
////			if(file == null && createLogFiles == true){
////				try {
////					file = new FileHandler("%uBodgittScarper_Log.txt", append);
////				} catch (final SecurityException e) {
////					ExceptionDialog.handleException("Looger Control: " 
////							+ e.getMessage());
////				} catch (final IOException e) {
////					ExceptionDialog.handleException("Looger Control: " 
////							+ e.getMessage());
////				}
////				final SimpleFormatter formatter = new SimpleFormatter();
////				file.setFormatter(formatter);
////			}		
//	 }

	/**
	 * Private constructors, because all methods marked 'static'.
	 */
	private LoggerControl() {
		// unused
	}

	/**
	 * Customizes the logger object. 
	 * 
	 * @param l
	 *            The Logger, which should be customized.
	 * @param level
	 *            Sets the lowest level. If set to 'OFF',
	 *            the adjustment via the dialog displayed
	 *            during the static initializer is running
	 *            will be disregarded.
	 * @return Logger - The customized Logger object.
	 */
	public static Logger getLoggerBS(final Logger l, final Level level) {
//		final Handler console = new ConsoleHandler();		
//		l.setUseParentHandlers(false);
//		final Handler[] h = l.getHandlers();
//		for (int i = 0; i < h.length; i++) {
//			l.removeHandler(h[i]);
//		}		
//		// The value is always 'Level.ALL'
//		console.setLevel(level);
//		// Sets the Level to all Logger instances.
//		l.setLevel(logLevel);		
//		l.addHandler(console);		
//		// If log files will be used:
//		if(createLogFiles){
//			l.addHandler(file);
//		}
		return l;
	}
}
