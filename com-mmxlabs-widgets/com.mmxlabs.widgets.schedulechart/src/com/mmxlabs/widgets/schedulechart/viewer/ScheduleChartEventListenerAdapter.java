package com.mmxlabs.widgets.schedulechart.viewer;

import java.util.Collection;

import org.eclipse.swt.events.MouseEvent;

import com.mmxlabs.widgets.schedulechart.IScheduleChartEventListener;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public class ScheduleChartEventListenerAdapter implements IScheduleChartEventListener {

	@Override
	public void eventSelected(ScheduleEvent e, Collection<ScheduleEvent> allSelectedEvents, MouseEvent me) {}

}
