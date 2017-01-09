/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;

/**
 * Interface defining a colour scheme used to render the {@link SchedulerView} elements.
 * 
 * TODO: Add a mechanism to define a key for the colour scheme.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScheduleViewColourScheme {

	/**
	 * Returns the name of this colour scheme
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the ID of this colour scheme
	 * 
	 * @return
	 */
	String getID();

	/**
	 * Sets the ID of this colour scheme
	 * 
	 * @return
	 */
	String setID(String id);

	GanttChartViewer getViewer();

	void setViewer(GanttChartViewer viewer);

	/**
	 * Returns the foreground {@link Color} to use for this element.
	 * 
	 * @param element
	 * @return
	 */
	Color getForeground(Object element);

	/**
	 * Returns the background {@link Color} to use for this element.
	 * 
	 * @param element
	 * @return
	 */
	Color getBackground(Object element);

	/**
	 * Returns the alpha value to apply.
	 * 
	 * @param element
	 * @return
	 */
	int getAlpha(Object element);

	/**
	 * Returns the {@link Color} used to draw borders
	 * 
	 * @param element
	 * @return
	 */
	Color getBorderColour(Object element);

	int getBorderWidth(Object element);
}
