package suncertify.gui.seller.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * An object of the class is a <code>java.awt.event.WindowAdapter</code>. 
 * It cares that only one add window 
 * <code>suncertify.gui.seller.WindowAddSeller</code>
 * can be open at the same time by enabling or disabling the 
 * button to open an add window.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerWinAddWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerWinAddWindowListener</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerWinAddWindowListener"), 
			Level.ALL);

	/**
	 * A reference to the panel, which enables read, update, delete and add
	 * functions.
	 */
	private final PanelUpDelAddSeller pan;

	/**
	 * Creates an object of this class.
	 * 
	 * @param panUDA
	 *            A reference to the panel, which enables read, update, delete
	 *            and add functions.
	 */
	public SellerWinAddWindowListener(final PanelUpDelAddSeller panUDA) {
		this.log.entering("SellerWinUpWinWindowListener", 
				"SellerWinUpWinWindowListener", new Object[] { panUDA });
		this.pan = panUDA;
		this.log.exiting("SellerWinUpWinWindowListener", 
				"SellerWinUpWinWindowListener");
	}

	/**
	 * Overridden method to handle a window closing event.
	 * 
	 * @param e
	 *            A window event
	 */
	@Override
	public void windowClosing(final WindowEvent e) {
		this.log.entering("SellerWinUpWinWindowListener", "windowClosing", e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.pan.setPanelUDA_AddButtEnabl(true);
		}
		this.pan.setPanelUDA_AddButtEnabl(true);
		
		this.log.exiting("SellerWinUpWinWindowListener", "windowClosing");
	}
}
