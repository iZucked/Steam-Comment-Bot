/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Alert_Crimson;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.FOBDES_Grey;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Faded_Alpha;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Gas_Blue;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Green;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Light_Gas_Blue;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Light_Green;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Locked_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Slot_White;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.VesselEvent_Brown;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.VesselEvent_LightPurple;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.VesselEvent_Purple;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
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
				return ColorCache.getColor(VesselEvent_Brown);
			}
			else if(vev.getVesselEvent() instanceof CharterOutEvent){
				return ColorCache.getColor(VesselEvent_Purple);
			}			
		} else if (element instanceof GeneratedCharterOut) {
			return ColorCache.getColor(VesselEvent_LightPurple);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (isOutsideTimeWindow(visit)) {
				return ColorCache.getColor(Alert_Crimson);
			} 
			return ColorCache.getColor(Slot_White);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (isOutsideTimeWindow(vev)) {
				return ColorCache.getColor(Alert_Crimson);
			}
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {
		
		int alpha = 255;
		if(element instanceof Event) {
			Event ev = (Event) (element);
			if(isLocked(ev, viewer) && !isOutsideTimeWindow(ev)) alpha = Faded_Alpha;
		}
		else if (element instanceof GeneratedCharterOut) {
			alpha -= 20;			
		}
		return alpha;
	}

	@Override
	public Color getBorderColour(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			if(isLocked(event, viewer)) return ColorCache.getColor(Locked_White);
			if(event instanceof SlotVisit){
				SlotVisit sv = (SlotVisit) event;
				if (ColourSchemeUtil.isFOBSaleCargo(sv) || ColourSchemeUtil.isDESPurchaseCargo(sv)){
					return ColorCache.getColor(FOBDES_Grey);
				}
			}
		}

		return null;
	}
	
	@Override
	public int getBorderWidth(final Object element) {
	
		if (element instanceof Event) {
			final Event event = (Event) element;
			if(event instanceof SlotVisit){
				SlotVisit sv = (SlotVisit) event;
				if (ColourSchemeUtil.isFOBSaleCargo(sv) || ColourSchemeUtil.isDESPurchaseCargo(sv))
					return 2;
			}
			else if(isLocked(event, viewer)) return 1;
		}
		return 1;
	}
}
