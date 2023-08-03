package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class DrawableScheduleEvent extends DrawableElement {
	
	public static final Color RED = new Color(new RGB(255, 0, 0));
	
	private final ScheduleEvent se;
	
	public DrawableScheduleEvent(ScheduleEvent se, Rectangle bounds) {
		this.se = se;
		setBounds(bounds);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds).bgColour(RED).create());
		return res;
	}

}
