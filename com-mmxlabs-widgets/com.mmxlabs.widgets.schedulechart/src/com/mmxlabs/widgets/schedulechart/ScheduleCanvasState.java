/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRowHeader;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class ScheduleCanvasState implements IScheduleChartContentBoundsProvider {
	
	private Rectangle originalBounds;
	private Rectangle mainBounds;
	private List<ScheduleChartRow> rows = new ArrayList<>();
	
	private ScheduleChartMode scm = ScheduleChartMode.NORMAL;

	private ScheduleEvent leftmostEvent;
	private ScheduleEvent rightmostEvent;

	private final Set<ScheduleEvent> selectedEvents = new HashSet<>();
	private Optional<DrawableScheduleEvent> hoveredEvent = Optional.empty();
	
	private List<DrawableScheduleChartRow> lastDrawnContent = new ArrayList<>();
	private List<DrawableScheduleChartRowHeader> lastDrawnRowHeaders = new ArrayList<>();
	
	private Set<ScheduleChartRowKey> hiddenRowKeys = new HashSet<>();
	
	private Map<ScheduleEvent, ScheduleEvent> diffEvents = new HashMap<>(); 

	public Map<ScheduleEvent, ScheduleEvent> getDiffEvents() {
		return diffEvents;
	}

	public void setDiffEvents(Map<ScheduleEvent, ScheduleEvent> diffEvents) {
		this.diffEvents = diffEvents;
	}

	public void setOriginalBounds(Rectangle originalBounds) {
		this.originalBounds = originalBounds;
	}

	public Rectangle getOriginalBounds() {
		return originalBounds;
	}
	
	public Rectangle getMainBounds() {
		return mainBounds;
	}

	public void setMainBounds(Rectangle mainBounds) {
		this.mainBounds = mainBounds;
	}

	public List<ScheduleChartRow> getRows() {
		return rows;
	}
	
	public List<ScheduleChartRow> getShownRows() {
		return rows.stream().filter(r -> scm == ScheduleChartMode.FILTER || !hiddenRowKeys.contains(r.getKey())).toList();
	}

	public void setRows(List<ScheduleChartRow> rows) {
		this.rows = rows;
	}

	@Override
	public ScheduleEvent getLeftmostEvent() {
		return leftmostEvent;
	}

	public void setLeftMostEvent(ScheduleEvent leftMostEvent) {
		this.leftmostEvent = leftMostEvent;
	}

	@Override
	public ScheduleEvent getRightmostEvent() {
		return rightmostEvent;
	}

	public void setRightMostEvent(ScheduleEvent rightMostEvent) {
		this.rightmostEvent = rightMostEvent;
	}

	public Set<ScheduleEvent> getSelectedEvents() {
		return selectedEvents;
	}

	public void setSelectedEvents(Collection<ScheduleEvent> selectedEvents) {
		this.selectedEvents.clear();
		this.selectedEvents.addAll(selectedEvents);
	}

	public List<DrawableScheduleChartRow> getLastDrawnContent() {
		return lastDrawnContent;
	}

	public void setLastDrawnContent(List<DrawableScheduleChartRow> lastDrawnContent) {
		this.lastDrawnContent = lastDrawnContent;
	}

	public List<DrawableScheduleChartRowHeader> getLastDrawnRowHeaders() {
		return lastDrawnRowHeaders;
	}
	
	public void setLastDrawnRowHeaders(List<DrawableScheduleChartRowHeader> lastDrawnRowHeaders) {
		this.lastDrawnRowHeaders = lastDrawnRowHeaders;
	}
	
	public Optional<DrawableScheduleEvent> getHoveredEvent() {
		return hoveredEvent;
	}

	public void setHoveredEvent(Optional<DrawableScheduleEvent> hoveredEvent) {
		this.hoveredEvent = hoveredEvent;
	}

	public Set<ScheduleChartRowKey> getHiddenRowKeys() {
		return hiddenRowKeys;
	}
	
	public ScheduleChartMode getScheduleChartMode() {
		return scm;
	}
	
	public void setScheduleChartMode(ScheduleChartMode scm) {
		this.scm = scm;
	}

}
