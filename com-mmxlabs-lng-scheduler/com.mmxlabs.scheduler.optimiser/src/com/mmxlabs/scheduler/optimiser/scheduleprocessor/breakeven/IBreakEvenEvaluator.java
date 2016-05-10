/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";

	@Nullable
	Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> processSchedule(int vesselStartTime, @NonNull IVesselAvailability vesselAvailability, @NonNull VoyagePlan vp,
			@NonNull IPortTimesRecord portTimesRecord);

}
