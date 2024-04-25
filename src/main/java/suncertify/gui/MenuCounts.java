package suncertify.gui;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import suncertify.db.LoggerControl;
import suncertify.db.Record;

/**
 * An object of the class is a <code>java.swing.JMenu</code>.<br>
 * <br>
 * The menu displays the following statistic numbers:<br>
 * - number of all Records (valid + deleted)<br>
 * - number of all valid Records<br>
 * - number of all deleted Records<br>
 * - number of all reserved Records<br>
 * - number of all locked Records
 * 
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuCounts extends JMenu {

	/**
	 * A version number for this class so that serialization 
	 * can occur without worrying about the underlying class 
	 * changing between serialization and deserialization.
	 */
	private static final long serialVersionUID = 207L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.MenuCounts</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuCounts"), Level.ALL);
	
	/**
	 * Displays the amount of all valid and deleted Records.
	 */
	private JMenuItem allRecsMenuItem = 
			new JMenuItem("Record all count: 0");

	/**
	 * Displays the amount of all valid Records.
	 */
	private JMenuItem recsValidMenuItem = 
			new JMenuItem("Record valid count: 0");

	/**
	 * Displays the amount of all reserved Records. 
	 */
	private JMenuItem rentMenuItem = new JMenuItem("Rented count: 0");

	/**
	 * Displays the amount of all deleted Records.
	 */
	private JMenuItem delMenuItem = new JMenuItem("Memory-Space: 0");
	
	/**
	 * Displays the amount of all locked Records.
	 */
	private JMenuItem lockedMenuItem = new JMenuItem("Locked count: 0");

	/**
	 * Stores the number of valid Records.
	 */
	private int countRecs = -1;

	/**
	 * Stores the number of all rented Records.
	 */
	private int countRented = -1;

	/**
	 * Stores the number of all deleted Records.
	 */
	private int countDeleted = -1;	
	
	/**
	 * Stores the number of all locked Records.
	 */
	private int countLocked = 0;
	
	/**
	 * Overloaded constructor, which sets the values of the 
	 * statistic numbers.
	 * 
	 * @param nos Stores the values of the statistic numbers as 
	 * properties.
	 */
	public MenuCounts(MenuCountsNos nos) {
		this.log.entering("MenuCounts", "MenuCounts", nos);
		this.setStatisticNumbers(nos);
		this.initialize();
		this.setItemText();
		this.log.exiting("MenuCounts", "MenuCounts");
	}	
	
	/**
	 * Sets the values of the statistic numbers
	 * by the properties of the method argument.
	 * 
	 * @param nos Stores the values of the statistic numbers as 
	 * properties.
	 */
	public void upateCounts(MenuCountsNos nos) {
		this.setStatisticNumbers(nos);
		this.setItemText();
	}

	/**
	 * Initializes the graphical components.
	 */
	private void initialize(){
		this.setText("Counts");
		this.setMnemonic(KeyEvent.VK_U);
		this.setToolTipText("Open with 'Alt' + U");
		this.add(this.allRecsMenuItem);
		this.add(this.recsValidMenuItem);
		this.add(this.delMenuItem);
		this.add(this.rentMenuItem);
		this.add(this.lockedMenuItem);
	}	
	
	/**
	 * Sets the text of the menu items.
	 */
	private void setItemText(){
		this.allRecsMenuItem.setText("Records all: " + Integer.valueOf(
				(this.countDeleted-70)/Record.RECORD_LENGTH));
		this.recsValidMenuItem.setText("Records valid: " 
				+ Integer.valueOf(this.countRecs));
		this.rentMenuItem.setText("Rented: " + Integer.valueOf(this.countRented));
		this.delMenuItem.setText("DB-Size: " + Integer.valueOf(this.countDeleted));
		this.lockedMenuItem.setText("Locked: " + Integer.valueOf(this.countLocked));
	}
	
	/**
	 * Sets the statistic numbers.
	 * 
	 * @param nos Stores the values of the statistic numbers as 
	 * properties.
	 */
	private void setStatisticNumbers(MenuCountsNos nos){
		this.countRecs = nos.getNos()[0];
		this.countRented =  nos.getNos()[1];		
		this.countDeleted =  nos.getNos()[2];
		this.countLocked =  nos.getNos()[3];
	}
}
