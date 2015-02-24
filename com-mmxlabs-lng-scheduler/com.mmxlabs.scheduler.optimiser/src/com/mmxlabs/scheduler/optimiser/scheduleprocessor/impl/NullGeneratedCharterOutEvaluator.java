/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Empty implementation of {@link IGeneratedCharterOutEvaluator}
 * 
 * @author Simon Goodall
 * 
 */
public class NullGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final int vesselStartTime, final IVesselAvailability vesselAvailability, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {
		return null;
	}

}
