/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;

public class SimpleCacheTest {

	@Test
	public void testSimpleCache() {
		final String name = "name";
		@SuppressWarnings("unchecked")
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final int binCount = 21;

		final SimpleCache<String, Object> cache = new SimpleCache<String, Object>(name, evaluator, binCount);

		Assert.assertSame(name, cache.getName());

		Assert.assertSame(evaluator, cache.evaluator);

		Assert.assertEquals(binCount, cache.entries.length);
		Assert.assertEquals(2, cache.evictionThreshold);
	}

	@Test
	public void testSimpleCache2() {
		final String name = "name";
		@SuppressWarnings("unchecked")
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final int binCount = 21;
		final int maxMisses = 30;

		final SimpleCache<String, Object> cache = new SimpleCache<String, Object>(name, evaluator, binCount, maxMisses);

		Assert.assertSame(name, cache.getName());

		Assert.assertSame(evaluator, cache.evaluator);

		Assert.assertEquals(binCount, cache.entries.length);
		Assert.assertEquals(maxMisses, cache.evictionThreshold);

	}
}
