/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.AbstractNinetyDayEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.AbstractTextContentNinetyDayEventTooltip;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

/**
 * Drawable tooltip that lists all buy/sell events that happen at the same time.
 * @author AP
 * 
 * Notes: Despite the fact that tooltip extends base tooltip classes,
 * It is different. abstract tooltip has header, body and footer, while
 * the buy sell tooltip draws everything in its body.
 */
public class BuySellEventTooltip extends AbstractNinetyDayEventTooltip {

	private final PositionsSequenceElement positionsSeqenceElement;
	
	public BuySellEventTooltip(final ScheduleEventTooltip tooltip, final PositionsSequenceElement positionsSequenceElement) {
		super(tooltip);
		this.positionsSeqenceElement = positionsSequenceElement;
	}
	
	@Override
	protected int getHeaderHeight(final DrawerQueryResolver drawerQueryResolver) {
		return 40;
	}
	
	@Override
	protected int getFooterHeight(DrawerQueryResolver resolver) {
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
