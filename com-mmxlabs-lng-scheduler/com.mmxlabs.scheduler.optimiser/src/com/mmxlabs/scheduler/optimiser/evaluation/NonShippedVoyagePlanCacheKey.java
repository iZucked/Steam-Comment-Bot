/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * A data storage class used by a cache object to calculate it's data.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public final class NonShippedVoyagePlanCacheKey {

	public final IResource resource;
	public final IVesselCharter vesselCharter;
	public final IPortTimesRecord portTimesRecord;
	public final boolean keepDetails;
	public final ISequencesAttributesProvider sequencesAttributesProvider;
	
	private final List<Integer> slotTimes = new LinkedList<>();

	private final int hash;

	public NonShippedVoyagePlanCacheKey(final IResource resource, final IVesselCharter vesselCharter, final IPortTimesRecord portTimesRecord, final boolean keepDetails, ISequencesAttributesProvider sequencesAttributesProvider) {

		this.resource = resource;
		this.vesselCharter = vesselCharter;
		this.portTimesRecord = portTimesRecord;
		this.keepDetails = keepDetails;
		this.sequencesAttributesProvider = sequencesAttributesProvider;
		portTimesRecord.getSlots().forEach(slot -> slotTimes.add(portTimesRecord.getSlotTime(slot)));
		final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
		if (returnSlot != null) {
			slotTimes.add(portTimesRecord.getSlotTime(returnSlot));
		}
		this.hash = Objects.hash(keepDetails, vesselCharter, // Vessel
				portTimesRecord.getSlots().stream().map(IPortSlot::getId).collect(Collectors.toList()), // Slot Ids
				slotTimes // Slot times.
		);
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

		if (obj instanceof NonShippedVoyagePlanCacheKey) {
			final NonShippedVoyagePlanCacheKey other = (NonShippedVoyagePlanCacheKey) obj;
			final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
			final IPortSlot otherReturnSlot = other.portTimesRecord.getReturnSlot();
			final boolean partA = keepDetails == other.keepDetails //
					&& Objects.equals(vesselCharter, other.vesselCharter) //
					&& Objects.equals(returnSlot, otherReturnSlot) //
					&& Objects.equals(portTimesRecord.getSlots(), other.portTimesRecord.getSlots()) //
					&& Objects.equals(slotTimes, other.slotTimes) //
					&& Objects.equals(sequencesAttributesProvider, other.sequencesAttributesProvider) //
			;

			if (keepDetails && partA) {
				return (portTimesRecord.equals(other.portTimesRecord));
			}
			return partA;
		}
		return false;
	}
}
