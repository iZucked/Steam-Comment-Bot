/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * This class contains the logic required to schedule a {@link ISequence}. This
 * will determine arrival times and additional information.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISequenceScheduler<T> {

	/**
	 * Schedule the given set of sequences, returning a {@link ScheduledSequences}
	 * 
	 * @return
	 */
	ScheduledSequences schedule(
			ISequences<T> sequences);

	/**
	 * Release resources.
	 */
	void dispose();

}
