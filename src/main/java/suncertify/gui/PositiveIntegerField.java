package suncertify.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import suncertify.db.LoggerControl;

/**
 * Extension of <code>javax.swing.JTextField</code>, which only allows positive integer
 * numbers to be entered. The method <code>setColumns</code> is not overridden,
 * the class cares for the functionality behind the graphic.
 * 
 * 
 * @author stefan.streifeneder@gmx.de
 */
public class PositiveIntegerField extends JTextField {

	/**
	 * A version number for this class so that serialization can occur without
	 * worrying about the underlying class changing between serialization and
	 * deserialization.
	 */
	private static final long serialVersionUID = 2014L;

	/**
	 * The Logger instance. All log messages from this class are routed through
	 * this member. The Logger namespace is
	 * <code>suncertify.gui.PositiveIntegerField</code>.
	 */
	private Logger log = LoggerControl.getLoggerBS(Logger.getLogger(
			"suncertify.gui.PositiveIntegerField"), Level.ALL);

	/**
	 * The maximum size of the entered number.
	 */
	int maxSizeNumber;

	/**
	 * Size of all counted columns.
	 */
	int countColumns;

	/**
	 * Constructs a new empty <code>suncertify.gui.PositiveIntegerField</code>.
	 * The maximum size is determinate by the first argument and the maximum
	 * number of columns is determinate by the second constructor argument.
	 * 
	 * @param maxNumberSize
	 *            The number of columns to use to calculate the preferred width.
	 * @param columnCountSize
	 *            Maximum length of the number.
	 */
	public PositiveIntegerField(int maxNumberSize, int columnCountSize) {
		this.log.entering("PositiveIntegerField", "PositiveIntegerField",
				new Object[] { Integer.valueOf(maxNumberSize), 
						Integer.valueOf(columnCountSize) });
		this.maxSizeNumber = maxNumberSize;
		this.countColumns = columnCountSize;
		this.log.exiting("PositiveIntegerField", "PositiveIntegerField");
	}

	/**
	 * Creates the default implementation of the model to be used at
	 * construction. An instance of 
	 * <code>suncertify.gui.PositiveIntegerField.NumericDocument</code> is returned.
	 *
	 * @return Document - A document which only allows positive integers.
	 */
	@Override
	protected Document createDefaultModel() {
		return new NumericDocument();
	}

	/**
	 * A document that only allows positive integer numbers to be entered.
	 *
	 * @see javax.swing.text.PlainDocument for further implementation issues and
	 *      issues with Serialization.
	 */
	class NumericDocument extends PlainDocument {
		/**
		 * A version number for this class so that serialization can occur
		 * without worrying about the underlying class changing between
		 * serialization and deserialization.
		 */
		private static final long serialVersionUID = 20011L;

		/**
		 * Noarg Constructor.
		 */
		NumericDocument() {
			//
		}

		/**
		 * Inserts content into the document.
		 * 
		 * @param offs
		 *            The starting offset.
		 * @param str
		 *            The number as a <code>java.lang.String</code> to insert.
		 * @param a
		 *            The attributes for the inserted content.
		 * @throws BadLocationException
		 *             The given insert position is not a valid position within
		 *             the document.
		 * @throws IllegalArgumentException
		 *             If the signs are not appropriate.
		 */
		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException, IllegalArgumentException {
			if (str == null) {
				return;
			}
			char[] input = str.toCharArray();
			int result = 0;
			boolean valid = false;
			for (int i = 0; i < input.length; i++) {
				if (Character.isDigit(input[i])) {
					result = result * 10 + Character.digit(input[i], 10);
				} else {
					return;
				}
			}
			String s = this.getText(0, this.getLength());
			if (s == null) {
				return;
			}
			StringBuilder sb = new StringBuilder(s);
			sb.insert(offs, Integer.toString(result));
			String s1 = new String(sb);
			if (!(s1.length() == 0) && 
					s1.length() > PositiveIntegerField.this.countColumns) {
				return;
			}
			int iI = Integer.parseInt(s1);
			if (iI == 0 | iI > PositiveIntegerField.this.maxSizeNumber) {
				return;
			}
			valid = true;
			if (valid) {
				super.insertString(offs, new String("" + result), a);
			}
		}
	}
}
