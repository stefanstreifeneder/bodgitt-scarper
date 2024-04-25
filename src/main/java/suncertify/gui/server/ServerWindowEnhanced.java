package suncertify.gui.server;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import suncertify.db.LoggerControl;
import suncertify.db.RecordDatabase;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.MyInetAddress;
import suncertify.gui.SavedConfiguration;


/**
 * The class represents the graphical part of the enhanced server version. 
 * It is subtype of class <code>suncertify.gui.server.ServerWindow</code>, 
 * which is a <code>javax.swing.JFrame</code>.<br>
 * <br>
 * The class removes the panel <code>suncertify.gui.ConfigOptions</code>,
 * which is settled by <code>BorderLayout.NORTH</code>.
 * <br>
 * That panel is embedded in another panel, which offers additional
 * functionalities.
 * <br> These are:
 * <br> - a check box to release forgotten locks automatically
 * <br> - a button to open a dialog, which enables to release locks
 * on Records.
 * 
 * 
 * @see ServerWindow
 * @author stefan.streifeneder@gmx.de
 */
public class ServerWindowEnhanced extends ServerWindow{
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.server.ServerWindowEnhanced</code>.
	 */
	final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.server.ServerWindowEnhanced"), Level.ALL);

	/**
	 * Check box to release locks of clients, who went offline.
	 */
	JCheckBox checkBoxClientExitCtrl;

	/**
	 * True, if locks were released, if clients go offline.
	 */
	boolean isExitCtrl = false;

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2417L;
	
	/**
	 * A button to open a dialog, which displays locking
	 * events and enables to release locks on Records.
	 */
	private final JButton buttonReleaseLock = new JButton("Track/Release Lock");

	/**
	 * A database handle to track locks and to release locks
	 * on Records.
	 */
	private static RecordDatabase database;
	
	/**
	 * The password of the database.
	 */
	private static String password;
	
	
	/**
	 * Stores "No Records locked.".
	 */
	private static String NO_LOCKS = "No Record locked.";

	/**
	 * A reference of this class, which registers the server to the system.
	 */
	private NetworkStarterEnhanced starter;

	/**
	 * Constructs an object of this class.
	 */
	public ServerWindowEnhanced(){	
		super();
		this.setTitle("B&S SERVER ENHANCED");
		this.setSize(750, 375);
		this.setResizable(false);
		this.setLocation(300, 250);
		this.remove(this.configOptionsPanel);		
		this.init();		
	}
	
	/**
	 * Closes the window. 
	 */
	@Override
	void closeServerWindow() {
		Logger.getAnonymousLogger()
				.info("ServerWindowEnhanced, closeServerWindow, " 
						+ "starts exit by closng the database file.");
		if (this.starter != null) {
			this.starter.getServerEnhancedObject().closeServer();
		}
		Logger.getAnonymousLogger().info("ServerWindowEnhanced, "
				+ "closeServerWindow, next call: System.exit(0)");
		System.exit(0);
	}
	
