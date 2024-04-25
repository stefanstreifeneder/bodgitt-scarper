package suncertify.gui.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import suncertify.db.LoggerControl;
import suncertify.gui.ApplicationMode;
import suncertify.gui.ConfigOptions;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.MenuFile;
import suncertify.gui.MenuHelp;
import suncertify.gui.MyInetAddress;
import suncertify.gui.SavedConfiguration;

/**
 * The class is a <code>javax.swing.JFrame</code>. It creates the graphical user interface 
 * of the server. This class provides the structure necessary for displaying 
 * configurable objects, for starting the server and for exiting the server in 
 * a safe manner.
 * <br>
 * The class implements the interface <code>PropertyChangeListener</code> of the package 
 * <code>java.beans</code> to monitor changes made to the connection data in class
 * <code>suncertify.gui.ConfigOptions</code>. The method <code>serverStart</code>
 * writes all configurations to the physical file called 'suncertify.properties', 
 * which resides in the currently working directory.
 * <br>
 * <br>
 * The class uses the <code>javax.swing.BorderLayout</code> to fix the graphics.<br>
 * The panel( @see <code>suncertif.gui.ConfigOptions</code> ), which possesses entry fields
 * to pass the connection data is settled
 * by <code>BorderLayout.NORTH</code>.<br>
 * The class provides a method, which returns a panel, which contains
 * the buttons to start or to exit. The panel is settled by
 * <code>BorderLayout.CENTER</code>.<br>
 * Another panel displays a message about the connecting status,
 * which is settled by <code>BorderLayout.SOUTH</code>.
 * <br> 
 * 
 * 
 * <br><br>
 * MNemonics:<br>
 * E - <code>exitButton</code><br>
 * F - menu file<br>
 * H - menu help<br>
 * Q - menu file/quite<br>
 * S - <code>startButton</code><br>
 * 
 * 
 * @see ConfigOptions
 * @see NetworkStarter
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class ServerWindow extends JFrame
					implements PropertyChangeListener{

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.server.ServerWindow</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.server.ServerWindow"), Level.ALL);

	/**
	 * Caption of the start button.
	 */
	private static final String START_BUTTON_TEXT = "Start";

	/**
	 * Tooltip of the start button.
	 */
	private static final String START_BUTTON_TOOL_TIP = 
			"Press Button to start Server!";

	/**
	 * Caption of the exit button.
	 */
	private static final String EXIT_BUTTON_TEXT = "Exit";

	/**
	 * Tooltip of the exit button.
	 */
	private static final String EXIT_BUTTON_TOOL_TIP = 
			"Stops the server as soon as it is safe to do so";

	/**
	 * Information of the connecting status.
	 */
	private static final String INITIAL_STATUS = 
			"Enter configuration parameters and click \""
			+ ServerWindow.START_BUTTON_TEXT + "\"";
	
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
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization
	 */
	private static final long serialVersionUID = 2416L;

	/**
	 * A variable, which stores the Application mode.
	 */
	private static ApplicationMode applicationMode = ApplicationMode.SERVER;

	/**
	 * Gets Saved configuration, DB location, 
	 * ipaddress, port and server name.
	 */
	protected static SavedConfiguration config = 
			SavedConfiguration.getSavedConfiguration();

	/**
	 * Stores, whether the connection data has changed or not.
	 */
	protected static boolean writeConData = false;

	/**
	 * A Reference to the location of the database.
	 */
	static String databaseLocation = "";

	/**
	 * A reference of this class, which registers the server to the system.
	 */
	static NetworkStarter networkStarterSocket;

	/**
	 * The exit button.
	 */
	JButton exitButton = 
			new JButton(ServerWindow.EXIT_BUTTON_TEXT);

	/**
	 * A reference to the panel, which possesses the components to enter the
	 * connection data.
	 */
	ConfigOptions configOptionsPanel = new ConfigOptions(applicationMode);
	
	
	/**
	 * The panel, which displays a message, whether the server is running
	 * or not.
	 */
	JPanel statusPanel;

	/**
	 * Stores information about the connecting status in the south of the
	 * window.
	 */
	JLabel status = new JLabel();	
	
	/**
	 * The start button. Default Access, because it is used in an inner class.
	 */
	JButton startServerButton = new JButton(ServerWindow.START_BUTTON_TEXT);
	
	
	/**
	 * A no-arg constructor, which initialize all components
	 * and calls the private method <code>commandOptionsPanel</code>
	 * of the class.
	 */
	public ServerWindow() {
		super("Bodgitt & Scarper / Enum - Server");
		this.log.entering("ServerWindow", "ServerWindow");
		this.setResizable(false);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new MenuFile());
		menuBar.add(new MenuHelp());
		this.setJMenuBar(menuBar);
		this.configOptionsPanel.addPropertyChangeListener(this);		
		this.configOptionsPanel.setKeyListener(new ServerConfOptsKeyListener(this));
		this.add(this.configOptionsPanel, BorderLayout.NORTH);
		this.add(commandOptionsPanel(), BorderLayout.CENTER);
		this.status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.statusPanel = new JPanel(new BorderLayout());
		this.statusPanel.add(this.status, BorderLayout.NORTH);		
		this.add(this.statusPanel, BorderLayout.SOUTH);
		String databaseLocationLoc = config.getParameter(SavedConfiguration.DATABASE_LOCATION);
		this.configOptionsPanel.setLocationFieldText(
				(databaseLocationLoc == null) ? "" : databaseLocationLoc);
		this.startServerButton.setEnabled(true);
		this.startServerButton.setToolTipText(ServerWindow.START_BUTTON_TOOL_TIP);
		this.startServerButton.setMnemonic('S');
		this.configOptionsPanel.setPasswordText(config.getParameter(SavedConfiguration.PASSWORD));
		this.configOptionsPanel.setPortNumberText(config.getParameter(SavedConfiguration.SERVER_PORT));
		this.configOptionsPanel.setIpAddressFieldText(config.getParameter(SavedConfiguration.SERVER_IP_ADDRESS));
		// Only as an Information. Values are not in use.
		InetAddress address = null;
		String ipString = null;
		try {
			address = InetAddress.getLocalHost();
			ipString = address.getHostName();
		} catch (UnknownHostException ex) {
			this.log.severe("ServerWindow, Constructor, InetAddress.getLocalHost(): " + ipString + " - Exc: "
					+ ex.getLocalizedMessage());
			ExceptionDialog.handleException("IP Validation failed: " + ipString);
		}
		this.status.setText(ServerWindow.INITIAL_STATUS);
		this.pack();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - this.getWidth()) / 2);
		int y = (int) ((d.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		this.setVisible(true);
		this.log.exiting("ServerWindow", "ServerWindow");
	}

	/**
	 * Overridden method to control the shutdown mechanism.
	 * 
	 * @param e
	 *            Represents the command.
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			closeServerWindow();
		}
		super.processWindowEvent(e); // Pass on the event
	}

	/**
	 * Overridden method to monitor changes to the connection data.
	 * @param arg0 A property change event.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName().equals("PORT")) {
			int p = Integer.parseInt(arg0.getNewValue().toString());
			if (p >= LOWEST_PORT && 
					p < HIGHEST_PORT_NO) {
				if (p < SYSTEM_PORT_BOUNDARY) {
					this.log.severe("DatabaseLocationDialog, " + "User chose "
							+ "System port " + arg0.getNewValue().toString());
				} else if (p < IANA_PORT_BOUNDARY) {
					this.log.severe("DatabaseLocationDialog, " + "User chose "
							+ "IANA port " + arg0.getNewValue().toString());
				} else {
					this.log.severe("DatabaseLocationDialog, " + "User "
							+ "chose dynamic port " + arg0.getNewValue().toString());
				}
			}
			writeConData = true;
		}else if(arg0.getPropertyName().equals("DB")) {
			writeConData = true;
		}		
		
	}

	/**
	 * Closes the window. 
	 */
	@SuppressWarnings("static-method")
	void closeServerWindow() {
		Logger.getAnonymousLogger()
				.info("ServerWindow, closeServerWindow, " + "starts exit by closng the database file.");		
		if (networkStarterSocket != null) {
			networkStarterSocket.getSocketServer().closeServer();
		}
		Logger.getAnonymousLogger().info("ServerWindow, closeServerWindow, next call: System.exit(0)");
		System.exit(0);
	}

	/**
	 * Starts the server. 
	 * <br> If registration as a server has succeeded the constructor 
	 * writes to the file 'suncertify.properties' by using the class
	 * <code>suncertify.gui.SavedConfiguration</code>.
	 */
	void serverStart() {
		this.log.entering("ServerWindow", "serverStart");
		boolean inputOk = false;
		try {
			inputOk = this.configOptionsPanel.validateConnectionConfig();
		} catch (IllegalArgumentException ex) {
			this.log.severe("ConfigOptions, serverStart, " + ex.getLocalizedMessage());
			ExceptionDialog.handleException(
					"Wrong input at the " + "configuration of the connection! " + ex.getLocalizedMessage());
			return;
		}
		if (inputOk == false) {
			JOptionPane.showMessageDialog(this.configOptionsPanel, "Wrong input! Server Start failed!",
					"Input Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String port = this.configOptionsPanel.getPortNumberText();
		ServerWindow.databaseLocation = this.configOptionsPanel.getLocationFieldText();	
		String password = this.configOptionsPanel.getPasswordFieldText();
		
		//Sets the exit routine
		Runtime.getRuntime().addShutdownHook(new CleanExit(databaseLocation));
		try {
			networkStarterSocket = 
					new NetworkStarter(
							ServerWindow.databaseLocation, port, this.status, password);				
		} catch (Exception ex) {// A general catcher.
			this.log.severe("ConfigOptions, serverStart, " + ex.getLocalizedMessage());
			ExceptionDialog.handleException("Server starting failed via: " + " DB Location: "
					+ ServerWindow.databaseLocation + " - Port: " + port);
			return;
		}		
		
		this.configOptionsPanel.setLocationFieldEnabled(false);
		this.configOptionsPanel.setPortNumberEnabled(false);
		this.configOptionsPanel.setBrowseButtonEnabled(false);
		this.configOptionsPanel.setPasswordFieldEnabled(false);
		this.startServerButton.setEnabled(false);
		if(writeConData) {
			config.setParameter(SavedConfiguration.DATABASE_LOCATION, ServerWindow.databaseLocation);
			config.setParameter(SavedConfiguration.SERVER_PORT, port);
		}
		config.setParameter(SavedConfiguration.SERVER_IP_ADDRESS, MyInetAddress.getIpAddressString());
		
		
		this.log.severe("Server entries: " + " - IP: " + MyInetAddress.getIpAddressString() 
				+ "\n- Port: " + port
				+ "\n- DB Location: " + databaseLocation);
		this.log.exiting("ServerWindow", "serverStart");
	}

	/**
	 * This private panel provides the buttons the user may click upon in order
	 * to do a major action (start the server or exit).
	 *
	 * @return JPanel - Containing the buttons.
	 */
	private JPanel commandOptionsPanel() {
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.startServerButton.setEnabled(false);
		this.startServerButton.setActionCommand("START");		
		this.startServerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!ServerWindow.this.startServerButton.isEnabled()) {
					return;
				}else if(arg0.getActionCommand().equals("START")){
					serverStart();
				}else if(arg0.getModifiers() == ActionEvent.ALT_MASK){
					if(arg0.getActionCommand().equals("START")){
						serverStart();
					}					
				}				
			}
		});		
		this.startServerButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					serverStart();
				}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					closeServerWindow();
				}
			}
		});
		thePanel.add(this.startServerButton);
		this.exitButton.setActionCommand("EXIT");
		this.exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(arg0.getActionCommand().equals("EXIT")){
					closeServerWindow();
				}else if (arg0.getModifiers() == ActionEvent.ALT_MASK) {
					if (arg0.getActionCommand().equals("EXIT")) {
						closeServerWindow();
					}
				}
			}
		});		
		this.exitButton.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					closeServerWindow();
				}else if (e.getKeyCode() == KeyEvent.VK_E) {
					closeServerWindow();
				}
			}
		});
		this.exitButton.setToolTipText(ServerWindow.EXIT_BUTTON_TOOL_TIP);
		this.exitButton.setMnemonic('E');
		thePanel.add(this.exitButton);
		return thePanel;
	}

	/**
	 * Key adapter to listen for key actions.
	 *
	 */
	private class ServerConfOptsKeyListener extends KeyAdapter {

		/**
		 * A reference to the graphical user interface of the server.
		 */
		ServerWindow serWin = null;

		/**
		 * Creates an object of this class.
		 * 
		 * @param sw
		 *            - A <code>suncertify.gui.server.ServerWindow</code> object.
		 */
		public ServerConfOptsKeyListener(ServerWindow sw) {
			this.serWin = sw;
		}

		/**
		 * Handles key events.
		 * 
		 * @param e
		 *            A key event.
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.serWin.serverStart();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				closeServerWindow();
			}
		}
	}
}
