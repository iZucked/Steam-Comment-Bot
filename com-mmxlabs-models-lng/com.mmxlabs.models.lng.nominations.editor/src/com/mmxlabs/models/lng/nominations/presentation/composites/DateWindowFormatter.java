/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class DateWindowFormatter extends AbstractTimeWindowFormatter {

	private static final String timeWindowStringEdit = "dd/MM/yyyy +WWF";
	private static final String timeWindowStringDisplay = "dd/MM/yyyy +WWF";
	
	public DateWindowFormatter() {
		//editPattern, displayPattern
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
		final String displayString = formatter.getDisplayString();
		if (displayString != null) {
			return displayString;
		}
		return "";
	}


}
