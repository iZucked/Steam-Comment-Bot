/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

final class NonShippedCargoCacheKey {

	public @NonNull VoyagePlan plan;
	public @NonNull IVesselAvailability vesselAvailability;
	public int planStartTime;
	public int vesselStartTime;
	private final int hash;

	public NonShippedCargoCacheKey(@NonNull final VoyagePlan plan, @NonNull final IVesselAvailability vesselAvailability, final int planStartTime, final int vesselStartTime) {
		this.plan = plan;
		this.vesselAvailability = vesselAvailability;
		this.planStartTime = planStartTime;
		this.vesselStartTime = vesselStartTime;

		this.hash = Objects.hash(plan, vesselAvailability, planStartTime, vesselStartTime);
	}

	@Override
	public final int hashCode() {

		return hash;
	}

	/**
	 * This equals method almost certainly doesn't fulfil the normal equality contract; however it should be fast, and because this class is final and private it ought not end up getting used wrongly.
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof NonShippedCargoCacheKey) {
			final NonShippedCargoCacheKey other = (NonShippedCargoCacheKey) obj;
			return planStartTime == other.planStartTime //
					&& vesselStartTime == other.vesselStartTime //
					&& vesselAvailability.equals(other.vesselAvailability) //
					&& plan.equals(other.plan);
		}
		return false;
	}
}
