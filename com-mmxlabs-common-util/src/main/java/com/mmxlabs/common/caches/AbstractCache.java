package com.mmxlabs.common.caches;

import com.mmxlabs.common.Pair;

public abstract class AbstractCache<K, V> {
	private static final int SAMPLE = 100000;
	int queries = 0;
	int hits = 0;
	int memoryMisses = 0;
	int valueMisses = 0;
	int evictions = 0;
	private final String name;
	
	public interface IKeyEvaluator<K, V> {
		public Pair<K, V> evaluate(K key);
	}
	protected final IKeyEvaluator<K, V> evaluator;
	
	public AbstractCache(final String name, final IKeyEvaluator<K, V> evaluator) {
		this.evaluator = evaluator;
		this.name = name;
	}
	
	protected final Pair<K, V> evaluate(final K key) {
		return evaluator.evaluate(key);
	}
	
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Cache " + name + " info: " + size() + " elements, ");
		
		sb.append(String.format(
				"%.2f%% hits %.2f%% misses %.2f%% evictions, %.2f%% gc",
				100 * (hits / (double)queries),
				100 * (valueMisses/ (double)queries),
				100 * (evictions / (double)queries),
				100 * (memoryMisses / (double)queries)));
		return sb.toString();
	}
	
	protected void resetCounters() {
		queries = hits = memoryMisses = valueMisses = evictions = 0;
	}
	
	protected final void report() {
		if (hits == SAMPLE) {
			System.err.println("Free memory: "+Runtime.getRuntime().freeMemory()/1024 +"K");
			System.err.println(this);
			resetCounters();
		}
	}
	
	public abstract void clear();
	
	public abstract V get(final K key);
	public abstract int size();
}
