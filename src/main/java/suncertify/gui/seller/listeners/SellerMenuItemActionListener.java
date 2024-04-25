package suncertify.gui.seller.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandMenuItems;
import suncertify.gui.PanelTable;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * The class implements the interface <code>java.awt.event.ActionListener</code>.
 * It handles all action events associated with the menu bar
 * of a Seller Client
 * <code>suncertify.gui.seller.MainWindowMenuBarSeller</code>.
 * <br>
 * The action commands are defined by the enum 
 * <code>suncertify.gui.ActionCommandMenuItems</code>.
 * 
 * 
 * 
 * @see suncertify.gui.ActionCommandMenuItems  
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerMenuItemActionListener implements ActionListener{

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.SellerMenuItemActionListener</code>.
	 */
	private final Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.buyer.listeners.SellerMenuItemActionListener"), 
					Level.ALL);
	/**
	 * A reference to the table.
	 */
	private final PanelTable panTable;

	/**
	 * A reference to the main window.
	 */
	private final MainWindowSeller mainW;
	
	/**
	 * The panel to read, update, delete and add a Record.
	 */
	private final PanelUpDelAddSeller panUpDel;

	/**
	 * Creates an object of this class.
	 * @param mw Reference to the main window.
	 * @param tablePan Reference to the table.
	 * @param upDelPan The panel to read, update, delete and add a Record.
	 */
	public SellerMenuItemActionListener(final MainWindowSeller mw,
			final PanelTable tablePan,
			final PanelUpDelAddSeller upDelPan) {
		this.mainW = mw;
		this.panTable = tablePan;
		this.panUpDel = upDelPan;
	}		
	
	/**
	 * Overridden method, which cares for <code>java.awt.event.ActionEvents</code>
	 * evoked by menu items of a Seller client's menu bar.
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {		
		final String butt = e.getActionCommand();
		switch (ActionCommandMenuItems.valueOf(butt)) {
			case MENUFILTER_REFRESH:
				if (!this.panTable.tableHasFocus()) {
					this.mainW.searchForRecords(new String[] {""});
				}
				break;
			case MENUFILTER_NAME:
				final String inputName = JOptionPane.showInputDialog(
						null, "Enter character to search in 'Name':");
				if(inputName == null) {
					break;
				}
				this.mainW.searchForRecords(new String[] { inputName });
				break;
			case MENUFILTER_CITY:
				final String inputCity = JOptionPane.showInputDialog(
						null, "Enter character to search in 'City':");
				if(inputCity == null) {
					break;
				}
				this.mainW.searchForRecords(new String[] { "", inputCity });
				break;
			case MENUFILTER_ALL:
				final String inputAll = JOptionPane.showInputDialog(null,
						"Search for any field of a Record!"
						+ "\nSplit by using ';'." 
						+ "\n"
						+ "\nExample: search for all companies with 4 staff"
						+ " members" 
						+ "\nInput:       ; ; ; 4 (;)(;)"
						+ "\n"
						+ "\nIf You search for certain rates, You have to "
						+ "omitt '$'."
						+ "\nExample: search for all companies with rates "
						+ "of $ 90.00" 
						+ "\nInput:       ; ; ; ; 90 (;)"
						+ "\n"
						+ "\n");

				if(inputAll == null) {
					break;
				}
				final String[] tokens = inputAll.split(";");
				this.mainW.searchForRecords(tokens);
				
				break;
			case MENUBOOKING_RENT:
				// not implemented for a Seller client
				break;
			case MENUBOOKING_RELEASE:
				// not implemented for a Seller client
				break;
			case MENUEXTRA_READ:
				this.mainW.readRec();
				break;
			case MENUEXTRA_UPDATE:
				this.mainW.updateRec();
				break;
			case MENUEXTRA_DELETE:
				this.mainW.deleteRec();
				break;
			case MENUEXTRA_ADD:
				this.mainW.addRec();
				this.panUpDel.setPanelUDA_AddButtEnabl(false);
				break;
			case MENUFILTER_FOCUS:
				if (this.panTable.isFocused() == false) {
					this.panTable.setFocus(true);
					this.panTable.setRequest(true);
				} else {
					JOptionPane.showMessageDialog(null,
						    "Please use the tab key to "
						    + "move to the component "
						    + "You want to control.");
					this.panTable.setFocus(false);
					this.panTable.setRequest(false);
				}
				break;
			default:
				break;
		}
		this.log.exiting("SellerMenuItemActionListener", 
				"SellerMenuItemActionListener", e);
	}
}

