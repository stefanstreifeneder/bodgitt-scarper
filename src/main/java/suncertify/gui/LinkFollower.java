package suncertify.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import suncertify.db.LoggerControl;

/**
 * An object of this class is a <code>javax.swing.event.HyperlinkListener</code>. 
 * It is used by the class <code>suncertify.gui.HelpHTMLPanel</code> to 
 * update links in the HTML file.
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class LinkFollower implements HyperlinkListener {

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.LinkFollower</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.LinkFollower"), Level.ALL);

	/**
	 * A Panel to display the userguide.
	 */
	private JEditorPane pane;

	/**
	 * Constructs a <code>suncertify.gui.LinkFollower</code> object
	 * and initializes the <code>javax.swing.JEditorPane</code> 
	 * instance variable.
	 * 
	 * @param paneParam
	 *            The panel, which contains the html code.
	 */
	public LinkFollower(JEditorPane paneParam) {
		this.log.entering("LinkFollower", "LinkFollower", paneParam);
		this.pane = paneParam;
	}

	/**
	 * Overrides the method of <code>javax.swing.event.HyperlinkListener</code>.
	 * 
	 * @param evt
	 *            If action is performed.
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent evt) {
		if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				this.pane.setPage(evt.getURL());
			} catch (IOException ex) {
				ExceptionDialog.handleException("This HTML tag can not be " 
						+ "proceeded!");
				this.log.severe("LinkFollower, hyperlinkUpdateUpdate, "
						+ "Eception: " + ex.getLocalizedMessage());
				return;
			}
		}
	}
}