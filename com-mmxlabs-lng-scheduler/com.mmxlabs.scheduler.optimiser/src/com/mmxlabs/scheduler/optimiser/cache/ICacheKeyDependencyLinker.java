package com.mmxlabs.scheduler.optimiser.cache;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * The {@link ICacheKeyDependencyLinker} is a way for external code to create dependencies between cargoes for purposes of correct caching.
 * 
 * @author Simon Goodall
 *
 */
public interface ICacheKeyDependencyLinker {
	/**
	 * The different types of cache
	 *
	 */
	enum CacheType {
		/* VoyagePlan, */Volume, PNL
	}

	@NonNull
	List<@NonNull DepCacheKey> link(@NonNull CacheType cacheType, @NonNull IPortTimesRecord portTimesRecord);
}
