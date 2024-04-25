package suncertify.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import suncertify.db.LoggerControl;

/**
 * The class is a <code>java.awt.event.WindowAdapter</code>.
 * It provides a graphical surface for a client to connect to the database remotely 
 * or locally. It possesses a <code>javax.swing.JDialog</code> object, which is 
 * initialized by an object of class <code>javax.swing.JOptionPane</code> 
 * in the method <code>init</code>. The <code>JOptionPane</code> object
 * possesses a <code>javax.swing.JPanel</code> object, of subtype 
 * <code>suncertify.gui.ConfigOptions</code> 
 * with entry fields to enter the connection data.
 * The class provides two different views according to the
 * access, which can be local or remote.
 * <br>
 * <br> The connection data will be obtained by the class
 * <code>SavedConfiguration</code>. Additional
 * <code>SavedConfiguration</code> is used to store the data connection.
 * <br>
 * <br> The class implements the interfaces <code>ActionListener</code>
 * of the package <code>java.awt.event</code>. The action listener cares for the 
 * 'Start' and 'Exit' button of the  * <code>javax.swing.JOptionPane</code> object.
 * Additional the class implements the interface <code>PropertyChangeListener</code> 
 * of the package <code>java.beans</code> to monitor changes made to the connection 
 * data in class <code>suncertify.gui.ConfigOptions</code>. 
 * 
 * 
 * @see ConfigOptions
 * @see SavedConfiguration
 * @author stefan.streifeneder@gmx.de
 * 
 */
