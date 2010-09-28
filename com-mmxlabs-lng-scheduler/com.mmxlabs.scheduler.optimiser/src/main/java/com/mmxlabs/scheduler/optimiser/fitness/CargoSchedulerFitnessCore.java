package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.DistanceComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.RouteCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

	private ISequenceScheduler<T> scheduler;

	private ISchedulerFactory<T> schedulerFactory;


	public CargoSchedulerFitnessCore() {

		// Create the fitness components

		components = new ArrayList<ICargoSchedulerFitnessComponent<T>>(5);
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

		components.add(new CharterCostFitnessComponent<T>(
				CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME,
				SchedulerConstants.DCP_vesselProvider, //not sure if this should be here or somewhere else
				this));
		
		components.add(new RouteCostFitnessComponent<T>(
			SchedulerConstants.DCP_routePriceProvider, SchedulerConstants.DCP_vesselProvider,
			CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME,
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
	public boolean evaluate(final ISequences<T> sequences) {

		// Notify fitness components a new full evaluation is about to begin
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.prepare();
		}

		// For each ISequence, run the scheduler
		for (final IResource resource : sequences.getResources()) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final List<VoyagePlan> plans = scheduler.schedule(resource,
					sequence);

			// Notify fitness components that the given ISequence has been
			// scheduled and is ready to be evaluated.
//			if (plans == null) {
//				return false;
//			}
			
			if (evaluateSequence(resource, sequence, plans, false) == false) {
				return false;
			}
		}

		// Notify fitness components that all sequences have been scheduled
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.complete();
		}

		return true;
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		// TODO!!
		// TODO!!
		// TODO!!
		// Add test case to catch missing prepareDelta calls
		//
		// Add a rejectMove() to completement accept();
		//
		//
		// TODO: Rethink how this ICargoSchedulerFitnessComponent should work as
		// we now have far too many method calls

		// Notify fitness components a new delta evaluation is about to begin
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.prepareDelta();
		}

		// Re-schedule changed sequences
		for (final IResource resource : affectedResources) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			if (sequence.size() > 0) {

				final List<VoyagePlan> plans = scheduler.schedule(resource,
						sequence);
				if (plans == null) {
					System.err.println("Scheduler has returned null voyage plan; backing out of move.");
					return false; //for some reason, this move has no valid plans. this
					//should probably be a serious issue.
				}
				if (evaluateSequence(resource, sequence, plans, true) == false) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return new ArrayList<IFitnessComponent<T>>(components);
	}

	@Override
	public void init(final IOptimisationData<T> data) {

		// TODO: getter/setters should provide these e.g. use factory to
		// populate
//		scheduler = SchedulerUtils.createSimpleSequenceScheduler(data);
		scheduler = schedulerFactory.createScheduler(data, components); 
			/*SchedulerUtils.createGASequenceScheduler(data, components);*/

		// Notify fitness components that a new optimisation is beginning
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.init(data);
		}
	}

	private boolean evaluateSequence(final IResource resource,
			final ISequence<T> sequence,
			final List<VoyagePlan> plans,
			final boolean newSequence) {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			if (c.evaluateSequence(resource, sequence, plans,
					newSequence) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void dispose() {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.dispose();
		}
		components.clear();
		scheduler.dispose();
	}

	public List<ICargoSchedulerFitnessComponent<T>> getCargoSchedulerFitnessComponent() {
		return components;
	}

	public void setSchedulerFactory(ISchedulerFactory<T> schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}
}
