/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import java.util.Calendar;


/**
 * Convenience class for creating a checkpoint instead of using the constructors on GanttEvent.
 * 
 */
public class GanttCheckpoint extends GanttEvent {

	/**
	 * {@inheritDoc}
	 */
	public GanttCheckpoint(final GanttChart parent, final String name, final Calendar date) {
		super(parent, null, name, date);
	}

	/**
	 * {@inheritDoc}
	 */
	public GanttCheckpoint(final GanttChart parent, final Object data, final String name, final Calendar date) {
		super(parent, data, name, date);
	}
	
}
