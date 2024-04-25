package suncertify.gui.admin.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.MenuConnection;
import suncertify.gui.admin.AdminAddOperation;
import suncertify.gui.admin.AdminDeleteOperation;
import suncertify.gui.admin.AdminUpdateOperation;
import suncertify.gui.admin.GuiControllerAdmin;
import suncertify.gui.admin.MainWindowAdmin;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.WindowAddSeller;
import suncertify.gui.seller.WindowDeleteSeller;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of this class is a <code>java.awt.event.KeyAdapter</code>. 
 * The class handles all key strokes on buttons, which an Admin Client
 * can do  via his graphical surface (main
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
public class AdminButtonKeyListener extends KeyAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminButtonKeyListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.admin.listeners.AdminButtonKeyListener"), 
					Level.ALL);

	/**
	 * A reference to the panel, which enables read, update, delete and add
	 * functions.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * A reference to an object of the class, which does the add operation.
	 */
	private AdminAddOperation addOpp;

	/**
	 * A reference to the add window.
	 */
	private WindowAddSeller winAdd;

	/**
	 * A reference to an object of the class, which does the delete operation.
	 */
	private AdminDeleteOperation delOpp;

	/**
	 * A reference to the delete window.
	 */
	private WindowDeleteSeller winDel;

	/**
	 * A reference to the main window.
	 */
	private MainWindowAdmin mainW;

	/**
	 * The Record number.
	 */
	private long recNo;

	/**
	 * The lock cookie.
	 */
	private long lockCookie;

	/**
	 * A reference to the update window.
	 */
	private WindowUpdateSeller winUp;

	/**
	 * A reference to an object of the class, which does the update 
	 * operation.
	 */
	private AdminUpdateOperation upOpp;
	
	/**
	 * Stores the ip, while a reload operation.
	 */
	private String ip;
	
	/**
	 *  Stores the port number, while a reload operation.
	 */
	private String port;
	
	/**
	 *  Stores the connection menu, while a reload operation.
	 */
	private MenuConnection menuCon;

	/**
	 * A reference to the controller.
	 */
	private GuiControllerAdmin controller;

	
	/**
	 * Overloaded constructor to support an add operation.
	 * 
	 * @param mw Reference to the main window.
	 * @param wa Reference to the add window.
	 * @param upDelPan  A reference to the panel to update, delete
	 * and add a Record.
	 * @param guiCtrl Reference to the controller to access the database.
	 * @param oppAdd A reference to an object of the class, 
	 * which does the add operation.
	 */
	public AdminButtonKeyListener(MainWindowAdmin mw, WindowAddSeller wa, 
			PanelUpDelAddSeller upDelPan, GuiControllerAdmin guiCtrl, 
				AdminAddOperation oppAdd) {
		this.mainW = mw;
		this.winAdd = wa;
		this.panUpDel = upDelPan;
		this.controller = guiCtrl;
		this.addOpp = oppAdd;
	}
	
	/**
	 * Overloaded constructor to support a delete operation.
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param cookie The lock cookie.
	 * @param wd Reference to the delete window.
	 * @param oppDel A reference to an object of the class, 
	 * which does the delete operation.
	 * @param guiCtrl A reference to the graphical 
	 * user interface's controller.
	 */
	public AdminButtonKeyListener(MainWindowAdmin mw, long recNoPara,
			long cookie, WindowDeleteSeller wd, 
				AdminDeleteOperation oppDel,
					GuiControllerAdmin guiCtrl) {		
		this.mainW = mw;
		this.recNo = recNoPara;
		this.lockCookie = cookie;
		this.winDel = wd;
		this.delOpp = oppDel;
		this.controller = guiCtrl;
	}
	
	/**
	 * Overloaded constructor to support an update operation.
	 * 
	 * @param mw A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param cookie The lock cookie.
	 * @param wUp Reference to the update window.
	 * @param oppUp A reference to an object of the class, 
	 * which does the update operation.
	 * @param guiCtrl A reference to the graphical 
	 * user interface's controller.
	 */
	public AdminButtonKeyListener(MainWindowAdmin mw, long recNoPara,
			long cookie, WindowUpdateSeller wUp, 
				AdminUpdateOperation oppUp,
					GuiControllerAdmin guiCtrl) {
		this.mainW = mw;
		this.recNo = recNoPara;
		this.lockCookie = cookie;
		this.winUp = wUp;
		this.upOpp = oppUp;
		this.controller = guiCtrl;
	}	
	
	/**
	 * Overloaded constructor to to control the search panel and the panel 
	 * to update, delete, read and add a Record.
	 * 
	 * @param mw A reference to the main window.
	 */
	public AdminButtonKeyListener(MainWindowAdmin mw){
		this.mainW = mw;
	}	
	
	/**
	 * Controls the button of the menu bar to reload the database.
	 * 
	 * @param window A reference to the main window.	
	 * @param ipAddress The ipaddress as a <code>String</code>.
	 * @param portNo The port number as a <code>String</code>.
	 * @param conMenu A reference to the menu, which
	 * displays the connection data. 
	 */
	public AdminButtonKeyListener(MainWindowAdmin window,
			String ipAddress, String portNo, MenuConnection conMenu) {		
		this.mainW = window;
		this.ip = ipAddress;
		this.port = portNo;
		this.menuCon = conMenu;		
	}
	
	/**
	 * Handles key events.
	 * 
	 * @param e
	 *            A key event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		this.log.entering("AdminButtonKeyListener", "keyPressed", e);
		JButton butt = (JButton) e.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {
		case WINUP_UPDATE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
				this.upOpp.updateMeth(this.winUp.getFieldVals(), 
						this.recNo, this.lockCookie, this.mainW);
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				try {
					this.controller.unlockRecord(this.recNo, 
							this.lockCookie);
				} catch (GuiControllerException gce) {
					ExceptionDialog.handleException(gce.toString());
				}
				this.mainW.setupTableDatabase();
				this.winUp.dispose();
			}
			break;
		case WINUP_EXIT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER |
					e.getKeyCode() == KeyEvent.VK_ESCAPE ){
				try {
					this.controller.unlockRecord(this.recNo, 
							this.lockCookie);
				} catch (GuiControllerException gce) {
					ExceptionDialog.handleException(gce.toString());
				}
				this.mainW.setupTableDatabase();
				this.winUp.dispose();			
			}						
			break;
		case WINDEL_DELETE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.delOpp.deleteMeth(this.recNo, this.lockCookie, 
						this.mainW);
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				try {
					this.controller.unlockRecord(this.recNo, 
							this.lockCookie);
				} catch (GuiControllerException gce) {
					ExceptionDialog.handleException(gce.toString());
				}
				this.mainW.setupTableDatabase();
				this.winDel.dispose();
			}
			break;
		case WINDEL_EXIT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER |
					e.getKeyCode() == KeyEvent.VK_ESCAPE ){
				try {
					this.controller.unlockRecord(this.recNo, 
							this.lockCookie);
				} catch (GuiControllerException gce) {
					ExceptionDialog.handleException(gce.toString());
				}
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
			if (e.getKeyCode() == KeyEvent.VK_ENTER |
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
			if (this.mainW.isPanelSellerAddButtEnabled()) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {					
					this.mainW.addRec();
					this.mainW.setPanelSellerAddButtonEnabled(false);
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
		case PANRR_RENT:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.rentRecord();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case PANRR_RELEASE:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.mainW.releaseRecord();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		case RELOAD_DB:
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String pathIp =
						System.getProperty("user.dir") + File.separator
											+ "db-2x2.db";
						String textCon;
						if(!this.port.equals("alone")){
							pathIp = this.ip +";"
									+ this.port;
							textCon = this.mainW.getMSG_ID() 
									+ "You are connected to: "
									+ pathIp
									+ "\nYou have to use a semicolon (;) "
									+ "to spilt ipaddress "
									+ "and the port number."
									+ "\nPlease enter the ipaddress and "
									+ "port number:";
						}else{				
							textCon = this.mainW.getMSG_ID() 
									+ "You are connected to: "
									+ pathIp
									+ "\nPlease enter the path:";
						}
						
						boolean runPath = true;
						while(runPath) {
							String input = JOptionPane.showInputDialog(
									null, textCon, pathIp);			
							try{
								if(input != null){
									if(!this.port.equals("alone")){
										String[] tokens = input.split(";");
										try {
											this.menuCon.setMenuConItemPort(
													tokens[1]);
											this.mainW.reloadDBRemote(
													tokens[0], tokens[1]);
											this.menuCon.setMenuConItemIP(
													tokens[0]);
											runPath = false;							
										}catch(Exception ae) {//due to many miscellaneous exceptions
											ExceptionDialog.handleException(
													"Connecting to the server "
													+ "failed!\n" 
													+ ae.getMessage());
										}
									}else {
										if (!new File(input).isFile()) {
											ExceptionDialog.handleException(
													"This is not a file!\n" 
															+ input);
											continue;
										}
										this.mainW.reloadDBLocal(input);
										this.mainW.setupTableDatabase();
										this.menuCon.setMenuConItemIP(input);
										runPath = false;						
									}
								}else {
									runPath = false;
								}
							}catch(GuiControllerException e1){
								ExceptionDialog.handleException(
										e1.getLocalizedMessage());
							}
						}
				
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.mainW.closeMainWindow();
			}
			break;
		default:
			break;
		}
		this.log.exiting("AdminButtonKeyListener", "keyPressed");
	}
}
