/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.ganttchart.SpecialDrawModes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;

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

		OpenOrNonShippedType classification = classify(element);
		if (classification != null) {
			if (!classification.fob() || classification.multi()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
			} else if (classification.open() && !classification.optional()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			} else {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
			}
		}

		if (element instanceof Journey journey) {
			if (journey.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, ColourElements.Background);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, ColourElements.Background);
			}
		} else if (element instanceof Idle idle) {
			if (idle.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Idle, ColourElements.Background);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Idle, ColourElements.Background);
			}
		} else if (element instanceof VesselEventVisit vev) {
			if (isOutsideTimeWindow(vev)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, ColourElements.Background);
			}
			if (vev.getVesselEvent() instanceof DryDockEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, ColourElements.Background);
			} else if (vev.getVesselEvent() instanceof MaintenanceEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Maintenance, ColourElements.Background);
			} else if (vev.getVesselEvent() instanceof CharterOutEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_CharterOut, ColourElements.Background);
			}
		} else if (element instanceof GeneratedCharterOut) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Background);
		} else if (element instanceof CharterLengthEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_CharterLength, ColourElements.Background);

		} else if (element instanceof CanalJourneyEvent canalBookingEvent) {
			final Journey journey = canalBookingEvent.getLinkedJourney();
			if (journey.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, ColourElements.Background);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, ColourElements.Background);
			}
		} else if (element instanceof CharterAvailableFromEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Charter_Range, ColourElements.Background);
		} else if (element instanceof CharterAvailableToEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Charter_Range, ColourElements.Background);
		} else if (element instanceof InventoryChangeEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Inventory_Breach, ColourElements.Background);

		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit visit) {
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
		}
		return null;
	}

	@Override
	public int getAlpha(final Object element) {

		int alpha = 255;
		if (element instanceof Event) {
//			Event ev = (Event) (element);
			// if(isLocked(ev, viewer) && !isOutsideTimeWindow(ev)) alpha = Faded_Alpha;
		} else if (element instanceof GeneratedCharterOut) {
			alpha -= 20;
		}
		return alpha;
	}

	@Override
	public SpecialDrawModes getSpecialDrawMode(final Object element) {
		OpenOrNonShippedType classification = classify(element);
		if (classification != null) {
			if (classification.multi()) {
				if (classification.fob() && classification.des()) {
					return SpecialDrawModes.THREE_DOTS_WITH_BORDER;
				} else if (classification.fob()) {
					return SpecialDrawModes.THREE_DOTS_NO_BORDER;
				} else {
					return SpecialDrawModes.ONE_DOT_WITH_BORDER;
				}
			} else {
				if (classification.fob()) {
					return SpecialDrawModes.SOLID;
				} else {
					return SpecialDrawModes.BORDER_ONLY;
				}
			}
		}
		return SpecialDrawModes.NONE;
	}

	@Override
	public Color getBorderColour(final Object element) {

		OpenOrNonShippedType classification = classify(element);
		if (classification != null) {
			if (classification.open() && !classification.optional()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			} else {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
			}
		}

		if (element instanceof Event event) {
			if (isLocked(event)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Locked, ColourElements.Border);
			}
			if (event instanceof CanalJourneyEvent canalBookingEvent) {
				Journey linkedJourney = canalBookingEvent.getLinkedJourney();
				if (linkedJourney.getCanalBooking() == null) {
					return ColourPalette.getInstance().getColour(ColourPalette.Black);
				}
			}
		}

		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {

		return 1;
	}

	private static record OpenOrNonShippedType(boolean multi, boolean fob, boolean des, boolean buy, boolean optional, boolean open) {
	}

	private @Nullable OpenOrNonShippedType classify(Object element) {
		if (element instanceof MultiEvent multiEvent) {
			boolean fob = false;
			boolean des = false;
			boolean buy = false;
			boolean sell = false;
			boolean compulsary = false;
			boolean open = false;
			for (Object e : multiEvent.getElements()) {
				OpenOrNonShippedType t = classify(e);
				fob |= t.fob();
				des |= !t.fob();

				buy |= t.buy();
				sell |= !t.buy();

				compulsary |= t.open() && !t.optional();
				open |= t.open();
			}
			// Can't have mixed buy and sell in the same event/row
			assert !(buy && sell);

			return new OpenOrNonShippedType(true, fob, des, buy, !compulsary, open);
		} else if (element instanceof OpenSlotAllocation sa) {
			if (sa.getSlot() != null) {
				if (sa.getSlot() instanceof LoadSlot s) {
					return new OpenOrNonShippedType(false, !s.isDESPurchase(), s.isDESPurchase(), true, s.isOptional(), true);
				} else if (sa.getSlot() instanceof DischargeSlot s) {
					return new OpenOrNonShippedType(false, s.isFOBSale(), !s.isFOBSale(), false, s.isOptional(), true);
				}
			}
		}

		if (element instanceof SlotVisit sv) {
			if (ColourSchemeUtil.isFOBSaleCargo(sv)) {
				boolean buy = sv.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.PURCHASE;
				return new OpenOrNonShippedType(false, true, false, buy, sv.getSlotAllocation().getSlot().isOptional(), false);
			} else if (ColourSchemeUtil.isDESPurchaseCargo(sv)) {
				boolean buy = sv.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.PURCHASE;
				return new OpenOrNonShippedType(false, false, true, buy, sv.getSlotAllocation().getSlot().isOptional(), false);
			}
		}

		return null;
	}
}
