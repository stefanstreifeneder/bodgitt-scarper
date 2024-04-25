package suncertify.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import suncertify.db.LoggerControl;

/**
 * An instance of the class is a <code>javax.swing.JMenu</code>. 
 * <br> The menu possesses five
 * menu items:
 * <br> Refresh - reloads all valid Records in the table.
 * <br> Search Name - opens a dialog to investigate the database 
 * for Records beginning with the entered name.
 * <br> Search City - opens a dialog to investigate the database 
 * for Records beginning with the entered city.
 * <br> Search All - opens a dialog to investigate the database 
 * for Records matching entered properties.
 * <br> Table set Focus - sets focus to the table or withdraws it.
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuFilter extends JMenu {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 209L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.gui.MenuFile</code>
	 * .
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuFilter"), Level.ALL);
	
	/**
	 * The menu item, which refreshes the content of the database file.
	 */
	private JMenuItem refreshTableMenuItem;
	
	/**
	 * The menu item, which enables to search for names.
	 */
	private JMenuItem nameSearchMenuItem;
	
	/**
	 * The menu item, which enables to search for cities.
	 */
	private JMenuItem citySearchMenuItem;
	
	/**
	 * The menu item, which enables to search for all elements.
	 */
	private JMenuItem searchAllMenuItem;

	/**
	 * The check box, which settles the focus
	 * of the table.
	 */
	private JCheckBoxMenuItem mainTableSetFocusMenuItem;
	
	
	/**
	 * Constructs a menu and adds an action listener to the menu items.
	 * 
	 * @param aL Reacts, if a menu item is pressed.
	 */
	public MenuFilter(ActionListener aL) {
		this.log.entering("MenuFilter", "MenuFilter");
		this.setText("Filter");
		
		// item refresh
		this.refreshTableMenuItem = new JMenuItem("Refresh");
		this.refreshTableMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUFILTER_REFRESH.name());
		this.refreshTableMenuItem.addActionListener(aL);
		this.refreshTableMenuItem.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_S, ActionEvent.CTRL_MASK | 
						ActionEvent.ALT_MASK));
		this.add(this.refreshTableMenuItem);
				
		// item name
		this.nameSearchMenuItem = new JMenuItem("Search Name");
		this.nameSearchMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUFILTER_NAME.name());
		this.nameSearchMenuItem.addActionListener(aL);
		this.add(this.nameSearchMenuItem);
		
		// item city
		this.citySearchMenuItem = new JMenuItem("Search City");
		this.citySearchMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUFILTER_CITY.name());
		this.citySearchMenuItem.addActionListener(aL);
		this.add(this.citySearchMenuItem);
		
		// item all
		this.searchAllMenuItem = new JMenuItem("Search All");
		this.searchAllMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUFILTER_ALL.name());
		this.searchAllMenuItem.addActionListener(aL);
		this.add(this.searchAllMenuItem);
				
		this.addSeparator();

		// table focus
		this.mainTableSetFocusMenuItem = new JCheckBoxMenuItem("Table set Focus");
		this.mainTableSetFocusMenuItem.setSelected(true);
		this.mainTableSetFocusMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUFILTER_FOCUS.name());
		this.mainTableSetFocusMenuItem.addActionListener(aL);
		this.add(this.mainTableSetFocusMenuItem);
		this.log.exiting("MenuFilter", "MenuFilter");
	}
}
