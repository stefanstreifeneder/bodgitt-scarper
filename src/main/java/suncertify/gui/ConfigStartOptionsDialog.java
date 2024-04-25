package suncertify.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import suncertify.db.LoggerControl;

/**
 * The class is a <code>java.awt.event.WindowAdapter</code>.
 * It possesses an object of type <code>javax.swing.JDialog</code>,
 * which is initialized by an object of class 
 * <code>javax.swing.JOptionPane</code> in the private method
 * <code>init</code>. The <code>JOptionPane</code> object
 * possesses a <code>javax.swing.JPanel</code> object,
 * which contains all the <code>javax.swing.JTextArea</code>
 * objects and <code>JPanel</code> objects created by the
 * provided private methods of the class.
 * <br>
 * <br> The class provides three different views according to the
 * the three different command line arguments ("alone", "server" and none).
 * <br>
 * <br>
 * If the command line argument is <b>"alone"</b> the configuration is:
 * <br> Default version (always Buyer Client):<br>
 * entry field - the ID of a client
 * <br> Enhanced version:<br>
 * A check box enables a check box, radio buttons
 * and a menu:<br>
 * check box - client roles<br>
 * radio button - seller client<br>
 * radio button - admin client<br>
 * menu - look and feel layout<br>
 * <br>
 * <br> If the command line argument is <b>"server"</b><br>
 * check box - controls, whether the regular server will be started
 * or the enhanced server<br>
 * menu - look and feel layout
 * <br>
 * <br> If the command line argument is <b>left blank</b><br>
 * A check box enables two check boxes for enhanced services.<br>
 * The first check box enables the user to choose the client role 
 * (Buyer, Seller, Admin) he wants to register. Three radio buttons
 * accomplish the functionality.<br>
 * The second check box starts the update functionality. 
 * Within the update mechanism the content of the table will be updated
 * in time intervals. If the update check box is selected the
 * entry field gets editable to enter the time interval in ms.<br>
 * A menu enables to adjust the look and feel layout.
 * <br>
 * <br>
 * <br>
 * <br>
 * MNemonics:<br>
 * A - <code>adminRadioButton</code><br>
 * B - <code>buyerRadioButton</code><br>
 * C - <code>checkBoxEnhancements</code><br>
 * E - <code>exitButton</code><br>
 * F - menu file<br>
 * H - menu help<br>
 * Q - menu file/quite<br>
 * R - <code>checkBoxClientRoles</code><br>
 * S - <code>startButton</code><br>
 * U - <code>checkBoxUpdates</code><br>
 * V - <code>sellerRadioButton</code><br>
 * 
 *
 * @author stefan.streifeneder@gmx.de
 */
public class ConfigStartOptionsDialog extends WindowAdapter {

	/**
	 * Stores which look and feel style is selected. 
	 */
	static int lookIndex = 0;

	/**
	 * First text sequence concerning the application modes.
	 */
	private static String TEXT_MODE = "Welcome to the application    'Bodgitt & Scarper'" + "\n"
			+ "_____________________________________________________________"
			+ "\nThe application can be started in three different modes:"
			+ "\nLocal Client                 Desktop Application"
			+ "\nNetwork Client            Client accesses database via a server"
			+ "\nServer                           Enables clients to access database";

	/**
	 * Displays the version of the assignment.
	 */
	private static String TEXT_MODE_TOOL_TIP = "Sun Certified Developer for the Java " 
			+ "2 Platform: Version 2.2.2";

	/**
	 * Displays a short explanation of the ID.
	 */
	private static String TEXT_ID = "_____________________________________"
			+ "________________________"
			+ "\nEach client has its own ID number. Please enter Your ID:";
	
	/**
	 * Displays a cutting line and below of it "Client Roles".
	 */
	private static String TEXT_CLIENT_ROLE = "_________________________________"
			+ "____________________________"
			+ "\n                                               "
			+ "          Client Roles";

	/**
	 * Tool tip for the ID.
	 */
	private static String TEXT_ID_TOOL_TIP = "Each client can have the same ID. "
			+ "The application distinguishes in the background between the clients.";

	/**
	 * Displays a cutting line and a short introduction to look and feel.
	 */
	private static String TEXT_LOOK = "__________________________________________"
			+ "_____________________________"
			+ "\nYou can adjust the graphic. Please make your choice!";

	
	/**
	 * Text sequence for the author.
	 */
	private static String TEXT_AUTHOR = "_______________________________"
			+ "_____________________________"
			+ "_________________________" + "\nAuthor: Stefan Streifeneder                     "
			+ "                           Contact: stefan.streifeneder@gmx.de";

	/**
	 * The start button.
	 */
	private final JButton startButton = new JButton("Start");

	/**
	 * The exit button.
	 */
	private final JButton exitButton = new JButton("Exit");

	/**
	 * Entry field for the ID of the client.
	 */
	private final JTextField idOwnerField = new PositiveIntegerField(
			100000000, 8);

	/**
	 * Stores the command line arguments.
	 */
	private final String[] argsPara;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.ConfigStartOptionsDialog</code>. 
	 */
	Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.ConfigStartOptionsDialog"), Level.ALL);

