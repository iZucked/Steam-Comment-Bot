/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;

public final class DrawableScheduleEventLabel extends RelativeDrawableElement {
	private final BasicDrawableElements.Text text;

	public DrawableScheduleEventLabel(Color labelTextColour, Function<ScheduleEvent, String> textGenerator, int textAlignment, Padding p, ScheduleEvent se) {
		this.text = BasicDrawableElements.Text.from(0, 0, textGenerator.apply(se)).padding(p).alignment(textAlignment).textColour(labelTextColour).create();
	}
	
	public int getLabelWidth(DrawerQueryResolver r) {
		return r.findSizeOfText(text).x;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		text.boundingBox().x = bounds.x;
		text.boundingBox().y = bounds.y;
		text.boundingBox().width = bounds.width;
		return List.of(text);
	}
}