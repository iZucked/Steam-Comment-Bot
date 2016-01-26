/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Abstract event filter superclass which filters out Event objects unless they have a particular field set to one of a particular set of values.
 * 
 * Implement getEventField() to produce a filter which filters on a specific field.
 * 
 * @author mmxlabs
 * 
 * @param <T>
 */
public abstract class FieldEventFilter<T> extends BaseEventFilter {
	private final List<T> permittedValues;

	public FieldEventFilter(@Nullable final EventFilter filter, @NonNull final List<T> values) {
		super(filter);
		permittedValues = values;
	}

	public FieldEventFilter(@NonNull final List<T> values) {
		this(null, values);
	}

	@Override
	public boolean isEventDirectlyFiltered(@NonNull final LocalDate date, @NonNull final Event event) {
		return (!permittedValues.contains(getEventField(event)));
	}

	protected abstract @Nullable T getEventField(@NonNull Event event);

}