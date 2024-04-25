package suncertify.gui.seller;

import java.awt.Cursor;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;

/**
 * This class proceeds the update operation for a Seller client. 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerUpdateOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.SellerUpdateOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.SellerUpdateOperation"),
			Level.ALL);

	/**
	 * A reference to the controller.
	 */
	private GuiControllerSeller controller;
	
	/**
	 * Stores the values of the Record in the moment,
	 * if the update window is opened.
	 */
	private String[] oldVals;
	
	/**
	 * Reference to the update window.
	 */
	private WindowUpdateSeller winUp;
	
	/**
	 * Creates an object of this class.
	 * 
	 * @param guiCtrl
	 *            A reference to the controller.
	 * @param recVals
	 *            The values of the Record in the moment, if the update window
	 *            is opened.
	 * @param upWin A reference to the update window.
	 */
	public SellerUpdateOperation(GuiControllerSeller guiCtrl, String[] recVals, 
			WindowUpdateSeller upWin) {
		this.log.entering("SellerUpdateOperation", "SellerUpdateOperation", 
				guiCtrl);
		this.controller = guiCtrl;
		this.oldVals = recVals;
		this.winUp = upWin;
		this.log.exiting("SellerUpdateOperation", "SellerUpdateOperation");

	}	

	/**
	 * Does the update operation. 
	 * <br> At first place the methods checks 
	 * whether the Record is locked or not. 
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 * 
	 * @param values
	 *            The values of the Record.
	 * @param recNo
	 *            The Record number.
	 * @param mainW
	 *            A reference to the main window.
	 */
	public void updateMeth(String[] values, long recNo, 
			MainWindowSeller mainW) {
		this.log.entering("SellerUpdateOperation", "updateMeth",
				new Object[]{values, Long.valueOf(recNo), mainW});
		this.winUp.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		mainW.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		boolean updateOk = false;		
		Record updateRecord = new Record(recNo
				, this.oldVals[0]
				, this.oldVals[1]
				, this.oldVals[2]
				, Integer.parseInt(this.oldVals[3])
				, this.oldVals[4]
				, this.oldVals[5]);
		
		try {
			for (Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					int n = JOptionPane.showConfirmDialog(null, mainW.MSG_ID 
							+ MainWindowSeller.getMsgRecNo(recNo) + 
							"This Record is locked, if you press 'yes', "
									+ "You have to wait until the Record will "
									+ "be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);	
					
					if (n == JOptionPane.NO_OPTION || 
												n == JOptionPane.CLOSED_OPTION) {
						mainW.removeOfUpList(recNo);
						mainW.setupTableDatabase();						
						this.winUp.dispose();
						return;
					}
				}
			}
		} catch (HeadlessException hexc) {
			mainW.removeOfUpList(recNo);		
			this.winUp.dispose();
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(hexc.getLocalizedMessage());
			return;
		} catch (GuiControllerException gce) {
			mainW.removeOfUpList(recNo);		
			this.winUp.dispose();
			mainW.setupTableDatabase();
			ExceptionDialog.handleException(gce.getLocalizedMessage());
			return;
		}
		
		try {
			boolean runWhile = true;
			while (runWhile) {
				mainW.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				//this.winUp.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				mainW.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				EnumChangeMode choice = this.controller.checkRecordHasChanged(
								updateRecord, recNo, 
								EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
								"SELLER - UPDATE - BEFORE LOCK");
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					mainW.removeOfUpList(recNo);
					mainW.setupTableDatabase();						
					this.winUp.dispose();
					return;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					mainW.setupTableDatabase();
					runWhile = false;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					mainW.setupTableDatabase();
					runWhile = true;
				}
			}
		} catch (GuiControllerException e) {
			mainW.removeOfUpList(recNo);		
			this.winUp.dispose();
			mainW.setupTableDatabase();						
			ExceptionDialog
					.handleException(mainW.MSG_ID 
							+ MainWindowSeller.getMsgRecNo(recNo) 
							+ e.getLocalizedMessage());
			return;
		}
		
		long cookie = -1;	
		try {
			cookie = this.controller.lockRecord(recNo);
			mainW.setupTableDatabase();
			// check values, if a waiting state has happened before
			try {				
				updateRecord = this.controller.getRecord(recNo);
				String[] data = this.oldVals;
				data[6] = Long.valueOf(recNo).toString();
				boolean notEqual = false;
				StringBuilder str = new StringBuilder(mainW.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) 
						+ "THIS RECORD HAS BEEN CHANGED!"
						+ "\nIf You want to update the Record, please press "
						+ "\n'Y(J)'" 
						+ "\nold entry / new entry");
				if (!data[0].equals(updateRecord.getName())) {
					notEqual = true;
					str.append("\nName: " + data[0] + "/" + updateRecord.getName());
				}
				if (!data[1].equals(updateRecord.getCity())) {
					notEqual = true;
					str.append("\nCity: " + data[1] + "/" + updateRecord.getCity());
				}
				if (!data[2].equals(updateRecord.getTypesOfWork())) {
					notEqual = true;
					str.append("\nTypes: " + data[2] + "/" 
							+ updateRecord.getTypesOfWork());
				}
				if (Integer.valueOf(data[3]).intValue() != updateRecord.getNumberOfStaff()) {
					notEqual = true;
					str.append("\nStaff: " + data[3] 
							+ "/" + updateRecord.getNumberOfStaff());
				}
				if (!data[4].equals(updateRecord.getHourlyChargeRate())) {
					notEqual = true;
					str.append("\nRate: " + data[4] + "/" 
							+ updateRecord.getHourlyChargeRate());
				}
				if (!data[5].equals(updateRecord.getOwner())) {
					notEqual = true;
					str.append("\nID Owner: " + data[5] 
							+ "/" + updateRecord.getOwner());
				}
				if (notEqual) {
					int n = JOptionPane.showConfirmDialog(null, new String(str), 
							"Update Operation - Changes",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.NO_OPTION || 
										n == JOptionPane.CLOSED_OPTION) {	
						mainW.removeOfUpList(recNo);					
						this.winUp.dispose();
						return;
					}
				}
			}catch (GuiControllerException e) {
				ExceptionDialog.handleException(mainW.MSG_ID 
						+ e.getLocalizedMessage());
				return;
			}
			
			// initialization new Record
			updateRecord.setName(values[0]);
			updateRecord.setCity(values[1]);
			updateRecord.setTypesOfWork(values[2]);
			try {
				updateRecord.setNumberOfStaff(Integer.parseInt(values[3]));
			} catch (NumberFormatException eh) {
				ExceptionDialog
						.handleException(mainW.MSG_ID 
								+ MainWindowSeller.getMsgRecNo(recNo) + "Update failed! " + "Your Input ('" + values[3] + "') in 'Staff' is wrong!");
				this.log.severe("SellerUpdateOperation, updateMeth, Exception: " + eh.getLocalizedMessage());
				return;
			}
			updateRecord.setHourlyChargeRate(values[4]);
			updateRecord.setOwner(values[5]);
			updateOk = this.controller.updateRecord(updateRecord, recNo, cookie);
			
			for(int i = 0; i < values.length; i++) {
				this.oldVals[i] = values[i];
			}			
		} catch (GuiControllerException ex) {
			ExceptionDialog.handleException(mainW.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) + "Update failed! " 
						+ ex.getLocalizedMessage());					
			return;
		}finally{			
			try {
				this.controller.unlockRecord(recNo, cookie);
			} catch (GuiControllerException e) {				
				mainW.setupTableDatabase();
				ExceptionDialog.handleException(mainW.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) + "Unlocking within an "
						+ "Update Operation failed, due to : " 
						+ e.getLocalizedMessage());
			}
			this.winUp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			mainW.setupTableDatabase();
		}
		this.winUp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		
		JOptionPane.showMessageDialog(null, mainW.MSG_ID 
				+ MainWindowSeller.getMsgRecNo(recNo) 
				+ "Update-Operation: " + updateOk);
		
		this.log.exiting("SellerUpdateOperation", "updateMeth");
	}
}
