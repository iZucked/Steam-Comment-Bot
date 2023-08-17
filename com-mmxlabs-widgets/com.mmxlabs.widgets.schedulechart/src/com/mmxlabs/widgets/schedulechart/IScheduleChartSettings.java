package com.mmxlabs.widgets.schedulechart;

public interface IScheduleChartSettings {
	int getHeaderHeight();
	int getRowEventHeight();
	int getSpacerWidth();

	int getMinimumRowHeaderWidth();
	int getRowHeaderRightPadding();
	
	default int getRowHeight() {
		return getRowEventHeight() + 2 * getSpacerWidth();
	}
	
	IScheduleChartColourScheme getColourScheme();
}
