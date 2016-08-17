/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link VoyagePlanOptimiser} performs an exhaustive search through the choices in a {@link VoyagePlan}. {@link IVoyagePlanChoice} implementations are provided in a set order which edit the
 * voyage plan objects. TODO: Develop unit tests.
 * 
 * @author Simon Goodall
 * 
 */
// @PerChainUnitScope
public class VoyagePlanOptimiser implements IVoyagePlanOptimiser {

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	private static final int RELAXATION_STEP = 6;

	public static class Record {

		public Record(@Nullable IResource resource, @NonNull IVessel vessel, long startHeel, int baseFuelPricePerMT, long vesselCharterInRatePerDay, IPortTimesRecord portTimesRecord,
				List<@NonNull IOptionsSequenceElement> basicSequence, List<@NonNull IVoyagePlanChoice> choices) {
			this.resource = resource;
			this.vessel = vessel;
			this.startHeelInM3 = startHeel;
			this.baseFuelPricePerMT = baseFuelPricePerMT;
			this.vesselCharterInRatePerDay = vesselCharterInRatePerDay;
			this.portTimesRecord = portTimesRecord;
			this.basicSequence = basicSequence;
			this.choices = choices;
		}

		public final List<@NonNull IVoyagePlanChoice> choices;// = new ArrayList<>();

		public final List<@NonNull IOptionsSequenceElement> basicSequence;

		public final IPortTimesRecord portTimesRecord;

		public final IVessel vessel;

		public final int baseFuelPricePerMT;

		public final long vesselCharterInRatePerDay;
		public final long startHeelInM3;

		public final @Nullable IResource resource;
	}

	static class InternalState {
		public int bestProblemCount = Integer.MAX_VALUE;

		public long bestCost = Long.MAX_VALUE;

		public @Nullable VoyagePlan bestPlan = null;
		/**
		 * True iff {@link #bestPlan} meets the requirement that every voyage uses less than or equal to the available time for that voyage
		 */
		public boolean bestPlanFitsInAvailableTime = false;
	}

	private final ILNGVoyageCalculator voyageCalculator;

	@Inject
	public VoyagePlanOptimiser(final ILNGVoyageCalculator voyageCalculator) {
		this.voyageCalculator = voyageCalculator;
	}

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	@Override
	public VoyagePlan optimise(IResource resource, IVessel vessel, long startHeel, int baseFuelPricePerMT, long vesselCharterInRatePerDay, IPortTimesRecord portTimesRecord,
			List<@NonNull IOptionsSequenceElement> basicSequence, List<@NonNull IVoyagePlanChoice> choices) {

		Record record = new Record(resource, vessel, startHeel, baseFuelPricePerMT, vesselCharterInRatePerDay, portTimesRecord, basicSequence, choices);

		InternalState state = new InternalState();
		runLoop(record, state, 0);
		return state.bestPlan;
	}

	private void evaluateVoyagePlan(Record record, InternalState state) {
		final PortOptions endOptions = (PortOptions) record.basicSequence.get(record.basicSequence.size() - 1);
		final IPortSlot slot = endOptions.getPortSlot();

		if (slot != null) {
			if (slot.getPortType() == PortType.End) {
				ITimeWindow window = slot.getTimeWindow();
				IResource resource = record.resource;
				if (resource != null) {
					final IEndRequirement requirement = startEndRequirementProvider.getEndRequirement(resource);
					if (requirement != null) {
						if (requirement.isOpen()) {
							evaluateVoyagePlan(record, state, 0);
							return;
						}
						window = requirement.getTimeWindow();
					}
				}

				final int lastArrivalTime = record.portTimesRecord.getSlotTime(slot);
				final int extraExtent = window == null ? 30 * RELAXATION_STEP : (lastArrivalTime >= window.getExclusiveEnd() ? 0 : window.getExclusiveEnd() - lastArrivalTime);

				// If this is non-zero then our end event rules will have kicked in and we should not engage the speed step code.

				evaluateVoyagePlan(record, state, 0);
				return;
			}
		}

		evaluateVoyagePlan(record, state, 0);
	}

