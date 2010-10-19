/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.common.caches;

import java.lang.ref.SoftReference;

import com.mmxlabs.common.Pair;

public final class SimpleCache<K, V> extends AbstractCache<K, V> {
	final int evictionThreshold;

	class Entry {
		SoftReference<Pair<K, V>> reference = new SoftReference<Pair<K, V>>(
				null);
		int misses;

		public void clear() {
			reference = new SoftReference<Pair<K, V>>(null);
		}

		public final V getAndUpdate(final IKeyEvaluator<K, V> evaluator,
				final K key) {
			final Pair<K, V> entry = reference.get();
			queries++;
			if (entry != null && key.equals(entry.getFirst())) {
				misses = 0;
				// hits++;
				return entry.getSecond();
			} else {
				final Pair<K, V> value = evaluator.evaluate(key);

				// if (entry == null) memoryMisses++;
				// else valueMisses++;

				if (entry == null || misses > evictionThreshold) {
					this.reference = new SoftReference<Pair<K, V>>(value);
					// evictions++;
					misses = 0;
				} else {
					misses++;
				}
				return value.getSecond();
			}
		}
	}

	Object[] entries;

	public SimpleCache(final String name, final IKeyEvaluator<K, V> evaluator,
			final int size) {
		this(name, evaluator, size, 2);
	}

	public SimpleCache(final String name, final IKeyEvaluator<K, V> evaluator,
			final int binCount, final int maxMisses) {
		super(name, evaluator);
		this.evictionThreshold = maxMisses;
		this.entries = new Object[binCount];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new Entry();
		}
	}

	public final V get(final K key) {
		final int hash = key.hashCode();
		final int hashPosition = Math.abs(hash) % entries.length;
		final Entry e = (Entry) entries[hashPosition];

		return e.getAndUpdate(evaluator, key);
	}

	public void clear() {
		for (Object o : entries) {
			((Entry) o).clear();
		}
	}

	@Override
	public int size() {
		return entries.length; // wrong
	}
}
