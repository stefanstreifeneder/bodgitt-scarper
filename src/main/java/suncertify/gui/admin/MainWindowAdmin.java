package suncertify.gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.ApplicationMode;
import suncertify.gui.DatabaseLocationDialog;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.RecordTableModel;
import suncertify.gui.SavedConfiguration;
import suncertify.gui.StartMonitor;
import suncertify.gui.admin.listeners.AdminButtonActionListener;
import suncertify.gui.admin.listeners.AdminButtonKeyListener;
import suncertify.gui.admin.listeners.AdminPanSearchFieldsKeyListener;
import suncertify.gui.admin.listeners.AdminPanTableKeyListener;
import suncertify.gui.admin.listeners.AdminPanTableMouseListener;
import suncertify.gui.admin.listeners.AdminWinAddFieldsKeyListener;
import suncertify.gui.admin.listeners.AdminWinAddWindowListener;
import suncertify.gui.admin.listeners.AdminWinDelWinWindowListener;
import suncertify.gui.admin.listeners.AdminWinUpFieldsKeyListener;
import suncertify.gui.admin.listeners.AdminWinUpWinWindowListener;
import suncertify.gui.buyer.PanelRentBuyer;
import suncertify.gui.seller.PanelUpDelAddSeller;
import suncertify.gui.seller.WindowAddSeller;
import suncertify.gui.seller.WindowDeleteSeller;
import suncertify.gui.seller.WindowUpdateSeller;

