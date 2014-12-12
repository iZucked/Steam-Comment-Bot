/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
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
	 * Like {@link #schedule(ISequences, Collection, boolean)}, but with all resources needing evaluation.
	 * 
	 * @param sequences
	 * @param forExport
	 * @return
	 */
	ScheduledSequences schedule(@NonNull ISequences sequences, @Nullable IAnnotatedSolution solution);
}
