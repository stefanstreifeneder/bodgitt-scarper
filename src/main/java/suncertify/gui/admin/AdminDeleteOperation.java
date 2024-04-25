package suncertify.gui.admin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.InterfaceClient_LockPermission;
import suncertify.db.LoggerControl;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.seller.WindowDeleteSeller;

/**
 * The class deletes a Record for an Admin client. 
 * It reads the Record's data out of the graphical component and passes 
 * them to the GUI controller, which is of type 
 * <code>suncertify.gui.GuiControllerAdmin</code>.<br>
 * <br>
 * To display the data of a Record, which should be deleted an Admin client 
 * uses a part of the graphical framework of a Seller client,
 * which resides in package <code>suncertify.gui.seller</code> and is represented 
 * by an object of type <code>suncertify.gui.seller.WindowDeleteSeller</code>.<br>
 * <br>
 * The lock of a Record by a call to the method <code>setRecordLocked</code> of
 * the public interface <code>suncertify.db.InterfaceClient_LockPermission</code> 
 * has to be done already. The method <code>deleteMeth</code> of the class 
 * needs the created lock cookie as a method argument.<br>
 * The lock of the Record is released automatically, done within the method
 * <code>deleteMeth</code>. 
 * 
 * @see MainWindowAdmin
 * @see GuiControllerAdmin
 * @see InterfaceClient_LockPermission
 * @see WindowDeleteSeller
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminDeleteOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.AdminDeleteOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.AdminDeleteOperation"),
			Level.ALL);

	/**
	 * A reference to the delete window. The component resides in the package
	 * <code>suncertify.gui.seller</code>.
	 */
	private WindowDeleteSeller winDel;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

	/**
	 * Creates an object of this class.
	 * 
	 * @param wdv
	 *            A reference to the delete window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 */
	public AdminDeleteOperation(WindowDeleteSeller wdv, GuiControllerAdmin guiCtrl) {
		this.log.entering("AdminDeleteOperation", "AdminDeleteOperation", 
				new Object[] { wdv, guiCtrl });
		this.winDel = wdv;
		this.controller = guiCtrl;
		this.log.exiting("AdminDeleteOperation", "AdminDeleteOperation");
	}

	/**
	 * Does the delete work and unlocks the Record.
	 * 
	 * @param recNo
	 *            The Record number of the Record.
	 * @param lockCookie
	 *            The lock cookie.
	 * @param mainW
	 *            A reference to the main window.
	 */
	public void deleteMeth(long recNo, long lockCookie, MainWindowAdmin mainW) {
		boolean deleteOk = false;
		try {
			deleteOk = this.controller.deleteRecord(recNo, lockCookie);
		} catch (GuiControllerException ge) {
			mainW.setupTableDatabase();				
			ExceptionDialog.handleException(mainW.getMSG_ID() +
					MainWindowAdmin.getMsgRecNo(recNo) + 
					ge.getCause().getLocalizedMessage());
			return;
		}finally{
			try {
				this.controller.unlockRecord(recNo, lockCookie);
			} catch (GuiControllerException gce) {
				mainW.setupTableDatabase();
				ExceptionDialog.handleException(mainW.getMSG_ID() +
						MainWindowAdmin.getMsgRecNo(recNo) 
							+ gce.getLocalizedMessage());
			}
		}
		mainW.setupTableDatabase();
		this.winDel.setDelButtEnabled(false);
		JOptionPane.showMessageDialog(null,  mainW.getMSG_ID() 
				+ MainWindowAdmin.getMsgRecNo(recNo) 
				+ "\nDeleting succeeded: " + deleteOk);
		this.winDel.dispose();
	}
}
