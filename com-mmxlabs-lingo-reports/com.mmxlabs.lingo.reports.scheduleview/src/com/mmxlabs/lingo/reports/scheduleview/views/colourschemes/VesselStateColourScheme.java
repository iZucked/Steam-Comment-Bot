/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.swt.graphics.Color;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
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
		return null;// ColourPalette.getInstance().Alert_Crimson);
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			if (journey.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, ColourElements.Background);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, ColourElements.Background);
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Idle, ColourElements.Background);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Idle, ColourElements.Background);
			}
		} else if (element instanceof VesselEventVisit) {
			VesselEventVisit vev = (VesselEventVisit) element;
			if (vev.getVesselEvent() instanceof DryDockEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, ColourElements.Background);
			} else if (vev.getVesselEvent() instanceof CharterOutEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_CharterOut, ColourElements.Background);
			}
		} else if (element instanceof GeneratedCharterOut) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Background);
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			if (isOutsideTimeWindow(visit)) {
				if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Load, ColourElements.Background);
				} else if (visit.getSlotAllocation().getSlot() instanceof DischargeSlot) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Discharge, ColourElements.Background);
				}
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, ColourElements.Background);
			} else {
				if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Load, ColourElements.Background);
				} else if (visit.getSlotAllocation().getSlot() instanceof DischargeSlot) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Discharge, ColourElements.Background);
				}
			}
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vev = (VesselEventVisit) element;
			if (isOutsideTimeWindow(vev)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, ColourElements.Background);
			}
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {

		int alpha = 255;
		if (element instanceof Event) {
			Event ev = (Event) (element);
			// if(isLocked(ev, viewer) && !isOutsideTimeWindow(ev)) alpha = Faded_Alpha;
		} else if (element instanceof GeneratedCharterOut) {
			alpha -= 20;
		}
		return alpha;
	}

	@Override
	public Color getBorderColour(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			if (isLocked(event, viewer)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Locked, ColourElements.Border);
			}
			if (event instanceof SlotVisit) {
				SlotVisit sv = (SlotVisit) event;
				if (ColourSchemeUtil.isFOBSaleCargo(sv)) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.FOB_Sale, ColourElements.Border);
				} else if (ColourSchemeUtil.isDESPurchaseCargo(sv)) {
					return ColourPalette.getInstance().getColourFor(ColourPaletteItems.DES_Purchase, ColourElements.Border);
				}
			}
		}

		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			if (event instanceof SlotVisit) {
				SlotVisit sv = (SlotVisit) event;
				if (ColourSchemeUtil.isFOBSaleCargo(sv) || ColourSchemeUtil.isDESPurchaseCargo(sv))
					return 2;
			} else if (isLocked(event, viewer))
				return 1;
		}
		return 1;
	}
}
