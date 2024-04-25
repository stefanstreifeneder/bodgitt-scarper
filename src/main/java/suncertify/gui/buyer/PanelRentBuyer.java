package suncertify.gui.buyer;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import suncertify.db.LoggerControl;
import suncertify.gui.ActionCommandButtons;

/**
 * An instance of the class is a <code>javax.swing.JPanel</code>. The panel contains two
 * buttons to rent a Record and to release a Record of a rented state.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class PanelRentBuyer extends JPanel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 213L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.buyer.PanelRentBuyer</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.buyer.PanelRentBuyer"), Level.ALL);

	/**
	 * A reference to the rent button. 
	 */
	private JButton rentButton;

	/**
	 * A reference to the return button. 
	 */
	private JButton returnButton;

	/**
	 * Constructs a <code>javax.swing.JPanel</code>, which possesses
	 * two buttons to rent a Record and to release it.
	 * @param aL Action listener to react, if a button is pressed.
	 * @param ma Key listener to react, if a button is pressed.
	 */
	public PanelRentBuyer(ActionListener aL, KeyAdapter ma) {
		this.log.entering("PanelRentBuyer", "PanelRentBuyer");
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.rentButton = new JButton("Rent Record");		
		this.rentButton.setActionCommand(ActionCommandButtons.PANRR_RENT.name());
		this.rentButton.setMnemonic(KeyEvent.VK_R);
		this.returnButton = new JButton("Release Record");
		this.returnButton.setActionCommand(
				ActionCommandButtons.PANRR_RELEASE.name());
		this.returnButton.setMnemonic(KeyEvent.VK_L);
		JPanel hiringPanel = new JPanel();
		hiringPanel.add(this.rentButton);
		hiringPanel.add(this.returnButton);
		this.returnButton.setToolTipText("Return the Record item selected in the "
				+ "above table.");
		this.rentButton.setToolTipText("Rent the Record item selected in the above "
				+ "table.");
		this.add(hiringPanel);		
		this.returnButton.addActionListener(aL);
		this.rentButton.addActionListener(aL);		
		this.rentButton.addKeyListener(ma);
		this.returnButton.addKeyListener(ma);		
		this.log.exiting("PanelRentBuyer", "PanelRentBuyer");
	}
}
