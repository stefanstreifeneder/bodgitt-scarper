package suncertify.gui.admin.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.admin.GuiControllerAdmin;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of this class is a <code>java.awt.event.WindowAdapter</code>. 
 * It is used by an Admin client. It closes the update window
 * <code>suncertify.gui.seller.WindowUpdateSeller</code> 
 * and it cares to release locks.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminWinUpWinWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed 
	 * through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminWinUpWinWindowListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger("suncertify.gui.admin."
					+ "listeners.AdminWinUpWinWindowListener"), Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * The Record number.
	 */
	private long recNo;

	/**
	 * The lock cookie.
	 */
	private long lockCookie;

	/**
	 * A reference to the update window.
	 */
	private WindowUpdateSeller winUp;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 * @param rNo
	 *            The Record number.
	 * @param lCookie
	 *            The lock cookie.
	 * @param muv
	 *            A reference to the update window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 */
	public AdminWinUpWinWindowListener(MainWindowAdmin mw, long rNo, 
			long lCookie, WindowUpdateSeller muv,
			GuiControllerAdmin guiCtrl) {
		this.log.entering("AdminWinUpWinWindowListener", 
				"AdminWinUpWinWindowListener",
				new Object[] { mw, Long.valueOf(rNo), 
						Long.valueOf(lCookie), muv, guiCtrl });
		this.mainW = mw;
		this.recNo = rNo;
		this.lockCookie = lCookie;
		this.winUp = muv;
		this.controller = guiCtrl;
		this.log.exiting("AdminWinUpWinWindowListener", 
				"AdminWinUpWinWindowListener");
	}

	/**
	 * Closes the update window and releases the lock
	 * on the Record.
	 * 
	 * @param e
	 *            A window event.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.log.entering("AdminWinUpWinWindowListener", 
				"windowClosing", e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				this.controller.unlockRecord(this.recNo, this.lockCookie);
				this.mainW.setupTableDatabase();
			} catch (GuiControllerException gec) {
				ExceptionDialog.handleException(gec.toString());
			}
			this.winUp.dispose();
		}
		this.log.exiting("AdminWinUpWinWindowListener", "windowClosing");
	}
}
