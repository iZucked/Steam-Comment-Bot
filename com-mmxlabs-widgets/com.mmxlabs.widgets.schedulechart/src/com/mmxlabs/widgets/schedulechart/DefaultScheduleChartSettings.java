package com.mmxlabs.widgets.schedulechart;

import com.mmxlabs.widgets.schedulechart.draw.DefaultScheduleChartColourScheme;

public class DefaultScheduleChartSettings implements IScheduleChartSettings {
	
	private final IScheduleChartColourScheme defaultColourScheme = new DefaultScheduleChartColourScheme();

	@Override
	public int getRowEventHeight() {
		return 20;
	}

	@Override
	public int getSpacerWidth() {
		return 3;
	}

	@Override
	public int getMinimumRowHeaderWidth() {
		return 100;
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

}