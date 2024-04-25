package suncertify.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;

import suncertify.db.LoggerControl;

/**
 * A <code>javax.swing.JEditorPane</code> to display the userguide, 
 * which is a html file. The class creates a reference of the 
 * class <code>suncertify.gui.LinkFollower</code>
 * to enable the buttons of the userguide.
 * 
 * @see HelpHTMLFrame
 * @author stefan.streifeneder@gmx.de
 */
public class HelpHTMLPanel extends JEditorPane {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 205L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.HelpHTMLPanel</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.HelpHTMLPanel"), Level.ALL);

	/**
	 * No-arg constructor. It initializes the panel with a call to
	 * <code>initHelpPanel</code>.
	 */
	public HelpHTMLPanel() {
		this.log.entering("HelpHTMLPanel", "HelpHTMLPanel");
		initHelpPanel();
		this.log.exiting("HelpHTMLPanel", "HelpHTMLPanel");
	}

	/**
	 * Loads the panel with the html file.
	 */
	private void initHelpPanel() {
		this.log.entering("HelpHTMLPanel", "initLabel");
		this.setEditable(false);
		URL url;
		try {
			url = new URL(new URL("file:"), "./docs/userguide.html");
		} catch (MalformedURLException ex) {
			this.log.log(Level.SEVERE, "unkown URL", ex);
			ExceptionDialog
					.handleException("Help failed! " 
							+ "The URL is unknown. " 
							+ ex.getLocalizedMessage());
			return;
		}
		try {
			this.setPage(url);
			this.addHyperlinkListener(new LinkFollower(this));
		} catch (IOException ex) {
			this.log.log(Level.SEVERE, "transmission problem", ex);
			ExceptionDialog
					.handleException("Help failed! " 
							+ "An Transmission problem occured. " 
							+ ex.getLocalizedMessage());
			return;
		}
	}
}
