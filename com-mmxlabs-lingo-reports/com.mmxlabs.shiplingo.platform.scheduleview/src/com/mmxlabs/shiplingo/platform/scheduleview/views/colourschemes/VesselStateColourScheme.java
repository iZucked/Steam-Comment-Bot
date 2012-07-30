/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Alert_Crimson;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Faded_Alpha;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Gas_Blue;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Green;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Light_Gas_Blue;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Light_Green;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Locked_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Slot_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.VesselEvent_Purple;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isLate;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class VesselStateColourScheme extends ColourScheme {
	
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
				return ColorCache.getColor(Green);
			} else {
				return ColorCache.getColor(Gas_Blue);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.isLaden()) {
				return ColorCache.getColor(Light_Green);
			} else {
				return ColorCache.getColor(Light_Gas_Blue);
			}
		} else if (element instanceof VesselEventVisit) {
			VesselEventVisit vev = (VesselEventVisit) element;
			if(vev.getVesselEvent() instanceof DryDockEvent){
				return ColorCache.getColor(VesselEvent_Purple);
			}
			else if(vev.getVesselEvent() instanceof CharterOutEvent){
				return ColorCache.getColor(90, 150, 0);
			}			
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (isLate(visit)) {
				return ColorCache.getColor(Alert_Crimson);
			} 
//			else if(isLocked(visit)) {
//				return ColorCache.getColor(Locked_White);
//			}
			return ColorCache.getColor(Slot_White);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (isLate(vev)) {
				return ColorCache.getColor(Alert_Crimson);
			}
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		
		if(element instanceof Event) {
			Event ev = (Event) (element);
			if(isLocked(ev, viewer) && !isLate(ev)) return Faded_Alpha;
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
}
