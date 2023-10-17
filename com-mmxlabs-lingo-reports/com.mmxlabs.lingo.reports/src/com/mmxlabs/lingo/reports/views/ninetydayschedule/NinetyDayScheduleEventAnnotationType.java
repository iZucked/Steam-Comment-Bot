/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

public enum NinetyDayScheduleEventAnnotationType {
	SLOT_WINDOW, LATENESS_BAR, CANAL_JOURNEY;
	
	private NinetyDayScheduleEventAnnotationType() {}
	
	public boolean isRenderedAfterEvent() {
		return this.compareTo(CANAL_JOURNEY) >= 0;
	}
	
	public boolean isRenderedBeforeEvent() {
		return !isRenderedAfterEvent();
	}

}
