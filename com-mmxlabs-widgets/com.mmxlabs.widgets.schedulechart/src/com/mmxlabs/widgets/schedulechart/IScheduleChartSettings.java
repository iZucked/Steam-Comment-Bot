/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public interface IScheduleChartSettings {
	int getHeaderHeight();
	int getEventHeight();
	int getWindowedEventHeight();
	int getSpacerWidth();

	int getMinimumRowHeaderWidth();
	int getRowHeaderLeftPadding();
	int getRowHeaderRightPadding();
	
	default int getRowHeight() {
		return (showWindows() ? Math.max(getEventHeight(), getWindowedEventHeight()) : getEventHeight()) + 2 * getSpacerWidth();
	}
	
	IScheduleChartColourScheme getColourScheme();

	boolean showWindows();
}
