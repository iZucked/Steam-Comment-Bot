/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;

public class SimpleCacheTest {

	@Test
	public void testSimpleCache() {
		final String name = "name";
		@SuppressWarnings("unchecked")
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final int binCount = 21;

		final SimpleCache<String, Object> cache = new SimpleCache<>(name, evaluator, binCount);

		Assertions.assertSame(name, cache.getName());

		Assertions.assertSame(evaluator, cache.evaluator);

		Assertions.assertEquals(binCount, cache.entries.length);
		Assertions.assertEquals(2, cache.evictionThreshold);
	}

	@Test
	public void testSimpleCache2() {
		final String name = "name";
		@SuppressWarnings("unchecked")
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final int binCount = 21;
		final int maxMisses = 30;

		final SimpleCache<String, Object> cache = new SimpleCache<>(name, evaluator, binCount, maxMisses);

		Assertions.assertSame(name, cache.getName());

		Assertions.assertSame(evaluator, cache.evaluator);

		Assertions.assertEquals(binCount, cache.entries.length);
		Assertions.assertEquals(maxMisses, cache.evictionThreshold);

	}
}
