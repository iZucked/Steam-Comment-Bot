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
	public ISequence getSequenceFromResource(@NonNull ISequences sequences, @NonNull IResource resource) {
		ISequence sequence = sequences.getSequence(resource);
		return sequence;
	}

	@NonNull
	public IVesselAvailability getVesselAvailabilityFromResource(@NonNull IResource resource) {
		return vesselProvider.getVesselAvailability(resource);
	}
	
	public long getVesselCharterInRatePerDay(IVesselAvailability vesselAvailability, int vesselStartTime, int voyagePlanStartTime) {
		long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, vesselStartTime, voyagePlanStartTime);
		return vesselCharterInRatePerDay;
	}
}
