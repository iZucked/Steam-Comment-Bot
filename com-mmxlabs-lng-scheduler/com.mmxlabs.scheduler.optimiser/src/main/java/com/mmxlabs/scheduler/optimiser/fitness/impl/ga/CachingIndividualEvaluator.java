package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.AbstractCache;
import com.mmxlabs.common.caches.AbstractCache.IKeyEvaluator;
import com.mmxlabs.common.caches.SimpleCache;

/**
 * A memoizing evaluator; wraps another evaluator but adds a cache to make
 * reevaluation efficient.
 * 
 * @author hinton
 * 
 * @param <T>
 */
public final class CachingIndividualEvaluator<T> implements
		IIndividualEvaluator<T> {
	final IIndividualEvaluator<T> delegate;

	private final AbstractCache<Individual, Long> cache;
	public CachingIndividualEvaluator(final IIndividualEvaluator<T> delegate,
			final int size) {
		super();

		cache = new SimpleCache<Individual, Long>("IE", 
				new IKeyEvaluator<Individual, Long>() {

					@Override
					public Pair<Individual, Long> evaluate(Individual key) {
						return new Pair<Individual, Long>(
								(Individual)key.clone(), //do clone key
								delegate.evaluate(key));
					}
				}, size, 3);

		this.delegate = delegate;
	}

	@Override
	public final long evaluate(Individual individual) {
		// final long x = delegate.evaluate(individual);
		final long y = cache.get((Individual)individual.clone());
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
