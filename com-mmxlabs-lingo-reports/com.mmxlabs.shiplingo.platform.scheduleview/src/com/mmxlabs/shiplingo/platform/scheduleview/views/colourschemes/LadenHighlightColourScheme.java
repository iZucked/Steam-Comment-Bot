/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Alert_Crimson;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LadenHighlightColourScheme extends ColourScheme {

	private GanttChartViewer viewer;

	@Override
	public String getName() {
		return "Laden Highlight";
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
	public Color getForeground(final Object element) {
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.isLaden()) {
				return ColorCache.getColor(0, 180, 80);
			} else {
				return ColorCache.getColor(200, 200, 200);
				// return ColorCache.getColor(223, 115, 255);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.isLaden()) {
				return ColorCache.getColor(40, 255, 80);
			} else {
				return null;
			}
		} else if (element instanceof VesselEventVisit) {
			return ColorCache.getColor(223, 115, 255);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return ColorCache.getColor(Alert_Crimson);
			}
			return ColorCache.getColor(0, 0, 0);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
				return ColorCache.getColor(255, 0, 0);
			}
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		return null;
	}
	

	@Override 
	public int getBorderWidth(final Object element) {
		return 1;
	}
}
