/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
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
public class VoyagePlanOptimiser implements IVoyagePlanOptimiser {

	private static final int RELAXATION_STEP = 6;

	private static final Logger log = LoggerFactory.getLogger(VoyagePlanOptimiser.class);

	private final List<IVoyagePlanChoice> choices = new ArrayList<IVoyagePlanChoice>();

	private List<Object> basicSequence;

	private IVessel vessel;

	private int vesselStartTime;

	private int bestProblemCount = Integer.MAX_VALUE;

	private long bestCost = Long.MAX_VALUE;

	private VoyagePlan bestPlan = null;
	
	private long startHeel;

	/**
	 * True iff {@link #bestPlan} meets the requirement that every voyage uses less than or equal to the available time for that voyage
	 */
	private boolean bestPlanFitsInAvailableTime = false;

	private final ILNGVoyageCalculator voyageCalculator;

	@Inject
	public VoyagePlanOptimiser(final ILNGVoyageCalculator voyageCalculator) {
		this.voyageCalculator = voyageCalculator;
	}

	/**
	 * Check internal state is valid (i.e. all setters have been called).
	 */
	@Override
	public void init() {
		if (vessel == null) {
			throw new IllegalStateException("Vessel has not been set");
		}
		if (voyageCalculator == null) {
			throw new IllegalStateException("Voyage Calculator has not been set");
		}
		if (basicSequence == null) {
			throw new IllegalStateException("Basic sequence has not been set");
		}
		if (arrivalTimes == null) {
			throw new IllegalStateException("Arrival times have not been set");
		}
	}

	/**
	 * Reset the state of this object ready for a new optimisation.
	 */
	@Override
	public void reset() {
		choices.clear();
		basicSequence = null;
		bestPlan = null;
		bestPlanFitsInAvailableTime = false;
		bestCost = Long.MAX_VALUE;
		arrivalTimes = null;
	}

	/**
	 * Clean up all references.
	 */
	@Override
	public void dispose() {
		choices.clear();
		vessel = null;
		basicSequence = null;
		bestPlan = null;
	}

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	@Override
	public VoyagePlan optimise() {

		// nonRecursiveRunLoop();
		// System.err.println("==============Optimising voyage plan=============");

		runLoop(0);

		// if (!bestPlanFitsInAvailableTime) {
		// System.err.println("Impossible situation!");
		// }

		return bestPlan;
	}

	private void evaluateVoyagePlan() {
		final PortOptions endOptions = (PortOptions) basicSequence.get(basicSequence.size() - 1);
		final IPortSlot slot = endOptions.getPortSlot();

		if (slot != null) {
			if (slot.getPortType() == PortType.End) {
				final ITimeWindow window = slot.getTimeWindow();

				final int lastArrivalTime = arrivalTimes.get(arrivalTimes.size() - 1);
				final int extraExtent = window == null ? 30 * RELAXATION_STEP : (lastArrivalTime >= window.getEnd() ? 0 : window.getEnd() - lastArrivalTime);

				evaluateVoyagePlan(extraExtent);
				return;
			}
		}

		evaluateVoyagePlan(0);
	}

	/**
	 * Recursive function to iterate through all the possible combinations of {@link IVoyagePlanChoice}s. For each set of choices, calculate a {@link VoyagePlan} and store the cheapest cost plan. The
	 * {@link VoyageOptions} objects will be modified, but cloned into each {@link VoyagePlan} calculated.
	 * 
	 * @param i
	 */
	private final void runLoop(final int i) {
		// Recursive termination point.
		if (i == choices.size()) {
			// Perform voyage calculations and populate plan.
			evaluateVoyagePlan();
		} else {
			// Perform recursive application of choice objects.
			final IVoyagePlanChoice c = choices.get(i);
			for (int ci = 0; ci < c.numChoices(); ++ci) {
				if (c.apply(ci)) {
					runLoop(i + 1);
				}
			}
		}
	}