	/**
	 * A <code>javax.swing.JDialog</code> object.
	 * Will be initialized in the method <code>init</code>.
	 */
	JDialog dialog;

	/**
	 * A combo box to display the look and feel adjustments.
	 */
	JComboBox<String> lookCombo;

	/**
	 * Check box whether enhancements are used or not.
	 */
	JCheckBox checkBoxEnhancements = new JCheckBox("Enhancements");
	
	/**
	 * Check box to set the client role (Buyer, Seller or Admin).
	 */
	JCheckBox checkBoxClientRoles = new JCheckBox("Client Roles");
	
	
	/**
	 * A check box to control the update mechanism.
	 */
	JCheckBox checkBoxUpdates = new JCheckBox("Updates");
	
	
	/**
	 * The entry field to set time out for the interval of the updates.
	 */
	JTextField entryFieldUpdates = new PositiveIntegerField(
			100000000, 8);

	/**
	 * Radio button to run the Buyer client mode.
	 */
	JRadioButton buyerRadioButton = new JRadioButton("Buyer");
	
	/**
	 * Radio button to run the Seller client mode.
	 */
	JRadioButton sellerRadioButton = new JRadioButton("Seller");
	
	/**
	 * Radio button to run the Admin client mode.
	 */
	JRadioButton adminRadioButton = new JRadioButton("Admin");	
	
	/**
	 * Stores the application mode.
	 */
	private ApplicationMode appMode;

	/**
	 * The client ID.
	 */
	private int idOwner;

	/**
	 * Constructs an object of this class, which expects
	 * a <code>String</code> array filled with one of the possible
	 * command line arguments (alone, server) or none.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public ConfigStartOptionsDialog(final String[] args) {
		this.argsPara = args;
		this.init(this.argsPara);
	}
	
	/**
	 * Stops the application.
	 * 
	 * @param we
	 *            A window event.
	 * 
	 */
	@Override
	public void windowClosing(final WindowEvent we) {
		if (we.getID() == WindowEvent.WINDOW_CLOSING) {
			this.log.severe("ConfigStartOptionsDialog, " 
					+ "Window event - THE APPLICATIONS STOPS NOW");
			System.exit(0);
		}
	}

	/**
	 * Returns the application mode.
	 * 
	 * @return ApplicationMode - Indicates alone, server oder network_client.
	 */
	public ApplicationMode getApplicationMode() {
		this.log.severe("ConfigStartOptionsDialog, Application Mode: " + this.appMode);
		return this.appMode;
	}

	/**
	 * Returns the ID of a client. If the value was not entered a dialog
	 * opens.
	 * 
	 * @return int - The ID number of a client.
	 */
	public int getIdOwner() {
		if ((this.appMode == ApplicationMode.NETWORK_CLIENT) | 
				(this.appMode == ApplicationMode.LOCAL_CLIENT)) {
			String idString = this.idOwnerField.getText();
			boolean idIsMissing = idString.isEmpty();
			while (idIsMissing) {
				final JOptionPane alert = new JOptionPane("ID number is missing", 
						JOptionPane.ERROR_MESSAGE,
						JOptionPane.DEFAULT_OPTION);
				final JDialog dialogLoc = alert.createDialog(null, "Alert");
				dialogLoc.setVisible(true);
				this.init(this.argsPara);
				idString = this.idOwnerField.getText();
				idIsMissing = idString.isEmpty();
			}
			this.idOwner = Integer.valueOf(this.idOwnerField.getText()).intValue();
		}
		this.log.severe("ConfigStartOptionsDialog, getIdOwner, ID: " + this.idOwner);
		return this.idOwner;
	}

	/**
	 * Returns the client role (BUYER, SELLER, ADMIN).
	 * 
	 * @return ClientRole - The client role.
	 */
	public ClientRole getClientRole() {
		ClientRole mode = null;
		if(this.buyerRadioButton.isSelected()){
			mode = ClientRole.BUYER;
		}else if(this.sellerRadioButton.isSelected()){
			mode = ClientRole.SELLER;
		}else if(this.adminRadioButton.isSelected()){
			mode = ClientRole.ADMIN;
		}		
		this.log.severe("ConfigStartOptionsDialog, getClientMode, " 
				+ "Client Mode: " + mode);
		return mode;
	}	
	
	
	/**
	 * Returns true, if the enhanced server program is selected.
	 * 
	 * @return boolean - true, if enhanced server runs.
	 */
	public boolean isServerEnhanced(){
		this.log.entering("ConfigStartOptionsDialog", "isServerEnhanced");
		this.log.exiting("ConfigStartOptionsDialog", "isServerEnhanced");
		return this.checkBoxEnhancements.isSelected();
	}
	
	/**
	 * Returns true, if the enhanced client version runs.
	 * 
	 * @return boolean - true, if the enhanced version of a client
	 * will run.
	 */
	public boolean isClientEnhanced(){
		this.log.entering("ConfigStartOptionsDialog", "isClientEnhanced");
		boolean isEnhanced = false;
		if(this.checkBoxEnhancements.isSelected()){
			isEnhanced = true;
		}	
		this.log.exiting("ConfigStartOptionsDialog", "isClientEnhanced");
		return isEnhanced;
	}
	
