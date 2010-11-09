/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

/**
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on total distance travelled.
 * 
 * This component is currently disabled.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class DistanceComponent<T> extends
		AbstractPerRouteSchedulerFitnessComponent<T>  {

	public DistanceComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

//	@Override
//	public long rawEvaluateSequence(final IResource resource,
//			final ISequence<T> sequence, final List<VoyagePlan> plans, final int startTime) {
//		// Calculate sum distance travelled.
//
////		long distance = 0;
////		for (final VoyagePlan plan : plans) {
////			for (final Object obj : plan.getSequence()) {
////				if (obj instanceof VoyageDetails) {
////					@SuppressWarnings("unchecked")
////					final VoyageDetails<T> detail = (VoyageDetails<T>) obj;
////					distance += detail.getOptions().getDistance();
////				}
////			}
////		}
//
//		// TODO: Temp remove distance from fitness - should really alter weight
//		// or remove component from evaluations instead
////		distance = 0;
//		return 0;// distance;
//	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(IResource resource) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return 0;
	}
}
