package com.mmxlabs.lingo.reports.views.vertical;

import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class which provides cell labels (and formatting if desired) for columns in a calendar-style vertical report, based on a list of events per cell.
 * <p/>
 * 
 * Must be initialised with an {@link EventProvider} object which provides a list of events for a given calendar date.
 * <p/>
 * 
 * Override {@link#getText(Date date, Event event)} to specify the formatting of cell contents.
 * <p/>
 * 
 * Example Usage:
 * <p/>
 * {@code 
 * column.setLabelProvider(new EventColumnLabelProvider(new SequenceEventProvider(sequence)); 
 * }
 * 
 * @author Simon McGregor
 * 
 */
public class EventColumnLabelProvider extends CalendarColumnLabelProvider<EventProvider> {
	
	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	public EventColumnLabelProvider(final EventProvider provider, AbstractVerticalReportVisualiser verticalReportVisualiser) {
		super(verticalReportVisualiser.createDateFormat(), provider);
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	protected int getRowSpan(final Date date, final EventProvider provider) {
		return 0;
	}

	protected int getColSpan(final Date date, final EventProvider provider) {
		return 0;
	}

	/**
	 * Returns the text for a column cell. Defers to {@link #getEventsText(Date, Event[])}; override that method if you want to change the behaviour.
	 */
	@Override
	protected final String getText(final Date element, final EventProvider provider) {
		// find the event text for the date given
		return getEventsText(element, provider.getEvents(element));
	}

	/**
	 * Returns the text for a particular series of events on a specified date. By default, calls {@link #getEventText(Date, Event)} for each event in the specified array.
	 * 
	 * @param element
	 * @param events
	 * @return
	 */
	protected String getEventsText(final Date date, final Event[] events) {
		final StringBuilder sb = new StringBuilder();
		String join = "";
		for (final Event event : events) {
			sb.append(join);
			sb.append(getEventText(date, event));
			join = "; ";
		}
		return sb.toString();
	}

	/**
	 * Returns By default, defers to {@link AbstractVerticalCalendarReportView#getEventText(Date, Event[], EventColumnLabelProvider)}
	 * 
	 * @param element
	 * @param event
	 * @return
	 */
	protected String getEventText(final Date element, final Event event) {
		return verticalReportVisualiser.getEventText(element, event);
	}

	/** Returns the desired background colour of the cell. */
	@Override
	protected Color getBackground(final Date element, final EventProvider provider) {
		return verticalReportVisualiser.getEventBackgroundColor(element, provider.getEvents(element));
	}

	/** Returns the desired foreground colour of the cell. */
	@Override
	protected Color getForeground(final Date element, final EventProvider provider) {
		return verticalReportVisualiser.getEventForegroundColor(element, provider.getEvents(element));
	}

	/** Returns the desired foreground colour of the cell. */
	@Override
	protected Font getFont(final Date element, final EventProvider provider) {
		return verticalReportVisualiser.getEventFont(element, provider.getEvents(element));
	}
}
