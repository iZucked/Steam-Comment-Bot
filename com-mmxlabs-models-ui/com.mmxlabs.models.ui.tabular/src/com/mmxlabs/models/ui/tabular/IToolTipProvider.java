/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public interface IToolTipProvider {
	
	public String getToolTipText(final Object element);

	public int getToolTipDisplayDelayTime(Object object);

	public Image getToolTipImage(Object object);

	public Point getToolTipShift(Object object);

	public int getToolTipTimeDisplayed(Object object);
}
