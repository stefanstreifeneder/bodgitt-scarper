package suncertify.gui.buyer;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuBar;
import suncertify.db.LoggerControl;
import suncertify.gui.MenuConnection;
import suncertify.gui.MenuCounts;
import suncertify.gui.MenuCountsNos;
import suncertify.gui.MenuFile;
import suncertify.gui.MenuHelp;
import suncertify.gui.MenuFilter;
import suncertify.gui.PanelTable;
import suncertify.gui.buyer.listeners.BuyerMenuItemActionListener;

/**
 * An instance of the class is a customized <code>javax.swing.JMenuBar</code>.
 * <br>
 * <br>
 * It possesses six menus:<br>
 * - File ( <code>suncertify.gui.MenuFile</code> )<br>
 * - Connection ( <code>suncertify.gui.MenuConnection</code> )<br>
 * - Edit ( <code>suncertify.gui.buyer.MenuBookingBuyer</code> )<br>
 * - Counts ( <code>suncertify.gui.MenuCounts</code> )<br>
 * - Help ( <code>suncertify.gui.MenuHelp</code> )<br>
 * - Filter ( <code>suncertify.gui.MenuFilter</code> )
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MainWindowMenuBarBuyer extends JMenuBar {
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 212L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.MainWindowMenuBarBuyer</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.buyer.MainWindowMenuBarBuyer"),
			Level.ALL);

	/**
	 * A reference to the main window. 
	 */
	private MainWindowBuyer mainW;

	/**
	 * A reference to the table panel. 
	 */
	private PanelTable panTable;

	/**
	 * A reference to the menu, which displays meaningful numbers (deleted,
	 * rented, all records).
	 */
	private MenuCounts mCounts;
	
	/**
	 * A reference to the menu, which displays
	 * the connection data (ip, port or path to the database).
	 */
	private MenuConnection menuCon;
	
	/**
	 * A reference to the menu, which supports the functionalities
	 * to rent or to release a Record.
	 */
	private MenuBookingBuyer menuBook;

	/**
	 * Stores the ipaddress or the path to the database.
	 * It will be displayed via menu 'Connection'.
	 */
	private String dbLocation;

	/**
	 * Stores the port number or just "alone"
	 * by local access. It will be displayed via menu 'Connection'.
	 */
	private String portOrLocal;

	/**
	 * Constructs a menu bar.
	 * 
	 * @param nos Stores statistic numbers.
	 * @param mw A reference to the main window.
	 * @param panT A reference to the panel, which contains the table.
	 * @param ipAddress The ipaddress of the server.
	 * @param portNo The port number of the server.
	 * @param intervalTime The interval time in ms.
	 */
	public MainWindowMenuBarBuyer(MenuCountsNos nos,
			MainWindowBuyer mw, 
			PanelTable panT
			, String ipAddress
			, String portNo
			, long intervalTime) {
		this.log.entering("MainWindowMenuBarBuyer", "MainWindowMenuBarBuyer",
				new Object[] {nos, mw, panT, ipAddress, portNo });
		this.mainW = mw;
		this.panTable = panT;
		this.dbLocation = ipAddress;
		this.portOrLocal = portNo;
		
		BuyerMenuItemActionListener menuItemtActLi =
				new BuyerMenuItemActionListener(this.mainW, this.panTable);
		
		// menu connection
		this.menuCon = new MenuConnection(this.dbLocation, this.portOrLocal, 
				intervalTime);
		this.menuCon.setMnemonic(KeyEvent.VK_C);
		this.menuCon.setToolTipText("Open with 'Alt' + C");		
		
		// menu booking
		this.menuBook = new MenuBookingBuyer(menuItemtActLi);
		this.menuBook.setMnemonic(KeyEvent.VK_E);
		this.menuBook.setToolTipText("Open with 'Alt' + E");
		
		// menu file
		this.add(new MenuFile());
		
		// menu connection
		this.add(this.menuCon);
		
		// menu booking
		this.add(this.menuBook);
		
		// menu counts
		this.mCounts = new MenuCounts(nos);		
		this.add(this.mCounts);
		
		// menu help
		this.add(new MenuHelp());
		
		// menu filter
		MenuFilter menuSearch = new MenuFilter(menuItemtActLi);
		menuSearch.setMnemonic(KeyEvent.VK_T);
		menuSearch.setToolTipText("Open with 'Alt' + T");
		this.add(menuSearch);
		
		this.log.exiting("MainWindowMenuBarBuyer", "MainWindowMenuBarBuyer");
	}
	
	/**
	 * Sets the value of the menu item 'Update time Interval',
	 * which displays the time interval.
	 * 
	 * @param tI A <code>java.lang.String</code> representation
	 * of number of milliseconds.
	 */
	public void setTimeInterval(String tI){
		this.menuCon.setTimeInterval(tI);
	}
	
	/**
	 * Updates the statistic numbers.
	 * 
	 * @param nos Stores statistic numbers.
	 */
	public void updateCount(MenuCountsNos nos){
		this.mCounts.upateCounts(nos);
	}
}
