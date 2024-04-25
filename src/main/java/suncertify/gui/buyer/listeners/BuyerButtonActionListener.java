package suncertify.gui.buyer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.buyer.MainWindowBuyer;

/**
 * The class implements the interface <code>java.awt.event.ActionListener</code>.
 * It handles all action events associated with buttons
 * an Buyer Client can control via his graphical surface.
 * <br>
 * The action commands are defined by the enum 
 * <code>suncertify.gui.ActionCommandButtons</code>.
 * 
 * 
 * @see suncertify.gui.ActionCommandButtons 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class BuyerButtonActionListener implements ActionListener{

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.BuyerButtonActionListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.buyer.listeners.BuyerButtonMouseListener"), 
					Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowBuyer mainW;

	/**
	 * Creates an object of this class.
	 * @param mw Reference to the main window.
	 */
	public BuyerButtonActionListener(MainWindowBuyer mw) {
		this.mainW = mw;	
	}

	/**
	 * Overridden method, which cares for <code>java.awt.event.ActionEvents</code>
	 * evoked by buttons of a Buyer client.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.log.entering("BuyerButtonActionListener", "actionaPerformed", arg0);
		JButton butt = (JButton) arg0.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {		
		case PANRR_RENT:
			this.mainW.rentRecord();
			break;
		case PANRR_RELEASE:
			this.mainW.releaseRecord();
			break;
		case PANSEARCH:
			this.mainW.searchCriteriaSetPanelSearch();
			break;			
		case PANUDRA_ADD:
			//not available for a Buyer client
		case PANUDRA_DELETE:
			//not available for a Buyer client
		case PANUDRA_READ:
			//not available for a Buyer client
		case PANUDRA_UPDATE:
			//not available for a Buyer client
		case WINADD_ADD:
			//not available for a Buyer client
		case WINADD_EXIT:
			//not available for a Buyer client
		case WINDEL_DELETE:
			//not available for a Buyer client
		case WINDEL_EXIT:
			//not available for a Buyer client
		case WINUP_EXIT:
			//not available for a Buyer client
		case WINUP_UPDATE:
			//not available for a Buyer client
		case RELOAD_DB:		
			//not available for a Buyer client
		default:
			break;
		}
		this.log.exiting("BuyerButtonActionListener", 
				"BuyerButtonActionListener", arg0);
		
	}
}
