package suncertify.gui.admin.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.admin.AdminAddOperation;
import suncertify.gui.admin.GuiControllerAdmin;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.WindowAddSeller;

/**
 * An object of this class is a <code>java.awt.event.KeyAdapter</code>. 
 * It belongs to the entry fields of the add window
 * <code>suncertify.gui.seller.WindowAddSeller</code> used by an 
 * Admin client.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminWinAddFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are 
	 * routed through this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminWinAddFieldsKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger("suncertify.gui.admin."
					+ "listeners.AdminWinAddFieldsKeyListener"), Level.ALL);

	/**
	 * A reference to the add window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * A reference to an object of the class, which does the add 
	 * operation.
	 */
	private AdminAddOperation addOpp;

	/**
	 * A reference to the controller of the graphical surface.
	 */
	private GuiControllerAdmin controller;

	/**
	 * A reference to the panel, which enables read, update, delete 
	 * and add functions.
	 */
	private PanelUpDelAddSeller panUpDel;

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
	 *            A reference to panel, which enables read by Record 
	 *            number,
	 *            update, delete and add a Record.
	 */
	public AdminWinAddFieldsKeyListener(
			MainWindowAdmin mW, 
			WindowAddSeller wa, 
			GuiControllerAdmin guiCtrl,
			PanelUpDelAddSeller panUDA) {
		this.log.entering("AdminWinAddFieldsKeyListener", 
				"AdminWinAddFieldsKeyListener",
				new Object[] { mW, wa, guiCtrl });
		this.mainW = mW;
		this.winAdd = wa;
		this.panUpDel = panUDA;
		this.controller = guiCtrl;
		this.addOpp = new AdminAddOperation(this.mainW, this.winAdd, 
				this.controller, this.panUpDel);
		this.log.exiting("AdminWinAddFieldsKeyListener", 
				"AdminWinAddFieldsKeyListener");
	}

	/**
	 * If the key 'enter' is pressed, the Record will be added.
	 * If the key 'Esc' is pressed the window closes.
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.addOpp.addMeth(this.winAdd.getFieldVals());
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.panUpDel.setPanelUDA_AddButtEnabl(true);
			this.winAdd.dispose();
			this.mainW.setupTableDatabase();
		}
	}
}
