package suncertify.gui.seller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.gui.ApplicationMode;
import suncertify.gui.DatabaseLocationDialog;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.MenuCountsNos;
import suncertify.gui.PanelSearch;
import suncertify.gui.PanelTable;
import suncertify.gui.RecordTableModel;
import suncertify.gui.StartMonitor;
import suncertify.gui.seller.listeners.SellerButtonActionListener;
import suncertify.gui.seller.listeners.SellerButtonKeyListener;
import suncertify.gui.seller.listeners.SellerPanSearchFieldsKeyListener;
import suncertify.gui.seller.listeners.SellerPanTableKeyListener;
import suncertify.gui.seller.listeners.SellerPanTableMouseListener;
import suncertify.gui.seller.listeners.SellerWinAddFieldsKeyListener;
import suncertify.gui.seller.listeners.SellerWinAddWindowListener;
import suncertify.gui.seller.listeners.SellerWinDelWinWindowListener;
import suncertify.gui.seller.listeners.SellerWinUpFieldsKeyListener;
import suncertify.gui.seller.listeners.SellerWinUpWinWindowListener;

/**
 * An object of the class is a <code>javax.swing.JFrame</code> and
 * represents the main window of a Seller client.<br>
 * <br>
 * The class will be used independent the database file will 
 * be accessed locally or remotely.<br>
 * <br>
 * The class uses the <code>javax.swing.BorderLayout</code> to fix the graphics.<br>
 * The panel(@see <code>suncertify.gui.PanelTable</code>), which contains the 
 * table is settled by  <code>BorderLayout.CENTER</code>.<br>
 * The panel, which contains the search panel(@see 
 * <code>suncertify.gui.PanelSearch</code>) and the panel (@see 
 * <code>suncertify.gui.seller.PanelUpDelAddSeller</code>) to read, update, 
 * delete and to add a Record, is settled by <code>BorderLayout.SOUTH</code>.<br> 
 * <br> 
 * The class uses an object of type 
 * <code>suncertify.gui.seller.MainWindowMenuBarSeller</code> as a menu bar.
 * <br>
 * <br>
 * Row coloring:
 * <br> - if an update window will be opened the row gets the color cyan
 * <br> - if a delete window will be opened the row gets the color magenta
 * <br> - if a Record is locked the row will be colored red so far there is no
 * open window to delete or update a Record which  coloration is prioritized
 * <br>
 * <br>
 * MNemonics:<br>
 * A - 'Read Record' button<br>
 * C - menu 'Connection'<br>
 * D - 'Delete Record' button<br>
 * F - menu 'File'<br>
 * H - menu 'Help'<br> 
 * N - check box 'Built intersection'<br>
 * O - 'Add Record' button<br>
 * P - 'Update Record' button<br>
 * Q - menu 'File' item 'Quite'<br>
 * T - menu 'Filter'<br>
 * S - 'Search/Refresh Record' button<br>
 * U - menu 'Counts'<br>
 * X - menu 'Extras'<br>
 * 
 * 
 * @see GuiControllerSeller
 * @see PanelTable
 * @see PanelSearch
 * @see PanelUpDelAddSeller
 * @see MainWindowMenuBarSeller
 * @see SellerUpdateOperation
 * @see SellerDeleteOperation
 * @author stefan.streifeneder@gmx.de
 */
public class MainWindowSeller extends JFrame {
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 222L;
	
	/**
	 * Returns s <code>String</code> representation of the
	 * ID, which is used, is there are messages displayed
	 * by a dialog.
	 */
	String MSG_ID;
	
	/**
	 * A variable, which stores the Application mode.
	 */
	private final ApplicationMode appType;

	/**
	 * The internal reference to the controller object.
	 */
	private final GuiControllerSeller controller;

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
	 * by local access. It will be displayed via menu 'MenuConnection'.
	 */
	private String portOrLocal = "";

	/**
	 * Panel contains the search panel and the
	 * panel, which provides buttons to read, update,
	 * delete and add a Record.
	 */
	private JPanel panBorderLaySouth;

	/**
	 * A panel, which contains a table.
	 */
	private PanelTable panTable;

	/**
	 * A reference to the search panel.
	 */
	private PanelSearch panSearch;

