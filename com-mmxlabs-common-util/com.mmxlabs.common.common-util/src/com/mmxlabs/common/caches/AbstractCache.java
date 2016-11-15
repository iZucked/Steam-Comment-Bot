/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public abstract class AbstractCache<K, V> {

	private static final int SAMPLE = 100_000;
	private final String name;

	int hits = 0;
	int queries = 0;

	@FunctionalInterface
	public interface IKeyEvaluator<K, V> {
		public Pair<K, V> evaluate(K key);
	}

	protected final @NonNull IKeyEvaluator<K, V> evaluator;

	public AbstractCache(final @NonNull String name, final @NonNull IKeyEvaluator<K, V> evaluator) {
		this.evaluator = evaluator;
		this.name = name;
	}

	protected final Pair<K, V> evaluate(final K key) {
		return evaluator.evaluate(key);
	}

	@Override
	public String toString() {
		return String.format("%s cache: size %d, hit rate %.2f%%", name, size(), 100 * (hits / (double) queries));
	}

	protected final void hit() {
		hits++;
	}

	protected final void query() {
		queries++;
		if (queries == SAMPLE) {
			// log.debug(this.toString());
			queries = hits = 0;
		}
	}

	public abstract void clear();

	public abstract V get(final @NonNull K key);

	public abstract int size();

	public final String getName() {
		return name;
	}
}
