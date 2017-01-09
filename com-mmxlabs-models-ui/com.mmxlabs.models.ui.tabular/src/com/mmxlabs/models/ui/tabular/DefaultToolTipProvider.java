/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public abstract class DefaultToolTipProvider implements IToolTipProvider {

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 100; // msec
	}

	@Override
	public Image getToolTipImage(Object object) {
		return null;
	}

	@Override
	public Point getToolTipShift(Object object) {
		return new Point(5, 5);
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 5000; // msec
	}

}
