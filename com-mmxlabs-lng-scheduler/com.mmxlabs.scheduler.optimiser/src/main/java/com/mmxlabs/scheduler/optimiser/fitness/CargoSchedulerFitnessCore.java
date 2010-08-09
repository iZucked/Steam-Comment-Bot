package com.mmxlabs.scheduler.optimiser.fitness;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.DistanceComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlanAnnotator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

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

	private IVoyagePlanAnnotator<T> voyagePlanAnnotator;

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

		components.add(new CostComponent<T>(
				CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME,
				CollectionsUtil.makeArrayList(FuelComponent.NBO,
						FuelComponent.FBO, FuelComponent.IdleNBO), this));

		components.add(new CostComponent<T>(
				CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME,
				CollectionsUtil
						.makeArrayList(FuelComponent.Base,
								FuelComponent.Base_Supplemental,
								FuelComponent.IdleBase), this));
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
			final List<IVoyagePlan> plans = scheduler.schedule(resource,
					sequence);
			voyagePlanAnnotator.annonateFromVoyagePlan(resource, plans,
					annotatedSequence);

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
// TODO!!
//		TODO!!
		//TODO!!
//		Add test case to catch missing prepareDelta calls
//		
//		Add a rejectMove() to completement accept();
//		
//		
//		TODO: Rethink how this ICargoSchedulerFitnessComponent should work as we now have far too many method calls
		
//		Notify fitness components a new delta evaluation is about to begin
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.prepareDelta();
		}
		
		// Re-schedule changed sequences
		for (final IResource resource : affectedResources) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final IAnnotatedSequence<T> annotatedSequence = new AnnotatedSequence<T>();
			if (sequence.size() > 0) {

				final List<IVoyagePlan> plans = scheduler.schedule(resource,
						sequence);

				voyagePlanAnnotator.annonateFromVoyagePlan(resource, plans,
						annotatedSequence);
			}
			evaluateSequence(resource, sequence, annotatedSequence, true);
		}
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return new ArrayList<IFitnessComponent<T>>(components);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(final IOptimisationData<T> data) {

		this.data = data;

		// TODO: getter/setters should provide these e.g. use factory to
		// populate
		scheduler = createSequenceScheduler();
		VoyagePlanAnnotator<T> vpa = new VoyagePlanAnnotator<T>();
		vpa.setPortSlotProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider,
				IPortSlotProvider.class));

		voyagePlanAnnotator = vpa;

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
		scheduler.setPortSlotProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider,
				IPortSlotProvider.class));
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));

		scheduler.setVesselProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class));

		final LNGVoyageCalculator<T> voyageCalculator = new LNGVoyageCalculator<T>();

		scheduler.setVoyageCalculator(voyageCalculator);

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
