package com.mmxlabs.lingo.reports.views.vertical;

import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

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

	protected String getText(final Date date, final Event event) {
		return verticalReportVisualiser.getEventText(date, event);
	}

	protected Font getFont(final Date date, final Event event) {
		return verticalReportVisualiser.getEventFont(date, new Event[] { event });
	}

	protected Color getBackground(final Date date, final Event event) {
		return verticalReportVisualiser.getEventBackgroundColor(date, new Event[] { event });
	}

	protected Color getForeground(final Date date, final Event event) {
		return null;
	}
}