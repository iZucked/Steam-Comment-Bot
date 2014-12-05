package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Date;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Class to provide the events on a given date from one or more Sequence objects.
 * 
 * 
 * Example Usage:
 * 
 * column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequence));
 * 
 * Override the {@link#filterEventOut(Date date, Event event)} method to filter the events more specifically.
 * 
 * @author Simon McGregor
 * 
 */
public class SequenceEventProvider extends EventProvider {
	final protected Sequence[] data;

	public SequenceEventProvider(final Sequence[] data, final EventFilter filter) {
		super(filter);
		this.data = data;
	}

	public SequenceEventProvider(final Sequence seq, final EventFilter filter) {
		this(new Sequence[] { seq }, filter);
	}

	@Override
	public Event[] getUnfilteredEvents(final Date date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Sequence seq : data) {
			if (seq != null) {
				final Event[] events = VerticalReportUtils.getEvents(seq, date);
				for (final Event event : events) {
					result.add(event);
				}
			}
		}

		return result.toArray(new Event[0]);
	}
}