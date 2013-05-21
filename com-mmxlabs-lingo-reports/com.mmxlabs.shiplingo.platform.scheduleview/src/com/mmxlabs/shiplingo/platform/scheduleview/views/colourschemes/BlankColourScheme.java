/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Alert_Crimson;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Faded_Alpha;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Locked_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class BlankColourScheme extends ColourScheme {
	
	@Override
	public String getName() {
		return "Vessel State";
	}

	@Override
	public Color getForeground(final Object element) {
		return ColorCache.getColor(Alert_Crimson);
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.isLaden()) {
			} else {
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.isLaden()) {
			} else {
			}
		} else if (element instanceof VesselEventVisit) {
		}

		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (isOutsideTimeWindow(visit)) {
			} 
		} else if (element instanceof VesselEventVisit) {
		}
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
		}

		return 1;
	}
}
