/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer;

/**
 * An interface which allows determine the sizes of
 * a Gantt section that depend on the model.
 * @author Andrey Popov
 *
 */
public interface GanttSectionSizeProvider {
	
	/**
	 * Finds out if the height of Gantt Section row must be fixed size.
	 * @param resource is an Object which can provide information
	 * necessary to derive the result of the method
	 * @return the answer
	 */
	boolean requiresFixedRowHeight(Object resource);
}
