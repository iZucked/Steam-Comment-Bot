/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isLocked;
import static com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ColourSchemeUtil.isOutsideTimeWindow;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.ganttchart.SpecialDrawModes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.CharterLengthEvent;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedIdle;
import com.mmxlabs.models.lng.schedule.NonShippedJourney;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;

public class VesselStateColourScheme extends ColourScheme {

	@Override
	public String getName() {
		return "Vessel State";
	}

	private Color getColorFor(final Object element, final @NonNull ColourElements colourElement) {
		final OpenOrNonShippedType classification = classify(element);
		if (classification != null) {
			if (!classification.fob() || classification.multi()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
			} else if (classification.open() && !classification.optional()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			} else {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
			}
		}

		if (element instanceof final Journey journey) {
			if (journey.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, colourElement);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, colourElement);
			}
		} else if (element instanceof final NonShippedJourney journey) {
			final ColourPaletteItems item = journey.isLaden() ? ColourPaletteItems.Voyage_Laden_Journey : ColourPaletteItems.Voyage_Ballast_Journey;
			return ColourPalette.getInstance().getColourFor(item, colourElement);
		} else if (element instanceof final Idle idle) {
			if (idle.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Idle, colourElement);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Idle, colourElement);
			}
		} else if (element instanceof final NonShippedIdle idle) {
			final ColourPaletteItems item = idle.isLaden() ? ColourPaletteItems.Voyage_Laden_Idle : ColourPaletteItems.Voyage_Ballast_Non_Shipped_Idle;
			return ColourPalette.getInstance().getColourFor(item, colourElement);
		} else if (element instanceof final VesselEventVisit vev) {
			if (isOutsideTimeWindow(vev)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Event, colourElement);
			}
			if (vev.getVesselEvent() instanceof DryDockEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, colourElement);
			} else if (vev.getVesselEvent() instanceof MaintenanceEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Maintenance, colourElement);
			} else if (vev.getVesselEvent() instanceof CharterOutEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_CharterOut, colourElement);
			} else if (vev.getVesselEvent() instanceof CharterLengthEvent) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_CharterLength, colourElement);
			}
		} else if (element instanceof GeneratedCharterOut) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, colourElement);
		} else if (element instanceof GeneratedCharterLengthEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterLength, colourElement);

		} else if (element instanceof final CanalJourneyEvent canalBookingEvent) {
			final Journey journey = canalBookingEvent.getLinkedJourney();
			if (journey.isLaden()) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, colourElement);
			} else {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, colourElement);
			}
		} else if (element instanceof CharterAvailableFromEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Charter_Range, colourElement);
		} else if (element instanceof CharterAvailableToEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Charter_Range, colourElement);
		} else if (element instanceof InventoryChangeEvent) {
			return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Inventory_Breach, colourElement);

		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof final SlotVisit visit) {
			if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Load, colourElement);
			} else if (visit.getSlotAllocation().getSlot() instanceof DischargeSlot) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Discharge, colourElement);
			}
		} else if (element instanceof final NonShippedSlotVisit visit) {
			final Slot<?> slot = visit.getSlot();
			if (slot instanceof LoadSlot) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Load, colourElement);
			} else if (slot instanceof DischargeSlot) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Discharge, colourElement);
			}
		}
		return null;
	}

	@Override
	public Color getForeground(final Object element) {
		return getColorFor(element, ColourElements.Foreground);
	}

	@Override
	public Color getBackground(final Object element) {
		return getColorFor(element, ColourElements.Background);
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
		final OpenOrNonShippedType classification = classify(element);
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

		final OpenOrNonShippedType classification = classify(element);
		if (classification != null) {
			if (classification.open() && !classification.optional()) {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			} else {
				return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
			}
		}

		if (element instanceof final Event event) {
			if (isLocked(event)) {
				return ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Locked, ColourElements.Border);
			}
			if (event instanceof final CanalJourneyEvent canalBookingEvent) {
				final Journey linkedJourney = canalBookingEvent.getLinkedJourney();
				if (linkedJourney.getCanalBooking() == null) {
					return ColourPalette.getInstance().getColour(ColourPalette.Black);
				}
			}
			if (event instanceof SlotVisit sv) { 
				final PortVisitLateness pvl = sv.getLateness();
				return Display.getDefault().getSystemColor(pvl != null && pvl.getLatenessInHours() > 0 ? SWT.COLOR_RED : SWT.COLOR_DARK_GRAY);
			} else if (event instanceof NonShippedSlotVisit sv) {
				final PortVisitLateness pvl = sv.getLateness();
				return Display.getDefault().getSystemColor(pvl != null && pvl.getLatenessInHours() > 0? SWT.COLOR_RED : SWT.COLOR_DARK_GRAY);
			}
		}

		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {
		if ((element instanceof SlotVisit sv && sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0) //
				|| (element instanceof NonShippedSlotVisit sv && sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0)) {
			return 3;
		}
		return 1;
	}
	
	@Override
	public boolean getIsBorderInner(final Object element) {
		return (element instanceof SlotVisit sv && sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0)
				|| (element instanceof NonShippedSlotVisit sv && sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0);
	}

	private static record OpenOrNonShippedType(boolean multi, boolean fob, boolean des, boolean buy, boolean optional, boolean open) {
	}

	private @Nullable OpenOrNonShippedType classify(final Object element) {
		if (element instanceof final MultiEvent multiEvent) {
			boolean fob = false;
			boolean des = false;
			boolean buy = false;
			boolean sell = false;
			boolean compulsary = false;
			boolean open = false;
			for (final Object e : multiEvent.getElements()) {
				final OpenOrNonShippedType t = classify(e);
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
		} else if (element instanceof final OpenSlotAllocation sa) {
			if (sa.getSlot() != null) {
				if (sa.getSlot() instanceof final LoadSlot s) {
					return new OpenOrNonShippedType(false, !s.isDESPurchase(), s.isDESPurchase(), true, s.isOptional(), true);
				} else if (sa.getSlot() instanceof final DischargeSlot s) {
					return new OpenOrNonShippedType(false, s.isFOBSale(), !s.isFOBSale(), false, s.isOptional(), true);
				}
			}
		}

		if (element instanceof final SlotVisit sv) {
			if (ColourSchemeUtil.isFOBSaleCargo(sv)) {
				final boolean buy = sv.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.PURCHASE;
				// Any spot market is counted as open
				boolean isSpot = false;
				for (final SlotAllocation sa : sv.getSlotAllocation().getCargoAllocation().getSlotAllocations()) {
					isSpot |= sa.getSpotMarket() != null;
				}
				return new OpenOrNonShippedType(false, true, false, buy, sv.getSlotAllocation().getSlot().isOptional(), isSpot);
			} else if (ColourSchemeUtil.isDESPurchaseCargo(sv)) {
				final boolean buy = sv.getSlotAllocation().getSlotAllocationType() == SlotAllocationType.PURCHASE;
				// Any spot market is counted as open
				boolean isSpot = false;
				for (final SlotAllocation sa : sv.getSlotAllocation().getCargoAllocation().getSlotAllocations()) {
					isSpot |= sa.getSpotMarket() != null;
				}
				return new OpenOrNonShippedType(false, false, true, buy, sv.getSlotAllocation().getSlot().isOptional(), isSpot);
			}
		}

		return null;
	}
}
