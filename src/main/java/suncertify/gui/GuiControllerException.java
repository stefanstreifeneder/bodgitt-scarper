package suncertify.gui;

/**
 * Wraps all exceptions that may occur in the packages <code>suncertify.gui.*</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class GuiControllerException extends Exception {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 203L;

	/**
	 * Creates a default <code>suncertify.gui.GuiControllerException</code> instance.
	 */
	public GuiControllerException() {
		//
	}

	/**
	 * Creates a <code>suncertify.gui.GuiControllerException</code> instance and chains an
	 * exception. 
	 * 
	 * @param e
	 *            The exception to wrap and chain.
	 */
	public GuiControllerException(Throwable e) {
		super(e);
	}
	
	/**
	 * Creates a <code>suncertify.gui.GuiControllerException</code> instance
	 * with a specified detailed message.
	 * 
	 * @param msg A detailed message.
	 */
	public GuiControllerException(String msg) {
		super(msg);
	}
}
