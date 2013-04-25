/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Faded_Alpha;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Locked_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.shiplingo.platform.scheduleview.views.IScheduleViewColourScheme;

public abstract class ColourScheme implements IScheduleViewColourScheme {

	private String ID;
	protected GanttChartViewer viewer;

	public ColourScheme() {
		super();
		ID = "";
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String setID(String id) {
		if(id != null){
			ID = id;
		}
		return ID;
	}
	
	@Override
	public GanttChartViewer getViewer() {
		return viewer;
	}

	@Override
	public void setViewer(final GanttChartViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}
	
	@Override
	public int getAlpha(final Object element) {
		
		if(element instanceof Event) {
			Event ev = (Event) (element);
			if(isLocked(ev, viewer) && !isOutsideTimeWindow(ev)) return Faded_Alpha;
		}
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
	
		if (element instanceof Event) {
			final Event event = (Event) element;
			if(isLocked(event, viewer)) return ColorCache.getColor(Locked_White);
		}
		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {
	
		if (element instanceof Event) {
			final Event event = (Event) element;
			if(isLocked(event, viewer)) return 1;
		}
		return 1;
	}
}