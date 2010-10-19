/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Simple scheduler. Try to arrive at the {@link ITimeWindow} start.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler<T> extends
		AbstractSequenceScheduler<T> {

	@Override
	public Pair<Integer, List<VoyagePlan>> schedule(final IResource resource,
			final ISequence<T> sequence) {

		int[] arrivalTimes = new int[sequence.size()];

		int idx = 0;
		for (final T element : sequence) {
			final List<ITimeWindow> timeWindows = getTimeWindowProvider()
					.getTimeWindows(element);

			// Find earliest start time.
			// TODO: No time windows means time window is Integer.MAX_VALUE -
			// not really sure if this is a sane thing to use.

			//if there's no time window, setting this to max value causes a 
			//capacity violation. needs something better. can't just set it to
			//the previous arrival time, because that won't allow enough travel time
			int timeWindowStart = Integer.MAX_VALUE;
			if (timeWindows.isEmpty() && idx > 0) {
				
				final int lastTimeWindowStart = arrivalTimes[idx-1];
				timeWindowStart = lastTimeWindowStart +
				Calculator.getTimeFromSpeedDistance(
					getVesselProvider().getVessel(resource).getVesselClass().getMaxSpeed(),
					getDistanceProvider().get(IMultiMatrixProvider.Default_Key).get(
							getPortProvider().getPortForElement(sequence.get(idx-1)),
							getPortProvider().getPortForElement(element)));
			} else {
				for (final ITimeWindow window : timeWindows) {
					timeWindowStart = Math.min(timeWindowStart, window.getStart());
				}
			}
			arrivalTimes[idx++] = timeWindowStart;
		}
		return super.schedule(resource, sequence, arrivalTimes);
	}
}
