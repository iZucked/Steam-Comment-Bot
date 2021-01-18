/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link VoyagePlanOptimiser} performs an exhaustive search through the choices in a {@link VoyagePlan}. {@link IVoyagePlanChoice} implementations are provided in a set order which edit the
 * voyage plan objects.
 * 
 * @author Simon Goodall
 * 
 */
public class VoyagePlanOptimiser implements IVoyagePlanOptimiser {

	public static class Record {

		public Record(@Nullable final IResource resource, @NonNull final IVessel vessel, final long[] startHeelRangeInM3, final int[] baseFuelPricesPerMT,
				final ICharterCostCalculator charterCostCalculator, final IPortTimesRecord portTimesRecord, final List<@NonNull IOptionsSequenceElement> basicSequence,
				final List<@NonNull IVoyagePlanChoice> choices) {
			this.resource = resource;
			this.vessel = vessel;
			this.startHeelRangeInM3 = startHeelRangeInM3;
			this.baseFuelPricesPerMT = baseFuelPricesPerMT;
			this.charterCostCalculator = charterCostCalculator;
			this.portTimesRecord = portTimesRecord;
			this.basicSequence = basicSequence;
			this.choices = choices;
		}

		public final List<@NonNull IVoyagePlanChoice> choices;

		public final List<@NonNull IOptionsSequenceElement> basicSequence;

		public final IPortTimesRecord portTimesRecord;

		public final IVessel vessel;

		public final int[] baseFuelPricesPerMT;

		public final ICharterCostCalculator charterCostCalculator;
		public final long[] startHeelRangeInM3;

		public final @Nullable IResource resource; // May be null for notional voyage calculations
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
	public VoyagePlan optimise(final IResource resource, final IVessel vessel, final long[] startHeelRangeInM3, final int[] baseFuelPricesPerMT, final ICharterCostCalculator charterCostCalculator,
			final IPortTimesRecord portTimesRecord, final List<@NonNull IOptionsSequenceElement> basicSequence, final List<@NonNull IVoyagePlanChoice> choices) {

		final Record record = new Record(resource, vessel, startHeelRangeInM3, baseFuelPricesPerMT, charterCostCalculator, portTimesRecord, basicSequence, choices);

		final InternalState state = new InternalState();
		runLoop(record, state, 0);
		return state.bestPlan;
	}

	@Override
	public void iterate(@Nullable final IResource resource, @NonNull final IVessel vessel, final long @NonNull [] heelVolumeRangeInM3, final int @NonNull [] baseFuelPricesPerMT,
			final ICharterCostCalculator charterCostCalculator, @NonNull final IPortTimesRecord portTimesRecord, @NonNull final List<@NonNull IOptionsSequenceElement> basicSequence,
			@NonNull final List<@NonNull IVoyagePlanChoice> choices, final Consumer<VoyagePlan> hook) {
		final Record record = new Record(resource, vessel, heelVolumeRangeInM3, baseFuelPricesPerMT, charterCostCalculator, portTimesRecord, basicSequence, choices);

		runLoopWithHook(record, 0, hook);

	}

	/**
	 * Recursive function to iterate through all the possible combinations of {@link IVoyagePlanChoice}s. For each set of choices, calculate a {@link VoyagePlan} and store the cheapest cost plan. The
	 * {@link VoyageOptions} objects will be modified, but cloned into each {@link VoyagePlan} calculated.
	 * 
	 * @param i
	 */
	private final void runLoopWithHook(final Record record, final int i, final Consumer<VoyagePlan> hook) {

		// Recursive termination point.
		if (i == record.choices.size()) {
			final InternalState state = new InternalState();
			// Perform voyage calculations and populate plan.
			evaluateVoyagePlan(record, state);
			hook.accept(state.bestPlan);
		} else {
			// Perform recursive application of choice objects.
			final IVoyagePlanChoice c = record.choices.get(i);
			for (int ci = 0; ci < c.numChoices(); ++ci) {
				if (c.apply(ci)) {
					runLoopWithHook(record, i + 1, hook);
				}
			}
		}
	}

	/**
	 * Recursive function to iterate through all the possible combinations of {@link IVoyagePlanChoice}s. For each set of choices, calculate a {@link VoyagePlan} and store the cheapest cost plan. The
	 * {@link VoyageOptions} objects will be modified, but cloned into each {@link VoyagePlan} calculated.
	 * 
	 * @param i
	 */
	private final void runLoop(final Record record, final InternalState state, final int i) {

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
	 * Evaluates the current sequences from the current choice set.
	 */
	private void evaluateVoyagePlan(final Record record, final InternalState state) {

		int currentProblemCount;

		// Fall back to a single evaluation assuming final voyage options
		// are good
		final VoyagePlan currentPlan = calculateVoyagePlan(record);
		final long cost = evaluatePlan(currentPlan);
		if (currentPlan != null) {
			currentProblemCount = currentPlan.getViolationsCount();
		} else {
			currentProblemCount = Integer.MAX_VALUE;
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
			for (final IDetailsSequenceElement obj : currentPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) obj;

					if ((details.getTravelTime() + details.getIdleTime() + details.getPurgeDuration()) //
							> details.getOptions().getAvailableTime()) {
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
			for (final IDetailsSequenceElement obj : currentPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) obj;
					// Skip cast check as we created the object in the first
					// place
					final VoyageOptions options = details.getOptions();
					details.setOptions(options.copy());
				}
			}
		}
	}

	public long evaluatePlan(final VoyagePlan plan) {
		if (plan == null) {
			return Long.MAX_VALUE;
		}

		long cost = plan.getBaseFuelCost() + plan.getCooldownCost() + plan.getLngFuelCost();
		cost += plan.getStartHeelCost();

		cost += plan.getTotalRouteCost();
		return cost;
	}

	/**
	 */
	private @Nullable VoyagePlan calculateVoyagePlan(final Record record) {
		// For each voyage options, calculate new Details.

		final List<IDetailsSequenceElement> currentSequence = voyageCalculator.generateFuelCostCalculatedSequence(record.basicSequence.toArray(new IOptionsSequenceElement[0]));

		final VoyagePlan currentPlan = new VoyagePlan();

		// Calculate voyage plan
		final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, record.vessel, record.charterCostCalculator, record.startHeelRangeInM3, record.baseFuelPricesPerMT,
				record.portTimesRecord, currentSequence.toArray(new IDetailsSequenceElement[0]));

		if (violationCount == Integer.MAX_VALUE) {
			return null;
		}

		return currentPlan;
	}
}