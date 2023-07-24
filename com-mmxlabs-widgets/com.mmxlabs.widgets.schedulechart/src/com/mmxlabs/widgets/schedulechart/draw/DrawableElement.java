package com.mmxlabs.widgets.schedulechart.draw;

import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

public abstract class DrawableElement {
	
	private Rectangle bounds;
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public final List<BasicDrawableElement> getBasicDrawableElements() {
		if (bounds == null) { 
			throw new UnsupportedOperationException("Can't get basic drawable elements without bounds"); 
		}
		
		return getBasicDrawableElements(bounds);
	}

	protected abstract List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds);
}
