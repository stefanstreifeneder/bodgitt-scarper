package suncertify.gui.seller.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.GuiControllerSeller;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of the class is a <code>java.awt.event.WindowAdapter</code>.  
 * It cares to close the update window
 * <code>suncertify.gui.seller.WindowUpdateSeller</code> 
 * and removes the Record number of the main window's list, which cares 
 * for the coloring of a main window's table.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerWinUpWinWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerWinUpWinWindowListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerWinUpWinWindowListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * A reference to the update window.
	 */
	private WindowUpdateSeller winUp;
	
	/**
	 * The Record number.
	 */
	private long recNo = 0;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 * @param rNo
	 *            The Record number.
	 * @param muv
	 *            A reference to the update window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 */
	public SellerWinUpWinWindowListener(MainWindowSeller mw
			, long rNo
			, WindowUpdateSeller muv
			, GuiControllerSeller guiCtrl
			) {
		this.log.entering("SellerWinUpWinWindowListener", 
				"SellerWinUpWinWindowListener",
				new Object[] { mw, Long.valueOf(rNo), guiCtrl });
		this.mainW = mw;
		this.winUp = muv;	
		this.recNo = rNo;
		this.log.exiting("SellerWinUpWinWindowListener", 
				"SellerWinUpWinWindowListener");
	}

	/**
	 * Overridden method to handle a window closing event 
	 * and removes the Record number of the main window's list, 
	 * which cares for the coloring of a main window's table.
	 * 
	 * @param e
	 *            A window event.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.log.entering("SellerWinUpWinWindowListener", "windowClosing", e);		
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.mainW.removeOfUpList(this.recNo);	
			this.mainW.setupTableDatabase();
			this.winUp.dispose();
		}
		this.log.exiting("SellerWinUpWinWindowListener", "windowClosing");
	}
}
