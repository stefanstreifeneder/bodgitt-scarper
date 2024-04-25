package suncertify.gui.seller;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.ApplicationMode;

/**
 * A frame, which possesses entry fields to set the values to create a new
 * Record.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class WindowAddSeller extends javax.swing.JFrame {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 224L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.WindowAddSeller</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.WindowAddSeller*"), 
			Level.ALL);

	/**
	 * Entry field for 'city'.
	 */
	private JTextField cityTextField;

	/**
	 * Entry field for 'name'.
	 */
	private JTextField nameTextField;

	/**
	 * Entry field for 'rate'.
	 */
	private JTextField rateTextField;

	/**
	 * Entry field for Record number.
	 */
	private JTextField recNoTextField;

	/**
	 * Entry field for 'types'.
	 */
	private JTextField serviceTextField;

	/**
	 * Entry field for number of 'staff'.
	 */
	private JTextField staffTextField;

	/**
	 * Entry field for ID of an owner.
	 */
	private JTextField idOwnerTextField;

	/**
	 * The 'Add' button.
	 */
	private JButton addButton;

	/**
	 * The 'Exit' button.
	 */
	private JButton exitButton;

	/**
	 * Creates a window to set the values for the new Record.
	 * 
	 * @param appMode
	 *            The application mode.
	 * @param idOwner
	 *            The ID of an owner.
	 */
	
	public WindowAddSeller(ApplicationMode appMode,
			int idOwner) {
		this.log.entering("WindowAddSeller", "WindowAddSeller",
				new Object[] {appMode, Integer.valueOf(idOwner) });		
		this.setTitle("B&S-ADD Client No: " + idOwner + " - " + appMode.name());
		this.initComponents();
		this.addButton.setMnemonic('A');
		this.exitButton.setMnemonic('E');
		this.setVisible(true);
		this.log.exiting("WindowAddSeller", "WindowAddSeller");
	}

	/**
	 * Returns the current values of the entry fields.
	 * 
	 * @return String[] - Values of the Record.
	 */
	public String[] getFieldVals() {
		String[] fields = new String[7];
		fields[0] = this.nameTextField.getText();
		fields[1] = this.cityTextField.getText();
		fields[2] = this.serviceTextField.getText();
		fields[3] = this.staffTextField.getText();
		fields[4] = this.rateTextField.getText();
		fields[5] = this.idOwnerTextField.getText();
		fields[6] = this.recNoTextField.getText();
		return fields;
	}

	/**
	 * Adds a key listener to all entry fields.
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
		this.idOwnerTextField.addKeyListener(ka);
		this.recNoTextField.addKeyListener(ka);
	}

	/**
	 * Adds a window listener.
	 * 
	 * @param wa
	 *            Handles window events.
	 */
	public void setAddWindowListener(WindowAdapter wa) {
		this.addWindowListener(wa);
	}		

	/**
	 * Sets the <code>java.awt.event.ActionListener</code> for the <code>addButton</code>
	 * and the <code>exitButton</code>.
	 * 
	 * @param al Handles actions.
	 */
	public void setWindowAddButtonActionListener(ActionListener al) {
		this.addButton.addActionListener(al);
		this.exitButton.addActionListener(al);
	}
	
	/**
	 * Sets the <code>java.awt.event.KeyListener</code> for the <code>addButton</code>
	 * and the <code>exitButton</code>.
	 * 
	 * @param ka Handles key press actions.
	 */
	public void setWindowAddButtonKexListener(KeyAdapter ka) {
		this.addButton.addKeyListener(ka);
		this.exitButton.addKeyListener(ka);
	}

	/**
	 * Initialize all Components.
	 */
	private void initComponents() {
		JLabel headLineLabel = new JLabel();
		JLabel nameLabel = new JLabel();
		JLabel cityLabel = new JLabel();
		JLabel serviceLabel = new JLabel();
		JLabel staffLabel = new JLabel();
		JLabel rateLabel = new JLabel();
		JLabel idOwnerLabel = new JLabel();
		JLabel recNoLabel = new JLabel();
		this.nameTextField = new JTextField();
		this.cityTextField = new JTextField();
		this.serviceTextField = new JTextField();
		this.staffTextField = new JTextField();
		this.rateTextField = new JTextField();
		this.idOwnerTextField = new JTextField();
		this.recNoTextField = new JTextField();
		this.addButton = new JButton();
		this.exitButton = new JButton();
		this.exitButton.setActionCommand(
				ActionCommandButtons.WINADD_EXIT.name());
		this.addButton.setActionCommand(
				ActionCommandButtons.WINADD_ADD.name());
		this.setSize(450, 450);
		this.setResizable(false);
		headLineLabel.setText("  Create new Record ");
		nameLabel.setText("  Name  (max 32 signs)");
		cityLabel.setText("  City  (max 64 signs)");
		serviceLabel.setText("  Services  (max 64 signs)");
		staffLabel.setText("  Staff  (max 6 digits)");
		rateLabel.setText("  Rate  ('$dddd.dd)");
		idOwnerLabel.setText(" Id Owner (max 8 digits)");
		recNoLabel.setText("  Record Number (only digits)");
		this.nameTextField
				.setToolTipText("Please enter your values, "
						+ "max 32 signs, only UTF-8!");
		this.cityTextField
				.setToolTipText("Please enter your values, " 
						+ "max 64 signs, only UTF-8!");
		this.serviceTextField
				.setToolTipText("Please enter your values, " 
						+ "max 64 signs, only UTF-8!");
		this.staffTextField.setToolTipText("Please max 6 digits!");
		this.rateTextField.setToolTipText("Please keep format: '$xxxx.xx");
		this.idOwnerTextField.setToolTipText("Please max 8 digits!");
		this.recNoTextField.setToolTipText(
				"Your enter could may be " + "not be accomplished. "
						+ "Instead of your entered record number, "
						+ "the first available number will be choosen!");
		this.addButton.setText("Add");
		this.addButton.setToolTipText("Please press button to add this Record!");
		this.exitButton.setText("Cancel");
		this.exitButton.setToolTipText(
				"Please press " + "this button to exit the adding operation " 
						+ "and to exit this window!");
		GridLayout layout = new GridLayout(9, 2);
		layout.setHgap(15);
		layout.setVgap(15);
		this.setLayout(layout);
		this.add(headLineLabel);
		this.add(new JLabel(" Please enter your values!"));
		this.add(nameLabel);
		this.add(this.nameTextField);
		this.add(cityLabel);
		this.add(this.cityTextField);
		this.add(serviceLabel);
		this.add(this.serviceTextField);
		this.add(staffLabel);
		this.add(this.staffTextField);
		this.add(rateLabel);
		this.add(this.rateTextField);
		this.add(idOwnerLabel);
		this.add(this.idOwnerTextField);
		this.add(recNoLabel);
		this.add(this.recNoTextField);
		this.add(this.addButton);
		this.add(this.exitButton);
	}	
}
