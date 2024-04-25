package suncertify.gui.buyer;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
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
import suncertify.gui.MenuCountsNos;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.RecordTableModel;
import suncertify.gui.StartMonitor;
import suncertify.gui.buyer.listeners.BuyerButtonActionListener;
import suncertify.gui.buyer.listeners.BuyerButtonKeyListener;
import suncertify.gui.buyer.listeners.BuyerPanSearchFieldsKeyListener;
import suncertify.gui.buyer.listeners.BuyerPanTableKeyListener;
import suncertify.gui.buyer.listeners.BuyerPanTableMouseListener;

/**
 * An object of the class is a <code>javax.swing.JFrame</code> and
 * represents the main window of a Buyer client.<br>
 * <br>
 * The class will be used independent the database file will 
 * be accessed locally or remotely.<br>
 * <br>
 * The class uses the <code>javax.swing.BorderLayout</code> to fix the graphics.<br>
 * The panel(@see <code>suncertify.gui.PanelTable</code>), which contains the table 
 * is settled by <code>BorderLayout.CENTER</code>.<br>
 * The panel, which contains
 * the search panel(@see <code>suncertify.gui.PanelSearch</code>) and the panel
 * (@see <code>suncertify.gui.buyer.PanelRentBuyer</code>) to rent is settled by
 * <code>BorderLayout.SOUTH</code>.<br>
 * <br> 
 * The class uses an object of type 
 * <code>suncertify.gui.buyer.MainWindowMenuBarBuyer</code>
 * as a menu bar.<br>
 * <br>
 * MNemonics:<br>
 * C - menu 'Connection'<br>
 * E - menu 'Edit'<br>
 * F - menu 'File'<br>
 * H - menu 'Help'<br>
 * N - check box 'Built intersection'<br>
 * L - 'Release Record' button<br>
 * Q - menu 'File' item 'Quite'<br>
 * R - 'Rent Record' button<br>
 * S - 'Search/Refresh Record' button<br>
 * T - menu 'Filter'<br>
 * U - menu 'Counts'<br>
 * 
 * 
 * @see GuiControllerBuyer
 * @see PanelTable
 * @see PanelSearch
 * @see PanelRentBuyer
 * @see MainWindowMenuBarBuyer
 * @author stefan.streifeneder@gmx.de
 */
public class MainWindowBuyer extends JFrame {
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 211L;
	
	/**
	 * Returns s <code>String</code> representation of the
	 * ID, which is used, if there is a displayed message 
	 * by a dialog.
	 */
	private final String MSG_ID;
	
	/**
	 * A variable, which stores the Application mode.
	 */
	private ApplicationMode appType = null;

	/**
	 * The internal reference to the controller object.
	 */
	private final GuiControllerBuyer controller;

	/**
	 * The ID number of this client. 
	 */
	private final int idOwner;

	/**
	 * Stores the ipaddress or the path to the database.
	 * It will be displayed via menu 'Connection'.
	 */
	private String dbLocation = "";

	/**
	 * Stores the port number or just "alone"
	 * by local access. It will be displayed via menu 'Connection'.
	 */
	private String portOrLocal = "";

	/**
	 * The panel, which contains the search panel and the rent panel.
	 */
	private JPanel panBorderLaySouth;

	/**
	 * The panel, which contains the table.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the search panel.
	 */
	private PanelSearch panSearch;

	/**
	 * The menu bar.
	 */
	private MainWindowMenuBarBuyer mBar;

