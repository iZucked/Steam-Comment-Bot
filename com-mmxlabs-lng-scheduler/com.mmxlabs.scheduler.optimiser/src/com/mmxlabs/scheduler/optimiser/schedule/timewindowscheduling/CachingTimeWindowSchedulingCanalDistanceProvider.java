/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class CachingTimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {

	private final class CacheKey {
		IPort load;
		IPort discharge;
		IVessel vessel;
		int ladenStartTime;
		AvailableRouteChoices availableRouteChoice;

		public CacheKey(final IPort load, final IPort discharge, final IVessel vessel, final int ladenStartTime, AvailableRouteChoices availableRouteChoice) {
			this.load = load;
			this.discharge = discharge;
			this.vessel = vessel;
			this.ladenStartTime = ladenStartTime;
			this.availableRouteChoice = availableRouteChoice;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				return this.load == other.load //
						&& this.discharge == other.discharge//
						&& this.vessel == other.vessel //
						&& this.ladenStartTime == other.ladenStartTime //
						&& this.availableRouteChoice == other.availableRouteChoice;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + (load != null ? load.hashCode() : 0);
			result = (prime * result) + (discharge != null ? discharge.hashCode() : 0);
			result = (prime * result) + (vessel != null ? vessel.hashCode() : 0);
			result = (prime * result) + ladenStartTime;
			if (availableRouteChoice != null) {
				result = (prime * result) + availableRouteChoice.ordinal();
			}
			return result;
		}
	}

	private final TimeWindowSchedulingCanalDistanceProvider delegate;

	private Map<CacheKey, @NonNull LadenRouteData[]> cache = new HashMap<>();

	public CachingTimeWindowSchedulingCanalDistanceProvider(final TimeWindowSchedulingCanalDistanceProvider canalDistanceProvider) {
		delegate = canalDistanceProvider;
	}

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumLadenTravelTimes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int ladenStartTime,
			AvailableRouteChoices availableRouteChoice) {
		final CacheKey key = new CacheKey(load, discharge, vessel, ladenStartTime, availableRouteChoice);
		@NonNull
		LadenRouteData @Nullable [] values = cache.get(key);
		if (values == null) {
			values = delegate.getMinimumLadenTravelTimes(load, discharge, vessel, ladenStartTime, availableRouteChoice);
			assert values != null;
			cache.put(key, values);
		}
		return values;
	}

	@Override
	@NonNull
	public List<@NonNull Integer> getFeasibleRoutes(@NonNull LadenRouteData @NonNull [] sortedCanalTimes, int minTime, int maxTime) {
		return delegate.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
	}

	@Override
	public @NonNull LadenRouteData getBestCanalDetails(@NonNull LadenRouteData @NonNull [] sortedCanalTimes, int maxTime) {
		return delegate.getBestCanalDetails(sortedCanalTimes, maxTime);
	}

	@Override
	public @NonNull List<Integer> getTimeDataForDifferentSpeedsAndRoutes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int cv, int startTime, boolean isLaden,
			AvailableRouteChoices availableRouteChoice) {
		return delegate.getTimeDataForDifferentSpeedsAndRoutes(load, discharge, vessel, cv, startTime, isLaden, availableRouteChoice);
	}

	@Override
	public @NonNull LadenRouteData @NonNull [] getMinimumBallastTravelTimes(@NonNull IPort load, @NonNull IPort discharge, @NonNull IVessel vessel, int ladenStartTime,
			AvailableRouteChoices availableRouteChoice) {
		return delegate.getMinimumBallastTravelTimes(load, discharge, vessel, ladenStartTime, availableRouteChoice);
	}
}