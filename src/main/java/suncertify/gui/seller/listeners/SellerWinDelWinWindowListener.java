package suncertify.gui.seller.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.WindowDeleteSeller;

/**
 * An object of the class is a <code>java.awt.event.WindowAdapter</code>. 
 * It cares to close the delete window
 * <code>suncertify.gui.seller.WindowDeleteSeller</code>
 * and removes the Record number of the list, which cares for the
 * coloring of a main window's table.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerWinDelWinWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerWinDelWinWindowListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerWinDelWinWindowListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel;
	
	/**
	 * The Record number.
	 */
	private long recNo = 0;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param mw A reference to the main window of a Seller client.
	 * @param mdv A reference to the delete window.
	 * @param rN The Record number
	 */
	public SellerWinDelWinWindowListener(MainWindowSeller mw,
			WindowDeleteSeller mdv,
			long rN) {		
		this.mainW = mw;
		this.winDel = mdv;
		this.recNo = rN;
		this.log.exiting("SellerWinDelWinWindowListener", 
				"SellerWinDelWinWindowListener");
	}

	/**
	 * Closes the delete windowand removes the Record number of the list, 
	 * which cares for the coloring of a main window's table.
	 * 
	 * @param e
	 *            A window event.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.log.entering("SellerWinDelWinWindowListener", "windowClosing", e);		
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.mainW.removeOfDelList(this.recNo);		
			this.mainW.setupTableDatabase();
			this.winDel.dispose();
		}
		this.log.exiting("SellerWinDelWinWindowListener", "windowClosing");
	}
}
