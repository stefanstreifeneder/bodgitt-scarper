package suncertify.gui.seller;

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
import suncertify.db.InterfaceClient_Seller;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.ApplicationMode;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.GuiControllerException;
import suncertify.gui.RecordTableModel;
import suncertify.sockets.seller.SocketClient_Seller;

/**
 * Handles all interactions of a Seller client. 
 * This class calls the connecting methods of
 * <code>suncertify.direct.seller.RecordConnector_Seller</code>,
 * which provides local access or
 * <code>suncertify.sockets.seller.SocketConnector_Seller</code>,
 * which provides remote access.
 * <br>
 * <br> The class provides access to all of the Records in the system and
 * all the operations a Seller client must have that act upon the Records. 
 * 
 * 
 * 
 * @see InterfaceClient_Seller
 * @see RecordTableModel
 * @see MainWindowSeller
 * @see GuiControllerException
 * @author stefan.streifeneder@gmx.de
 */
public class GuiControllerSeller {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is 
	 * <code>suncertify.gui.seller.GuiControllerSeller</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.seller.GuiControllerSeller"), Level.ALL);
	
	/**
	 * Holds a reference to the client interface, which connects to
	 * the database.
	 */
	private InterfaceClient_Seller connection;
	
	/**
	 * A reference to the main window.
	 */
	private final MainWindowSeller mainW;