	/**
	 * Returns true, if update functionality is in use.
	 * 
	 * @return boolean - false, if no updates will be done.
	 */
	public boolean getIsUpdateRunning(){
		this.log.entering("ConfigStartOptionsDialog", "getIsUpdateRunning");
		boolean runUpdate = false;
		if(this.checkBoxUpdates.isSelected()){
			runUpdate = true;
		}	
		this.log.exiting("ConfigStartOptionsDialog", "getIsUpdateRunning", 
				Boolean.valueOf(runUpdate));
		return runUpdate;
	}
	
	/**
	 * Returns the time out of the interval of the updates.
	 * 
	 * @return long Time of the interval of the updates.
	 */
	public long getUpdateInterval(){
		this.log.entering("ConfigStartOptionsDialog", "getUpdateInterval");
		long timeOut = -1;
		if(this.appMode == ApplicationMode.NETWORK_CLIENT){
			timeOut = Long.parseLong(this.entryFieldUpdates.getText());
		}		
		this.log.exiting("ConfigStartOptionsDialog", "getUpdateInterval");
		return timeOut;
	}

	/**
	 * Sets the look and feel design.
	 * 
	 * @param modeLook
	 *            Sets the view.
	 */
	void setLookAndFeel(final String modeLook) {
		this.log.entering("ConfigStartOptionsDialog", "setLookAndFeel", modeLook);
		try {			
			UIManager.setLookAndFeel(modeLook);
			this.dialog.dispose();
			this.init(this.argsPara);
		} catch (final ClassNotFoundException e) {
			this.log.warning("ConfigStartOptionsDialog, setLookAndFeel,"
					+ " Exc: " + e.getLocalizedMessage());
		} catch (final InstantiationException e) {
			this.log.warning("ConfigStartOptionsDialog, setLookAndFeel,"
					+ " Exc: " + e.getLocalizedMessage());
		} catch (final IllegalAccessException e) {
			this.log.warning("ConfigStartOptionsDialog, setLookAndFeel,"
					+ " Exc: " + e.getLocalizedMessage());
		} catch (final UnsupportedLookAndFeelException e) {
			this.log.warning("ConfigStartOptionsDialog, setLookAndFeel,"
					+ " Exc: " + e.getLocalizedMessage());
		}
		this.log.exiting("ConfigStartOptionsDialog", "setLookAndFeel");
	}

