/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * This is similar to the IndividualEvaluator, but just uses a schedule The IE could probably be refactored to use this, but it's a bit tangled at the moment
 * 
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 * @param
 */
public class ScheduleEvaluator {

	private final VoyagePlanIterator vpIterator = new VoyagePlanIterator();

	@Inject
	private ICargoAllocator cargoAllocator;

	private Collection<ILoadPriceCalculator> loadPriceCalculators;
	private Collection<ICargoSchedulerFitnessComponent> fitnessComponents;

	private Collection<ICargoAllocationFitnessComponent> allocationComponents;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	private long[] fitnesses;

	/**
	 * @since 2.0
	 */
	@Inject
	public void init() {
		setLoadPriceCalculators(calculatorProvider.getLoadPriceCalculators());
	}

	public long evaluateSchedule(final ScheduledSequences scheduledSequences, final int[] changedSequences) {
		// First, we evaluate all the scheduler components. These are fitness components dependent only on
		// values from the ScheduledSequences.

		final HashSet<IResource> affectedResources = new HashSet<IResource>();
		for (final int index : changedSequences) {
			affectedResources.add(scheduledSequences.get(index).getResource());
		}
		if (!vpIterator.iterateSchedulerComponents(fitnessComponents, scheduledSequences, affectedResources, fitnesses)) {
			return Long.MAX_VALUE;
		}

		long total = 0;
		for (final long l : fitnesses) {
			if (l == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			total += l;
		}
		if (entityValueCalculator != null) {
			// Charter Out Optimisation... Detect potential charter out opportunities.
			for (ScheduledSequence seq : scheduledSequences) {

				int currentTime = seq.getStartTime();
				for (VoyagePlan vp : seq.getVoyagePlans()) {

					// Grab the current list of arrival times and update the rolling currentTime
					// 5 as we know that is the max we need (currently - a single cargo)
					int[] arrivalTimes = new int[5];
					int idx = -1;
					arrivalTimes[++idx] = currentTime;
					Object[] currentSequence = vp.getSequence();
					for (Object obj : currentSequence) {
						if (obj instanceof PortDetails) {
							final PortDetails details = (PortDetails) obj;
							if (idx != (currentSequence.length - 1)) {
								currentTime += details.getVisitDuration();
								arrivalTimes[++idx] = currentTime;
							}
						} else if (obj instanceof VoyageDetails) {
							final VoyageDetails details = (VoyageDetails) obj;
							currentTime += details.getOptions().getAvailableTime();
							arrivalTimes[++idx] = currentTime;
						}
					}

					// 5 is a cargo (load, voyage, discharge, voyage, next port)
					if (currentSequence.length == 5) {
						// Take original ballast details, and recalculate with charter out idle set to true.
						VoyageDetails ballastDetails = (VoyageDetails) currentSequence[3];

						// Preliminary check on voyage suitability.
						{

							// TODO: Grab as an input!
							int threshold = 20;
							// If we go full speed, is there still more than 20 of idle time?

							int availableTime = ballastDetails.getOptions().getAvailableTime();
							int distance = ballastDetails.getOptions().getDistance();
							int maxSpeed = ballastDetails.getOptions().getVessel().getVesselClass().getMaxSpeed();

							int travelTime = Calculator.getTimeFromSpeedDistance(maxSpeed, distance);

							if (((availableTime - travelTime) / 24) < threshold) {
								continue;
							}
						}

						// Need to reproduce P&L calculations here, switching the charter flag on/off on ballast idle.
						// Duplicate all the relevant objects and replay calcs
						VoyageOptions options;
						try {
							options = ballastDetails.getOptions().clone();
						} catch (CloneNotSupportedException e) {
							// Do not expect this, VoyageOptions implements Cloneable
							throw new RuntimeException(e);
						}
						options.setCharterOutIdleTime(true);
						VoyageDetails newDetails = new VoyageDetails();
						voyageCalculator.calculateVoyageFuelRequirements(options, newDetails);

						VoyagePlan newVoyagePlan = new VoyagePlan();

						Object[] newSequence = currentSequence.clone();
						newSequence[3] = newDetails;

						IVessel vessel = options.getVessel();
						voyageCalculator.calculateVoyagePlan(newVoyagePlan, vessel, arrivalTimes, newSequence);

						// Get the new cargo allocation.
						IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vp, arrivalTimes);
						IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, newVoyagePlan, arrivalTimes);

						long originalOption = entityValueCalculator.evaluate(vp, currentAllocation, vessel, null);
						long newOption = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, null);

						// TODO: This should be recorded based on market availability groups and then processed.
						if (originalOption >= newOption) {
							// Keep
						} else {
							// Overwrite details
							voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newSequence);
						}
					}
				}
			}
		}

		// Next we do P&L related business; first we have to assign the load volumes,
		// and then compute the resulting P&L fitness components.

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : loadPriceCalculators) {
			calculator.prepareEvaluation(scheduledSequences);
		}

		// Compute load volumes and prices
		final Collection<IAllocationAnnotation> allocations = cargoAllocator.allocate(scheduledSequences);
		scheduledSequences.setAllocations(allocations);
		// Finally evaluate whole-solution components, like P&L.

		for (final ICargoAllocationFitnessComponent component : allocationComponents) {
			final long l = component.evaluate(scheduledSequences, allocations);
			if (l == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			total += l;
		}

		return total;
	}

	public Collection<ICargoSchedulerFitnessComponent> getFitnessComponents() {
		return fitnessComponents;
	}

	public void setCargoAllocator(final ICargoAllocator cargoAllocator) {
		this.cargoAllocator = cargoAllocator;
	}

	public void setFitnessComponents(final Collection<ICargoSchedulerFitnessComponent> fitnessComponents, final Collection<ICargoAllocationFitnessComponent> allocationComponents) {
		this.fitnessComponents = fitnessComponents;
		this.fitnesses = new long[fitnessComponents.size()];
		this.allocationComponents = allocationComponents;
	}

	public void setLoadPriceCalculators(final Collection<ILoadPriceCalculator> loadPriceCalculators) {
		this.loadPriceCalculators = loadPriceCalculators;
	}

	public void evaluateSchedule(final ScheduledSequences bestResult) {
		final HashSet<IResource> allResources = new HashSet<IResource>();
		for (final ScheduledSequence schedule : bestResult) {
			allResources.add(schedule.getResource());
		}
		vpIterator.iterateSchedulerComponents(fitnessComponents, bestResult, allResources);
	}
}
