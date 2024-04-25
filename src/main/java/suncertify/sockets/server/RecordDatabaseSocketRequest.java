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

import suncertify.db.InterfaceClient_Admin;
import suncertify.db.LoggerControl;
import suncertify.db.RecordDatabase;
import suncertify.db.RecordNotFoundException;
import suncertify.sockets.RecordDatabaseCommand;
import suncertify.sockets.RecordDatabaseResult;

/**
 * The class is part of the server in a network environment.
 * It processes a request received over a socket connection from a client. 
 * An object of the class runs on its own thread. It is spawned from the 
 * main worker thread of the <code>java.net.ServerSocket</code> (see
 * class <code>suncertify.sockets.server.RecordDatabaseSocketServer</code>)
 * and represents a client. This class explicitly implements the 
 * application protocol.
 * 
 * 
 * @see RecordDatabaseSocketServer
 * @see RecordDatabase
 * @see ExitControl
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RecordDatabaseSocketRequest extends Thread {
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.sockets.server.RecordDatabaseSocketRequest</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.sockets.server.RecordDatabaseSocketRequest"), 
			Level.ALL);

	/**
	 * Counts all clients and is part of the <code>java.lang.String</code> 
	 * representation of an object of the class.
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
	private final InterfaceClient_Admin recordDatabase;

	/**
	 * Stores the point in time, when the client registers and is part of 
	 * the <code>java.lang.String</code> representation
	 * of an object of the class.
	 */
	private String registrationTime = "";

	/**
	 * Creates an object of the class.
	 *
	 * @param dbLocation
	 *            The location of the database file.
	 * @param socketClient
	 *            The socket end-point that listens for a client request.
	 * @param passWord The password of the database.
	 * @throws IOException
	 *             A network error or on client file access error.
	 */
	//new
	public RecordDatabaseSocketRequest(final String dbLocation, 
			final Socket socketClient, final String passWord)throws IOException {
		
		super("RecordDatabaseSocketRequest");	
		this.log.entering("RecordDatabaseSocketRequest", 
				"RecordDatabaseSocketRequest",
				new Object[] { dbLocation, socketClient });
		this.socket = socketClient;
		this.recordDatabase = new RecordDatabase(dbLocation, passWord);
		this.registrationTime = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a")
				.format(new Date(System.currentTimeMillis()));		
		this.threadIntID = Thread.currentThread().getId() + count++;
		this.log.severe(
				"RecordDatabaseSocketRequest, Constructor, regular"
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
		this.log.entering("RecordDatabaseSocketRequest", "run");
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

			this.log.severe("RecordDatabaseSocketRequest, run, Exception, START"
						+ "\n- Exc: " + e 
						+ "\n" + this + " - closed: " + this.socket.isClosed());
			
			// Should never happen, but if, you will know, what happened
			if( e instanceof OutOfMemoryError){
				System.out.println("RecordDatabaseSocketRequest, run, "
						+ "e: " + e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}			
						
			// A must, if connection is interrupted involuntary
			try {				
				if (!this.socket.isClosed()) {
					this.socket.close();					
				}
			} catch (final IOException ioe) {
				this.log.severe("RecordDatabasaeSocketRequest, run, "
						+ "client was not closed, Exc: " + ioe.getMessage());
			}
			
			this.log.severe("RecordDatabaseSocketRequest, run, Exception, END"
					+ "\n- Exc: " + e					
					+ "\n- socket: " + this.socket + " - closed: " + this.socket.isClosed() 
					+ "\n- client: " + Thread.currentThread().getId());
		}//Exception end
		this.log.exiting("RecordDatabaseSocketRequest", "run");
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
		this.log.entering("RecordDatabaseSocketRequest", "execCmdObject", recCmd);
		RecordDatabaseResult result =  null;
		try {
			switch (recCmd.getCommandId()) {
			case FIND:
				result = new RecordDatabaseResult(
						this.recordDatabase.findByFilter(
								recCmd.getCriteria()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject, FIND");
				break;
			case GET_RECORD:
				result = new RecordDatabaseResult(this.recordDatabase.getRecord(
						recCmd.getRecNo()));
				break;
			case RELEASE:
				result = new RecordDatabaseResult(this.recordDatabase.releaseRecord(
						recCmd.getRecNo(), recCmd.getCookie()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
						+ "RELEASE: " + result.toString());
				break;
			case RENT:
				result = new RecordDatabaseResult(this.recordDatabase.reserveRecord(
						recCmd.getRecNo(), recCmd.getIdOwner(), 
							recCmd.getCookie()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
								+ "RENT: " + result.toString());
				break;
			case ADD:
				result =  new RecordDatabaseResult();
				result.setRecNo(this.recordDatabase.addRecord(recCmd.getRecord()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
						+ "ADD: " + result.toString());
				break;
			case MODIFY:
				result = new RecordDatabaseResult(this.recordDatabase.modifyRecord(
						recCmd.getRecord(), recCmd.getRecNo(), 
							recCmd.getCookie()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
						+ "MODIFY: " + result.toString());
				break;
			case REMOVE:
				result = new RecordDatabaseResult(this.recordDatabase.removeRecord(
						recCmd.getRecNo(), recCmd.getCookie()));
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
						+ "REMOVE: " + result.toString());
				break;
			case LOCK_RECORD:				
				final long recNoLock = recCmd.getRecNo();
				this.log.finer("RecordDatabaseSocketRequest, exCMD, LOCK, Start, " + " client: " + this.socket
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
						this.log.log(Level.FINE, "RecordDatabaseSocketRequest, " + "LOCK, " + e.getLocalizedMessage());
						throw new SecurityException(e.getLocalizedMessage());
					}
					this.log.log(Level.SEVERE, "SocketRequest: general Exception");
					throw new Exception(e);
				}
				result =  new RecordDatabaseResult();
				result.setCookie(cookie);
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," + "LOCK_RECORD: " + result.toString());
				break;
			case UNLOCK_RECORD:
				this.recordDatabase.setRecordUnlocked(recCmd.getRecNo(), recCmd.getCookie());
				result = new RecordDatabaseResult(true);
				this.log.finer("RecordDatabaseSocketRequest, execCmdObject," 
						+ "UNLOCK_RECORD: " + result.toString());
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
				e.printStackTrace();
				System.exit(-1);
			}			
			
			this.log.severe(e.toString() + " - will be propagated.");
			// Client must not know the stack trace of the server.
			final StackTraceElement[] st = { new StackTraceElement("", "", "", 0) };
			e.setStackTrace(st);
			result = new RecordDatabaseResult((Exception)e);
		}
		this.log.exiting("RecordDatabaseSocketRequest", "execCmdObject", result);
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
