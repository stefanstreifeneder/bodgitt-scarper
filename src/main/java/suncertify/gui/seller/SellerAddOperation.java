package suncertify.gui.seller;

import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.DuplicateKeyException;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;

/**
 * This class does the work of an add operation for a Seller client.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerAddOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.SellerAddOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.SellerAddOperation"),
			Level.ALL);

	/**
	 * A reference to the controller.
	 */
	private GuiControllerSeller controller;

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * A reference to the add window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * The panel to read, update, delete and add a Record.
	 */
	private PanelUpDelAddSeller panUDA = null;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 * @param wav
	 *            A reference to the add window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 * @param pan
	 *            A reference to the panel, which enables reading by Record
	 *            number, update, delete and add a Record.
	 */
	public SellerAddOperation(MainWindowSeller mw, 
			WindowAddSeller wav, 
			GuiControllerSeller guiCtrl,
			PanelUpDelAddSeller pan) {
		this.log.entering("SellerAddOperation", "SellerAddOperation", 
				new Object[] { mw, wav, guiCtrl });
		this.mainW = mw;
		this.winAdd = wav;
		this.panUDA = pan;
		this.controller = guiCtrl;
		this.log.exiting("SellerAddOperation", "SellerAddOperation");
	}

	/**
	 * Does the add operation.
	 * 
	 * @param filedVals
	 *            The values of the Record.
	 */
	public void addMeth(String[] filedVals) {
		int staff;		
		try {
			staff = Integer.parseInt(filedVals[3]);
			
		} catch (NumberFormatException e) {
			ExceptionDialog.handleException(this.mainW.getMSG_ID()
					+ "Your input ('" + filedVals[3] + "') in 'Staff' is " 
					+ "not a digit!");
			this.log.severe("SellerAddOperation, addMeth, Exception: " 
					+ e.getLocalizedMessage());
			return;
		}
		long recNo;
		try {
			recNo = Long.parseLong(filedVals[6]);
		} catch (NumberFormatException e) {
			ExceptionDialog.handleException(this.mainW.getMSG_ID()
					+ "Your input ('" + filedVals[6] 
							+ "') in 'Record Number' is " + "not a digit!");
			this.log.severe("SellerAddOperation, addMeth, Exception: " 
							+ e.getLocalizedMessage());
			return;
		}
		
		Record addThisRecord = new Record(recNo, filedVals[0], 
				filedVals[1], filedVals[2], staff, filedVals[4],
				filedVals[5]);	
		
		long recNoNew = 0;
		try {
			recNoNew = this.controller.createRecord(addThisRecord);
			if(Long.parseLong(filedVals[6]) != recNoNew){
				ExceptionDialog
				.handleException("Your requested Record number("
				+ Long.parseLong(filedVals[6]) + ") "
				+ "has changed to: " + recNoNew);
			}
		} catch (GuiControllerException ge) {
			this.log.severe("Record: " + recNoNew + " - " + ge.getLocalizedMessage());
			ExceptionDialog.handleException(this.mainW.getMSG_ID() +
					ge.getLocalizedMessage());
			if (ge.getCause() instanceof IllegalArgumentException) {
				return;
			} else if ((ge.getCause() instanceof DuplicateKeyException)) {
				return;
			}
		}
		this.panUDA.setPanelUDA_AddButtEnabl(true);
		this.winAdd.dispose();
		this.mainW.setupTableDatabase();
	}
}
