package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public interface IVoyagePlanEvaluator {

	public List<ScheduledVoyagePlanResult> evaluateShipped(IResource resource, //
			IVesselAvailability vesselAvailability, //
			ICharterCostCalculator charterCostCalculator, //
			int vesselStartTime, //
			PreviousHeelRecord previousHeelRecord, //
			IPortTimesRecord portTimesRecord, //
			boolean lastPlan, //
			boolean returnAll, //
			boolean keepDetails, //
			@Nullable IAnnotatedSolution annotatedSolution);

	ScheduledVoyagePlanResult evaluateNonShipped(IResource resource, IVesselAvailability vesselAvailability, int vesselStartTime, IPortTimesRecord portTimesRecord, boolean keepDetails,
			@Nullable IAnnotatedSolution annotatedSolution);

}
