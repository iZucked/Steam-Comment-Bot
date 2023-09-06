/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.function.Function;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public abstract class RelativeDrawableElement extends DrawableElement {
	
	private RelativeBounds relBounds;

	@Override
	public final void setBounds(Rectangle bounds) {
		throw new UnsupportedOperationException("Can't set bounds directly on a RelativeDrawableElement");
	}
	
	public void setRelativeBounds(DrawableElement de, Function<DrawableElement, Rectangle> relativeBoundsCalculator) {
		super.setBounds(relativeBoundsCalculator.apply(de));
	}
	
	public void setRelativeBounds(RelativeBounds relBounds) {
		this.relBounds = relBounds;
	}
	
	public void setBoundsFromRelative(DrawableElement de) {
		super.setBounds(RelativeBoundsCalculationFunctions.getBoundsFromRelative(relBounds).apply(de));
	}
	
	public record RelativeBounds(Point relParentAnchor, Point relChildAnchor, int widthScale, int heightScale) {}

}
