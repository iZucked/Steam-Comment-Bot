/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public final class SimpleCache<K, V> extends AbstractCache<K, V> {
	final int evictionThreshold;

	class Entry {
		SoftReference<Pair<K, V>> reference = new SoftReference<Pair<K, V>>(null);
		int misses;

		public void clear() {
			reference = new SoftReference<Pair<K, V>>(null);
		}

		public final V getAndUpdate(final @NonNull IKeyEvaluator<K, V> evaluator, final @NonNull K key) {
			final Pair<K, V> entry = reference.get();
			queries++;
			if ((entry != null) && key.equals(entry.getFirst())) {
				misses = 0;
				// hits++;
				return entry.getSecond();
			} else {
				final Pair<K, V> value = evaluator.evaluate(key);

				// if (entry == null) memoryMisses++;
				// else valueMisses++;

				if ((entry == null) || (misses > evictionThreshold)) {
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

	Entry[] entries;

	public SimpleCache(final @NonNull String name, final @NonNull IKeyEvaluator<K, V> evaluator, final int size) {
		this(name, evaluator, size, 2);
	}

	@SuppressWarnings("unchecked")
	public SimpleCache(final @NonNull String name, final @NonNull IKeyEvaluator<K, V> evaluator, final int binCount, final int maxMisses) {
		super(name, evaluator);
		this.evictionThreshold = maxMisses;
		this.entries = (Entry[]) Array.newInstance(Entry.class, binCount);
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new Entry();
		}
	}

	@Override
	public final V get(final K key) {
		final int hash = key.hashCode();
		final int hashPosition = hash == Integer.MIN_VALUE ? 0 : Math.abs(hash) % entries.length;
		final Entry e = entries[hashPosition];

		return e.getAndUpdate(evaluator, key);
	}

	@Override
	public void clear() {
		for (final Entry o : entries) {
			o.clear();
		}
	}

	@Override
	public int size() {
		return entries.length; // wrong
	}
}
