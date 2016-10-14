/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

/**
 * A convenience class for creating a GanttScope instead of using the specific constructors on the GanttEvent.
 *  
 */
public final class GanttScope extends GanttEvent {

	/**
	 * {@inheritDoc}
	 */
	public GanttScope(final GanttChart parent, final String name) {
		super(parent, null, name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public GanttScope(final GanttChart parent, final Object data, final String name) {
		super(parent, data, name);
	}
	
}
