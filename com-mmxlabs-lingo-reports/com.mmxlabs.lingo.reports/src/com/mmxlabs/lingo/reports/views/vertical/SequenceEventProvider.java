package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import org.joda.time.LocalDate;

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
	private boolean asUTCEquivalent;

	public SequenceEventProvider(final Sequence[] data, final EventFilter filter, boolean asUTCEquivalent) {
		super(filter);
		this.data = data;
		this.asUTCEquivalent = asUTCEquivalent;
	}

	public SequenceEventProvider(final Sequence seq, final EventFilter filter, boolean asUTCEquivalent) {
		this(new Sequence[] { seq }, filter, asUTCEquivalent);
	}

	@Override
	public Event[] getUnfilteredEvents(final LocalDate date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Sequence seq : data) {
			if (seq != null) {
				final Event[] events = VerticalReportUtils.getEvents(seq, date, asUTCEquivalent);
				for (final Event event : events) {
					result.add(event);
				}
			}
		}

		return result.toArray(new Event[0]);
	}
}