package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;

public class ScheduleCanvasState implements IScheduleChartContentBoundsProvider {
	
	private Rectangle mainBounds;
	private List<ScheduleChartRow> rows = new ArrayList<>();

	private ScheduleEvent leftmostEvent;
	private ScheduleEvent rightmostEvent;

	private final Set<ScheduleEvent> selectedEvents = new HashSet<>();
	
	private List<DrawableScheduleChartRow> lastDrawnContent;

	public Rectangle getMainBounds() {
		return mainBounds;
	}

	public void setMainBounds(Rectangle mainBounds) {
		this.mainBounds = mainBounds;
	}

	public List<ScheduleChartRow> getRows() {
		return rows;
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
	
}
