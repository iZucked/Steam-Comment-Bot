/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A {@link IVoyagePlanAnnotator} uses {@link VoyagePlan}s to annotate
 * {@link IAnnotatedSequence} objects.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IVoyagePlanAnnotator<T> {

	/**
	 * Annotate the {@link IAnnotatedSequence} from the list of
	 * {@link VoyagePlan} objects.
	 * 
	 * @param resource
	 * @param plans
	 * @param annotatedSequence
	 */
	void annotateFromVoyagePlan(IResource resource, List<VoyagePlan> plans, int startTime,
			IAnnotatedSolution<T> annotatedSolution);

}
