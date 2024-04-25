package suncertify.gui.buyer;

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
 * This class is used to start a Buyer client only in a network
 * environment. This class is made for future deployment, if the 
 * application is departed and an individual application for a 
 * Buyer can be provided.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AppStarterBuyer {

	/**
	 * A Logger object to monitor the program. 'Static' modifier 
	 * justified, because the program will be started one time.
	 */
	private static Logger log = 
			LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.AppStarterBuyer"),
			Level.ALL);

	/**
	 * Overloaded Constructor to start the Buyer client.
	 * 
	 * @param appMode
	 *            The application mode.
	 * @param id
	 *            The ID number of the client.
	 * @param clientMode
	 *            Indicates which client mode (always Buyer) should run.
	 * @throws GuiControllerException
	 *             Thrown if connecting fails.
	 * 
	 */
	private AppStarterBuyer(ApplicationMode appMode, int id, 
			ClientRole clientMode) throws GuiControllerException {
		AppStarterBuyer.log.entering("AppStarterBuyer", 
				"AppStarterBuyer", new Object[] { appMode, 
						Integer.valueOf(id), clientMode });
		new MainWindowBuyer(appMode, id);
		AppStarterBuyer.log.exiting("AppStarterBuyer", "AppStarterBuyer");
	}

	/**
	 * Start of the Program.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		AppStarterBuyer.log.severe("AppStarterBuyer, main");
		final String[] ar = args;
		if (args.length != 0) {
			JOptionPane.showMessageDialog(null,
					"You run a Buyer client, which is a derivate of "
					+ "Bodgitt&Scarper Appplication!"
							+ "\nThis Version has to run in network mode." 
							+ "\n'" + args[0] + "' is not a acceptable "
							+ "command line argument! No command line "
							+ "argument is" + " allowed."
							+ "\nThe Application automatically stops.");
			System.exit(-1);

		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					AppStarterBuyer.startApp(ar);
				}
			});
		}
	}

	/**
	 * Starts the application.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	static void startApp(String[] args) {
		ConfigStartOptionsDialog configStart;
		
		try {
			configStart = new ConfigStartOptionsDialog(args);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,
					"This is not a acceptable " + "command line "
							+ "argument ('alone', 'server' or none!)"
							+ "\nThe program stops! Please choose a "
							+ "appropriate argument!");
			AppStarterBuyer.log.severe("AppStarterBuyer, startApp, "
					+ "Exception: " + e.getLocalizedMessage());
			return;
		}
		ApplicationMode appMode = configStart.getApplicationMode();
		int id = configStart.getIdOwner();
		boolean runUntillAppStarts = true;
		ClientRole clientMode = configStart.getClientRole();
		if (!clientMode.equals(ClientRole.BUYER)) {

			JOptionPane alert = new JOptionPane(
					"AppStarter:  " + "The implementation runs always "
							+ "a Buyer client!",
					JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
			JDialog dialog = alert.createDialog(null, "Alert");
			dialog.setVisible(true);
		} 
		while (runUntillAppStarts) {
			try {
				new AppStarterBuyer(appMode, id, clientMode);// Client
				runUntillAppStarts = false;
			} catch (GuiControllerException e) {
				StartMonitor.disposeStartMonitor();
				String input = (String) JOptionPane.showInputDialog(null,
						"Error:" + e.getMessage() + "\nThe Application "
								+ "failed to start!"
								+ "\nThis Version of Bodgitt&Scarper is "
								+ "a derivative, which "
								+ "runs always in Buyer network client "
								+ "mode!"
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
