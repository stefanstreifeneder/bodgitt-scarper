package suncertify.gui.buyer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandMenuItems;
import suncertify.gui.PanelTable;
import suncertify.gui.buyer.MainWindowBuyer;

/**
 * The class implements the interface <code>java.awt.event.ActionListener</code>.
 * It handles all action events associated with the menu bar
 * of a Buyer Client
 * <code>suncertify.gui.buyer.MainWindowMenuBarBuyer</code>.
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
public class BuyerMenuItemActionListener implements ActionListener{

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.BuyerMenuItemActionListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.buyer.listeners.BuyerMenuItemActionListener"), 
					Level.ALL);

	/**
	 * A reference to the search panel.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the main window.
	 */
	private MainWindowBuyer mainW;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw Reference to the main window.
	 * @param tablePan Reference to the table of the main window.
	 */
	public BuyerMenuItemActionListener(MainWindowBuyer mw,
			PanelTable tablePan) {
		this.mainW = mw;
		this.panTable = tablePan;
	}	

	/**
	 * Overridden method, which cares for <code>java.awt.event.ActionEvent</code>
	 * evoked by menu items of a Buyer client's menu bar.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String butt = e.getActionCommand();
		switch (ActionCommandMenuItems.valueOf(butt)) {
			case MENUFILTER_REFRESH:
				if (!this.panTable.tableHasFocus()) {
					this.mainW.searchForRecords(new String[] {""});
				}
				break;
			case MENUFILTER_NAME:
				String inputName = JOptionPane.showInputDialog(
						null, "Enter character to search in 'Name':");
				if(inputName == null) {
					break;
				}
				this.mainW.searchForRecords(new String[] { inputName });
				break;
			case MENUFILTER_CITY:
				String inputCity = JOptionPane.showInputDialog(
						null, "Enter character to search in 'City':");
				if(inputCity == null) {
					break;
				}
				this.mainW.searchForRecords(new String[] { "", inputCity });
				break;
			case MENUFILTER_ALL:
				String inputAll = JOptionPane.showInputDialog(null,
						"Search for any field of a Record!"
						+ "\nSplit by using ';'." 
						+ "\n"
						+ "\nExample: search for all companies with 4 staff "
						+ "members" 
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
				String[] tokens = inputAll.split(";");
				this.mainW.searchForRecords(tokens);				
				break;
			case MENUBOOKING_RENT:
				this.mainW.rentRecord();
				break;
			case MENUBOOKING_RELEASE:
				this.mainW.releaseRecord();
				break;
			case MENUEXTRA_READ:
				// not for Buyer clients implemented
				break;
			case MENUEXTRA_UPDATE:
				// not for Buyer clients implemented
				break;
			case MENUEXTRA_DELETE:
				// not for Buyer clients implemented
				break;
			case MENUEXTRA_ADD:
				// not for Buyer clients implemented
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
		this.log.exiting("BuyerButtonActionListener", 
				"BuyerButtonActionListener", e);		
	}
}

