/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public interface IScheduleChartSettings {
	int getHeaderHeight();
	int getEventHeight();
	int getTopAnnotationHeight();
	int getBottomAnnotationHeight();
	int getSpacerWidth();

	int getMinimumRowHeaderWidth();
	int getRowHeaderLeftPadding();
	int getRowHeaderRightPadding();
	int getHeaderLeftPadding();
	int filterModeCheckboxColumnWidth();
	
	int getEventLabelFontSize();
	
	boolean showAnnotations();
	default boolean allowResizing() {
		return showAnnotations();
	}
	
	default int getRowHeightWithAnnotations() {
		int withAnnotations = getTopAnnotationHeight() + getBottomAnnotationHeight() + getEventHeight() + 4 * getSpacerWidth();
		return showAnnotations() ? withAnnotations : getRowHeight();
	}

	default int getRowHeight() {
		return getEventHeight() + 2 * getSpacerWidth();
	}
	
	IScheduleChartColourScheme getColourScheme();

}
