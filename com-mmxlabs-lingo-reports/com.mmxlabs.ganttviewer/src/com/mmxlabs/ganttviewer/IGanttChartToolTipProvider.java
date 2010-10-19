/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.ganttviewer;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public interface IGanttChartToolTipProvider extends ILabelProvider {

	String getToolTipText(Object element);
	
	String getToolTipTitle(Object element);

	Image getToolTipImage(Object element);
}
