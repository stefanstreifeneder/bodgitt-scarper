package suncertify.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import suncertify.db.LoggerControl;

/**
 * Provides the ipaddress of the system on which the
 * applications runs as a <code>java.lang.String</code> or an object of type
 * <code>java.net.InetAddress</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class MyInetAddress {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.MyInetAddress</code>.
	 */
	private static Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MyInetAddress"), Level.ALL);

	/**
	 * Reference to the ipaddress.
	 */
	private static InetAddress address;

	/**
	 * Stores the ipaddress as bytes.
	 */
	private static byte[] byteAddress;

	/**
	 * Stores the ipaddress as ints.
	 */
	private static int[] intAdr;

	/**
	 * Static initializer to customize the logger and to get the ipaddress of
	 * the computer.
	 */
	static {
		MyInetAddress.log = LoggerControl.getLoggerBS(
				MyInetAddress.log, Level.ALL);
		try {
			MyInetAddress.address = InetAddress.getLocalHost();
			MyInetAddress.byteAddress = MyInetAddress.address.getAddress();
			MyInetAddress.intAdr = new int[MyInetAddress.byteAddress.length];
			for (int i = 0; i < MyInetAddress.byteAddress.length; i++) {
				int i1 = MyInetAddress.byteAddress[i];
				if (i1 < 0) {
					MyInetAddress.intAdr[i] = MyInetAddress.byteAddress[i] + 256;
				} else {
					MyInetAddress.intAdr[i] = i1;
				}
			}
		} catch (UnknownHostException ex) {
			MyInetAddress.log.log(Level.SEVERE, "Unkown host", ex);
			ExceptionDialog.handleException("Unknown Host Exception!");
		}
	}

	/**
	 * Since this is a utility class (it only exists for other classes to call
	 * it's static methods), stops users creating unneeded instances of this
	 * class by creating a private constructor.
	 * 
	 */
	private MyInetAddress() {
		// unused
	}

	/**
	 * Returns the ipaddress as an formatted 
	 * <code>java.lang.String</code> "x.x.x.x"
	 * or "x.x.x.x.x.x".
	 * 
	 * @return String - Returns the ipaddress as an <code>java.lang.String</code>.
	 */
	public static String getIpAddressString() {
		MyInetAddress.log.entering("MyInetAddress", "ipAddressString");
		String ipAddress = "x.x.x.x";
		if(MyInetAddress.intAdr.length == 4){
			ipAddress = MyInetAddress.intAdr[0] + "." 
					+ MyInetAddress.intAdr[1] + "." + MyInetAddress.intAdr[2] + "."
					+ MyInetAddress.intAdr[3];
		}else if (MyInetAddress.intAdr.length == 6) {
			ipAddress = MyInetAddress.intAdr[0] + "." 
					+ MyInetAddress.intAdr[1] + "." 
					+ MyInetAddress.intAdr[2] + "."
					+ MyInetAddress.intAdr[3] + "." 
					+ MyInetAddress.intAdr[4] + "." 
					+ MyInetAddress.intAdr[5];
		}
		MyInetAddress.log.exiting("MyInetAddress", "ipAddressString", ipAddress);
		return ipAddress;
	}	
	
	/**
	 * Returns a representation of the underlying internet address
	 * in this format:<br>
	 * hostName/127.0.0.0<br>
	 * 
	 * 
	 * @return InetAddress - A representation of the internet address 
	 * (hostName/127.0.0.0).
	 */
	public static InetAddress getInetAddress(){
        return address;
    }
}