	/**
	 * Recursive function to iterate through all the possible combinations of {@link IVoyagePlanChoice}s. For each set of choices, calculate a {@link VoyagePlan} and store the cheapest cost plan. The
	 * {@link VoyageOptions} objects will be modified, but cloned into each {@link VoyagePlan} calculated.
	 * 
	 * @param i
	 */
	private final void runLoop(Record record, InternalState state, final int i) {

		// Recursive termination point.
		if (i == record.choices.size()) {
			// Perform voyage calculations and populate plan.
			evaluateVoyagePlan(record, state);
		} else {
			// Perform recursive application of choice objects.
			final IVoyagePlanChoice c = record.choices.get(i);
			for (int ci = 0; ci < c.numChoices(); ++ci) {
				if (c.apply(ci)) {
					runLoop(record, state, i + 1);
				}
			}
		}
	}

	/**
	 * Evaluates the current sequences from the current choice set. Optionally, the last voyage can be further optimised to find the best arrival time that minimises cost.
	 * 
	 * @param optimiseLastLeg
	 */
	private void evaluateVoyagePlan(Record record, InternalState state, final int timeExtent) {

		int currentProblemCount;
		long cost;
		VoyagePlan currentPlan;

		VoyageOptions optionsToRestore = null;
		int availableTimeToRestore = 0;

		if (timeExtent / RELAXATION_STEP > 0 && record.basicSequence.size() > 1) {

			// There are some cases where we wish to evaluate the best time to
			// end the sequence, rather than the specified value. Typically
			// this will be because no end date has been set and the specified
			// time will just be the quickest time to get between ports. Here we
			// increase the available time and pick the cheapest cost.

			// There are two elements to this implementation which should be
			// considered further.
			// 1) We break out at the first sign of a cost increase. This
			// assumes there is no local minima/maxima which may not hold true.
			// 2) We limit the number of iterations to avoid potential infinite
			// loops should we never get a cost value to compare against.
			// However this may miss potential cheaper solutions past this
			// boundary

			final VoyageOptions options = (VoyageOptions) record.basicSequence.get(record.basicSequence.size() - 2);
			final int originalTime = options.getAvailableTime();
			optionsToRestore = options;
			availableTimeToRestore = originalTime;

			VoyagePlan bestLastLegPlan = null;
			long bestLastLegCost = Long.MAX_VALUE;
			int bestLastProblemCount = Short.MAX_VALUE;
			int bestAvailableTime = options.getAvailableTime();

			// TODO: Turn into a parameter -- probably want this to be longer than slightly over one day - could also scale it to 6/12 hours etc.
			for (int i = 0; i < timeExtent / RELAXATION_STEP; i++) {

				currentPlan = calculateVoyagePlan(record);

				if (currentPlan != null) {
					final VoyageDetails lastVoyageDetails = (VoyageDetails) (currentPlan.getSequence()[currentPlan.getSequence().length - 2]);

					long currentCost = evaluatePlan(currentPlan);

					// Only add in hire cost for last leg evaluation. It is constant otherwise and does not need to be part of the cost comparison.
					// Hire cost will be properly calculated in a different step.

					// This is not calculator.multiply, because hireRate is not scaled.
					{
						final long hireRatePerDay = currentPlan.getCharterInRatePerDay();
						final long hireCost = hireRatePerDay * (long) (lastVoyageDetails.getIdleTime() + lastVoyageDetails.getTravelTime()) / 24;
						currentCost += hireCost;
					}

					// Check for violations, prefer solutions with fewer violations
					currentProblemCount = currentPlan.getViolationsCount();

					if (currentProblemCount < bestLastProblemCount || (currentProblemCount == bestLastProblemCount && currentCost < bestLastLegCost)) {
						bestLastLegCost = currentCost;
						bestLastLegPlan = currentPlan;
						bestLastProblemCount = currentProblemCount;
						bestAvailableTime = options.getAvailableTime();
					}
					//
					// if (currentCost > lastCost) {
					// // // back out one step. this is ugly.
					// // options.setAvailableTime(options.getAvailableTime() - 1);
					// // TODO: This is not really all that good.
					// // break; // presume minimum.
					// } else {
					// lastCost = currentCost;
					// }
				}
				options.setAvailableTime(options.getAvailableTime() + RELAXATION_STEP);
			}
			options.setAvailableTime(bestAvailableTime);
			currentProblemCount = bestLastProblemCount;
			cost = bestLastLegCost;
			currentPlan = bestLastLegPlan;
		} else {
			// Fall back to a single evaluation assuming final voyage options
			// are good
			currentPlan = calculateVoyagePlan(record);
			cost = evaluatePlan(currentPlan);
			if (currentPlan != null) {
				currentProblemCount = currentPlan.getViolationsCount();
			} else {
				currentProblemCount = Integer.MAX_VALUE;
			}
		}

		// this way could be cheaper, but we need to add in a sanity check
		// it may be because of a route choice decision, which could
		// have made the plan use more than the available time; plans which
		// use more than the available time are definitely worse than plans
		// which don't, even if they are cheaper so we do a check here to
		// determine whether the plan is OK in that respect

		/**
		 * True iff the current plan ensures that every voyage fits in the available time for that voyage.
		 */
		boolean currentPlanFitsInAvailableTime = true;
		if (currentPlan == null) {
			currentPlanFitsInAvailableTime = false;
		} else {
			for (final Object obj : currentPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) obj;

					if ((details.getTravelTime() + details.getIdleTime()) > details.getOptions().getAvailableTime()) {
						// this plan is bad. If the old plan was not bad, we
						// should stick with the old plan even though this one
						// costs less. If the old plan was bad, we might as well
						// go with it
						currentPlanFitsInAvailableTime = false;
						break;
					}
				}
			}
		}

		boolean storePlan = false;
		// Store cheapest cost, but take into account time or capacity problems
		if (currentPlan != null) {

			// this plan is valid, but the other is not, who cares about cost
			if (currentPlanFitsInAvailableTime && !state.bestPlanFitsInAvailableTime) {
				storePlan = true;
				// this plan is valid, or the other is not, and it's cheaper
			} else if (currentPlanFitsInAvailableTime || !state.bestPlanFitsInAvailableTime) {

				if (currentProblemCount < state.bestProblemCount) {
					storePlan = true;
				} else if ((currentProblemCount == state.bestProblemCount) && (cost < state.bestCost)) {
					storePlan = true;
				}
			}
		}

		if (storePlan) {

			assert currentPlan != null;
			state.bestPlanFitsInAvailableTime = currentPlanFitsInAvailableTime;
			state.bestCost = cost;
			state.bestPlan = currentPlan;
			state.bestProblemCount = currentProblemCount;

			// We need to ensure the best plan as a set of options which are not
			// changed by further iterations through choices, so lets loop
			// through the plan and replace the voyage details options with
			// cloned ones.
			for (final Object obj : currentPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) obj;
					// Skip cast check as we created the object in the first
					// place
					final VoyageOptions options = details.getOptions();
					details.setOptions(options.clone());
				}
			}
		}
		if (optionsToRestore != null) {
			optionsToRestore.setAvailableTime(availableTimeToRestore);
		}
	}

	public long evaluatePlan(final VoyagePlan plan) {
		if (plan == null) {
			return Long.MAX_VALUE;
		}

		long cost = 0;
		for (final FuelComponent fuel : FuelComponent.values()) {
			cost += plan.getTotalFuelCost(fuel);
		}

		cost += plan.getTotalRouteCost();
		return cost;
	}

	/**
	 */
	private VoyagePlan calculateVoyagePlan(Record record) {
		// For each voyage options, calculate new Details.

		final List<IDetailsSequenceElement> currentSequence = voyageCalculator.generateFuelCostCalculatedSequence(record.basicSequence.toArray(new IOptionsSequenceElement[0]));

		final VoyagePlan currentPlan = new VoyagePlan();
		currentPlan.setCharterInRatePerDay(record.vesselCharterInRatePerDay);

		// Calculate voyage plan
		voyageCalculator.calculateVoyagePlan(currentPlan, record.vessel, record.startHeelInM3, record.baseFuelPricePerMT, record.portTimesRecord,
				currentSequence.toArray(new IDetailsSequenceElement[0]));

		return currentPlan;
	}
}