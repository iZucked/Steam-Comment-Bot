/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.formatters;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.nebula.widgets.formattedtext.AbstractFormatter;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
	protected Calendar calendar;
	/** Date formatter for display */
	protected SimpleDateFormat sdfDisplay;
	/** Input mask */
	protected StringBuffer inputMask;
	/** Current edited value */
	protected StringBuffer inputCache;
	/** Fields descriptions */
	protected FieldDesc[] fields;
	/** Number of fields in edit pattern */
	protected int fieldCount;
	/** Year limit for 2 digits year field */
	protected int yearStart = 0;
	/** Key listener on the Text widget */
	protected KeyListener klistener;
	/** Focus listener on the Text widget */
	protected FocusListener flistener;
	/** Filter for modify events */
	protected Listener modifyFilter;
	/** The Locale used by this formatter */
	protected Locale locale;

	protected class FieldDesc {
		/** Time field in Calendar */
		int field;
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

	static {
		nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
	}

	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT format for both date and time parts for the default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * <li>default locale</li>
	 * </ul>
	 */
	public AbstractDateTimeTextFormatter() {
		this(null, null, Locale.getDefault());
	}

	/**
	 * Constructs a new instance with default edit and display masks for the given locale.
	 * 
	 * @param loc
	 *            locale
	 */
	public AbstractDateTimeTextFormatter(final Locale loc) {
		this(null, null, loc);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
	public AbstractDateTimeTextFormatter(final String editPattern) {
		this(editPattern, null, Locale.getDefault());
	}

	/**
	 * Constructs a new instance with the given edit mask and locale. Display mask is identical to the edit mask.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param loc
	 *            locale
	 */
	public AbstractDateTimeTextFormatter(final String editPattern, final Locale loc) {
		this(editPattern, null, loc);
	}

	/**
	 * Constructs a new instance with the given edit and display masks. Uses the default locale.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param displayPattern
	 *            display mask
	 */
	public AbstractDateTimeTextFormatter(final String editPattern, final String displayPattern) {
		this(editPattern, displayPattern, Locale.getDefault());
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
	public AbstractDateTimeTextFormatter(String editPattern, String displayPattern, final Locale loc) {
		// Set the default value
		calendar = Calendar.getInstance(loc);
		if (yearStart == -1) {
			calendar.setTime(sdfDisplay.get2DigitYearStart());
			yearStart = calendar.get(Calendar.YEAR) % 100;
		}
		calendar.setTimeInMillis(0);
		// Creates the formatter for the edit value
		if (editPattern == null) {
			editPattern = getDefaultEditPattern(loc);
		}
		compile(editPattern);
		// Creates the formatter for the display value
		if (displayPattern == null) {
			displayPattern = editPattern;
		}
		sdfDisplay = new SimpleDateFormat(displayPattern, loc);
		{
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			c.clear();
			c.set(Calendar.YEAR, 2000);
			sdfDisplay.set2DigitYearStart(c.getTime());
			sdfDisplay.setLenient(false);
		}
		locale = loc;
		// Instantiate the key listener
		klistener = new KeyListener() {
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

			@Override
			public void keyReleased(final KeyEvent e) {
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
		modifyFilter = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.type = SWT.None;
			}
		};
		setTimeZone(TimeZone.getTimeZone("UTC"));

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
		if (p == l)
			return;
		final char m = inputMask.charAt(p);
		if (m == '*')
			return;
		final FieldDesc f = getField(p, 0);
		final int b = f.pos;
		if (countValid() == 0) {
			setValue(createDefaultValue());
		} else if (f.field == Calendar.YEAR && f.maxLen <= 2) {
			int year = calendar.get(Calendar.YEAR) % 100 + inc;
			year += year >= yearStart ? 1900 : 2000;
			calendar.set(f.field, year);
		} else {
			calendar.add(f.field, inc);
		}
		setInputCache();
		locateField(f, 0);
		ignore = true;
		updateText(inputCache.toString(), Math.min(f.pos + p - b, f.pos + f.curLen - 1));
		ignore = false;
	}

	protected abstract Object createDefaultValue();

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
		int i = b, from = 0;
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
				fields[fi].field = Calendar.YEAR;
				fields[fi].minLen = fields[fi].maxLen = l <= 2 ? 2 : 4;
				if (fields[fi].maxLen == 2) {
					yearStart = -1;
				}
				break;
			case 'M': // Month
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.MONTH;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'd': // Day in month
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.DAY_OF_MONTH;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'H': // Hour (0-23)
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.HOUR_OF_DAY;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'h': // Hour (1-12 AM-PM)
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.HOUR;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'm': // Minutes
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.MINUTE;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 's': // Seconds
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.SECOND;
				fields[fi].minLen = Math.min(l, 2);
				fields[fi].maxLen = 2;
				break;
			case 'S': // Milliseconds
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.MILLISECOND;
				fields[fi].minLen = Math.min(l, 3);
				fields[fi].maxLen = 3;
				break;
			case 'a': // AM-PM marker
				fields[fi] = new FieldDesc();
				fields[fi].field = Calendar.AM_PM;
				fields[fi].minLen = fields[fi].maxLen = 2;
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
			calendar.clear(fields[fi].field);
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
	public String getDefaultEditPattern(Locale loc) {
		if (loc == null) {
			loc = Locale.getDefault();
		}
		final String key = "DT" + loc.toString();
		String pattern = (String) cachedPatterns.get(key);
		if (pattern == null) {
			final DateFormat df = createDefaultDateFormat(loc);
			if (!(df instanceof SimpleDateFormat)) {
				throw new IllegalArgumentException("No default pattern for locale " + loc.getDisplayName());
			}
			final StringBuffer buffer = new StringBuffer();
			buffer.append(((SimpleDateFormat) df).toPattern());
			int i;
			if (buffer.indexOf("yyy") < 0 && (i = buffer.indexOf("yy")) >= 0) {
				buffer.insert(i, "yy");
			}
			pattern = buffer.toString();
			cachedPatterns.put(key, pattern);
		}
		return pattern;
	}

	/**
	 * Returns the current value formatted for display. This method is called by <code>FormattedText</code> when the <code>Text</code> widget looses focus. The displayed value is the result of
	 * formatting on the <code>calendar</code> with a <code>SimpleDateFormat<code> for the display pattern passed in
	 * constructor. In case the input is invalid (eg. blanks fields), the edit
	 * string is returned in place of the display string.
	 * 
	 * @return display string if valid, edit string else
	 * @see ITextFormatter#getDisplayString()
	 */
	@Override
	public String getDisplayString() {
		return isValid() ? sdfDisplay.format(calendar.getTime()) : getEditString();
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
			int v = calendar.get(f.field);
			switch (f.field) {
			case Calendar.MONTH:
				v++;
				break;
			case Calendar.HOUR:
				if (v == 0)
					v = 12;
				break;
			case Calendar.AM_PM:
				return sdfDisplay.getDateFormatSymbols().getAmPmStrings()[v];
			default:
				break;
			}
			value.append(v);
			if (value.length() > f.maxLen) {
				value.delete(0, value.length() - f.maxLen);
			} else
				while (value.length() < f.minLen) {
					value.insert(0, '0');
				}
		} else {
			while (value.length() < f.minLen) {
				value.append(SPACE);
			}
		}
		return value.toString();
	}

	/**
	 * Returns the current Locale used by this formatter.
	 * 
	 * @return Current Locale used
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Returns the current value of the text control if it is a valid <code>Date</code>.<br>
	 * The date is valid if all the input fields are set. If invalid, returns <code>null</code>.
	 * 
	 * @return current date value if valid, <code>null</code> else
	 * @see ITextFormatter#getValue()
	 */
	@Override
	public abstract Object getValue();

	/**
	 * Returns the type of value this {@link ITextFormatter} handles, i.e. returns in {@link #getValue()}.<br>
	 * A DateTimeFormatter always returns a Date value.
	 * 
	 * @return The value type.
	 */
	@Override
	public abstract Class<?> getValueType();

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
		int i = 0, from = 0, t;
		char c, m, o;
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
			if (t == Character.DECIMAL_DIGIT_NUMBER && fd.field != Calendar.AM_PM) {
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
				// if ( ! updateFieldValue(fd, p < fd.pos + fd.curLen - 1) ) {
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
			} else if (fd.field == Calendar.AM_PM && (t == Character.UPPERCASE_LETTER || t == Character.LOWERCASE_LETTER)) {
				final String[] ampm = sdfDisplay.getDateFormatSymbols().getAmPmStrings();
				if (Character.toLowerCase(c) == Character.toLowerCase(ampm[0].charAt(0))) {
					t = 0;
				} else if (Character.toLowerCase(c) == Character.toLowerCase(ampm[1].charAt(0))) {
					t = 1;
				} else {
					beep();
					return p;
				}
				inputCache.replace(fd.pos, fd.pos + fd.curLen, ampm[t]);
				while (fd.curLen < ampm[t].length()) {
					inputMask.insert(p, m);
					fd.curLen++;
				}
				while (fd.curLen > ampm[t].length()) {
					inputMask.deleteCharAt(p);
					fd.curLen--;
				}
				updateFieldValue(fd, false);
				i++;
				p = fd.pos + fd.curLen;
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
	 * Sets a new <code>Locale</code> on this formatter.
	 * 
	 * @param loc
	 *            locale
	 */
	public void setLocale(final Locale loc) {
		sdfDisplay.setDateFormatSymbols(new DateFormatSymbols(loc));
		final Calendar newCal = Calendar.getInstance(calendar.getTimeZone(), loc);
		newCal.setTimeInMillis(0);
		for (int i = 0; i < fieldCount; i++) {
			if (fields[i].valid) {
				newCal.set(fields[i].field, calendar.get(fields[i].field));
			} else {
				newCal.clear(fields[i].field);
			}
		}
		calendar = newCal;
		locale = loc;
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
	 * Sets the time zone with the given time zone value. The time zone is applied to both the <code>Calendar</code> used as value cache, and the <code>SimpleDateFormat</code> used for display mask.
	 * 
	 * @param zone
	 *            Time zone
	 */
	public void setTimeZone(final TimeZone zone) {
		if (zone == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		sdfDisplay.setTimeZone(zone);
		calendar.setTimeZone(zone);
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
	public abstract void setValue(Object value);

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
			calendar.clear(f.field);
			f.empty = true;
			f.valid = false;
		} else if (f.field == Calendar.AM_PM) {
			calendar.set(f.field, sdfDisplay.getDateFormatSymbols().getAmPmStrings()[0].equals(s) ? 0 : 1);
			f.valid = true;
		} else {
			int v = 0;
			try {
				v = nf.parse(s).intValue();
			} catch (final ParseException e) {
				e.printStackTrace(System.err);
			}
			if (v == 0 && f.field <= Calendar.DAY_OF_MONTH && s.length() < f.maxLen) {
				calendar.clear(f.field);
				f.valid = false;
			} else {
				if (f.field == Calendar.YEAR && f.maxLen <= 2) {
					v += v >= yearStart ? 1900 : 2000;
				} else if (f.field == Calendar.YEAR) {
					// SG Addition, 2 digit dates add on 2000
					if (v < 100) {
						v += 2000;
					}
				} else if (f.field == Calendar.HOUR && v == 12) {
					v = 0;
				}
				int min = calendar.getActualMinimum(f.field);
				int max = calendar.getActualMaximum(f.field);
				if (f.field == Calendar.MONTH) {
					min++;
					max++;
				}
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
				calendar.set(f.field, f.field == Calendar.MONTH ? v - 1 : v);
				f.valid = true;
			}
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

	protected abstract DateFormat createDefaultDateFormat(Locale loc);

}
