package com.mmxlabs.widgets.schedulechart.draw;

import java.util.function.Function;

import org.eclipse.swt.graphics.Rectangle;

public abstract class RelativeDrawableElement extends DrawableElement {

	@Override
	public final void setBounds(Rectangle bounds) {
		throw new UnsupportedOperationException("Can't set bounds directly on a RelativeDrawableElement");
	}
	
	public void setRelativeBounds(DrawableElement de, Function<DrawableElement, Rectangle> relativeBoundsCalculator) {
		super.setBounds(relativeBoundsCalculator.apply(de));
	}

}
