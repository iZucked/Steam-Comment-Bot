package com.mmxlabs.scheduler.optimiser.contracts;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public interface IEntityValueCalculator {

	void init(IOptimisationData data);

	void dispose();

	long evaluate(VoyagePlan plan, IAllocationAnnotation currentAllocation, IVessel vessel, IAnnotatedSolution annotatedSolution);

	long evaluate(VoyagePlan plan, IVessel vessel, int time, IAnnotatedSolution annotatedSolution);

}
