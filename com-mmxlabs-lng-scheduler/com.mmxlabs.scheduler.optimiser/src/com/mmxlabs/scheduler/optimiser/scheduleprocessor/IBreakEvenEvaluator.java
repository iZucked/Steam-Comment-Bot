/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";
	
//	void processSchedule(ScheduledSequences scheduledSequences);

	Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, IVessel vessel, VoyagePlan vp, List<Integer> arrivalTimes);

}
