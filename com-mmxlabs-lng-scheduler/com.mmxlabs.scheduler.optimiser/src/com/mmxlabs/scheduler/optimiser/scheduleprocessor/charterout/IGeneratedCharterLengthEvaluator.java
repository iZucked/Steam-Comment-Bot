/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public interface IGeneratedCharterLengthEvaluator {

	@Nullable
	List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(int vesselStartTime, long[] startHeelVolumeRangeInM3, IVesselAvailability vesselAvailability, VoyagePlan vp,
			IPortTimesRecord portTimesRecord, @Nullable IAnnotatedSolution annotatedSolution);
}
