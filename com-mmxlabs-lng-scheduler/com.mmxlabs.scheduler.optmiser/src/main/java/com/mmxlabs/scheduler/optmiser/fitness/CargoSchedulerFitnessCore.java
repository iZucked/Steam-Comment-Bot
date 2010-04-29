package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optmiser.builder.SchedulerConstants;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.providers.IPortProvider;

public class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Number> portDistances;

	private List<IFitnessComponent<T>> components;

	public CargoSchedulerFitnessCore() {
		components = new ArrayList<IFitnessComponent<T>>(1);
		components
				.add(new DistanceComponent<T>(
						CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME,
						this));
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void evaluate(final ISequences<T> sequences) {

		// TODO Auto-generated method stub

	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return components;
	}

	@Override
	public void init(final IOptimisationData<T> data) {

		portProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class);
		portDistances = data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMatrixProvider.class);

		// init data structures
	}

	private long evaluateSequence(final IResource resource,
			final ISequence<T> sequence) {
		long distance = 0;

		T prevElement = null;

		for (final T element : sequence) {

			if (prevElement != null) {
				final IPort from = portProvider.getPortForElement(prevElement);
				final IPort to = portProvider.getPortForElement(element);

				final Number dist = portDistances.get(from, to);
				if (dist != null) {
					distance += dist.longValue();
				} else {
					return Long.MAX_VALUE;
				}
			}

			prevElement = element;
		}

		return distance;
	}

	public long getDistanceFitness() {
		// TODO Auto-generated method stub
		return 0;
	}
}
