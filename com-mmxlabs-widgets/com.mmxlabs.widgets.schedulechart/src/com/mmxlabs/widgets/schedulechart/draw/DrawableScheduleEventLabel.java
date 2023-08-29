package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public final class DrawableScheduleEventLabel extends RelativeDrawableElement {
	private static final int PADDING = 0;
	private final BasicDrawableElements.Text text;

	public DrawableScheduleEventLabel(Color labelTextColour, Color labelBgColour, Function<ScheduleEvent, String> textGenerator, int textAlignment, ScheduleEvent se) {
		this.text = BasicDrawableElements.Text.from(0, 0, textGenerator.apply(se)).padding(PADDING).alignment(textAlignment).textColour(labelTextColour).bgColour(labelBgColour).create();
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