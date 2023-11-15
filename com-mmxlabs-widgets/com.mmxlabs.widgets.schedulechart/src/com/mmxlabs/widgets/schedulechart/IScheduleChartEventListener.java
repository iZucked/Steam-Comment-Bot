/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

@NonNullByDefault
public interface IScheduleChartEventListener {
	
	void eventSelected(ScheduleEvent e, MouseEvent me);
	
	void annotationEdited(ScheduleEvent se, ScheduleEventAnnotation old, ScheduleEventAnnotation updated);

	void timeScaleZoomLevelChanged(Rectangle mainBounds, IScheduleChartContentBoundsProvider boundsProvider);
	
}
