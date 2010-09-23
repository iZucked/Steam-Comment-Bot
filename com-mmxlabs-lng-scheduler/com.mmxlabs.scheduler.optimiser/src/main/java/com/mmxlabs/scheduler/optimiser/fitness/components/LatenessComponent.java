package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IPortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on lateness.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class LatenessComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	public LatenessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final List<VoyagePlan> plans) {

		long lateness = 0;

		for (final VoyagePlan plan : plans) {
			for (final Object obj : plan.getSequence()) {
				if (obj instanceof IPortDetails) {
					final IPortDetails detail = (IPortDetails) obj;
					final int arrival = detail.getStartTime();
					final ITimeWindow tw = detail.getPortSlot().getTimeWindow();

					if (tw != null && arrival > tw.getEnd()) {
						lateness += arrival - tw.getEnd();
					}
				}
			}
		}

		// TODO: Hack in a weighting
		lateness *= 1000000;

		return lateness;
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		
	}
}
