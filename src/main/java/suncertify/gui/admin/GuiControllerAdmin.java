package suncertify.gui.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.DuplicateKeyException;
import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.db.RecordDatabase;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.ApplicationMode;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.GuiControllerException;
import suncertify.gui.RecordTableModel;
import suncertify.sockets.admin.SocketClient_Admin;

/**
 * Handles all interactions of an Admin client. This class 
 * calls the connecting methods of
 * <code>suncertify.direct.admin.RecordConnector_Admin</code>,
 * which provides local access or
 * <code>suncertify.sockets.admin.SocketConnector_Admin</code>,
 * which provides remote access.
 * <br>
 * <br>
 * The class provides access to all of the Records in the system and
 * all the operations that act upon the Records. 
 * <br>
 * <br> Locking system
 * <br> An Admin client is able to hold a lock on a Record. If an Admin
 * client proceeds an update or delete operation, the lock of the 
 * Record will be acquired in the moment he opens either an update
 * window or a delete window. He keeps the lock as long one of the
 * windows stays open. The lock is released, if the update window 
 * or delete window was closed or the Record was deleted.
 * <br> The data access system of this application does not prevent 
 * clients to lock more than one Record at the same time on database
 * layer level (deep end/backbone). Therefore there has to be
 * a mechanism at the client side to avoid more than one lock at the
 * same time concerning deadlock issues and due to the special
 * lock implementation of an Admin client. This mechanism is placed
 * in the method <code>lockRecord</code> of this class.
 * 
 * 
 * @see InterfaceClient_Admin
 * @see GuiControllerException
 * @see RecordTableModel
 * @see MainWindowAdmin
 * @author stefan.streifeneder@gmx.de
 */
