/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor;

import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

/**
 * @since 2.0
 */
public interface IBreakEvenEvaluator {

	static final String MARKER = "?";
	
	void processSchedule(ScheduledSequences scheduledSequences);

}
