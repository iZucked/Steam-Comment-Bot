/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public interface IGeneratedCharterOutEvaluator {

	@Nullable
	List<@NonNull Pair<@NonNull VoyagePlan, @NonNull IPortTimesRecord>> processSchedule(int vesselStartTime, long[] startHeelVolumeRangeInM3, IVesselAvailability vesselAvailability, VoyagePlan vp,
			IPortTimesRecord portTimesRecord);
}
