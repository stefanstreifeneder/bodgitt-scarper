package suncertify.gui.buyer.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.buyer.MainWindowBuyer;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class is used by an Buyer client. The class handles all
 * key strokes, which were done on the entry fields of the search panel
 * <code>suncertify.gui.PanelSearch</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class BuyerPanSearchFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.BuyerPanSearchFieldsKeyListener</code>
	 * .
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.buyer.listeners."
					+ "BuyerPanSearchFieldsKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowBuyer mainW;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 */
	public BuyerPanSearchFieldsKeyListener(MainWindowBuyer mw) {
		this.log.entering("BuyerPanSearchFieldsKeyListener", 
				"BuyerPanSearchFieldsKeyListener",
				mw );
		this.mainW = mw;
		this.log.exiting("BuyerPanSearchFieldsKeyListener", 
				"BuyerPanSearchFieldsKeyListener");
	}

	/**
	 * If the key 'enter' is pressed, the application
	 * searches for matching Records and set afterwards
	 * the entry fields blank.<br>
	 * If the key 'Esc' is pressed, the application stops.
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.mainW.searchCriteriaSetPanelSearch();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.mainW.closeMainWindow();
		}
	}
}
