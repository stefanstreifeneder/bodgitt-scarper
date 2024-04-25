package suncertify.gui.buyer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.ApplicationMode;
import suncertify.gui.EnumChangeMode;
import suncertify.gui.GuiControllerException;
import suncertify.gui.RecordTableModel;
import suncertify.sockets.buyer.SocketClient_Buyer;

/**
 * Handles all interactions of a Buyer client. 
 * The class calls the connecting methods of the classes
 * <code>suncertify.direct.buyer.RecordConnector_Buyer</code>,
 * which provides local access or
 * <code>suncertify.sockets.buyer.SocketConnector_Buyer</code>,
 * which provides remote access.
 * <br>
 * <br> The class provides access to all of the Records in the system and
 * all the operations a Buyer client must have that act upon the Records.
 * 
 * 
 * @see InterfaceClient_Buyer
 * @see RecordTableModel
 * @see MainWindowBuyer
 * @see GuiControllerException
 * @author stefan.streifeneder@gmx.de
 */
public class GuiControllerBuyer {

	/**
	 * Holds a reference to the client interface.
	 */
	private InterfaceClient_Buyer connection;

	/**
	 * A reference to the main window.
	 */
	private final MainWindowBuyer mainW;

	/**
	 * The Logger instance. All log messages from this class are routed 
	 * through this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.GuiControllerBuyer</code>.
	 */
	private final Logger log = 
			LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.buyer.GuiControllerBuyer"),
			Level.ALL);

	/**
	 * Constructs an object, which can connect to the database in two 
	 * ways: local or over a network by ipaddress and port. The object 
	 * can process commands made by the graphical mask.<br>
	 * <code>suncertify.gui.GuiControllerException</code> catches an 
	 * <code>java.io.IOException</code>.
	 * 
	 * @param mw
	 *            A reference to the main window of the Buyer client.
	 * @param appModePara
	 *            - Indicates local or network connection.
	 * @param dbLocation
	 *            The path to the database file or the ipaddress of the 
	 *            server.
	 * @param port
	 *            The port of the server.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerBuyer(final MainWindowBuyer mw, 
			final ApplicationMode appModePara, 
			final String dbLocation, final String port) 
					throws GuiControllerException {
		this.log.entering("GuiControllerBuyer", "GuiControllerBuyer",
				new Object[] { appModePara, dbLocation, port});
		this.mainW = mw;
		try {					
			if (appModePara == ApplicationMode.NETWORK_CLIENT) {
				this.connection = 
						suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
								dbLocation, port);
				this.log.info("GuiControllerBuyer, " 
						+ "GuiControllerBuyer, NETWORK_CLIENT, port: " 
						+ port + " - ip: "
						+ dbLocation);			
			}
			// If there is an individual application for a Seller client,
			// who will always access the database via a server the 
			// following code should be deleted or set in comments.
			else if (appModePara == ApplicationMode.LOCAL_CLIENT) {
//				this.connection = 
//						suncertify.direct.buyer.DirectConnector_Buyer.getLocal(
//								dbLocation);
				this.log.info("GuiControllerBuyer, " 
						+ "GuiControllerBuyer, DIRECT, dbLocation: " 
						+ dbLocation);
			}				
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, GuiControllerBuyer," 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.exiting("GuiControllerBuyer", "GuiControllerBuyer");
	}
	
	
	/**
	 * Constructs an object, which can connect to the database in two 
	 * ways: local or over a network by ipaddress and port. The object 
	 * can process commands made by the graphical mask.<br>
	 * <code>suncertify.gui.GuiControllerException</code> catches an 
	 * <code>java.io.IOException</code>.
	 * 
	 * @param mw
	 *            A reference to the main window of the Buyer client.
	 * @param appModePara
	 *            - Indicates local or network connection.
	 * @param dbLocation
	 *            The path to the database file or the ipaddress of the 
	 *            server.
	 * @param port
	 *            The port of the server.
	 * @param password The password of the database.
	 * @throws GuiControllerException
	 *             Catches an <code>java.io.IOException</code>.
	 */
	public GuiControllerBuyer(final MainWindowBuyer mw, 
			final ApplicationMode appModePara, 
			final String dbLocation, final String port, final String password) 
					throws GuiControllerException {
		this.log.entering("GuiControllerBuyer", "GuiControllerBuyer",
				new Object[] { appModePara, dbLocation, port});
		this.mainW = mw;
		try {					
			if (appModePara == ApplicationMode.NETWORK_CLIENT) {
				this.connection = 
						suncertify.sockets.buyer.SocketConnector_Buyer.getRemote(
								dbLocation, port);
				this.log.info("GuiControllerBuyer, " 
						+ "GuiControllerBuyer, NETWORK_CLIENT, port: " 
						+ port + " - ip: "
						+ dbLocation);			
			}
			// If there is an individual application for a Seller client,
			// who will always access the database via a server the 
			// following code should be deleted or set in comments.
			else if (appModePara == ApplicationMode.LOCAL_CLIENT) {
				this.connection = 
						suncertify.direct.buyer.DirectConnector_Buyer.getLocal(
								dbLocation, password);
				
				
				this.log.info("GuiControllerBuyer, " 
						+ "GuiControllerBuyer, DIRECT, dbLocation: " 
						+ dbLocation);
			}				
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, GuiControllerBuyer," 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.exiting("GuiControllerBuyer", "GuiControllerBuyer");
	}
	
	/**
	 * Returns a table model, which contains all Records
	 * found by given parameters. The method argument 
	 * <code>interSection</code> has to be true to built an union.
	 * 
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
		this.log.entering("GuiControllerBuyer", "findRecordByCriteria",
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
						this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
							this.log.severe("GuiControllerBuyer, findRecordByCriteria, "
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
			this.log.severe("GuiControllerBuyer, findRecordByCriteria, " + ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.info("GuiControllerBuyer, findRecordByCriteria, count: " + out.getRowCount());
		this.log.entering("GuiControllerBuyer", "findRecordByCriteria", out);
		return out;
	}
	
	/**
	 * Retrieves all valid Records from the database and returns a 
	 * model object to display the Records.
	 * 
	 * @return RecordTableModel - A table model containing all Records.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public RecordTableModel getAllRecordsModel() throws GuiControllerException {
		this.log.entering("GuiControllerBuyer", "getRecords");
		final RecordTableModel out = new RecordTableModel();
		try {
			for(Record record : 
				this.connection.getAllValidRecords()){
				out.addRecord(record);
			}
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, getRecordsModel, " 
					+ ex.getMessage());
			throw new GuiControllerException(ex);
		}
		this.log.entering("GuiControllerBuyer", "getRecordsModel", out);
		return out;
	}

	/**
	 * Returns a Record object according to the Record number.
	 * 
	 * @param recNo
	 *            The Record number.
	 * @return Record - A Record.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public Record getRecord(final long recNo) throws GuiControllerException {
		Record rec;
		try {
			rec = this.connection.getRecord(recNo);
		} catch (final IOException e) {
			this.log.severe("GuiControllerBuyer, getRecord," + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerBuyer, getRecord," + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerBuyer, getRecord, Record number: " + recNo);
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
			this.log.severe("GuiControllerBuyer, getRecordsList, " 
					+ e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerBuyer, getRecordsList, count: " 
				+ list.size());
		return list;
	}

	/**
	 * Reserves a Record of a contractor for home improvements by this client.
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 * 
	 * @param recNo
	 *            The Record number for identifying the Record.
	 * @param idOwner
	 *            The ID of the owner.
	 * @return boolean - Indicates whether the operation has accomplished.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code>, 
	 *             <code>java.io.IOException</code> or
	 *             <code>java.lang.SecurityException</code>.
	 */
	public boolean rent(final long recNo, final int idOwner) 
			throws GuiControllerException {
		this.log.entering("GuiControllerBuyer", "rent", 
				new Object[] { Long.valueOf(recNo), Integer.valueOf(idOwner) });
		boolean returnValue = false;
		long cookie;
		Record rec;
		try {
			rec = this.connection.getRecord(recNo);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		}		
		try {
			cookie = this.connection.setRecordLocked(recNo);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} 
		if(
			this.checkRecordHasChanged(rec, recNo, 
						EnumChangeMode.CHANGE_MODE_END_GOON,
					"BUYER - RENT - YOU GET LOCK")
				== EnumChangeMode.CHANGE_RETURN_EXIT_METH){
			
			this.setUnlocked(recNo, cookie);
			throw new GuiControllerException(this.mainW.getMSG_ID() 
							+"You have canceled the rent operation " 
							+ "due to changings!");
		}
		try {
			returnValue = this.connection.reserveRecord(recNo, idOwner, cookie);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final SecurityException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final IllegalArgumentException ex) {
			this.log.severe("GuiControllerBuyer, rent," + ex.getMessage());
			throw new GuiControllerException(ex);
		}finally{
			this.setUnlocked(recNo, cookie);
			this.log.info("GuiControllerBuyer, rent, Record number: " 
					+ recNo + " ok: " + returnValue);
		}
		this.log.entering("GuiControllerBuyer", "rent", Boolean.valueOf(returnValue));
		return returnValue;
	}

	/**
	 * Releases the Record out of the rented state. 
	 * <br> A dialog will be displayed, if another client has changed the
	 * values of the Record meanwhile.
	 * 
	 * @param recNo
	 *            The Record number to identify the Record.
	 * @return boolean - True, if the operation has accomplished.
	 * @throws GuiControllerException
	 *             Catches <code>suncertify.db.RecordNotFoundException</code> or 
	 *             <code>java.io.IOException</code>.
	 */
	public boolean returnRental(final long recNo) throws GuiControllerException {
		this.log.entering("GuiControllerBuyer", "returnRental", Long.valueOf(recNo));
		boolean returnValue = false;
		long cookie;
		Record recOld;
		try {
			recOld = this.connection.getRecord(recNo);
		} catch (final IOException e1) {
			this.log.severe("GuiControllerBuyer, returnRental, getRecord-old, " 
					+ e1.getMessage());
			throw new GuiControllerException(e1);
		} catch (final RecordNotFoundException e1) {
			this.log.severe("GuiControllerBuyer, returnRental, getRecord-old, " 
					+ e1.getMessage());
			throw new GuiControllerException(e1);
		}			
		try {			
			cookie = this.connection.setRecordLocked(recNo);			
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, returnRental," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerBuyer, returnRental," + ex.getMessage());
			throw new GuiControllerException(ex);
		} 
		if(
			this.checkRecordHasChanged(recOld, recNo, 
						EnumChangeMode.CHANGE_MODE_END_GOON,
						"BUYER - RELEASE - YOU GET LOCK")
				== EnumChangeMode.CHANGE_RETURN_EXIT_METH){			
			
			this.setUnlocked(recNo, cookie);
			throw new GuiControllerException(this.mainW.getMSG_ID() 
						+ "You have canceled the release operation " 
							+ "due to changings!");
		}
		try {
			returnValue = this.connection.releaseRecord(recNo, cookie);
		} catch (final IOException ex) {
			this.log.severe("GuiControllerBuyer, returnRental," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final RecordNotFoundException ex) {
			this.log.severe("GuiControllerBuyer, returnRental," + ex.getMessage());
			throw new GuiControllerException(ex);
		} catch (final SecurityException ex) {
			this.log.severe("GuiControllerBuyer, returnRental," + ex.getMessage());
			throw new GuiControllerException(ex);
		}finally{
			this.setUnlocked(recNo, cookie);
			this.log.info("GuiControllerBuyer, returnRental, Record number: " 
					+ recNo + " ok: " + returnValue);
		}		
		this.log.entering("GuiControllerBuyer", "returnRental", 
				Boolean.valueOf(returnValue));
		return returnValue;
	}

	/**
	 * Returns a set with the Record numbers of all locked Records.
	 * 
	 * @return Set - A set with all Record numbers of locked Records.
	 * @throws GuiControllerException
	 *             <code>java.io.IOException</code> is thrown, if problems w
	 *             ithin transmission happens.
	 */
	public Set<Long> getLocks() throws GuiControllerException {
		final Set<Long> set = new HashSet<Long>();
		try {
			set.addAll(this.connection.getLocked());
		} catch (final IOException e) {
			this.log.severe("GuiControllerBuyer, getLocks," + e.getMessage());
			throw new GuiControllerException(e);
		}
		this.log.info("GuiControllerBuyer, getLocks, " + "count: " + set.size());
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
	 * @param titleOpp Description of the operation for the dialog's title.
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
		final StringBuilder str = new StringBuilder("THIS RECORD HAS BEEN CHANGED! "
				+ "Please check the entries."
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
			str.append("\nStaff: " + rec.getNumberOfStaff() + "/" 
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
					this.mainW.getMSG_ID() + MainWindowBuyer.getMsgRecNo(recNo) 
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
					this.mainW.getMSG_ID() + MainWindowBuyer.getMsgRecNo(recNo) 
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
	 * Closes the <code>java.net.Socket</code> object and the streams.
	 * @throws GuiControllerException Wraps an <code>java.io.IOException</code>.
	 * Only used in a network environment.
	 */
	public void closeNetworkConnection() throws GuiControllerException{
		try {
			((SocketClient_Buyer)this.connection).saveExit();
		} catch (final IOException e) {
			this.log.severe("GuiControllerBuyer, closeNetworkConnection, " 
					+ e.getMessage());					
			throw new GuiControllerException(e);
		}	
	}

	/**
	 * Utility method to unlock the Record and to refresh the content 
	 * of the table.
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
		try {
			this.connection.setRecordUnlocked(recNo, cookie);
			this.mainW.setupTableDatabase();
		} catch (final SecurityException e) {
			this.log.severe("GuiControllerBuyer, setUnlocked, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final IOException e) {
			this.log.severe("GuiControllerBuyer, setUnlocked, " + e.getMessage());
			throw new GuiControllerException(e);
		} catch (final RecordNotFoundException e) {
			this.log.severe("GuiControllerBuyer, setUnlocked, " + e.getMessage());
			throw new GuiControllerException(e);
		}
	}
}
