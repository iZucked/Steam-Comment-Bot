/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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
	 * Attempt to schedule the given {@link ISequence}. Returns a {@link List}
	 * of {@link VoyagePlan}s or null if there was a problem.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	Pair<Integer, List<VoyagePlan>> schedule(IResource resource, ISequence<T> sequence);

	/**
	 * Release resources.
	 */
	void dispose();

}
