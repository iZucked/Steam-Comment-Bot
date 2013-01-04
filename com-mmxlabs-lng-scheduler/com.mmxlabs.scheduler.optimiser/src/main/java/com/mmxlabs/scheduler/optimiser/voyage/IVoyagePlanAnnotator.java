/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
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
	 * @param resource
	 * @param plans
	 * @param annotatedSequence
	 * @since 2.0
	 */
	void annotateFromVoyagePlan(IResource resource, List<VoyagePlan> plans, IAnnotatedSolution annotatedSolution, int[] arrivalTimes);

}
