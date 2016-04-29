/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * The {@link IProfitAndLossCacheKeyDependencyLinker} is a way for external code to create dependencies between cargoes for purposes of correct caching.
 * 
 * @author Simon Goodall
 *
 */
public interface IProfitAndLossCacheKeyDependencyLinker {

	@NonNull
	List<@NonNull DepCacheKey> link(@NonNull IPortTimesRecord portTimesRecord, @NonNull VolumeAllocatedSequences volumeAllocatedSequences);
}
