package suncertify.gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.db.LoggerControl;

/**
 * An object of the class is a <code>javax.swing.JPanel</code>. 
 * It is used by all client
 * roles (Buyer, Seller, Admin).<br>
 * The panel possesses two entry fields for name and city, a button
 * to start the search or to refresh the table and a check box to
 * determine whether an union or an intersection will be created.<br>
 * <br>
 * The class provides methods to register listeners.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class PanelSearch extends JPanel {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2012L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.PanelSearch</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.PanelSearch"), Level.ALL);

	/**
	 * Entry field for the name.
	 */
	private JTextField nameField = new JTextField(30);

	/**
	 * Entry field for the city.
	 */
	private JTextField cityField = new JTextField(30);

	/**
	 * A button to execute search operations.
	 */
	private JButton searchButton;

	/**
	 * Cares whether search logic builds an intersection or an union.
	 */
	JCheckBox checkBoxSumResults;

	/**
	 * Creates the panel.
	 * @param searchButtonKeyListener Adds a key listener to 
	 * 									the search button.
	 * @param entryFieldKeyListener Adds a key listener to 
	 * 									the entry fields.
	 * @param buttonActionListner  Adds an action listener to 
	 * 									the search button.
	 */
	public PanelSearch(KeyListener searchButtonKeyListener,
			KeyListener entryFieldKeyListener, 
				ActionListener buttonActionListner) {
		this.log.entering("PanelSearch", "PanelSearch");
		this.setLayout(new GridLayout(2, 1));
		// Panel contains in one line
		// a label 'Name', a text field to enter the searched
		// name and the 'Search/Refresh' button
		JPanel searchNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.nameField.setToolTipText("32 Signs, only UTF-8");
		this.cityField.setToolTipText("64 Signs, only UTF-8");
		// Search Button
		this.searchButton = new JButton("Search/Refresh");
		this.searchButton.setActionCommand(ActionCommandButtons.PANSEARCH.name());		
		this.searchButton.setMnemonic(KeyEvent.VK_S);
		this.searchButton.setToolTipText("All Records will be displayed, " 
					+ "if 'Name' and 'City' are empty");
		searchNamePanel.add(new JLabel("Name: "));
		searchNamePanel.add(this.nameField);
		searchNamePanel.add(this.searchButton);
		searchNamePanel.setToolTipText("Not casesenitive - " 
										+ "De or dE (same result)");
		// Panel contains in one line
		// a label 'City', a text field to enter the searched
		// city.
		JPanel searchPanelCity = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanelCity.add(new JLabel("City:    "));
		searchPanelCity.add(this.cityField);

		// checkbox to build an intersection or an union
		this.checkBoxSumResults = new JCheckBox("Built Union");
		this.checkBoxSumResults.setMnemonic('N');
		this.checkBoxSumResults
				.setToolTipText("Adds all result as opposite to built" 
						+ " the intersection of all results.");
		this.checkBoxSumResults.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (PanelSearch.this.checkBoxSumResults.isSelected()) {
						PanelSearch.this.checkBoxSumResults.setSelected(false);
					} else if (!PanelSearch.this.checkBoxSumResults.isSelected()) {
						PanelSearch.this.checkBoxSumResults.setSelected(true);
					}
				}
			}
		});
		searchPanelCity.add(this.checkBoxSumResults);
		this.add(searchNamePanel);
		this.add(searchPanelCity);
		searchPanelCity.setToolTipText("Search logic: Name and/or City");
		
		this.searchButton.addKeyListener(searchButtonKeyListener);
		this.nameField.addKeyListener(entryFieldKeyListener);
		this.cityField.addKeyListener(entryFieldKeyListener);
		this.searchButton.addActionListener(buttonActionListner);
		
		this.log.exiting("PanelSearch", "PanelSearch");
	}

	/**
	 * Returns the entered text of the <code>javax.swing.JTextField</code>
	 * <code>nameField</code>.
	 * 
	 * @return String - The name, which should be searched for.
	 */
	public String getNameField() {
		return this.nameField.getText();
	}

	/**
	 * Returns the entered text of the <code>javax.swing.JTextField</code>
	 * <code>cityField</code>.
	 * 
	 * @return String - Returns the city, which should be 
	 * searched for.
	 */
	public String getCityField() {
		return this.cityField.getText();
	}

	/**
	 * Returns the selected state of the check box, which controls to build the
	 * intersection or union of a search result. Returns true, if
	 * an union of the search results is built.
	 * 
	 * @return boolean - True if union is selected.
	 */
	public boolean getCheckBoxUnionSelected() {
		return this.checkBoxSumResults.isSelected();
	}

	/**
	 * Sets the entry for the name.
	 * 
	 * @param name
	 *            The name of the company.
	 */
	public void setFieldName(String name) {
		this.nameField.setText(name);
	}

	/**
	 * Sets the entry field for city.
	 * 
	 * @param city
	 *            The city of the improvement contractor.
	 */
	public void setFieldCity(String city) {
		this.cityField.setText(city);
	}

	/**
	 * Sets the cursor of the entry fields.
	 * 
	 * @param state
	 *            - Determines the displayed cursor.
	 */
	public void setCursorFields(int state) {
		this.nameField.setCursor(new Cursor(state));
		this.cityField.setCursor(new Cursor(state));
	}
}
