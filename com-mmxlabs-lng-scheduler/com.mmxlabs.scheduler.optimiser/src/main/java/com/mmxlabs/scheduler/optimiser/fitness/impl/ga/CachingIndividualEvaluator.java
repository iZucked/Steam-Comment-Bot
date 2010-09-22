package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

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

	private final ConcurrentMap<Individual, Long> cache;

	public CachingIndividualEvaluator(final IIndividualEvaluator<T> delegate,
			final int size) {
		super();

		cache = new MapMaker().concurrencyLevel(1)
				.softKeys()
				.initialCapacity(size)
				.makeComputingMap(new Function<Individual, Long>() {
					@Override
					public Long apply(Individual arg) {
						return delegate.evaluate(arg);
					}
				});

		this.delegate = delegate;
	}

	@Override
	public final long evaluate(Individual individual) {
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
