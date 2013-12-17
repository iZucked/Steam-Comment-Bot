package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;

/**
 * Empty implementation of {@link IGeneratedCharterOutEvaluator}
 * 
 * @author Simon Goodall
 * 
 */
public class NullGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Override
	public void processSchedule(final ScheduledSequences scheduledSequences) {
		// Does nothing
	}

}
