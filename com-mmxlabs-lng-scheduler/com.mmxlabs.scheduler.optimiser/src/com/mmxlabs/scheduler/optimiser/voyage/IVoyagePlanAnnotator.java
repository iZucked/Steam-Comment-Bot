/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A {@link IVoyagePlanAnnotator} uses {@link VoyagePlan}s to annotate {@link IAnnotatedSequence} objects.
 * 
 * @author Simon Goodall
 * 
 */
public interface IVoyagePlanAnnotator {

	/**
	 * Annotate the {@link IAnnotatedSequence} from the list of {@link VoyagePlan} objects.
	 * 
	 * @param Sequence
	 * @param annotatedSequence
	 */
	void annotateFromVoyagePlan(ScheduledSequence scheduledSequence, IAnnotatedSolution annotatedSolution);

}
