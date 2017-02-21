/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.util;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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

	public long getVesselCharterInRatePerDay(final IVesselAvailability vesselAvailability, final int vesselStartTime, final int voyagePlanStartTime) {
		final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, vesselStartTime, voyagePlanStartTime);
		return vesselCharterInRatePerDay;
	}
}
