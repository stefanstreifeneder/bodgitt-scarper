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

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.ApplicationMode;

/**
 * A graphical surface to delete a Record.
 * It displays the values of the Record.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class WindowDeleteSeller extends JFrame {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 225L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.WindowDeleteSeller</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.WindowDeleteSeller"), 
			Level.ALL);

	/**
	 * The headline.
	 */
	private JLabel headlineLabel;

	/**
	 * Displays the Record number of the Record, which should deleted.
	 */
	private JLabel headlineRecNoLabel;

	/**
	 * Displays 'Name'.
	 */
	private JLabel nameLabel;

	/**
	 * Displays 'City'.
	 */
	private JLabel cityLabel;

	/**
	 * Displays 'Services'.
	 */
	private JLabel serviceLabel;

	/**
	 * Displays 'Staff'.
	 */
	private JLabel staffLabel;

	/**
	 * Displays 'Rate'.
	 */
	private JLabel rateLabel;

	/**
	 * Displays 'ID Owner'.
	 */
	private JLabel idOwnerLabel;

	/**
	 * Displays the name of the Record, which should be deleted.
	 */
	private JLabel recordNameLabel;

	/**
	 * Displays the city of the Record, which should be deleted.
	 */
	private JLabel recordCityLabel;

	/**
	 * Displays the services of the Record, which should be deleted.
	 */
	private JLabel recordServiceLabel;

	/**
	 * Displays the rate of the Record, which should be deleted.
	 */
	private JLabel recordRateLabel;

	/**
	 * Displays the staff of the Record, which should be deleted.
	 */
	private JLabel recordStaffLabel;

	/**
	 * Displays the ID owner of the Record, which should be deleted.
	 */
	private JLabel recordIdOwnerLabel;

	/**
	 * The 'Delete' button.
	 */
	private JButton deleteButton;

	/**
	 * The 'Exit' button.
	 */
	private JButton exitButton;

	/**
	 * Creates a window to delete a Record.
	 * 
	 * @param data
	 *            The Record values as <code>java.lang.String</code> objects.
	 * @param appMode
	 *            The application mode.
	 * @param idOwner
	 *            The ID of an owner.
	 */
	public WindowDeleteSeller(String[] data, 
			ApplicationMode appMode, int idOwner) {
		this.log.entering("WindowDeleteSeller", "WindowDeleteSeller",
				new Object[] { data, appMode, Integer.valueOf(idOwner) });
		this.setTitle("B&S-DELETE Client No: " + idOwner + " - " 
				+ appMode.name());
		this.initComponents();
		this.headlineRecNoLabel.setText(data[6]);
		this.recordNameLabel.setText(data[0]);
		this.recordCityLabel.setText(data[1]);
		this.recordServiceLabel.setText(data[2]);
		this.recordStaffLabel.setText(data[3]);
		this.recordRateLabel.setText(data[4]);
		this.recordIdOwnerLabel.setText(data[5]);
		this.setVisible(true);
		this.log.exiting("WindowDeleteSeller", "WindowDeleteSeller");
	}

	/**
	 * Sets the delete button disabled.
	 * 
	 * @param enabled
	 *            True, if the button is enabled.
	 */
	public void setDelButtEnabled(boolean enabled) {
		this.deleteButton.setEnabled(enabled);
	}

	/**
	 * Adds a window listener.
	 * 
	 * @param wa
	 *            Handles window events.
	 */
	public void setThisDelWindowListener(WindowAdapter wa) {
		this.addWindowListener(wa);
	}

	/**
	 * Returns the values of the Record.
	 * 
	 * @return String[] - The values of the Record.
	 */
	public String[] getFieldVals() {
		String[] fields = new String[6];
		fields[0] = this.recordNameLabel.getText();
		fields[1] = this.recordCityLabel.getText();
		fields[2] = this.recordServiceLabel.getText();
		fields[3] = this.recordStaffLabel.getText();
		fields[4] = this.recordRateLabel.getText();
		fields[5] = this.recordIdOwnerLabel.getText();
		return fields;
	}
	
	/**
	 * Sets the <code>java.awt.event.ActionListener</code> for the 
	 * <code>deleteButton</code> and the <code>exitButton</code>.
	 * 
	 * @param aL Handles actions.
	 */
	public void setWindowDeleteActionListener(ActionListener aL){
		this.deleteButton.addActionListener(aL);
		this.exitButton.addActionListener(aL);
	}
	
	/**
	 * Sets the <code>java.awt.event.KeyListener</code> for the 
	 * <code>deleteButton</code> and the <code>exitButton</code>.
	 * 
	 * @param kA Handles keystroke actions.
	 */
	public void setWindowDeleteKeyListener(KeyAdapter kA){
		this.deleteButton.addKeyListener(kA);
		this.exitButton.addKeyListener(kA);
	}

	/**
	 * Initializes the components.
	 */
	private void initComponents() {
		this.headlineLabel = new javax.swing.JLabel();
		this.nameLabel = new JLabel();
		this.cityLabel = new JLabel();
		this.serviceLabel = new JLabel();
		this.staffLabel = new JLabel();
		this.rateLabel = new JLabel();
		this.idOwnerLabel = new JLabel();
		this.recordNameLabel = new JLabel();
		this.recordCityLabel = new JLabel();
		this.recordServiceLabel = new JLabel();
		this.recordStaffLabel = new JLabel();
		this.recordRateLabel = new JLabel();
		this.recordIdOwnerLabel = new JLabel();
		this.deleteButton = new JButton();
		this.exitButton = new JButton();
		this.exitButton.setActionCommand(
				ActionCommandButtons.WINDEL_EXIT.name());
		this.deleteButton.setActionCommand(
				ActionCommandButtons.WINDEL_DELETE.name());
		this.headlineRecNoLabel = new JLabel();
		this.setSize(450, 400);
		this.setResizable(false);
		this.headlineLabel.setText("  DELETE Record Number:");
		this.nameLabel.setText("  Name");
		this.cityLabel.setText("  City");
		this.serviceLabel.setText("  Services");
		this.staffLabel.setText("  Staff");
		this.rateLabel.setText("  Rate");
		this.idOwnerLabel.setText(" Id Owner");
		this.headlineRecNoLabel.setText("XXXXXXXX");
		this.recordNameLabel.setText("name");
		this.deleteButton.setMnemonic('D');
		this.deleteButton.setText("Delete");
		this.deleteButton.setToolTipText("Press button to delete this Record!");
		this.exitButton.setMnemonic('E');
		this.exitButton.setText("Cancel");
		this.exitButton.setToolTipText("Press this button to exit the "
				+ "deleteing operation and to exit this window!");
		GridLayout layout = new GridLayout(8, 2);
		layout.setHgap(15);
		layout.setVgap(15);
		this.setLayout(layout);
		this.add(this.headlineLabel);
		this.add(this.headlineRecNoLabel);
		this.add(this.nameLabel);
		this.add(this.recordNameLabel);
		this.add(this.cityLabel);
		this.add(this.recordCityLabel);
		this.add(this.serviceLabel);
		this.add(this.recordServiceLabel);
		this.add(this.staffLabel);
		this.add(this.recordStaffLabel);
		this.add(this.rateLabel);
		this.add(this.recordRateLabel);
		this.add(this.idOwnerLabel);
		this.add(this.recordIdOwnerLabel);
		this.add(this.deleteButton);
		this.add(this.exitButton);
	}
}
