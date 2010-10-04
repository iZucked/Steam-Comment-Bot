package com.mmxlabs.common.caches;

import com.mmxlabs.common.Pair;

public abstract class AbstractCache<K, V> {
	private static final int SAMPLE = 1000000;
	private final String name;
	
	int hits = 0;
	int queries = 0;
	
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
		return String.format("%s cache: size %d, hit rate %.2f",
				name, size() , 100*(hits/(double)queries));
	}
	
	protected final void hit() {
		hits++;
	}
	
	protected final void query() {
		queries++;
		if (queries == SAMPLE) {
			System.err.println(this);
			queries = hits = 0;
		}
	}
	
		
	public abstract void clear();
	
	public abstract V get(final K key);
	public abstract int size();
}
