/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;

@SuppressWarnings("unchecked")
public class AbstractCacheTest {

	@Test
	public void testAbstractCache() {

		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final TestAbstractCache cache = new TestAbstractCache(name, evaluator);

		Assertions.assertSame(name, cache.getName());
		Assertions.assertSame(evaluator, cache.evaluator);
	}

	@Test
	public void testEvaluate() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final TestAbstractCache cache = new TestAbstractCache(name, evaluator);

		Assertions.assertSame(name, cache.getName());
		Assertions.assertSame(evaluator, cache.evaluator);

		final String key = "key";

		cache.evaluate(key);
		Mockito.verify(evaluator).evaluate(key);

	}

	@Test
	public void testToString() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final TestAbstractCache cache = new TestAbstractCache(name, evaluator);
		Assertions.assertNotNull(cache.toString());
	}

	@Test
	public void testHit() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final TestAbstractCache cache = new TestAbstractCache(name, evaluator);
		Assertions.assertEquals(0, cache.hits);
		cache.hit();
		Assertions.assertEquals(1, cache.hits);
	}

	@Test
	public void testQuery() {
		final String name = "name";
		final IKeyEvaluator<String, Object> evaluator = Mockito.mock(IKeyEvaluator.class);
		final TestAbstractCache cache = new TestAbstractCache(name, evaluator);

		cache.hit();
		Assertions.assertEquals(1, cache.hits);
		Assertions.assertEquals(0, cache.queries);
		// 100000 is the SAMPLE constant in AbstractCache
		for (int i = 0; i < 100000; ++i) {
			Assertions.assertEquals(i, cache.queries);
			Assertions.assertEquals(1, cache.hits);

			cache.query();
		}
		Assertions.assertEquals(0, cache.queries);
		Assertions.assertEquals(0, cache.hits);

	}

	private class TestAbstractCache extends AbstractCache<String, Object> {

		public TestAbstractCache(final @NonNull String name, final com.mmxlabs.common.caches.AbstractCache.@NonNull IKeyEvaluator<String, Object> evaluator) {
			super(name, evaluator);
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException("This method is not part of the test");
		}

		@Override
		public Object get(final String key) {
			throw new UnsupportedOperationException("This method is not part of the test");
		}

		@Override
		public int size() {
			// This method is used in toString();
			return 0;
		}
	}
}
