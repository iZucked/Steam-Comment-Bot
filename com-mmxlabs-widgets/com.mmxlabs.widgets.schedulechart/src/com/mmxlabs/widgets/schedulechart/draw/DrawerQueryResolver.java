package com.mmxlabs.widgets.schedulechart.draw;

import org.eclipse.swt.graphics.Point;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Text;

public interface DrawerQueryResolver {
	Point findSizeOfText(String text);
	Point findSizeOfText(Text text);
}
