/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public interface IVoyagePlanEvaluator {

	public ImmutableList<ScheduledVoyagePlanResult> evaluateShipped(IResource resource, //
			IVesselAvailability vesselAvailability, //
			ICharterCostCalculator charterCostCalculator, //
			int vesselStartTime, //
			@Nullable IPort firstLoadPort,
			PreviousHeelRecord previousHeelRecord, //
			IPortTimesRecord portTimesRecord, //
			boolean lastPlan, //
			boolean returnAll, //
			boolean keepDetails, //
			@Nullable IAnnotatedSolution annotatedSolution);
	
	public ImmutableList<ScheduledVoyagePlanResult> evaluateRoundTrip(IResource resource, //
			IVesselAvailability vesselAvailability, //
			ICharterCostCalculator charterCostCalculator, //
			IPortTimesRecord portTimesRecord, //
			boolean returnAll, //
			boolean keepDetails, //
			@Nullable IAnnotatedSolution annotatedSolution);

	ScheduledVoyagePlanResult evaluateNonShipped(IResource resource, IVesselAvailability vesselAvailability, IPortTimesRecord portTimesRecord, boolean keepDetails,
			@Nullable IAnnotatedSolution annotatedSolution);

}
