package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;

public class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private final List<ICargoSchedulerFitnessComponent<T>> components;

	private IOptimisationData<T> data;

	private ISequenceScheduler<T> scheduler;

	public CargoSchedulerFitnessCore() {
		components = new ArrayList<ICargoSchedulerFitnessComponent<T>>(2);
		components
				.add(new DistanceComponent<T>(
						CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME,
						this));
		components
				.add(new LatenessComponent<T>(
						CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME,
						this));
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.accepted(sequences, affectedResources);
		}
	}

	@Override
	public void evaluate(final ISequences<T> sequences) {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.prepare();
		}

		for (final IResource resource : sequences.getResources()) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			scheduler.schedule(resource, sequence);
			evaluateSequence(resource, sequence, scheduler, false);
		}
		
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.complete();
		}
	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		for (final IResource resource : affectedResources) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			scheduler.schedule(resource, sequence);
			evaluateSequence(resource, sequence, scheduler, true);
		}
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return new ArrayList<IFitnessComponent<T>>(components);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

		this.data = data;

		scheduler = createSequenceScheduler();
		
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.init(data);
		}

	}

	private void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler,
			final boolean newSequence) {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.evaluateSequence(resource, sequence, scheduler, newSequence);
		}
	}

	ISequenceScheduler<T> createSequenceScheduler() {
		final SimpleSequenceScheduler<T> scheduler = new SimpleSequenceScheduler<T>();

		scheduler.setDistanceProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMatrixProvider.class));
		scheduler.setDurationsProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class));
		scheduler.setPortProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class));

		scheduler.init();

		return scheduler;
	}

	@Override
	public void dispose() {

		this.data = null;
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.dispose();
		}
		components.clear();
		scheduler.dispose();
	}
}
