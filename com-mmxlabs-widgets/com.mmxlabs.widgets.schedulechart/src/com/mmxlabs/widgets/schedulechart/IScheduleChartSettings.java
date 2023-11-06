/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

public interface IScheduleChartSettings {
	int getHeaderHeight();
	default int getEventHeight() {
		return getEventSizing().getEventHeight();
	}
	int getBuySellEventHeight();
	int getTopAnnotationHeight();
	int getBottomAnnotationHeight();
	int getSpacerWidth();

	int getMinimumRowHeaderWidth();
	int getRowHeaderLeftPadding();
	int getRowHeaderRightPadding();
	int getHeaderLeftPadding();
	int filterModeCheckboxColumnWidth();
	
	default int getEventLabelFontSize() {
		return getEventSizing().getFontSize();
	}
	
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
	
	default int getBuySellRowHeight() {
		return getBuySellEventHeight() + 2 * getSpacerWidth();
	}
	
	IScheduleChartColourScheme getColourScheme();
	
	EventSize getEventSizing();
	void setEventSizing(EventSize size);
	boolean hasMultipleScenarios();
	boolean showLegend();

}
