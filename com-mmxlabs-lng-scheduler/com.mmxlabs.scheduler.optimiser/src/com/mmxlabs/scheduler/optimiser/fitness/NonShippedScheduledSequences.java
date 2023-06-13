package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public class NonShippedScheduledSequences {

	private final Map<IResource, @Nullable Pair<MinTravelTimeData, List<IPortTimesRecord>>> schedule;

	public NonShippedScheduledSequences(final Map<IResource, @Nullable Pair<MinTravelTimeData, List<IPortTimesRecord>>> schedule) {
		this.schedule = schedule;
	}

	public Map<IResource, @Nullable Pair<MinTravelTimeData, List<IPortTimesRecord>>> getSchedule() {
		return schedule;
	}
}
