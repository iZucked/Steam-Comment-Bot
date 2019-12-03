/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IVoyagePlanner {

	public static final int ROUNDING_EPSILON = 10;

	/**
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	List<@NonNull Pair<VoyagePlan, IPortTimesRecord>> makeVoyagePlans(@NonNull IResource resource, @NonNull ISequence sequence, @NonNull List<IPortTimesRecord> portTimesRecords);

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	@Nullable
	VoyagePlan makeVoyage(@NonNull IResource resource, @NonNull final ICharterCostCalculator charterCostCalculator, @NonNull IPortTimesRecord portTimesRecord, long[] heelVolumeRangeInM3, int startingTime);

	/**
	 * Returns a voyage plan based on the given sequence
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes
	 * @return
	 */
	@Nullable
	VoyagePlan makeVoyage(@NonNull IResource resource, @NonNull final ICharterCostCalculator charterCostCalculator, @NonNull IPortTimesRecord portTimesRecord, long[] initialHeelVolumeRangeInM3,
			@NonNull IVoyagePlanOptimiser voyagePlanOptimiser, int startingTime);

	@NonNull
	VoyagePlan makeDESOrFOBVoyagePlan(@NonNull IResource resource, @NonNull IPortTimesRecord portTimesRecord);

	@NonNull
	Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> makeDESOrFOBVoyagePlanPair(@NonNull IResource resource, @NonNull ISequence sequence, @NonNull IPortTimesRecord portTimesRecord);

	/**
	 * Returns a VoyagePlan produced by the optimiser from a cargo itinerary.
	 * 
	 * @param voyageOrPortOptionsSubsequence
	 *            An alternating list of PortOptions and VoyageOptions objects
	 * @param arrivalTimes
	 * @param optimiser
	 * @param startHeelVolumeInM3
	 * @return An optimised VoyagePlan
	 */
	@Nullable
	VoyagePlan getOptimisedVoyagePlan(@NonNull List<@NonNull IOptionsSequenceElement> voyageOrPortOptionsSubsequence, @NonNull IPortTimesRecord portTimesRecord,
			@NonNull IVoyagePlanOptimiser optimiser, long @NonNull [] heelVolumeRangeInM3, @NonNull final ICharterCostCalculator charterCostCalculator, @NonNull VesselInstanceType vesselInstanceType,
			Triple<@NonNull IVessel, @Nullable IResource,  int[]> vesselTriple, @NonNull List<@NonNull IVoyagePlanChoice> vpoChoices, int startingTime);
}
