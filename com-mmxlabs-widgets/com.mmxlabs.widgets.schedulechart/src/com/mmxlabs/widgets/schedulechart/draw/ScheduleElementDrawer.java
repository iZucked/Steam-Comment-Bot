package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;

public interface ScheduleElementDrawer {
	void drawOne(BasicDrawableElement b);
	
	default void drawOne(DrawableElement d) {
		for (BasicDrawableElement b: d.getBasicDrawableElements()) {
			drawOne(b);
		}
	}

	default void draw(List<DrawableElement> ds) {
		for (DrawableElement d: ds) {
			drawOne(d);
		}
	}

	default void drawOne(DrawableElement d, DrawerQueryResolver queryResolver) {
		for (BasicDrawableElement b: d.getBasicDrawableElements(queryResolver)) {
			drawOne(b);
		}
	}

	default void draw(List<DrawableElement> ds, DrawerQueryResolver queryResolver) {
		for (DrawableElement d: ds) {
			drawOne(d, queryResolver);
		}
	}
}
