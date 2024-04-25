package suncertify.gui.seller.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.SellerAddOperation;
import suncertify.gui.seller.SellerDeleteOperation;
import suncertify.gui.seller.SellerUpdateOperation;
import suncertify.gui.seller.WindowAddSeller;
import suncertify.gui.seller.WindowDeleteSeller;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * The class implements the interface <code>java.awt.event.ActionListener</code>.
 * It handles all action events associated with buttons
 * an Seller Client can control via his graphical surface (main
 * window, add window, delete window and update window).
 * <br>
 * The action commands are defined by the enum 
 * <code>suncertify.gui.ActionCommandButtons</code>.
 * 
 * 
 * @see suncertify.gui.ActionCommandButtons  
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerButtonActionListener implements ActionListener{

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.listeners.SellerButtonActionListener</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerButtonActionListener"), 
			Level.ALL);

	/**
	 * A reference to the panel to update, delete
	 * and add a Record.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * A reference to an object of the class, which does the add operation.
	 */
	private SellerAddOperation addOpp;

	/**
	 * A reference to the main window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to an object of the class, which does the delete operation.
	 */
	private SellerDeleteOperation delOpp;

	/**
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel = null;

	/**
	 * A reference to the main window.
	 */
	private final MainWindowSeller mainW;

	/**
	 * The Record number.
	 */
	private long recNo;

	/**
	 * A reference to the update window.
	 */
	private WindowUpdateSeller winUp;

	/**
	 * A reference to an object of the class, which does the update operation.
	 */
	private SellerUpdateOperation upOpp;

	
	/**
	 * Overloaded constructor to support an add operation.
	 * 
	 * @param mw Reference to the main window.
	 * @param addWin Reference to the add window.
	 * @param upDelPan  A reference to the panel to update, delete
	 * and add a Record.
	 * @param oppAdd A reference to an object of the class, 
	 * which does the add operation.
	 */
	public SellerButtonActionListener(final MainWindowSeller mw, 
			final WindowAddSeller addWin, final PanelUpDelAddSeller upDelPan,
			final SellerAddOperation oppAdd) {
		this.mainW = mw;
		this.winAdd = addWin;
		this.panUpDel = upDelPan;
		this.addOpp = oppAdd;
	}	
	
	/**
	 * Overloaded constructor to support a delete operation.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param delWin Reference to the delete window.
	 * @param oppDel A reference to an object of the class, 
	 * which does the delete operation.
	 */
	public SellerButtonActionListener(final MainWindowSeller mw, 
			final long recNoPara,
			final WindowDeleteSeller delWin, 
			final SellerDeleteOperation oppDel) {
		this.mainW = mw;
		this.recNo = recNoPara;
		this.winDel = delWin;
		this.delOpp = oppDel;
	}	
	
	/**
	 * Overloaded constructor to support an update operation.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param upWin Reference to the update window.
	 * @param upDelPan  A reference to the panel to update, delete
	 * and add a Record.
	 * @param oppUp A reference to an object of the class, 
	 * which does the update operation.
	 */
	public SellerButtonActionListener(final MainWindowSeller mw, 
			final long recNoPara,
			final WindowUpdateSeller upWin,
			final PanelUpDelAddSeller upDelPan,
			final SellerUpdateOperation oppUp) {
		this.mainW = mw;
		this.recNo = recNoPara;
		this.winUp = upWin;
		this.panUpDel = upDelPan;
		this.upOpp = oppUp;
	}	
	
	/**
	 * Overloaded constructor to control the
	 * search panel.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 */	
	public SellerButtonActionListener(final MainWindowSeller mw) {
		this.mainW = mw;
	}	

	/**
	 * Overridden method, which cares for <code>java.awt.event.ActionEvents</code>
	 * evoked by buttons of an Seller client.
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		
		this.log.entering("SellerButtonActionListener", "actionPerformed", e);
		final JButton butt = (JButton) e.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {		
		case WINUP_UPDATE:
			this.upOpp.updateMeth(
					this.winUp.getFieldVals(), this.recNo, this.mainW);			
			break;
		case WINUP_EXIT:
			this.mainW.removeOfUpList(this.recNo);		
			this.mainW.setupTableDatabase();
			this.winUp.dispose();
			break;
		case WINDEL_DELETE:			
			this.mainW.removeOfDelList(this.recNo);				
			this.delOpp.deleteMeth(this.recNo, this.mainW);
			break;
		case WINDEL_EXIT:
			this.mainW.removeOfDelList(this.recNo);
			this.mainW.setupTableDatabase();
			this.winDel.dispose();
			break;
		case WINADD_ADD:
			this.addOpp.addMeth(this.winAdd.getFieldVals());
			break;
		case WINADD_EXIT:
			try {
				this.panUpDel.setPanelUDA_AddButtEnabl(true);
				this.winAdd.dispose();
			} catch (final Exception ex) {
				ExceptionDialog.handleException(("Exit " + "caused a failure: " 
						+ ex.getLocalizedMessage()));
			}
			this.mainW.setupTableDatabase();
			break;
		case PANUDRA_DELETE:
			this.mainW.deleteRec();
			break;
		case PANUDRA_UPDATE:
			this.mainW.updateRec();
			break;
		case PANUDRA_ADD:
			if (this.mainW.isPanelSellerEnabled()) {
				this.mainW.addRec();
				this.mainW.setPanelSellerEnabled(false);
			}
			break;
		case PANUDRA_READ:
			this.mainW.readRec();
			break;
		case PANSEARCH:
			this.mainW.searchCriteriaSetPanelSearch();
			break;
		case PANRR_RELEASE:
			//not available for a Seller client
		case PANRR_RENT:
			//not available for a Seller client
		case RELOAD_DB:
			//not available for a Seller client
		default:
			break;
		}
		this.log.exiting("SellerButtonActionListener", 
				"actionPerformed");		
	}
}
