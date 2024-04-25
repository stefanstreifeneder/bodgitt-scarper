package suncertify.gui.seller.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.seller.MainWindowSeller;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.SellerAddOperation;
import suncertify.gui.seller.SellerDeleteOperation;
import suncertify.gui.seller.SellerUpdateOperation;
import suncertify.gui.seller.WindowAddSeller;
import suncertify.gui.seller.WindowDeleteSeller;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of the class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class handles all key strokes on buttons, which a Seller client 
 * can do via his graphical user interface (main window, add window, 
 * delete window, update window).
 * <br>
 * The action commands are defined by the enum 
 * <code>suncertify.gui.ActionCommandButtons</code>.
 * 
 * 
 * @see suncertify.gui.ActionCommandButtons   
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerButtonKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerButtonKeyListener</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.listeners.SellerButtonKeyListener"), 
			Level.ALL);

	/**
	 * A reference to the panel, which enables read, update, delete and add
	 * functions.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * A reference to an object of the class, which does the add operation.
	 */
	private SellerAddOperation addOpp;

	/**
	 * A reference to the add window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to an object of the class, which does the delete operation.
	 */
	private SellerDeleteOperation delOpp;

	/**
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel;

	/**
	 * A reference to the main window.
	 */
	private MainWindowSeller mainW;

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
	 * @param wa Reference to the add window.
	 * @param upDelPan  A reference to the panel to update, delete
	 * and add a Record.
	 * @param oppAdd A reference to an object of the class, 
	 * which does the add operation.
	 */
	public SellerButtonKeyListener(MainWindowSeller mw, WindowAddSeller wa,
			PanelUpDelAddSeller upDelPan, SellerAddOperation oppAdd) {
		this.mainW = mw;
		this.winAdd = wa;
		this.panUpDel = upDelPan;
		this.addOpp = oppAdd;
	}
	
	/**
	 * Overloaded constructor to support an update operation.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param wu Reference to the update window.
	 * @param upDelPan  A reference to the panel to update, delete
	 * and add a Record.
	 * @param oppUp A reference to an object of the class, 
	 * which does the update operation.
	 */
	public SellerButtonKeyListener(MainWindowSeller mw, 
			long recNoPara,
			WindowUpdateSeller wu,
			PanelUpDelAddSeller upDelPan,
			SellerUpdateOperation oppUp) {
		this.mainW = mw;
		this.recNo = recNoPara;
		this.winUp = wu;
		this.panUpDel = upDelPan;
		this.upOpp = oppUp;
	}
	
	/**
	 * Overloaded constructor to support a delete operation.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param wd Reference to the delete window.
	 * @param oppDel A reference to an object of the class, 
	 * which does the delete operation.
	 */
	public SellerButtonKeyListener(MainWindowSeller mw, 
			long recNoPara,
			WindowDeleteSeller wd,
			SellerDeleteOperation oppDel) {
		this.mainW = mw;
		this.recNo = recNoPara;
		this.winDel = wd;
		this.delOpp = oppDel;
	}
	
	/**
	 * Overloaded constructor to control the
	 * search panel and the panel to update, delete, read and
	 * add a Record.
	 * 
	 * 
	 * @param mw A reference to the main window.
	 */
	public SellerButtonKeyListener(MainWindowSeller mw) {
		this.mainW = mw;
	}	

	/**
	 * Handles key events.
	 * 
	 * @param e
	 *            A key event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		this.log.entering("SellerButtonKeyListener", "SellerButtonKeyListener", e);
		JButton butt = (JButton) e.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {		
		case WINUP_UPDATE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.upOpp.updateMeth(this.winUp.getFieldVals(), 
						this.recNo, this.mainW);	
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.setupTableDatabase();
				this.winUp.dispose();
			}
			break;
		case WINUP_EXIT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER|
						e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.removeOfUpList(this.recNo);
				this.mainW.setupTableDatabase();
				this.winUp.dispose();
			}
			break;
		case WINDEL_DELETE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {				
				this.delOpp.deleteMeth(this.recNo, this.mainW);
				this.mainW.removeOfDelList(this.recNo);
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.removeOfDelList(this.recNo);				
				this.mainW.setupTableDatabase();
				this.winDel.dispose();
			}
			break;
		case WINDEL_EXIT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER|
						e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.removeOfDelList(this.recNo);
				this.mainW.setupTableDatabase();
				this.winDel.dispose();
			}
			break;
		case WINADD_ADD:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.addOpp.addMeth(this.winAdd.getFieldVals());
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.panUpDel.setPanelUDA_AddButtEnabl(true);
				this.winAdd.dispose();
				this.mainW.setupTableDatabase();			
			}
			break;
		case WINADD_EXIT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER|
						e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.panUpDel.setPanelUDA_AddButtEnabl(true);
				this.winAdd.dispose();
				this.mainW.setupTableDatabase();
			} 
			break;
		case PANUDRA_DELETE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {			
				this.mainW.deleteRec();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANUDRA_UPDATE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {	
				this.mainW.updateRec();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANUDRA_ADD:
			if (this.mainW.isPanelSellerEnabled()) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					this.mainW.addRec();
					this.mainW.setPanelSellerEnabled(false);
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANUDRA_READ:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.readRec();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANSEARCH:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.searchCriteriaSetPanelSearch();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
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
		this.log.exiting("SellerButtonKeyListener", "SellerButtonKeyListener");
	}
}
