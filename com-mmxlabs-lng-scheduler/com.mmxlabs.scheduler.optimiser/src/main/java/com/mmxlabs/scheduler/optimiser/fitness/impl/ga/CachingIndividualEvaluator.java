package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

/**
 * A memoizing evaluator; wraps another evaluator but adds a cache to make reevaluation
 * efficient.
 * 
 * @author hinton
 *
 * @param <T>
 */
public final class CachingIndividualEvaluator<T> implements IIndividualEvaluator<T> {
	final IIndividualEvaluator<T> delegate;
	

	
	private final ConcurrentMap<Individual, Long> cache = 
		new MapMaker()
			.concurrencyLevel(1)
//			.softKeys()
			.weakValues()
			.expiration(1, TimeUnit.MINUTES)
			.makeComputingMap(new Function<Individual, Long>() {
				@Override
				public Long apply(Individual arg) {
					return delegate.evaluate(arg);
				}
			});
		
	
	/*final int tableSize;
	byte [][] occupants;
	long []   values;
	int  []   hits;
	int  []   misses;
	
	int totalHits;
	int totalMisses;*/
	
	public CachingIndividualEvaluator(IIndividualEvaluator<T> delegate, int size) {
		super();
		
		
		this.delegate = delegate;
		/*this.tableSize = size;
		occupants = new byte[size][];
		values = new long[size];
		misses = new int[size];
		hits = new int[size];
		totalHits = totalMisses = 0;
		*/
	}

	@Override
	public long evaluate(Individual individual) {
		return cache.get(individual.clone());
		
		
//		final int hash = individual.hashBytes() % tableSize;
//		if (Arrays.equals(individual.bytes, occupants[hash])) {
//			hits[hash]++;
//			totalHits++;
//			misses[hash] = 0;
//			return values[hash];
//		} else {
//			totalMisses++;
//			final long value = delegate.evaluate(individual);
//			
//			if (misses[hash] > 10) {
//				//replace
//				misses[hash] = 0;
//				hits[hash] = 1;
//				values[hash] = value;
//				occupants[hash] = individual.bytes.clone(); //is this a terrible idea?
//			} else {
//				misses[hash]++;
//			}
//			
//			return value;
//		}
	}

	@Override
	public void dispose() {
		delegate.dispose();
		cache.clear();
	}
}
