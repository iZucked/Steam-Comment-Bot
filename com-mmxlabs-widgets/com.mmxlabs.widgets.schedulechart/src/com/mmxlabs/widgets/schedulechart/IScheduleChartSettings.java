/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public interface IScheduleChartSettings {
	int getHeaderHeight();
	int getRowEventHeight();
	int getSpacerWidth();

	int getMinimumRowHeaderWidth();
	int getRowHeaderLeftPadding();
	int getRowHeaderRightPadding();
	
	default int getRowHeight() {
		return getRowEventHeight() + 2 * getSpacerWidth();
	}
	
	IScheduleChartColourScheme getColourScheme();
}
