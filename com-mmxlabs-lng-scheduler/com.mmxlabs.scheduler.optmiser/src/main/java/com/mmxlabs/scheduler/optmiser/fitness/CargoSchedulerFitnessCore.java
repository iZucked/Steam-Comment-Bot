package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.Collection;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.providers.IPortProvider;

public class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Number> portDistances;

	@Override
	public void accepted(ISequences<T> sequences,
			Collection<IResource> affectedResources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void evaluate(ISequences<T> sequences) {

		// TODO Auto-generated method stub

	}

	@Override
	public void evaluate(ISequences<T> sequences,
			Collection<IResource> affectedResources) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(IOptimisationData<T> data) {

		portProvider = data.getDataComponentProvider("port-provider",
				IPortProvider.class);
		portDistances = data.getDataComponentProvider("port-distances",
				IMatrixProvider.class);
	}

	private long evaluateSequence(IResource resource, ISequence<T> sequence) {
		long distance = 0;

		T prevElement = null;

		for (T element : sequence) {

			if (prevElement != null) {
				IPort from = portProvider.getPortForElement(prevElement);
				IPort to = portProvider.getPortForElement(element);

				Number dist = portDistances.get(from, to);
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
}
