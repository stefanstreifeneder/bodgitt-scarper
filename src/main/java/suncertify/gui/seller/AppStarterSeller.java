package suncertify.gui.seller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import suncertify.db.LoggerControl;
import suncertify.gui.ApplicationMode;
import suncertify.gui.ClientRole;
import suncertify.gui.ConfigStartOptionsDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.StartMonitor;

/**
 * Starts only the Seller client for a network connection. This class is made
 * for future deployment, if the application is departed and an individual
 * application for a Seller can be provided.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AppStarterSeller {

	/**
	 * A Logger object to monitor the program. 
	 */
	private static Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.AppStarterSeller"),
			Level.ALL);

	/**
	 * Constructor to start the Seller client in a network environment.
	 * 
	 * @param appMode
	 *            - The application mode.
	 * @param id
	 *            - The ID number of the client.
	 * @param clientMode
	 *            - Indicates which client mode (always Seller) should run.
	 * @throws GuiControllerException
	 *             - Wraps exceptions to handle them at a different location of
	 *             the program.
	 * 
	 */
	private AppStarterSeller(ApplicationMode appMode, int id, ClientRole clientMode) throws GuiControllerException {
		AppStarterSeller.log.entering("AppStarterSeller", "AppStarterSeller",
				new Object[] { appMode, Integer.valueOf(id), clientMode });
		new MainWindowSeller(appMode, id);
		AppStarterSeller.log.exiting("AppStarterSeller", "AppStarterSeller");
	}

	/**
	 * Start of the Program.
	 * 
	 * @param args
	 *            - Command line arguments.
	 */
	public static void main(String[] args) {
		Logger.getAnonymousLogger().info("AppStarterSeller, main");
		final String[] ar = args;
		if (args.length != 0) {
			JOptionPane.showMessageDialog(null, "You run a Seller client, "
					+ "which is a derivate of Bodgitt and Scarper Appplication."
					+ "\nThis Version has to run in network mode." + "\n'" 
					+ args[0] + "' is not a acceptable "
					+ "command line argument!" + "\nThe Application automatically "
							+ "stops. "
					+ "\nPlease enter an appropriate command " + "line argument "
							+ "and start the application again!");
			System.exit(-1);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					AppStarterSeller.startApp(ar);
				}
			});
		}
	}

	/**
	 * Starts the application.
	 * 
	 * @param args
	 *            - Command line arguments.
	 * 
	 */
	static void startApp(String[] args) {
		ConfigStartOptionsDialog configStart;
		try {
			configStart = new ConfigStartOptionsDialog(args);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,
					"This is not a acceptable " + "command line argument "
							+ "('alone', 'server' or none!)"
							+ "\nThe program stops! Please choose a "
							+ "appropriate argument!");
			AppStarterSeller.log.severe("AppStarterSeller, startApp, " 
							+ "Exception: " + e.getLocalizedMessage());
			return;
		}
		ApplicationMode appMode = configStart.getApplicationMode();
		int id = configStart.getIdOwner();
		boolean runUntillAppStarts = true;
		ClientRole clientMode = configStart.getClientRole();
		if (!clientMode.equals(ClientRole.SELLER)) {
			JOptionPane alert = new JOptionPane(
					"AppStarter:  The implementation runs always a Seller client!",
					JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
			JDialog dialog = alert.createDialog(null, "Alert");
			dialog.setVisible(true);
		} 
		while (runUntillAppStarts) {
			try {
				new AppStarterSeller(appMode, id, clientMode);// Client
				runUntillAppStarts = false;
			} catch (GuiControllerException e) {
				StartMonitor.disposeStartMonitor();
				String input = (String) JOptionPane.showInputDialog(null,
						"Error:" + e.getMessage() + "\nThe Application failed "
								+ "to start!"
								+ "\nThis Version of Bodgitt&Scarper is a "
								+ "derivative, which "
								+ "runs always in Seller network client mode!"
								+ "\n'network_client': Connects Client "
								+ "with Server",
						"Alert", JOptionPane.YES_NO_OPTION, null, 
						new Object[] { "exit", "network_client" },
						"network_client");

				if ("exit".equalsIgnoreCase(input)) {
					System.exit(0);
				} else if (input == null) {
					System.exit(0);
				} else if (appMode == ApplicationMode.NETWORK_CLIENT) {
					// jumps to the start of the loop
				} else {
					System.exit(0);
				}
			} // Exception
		} // while loop
	}
}
