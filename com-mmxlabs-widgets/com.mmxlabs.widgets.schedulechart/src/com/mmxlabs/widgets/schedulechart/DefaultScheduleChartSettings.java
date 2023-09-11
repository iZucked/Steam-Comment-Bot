/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.widgets.schedulechart.draw.DefaultScheduleChartColourScheme;

public class DefaultScheduleChartSettings implements IScheduleChartSettings {
	
	private final IScheduleChartColourScheme defaultColourScheme = new DefaultScheduleChartColourScheme();

	@Override
	public int getEventHeight() {
		return 20;
	}

	@Override
	public int getWindowedEventHeight() {
		return 30;
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
		return 2;
	}

	@Override
	public int getRowHeaderRightPadding() {
		return 15;
	}

	@Override
	public int getHeaderHeight() {
		return 24;
	}

	@Override
	public IScheduleChartColourScheme getColourScheme() {
		return defaultColourScheme;
	}

	@Override
	public boolean showWindows() {
		return false;
	}

}
