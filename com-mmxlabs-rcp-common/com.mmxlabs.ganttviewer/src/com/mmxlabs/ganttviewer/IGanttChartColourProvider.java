package com.mmxlabs.ganttviewer;

import org.eclipse.jface.viewers.IColorProvider;

/**
 * Extended version of {@link IColorProvider} returning alpha values
 * 
 * @author Simon Goodall
 * 
 */
public interface IGanttChartColourProvider extends IColorProvider {
	/**
	 * Returns the alpha value to apply.
	 * 
	 * @param element
	 * @return
	 */
	int getAlpha(Object element);
}
