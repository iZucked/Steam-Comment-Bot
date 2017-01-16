/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.lingo.reports.scheduleview.views.IScheduleViewColourScheme;
import com.mmxlabs.models.lng.schedule.Event;

public abstract class ColourScheme implements IScheduleViewColourScheme {

	private static final int Faded_Alpha = 200;

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
		if (id != null) {
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

		if (element instanceof Event) {
			Event ev = (Event) (element);
			if (isLocked(ev, viewer) && !isOutsideTimeWindow(ev))
				return Faded_Alpha;
		}
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			if (isLocked(event, viewer))
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Locked, ColourElements.Border);
		}
		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			if (isLocked(event, viewer))
				return 1;
		}
		return 1;
	}
}