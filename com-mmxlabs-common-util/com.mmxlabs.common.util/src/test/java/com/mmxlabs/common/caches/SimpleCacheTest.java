/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;

@RunWith(JMock.class)
public class SimpleCacheTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testSimpleCache() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = context.mock(IKeyEvaluator.class);
		final int binCount = 21;

		final SimpleCache<String, Object> cache = new SimpleCache<String, Object>(name, evaluator, binCount);

		Assert.assertSame(name, cache.getName());

		Assert.assertSame(evaluator, cache.evaluator);

		Assert.assertEquals(binCount, cache.entries.length);
		Assert.assertEquals(2, cache.evictionThreshold);

		context.assertIsSatisfied();
	}

	@Test
	public void testSimpleCache2() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = context.mock(IKeyEvaluator.class);
		final int binCount = 21;
		final int maxMisses = 30;

		final SimpleCache<String, Object> cache = new SimpleCache<String, Object>(name, evaluator, binCount, maxMisses);

		Assert.assertSame(name, cache.getName());

		Assert.assertSame(evaluator, cache.evaluator);

		Assert.assertEquals(binCount, cache.entries.length);
		Assert.assertEquals(maxMisses, cache.evictionThreshold);

		context.assertIsSatisfied();
	}

	@Test
	public void testSimpleCache3() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = context.mock(IKeyEvaluator.class);
		final int binCount = 21;
		final int maxMisses = 30;

		final SimpleCache<String, Object> cache = new SimpleCache<String, Object>(name, evaluator, binCount, maxMisses);

		context.assertIsSatisfied();
	}
}
