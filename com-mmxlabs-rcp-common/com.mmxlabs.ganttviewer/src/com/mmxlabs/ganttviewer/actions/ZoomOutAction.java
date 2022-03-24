/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 */
public class ZoomOutAction extends Action {

	private GanttChart ganttChart;

	public ZoomOutAction() {
		super();
		setText("Zoom Out");
		CommonImages.setImageDescriptors(this, IconPaths.ZoomOut);
	}

	public ZoomOutAction(final GanttChart ganttChart) {
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
		return (ganttChart != null) && ganttChart.getSettings().enableZooming();
	}

	@Override
	public void run() {

		ganttChart.getGanttComposite().zoomOut();
		ganttChart.getGanttComposite().setFocus();
	}
}
