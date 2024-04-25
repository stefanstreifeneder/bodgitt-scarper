package suncertify.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import suncertify.db.LoggerControl;

/**
 * The class is a <code>javax.swing.JDialog</code>, which runs during the connection phase.
 * It is only for information.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class StartDialog extends JDialog {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2017L;
	
	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.StartDialog</code>.
	 */
	private final Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.StartDialog"), Level.ALL);
	

	/**
	 * The text, which will be displayed of the dialog.
	 */
	private final String msg = "This monitor appears during Your "
			+ "connection phase."
			+ "\nIt can be seen until You have connected successfully "
			+ "\nor You exit the application. The connection dialog"
			+ "\nvanishes, after You have pressed the 'Connect' button"
			+ "\nof that dialog . Please go to the menu 'Help' for"
			+ "\nfurther informations. It can take several minutes to"
			+ "\nset up the connection. If the connection fails, which"
			+ "\nalso can take some minutes, You will see an error"
			+ "\ndialog, which enables You to connect again or You can" 
			+ "\nstart the application in a different mode."
			+ "\n";

	/**
	 * Component to display the text.
	 */
	private final JOptionPane optionPane;
	
	/**
	 * Value to determine the vertical position
	 * on screen.
	 */
	static int verti = 200;
	
	/**
	 * No-arg constructor, which creates an object of this class.
	 */
	public StartDialog() {
		this.log.entering("StartDialog", "StartDialog");
		this.optionPane = new JOptionPane(this.msg, 
				JOptionPane.INFORMATION_MESSAGE, 
				JOptionPane.DEFAULT_OPTION, 
				null,
				new Object[] {}, null);
		this.setUndecorated(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED));
		this.setTitle("Bodgitt & Scarper - Start Monitor");
		this.setModal(true);
		this.setContentPane(this.optionPane);		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.log.exiting("StartDialog", "StartDialog");
	}
	
	
	/**
	 * Sets the text of the option pane. It adds
	 * a <code>MouseAdapter</code> to exit the program,
	 * if on the dialog will be clicked two times.
	 * Additional the dialog gets a 
	 * <code>MouseWheelListener</code> to move the dialog.
	 * Further more the dialog obtains a menu bar.
	 * The method is only in use within test programs.
	 * 
	 * @param t Text of the pane.
	 */
	public void setPaneText(final String t){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					JOptionPane.showConfirmDialog(null, 
	    					"PROGRAM - STOPS NOW", 
	    					 		"StartDialog",
	    					JOptionPane.DEFAULT_OPTION);	
					
					System.exit(0);
				}
			}
		});		
		
		this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(final MouseWheelEvent event) {
            	if(event.getWheelRotation() == 1 || 
            			event.getWheelRotation() == -1){
            		
            		setLocation(0, verti);
					if(verti > 599){
						verti -= 200;
					}else if(verti < 600){
						verti += 200;
					}
            	}
            }
        });
		
		
		// new menu bar
		final JMenuBar bar = new JMenuBar();
		final JMenu mFile = new MenuFile();
		final JMenu newM = new JMenu("Placement");
		final JMenuItem itemMove = new JMenuItem("Move Dialog");
		itemMove.setActionCommand("move");
		itemMove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				if(arg0.getActionCommand().equals("move")){
					setLocation(0, verti);
					if(verti > 599){
						verti -= 200;
					}else if(verti < 600){
						verti += 200;
					}
				}
			}});
		newM.add(itemMove);
		bar.add(mFile);
		bar.add(newM);
		this.setJMenuBar(bar);
		// end new menu bar				
		this.optionPane.setMessage(t);
	}	

	/**
	 * Sets the modalitity to false to set other dialogs enabled.
	 * 
	 * @param setModal False, if other windows should have focus.
	 */
	public void setModalitiy(final boolean setModal) {
		this.setModal(setModal);
	}
}
