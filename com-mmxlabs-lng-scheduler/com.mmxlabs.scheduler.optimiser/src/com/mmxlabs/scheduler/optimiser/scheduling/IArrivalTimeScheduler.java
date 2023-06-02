/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public interface IArrivalTimeScheduler {

	Map<IResource, List<@NonNull IPortTimesRecord>> schedule(@NonNull ISequences fullSequences);

	@NonNull
	Map<@NonNull IResource, Pair<@NonNull MinTravelTimeData, @NonNull List<@NonNull IPortTimesRecord>>> scheduleNonShipped(@NonNull ISequences sequences);
}