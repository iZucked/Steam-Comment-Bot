/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on total distance travelled.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class DistanceComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	public DistanceComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final List<VoyagePlan> plans, final int startTime) {
		// Calculate sum distance travelled.

//		long distance = 0;
//		for (final VoyagePlan plan : plans) {
//			for (final Object obj : plan.getSequence()) {
//				if (obj instanceof VoyageDetails) {
//					@SuppressWarnings("unchecked")
//					final VoyageDetails<T> detail = (VoyageDetails<T>) obj;
//					distance += detail.getOptions().getDistance();
//				}
//			}
//		}

		// TODO: Temp remove distance from fitness - should really alter weight
		// or remove component from evaluations instead
//		distance = 0;
		return 0;// distance;
	}

	@Override
	public boolean shouldIterate() {
		return false;
	}

	@Override
	public void beginIterating(IResource resource) {
		
	}

	@Override
	public void evaluateNextObject(Object object, int startTime) {
		
	}

	@Override
	public void endIterating() {
		
	}
}
