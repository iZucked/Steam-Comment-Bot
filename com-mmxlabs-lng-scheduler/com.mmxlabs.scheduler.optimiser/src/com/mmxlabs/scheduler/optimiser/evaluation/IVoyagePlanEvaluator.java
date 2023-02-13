/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public interface IVoyagePlanEvaluator {

	ImmutableList<ScheduledVoyagePlanResult> evaluateShipped(IResource resource, //
			IVesselCharter vesselCharter, //
			ICharterCostCalculator charterCostCalculator, //
			VesselStartState vesselStartState, //
			PreviousHeelRecord previousHeelRecord, //
			IPortTimesRecord portTimesRecord, //
			boolean lastPlan, //
			boolean returnAll, //
			boolean keepDetails, //
			ISequencesAttributesProvider sequencesAttributesProvider, //
			@Nullable IAnnotatedSolution annotatedSolution);

	ImmutableList<ScheduledVoyagePlanResult> evaluateRoundTrip(IResource resource, //
			IVesselCharter vesselCharter, //
			ICharterCostCalculator charterCostCalculator, //
			IPortTimesRecord portTimesRecord, //
			boolean returnAll, //
			boolean keepDetails, //
			ISequencesAttributesProvider sequencesAttributesProvider, //
			@Nullable IAnnotatedSolution annotatedSolution);

	ScheduledVoyagePlanResult evaluateNonShipped(IResource resource, IVesselCharter vesselCharter, IPortTimesRecord portTimesRecord, boolean keepDetails,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution);

	Consumer<List<@NonNull Pair<VoyagePlan, IPortTimesRecord>>> evaluateVoyagePlan(IResource resource, IVesselCharter vesselCharter, VesselStartState vesselStartState,
			PreviousHeelRecord previousHeelRecord, IPortTimesRecord initialPortTimesRecord, boolean lastPlan, boolean keepDetails, @Nullable IAnnotatedSolution annotatedSolution,
			List<ScheduledVoyagePlanResult> results);

}
