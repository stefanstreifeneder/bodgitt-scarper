package suncertify.gui.admin.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.seller.PanelUpDelAddSeller;

/**
 * An object of this class is a <code>java.awt.event.KeyAdapter</code>. 
 * It handles key events of the table panel
 * <code>suncertify.gui.PanelTable</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class AdminPanTableKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminPanTableKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(
					Logger.getLogger("suncertify.gui.admin."
							+ "listeners.AdminPanTableKeyListener"), 
					Level.ALL);

	/**
	 * A reference to the panel, which possesses the table.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * A reference to the search panel.
	 */
	private PanelSearch panSearch;

	/**
	 * A reference to the panel, which enables to read by Record number, 
	 * update, delete or add a Record.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * Constructs a key adapter.
	 * 
	 * @param mw
	 *            An object of the main window.
	 * @param searchPan
	 *            A reference to the panel, which supports the search 
	 *            mechanism.
	 * @param pUpDel
	 *            A reference to the panel, which enables read by Record 
	 *            number,
	 *            update, delete and add a Record.
	 * @param panT
	 *            A reference to the panel, which possesses the table.
	 */
	public AdminPanTableKeyListener(MainWindowAdmin mw, 
			PanelSearch searchPan, PanelUpDelAddSeller pUpDel,
			PanelTable panT) {
		this.log.entering("AdminPanTableKeyListener", 
				"AdminPanTableKeyListener", new Object[] { mw, searchPan, panT });
		this.mainW = mw;
		this.panSearch = searchPan;
		this.panUpDel = pUpDel;
		this.panTable = panT;
		this.log.exiting("AdminPanTableKeyListener", 
				"AdminPanTableKeyListener");
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
	public void keyPressed(KeyEvent e) {
		if (this.panTable.isFocused()) {
			final int val = e.getKeyCode();
			switch (val) {
			case KeyEvent.VK_R:// 'Rent Record'
				this.mainW.rentRecord();
				break;
			case KeyEvent.VK_S:// 'Search/Referesh'
				String[] cri = new String[] { 
						this.panSearch.getNameField(), 
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
			case KeyEvent.VK_O:// 'Add Record'
				if (this.panUpDel.isPanUpDelAddButtEnabl()) {
					this.mainW.addRec();
					this.panUpDel.setPanelUDA_AddButtEnabl(false);
				}
				break;
			default:
				break;
			}
		}
	}
}
