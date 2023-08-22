/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;

public class ScheduleEvent {

	private final LocalDateTime startDate;
	private final LocalDateTime endDate;

	private final LocalDateTime plannedStartDate;
	private final LocalDateTime plannedEndDate;
	
	private final Object data;
	private boolean visible;
	private ScheduleEventSelectionState selectionState;
	
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime plannedStartDate, LocalDateTime plannedEndDate, Object data) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.data = data;
		this.selectionState = ScheduleEventSelectionState.UNSELECTED;
	}
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate) {
		this(startDate, endDate, null, null, null);
	}
	
	public LocalDateTime getStart() {
		return startDate;
	}
	
	public LocalDateTime getEnd() {
		return endDate;
	}
	
	public Object getData() {
		return data;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setSelectionState(ScheduleEventSelectionState selectionState) {
		this.selectionState = selectionState;
	}

	public ScheduleEventSelectionState getSelectionState() {
		return selectionState;
	}
}
