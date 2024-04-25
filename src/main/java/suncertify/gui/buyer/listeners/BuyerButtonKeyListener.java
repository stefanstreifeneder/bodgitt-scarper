package suncertify.gui.buyer.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.buyer.MainWindowBuyer;

/**
 * An object of this class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class handles all key strokes on buttons, which a Buyer Client
 * can do via his graphical user interface.
 * <br>
 * The action commands are defined by the enum 
 * <code>suncertify.gui.ActionCommandButtons</code>.
 * 
 * 
 * @see suncertify.gui.ActionCommandButtons  
 * @author stefan.streifeneder@gmx.de
 *
 */
public class BuyerButtonKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.BuyerButtonKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.buyer.listeners.BuyerButtonKeyListener"), 
					Level.ALL);
	
	/**
	 * A reference to the main window.
	 */
	private MainWindowBuyer mainW;

	/**
	 * Constructor to create an object of this class. 
	 * 
	 * @param mw Reference to the main window.
	 */
	public BuyerButtonKeyListener(MainWindowBuyer mw) {
		this.mainW = mw;
	}

	/**
	 * Handles key events.
	 * 
	 * @param e
	 *            A key event.
	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
		this.log.entering("BuyerButtonKeyListener", "BuyerButtonKeyListener", e);
		JButton butt = (JButton) e.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {
		case PANRR_RENT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.rentRecord();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANRR_RELEASE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.releaseRecord();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANSEARCH:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.searchCriteriaSetPanelSearch();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
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
		this.log.exiting("BuyerButtonKeyListener", "BuyerButtonKeyListener");
	}
}
