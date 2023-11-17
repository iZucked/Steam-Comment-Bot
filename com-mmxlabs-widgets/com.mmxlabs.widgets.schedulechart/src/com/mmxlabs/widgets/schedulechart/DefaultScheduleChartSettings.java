/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.widgets.schedulechart.draw.DefaultScheduleChartColourScheme;

public class DefaultScheduleChartSettings implements IScheduleChartSettings {
	
	private final IScheduleChartColourScheme defaultColourScheme = new DefaultScheduleChartColourScheme();
	private EventSize size = EventSize.SMALL;  
	
	@Override
	public int getHeaderHeight() {
		return 24;
	}
	
	@Override
	public int getBuySellEventHeight() {
		return 15;
	}

	@Override
	public int getTopAnnotationHeight() {
		return 8;
	}

	@Override
	public int getBottomAnnotationHeight() {
		return 8;
	}

	@Override
	public int getSpacerWidth() {
		return 4;
	}

	@Override
	public int getMinimumRowHeaderWidth() {
		return 0;
	}

	@Override
	public int getRowHeaderLeftPadding() {
		return 10;
	}

	@Override
	public int getRowHeaderRightPadding() {
		return 15;
	}

	@Override
	public boolean showAnnotations() {
		return false;
	}

	@Override
	public IScheduleChartColourScheme getColourScheme() {
		return defaultColourScheme;
	}

	@Override
	public int filterModeCheckboxColumnWidth() {
		return getRowHeight();
	}

	@Override
	public int getHeaderLeftPadding() {
		return 2;
	}

	@Override
	public EventSize getEventSizing() {
		return size;
	}

	@Override
	public void setEventSizing(EventSize size) {
		this.size = size;
	}

	@Override
	public boolean hasMultipleScenarios() {
		return false;
	}
	
	@Override
	public boolean showLegend() {
		return false;
	}
}
