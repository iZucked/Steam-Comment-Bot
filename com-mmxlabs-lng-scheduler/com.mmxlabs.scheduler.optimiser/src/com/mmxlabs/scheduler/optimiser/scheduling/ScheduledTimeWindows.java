package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class ScheduledTimeWindows {
	public ScheduledTimeWindows(MinTravelTimeData travelTimeData, Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows) {
		this.travelTimeData = travelTimeData;
		trimmedTimeWindows = trimmedWindows;
	}

	private final MinTravelTimeData travelTimeData;

	public MinTravelTimeData getTravelTimeData() {
		return travelTimeData;
	}

	private final Map<IResource, List<IPortTimeWindowsRecord>> trimmedTimeWindows;

	public Map<IResource, List<IPortTimeWindowsRecord>> getTrimmedTimeWindowsMap() {
		return trimmedTimeWindows;
	}
}