public class DatabaseLocationDialog extends WindowAdapter 
			implements ActionListener, PropertyChangeListener{

	/**
	 * Caption of the connect button.
	 */
	private static final String CONNECT = "Connect";

	/**
	 * Caption of the exit button.
	 */
	private static final String EXIT = "Exit";

	/**
	 * Tool tip of the connect button.
	 */
	private static final String CONNECT_BUTTON_TOOL_TIP = 
			"Connects to the Database";

	/**
	 * Tool tip of the exit button.
	 */
	private static final String EXIT_BUTTON_TOOL_TIP = "Finish Programm";

	/**
	 * Some values for possible port ranges so we can determine what sort of
	 * port the user has specified.
	 */
	private static final int LOWEST_PORT = 1;

	/**
	 * Highest value of the port (65535).
	 */
	private static final int HIGHEST_PORT_NO = 65535;

	/**
	 * A port SYSTEM_PORT_BOUNDARY value (1024).
	 */
	private static final int SYSTEM_PORT_BOUNDARY = 1024;

	/**
	 * A port IANA_PORT_BOUNDARY value (49151).
	 */
	private static final int IANA_PORT_BOUNDARY = 49151;

	/**
	 * Stores the title of this dialog.
	 */
	private static String TITLE = "";

	/**
	 * Gets Saved configuration, DB location, 
	 * ipaddress, port and server name.
	 */
	private static SavedConfiguration config = 
			SavedConfiguration.getSavedConfiguration();

	/**
	 * A reference to the dialog. 
	 */
	JDialog dialog;

	/**
	 * A check box to start the userguide. 
	 */
	JCheckBoxMenuItem helpCeckBoxMenuItem = new JCheckBoxMenuItem("Userguide");

	/**
	 * The bits and pieces that comprise our dialog box. 
	 */
	JOptionPane options;

	/**
	 * A scroll panel for the help panel. 
	 */
	JScrollPane scrollPane;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.DatabaseLocationDialog</code>. Default access level,
	 * because it is used in an inner class.
	 */
	Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.DatabaseLocationDialog"), Level.ALL);

	/**
	 * The connect button.
	 */
	private JButton connectButton = new JButton(DatabaseLocationDialog.CONNECT);

	/**
	 * The exit button.
	 */
	private JButton exitButton = new JButton(DatabaseLocationDialog.EXIT);

	/**
	 * The common panel for specifying where the database is located.
	 */
	private ConfigOptions configOptions;

	/**
	 * The application mode.
	 */
	private ApplicationMode appMode;

	/**
	 * Stores the ipaddress.
	 */
	private String ipAddress;

	/**
	 * Details specified in the <code>configOptions</code> panel 
	 * where the database is located.
	 */
	private String location;
	
	/**
	 * Stores the password.
	 */
	private String password;

	/**
	 * Stores the value of the port.
	 */
	private String port;

	/**
	 * The owner ID.
	 */
	private int owner;

	/**
	 * Creates an object of the class, which can connect to the database 
	 * in a local or remote way. 
	 * 
	 * 
	 * @param appType
	 *            The application mode.
	 * @param idOwner
	 *            The ID of the owner.
	 */
	public DatabaseLocationDialog(ApplicationMode appType, int idOwner) {
		this.log.entering("DatabaseLocationDialog", "DatabaseLocationDialog",
				new Object[] { appType, Integer.valueOf(idOwner) });
		this.owner = idOwner;
		this.appMode = appType;
		init();
		this.log.exiting("DatabaseLocationDialog", "DatabaseLocationDialog");
	}

	/**
	 * Event handler to process clicks on the 'Connect' or
	 * the 'Exit' button.
	 * 
	 * @param ae
	 *            Represents the command.
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Connect")) {
			this.getConnected();
		} else if (ae.getActionCommand().equals("Exit")) {
			DatabaseLocationDialog.setDatabaseLocationDialogClosed();
		}
	}

	/**
	 * Stops the application. Window handler to process situations where
	 * the user has closed the window rather than clicking one of the buttons.
	 * 
	 * @param we
	 *            A window event.
	 */
	@Override
	public void windowClosing(WindowEvent we) {
		this.log.entering("DatabaseLocationDialog", "windowClosing", we);
		if (we.getID() == WindowEvent.WINDOW_CLOSING) {
			setDatabaseLocationDialogClosed();
		}
		this.log.exiting("DatabaseLocationDialog", "windowClosing");
	}

	/**
	 * Reacts if the connection data are changed.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName().equals("PORT")) {
			int p = Integer.parseInt(arg0.getNewValue().toString());
			if (p >= DatabaseLocationDialog.LOWEST_PORT && 
					p < DatabaseLocationDialog.HIGHEST_PORT_NO) {
				if (p < DatabaseLocationDialog.SYSTEM_PORT_BOUNDARY) {
					this.log.severe("DatabaseLocationDialog, " + "User chose "
							+ "System port " + arg0.getNewValue().toString());
				} else if (p < DatabaseLocationDialog.IANA_PORT_BOUNDARY) {
					this.log.severe("DatabaseLocationDialog, " + "User chose "
							+ "IANA port " + arg0.getNewValue().toString());
				} else {
					this.log.severe("DatabaseLocationDialog, " + "User "
							+ "chose dynamic port " + arg0.getNewValue().toString());
				}
				
				this.port = arg0.getNewValue().toString();
				this.log.severe("DatabaseLocationDialog, Chose port: " 
				+ this.port);
				config.setParameter(
						SavedConfiguration.SERVER_PORT, arg0.getNewValue().toString());
			}
		}else if(arg0.getPropertyName().equals("IP")) {
			this.ipAddress = (String)arg0.getNewValue();
			config.setParameter(SavedConfiguration.SERVER_IP_ADDRESS, "" 
					+ (String)arg0.getNewValue());
		}else if(arg0.getPropertyName().equals("DB")) {
			this.location = (String)arg0.getNewValue();
			config.setParameter(SavedConfiguration.DATABASE_LOCATION, 
					(String)arg0.getNewValue());
		}else if(arg0.getPropertyName().equals("PASSWORD")) {
			this.password = (String)arg0.getNewValue();
			config.setParameter(SavedConfiguration.PASSWORD, 
					(String)arg0.getNewValue());
		}			
	}

	/**
	 * Returns the ipaddress.
	 * 
	 * @return String - The ipaddress.
	 */
	public String getIpAddress() {
		this.log.entering("DatabaseLocationDialog", "getIpAddress");
		this.log.entering("DatabaseLocationDialog", "getIpAddress", this.ipAddress);
		return this.ipAddress;
	}
	
	/**
	 * Returns the password.
	 * 
	 * @return String - the password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Returns the location path to the database file.
	 * 
	 * @return String - The location of the database file.
	 */
	public String getLocation() {
		
		System.out.println("DBLocation, getLocation, loc: " + this.location);
		
		this.log.entering("DatabaseLocationDialog", "getLocation");
		this.log.entering("DatabaseLocationDialog", "getLocation", this.location);
		return this.location;
	}

	/**
	 * Returns the port number.
	 * 
	 * @return String - The port number.
	 */
	public String getPort() {
		this.log.entering("DatabaseLocationDialog", "getPort");
		this.log.entering("DatabaseLocationDialog", "getPort", this.port);
		return this.port;
	}

	/**
	 * Validates the entered connection's data and connects to the database
	 * or to the server.
	 */
	void getConnected() {
		this.log.entering("DatabaseLocationDialog", "connectToServer");
		try {
			if (this.configOptions.validateConnectionConfig()) {
				this.options.setValue(Integer.valueOf(JOptionPane.OK_OPTION));
				this.log.severe("DatabaseLocationDialog, connectToServer, " 
						+ "Client ID: " 
						+ this.owner
						+ "\n- Client Mode: " + this.appMode + " - IP: " 
						+ this.ipAddress 
						+ "\n- Port: " + this.port + " - DB location: " 
						+ this.location);
			}
		} catch (IllegalArgumentException ex) {
			ExceptionDialog.handleException("AppRunner: " + ex.toString());
		} 
		this.log.exiting("DatabaseLocationDialog", "connectToServer");
	}

	/**
	 * Creates the dialog.
	 */
	private void init() {
		this.log.entering("DatabaseLocationDialog", "init");
		this.configOptions = new ConfigOptions(this.appMode);
		this.configOptions.addPropertyChangeListener(this);
		DatabaseLocationDialog.TITLE = "B & S, ID: " + this.owner + " - " 
											+ "Connection Dialog - "
														+ this.appMode.name();
		this.configOptions.setKeyListener( //
				new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							getConnected();
						} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							setDatabaseLocationDialogClosed();
						}
					}
				});
		if (this.appMode == ApplicationMode.LOCAL_CLIENT) {
			this.location = config.getParameter(
					SavedConfiguration.DATABASE_LOCATION);
			this.configOptions.setLocationFieldText(this.location);
			this.password = config.getParameter(
					SavedConfiguration.PASSWORD);
			this.configOptions.setPasswordText(this.password);
		} else {
			this.appMode = ApplicationMode.NETWORK_CLIENT;
			this.port = config.getParameter(SavedConfiguration.SERVER_PORT);
			this.configOptions.setPortNumberText(this.port);
			this.ipAddress = config.getParameter(
					SavedConfiguration.SERVER_IP_ADDRESS);
			this.configOptions.setIpAddressFieldText(this.ipAddress);
		}
		this.options = new JOptionPane(
				this.configOptions, JOptionPane.DEFAULT_OPTION, 
				JOptionPane.OK_CANCEL_OPTION);
		this.connectButton.setActionCommand(DatabaseLocationDialog.CONNECT);
		this.connectButton.setMnemonic('C');
		this.connectButton.addActionListener(this);
		this.connectButton.setActionCommand(DatabaseLocationDialog.CONNECT);
		this.connectButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setDatabaseLocationDialogClosed();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					getConnected();
				} else if (e.getKeyCode() == KeyEvent.VK_C) {
					getConnected();
				}
			}
		});
		this.connectButton.setToolTipText(
				DatabaseLocationDialog.CONNECT_BUTTON_TOOL_TIP);
		this.exitButton.setMnemonic('E');
		this.exitButton.setActionCommand(DatabaseLocationDialog.EXIT);
		this.exitButton.addActionListener(this);
		this.exitButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setDatabaseLocationDialogClosed();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					setDatabaseLocationDialogClosed();
				} else if (e.getKeyCode() == KeyEvent.VK_E) {
					setDatabaseLocationDialogClosed();
				}
			}
		});
		this.exitButton.setToolTipText(
				DatabaseLocationDialog.EXIT_BUTTON_TOOL_TIP);
		this.options.setOptions(new Object[] { this.connectButton, this.exitButton });
		this.dialog = this.options.createDialog(null, DatabaseLocationDialog.TITLE);
		
		if(this.appMode.equals(ApplicationMode.NETWORK_CLIENT)){
			DatabaseLocationDialog.this.dialog.setSize(400, 220);
			DatabaseLocationDialog.this.dialog.setResizable(false);		
		}else {
			DatabaseLocationDialog.this.dialog.setSize(800, 200);
			DatabaseLocationDialog.this.dialog.setResizable(true);	
		}
		this.dialog.addWindowListener(this);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Help");
		menu.setMnemonic('H');
		menu.setToolTipText("Open with 'Alt' + H");
		menu.add(this.helpCeckBoxMenuItem);
		menuBar.add(menu);
		this.helpCeckBoxMenuItem.addActionListener(new HelpCheckBoxListener());
		this.dialog.setJMenuBar(menuBar);
		StartMonitor.setVisi();
		this.dialog.setVisible(true);
		this.log.exiting("DatabaseLocationDialog", "init");
	}

	/**
	 * Closes the application.
	 */
	static void setDatabaseLocationDialogClosed() {
		Logger.getAnonymousLogger()
				.severe("DatabaseLocationDialog, setDatabaseLocationDialogClosed, " + " Application exits!");
		System.exit(0);
	}

	/**
	 * Extends the dialog to display the help panel.
	 * 
	 */
	private class HelpCheckBoxListener implements ActionListener {
		/**
		 * No-arg Constructor
		 */
		public HelpCheckBoxListener() {
			//
		}

		/**
		 * Overridden method to handle action on the menu item, which is a check
		 * box 'Help'.
		 * 
		 * @param e
		 *            An action event
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e == null) {
				return;
			}
			boolean state = 
					DatabaseLocationDialog.this.helpCeckBoxMenuItem.getState();
			if (state == true) {
				DatabaseLocationDialog.this.dialog.setResizable(true);
				DatabaseLocationDialog.this.dialog.setSize(800, 500);
				DatabaseLocationDialog.this.dialog.remove(
						DatabaseLocationDialog.this.options);
				DatabaseLocationDialog.this.dialog.getContentPane().add(
						DatabaseLocationDialog.this.options,
						BorderLayout.NORTH);
				DatabaseLocationDialog.this.scrollPane = new JScrollPane(
						new HelpHTMLPanel());
				DatabaseLocationDialog.this.dialog.getContentPane().add(
						DatabaseLocationDialog.this.scrollPane,
						BorderLayout.CENTER);
				DatabaseLocationDialog.this.dialog.setVisible(true);
			} else {
				DatabaseLocationDialog.this.scrollPane.setVisible(false);
				DatabaseLocationDialog.this.dialog.setSize(700, 200);
				DatabaseLocationDialog.this.dialog.setResizable(false);
				DatabaseLocationDialog.this.dialog.setResizable(false);
				DatabaseLocationDialog.this.dialog.setVisible(true);
			}
		}
	}
}
