package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optmiser.SchedulerConstants;

public class DistanceComponent<T> extends
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	protected DistanceComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

	@Override
	public void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler,
			final boolean newSequence) {

		long distance = 0;

		for (final T element : sequence) {

			final IJourneyEvent<T> e = scheduler.getAdditionalInformation(
					element, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);
			if (e != null) {
				distance += e.getDistance();
			}
		}

		updateFitness(resource, distance, newSequence);
	}
}
