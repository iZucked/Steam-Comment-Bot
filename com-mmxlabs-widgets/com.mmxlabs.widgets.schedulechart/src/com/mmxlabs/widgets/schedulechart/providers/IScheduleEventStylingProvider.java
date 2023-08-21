package com.mmxlabs.widgets.schedulechart.providers;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public interface IScheduleEventStylingProvider {
	Color getBackgroundColour(ScheduleEvent e);
	Color getBorderColour(ScheduleEvent e);
	int getBorderThickness(ScheduleEvent e);
	boolean getIsBorderInner(ScheduleEvent e);
}