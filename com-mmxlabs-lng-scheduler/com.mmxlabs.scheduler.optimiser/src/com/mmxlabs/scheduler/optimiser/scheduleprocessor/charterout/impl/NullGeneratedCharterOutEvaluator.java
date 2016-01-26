/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
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
	public List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final int vesselStartTime, final long startHeelVolumeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {
		return null;
	}

}
