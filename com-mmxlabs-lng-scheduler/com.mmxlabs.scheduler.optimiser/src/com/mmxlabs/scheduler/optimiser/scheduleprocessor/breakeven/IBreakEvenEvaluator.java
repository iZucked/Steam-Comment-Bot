/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";

	@Nullable
	Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, IVesselAvailability vesselAvailability, VoyagePlan vp, IPortTimesRecord portTimesRecord,
			@Nullable IAnnotatedSolution annotatedSolution);

}
