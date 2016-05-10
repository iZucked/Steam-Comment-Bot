/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class VesselAssignmentFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) object;
			// final Sequence sequence = cargoAllocation.getSequence();
			// if (sequence != null) {
			// if (!sequence.isFleetVessel()) {
			final Cargo inputCargo = cargoAllocation.getInputCargo();
			if (inputCargo == null) {
				return null;
			}
			switch (inputCargo.getCargoType()) {
			case DES:
				for (final Slot slot : inputCargo.getSortedSlots()) {
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
				for (final Slot slot : inputCargo.getSortedSlots()) {
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
				final VesselAssignmentType vesselAssignmentType = inputCargo.getVesselAssignmentType();

				if (vesselAssignmentType instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
					final Vessel vessel = vesselAvailability.getVessel();
					if (vessel != null) {
						return vessel.getName();
					}
				} else if (vesselAssignmentType instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
					return String.format("%s (spot)", charterInMarket.getName());
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
				return sequence.getName();
			}
		}

		return null;
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
