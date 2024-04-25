package suncertify.gui.seller.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.GuiControllerSeller;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.SellerUpdateOperation;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * It belongs to the entry fields of the update window
 * <code>suncertify.gui.seller.WindowUpdateSeller</code>.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerWinUpFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerWinUpFieldsKeyListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerWinUpFieldsKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * The Record number.
	 */
	private long recNo;

	/**
	 * A reference to the update window.
	 */
	private WindowUpdateSeller winUp;

	/**
	 * A reference to an object of the class, which does the update operation.
	 */
	private SellerUpdateOperation upOpp;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerSeller controller;

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
	 * @param data
	 *            The values of the Record, which should be updated.
	 */
	public SellerWinUpFieldsKeyListener(
			MainWindowSeller mw
			, long rNo
			, WindowUpdateSeller muv
			, GuiControllerSeller guiCtrl
			, String[] data
			) {
		this.mainW = mw;
		this.recNo = rNo;
		this.winUp = muv;
		this.controller = guiCtrl;
		this.upOpp = new SellerUpdateOperation(this.controller, data, this.winUp);
		this.log.exiting("SellerWinUpFieldsKeyListener", 
				"SellerWinUpFieldsKeyListener");
	}
	

	/**
	 * Handles key events.
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.upOpp.updateMeth(
					this.winUp.getFieldVals(), this.recNo, this.mainW);
			
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.mainW.removeOfUpList(this.recNo);			
			this.mainW.setupTableDatabase();
			this.winUp.dispose();
		}
	}
}
