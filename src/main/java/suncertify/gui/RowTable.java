package suncertify.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import suncertify.db.LoggerControl;

/**
 * An object of the class is a <code>javax.swing.JTable</code>. 
 * <br> As a special feature of the class is the
 * possibility to color rows.
 * 
 * @author stefan.streifeneder@gmx.de
 *
 */
public class RowTable extends JTable {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2016L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is <code>suncertify.gui.RowTable</code>
	 * .
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.RowTable"), Level.ALL);
	/**
	 * Stores all colored rows and their color.
	 */
	private Map<Integer, Color> mapRowsColors = new HashMap<Integer, Color>();

	/**
	 * Constructs a <code>javax.swing.JTable</code> object with an table model, which is
	 * assigned by the constructor argument.
	 * 
	 * @param model
	 *            The model how the table displays the values.
	 */
	public RowTable(RecordTableModel model) {
		super(model);
	}

	/**
	 * Overridden method to color a certain row and the selected row.
	 * Is never directly called.
	 * 
	 * @param renderer
	 *            A renderer object to customize the graphic.
	 * @param row
	 *            The row.
	 * @param column
	 *            The column.
	 * @return Component - The graphical component.
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, 
			int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		Color color = this.mapRowsColors.get(Integer.valueOf(row));
		if (!isRowSelected(row)) {
			c.setBackground(color == null ? getBackground() : color);
		} else
			c.setBackground(color == null ? getBackground() : color.darker());		
		return c;
	}

	/**
	 * Sets the color of a row.
	 * 
	 * @param row
	 *            The number of the row.
	 * @param color
	 *            The Color of the row.
	 */
	public void setRowColor(int row, Color color) {
		this.log.entering("RowTable", "setRowColor", 
				new Object[] { Integer.valueOf(row), color });
		this.mapRowsColors.put(Integer.valueOf(row), color);
		this.log.exiting("RowTable", "setRowColor");
	}
}
