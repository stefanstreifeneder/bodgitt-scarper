package suncertify.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

/**
 * An object of the class is a <code>javax.swing.JButton</code>.
 * The button opens a dialog to reload the database file or 
 * to change it. 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class ButtonReloadDB extends JButton{
	
	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 201L;
	
	/**
	 * Creates an button and initializes the action listener, the
	 * key listener and the action Command.
	 * 
	 * @param aL Action listener to react if action is performed.
	 * @param kLi Key listener to react if a key is pressed.
	 * @param actionCommand The action command to identify
	 * the component.
	 */
	public ButtonReloadDB(ActionListener aL, KeyListener kLi, 
			String actionCommand){
		super("Reload DB");
		this.setMnemonic(KeyEvent.VK_B);
		this.setToolTipText("Trigger with 'Alt+B");
		this.addActionListener(aL);
		this.addKeyListener(kLi);
		this.setActionCommand(actionCommand);
	}
}
