package com.mmxlabs.widgets.schedulechart;

import java.time.DayOfWeek;

import org.eclipse.swt.graphics.Color;

public interface IScheduleChartColourScheme {
	Color getBaseBgColour();
	Color getHeaderBgColour(int headerNum);
	Color getHeaderTextColour(int headerNum);
	Color getHeaderOutlineColour();
	
	// if rowNum is negative, then its for the header area before / after the rows
	Color getRowHeaderBgColour(int rowNum);
	Color getRowHeaderTextColour(int rowNum);

	Color getRowBgColour(int rowNum);
	Color getRowOutlineColour(int rowNum);
	Color getDayBgColour(DayOfWeek d);
	Color getGridStrokeColour();
}
