package suncertify.gui.seller;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;

/**
 * An object of the class is a <code>javax.swing.JPanel</code>. It  
 * possesses four buttons to support the features to read a Record by 
 * Record number, to update, to delete or to add a Record.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class PanelUpDelAddSeller extends JPanel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 223L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.PanelUpDelAddSeller</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.PanelUpDelAddSeller"), 
			Level.ALL);

	/**
	 * The read button.
	 */
	private JButton readButton = new JButton("Choose Record");

	/**
	 * The update button.
	 */
	private JButton updateButton = new JButton("Update Record");

	/**
	 * The delete button.
	 */
	private JButton deleteButton = new JButton("Delete Record");

	/**
	 * The add button.
	 */
	private JButton addButton = new JButton("Add Record");

	/**
	 * Constructs an object of the class.
	 * Adds an <code>ActionListener</code> and a 
	 * <code>KeyAdapter</code> to the panel.
	 * @param aL An action listeners for the buttons.
	 * @param kL A key listeners listeners to the buttons.
	 * 
	 */
	public PanelUpDelAddSeller(ActionListener aL, KeyAdapter kL) {
		this.log.entering("PanelUpDelAddSeller", "PanelUpDelAddSeller");
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.readButton = new JButton("Read Record");
		this.readButton.setActionCommand(ActionCommandButtons.PANUDRA_READ.name());		
		this.readButton.setMnemonic(KeyEvent.VK_A);
		this.readButton.setToolTipText("Opens a window with all listed "
				+ "Records by record Number");
		this.updateButton = new JButton("Update Record");
		this.updateButton.setActionCommand(
				ActionCommandButtons.PANUDRA_UPDATE.name());		
		this.updateButton.setMnemonic(KeyEvent.VK_P);
		this.updateButton.setToolTipText("Opens a window to update a Record");
		this.deleteButton = new JButton("Delete Record");
		this.deleteButton.setActionCommand(
				ActionCommandButtons.PANUDRA_DELETE.name());		
		this.deleteButton.setMnemonic(KeyEvent.VK_D);
		this.deleteButton.setToolTipText("Opens a window to delete a Record");
		this.addButton = new JButton("Add Record");
		this.addButton.setActionCommand(
				ActionCommandButtons.PANUDRA_ADD.name());
		this.addButton.setMnemonic(KeyEvent.VK_O);
		this.addButton.setToolTipText("Opens a window to create " 
				+ "a new Record");
		this.add(this.readButton);
		this.add(this.updateButton);
		this.add(this.deleteButton);
		this.add(this.addButton);		
		this.readButton.addActionListener(aL);
		this.updateButton.addActionListener(aL);
		this.deleteButton.addActionListener(aL);
		this.addButton.addActionListener(aL);		
		this.readButton.addKeyListener(kL);
		this.updateButton.addKeyListener(kL);
		this.deleteButton.addKeyListener(kL);
		this.addButton.addKeyListener(kL);		
		this.log.exiting("PanelUpDelAddSeller", "PanelUpDelAddSeller");
	}	

	/**
	 * Sets the 'Add Record' button enabled or disabled.
	 * 
	 * @param enabled
	 *            Indicates whether the add button should be enabled or not.
	 */
	public void setPanelUDA_AddButtEnabl(boolean enabled) {
		this.addButton.setEnabled(enabled);
	}

	/**
	 * Returns true, if the 'Add Record' button is enabled.
	 * 
	 * @return boolean - True, if the 'Add' button is enabled.
	 */
	public boolean isPanUpDelAddButtEnabl() {
		return this.addButton.isEnabled();
	}
}
