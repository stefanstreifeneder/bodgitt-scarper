package suncertify.gui.seller;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuBar;

import suncertify.db.LoggerControl;
import suncertify.gui.MenuConnection;
import suncertify.gui.MenuCounts;
import suncertify.gui.MenuCountsNos;
import suncertify.gui.MenuFile;
import suncertify.gui.MenuFilter;
import suncertify.gui.MenuHelp;
import suncertify.gui.PanelTable;
import suncertify.gui.seller.listeners.SellerMenuItemActionListener;

/**
 * An object of the class is a <code>javax.swing.JMenuBar</code>.
 * It is applied in the class 
 * <code>suncertify.gui.seller.MainWindowSeller</code>.
 * <br> It possesses six menus:<br>
 * - File ( <code>suncertify.gui.MenuFile</code> )<br>
 * - Connection ( <code>suncertify.gui.MenuConnection</code> )<br>
 * - Counts ( <code>suncertify.gui.MenuCounts</code> )<br>
 * - Extra ( <code>MenuExtraSeller</code> )<br>
 * - Help ( <code>suncertify.gui.MenuHelp</code> )<br>
 * - Filter ( <code>suncertify.gui.MenuFilter</code> )
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MainWindowMenuBarSeller extends JMenuBar {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 221L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.MainWindowMenuBarSeller</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.MainWindowMenuBarSeller"), 
			Level.ALL);

	/**
	 * A reference to the main window. 
	 */
	private final MainWindowSeller mainW;

	/**
	 * A reference to the table panel. 
	 */
	private final PanelTable panTable;

	/**
	 * A reference to the menu, which displays meaningful numbers. 
	 */
	private final MenuCounts menueCounts;
	
	/**
	 * A reference to the menu, which displays
	 * the connection data (ipaddress, port or path to the database).
	 */
	private final MenuConnection menuCon;
	
	/**
	 * A reference to the menu, which enables to read,
	 * update, delete and add a Record.
	 */
	private final MenuExtraSeller menuExtra;

	/**
	 * Stores the ipaddress or the path to the database.
	 * It will be displayed via menu 'MenuConnection'.
	 */
	private final String dbLocation;

	/**
	 * Stores the port number or just "alone"
	 * by local access. It will be displayed via menu 'MenuConnection'.
	 */
	private final String portOrLocal;

	/**
	 * Constructs a menu bar.
	 * 
	 * @param nos Stores statistic numbers.
	 * @param mw A reference to the main window.
	 * @param panT A reference to the table. 
	 * @param panUp A reference to the panel, 
	 * 		which contains four buttons to read, update,
	 * 		delete and add a Record. 
	 * @param ipAddress The ipaddress of the server.
	 * @param portNo The port number of the server.
	 * will be tracked or not.
	 * @param intervalTime The interval time in ms.
	 */
	public MainWindowMenuBarSeller(final MenuCountsNos nos, 
			final MainWindowSeller mw, 
			final PanelTable panT, 
			final PanelUpDelAddSeller panUp, 
			final String ipAddress, 
			final String portNo, 
			final long intervalTime) {	
		
		this.mainW = mw;
		this.panTable = panT;
		this.dbLocation = ipAddress;
		this.portOrLocal = portNo;	
		
		final SellerMenuItemActionListener menuItemtActLi = 
				new SellerMenuItemActionListener(this.mainW, this.panTable, 
						panUp);
		
		// menu connection
		this.menuCon = new MenuConnection(this.dbLocation, 
				this.portOrLocal, intervalTime);
		this.menuCon.setMnemonic(KeyEvent.VK_C);
		this.menuCon.setToolTipText("Open with 'Alt' + C");				
		
		// menu extra
		this.menuExtra = new MenuExtraSeller(menuItemtActLi);
		this.menuExtra.setMnemonic(KeyEvent.VK_X);
		this.menuExtra.setToolTipText("Open with 'Alt' + X");
		
		// menu file
		this.add(new MenuFile());	
		
		// menu connection
		this.add(this.menuCon);		
		
		// menu counts
		this.menueCounts = new MenuCounts(nos);
		this.add(this.menueCounts);
		this.add(this.menuExtra);
		
		// menu help
		this.add(new MenuHelp());
		
		// menu filter
		final MenuFilter menuSearch = new MenuFilter(menuItemtActLi);
		menuSearch.setMnemonic(KeyEvent.VK_T);
		menuSearch.setToolTipText("Open with 'Alt' + T");
		this.add(menuSearch);
		
		this.log.exiting("MainWindowMenuBarSeller", "MainWindowMenuBarSeller");
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
	
	/**
	 * Updates the statistic numbers.
	 * 
	 * @param nos Stores statistic numbers.
	 */
	public void updateCount(final MenuCountsNos nos){
		this.menueCounts.upateCounts(nos);
	}
}
