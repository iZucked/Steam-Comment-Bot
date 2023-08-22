/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events;

import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;

public class PortEventTooltip extends AbstractNinetyDayEventTooltip {

	public PortEventTooltip(ScheduleEventTooltip tooltip) {
		super(tooltip);
	}

	@Override
	protected int getHeaderHeight(DrawerQueryResolver r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getBodyHeight(DrawerQueryResolver r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getFooterHeight(DrawerQueryResolver r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected DrawableElement getTooltipHeader() {
		return null;
	}

	@Override
	protected DrawableElement getTooltipBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DrawableElement getTooltipFooter() {
		// TODO Auto-generated method stub
		return null;
	}

}
