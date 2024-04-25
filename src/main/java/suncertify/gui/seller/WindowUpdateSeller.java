package suncertify.gui.seller;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.ApplicationMode;

/**
 * Creates a window to update the values of a Record.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class WindowUpdateSeller extends JFrame {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 226L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.WindowUpdateSeller</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.WindowUpdateSeller"), 
			Level.ALL);

	/**
	 * Stores the introduction text for the city.
	 */
	private JLabel cityLabel = null;

	/**
	 * Entry field for 'city'.
	 */
	private JTextField cityTextField;

	/**
	 * Introduction text.
	 */
	private JLabel headlineLabel;

	/**
	 * Stores the Record number of the Record to update.
	 */
	private JLabel headlineRecNoLabel;

	/**
	 * Stores 'name'.
	 */
	private JLabel nameLabel;

	/**
	 * Entry field to set the name.
	 */
	private JTextField nameTextField;

	/**
	 * Stores 'Hourly Rate ('$dddd.dd)'.
	 */
	private JLabel rateLabel;

	/**
	 * Entry field to set the rate value.
	 */
	private JTextField rateTextField;

	/**
	 * Stores 'ID Owner (1-99999999)'.
	 */
	private JLabel ownerLabel;

	/**
	 * Entry field to set the rate value.
	 */
	private JTextField ownerTextField;

	/**
	 * Stores 'Service (max 64 signs)'.
	 */
	private JLabel serviceLabel;

	/**
	 * Entry field for types of services.
	 */
	private JTextField serviceTextField;

	/**
	 * Stores 'Staff (max. 6 digits)'.
	 */
	private JLabel staffLabel;

	/**
	 * Entry field for the amount of members of the staff.
	 */
	private JTextField staffTextField;

	/**
	 * The exit button.
	 */
	private JButton exitButton;

	/**
	 * The update button.
	 */
	private JButton updateButton;

	/**
	 * Creates a new update window.
	 * 
	 * @param data
	 *            The new values of the Record.
	 * @param appMode
	 *            Indicates the application mode.
	 * @param idOwner
	 *            Represents the ID of the owner. Only used for the title
	 *            of the window.
	 */
	public WindowUpdateSeller(String[] data, 
			ApplicationMode appMode, int idOwner) {
		this.log.entering("WindowUpdateSeller", "WindowUpdateSeller", data);
		this.setTitle("B&S-UPDATE Client No: " + idOwner + " - " 
				+ appMode.name());
		this.initComponents();
		this.headlineRecNoLabel.setText(data[6]);
		this.nameTextField.setText(data[0]);
		this.cityTextField.setText(data[1]);
		this.serviceTextField.setText(data[2]);
		this.staffTextField.setText(data[3]);
		this.rateTextField.setText(data[4]);
		this.ownerTextField.setText(data[5]);
		this.setVisible(true);
		this.log.exiting("WindowUpdateSeller", "WindowUpdateSeller");
	}

	/**
	 * Returns the values of a Record.
	 * 
	 * @return String[] - The values of the Record.
	 */
	public String[] getFieldVals() {
		String[] fields = new String[6];
		fields[0] = this.nameTextField.getText();
		fields[1] = this.cityTextField.getText();
		fields[2] = this.serviceTextField.getText();
		fields[3] = this.staffTextField.getText();
		fields[4] = this.rateTextField.getText();
		fields[5] = this.ownerTextField.getText();// owner value
		return fields;
	}

	/**
	 * Adds a key listener to the entry fields.
	 * 
	 * @param ka
	 *            Handles key events.
	 */
	public void setFieldKeyListener(KeyAdapter ka) {
		this.nameTextField.addKeyListener(ka);
		this.cityTextField.addKeyListener(ka);
		this.serviceTextField.addKeyListener(ka);
		this.staffTextField.addKeyListener(ka);
		this.rateTextField.addKeyListener(ka);
		this.ownerTextField.addKeyListener(ka);
		this.ownerTextField.setName("WinUp_fields");
	}	

	/**
	 * Adds a window listener.
	 * 
	 * @param wa
	 *            Handles window events.
	 */
	public void setThisUpWindowListener(WindowAdapter wa) {
		this.addWindowListener(wa);
	}

	/**
	 * Sets the <code>java.awt.event.ActionListener</code> for the 
	 * <code>updateButton</code> and the <code>exitButton</code>.
	 * 
	 * @param aL Handles actions.
	 */
	public void setWindowUpdateActionListener(ActionListener aL){
		this.exitButton.addActionListener(aL);
		this.updateButton.addActionListener(aL);
	}
	
	/**
	 * Sets the <code>java.awt.event.KeyListener</code> for the 
	 * <code>updateButton</code> and the <code>exitButton</code>.
	 * 
	 * @param kA Handles keystroke actions.
	 */
	public void setWindowUpdateKeyListener(KeyAdapter kA){
		this.exitButton.addKeyListener(kA);
		this.updateButton.addKeyListener(kA);
	}

	/**
	 * Initializes all Components.
	 */
	private void initComponents() {
		this.headlineLabel = new JLabel();
		this.headlineRecNoLabel = new JLabel();
		this.nameLabel = new JLabel();
		this.cityLabel = new JLabel();
		this.serviceLabel = new JLabel();
		this.staffLabel = new JLabel();
		this.rateLabel = new JLabel();
		this.ownerLabel = new JLabel();
		this.nameTextField = new JTextField();
		this.cityTextField = new JTextField();
		this.serviceTextField = new JTextField();
		this.staffTextField = new JTextField();
		this.rateTextField = new JTextField();
		this.ownerTextField = new JTextField();
		this.updateButton = new JButton();
		this.exitButton = new JButton();
		this.exitButton.setActionCommand(ActionCommandButtons.WINUP_EXIT.name());
		this.updateButton.setActionCommand(
				ActionCommandButtons.WINUP_UPDATE.name());
		this.setSize(450, 400);
		this.setResizable(false);
		this.headlineLabel.setText("  UPDATE Record Number:");
		this.headlineRecNoLabel.setText("record number");
		this.nameLabel.setText("  Name  (max 32 signs)");
		this.cityLabel.setText("  City  (max 64 signs)");
		this.serviceLabel.setText("  Service (max 64 signs)");
		this.staffLabel.setText("  Staff  (max. 6 digits)");
		this.rateLabel.setText("  Hourly Rate  ('$dddd.dd)");
		this.ownerLabel.setText("  ID Owner  (1-99999999)");
		this.nameTextField.setText("jTextField1");
		this.nameTextField
				.setToolTipText("Please enter your values, max " 
						+ "32 signs, only UTF-8!");
		this.cityTextField.setText("jTextField2");
		this.cityTextField
				.setToolTipText("Please enter your values, max " 
						+ "32 signs, only UTF-8!");
		this.serviceTextField.setText("jTextField3");
		this.serviceTextField
				.setToolTipText("Please enter your values, max " 
						+ "32 signs, only UTF-8!");
		this.staffTextField.setText("jTextField4");
		this.staffTextField.setToolTipText("Please max 6 digits!");
		this.rateTextField.setText("jTextField5");
		this.rateTextField.setToolTipText("Please keep the "
				+ "format: '$xxxx.xx'!");
		this.ownerTextField.setText("jTextField5");
		this.ownerTextField.setToolTipText("Please keep the "
				+ "format: 1-99999999!");
		this.updateButton.setMnemonic('U');
		this.updateButton.setText("Update");
		this.updateButton.setToolTipText("Press Button for "
				+ "updating this Record!");
		this.exitButton.setMnemonic('e');
		this.exitButton.setText("Exit");
		this.exitButton.setToolTipText("Press button to exit the operating " 
				+ "oparation and to exit this window!");
		GridLayout layout = new GridLayout(8, 2);
		layout.setHgap(15);
		layout.setVgap(15);
		this.setLayout(layout);
		this.add(this.headlineLabel);
		this.add(this.headlineRecNoLabel);
		this.add(this.nameLabel);
		this.add(this.nameTextField);
		this.add(this.cityLabel);
		this.add(this.cityTextField);
		this.add(this.serviceLabel);
		this.add(this.serviceTextField);
		this.add(this.staffLabel);
		this.add(this.staffTextField);
		this.add(this.rateLabel);
		this.add(this.rateTextField);
		this.add(this.ownerLabel);
		this.add(this.ownerTextField);
		this.add(this.updateButton);
		this.add(this.exitButton);
	}
}
