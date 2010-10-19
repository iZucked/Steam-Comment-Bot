/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.ganttviewer;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;

public class ZoomInAction extends Action {

	private GanttChart ganttChart;

	public ZoomInAction() {
		super();
		setText("Zoom In");
	}

	public ZoomInAction(final GanttChart ganttChart) {
		this();
		this.ganttChart = ganttChart;
	}

	public GanttChart getGanttChart() {
		return ganttChart;
	}

	public void setGanttChart(final GanttChart ganttChart) {
		this.ganttChart = ganttChart;
	}

	@Override
	public boolean isEnabled() {
		return ganttChart != null && ganttChart.getSettings().enableZooming();
	}

	@Override
	public void run() {

		ganttChart.getGanttComposite().zoomIn(true);
		ganttChart.getGanttComposite().setFocus();
	}
}
