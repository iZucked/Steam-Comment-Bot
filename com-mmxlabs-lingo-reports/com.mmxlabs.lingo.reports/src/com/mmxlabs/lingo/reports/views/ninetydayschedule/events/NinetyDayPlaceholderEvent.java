/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class NinetyDayPlaceholderEvent extends NinetyDayDrawableScheduleEvent {

	public NinetyDayPlaceholderEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
	}

	@Override
	protected Color getBackgroundColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}

	@Override
	protected Color getBorderColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}

}
