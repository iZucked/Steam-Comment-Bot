package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;

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

	protected DistanceComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler,
			final boolean newSequence) {
		// Calculate sum distance travelled.
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
