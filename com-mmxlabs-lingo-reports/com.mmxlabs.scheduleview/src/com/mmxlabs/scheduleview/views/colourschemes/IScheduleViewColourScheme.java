/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views.colourschemes;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.scheduleview.views.SchedulerView;

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
	 * Returns the foreground {@link Color} to use for this element.
	 * 
	 * @param element
	 * @return
	 */
	Color getForeground(Object element);

	/**
	 * Returns the backgroun{@link Color} to use for this element.
	 * 
	 * @param element
	 * @return
	 */
	Color getBackground(Object element);
}
