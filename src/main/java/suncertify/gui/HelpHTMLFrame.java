package suncertify.gui;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import suncertify.db.LoggerControl;

/**
 * The class is a <code>javax.swing.JFrame</code>. It possesses
 * a reference of the class <code>suncertify.gui.HelpHTMLPanel</code>,
 * which displays the userguide.
 * 
 * @see HelpHTMLPanel
 * @author stefan.streifeneder@gmx.de
 */
public class HelpHTMLFrame extends JFrame {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 204L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.HelpHTMLFrame</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.HelpHTMLFrame"), Level.ALL);

	/**
	 * Constructs a <code>suncertify.gui.HelpHTMLFrame</code> object and calls the method
	 * <code>startHelp</code> to initialize and to display the frame.
	 */
	public HelpHTMLFrame() {
		this.log.entering("HelpHTMLFrame", "HelpHTMLFrame");
		initHelp();
		this.log.exiting("HelpHTMLFrame", "HelpHTMLFrame");
	}

	/**
	 * Initializes all components and displays the frame.
	 * It adds a menu bar and its menu item enables
	 * to close the frame by appending an action listener.
	 */
	public void initHelp() {
		this.log.entering("HelpHTMLFrame", "startHelp");
		this.setTitle("Bodgitt & Scarper, HELP - userguide");
		JMenuBar menuBar = new JMenuBar();
		JMenu quitMenu = new JMenu("Quit");
		quitMenu.setMnemonic(KeyEvent.VK_Q);
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setActionCommand("QUIT");
		quitMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("QUIT")) {
					dispose();
				}
			}
		});
		menuBar.add(quitMenu);
		this.setResizable(true);
		this.setSize(600, 400);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JScrollPane sbE = new JScrollPane(new HelpHTMLPanel());
		this.add(sbE);
		this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.setJMenuBar(menuBar);
		this.setVisible(true);
		this.log.exiting("HelpHTMLFrame", "startHelp");
	}
}
