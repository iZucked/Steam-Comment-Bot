/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A sequence scheduler which enumerates all bit-strings for the input, and uses the best one.
 * 
 * Mostly intended for a quick investigation of what the search space typically looks like
 * 
 * @author hinton
 *
 * @param <T>
 */
public class ByteEnumeratingSequenceScheduler<T> { //extends AbstractSequenceScheduler<T> {
//
//	IndividualEvaluator<T> individualEvaluator;
//	
//	public IndividualEvaluator<T> getIndividualEvaluator() {
//		return individualEvaluator;
//	}
//
//	public void setIndividualEvaluator(IndividualEvaluator<T> individualEvaluator) {
//		this.individualEvaluator = individualEvaluator;
//	}
//
//	@Override
//	public Pair<Integer, List<VoyagePlan>> schedule(IResource resource,
//			ISequence<T> sequence) {
//		final int bytes = individualEvaluator.setup(resource, sequence);
//		
//		byte[] array = new byte[bytes];
//		byte[] best = new byte[bytes];
//		long bestCost = Long.MAX_VALUE;
//		Individual individual = new Individual(array);
//		boolean finished = false;
//		while (!finished) {
//			boolean carry = true;
//			int column = 0;
//			//increment byte array
//			while (carry) {
//				array[column]++;
//				if (array[column] == 0) {
//					carry = true;
//					column++;
//					if (column == array.length) {
//						carry = false;
//						finished = true;
//					}
//				} else {
//					carry = false;
//				}
//			}
//			//evaluate individual
//			
//			final long cost = individualEvaluator.evaluate(individual);
//			if (cost < bestCost) {
//				bestCost = cost;
//				System.arraycopy(array, 0, best, 0, array.length);
//			}
//		}
//		
//		final int[] arrivalTimes = new int[sequence.size()];
//		System.arraycopy(best, 0, array, 0, best.length);
//		individualEvaluator.decode(individual, arrivalTimes);		
//		return super.schedule(resource, sequence, arrivalTimes);
//	}
//
//	@Override
//	public void dispose() {
//		setIndividualEvaluator(null);
//	}
}
