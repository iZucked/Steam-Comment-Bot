package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * @since 2.0
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";
	
	void processSchedule(ScheduledSequences scheduledSequences);

}