	/**
	 * Evaluates the current sequences from the current choice set. Optionally, the last voyage can be further optimised to find the best arrival time that minimises cost.
	 * 
	 * @param optimiseLastLeg
	 */
	private void evaluateVoyagePlan(final int timeExtent) {

		int currentProblemCount;
		long cost;
		VoyagePlan currentPlan;

		VoyageOptions optionsToRestore = null;
		int availableTimeToRestore = 0;

		if (timeExtent / RELAXATION_STEP > 0) {

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

			final VoyageOptions options = (VoyageOptions) basicSequence.get(basicSequence.size() - 2);
			final int originalTime = options.getAvailableTime();
			optionsToRestore = options;
			availableTimeToRestore = originalTime;

			VoyagePlan bestLastLegPlan = null;
			long bestLastLegCost = Long.MAX_VALUE;
			int bestLastProblemCount = Short.MAX_VALUE;
			int bestAvailableTime = options.getAvailableTime();

			final ICurve hireRateCurve;
			switch (vessel.getVesselInstanceType()) {
			case SPOT_CHARTER:
				hireRateCurve = vessel.getHourlyCharterInPrice();
				break;
			case TIME_CHARTER:
				hireRateCurve = vessel.getHourlyCharterInPrice();
				break;
			default:
				hireRateCurve = null;
				break;
			}

			// TODO: Turn into a parameter -- probably want this to be longer than slightly over one day - could also scale it to 6/12 hours etc.
			for (int i = 0; i < timeExtent / RELAXATION_STEP; i++) {

				currentPlan = calculateVoyagePlan();

				if (currentPlan != null) {
					final VoyageDetails lastVoyageDetails = (VoyageDetails) (currentPlan.getSequence()[currentPlan.getSequence().length - 2]);

					long currentCost = evaluatePlan(currentPlan);

					// Only add in hire cost for last leg evaluation. It is constant otherwise and does not need to be part of the cost comparison.
					// Hire cost will be properly calculated in a different step.

					// This is not calculator.multiply, because hireRate is not scaled.
					if (hireRateCurve != null) {
						int hireRate = hireRateCurve.getValueAtPoint(vesselStartTime);
						final long hireCost = (long) hireRate * (long) (lastVoyageDetails.getIdleTime() + lastVoyageDetails.getTravelTime());
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
			currentPlan = calculateVoyagePlan();
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
			if (currentPlanFitsInAvailableTime && !bestPlanFitsInAvailableTime) {
				storePlan = true;
				// this plan is valid, or the other is not, and it's cheaper
			} else if (currentPlanFitsInAvailableTime || !bestPlanFitsInAvailableTime) {

				if (currentProblemCount < bestProblemCount) {
					storePlan = true;
				} else if ((currentProblemCount == bestProblemCount) && (cost < bestCost)) {
					storePlan = true;
				}
			}
		}

		if (storePlan) {
			bestPlanFitsInAvailableTime = currentPlanFitsInAvailableTime;
			bestCost = cost;
			bestPlan = currentPlan;
			bestProblemCount = currentProblemCount;

			// We need to ensure the best plan as a set of options which are not
			// changed by further iterations through choices, so lets loop
			// through the plan and replace the voyage details options with
			// cloned ones.
			for (final Object obj : bestPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					final VoyageDetails details = (VoyageDetails) obj;
					// Skip cast check as we created the object in the first
					// place
					final VoyageOptions options = details.getOptions();

					try {
						details.setOptions(options.clone());
					} catch (final CloneNotSupportedException e) {
						// Record error, wrap up and rethrow
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
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
			// System.err.println("Evaluating a plan");
			// for (final Object o : plan.getSequence()) {
			// if (o instanceof VoyageDetails) {
			// final VoyageDetails vd = (VoyageDetails) o;
			// System.err.println("\tvoyage from " + vd.getOptions().getFromPortSlot().getPort().getName() + " to " + vd.getOptions().getToPortSlot().getPort().getName());
			// System.err.println(vd.getOptions());
			// System.err.println("idle:" + vd.getIdleTime() + ", journey" + vd.getTravelTime());
			// for (final FuelComponent fc : FuelComponent.values()) {
			// final long consumption = vd.getFuelConsumption(fc, fc.getDefaultFuelUnit());
			// final long up = vd.getFuelUnitPrice(fc);
			// System.err.println("\t\t" + fc + " = " + consumption + ", " + Calculator.multiply(consumption, up));
			// }
			// }
			// }
		}

		// long revenue = currentPlan.getSalesRevenue() -
		// currentPlan.getSalesRevenue();
		long cost = 0;
		for (final FuelComponent fuel : FuelComponent.values()) {
			cost += plan.getTotalFuelCost(fuel);
		}
		// System.err.println("Fuel Cost = " + cost);
		cost += plan.getTotalRouteCost();
		// System.err.println("Total Cost = " + cost);
		// if (cost < bestCost)
		// System.err.println("Maybe new best ^^");
		// // include cost of hire

		// cost += plan.getTotalHireCost();
		return cost;
	}

	/**
	 */
	private VoyagePlan calculateVoyagePlan() {
		// For each voyage options, calculate new Details.

		final List<Object> currentSequence = voyageCalculator.generateFuelCostCalculatedSequence(basicSequence.toArray());

		final VoyagePlan currentPlan = new VoyagePlan();

		// Calculate voyage plan
		final int feasibility = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, arrivalTimes, currentSequence.toArray());

		if (feasibility >= 0) {
			return currentPlan;
		} else {
			return currentPlan;//null;
		}
	}

	private List<Integer> arrivalTimes;

	@Override
	public void setArrivalTimes(final List<Integer> arrivalTimes) {
		this.arrivalTimes = arrivalTimes;
	}

	/**
	 * Returns the basic sequence that is being optimised over.
	 * 
	 * @return
	 */
	@Override
	public List<Object> getBasicSequence() {
		return basicSequence;
	}

	/**
	 * Sets the basic voyage plan sequence. This should be {@link IPortSlot} instances separated by {@link VoyageOptions} instances implementing {@link Cloneable}. The {@link VoyageOptions} objects
	 * will be modified during optimisation.
	 * 
	 * @param basicSequence
	 */
	@Override
	public void setBasicSequence(final List<Object> basicSequence) {
		this.basicSequence = basicSequence;
	}

	/**
	 * Get the {@link IVessel} to evaluate voyages against.
	 * 
	 * @return
	 */
	@Override
	public IVessel getVessel() {
		return vessel;
	}

	/**
	 * Set the {@link IVessel} to evaluate voyages against.
	 * 
	 * @param vessel
	 * @since 2.0
	 */
	@Override
	public void setVessel(final IVessel vessel, final int vesselStartTime) {
		this.vessel = vessel;
		this.vesselStartTime = vesselStartTime;
	}

	/**
	 * Once optimised, returns the best {@link VoyagePlan} cost.
	 * 
	 * @return
	 */
	@Override
	public long getBestCost() {
		return bestCost;
	}

	/**
	 * Once optimised, returns the best {@link VoyagePlan}.
	 * 
	 * @return
	 */
	@Override
	public VoyagePlan getBestPlan() {
		return bestPlan;
	}

	/**
	 * Returns the {@link ILNGVoyageCalculator} used in the {@link VoyagePlanOptimiser}.
	 * 
	 * @return
	 */
	@Override
	public ILNGVoyageCalculator getVoyageCalculator() {
		return voyageCalculator;
	}

	/**
	 * Add a new choice to the ordered stack of choices. If this choice depends upon the choice of another {@link IVoyagePlanChoice}, then that object should have already been added.
	 * 
	 * @param choice
	 */
	@Override
	public void addChoice(final IVoyagePlanChoice choice) {
		choices.add(choice);
	}

	@Override
	public void setStartHeel(long heelVolumeInM3) {
		startHeel = heelVolumeInM3;
	}

	@Override
	public long getStartHeel() {
		return startHeel;
	}
}