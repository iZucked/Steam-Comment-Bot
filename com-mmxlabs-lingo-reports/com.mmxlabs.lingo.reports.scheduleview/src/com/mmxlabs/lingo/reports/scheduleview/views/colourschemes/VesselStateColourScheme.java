/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.ColourPalette.*;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
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
				return ColorCache.getColor(Vessel_Laden_Journey);
			} else {
				return ColorCache.getColor(Vessel_Ballast_Journey);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.isLaden()) {
				return ColorCache.getColor(Vessel_Laden_Idle);
			} else {
				return ColorCache.getColor(Vessel_Ballast_Idle);
			}
		} else if (element instanceof VesselEventVisit) {
			VesselEventVisit vev = (VesselEventVisit) element;
			if(vev.getVesselEvent() instanceof DryDockEvent){
				return ColorCache.getColor(Vessel_Dry_Dock);
			}
			else if(vev.getVesselEvent() instanceof CharterOutEvent){
				return ColorCache.getColor(Vessel_Charter_Out);
			}			
		} else if (element instanceof GeneratedCharterOut) {
			return ColorCache.getColor(Vessel_Generated_Charter_Out);
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
//			if(isLocked(ev, viewer) && !isOutsideTimeWindow(ev)) alpha = Faded_Alpha;
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
