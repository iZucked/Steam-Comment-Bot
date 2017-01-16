/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class VesselAssignmentFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) object;
			 
			switch (cargoAllocation.getCargoType()) {
			case DES:
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					Slot slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) slot;
						if (loadSlot.isDESPurchase()) {
							final Vessel assignment = slot.getNominatedVessel();
							if (assignment != null) {
								return assignment.getName();
							}
							break;
						}
					}
				}
			case FOB:
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					Slot slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) slot;
						if (dischargeSlot.isFOBSale()) {
							final Vessel assignment = slot.getNominatedVessel();
							if (assignment != null) {
								return assignment.getName();
							}
							break;
						}
					}
				}
			case FLEET:
				Sequence sequence = cargoAllocation.getSequence();
				if (sequence.isSetVesselAvailability()) {
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					final Vessel vessel = vesselAvailability.getVessel();
					if (vessel != null) {
						return vessel.getName();
					}
				} else if (sequence.isSetCharterInMarket()) {
					final CharterInMarket charterInMarket = sequence.getCharterInMarket();
					return formatCharterInMarket(charterInMarket, sequence.getSpotIndex());
				}
				break;
			default:
				break;
			}
			return null;

		} else if (object instanceof Event) {
			final Event event = (Event) object;
			final Sequence sequence = event.getSequence();
			if (sequence != null) {
				if (sequence.isSetCharterInMarket()) {
					return formatCharterInMarket(sequence.getCharterInMarket(), sequence.getSpotIndex());
				}

				return sequence.getName();
			}
		} else if (object instanceof Sequence) {
			final Sequence sequence = (Sequence) object;
			if (sequence.isSetCharterInMarket()) {
				return formatCharterInMarket(sequence.getCharterInMarket(), sequence.getSpotIndex());
			}

			return sequence.getName();
		}

		return null;
	}

	protected String formatCharterInMarket(final CharterInMarket charterInMarket, int spotIndex) {
		if (spotIndex == -1) {
			return String.format("%s (nominal)", charterInMarket.getName());
		} else {
			return String.format("%s (model)", charterInMarket.getName());
		}
	}

	@Override
	public Comparable<?> getComparable(final Object object) {

		// if (object instanceof CargoAllocation) {
		// final CargoAllocation cargoAllocation = (CargoAllocation) object;
		// final Sequence sequence = cargoAllocation.getSequence();
		// if (sequence != null) {
		// return sequence.getName();
		// }
		// } else if (object instanceof Event) {
		// final Event event = (Event) object;
		// final Sequence sequence = event.getSequence();
		// if (sequence != null) {
		// return sequence.getName();
		// }
		// }
		return render(object);
	}
}
