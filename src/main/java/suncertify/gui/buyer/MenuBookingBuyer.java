package suncertify.gui.buyer;

import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandMenuItems;

/**
 * The class is a <code>javax.swing.JMenu</code> and enables
 * to rent and to release a Record.
 * It possesses two menu items.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuBookingBuyer extends JMenu{
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 214;
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.MenuBookingBuyer</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.buyer.MenuBookingBuyer"),
			Level.ALL);
	
	/**
	 * Item to rent a Record.
	 */
	private JMenuItem rentMenuItem;
	
	/**
	 * Item to release a Record.
	 */
	private JMenuItem returnMenuItem;
	
	/**
	 * Constructor to built an object of the class.
	 * Adds an <code>ActionListener</code> to the component.
	 * 
	 * 
	 * @param aL Listener to react, if a menu item is selected.
	 */
	public MenuBookingBuyer(ActionListener aL){
		this.log.entering("MenuBookingBuyer", "MenuBookingBuyer");
		this.setText("Edit");
		this.rentMenuItem = new JMenuItem("Rent Record");	
		this.rentMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUBOOKING_RENT.name());
		this.add(this.rentMenuItem);
		this.returnMenuItem = new JMenuItem("Release Record");
		this.returnMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUBOOKING_RELEASE.name());
		this.add(this.returnMenuItem);		
		this.rentMenuItem.addActionListener(aL);
		this.returnMenuItem.addActionListener(aL);		
		this.log.exiting("MenuBookingBuyer", "MenuBookingBuyer");
	}
}
