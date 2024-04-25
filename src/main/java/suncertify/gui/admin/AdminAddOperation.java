package suncertify.gui.admin;

import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.DuplicateKeyException;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.WindowAddSeller;

/**
 * The class does the work of an add operation for an Admin client. 
 * It reads a new Record's data out of the graphical component and passes 
 * it to the GUI controller, which is of type 
 * <code>suncertify.gui.GuiControllerAdmin</code>.<br>
 * To display the data of a Record, which should be added to the database file
 * an Admin client uses a part of the graphical framework of a Seller client,
 * which resides in package <code>suncertify.gui.seller</code> and is represented 
 * by an object of type <code>suncertify.gui.seller.WindowAddSeller</code>,
 * which provides the graphical surface.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminAddOperation {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.AdminAddOperation</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.AdminAddOperation"),
			Level.ALL);

	/**
	 * A reference to the controller to have access to the database.
	 */
	private GuiControllerAdmin controller;

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * A reference to the add window. The component resides in the package
	 * <code>suncertify.gui.seller</code>.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to the panel to read, update, delete and add a Record
	 * of the main window. The component resides in the package
	 * <code>suncertify.gui.seller</code>.
	 */
	private PanelUpDelAddSeller panUDA;

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
	 *            A reference to the panel, which possesses four buttons to
	 *            read, update, delete or add a Record.
	 * 	
	 */
	public AdminAddOperation(MainWindowAdmin mw, WindowAddSeller wav, 
			GuiControllerAdmin guiCtrl, PanelUpDelAddSeller pan) {		
		this.log.entering("AdminAddOperation", "AdminAddOperation", 
				new Object[] { mw, wav, guiCtrl });
		this.mainW = mw;
		this.panUDA = pan;
		this.controller = guiCtrl;
		this.winAdd = wav;
		this.log.exiting("AdminAddOperation", "AdminAddOperation");
	}

	/**
	 * Adds a Record. The elements 'Staff' and 'ID Owner' will be transformed in
	 * to the type <code>int</code>, this can throw an
	 * <code>java.lang.NumberFormatException</code>, which is catched and an 
	 * exception message will be displayed in a dialog and the add 
	 * operation will be stopped.
	 * 
	 * 
	 * @param filedVals
	 *            Array with the values of a Record.
	 */
	public void addMeth(String[] filedVals) {
		int staff;		
		try {
			staff = Integer.parseInt(filedVals[3]);
			
		} catch (NumberFormatException e) {
			ExceptionDialog.handleException(this.mainW.getMSG_ID()
					+ "Your input ('" + filedVals[3] + "') in 'Staff' is " 
					+ "not a digit!");
			this.log.severe("AdminDeleteOperation, addMeth, Exception: " 
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
			this.log.severe("AdminAddOperation, addMeth, Exception: " 
							+ e.getLocalizedMessage());
			return;
		}
		
		Record addThisRecord = new Record(recNo, filedVals[0], 
				filedVals[1], filedVals[2], staff, filedVals[4],
				filedVals[5]);		
		long recNoNew = 0;
		try {
			recNoNew = this.controller.createRecord(addThisRecord);
			if(recNo != recNoNew){
				ExceptionDialog.handleException(this.mainW.getMSG_ID()
						+ "Your requested Record number("
						+ Long.parseLong(filedVals[6]) + ") "
						+ "has changed to: " + recNoNew);
			}			
		} catch (GuiControllerException ge) {
			ExceptionDialog.handleException(this.mainW.getMSG_ID()
					+ MainWindowAdmin.getMsgRecNo(Long.parseLong(filedVals[6])) 
					+ ge.getLocalizedMessage());
			this.log.severe("Problems to create Record: " + ge.getCause() 
				+ " - Record numer: " + recNoNew);
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
