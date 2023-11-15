/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

/**
 * Definition of the highlighters for the view. These define overrides to the default colour scheme
 *
 */
public interface IScheduleEventStylingProvider {
	String getName();

	default Color getBackgroundColour(DrawableScheduleEvent e, Color defaultColour) {
		return defaultColour;
	}

	default Color getBorderColour(DrawableScheduleEvent e, Color defaultColour) {
		return defaultColour;
	}

	default int getBorderThickness(DrawableScheduleEvent e, int defaultValue) {
		return defaultValue;
	}

	default boolean getIsBorderInner(DrawableScheduleEvent e, boolean defaultValue) {
		return defaultValue;
	}

	/**
	 * Set the ID used to persist enabled highlighters
	 * 
	 * @param id
	 */
	void setID(String id);

	/**
	 * Returns the ID used to persist enabled highlighters.
	 * 
	 */
	String getID();
}
