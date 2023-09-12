/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ScheduleEvent {

	private final LocalDateTime startDate;
	private final LocalDateTime endDate;

	private final Object data;
	private boolean visible;
	private boolean forceVisible;
	private ScheduleEventSelectionState selectionState;
	
	private final List<ScheduleEventAnnotation> annotations;
	
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate, Object data, List<ScheduleEventAnnotation> annotations) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.data = data;
		this.selectionState = ScheduleEventSelectionState.UNSELECTED;
		this.annotations = annotations;
	}

	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate) {
		this(startDate, endDate, null, Collections.emptyList());
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
	 
	public List<ScheduleEventAnnotation> getAnnotations() {
		return annotations;
	}
	
	public void forceVisible() {
		this.forceVisible = true;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return forceVisible || visible;
	}
	
	public void setSelectionState(ScheduleEventSelectionState selectionState) {
		this.selectionState = selectionState;
	}

	public ScheduleEventSelectionState getSelectionState() {
		return selectionState;
	}

}
