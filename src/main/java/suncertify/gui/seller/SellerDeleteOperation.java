package suncertify.gui.seller;

import java.awt.Cursor;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.db.RecordFormatter;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;

/**
 * The class proceeds a delete operation for a Seller client. 
 * <br> If the delete operation has accomplished, the space will be still available
 * and the elements set to 'DELETED'. Deleted Records are not displayed in the table
 * of the main window. They keep their Record number. It is not possible to
 * erase physical space. 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerDeleteOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.SellerDeleteOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.SellerDeleteOperation*"), 
			Level.ALL);	
	
	/**
	 * Stores the values of the Record in the moment,
	 * if the delete window is opened.
	 */
	private String[] oldVals;

	/**
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerSeller controller;
	
	/**
	 * Creates an object of this class. Assigns the values of the Record,
	 * which should be deleted by the constructor argument.
	 * 
	 * @param wdv
	 *            A reference to the delete window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 * @param oldValues
	 *            The values of the Record in the moment, if the delete window
	 *            is opened.
	 */
	public SellerDeleteOperation(WindowDeleteSeller wdv, 
			GuiControllerSeller guiCtrl,
			String[] oldValues) {
		this.log.entering("SellerDeleteOperation", "SellerDeleteOperation", 
				new Object[] { wdv, guiCtrl });
		this.winDel = wdv;
		this.controller = guiCtrl;
		this.oldVals = oldValues;
		this.log.exiting("SellerDeleteOperation", "SellerDeleteOperation");
	}

	/**
	 * Does the delete operation. 
	 * <br> At first place the methods checks 
	 * whether the Record is locked or not.  
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 * 
	 * @param recNo
	 *            The Record number.
	 * @param mainW
	 *            A reference to the main window.
	 */
	public void deleteMeth(long recNo, MainWindowSeller mainW) {
		this.winDel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		mainW.setCursor(new Cursor(Cursor.WAIT_CURSOR));		
		try {
			for (Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					int n = JOptionPane.showConfirmDialog(null, mainW.MSG_ID 
							+ MainWindowSeller.getMsgRecNo(recNo) + 
							"This Record is locked, if you press 'yes', "
									+ "You have to wait until the Record "
									+ "will be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);
					
					if (n == JOptionPane.NO_OPTION || 
										n == JOptionPane.CLOSED_OPTION) {
						mainW.removeOfDelList(recNo);
						mainW.setupTableDatabase();
						this.winDel.dispose();
						return;
					}
				}
			}
		} catch (HeadlessException hexc) {
			mainW.removeOfDelList(recNo);
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(hexc.getLocalizedMessage());
			return;
		} catch (GuiControllerException gce) {
			mainW.removeOfDelList(recNo);
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) 
					+ gce.getLocalizedMessage());
			return;
		}
		
		Record deleteRecord;
		try {
			deleteRecord = this.controller.getRecord(recNo);
		} catch (GuiControllerException ex) {
			mainW.removeOfDelList(recNo);
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) 
					+ "DELETE failed! " + ex.getMessage());
			return;
		}	
			
		String[] data = this.oldVals;
		data[6] = Long.valueOf(recNo).toString();
		String[] newRec = RecordFormatter.recToStringArr(deleteRecord);
		
		Record oldRecord = new Record(recNo, data[0], data[1], data[2], 
				Integer.parseInt(data[3]), data[4], data[5]);
		try {
			boolean runWhile = true;
			while (runWhile) {
				mainW.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				this.winDel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				mainW.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				EnumChangeMode choice = this.controller.checkRecordHasChanged(
								oldRecord, recNo, 
								EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
								"SELLER - DELETE - BEFORE LOCK");
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					mainW.removeOfDelList(recNo);
					mainW.setupTableDatabase();	
					this.winDel.dispose();
					return;
				} else if (choice == 
							EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					runWhile = false;
				} else if (choice == 
							EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					runWhile = true;
				}
			}
		} catch (GuiControllerException e) {
			mainW.removeOfDelList(recNo);
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.MSG_ID 
							+ MainWindowSeller.getMsgRecNo(recNo) 
							+ e.getLocalizedMessage());
			return;
		}
		
		for (int i = 0; i < data.length; i++) {
			data[i] = newRec[i];
		}				
		boolean deleteOk = false;
		long cookie = -1;		
		try {
			cookie = this.controller.lockRecord(recNo);
			mainW.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			this.winDel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			// check values, if a waiting state has happened before
			try {		
				deleteRecord = this.controller.getRecord(recNo);
				mainW.setupTableDatabase();
				boolean notEqual = false;
				StringBuilder str = new StringBuilder(mainW.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) + "THIS RECORD HAS BEEN CHANGED!"
								+ "\nIf You want to delete the Record, please press: "
								+ "\n'Y(J)'" 
						+ "\nold entry / new entry");
				if (!data[1].equals(deleteRecord.getName())) {
					notEqual = true;
					str.append("\nName: " + data[1] + "/" + deleteRecord.getName());
				}
				if (!data[2].equals(deleteRecord.getCity())) {
					notEqual = true;
					str.append("\nCity: " + data[2] + "/" + deleteRecord.getCity());
				}
				if (!data[3].equals(deleteRecord.getTypesOfWork())) {
					notEqual = true;
					str.append("\nTypes: " + data[3] + "/" + deleteRecord.getTypesOfWork());
				}
				if (Integer.valueOf(data[4]).intValue() != deleteRecord.getNumberOfStaff()) {
					notEqual = true;
					str.append("\nStaff: " + data[4] + "/" + deleteRecord.getNumberOfStaff());
				}
				if (!data[5].equals(deleteRecord.getHourlyChargeRate())) {
					notEqual = true;
					str.append("\nRate: " + data[5] + "/" + deleteRecord.getHourlyChargeRate());
				}
				if (!data[6].equals(deleteRecord.getOwner())) {
					notEqual = true;
					str.append("\nID Owner: " + data[6] + "/" + deleteRecord.getOwner());
				}
				if (notEqual) {
					int n = JOptionPane.showConfirmDialog(null, new String(str), "Delete Operation - Changes",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
						try {
							mainW.removeOfDelList(recNo);							
							mainW.setupTableDatabase();	
							this.winDel.dispose();
						} catch (SecurityException ex) {
							this.log.log(Level.SEVERE, ex.getMessage(), ex.getLocalizedMessage());
							throw new GuiControllerException(ex);
						}
						return;
					}
				}
			}catch (GuiControllerException e) {
				mainW.setupTableDatabase();				
				ExceptionDialog.handleException(mainW.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) + "Delete failed "
						+ "- values changed! " 
								+ e.getLocalizedMessage());
				return;				
			}		
			
			deleteOk = this.controller.deleteRecord(recNo, cookie);
		} catch (GuiControllerException ex) {
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(mainW.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) + 
					"Delete failed - locking/deleting/locking! " 
							+ ex.getLocalizedMessage());
			return;
		}finally{
			try {				
				this.controller.unlockRecord(recNo, cookie);				
			} catch (GuiControllerException e) {
				ExceptionDialog.handleException(mainW.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) + 
						"Delete failed - unlocking! " + e.getLocalizedMessage());
			}
			this.winDel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			mainW.setupTableDatabase();			
		}
		JOptionPane.showMessageDialog(null, mainW.MSG_ID 
				+ MainWindowSeller.getMsgRecNo(recNo) + "Deleting succeeded: " + deleteOk);
		this.winDel.dispose();
	}
}
