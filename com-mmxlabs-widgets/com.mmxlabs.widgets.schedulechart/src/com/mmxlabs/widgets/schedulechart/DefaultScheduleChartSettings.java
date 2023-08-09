package com.mmxlabs.widgets.schedulechart;

public class DefaultScheduleChartSettings implements IScheduleChartSettings {

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

}