	/**
	 * The internal reference to the table model to display data.
	 */
	private RecordTableModel tableData = new RecordTableModel();

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.MainWindowBuyer</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.buyer.MainWindowBuyer"),
			Level.ALL);

	/**
	 * Stores how long it takes to the next update.
	 */
	long updateInterval = -1;	
	
	/**
	 * Stores statistic numbers.
	 */
	MenuCountsNos nos;
	

	/**
	 * Builds and displays the main application window in local 
	 * or remote mode. The constructor begins  by building the 
	 * connection dialog box, then it constructs an object of 
	 * type <code>suncertify.gui.buyer.GuiControllerBuyer</code> to
	 * access the database file. After the user has made
	 * his connection configurations the constructor makes a call to the method
	 * <code>initMainWindow</code> to initialize the graphical components.
	 * 
	 * @param appMode
	 *            The Application mode.
	 * @param id
	 *            The ID of the client.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public MainWindowBuyer(final ApplicationMode appMode, final int id) 
											throws GuiControllerException{
		this.log.entering("MainWindowBuyer", "MainWindowBuyer", 
				new Object[] { appMode, Integer.valueOf(id) });
		this.idOwner = id;
		this.MSG_ID = "Cient ID " + this.idOwner + "\n\n";
		this.appType = appMode;
		String db = "";
		
		final DatabaseLocationDialog dbLocationLocal = new DatabaseLocationDialog(
				this.appType, this.idOwner);
		
		if(this.appType == ApplicationMode.NETWORK_CLIENT){
			this.dbLocation = dbLocationLocal.getIpAddress();
			this.portOrLocal = dbLocationLocal.getPort();
			db = dbLocationLocal.getIpAddress();
			this.controller = new GuiControllerBuyer(this, this.appType, 
					db, this.portOrLocal);
		} else {
			this.dbLocation = dbLocationLocal.getLocation();
			this.portOrLocal = "alone";
			db = dbLocationLocal.getLocation();
			this.controller = new GuiControllerBuyer(this, this.appType, 
					db, this.portOrLocal, dbLocationLocal.getPassword());
		}
		this.initMainWindow();
		this.log.exiting("MainWindowBuyer", "MainWindowBuyer");
	}
	
	/**
	 * Builds and displays the main application window in remote mode
	 * and refreshes the current content of the database in time
	 * intervals. The first call is to the other overloaded constructor,
	 * afterwards the update functionality will be adjusted.
	 * 
	 * 
	 * @param id The ID owner number.
	 * @param msInterval The time interval of the updates.
	 * @throws GuiControllerException 
	 *             Indicates a database or network level exception.
	 */
	public MainWindowBuyer(final int id, final long msInterval) 
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
		this.log.severe("MainWindowBuyer, closeMainWindow, " + "start");
		try {
			if(this.appType == ApplicationMode.NETWORK_CLIENT){
				this.controller.closeNetworkConnection();
			}			
		} catch (final GuiControllerException e) {
			this.log.severe("MainWindowBuyer, closeMainWindow,"
					+ " Exc: " + e.getLocalizedMessage());				
		}
		this.log.severe("MainWindowBuyer, closeMainWindow, " 
				+ "Applications exits!");		
		System.exit(0);
	}

	/**
	 * Handles the rent operation.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void rentRecord() {
		this.log.entering("MainWindowBuyer", "rentRecord");
		// There won't be assigned a new default Cursor object,
		// because this is done by the method setupTableDatabase().
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
			this.setupTableDatabase();
			return;
		}		
		// check locks
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(this, this.MSG_ID 
							+ getMsgRecNo(recNo) 
							+ "This Record is locked, if you press 'yes', "
									+ "You have to wait til the Record will "
									+ "be unlocked!",
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
			ExceptionDialog.handleException(this.MSG_ID 
					+ e.getLocalizedMessage());
			return;
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ e.getLocalizedMessage());
			return;
		}
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
								"BUYER - RENT - BEFORE LOCK");
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
			ExceptionDialog.handleException(
					this.MSG_ID + getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		try {
			final boolean rentStatus = this.controller.rent(recNo, this.idOwner);
			if (rentStatus == false) {
				JOptionPane.showMessageDialog(this.rootPane, this.MSG_ID 
						+ getMsgRecNo(recNo) + " - The 'ID Owner' field is not "
								+ "blank!");
				this.setupTableDatabase();
				return;
			}
			this.setupTableDatabase();
		} catch (final GuiControllerException gce) {
			this.log.severe("MainWindowBuyer, rentRecord, " + gce.getMessage());
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + gce.getLocalizedMessage());
			return;
		}
		this.log.exiting("MainWindowBuyer", "rentRecord");
	}

	/**
	 * Handles the release operation of a Record of a rented state.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 */
	public void releaseRecord() {
		this.log.entering("MainWindowBuyer", "releaseRecord");
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));		
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
					+ getMsgRecNo(recNo) + e.getLocalizedMessage());
			return;
		}
		
		// Tests the ID of the client to be equal to the entry in the table.
		int ownerInteger = 0;
		if (ownerString != null) {
			if (!ownerString.equals("")) {
				ownerInteger = Integer.parseInt(ownerString);
				if (ownerInteger != this.idOwner) {
					this.log.fine("MainWindow, releaseRecord, Record "
							+ "was (possibly) " + "reservated by an other client.");
					final int answer = JOptionPane.showConfirmDialog(
							null, this.MSG_ID +
							"Your ID " + "is not equal to the entry in 'ID "
									+ "Owner'!\n"
									+ "Do You want to release the Record?",
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
		try {
			if (
				this.controller.checkRecordHasChanged(rec, recNo,
							EnumChangeMode.CHANGE_MODE_END_GOON,
							"BUYER - RELEASE - AFTER ID")
					== EnumChangeMode.CHANGE_RETURN_EXIT_METH) {	
				this.setupTableDatabase();
				ExceptionDialog.handleException(this.MSG_ID 
						+ getMsgRecNo(recNo) + "Release operation is "
						+ "cancled! Meanwhile values of the Record has changed");
				return;
			}
		} catch (final GuiControllerException e1) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e1.getLocalizedMessage());
			return;
		}
		// tests locks on the selected Record
		try {
			for (final Long l : this.controller.getLocks()) {
				if (l.longValue() == recNo) {
					final int n = JOptionPane.showConfirmDialog(
							this, this.MSG_ID + getMsgRecNo(recNo) 
							+ "This Record "
							+ "is locked, if you press 'yes', You have to wait "
							+ "til the Record will be unlocked!",
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
			ExceptionDialog.handleException(this.MSG_ID + getMsgRecNo(recNo) 
				+ e.getLocalizedMessage());
			return;
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID + getMsgRecNo(recNo) 
				+ e.getLocalizedMessage());
			return;
		}
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
								rec, recNo, 
									EnumChangeMode.CHANGE_MODE_END_GOON_CHECK,
									"BUYER - RELEASE - BEFORE LOCK");
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
			this.log.severe("MainWindowBuyer, release, " + gce.getMessage());
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ getMsgRecNo(recNo) + gce.getLocalizedMessage());
			return;
		}
		this.log.exiting("MainWindowBuyer", "releaseRecord");
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
	 * @param criteria
	 *            Filter to search for matching Records.
	 */
	public void searchForRecords(final String[] criteria) {
		this.log.entering("MainWindowBuyer", "searchForRecords");
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		final long index = this.tableData.getRecNo(Integer.valueOf(
				this.panTable.getRowSelected()));
		try {
			this.tableData = this.controller.findRecordByCriteria(
					criteria, this.panSearch.getCheckBoxUnionSelected());			
			final Map<Integer, Long> modelRowRecNo = new HashMap<Integer, Long>();
			modelRowRecNo.putAll(this.tableData.getRowRecNoMap());			
			final List<Integer> colRecs = new ArrayList<Integer>();			
			final Set<Long> locks = new HashSet<Long>();
			locks.addAll(this.controller.getLocks());			
			for (int i = 0; i <= this.tableData.getRowCount(); i++) {
				if (locks.isEmpty()) {
					this.panTable.setRowTabelColor(i, Color.WHITE);
				} else {
					for (final Long l : locks) {
						if (l.equals(modelRowRecNo.get(Integer.valueOf(i)))) {
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
		} catch (final GuiControllerException e) {ExceptionDialog.handleException(
				this.MSG_ID + "Search/Refresh Operation failed!" 
						+ " " + e.getLocalizedMessage());
		}
		try {
			this.nos.setNos(this.controller.getRecordsList().size(), 					
					this.controller.getBooked(), 
					(int) this.controller.getMemory(),
					this.controller.getLocks().size());
			this.mBar.updateCount(this.nos);
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException(this.MSG_ID + e.toString());
		}
		this.panTable.setModelEx(this.tableData);
		if (this.tableData.getRowRecNoMap().values().contains(Long.valueOf(index))) {
			for (final Map.Entry<Integer, Long> map : this.tableData.getRowRecNoMap().entrySet()) {
				if (map.getValue().equals(Long.valueOf(index))) {
					final int id = map.getKey().intValue();
					this.panTable.setRowSelect(id);
				}
			}
		}
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.log.entering("MainWindowBuyer", "searchForRecords");
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
	 * Refreshes the content of the table with a call to the database.
	 * Locked Records will be colored red. The numbers
	 * of the menu 'MenuCount' will be refreshed.
	 */
	void setupTableDatabase() {
		this.log.entering("MainWindowBuyer", "setupTableDatabase");		
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		final int rowSelected = this.panTable.getRowSelected();
		final long recNoPara = this.tableData.getRecNo(Integer.valueOf(rowSelected));
		final int recNoInt = (int) recNoPara;
		Integer index = Integer.valueOf(recNoInt);
		final Map<Integer, Long> mapRowRecNo = new HashMap<Integer, Long>();
		try {			
			this.tableData = this.controller.getAllRecordsModel();
			mapRowRecNo.putAll(this.tableData.getRowRecNoMap());
			this.panTable.setModelEx(this.tableData);
			final List<Integer> colRecs = new ArrayList<Integer>();			
			final Set<Long> locks = new HashSet<Long>();
			locks.addAll(this.controller.getLocks());			
			for (int i = 0; i <= this.tableData.getRowCount(); i++) {
				if (locks.isEmpty()) {
					this.panTable.setRowTabelColor(i, Color.WHITE);
				} else {
					for (final Long l : locks) {
						if (l.equals(mapRowRecNo.get(Integer.valueOf(i)))) {
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
			this.nos.setNos(this.controller.getRecordsList().size(), 					
					this.controller.getBooked(), 
					(int) this.controller.getMemory(),
					this.controller.getLocks().size());
			this.mBar.updateCount(this.nos);	
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException(this.MSG_ID +"Setup Failed: " 
					+ e.getLocalizedMessage());
		}		
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		for (final Entry<Integer, Long> map : mapRowRecNo.entrySet()) {
			if (recNoPara == map.getValue().longValue()) {
				index = map.getKey();
				break;
			}
			index = Integer.valueOf(0);
		}
		this.panTable.setRowSelect(index.intValue());
		this.log.exiting("MainWindowBuyer", "setupTableDatabase");
	}

	/**
	 * Returns a short introduction and the client ID,
	 * which can be displayed by dialogs.
	 * 
	 * @return String - a short introduction and the client ID.
	 */
	String getMSG_ID(){
		return this.MSG_ID;
	}

	/**
	 * Starts a thread to update the content of the table.
	 */
	private void  getUpdates(){
		new GetUpdate().start();
	}

	/**
	 * Initializes the components.
	 * 
	 * @throws GuiControllerException
	 *             Wraps an IOException, possibly thrown during constructing
	 *             an object of type GuiControllerBuyer.
	 */
	private void initMainWindow() throws GuiControllerException {
		this.log.entering("MainWindowBuyer", "initMainWindow");
		StartMonitor.disposeStartMonitor();
		this.setTitle("Bodgitt & Scarper, Buyer, ID: " + this.idOwner + " - " 
				+ this.appType.toString());
		this.panBorderLaySouth = new JPanel();
		final GridBagLayout gridLayout = new GridBagLayout();
		this.panBorderLaySouth.setLayout(gridLayout);
		final GridBagConstraints constraints = new GridBagConstraints();
		this.panBorderLaySouth.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		// creates the listeners
		final BuyerButtonActionListener actionListenerBuyer = 
				new BuyerButtonActionListener(this);
		final BuyerButtonKeyListener bbKliS = new BuyerButtonKeyListener(this);
		
		// initializes the search panel
		this.panSearch = new PanelSearch(bbKliS, 
							new BuyerPanSearchFieldsKeyListener(this), 
								actionListenerBuyer);
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridy = 0;		
		this.panBorderLaySouth.add(this.panSearch, constraints);

		// Rent
		constraints.gridx = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridy = 1;		
		final PanelRentBuyer panRent = new PanelRentBuyer(actionListenerBuyer, bbKliS);
		
		this.panBorderLaySouth.add(panRent, constraints);
		// Table
		this.panTable = new PanelTable(this.controller.getAllRecordsModel());
		this.panTable.setRowSelect(0);
		this.panTable.setMouseListener(new BuyerPanTableMouseListener(this.panTable));
		this.panTable.setKeyListener(new BuyerPanTableKeyListener(this, this.panSearch, this.panTable));
		this.add(this.panTable, BorderLayout.CENTER);
		// Panel with Search, Rent
		this.add(this.panBorderLaySouth, BorderLayout.SOUTH);
		// Menu bar
		this.nos = new MenuCountsNos(
				this.controller.getRecordsList().size(), 					
				this.controller.getBooked(),
				(int) this.controller.getMemory(),
				this.controller.getLocks().size() 
				);	
		this.mBar = new MainWindowMenuBarBuyer(this.nos
				, this
				, this.panTable
				, this.dbLocation
				, this.portOrLocal
				, this.updateInterval);
		this.setJMenuBar(this.mBar);
		this.pack();
		this.setSize(650, 350);
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((d.getWidth() - this.getWidth()) / 2);
		final int y = (int) ((d.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		this.setupTableDatabase();
		this.setVisible(true);
		this.log.exiting("MainWindowBuyer", "initMainWindow");
	}
	
	/**
	 * Returns a short introduction and the Record number,
	 * which can be displayed by dialogs.
	 * 
	 * @param recNo - The Record number of the Record, which will be
	 * treated.
	 * 
	 * @return String - a short introduction and the Record number.
	 */
	static String getMsgRecNo(final long recNo){
		return "Record No. " + recNo + "\n";
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
			// No instance initialization
		}

		/**
		 * Overridden method. Refreshes the content of the 
		 * database file.
		 */
		@Override
		public void run(){
			while(true){
				try {
					Thread.sleep(MainWindowBuyer.this.updateInterval);
				} catch (final InterruptedException e) {
					MainWindowBuyer.this.log.severe("GetUpdate, run, " 
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
