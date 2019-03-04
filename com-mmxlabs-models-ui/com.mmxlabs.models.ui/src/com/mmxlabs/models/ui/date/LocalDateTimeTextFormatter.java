/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.date;

import java.time.LocalDateTime;
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
public class LocalDateTimeTextFormatter extends AbstractDateTimeTextFormatter {
	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT format for both date and time parts for the default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * <li>default locale</li>
	 * </ul>
	 */
	public LocalDateTimeTextFormatter() {
		super(null, null);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
	public LocalDateTimeTextFormatter(String editPattern) {
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
	public LocalDateTimeTextFormatter(String editPattern, String displayPattern) {
		super(editPattern, displayPattern);
	}

	/**
	 * Returns the type of value this {@link ITextFormatter} handles, i.e. returns in {@link #getValue()}.<br>
	 * A DateTimeFormatter always returns a Date value.
	 * 
	 * @return The value type.
	 */
	@Override
	public Class<?> getValueType() {
		return LocalDateTime.class;
	}

	@Override
	protected Temporal createDefaultValue() {
		return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
	}

	public static @NonNull String format(final @Nullable LocalDateTime date) {
		final LocalDateTimeTextFormatter formatter = new LocalDateTimeTextFormatter();
		formatter.setValue(date);
		final String displayString = formatter.getDisplayString();
		if (displayString != null) {
			return displayString;
		}
		return "";
	}

	@Override
	public String getDefaultEditPattern() {
		return DateTimeFormatsProvider.INSTANCE.getDateTimeStringEdit();
	}
}
