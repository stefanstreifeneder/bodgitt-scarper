package suncertify.gui.admin.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * An object of this class is a <code>java.awt.event.WindowAdapter</code>. 
 * It will be registered to the add window
 * <code>suncertify.gui.seller.WindowAddSeller</code>. 
 * It cares that only one add window can be opened.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminWinAddWindowListener extends WindowAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed 
	 * through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.AdminWinAddWindowListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.seller.listeners.AdminWinAddWindowListener"), 
			Level.ALL);

	/**
	 * A reference to the panel, which enables read, update, delete and 
	 * add operations.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * Creates an object of this class.
	 * 
	 * @param panUDA
	 *            A reference to the panel, which enables to read, delete,
	 *            update and add a Record.
	 */
	public AdminWinAddWindowListener(PanelUpDelAddSeller panUDA) {
		this.log.entering("AdminWinAddWindowListener", 
				"AdminWinAddWindowListener", new Object[] { panUDA });
		this.panUpDel = panUDA;
		this.log.exiting("AdminWinAddWindowListener", 
				"AdminWinAddWindowListener");
	}

	/**
	 * Overridden method to handle window closing event. It enables the
	 * add button of the panel, which provides four buttons to read
	 * a Record by Record number, to update a Record, to delete a Record
	 * and to add a Record.
	 * 
	 * @param e
	 *            A window event
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.log.entering("AdminWinAddWindowListener", "windowClosing", e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.panUpDel.setPanelUDA_AddButtEnabl(true);
		}
		this.log.exiting("AdminWinAddWindowListener", "windowClosing");
	}
}
