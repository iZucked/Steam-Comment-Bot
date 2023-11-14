/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.function.UnaryOperator;

import org.eclipse.swt.graphics.Color;

public interface ScheduleElementDrawer {
	void drawOne(BasicDrawableElement b);
	
	default void drawOne(DrawableElement d) {
		var prev = getColourFilter();
		if (d.getColourFilter().isPresent()) {
			applyColourFilter(d.getColourFilter().get());
		}

		for (BasicDrawableElement b: d.getBasicDrawableElements()) {
			drawOne(b);
		}

		applyColourFilter(prev);
	}

	default void draw(List<DrawableElement> ds) {
		for (DrawableElement d: ds) {
			drawOne(d);
		}
	}

	default void drawOne(DrawableElement d, DrawerQueryResolver queryResolver) {
		var prev = getColourFilter();
		if (d.getColourFilter().isPresent()) {
			applyColourFilter(d.getColourFilter().get());
		}
		List<BasicDrawableElement> basicElements = d.getBasicDrawableElements(queryResolver);
		for (BasicDrawableElement b: basicElements) {
			drawOne(b);
		}
		
		applyColourFilter(prev);
	}

	default void draw(List<DrawableElement> ds, DrawerQueryResolver queryResolver) {
		for (DrawableElement d: ds) {
			drawOne(d, queryResolver);
		}
	}
	
	void applyColourFilter(UnaryOperator<Color> filter);
	void removeColourFilter();
	UnaryOperator<Color> getColourFilter();
}
