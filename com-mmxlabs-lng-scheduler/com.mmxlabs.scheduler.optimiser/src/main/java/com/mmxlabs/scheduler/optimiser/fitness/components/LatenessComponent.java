package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;

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

	private ITimeWindowDataComponentProvider timeWindowProvider;

	public LatenessComponent(final String name,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
	}

	@SuppressWarnings("unchecked")
	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final IAnnotatedSequence<T> annotatedSequence) {

		long lateness = 0;

		for (final T element : sequence) {

			final IPortVisitEvent<T> e = annotatedSequence.getAnnotation(
					element, SchedulerConstants.AI_visitInfo,
					IPortVisitEvent.class);
			assert e != null;
			if (e != null) {
				final int arrival = e.getStartTime();
				final List<ITimeWindow> tws = timeWindowProvider
						.getTimeWindows(element);
				for (final ITimeWindow tw : tws) {
					// If we have arrived after our end window, then add the difference.
					// TODO: Should the end be inclusive or exclusive?
					if (arrival > tw.getEnd()) {
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
	public void init(IOptimisationData<T> data) {
		timeWindowProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class);
	}

	@Override
	public void dispose() {
		timeWindowProvider = null;
		super.dispose();
	}
}
