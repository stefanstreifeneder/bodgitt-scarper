package suncertify.gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import suncertify.db.LoggerControl;

/**
 * An object of the class is a <code>javax.swing.JScrollPanel</code>. It has a
 * <code>javax.swing.JTable</code>, which is subtype of class 
 * <code>suncertify.gui.RowTable</code> to display the Records.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class PanelTable extends JScrollPane {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2013L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.PanelTable</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.PanelTable"), Level.ALL);

	/**
	 * A reference to the table.
	 */
	private RowTable table = new RowTable(new RecordTableModel());

	/**
	 * The actual row number.
	 */
	int index = 0;

	/**
	 * A reference to the table model.
	 */
	RecordTableModel model;

	/**
	 * Constructs a scroll panel with a table. 
	 * 
	 * @param mod
	 *            An table model object to fill the table.
	 */
	public PanelTable(TableModel mod) {
		this.log.entering("PanelTable", "PanelTable", mod);
		this.setViewportView(this.table);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setToolTipText("Double click on a row displays "
				+ "the Record number");
		this.model = (RecordTableModel) mod;
		setModelEx(this.model);
		this.log.exiting("PanelTable", "PanelTable");
	}

	/**
	 * Adds a <code>java.awt.event.MouseListener</code> to the table.
	 * 
	 * @param ev
	 *            To handle a mouse event.
	 */
	public void setMouseListener(MouseAdapter ev) {
		this.table.addMouseListener(ev);
	}

	/**
	 * Adds a <code>java.awt.event.KeyAdapter</code> to the table.
	 * 
	 * @param ev
	 *            To handle key strokes.
	 */
	public void setKeyListener(KeyAdapter ev) {
		this.table.addKeyListener(ev);
	}

	/**
	 * Set the focus of the table.
	 * 
	 * @param focus
	 *            Indicates the focus state.
	 */
	public void setFocus(boolean focus) {
		this.table.setFocusable(focus);
	}

	/**
	 * Returns whether the table is focusable.
	 * 
	 * @return boolean - Indicates the focus state of the table.
	 */
	public boolean isFocused() {
		return this.table.isFocusable();
	}

	/**
	 * Requests the focus.
	 * 
	 * @param request
	 *            Indicates whether the table gets focus.
	 */
	public void setRequest(boolean request) {
		this.table.requestFocus(request);
	}

	/**
	 * Sets the table model.
	 * 
	 * @param mod
	 *            The table model.
	 */
	public void setModelEx(TableModel mod) {
		this.model = (RecordTableModel) mod;
		if (this.model.getRowCount() != 0) {
			if ((this.index >= 0) && (this.index <= this.table.getRowCount())) {
				this.table.setModel(this.model);
			}
		}
		this.table.setModel(this.model);
	}

	/**
	 * Returns the Record number of the selected Record of the table.
	 * 
	 * @return long - The Record number of the selected row.
	 * @throws GuiControllerException
	 *             Thrown, if no row is selected or the Record number could 
	 *             not been found.
	 */
	public long getRecNo() throws GuiControllerException {
		long recNo = 0;
		this.index = this.getRowSelected();
		if (this.index == -1) {
			throw new GuiControllerException(
					new SecurityException("Table: Nothing selected"));
		} else if ((this.index >= 0) && 
				(this.index <= this.table.getRowCount())) {
			recNo = this.model.getRecNo(Integer.valueOf(this.index));
		} else {
			throw new GuiControllerException(
					new SecurityException("Table: Can NOT cumpute the Record number!"));
		}
		return recNo;
	}

	/**
	 * Returns a <code>java.lang.String</code> array, which represents the values of a
	 * Record.
	 * 
	 * @return String[] - Values of the Record.
	 * 
	 * @throws GuiControllerException
	 *             Wraps a <code>java.lang.SecurityException</code>.
	 */
	public String[] getRecord() throws GuiControllerException {
		this.index = this.table.getSelectedRow();
		String[] record = new String[this.table.getColumnCount()];
		if (this.index == -1) {
			throw new GuiControllerException(
					new SecurityException("Table: Nothing selected"));
		} else if ((this.index >= 0) && 
				(this.index <= this.table.getRowCount())) {

			for (int i = 0; i < record.length; i++) {
				record[i] = (String) this.table.getValueAt(this.index, i);
			}
		} else {
			throw new GuiControllerException(
					new SecurityException("Table: Can NOT cumpute "
							+ "the Record number!"));
		}
		return record;
	}

	/**
	 * Sets the row selected.
	 * 
	 * @param rowNumber
	 *            The number of the row, which should be selected.
	 */
	public void setRowSelect(int rowNumber) {
		int allRecs = this.table.getRowCount() - 1;
		if ((rowNumber >= 0) && (rowNumber <= allRecs)) {		
			this.table.setRowSelectionInterval(rowNumber, rowNumber);
			this.index = rowNumber;
		}else if( (rowNumber >= allRecs)  && ( allRecs > 0) ){
			this.table.setRowSelectionInterval(allRecs, allRecs);
			this.index = allRecs;
		}else if( allRecs == 0){
			this.table.setRowSelectionInterval(0, 0);
			this.index = allRecs;
		}
	}

	/**
	 * Returns the row number of the selected row.
	 * 
	 * @return int - The selected row number.
	 */
	public int getRowSelected() {
		this.index = this.table.getSelectedRow();
		if (!(this.index >= 0) && (this.index <= this.table.getRowCount())) {
			this.index = 0;
		}
		return this.index;
	}

	/**
	 * Sets the color of a row.
	 * 
	 * @param row
	 *            The row number, which should be colored.
	 * @param col
	 *            The color of the row.
	 */
	public void setRowTabelColor(int row, Color col) {
		this.table.setRowColor(row, col);
	}

	/**
	 * Returns true, if the table has focus.
	 * 
	 * @return boolean - True, if the table has focus.
	 */
	public boolean tableHasFocus() {
		return this.table.hasFocus();
	}
}