public class GuiControllerAdmin {
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.admin.GuiControllerAdmin</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.admin.GuiControllerAdmin"),
			Level.ALL);

	/**
	 * A reference to the main window.
	 */
	private final MainWindowAdmin mainW;

	/**
	 * The public interface with all functionalities.
	 */
	private InterfaceClient_Admin connection;

	/**
	 * Stores whether there is already a locked Record or not.
	 */
	private boolean isThereALockedRecord = false;
	
	/**
	 * Constructs an object, which can connect to the database in two ways:
	 * local or over a network by ipaddress and port. The object is used
	 * to process commands made by the graphical mask.<br>
	 * <code>suncertify.gui.GuiControllerException</code> catches an 
	 * <code>java.io.IOException</code>.
	 * 
	 * @param mw
	 *            A reference to the main window of the Admin client.
	 * @param appMode
	 *            - Indicates local or network connection.
	 * @param dbLocation
	 *            The path to the database file or the ipaddress of the server.
	 * @param port
	 *            The port of the server.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerAdmin(final ApplicationMode appMode, 
			final MainWindowAdmin mw, final String dbLocation, 
			final String port) throws GuiControllerException{
		this.mainW = mw;
		try {
			if(appMode == ApplicationMode.LOCAL_CLIENT){
//				this.connection = 
//						suncertify.direct.admin.DirectConnector_Admin.getLocal(
//								dbLocation);
			}else if(appMode == ApplicationMode.NETWORK_CLIENT){
				this.connection = 
						suncertify.sockets.admin.SocketConnector_Admin.getRemote(
								dbLocation, port);
			}
		}catch(final IOException e){
			throw new GuiControllerException(e);
		}
	}
	
	/**
	 * Constructs an object, which can connect to the database in two ways:
	 * local or over a network by ipaddress and port. The object is used
	 * to process commands made by the graphical mask.<br>
	 * <code>suncertify.gui.GuiControllerException</code> catches an 
	 * <code>java.io.IOException</code>.
	 * 
	 * @param mw
	 *            A reference to the main window of the Admin client.
	 * @param appMode
	 *            - Indicates local or network connection.
	 * @param dbLocation
	 *            The path to the database file or the ipaddress of the server.
	 * @param port
	 *            The port of the server.
	 * @param password The password of the database.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerAdmin(final ApplicationMode appMode, 
			final MainWindowAdmin mw, final String dbLocation, 
			final String port, final String password) throws GuiControllerException{
		this.mainW = mw;
		try {
			if(appMode == ApplicationMode.LOCAL_CLIENT){
				this.connection =
						suncertify.direct.admin.DirectConnector_Admin.getLocal(
								dbLocation, password);
			}else if(appMode == ApplicationMode.NETWORK_CLIENT){
				this.connection = 
						suncertify.sockets.admin.SocketConnector_Admin.getRemote(
								dbLocation, port);
			}
		}catch(final IOException e){
			throw new GuiControllerException(e);
		}
	}

	/**
	 * Locates a Record by Record number and returns a table model object
	 * to display the found Record.
	 *
	 * @param recNo
	 *            Representing the Record number identifying the Record.
	 * @return RecordTableModel - A model object for the table to display 
	 * the result.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public RecordTableModel findRecord(final long recNo) 
			throws GuiControllerException {
		this.log.entering("GuiControllerAdmin", "findRecord", Long.valueOf(recNo));
		final RecordTableModel out = new RecordTableModel();
		try {
			out.addRecord(this.connection.getRecord(recNo));
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, findRecord," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerAdmin, findRecord," + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.info("GuiControllerAdmin, findRecord, Record number: " + recNo);
		this.log.exiting("GuiControllerAdmin", "findRecord", out);
		return out;
	}

	/**
	 * Returns a table model, which contains all Records
	 * found by given parameters. The method argument <code>interSection</code> 
	 * has to be true to built an union.
	 * 
	 * 
	 * @param searchString
	 *            The Parameters based on Records are searched for.
	 * @param isUnion
	 *            True, if an union is build.
	 * @return RecordTableModel - The Table Model for the 
	 * <code>javax.swing.JTable</code> to display the Records.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public RecordTableModel findRecordByCriteria(final String[] searchString, 
			final boolean isUnion) throws GuiControllerException {
		this.log.entering("GuiControllerAdmin", "findRecordByCriteria",
				searchString);
		final RecordTableModel out = new RecordTableModel();
		try {
			String t = "";
			for (int i = 0; i < searchString.length; i++) {
				t += searchString[i].trim();
			}
			if (t.equals("")) {
				for(Record record : 
					this.connection.getAllValidRecords()){
					out.addRecord(record);
				}
				return out;
			}			
			final Set<Long> setRecNos = new HashSet<Long>();
			if(!isUnion){
				final long[] resInter = this.connection.findByFilter(searchString);
				for(int i = 0; i < resInter.length; i++){
					try {
						out.addRecord(this.connection.getRecord(resInter[i]));
					} catch (final RecordNotFoundException e) {
						this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
								+ "criteria is null - " 
								+ e.getMessage());
						throw new GuiControllerException(e);
					}
				}
			}else{
				switch (searchString.length) {
				case 1:
					final long[] resName = this.connection.findByFilter(new String[]{searchString[0]});					
					for(int i = 0; i < resName.length; i++){
						try {							
							out.addRecord(this.connection.getRecord(resName[i]));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 1 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				case 2:
					if(!searchString[0].equals("")) {
						final long[] resCityName = this.connection.findByFilter(
								new String[]{searchString[0]});
						
						for(int i = 0; i < resCityName.length; i++){						
							setRecNos.add(Long.valueOf(resCityName[i]));
						}
					}
						
					if(!searchString[1].equals("")) {
						final long[] resCityCity = this.connection.findByFilter(
								new String[]{"", searchString[1]});
						
						for(int i = 0; i < resCityCity.length; i++){						
							setRecNos.add(Long.valueOf(resCityCity[i]));
						}
					}
					final List<Long> li = new ArrayList<Long>();
					li.addAll(setRecNos);
					Collections.sort(li);
					for(final Long recNo : li){
						try {
							out.addRecord(this.connection.getRecord(recNo.longValue()));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 2 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				case 3:
					if(!searchString[0].equals("")) {
						final long[] resCityName = this.connection.findByFilter(
								new String[]{searchString[0]});
						
						for(int i = 0; i < resCityName.length; i++){						
							setRecNos.add(Long.valueOf(resCityName[i]));
						}
					}
						
					if(!searchString[1].equals("")) {
						final long[] resCityCity = this.connection.findByFilter(
								new String[]{"", searchString[1]});
						
						for(int i = 0; i < resCityCity.length; i++){						
							setRecNos.add(Long.valueOf(resCityCity[i]));
						}
					}
					if(!searchString[2].equals("")) {
						final long[] resTypesTypes = this.connection.findByFilter(
								new String[]{"", "", searchString[2]});
						for(int i = 0; i < resTypesTypes.length; i++){						
							setRecNos.add(Long.valueOf(resTypesTypes[i]));
						}
					}
					final List<Long> liTypes = new ArrayList<Long>();
					liTypes.addAll(setRecNos);
					Collections.sort(liTypes);
					for(final Long recNo : liTypes){
						try {
							out.addRecord(this.connection.getRecord(recNo.longValue()));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 3 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				case 4:
					if(!searchString[0].equals("")) {
						final long[] resCityName = this.connection.findByFilter(
								new String[]{searchString[0]});
						
						for(int i = 0; i < resCityName.length; i++){						
							setRecNos.add(Long.valueOf(resCityName[i]));
						}
					}						
					if(!searchString[1].equals("")) {
						final long[] resCityCity = this.connection.findByFilter(
								new String[]{"", searchString[1]});
						
						for(int i = 0; i < resCityCity.length; i++){						
							setRecNos.add(Long.valueOf(resCityCity[i]));
						}
					}
					if(!searchString[2].equals("")) {
						final long[] resTypesTypes = this.connection.findByFilter(
								new String[]{"", "", searchString[2]});
						for(int i = 0; i < resTypesTypes.length; i++){						
							setRecNos.add(Long.valueOf(resTypesTypes[i]));
						}
					}
					if(!searchString[3].equals("")) {
						final long[] resStaffStaff = this.connection.findByFilter(
								new String[]{"", "", "", searchString[3]});
						for(int i = 0; i < resStaffStaff.length; i++){						
							setRecNos.add(Long.valueOf(resStaffStaff[i]));
						}	
					}
					final List<Long> liStaff = new ArrayList<Long>();
					liStaff.addAll(setRecNos);
					Collections.sort(liStaff);
					for(final Long recNo : liStaff){
						try {
							out.addRecord(this.connection.getRecord(recNo.longValue()));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 4 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				case 5:
					if(!searchString[0].equals("")) {
						final long[] resCityName = this.connection.findByFilter(
								new String[]{searchString[0]});
						
						for(int i = 0; i < resCityName.length; i++){						
							setRecNos.add(Long.valueOf(resCityName[i]));
						}
					}						
					if(!searchString[1].equals("")) {
						final long[] resCityCity = this.connection.findByFilter(
								new String[]{"", searchString[1]});
						
						for(int i = 0; i < resCityCity.length; i++){						
							setRecNos.add(Long.valueOf(resCityCity[i]));
						}
					}
					if(!searchString[2].equals("")) {
						final long[] resTypesTypes = this.connection.findByFilter(
								new String[]{"", "", searchString[2]});
						for(int i = 0; i < resTypesTypes.length; i++){						
							setRecNos.add(Long.valueOf(resTypesTypes[i]));
						}
					}
					if(!searchString[3].equals("")) {
						final long[] resStaffStaff = this.connection.findByFilter(
								new String[]{"", "", "", searchString[3]});
						for(int i = 0; i < resStaffStaff.length; i++){						
							setRecNos.add(Long.valueOf(resStaffStaff[i]));
						}	
					}
					if(!searchString[4].equals("")) {
						final long[] resRateRate = this.connection.findByFilter(
								new String[]{"", "", "", "",
								searchString[4]});
						for(int i = 0; i < resRateRate.length; i++){						
							setRecNos.add(Long.valueOf(resRateRate[i]));
						}	
					}
					final List<Long> liRate = new ArrayList<Long>();
					liRate.addAll(setRecNos);
					Collections.sort(liRate);
					for(final Long recNo : liRate){
						try {
							out.addRecord(this.connection.getRecord(recNo.longValue()));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 5 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				case 6:
					if(!searchString[0].equals("")) {
						final long[] resCityName = this.connection.findByFilter(
								new String[]{searchString[0]});
						
						for(int i = 0; i < resCityName.length; i++){						
							setRecNos.add(Long.valueOf(resCityName[i]));
						}
					}						
					if(!searchString[1].equals("")) {
						final long[] resCityCity = this.connection.findByFilter(
								new String[]{"", searchString[1]});
						
						for(int i = 0; i < resCityCity.length; i++){						
							setRecNos.add(Long.valueOf(resCityCity[i]));
						}
					}
					if(!searchString[2].equals("")) {
						final long[] resTypesTypes = this.connection.findByFilter(
								new String[]{"", "", searchString[2]});
						for(int i = 0; i < resTypesTypes.length; i++){						
							setRecNos.add(Long.valueOf(resTypesTypes[i]));
						}
					}
					if(!searchString[3].equals("")) {
						final long[] resStaffStaff = this.connection.findByFilter(
								new String[]{"", "", "", searchString[3]});
						for(int i = 0; i < resStaffStaff.length; i++){						
							setRecNos.add(Long.valueOf(resStaffStaff[i]));
						}	
					}
					if(!searchString[4].equals("")) {
						final long[] resRateRate = this.connection.findByFilter(
								new String[]{"", "", "", "",
								searchString[4]});
						for(int i = 0; i < resRateRate.length; i++){						
							setRecNos.add(Long.valueOf(resRateRate[i]));
						}	
					}
					if(!searchString[5].equals("")) {
						final long[] resOwnerOwner = this.connection.findByFilter(
								new String[]{
								"", "", "", "", "", searchString[5]});
						for(int i = 0; i < resOwnerOwner.length; i++){						
							setRecNos.add(Long.valueOf(resOwnerOwner[i]));
						}	
					}
					final List<Long> liOwner = new ArrayList<Long>();
					liOwner.addAll(setRecNos);
					Collections.sort(liOwner);
					for(final Long recNo : liOwner){
						try {
							out.addRecord(this.connection.getRecord(recNo.longValue()));
						} catch (final RecordNotFoundException e) {
							this.log.severe("GuiControllerAdmin, findRecordByCriteria, "
									+ "criteria 6 - " 
									+ e.getMessage());
							throw new GuiControllerException(e);
						}
					}
					break;
				default:
					//
					break;
				}
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, findRecordByCriteria, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.info("GuiControllerAdmin, findRecordByCriteria, count: " + out.getRowCount());
		this.log.entering("GuiControllerAdmin", "findRecordByCriteria", out);
		return out;
	}

	/**
	 * Retrieves all valid Records from the database. Returns a table model 
	 * to display the result.
	 * 
	 * @return RecordTableModel - A table model containing all Records.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> 
	 *             or <code>java.io.IOException</code>.
	 */
	public RecordTableModel getAllRecordsModel() throws GuiControllerException {
		this.log.entering("GuiControllerAdmin", "getRecordsModel");
		final RecordTableModel out = new RecordTableModel();
		try {
			for(Record record : 
					this.connection.getAllValidRecords()){
				out.addRecord(record);
				
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, getRecordsModel, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.entering("GuiControllerAdmin", "getRecordsModel", out);
		return out;
	}

	/**
	 * Locks a Record to update, to delete, to rent and to release a Record
	 * of rented state.
	 * <br> Returns a cookie, which has to be used to proceed the mentioned
	 * operations and to unlock the Record.
	 * <br> The method cares, that only one lock can be created.
	 * 
	 * @param recNo
	 *            The Record number.
	 * @return long - The lock cookie.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public long lockRecord(final long recNo) throws GuiControllerException {
		if(this.isThereALockedRecord){
			throw new GuiControllerException(new SecurityException("You are NOT"
					+ " allowed " 
					+ "to lock more than ONE Record!"));
		}
		long cookie;
		try {
			cookie = this.connection.setRecordLocked(recNo);
			this.isThereALockedRecord = true;
		} catch (final IOException e) {			
			this.log.severe("GuiControllerAdmin, lockRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerAdmin, lockRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, lockRecord, " + "Record number: " + recNo + " - cookie: " + cookie);
		return cookie;
	}

	/**
	 * Returns a Record object according to the Record number.
	 * 
	 * @param recNo
	 *            The Record number of the searched Record.
	 * @return Record - A Record searched by the given Record number.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public Record getRecord(final long recNo) throws GuiControllerException {
		Record rec;
		try {
			rec = this.connection.getRecord(recNo);
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, getRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerAdmin, getRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, getRecord, Record number: " + recNo);
		return rec;
	}

	/**
	 * Returns a list with all valid Records. 
	 * 
	 * @return List - A list with all Records.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public List<Record> getRecordsList() throws GuiControllerException {		
		final List<Record> list = new ArrayList<Record>();
		try {
			list.addAll(this.connection.getAllValidRecords());
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, getRecordsList, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, getRecordsList, count: " + list.size());
		return list;
	}

	/**
	 * Reserves a Record of a home improving contractor.
	 * <br>
	 * The method acquires the lock of the Record and releases the lock automatically.<br>
	 * If the Record was locked of another client and the currently executing
	 * client returns of a waiting state and gets the lock of the Record, may be
	 * the former client changed the values of the Record, then the method
	 * shows the changes in a dialog and asks to continue or to break up the
	 * procedure.
	 * 
	 * @param recNo
	 *            The Record number for identifying.
	 * @param idOwner
	 *            The ID of an owner.
	 * @return boolean - Indicates whether the operation has succeeded.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public boolean rent(final long recNo, final int idOwner) throws GuiControllerException {
		this.log.entering("GuiControllerAdmin", "rent", 
				new Object[] { Long.valueOf(recNo), Integer.valueOf(idOwner) });
		boolean returnValue = false;
		long cookie;
		Record rec;
		try {
			// reads the Record to check, whether the Record does exist
			rec = this.connection.getRecord(recNo);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		cookie = this.lockRecord(recNo);
		if(
			this.checkRecordHasChanged(rec, recNo, EnumChangeMode.CHANGE_MODE_END_GOON,
					"ADMIN - RENT - YOU GET LOCK")
								== EnumChangeMode.CHANGE_RETURN_EXIT_METH){
			this.setUnlocked(recNo, cookie);
			this.mainW.setupTableDatabase();
			throw new GuiControllerException(this.mainW.getMSG_ID() 
							+ "You have canceled the rent operation " 
							+ "due to changings! ");
		}
		try {
			returnValue = this.connection.reserveRecord(recNo, idOwner, cookie);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final SecurityException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final IllegalArgumentException ex) {
			this.log.severe("GuiControllerAdmin, rent, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}finally{
			this.setUnlockedAndUpdate(recNo, cookie);
			this.log.info("GuiControllerAdmin, rent, Record number: " 
						+ recNo + " ok: " + returnValue);
		}
		this.log.entering("GuiControllerAdmin", "rent", Boolean.valueOf(returnValue));
		return returnValue;
	}

	/**
	 * Releases the Record out of the rented state.
	 * If the Record was locked of another client and the currently executing
	 * client returns of a waiting state and get the lock of the Record, may be
	 * the former client changed the values of the Record, then the application
	 * shows the changes in a dialog and asks to continue or to break up the
	 * procedure. 
	 * <br> The method acquires the lock of the Record and releases the lock.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return boolean - Indicates whether the operation has succeeded.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>, 
	 *             <code>java.io.IOException</code> or <code>java.lang.SecurityException</code>.
	 */
	public boolean returnRental(final long recNo) throws GuiControllerException {
		this.log.entering("GuiControllerAdmin", "returnRental", Long.valueOf(recNo));
		boolean returnValue = false;
		long cookie;
		Record recOld;
		try {
			recOld = this.connection.getRecord(recNo);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, returnRental, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerAdmin, returnRental, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		
		cookie = this.lockRecord(recNo);		
		
		if(
			this.checkRecordHasChanged(recOld, recNo, 
						EnumChangeMode.CHANGE_MODE_END_GOON,
						"ADMIN - RELEASE - YOU GET LOCK")
				== EnumChangeMode.CHANGE_RETURN_EXIT_METH){
			
			this.setUnlocked(recNo, cookie);
			throw new GuiControllerException(this.mainW.getMSG_ID() 
						+ "You have canceled the release operation " 
							+ "due to changings! ");
		}
		try {
			returnValue = this.connection.releaseRecord(recNo, cookie);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, returnRental, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerAdmin, returnRental, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final SecurityException ex) {
			this.log.severe("GuiControllerAdmin, returnRental, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}finally{
			this.setUnlockedAndUpdate(recNo, cookie);
			this.log.info("GuiControllerAdmin, returnRental, Record number: " 
					+ recNo + " ok: " + returnValue);
		}
		this.log.entering("GuiControllerAdmin", "returnRental", Boolean.valueOf(returnValue));
		return returnValue;
	}

	/**
	 * Unlocks a Record.
	 * 
	 * @param recNo
	 *            The Record number of the Record to unlock.
	 * @param cookie
	 *            The cookie, which is created during the lock acquisition of
	 *            the Record.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>, 
	 *             <code>java.io.IOException</code> or 
	 *             <code>java.lang.SecurityException</code>.
	 */
	public void unlockRecord(final long recNo, final long cookie) 
										throws GuiControllerException {
		try {
			this.connection.setRecordUnlocked(recNo, cookie);
		} catch (final SecurityException e) {
			this.log.severe("GuiControllerAdmin, unlockRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, unlockRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerAdmin, unlockRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		}
		this.isThereALockedRecord = false;
		this.log.info("GuiControllerAdmin, unlockRecord, " 
		+ "Record number: " + recNo + " - cookie: " + cookie);
	}

	/**
	 * Returns the Record numbers of all locked Records.
	 * 
	 * @return Set - A set, which reference type is <code>java.lang.Long</code> 
	 * according to primitive type of long, which is the type of the Record number.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public Set<Long> getLocks() throws GuiControllerException {
		final Set<Long> set = new HashSet<Long>();
		try {
			set.addAll(this.connection.getLocked());
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, getLocks, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, getLocks, " + "count: " + set.size());
		return set;
	}	
	
	/**
	 * Returns the size of the database.
	 * 
	 * @return long - size of the database.
	 * @throws GuiControllerException 
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public long getMemory() throws GuiControllerException{
		long memory = -1;
		try {
			memory = this.connection.getAllocatedMemory();
		} catch (IOException e) {
			this.log.severe("GuiControllerAdmin, getMemory, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		return memory;
	}
	
	/**
	 * Adds a new Record to the database file.
	 * 
	 * @param rec
	 *            The Record to add.
	 * @return long - Returns the Record number of the new Record.
	 * @throws GuiControllerException
	 *             Catches <code>java.lang.IllegalArgumentException</code>, 
	 *             <code>java.io.IOException</code>, 
	 *             <code>suncertify.db.DuplicateKeyException</code>.
	 */
	public long createRecord(final Record rec) throws GuiControllerException {
		long recNo;
		try {
			recNo = this.connection.addRecord(rec);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerAdmin, createRecord, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final DuplicateKeyException ex) {
			this.log.severe("GuiControllerAdmin, createRecord, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final IllegalArgumentException ex) {
			this.log.severe("GuiControllerAdmin, createRecord, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} 		
		this.log.info("GuiControllerAdmin, createRecord, " + "Record number: " + recNo);
		return recNo;
	}

	/**
	 * Deletes a Record.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be deleted.
	 * @param lockCookie
	 *            The cookie, which is created during the lock acquisition of
	 *            the Record by calling <code>setRecordLocked</code> of the
	 *            public interface.
	 * @return boolean - Indicates whether the operation has succeeded.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>, 
	 *             <code>java.io.IOException</code> or 
	 *             <code>java.lang.SecurityException</code>.
	 */
	public boolean deleteRecord(final long recNo, final long lockCookie) throws GuiControllerException {
		boolean deleteOk = false;
		try {
			deleteOk = this.connection.removeRecord(recNo, lockCookie);
		} catch (final SecurityException e) {
			this.log.severe("GuiControllerAdmin, deleteRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, deleteRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerAdmin, deleteRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, deleteRecord, " + "Record number: " 
				+ recNo + " - cookie: " + lockCookie
				+ " - ok: " + deleteOk);
		return deleteOk;
	}

	/**
	 * Updates a Record and returns true, if the update has succeeded.
	 * 
	 * @param rec
	 *            The Record with the new values.
	 * @param recNo
	 *            The Record number.
	 * @param lockCookie
	 *            The cookie, which is created during the lock acquisition of
	 *            the Record.
	 * @return boolean - True, if the method has accomplished.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>, 
	 *             <code>java.io.IOException</code> or 
	 *             <code>java.lang.SecurityException</code>.
	 */
	public boolean updateRecord(final Record rec, final long recNo, 
							final long lockCookie) throws GuiControllerException {
		boolean updateOk = false;
		try {
			updateOk = this.connection.modifyRecord(rec, recNo, lockCookie);
		} catch (final SecurityException e) {
			this.isThereALockedRecord = false;
			this.log.severe("GuiControllerAdmin, updateRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, updateRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerAdmin, updateRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		} catch (final IllegalArgumentException e) {
			this.log.severe("GuiControllerAdmin, updateRecord, " + e.getMessage());
			this.isThereALockedRecord = false;
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerAdmin, updateRecord, " + "Record number: " 
		+ recNo + " - cookie: " + lockCookie
				+ " - ok: " + updateOk);
		return updateOk;
	}

	/**
	 * Closes the <code>java.net.Socket</code> object and the streams.
	 * @throws GuiControllerException Wraps an <code>java.io.IOException</code>.
	 * Only used in a network environment.
	 */
	public void closeNetworkConnection() throws GuiControllerException{
		try {
			((SocketClient_Admin)this.connection).saveExit();
		} catch (final IOException e) {
			this.log.severe("GuiControllerAdmin, closeNetworkConnection, " 
					+ e.getMessage());					
			throw new GuiControllerException(e);
		}	
	}
	
	/**
	 * The method provides two different dialogs based on the method argument
	 * of type <code>suncertify.gui.EnumChangeMode</code>.
	 * The return type is also represented by the
	 * enum type <code>suncertify.gui.EnumChangeMode</code>. <br>
	 * <br> 
	 * The accepted method arguments are: <br>
	 * CHANGE_MODE_END_GOON - indicates to display a dialog to exit the
	 * currently proceeding method or to go ahead <br>
	 * CHANGE_MODE_END_GOON_CHECK - indicates to display a dialog to exit the
	 * currently proceeding method, to go ahead or to check again the values of
	 * the Record <br>
	 * <br>
	 * The possible return values are: <br>
	 * CHANGE_RETURN_EXIT_METH - indicates to exit the proceeding method <br>
	 * CHANGE_RETURN_LOCK_WITHOUT_CHECK - indicates to continue the proceeding
	 * of the executing method without checking for new changes of the Record's
	 * values <br>
	 * CHANGE_RETURN_GOON_BY_CHECK - indicates to continue the proceeding of the
	 * executing method by checking for new changes of the Record's values
	 * 
	 * @param rec
	 *            The old Record, which will be checked against changes.
	 * @param recNo
	 *            The Record number of the Record, which should be checked.
	 * @param mode
	 *            The mode, how the Record should be checked.
	 * @param titleOpp Description of the operation for the dialog's title.
	 * @return int - indicates, how to continue.
	 * @throws GuiControllerException
	 *             It wraps an IOException and a RecordNotFoundException.
	 */
	public  EnumChangeMode checkRecordHasChanged(
			final Record rec, 
			final long recNo, 
			final EnumChangeMode mode,
			String titleOpp) throws GuiControllerException {
		
		Record newRecord;
		try {
			newRecord = this.connection.getRecord(recNo);
		} catch (final IOException e) {
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			throw new GuiControllerException(e);
		}
		boolean notEqual = false;
		final StringBuilder str = new StringBuilder("THIS RECORD HAS BEEN CHANGED!"
				+ " Please check the changes."
		+ "\n\nField: Old entry -/- New entry");

		if (!rec.getName().equals(newRecord.getName())) {
			notEqual = true;
			str.append("\nName: " + rec.getName() + " -/- " + newRecord.getName());
		}
		if (!rec.getCity().equals(newRecord.getCity())) {
			notEqual = true;
			str.append("\nCity: " + rec.getCity() + " -/- " + newRecord.getCity());
		}
		if (!rec.getTypesOfWork().equals(newRecord.getTypesOfWork())) {
			notEqual = true;
			str.append("\nTypes: " + rec.getTypesOfWork() + " -/- " 
					+ newRecord.getTypesOfWork());
		}
		if (rec.getNumberOfStaff() != newRecord.getNumberOfStaff()) {
			notEqual = true;
			str.append("\nStaff: " + rec.getNumberOfStaff() + " -/- " 
					+ newRecord.getNumberOfStaff());
		}
		if (!rec.getHourlyChargeRate().equals(newRecord.getHourlyChargeRate())) {
			notEqual = true;
			str.append("\nRate: " + rec.getHourlyChargeRate() + " -/- " 
					+ newRecord.getHourlyChargeRate());
		}
		if (!rec.getOwner().equals(newRecord.getOwner())) {
			notEqual = true;
			str.append("\nRate: " + rec.getOwner() + " -/- " + newRecord.getOwner());
		}		

		if (notEqual && (mode == EnumChangeMode.CHANGE_MODE_END_GOON)) {
			final int n = JOptionPane.showConfirmDialog(null,
					this.mainW.getMSG_ID() + 
					MainWindowAdmin.getMsgRecNo(recNo) 
					+ new String(str)
					+ "\n\nPlease press (Y) to proceed the operation.",					
					titleOpp + " - Changes", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
				return EnumChangeMode.CHANGE_RETURN_EXIT_METH;
			}
		} else if (notEqual && (mode == 
				EnumChangeMode.CHANGE_MODE_END_GOON_CHECK)) {
			final Object[] options = { "Lock - without checking", // 0
					"CANCEL", // 1
					"Check again - before locking"// 2
			};
			final int n = JOptionPane.showOptionDialog(null,
					this.mainW.getMSG_ID() + 
					MainWindowAdmin.getMsgRecNo(recNo) 
					+ new String(str)
					+ "\n"
					+ "\nBeware the fields of the Record could be CHANGED"
					+ " at any time.",
					titleOpp + " - Changes", JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null,
					options, options[2]);
			
			if (n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION) {
				return EnumChangeMode.CHANGE_RETURN_EXIT_METH;			
			} else if (n == JOptionPane.OK_OPTION) {
				return EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK;
			} else if (n == JOptionPane.OK_CANCEL_OPTION) {
				return EnumChangeMode.CHANGE_RETURN_GOON_BY_CHECK;
			}
		}
		return EnumChangeMode.CHANGE_RETURN_LOCK_WITHOUT_CHECK;
	}

	/**
	 * Loads a new or the same database file, if the access is locally.
	 * The method argument is the path on the local disk.
	 * 
	 * @param path String representation of the path to the database file.
	 * @throws GuiControllerException Thrown, if the database
	 * file could not be accessed.
	 */
	public void reloadDB(final String path) throws GuiControllerException{
		final RecordDatabase db = (RecordDatabase) this.connection;		
		try {
			db.reloadDB(path);
		} catch (final IOException e) {
			throw new GuiControllerException(e);
		}
	}

	/**
	 * Utility method to unlock a Record and to refresh
	 * the content of the table.
	 * 
	 * @param recNo
	 *            The Record number of the Record to unlock.
	 * @param cookie
	 *            The lock cookie, which is necessary to unlock the Record.
	 * @throws GuiControllerException
	 *             Wraps a <code>java.lang.SecurityException</code>, 
	 *             <code>java.io.IOException</code>
	 *             and a <code>suncertify.db.RecordNotFoundException</code>.
	 */
	private void setUnlockedAndUpdate(final long recNo, final long cookie) 
			throws GuiControllerException {
		this.isThereALockedRecord = false;
		this.unlockRecord(recNo, cookie);
		this.mainW.setupTableDatabase();
	}
	
	/**
	 * Utility method to unlock the Record and to set the instance
	 * variable <code>isThereALockedRecord</code>:
	 * 
	 * @param recNo
	 *            The Record number of the Record to unlock.
	 * @param cookie
	 *            The lock cookie, which is necessary to unlock the Record.
	 * @throws GuiControllerException
	 *             Wraps a <code>java.lang.SecurityException</code>, 
	 *             <code>java.io.IOException</code> and a
	 *             <code>suncertify.db.RecordNotFoundException</code>.
	 */
	private void setUnlocked(final long recNo, final long cookie) 
			throws GuiControllerException {
		this.isThereALockedRecord = false;
		this.unlockRecord(recNo, cookie);
	}
}
