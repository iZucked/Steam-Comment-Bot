/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Empty implementation of {@link IBreakEvenEvaluator}
 * 
 * @author Simon Goodall
 * 
 */
public class NullBreakEvenEvaluator implements IBreakEvenEvaluator {

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final int vesselStartTime, final IVessel vessel, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {
		// Does nothing
		return null;
	}

}