	/**
	 * Constructs an object, which can connect to the database in two ways:
	 * local or over the network. 
	 * 
	 * @param appModePara
	 *            - The <code>suncertify.gui.ApplicationMode</code>, 
	 *            in this case only <code>LOCAL_CLIENT</code> or
	 *            <code>NETWORK_CLIENT</code>.
	 * @param dbLocation
	 *            Classpath of the location of the database file or ipaddress
	 *            of the server.
	 * @param port
	 *            The port number of the server.
	 * @param mw A reference to the main window.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerSeller(final ApplicationMode appModePara, 
			final String dbLocation, 
			final String port, final MainWindowSeller mw)
			throws GuiControllerException {
		this.log.entering("GuiControllerSeller", "GuiControllerSeller",
				new Object[] { appModePara, dbLocation, port});		
		this.mainW = mw;
		try {
			if (appModePara == ApplicationMode.NETWORK_CLIENT) {
				this.connection = 
						suncertify.sockets.seller.SocketConnector_Seller.getRemote(
								dbLocation, port);
				this.log.info("GuiControllerSeller, " 
						+ "GuiControllerSeller, NETWORK_CLIENT, port: " 
						+ port + " - ip: "
						+ dbLocation);
			}
			// If there is an individual application for a Seller client,
			// who will always access the database via a server the 
			// following code should be deleted or set in comments.
			else if (appModePara == ApplicationMode.LOCAL_CLIENT) {
//				this.connection = 
//						suncertify.direct.seller.DirectConnector_Seller.getLocal(
//								dbLocation);
				this.log.info("GuiControllerSeller, " + "GuiControllerSeller, "
						+ "DIRECT, dbLocation: " + dbLocation);
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerSeller, GuiControllerSeller, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.exiting("GuiControllerSeller", "GuiControllerSeller");
	}
	
	/**
	 * Constructs an object, which can connect to the database in two ways:
	 * local or over the network. 
	 * 
	 * @param appModePara
	 *            - The <code>suncertify.gui.ApplicationMode</code>, 
	 *            in this case only <code>LOCAL_CLIENT</code> or
	 *            <code>NETWORK_CLIENT</code>.
	 * @param dbLocation
	 *            Classpath of the location of the database file or ipaddress
	 *            of the server.
	 * @param port
	 *            The port number of the server.
	 * @param mw A reference to the main window.
	 * @param password The password of the database.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerSeller(final ApplicationMode appModePara, 
			final String dbLocation, 
			final String port, final MainWindowSeller mw, String password)
			throws GuiControllerException {
		this.log.entering("GuiControllerSeller", "GuiControllerSeller",
				new Object[] { appModePara, dbLocation, port});		
		this.mainW = mw;
		try {
			if (appModePara == ApplicationMode.NETWORK_CLIENT) {
				this.connection = 
						suncertify.sockets.seller.SocketConnector_Seller.getRemote(
								dbLocation, port);
				this.log.info("GuiControllerSeller, " 
						+ "GuiControllerSeller, NETWORK_CLIENT, port: " 
						+ port + " - ip: "
						+ dbLocation);
			}
			// If there is an individual application for a Seller client,
			// who will always access the database via a server the 
			// following code should be deleted or set in comments.
			else if (appModePara == ApplicationMode.LOCAL_CLIENT) {
				
				//new
				this.connection = 
						suncertify.direct.seller.DirectConnector_Seller.getLocal(
								dbLocation, password);
				//old
//				this.connection = 
//						suncertify.direct.seller.DirectConnector_Seller.getLocal(
//								dbLocation);
				this.log.info("GuiControllerSeller, " + "GuiControllerSeller, "
						+ "DIRECT, dbLocation: " + dbLocation);
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerSeller, GuiControllerSeller, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.exiting("GuiControllerSeller", "GuiControllerSeller");
	}

	/**
	 * Adds a new Record to the database and returns the Record number
	 * of the new Record.
	 * 
	 * @param rec
	 *            The Record to add.
	 * @return long - True, if the Record is created. 
	 * 
	 * @throws GuiControllerException
	 *             Catches <code>java.lang.IllegalArgumentException</code>, 
	 *             <code>java.io.IOException</code>, 
	 *             <code>suncertify.db.DuplicateKeyException</code>.
	 * 
	 */
	public long createRecord(final Record rec) throws GuiControllerException {
		long recNo;
		try {
			recNo = this.connection.addRecord(rec);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerSeller, createRecord, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final DuplicateKeyException ex) {
			this.log.severe("GuiControllerSeller, createRecord, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final IllegalArgumentException ex) {
			this.log.severe("GuiControllerSeller, createRecord, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		} 
		this.log.info("GuiControllerSeller, createRecord, " + "Record number: " + recNo);
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
	 * @return boolean - True, if the Record has been deleted.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>,
	 *             <code>java.io.IOException</code> or 
	 *             <code>java.lang.SecurityException</code>.
	 */
	public boolean deleteRecord(final long recNo, final long lockCookie) 
			throws GuiControllerException {
		boolean deleteOk = false;
		try {
			deleteOk = this.connection.removeRecord(recNo, lockCookie);
		} catch (final SecurityException e) {
			this.log.severe("GuiControllerSeller, deleteRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, deleteRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerSeller, deleteRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, deleteRecord, " + "Record number: " 
				+ recNo + " - cookie: " + lockCookie
				+ " - ok: " + deleteOk);
		return deleteOk;
	}

	/**
	 * Returns a table model, which possesses a Record,
	 * which is located by Record number.
	 *
	 * @param recNo
	 *            A long representing the Record number identifying the Record.
	 * @return RecordTableModel - A model object for the table to display the
	 *         client.
	 * @throws GuiControllerException
	 *             Catches <code>RecordNotFoundException</code> 
	 *             or <code>java.io.IOException</code>.
	 */
	public RecordTableModel findRecord(final long recNo) 
			throws GuiControllerException {
		this.log.entering("GuiControllerSeller", "findRecord", Long.valueOf(recNo));
		final RecordTableModel out = new RecordTableModel();
		try {
			out.addRecord(this.connection.getRecord(recNo));
		} catch (final IOException ex) {
			this.log.severe("GuiControllerSeller, findRecord, " + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerSeller, findRecord, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.info("GuiControllerSeller, findRecord, Record number: " + recNo);
		this.log.exiting("GuiControllerSeller", "findRecord", out);
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
		this.log.entering("GuiControllerSeller", "findRecordByCriteria",
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
						this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
							this.log.severe("GuiControllerSeller, findRecordByCriteria, "
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
			this.log.severe("GuiControllerSeller, findRecordByCriteria, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.info("GuiControllerSeller, findRecordByCriteria, count: " + out.getRowCount());
		this.log.entering("GuiControllerSeller", "findRecordByCriteria", out);
		return out;
	}
	

	/**
	 * Locks a Record to delete or to update the Record.
	 * 
	 * @param recNo
	 *            The Record number.
	 * @return long - The lock cookie.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public long lockRecord(final long recNo) throws GuiControllerException {
		long cookie;
		try {
			cookie = this.connection.setRecordLocked(recNo);
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, lockRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerSeller, lockRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, lockRecord, " + "Record number: " 
		+ recNo + " - cookie: " + cookie);
		return cookie;
	}

	/**
	 * Returns a Record searched by its Record number.
	 * 
	 * @param recNo
	 *            The Record number.
	 * @return Record - The Record.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>
	 *             or <code>java.io.IOException</code>.
	 */
	public Record getRecord(final long recNo) throws GuiControllerException {
		Record rec;
		try {
			rec = this.connection.getRecord(recNo);
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, getRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerSeller, getRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, getRecord, Record number: " + recNo);
		return rec;
	}

	/**
	 * Retrieves all valid Records from the database and returns a table model, 
	 * which displays the found Records.
	 * 
	 * @return RecordTableModel - A table model containing all Records.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>
	 *             or <code>java.io.IOException</code>.
	 */
	public RecordTableModel getAllRecordsModel() throws GuiControllerException {
		this.log.entering("GuiControllerSeller", "getRecordsModel");
		final RecordTableModel out = new RecordTableModel();
		try {
			for(Record record : 
				this.connection.getAllValidRecords()){
				out.addRecord(record);
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerSeller, getRecordsModel, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.entering("GuiControllerSeller", "getRecordsModel", out);
		return out;				
	}

	/**
	 * Returns a list with all valid Records.
	 * 
	 * @return List - A list with all Records.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>
	 *             or <code>java.io.IOException</code>.
	 */
	public List<Record> getRecordsList() throws GuiControllerException {
		final List<Record> list = new ArrayList<Record>();
		try {
			list.addAll(this.connection.getAllValidRecords());
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, getRecordsList, " 
					+ e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, getRecordsList, count: " 
				+ list.size());
		return list;
	}

	/**
	 * Unlocks a Record. 
	 * 
	 * @param recNo
	 *            The Record number.
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
			this.log.severe("GuiControllerSeller, unlockRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, unlockRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerSeller, unlockRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, unlockRecord, " + "Record number: " 
					+ recNo + " - cookie: " + cookie);
	}

	/**
	 * Updates a Record and returns true, if the Record has been updated.
	 * 
	 * @param rec
	 *            The Record to update.
	 * @param recNo
	 *            The Record number.
	 * @param lockCookie
	 *            The cookie, which is created during the lock acquisition of
	 *            the Record.
	 * @return boolean - True, if the Record is been updated.
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
			this.log.severe("GuiControllerSeller, updateRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, updateRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerSeller, updateRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IllegalArgumentException e) {
			this.log.severe("GuiControllerSeller, updateRecord, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, updateRecord, " + "Record number: " 
				+ recNo + " - cookie: " + lockCookie
				+ " - ok: " + updateOk);
		return updateOk;
	}

	/**
	 * Returns the Record numbers of all locked Records.
	 * 
	 * @return Set - A set, which generic type is <code>java.lang.Long</code>, 
	 * and every element represents a Record number.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public Set<Long> getLocks() throws GuiControllerException {
		final Set<Long> set = new HashSet<Long>();
		try {
			set.addAll(this.connection.getLocked());
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, getLocks, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerSeller, getLocks, " + "count: " + set.size());
		return set;
	}
	
	/**
	 * Returns the number of all Records, which possesses an entry in
	 * 'ID Owner'.
	 * 
	 * @return Set - A set of the Record numbers of the booked Records.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public int getBooked() throws GuiControllerException {
		int idsOwner = 0;
		for(final Record r : this.getRecordsList()){
			if (r.getOwner().length() != 0) {
				idsOwner++;
			}
		}
		this.log.info("GuiControllerBuyer, getBooked, " + "count: " + idsOwner);
		return idsOwner;
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
			this.log.severe("GuiControllerBuyer, getMemory, " + e.getMessage());
			throw new GuiControllerException(e);
		}
		return memory;
	}

	/**
	 * Closes the <code>java.net.Socket</code> object and the streams.
	 * @throws GuiControllerException Wraps an <code>java.io.IOException</code>.
	 * Only used in a network environment.
	 */
	public void closeNetworkConnection() throws GuiControllerException{
		try {
			((SocketClient_Seller)this.connection).saveExit();
		} catch (final IOException e) {
			this.log.severe("GuiControllerSeller, closeNetworkConnection, " 
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
	 * @param rec The old Record, which will be checked against changes.
	 * @param recNo The Record number of the Record, which should be checked.
	 * @param mode The mode, how the Record should be checked.
	 * @param titleOpp  Description of the operation for the dialog's title.
	 * @return int - indicates, how to continue.
	 * @throws GuiControllerException It wraps an IOException and a
	 * RecordNotFoundException.
	 */
	public  EnumChangeMode checkRecordHasChanged(
			final Record rec, 
			final long recNo, 
			final EnumChangeMode mode,
			String titleOpp) 
					throws GuiControllerException {
		Record newRecord;
		try {
			newRecord = this.connection.getRecord(recNo);
		} catch (final IOException e) {
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			throw new GuiControllerException(e);
		}
		boolean notEqual = false;
		final StringBuilder str = new StringBuilder("THIS RECORD HAS BEEN CHANGED" 
		+ "\n\nold entry / new entry");

		if (!rec.getName().equals(newRecord.getName())) {
			notEqual = true;
			str.append("\nName: " + rec.getName() + "/" + newRecord.getName());
		}
		if (!rec.getCity().equals(newRecord.getCity())) {
			notEqual = true;
			str.append("\nCity: " + rec.getCity() + "/" + newRecord.getCity());
		}
		if (!rec.getTypesOfWork().equals(newRecord.getTypesOfWork())) {
			notEqual = true;
			str.append("\nTypes: " + rec.getTypesOfWork() + "/" 
					+ newRecord.getTypesOfWork());
		}
		if (rec.getNumberOfStaff() != newRecord.getNumberOfStaff()) {
			notEqual = true;
			str.append("\nStaff: " + rec.getNumberOfStaff() + "/" 
					+ newRecord.getNumberOfStaff());
		}
		if (!rec.getHourlyChargeRate().equals(newRecord.getHourlyChargeRate())) {
			notEqual = true;
			str.append("\nRate: " + rec.getHourlyChargeRate() + "/" 
					+ newRecord.getHourlyChargeRate());
		}
		if (!rec.getOwner().equals(newRecord.getOwner())) {
			notEqual = true;
			str.append("\nRate: " + rec.getOwner() + " -/- " + newRecord.getOwner());
		}	

		if (notEqual && (mode == EnumChangeMode.CHANGE_MODE_END_GOON)) {
			final int n = JOptionPane.showConfirmDialog(null,
					this.mainW.getMSG_ID() + MainWindowSeller.getMsgRecNo(recNo) 
					+ new String(str),
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
					this.mainW.getMSG_ID() + MainWindowSeller.getMsgRecNo(recNo) 
					+ new String(str),
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
	
}
