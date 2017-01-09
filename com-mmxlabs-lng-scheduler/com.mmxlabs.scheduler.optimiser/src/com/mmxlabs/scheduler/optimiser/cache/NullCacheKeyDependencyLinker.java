/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class NullCacheKeyDependencyLinker implements IProfitAndLossCacheKeyDependencyLinker {

	@Override
	public List<DepCacheKey> link(@NonNull IPortTimesRecord portTimesRecord, @NonNull VolumeAllocatedSequences volumeAllocatedSequences) {
		return Collections.emptyList();
	}
}
