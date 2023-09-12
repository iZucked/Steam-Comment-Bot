/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.viewer;

import java.util.Collection;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartContentBoundsProvider;
import com.mmxlabs.widgets.schedulechart.IScheduleChartEventListener;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;

public class ScheduleChartEventListenerAdapter implements IScheduleChartEventListener {

	@Override
	public void eventSelected(ScheduleEvent e, Collection<ScheduleEvent> allSelectedEvents, MouseEvent me) {}

	@Override
	public void timeScaleZoomLevelChanged(Rectangle mainBounds, IScheduleChartContentBoundsProvider boundsProvider) {}

	@Override
	public void annotationEdited(ScheduleEvent se, ScheduleEventAnnotation old, ScheduleEventAnnotation updated) {}

}
