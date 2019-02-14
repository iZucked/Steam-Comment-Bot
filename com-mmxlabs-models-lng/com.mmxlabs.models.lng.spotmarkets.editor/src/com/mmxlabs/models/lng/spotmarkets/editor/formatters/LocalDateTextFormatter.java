/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.formatters;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.swt.SWT;

/**
 * Derived from {@link DateTimeFormatter}
 * 
 * @author Simon Goodall
 * 
 */
public class LocalDateTextFormatter extends AbstractDateTimeTextFormatter {
	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT format for both date and time parts for the default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * <li>default locale</li>
	 * </ul>
	 */
	public LocalDateTextFormatter() {
		super(null, null, Locale.getDefault());
	}

	/**
	 * Constructs a new instance with default edit and display masks for the given locale.
	 * 
	 * @param loc
	 *            locale
	 */
	public LocalDateTextFormatter(final Locale loc) {
		super(null, null, loc);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
	public LocalDateTextFormatter(final String editPattern) {
		super(editPattern, null, Locale.getDefault());
	}

	/**
	 * Constructs a new instance with the given edit mask and locale. Display mask is identical to the edit mask.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param loc
	 *            locale
	 */
	public LocalDateTextFormatter(final String editPattern, final Locale loc) {
		super(editPattern, null, loc);
	}

	/**
	 * Constructs a new instance with the given edit and display masks. Uses the default locale.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param displayPattern
	 *            display mask
	 */
	public LocalDateTextFormatter(final String editPattern, final String displayPattern) {
		super(editPattern, displayPattern, Locale.getDefault());
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
	public LocalDateTextFormatter(final String editPattern, final String displayPattern, final Locale loc) {
		super(editPattern, displayPattern, loc);
	}

	@Override
	protected DateFormat createDefaultDateFormat(final Locale loc) {
		return DateFormat.getDateInstance(DateFormat.SHORT, loc);
	}

	/**
	 * Returns the current value of the text control if it is a valid <code>Date</code>.<br>
	 * The date is valid if all the input fields are set. If invalid, returns <code>null</code>.
	 * 
	 * @return current date value if valid, <code>null</code> else
	 * @see ITextFormatter#getValue()
	 */
	@Override
	public Object getValue() {
		if (isValid()) {
			final LocalDate mDate = LocalDate.of(calendar.get(Calendar.YEAR), 1 + calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			return mDate;
		}
		return null;
	}

	/**
	 * Returns the type of value this {@link ITextFormatter} handles, i.e. returns in {@link #getValue()}.<br>
	 * A DateTimeFormatter always returns a Date value.
	 * 
	 * @return The value type.
	 */
	@Override
	public Class<?> getValueType() {
		return LocalDate.class;
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
		if (value instanceof LocalDate) {
			calendar.clear();
			calendar.set(Calendar.YEAR, ((LocalDate) value).getYear());
			calendar.set(Calendar.MONTH, ((LocalDate) value).getMonthValue() - 1);
			calendar.set(Calendar.DAY_OF_MONTH, ((LocalDate) value).getDayOfMonth());
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

	@Override
	protected Object createDefaultValue() {
		return LocalDate.now();
	}

	public static @NonNull String format(final @Nullable LocalDate date) {
		final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
		formatter.setValue(date);
		final String displayString = formatter.getDisplayString();
		if (displayString != null) {
			return displayString;
		}
		return "";
	}
}
