package suncertify.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import suncertify.db.LoggerControl;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.buyer.MainWindowBuyer;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.server.ServerWindow;
import suncertify.gui.server.ServerWindowEnhanced;

/**
 * The Bodgitt and Scarper application loader - a facade to the three modes (alone,
 * server, network client). Additional the application provides three different
 * client roles (Admin, Buyer, Seller).
 * <br>
 * <br>
 * The class checks the command line arguments and calls the classes to
 * start the application in the correct mode.<br>
 * The acceptable command line arguments are:<br>
 * none - starts a network client<br>
 * "alone" - starts a local client<br>
 * "server" - starts the server<br>
 * <br>
 * If the network client can not connect to a server a dialog
 * will be displayed, which enables the user to start any mode (alone,
 * server, network client).
 * 
 * @see MainWindowBuyer
 * @see MainWindowSeller
 * @see MainWindowAdmin
 * @see ConfigStartOptionsDialog
 * @author stefan.streifeneder@gmx.de
 */
public class ApplicationRunner {
	
	/**
	 * A Logger object to monitor the construction of an object of the class.
	 */
	private final Logger logInstance = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.ApplicationRunner"),
			Level.ALL);

	/**
	 * A Logger object to monitor the program's static methods. 
	 */
	private static Logger logStatic;
	
	/**
	 * Stores the mode, whether the regular server will run or the
	 * enhanced server. Default is false, which means the regular server
	 * will run.
	 */
	private static boolean serverEnhanced;
	
	/**
	 * Stores the mode, whether the enhanced version of the application
	 * for clients will run or not. Default is false, which means the
	 * regular version will run.
	 */
	private static boolean clientEnhanced;
	
	/**
	 * Stores whether the content of the table will be updated
	 * automatically.
	 */
	private static boolean runUpdate;
	
	/**
	 * Stores the time, how long a period lasts until the content
	 * of the table will be updated.
	 */
	private static long timeUpdate;
	
	/**
	 * The ID of a client.
	 */
	private static int idClient;
	
	/**
	 * Stores the application mode.
	 */
	private static ApplicationMode applicationMode;
	
	/**
	 * Initializer for static objects.
	 */
	static {
		ApplicationRunner.logStatic = LoggerControl.getLoggerBS(
				Logger.getLogger("suncertify.gui.ApplicationRunner"),
				Level.ALL);
	}

	/**
	 * Constructor to start a default client, which is always
	 * a Buyer client. The access could be local or remote.
	 * @throws GuiControllerException Thrown, if starting failed.
	 */
	private ApplicationRunner() throws GuiControllerException {
		this.logInstance.entering("ApplicationRunner", 
				"ApplicationRunner");
		new MainWindowBuyer(applicationMode, idClient);
		this.logInstance.exiting("ApplicationRunner", "ApplicationRunner");
	}
	
	/**
	 * Overloaded constructor to start the server.
	 * If the method argument is true, the enhanced server
	 * version will run.
	 *  
	 * @param enhancedServer Runs the enhanced server, if true.
	 */
	private ApplicationRunner(final boolean enhancedServer) {
		this.logInstance.entering("ApplicationRunner", "ApplicationRunner",
				Boolean.valueOf(enhancedServer));	
		if(enhancedServer){
			new ServerWindowEnhanced();
		}else{
			new ServerWindow();
		}
		this.logInstance.exiting("ApplicationRunner", "ApplicationRunner");
	}
	
	/**
	 * Overloaded constructor to start all client roles (Buyer, Seller and
	 * Admin). The access can be local or remote.
	 * 
	 * 
	 * @param appMode The application mode.
	 * @param id
	 *            The ID number of the client.
	 * @param clientRole The different roles a client can appear.
	 * @throws GuiControllerException
	 *             If starting failed.
	 */
	private ApplicationRunner(final ApplicationMode appMode, 
			final int id, final ClientRole clientRole) throws GuiControllerException {		
		this.logInstance.entering("ApplicationRunner", "ApplicationRunner",
				new Object[]{appMode, Integer.valueOf(id), clientRole});		
		if (clientRole == ClientRole.BUYER) {
			if(appMode == ApplicationMode.NETWORK_CLIENT){				
				if(!runUpdate){
					new MainWindowBuyer(appMode, id);
				}else{
					new MainWindowBuyer(id, timeUpdate);
				}
			}else if(appMode == ApplicationMode.LOCAL_CLIENT){					
				new MainWindowBuyer(appMode, id);
			}			
		} else if (clientRole == ClientRole.ADMIN) {
			if(appMode.equals(ApplicationMode.NETWORK_CLIENT)){				
				if(!runUpdate){
					new MainWindowAdmin(appMode, id);
				}else{
					new MainWindowAdmin(id, timeUpdate);
				}
			}else if(appMode == ApplicationMode.LOCAL_CLIENT){
				new MainWindowAdmin(appMode, id);
			}
		} else if (clientRole == ClientRole.SELLER) {	
			if (appMode.equals(ApplicationMode.NETWORK_CLIENT)) {
				if (!runUpdate) {
					new MainWindowSeller(appMode, id);
				} else {
					new MainWindowSeller(id, timeUpdate);
				}
			} else if (appMode.equals(ApplicationMode.LOCAL_CLIENT)) {
				new MainWindowSeller(appMode, id);
			}			
		}
		this.logInstance.exiting("ApplicationRunner", "ApplicationRunner");
	}	

	/**
	 * Start of the Program.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(final String[] args) {
		ApplicationRunner.logStatic.info("ApplicationRunner, main, " 
					+ "args length: " + Integer.valueOf(args.length));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ApplicationRunner.startApp(args);
			}
		});
	}

	/**
	 * Starts the application. At first place it creates an object of
	 * type <code>suncertify.gui.ConfigStartOptionsDialog</code>.
	 * Afterwards it calls according to the adjustments the different 
	 * constructors.
	 * 
	 * @param args
	 *            Command line arguments.
	 * 
	 */
	static void startApp(final String[] args) {
		ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " + "start");
		ConfigStartOptionsDialog configStart = new ConfigStartOptionsDialog(args);
		final ApplicationMode appMode = configStart.getApplicationMode();
		if (appMode == ApplicationMode.SERVER) {			
			serverEnhanced = configStart.isServerEnhanced();
			if(serverEnhanced){
				new ApplicationRunner(serverEnhanced);
			}else{
				new ApplicationRunner(false);
			}
		} else {// starts a client
			applicationMode = appMode;			
			final int id = configStart.getIdOwner();
			idClient = id;
			final ClientRole clientMode = configStart.getClientRole();		
			clientEnhanced = configStart.isClientEnhanced();			
			if(clientEnhanced){
				runUpdate = configStart.getIsUpdateRunning();
				timeUpdate = configStart.getUpdateInterval();
			}
			boolean runUntillAppStarts = true;
			while (runUntillAppStarts) {
				try {	
					if(clientEnhanced){
						new ApplicationRunner(appMode, id, clientMode);
						runUntillAppStarts = false;
					}else{
						new ApplicationRunner();
						runUntillAppStarts = false;
					}
				} catch (final GuiControllerException e) {
					ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " 
							+ e.getLocalizedMessage());
					StartMonitor.disposeStartMonitor();
					final String input = (String) JOptionPane.showInputDialog(null,
							"The Application failed to connect to the server, because"
							+ "\n"
							+ "\n" + e.getMessage()
							+ "\n"
							+ "\nPlease choose beetween:" 
							+ "\n'alone': Desktop-Application"
							+ "\n'server': Start Server running (no Client!)"
							+ "\n'network_client': Connects Client with Server",
							"B & S - Mode Chooser", 
							JOptionPane.INFORMATION_MESSAGE,
							null,
							new Object[] { "alone", "server", 
									"exit(abbrechen)", "network_client" }, "alone");
					if ("alone".equalsIgnoreCase(input)) {
						try {
							if(clientEnhanced){								
								new ApplicationRunner(ApplicationMode.LOCAL_CLIENT, id
										, clientMode);
								runUntillAppStarts = false;
							}else{
								applicationMode = ApplicationMode.LOCAL_CLIENT;
								new ApplicationRunner();
								runUntillAppStarts = false;
							}
						} catch (final GuiControllerException gce) {
							ExceptionDialog.handleException(
									"ApplicationStarter - local client failed to " 
											+ "connect to the database!"
											+ "\n"
											+ "\nCaused: " 
											+ gce.getLocalizedMessage());
							ApplicationRunner.logStatic.severe(
									"ApplicationRunner, startApp, " + "Exception: " 
											+ gce.getLocalizedMessage());
						}
						ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " 
								+ ApplicationMode.LOCAL_CLIENT
								+ " - ID: " + id + " - mode: " + clientMode);
					} else if ("server".equalsIgnoreCase(input)) {						
						// enhanced server or not
						boolean admin = true;
						final int n = JOptionPane.showConfirmDialog(null,
								"You want to start the server. There is an "
								+ "enhanced server "
								+ "available and a regular version."
								+ ""
								+ "\nPlease press 'yes' if you want to run the "
								+ "enhanced server."
								+ "\n"
								+ "\nKeystrokes: "
								+ "\nEnhanced server - please press the Spacebar"
								+ "\nRegular server - please use 'Alt'+'N'.",
								"Server - Enhanced Functions", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.NO_OPTION) {
							admin = false;
						}else if(n == JOptionPane.CLOSED_OPTION){
							System.exit(0);							
						}
						new ApplicationRunner(admin);
						runUntillAppStarts = false;
					} else if ("exit(abbrechen)".equalsIgnoreCase(input)) {
						ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " 
								+ "Applications exits!");
						System.exit(0);
					} else if (input == null) {
						ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " 
								+ "Applications exits!");
						System.exit(0);
					} else if (appMode == ApplicationMode.NETWORK_CLIENT) {
						// jumps to the start of the loop
					} else {
						ApplicationRunner.logStatic.severe("ApplicationRunner, startApp, " 
								+ "Applications exits!");
						System.exit(0);
					}
				} // end catch Exception
			} // while loop
		}
	}
}
