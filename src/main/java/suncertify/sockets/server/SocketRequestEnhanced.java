package suncertify.sockets.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;
import suncertify.db.RecordDatabase;
import suncertify.db.RecordNotFoundException;
import suncertify.sockets.RecordDatabaseCommand;
import suncertify.sockets.RecordDatabaseResult;

/**
 * The class is part of the enhanced server in a network environment.
 * It processes a request received over a socket connection from a client. 
 * An object of the class runs on its own thread. It is spawned from the 
 * main worker thread of the <code>java.net.ServerSocket</code> and represents a 
 * client. This class explicitly implements the 
 * application protocol.
 * <br>
 * <br>
 * The class possesses an instance variable of type 
 * <code>suncertify.db.RecordDatabase</code> instead of interface type
 * <code>suncertify.db.InterfaceClient_Admin</code>, which enables the access
 * to all provided methods of class <code>RecordDatabase</code>.
 * <br> These concerning methods for the server are:
 * <br> <code>saveExit</code> - Closes the database.
 * <br> <code>eraseLock</code> - It releases the lock on a Record
 * by Record number.  It should only used by the enhanced server.
 * server.
 * 
 * 
 * @see SocketServerEnhanced
 * @see RecordDatabase
 * @see ExitControl
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SocketRequestEnhanced extends Thread {
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.server.SocketRequestEnhanced</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.sockets.server.SocketRequestEnhanced"), 
			Level.ALL);
	
	/**
	 * True, if exit control mechanism is enabled.
	 */
	private boolean runExitControl = false;
	
	/**
	 * Counts all clients and is part of the 
	 * <code>java.lang.String</code> representation
	 * of an object of the class.
	 */
	private static long count = 1;

	/**
	 * Stores the <code>Thread</code> ID and is part of the 
	 * <code>String</code> representation of an started
	 * <code>Thread</code> object of the class.
	 */
	private final long threadIntID;

	/**
	 * Holds the socket connection to the client.
	 */
	private final Socket socket;

	/**
	 * The reference to the database.
	 */
	private final RecordDatabase recordDatabase;

	/**
	 * Stores the point in time, when the client registers and is part of 
	 * the <code>java.lang.String</code> representation
	 * of an object of the class.
	 */
	private String registrationTime = "";
	
	/**
	 * Reference to the class, which releases locks.
	 */
	ExitControl clientExit;
	
	/**
	 * Creates an object of this class.
	 * 
	 * @param dbLocation
	 *            The location of the database file on disk.
	 * @param socketClient A socket object, which represents the client.
	 * @param exitCtrl Locks will be released automatically,
	 * if a client disconnects to the server.
	 * @param passWord The password of the database.
	 * @throws IOException
	 *             Indicates there is a problem to access the data.
	 */
	public SocketRequestEnhanced(final String dbLocation
			, final Socket socketClient
			, final boolean exitCtrl
			, final String passWord) throws IOException {		
		super("SocketRequestEnhanced");
		
		this.log.entering("SocketRequestEnhanced", "SocketRequestEnhanced",
				new Object[] { dbLocation, socketClient });
		this.socket = socketClient;	
		this.recordDatabase = new RecordDatabase(dbLocation, passWord);
		this.runExitControl = exitCtrl;
		this.clientExit = new ExitControl();
		this.registrationTime = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a")
				.format(new Date(System.currentTimeMillis()));		
		this.threadIntID = Thread.currentThread().getId() + count++;
		this.log.severe(
				"SocketRequestEnhanced, Constructor, enhanced(exit control)"
						+ "\nBeware the Thread-ID -" + this.threadIntID + "- "
						+ "is provisional" 
						+ "\n" + this);
	}

	/**
	 * Required for a class that extends thread, this is the main path of
	 * execution for this thread.
	 */
	@Override
	public void run() {
		this.log.entering("SocketRequestEnhanced", "run");
		try {
			final ObjectOutputStream out = 
					new ObjectOutputStream(this.socket.getOutputStream());
			final ObjectInputStream in = 
					new ObjectInputStream(this.socket.getInputStream());
			for (;;) {
				final RecordDatabaseCommand cmdObj = 
						(RecordDatabaseCommand) in.readObject();
				out.writeObject(this.execCmdObject(cmdObj));
			}
		} catch (final Throwable e) {
			/*
			 * Using this technique would make all other obligate
			 * try/catch blocks unnecessary.
			 * 
			 * It follows the rule: IF THE RUN METHOD THROWS AN EXCEPTION,
			 * THE SOCKET TO THIS CLIENT WILL BE CLOSED!
			 * 
			 * This are the exceptions thrown by the call to readObject() with
			 * the ObjectInputStream object: 
			 * - ClassNotFoundException
			 * - InvalidClassException
			 * - StreamCorruptedException 
			 * - OptionalDataException
			 * - EOFException
			 * 
			 * This are the exceptions thrown by the call to writeObject(obj)
			 * with the ObjectOutputStream object:
			 * - InvalidClassException 
			 * - NotSerializableException 
			 * - IOException
			 * - SocketException
			 * 
			 */

			this.log.severe("SocketRequestEnhanced, run, Exception, START"
						+ "\n- Exc: " + e 
						+ "\n" + this + " - closed: " + this.socket.isClosed());
			
			// Should never happen, but if, you will know, what happened
			if( e instanceof OutOfMemoryError){
				System.out.println("SocketRequestEnhanced, run, "
						+ "e: " + e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
			
			try{
				// Releases all locks on Records.
				if(this.runExitControl){
					this.clientExit.irregularExit(
							Thread.currentThread(), this.recordDatabase);
				}
			}catch(final Exception eA){
				eA.printStackTrace();
				this.log.severe("SocketRequestEnhanced, run, "
						+ "Exception due to ExitControl: "
						+ eA.getLocalizedMessage());
			}			
						
			// A must, if connection is interrupted involuntary
			try {				
				if (!this.socket.isClosed()) {
					this.socket.close();					
				}
			} catch (final IOException ioe) {
				this.log.severe("SocketRequestEnhanced, run, "
						+ "client was not closed, Exc: " + ioe.getMessage());
			}
			
			this.log.severe("SocketRequestEnhanced, run, Exception, END"
					+ "\n- Exc: " + e					
					+ "\n- socket: " + this.socket + " - closed: " + this.socket.isClosed() 
					+ "\n- client: " + Thread.currentThread().getId());
		}//Exception end
		this.log.exiting("SocketRequestEnhanced", "run");
	}

	/**
	 * Helper method that takes the command object from the client and hands it
	 * to the database.
	 *
	 * @param recCmd
	 *            The command object from the socket client.
	 * @return Object - The return result from the database wrapped in a
	 *         <code>suncertify.sockets.RecordDatabaseResult</code> object.
	 */
	private Object execCmdObject(final RecordDatabaseCommand recCmd) {
		this.log.entering("SocketRequestEnhanced", "execCmdObject", recCmd);
		RecordDatabaseResult result =  null;
		try {
			switch (recCmd.getCommandId()) {
			case FIND:
				result = new RecordDatabaseResult(
							this.recordDatabase.findByFilter(
									recCmd.getCriteria()));
				this.log.finer("SocketRequestEnhanced, execCmdObject, FIND");
				break;
			case GET_RECORD:
				result = new RecordDatabaseResult(this.recordDatabase.getRecord(
						recCmd.getRecNo()));
				break;
			case RELEASE:
				result = new RecordDatabaseResult(this.recordDatabase.releaseRecord(
						recCmd.getRecNo(), recCmd.getCookie()));
				this.log.finer("SocketRequestEnhanced, execCmdObject," 
						+ "RELEASE: " + result.toString());
				break;
			case RENT:
				result = new RecordDatabaseResult(this.recordDatabase.reserveRecord(
						recCmd.getRecNo(), recCmd.getIdOwner(), 
							recCmd.getCookie()));
				this.log.finer("SocketRequestEnhanced, execCmdObject," 
								+ "RENT: " + result.toString());
				break;
			case ADD:
				result =  new RecordDatabaseResult();
				result.setRecNo(this.recordDatabase.addRecord(recCmd.getRecord()));
				this.log.finer("SocketRequestEnhanced, execCmdObject," 
						+ "ADD: " + result.toString());
				break;
			case MODIFY:
				result = new RecordDatabaseResult(this.recordDatabase.modifyRecord(
						recCmd.getRecord(), recCmd.getRecNo(), 
							recCmd.getCookie()));
				this.log.finer("SocketRequestEnhanced, execCmdObject," 
						+ "MODIFY: " + result.toString());
				break;
			case REMOVE:
				result = new RecordDatabaseResult(this.recordDatabase.removeRecord(
						recCmd.getRecNo(), recCmd.getCookie()));
				this.log.finer("SocketRequestEnhanced, execCmdObject," 
						+ "REMOVE: " + result.toString());
				break;
			case LOCK_RECORD:				
				final long recNoLock = recCmd.getRecNo();
				this.log.finer("SocketRequestEnhanced, exCMD, LOCK, Start, " + " client: " + this.socket
						+ " - recNo: " + recNoLock);
				// creates lock cookie, which is the return value
				long cookie = 0;
				try {
					cookie = this.recordDatabase.setRecordLocked(recNoLock);
				} catch (final Exception e) {
					if (e instanceof RecordNotFoundException) {
						this.log.log(Level.FINE, "SocketRequest: Record deleted");						
						throw new RecordNotFoundException(e.getLocalizedMessage());
					}else if (e instanceof SecurityException) {
						this.log.log(Level.FINE, "SocketRequestEnhanced, " + "LOCK, " + e.getLocalizedMessage());
						throw new SecurityException(e.getLocalizedMessage());
					}
					this.log.log(Level.SEVERE, "SocketRequest: general Exception");
					throw new Exception(e);
				}
				// cares for lock issues
				if(this.runExitControl){
					this.clientExit.fillList(
							Long.valueOf(recNoLock), Long.valueOf(cookie), 
							Thread.currentThread());
				}
				result =  new RecordDatabaseResult();
				result.setCookie(cookie);
				this.log.finer("SocketRequestEnhanced, execCmdObject," + "LOCK_RECORD: " + result.toString());
				break;
			case UNLOCK_RECORD:
				this.recordDatabase.setRecordUnlocked(recCmd.getRecNo(), recCmd.getCookie());
				result = new RecordDatabaseResult(true);
				// cares for lock issues
				if(this.runExitControl){
					this.clientExit.regularExit(Long.valueOf(recCmd.getRecNo()), 
							Long.valueOf(recCmd.getCookie()),
							Thread.currentThread());
				}
				
				this.log.finer("SocketRequestEnhanced, execCmdObject," + "UNLOCK_RECORD: " + result.toString());
				break;
			case GET_LOCKS:
				result = 
					new RecordDatabaseResult(this.recordDatabase.getLocked());
				break;					
			case GET_MEMORY:
				result = new RecordDatabaseResult();
				result.setMemory(
						this.recordDatabase.getAllocatedMemory());
				break;					
			case GET_VALID_RECORDS:
				result = new RecordDatabaseResult(
						this.recordDatabase.getAllValidRecords());
				break;
			case UNSPECIFIED:
				throw new SecurityException("The Record Command is unknown!");
			default:
				break;
			}
		}catch (final Throwable e) {
			
			//Should never happen, but if, you will know, what happened
			if(e instanceof ConcurrentModificationException){
				e.printStackTrace();
				System.exit(-1);
			}else if( e instanceof OutOfMemoryError){
				System.out.println("SocketRequestEnhanced, execCmdObject, "
						+ "e: " + e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}			
			
			this.log.severe(e.toString() + " - will be propagated.");
			// Client must not know the stack trace of the server.
			final StackTraceElement[] st = { new StackTraceElement("", "", "", 0) };
			e.setStackTrace(st);
			result = new RecordDatabaseResult((Exception)e);
		}
		this.log.exiting("SocketRequestEnhanced", "execCmdObject", result);
		return result;
	}

	/**
	 * Overridden method to return a meaningful information.
	 * 
	 * @return String - A meaningful information about an object of this class.
	 */
	@Override
	public String toString() {
		return new String("Thread-ID: " 
				+ Long.valueOf(this.threadIntID).toString()
				+ " - Client-Count: " + Long.valueOf(count).toString()
				+ " - Socket: " + this.socket.getRemoteSocketAddress().toString()
				+ " - " + this.registrationTime);
	}
}
