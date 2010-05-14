package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.optimiser.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optmiser.SchedulerConstants;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optmiser.providers.IPortProvider;

public class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private IPortProvider portProvider;

	private IMatrixProvider<IPort, Number> portDistances;

	private ITimeWindowDataComponentProvider timeWindowProvider;
	
	private final List<IFitnessComponent<T>> components;

	private IOptimisationData<T> data;

	public CargoSchedulerFitnessCore() {
		components = new ArrayList<IFitnessComponent<T>>(2);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void evaluate(final ISequences<T> sequences) {

		// TODO Auto-generated method stub

		final ISequenceScheduler scheduler = createSequenceScheduler();
		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);

			scheduler.schedule(resource, sequence);

			evaluateSequence(resource, sequence, scheduler);
		}

	}

	@Override
	public void evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		// TODO Auto-generated method stub

		final ISequenceScheduler scheduler = createSequenceScheduler();
		for (final IResource resource : affectedResources) {
			final ISequence sequence = sequences.getSequence(resource);

			scheduler.schedule(resource, sequence);

			evaluateSequence(resource, sequence, scheduler);
		}
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return components;
	}

	@Override
	public void init(final IOptimisationData<T> data) {

		this.data = data;

		portProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class);
		portDistances = data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMatrixProvider.class);

		// init data structures
	}

	private long evaluateSequenceDistance(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler) {

		long distance = 0;

		for (final T element : sequence) {

			IJourneyElement e = scheduler.getAdditionalInformation(element,
					SchedulerConstants.AI_journeyInfo, IJourneyElement.class);
			if (e != null) {
				distance += e.getDistance();
			}

		}
		return distance;
	}

	private long evaluateSequenceLateness(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler) {

		long lateness = 0;

		for (final T element : sequence) {

			
			IVisitElement e = scheduler.getAdditionalInformation(element,
					SchedulerConstants.AI_visitInfo, IVisitElement.class);
			assert e != null;
			if (e != null) {
				int arrival= e.getStartTime();
				List<ITimeWindow> tws = timeWindowProvider.getTimeWindows(element);
				for (ITimeWindow tw : tws) {
					if (arrival > tw.getEnd()) {
						lateness += arrival - tw.getEnd();
					}
				}
			}

		}
		return lateness;
	}

	
	private void evaluateSequence(final IResource resource,
			final ISequence<T> sequence, final ISequenceScheduler<T> scheduler) {

		// TODO: Move these into the components themselves
		long distanceFitness = evaluateSequenceDistance(resource, sequence, scheduler);
		long latenessFitness = evaluateSequenceLateness(resource, sequence, scheduler);
		
		// TODO: Do something with this data
		
		
	}

	public long getDistanceFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLatenessFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	ISequenceScheduler createSequenceScheduler() {
		final SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();

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
		this.portDistances = null;
		this.portProvider = null;
	}
}