	/**
	 * Registers the server to the system.
	 * <br> If registration as a server has succeeded the constructor 
	 * writes to the file 'suncertify.properties' by using the class
	 * <code>suncertify.gui.SavedConfiguration</code>.
	 */
	@Override
	void serverStart() {
		this.log.entering("ServerWindowEnhanced", "serverStart");
		boolean inputOk = false;
		try {
			inputOk = this.configOptionsPanel.validateConnectionConfig();
		} catch (final IllegalArgumentException ex) {
			this.log.severe("ServerWindowEnhanced, serverStart, " 
					+ ex.getLocalizedMessage());
			ExceptionDialog.handleException(
					"Wrong input at the " + "configuration of the connection! " 
							+ ex.getLocalizedMessage());
			return;
		}
		if (inputOk == false) {
			JOptionPane.showMessageDialog(this.configOptionsPanel, "Wrong input! "
					+ "Server Start failed!",
					"Input Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		final String port = this.configOptionsPanel.getPortNumberText();
		databaseLocation = this.configOptionsPanel.getLocationFieldText();
		password = this.configOptionsPanel.getPasswordFieldText();
		
		//Sets the exit routine
		Runtime.getRuntime().addShutdownHook(new CleanExit(databaseLocation));
		try {
			this.starter = new NetworkStarterEnhanced(
					databaseLocation
					, port
					, this.status
					, this.isExitCtrl, password);
			this.log.severe("ServerWindowEnhanced, serverStart, "
					+ "\ndb: " + databaseLocation
					+ "\nip: " + MyInetAddress.getIpAddressString()
					+ "\nport: " + port
					+ "\nexit-control: " + this.isExitCtrl
					);	
		} catch (final Exception ex) {// A general catcher.
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
		this.checkBoxClientExitCtrl.setEnabled(false);
		this.buttonReleaseLock.setEnabled(true);
		
		//writes to suncertify.properties, if connection data has changed
		if(ServerWindow.writeConData) {
			ServerWindow.config.setParameter(
					SavedConfiguration.DATABASE_LOCATION, ServerWindow.databaseLocation);
			ServerWindow.config.setParameter(SavedConfiguration.SERVER_PORT, port);
		}
		ServerWindow.config.setParameter(
				SavedConfiguration.SERVER_IP_ADDRESS, MyInetAddress.getIpAddressString());
		
		
		this.log.severe("Server entries: " + " - IP: " + MyInetAddress.getIpAddressString() 
				+ "\n- Port: " + port
				+ "\n- DB Location: " + databaseLocation);
		this.log.exiting("ServerWindow", "serverStart");
	}

	/**
	 * Releases a lock on a Record.
	 */
	void releaseLockDialog(){
		if(database == null){
			try {
				database = new RecordDatabase(databaseLocation, "");
			} catch (FileNotFoundException e1) {
				this.log.severe("ServerWindowEnhanced, getPanelLockRelease, " 
						+ e1.getLocalizedMessage());
				ExceptionDialog.handleException(
						"ServerWindowEnhanced, getPanelLockRelease, " 
								+ e1.getLocalizedMessage());
			} catch (IOException e1) {
				this.log.severe("ServerWindowEnhanced, getPanelLockRelease, " 
						+ e1.getLocalizedMessage());
				ExceptionDialog.handleException(
						"ServerWindowEnhanced, getPanelLockRelease, " 
								+ e1.getLocalizedMessage());
			}
		}
		Object[] arrayLi = 
				new Object[database.getLocked().size()];
		if(arrayLi.length == 0){
			arrayLi = new Object[1];
			arrayLi[0] = NO_LOCKS;
		}
		int countElements = 0;
		for(Long recNo : database.getLocked()) {
			arrayLi[countElements] = "RecNo-" + recNo
					+ "- " + new Date();
			countElements++;
		}
		final String input = (String) JOptionPane
				.showInputDialog(null,
						"Displays all locked Records."
						+ " You are enabled to release locks."
						+ "\nPlease press 'OK' to release the lock.",
						"Bodgitt & Scarper - Logger Message Dialog", 
						JOptionPane.INFORMATION_MESSAGE, 
						null, 							
						arrayLi, 
						arrayLi[0]);				
		Long rNo = null;
		if(input != null && !input.equals(NO_LOCKS)){
			final String[] tokens = input.split("-");
			rNo = Long.valueOf(tokens[1]);
			database.eraseLock(rNo);
		}
	}

	/**
	 * Initializes the components.
	 */
	private void init(){
		final JPanel panelCentreNew = new JPanel();
		final GridBagLayout gridbagPanelCentreNew = new GridBagLayout();
		final GridBagConstraints constraintsPanelCentreNew = new GridBagConstraints();
		panelCentreNew.setLayout(gridbagPanelCentreNew);
		
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbagPanelCentreNew.setConstraints(this.configOptionsPanel, constraintsPanelCentreNew);
		panelCentreNew.add(this.configOptionsPanel);
		
		//enhancements
		final String labSplLiEnd = "___________________________________________"
				+ "______________________________"
				+ "______________________________";
		
		final JLabel labelSplitline = new JLabel(labSplLiEnd);
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		gridbagPanelCentreNew.setConstraints(labelSplitline, constraintsPanelCentreNew);
		panelCentreNew.add(labelSplitline);
		
		// Panel boxes
		final JPanel panelExitCtrl = new JPanel();		
		final GridBagLayout gridbagCBox = new GridBagLayout();
		final GridBagConstraints constraintsCBox = new GridBagConstraints();
		panelExitCtrl.setLayout(gridbagCBox);

		// check box client exit control				
		this.checkBoxClientExitCtrl = new JCheckBox("Client Exit Lock Control - "
				+ "Server releases locks on Records, if a client disconnects");
		this.checkBoxClientExitCtrl.setMnemonic('C');
		constraintsCBox.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraintsCBox.anchor = GridBagConstraints.WEST;
		constraintsCBox.insets = new Insets(15, 2, 2, 2);
		gridbagCBox.setConstraints(this.checkBoxClientExitCtrl, constraintsCBox);
		panelExitCtrl.add(this.checkBoxClientExitCtrl);
		this.checkBoxClientExitCtrl.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ServerWindowEnhanced.this.isExitCtrl = false;
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ServerWindowEnhanced.this.isExitCtrl = true;
				}
			}
		});
		this.checkBoxClientExitCtrl.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!ServerWindowEnhanced.this.checkBoxClientExitCtrl.isSelected()){
						ServerWindowEnhanced.this.checkBoxClientExitCtrl.setSelected(true);
						ServerWindowEnhanced.this.isExitCtrl = true;
						return;
					}
					ServerWindowEnhanced.this.checkBoxClientExitCtrl.setSelected(false);
					ServerWindowEnhanced.this.isExitCtrl = false;
					
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					closeServerWindow();
				}
			}
		});
		
		
		// add panel check boxes to center panel
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraintsPanelCentreNew.anchor = GridBagConstraints.WEST;
		gridbagPanelCentreNew.setConstraints(panelExitCtrl, constraintsPanelCentreNew);
		panelCentreNew.add(panelExitCtrl);
		
		
		final JPanel panelSplitLine = new JPanel();
		final JLabel labelSplitlineEnd = new JLabel(labSplLiEnd);
		panelSplitLine.add(labelSplitlineEnd);
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraintsPanelCentreNew.anchor = GridBagConstraints.WEST;
		gridbagPanelCentreNew.setConstraints(
				panelSplitLine, constraintsPanelCentreNew);
		panelCentreNew.add(panelSplitLine);
		
		final JPanel panelReleaseLock = this.getPanelLockRelease();

		constraintsPanelCentreNew.weightx = 0.0;
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraintsPanelCentreNew.anchor = GridBagConstraints.WEST;
		gridbagPanelCentreNew.setConstraints(
				panelReleaseLock, constraintsPanelCentreNew);		
		panelCentreNew.add(panelReleaseLock);
		final JPanel panelSplitLineRelLockTop2 = new JPanel();
		final JLabel labelSplitlineRelLockEnd2 = new JLabel(labSplLiEnd);
		panelSplitLineRelLockTop2.add(labelSplitlineRelLockEnd2);
		constraintsPanelCentreNew.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraintsPanelCentreNew.anchor = GridBagConstraints.WEST;
		gridbagPanelCentreNew.setConstraints(panelSplitLineRelLockTop2, constraintsPanelCentreNew);
		panelCentreNew.add(panelSplitLineRelLockTop2);
		// add to JFrame		
		this.add(panelCentreNew, BorderLayout.NORTH);	
	}
	
	
	/**
	 * Creates a panel, which possesses a button to open a dialog,
	 * which enables to monitor locking events and to release locks
	 * on Records.
	 * 
	 * @return JPanel - a panel with a button to open a dialog,
	 * which displays locking events and enables to release locks
	 * on Records.
	 */
	private JPanel getPanelLockRelease(){
		final JPanel panel = new JPanel();
		final GridBagLayout gridbag = new GridBagLayout();
		final GridBagConstraints constraints = new GridBagConstraints();
		panel.setLayout(gridbag);

		// Standard options
		// ensure there is always a gap between components
		constraints.insets = new Insets(10, 2, 2, 15);
		gridbag.setConstraints(panel, constraints);
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(this.buttonReleaseLock, constraints);	
		this.buttonReleaseLock.setMnemonic('R');
		this.buttonReleaseLock.setToolTipText("Will be enabled, "
				+ "if the server is started. Opens a dialog to "
				+ "track/release locks.");
		this.buttonReleaseLock.setEnabled(false);
		this.buttonReleaseLock.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerWindowEnhanced.this.log.info(
						"ServerWindowEnhanced, getPanelLockRelease, "
						+ "event: " + e.getActionCommand());
				releaseLockDialog();
		}});			
		
		this.buttonReleaseLock.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {				
				if (e.getKeyCode() == KeyEvent.VK_ENTER | 
						e.getKeyCode() == KeyEvent.VK_R) {
					releaseLockDialog();
				}
			}
		});
		panel.add(this.buttonReleaseLock);
		panel.add(new JLabel("Opens a Window to track locks "
				+ "and to release them."));
		return panel;
	}
}
