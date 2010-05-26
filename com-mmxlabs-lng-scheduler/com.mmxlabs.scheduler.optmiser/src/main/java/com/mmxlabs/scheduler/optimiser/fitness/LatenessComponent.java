package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;

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
	public void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler,
			final boolean newSequence) {

		long lateness = 0;

		for (final T element : sequence) {

			final IPortVisitEvent<T> e = scheduler.getAdditionalInformation(
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
		updateFitness(resource, lateness, newSequence);
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
