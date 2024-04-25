package suncertify.gui;

/**
 * The class controls the visibility of the dialog, which is constructed by the
 * class <code>suncertify.gui.StartDialog</code>.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class StartMonitor {

	/**
	 * A reference to the dialog.
	 */
	private static StartDialog dialog;

	/**
	 * Static initializer.
	 */
	static {
		StartMonitor.dialog = new StartDialog();
		StartMonitor.dialog.setModalitiy(false);
	}

	/**
	 * Private Constructor, to avoid unneeded objects.
	 */
	private StartMonitor() {
		// unused
	}

	/**
	 * Closes the dialog.
	 */
	public static void disposeStartMonitor() {
		StartMonitor.dialog.dispose();
	}

	/**
	 * Set the dialog visible.
	 */
	public static void setVisi() {
		StartMonitor.dialog.setVisible(true);
	}
	
	
	/**
	 * Sets the option pane of the start dialog.
	 * The method is only in use within test programs.
	 * 
	 * @param text The text to display of the option pane.
	 */
	public static void setTestPane(final String text) {
		StartMonitor.dialog.setPaneText(text);		
		StartMonitor.dialog.dispose();
	}
}
