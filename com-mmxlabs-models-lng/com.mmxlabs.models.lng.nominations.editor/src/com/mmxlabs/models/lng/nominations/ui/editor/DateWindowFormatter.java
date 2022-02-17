/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editor;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class DateWindowFormatter extends AbstractTimeWindowFormatter {

	private static final String timeWindowStringEdit = "dd/MM/yyyy +WWF";
	private static final String timeWindowStringDisplay = "dd/MM/yyyy +WWF";

	public DateWindowFormatter() {
		// editPattern, displayPattern
		super(timeWindowStringEdit, timeWindowStringDisplay);
	}

	@Override
	public Class<?> getValueType() {
		return TimeWindowHolder.class;
	}

	@Override
	protected TimeWindowHolder createDefaultValue() {
		return new TimeWindowHolder(LocalDate.now(), 24, 'h');
	}

	@Override
	public String getDefaultEditPattern() {
		return timeWindowStringEdit;
	}

	public static @NonNull String format(final @Nullable TimeWindowHolder timeWindow) {
		final DateWindowFormatter formatter = new DateWindowFormatter();
		formatter.setValue(timeWindow);
		String displayString = formatter.getDisplayString();
		if (displayString != null) {
			String[] dateWindow = displayString.split("\\+");
			if (dateWindow.length == 2 && dateWindow[1].length() == 2) {
				// Ensure 2 digits to prevent units being removed when editing.
				displayString = dateWindow[0] + "+0" + dateWindow[1];
			}
			return displayString;
		}
		return "";
	}
}