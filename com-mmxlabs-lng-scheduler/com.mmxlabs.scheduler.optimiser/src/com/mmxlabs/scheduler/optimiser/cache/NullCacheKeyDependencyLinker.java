package com.mmxlabs.scheduler.optimiser.cache;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public final class NullCacheKeyDependencyLinker implements ICacheKeyDependencyLinker {

	@Override
	public List<DepCacheKey> link(@NonNull CacheType phase, @NonNull IPortTimesRecord portTimesRecord) {
		return Collections.emptyList();
	}
}
