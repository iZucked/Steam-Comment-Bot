/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public class ScheduleFilterSupport {

	private final ScheduleCanvasState canvasState;
	private final IScheduleChartSettings settings;

	public ScheduleFilterSupport(ScheduleCanvasState canvasState, IScheduleChartSettings settings) {
		this.canvasState = canvasState;
		this.settings = settings;
	}
	
	public void hideRow(ScheduleChartRowKey key) {
		canvasState.getHiddenRowKeys().add(key);
	}
	
	public void showHiddenRows() {
		canvasState.getHiddenRowKeys().clear();
	}

	public boolean isFiltered() {
		return !canvasState.getHiddenRowKeys().isEmpty();
	}

}
