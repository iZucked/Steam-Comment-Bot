/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.caches;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public final class LHMCache<K, V> extends AbstractCache<K, V> {
	final private LinkedHashMap<K, Reference<V>> map;

	@SuppressWarnings("serial")
	public LHMCache(final @NonNull String name, final @NonNull IKeyEvaluator<K, V> evaluator, final int intendedSize) {
		super(name, evaluator);

		map = new LinkedHashMap<K, Reference<V>>(intendedSize + 1, 1, true) {

			@Override
			protected boolean removeEldestEntry(final Entry<K, Reference<V>> eldest) {
				return this.size() > intendedSize;
			}
		};
	}

	@Override
	public final V get(final K key) {
		query();
		final Reference<V> ref = map.get(key);
		@SuppressWarnings("null")
		V value = (V) null;
		if ((ref == null) || ((value = ref.get()) == null)) {
			final Pair<K, V> pair = evaluate(key);
			map.put(pair.getFirst(), new SoftReference<V>(pair.getSecond()));
			value = pair.getSecond();
		} else {
			hit();
		}
		return value;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public int size() {
		return map.size();
	}
}
