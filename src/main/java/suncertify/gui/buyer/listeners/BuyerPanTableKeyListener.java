package suncertify.gui.buyer.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.buyer.MainWindowBuyer;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code> 
 * and handles all key press events of the table
 * <code>suncertify.gui.PanelTable</code> used in Buyer's main 
 * window.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class BuyerPanTableKeyListener extends KeyAdapter {
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.listeners.BuyerPanTableKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.buyer.listeners.BuyerPanTableKeyListener"),
					Level.ALL);

	/**
	 * A reference to the panel, which possesses the table.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the main window.
	 */
	private MainWindowBuyer mainW;

	/**
	 * A reference to the search panel.
	 */
	private PanelSearch panSearch;

	/**
	 * Constructs an object and initializes the instance variables.
	 * 
	 * @param mw
	 *            The reference to the main window.
	 * @param searchPan
	 *            A reference to the panel, which supports the search mechanism.
	 * @param panT
	 *            A reference to the panel, which possesses the table.
	 */
	public BuyerPanTableKeyListener(MainWindowBuyer mw, PanelSearch searchPan, 
			PanelTable panT) {
		this.log.entering("BuyerPanTableKeyListener", "BuyerPanTableKeyListener", 
				new Object[] { mw, searchPan, panT });
		this.mainW = mw;
		this.panSearch = searchPan;
		this.panTable = panT;
		this.log.exiting("BuyerPanTableKeyListener", "BuyerPanTableKeyListener");
	}

	/**
	 * Overridden method to handle key pressing events.<br>
	 * The key strokes are:<br>
	 * R - Rent Record<br>
	 * S - Search/Refresh<br>
	 * L - Return Record<br>
	 * Esc - exits application
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (this.panTable.isFocused()) {
			final int val = e.getKeyCode();
			switch (val) {
			case KeyEvent.VK_R:// 'Rent Record'
				this.mainW.rentRecord();
				break;
			case KeyEvent.VK_S:// 'Search/Refresh'
				String[] cri = new String[] { this.panSearch.getNameField(), 
						this.panSearch.getCityField() };
				this.mainW.searchForRecords(cri);
				this.panSearch.setFieldName("");
				this.panSearch.setFieldCity("");
				break;
			case KeyEvent.VK_L:// 'Return Record'
				this.mainW.releaseRecord();
				break;
			case KeyEvent.VK_ESCAPE:
				this.mainW.closeMainWindow();
				break;
			default:
				break;
			}
		}
	}
}
