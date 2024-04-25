package suncertify.gui.admin.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import suncertify.gui.seller.WindowAddSeller;
import suncertify.gui.seller.WindowDeleteSeller;
import suncertify.gui.seller.WindowUpdateSeller;


/**
 * The class implements the interface <code>java.awt.event.ActionListener</code>.
 * It handles all action events associated with buttons
 * an Admin Client can control via his graphical surface (main
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
public class AdminButtonActionListener implements ActionListener {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.listeners.AdminButtonMouseListener</code>.
	 */
	private Logger log = LoggerControl
			.getLoggerBS(Logger.getLogger(
					"suncertify.gui.admin.listeners.AdminButtonMouseListener"), 
					Level.ALL);

	/**
	 * A reference to an object of the class, 
	 * which does the add operation.
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
	 * A reference to an object of the class, 
	 * which does the update operation.
	 */
	private AdminUpdateOperation upOpp;
	
	/**
	 * A <code>java.lang.String</code> representation of the
	 * ipaddress.
	 */
	private String ip;
	
	/**
	 * A <code>java.lang.String</code> representation of the
	 * port number.
	 */
	private String port;
	
	/**
	 * A reference to the menu to display the connection
	 * adjustments.
	 */
	private MenuConnection menuCon;

	/**
	 * A reference to the graphical user interface's controller.
	 */
	private GuiControllerAdmin controller;

	
	/**
	 * Overloaded constructor to support an add operation.
	 * 
	 * @param window Reference to the main window.
	 * @param oppAdd A reference to an object of the class, 
	 * which does the add operation.
	 * @param addWin A reference to the add window.
	 */
	public AdminButtonActionListener(MainWindowAdmin window,
			AdminAddOperation oppAdd, WindowAddSeller addWin) {
		this.mainW = window;
		this.addOpp = oppAdd;
		this.winAdd = addWin;
	}
	
	/**
	 * Overloaded constructor to support an update operation.
	 * 
	 * @param window A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param cookie The lock cookie.
	 * @param upWin Reference to the update window.
	 * @param oppUp A reference to an object of the class, 
	 * which does the update operation.
	 * @param guiCtrl A reference to the graphical 
	 * user interface's controller.
	 */
	public AdminButtonActionListener(MainWindowAdmin window, long recNoPara, 
			long cookie, WindowUpdateSeller upWin, AdminUpdateOperation oppUp,
				GuiControllerAdmin guiCtrl) {
		this.mainW = window;
		this.recNo = recNoPara;
		this.lockCookie = cookie;
		this.winUp = upWin;
		this.upOpp = oppUp;
		this.controller = guiCtrl;		
	}
	
	/**
	 * Overloaded constructor to support a delete operation.
	 * 
	 * @param window A reference to the main window.
	 * @param recNoPara The Record number.
	 * @param cookie The lock cookie.
	 * @param delWin Reference to the delete window.
	 * @param oppDel A reference to an object of the class, 
	 * which does the delete operation.
	 * @param guiCtrl A reference to the graphical 
	 * user interface's controller.
	 */
	public AdminButtonActionListener(MainWindowAdmin window, long recNoPara, 
			long cookie, WindowDeleteSeller delWin, 
			AdminDeleteOperation oppDel,
				GuiControllerAdmin guiCtrl) {		
		this.mainW = window;
		this.recNo = recNoPara;
		this.lockCookie = cookie;
		this.winDel = delWin;
		this.delOpp = oppDel;
		this.controller = guiCtrl;
	}
	
	/**
	 * Overloaded constructor to to control the
	 * search panel.
	 * 
	 * @param window A reference to the main window.
	 */
	public AdminButtonActionListener(MainWindowAdmin window) {
		this.mainW = window;
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
	public AdminButtonActionListener(MainWindowAdmin window,
			String ipAddress, String portNo, MenuConnection conMenu) {		
		this.mainW = window;
		this.ip = ipAddress;
		this.port = portNo;
		this.menuCon = conMenu;		
	}
	
	/**
	 * Overridden method, which cares for <code>java.awt.event.ActionEvents</code>
	 * evoked by buttons of an Admin client.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.log.entering("AdminButtonMouseListener", 
				"AdminButtonMouseListener", arg0);
		JButton butt = (JButton) arg0.getSource();
		switch (ActionCommandButtons.valueOf(butt.getActionCommand())) {		
		case WINUP_UPDATE:
			this.upOpp.updateMeth(this.winUp.getFieldVals(), 
					this.recNo, this.lockCookie, this.mainW);
			break;
		case WINUP_EXIT:
			try {
				this.controller.unlockRecord(this.recNo, this.lockCookie);
			} catch (GuiControllerException gce) {
				ExceptionDialog.handleException(gce.toString());
			}
			this.mainW.setupTableDatabase();
			this.winUp.dispose();
			break;
		case WINDEL_DELETE:
			this.delOpp.deleteMeth(this.recNo, this.lockCookie, this.mainW);
			break;
		case WINDEL_EXIT:
			try {
				this.controller.unlockRecord(this.recNo, this.lockCookie);
			} catch (GuiControllerException gce) {
				ExceptionDialog.handleException(gce.toString());
			}
			this.mainW.setupTableDatabase();
			this.winDel.dispose();
			break;
		case WINADD_ADD:
			this.addOpp.addMeth(this.winAdd.getFieldVals());
			break;
		case WINADD_EXIT:
			this.mainW.setPanelSellerAddButtonEnabled(true);
			this.winAdd.dispose();
			this.mainW.setupTableDatabase();			
			break;
		case PANUDRA_DELETE:
			this.mainW.deleteRec();
			break;
		case PANUDRA_UPDATE:
			this.mainW.updateRec();
			break;
		case PANUDRA_ADD:
			if (this.mainW.isPanelSellerAddButtEnabled()) {
				this.mainW.addRec();
				this.mainW.setPanelSellerAddButtonEnabled(false);
			}
			break;
		case PANUDRA_READ:
			this.mainW.readRec();
			break;
		case PANSEARCH:
			this.mainW.searchCriteriaSetPanelSearch();
			break;
		case PANRR_RENT:
			this.mainW.rentRecord();
			break;
		case PANRR_RELEASE:
			this.mainW.releaseRecord();
			break;
		case RELOAD_DB:
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
								this.menuCon.setMenuConItemPort(tokens[1]);
								this.mainW.reloadDBRemote(tokens[0], tokens[1]);
								this.menuCon.setMenuConItemIP(tokens[0]);
								runPath = false;							
							}catch(Exception ae) {//due to many miscellaneous exceptions
								ExceptionDialog.handleException(
										"Connecting to the server failed!\n" 
										+ ae.getMessage());
							}
						}else {
							if (!new File(input).isFile()) {
								ExceptionDialog.handleException("This is not a file!\n" + input);
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
			break;
		default:
			break;
		}
		this.log.exiting("AdminButtonMouseListener", 
				"AdminButtonMouseListener");		
	}
}
