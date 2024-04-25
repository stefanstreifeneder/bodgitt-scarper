package suncertify.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import suncertify.db.LoggerControl;


/**
 * The class is a <code>javax.swing.JMenu</code>
 * and displays the connection data (ip, port or
 * path to the database file) and the time period of the update
 * interval.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuConnection extends JMenu {
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 206L;
	
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.MenuConnection</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuConnection"), Level.ALL);	
	
	/**
	 * Menu item displays the time interval of the updates.
	 */
	private JMenuItem timeIntervalMenuItem;
	
	/**
	 * Menu item, which displays the ipaddress or path to the
	 * database.
	 */
	private JMenuItem dbLocationMenuItem;
	
	/**
	 * Menu item, which displays the port number or "alone",
	 * if the access is local.
	 */
	private JMenuItem portOrLocalMenuItem;
	
	/**
	 * Stores the ipaddress or the path to the database.
	 */
	private static String dbLocation;

	/**
	 * Stores the port number or just "alone"
	 * by local access.
	 */
	private static String portOrLocal;
	
	/**
	 * Stores the passed time between
	 * the updates of the table.
	 */
	private static long updateIntervale;
	
	/**
	 * Constructs an object of the class and initializes 
	 * the connection data and the time interval.
	 * 
	 * @param locationDB The ipaddress or the path to the
	 * database.
	 * @param localOrPort The port number of the server or just
	 * the "alone", if the access is local.
	 * @param intervalTime The number of milliseconds it takes to
	 * the next update of the table's content.
	 */
	public MenuConnection(String locationDB, String localOrPort, 
			long intervalTime){
		this.log.entering("MenuConnection", "MenuConnection", new Object[]{
				locationDB, localOrPort, Long.valueOf(intervalTime)});
		MenuConnection.dbLocation = locationDB;
		MenuConnection.portOrLocal = localOrPort;
		MenuConnection.updateIntervale = intervalTime;
		this.initialize();		
		this.log.exiting("MenuConnection", "MenuConnection");
	}
	
	/**
	 * Sets the text of the menu item,
	 * which displays the time interval.
	 * 
	 * @param tI A <code>java.lang.String</code> representation
	 * of number of milliseconds.
	 */
	public void setTimeInterval(String tI){
		this.timeIntervalMenuItem.setText(tI);
	}
	
	/**
	 * Sets the text of the menu item, which displays
	 * the ipaddress or the path to the database. 
	 * 
	 * @param path The ip of the server.
	 */
	public void setMenuConItemIP(String path){
		this.dbLocationMenuItem.setText(path);
	}
	
	/**
	 * Sets the text of the menu item, which displays
	 * the port number or "alone", if the access is local. 
	 * 
	 * @param portpara The port of the server's ip.
	 */
	public void setMenuConItemPort(String portpara){
		this.portOrLocalMenuItem.setText(portpara);
	}
	
	
	/**
	 * Initializes the graphical components.
	 */
	private void initialize(){
		// Menu Bar, ipaddress and port
		this.setText("Connection");
		// IP/Path
		JMenu connectionMenuItem = new JMenu("IP/Path: ");
		this.add(connectionMenuItem);
		this.dbLocationMenuItem = new JMenuItem(MenuConnection.dbLocation);
		connectionMenuItem.add(this.dbLocationMenuItem);
		// port
		JMenu portMenuItemText = new JMenu("Port/Mode: ");
		this.add(portMenuItemText);
		this.portOrLocalMenuItem = 
				new JMenuItem(MenuConnection.portOrLocal);
		portMenuItemText.add(this.portOrLocalMenuItem);

		// Interval Time
		JMenu intervalTimeMenuItem = new JMenu("Update time interval: ");
		this.timeIntervalMenuItem = 
				new JMenuItem(Long.valueOf(
						MenuConnection.updateIntervale).toString());
		intervalTimeMenuItem.add(this.timeIntervalMenuItem);
		this.add(intervalTimeMenuItem);
	}
}
