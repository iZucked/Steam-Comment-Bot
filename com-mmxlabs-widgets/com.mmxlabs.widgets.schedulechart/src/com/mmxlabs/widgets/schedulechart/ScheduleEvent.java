/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ScheduleEvent {

	private final LocalDateTime startDate;
	private final LocalDateTime endDate;
	private final ScenarioResult scenarioResult;
	private final boolean isPinned;

	private final Object data;
	private final Object element;
	private boolean visible;
	private boolean forceVisible;
	private boolean showAnnotations;
	private ScheduleEventSelectionState selectionState;
	
	private final List<ScheduleEventAnnotation> annotations;
	
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate, Object data, Object element, ScenarioResult scenarioResult, boolean isPinned, List<ScheduleEventAnnotation> annotations) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.scenarioResult = scenarioResult;
		this.isPinned = isPinned;
		this.data = data;
		this.element = element;
		this.selectionState = ScheduleEventSelectionState.UNSELECTED;
		this.annotations = annotations;
		this.showAnnotations = true;
	}
	
	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate, Object data, Object element, ScenarioResult scenarioResult, boolean isPinned, List<ScheduleEventAnnotation> annotations, boolean showAnnotations) {
		this(startDate, endDate, data, element, scenarioResult, isPinned, annotations);
		this.showAnnotations = showAnnotations;
	}

	public ScheduleEvent(LocalDateTime startDate, LocalDateTime endDate) {
		this(startDate, endDate, null, null, null, false, Collections.emptyList());
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
	
	public Object getElement() {
		return element;
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
	
	public boolean showsAnnotations() {
		return showAnnotations;
	}
	
	public String getScenarioName() {
		if(scenarioResult == null)
			return "";
		
		return scenarioResult.getModelRecord().getName();
	}
	
	public ScheduleModel getScheduleModel() {
		if(scenarioResult == null)
			return null;
		
		return scenarioResult.getTypedResult(ScheduleModel.class);
	}
	
	public ScenarioResult getScenarioResult() {
		return scenarioResult;
	}
	
	public boolean isPinnedScenario() {
		return isPinned;
	}
	
	public boolean hasElement() {
		return element != null;
	}

}
