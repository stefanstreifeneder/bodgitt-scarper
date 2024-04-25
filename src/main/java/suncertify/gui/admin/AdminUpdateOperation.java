package suncertify.gui.admin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;

/**
 * This class updates a Record for an Admin client.
 * The lock of a Record by a call to
 * <code>setRecordLocked</code> of the public interface
 * <code>suncertify.db.InterfaceClient_LockPermission</code> has to be done already. The
 * method <code>updateMeth</code> of this class needs the created lock cookie as
 * a method argument.<br>
 * The lock of the Record must be released manually, because this class does not
 * care!<br>
 * The Admin client uses the class 
 * <code>suncertify.gui.seller.WindowUpdateSeller</code> of the
 * package <code>suncertify.gui.seller</code> to display the values of the
 * Record, which should be modified.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminUpdateOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.AdminUpdateOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.AdminUpdateOperation"),
			Level.ALL);

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

	/**
	 * Creates an object of this class.
	 * 
	 * @param guiCtrl
	 *            A reference to the controller.
	 */
	public AdminUpdateOperation(GuiControllerAdmin guiCtrl) {
		this.log.entering("AdminUpdateOperation", "AdminUpdateOperation", guiCtrl);
		this.controller = guiCtrl;
		this.log.exiting("AdminUpdateOperation", "AdminUpdateOperation");
	}

	/**
	 * Updates a Record.
	 * 
	 * @param values
	 *            The values of the Record.
	 * @param recNo
	 *            The Record number.
	 * @param lockCookie
	 *            The lock cookie.
	 * @param mainW
	 *            A reference to the main window.
	 */
	public void updateMeth(String[] values, long recNo, long lockCookie, MainWindowAdmin mainW) {
		Record updateRecord = null;
		try {
			updateRecord = this.controller.getRecord(recNo);
		} catch (GuiControllerException ex) {
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.getMSG_ID() +
					MainWindowAdmin.getMsgRecNo(recNo) + "Unlock Record failed! " + ex.getMessage());
			return;
		}
		updateRecord.setName(values[0]);
		updateRecord.setCity(values[1]);
		updateRecord.setTypesOfWork(values[2]);
		try {
			updateRecord.setNumberOfStaff(Integer.parseInt(values[3]));
		} catch (NumberFormatException eh) {
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.getMSG_ID() +
					MainWindowAdmin.getMsgRecNo(recNo) + "Update failed! " + "Your Input ('" + values[3] + "') " + "in 'Staff' is wrong!");
			this.log.severe("AdminUpdateOperation, updateMeth, " + eh.getLocalizedMessage());
			return;
		}
		updateRecord.setHourlyChargeRate(values[4]);
		updateRecord.setOwner(values[5]);
		boolean updateOk = false;
		try {
			updateOk = this.controller.updateRecord(updateRecord, recNo, lockCookie);
		} catch (GuiControllerException ex) {
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.getMSG_ID() +
					MainWindowAdmin.getMsgRecNo(recNo) + "Update failed! " + ex.getLocalizedMessage());
			return;
		}
		mainW.setupTableDatabase();
		JOptionPane.showMessageDialog(null, mainW.getMSG_ID() +
				MainWindowAdmin.getMsgRecNo(recNo) + "Update-Operation: " + updateOk);
	}
}
