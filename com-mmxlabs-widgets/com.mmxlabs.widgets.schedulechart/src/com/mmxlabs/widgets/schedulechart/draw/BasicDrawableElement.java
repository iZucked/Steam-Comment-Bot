/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Line;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Rectangle;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public sealed interface BasicDrawableElement permits Line, Rectangle, Text {
	Color getBackgroundColour();
	Color getBorderColour();
	int getBorderThickness();
	int getAlpha();
}
