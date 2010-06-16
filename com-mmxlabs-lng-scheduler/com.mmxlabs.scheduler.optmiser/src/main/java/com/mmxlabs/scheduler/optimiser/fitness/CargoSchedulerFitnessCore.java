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
import com.mmxlabs.scheduler.optimiser.fitness.components.DistanceComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;

/**
 * {@link IFitnessCore} which schedules {@link ISequences} objects using an
 * {@link ISequenceScheduler}. Various {@link IFitnessComponent}s implementing
 * {@link ICargoSchedulerFitnessComponent} calculate fitnesses on the scheduled
 * {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private final List<ICargoSchedulerFitnessComponent<T>> components;

	private IOptimisationData<T> data;

	private ISequenceScheduler<T> scheduler;

	public CargoSchedulerFitnessCore() {

		// Create the fitness components

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

		// Notify fitness components last state was accepted.
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.accepted(sequences, affectedResources);
		}
	}

	@Override
	public void evaluate(final ISequences<T> sequences) {

		// Notify fitness components a new full evaluation is about to begin
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.prepare();
		}

		// For each ISequence, run the scheduler
		for (final IResource resource : sequences.getResources()) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final IAnnotatedSequence<T> annotatedSequence = new AnnotatedSequence<T>();
			scheduler.schedule(resource, sequence, annotatedSequence);
			// Notify fitness components that the given ISequence has been
			// scheduled and is ready to be evaluated.
			evaluateSequence(resource, sequence, annotatedSequence, false);
		}

		// Notify fitness components that all sequences have been scheduled
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.complete();
		}
	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// Re-schedule changed sequences
		for (final IResource resource : affectedResources) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final IAnnotatedSequence<T> annotatedSequence = new AnnotatedSequence<T>();
			scheduler.schedule(resource, sequence, annotatedSequence);
			evaluateSequence(resource, sequence, annotatedSequence, true);
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

		// Notify fitness components that a new optimisation is beginning
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.init(data);
		}

	}

	private void evaluateSequence(final IResource resource,
			final ISequence<T> sequence,
			final IAnnotatedSequence<T> annotatedSequence,
			final boolean newSequence) {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.evaluateSequence(resource, sequence, annotatedSequence,
					newSequence);
		}
	}

	/**
	 * Create a new {@link ISequenceScheduler} instance.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ISequenceScheduler<T> createSequenceScheduler() {
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
