/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.providers;

import java.util.List;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement;

public interface IDrawableScheduleEventLabelProvider {
	
	public List<RelativeDrawableElement> getEventLabels(final DrawableScheduleEvent dse, DrawerQueryResolver r);

}
