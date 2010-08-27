package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

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

	@SuppressWarnings("unchecked")
	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final IAnnotatedSequence<T> annotatedSequence) {
		// Calculate sum distance travelled.
		long distance = 0;

		for (final T element : sequence) {

			final IJourneyEvent<T> e = annotatedSequence.getAnnotation(
					element, SchedulerConstants.AI_journeyInfo,
					IJourneyEvent.class);

			if (e != null) {
				distance += e.getDistance();
			}
		}

		//TODO: Temp remove distance from fitness - should really alter weight or remove component from evaluations instead
		distance = 0;
		return distance;
	}
}
