package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

public class CachingTimeWindowSchedulingCanalDistanceProvider implements ITimeWindowSchedulingCanalDistanceProvider {
	
	private final class CacheKey {
		IPort load; IPort discharge; IVesselClass vesselClass; int ladenStartTime;
		
		public CacheKey(IPort load, IPort discharge, IVesselClass vesselClass, int ladenStartTime) {
			this.load = load;
			this.discharge = discharge;
			this.vesselClass = vesselClass;
			this.ladenStartTime = ladenStartTime;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if (obj instanceof CacheKey) {
				CacheKey other = (CacheKey) obj;
				return this.load == other.load && this.discharge == other.discharge && this.vesselClass == other.vesselClass
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
			result = (prime * result) + (vesselClass != null ? vesselClass.hashCode() : 0);
			result = (prime * result) + ladenStartTime;
			return result;
		}
	}

	private TimeWindowSchedulingCanalDistanceProvider delegate;
	private Map<CacheKey, long[][]> cache = new HashMap<>();
	
	public CachingTimeWindowSchedulingCanalDistanceProvider(TimeWindowSchedulingCanalDistanceProvider canalDistanceProvider) {
		delegate = canalDistanceProvider;
	}
	
	@Override
	public long[][] getMinimumLadenTravelTimes(IPort load, IPort discharge, IVesselClass vesselClass, int ladenStartTime) {
		CacheKey key = new CacheKey(load, discharge, vesselClass, ladenStartTime);
		long[][] values = cache.get(key);
		if (values == null) {
			values = delegate.getMinimumLadenTravelTimes(load, discharge, vesselClass, ladenStartTime);
			assert values != null;
			cache.put(key, values);
		}
		return values;
	}

	@Override
	@NonNull
	public List<Integer> getFeasibleRoutes(long[][] sortedCanalTimes, int minTime, int maxTime) {
		return delegate.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
	}

	@Override
	@NonNull
	public long[] getBestCanalDetails(long[][] times, int maxTime) {
		return delegate.getBestCanalDetails(times, maxTime);
	}
}