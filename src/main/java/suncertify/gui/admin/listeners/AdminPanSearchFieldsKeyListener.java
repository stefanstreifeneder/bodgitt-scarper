package suncertify.gui.admin.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.admin.MainWindowAdmin;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class handles all key strokes, which were done 
 * on the entry fields of the search panel 
 * <code>suncertify.gui.PanelSearch</code> of an Admin client.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminPanSearchFieldsKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminPanSearchFieldsKeyListener</code>
	 * .
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.admin.listeners.AdminPanSearchFieldsKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 */
	public AdminPanSearchFieldsKeyListener(MainWindowAdmin mw) {
		this.log.entering("AdminPanSearchFieldsKeyListener", 
				"AdminPanSearchFieldsKeyListener",
				mw );
		this.mainW = mw;
		this.log.exiting("AdminPanSearchFieldsKeyListener", 
				"AdminPanSearchFieldsKeyListener");
	}

	/**
	 * If the key 'enter' is pressed, the method <code>findByFilter</code>
	 * of the interface <code>suncertify.db.InterfaceClient_ReadOnly</code>
	 * will be executed and the entry fields will be set blank.
	 * If the key 'Esc' is pressed, the application ends.
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.mainW.searchCriteriaSetPanelSearch();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.mainW.closeMainWindow();
		}
	}
}
