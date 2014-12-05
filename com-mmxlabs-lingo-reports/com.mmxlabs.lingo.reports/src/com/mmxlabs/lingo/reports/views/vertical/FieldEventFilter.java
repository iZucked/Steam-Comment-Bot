package com.mmxlabs.lingo.reports.views.vertical;

import java.util.Date;
import java.util.List;

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
public abstract  class FieldEventFilter<T> extends BaseEventFilter {
	final private List<T> permittedValues;

	public FieldEventFilter(final EventFilter filter, final List<T> values) {
		super(filter);
		permittedValues = values;
	}

	public FieldEventFilter(final List<T> values) {
		this(null, values);
	}

	@Override
	public boolean isEventDirectlyFiltered(final Date date, final Event event) {
		return (permittedValues.contains(getEventField(event)) == false);
	}

	abstract T getEventField(Event event);

}