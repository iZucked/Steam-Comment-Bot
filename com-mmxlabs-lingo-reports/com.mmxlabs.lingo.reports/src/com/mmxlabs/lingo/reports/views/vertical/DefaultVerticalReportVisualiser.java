/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Event;

public class DefaultVerticalReportVisualiser extends AbstractVerticalReportVisualiser {

	@Override
	public Color getEventForegroundColor(final LocalDate date, final Event event) {
		return null;
	}

	@Override
	public boolean datesAreUTCEquivalent() {
		// Always render as local time.
		return false;
	}
}
