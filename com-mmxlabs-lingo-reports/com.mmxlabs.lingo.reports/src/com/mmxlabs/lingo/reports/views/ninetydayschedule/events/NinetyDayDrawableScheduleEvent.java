/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public abstract class NinetyDayDrawableScheduleEvent extends DrawableScheduleEvent {

	protected NinetyDayDrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
	}

	@Override
	public int getBorderThickness(ScheduleEventSelectionState s) {
		return (s == ScheduleEventSelectionState.SELECTED) ? 1 : 0;
	}
	
	@Override
	public boolean getIsBorderInner() {
		return false;
	}

	@Override
	public int getAlpha(ScheduleEventSelectionState s) {
		return switch (s) {
		case UNSELECTED -> 100;
		case HOVER -> 200;
		case SELECTED -> BasicDrawableElements.MAX_ALPHA;
		default ->
			throw new IllegalArgumentException("Unexpected value: " + s);
		};
	}

}
