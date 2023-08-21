package com.mmxlabs.widgets.schedulechart;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

public interface IScheduleChartEventListener {
	
	void eventSelected(ScheduleEvent e, Collection<ScheduleEvent> allSelectedEvents, MouseEvent me);
	
	void timeScaleZoomLevelChanged(Rectangle mainBounds, IScheduleChartContentBoundsProvider boundsProvider);

}