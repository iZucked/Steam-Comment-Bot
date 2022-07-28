/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public interface IVoyagePlanEvaluator {

	public ImmutableList<ScheduledVoyagePlanResult> evaluateShipped(IResource resource, //
			IVesselCharter vesselCharter, //
			ICharterCostCalculator charterCostCalculator, //
			int vesselStartTime, //
			@Nullable IPort firstLoadPort, PreviousHeelRecord previousHeelRecord, //
			IPortTimesRecord portTimesRecord, //
			boolean lastPlan, //
			boolean returnAll, //
			boolean keepDetails, //
			ISequencesAttributesProvider sequencesAttributesProvider, //
			@Nullable IAnnotatedSolution annotatedSolution);

	public ImmutableList<ScheduledVoyagePlanResult> evaluateRoundTrip(IResource resource, //
			IVesselCharter vesselCharter, //
			ICharterCostCalculator charterCostCalculator, //
			IPortTimesRecord portTimesRecord, //
			boolean returnAll, //
			boolean keepDetails, //
			ISequencesAttributesProvider sequencesAttributesProvider, //
			@Nullable IAnnotatedSolution annotatedSolution);

	ScheduledVoyagePlanResult evaluateNonShipped(IResource resource, IVesselCharter vesselCharter, IPortTimesRecord portTimesRecord, boolean keepDetails,
			ISequencesAttributesProvider sequencesAttributesProvider,
			@Nullable IAnnotatedSolution annotatedSolution);
	
	public Consumer<List<@NonNull Pair<VoyagePlan, IPortTimesRecord>>> evaluateVoyagePlan(final IResource resource, final IVesselCharter vesselCharter, final int vesselStartTime,
			final @Nullable IPort firstLoadPort, final PreviousHeelRecord previousHeelRecord, final IPortTimesRecord initialPortTimesRecord, final boolean lastPlan, final boolean keepDetails,
			final @Nullable IAnnotatedSolution annotatedSolution, final List<ScheduledVoyagePlanResult> results);

}
