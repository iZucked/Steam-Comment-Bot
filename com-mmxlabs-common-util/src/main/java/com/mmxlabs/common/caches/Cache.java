package com.mmxlabs.common.caches;

import java.lang.ref.SoftReference;

import com.mmxlabs.common.Pair;

public class Cache<K, V> {
	public interface IKeyEvaluator<K, V> {
		public V evaluate(K key);
	}

	private static final int SAMPLE = 100000;
	
	int queries = 0;
	int hits = 0;
	int memoryMisses = 0;
	int valueMisses = 0;
	int evictions = 0;
	
	final int evictionThreshold;
	
	class Entry {
		SoftReference<Pair<K, V>> reference = new SoftReference<Pair<K, V>>(null);
		int misses;
		
		public void clear() {
			reference = new SoftReference<Pair<K, V>>(null);
		}
		
		public final V getAndUpdate(final IKeyEvaluator<K, V> evaluator, final K key) {
			final Pair<K, V> entry = reference.get();
			queries++;
			if (entry != null && key.equals(entry.getFirst())) {
				misses = 0;
				hits++;
				return entry.getSecond();
			} else {
				final V value = evaluator.evaluate(key);
				
				if (entry == null) memoryMisses++;
				else valueMisses++;
				
				if (entry == null || misses > evictionThreshold) {
					this.reference = new SoftReference<Pair<K, V>>(new Pair<K, V>(key, value));
					evictions++;
					misses = 0;
				} else {
					misses++;
				}
				return value;
			}
		}
	}
	
	Object[] entries;
	private IKeyEvaluator<K, V> evaluator;
	private final String name;
	
	public Cache(final String name, final IKeyEvaluator<K, V> evaluator, final int binCount, final int maxMisses) {
		this.evictionThreshold = maxMisses;
		this.name = name;
		this.entries = new Object[binCount];
		for (int i = 0; i<entries.length; i++) {
			entries[i] = new Entry();
		}
		this.evaluator = evaluator;
	}
	
	public V get(final K key) {
		final int hash = key.hashCode();
		final int hashPosition = Math.abs(hash) % entries.length;
		final Entry e = (Entry) entries[hashPosition];
		
		if (hits == SAMPLE) {
			System.err.println(this);
			resetCounters();
		}
		
		return e.getAndUpdate(evaluator, key);
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Cache " + name + " info:");
		
		sb.append(String.format(
				"%.2f%% hits %.2f%% misses %.2f%% evictions, %.2f%% gc",
				100 * (hits / (double)queries),
				100 * (valueMisses/ (double)queries),
				100 * (evictions / (double)queries),
				100 * (memoryMisses / (double)queries)));
		return sb.toString();
	}
	
	public void resetCounters() {
		queries = hits = memoryMisses = valueMisses = evictions = 0;
	}
	
	public void clear() {
		for (Object o : entries) {
			((Entry)o).clear();
		}
//		resetCounters();40*
	}
}
