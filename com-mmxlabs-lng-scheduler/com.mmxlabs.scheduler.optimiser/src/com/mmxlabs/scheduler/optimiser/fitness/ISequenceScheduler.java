/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * This class contains the logic required to schedule a {@link ISequence}. This will determine arrival times and additional information.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequenceScheduler {

	/**
	 * Schedule the given set of sequences, returning a {@link ScheduledSequences}.
	 * 
	 * It is now the schedulers job to make sure that the schedule returned is the last one evaluated.
	 * 
	 * This is in the interests of saving an unnecessary evaluation in the cargo scheduler fitness core in the event that the scheduler evaluates a schedule and then returns it directly.
	 * 
	 * @return
	 */
	ScheduledSequences schedule(ISequences sequences, final Collection<IResource> affectedResources, IAnnotatedSolution solution);

	/**
	 * Like {@link #schedule(ISequences, Collection, boolean)}, but with all resources needing evaluation.
	 * 
	 * @param sequences
	 * @param forExport
	 * @return
	 */
	ScheduledSequences schedule(ISequences sequences, IAnnotatedSolution solution);

	/**
	 * The caller can use this to avoid full evaluation on the next cycle if a schedule is accepted.
	 */
	void acceptLastSchedule();
}
