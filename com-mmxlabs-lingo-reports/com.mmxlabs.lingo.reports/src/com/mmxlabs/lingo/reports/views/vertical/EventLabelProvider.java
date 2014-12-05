package com.mmxlabs.lingo.reports.views.vertical;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.joda.time.LocalDate;

import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to provide formatting information for individual calendar events.
 * 
 * @author mmxlabs
 * 
 */
public class EventLabelProvider {

	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	public EventLabelProvider(AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	protected String getText(final LocalDate date, final Event event) {
		return verticalReportVisualiser.getEventText(date, event);
	}

	protected Font getFont(final LocalDate date, final Event event) {
		return verticalReportVisualiser.getEventFont(date, event);
	}

	protected Color getBackground(final LocalDate date, final Event event) {
		return verticalReportVisualiser.getEventBackgroundColor(date, event);
	}

	protected Color getForeground(final LocalDate date, final Event event) {
		return verticalReportVisualiser.getEventForegroundColor(date, event);
	}
}