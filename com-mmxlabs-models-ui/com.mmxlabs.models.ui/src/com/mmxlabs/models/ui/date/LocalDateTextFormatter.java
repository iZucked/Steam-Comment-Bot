/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.LocalDate;
import java.time.temporal.Temporal;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;

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
		super(null, null);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
	public LocalDateTextFormatter(final String editPattern) {
		super(editPattern, null);
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
		super(editPattern, displayPattern);
	}

	public LocalDateTextFormatter(boolean emptyStringIsValid) {
		super();
		setEmptyStringIsValid(emptyStringIsValid);
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

	@Override
	protected Temporal createDefaultValue() {
		return LocalDate.now();
	}

	@Override
	public String getDefaultEditPattern() {
		return DateTimeFormatsProvider.INSTANCE.getDateStringEdit();
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
