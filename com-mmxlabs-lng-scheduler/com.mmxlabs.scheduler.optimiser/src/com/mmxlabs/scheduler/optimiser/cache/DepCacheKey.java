/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * The {@link DepCacheKey} is a supplemental component of a {@link CacheKey} linking additional elements that could cause the cached data to change in some way. FOr example a cargo could be depenedent
 * on another cargoes state for it's P&L calculations.
 * 
 * @author Simon Goodall
 *
 */
public final class DepCacheKey {

	private final @NonNull IVesselAvailability vesselAvailability;
	private final @NonNull IPortTimesRecord portTimesRecord;
	private final long startHeelInM3;
	private final int hash;

	public DepCacheKey(final @NonNull IVesselAvailability vesselAvailability, final long startHeelInM3, @NonNull final IPortTimesRecord portTimesRecord) {

		this.vesselAvailability = vesselAvailability;
		this.startHeelInM3 = startHeelInM3;
		this.portTimesRecord = portTimesRecord;

		// Pre-compute the hash
		this.hash = Objects.hash(vesselAvailability, startHeelInM3, portTimesRecord);
	}

	@Override
	public final int hashCode() {
		return hash;
	}

	@Override
	public final boolean equals(final Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof DepCacheKey) {
			final DepCacheKey other = (DepCacheKey) obj;
			
			final IPortSlot thisReturnSlot = portTimesRecord.getReturnSlot();
			final IPortSlot otherReturnSlot = other.portTimesRecord.getReturnSlot();
			// Do some quicker comparisons first
			final boolean partA = (this.startHeelInM3 == other.startHeelInM3) //
					&& Objects.equals(this.vesselAvailability, other.vesselAvailability) //
					&& Objects.equals(thisReturnSlot, otherReturnSlot) //
					&& Objects.equals(this.portTimesRecord.getSlots(), other.portTimesRecord.getSlots());

			// Then do more detailed comparisons
			if (partA) {
				for (final IPortSlot slot : this.portTimesRecord.getSlots()) {
					if (this.portTimesRecord.getSlotDuration(slot) != other.portTimesRecord.getSlotDuration(slot)) {
						return false;
					}
					if (this.portTimesRecord.getSlotTime(slot) != other.portTimesRecord.getSlotTime(slot)) {
						return false;
					}
				}
				
				if (thisReturnSlot != null && otherReturnSlot != null) {
					if (this.portTimesRecord.getSlotTime(thisReturnSlot) != other.portTimesRecord.getSlotTime(otherReturnSlot)) {
						return false;
					}
				}

				return true;
			}
		}
		return false;
	}

	public IVesselAvailability getVesselAvailability() {
		return vesselAvailability;
	}

	public IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public long getStartHeelInM3() {
		return startHeelInM3;
	}
}
