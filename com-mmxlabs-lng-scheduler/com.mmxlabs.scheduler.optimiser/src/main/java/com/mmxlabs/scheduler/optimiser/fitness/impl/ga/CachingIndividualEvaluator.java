package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Arrays;
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

		cache = new MapMaker().concurrencyLevel(1).weakValues()
				.initialCapacity(size).expiration(1, TimeUnit.MINUTES)
				.makeComputingMap(new Function<Individual, Long>() {
					@Override
					public Long apply(Individual arg) {
						return delegate.evaluate(arg);
					}
				});

		this.delegate = delegate;
	}

	@Override
	public long evaluate(Individual individual) {
		return cache.get(individual.clone());
	}

	@Override
	public void dispose() {
		delegate.dispose();
		cache.clear();
	}

	public void clearCache() {
		cache.clear();
	}
}
