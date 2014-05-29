/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IGeneratedCharterOutEvaluator {

//	void processSchedule(ScheduledSequences scheduledSequences);
	Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, final IVessel vessel ,final VoyagePlan vp, final List<Integer> arrivalTimes );
}
