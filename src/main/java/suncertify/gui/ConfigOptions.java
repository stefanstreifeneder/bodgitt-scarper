package suncertify.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import suncertify.db.LoggerControl;

/**
 * A common panel used by both the client and the server application to specify the
 * configuration options. For the most part these configuration options are the
 * same for either a client and the server (even if used in different ways). 
 * For example the port the server will listen on must be the same port the 
 * client will use to connect to the server. So it makes sense to have 
 * a common panel so that users can be presented with a familiar layout for 
 * all three application modes.
 * <br>
 * <br>
 * The class has overridden its method 'addPropertyChangeListener()' and 
 * possesses an object of type <code>java.beans.PropertyChangeSupport</code>,
 * which enables PropertyChangeListener to monitor changes done to the connection
 * data. This design substitutes the observer design pattern
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class ConfigOptions extends JPanel {
	
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 202L;

	/**
	 * Maximum number (65536) of ports.
	 */
	private static final int HIGHEST_PORT_NO = 65536;
	/**
	 * Default tool tip associated with the locationField.
	 */
	private static final String DB_HD_LOCATION_TOOL_TIP = 
			"The location of the database on an accessible hard drive";

	/**
	 * Tool tip associated with the combo box displaying the ipaddress.
	 */
	private static final String DB_IP_LOCATION_TOOL_TIP = 
			"The server where the database is located (IP address)";

	/**
	 * Sets the text of the JLabel associated with the locationField.
	 */
	private static final String DB_LOCATION_LABEL = "Database location: "
			+ "jdbc:mysql://localhost/";

	/**
	 * Sets the text of the JLabel associated with combo box.
	 */
	private static final String SERVER_IP_ADDRESS_LABEL = "Server ip-address: ";

	/**
	 * Sets the text of the JLabel associated with the port number.
	 */
	private static final String SERVER_PORT_LABEL = "Server port: ";

	/**
	 * Tool tip associated with the port number.
	 */
	private static final String SERVER_PORT_TOOL_TIP = "The port number the Server uses to listens for requests ( between"
			+ " 1 - 65535 )";

	/**
	 * The ipaddress of running computer.
	 */
	private final String comboIpMyIp = MyInetAddress.getIpAddressString();

	/**
	 * Field to display the path of the location of the database file.
	 */
	private final JTextField locationField = new JTextField(25);
	
	/**
	 * Field to display the password of the database file.
	 */
	private final JTextField passwordField = new JTextField(25);

	/**
	 * Field for the port number.
	 */
	private final JTextField portNumberField = 
			new PositiveIntegerField(ConfigOptions.HIGHEST_PORT_NO, 5);

	/**
	 * Helps to track changes happen to the conncetion data.
	 */
	private PropertyChangeSupport changes = 
						new PropertyChangeSupport(this);

	/**
	 * Stores the application mode.
	 */
	private ApplicationMode applicationMode;

	/**
	 * A combo box to display ipaddresses or 'localhost'
	 */
	private JComboBox<String> combo;

	/**
	 * Stores the ipaddress, which is used by a client to connect
	 * to the server.
	 */
	private String ipAddress;

	/**
	 * The path to the database file.
	 */
	private String location;
	
	/**
	 * The password of the database.
	 */
	private String password;

	/**
	 * Stores the port number.
	 */
	private String port = null;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.ConfigOptions</code>. Default access, because the
	 * instance will be accesses in an inner class.
	 */
	Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.ConfigOptions"), Level.ALL);

	/**
	 * The browse button for searches in the file system.
	 */
	JButton browseButton = new JButton("...");
	
	/**
	 * Creates a new instance of <code>suncertify.gui.ConfigOptions</code> - the panel to
	 * configure database connectivity.
	 * 
	 * @param applicationModePara
	 *            One of <code>LOCAL_CLIENT</code>,
	 *            <code>NETWORK_CLIENT_MODE</code> or <code>SERVER</code>.
	 */
	public ConfigOptions(final ApplicationMode applicationModePara) {
		super();
		this.log.entering("ConfigOptions", "ConfigOptions", 
				applicationModePara);		
		this.applicationMode = applicationModePara;
		final GridBagLayout gridbag = new GridBagLayout();
		final GridBagConstraints constraints = new GridBagConstraints();
		this.setLayout(gridbag);
		constraints.insets = new Insets(2, 2, 2, 2);		

		// local or server
		if ((applicationModePara == ApplicationMode.LOCAL_CLIENT) | 
					(applicationModePara == ApplicationMode.SERVER)) {
			final JLabel dbLocationLabel = new JLabel(ConfigOptions.DB_LOCATION_LABEL);
			gridbag.setConstraints(dbLocationLabel, constraints);
			this.add(dbLocationLabel);
			this.locationField.setToolTipText(ConfigOptions.DB_HD_LOCATION_TOOL_TIP);
			constraints.gridwidth = GridBagConstraints.RELATIVE;
			this.locationField.setName(ConfigOptions.DB_LOCATION_LABEL);
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row			
			gridbag.setConstraints(this.locationField, constraints);
			this.add(this.locationField);
			
			constraints.weightx = 0.0;
			final JLabel passwordLabel = new JLabel("Password: user=root&password=");
			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(passwordLabel, constraints);
			this.add(passwordLabel);
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(this.passwordField, constraints);
			this.add(this.passwordField);			
			
		}

		// server or network_client
		if ((applicationModePara == ApplicationMode.NETWORK_CLIENT) | 
					(applicationModePara == ApplicationMode.SERVER)) {
			constraints.weightx = 0.0;
			final JLabel serverPortLabel = new JLabel(ConfigOptions.SERVER_PORT_LABEL);
			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(serverPortLabel, constraints);
			this.add(serverPortLabel);
			this.portNumberField.setColumns(4);
			this.portNumberField.setToolTipText(ConfigOptions.SERVER_PORT_TOOL_TIP);
			this.portNumberField.setName(ConfigOptions.SERVER_PORT_LABEL);
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(this.portNumberField, constraints);
			this.add(this.portNumberField);
			constraints.weightx = 0.0;
			final JLabel ipAddressLabel = new JLabel(ConfigOptions.SERVER_IP_ADDRESS_LABEL);
			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(ipAddressLabel, constraints);
			this.add(ipAddressLabel);

			// only server
			if (applicationModePara == ApplicationMode.SERVER) {
				constraints.weightx = 0.0;
				final JLabel ipLabel = new JLabel(MyInetAddress.getIpAddressString());
				constraints.gridwidth = 1;
				constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
				constraints.anchor = GridBagConstraints.WEST;
				gridbag.setConstraints(ipLabel, constraints);
				this.add(ipLabel);
			}

			// only network_client
			if (applicationModePara == ApplicationMode.NETWORK_CLIENT) {
				final String[] ipStrings = 
						new String[] { this.comboIpMyIp, 
								"localhost"};
				this.combo = new JComboBox<String>(ipStrings);
				this.combo.setEditable(true);
				this.combo.setName(ConfigOptions.SERVER_IP_ADDRESS_LABEL);
				this.combo.setToolTipText(ConfigOptions.DB_IP_LOCATION_TOOL_TIP);
				constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
				constraints.anchor = GridBagConstraints.WEST;
				gridbag.setConstraints(this.combo, constraints);
				this.add(this.combo);
			}
		}
		this.log.exiting("ConfigOptions", "ConfigOptions");
	}

	/**
	 * Adds a property change listener to the panel.
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		try {
			this.changes.addPropertyChangeListener(l);
		}catch(Exception e) {
			// the only call to avoid a NullPointerException,
			// if javax.swing.plaf.NimbusLookAndFeel
			System.out.println("ConfigOptions, addPropertyChangeListener,"
								+ " e: " + e.getMessage());
		}
	}

	/**
	 * Removes a property change listener to the panel.
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
	    this.changes.removePropertyChangeListener(l);
	}

	/**
	 * Returns the ipaddress.
	 * 
	 * @return String - The ipaddress.
	 */
	public String getIpAddressFieldText() {
		this.log.entering("ConfigOptions", "getIpAddressFieldText");
		this.log.exiting("ConfigOptions", "getIpAddressFieldText", 
				this.combo.getSelectedItem());
		return (String) this.combo.getSelectedItem();
	}

	/**
	 * Sets the ipaddress.
	 * 
	 * @param ipAddressParam
	 *            The ipaddress.
	 */
	public void setIpAddressFieldText(final String ipAddressParam) {
		this.log.entering("ConfigOptions", "setIpAddressFieldText", ipAddressParam);
		this.ipAddress = ipAddressParam;
		if(this.applicationMode == ApplicationMode.NETWORK_CLIENT){
			this.combo.setSelectedItem(ipAddressParam);
		}
		this.log.exiting("ConfigOptions", "setIpAddressFieldText");
	}

	/**
	 * Enables or disables the ipaddress field.
	 * 
	 * @param enabled
	 *            Enables or disables the ip field.
	 */
	public void setIpAddressFieldEnabled(final boolean enabled) {
		this.log.entering("ConfigOptions", "setIpAddressFieldEnabled", 
				Boolean.valueOf(enabled));
		this.combo.setEnabled(enabled);
		this.log.exiting("ConfigOptions", "setIpAddressFieldEnabled");
	}

	/**
	 * Returns the Application Mode.
	 * 
	 * @return ApplicationMode - One of <code>LOCAL_CLIENT</code>,
	 *         <code>NETWORK_CLIENT_MODE</code> or <code>SERVER</code>.
	 */
	public ApplicationMode getApplicationMode() {
		this.log.entering("ConfigOptions", "getApplicationMode");
		this.log.exiting("ConfigOptions", "getApplicationMode", this.applicationMode);
		return this.applicationMode;
	}

	/**
	 * Returns the location of the database.
	 * 
	 * @return String - The path of the database file.
	 */
	public String getLocationFieldText() {
		this.log.entering("ConfigOptions", "getLocationFieldText");
		this.log.exiting("ConfigOptions", "getLocationFieldText", 
				this.locationField.getText());
		return this.locationField.getText();
	}

	/**
	 * Sets the location of the database file.
	 * 
	 * @param locationFieldText
	 *            The path to the database file.
	 */
	public void setLocationFieldText(final String locationFieldText) {
		this.log.entering("ConfigOptions", "setLocationFieldText", 
				locationFieldText);
		this.location = locationFieldText;
		this.locationField.setText(locationFieldText);
		this.locationField.setToolTipText(this.location);
		this.log.exiting("ConfigOptions", "setLocationFieldText");
	}

	/**
	 * Enables or disables the location field.
	 * 
	 * @param enabled
	 *            Enables or disables the <code>javax.swing.JTextField</code> of the
	 *            location.
	 */
	public void setLocationFieldEnabled(final boolean enabled) {
		this.log.entering("ConfigOptions", "setLocationFieldEnabled", 
				Boolean.valueOf(enabled));
		this.locationField.setEnabled(enabled);
		this.log.exiting("ConfigOptions", "setLocationFieldEnabled");
	}
	
	/**
	 * Returns the location of the database.
	 * 
	 * @return String - The path of the database file.
	 */
	public String getPasswordFieldText() {		
		return this.passwordField.getText();
	}

	/**
	 * Sets the location of the database file.
	 * 
	 * @param locationFieldText
	 *            The path to the database file.
	 */
	public void setPasswordText(final String locationFieldText) {
		this.password = locationFieldText;
		this.passwordField.setText(locationFieldText);
		this.passwordField.setToolTipText(this.password);
	}

	/**
	 * Enables or disables the location field.
	 * 
	 * @param enabled
	 *            Enables or disables the <code>javax.swing.JTextField</code> of the
	 *            location.
	 */
	public void setPasswordFieldEnabled(final boolean enabled) {
		this.passwordField.setEnabled(enabled);
	}

	/**
	 * Enables or disables the Browser Button.
	 * 
	 * @param enabled
	 *            Enables or disables the Browser Button.
	 */
	public void setBrowseButtonEnabled(final boolean enabled) {
		this.log.entering("ConfigOptions", "setBrowseButtonEnabled", 
				Boolean.valueOf(enabled));
		this.browseButton.setEnabled(enabled);
		this.log.exiting("ConfigOptions", "setBrowseButtonEnabled");
	}

	/**
	 * Returns the port number.
	 * 
	 * @return String - The port number as a <code>java.lang.String</code>.
	 */
	public String getPortNumberText() {
		this.log.entering("ConfigOptions", "getPortNumberText");
		this.log.exiting("ConfigOptions", "getPortNumberText", 
				this.portNumberField.getText());
		return this.portNumberField.getText();
	}

	/**
	 * Sets the port number.
	 * 
	 * @param portNumber
	 *            The port number.
	 */
	public void setPortNumberText(final String portNumber) {
		this.log.entering("ConfigOptions", "setPortNumberText", portNumber);
		this.port = portNumber;
		this.portNumberField.setText(portNumber);
		this.log.exiting("ConfigOptions", "setPortNumberText");
	}

	/**
	 * Enables or disables the port number field.
	 * 
	 * @param enabled
	 *            Enables or disables the <code>javax.swing.JTextField</code> for the port number.
	 */
	public void setPortNumberEnabled(final boolean enabled) {
		this.log.entering("ConfigOptions", "setPortNumberEnabled", 
				Boolean.valueOf(enabled));
		this.portNumberField.setEnabled(enabled);
		this.log.exiting("ConfigOptions", "setPortNumberEnabled");
	}

	/**
	 * This method is a facade to the the private method
	 * <code>validateAll</code>.
	 * 
	 * @return boolean - True if user input is correct.
	 * @throws IllegalArgumentException
	 *             If the arguments do not fit.
	 */
	public boolean validateConnectionConfig() throws IllegalArgumentException {
		this.log.entering("ConfigOptions", "validateConnectionConfig");
		boolean inputOk = false;
		inputOk = this.validateAll();
		this.log.exiting("ConfigOptions", "validateConnectionConfig", 
				Boolean.valueOf(inputOk));
		return inputOk;
	}

	/**
	 * Sets key listeners for the location field, the port number field and the
	 * combo box, which displays the ipaddress.
	 * 
	 * @param ka
	 *            The KeyAdapter.
	 */
	public void setKeyListener(final KeyAdapter ka) {
		this.addKeyListener(ka);
		this.locationField.addKeyListener(ka);
		this.portNumberField.addKeyListener(ka);
		if(this.applicationMode == ApplicationMode.NETWORK_CLIENT){
			final Component[] comps = this.combo.getComponents();
			for (int i = 0; i < comps.length; i++) {
				comps[i].addKeyListener(ka);
			}
		}
	}

	/**
	 * Validates connection data.
	 * 
	 * @return boolean - True, if user input is correct.
	 * @throws IllegalArgumentException
	 *             If the configurations arguments do not fit.
	 */
	private boolean validateAll() throws IllegalArgumentException {
		this.log.entering("ConfigOptions", "validateAll");
		boolean inputOk = true;
		final ApplicationMode finAppMode = this.getApplicationMode();
		if ( (finAppMode == ApplicationMode.NETWORK_CLIENT) | 
										(finAppMode == ApplicationMode.SERVER) ) {
			final Integer portNumber = 
					Integer.valueOf(this.portNumberField.getText());
			//port
			if ((portNumber.intValue() > ConfigOptions.HIGHEST_PORT_NO) | 
												(portNumber.intValue() <= 0)) {
				inputOk = false;
				this.log.severe("ConfigOptions, validateAll, wrong port number: " + portNumber
						+ ", a IllegalArgumentException will be thrown.");
				throw new IllegalArgumentException(
						"Input: " + portNumber + "(Failed). " + "Port value must between 1 - 65.536!");
			} else if (!portNumber.toString().equals(this.port)) {
				this.changes.firePropertyChange("PORT", this.port, portNumber);
				this.port = portNumber.toString();
				this.log.fine("ConfigOptions, validateAll, port changed to: " + this.port);
			}
			
			//ip 
			if (finAppMode == ApplicationMode.NETWORK_CLIENT) {
				final ComboBoxEditor cb = this.combo.getEditor();
				final String ipSelected = cb.getItem().toString();
				if (ipSelected.matches("localhost")) {
					this.log.fine("ConfigOptions, validateAll, " + "'localhost' is selected");
					inputOk = true;
				} else if (!ipSelected.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
					this.log.severe("ConfigOptions, validateAll, wrong 'ip': " + ipSelected);
					throw new IllegalArgumentException("IP: " + ipSelected + " - Failed!");
				}
				final String ipString = ipSelected;
				if (!ipString.equals(this.ipAddress)) {
					this.changes.firePropertyChange("IP", this.ipAddress, ipString);
					this.log.fine("ConfigOptions, validateAll, ip changed to: " + this.ipAddress);
				}
			}
		}
		
		
		if ( (finAppMode == ApplicationMode.LOCAL_CLIENT) | 
				(finAppMode == ApplicationMode.SERVER) ) {
			String loc = this.locationField.getText();
			if(!loc.equals(this.location)) {
				this.changes.firePropertyChange("DB", this.location, loc);
			}
			String pw = this.passwordField.getText();
			if(!pw.equals(this.password)) {
				this.changes.firePropertyChange("PASSWORD", 
						this.password, pw);
			}
		}
		
		
		this.log.exiting("ConfigOptions", "validateAll", Boolean.valueOf(inputOk));
		return inputOk;
	}

	
}
