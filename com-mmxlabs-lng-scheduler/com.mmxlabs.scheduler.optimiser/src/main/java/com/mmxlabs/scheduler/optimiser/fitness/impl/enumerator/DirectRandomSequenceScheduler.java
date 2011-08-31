/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * Another random sequence scheduler, which works by resting on top of the {@link EnumeratingSequenceScheduler}
 * and replacing the exhaustive recursive loop with something randomised; for each element it will randomly pick
 * an arrival time from the feasible range
 * @author hinton
 *
 * @param <T>
 */
public class DirectRandomSequenceScheduler<T> extends
		EnumeratingSequenceScheduler<T> {
	/**
	 * 
	 */
	private static final int RETRY_COUNT = 5;
	private final int seed = 0;
	private Random random;
	private static final Logger log = LoggerFactory.getLogger(DirectRandomSequenceScheduler.class);
	/**
	 * Do this many times the usual number of samples for an exported solution.
	 * Slightly improves the quality of the exported schedule.
	 */
	private static final int EXPORT_INTENSITY = 1000;

	/**
	 * Never do more than this many samples
	 */
	private int samplingUpperBound = 1;
	/**
	 * Sample this proportion of the search space, up to {@code samplingUpperBound}
	 */
	private double sampleProportion = 0.005;
	
	
	
	@Override
	public ScheduledSequences schedule(final ISequences<T> sequences, final boolean forExport) {
		random = new Random(seed);
		
		setSequences(sequences);
		resetBest();
		final long approximateSpaceSize = prepare(samplingUpperBound*10000);
		final int sampleCount = Math.min(samplingUpperBound, (int)(sampleProportion * approximateSpaceSize))
			* (forExport ? EXPORT_INTENSITY : 1);
		
		int failures = 0;
		
//		log.debug("sampling " +sampleCount + " schedules");
		for (int i = 0; i<sampleCount; i++) {
			randomise();
			evaluate();
			if (getBestResult() == null) {
				failures++;
				i = 0;
				if (failures > RETRY_COUNT * sampleCount) {
					log.error("Sequences have not had a feasible solution in " + sampleCount * 5 + " samples");
					return null;
				}
			}
		}
		
//		log.debug("best found after " + bestIndex);
		
		return getBestResult();
	}

	/**
	 * 
	 */
	private void randomise() {
		for (int seq = 0; seq<arrivalTimes.length; seq++) {
			for (int pos = 0; pos<sizes[seq]; pos++) {
				final int min = getMinArrivalTime(seq, pos);
				final int max = getMaxArrivalTime(seq, pos);
				arrivalTimes[seq][pos] = RandomHelper.nextIntBetween(random, min, max);
			} 	
		}
	}

	public int getSamplingUpperBound() {
		return samplingUpperBound;
	}

	public void setSamplingUpperBound(final int samplingUpperBound) {
		this.samplingUpperBound = samplingUpperBound;
	}

	public double getSampleProportion() {
		return sampleProportion;
	}

	public void setSampleProportion(final double sampleProportion) {
		this.sampleProportion = sampleProportion;
	}
}
