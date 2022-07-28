/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
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
	public IVesselCharter getVesselCharterFromResource(@NonNull final IResource resource) {
		return vesselProvider.getVesselCharter(resource);
	}

	@NonNull
	public List<IVesselCharter> getAllVesselCharters() {
		return vesselProvider.getSortedResources().stream().map(r -> vesselProvider.getVesselCharter(r)).collect(Collectors.toList());
	}

	public long getVesselCharterInRatePerDay(final IVesselCharter vesselCharter, final int voyagePlanStartTime) {
		/*
		 * final long charterRatePerDay = schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselCharter, vesselStartTime, purchaseIntervals[purchaseIndex].start);
		 * 
		 */

		/*
		 * return schedulerCalculationUtils.getVesselCharterInRatePerDay(vesselCharter, portTimeWindowRecord.getFirstSlotFeasibleTimeWindow().getInclusiveStart(),
		 * portTimeWindowRecord.getFirstSlotFeasibleTimeWindow().getInclusiveStart());
		 */
		final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselCharter, voyagePlanStartTime);
		return vesselCharterInRatePerDay;
	}
	/*
	 * TODO: replace above method with this one. public long getVesselCharterCost(final IVesselCharter vesselCharter, final int vesselStartTime, final int vesselEndTime) { final long
	 * vesselCharterInRatePerDay = charterCostCalculator.getCharterCost(vesselCharter, vesselStartTime, v); return vesselCharterInRatePerDay; }
	 */

	/**
	 * Determines whether the vessel should be considered optional, and consequently the charter cost may not be sunk.
	 * 
	 * @param vesselCharter
	 * @return
	 */
	public static boolean isVesselCharterOptional(IVesselCharter vesselCharter) {
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP || vesselCharter.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER
				|| vesselCharter.isOptional()) {
			return true;
		}
		return false;
	}
}
