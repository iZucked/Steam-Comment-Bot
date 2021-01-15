/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

public enum CacheMode {
	Off, // Caching is disabled
	On, // Caching is enabled
	Verify // Caching is on and in verification mode (i.e. compare against non-cached version)
}