	/**
	 * Initializes all components.
	 * 
	 * @param args
	 *            Arguments of the command line.
	 */
	private void init(final String[] args) {
		this.log.entering("ConfigStartOptionsDialog", "init", args);
		
		// check command line
		if (args.length > 1) {
			this.log.severe("ConfigStartOptionsDialog, init, IIAException" 
					+ args[0]);
			JOptionPane.showMessageDialog(null,
					"Your entry has more than one arguement: "
					+ args.length + "(arguments)" 
					+ "\nYou have to use the following arguments:"
					+ "\n'alone' - starts a local client"
					+ "\n'server' - starts the server"
					+ "\n''(none) - starts a network client)"
					+ "\nThe program stops! Please enter an appropriate argument!");			
			System.exit(1);
		} else if (args.length == 0) {
			this.appMode = ApplicationMode.NETWORK_CLIENT;
			this.log.severe("ConfigStartOptionsDialog, init, appMode: " 
					+ this.appMode);
		} else if (!(args.length == 0) & args[0].equalsIgnoreCase("alone")) {
			this.appMode = ApplicationMode.LOCAL_CLIENT;
			this.log.severe("ConfigStartOptionsDialog, init, appMode: " 
					+ this.appMode);
		} else if (!(args.length == 0) & args[0].equalsIgnoreCase("server")) {
			this.appMode = ApplicationMode.SERVER;
			this.log.severe("ConfigStartOptionsDialog, init, appMode: " 
					+ this.appMode);
		}else {
			this.log.severe("ConfigStartOptionsDialog, init, IIAException" 
					+ args[0]);
			JOptionPane.showMessageDialog(null,
					"Your entry: "
					+ args[0] + "\nThis is not an acceptable "
							+ "command line argument ('alone', 'server' or none!)"
							+ "\nThe program stops! Please enter an appropriate argument!");			
			System.exit(1);
		}
		
		// Initializing 'Start', 'Exit' button and the
		//check box 'Enhancements'
		this.setStartExitListener();
		// Panel, which contain all other text areas and panels.
		final JPanel panelAll = new JPanel();
		// Initializing the option panel to control the start and exit button.		
		final JOptionPane options = new JOptionPane(panelAll, 
				JOptionPane.DEFAULT_OPTION, 
						JOptionPane.OK_CANCEL_OPTION);
		// Layout and initializing the panel, which contains all the
		// components
		final GridBagLayout gridbag = new GridBagLayout();
		final GridBagConstraints constraints = new GridBagConstraints();
		panelAll.setLayout(gridbag);
				
		// Text Application Mode
		final JTextArea textModeArea = ConfigStartOptionsDialog.getTextAreaAppMode();
		constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraints.anchor = GridBagConstraints.WEST;		
		gridbag.setConstraints(textModeArea, constraints);
		panelAll.add(textModeArea);		
				
		// Label, which displays the running application mode.
		final JLabel modeValueLabel = 
				ConfigStartOptionsDialog.getLabelAppMode(this.appMode);
		constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(modeValueLabel, constraints);
		panelAll.add(modeValueLabel);
		
		// Network or Alone	
		// ID Owner + Roles (Buyer, Seller, Admin)
		if ((this.appMode == ApplicationMode.NETWORK_CLIENT) ^ 
				(this.appMode == ApplicationMode.LOCAL_CLIENT)) {
			
			// Text area client ID
			final JTextArea textIdLabel = ConfigStartOptionsDialog.getTextAreaId();	
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(textIdLabel, constraints);
			panelAll.add(textIdLabel);
			
			// ID entry pan
			final JPanel idPan = this.getPanelIdEntry();
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(idPan, constraints);
			panelAll.add(idPan);
			
			// Client Role - text
			final JTextArea textClientRoles = 
					ConfigStartOptionsDialog.getTextAreaClientRole();
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(textClientRoles, constraints);
			panelAll.add(textClientRoles);
			
			// panel with radio buttons
			final JPanel radioPan = this.getPanelRadioButtsClientRoles();
			gridbag.setConstraints(radioPan, constraints);
			panelAll.add(radioPan);
		}// end ID Owner + Roles (Buyer, Seller, Admin)
		
		// text area enhancements
		final JTextArea textEnhancements = 
				ConfigStartOptionsDialog.getTextAreaEnhancementsAll();
		constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(textEnhancements, constraints);
		panelAll.add(textEnhancements);
		
		// panel with check box enhancements
		constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(this.checkBoxEnhancements, constraints);
		panelAll.add(this.checkBoxEnhancements);	
		
		
		// Alone or Network Client 
		if ((this.appMode == ApplicationMode.NETWORK_CLIENT) ^ 
					(this.appMode == ApplicationMode.LOCAL_CLIENT)) {			
			
			// panel check box client role
			final JPanel panelRolesCheckBox = this.getPanelCheckBoxClientRole();
			constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
			constraints.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(panelRolesCheckBox, constraints);
			panelAll.add(panelRolesCheckBox);	
			
			// Network 
			if (this.appMode == ApplicationMode.NETWORK_CLIENT) {				
				// panel update
				final JPanel panelUpdates = this.getPanelUpdate();				
				constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
				constraints.anchor = GridBagConstraints.WEST;
				gridbag.setConstraints(panelUpdates, constraints);
				panelAll.add(panelUpdates);
				
				textEnhancements.setText(
					"_____________________________________________________________"
					+ "\nThe enhanced network client version supports the following abilities:"
					+ "\n- client roles (Buyer, Seller, Admin)"
					+ "\n- do Updates within time cycles"
					+ "\n- choose a look and feel layout");
				
			}// end Network				
			
			
			// alone			
			if (this.appMode == ApplicationMode.LOCAL_CLIENT) {	
				
				textEnhancements.setText(
					"_____________________________________________________________"
					+ "\nThe enhanced local client version supports the following abilities:"
					+ "\n- client roles (Buyer, Seller, Admin)"
					+ "\n- choose a look and feel layout");
				// add listeners
				this.setLocalClientListeners();				
			}// only alone end
			
		}// end alone or network		
		
		// Server
		if (this.appMode == ApplicationMode.SERVER) {
			
			textEnhancements.setText(
			"_____________________________________________________________"
			+ "\nThe enhanced server version supports the following abilities:"
			+ "\n- release locks automatically, if a client missed it "
			+ "\n- lock monitoring and lock releasing "
			+ "\n- choose a look and feel layout");
			
			// adds listener to the check box 'Enhancements'
			this.setServerListeners();
		}// end Server
		
		// Look&Feel
		final JTextArea textLookFeel = 
				ConfigStartOptionsDialog.getTextAreaLookFeel();
		constraints.gridwidth = GridBagConstraints.REMAINDER; 
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(textLookFeel, constraints);
		panelAll.add(textLookFeel);
		
		// combo box look and feel
		final JComboBox<String> comboLookFeel = this.getComboBoxLookAndFell();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(comboLookFeel, constraints);
		panelAll.add(comboLookFeel);		
		
		// Text Author
		final JTextArea textAutor = 
				ConfigStartOptionsDialog.getTextAreaAuthor();
		constraints.gridwidth = GridBagConstraints.REMAINDER; // end row
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(textAutor, constraints);
		panelAll.add(textAutor);
		
		options.setOptions(new Object[] { this.startButton, this.exitButton });
		this.dialog = options.createDialog(
				null, "B & S -  Start Options Dialog - " + this.appMode);			
		//this.dialog.getRootPane().setDefaultButton(this.startButton);		
		// has to be after create other components
		this.dialog.addWindowListener(this);
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(new MenuFile());
		menuBar.add(new MenuHelp());
		this.dialog.setJMenuBar(menuBar);
		this.dialog.pack();
		this.dialog.setVisible(true);
	} // end init()
	
	
	
