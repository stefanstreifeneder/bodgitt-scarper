package suncertify.gui.seller.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.seller.GuiControllerSeller;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.SellerAddOperation;
import suncertify.gui.seller.WindowAddSeller;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * It belongs to the entry fields of the add window
 * <code>suncertify.gui.seller.WindowAddSeller</code>.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerWinAddFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerWinAddFieldsKeyListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerWinAddFieldsKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the panel, which enables read, update, delete and add
	 * functions.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * A reference to the add window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

	/**
	 * A reference to an object of the class, which does the add operation.
	 */
	private SellerAddOperation addOpp;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerSeller controller;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mW
	 *            A reference to the main window.
	 * @param wa
	 *            A reference to the add window.
	 * @param guiCtrl
	 *            A reference to the controller.
	 * @param panUDA
	 *            A reference to the panel, which enables read, update, delete
	 *            and add a Record.
	 */
	public SellerWinAddFieldsKeyListener(MainWindowSeller mW, 
			WindowAddSeller wa, 
			GuiControllerSeller guiCtrl,
			PanelUpDelAddSeller panUDA) {
		
		this.log.entering("SellerWinAddFieldsKeyListener", 
				"SellerWinAddFieldsKeyListener", new Object[] { mW, wa, guiCtrl });
		this.mainW = mW;
		this.winAdd = wa;
		this.panUpDel = panUDA;
		this.controller = guiCtrl;
		this.addOpp = new SellerAddOperation(
				this.mainW, this.winAdd, 
				this.controller, this.panUpDel);
		this.log.exiting("SellerWinAddFieldsKeyListener", 
				"SellerWinAddFieldsKeyListener");
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
			this.addOpp.addMeth(this.winAdd.getFieldVals());
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			try {
				this.panUpDel.setPanelUDA_AddButtEnabl(true);
				this.winAdd.dispose();
			} catch (Exception ex) {
				ExceptionDialog.handleException("Exit caused a failure: " 
						+ ex.getLocalizedMessage());
				return;
			} finally {
				this.mainW.setupTableDatabase();
			}
		}
	}
}
