/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer.actions;

import java.util.Calendar;

import javax.management.timer.Timer;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.ganttchart.GanttChart;
import org.eclipse.nebula.widgets.ganttchart.GanttComposite;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.ganttviewer.internal.Activator;

/**
 */
public class PackAction extends Action {
	private final GanttChart ganttChart;

	public PackAction(final GanttChart ganttChart) {
		super();
		setText("Fit");
		setImageDescriptor(Activator.getImageDescriptor("/icons/pack.gif"));
		this.ganttChart = ganttChart;
	}

	@Override
	public void run() {
		// there may be a better way to do all this
		final GanttComposite composite = ganttChart.getGanttComposite();

		if (composite == null || composite.getVisibleBounds() == null) {
			return;
		}

		Calendar earliestDate = null;
		Calendar latestDate = null;

		for (final Object obj : composite.getEvents()) {
			if (obj instanceof GanttEvent) {
				final GanttEvent event = (GanttEvent) obj;
				final Calendar startDate = event.getEarliestStartDate();
				final Calendar endDate = event.getLatestEndDate();
				if ((earliestDate == null) || startDate.before(earliestDate)) {
					earliestDate = startDate;
				}
				if ((latestDate == null) || endDate.after(latestDate)) {
					latestDate = endDate;
				}
			}
		}

		// we have found the extent, now zoom
		if ((earliestDate != null) && (latestDate != null)) {
			// adjust the zoom
			final int daysNeeded = (int) ((latestDate.getTimeInMillis() - earliestDate.getTimeInMillis()) / (Timer.ONE_DAY)) + 28;

			final Rectangle visibleBounds = composite.getVisibleBounds();
			final int viewWidth = visibleBounds.width - composite.getLastSectionColumnWidth();
			final int ratio = (int) (Math.floor((float) viewWidth / (float) daysNeeded));

			int zoom = ISettings.MAX_ZOOM_LEVEL - (ratio - 1);
			// Lower than 5 is a bit too zoomed in.
			if (zoom < 5) {
				zoom = 5;
			}
			composite.setZoomLevel(zoom);

			final Calendar leftDate = (Calendar) earliestDate.clone();
			leftDate.setTimeInMillis(leftDate.getTimeInMillis());

			// Set the left hand side
			composite.setDate(leftDate, SWT.LEFT);
		}
		composite.setFocus();
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && (ganttChart != null) && ganttChart.getSettings().enableZooming();
	}

}
