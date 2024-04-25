package suncertify.gui.seller.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.MainWindowSeller;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class handles all key strokes, which are done on the entry fields of 
 * the search panel <code>suncertify.gui.PanelSearch</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerPanSearchFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerPanSearchFieldsKeyListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerPanSearchFieldsKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 */
	public SellerPanSearchFieldsKeyListener(MainWindowSeller mw) {
		this.log.entering("SellerPanSearchFieldsKeyListener", 
				"SellerPanSearchFieldsKeyListener", mw );
		this.mainW = mw;
		this.log.exiting("SellerPanSearchFieldsKeyListener", 
				"SellerPanSearchFieldsKeyListener");
	}

	/**
	 * Handles key events.
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