	/**
	 * A panel contains four buttons to read, update, delete and add Records.
	 */
	private PanelUpDelAddSeller panUpDel;

	/**
	 * The menu bar.
	 */
	private MainWindowMenuBarSeller mBar;

	/**
	 * The internal reference to the table model to display data.
	 */
	private RecordTableModel tableData = new RecordTableModel();

	/**
	 * Stores all opened update windows.
	 */
	private final List<Long> recNosUpdate = new ArrayList<Long>();
	
	/**
	 * Stores all opened delete windows.
	 */
	private final List<Long> recNosDelete = new ArrayList<Long>();

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.MainWindowSeller</code>.
	 */
	Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.gui.seller.MainWindowSeller"),
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
	 * Builds and displays the main application window of a 
	 * Seller client in local and remote mode. The constructor 
	 * begins by building the connection via a dialog box. It constructs 
	 * an object of type 
	 * <code>suncertify.gui.seller.GuiControllerSeller</code> to access 
	 * the database file. After the user has made his connection 
	 * configurations this constructor makes a call to the method 
	 * <code>initMainWindow</code> to initialize the graphical
	 * components.
	 * 
	 * @param appMode
	 *            The Application mode.
	 * @param id
	 *            The ID of the client.
	 * @throws GuiControllerException
	 *             Indicates a database or network level exception.
	 */
	public MainWindowSeller(final ApplicationMode appMode, final int id) 
			throws GuiControllerException {
		this.log.entering("MainWindowSeller", "MainWindowSeller", 
				new Object[] { appMode, Integer.valueOf(id) });
		this.idOwner = id;
		this.appType = appMode;
		this.MSG_ID = "Cient ID " + this.idOwner + "\n\n";
		String db = "";		
		final DatabaseLocationDialog dbLocationLocal = new DatabaseLocationDialog(
				this.appType, this.idOwner);
		if(this.appType == ApplicationMode.NETWORK_CLIENT){
			this.dbLocation = dbLocationLocal.getIpAddress();
			this.portOrLocal = dbLocationLocal.getPort();
			db = dbLocationLocal.getIpAddress();
			this.controller = new GuiControllerSeller(this.appType, 
					db, this.portOrLocal, this);
		} else {
			this.dbLocation = dbLocationLocal.getLocation();
			this.portOrLocal = "alone";
			db = dbLocationLocal.getLocation();
			this.controller = new GuiControllerSeller(this.appType, 
					db, this.portOrLocal, this, dbLocationLocal.getPassword());
			
		}
		initMainWindow();
		this.log.exiting("MainWindowSeller", "MainWindowSeller");
	}
	
	
	/**
	 * Builds and displays the main application window in remote mode
	 * and refreshes the current content of the database in time
	 * intervals. The first call is to the other overloaded constructor, 
	 * afterwards the update functionality will be adjusted.
	 * 
	 * @param id The ID owner number.
	 * @param msInterval The time interval of the updates.
	 * @throws GuiControllerException 
	 *             Indicates a database or network level exception.
	 */
	public MainWindowSeller(final int id, final long msInterval) 
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
		this.log.severe("MainWindowSeller, closeMainWindow, " + "start");
		try {
			if(this.appType == ApplicationMode.NETWORK_CLIENT){
				this.controller.closeNetworkConnection();
			}
		} catch (final GuiControllerException e) {
			this.log.severe("MainWindowSeller, closeMainWindow,"
					+ " Exc: " + e.getLocalizedMessage());			
		}
		this.log.severe("MainWindowSeller, closeMainWindow, " 
				+ "Applications exits!");		
		System.exit(0);
	}

	/**
	 * Searches for Records by filter. 
	 * Found Records will be displayed in the table.
	 * Locked Records will be colored red so far there is no
	 * open window to delete or update a Record which  coloration is 
	 * prioritized. The row of a Record, which are selected to be 
	 * updated by opening the update window, will be colored cyan. 
	 * The row of a Record, which are selected to be deleted by 
	 * opening the delete window, will be colored magenta. 
	 * The selected row is set by the Record number. The statistic 
	 * numbers will be set.
	 * 
	 * @param crit
	 *            Filter to search for matching Records.
	 */
	public void searchForRecords(final String[] crit) {
		this.log.entering("MainWindowSeller", "searchForRecords");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		final long index = this.tableData.getRecNo(Integer.valueOf(
				this.panTable.getRowSelected()));
		
		try {
			this.tableData = this.controller.findRecordByCriteria(crit,
						this.panSearch.getCheckBoxUnionSelected());			
			final Map<Integer, Long> modelRowRecNo = 
					this.tableData.getRowRecNoMap();
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
			for (final Map.Entry<Integer, Long> map : modelRowRecNo.entrySet()) {
				for (final Long recNo : this.recNosUpdate) {
					if (recNo.equals(map.getValue())) {
						this.panTable.setRowTabelColor(
								map.getKey().intValue(), Color.CYAN);
					}
				}
			}
			for (final Map.Entry<Integer, Long> map : modelRowRecNo.entrySet()) {
				for (final Long recNo : this.recNosDelete) {
					if (recNo.equals(map.getValue())) {
						this.panTable.setRowTabelColor(
								map.getKey().intValue(), Color.MAGENTA);
					}
				}
			}
		} catch (final GuiControllerException e) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
			ExceptionDialog.handleException(this.MSG_ID 
					+ "Search/Refresh Operation failed!" + " " 
					+ e.getLocalizedMessage());
		}
		try {
			this.nos.setNos(this.controller.getRecordsList().size(), 					
					this.controller.getBooked(), 
					(int) this.controller.getMemory(),
					this.controller.getLocks().size());
			this.mBar.updateCount(this.nos);
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException(this.MSG_ID 
					+ e.toString());
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
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		this.log.entering("MainWindowSeller", "searchForRecords");
	}

	/**
	 * Refreshes the content of the table with a call to the database.
	 * Locked Records will be colored red. The row of a Record, 
	 * which are selected to be updated by opening the update window,
	 * will be colored cyan. The row of a Record, which are selected to 
	 * be deleted by opening the delete window, will be colored magenta. 
	 * The numbers of the menu 'MenuCount' will be refreshed.
	 */
	public void setupTableDatabase() {
		this.log.entering("MainWindowSeller", "setupTableDatabase");
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.panSearch.setCursorFields(Cursor.WAIT_CURSOR);
		final int rowSelected = this.panTable.getRowSelected();
		final long recNoPara = this.tableData.getRecNo(Integer.valueOf(rowSelected));
		final int recNoInt = (int) recNoPara;
		Integer index = Integer.valueOf(recNoInt);
		final Map<Integer, Long> modelRowRecNo = new HashMap<Integer, Long>();
		try {
			this.tableData = this.controller.getAllRecordsModel();
			modelRowRecNo.putAll(this.tableData.getRowRecNoMap());			
			this.panTable.setModelEx(this.tableData);
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
			for (final Map.Entry<Integer, Long> map : modelRowRecNo.entrySet()) {
				for (final Long recNo : this.recNosUpdate) {
					if (recNo.equals(map.getValue())) {
						this.panTable.setRowTabelColor(
								map.getKey().intValue(), Color.CYAN);
					}
				}
			}	
			for (final Map.Entry<Integer, Long> map : modelRowRecNo.entrySet()) {
				for (final Long recNo : this.recNosDelete) {
					if (recNo.equals(map.getValue())) {
						this.panTable.setRowTabelColor(
								map.getKey().intValue(), Color.MAGENTA);
					}
				}
			}
			this.nos.setNos(this.controller.getRecordsList().size(), 					
					this.controller.getBooked(), 
					(int) this.controller.getMemory(),
					this.controller.getLocks().size());
			this.mBar.updateCount(this.nos);
		} catch (final GuiControllerException e) {
			ExceptionDialog.handleException("Set up Failed: " 
					+ e.toString());
		}
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.panSearch.setCursorFields(Cursor.DEFAULT_CURSOR);
		for (final Entry<Integer, Long> map : modelRowRecNo.entrySet()) {
			if (recNoPara == map.getValue().longValue()) {
				index = map.getKey();
				break;
			}
			index = Integer.valueOf(0);
		}
		this.panTable.setRowSelect(index.intValue());
		this.log.exiting("MainWindowSeller", "setupTableDatabase");
	}

	/**
	 * Creates a frame to add a Record to the database file.
	 */
	public void addRec() {
		this.log.entering("MainWindowSeller", "addRec");
		this.setupTableDatabase();
		// creates an add window
		final WindowAddSeller wa = new WindowAddSeller(//data, 
				this.appType, this.idOwner);
		final SellerAddOperation addOpp = new SellerAddOperation(this, wa, this.controller, 
				this.panUpDel);
		wa.setFieldKeyListener(new SellerWinAddFieldsKeyListener(
				this, wa, this.controller, this.panUpDel));
		wa.setWindowAddButtonActionListener(
				new SellerButtonActionListener(this, wa, this.panUpDel,	addOpp));
		wa.setWindowAddButtonKexListener(new SellerButtonKeyListener(this, wa,
				this.panUpDel, addOpp));
		wa.setAddWindowListener(new SellerWinAddWindowListener(this.panUpDel));
		this.log.exiting("MainWindowSeller", "addRec");
	}

	/**
	 * Creates a frame to delete a Record.
	 * <br>	 
	 * If the delete window has opened, the row in the table will be colored
	 * in magenta.
	 */
	public void deleteRec() {
		this.log.entering("MainWindowSeller", "deleteRec");
		this.setupTableDatabase();
		long recNo = 0;
		try {
			recNo = this.panTable.getRecNo();
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(e.getLocalizedMessage());
			return;
		}		
		final List<Long> allRecNos = new ArrayList<Long>();
		allRecNos.addAll(this.recNosDelete);
		allRecNos.addAll(this.recNosUpdate);
		if(allRecNos.contains(Long.valueOf(recNo))){
			ExceptionDialog.handleException(this.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) + "A WINDOW "
					+ "WITH THIS RECORD NUMBER IS OPENED ALREADY!");
			this.setupTableDatabase();
			return;
		}		
		
		final String[] data = new String[Record.RECORD_FIELDS_WITHOUT_FLAG_PLUS_RECNO];
		for (int i = 0; i < Record.RECORD_FIELDS_WITHOUT_FLAG; i++) {
			try {
				data[i] = this.panTable.getRecord()[i];
			} catch (final GuiControllerException e) {
				this.setupTableDatabase();
				ExceptionDialog.handleException(this.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) + e.getLocalizedMessage());
				return;
			}
		}
		data[6] = Long.valueOf(recNo).toString();
		Record newRecord = null;
		try {
			newRecord = this.controller.getRecord(recNo);
			data[0] = newRecord.getName();
			data[1] = newRecord.getCity();
			data[2] = newRecord.getTypesOfWork();
			data[3] = Integer.valueOf(newRecord.getNumberOfStaff()).toString();
			data[4] = newRecord.getHourlyChargeRate();
			data[5] = newRecord.getOwner();

			// creates a delete window
			final WindowDeleteSeller wd = new WindowDeleteSeller(data, 
					this.appType, this.idOwner);
			final SellerDeleteOperation delOpp = new SellerDeleteOperation(wd, 
					this.controller, data);
			wd.setThisDelWindowListener(new SellerWinDelWinWindowListener(this, 
					wd, recNo));
			wd.setWindowDeleteActionListener(new SellerButtonActionListener(this, 
					recNo, wd, delOpp));
			wd.setWindowDeleteKeyListener(new SellerButtonKeyListener(this, 
					recNo, wd, delOpp));
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) 
					+ e.getLocalizedMessage());
		}
		this.recNosDelete.add(Long.valueOf(recNo));	
		this.setupTableDatabase();
		this.log.exiting("MainWindowSeller", "deleteRec");
	}

	/**
	 * Opens a frame to update a Record. <br> 
	 * If the update window has opened, the row in the table will be colored
	 * in cyan. 
	 */
	public void updateRec() {
		this.log.entering("MainWindowSeller", "updateRec");
		this.setupTableDatabase();
		long recNo = 0;
		try {
			recNo = this.panTable.getRecNo();
		} catch (final GuiControllerException e) {
			this.setupTableDatabase();
			ExceptionDialog.handleException(this.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) 
					+ e.getLocalizedMessage());
			return;
		}
		final List<Long> allRecNos = new ArrayList<Long>();
		allRecNos.addAll(this.recNosDelete);
		allRecNos.addAll(this.recNosUpdate);
		if (allRecNos.contains(Long.valueOf(recNo))) {
			ExceptionDialog.handleException(this.MSG_ID 
					+ MainWindowSeller.getMsgRecNo(recNo) + "A WINDOW " 
					+ "WITH THIS RECORD NUMBER IS OPENED ALREADY!");
			this.setupTableDatabase();
			return;
		}
		final String[] data = 
				new String[Record.RECORD_FIELDS_WITHOUT_FLAG_PLUS_RECNO];
		for (int i = 0; i < Record.RECORD_FIELDS_WITHOUT_FLAG; i++) {
			try {
				data[i] = this.panTable.getRecord()[i];
			} catch (final GuiControllerException e) {
				this.setupTableDatabase();
				ExceptionDialog.handleException(this.MSG_ID 
						+ MainWindowSeller.getMsgRecNo(recNo) 
						+ e.getLocalizedMessage());
				return;
			}
		}
		data[6] = Long.valueOf(recNo).toString();
		// creates an update window
		final WindowUpdateSeller wu = new WindowUpdateSeller(data, this.appType, 
				this.idOwner);
		final SellerUpdateOperation upOpp = 
				new SellerUpdateOperation(this.controller, data, wu);
		// adds a key listener to the entry fields for the values
		// of the Record, which should be updated
		wu.setFieldKeyListener(
				new SellerWinUpFieldsKeyListener(this, recNo, wu, 
						this.controller, data));
		// adds a window listener to the update window
		wu.setThisUpWindowListener(
				new SellerWinUpWinWindowListener(this, recNo, wu, this.controller));
		wu.setWindowUpdateActionListener(new SellerButtonActionListener(this, 
				recNo, wu, this.panUpDel, upOpp));
		wu.setWindowUpdateKeyListener(new SellerButtonKeyListener(this, recNo,
				wu, this.panUpDel, upOpp));
		this.recNosUpdate.add(Long.valueOf(recNo));
		this.setupTableDatabase();
		this.log.exiting("MainWindowSeller", "updateRec");
	}

	/**
	 * Opens a dialog to choose a Record by Record number. The selected Record
	 * will be displayed in the table. 
	 * 
	 */
	public void readRec() {
		List<Record> list = new ArrayList<Record>();
		try {
			list = this.controller.getRecordsList();
		} catch (final GuiControllerException ex) {
			ExceptionDialog.handleException(this.MSG_ID 
					+ "Read Record failed!" + ex.getLocalizedMessage());
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
		final Long input = (Long) JOptionPane.showInputDialog(dialog, this.MSG_ID 
				+ "You can see all Record numbers below. "
				+ "\nChoose one record number the Record "
				+ "\nwill be displayed.",
				"Client-No: " + this.idOwner + " - " + this.appType.name(), 
				JOptionPane.PLAIN_MESSAGE, null, o,
				o[0].toString());
		if (input != null) {
			try {
				//row coloring
				this.panTable.setRowTabelColor(0, Color.WHITE);
				if(this.controller.getLocks().contains(input)){
					this.panTable.setRowTabelColor(0, Color.RED);
				}
				if(this.recNosDelete.contains(input)){
					this.panTable.setRowTabelColor(0, Color.MAGENTA);
				}
				if(this.recNosUpdate.contains(input)){
					this.panTable.setRowTabelColor(0, Color.CYAN);
				}
				// assigning the data
				this.tableData = this.controller.findRecord(input.longValue());
				this.panTable.setModelEx(this.tableData);
				this.panTable.setRowSelect(0);
			} catch (final GuiControllerException ex) {
				ExceptionDialog.handleException(this.getMSG_ID()
						+ "To read Record (" + input.longValue() 
						+ ") failed!\n" + ex.getMessage());
				this.log.severe("MainWindowSeller, readRec, Exception: " 
						+ ex.getLocalizedMessage());
				return;
			}
		} else {
			dialog.dispose();
		}
	}
	
	/**
	 * Removes the Record number of the list, which stores all
	 * opened update windows.
	 * 
	 * 
	 * @param recNo The Record number of the Record,
	 * which should be updated.
	 */
	public void removeOfUpList(final long recNo){
		this.recNosUpdate.remove(Long.valueOf(recNo));
	}
	
	/**
	 * Removes the Record number of the list, which stores all
	 * opened delete windows.
	 * 
	 * @param recNo The Record number of the Record,
	 * which should be deleted.
	 */
	public void removeOfDelList(final long recNo){
		this.recNosDelete.remove(Long.valueOf(recNo));
	}
	
	/**
	 * Sets the add button of the main window enabled or not.
	 * 
	 * @param enable Enables the add button or not.
	 */
	public void setPanelSellerEnabled(final boolean enable){
		this.panUpDel.setPanelUDA_AddButtEnabl(enable);
	}
	
	/**
	 * Checks whether the add button of the main window is enabled or not.
	 * 
	 * @return boolean True if the add button of the main window, which actually to
	 * a panel of a Seller client is enabled.
	 */
	public boolean isPanelSellerEnabled(){
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
	 * Initializes the graphical components.
	 * 
	 * @throws GuiControllerException
	 *             Wraps an <code>java.io.IOException</code>, possibly thrown 
	 *             during constructing an object of type 
	 *             <code>suncertify.gui.seller.GuiControllerSeller</code>.
	 */
	private void initMainWindow() throws GuiControllerException {
		this.log.entering("MainWindowSeller", "initMainWindow");
		StartMonitor.disposeStartMonitor();
		this.setTitle("Bodgitt & Scarper, Seller, ID: " + this.idOwner + " - " 
				+ this.appType.toString());
		this.panBorderLaySouth = new JPanel();
		final GridBagLayout gl = new GridBagLayout();
		this.panBorderLaySouth.setLayout(gl);
		final GridBagConstraints c = new GridBagConstraints();
		this.panBorderLaySouth.setComponentOrientation(
				ComponentOrientation.LEFT_TO_RIGHT);

		//creates the listeners
		final SellerButtonActionListener aL = new SellerButtonActionListener(this);
		final SellerButtonKeyListener vdKlS = new SellerButtonKeyListener(this);
		
		// initializes panels
		this.panSearch = new PanelSearch(vdKlS, 
				new SellerPanSearchFieldsKeyListener(this), aL);
		this.panUpDel = new PanelUpDelAddSeller(aL, vdKlS);
		
		// Search
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = 0;
		
		this.panBorderLaySouth.add(this.panSearch, c);

		// Read, update, delete, add panel
		c.gridx = 0;
		c.gridy = 1;// 2 row
		c.insets = new Insets(0, 0, 10, 0);// padding to button

		this.panBorderLaySouth.add(this.panUpDel, c);

		// Table
		this.panTable = new PanelTable(this.controller.getAllRecordsModel());
		this.panTable.setRowSelect(0);
		// adds a mouse listener to the table to display of the
		// Record number of a double clicked row
		this.panTable.setMouseListener(new SellerPanTableMouseListener(
				this.panTable));
		this.panTable.setKeyListener(new SellerPanTableKeyListener(
				this, this.panSearch, this.panUpDel, this.panTable));
		// adds table to the main window
		this.add(this.panTable, BorderLayout.CENTER);
		// adds panel, which enables to read, update, delete and add a Record
		// to the main window
		this.add(this.panBorderLaySouth, BorderLayout.SOUTH);
		this.nos = new MenuCountsNos(this.controller.getRecordsList().size(), 					
				this.controller.getBooked(), 
				(int) this.controller.getMemory(),
				this.controller.getLocks().size());
		this.mBar = new MainWindowMenuBarSeller(
				this.nos
				, this
				, this.panTable
				, this.panUpDel
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
		setupTableDatabase();
		this.setVisible(true);
		this.log.exiting("MainWindowSeller", "initMainWindow");
	}
	
	/**
	 * Returns a short introduction and the Record number,
	 * which can be displayed by dialogs.
	 * 
	 * @param recNo - The Record number of the Record, which will be
	 * displayed.
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
					Thread.sleep(MainWindowSeller.this.updateInterval);
				} catch (final InterruptedException e) {
					MainWindowSeller.this.log.severe("GetUpdate, run, " 
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
