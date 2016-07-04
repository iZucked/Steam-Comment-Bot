/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.ga.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.common.caches.SimpleCache;
import com.mmxlabs.optimiser.ga.IIndividualEvaluator;
import com.mmxlabs.optimiser.ga.Individual;

/**
 * A memoizing evaluator; wraps another evaluator but adds a cache to make reevaluation efficient.
 * 
 * @param I
 *            Individual Type
 * 
 * @author Tom Hinton
 * 
 */
public final class CachingIndividualEvaluator<I extends Individual<I>> implements IIndividualEvaluator<I> {
	final IIndividualEvaluator<I> delegate;

	private final AbstractCache<@NonNull I, @NonNull Long> cache;

	public CachingIndividualEvaluator(final IIndividualEvaluator<I> delegate, final int size) {
		super();

		cache = new SimpleCache<@NonNull I, @NonNull Long>("IE", new IKeyEvaluator<@NonNull I, @NonNull Long>() {

			@Override
			public Pair<@NonNull I, @NonNull Long> evaluate(final I key) {
				// do clone key
				return new Pair<@NonNull I, @NonNull Long>(key.clone(), delegate.evaluate(key));
			}
		}, size, 3);

		this.delegate = delegate;
	}

	@Override
	public final long evaluate(final I individual) {
		// final long x = delegate.evaluate(individual);
		final long y = cache.get(individual.clone());
		// assert(x == y);
		return y;
	}

	@Override
	public void dispose() {
		delegate.dispose();
		cache.clear();
	}

	public final void clearCache() {
		cache.clear();
	}
}
