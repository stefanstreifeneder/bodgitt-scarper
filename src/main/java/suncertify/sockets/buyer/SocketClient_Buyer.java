package suncertify.sockets.buyer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.InterfaceClient_Buyer;
import suncertify.db.LoggerControl;
import suncertify.db.Record;
import suncertify.db.RecordNotFoundException;
import suncertify.gui.MyInetAddress;
import suncertify.sockets.RecordDatabaseCommand;
import suncertify.sockets.RecordDatabaseResult;
import suncertify.sockets.SocketCommand;

/**
 * The class is a point-to-point socket client.<br>
 * It implements all of the methods according to the interface 
 * <code>suncertify.db.InterfaceClient_Buyer</code>.<br>
 * <br>
 * The class settles input streams and output streams to communicate
 * with the server in a synchronous way. The class sends a request
 * by an object of type <code>suncertify.sockets.RecordDatabaseCommand</code>
 * to the server. The class receives responds of the server 
 * by an object of type <code>suncertify.sockets.RecordDatabaseResult</code>.
 * <br>
 * <br>
 * The class uses an object of type <code>java.net.Socket</code> to communicate,
 * which has to be closed at the end of a session. 
 * 
 * To ensure all resources will be cleaned up the class provides 
 * a public method called <code>saveExit</code> to start the 
 * closing procedure, which calls the private method 
 * <code>closeConnection</code> to close the streams and the 
 * <code>Socket</code>. 
 * 
 * @see InterfaceClient_Buyer
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketClient_Buyer implements InterfaceClient_Buyer {

	/**
	 * Initially it is initialized with the ip of the currently 
	 * working computer.
	 */
	private static String ip = "192.168.2.101";

	/**
	 * The port number to connect to.
	 */
	private static Integer port = Integer.valueOf(3000);
	
	/**
	 * Used in a case the default inner port number
	 * is already in use and <code>java.net.BindException</code>
	 * is thrown.
	 */
	private static int countPorts = 10000;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.buyer.SocketClient_Buyer</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger("suncertify.sockets.buyer.SocketClient_Buyer"), 
			Level.ALL);

	/**
	 * The output stream used to write a serialized object to a socket server.
	 */
	private ObjectOutputStream oos;

	/**
	 * The input stream used to read a serialized object (a response) from the
	 * socket server.
	 */
	private ObjectInputStream ois;

	/**
	 * Represents the connection.
	 */
	private Socket socket = null;

	/**
	 * Default constructor.
	 * Never called in this application.
	 *
	 * @throws UnknownHostException
	 *             If unable to connect.
	 * @throws IOException
	 *             A network error.
	 */
	public SocketClient_Buyer() throws UnknownHostException, IOException {
		this(SocketClient_Buyer.ip, SocketClient_Buyer.port.toString());
		this.log.entering("SocketClient_Buyer", "SocketClient_Buyer");
		this.log.exiting("SocketClient_Buyer", "SocketClient_Buyer");
	}

	/**
	 * Constructor takes in a ipaddress and the port number of the server to
	 * connect.
	 *
	 * @param hostname
	 *            The ipaddress to connect to.
	 * @param portNumber
	 *            The <code>java.lang.String</code> representation of the port to connect
	 *            on.
	 * @throws UnknownHostException
	 *             If unable to connect.
	 * @throws IOException
	 *             A network error.
	 */
	public SocketClient_Buyer(final String hostname, final String portNumber) throws UnknownHostException, IOException {
		this.log.entering("SocketClient_Buyer", "SocketClient_Buyer", new Object[] { hostname, portNumber });
		SocketClient_Buyer.ip = hostname;
		SocketClient_Buyer.port = Integer.valueOf(portNumber);
		this.initialize();
		this.log.exiting("SocketClient_Buyer", "SocketClient_Buyer");
	}
		
	/**
	 * Searches for Records, which match with the criteria.
	 * 
	 * @param criteria
	 *            Elements of a Record as filters.
	 * @return long - an array, which contains all matching Record numbers.
	 * @throws IOException
	 *             Thrown if transmission problems occur.
	 */
	@Override
	public long[] findByFilter(final String[] criteria)
			throws IOException{
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.FIND, criteria);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.warning("SocketClient_Buyer, findByFilter, - criteria-size: " 
								+ criteria.length
								+ "\nresult: " 
								+ res.getFindBy().length
								+ "(found matches)");		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, findRecord, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getFindBy();
	}		
		

	/**
	 * Finds a Record by Record number.
	 * 
	 * @param recNo
	 *            The Record number of the searched Record.
	 * @return Record - The Record according to the Record number.
	 * @throws IOException
	 *             If transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If a Record could not been found or is marked deleted.
	 */
	@Override
	public Record getRecord(final long recNo) throws IOException, 
										RecordNotFoundException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.GET_RECORD, recNo);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, getRecord, - recNo: " 
							+ recNo
							+ "\nresult: " + res.getRecord());
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof RecordNotFoundException) {
				this.log.severe("SocketClient_Buyer, getRecord, " + e.getMessage());
				throw new RecordNotFoundException(e.getMessage());
			} else if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, getRecord, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getRecord();
	}

	/**
	 * Releases a Record of a reserved status.
	 * 
	 * @param recNo
	 *             The Record number of the Record, which will
	 *            be released.
	 * @param lockCookie
	 *            The cookie, which will be created, if a Record is locked.
	 * @return boolean - True, if Record is been released of a rented state.
	 * @throws IOException
	 *             If transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If a Record could not been found or is marked deleted.
	 * @throws SecurityException
	 *             If the lock cookie is wrong or locking problems occur.
	 */
	@Override
	public boolean releaseRecord(final long recNo, final long lockCookie)
			throws IOException, RecordNotFoundException, SecurityException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.RELEASE, recNo, lockCookie);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, releaseRecord, - recNo: " 
						+ recNo + " - cookie: " + lockCookie
						+ "\nresult: " + res.getBoolean()
						+ "(successful realease)");		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof RecordNotFoundException) {
				this.log.severe("SocketClient_Buyer, releaseRecord, " + e.getMessage());
				throw new RecordNotFoundException(e.getMessage());
			} else if (e instanceof SecurityException) {
				this.log.severe("SocketClient_Buyer, releaseRecord, " + e.getMessage());
				throw new SecurityException(e.getMessage());
			} else if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, releaseRecord, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getBoolean();
	}

	/**
	 * Rents a Record.
	 * 
	 * @param idOwner
	 *            The ID of the owner, who rents the Record.
	 * @param recNo
	 *            The Record number of the Record, which should be rented.
	 * @param lockCookie
	 *            The lock cookie.
	 * @return boolean - True, if the Record is been rented.
	 * @throws IOException
	 *             If transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If a Record could not been found or is marked deleted.
	 * @throws SecurityException
	 *             If the lock cookie is wrong or locking problems occur.
	 * @throws IllegalArgumentException 
	 * 			   Thrown if the Record number does not confirm the format
	 * 			   of the database file.
	 */
	@Override
	public boolean reserveRecord(final long recNo, final int idOwner, final long lockCookie)
			throws IOException, RecordNotFoundException, SecurityException,
						IllegalArgumentException{
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.RENT, recNo, idOwner, lockCookie);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, reserveRecord, - recNo: " 
						+ recNo
						+ "\nresult: " + res.getBoolean()
						+ "(successful rent)");		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof RecordNotFoundException) {
				this.log.severe("SocketClient_Buyer, reserveRecord, " + e.getMessage());
				throw new RecordNotFoundException(e.getMessage());
			} else if (e instanceof SecurityException) {
				this.log.severe("SocketClient_Buyer, reserveRecord, " + e.getMessage());
				throw new SecurityException(e.getMessage());
			} else if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, reserveRecord, " + e.getMessage());
				throw new IOException(e.getMessage());
			} else if (e instanceof IllegalArgumentException) {
				this.log.severe("SocketClient_Buyer, reserveRecord, " + e.getMessage());
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		return res.getBoolean();
	}

	/**
	 * Sets a Record locked.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be rented.
	 * @return long - The lock cookie.
	 * @throws IOException
	 *             If transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If a Record could not been found or is marked deleted.
	 */
	@Override
	public long setRecordLocked(final long recNo) throws IOException, 
								RecordNotFoundException, SecurityException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.LOCK_RECORD, recNo);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, setRecordLocked, - recNo: " 
							+ recNo 
							+ "\nresult: " + res.getCookie()
							+ "(lock cookie)");		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof RecordNotFoundException) {
				this.log.severe("SocketClient_Buyer, setRecordLocked, " + e.getMessage());
				throw new RecordNotFoundException(e.getMessage());
			} else if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, setRecordLocked, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getCookie();
	}

	/**
	 * Sets a Record unlocked.
	 * 
	 * @param recNo
	 *            The Record number of the Record, which should be unlocked.
	 * @param lockCookie
	 *            The lock cookie.
	 * @throws IOException
	 *             If transmission problems occur.
	 * @throws RecordNotFoundException
	 *             If a Record could not been found or is marked deleted.
	 * @throws SecurityException
	 *             If the lock cookie is wrong or locking problems occur.
	 */
	@Override
	public void setRecordUnlocked(final long recNo, final long lockCookie)
			throws IOException, SecurityException, RecordNotFoundException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.UNLOCK_RECORD, recNo, lockCookie);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, setRecordUnlocked, - recNo: " 
							+ recNo + " - cookie: " + lockCookie 
							+ "\nresult: " + res.getBoolean()
							+ "(successful unlock)");
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof RecordNotFoundException) {
				this.log.severe("SocketClient_Buyer, setRecordUnlocked, " + e.getMessage());
				throw new RecordNotFoundException(e.getMessage());
			} else if (e instanceof SecurityException) {
				this.log.severe("SocketClient_Buyer, setRecordUnlocked, " + e.getMessage());
				throw new SecurityException(e.getMessage());
			} else if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, setRecordUnlocked, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		res.getBoolean();
	}

	/**
	 * Returns a set of all Record numbers of locked Records.
	 * 
	 * @return Set - All locked Record numbers.
	 * @throws IOException
	 *             If there are transmission problems.
	 */
	@Override
	public Set<Long> getLocked() throws IOException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
					SocketCommand.GET_LOCKS);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, getLocked" 
								+ "\nresult: " 
								+ res.getListLocked().size()
								+ ("(size)"));
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, getLocked, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getListLocked();
	}
	
	/**
	 * Returns the size of the database.
	 * 
	 * @return long - the size of the database.
	 */
	@Override
	public long getAllocatedMemory() throws IOException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
		SocketCommand.GET_MEMORY);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, getAllocatedMemory" 
						+ "\nresult: " 
						+ res.getMemory()
						+ "(size)");		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, "
						+ "getAllocatedMemory, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getMemory();
	}

	/**
	 * Returns a list, which elements are valid Records.
	 * 
	 * @return List - all valid Records.
	 * 
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */
	@Override
	public List<Record> getAllValidRecords() throws IOException {
		final RecordDatabaseCommand rdc = new RecordDatabaseCommand(
				SocketCommand.GET_VALID_RECORDS);
		final RecordDatabaseResult res = getResultFor(rdc);
		this.log.severe("SocketClient_Buyer, getAllValidRecords" 
									+ "\nresult: " 
									+ res.getValidsRecords().size()
									+ ("(size)"));		
		final Exception e = res.getException();
		if (e != null) {
			if (e instanceof IOException) {
				this.log.severe("SocketClient_Buyer, getLocked, " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		}
		return res.getValidsRecords();
	}
	
	/**
	 * Cares for a save exit.
	 * 
	 * @throws IOException
	 * 				If problem occur within transmission.
	 */
	public void saveExit() throws IOException {		
		this.closeConnections();	
	}

	/**
	 * A helper method which closes the socket connection. 
	 *
	 * @throws IOException
	 *             If the close operation fails.
	 */
	private void closeConnections() throws IOException {
		
		while (!this.socket.isClosed()) {
			this.oos.close();
			this.ois.close();
			this.socket.close();
			this.log.severe("SocketClient_Buyer, closeConnections" + "\n- client: " + this.socket.toString()
			+ " - closed: " + this.socket.isClosed());
		}
	}

	/**
	 * Method that does the work of sending the request to the server and
	 * getting the response back. Doing any necessary conversions between a
	 * <code>suncertify.sockets.RecordDatabaseCommand</code> object, the
	 * serialized objects sent and received over the Socket, and the
	 * <code>suncertify.sockets.RecordDatabaseResult</code> object.
	 *
	 * @param command
	 *            The command to be performed on the remote database.
	 * @return RecordDatabaseResult - A value object containing the result of
	 *         the command requested.
	 * @throws IOException
	 *             A network error.
	 */
	private RecordDatabaseResult getResultFor(final RecordDatabaseCommand command) throws IOException {
		this.log.entering("SocketClient_Buyer", "getResultFor", command);
		RecordDatabaseResult result = null;
		try {
			this.oos.writeObject(command);
			result = (RecordDatabaseResult) this.ois.readObject();			
			final Exception e = result.getException();
			if (e != null) {
				if (e instanceof ClassNotFoundException) {
					this.log.severe("SocketClient_Buyer, getResultFor, " + e.getMessage());
					throw new ClassNotFoundException(e.getMessage());
				}
			}
		} catch (final ClassNotFoundException e) {
			this.log.log(Level.SEVERE, e.getMessage(), e);
			final IOException ioe = new IOException("ClassNotFoundException");
			ioe.initCause(e);
			throw ioe;
		}
		this.log.exiting("SocketClient_Buyer", "getResultFor", result);
		return result;
	}

	/**
	 * A helper method which initializes a socket connection on specified port.
	 *
	 * @throws UnknownHostException
	 *             If the ipaddress of the host could not be determined.
	 * @throws IOException
	 *             If the socket channel cannot be opened.
	 */
	private void initialize() throws UnknownHostException, IOException {
		this.log.entering("SocketClient_Buyer", "initialize");

		try {
			this.socket = new Socket(ip, port.intValue());
		} catch (final BindException e) {
			this.log.severe("SocketClient_Buyer, initialize, Exc: " 
					+ e.getMessage());
			if (countPorts >= 65000) {
				countPorts = 10000;
			}
			countPorts++;
			this.socket = new Socket(ip, port.intValue(), 
					MyInetAddress.getInetAddress(), countPorts);
		}
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.ois = new ObjectInputStream(this.socket.getInputStream());
		this.log.exiting("SocketClient_Buyer", "initialize");
	}
}
