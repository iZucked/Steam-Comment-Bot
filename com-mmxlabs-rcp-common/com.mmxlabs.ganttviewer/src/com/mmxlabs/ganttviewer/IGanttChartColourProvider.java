/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;

/**
 * Extended version of {@link IColorProvider} returning additional colour scheme values
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

	/**
	 * Returns the {@link Color} used to draw borders
	 * 
	 * @param element
	 * @return
	 */
	Color getBorderColour(Object element);

	/**
	 * Returns the border width of this element
	 * @param element
	 * @return
	 */
	int getBorderWidth(Object element);
}
