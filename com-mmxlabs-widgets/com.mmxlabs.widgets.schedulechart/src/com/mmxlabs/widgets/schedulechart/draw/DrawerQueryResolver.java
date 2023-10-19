/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public interface DrawerQueryResolver {
	Point findSizeOfText(String text, Font f, int fontSize);
	Point findSizeOfText(Text text);
}
