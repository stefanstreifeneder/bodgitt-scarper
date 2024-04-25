package suncertify.gui.admin;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuBar;

import suncertify.db.LoggerControl;
import suncertify.gui.ButtonReloadDB;
import suncertify.gui.MenuConnection;
import suncertify.gui.MenuFile;
import suncertify.gui.MenuFilter;
import suncertify.gui.MenuHelp;
import suncertify.gui.PanelTable;
import suncertify.gui.admin.listeners.AdminButtonActionListener;
import suncertify.gui.admin.listeners.AdminButtonKeyListener;
import suncertify.gui.admin.listeners.AdminMenuItemActionListener;
import suncertify.gui.buyer.MenuBookingBuyer;
import suncertify.gui.seller.MenuExtraSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * An object of the class is a <code>javax.swing.JMenuBar</code>. 
 * It is used of a Admin client exclusively. <br>
 * The class uses all menus of the package <code>suncertify.gui</code>,
 * except the menu 'MenuCounts'.
 * <br> Additional the class uses the menu
 * <code>suncertify.gui.seller.MenuExtraSeller</code> of a Seller 
 * client and the menu 
 * <code>suncertify.gui.buyer.MenuBookingBuyer</code> of a Buyer client.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MainWindowMenuBarAdmin extends JMenuBar {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 232L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.MainWindowMenuBarAdmin</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.MainWindowMenuBarAdmin"),
			Level.ALL);

	/**
	 * A reference to the main window. 
	 */
	private final MainWindowAdmin mainW;

	/**
	 * A reference to the table. 
	 */
	private final PanelTable panTable;

	/**
	 * Stores the ipaddress or the path to the database.
	 * It will be displayed via menu 'Connection'.
	 */
	private final String dbLocation;

	/**
	 * Stores the port number or just "alone"
	 * by local access. It will be displayed via menu 'Connection'.
	 */
	private final String portOrLocal;
	
	/**
	 * A reference to the menu, which displays
	 * the connection data (ip or path to the database and port 
	 * or "alone").
	 */
	private final MenuConnection menuCon;
	
	/**
	 * A reference to the menu, which supports the functionalities
	 * to rent or to release a Record.
	 */
	private final MenuBookingBuyer menuBook;
	
	/**
	 * A reference to the menu, which enables to read,
	 * update, delete and add a Record.
	 */
	private final MenuExtraSeller menuExtra;
	
	
	/**
	 * Constructs a menu bar.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 * @param panT A reference to the panel, which contains the table.
	 * @param panUp A reference to the panel, which possesses four
	 * buttons to read, update, delete and add a Record.
	 * @param locationDB The ipaddress of the server.
	 * @param localOrPort The port number of the server.
	 * @param intervalTime The interval time in ms.
	 */
	public MainWindowMenuBarAdmin(final MainWindowAdmin mw
			, final PanelTable panT
			, final PanelUpDelAddSeller panUp
			, final String locationDB
			, final String localOrPort
			, final long intervalTime) {	
		
		this.log.entering("MainWindowMenuBarAdmin", "MainWindowMenuBarAdmin",
				new Object[] { mw, panT, locationDB, localOrPort });
		this.mainW = mw;
		this.panTable = panT;
		this.dbLocation = locationDB;
		this.portOrLocal = localOrPort;
		
		final AdminMenuItemActionListener menuItemtActLi = 
				new AdminMenuItemActionListener(this.mainW, this.panTable,
						panUp);
		
		// menu connection
		this.menuCon = new MenuConnection(this.dbLocation, 
				this.portOrLocal, intervalTime);
		this.menuCon.setMnemonic(KeyEvent.VK_C);
		this.menuCon.setToolTipText("Open with 'Alt' + C");		
		
		// menu booking	
		this.menuBook = new MenuBookingBuyer(menuItemtActLi);
		this.menuBook.setMnemonic(KeyEvent.VK_E);
		this.menuBook.setToolTipText("Open with 'Alt' + E");
				
		// menu extra
		this.menuExtra = new MenuExtraSeller(menuItemtActLi);
		this.menuExtra.setMnemonic(KeyEvent.VK_X);
		this.menuExtra.setToolTipText("Open with 'Alt' + X");
		
		// menu file
		this.add(new MenuFile());
		
		// menu connection
		this.add(this.menuCon);			
		
		// menu booking	
		this.add(this.menuBook);
		
		// menu extra
		this.add(this.menuExtra);
		
		//menu help
		this.add(new MenuHelp());
		
		// menu filter
		final MenuFilter menuSearch = new MenuFilter(menuItemtActLi);
		menuSearch.setMnemonic(KeyEvent.VK_T);
		menuSearch.setToolTipText("Open with 'Alt' + T");
		this.add(menuSearch);	
		
		// reload button
		final ButtonReloadDB buttReload = 
				new ButtonReloadDB(new AdminButtonActionListener(this.mainW, 
						this.dbLocation, this.portOrLocal, this.menuCon), 
						new AdminButtonKeyListener(this.mainW,
								this.dbLocation, this.portOrLocal, 
								this.menuCon),
						"RELOAD_DB");
		this.add(buttReload);		
		this.log.exiting("MainWindowMenuBarAdmin", "MainWindowMenuBarAdmin");
	}
	
	/**
	 * Sets the value of the menu item 'Update time Interval',
	 * which displays the time interval.
	 * 
	 * @param tI A <code>java.lang.String</code> representation
	 * of number of milliseconds.
	 */
	public void setTimeInterval(final String tI){
		this.menuCon.setTimeInterval(tI);
	}
}
