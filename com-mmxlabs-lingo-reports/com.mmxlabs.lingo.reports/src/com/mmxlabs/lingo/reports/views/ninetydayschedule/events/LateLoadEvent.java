/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class LateLoadEvent extends LoadEvent {

	private static final int TOP_PADDING = 2;
	
	public LateLoadEvent(ScheduleEvent se, Rectangle bounds, boolean noneSelected) {
		super(se, bounds, noneSelected);
	}
	
	@Override
	public int getBorderThickness(ScheduleEventSelectionState s) {
		return super.getBorderThickness(s) + 1;
	}
	
	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();

		if (!getScheduleEvent().isVisible()) {
			return res;
		}
		
		bounds.y -= TOP_PADDING;
		bounds.height += TOP_PADDING;
		
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds)
				.bgColour(getBackgroundColour())
				.border(getBorderColour(), getBorderThickness(getSelectionState()), getIsBorderInner())
				.alpha(getAlpha(getSelectionState()))
				.create());

		return res;
	}

	@Override
	public boolean getIsBorderInner() {
		return true;
	}

	@Override
	protected Color getBorderColour() {
		return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Load, ColourElements.Background);
	}

}
