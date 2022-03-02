/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer;

import java.util.Calendar;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * 
 * {@link #getElements(Object)} returns top level resources these should (could) return groups and again these can return actual events
 * 
 * @author Simon Goodall
 * 
 */
public interface IGanttChartContentProvider extends ITreeContentProvider {

	boolean isVisibleByDefault(Object resource);
	
	Calendar getElementStartTime(Object element);

	Calendar getElementEndTime(Object element);

	Calendar getElementPlannedStartTime(Object element);

	Calendar getElementPlannedEndTime(Object element);

	/**
	 */
	String getGroupIdentifier(Object element);

	/**
	 * Returns another element on the gantt view the given element depends upon.
	 * 
	 */
	Object getElementDependency(Object element);
}
