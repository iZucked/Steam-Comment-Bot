/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class PortEventTooltip extends AbstractNinetyDayEventTooltip {

	public PortEventTooltip(ScheduleEventTooltip tooltip) {
		super(tooltip);
	}

	@Override
	protected int getHeaderHeight(DrawerQueryResolver r) {
		return 20;
	}

	@Override
	protected int getFooterHeight(DrawerQueryResolver r) {
		return 40;
	}

	@Override
	protected DrawableElement getTooltipHeader() {
		return new DrawableElement() {
			
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver resolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				if(!tooltip.headerNames().isEmpty())
					res.add(BasicDrawableElements.Text.from(bounds.x + TEXT_PADDING, bounds.y + TEXT_PADDING,  tooltip.headerNames().get(0)).textColour(getTextColour()).create());
				
				return res;
			}
		};
	}

}
