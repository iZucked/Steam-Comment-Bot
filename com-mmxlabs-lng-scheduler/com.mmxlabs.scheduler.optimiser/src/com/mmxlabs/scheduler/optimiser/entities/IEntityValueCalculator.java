/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.entities;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public interface IEntityValueCalculator {

	long evaluate(VoyagePlan plan, IAllocationAnnotation currentAllocation, IVessel vessel, int vesselStartTime, IAnnotatedSolution annotatedSolution);

	long evaluate(VoyagePlan plan, IVessel vessel, int planStartTime, int vesselStartTime, IAnnotatedSolution annotatedSolution);
}
