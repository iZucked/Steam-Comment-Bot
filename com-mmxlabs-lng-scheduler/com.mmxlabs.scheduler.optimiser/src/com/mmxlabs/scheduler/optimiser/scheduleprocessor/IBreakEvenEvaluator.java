/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";

	Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, IVessel vessel, VoyagePlan vp, IPortTimesRecord portTimesRecord);

}
