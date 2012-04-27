/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author hinton
 *
 */
public class PieChartRenderer {
	public static Image renderPie(final Color pieColor, final double p) {
		final Image image = new Image(Display.getDefault(), 16, 16);
		final GC context = new GC(image);
		context.setBackground(pieColor);
		context.fillArc(0, 0, 15, 15, 0, (int) (360 * p));

		return image;
	}
}
