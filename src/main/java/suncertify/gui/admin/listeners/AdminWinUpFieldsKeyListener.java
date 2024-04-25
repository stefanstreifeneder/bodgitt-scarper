package suncertify.gui.admin.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.admin.AdminUpdateOperation;
import suncertify.gui.admin.GuiControllerAdmin;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of this class is a <code>java.awt.event.KeyAdapter</code>. 
 * It is used by an Admin client. It belongs to the entry fields 
 * of the update window 
 * <code>suncertify.gui.seller.WindowUpdateSeller</code>.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminWinUpFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are 
	 * routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminWinUpFieldsKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger("suncertify.gui.admin."
					+ "listeners.AdminWinUpFieldsKeyListener"), Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

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
	 * A reference to an object of the class, which does the update 
	 * operation.
	 */
	private AdminUpdateOperation upOpp;

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
	public AdminWinUpFieldsKeyListener(MainWindowAdmin mw, long rNo, 
			long lCookie, WindowUpdateSeller muv,
			GuiControllerAdmin guiCtrl) {
		this.log.entering("AdminWinUpFieldsKeyListener", 
				"AdminWinUpFieldsKeyListener",
				new Object[] { mw, Long.valueOf(rNo), 
						Long.valueOf(lCookie), muv, guiCtrl });
		this.mainW = mw;
		this.recNo = rNo;
		this.lockCookie = lCookie;
		this.winUp = muv;
		this.controller = guiCtrl;
		this.upOpp = new AdminUpdateOperation(this.controller);
		this.log.exiting("AdminWinUpFieldsKeyListener", 
				"AdminWinUpFieldsKeyListener");
	}

	/**
	 * If the key 'enter' is pressed, the Record will be updated.
	 * If the key 'Esc' is pressed, the lock on the Record
	 * will be released and the update window will be closed.
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.upOpp.updateMeth(this.winUp.getFieldVals(), 
					this.recNo, this.lockCookie, this.mainW);
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			try {
				this.controller.unlockRecord(this.recNo, this.lockCookie);
			} catch (GuiControllerException gce) {
				ExceptionDialog.handleException(gce.toString());
			}
			this.mainW.setupTableDatabase();
			this.winUp.dispose();
		}
	}
}
