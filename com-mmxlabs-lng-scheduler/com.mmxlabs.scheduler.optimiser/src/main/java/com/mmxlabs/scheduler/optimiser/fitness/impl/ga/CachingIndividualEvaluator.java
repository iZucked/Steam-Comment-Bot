package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.Arrays;

/**
 * A memoizing evaluator; wraps another evaluator but adds a cache to make reevaluation
 * efficient.
 * 
 * @author hinton
 *
 * @param <T>
 */
public class CachingIndividualEvaluator<T> implements IIndividualEvaluator<T> {
	final IIndividualEvaluator<T> delegate;
	
	final int tableSize;
	byte [][] occupants;
	long []   values;
	int  []   hits;
	int  []   misses;
	
	int totalHits;
	int totalMisses;
	
	public CachingIndividualEvaluator(IIndividualEvaluator<T> delegate, int size) {
		super();
		this.delegate = delegate;
		this.tableSize = size;
		occupants = new byte[size][];
		values = new long[size];
		misses = new int[size];
		hits = new int[size];
		totalHits = totalMisses = 0;
	}

	@Override
	public long evaluate(Individual individual) {
		final int hash = individual.hashBytes() % tableSize;
//		System.out.println(totalHits / (0.0 + totalHits + totalMisses));
		if (Arrays.equals(individual.bytes, occupants[hash])) {
			hits[hash]++;
			totalHits++;
			return values[hash];
		} else {
			totalMisses++;
			final long value = delegate.evaluate(individual);
			
			if (misses[hash] > hits[hash]) {
				//replace
				misses[hash] = 0;
				hits[hash] = 1;
				values[hash] = value;
				occupants[hash] = individual.bytes.clone(); //is this a terrible idea?
			} else {
				misses[hash]++;
			}
			
			return value;
		}
	}

	@Override
	public void dispose() {
		delegate.dispose();
	}
}
