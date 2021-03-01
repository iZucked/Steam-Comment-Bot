package com.mmxlabs.scheduler.optimiser.scheduleprocessor.maintenance;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public interface IMaintenanceEvaluator {
	@Nullable
	List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(long[] startHeelVolumeRangeInM3, IVesselAvailability vesselAvailability, VoyagePlan vp, IPortTimesRecord portTimesRecord,
			@Nullable IAnnotatedSolution annotatedSolution);
}
