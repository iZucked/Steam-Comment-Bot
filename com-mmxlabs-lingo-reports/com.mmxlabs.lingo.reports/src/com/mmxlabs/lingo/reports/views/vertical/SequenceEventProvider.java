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
	private final AbstractVerticalReportVisualiser verticalReportVisualiser;

	public SequenceEventProvider(final Sequence[] data, final EventFilter filter, final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super(filter);
		this.data = data;
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	public SequenceEventProvider(final Sequence seq, final EventFilter filter, final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this(new Sequence[] { seq }, filter, verticalReportVisualiser);
	}

	@Override
	public Event[] getUnfilteredEvents(final LocalDate date) {
		final ArrayList<Event> result = new ArrayList<>();

		for (final Sequence seq : data) {
			if (seq != null) {
				final Event[] events = getEvents(date, seq);
				for (final Event event : events) {
					result.add(event);
				}
			}
		}

		return result.toArray(new Event[0]);
	}

	protected Event[] getEvents(final LocalDate date, final Sequence seq) {
		final Event[] events = verticalReportVisualiser.getEventsByScheduledDate(seq, date);
		return events;
	}
}