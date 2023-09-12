/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw; 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;

public abstract class DrawableScheduleEvent extends DrawableElement {
	
	private final ScheduleEvent se;
	private ScheduleEventSelectionState selectionState;
	
	protected DrawableScheduleEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		this.se = se;
		this.selectionState = noneSelected ? ScheduleEventSelectionState.SELECTED : se.getSelectionState();
		setBounds(bounds);
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		if (!se.isVisible()) {
			return res;
		}
		
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds)
				.bgColour(getBackgroundColour())
				.border(getBorderColour(), getBorderThickness(selectionState), getIsBorderInner())
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
}
