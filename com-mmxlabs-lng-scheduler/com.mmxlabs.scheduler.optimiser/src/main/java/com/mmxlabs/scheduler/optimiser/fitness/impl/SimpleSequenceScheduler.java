package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;

/**
 * Simple scheduler. Try to arrive at the {@link ITimeWindow} start.
 * 
 * @author Simon Goodall
 * 
 */
public final class SimpleSequenceScheduler<T> extends
		AbstractSequenceScheduler<T> {

	@Override
	public List<IVoyagePlan> schedule(final IResource resource,
			final ISequence<T> sequence) {

		int[] arrivalTimes = new int[sequence.size()];

		int idx = 0;
		for (final T element : sequence) {
			final List<ITimeWindow> timeWindows = getTimeWindowProvider()
					.getTimeWindows(element);

			// Find earliest start time.
			// TODO: No time windows means time window is Integer.MAX_VALUE -
			// not really sure if this is a sane thing to use.
			int timeWindowStart = Integer.MAX_VALUE;
			for (final ITimeWindow window : timeWindows) {
				timeWindowStart = Math.min(timeWindowStart, window.getStart());
			}
			arrivalTimes[idx++] = timeWindowStart;
		}
		return super.schedule(resource, sequence, arrivalTimes, true);
	}
}
