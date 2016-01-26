/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.labellers;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser;
import com.mmxlabs.lingo.reports.views.vertical.AbstractVerticalReportVisualiser.Alignment;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Class to provide formatting information for individual calendar events.
 * 
 * @author mmxlabs
 * 
 */
public class EventLabelProvider {

	@NonNull
	protected final AbstractVerticalReportVisualiser verticalReportVisualiser;

	public EventLabelProvider(@NonNull final AbstractVerticalReportVisualiser verticalReportVisualiser) {
		this.verticalReportVisualiser = verticalReportVisualiser;
	}

	protected String getText(@NonNull final LocalDate date, @NonNull final Event event) {
		return verticalReportVisualiser.getEventText(date, event);
	}

	protected Font getFont(@NonNull final LocalDate date, @NonNull final Event event) {
		return verticalReportVisualiser.getEventFont(date, event);
	}

	protected Color getBackground(@NonNull final LocalDate date, @NonNull final Event event) {
		return verticalReportVisualiser.getEventBackgroundColor(date, event);
	}

	protected Color getForeground(@NonNull final LocalDate date, @NonNull final Event event) {
		return verticalReportVisualiser.getEventForegroundColor(date, event);
	}

	public Alignment getTextAlignment(@NonNull final LocalDate date, @NonNull final Event event) {
		return verticalReportVisualiser.getEventTextAlignment(date, event);
	}
}