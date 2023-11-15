/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public abstract class DrawableElement {
	
	private Rectangle bounds;
	private Optional<UnaryOperator<Color>> optColourFilter = Optional.empty();
	private IScheduleEventStylingProvider stylingProvider;

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Optional<UnaryOperator<Color>> getColourFilter() {
		return optColourFilter;
	}
	
	public void setColourFilter(UnaryOperator<Color> colourFilter) {
		optColourFilter = Optional.of(colourFilter);
	}

	public IScheduleEventStylingProvider getStylingProvider() {
		return stylingProvider;
	}

	public void setStylingProvider(final IScheduleEventStylingProvider stylingProvider) {
		this.stylingProvider = stylingProvider;
	}

	public final List<BasicDrawableElement> getBasicDrawableElements() {
		return getBasicDrawableElements((DrawerQueryResolver) null);
	}

	public final List<BasicDrawableElement> getBasicDrawableElements(DrawerQueryResolver queryResolver) {
		if (bounds == null) { 
			throw new UnsupportedOperationException("Can't get basic drawable elements without bounds"); 
		}
		
		return getBasicDrawableElements(bounds, queryResolver);
	}

	protected abstract List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver);
	
	/**
	 * Quick factory method for Empty drawable Element.
	 * @return
	 */
	public static DrawableElement drawableElementWithoutAnyDrawing() {
		return new DrawableElement() {
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
				return List.of();
			}
		};
	}
}
