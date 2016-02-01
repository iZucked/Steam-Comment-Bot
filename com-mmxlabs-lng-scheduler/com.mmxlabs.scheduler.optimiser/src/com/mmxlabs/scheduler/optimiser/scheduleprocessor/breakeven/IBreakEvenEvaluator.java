/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";

	Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, IVesselAvailability vesselAvailability, VoyagePlan vp, IPortTimesRecord portTimesRecord);

}
