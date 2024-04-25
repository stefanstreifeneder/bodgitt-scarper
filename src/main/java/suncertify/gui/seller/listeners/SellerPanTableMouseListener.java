package suncertify.gui.seller.listeners;

import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import suncertify.db.LoggerControl;
import suncertify.gui.ExceptionDialog;
import suncertify.gui.GuiControllerException;
import suncertify.gui.PanelTable;

/**
 * An object of the class is a <code>java.awt.event.MouseAdapter</code>. It handles
 * a mouse event of the table panel <code>suncertify.gui.PanelTable</code>. 
 * If mouse is pressed two times, the Record number
 * will be displayed in a dialog.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class SellerPanTableMouseListener extends MouseAdapter {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.seller.listeners.SellerPanTableMouseListener</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(
			Logger.getLogger(
					"suncertify.gui.seller.listeners.SellerPanTableMouseListener"), 
			Level.ALL);

	/**
	 * A reference to the table panel.
	 */
	private final PanelTable panTable;

	/**
	 * Creates an object of this class.
	 * 
	 * @param panT
	 *            A reference to the panel, which possesses the table.
	 */
	public SellerPanTableMouseListener(final PanelTable panT) {
		this.log.entering("SellerPanTableMouseListener", 
				"SellerPanTableMouseListener", panT);
		this.panTable = panT;
		this.log.exiting("SellerPanTableMouseListener", 
				"SellerPanTableMouseListener");
	}

	/**
	 * If the mouse is clicked twice on a Record, a dialog
	 * displays the Record number.
	 * 
	 * @param e
	 *            Reacts on a double click.
	 */
	@Override
	public void mouseClicked(final MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				JOptionPane.showMessageDialog(null,
						"The record has " + "the record number: " 
								+ this.panTable.getRecNo());
			} catch (final HeadlessException he) {
				ExceptionDialog.handleException(he.toString());
			} catch (final GuiControllerException gce) {
				ExceptionDialog.handleException(gce.toString());
			}
		}
	}
}
