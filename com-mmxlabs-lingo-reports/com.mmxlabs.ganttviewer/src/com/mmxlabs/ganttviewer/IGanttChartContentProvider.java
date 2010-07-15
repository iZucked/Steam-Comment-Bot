package com.mmxlabs.ganttviewer;

import java.util.Calendar;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * 
 * {@link #getElements(Object)} returns top level resources
 * these should (could) return groups
 * and again these can return actual events
 * 
 * @author sg
 *
 */
public interface IGanttChartContentProvider extends ITreeContentProvider {

	
	Calendar getElementStartTime(Object element);
	
	Calendar getElementEndTime(Object element);
}
