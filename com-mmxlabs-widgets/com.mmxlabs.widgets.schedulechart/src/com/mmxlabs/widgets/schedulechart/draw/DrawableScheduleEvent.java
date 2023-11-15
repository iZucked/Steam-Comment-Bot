/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventStylingProvider;

public abstract class DrawableScheduleEvent extends DrawableElement {
	
	protected final ScheduleEvent se;
	protected ScheduleEventSelectionState selectionState;
	
	
	protected DrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		this.se = se;
		this.selectionState = noneSelected ? ScheduleEventSelectionState.SELECTED : se.getSelectionState();
		setBounds(bounds);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(final Rectangle bounds, final DrawerQueryResolver queryResolver) {
		final List<BasicDrawableElement> res = new ArrayList<>();

		if (!se.isVisible()) {
			return res;
		}
		final IScheduleEventStylingProvider stylingProvider = getStylingProvider();
		assert stylingProvider != null;

		res.add(BasicDrawableElements.Rectangle.withBounds(bounds)
				.bgColour(stylingProvider.getBackgroundColour(this, getBackgroundColour()))
				.border(stylingProvider.getBackgroundColour(this, getBorderColour()), stylingProvider.getBorderThickness(this, getBorderThickness(selectionState)),
						stylingProvider.getIsBorderInner(this, getIsBorderInner()))
				.alpha(getAlpha(selectionState))
				.create());

		return res;
	}

	public ScheduleEvent getScheduleEvent() {
		return se;
	}

	public abstract Color getBackgroundColour();

	public abstract int getAlpha(ScheduleEventSelectionState s);
	public int getAlpha() {
		return getAlpha(selectionState);
	}

	protected abstract Color getBorderColour();
	protected abstract boolean getIsBorderInner();
	public abstract int getBorderThickness(ScheduleEventSelectionState s);
	public int getBorderThickness() {
		return getBorderThickness(selectionState);
	}
	
	public Color getLabelTextColour() {
		return ScheduleChartColourUtils.calculateTextColourForBestContrast(getBackgroundColour());
	}

	public ScheduleEventSelectionState getSelectionState() {
		return selectionState;
	}
}
