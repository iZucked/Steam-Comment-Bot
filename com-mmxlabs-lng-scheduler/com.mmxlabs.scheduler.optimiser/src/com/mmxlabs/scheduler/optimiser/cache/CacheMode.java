package com.mmxlabs.scheduler.optimiser.cache;

public enum CacheMode {
	Off, // Caching is disabled
	On, // Caching is enabled
	Verify // Caching is on and in verification mode (i.e. compare against non-cached version)
}
