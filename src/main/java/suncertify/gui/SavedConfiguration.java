package suncertify.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;

/**
 * The class stores and maintains the connection properties ( ipaddress, 
 * port and location of database). <br>
 * It provides read/write access to the user's saved configuration parameters on
 * disk (suncertify.properties), so that next time they connect, 
 * the same configuration parameters are offered as a default. 
 * <br>
 * <br> If there is no entry in the file suncertify.propperties, the class assigns
 * default values:
 * <br> - database file path - the current working directory
 * <br> - port number - 3000
 * <br> - ipaddress - the application automatically finds out the ipaddress of the
 * actual used computer
 * <br>
 * <br> The class uses the singleton design pattern to create the only
 * available instance of the class.
 * 
 * @see MyInetAddress
 * @author stefan.streifeneder@gmx.de
 */
public class SavedConfiguration {

	/**
	 * Property indicating that the value will be the database location.
	 */
	public static final String DATABASE_LOCATION = "DatabaseLocation";

	/**
	 * Property indicating that the value will be the port.
	 */
	public static final String SERVER_PORT = "ServerPort";

	/**
	 * Property indicating that the value will be the ipaddress.
	 */
	public static final String SERVER_IP_ADDRESS = "IpAddress";

	/**
	 * Stores the name (scjddb) of the database file.
	 */
	private final static String DB_NAME = "scjddb";
	
	/**
	 * Stores the password.
	 */
	public final static String PASSWORD = "";

	/**
	 * The location where the configuration file will be saved.
	 */
	private final static String BASE_DIRECTORY = System.getProperty("user.dir");

	/**
	 * The name of the properties file.
	 */
	private final static String OPTIONS_FILENAME = "suncertify.properties";
	
	/**
	 * The stored Configurations.
	 */
	private static SavedConfiguration savedConfiguration = 
			new SavedConfiguration();

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.SavedConfiguration</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.SavedConfiguration"), Level.ALL);

	/**
	 * The file containing the last stored configuration.
	 */
	private File savedOptionsFile = new File(
			BASE_DIRECTORY, OPTIONS_FILENAME);

	/**
	 * The Properties for this application.
	 */
	private Properties parameters;

	/**
	 * There should only ever be one instance of this class (a Singleton), 
	 * so it is private.
	 */
	private SavedConfiguration() {
		this.log.entering("SavedConfiguration", "SavedConfiguration");
		// should never run, because the file exists
		if (!this.savedOptionsFile.isFile()) {			
			try {
				this.savedOptionsFile.createNewFile();
			} catch (IOException ex) {
				this.log.log(Level.SEVERE, "Create suncertify.Properties "
						+ "failed", ex);
				ExceptionDialog
						.handleException("Saved configuration: " 
								+ "suncertify.Properties file failed "
								+ "to create!");
				return;
			}
		}
		this.parameters = loadParametersFromFile();
		if (this.parameters.isEmpty() == true) {
			this.parameters = new Properties();
			this.setParameter(
					SavedConfiguration.DATABASE_LOCATION, DB_NAME);
			this.setParameter(SavedConfiguration.PASSWORD, "isabella");
			this.setParameter(SavedConfiguration.SERVER_PORT, "3000");
			this.setParameter(
					SavedConfiguration.SERVER_IP_ADDRESS, 
					MyInetAddress.getIpAddressString());
		}
		this.log.severe("SavedConfiguration, SavedConfiguration, " 
				+ "\nlocation: "
				+ this.getParameter(SavedConfiguration.DATABASE_LOCATION) 
				+ "\nport: "
				+ this.getParameter(SavedConfiguration.SERVER_PORT) + "\nip: "
				+ this.getParameter(SavedConfiguration.SERVER_IP_ADDRESS));
		this.log.exiting("SavedConfiguration", "SavedConfiguration");
	}

	/**
	 * Returns the single instance of the 
	 * <code>suncertify.gui.SavedConfiguration</code> class.
	 * 
	 * @return SavedConfiguration - The actual connection data.
	 */
	public static SavedConfiguration getSavedConfiguration() {
		return SavedConfiguration.savedConfiguration;
	}

	/**
	 * Returns the value of the named parameter.
	 *
	 * @param parameterName
	 *            The name of the parameter for which the user is requesting the
	 *            value.
	 * @return String - The value of the named parameter.
	 */
	public String getParameter(String parameterName) {
		this.log.entering("SavedConfiguration", "getParameter", parameterName);
		this.log.entering("SavedConfiguration", "getParameter", 
				this.parameters.getProperty(parameterName));
		return this.parameters.getProperty(parameterName);
	}

	/**
	 * Updates the saved parameters with the new values. 
	 *
	 * @param parameterName
	 *            The name of the parameter.
	 * @param parameterValue
	 *            The value to be stored for the parameter.
	 */
	public void setParameter(String parameterName, String parameterValue) {
		this.log.entering("SavedConfiguration", "setParameter", 
				new Object[] { parameterName, parameterValue });
		this.parameters.setProperty(parameterName, parameterValue);
		this.saveParametersToFile();
		this.log.entering("SavedConfiguration", "setParameter");
	}

	/**
	 * Attempts to load the saved parameters from the file so that the user does
	 * not have to reenter all the information.
	 *
	 * @return Properties - Loaded from file.
	 */
	private Properties loadParametersFromFile() {
		this.log.entering("SavedConfiguration", "loadParametersFromFile");
		Properties loadedProperties = null;
		if (this.savedOptionsFile.exists() && this.savedOptionsFile.canRead()) {
			synchronized (this.savedOptionsFile) {
				try {
					FileInputStream fis = 
							new FileInputStream(this.savedOptionsFile);
					loadedProperties = new Properties();
					loadedProperties.load(fis);
					fis.close();
				} catch (FileNotFoundException e) {
					this.log.log(Level.SEVERE, "File not found!", e);
					ExceptionDialog.handleException(
							"Paramter storing failed! " + "File not found! " 
					+ e.getLocalizedMessage());

				} catch (IOException e) {
					this.log.log(Level.SEVERE, "Transmission problems "
							+ "occured!", e);
					ExceptionDialog.handleException(
							"Parameter storing failed. " + "Transmission "
									+ "problems occured! " + e.getLocalizedMessage());
				}
			}
		}
		this.log.exiting("SavedConfiguration", "loadParametersFromFile", 
				loadedProperties);
		return loadedProperties;
	}

	/**
	 * Saves the parameters to a file so that they can be used again next time
	 * the application starts.
	 */
	private void saveParametersToFile() {
		this.log.entering("SavedConfiguration", "saveParametersToFile");
		try {
			synchronized (this.savedOptionsFile) {
				if (this.savedOptionsFile.exists()) {
					this.savedOptionsFile.delete();
				}
				this.savedOptionsFile.createNewFile();
				FileOutputStream fos = 
						new FileOutputStream(this.savedOptionsFile);
				this.parameters.store(fos, "Bodgitt$Scarper configuration");
				fos.close();
			}
		} catch (IOException e) {
			this.log.log(Level.SEVERE, "Transmission problems occured!", e);
			ExceptionDialog.handleException(
					"Parameter storing failed. Transmission problems " 
							+ "occured! " + 
							e.getLocalizedMessage());
		}
	}
}
