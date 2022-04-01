/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
@NonNullByDefault
public interface IVoyagePlanner {

	public static final int ROUNDING_EPSILON = 10;

	Pair<VoyagePlan, IAllocationAnnotation> makeNonShippedVoyagePlan(IResource resource, IPortTimesRecord portTimesRecord, boolean extendedEvaluation,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution);

	/**
	 * Returns a VoyagePlan produced by the optimiser from a cargo itinerary.
	 * 
	 * @param voyageOrPortOptionsSubsequence An alternating list of PortOptions and
	 *                                       VoyageOptions objects
	 * @param arrivalTimes
	 * @param optimiser
	 * @param startHeelVolumeInM3
	 * @return An optimised VoyagePlan
	 */
	void makeShippedVoyagePlans(IResource resource, ICharterCostCalculator charterCostCalculator, IPortTimesRecord portTimesRecord, long[] initialHeelVolumeRangeInM3, int lastCV, boolean lastPlan,
			boolean evaluateAll, boolean extendedEvaluation, Consumer<List<Pair<VoyagePlan, IPortTimesRecord>>> hook, ISequencesAttributesProvider sequencesAttributesProvider,
			@Nullable IAnnotatedSolution annotatedSolution);
}
