/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Hashtable;

import org.eclipse.nebula.widgets.formattedtext.AbstractFormatter;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Derived from {@link DateTimeFormatter} to be a more generic superclass to handle different java time data types.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractDateTimeTextFormatter extends AbstractFormatter {
	/** Cache of patterns by locale ISO3 codes */
	protected Hashtable<String, String> cachedPatterns = new Hashtable<>();
	/** Numbers formatter */
	private static NumberFormat nf;
	/** Calendar containing the current value */
	protected Temporal temporal = createDefaultValue();
	/** Date formatter for display */
	protected java.time.format.DateTimeFormatter sdfDisplay;
	/** Input mask */
	protected StringBuffer inputMask;
	/** Current edited value */
	protected StringBuffer inputCache;
	/** Fields descriptions */
	protected FieldDesc[] fields;
	/** Number of fields in edit pattern */
	protected int fieldCount;
	/** Key listener on the Text widget */
	protected KeyListener klistener;
	/** Focus listener on the Text widget */
	protected FocusListener flistener;
	/** Filter for modify events */
	protected Listener modifyFilter;

	protected class FieldDesc {
		/** Time field in Calendar */
		ChronoField field;
		/** Minimum length of the field in chars */
		int minLen;
		/** Maximum length of the field in chars */
		int maxLen;
		/** true if the field is empty, else false */
		boolean empty;
		/** true if the field contains a valid value, else false */
		boolean valid;
		char index;
		int pos;
		int curLen;
	}

	protected boolean emptyStringIsValid = false;

	static {
		nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
	}

	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT format for both date and time parts for the default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * </ul>
	 */
	public AbstractDateTimeTextFormatter() {
		this(null, null);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
	public AbstractDateTimeTextFormatter(final String editPattern) {
		this(editPattern, null);
	}

	/**
	 * Constructs a new instance with the given masks and locale.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param displayPattern
	 *            display mask
	 * @param loc
	 *            locale
	 */
	public AbstractDateTimeTextFormatter(String editPattern, String displayPattern) {

		// Creates the formatter for the edit value
		if (editPattern == null) {
			editPattern = getDefaultEditPattern();
		}
		editPattern = check4DigitYear(editPattern);

		compile(editPattern);
		// Creates the formatter for the display value
		if (displayPattern == null) {
			displayPattern = editPattern;
		}
		// Ensure 4 digit year
		displayPattern = check4DigitYear(displayPattern);

		sdfDisplay = java.time.format.DateTimeFormatter.ofPattern(displayPattern);

		// Instantiate the key listener
		klistener = new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.stateMask != 0)
					return;
				switch (e.keyCode) {
				case SWT.ARROW_UP:
					arrow(1);
					break;
				case SWT.ARROW_DOWN:
					arrow(-1);
					break;
				default:
					return;
				}
				e.doit = false;
			}
		};
		// Instantiate the focus listener
		flistener = new FocusListener() {
			String lastInput;

			@Override
			public void focusGained(final FocusEvent e) {
				final int p = text.getCaretPosition();
				setInputCache();
				final String t = inputCache.toString();
				if (!t.equals(lastInput)) {
					final Display display = text.getDisplay();
					try {
						display.addFilter(SWT.Modify, modifyFilter);
						updateText(inputCache.toString(), p);
					} finally {
						display.removeFilter(SWT.Modify, modifyFilter);
					}
				}
			}

			@Override
			public void focusLost(final FocusEvent e) {
				if (text != null) {
					lastInput = text.getText();
				}
			}
		};
		modifyFilter = event -> event.type = SWT.None;

	}

	private String check4DigitYear(String str) {
		if (!str.contains("yyyy")) {
			if (str.contains("yyy")) {
				str = str.replaceAll("yyy", "yyyy");
			} else if (str.contains("yy")) {
				str = str.replaceAll("yy", "yyyy");
			} else if (str.contains("y")) {
				str = str.replaceAll("y", "yyyy");
			}
		}
		return str;
	}

	/**
	 * Adjust a field length in the mask to a given length.
	 * 
	 * @param b
	 *            begin position (inclusive)
	 * @param e
	 *            end position (exclusive)
	 * @param l
	 *            new length
	 * @return new end position
	 */
	private int ajustMask(final int b, int e, final int l) {
		final char c = inputMask.charAt(b);
		while (e - b + 1 < l) {
			inputMask.insert(e++, c);
		}
		while (e - b + 1 > l) {
			inputMask.deleteCharAt(e--);
		}
		return e;
	}

	/**
	 * Executes the increment or decrement of the date value when an arrow UP or DOWN is pressed.<br>
	 * The calendar field at the position of cursor is incremented / decremented. If the field value exceeds its range, the next larger field is incremented or decremented and the field value is
	 * adjusted back into its range.
	 * <p>
	 * 
	 * If the calendar is empty, it is initialized with the current date. In these case, the arrow is ignored.
	 * 
	 * @param inc
	 *            +1 for increment, -1 for decrement
	 */
	private void arrow(final int inc) {
		final int p = text.getCaretPosition();
		final int l = inputMask.length();
		if (p == l) {
			return;
		}
		final char m = inputMask.charAt(p);
		if (m == '*') {
			return;
		}
		final FieldDesc f = getField(p, 0);
		final int b = f.pos;
		if (countValid() == 0) {
			setValue(createDefaultValue());
		} else if (f.field == ChronoField.YEAR && f.maxLen <= 2) {
			final long year = temporal.getLong(ChronoField.YEAR) + inc;
			temporal = temporal.with(f.field, year);
		} else {
			temporal = temporal.plus(inc, f.field.getBaseUnit());
		}
		setInputCache();
		locateField(f, 0);
		ignore = true;
		updateText(inputCache.toString(), Math.min(f.pos + p - b, f.pos + f.curLen - 1));
		ignore = false;
	}

	protected abstract Temporal createDefaultValue();

	/**
	 * Clear a part of the input cache.<br>
	 * Characters are replaced by spaces in the fields, but separators are preserved.
	 * 
	 * @param b
	 *            beginning index (inclusive)
	 * @param e
	 *            end index (exclusive)
	 */
	protected void clear(final int b, int e) {
		char m;
		int i = b;
		int from = 0;
		FieldDesc field;
		while (i < e) {
			m = inputMask.charAt(i);
			if (m == '*') {
				from = ++i;
				continue;
			}
			field = getField(i, from);
			final int numCharsLeftOfRangeToClear = i - field.pos;
			while (i < e && field.curLen - numCharsLeftOfRangeToClear > 0) {
				inputCache.deleteCharAt(i);
				inputMask.deleteCharAt(i);
				e--;
				field.curLen--;
			}
			while (field.curLen < field.minLen) {
				inputCache.insert(field.pos + field.curLen, SPACE);
				inputMask.insert(field.pos + field.curLen, field.index);
				i++;
				e++;
				field.curLen++;
			}
			updateFieldValue(field, false);
		}
	}

	/**
	 * Compiles a given edit pattern, initializing <code>inputMask</code> and <code>inputCache</code>. The content of the edit pattern is analyzed char by char and the array of field descriptors is
	 * initialized. Pattern chars allowed are : y, M, d, H, h, s, S, a. The presence of other pattern chars defined in <code>SimpleDateFormat</code> will raised an
	 * <code>IllegalArgumentException</code>.
	 * 
	 * @param editPattern
	 *            edit pattern
	 * @throws IllegalArgumentException
	 *             pattern is invalid
	 */
	private void compile(final String editPattern) {
		inputMask = new StringBuffer();
		inputCache = new StringBuffer();
		fields = new FieldDesc[10];
		int fi = 0;
		final int length = editPattern.length();
		for (int i = 0; i < length; i++) {
			final char c = editPattern.charAt(i);
			int l = 1;
			while (i < length - 1 && editPattern.charAt(i + 1) == c) {
				i++;
				l++;
			}
			isValidCharPattern(c);
			switch (c) {
			case 'y': // Year
				fields[fi] = new FieldDesc();
				fields[fi].field = ChronoField.YEAR;
				fields[fi].minLen = fields[fi].maxLen = l <= 2 ? 2 : 4;

				break;
			case 'M': // Month
				fields[fi] = new FieldDesc();
				fields[fi].field = ChronoField.MONTH_OF_YEAR;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'd': // Day in month
				fields[fi] = new FieldDesc();
				fields[fi].field = ChronoField.DAY_OF_MONTH;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'H': // Hour (0-23)
			case 'h': // Hour (1-12 AM-PM)
				fields[fi] = new FieldDesc();
				fields[fi].field = ChronoField.HOUR_OF_DAY;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'm': // Minutes
				fields[fi] = new FieldDesc();
				fields[fi].field = ChronoField.MINUTE_OF_HOUR;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			default:
				for (int j = 0; j < l; j++) {
					inputMask.append('*');
					inputCache.append(c);
				}
				continue;
			}
			fields[fi].empty = true;
			fields[fi].valid = false;
			clear(fields[fi].field);
			final char k = (char) ('0' + fi);
			for (int j = 0; j < fields[fi].minLen; j++) {
				inputMask.append(k);
				inputCache.append(SPACE);
			}
			fields[fi].index = k;
			fi++;
		}
		fieldCount = fi;
	}

	/**
	 * Returns the count of valid fields. Value returned is between 0 and fieldcount.
	 * 
	 * @return Count of valid fields
	 */
	private int countValid() {
		int count = 0;
		for (int i = 0; i < fieldCount; i++) {
			if (fields[i].valid)
				count++;
		}
		return count;
	}

	/**
	 * Called when the formatter is replaced by an other one in the <code>FormattedText</code> control. Allow to release resources like additional listeners.
	 * <p>
	 * 
	 * Removes the <code>KeyListener</code> on the text widget.
	 * 
	 * @see ITextFormatter#detach()
	 */
	@Override
	public void detach() {
		text.removeKeyListener(klistener);
		text.removeFocusListener(flistener);
	}

	/**
	 * Returns the default edit pattern for the given <code>Locale</code>.
	 * <p>
	 * 
	 * A <code>DateFormat</code> object is instantiated with SHORT format for both date and time parts for the given locale. The corresponding pattern string is then retrieved by calling the
	 * <code>toPattern</code>.
	 * <p>
	 * 
	 * Default patterns are stored in a cache with ISO3 language and country codes as key. So they are computed only once by locale.
	 * 
	 * @param loc
	 *            locale
	 * @return edit pattern for the locale
	 */
	public abstract String getDefaultEditPattern();

	/**
	 * Returns the current value formatted for display. This method is called by <code>FormattedText</code> when the <code>Text</code> widget looses focus. The displayed value is the result of
	 * formatting on the <code>calendar</code> with a <code>SimpleDateFormat<code> for the display pattern passed in constructor. In case the input is invalid (eg. blanks fields), the edit string is
	 * returned in place of the display string.
	 * 
	 * @return display string if valid, edit string else
	 * @see ITextFormatter#getDisplayString()
	 */
	@Override
	public String getDisplayString() {
		if (emptyStringIsValid && isFieldsEmpty()) {
			return getEditString();
		}

		return isValid() ? sdfDisplay.format(temporal) : getEditString();
	}

	/**
	 * Returns the current value formatted for input. This method is called by <code>FormattedText</code> when the <code>Text</code> widget gains focus. The value returned is the content of the
	 * StringBuilder used as cache.
	 * 
	 * @return edit string
	 * @see ITextFormatter#getEditString()
	 */
	@Override
	public String getEditString() {
		return inputCache.toString();
	}

	/**
	 * Returns the field descriptor corresponding to a given position in the <code>inputMask</code>. The current starting position and length of the field are set in the descriptor.
	 * 
	 * @param p
	 *            position in mask of the field
	 * @param from
	 *            starting position in mask to search for the beginning of the field
	 * @return Field descriptor
	 */
	private FieldDesc getField(int p, final int from) {
		FieldDesc f;
		if (p >= inputMask.length()) {
			p = inputMask.length() - 1;
		}
		char c = inputMask.charAt(p);
		while (c == '*') {
			p--;
			if (p < 0) {
				return null;
			}
			c = inputMask.charAt(p);
		}
		f = fields[c - '0'];
		locateField(f, from);
		return f;
	}

	/**
	 * Returns a string representing the current value of a given field based on the content of the calendar.
	 * 
	 * @param f
	 *            field descriptor
	 * @return formatted value of field
	 */
	private String getFormattedValue(final FieldDesc f) {
		final StringBuffer value = new StringBuffer();
		if (f.valid) {
			final long v = temporal.getLong(f.field);
			value.append(v);
			if (value.length() > f.maxLen) {
				value.delete(0, value.length() - f.maxLen);
			} else {
				while (value.length() < f.minLen) {
					value.insert(0, '0');
				}
			}
		} else {
			while (value.length() < f.minLen) {
				value.append(SPACE);
			}
		}
		return value.toString();
	}

	/**
	 * Returns the current value of the text control if it is a valid <code>Date</code>.<br>
	 * The date is valid if all the input fields are set. If invalid, returns <code>null</code>.
	 * 
	 * @return current date value if valid, <code>null</code> else
	 * @see ITextFormatter#getValue()
	 */
	@Override
	public Temporal getValue() {
		if (isEmptyStringIsValid() && isEmpty()) {
			return null;
		}
		if (isValid()) {
			return temporal;
		}
		return null;
	}

	/**
	 * Inserts a sequence of characters in the input buffer. The current content of the buffer is override. The new position of the cursor is computed and returned.
	 * 
	 * @param txt
	 *            String of characters to insert
	 * @param p
	 *            Starting position of insertion
	 * @return New position of the cursor
	 */
	private int insert(final String txt, int p) {
		FieldDesc fd = null;
		int i = 0;
		int from = 0;
		int t;
		char c;
		char m;
		char o;
		while (i < txt.length()) {
			c = txt.charAt(i);
			if (p < inputMask.length()) {
				m = inputMask.charAt(p);
				if (m == '*' && inputCache.charAt(p) == c) {
					from = p;
					i++;
					p++;
					fd = null;
					continue;
				}
			} else {
				m = inputMask.charAt(inputMask.length() - 1);
			}
			if (fd == null) {
				fd = getField(p, from);
				for (int j = fd.pos; j < p; j++) {
					if (inputCache.charAt(j) == SPACE) {
						inputCache.setCharAt(j, '0');
					}
				}
			}
			t = Character.getType(c);
			if (t == Character.DECIMAL_DIGIT_NUMBER) {
				o = '#';
				if (m != '*' && p < inputMask.length()) {

					o = inputCache.charAt(p);
					inputCache.setCharAt(p, c);
				} else if (fd.curLen < fd.maxLen) {
					inputCache.insert(p, c);
					inputMask.insert(p, inputMask.charAt(fd.pos));
					fd.curLen++;
				} else {
					beep();
					return p;
				}
				if (!updateFieldValue(fd, true)) {
					if (o != '#') {
						inputCache.setCharAt(p, o);
					} else {
						inputCache.deleteCharAt(p);
						inputMask.deleteCharAt(p);
						fd.curLen--;
					}
					beep();
					return p;
				}
				i++;
				p++;
			} else {
				t = fd.pos + fd.curLen;
				if (t < inputCache.length() && c == inputCache.charAt(t) && i == txt.length() - 1) {
					p = getNextFieldPos(fd);
				} else {
					beep();
				}
				return p;
			}
		}
		if (fd != null && p == fd.pos + fd.curLen && fd.curLen == fd.maxLen) {
			p = getNextFieldPos(fd);
		}
		return p;
	}

	/**
	 * Returns <code>true</code> if current edited value is empty, else returns <code>false</code>.<br>
	 * For a datetime, the value is considered empty if each field composing the datetime pattern contains an empty string.
	 * 
	 * @return true if empty, else false
	 */
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < fieldCount; i++) {
			if (!fields[i].empty)
				return false;
		}
		return true;
	}

	/**
	 * Returns <code>true</code> if current edited value is valid, else returns <code>false</code>. An empty value is considered as invalid.
	 * 
	 * @return true if valid, else false
	 * @see ITextFormatter#isValid()
	 */
	@Override
	public boolean isValid() {
		if (emptyStringIsValid && isFieldsEmpty()) {
			return true;
		}
		return countValid() == fieldCount;
	}

	/**
	 * Checks if a given char is valid for the edit pattern. This method must be override to restrict the edit pattern in subclasses.
	 * 
	 * @param c
	 *            pattern char
	 * @throws IllegalArgumentException
	 *             if not valid
	 */
	protected void isValidCharPattern(final char c) {
		switch (c) {
		case 'D':
		case 'G':
		case 'w':
		case 'W':
		case 'F':
		case 'E':
		case 'k':
		case 'K':
		case 'z':
		case 'Z':
			throw new IllegalArgumentException("Invalid datetime pattern");
		}
	}

	/**
	 * Searches the current start position and length of a given field, starting search to the given index.
	 * 
	 * @param f
	 *            field descriptor
	 * @param from
	 *            index to begin to search
	 */
	private void locateField(final FieldDesc f, int from) {
		while (inputMask.charAt(from) != f.index) {
			from++;
		}
		f.pos = from;
		int l = from + 1;
		while (l < inputMask.length() && inputMask.charAt(l) == f.index) {
			l++;
		}
		f.curLen = l - from;
	}

	/**
	 * Returns the start position in cache of the next field of a given field.
	 * 
	 * @param f
	 *            field descriptor
	 * @return start position of next field
	 */
	private int getNextFieldPos(final FieldDesc f) {
		int p = f.pos + f.curLen;
		while (p < inputMask.length() && inputMask.charAt(p) == '*') {
			p++;
		}
		return p;
	}

	/**
	 * Resets the full content of the input cache, based on input mask, fields descriptors and current value of the calendar.
	 */
	protected void setInputCache() {
		int l = inputCache.length();
		for (int i = 0; i < l; i++) {
			final char c = inputMask.charAt(i);
			if (c == '*') {
				continue;
			}
			int j = i;
			while (j < l - 1 && inputMask.charAt(j + 1) == c) {
				j++;
			}
			final FieldDesc f = fields[c - '0'];
			final String value = getFormattedValue(f);
			inputCache.replace(i, j + 1, value);
			i = ajustMask(i, j, value.length());
			l = inputCache.length();
		}
	}

	/**
	 * Sets the <code>Text</code> widget that will be managed by this formatter.
	 * <p>
	 * 
	 * The ancestor is override to add a key listener on the text widget.
	 * 
	 * @param text
	 *            Text widget
	 * @see ITextFormatter#setText(Text)
	 */
	@Override
	public void setText(final Text text) {
		super.setText(text);
		text.addKeyListener(klistener);
		text.addFocusListener(flistener);
	}

	/**
	 * Sets the value to edit. The value provided must be a <code>Date</code>.
	 * 
	 * @param value
	 *            date value
	 * @throws IllegalArgumentException
	 *             if not a date
	 * @see ITextFormatter#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(final Object value) {
		if (value instanceof Temporal) {
			temporal = (Temporal) value;
			for (int i = 0; i < fieldCount; i++) {
				fields[i].valid = (value != null);
				fields[i].empty = !fields[i].valid;
			}
			setInputCache();
		} else if (value == null) {
			clear(0, inputCache.length());
		} else {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	/**
	 * Update a calendar field with the current value string of the given field in the input cache. The string value is converted according to the field type. If the conversion is invalid, or if the
	 * value is out of the field limits, it is rejected. Else the corresponding field is updated in the calendar.
	 * 
	 * If the checkLimits flag is set to true, we try to replace the last digit of the field by 0 (if over max) or a 1 (if under min). If the resulting value is valid, then the input cache is updated.
	 * 
	 * @param f
	 *            field descriptor
	 * @param checkLimits
	 *            <code>true</code> to check limits, else <code>false</code>
	 * @return <code>true</code> if calendar has been updated, <code>false</code> if value is rejected
	 */
	private boolean updateFieldValue(final FieldDesc f, final boolean checkLimits) {
		final String s = inputCache.substring(f.pos, f.pos + f.curLen).trim();
		f.empty = false;
		if (s.length() == 0 || s.indexOf(SPACE) >= 0) {
			clear(f.field);
			f.empty = true;
			f.valid = false;
		} else {
			int v = 0;
			try {
				v = nf.parse(s).intValue();
			} catch (final ParseException e) {
				e.printStackTrace(System.err);
			}

			if (f.field == ChronoField.YEAR && v < 100) {
				// SG Addition, 2 digit dates add on 2000
				v += 2000;
			}

			long min = f.field.range().getMinimum();
			long max = f.field.range().getMaximum();

			if (v < min || v > max) {
				if (!checkLimits) {
					return false;
				}
				if (v > max) {

					v = (v / 10) * 10;
					if (v > max) {
						return false;
					}
					inputCache.setCharAt(f.pos + f.curLen - 1, '0');
				} else if (f.curLen == f.maxLen) {
					v = (v / 10) * 10 + 1;
					if (v < min) {
						return false;
					}
					inputCache.setCharAt(f.pos + f.curLen - 1, '1');
				}
			}
			temporal = temporal.with(f.field, v);
			f.valid = true;
		}
		return true;
	}

	/**
	 * Handles a <code>VerifyEvent</code> sent when the text is about to be modified. This method is the entry point of all operations of formatting.
	 * 
	 * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
	 */
	@Override
	public void verifyText(final VerifyEvent e) {
		if (ignore) {
			return;
		}

		e.doit = false;
		if (e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
			clear(e.start, e.end);
		} else {
			e.start = insert(e.text, e.start);
		}
		updateText(inputCache.toString(), e.start);
	}

	protected void clear(final ChronoField field) {
		temporal = temporal.with(field, field.range().getMinimum());
	}

	protected boolean isFieldsEmpty() {
		boolean result = false;

		for (final FieldDesc fd : fields) {
			if (fd != null) {
				result = fd.empty;
				if (!result) {
					break;
				}
			}
		}
		return result;
	}

	public boolean isEmptyStringIsValid() {
		return emptyStringIsValid;
	}

	public void setEmptyStringIsValid(boolean emptyStringIsValid) {
		this.emptyStringIsValid = emptyStringIsValid;
	}
}
