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
 * An instance of the class is a <code>javax.swing.JMenu</code>. The menu possesses two
 * menu items:<br>
 * Quit - to exit the application<br>
 * Author - who wrote this application
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class MenuFile extends JMenu {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 208L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.gui.MenuFile</code>
	 * .
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.MenuFile"), Level.ALL);

	/**
	 * Constructs a menu with two items 'Quit' and 'Author'.
	 * Adds an action listener to the item 'Quit'
	 * to exit the application.
	 */
	public MenuFile() {
		this.log.entering("MenuFile", "MenuFile");
		this.setText("File");
		this.setMnemonic(KeyEvent.VK_F);
		this.setToolTipText("Open with 'Alt' + F");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setActionCommand("quit");
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("quit")) {
					Logger.getAnonymousLogger().info("Menu 'File' "
							+ "- 'Quit' pressed! " 
								+ "Application stops now!");
					//System.exit(0);// may be not insufficient
					Runtime.getRuntime().halt(1);
				}
			}

		});
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		JMenuItem authorMenuItem = new JMenuItem("Author: "
				+ "stefan.streifeneder@gmx.de");
		this.add(quitMenuItem);
		this.add(authorMenuItem);
		this.log.exiting("MenuFile", "MenuFile");
	}
}
