/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.ISequences;

/**
 * Relaxing sequence scheduler; solution procedure is like this:
 * 
 * <ol>
 * <li>Set every arrival time to its minimum value</li>
 * <li>Relax each arrival time to the first local minimum for that time</li>
 * <li>Maybe do another pass, or something?</li>
 * </ol>
 * 
 * @author hinton FIXME: 2015-01-21; SG -- Disable class as re-evaluating with the VPO return time changes can lead to inconsistent results. Need to ensure arrivalTimes array is properly reset
 */
@Deprecated
/* public */class RelaxingSequenceScheduler extends EnumeratingSequenceScheduler {
	private static final Logger log = LoggerFactory.getLogger(RelaxingSequenceScheduler.class);

	final int steps = 20;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {
		// setSequences(sequences);
		// resetBest();
		// prepare2();
		// evaluate();
		//
		// for (int seq = 0; seq < arrivalTimes.length; seq++) {
		// for (int pos = 0; pos < sizes[seq]; pos++) {
		// relax(seq, pos);
		// }
		// }
		// log.debug("relaxed; " + count + " evaluations");
		// return reEvaluateAndGetBestResult(solution);
		return null;
	}

	private void relax(final int seq, final int pos) {
		// final int min = arrivalTimes[seq][pos];
		// final int max = getMaxArrivalTime(seq, pos);
		// // log.debug("relax(" + seq + ", " + pos + "), time between " + min
		// // + ", " + max);
		// int lastArrivalTime;
		// for (int step = 0; step < steps; step++) {
		// final int i = min + 1 + ((step * ((max - min) + 1)) / steps);
		// lastArrivalTime = arrivalTimes[seq][pos];
		// arrivalTimes[seq][pos] = i;
		// int next;
		// // push later arrival times in this sequence
		// for (next = pos + 1; next < arrivalTimes[seq].length; next++) {
		// final int mat = getMinArrivalTime(seq, next);
		// if (mat == arrivalTimes[seq][next]) {
		// break;
		// }
		// arrivalTimes[seq][next] = mat;
		// }
		// final long prevValue = getLastValue();
		// evaluate();
		// if ((prevValue <= getLastValue()) && (prevValue != Long.MAX_VALUE)) {
		// // roll back change
		// arrivalTimes[seq][pos] = lastArrivalTime;
		// for (int fix = pos; (fix < next) && (fix < arrivalTimes[seq].length); fix++) {
		// arrivalTimes[seq][fix] = getMinArrivalTime(seq, fix);
		// }
		// evaluate();
		// // log.debug("relaxed to " + (i - 1));
		// return;
		// }
		// }
	}

	/**
	 * Prepare and ignore approximate space size; also set arrival times to min
	 */
	private final void prepare2() {
		prepare();

		for (int seq = 0; seq < arrivalTimes.length; seq++) {
			for (int pos = 0; pos < sizes[seq]; pos++) {
				arrivalTimes[seq][pos] = getMinArrivalTime(seq, pos);
			}
		}
	}

}
