package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;

/**
 * Empty implementation of {@link IBreakEvenEvaluator}
 * 
 * @author Simon Goodall
 * @since 8.0
 * 
 */
public class NullBreakEvenEvaluator implements IBreakEvenEvaluator {

	@Override
	public void processSchedule(final ScheduledSequences scheduledSequences) {
		// Does nothing
	}

}
