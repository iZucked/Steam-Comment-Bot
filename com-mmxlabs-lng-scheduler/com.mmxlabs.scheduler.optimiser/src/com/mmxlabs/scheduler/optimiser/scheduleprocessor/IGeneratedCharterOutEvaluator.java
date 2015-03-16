/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IGeneratedCharterOutEvaluator {

	List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(int vesselStartTime, long startHeelVolumeInM3, IVesselAvailability vesselAvailability, VoyagePlan vp, IPortTimesRecord portTimesRecord);
}
