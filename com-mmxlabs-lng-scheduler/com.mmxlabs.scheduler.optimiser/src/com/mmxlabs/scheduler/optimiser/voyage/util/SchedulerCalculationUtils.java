/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

public class SchedulerCalculationUtils {
	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private IVesselProvider vesselProvider;

	@NonNull
	public ISequence getSequenceFromResource(@NonNull final ISequences sequences, @NonNull final IResource resource) {
		final ISequence sequence = sequences.getSequence(resource);
		return sequence;
	}

	@NonNull
	public IVesselAvailability getVesselAvailabilityFromResource(@NonNull final IResource resource) {
		return vesselProvider.getVesselAvailability(resource);
	}
	
	@NonNull
	public List<IVesselAvailability> getAllVesselAvailabilities() {
		return vesselProvider.getSortedResources().stream()
				.map(r->vesselProvider.getVesselAvailability(r)).collect(Collectors.toList());
	}


	public long getVesselCharterInRatePerDay(final IVesselAvailability vesselAvailability, final int vesselStartTime, final int voyagePlanStartTime) {
		final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, vesselStartTime, voyagePlanStartTime);
		return vesselCharterInRatePerDay;
	}
	
	/**
	 * Determines whether the vessel should be considered optional, and consequently the charter
	 * cost may not be sunk.
	 * @param vesselAvailability
	 * @return
	 */
	public static boolean isVesselAvailabilityOptional(IVesselAvailability vesselAvailability) {
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER
				|| vesselAvailability.isOptional()) {
			return true;
		}
		return false;
	}
}