/**
 * An object of the class is a <code>javax.swing.JFrame</code> and
 * represents the main window of an Admin client.<br>
 * <br>
 * The class will be used independent the database file will 
 * be accessed locally or remotely.<br>
 * <br>
 * The class uses the <code>java.awt.BorderLayout</code> to fix the graphics.<br>
 * The panel(@see <code>suncertify.gui.PanelTable</code>), which contains the 
 * table is settled by <code>BorderLayout.CENTER</code>.<br>
 * The panel, which contains the search panel(@see 
 * <code>suncertify.gui.PanelSearch</code>) , the panel
 * (@see <code>suncertify.gui.buyer.PanelRentBuyer</code>) to rent and the panel
 * (@see <code>suncertify.gui.seller.PanelUpDelAddSeller</code>) to read,
 * update, delete and to add a Record, is settled by
 * <code>BorderLayout.SOUTH</code>.<br>
 * In difference to the main windows of a Buyer client and a Seller client 
 * another panel is settled by a call to <code>BorderLayout.NORTH</code>, 
 * which displays statistic numbers. That panel substitutes the menu 'MenuCounts',
 * which is used by a Buyer client and a Seller Client to display
 * statistic numbers.
 * <br> 
 * The class uses an object of type 
 * <code>suncertify.gui.admin.MainWindowMenuBarAdmin</code>
 * as a menu bar.<br>
 * <br>
 * Implemented locking mechanism<br>
 * An Admin client acquires the lock of a Record by opening the update
 * or delete window. Within an update procedure a client keeps the lock until
 * he closes the window. Within a delete procedure a client
 * keeps the lock until he closes the window or he removes
 * the Record.
 * <br> It is not possible to lock more than one Record at the same
 * time.
 * <br>
 * <br> The class uses components of a Buyer client and a Seller client.
 * Of a Buyer client the class uses the panel 
 * <code>suncertify.gui.buyer.PanelRentBuyer</code> to proceed rent and release
 * operations. The panel 
 * <code>suncertify.gui.seller.PanelUpDelAddSeller</code> is provided
 * by the Seller client. 
 * <br> Additional the class uses the following classes of the a Seller client:
 * <br> to add a Record - <code>suncertify.gui.seller.WindowAddSeller</code>
 * <br> to delete a Record - <code>suncertify.gui.seller.WindowDeleteSeller</code>
 * <br> to update a Record - <code>suncertify.gui.seller.WindowUpdateSeller</code>
 * <br>  
 * <br>
 * MNemonics:<br>
 * A - 'Read Record' button<br>
 * B - menu button 'Reload DB'
 * C - menu 'Connection'<br>
 * D - 'Delete Record' button<br>
 * E - menu 'Edit'<br>
 * F - menu 'File'<br>
 * H - menu 'Help'<br>
 * L - 'Release Record' button<br>
 * N - check box 'Built intersection'<br>
 * O - 'Add Record' button<br>
 * P - 'Update Record' button<br>
 * Q - menu 'File' item 'Quite'<br>
 * R - 'Rent Record' button<br>
 * S - 'Search/Refresh Record' button<br>
 * T - menu 'Filter'<br>
 * U - menu 'Counts'<br>
 * X - menu 'Extras'<br>
 * 
 * 
 * @see GuiControllerAdmin
 * @see PanelTable
 * @see PanelSearch
 * @see PanelRentBuyer
 * @see PanelUpDelAddSeller
 * @see MainWindowMenuBarAdmin
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MainWindowAdmin extends JFrame {
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 231L;
	
	/**
	 * Returns s <code>String</code> representation of the
	 * ID, which is used, if there are messages displayed
	 * by a dialog.
	 */
	private final String MSG_ID;
	
	/**
	 * A variable, which stores the Application mode.
	 */
	private final ApplicationMode appType;

	/**
	 * The ID number of this client.
	 */
	private final int idOwner;

	/**
	 * The internal reference to the controller object.
	 */
	private GuiControllerAdmin controller;

	/**
	 * Stores the ipaddress or the path to the database.
	 * It will be displayed via menu 'MenuConnection'.
	 */
	private String dbLocation = "";

	/**
	 * Stores the port number or just "alone"
	 * by local access. It will be displayed via menu 'Connection'.
	 */
	private String portOrLocal = "";

	/**
	 * Panel contains the search panel, the panel to rent a Record 
	 * and the panel, which provides buttons to read, update,
	 * delete and add a Record.
	 */
	private JPanel panBorderLaySouth;

	/**
	 * Panel contains the table.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the search panel.
	 */
	private PanelSearch panSearch;

	/**
	 * A reference to the rent panel.
	 */
	private PanelRentBuyer panRent;

	/**
	 * A reference to the panel, which contains the four buttons to read,
	 * update, delete and add a Record.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * The menu bar.
	 */
	private MainWindowMenuBarAdmin mBar;

	/**
	 * The internal reference to the table model to display data.
	 */
	private RecordTableModel tableData = new RecordTableModel();

	/**
	 * A reference to the class, which reads and writes to the
	 * file suncertify.properties, which stores the connection
	 * data.
	 */
	private SavedConfiguration config;
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.MainWindowAdmin</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.MainWindowAdmin"),
			Level.ALL);

	/**
	 * Displays statistic numbers. These are the number of all Records,
	 * of all valid Records, the size of the database, the number 
	 * of all Records, which have an entry under ID owner and the number
	 * of all locked. 
	 */
	JLabel statisticNumbers;
	
	/**
	 * Stores the text for the <code>javax.swing.JLabel</code>,
	 * which displays statistic numbers.
	 */
	String sNums = "";
	
	/**
	 * Stores how long it takes to the next update.
	 */
	long updateInterval = -1;	
	
	/**
	 * Builds and displays the main application window in local 
	 * or remote mode. The constructor begins  by building the 
	 * connection dialog box, then it constructs an object of 
	 * type <code>suncertify.gui.buyer.GuiControllerAdmin</code> to
	 * access the database file. After the user has made
	 * his connection configurations the constructor makes a call to the method
	 * <code>initMainWindow</code> to initialize the graphical components.
	 * 
	 * @param id The ID of the client.
	 * @param appMode The application mode.
	 * @throws GuiControllerException Thrown, if main window
	 * can no be built.
	 */
	public MainWindowAdmin(final ApplicationMode appMode, final int id) 
			throws GuiControllerException {
		this.log.entering("MainWindowAdmin", "MainWindowAdmin", 
				new Object[] {Integer.valueOf(id), appMode });
		this.idOwner = id;
		this.MSG_ID = "Cient ID " + this.idOwner + "\n\n";
		this.appType = appMode;
		String db = "";
		final DatabaseLocationDialog dbLocationLocal = 
				new DatabaseLocationDialog(this.appType, this.idOwner);
		if(this.appType == ApplicationMode.NETWORK_CLIENT){
			this.dbLocation = dbLocationLocal.getIpAddress();
			this.portOrLocal = dbLocationLocal.getPort();
			db = dbLocationLocal.getIpAddress();
			this.controller = new GuiControllerAdmin(this.appType, 
					this, db, this.portOrLocal);
			
		} else {
			this.dbLocation = dbLocationLocal.getLocation();
			this.portOrLocal = "alone";
			db = dbLocationLocal.getLocation();
			this.controller = new GuiControllerAdmin(this.appType, 
					this, db, this.portOrLocal, dbLocationLocal.getPassword());
			
		}
		initMainWindow();		
		this.log.exiting("MainWindowAdmin", "MainWindowAdmin");
	}
	
	
	
	/**
	 * Builds and displays the main application window in remote mode
	 * and refreshes the current content of the database in time
	 * intervals. The first call is to the other overloaded constructor, 
	 * whereby <code>ApplicationMode.NETWORK_CLIENT</code> is used as
	 * a method argument. Afterwards the update functionality will be 
	 * adjusted.
	 * 
	 * @param id The id owner number.
	 * @param msInterval The time interval of the updates.
	 * @throws GuiControllerException 
	 *             Indicates a database or network level exception.
	 */
	public MainWindowAdmin(final int id, final long msInterval) 
			throws GuiControllerException{
		this(ApplicationMode.NETWORK_CLIENT, id);
		this.mBar.setTimeInterval(Long.valueOf(msInterval).toString());
		this.getUpdates();
		this.updateInterval = msInterval;
	}

	/**
	 * Overridden method to control the shutdown mechanism.
	 * 
	 * @param e
	 *            Represents the command.
	 */
	@Override
	protected void processWindowEvent(final WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			closeMainWindow();
		}
		super.processWindowEvent(e);
	}

	/**
	 * Closes the application.
	 */
	public void closeMainWindow() {
		this.log.severe("MainWindowAdmin, closeMainWindow, " + "start");
		try {
			if(this.appType == ApplicationMode.NETWORK_CLIENT){
				this.controller.closeNetworkConnection();
			}
		} catch (final GuiControllerException e) {
			this.log.severe("MainWindowAdmin, closeMainWindow,"
					+ " Exc: " + e.getLocalizedMessage());		
		}
		this.log.severe("MainWindowAdmin, closeMainWindow, " + "Applications exits!");		
		System.exit(0);
	}

	/**
	 * Creates a frame to add a Record to the database file.
	 * The frame is represented by an object of type
	 * <code>suncertify.gui.seller.WindowAddSeller</code>, which
	 * belongs to the Seller client. 
	 */
	public void addRec() {
		this.log.entering("MainWindowAdmin", "addRec");
		this.setupTableDatabase();		
		final WindowAddSeller wa = new WindowAddSeller(this.appType, this.idOwner);		
		final AdminAddOperation addOpp = new AdminAddOperation(this, wa, 
				this.controller, this.panUpDel);		
		wa.setFieldKeyListener(new AdminWinAddFieldsKeyListener(this, 
				wa, this.controller, this.panUpDel));
		wa.setWindowAddButtonActionListener(new AdminButtonActionListener(this,
						addOpp, wa));
		wa.setWindowAddButtonKexListener(new AdminButtonKeyListener(this, wa,
				this.panUpDel, this.controller, addOpp));
		wa.setAddWindowListener(new AdminWinAddWindowListener(this.panUpDel));
		this.log.exiting("MainWindowAdmin", "addRec");
	}

	/**
	 * Creates a frame to delete a Record. The frame is represented by an object of type
	 * <code>suncertify.gui.seller.WindowDeleteSeller</code>, which
	 * belongs to the Seller client. The method displays a dialog, if the 
	 * Record is locked. It acquires the lock of a Record.<br>  
	 * If the delete window has opened, the row in the table will be colored
	 * red.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void deleteRec() {
		this.log.entering("MainWindowAdmin", "deleteRec");
		// There won't be assigned a new default Cursor object,
		// because this is done by the method setupTableDatabase().
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		long recNo = 0;
		Record rec = null;
		try {
			recNo = this.panTable.getRecNo();
			rec = this.controller.getRecord(recNo);
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(this,
							this.MSG_ID + getMsgRecNo(recNo) 
								+ "This Record is locked, if you press 'yes', "
									+ "You have to wait til the Record "
									+ "will be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);	
					
					if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
						this.setupTableDatabase();
						return;
					}
				}
			}
		} catch (final HeadlessException e1) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e1.getLocalizedMessage());
			return;
		} catch (final GuiControllerException e1) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e1.getLocalizedMessage());
			return;
		}		
		final String[] data = 
				new String[Record.RECORD_FIELDS_WITHOUT_FLAG_PLUS_RECNO];
		for (int i = 0; i < Record.RECORD_FIELDS_WITHOUT_FLAG; i++) {
			try {
				data[i] = this.panTable.getRecord()[i];
			} catch (final GuiControllerException e) {
				this.setupTableDatabase();
				ExceptionDialog.handleException(e.getLocalizedMessage());
				return;
			}
		}
		// Check changes of the Record's values.
		try {
			boolean runWhile = true;
			while (runWhile) {
				this.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
				
				final EnumChangeMode choice =
						this.controller.checkRecordHasChanged(
								rec, recNo, EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
								"ADMIN - DELETE - BEFORE LOCK");
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					this.setupTableDatabase();
					return;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					runWhile = false;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					runWhile = true;
				}
			}
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		
		
		data[6] = Long.valueOf(recNo).toString();
		long lockCookie = 0;
		Record newRecord = null;
		try {
			lockCookie = this.controller.lockRecord(recNo);
			newRecord = this.controller.getRecord(recNo);
			this.setupTableDatabase();
			newRecord = this.controller.getRecord(recNo);
			final int mode = checkRecChangedMainWinAdmin(data, recNo, 
					this, newRecord, "ADMIN - DELETE -  - YOU GET THE LOCK");
			if(mode == JOptionPane.NO_OPTION || 
					mode == JOptionPane.CLOSED_OPTION){
				this.controller.unlockRecord(recNo, lockCookie);
				this.setupTableDatabase();
				return;
			}
			data[0] = newRecord.getName();
			data[1] = newRecord.getCity();
			data[2] = newRecord.getTypesOfWork();
			data[3] = Integer.valueOf(newRecord.getNumberOfStaff()).toString();
			data[4] = newRecord.getHourlyChargeRate();
			data[5] = newRecord.getOwner();
			
			// creates a delete window
			final WindowDeleteSeller wd = 
					new WindowDeleteSeller(data, this.appType, this.idOwner);
			final AdminDeleteOperation delOpp = 
					new AdminDeleteOperation(wd, this.controller);
			wd.setThisDelWindowListener(
					new AdminWinDelWinWindowListener(
							this, recNo, lockCookie, wd, this.controller));
			wd.setWindowDeleteActionListener(
					new AdminButtonActionListener(
							this, recNo, lockCookie, wd, delOpp, this.controller));
			wd.setWindowDeleteKeyListener(
					new AdminButtonKeyListener(this,
							recNo, lockCookie, wd, delOpp, this.controller));
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}	
		this.setupTableDatabase();
		this.log.exiting("MainWindowAdmin", "deleteRec");
	}

	/**
	 * Opens a frame to update a Record. Displays a dialog, if the Record is
	 * locked. The method acquires the lock of a Record.
	 * <br>
	 * If the update window has opened, the row in the table will be colored
	 * red. 
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void updateRec() {
		this.log.entering("MainWindowAdmin", "updateRec");		
		this.setupTableDatabase();		
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		long recNo = 0;
		Record rec;
		try {
			recNo = this.panTable.getRecNo();
			rec = this.controller.getRecord(recNo);
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(this, this.MSG_ID 
							+ getMsgRecNo(recNo) + "This Record is locked, if "
									+ "you press 'yes', You have to wait til the "
									+ "Record will be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);					
					if (n == JOptionPane.NO_OPTION || 
								n == JOptionPane.CLOSED_OPTION) {
						setupTableDatabase();
						return;
					}
				}
			}
		} catch (final HeadlessException hexc) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(hexc.getLocalizedMessage());
			return;
		} catch (final GuiControllerException gec) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(gec.getLocalizedMessage());
			return;
		}
		
		final String[] data = 
				new String[Record.RECORD_FIELDS_WITHOUT_FLAG_PLUS_RECNO];
		for (int i = 0; i < Record.RECORD_FIELDS_WITHOUT_FLAG; i++) {
			try {
				data[i] = this.panTable.getRecord()[i];
			} catch (final GuiControllerException e) {
				this.setupTableDatabase();
				ExceptionDialog.handleException(e.getLocalizedMessage());
				return;
			}
		}
		// Check changes of the Record's values.
		try {
			boolean runWhile = true;
			while (runWhile) {
				this.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
				
				final EnumChangeMode choice = 
						this.controller.checkRecordHasChanged(
								rec, recNo, EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
								"ADMIN - UPDATE - BEFORE LOCK");
				
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					this.setupTableDatabase();					
					return;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					runWhile = false;
				} else if (choice == EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					runWhile = true;
				}
			}
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID + getMsgRecNo(recNo) 
			+ e.getLocalizedMessage());
			return;
		}
		
		data[6] = Long.valueOf(recNo).toString();
		long lockCookie;
		Record newRecord;
		try {
			lockCookie = this.controller.lockRecord(recNo);
			newRecord = this.controller.getRecord(recNo);
			this.setupTableDatabase();
			final int mode = checkRecChangedMainWinAdmin(data, recNo, this, 
					newRecord, "ADMIN - UPDATE - YOU GET THE LOCK");
			if(mode == JOptionPane.NO_OPTION || 
					mode == JOptionPane.CLOSED_OPTION){
				this.controller.unlockRecord(recNo, lockCookie);
				this.setupTableDatabase();
				return;
			}	
			data[0] = newRecord.getName();
			data[1] = newRecord.getCity();
			data[2] = newRecord.getTypesOfWork();
			data[3] = Integer.valueOf(newRecord.getNumberOfStaff()).toString();
			data[4] = newRecord.getHourlyChargeRate();
			data[5] = newRecord.getOwner();

			// creates an update window
			final WindowUpdateSeller wu = 
					new WindowUpdateSeller(data, this.appType, this.idOwner);
			final AdminUpdateOperation upOpp = 
					new AdminUpdateOperation(this.controller);
			wu.setFieldKeyListener(
					new AdminWinUpFieldsKeyListener(
							this, recNo, lockCookie, wu, this.controller));
			wu.setThisUpWindowListener(
					new AdminWinUpWinWindowListener(
							this, recNo, lockCookie, wu, this.controller));
			wu.setWindowUpdateActionListener(new AdminButtonActionListener(this,
					recNo, lockCookie, wu, upOpp, this.controller));
			wu.setWindowUpdateKeyListener(new AdminButtonKeyListener(
					this, recNo, lockCookie, wu, upOpp, this.controller));
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		this.setupTableDatabase();
		this.log.exiting("MainWindowAdmin", "updateRec");
	}

	/**
	 * Handles the rent operation.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void rentRecord() {
		this.log.entering("MainWindowAdmin", "rentRecord");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		long recNo = 0;
		Record rec;
		try {
			recNo = this.panTable.getRecNo();
			rec = this.controller.getRecord(recNo);
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			setupTableDatabase();
			return;
		}		
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(this, this.MSG_ID 
							+ getMsgRecNo(recNo) + "This Record is locked, "
									+ "if you press 'yes', You have to wait "
									+ "til the Record will be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.NO_OPTION || 
							n == JOptionPane.CLOSED_OPTION) {
						setupTableDatabase();
						return;
					}
				}
			}
		} catch (final HeadlessException e) {
			setupTableDatabase();
			ExceptionDialog.handleException(e.getLocalizedMessage());
			return;
		} catch (final GuiControllerException e) {
			setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		// Check changes of the Record's values.
		try{
			boolean runWhile = true;
			while(runWhile){
				this.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
				
				
				final EnumChangeMode choice = 
						this.controller.checkRecordHasChanged(
								rec, recNo, EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
								"ADMIN - RENT - BEFORE LOCK");
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					this.setupTableDatabase();
					return;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					runWhile = false;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					runWhile = true;
				}
			}
		}catch (final GuiControllerException e) {
			setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage()
					);
			return;
		}	
		try {
			final boolean rentStatus = 
					this.controller.rent(recNo, this.idOwner);
			if (rentStatus == false) {
				JOptionPane.showMessageDialog(this.rootPane,
						this.MSG_ID + getMsgRecNo(recNo) 
						+ " - The 'ID Owner' field is not blank!");
				setupTableDatabase();
				return;
			}
			setupTableDatabase();
		} catch (final GuiControllerException gce) {
			this.log.severe("MainWindowAdmin, rentRecord, " + gce.getMessage());
			ExceptionDialog.handleException(this.MSG_ID + getMsgRecNo(recNo) 
				+ gce.getMessage());
			setupTableDatabase();
			return;
		}
	}

	/**
	 * Handles the release operation. Displays a dialog, if the ID is not the
	 * same. Displays a dialog, if the Record is locked.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void releaseRecord() {
		this.log.entering("MainWindowAdmin", "releaseRecord");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);		
		long recNo = 0;
		Record rec;
		String ownerString;
		try {
			recNo = this.panTable.getRecNo();
			rec = this.controller.getRecord(recNo);
			ownerString = this.panTable.getRecord()[5];
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + 	e.getLocalizedMessage());
			return;
		}
			
		if (ownerString != null) {
			if (!ownerString.equals("")) {				
				if (!ownerString.equals(Integer.toString(this.idOwner))) {				
					this.log.fine("MainWindowAdmin, releaseRecord, "
							+ "Record was " 
							+ "reservated by an other client.");
					final int answer = 
							JOptionPane.showConfirmDialog(null, this.MSG_ID 
							+ getMsgRecNo(recNo) + 	"Your ID is not equal to the "
									+ "entry in 'ID Owner'!"
									+ "\nDo You want to release the Record?",
							"B & S - Release Record Dialog",
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE);
					
					if (answer == JOptionPane.NO_OPTION || 
							answer == JOptionPane.CLOSED_OPTION) {
						this.log.fine("MainWindow, releaseRecord, exit");
						this.setupTableDatabase();
						return;
					}
				}
			}
		} // test ID end		
		
		// Check changes of the Record's values.
		try {
			if(this.controller.checkRecordHasChanged(rec, recNo, 
							EnumChangeMode.CHANGE_MODE_END_GOON,
							"ADMIN - RELEASE - AFTER ID")
					== EnumChangeMode.CHANGE_RETURN_EXIT_METH){
				this.setupTableDatabase();				
				ExceptionDialog
				.handleException(this.MSG_ID + getMsgRecNo(recNo) 
						+ "Release operation is " 
						+ "cancled! Meanwhile values of the "
						+ "Record has changed");
				return;
			}
		} catch (final GuiControllerException e1) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e1.getLocalizedMessage());
			return;
		}
		// tests locks 
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(this, 
							this.getMSG_ID() 
							+ MainWindowAdmin.getMsgRecNo(recNo) + 
							"This Record is locked, if you press 'yes', "
									+ "You have to wait til the Record "
									+ "will be unlocked!",
							"Information Dialog - Locks", 
							JOptionPane.YES_NO_OPTION);
					
					if (n == JOptionPane.NO_OPTION || 
							n == JOptionPane.CLOSED_OPTION) {
						this.setupTableDatabase();
						return;
					}
				}
			}
		} catch (final HeadlessException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e.getLocalizedMessage());
			return;
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e.getLocalizedMessage());
			return;
		}
		// Check changes of the Record's values.	
		try{
			boolean runWhile = true;
			while(runWhile){
				this.setupTableDatabase();
				// must, because 'setupTableDatabase()' sets
				// the cursor to default
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
				
				final EnumChangeMode choice =
						this.controller.checkRecordHasChanged(
							rec, recNo, EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
							"ADMIN - RELEASE - BEFORE LOCK");
				if (choice == EnumChangeMode.CHANGE_RETURN_EXIT_METH) {
					this.setupTableDatabase();
					return;
				} else if (choice == 
						EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK) {
					runWhile = false;
				} else if (choice == EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK) {
					runWhile = true;
				}
			}
		}catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		try {
			final boolean rentStatus = 
					this.controller.returnRental(recNo);			
			if (rentStatus == false) {
				// Actually can never happen.
				// The method <code>releaseRecord</code> of the public interface 
				// <code>InterfaceClient_Buyer</code> returns true or an
				// exception will be thrown.
			}
			this.setupTableDatabase();
		} catch (final GuiControllerException gce) {
			this.log.severe("MainWindowAdmin, releaseRecord, " 
					+ gce.getMessage());
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID + getMsgRecNo(recNo) 
			+ gce.getMessage());
			return;
		}
		this.log.exiting("MainWindowAdmin", "releaseRecord");
	}

	/**
	 * Opens a dialog to choose a Record by Record number. The selected Record
	 * will be displayed in the table.
	 */
	public void readRec() {
		List<Record> list = new ArrayList<Record>();
		try {
			list = this.controller.getRecordsList();
		} catch (final GuiControllerException ex) {
			ExceptionDialog.handleException(
					"Read Record failed! Transmission " + "problems occured! " 
							+ ex.getLocalizedMessage());
			return;
		}
		final List<Long> recordNumbers = new ArrayList<Long>();
		final Object[] o = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			recordNumbers.add(Long.valueOf(list.get(i).getRecNo()));
			o[i] = recordNumbers.get(i);
		}
		if(o.length == 0){
			final JOptionPane alert = new JOptionPane(this.MSG_ID 
					+ "THERE ARE NO VALID RECORDS! CHECK MENU 'COUNT'!",
							JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
					final JDialog dialog = alert.createDialog(null, "Alert");
					final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					final int x = (int) ((d.getWidth() - dialog.getWidth()) / 2);
					final int y = (int) ((d.getHeight() - dialog.getHeight()) / 2);
					dialog.setLocation(x, y);
					dialog.setVisible(true);
					return;
		}
		Arrays.sort(o);
		final JDialog dialog = new JDialog();
		final Long input = (Long) JOptionPane.showInputDialog(dialog,
				"All Record numbers are displayed in the menu." 
				+ "\nChoose a Record number. That Record "
				+ "\nwill be displayed in the table of the main window.",
				"Client-No: " + this.idOwner + " - " + this.appType.name(), 
				JOptionPane.PLAIN_MESSAGE, null, o,
				o[0].toString());
		if (input != null) {
			try {
				// row coloring
				this.panTable.setRowTabelColor(0, Color.WHITE);//clean row
				final Set<Long> locks = this.controller.getLocks();
				if (!locks.isEmpty()) {
					for (final Long l : locks) {
						if (l.equals(input)) {
							this.panTable.setRowTabelColor(0, Color.RED);
						}
					}
				}
				// assigning the data
				this.tableData = this.controller.findRecord(input.longValue());
				this.panTable.setModelEx(this.tableData);
				this.panTable.setRowSelect(0);
			} catch (final GuiControllerException ex) {
				ExceptionDialog.handleException(this.getMSG_ID()
						+ "To read Record (" + input.longValue() 
						+ ") failed!\n" + ex.getMessage());
				this.log.severe("MainWindowAdmin, readRec, Exception: " 
						+ ex.getLocalizedMessage());
				return;
			}
		} else {
			dialog.dispose();
		}
	}

	/**
	 * Searches for Records by filter. The method checks
	 * the state of the check box button of the panel
	 * <code>panSearch</code> to build an union or an intersection.
	 * Found Records will be displayed in the table.
	 * Locked Records will be colored red. The selected row
	 * is set by the Record number. The statistic numbers
	 * will be set.
	 * 
	 * @param crit
	 *            Filter to search for matching Records.
	 */
	public void searchForRecords(final String[] crit) {		
		this.log.entering("MainWindowAdmin", "searchForRecords");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		final long recNoOfIndex = this.tableData.getRecNo(Integer.valueOf(
				this.panTable.getRowSelected()));
		try {
			this.tableData = this.controller.findRecordByCriteria(
					crit, this.panSearch.getCheckBoxUnionSelected());
			final Map<Integer, Long> mapRowNoRecNo = new HashMap<Integer, Long>();
			mapRowNoRecNo.putAll(this.tableData.getRowRecNoMap());
			final List<Integer> colRecs = new ArrayList<Integer>();
			final Set<Long> locks = this.controller.getLocks();
			for (int i = 0; i <= this.tableData.getRowCount(); i++) {
				if (locks.isEmpty()) {
					this.panTable.setRowTabelColor(i, Color.WHITE);
				} else {
					for (final Long l : locks) {
						if (l.equals(mapRowNoRecNo.get(Integer.valueOf(i)))) {
							colRecs.add(Integer.valueOf(i));
						}else {
							this.panTable.setRowTabelColor(i, Color.WHITE);
						}
					}
				}
			}
			for (final Integer i : colRecs) {
				this.panTable.setRowTabelColor(i.intValue(), Color.RED);
			}
			// statistic numbers
			int idsOwner = 0;
			int recsValid = 0;
			for (final Record r : this.controller.getRecordsList()) {
				recsValid++;
				if (r.getOwner().length() != 0) {
					idsOwner++;
				}
			}
			final int dbSize = (int) this.controller.getMemory();
			this.sNums = "All Records: " + ((dbSize-70)/Record.RECORD_LENGTH) 
					+ " - Valid Records: " + recsValid 
					+ " - DB-Size: " + dbSize
					+ " - Rented: " + idsOwner 
					+ " - Locked: " + locks.size();
			this.statisticNumbers.setText(this.sNums);		
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException(this.MSG_ID 
					+ "Search/Refresh Operation failed!" 
							+ " " + e.getLocalizedMessage());
		}
		this.panTable.setModelEx(this.tableData);
		if (this.tableData.getRowRecNoMap().values().contains(Long.valueOf(recNoOfIndex))) {
			for (final Map.Entry<Integer, Long> map : this.tableData.getRowRecNoMap().entrySet()) {
				if (map.getValue().equals(Long.valueOf(recNoOfIndex))) {
					final int id = map.getKey().intValue();
					this.panTable.setRowSelect(id);
				}
			}
		}
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.log.entering("MainWindowAdmin", "searchForRecords");
	}

	/**
	 * Refreshes the content of the table with a call to the database.
	 * Locked Records will be colored red. The statistic numbers
	 * above the table will be refreshed. 
	 * 
	 */
	public void setupTableDatabase() {
		this.log.entering("MainWindowAdmin", "setupTableDatabase");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		final int rowSelected = this.panTable.getRowSelected();
		final long recNoPara = this.tableData.getRecNo(Integer.valueOf(rowSelected));
		final int recNoInt = (int) recNoPara;
		Integer index = Integer.valueOf(recNoInt);
		final Map<Integer, Long> mapRowNoRecNo = new HashMap<Integer, Long>();
		try {
			this.tableData = this.controller.getAllRecordsModel();
			mapRowNoRecNo.putAll(this.tableData.getRowRecNoMap());
			this.panTable.setModelEx(this.tableData);				
			final List<Integer> colRecs = new ArrayList<Integer>();			
			final Set<Long> locks = new HashSet<Long>();
			locks.addAll(this.controller.getLocks());
			// locked Records
			for (int i = 0; i < this.tableData.getRowCount(); i++) {
				if (locks.isEmpty()) {
					this.panTable.setRowTabelColor(i, Color.WHITE);
				} else {
					for (final Long l : locks) {
						if (l.equals(mapRowNoRecNo.get(Integer.valueOf(i)))) {
							colRecs.add(Integer.valueOf(i));
						} else {
							this.panTable.setRowTabelColor(i, Color.WHITE);
						}						
					}
				}				
			}			
			for (final Integer i : colRecs) {
				this.panTable.setRowTabelColor(i.intValue(), Color.RED);
			}			
			// statistic numbers
			int idsOwner = 0;
			int recsValid = 0;
			for(final Record r : this.controller.getRecordsList()){
				recsValid++;
				if (r.getOwner().length() != 0) {
					idsOwner++;
				}	
			}
			final int dbSize = (int) this.controller.getMemory();
			this.sNums = "All Records: " + ((dbSize-70)/Record.RECORD_LENGTH)
					+ " - Valid Records: " + recsValid
					+ " - DB-Size: " + dbSize
					+ " - Rented: " + idsOwner
					+ " - Locked: " + locks.size();			
			this.statisticNumbers.setText(this.sNums);
		} catch (final GuiControllerException e) {
			this.log.severe("MainWindowAdmin, setupTableDatabase, " 
					+ e.getMessage());
			ExceptionDialog.handleException("Setup Failed: " 
					+ e.getLocalizedMessage());
		}		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		for(final Entry<Integer, Long> map : mapRowNoRecNo.entrySet()){
			if(recNoPara == map.getValue().longValue()){
				index = map.getKey();
				break;
			}
			index = Integer.valueOf(0);
		}
		this.panTable.setRowSelect(index.intValue());
		this.log.exiting("MainWindowAdmin", "setupTableDatabase");
	}	
	
	/**
	 * If the access is locally, the method enables to load a new or 
	 * the same database file.
	 * The method argument is the path on the local disk.
	 * The method writes to the file suncertify.properties
	 * by using the instance variable <code>config</code> of
	 * type  <code>suncertify.gui.SavedConfiguration</code>.
	 * 
	 * @param path String representation of the path to the database file.
	 * @throws GuiControllerException Thrown, if the database
	 * file could not be accessed.
	 */
	public void reloadDBLocal(final String path) 
			throws GuiControllerException{
		this.controller.reloadDB(path);
		if(this.config == null){
			this.config = SavedConfiguration.getSavedConfiguration();
		}		
		this.config.setParameter(SavedConfiguration.DATABASE_LOCATION, path);
	}
	
	/**
	 * If the access is remotely, the method enables to reconnect to
	 * the same server or to another server.
	 * The method argument is the ipaddress and port number
	 * to the server. The method assigns a new controller object
	 * of type <code>GuiControllerAdmin</code> to access the server.
	 * It writes to the file suncertify.properties
	 * by using the instance variable <code>config</code> of
	 * type  <code>suncertify.gui.SavedConfiguration</code>.
	 * 
	 * @param ipAdr String representation of the ipaddress of the server.
	 * @param portNr String representation of the port number.
	 * @throws GuiControllerException Thrown, if the server
	 * could not be reached.
	 */
	public void reloadDBRemote(final String ipAdr, final String portNr) 
			throws GuiControllerException{		
		this.controller.closeNetworkConnection();
		this.controller = new GuiControllerAdmin(ApplicationMode.NETWORK_CLIENT,
				this, ipAdr, portNr);
		if(this.config == null){
			this.config = SavedConfiguration.getSavedConfiguration();
		}
		this.config.setParameter(SavedConfiguration.SERVER_PORT, portNr);
		this.config.setParameter(SavedConfiguration.SERVER_IP_ADDRESS, ipAdr);		
		this.tableData = this.controller.getAllRecordsModel();		
		this.setupTableDatabase();
	}
	
	/**
	 * Returns a short introduction and the client ID,
	 * which can be displayed by dialogs.
	 * 
	 * @return String - A short introduction and the client ID.
	 */
	public String getMSG_ID(){
		return this.MSG_ID;
	}	
	
	/**
	 * Enables or disables the add button of the main window.
	 * 
	 * @param enable Enables the add button or not.
	 */
	public void setPanelSellerAddButtonEnabled(final boolean enable){
		this.panUpDel.setPanelUDA_AddButtEnabl(enable);
	}	
	
	/**
	 * Checks whether the add button of the main window is enabled.
	 * 
	 * @return boolean True if the add button of the main window is enabled.
	 */
	public boolean isPanelSellerAddButtEnabled(){
		return this.panUpDel.isPanUpDelAddButtEnabl();
	}	
	
	/**
	 * Calls the method <code>searchForRecords</code> and sets
	 * the entry fields blank.
	 */
	public void searchCriteriaSetPanelSearch(){
		final String[] cri = new String[2];
		cri[0] = this.panSearch.getNameField();
		cri[1] = this.panSearch.getCityField();
		this.searchForRecords(cri);
		this.panSearch.setFieldName("");
		this.panSearch.setFieldCity("");
	}

	/**
	 * Starts a thread to update the content of the table
	 * in time cycles.
	 */
	private void  getUpdates(){
		new GetUpdate().start();
	}	

	/**
	 * Initializes the components.
	 * 
	 * @throws GuiControllerException
	 *             Wraps an <code>java.io.IOException</code>, 
	 *             possibly thrown during 
	 *             constructing an object of type 
	 *             <code>suncertify.gui.admin.GuiControllerAdmin</code>.
	 */
	private void initMainWindow() throws GuiControllerException {
		this.log.entering("MainWindowAdmin", "initMainWindowAdmin");				
		StartMonitor.disposeStartMonitor();
		this.setTitle("Bodgitt & Scarper, Admin, ID: " + this.idOwner + " - " 
		+ this.appType.toString());
		this.panBorderLaySouth = new JPanel();
		final GridBagLayout gl = new GridBagLayout();
		this.panBorderLaySouth.setLayout(gl);
		final GridBagConstraints c = new GridBagConstraints();
		this.panBorderLaySouth.setComponentOrientation(
				ComponentOrientation.LEFT_TO_RIGHT);
		
		// creates the listeners
		final AdminButtonActionListener aLi = new AdminButtonActionListener(this);
		final AdminButtonKeyListener keyListenerAdminButtons = 
				new AdminButtonKeyListener(this);
		
		// Initializes the panels
		this.panSearch = new PanelSearch(keyListenerAdminButtons, 
				new AdminPanSearchFieldsKeyListener(this), aLi);
		this.panUpDel = new PanelUpDelAddSeller(aLi, keyListenerAdminButtons);
		
		this.panBorderLaySouth.add(this.panSearch, c);

		// Rent/Release panel
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = 1;
		// creates the panel, which possesses the 'Rent Record'
		// and 'Release Record' buttons
		this.panRent = new PanelRentBuyer(aLi, keyListenerAdminButtons);
		
		this.panBorderLaySouth.add(this.panRent, c);

		// Read, Update, Delete, Add
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = 2;		
		
		this.panBorderLaySouth.add(this.panUpDel, c);

		// Table
		this.panTable = new PanelTable(
				this.controller.getAllRecordsModel());
		this.panTable.setFocus(true);
		this.panTable.setRowSelect(0);
		this.panTable.setMouseListener(new AdminPanTableMouseListener(
				this.panTable));
		this.panTable.setKeyListener(
				new AdminPanTableKeyListener(
						this, this.panSearch, this.panUpDel, this.panTable));
		this.add(this.panTable, BorderLayout.CENTER);
		
		// adds search panel, rent panel and update panel
		this.add(this.panBorderLaySouth, BorderLayout.SOUTH);		
		this.sNums = "Meaningful Numbers";
		this.statisticNumbers = new JLabel(this.sNums);
		this.statisticNumbers.setToolTipText("Meaningful numbers");
		this.add(this.statisticNumbers, BorderLayout.NORTH);
		
		this.mBar = new MainWindowMenuBarAdmin(
				this
				, this.panTable
				, this.panUpDel
				, this.dbLocation
				, this.portOrLocal
				, this.updateInterval);
		
		this.mBar.setPreferredSize(new Dimension(this.mBar.getSize().width,
				50));
		
		this.setJMenuBar(this.mBar);
		this.pack();
		this.setSize(650, 450);
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((d.getWidth() - this.getWidth()) / 2);
		final int y = (int) ((d.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		this.setupTableDatabase();
		this.panTable.setFocus(true);
		this.panTable.setRequest(true);
		this.setVisible(true);		
		this.log.exiting("MainWindowAdmin", "initMainWindow");
	}

	/**
	 * Returns a short introduction and Record number,
	 * which can be displayed by dialogs.
	 * 
	 * @param recNo - The Record number of the Record, which will be
	 * treated.
	 * 
	 * @return String - A short introduction and Record record number.
	 */
	static String getMsgRecNo(final long recNo){
		return "Record No. " + recNo + "\n";
	}

	/**
	 * Checks, whether the values of the Record have changed.
	 * <br> The return value is created by an 
	 * <code>javax.swing.JOptionPane</code> therefore
	 * the return values of this method are equivalent 
	 * to the pane.
	 * <br> '0' (<code>JOptionPane.YES_OPTION</code>), if the values 
	 * have not changed.
	 * <br> '1'(<code>JOptionPane.NO_OPTION</code>) or 
	 * '-1'(<code>JOptionPane.CLOSED_OPTION</code>), if the process should be discontinued
	 * to changes.
	 * 
	 * 
	 * @param data The values of the graphical surface, which 
	 * represents the old values. 
	 * @param recNo The Record number of the Record, which will be tested.
	 * @param mainW A reference to the main window.
	 * @param deleteRecord The very new Record.
	 * @param titleOpp Set the title of the dialog.
	 * 
	 * @return int - Returns 0, if the values have not changed.
	 * Returns 1 and -1, if the process should be discontinued.
	 */
	private static int checkRecChangedMainWinAdmin(
			final String[] data, 
			final long recNo, 
			final MainWindowAdmin mainW, 
			final Record deleteRecord,
			final String titleOpp) {
		boolean notEqual = false;
		final StringBuilder str = new StringBuilder("THIS RECORD HAS BEEN CHANGED! "
				+ "Please check the entries." 
		+ "\nField:  Old entry  -/-  New entry");
		if (!data[0].equals(deleteRecord.getName())) {
			notEqual = true;
			str.append("\nName: " + data[0] + " -/- " + deleteRecord.getName());
		}
		if (!data[1].equals(deleteRecord.getCity())) {
			notEqual = true;
			str.append("\nCity: " + data[1] + " -/- " + deleteRecord.getCity());
		}
		if (!data[2].equals(deleteRecord.getTypesOfWork())) {
			notEqual = true;
			str.append("\nTypes: " + data[2] + " -/- " 
			+ deleteRecord.getTypesOfWork());
		}
		if (Integer.valueOf(data[3]).intValue() != deleteRecord.getNumberOfStaff()) {
			notEqual = true;
			str.append("\nStaff: " + data[3] + " -/- " 
			+ deleteRecord.getNumberOfStaff());
		}
		if (!data[4].equals(deleteRecord.getHourlyChargeRate())) {
			notEqual = true;
			str.append("\nRate: " + data[4] + " -/- " 
			+ deleteRecord.getHourlyChargeRate());
		}
		if (!data[5].equals(deleteRecord.getOwner())) {
			notEqual = true;
			str.append("\nID Owner: " + data[5] + " -/- " + deleteRecord.getOwner());
		}
	
		if (notEqual) {
			final int n = JOptionPane.showConfirmDialog(null,
					mainW.getMSG_ID() + MainWindowAdmin.getMsgRecNo(recNo) 
						+ new String(str)
						+ "\n\nPlease press (Y) to proceed the operation.",
						titleOpp + " - Changes", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
				return n;
			}
		}
		return 0;
	}	

	/**
	 * The class is a <code>java.lang.Thread</code> and manages the 
	 * update mechanism. It will be started
	 * at construction time and runs to the end of the session.
	 * The method <code>run</code> starts an endless while loop,
	 * which refreshes in a time interval.
	 * 
	 * @author stefan.streifender@gmx.de
	 *
	 */
	private class GetUpdate extends Thread{
		
		/**
		 * Constructs an object of the class.
		 */
		public GetUpdate() {
			// 
		}
	
		/**
		 * Overridden method. Refreshes the content of the 
		 * database file.
		 */
		@Override
		public void run(){
			while(true){
				try {
					Thread.sleep(MainWindowAdmin.this.updateInterval);
				} catch (final InterruptedException e) {
					MainWindowAdmin.this.log.severe("GetUpdate, run, " 
							+ e.getMessage());
				}				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						setupTableDatabase();
					}
				});
			}
		}
	}
}
