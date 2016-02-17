/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class CachingTimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {
	
	private final class CacheKey {
		IPort load; IPort discharge; IVessel vessel; int ladenStartTime;
		
		public CacheKey(IPort load, IPort discharge, IVessel vessel, int ladenStartTime) {
			this.load = load;
			this.discharge = discharge;
			this.vessel = vessel;
			this.ladenStartTime = ladenStartTime;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof CacheKey) {
				CacheKey other = (CacheKey) obj;
				return this.load == other.load && this.discharge == other.discharge && this.vessel == other.vessel
						&& this.ladenStartTime == other.ladenStartTime;
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
			return result;
		}
	}

	private TimeWindowSchedulingCanalDistanceProvider delegate;
	private Map<CacheKey, LadenRouteData[]> cache = new HashMap<>();
	
	public CachingTimeWindowSchedulingCanalDistanceProvider(TimeWindowSchedulingCanalDistanceProvider canalDistanceProvider) {
		delegate = canalDistanceProvider;
	}
	
	@Override
	public LadenRouteData[] getMinimumLadenTravelTimes(IPort load, IPort discharge, IVessel vessel, int ladenStartTime) {
		CacheKey key = new CacheKey(load, discharge, vessel, ladenStartTime);
		LadenRouteData[] values = cache.get(key);
		if (values == null) {
			values = delegate.getMinimumLadenTravelTimes(load, discharge, vessel, ladenStartTime);
			assert values != null;
			cache.put(key, values);
		}
		return values;
	}

	@Override
	@NonNull
	public List<Integer> getFeasibleRoutes(LadenRouteData[] sortedCanalTimes, int minTime, int maxTime) {
		return delegate.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
	}

	@Override
	@NonNull
	public LadenRouteData getBestCanalDetails(LadenRouteData[] sortedCanalTimes, int maxTime) {
		return delegate.getBestCanalDetails(sortedCanalTimes, maxTime);
	}
}