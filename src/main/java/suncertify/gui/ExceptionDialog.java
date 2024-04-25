package suncertify.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This class shows in a dialog the message, which is
 * assigned by the method argument of the static method.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class ExceptionDialog {

	/**
	 * Private noarg constructor to avoid the instantiation
	 * of objects, which are not used. 
	 */
	private ExceptionDialog() {
		// unused
	}

	/**
	 * Shows a dialog with an exception message.
	 * 
	 * @param msg
	 *            - The exception message.
	 */
	public static void handleException(String msg) {
		JOptionPane alert = new JOptionPane("ATTENTION" + "\n"
				+ msg, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dialog = alert.createDialog(null, "Alert");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - dialog.getWidth()) / 2);
		int y = (int) ((d.getHeight() - dialog.getHeight()) / 2);
		dialog.setLocation(x, y);
		dialog.setVisible(true);
	}
}
