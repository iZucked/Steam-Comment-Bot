/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import java.util.Calendar;

import org.eclipse.swt.graphics.Image;

/**
 * Convenience class for creating an image in the chart instead of using the constructors in GanttEvent. 
 */
public class GanttImage extends GanttEvent {

	/**
	 * {@inheritDoc)
	 */
	public GanttImage(final GanttChart parent, final String name, final Calendar date, final Image picture) {
		super(parent, name, date, picture);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public GanttImage(final GanttChart parent, final Object data, final String name, final Calendar date, final Image picture) {
		super(parent, data, name, date, picture);
	}
}
