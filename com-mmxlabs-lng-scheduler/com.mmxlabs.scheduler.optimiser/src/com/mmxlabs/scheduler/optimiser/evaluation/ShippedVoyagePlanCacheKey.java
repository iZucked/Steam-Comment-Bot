/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * A data storage class used by a cache object to calculate it's data.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class ShippedVoyagePlanCacheKey {

	public final Object vesselKey;
	public final ICharterCostCalculator charterCostCalculator;
	public final int vesselStartTime;
	public final @Nullable IPort firstLoadPort;
	public final PreviousHeelRecord previousHeelRecord;
	public final IPortTimesRecord portTimesRecord;
	public final boolean lastPlan;
	public final boolean keepDetails;

	private final List<AvailableRouteChoices> voyageKeys = new LinkedList<>();
	private final List<@Nullable IRouteOptionBooking> canalBookings = new LinkedList<>();
	private final List<String> slotsIds = new LinkedList<>();
	private final List<Integer> slotTimes = new LinkedList<>();
	private final boolean isCargo;
	private final int hash;
	private int effectiveLastHeelPricePerMMBTU;
	private int effectiveLastHeelCV;
	public final IVesselAvailability vesselAvailability;
	public final ISequencesAttributesProvider sequencesAttributesProvider;

	// Not part of cache
	public final IResource resource;

	public ShippedVoyagePlanCacheKey(final IResource resource, final IVesselAvailability vesselAvailability, ICharterCostCalculator charterCostCalculator, final int vesselStartTime,
			@Nullable final IPort firstLoadPort, final PreviousHeelRecord previousHeelRecord, final IPortTimesRecord portTimesRecord, boolean lastPlan, boolean keepDetails,
			ISequencesAttributesProvider sequencesAttributesProvider) {

		this.sequencesAttributesProvider = sequencesAttributesProvider;
		// Spot market vessels are equivalent
		ISpotCharterInMarket spotCharterInMarket = vesselAvailability.getSpotCharterInMarket();
		if (!keepDetails && spotCharterInMarket != null && vesselAvailability.getSpotIndex() >= 0) {
			this.vesselKey = spotCharterInMarket;
		} else {
			this.vesselKey = vesselAvailability;
		}
		//
		this.charterCostCalculator = charterCostCalculator;
		this.vesselStartTime = vesselStartTime;
		this.firstLoadPort = firstLoadPort;
		this.previousHeelRecord = previousHeelRecord;
		this.portTimesRecord = portTimesRecord;
		this.lastPlan = lastPlan;
		this.keepDetails = keepDetails;
		for (IPortSlot slot : portTimesRecord.getSlots()) {
			voyageKeys.add(portTimesRecord.getSlotNextVoyageOptions(slot));
			slotTimes.add(portTimesRecord.getSlotTime(slot));
			canalBookings.add(portTimesRecord.getRouteOptionBooking(slot));
			slotsIds.add(slot.getKey()); // Use equivalence key rather than real id if possible
		}
		IPortSlot returnSlot = portTimesRecord.getReturnSlot();
		if (returnSlot != null) {
			slotTimes.add(portTimesRecord.getSlotTime(returnSlot));
			slotsIds.add(portTimesRecord.getReturnSlot().getKey()); // Use equivalence key rather than real id if possible
		}
		isCargo = portTimesRecord.getSlots().get(0) instanceof ILoadOption;

		// These values have no impact on a cargo - heel volume is the important field
		// going into a cargo.
		effectiveLastHeelPricePerMMBTU = isCargo ? 0 : previousHeelRecord.lastHeelPricePerMMBTU;
		effectiveLastHeelCV = isCargo ? 0 : previousHeelRecord.lastCV;
		this.hash = Objects.hash(lastPlan, keepDetails, //
				vesselKey, // Vessel
				// charterCostCalculator, // Charter costs
				getPortName(firstLoadPort), previousHeelRecord.heelVolumeInM3, effectiveLastHeelCV, effectiveLastHeelPricePerMMBTU, // Heel record info
				slotsIds, //
				slotTimes, // Slot times.
				canalBookings, voyageKeys);

		this.resource = resource;
		this.vesselAvailability = vesselAvailability;
	}

	private String getPortName(@Nullable final IPort port) {
		String portName = port == null ? "" : port.getName();
		return portName;
	}

	@Override
	public final int hashCode() {

		return hash;
	}

	@Override
	public final boolean equals(final @Nullable Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof ShippedVoyagePlanCacheKey) {
			final ShippedVoyagePlanCacheKey other = (ShippedVoyagePlanCacheKey) obj;
			// Do quick simple checks first
			final boolean partA = keepDetails == other.keepDetails //
					&& Objects.equals(getPortName(firstLoadPort), getPortName(other.firstLoadPort)) && lastPlan == other.lastPlan //
					&& previousHeelRecord.heelVolumeInM3 == other.previousHeelRecord.heelVolumeInM3 //
					&& effectiveLastHeelCV == other.effectiveLastHeelCV //
					&& effectiveLastHeelPricePerMMBTU == other.effectiveLastHeelPricePerMMBTU //
					&& previousHeelRecord.forcedCooldown == other.previousHeelRecord.forcedCooldown //
					&& Objects.equals(sequencesAttributesProvider, other.sequencesAttributesProvider) //
					&& Objects.equals(vesselKey, other.vesselKey); //

			if (!partA) {
				return false;
			}

			if (!keepDetails) {
				// Perform simple equivalence checks as we don't store the fully built up data
				// structures.
				// Spot market slots and charter in vessel options can be equivalent.
				return Objects.equals(slotsIds, other.slotsIds) //
						&& Objects.equals(slotTimes, other.slotTimes) //
						&& Objects.equals(canalBookings, other.canalBookings) //
						&& Objects.equals(voyageKeys, other.voyageKeys);
			} else {
				// With keep details, we need to ensure exact matches rather than just
				// equivalence
				if (vesselAvailability != other.vesselAvailability) {
					return false;
				}

				if (!portTimesRecord.equals(other.portTimesRecord)) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}