	/**
	 * Returns a panel, which contains an entry field to enter the
	 * ID. 
	 * <br> The method adds a key listener to the entry field.
	 * 
	 * @return JPanel - it contains the entry field for the 
	 * ID of a client.
	 */
	private JPanel getPanelIdEntry(){
		this.idOwnerField.setColumns(8);
		this.idOwnerField.setToolTipText("Possible entries: "
				+ "1 - 99999999");
		final JPanel idPan = new JPanel();
		final JLabel labId = new JLabel("Please keep the format: "
				+ "1 - 99999999");
		labId.setFont(new Font("Italic", Font.PLAIN, 10));
		idPan.add(this.idOwnerField);
		this.idOwnerField.setText("1");
		this.idOwnerField.selectAll();
		idPan.add(labId);
		this.idOwnerField.addKeyListener(new KeyAdapter(){			
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.dialog.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.info("ConfigStartOptionsDialog, "
									+ "'Esc' pressed - " 
									+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		return idPan;
	}

	/**
	 * Returns a panel, which contains the radio buttons
	 * of the different client roles.
	 * <br>  The method adds a key listener to the radio
	 * buttons 'Buyer', 'Admin' and 'Seller.
	 * 
	 * @return JPanel - it contains the radio buttons
	 * of the different client roles.
	 */
	private JPanel getPanelRadioButtsClientRoles(){
		this.buyerRadioButton.setSelected(true);	
		this.adminRadioButton.setMnemonic('A');
		this.buyerRadioButton.setMnemonic('B');
		this.sellerRadioButton.setMnemonic('V');
		final ButtonGroup group = new ButtonGroup();
		group.add(this.buyerRadioButton);
		group.add(this.sellerRadioButton);
		group.add(this.adminRadioButton);	
		
		final JPanel radioPan = new JPanel();
		radioPan.add(this.buyerRadioButton);
		radioPan.add(this.sellerRadioButton);
		radioPan.add(this.adminRadioButton);
		final JLabel radioText = new JLabel(" to enable: "
				+ "Enhancements/Client Roles");
		radioText.setFont(new Font("Italic", Font.PLAIN, 10));
		radioPan.add(radioText);
		radioPan.add(radioText);
		this.adminRadioButton.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.dialog.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
					.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		this.buyerRadioButton.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.dialog.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
					.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		this.sellerRadioButton.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.dialog.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
					.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});		
		return radioPan;
	}

	/**
	 * Returns a panel, which contains a check box to enable
	 * the update functionality and an entry field to
	 * set the time of the update intervals.
	 * <br> The method adds a key listener to the check boxes, which 
	 * enables the enhanced version, the update functionality and the 
	 * entry field to set the time interval. Additional it
	 * adds a item listener to the check boxes.
	 * 
	 * @return JPanel - it contains a check box to enables
	 * the update functionality and an entry field to
	 * set the time of the update intervals. 
	 */
	private JPanel getPanelUpdate(){
		this.checkBoxUpdates.setEnabled(false);
		this.checkBoxUpdates.setMnemonic('U');		
		//entry field updates
		this.entryFieldUpdates.setEnabled(false);
		this.entryFieldUpdates.setColumns(8);
		this.entryFieldUpdates.setText("5000");
		final JPanel panelUpdates = new JPanel();
		final JLabel labelUpdates = new JLabel(" ");		
		final JLabel updatesText = new JLabel("Time in Milliseconds");
		updatesText.setFont(new Font("Italic", Font.PLAIN, 11));
		panelUpdates.add(labelUpdates);
		this.checkBoxUpdates.setFont(new Font("Italic", Font.PLAIN, 11));
		panelUpdates.add(this.checkBoxUpdates);
		panelUpdates.add(this.entryFieldUpdates);
		panelUpdates.add(updatesText);
		this.entryFieldUpdates.addKeyListener(
				new KeyAdapter(){
					@Override
					public void keyPressed(final KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							ConfigStartOptionsDialog.this.dialog.dispose();				
						}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							ConfigStartOptionsDialog.this.log
							.severe(
									"ConfigStartOptionsDialog, "
									+ "EntryFieldUpdatesKeyAdapter" 
											+ "'Exit' button pressed - "
											+ "THE APPLICATIONS STOPS");
							System.exit(0);
						}
					}
				});			
		// check box enhancements key adapter
		this.checkBoxEnhancements.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(false);
						ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(false);
						ConfigStartOptionsDialog.this.checkBoxUpdates.setEnabled(false);
						ConfigStartOptionsDialog.this.entryFieldUpdates.setText("5000");
						ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(false);
						ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
					} else if (!ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(true);
						ConfigStartOptionsDialog.this.checkBoxUpdates.setEnabled(true);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.severe("ConfigStartOptionsDialog, " 
									+ "CheckBoxEnhanceNetworkKeyAdapter"
									+ "'Exit' button pressed - " 
									+ "THE APPLICATIONS STOPS");
					System.exit(0);
				}
			}
		});				
		this.checkBoxUpdates.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (ConfigStartOptionsDialog.this.checkBoxUpdates.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(false);
						ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(false);
						ConfigStartOptionsDialog.this.entryFieldUpdates.setText("5000");
					} else if (!ConfigStartOptionsDialog.this.checkBoxUpdates.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(true);
						ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(true);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.severe("ConfigStartOptionsDialog, " 
									+ "CheckBoxUpdatesNetworkKeyAdapter"
									+ "'Exit' button pressed - " 
										+ "THE APPLICATIONS STOPS");
					System.exit(0);
				}
			}
		});
		this.checkBoxEnhancements.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(false);
					ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(false);
					ConfigStartOptionsDialog.this.checkBoxUpdates.setEnabled(false);
					ConfigStartOptionsDialog.this.entryFieldUpdates.setText("5000");
					ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(false);
					ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(false);					
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(true);
					ConfigStartOptionsDialog.this.checkBoxUpdates.setEnabled(true);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(true);
				}
			}				
		});
		this.checkBoxUpdates.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(false);
					ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(false);
					ConfigStartOptionsDialog.this.entryFieldUpdates.setText("5000");
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.checkBoxUpdates.setSelected(true);
					ConfigStartOptionsDialog.this.entryFieldUpdates.setEnabled(true);
				}
			}
		});
		return panelUpdates;
	}

	/**
	 * Returns a panel, which contains a check box to enable
	 * different client roles.
	 * <br> The method adds a key listener and an item listener
	 * to the check box, which enables the different client roles 
	 * (Buyer, Seller and Admin).
	 * 
	 * @return JPanel - it contains a check box to enable
	 * different client roles.
	 */
	private JPanel getPanelCheckBoxClientRole(){
		final JPanel panelRolesCheckBox = new JPanel();
		final JLabel labelRoles = new JLabel(" ");
		labelRoles.setFont(new Font("Italic", Font.PLAIN, 8));			
		this.checkBoxClientRoles.setMnemonic('R');
		this.checkBoxClientRoles.setToolTipText("Client Roles.");			
		this.checkBoxClientRoles.setFont(new Font("Italic", Font.PLAIN, 11));			
		this.checkBoxClientRoles.setEnabled(false);
		this.checkBoxClientRoles.setSelected(false);
		this.sellerRadioButton.setEnabled(false);
		this.adminRadioButton.setEnabled(false);			
		final JLabel modesText = new JLabel("( enables Seller Client and Admin Client )");
		modesText.setFont(new Font("Italic", Font.PLAIN, 10));
		panelRolesCheckBox.add(labelRoles);
		panelRolesCheckBox.add(this.checkBoxClientRoles);
		panelRolesCheckBox.add(modesText);		
		this.checkBoxClientRoles.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (ConfigStartOptionsDialog.this.checkBoxClientRoles.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
						ConfigStartOptionsDialog.this.sellerRadioButton.setEnabled(false);
						ConfigStartOptionsDialog.this.adminRadioButton.setEnabled(false);
						ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
					} else if (!ConfigStartOptionsDialog.this.checkBoxClientRoles.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(true);
						ConfigStartOptionsDialog.this.sellerRadioButton.setEnabled(true);
						ConfigStartOptionsDialog.this.adminRadioButton.setEnabled(true);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log.severe("ConfigStartOptionsDialog, "
							+ "CheckBoxClientRolesKeyAdapter" + "'Exit' button pressed - " 
							+ "THE APPLICATIONS STOPS");
					System.exit(0);
				}
			}
		});		
		// check box client roles item listener
		this.checkBoxClientRoles.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
					ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
					ConfigStartOptionsDialog.this.sellerRadioButton.setEnabled(false);
					ConfigStartOptionsDialog.this.adminRadioButton.setEnabled(false);
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(true);
					ConfigStartOptionsDialog.this.sellerRadioButton.setEnabled(true);
					ConfigStartOptionsDialog.this.adminRadioButton.setEnabled(true);
				}
			}
		});			
		return panelRolesCheckBox;
	}

	/**
	 * Returns a combo box to display the different look and feel
	 * layouts.
	 * <br> The method adds a key listener and a pop up menu listener
	 * to the combo box.
	 * 
	 * @return JComboBox - it displays the different look and feel
	 * layouts.
	 */
	private JComboBox<String> getComboBoxLookAndFell(){
		final LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		final String[] lookStrings = new String[installedLookAndFeels.length];
		int i = 0;
		for (final LookAndFeelInfo lafInfo : installedLookAndFeels) {
			lookStrings[i++] = lafInfo.getClassName();
		}
		this.lookCombo = new JComboBox<String>(lookStrings);
		this.lookCombo.setEnabled(false);
		this.lookCombo.setMaximumRowCount(45);
		this.lookCombo.setFont(new Font("Italic", Font.ROMAN_BASELINE, 11));
		this.lookCombo.setEditable(false);
		this.lookCombo.setToolTipText("Available look&feels");
		this.lookCombo.setSelectedIndex(lookIndex);
		this.lookCombo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
									+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.log
							.info("ConfigStartOptionsDialog, 'Enter' pressed - " 
											+ "the look and feel layout will be adjusted");
					lookIndex = ConfigStartOptionsDialog.this.lookCombo.getSelectedIndex();
					setLookAndFeel(
							lookStrings[
							  ConfigStartOptionsDialog.this.lookCombo.getSelectedIndex()]);
				}
			}
		});
		this.lookCombo.addPopupMenuListener(new PopupMenuListener(){
			@Override
			public void popupMenuCanceled(final PopupMenuEvent arg0) {
				ConfigStartOptionsDialog.this.log.fine("ConfigStartOptilog, init, "
						+ "lockCombo, PopupMenuListener, "
						+ "event: " + arg0.toString());
			}
			@Override
			public void popupMenuWillBecomeInvisible(final PopupMenuEvent arg0) {
				ConfigStartOptionsDialog.this.log.fine("ConfigStartOptilog, init, "
						+ "lockCombo, PopupMenuListener, "
						+ "event: " + arg0.toString());
				lookIndex = ConfigStartOptionsDialog.this.lookCombo.getSelectedIndex();
				setLookAndFeel(
						lookStrings[
						        ConfigStartOptionsDialog.this.lookCombo.getSelectedIndex()]);
			}
			@Override
			public void popupMenuWillBecomeVisible(final PopupMenuEvent arg0) {
				ConfigStartOptionsDialog.this.log.fine("ConfigStartOptilog, init, "
						+ "lockCombo, PopupMenuListener, "
						+ "event: " + arg0.toString());				
			}
		});
		return this.lookCombo;
	}

	/**
	 * Initializes 'Start', 'Exit' button and the 
	 * check box 'Enhancements'.
	 * <br> The method adds a key listener and a 
	 * an action listener to the start and exit button.
	 * <br> Additional it does some adjustments to the check
	 * box, which controls enhancements.
	 */
	private void setStartExitListener(){
		this.startButton.setActionCommand("START");
		this.startButton.setMnemonic('S');
		this.startButton.setToolTipText(
				"Starts Application after start configuration");	
		// Initializing 'Exit' button
		this.exitButton.setActionCommand("EXIT");
		this.exitButton.setMnemonic('E');
		this.exitButton.setToolTipText("Press Button if you want to quit");
		this.checkBoxEnhancements.setMnemonic('C');
		this.checkBoxEnhancements.setToolTipText("Enables enhancements.");
		this.checkBoxEnhancements.setFont(new Font("Italic", Font.PLAIN, 11));		
		this.checkBoxEnhancements.setSelected(false);	
		
		// start button add key listener
		this.startButton.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.dialog.dispose();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		this.startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if(arg0.getActionCommand().equals("START")){
					ConfigStartOptionsDialog.this.dialog.dispose();
				}
			}	
		});
		this.exitButton.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ConfigStartOptionsDialog.this.log.info("ConfigStartOptionsDialog, "
							+ "'Exit' button selected, enter pressed - "
							+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
					.info("ConfigStartOptionsDialog, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		// button exit action listener
		this.exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e) {			
				if (e.getActionCommand().equals("EXIT")) {
					ConfigStartOptionsDialog.this.log
					.info("ButtonExitActionListener, 'alt + E' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Adds a key listener to the check box 'Enhancements'
	 * and adds an item listener to the check box
	 * 'Enhancements' and to the radio buttons
	 * 'Buyer', 'Seller' and 'Admin', if the application
	 * runs as a local client.
	 */
	private void setLocalClientListeners(){
		this.checkBoxEnhancements.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(false);
						ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
	
					} else if (!ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
						ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(true);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log.severe("ConfigStartOptionsDialog, "
							+ "CheckBoxEnhanceAloneKeyAdapter" + "'Exit' button pressed - " 
							+ "THE APPLICATIONS STOPS");
					System.exit(0);
				}
			}
		});
		this.checkBoxEnhancements.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setSelected(false);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(false);
					ConfigStartOptionsDialog.this.buyerRadioButton.setSelected(true);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(false);
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
					ConfigStartOptionsDialog.this.checkBoxClientRoles.setEnabled(true);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(true);
				}
			}
		});
			
		this.sellerRadioButton.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.sellerRadioButton.setSelected(false);
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.sellerRadioButton.setSelected(true);
				}
			}
		});
		this.adminRadioButton.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.adminRadioButton.setSelected(false);
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.adminRadioButton.setSelected(true);
				}
			}
		});
	}

	/**
	 * Adds a key listener and a item listener to the
	 * check box 'Enhancements', if the application runs
	 * as a server.
	 */
	private void setServerListeners(){
		this.checkBoxEnhancements.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
					} else if (!ConfigStartOptionsDialog.this.checkBoxEnhancements.isSelected()) {
						ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ConfigStartOptionsDialog.this.log
							.info("CheckBoxEnhanceServerKeyAdapter, 'Esc' pressed - " 
					+ "THE APPLICATIONS STOPS NOW");
					System.exit(0);
				}
			}
		});
		// check box enhanced server item listener
		this.checkBoxEnhancements.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(false);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(false);
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					ConfigStartOptionsDialog.this.checkBoxEnhancements.setSelected(true);
					ConfigStartOptionsDialog.this.lookCombo.setEnabled(true);
				}
			}
		});
	}

	/**
	 * Returns a text area, which displays an introduction
	 * to the application mode.
	 * 
	 * @return JTextArea - display an introduction to the application
	 * mode.
	 * 
	 */
	private static JTextArea getTextAreaAppMode(){
		final JTextArea textModeArea = 
				new JTextArea(ConfigStartOptionsDialog.TEXT_MODE);
		textModeArea.setEditable(false);
		textModeArea.setFocusable(false);
		textModeArea.setToolTipText(
				ConfigStartOptionsDialog.TEXT_MODE_TOOL_TIP);
		textModeArea.setFont(new Font("Bolt", Font.PLAIN, 12));
		return textModeArea;
	}
	
	/**
	 * Returns a label, which displays the selected application mode.
	 * 
	 * @param aM The application node.
	 * 
	 * @return JLabel - displays the chosen application mode.
	 */
	private static JLabel getLabelAppMode(final ApplicationMode aM){
		String modeStatusString = "";
		if (aM == ApplicationMode.NETWORK_CLIENT) {
			modeStatusString = "Your Mode:          Network Client   " 
					+ "(needs a running server)";
		} else if (aM == ApplicationMode.LOCAL_CLIENT) {
			modeStatusString = "Your Mode:          Local Client";
		} else if (aM == ApplicationMode.SERVER) {
			modeStatusString = "Your Mode:          Server";
		} else {
			JOptionPane.showMessageDialog(null, "wrong input command line");
		}
		final JLabel modeValueLabel = new JLabel(modeStatusString);
		modeValueLabel.setToolTipText("The application recognizes "
				+ "3 command line arguements: "
				+ "1. \"alone\" - 2. \"server\" - 3.\"\"(no argument)");
		modeValueLabel.setFocusable(false);	
		return modeValueLabel;
	}
	
	/**
	 * Returns a text area, which displays an introduction to the
	 * ID of a client.
	 * 
	 * @return JTextArea - displays introduction to the ID of a client.
	 */
	private static JTextArea getTextAreaId(){
		final JTextArea textIdLabel = 
				new JTextArea(ConfigStartOptionsDialog.TEXT_ID);
		textIdLabel.setEditable(false);
		textIdLabel.setFont(new Font("Bolt", Font.PLAIN, 12));
		textIdLabel.setFocusable(false);
		textIdLabel.setToolTipText(
				ConfigStartOptionsDialog.TEXT_ID_TOOL_TIP);
		return textIdLabel;
	}
	
	/**
	 * Returns a text area, which displays an introduction 
	 * to the client role.
	 * 
	 * @return JTextArea - it displays an introduction 
	 * to the client role.
	 */
	private static JTextArea getTextAreaClientRole(){
		final JTextArea textClientRoles = new JTextArea(
				ConfigStartOptionsDialog.TEXT_CLIENT_ROLE);
		textClientRoles.setEditable(false);
		textClientRoles.setFocusable(false);
		textClientRoles.setFont(new Font("Bolt", Font.PLAIN, 12));
		textClientRoles.setToolTipText("Sets the Access Level");
		return textClientRoles;
	}

	/**
	 * Returns a text area, which displays an introduction to the
	 * enhancements.
	 * 
	 * @return JTextArea - displays an introduction to the
	 * enhancements.
	 */
	private static JTextArea getTextAreaEnhancementsAll(){
		final JTextArea textEnhancements = new JTextArea();
		textEnhancements.setEditable(false);
		textEnhancements.setFont(new Font("Bolt", Font.PLAIN, 12));
		textEnhancements.setFocusable(false);
		textEnhancements.setToolTipText(
				ConfigStartOptionsDialog.TEXT_ID_TOOL_TIP);
		return textEnhancements;
	}

	/**
	 * Returns a text area, which displays an introduction to the 
	 * look and feel layout.
	 * 
	 * @return JTextArea - it displays an introduction to the 
	 * look and feel layout.
	 */
	private static JTextArea getTextAreaLookFeel(){
		final JTextArea textLook = 
				new JTextArea(ConfigStartOptionsDialog.TEXT_LOOK);
		textLook.setEditable(false);
		textLook.setFocusable(false);
		textLook.setFont(new Font("Italic", Font.PLAIN, 11));
		textLook.setToolTipText("Layouts the sheet");
		return textLook;
	}

	/**
	 * Returns a text area to display the data of the author.
	 * 
	 * @return JTextArea - it displays the data of the author.
	 */
	private static JTextArea getTextAreaAuthor(){
		final JTextArea textAutor = 
				new JTextArea(ConfigStartOptionsDialog.TEXT_AUTHOR);
		textAutor.setEditable(false);
		textAutor.setFocusable(false);
		textAutor.setFont(new Font("Italic", Font.PLAIN, 9));
		textAutor.setToolTipText("Stefan Streifeneder, Tel. +49 151 401 45 725");
		return textAutor;
	}
}
