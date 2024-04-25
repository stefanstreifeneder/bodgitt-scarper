package suncertify.gui.seller;

import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandMenuItems;

/**
 * The class is a <code>javax.swing.JMenu</code>
 * and enables to read, update, delete and to add a Record.
 * It possesses four menu items to support these functionalities.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuExtraSeller  extends JMenu{
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 227;
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.MenuExtraSeller</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.MenuExtraSeller"),
			Level.ALL);
	
	/**
	 * A menu item to read a Record by Record number.
	 */
	private JMenuItem readMenuItem;
	
	/**
	 * A menu item to update a Record.
	 */
	private JMenuItem updateMenuItem;
	
	/**
	 * A menu item to delete a Record.
	 */
	private JMenuItem deleteMenuItem;
	
	/**
	 * A menu item to add a Record.
	 */
	private JMenuItem addMenuItem;
	
	/**
	 * Constructor to built an object of the class.
	 * Adds an <code>ActionListener</code> to the component.
	 * 
	 * @param al Listener to react, if a menu item is selected.
	 */
	public MenuExtraSeller(ActionListener al){	
		this.log.entering("MenuExtraSeller", "MenuExtraSeller");
		this.setText("Extra");
		
		// menu read
		this.readMenuItem = new JMenuItem("Read Record");
		this.readMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUEXTRA_READ.name());
		this.add(this.readMenuItem);
		
		// menu update
		this.updateMenuItem = new JMenuItem("Update Record");
		this.updateMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUEXTRA_UPDATE.name());
		this.add(this.updateMenuItem);
		
		// menu delete
		this.deleteMenuItem = new JMenuItem("Delete Record");
		this.deleteMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUEXTRA_DELETE.name());
		this.add(this.deleteMenuItem);
		
		// menu add
		this.addMenuItem = new JMenuItem("Add Record");
		this.addMenuItem.setActionCommand(
				ActionCommandMenuItems.MENUEXTRA_ADD.name());
		this.add(this.addMenuItem);		
		
		//listeners
		this.readMenuItem.addActionListener(al);
		this.updateMenuItem.addActionListener(al);
		this.deleteMenuItem.addActionListener(al);
		this.addMenuItem.addActionListener(al);	
		
		this.log.exiting("MenuExtraSeller", "MenuExtraSeller");
	}
}
