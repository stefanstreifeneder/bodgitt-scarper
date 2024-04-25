package suncertify.gui.seller.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code> 
 * and handles all key press events on the table 
 * <code>suncertify.gui.PanelTable</code> used by the Seller main window.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class SellerPanTableKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerPanTableKeyListener</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerPanTableKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the panel, which possesses the table.
	 */
	private final PanelTable panTable;

	/**
	 * A reference to the main window.
	 */
	private final MainWindowSeller mainW;

	/**
	 * A reference to the search panel.
	 */
	private final PanelSearch panSearch;

	/**
	 * A reference to the panel, which enables to read, update, delete or add a
	 * Record.
	 */
	private final PanelUpDelAddSeller panUpDel;

	/**
	 * Creates an object of this class.
	 * 
	 * @param mw
	 *            A reference to the main window.
	 * @param searchPan
	 *            A reference to the search window.
	 * @param pUpDel
	 *            A reference to the panel, which enables read, update, delete
	 *            and add a Record.
	 * @param panT
	 *            A reference to the panel, which possesses the table.
	 */
	public SellerPanTableKeyListener(final MainWindowSeller mw, 
			final PanelSearch searchPan, final PanelUpDelAddSeller pUpDel,
			final PanelTable panT) {
		this.log.entering("SellerPanTableKeyListener", 
				"SellerPanTableKeyListener",
				new Object[] { mw, searchPan, panT });
		this.mainW = mw;
		this.panSearch = searchPan;
		this.panUpDel = pUpDel;
		this.panTable = panT;
		this.log.entering("SellerPanTableKeyListener", 
				"SellerPanTableKeyListener");
	}

	/**
	 * Overridden method, which handles a key pressing event.
	 * <br>
	 * Implemented key strokes:<br>
	 * R - Rent Record<br>
	 * S - Search/Referesh<br>
	 * Esc - exits application<br>
	 * O - Add Record<br>
	 * 
	 * @param e
	 *            A key event.
	 */
	@Override
	public void keyPressed(final KeyEvent e) {

		if (this.panTable.isFocused()) {
			final int val = e.getKeyCode();
			switch (val) {
			case KeyEvent.VK_ESCAPE:
				this.mainW.closeMainWindow();
				break;
			case KeyEvent.VK_O:// 'Add Record'
				if (this.panUpDel.isPanUpDelAddButtEnabl()) {
					this.mainW.addRec();
					this.panUpDel.setPanelUDA_AddButtEnabl(false);
				}
				break;
			case KeyEvent.VK_S:// 'Search/Refresh'
				final String[] cri = new String[] { 
						this.panSearch.getNameField(), 
						this.panSearch.getCityField() };
				this.mainW.searchForRecords(cri);
				this.panSearch.setFieldName("");
				this.panSearch.setFieldCity("");
				break;
			default:
				break;
			}
		}
	}
}
