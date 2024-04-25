package suncertify.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import suncertify.db.LoggerControl;

/**
 * An instance of the class is a <code>javax.swing.JMenu</code>. The menu possesses one
 * menu item: Help. It displays the userguide by using an object
 * of type <code>suncertify.gui.HelpHTMLFrame</code>.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuHelp extends JMenu {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2010L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.gui.MenuHelp</code>
	 * .
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuHelp"), Level.ALL);

	/**
	 * Creates a menu and adds an action listener
	 * to the menu item 'Userguide'.
	 */
	public MenuHelp() {
		this.log.entering("MenuHelp", "MenuHelp");
		this.setText("Help");
		this.setMnemonic(KeyEvent.VK_H);
		this.setToolTipText("Open with 'Alt' + H");
		final JMenuItem helpMenuItem = new JMenuItem("Userguide");
		helpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				if (ae == null) {
					return;
				}
				new HelpHTMLFrame();
			}
		});
		this.add(helpMenuItem);
		this.log.exiting("MenuHelp", "MenuHelp");
	}
}
