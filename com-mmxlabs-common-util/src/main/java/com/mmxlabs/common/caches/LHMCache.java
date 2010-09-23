package com.mmxlabs.common.caches;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LHMCache<K, V> extends AbstractCache<K, V> {
	

	final private LinkedHashMap<K, SoftReference<V>> map;

	public LHMCache(final String name, final IKeyEvaluator<K, V> evaluator, final int intendedSize) {
		super(name, evaluator);
		  
		
		map = new LinkedHashMap<K, SoftReference<V>> ((int)(intendedSize) + 20, 1f, true) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					Entry<K, SoftReference<V>> eldest) {
				return size() > intendedSize;
			}
		};
	}

	public V get(final K key) {
		final SoftReference<V> ref = map.get(key);
		V value = null;
		queries++;
		if (ref == null || (value = ref.get()) == null) {
			value = evaluate(key);
			map.put(key, new SoftReference<V>(value));
			if (ref == null) valueMisses++;
			else memoryMisses++;
		} else {
			hits++;
		}
		report();
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
