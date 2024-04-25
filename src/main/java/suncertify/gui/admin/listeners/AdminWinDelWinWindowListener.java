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
import suncertify.gui.seller.WindowDeleteSeller;

/**
 * An object of this class is a <code>java.awt.event.WindowAdapter</code>. 
 * It is used by an admin client. It cares to dispose the delete window 
 * <code>suncertify.gui.seller.WindowDeleteSeller</code> and to release the
 * lock.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminWinDelWinWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed 
	 * through this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminWinDelWinWindowListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.admin.listeners."
					+ "AdminWinDelWinWindowListener"), 
						Level.ALL);

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
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            - A reference to the main window.
	 * @param rNo
	 *            - The Record number.
	 * @param lCookie
	 *            - The lock cookie.
	 * @param mdv
	 *            - A reference to the delete window.
	 * @param guiCtrl
	 *            - A reference to the controller.
	 */
	public AdminWinDelWinWindowListener(MainWindowAdmin mw, long rNo, 
			long lCookie, WindowDeleteSeller mdv,
			GuiControllerAdmin guiCtrl) {
		this.log.entering("AdminWinDelWinWindowListener", 
				"AdminWinDelWinWindowListener",
				new Object[] { mw, Long.valueOf(rNo), 
						Long.valueOf(lCookie), mdv, guiCtrl });
		this.mainW = mw;
		this.recNo = rNo;
		this.lockCookie = lCookie;
		this.winDel = mdv;
		this.controller = guiCtrl;
		this.log.entering("AdminWinDelWinWindowListener", 
				"AdminWinDelWinWindowListener");
	}

	/**
	 * Handles, if the delete window will be closed
	 * and releases the lock on the Record.
	 * 
	 * @param e
	 *            A window event.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.log.entering("AdminWinDelWinWindowlistener", 
				"windowClosing", e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				this.controller.unlockRecord(this.recNo, 
						this.lockCookie);				
			} catch (GuiControllerException e1) {
				ExceptionDialog.handleException(e1.toString());
			}
			this.mainW.setupTableDatabase();
			this.winDel.dispose();
		}
		this.log.exiting("AdminWinDelWinWindowlistener", 
				"windowClosing");
	}
}
