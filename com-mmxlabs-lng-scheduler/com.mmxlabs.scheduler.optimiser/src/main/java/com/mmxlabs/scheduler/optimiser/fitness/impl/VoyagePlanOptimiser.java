package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link VoyagePlanOptimiser} performs an exhaustive search through the
 * choices in a {@link IVoyagePlan}. {@link IVoyagePlanChoice} implementations
 * are provided in a set order which edit the voyage plan objects. TODO: Develop
 * unit tests.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class VoyagePlanOptimiser<T> implements IVoyagePlanOptimiser<T> {

	private final List<IVoyagePlanChoice> choices = new ArrayList<IVoyagePlanChoice>();

	private List<Object> basicSequence;

	private IVessel vessel;

	private long bestCost = Long.MAX_VALUE;

	private IVoyagePlan bestPlan = null;

	private ILNGVoyageCalculator<T> voyageCalculator;

	/**
	 * Check internal state is valid (i.e. all setters have been called).
	 */
	@Override
	public void init() {
		if (vessel == null) {
			throw new IllegalStateException("Vessel has not been set");
		}
		if (voyageCalculator == null) {
			throw new IllegalStateException(
					"Voyage Calculator has not been set");
		}
		if (basicSequence == null) {
			throw new IllegalStateException("Basic sequence has not been set");
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
		bestCost = Long.MAX_VALUE;
	}

	/**
	 * Clean up all references.
	 */
	@Override
	public void dispose() {
		choices.clear();
		vessel = null;
		voyageCalculator = null;
		basicSequence = null;
		bestPlan = null;
	}

	/**
	 * Optimise the voyage plan
	 * 
	 * @return
	 */
	@Override
	public IVoyagePlan optimise() {

		nonRecursiveRunLoop();
//		runLoop(0);

		return bestPlan;
	}

	private void nonRecursiveRunLoop() {
		for (final IVoyagePlanChoice c : choices) {
			if (c.reset() == false) {
				return; //handle error properly.
			}
		}
		final int cc = choices.size();
		if (cc == 0) {
			evaluateVoyagePlan(false);
			return;
		}
		while (true) {
			evaluateVoyagePlan(false);
			int i;
			carry:
				for (i = 0; i< cc ; i++) {
				if (choices.get(i).nextChoice() == false) {
					break carry;
				}
			}
			if (i == cc-1) {
				return;
			}
		}
	}

	/**
	 * Recursive function to iterate through all the possible combinations of
	 * {@link IVoyagePlanChoice}s. For each set of choices, calculate a
	 * {@link IVoyagePlan} and store the cheapest cost plan. The
	 * {@link IVoyageOptions} objects will be modified, but cloned into each
	 * {@link IVoyagePlan} calculated.
	 * 
	 * @param i
	 */
	private final void runLoop(final int i) {
		// Recursive termination point.
		if (i == choices.size()) {
			// Perform voyage calculations and populate plan.
			evaluateVoyagePlan(false);
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
	 * Evaluates the current sequences from the current choice set. Optionally,
	 * the last voyage can be further optimised to find the best arrival time
	 * that miminises cost.
	 * 
	 * @param optimiseLastLeg
	 */
	private void evaluateVoyagePlan(final boolean optimiseLastLeg) {

		long cost;
		VoyagePlan currentPlan;

		if (optimiseLastLeg) {

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

			final VoyageOptions options = (VoyageOptions) basicSequence
					.get(basicSequence.size() - 2);
			final int originalTime = options.getAvailableTime();
			currentPlan = null;
			final long bestCost = Long.MAX_VALUE;

			// Arbitarily picked number of steps
			for (int i = 0; i < 500; ++i) {
				// Increase available time
				options.setAvailableTime(originalTime + i);
				// Evaluate plan with new time
				currentPlan = calculateVoyagePlan();
				// Is plan valid?
				if (currentPlan != null) {
					final long currentCost = evaluatePlan(currentPlan);
					// Has cost increased over previous best (i.e. last
					// iteration)
					if (currentCost > bestCost) {
						// Lets assume cost always increases from this point on
						// and terminate the evaluation
						// If we do not have this condition, i.e. evaluate all
						// loop iterations, we will need to store and clone best
						// plan/options
						break;
					}
				}
			}
			cost = bestCost;
		} else {
			// Fall back to a single evaluation assuming final voyage options
			// are good
			currentPlan = calculateVoyagePlan();
			cost = evaluatePlan(currentPlan);
		}

		// Store cheapest cost
		if (cost < bestCost) {
			bestCost = cost;
			bestPlan = currentPlan;

			// We need to ensure the best plan as a set of options which are not
			// changed by further iterations through choices, so lets loop
			// through the plan and replace the voyage details options with
			// cloned ones.
			for (final Object obj : bestPlan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					@SuppressWarnings("unchecked")
					final VoyageDetails<T> details = (VoyageDetails<T>) obj;
					// Skip cast check as we created the object in the first
					// place
					final VoyageOptions options = (VoyageOptions) details
							.getOptions();

					try {
						details.setOptions(options.clone());
					} catch (final CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public long evaluatePlan(final VoyagePlan plan) {

		// long revenue = currentPlan.getSalesRevenue() -
		// currentPlan.getSalesRevenue();
		long cost = 0;
		for (final FuelComponent fuel : FuelComponent.values()) {
			cost += plan.getTotalFuelCost(fuel);
		}
		return cost;
	}

	/**
	 */
	private VoyagePlan calculateVoyagePlan() {
		// For each voyage options, calculate new Details.

		final List<Object> currentSequence = new ArrayList<Object>(
				basicSequence.size());

		for (final Object element : basicSequence) {

			// Loop through basic elements, calculating voyage requirements
			// to build up basic voyage plan details.
			if (element instanceof VoyageOptions) {
				final VoyageOptions options = (VoyageOptions) element;

				final VoyageDetails<T> voyageDetails = new VoyageDetails<T>();
				voyageDetails.setOptions(options);

				// Calculate voyage cost
				voyageCalculator.calculateVoyageFuelRequirements(options,
						voyageDetails);
				currentSequence.add(voyageDetails);
			} else {
				currentSequence.add(element);
			}
		}

		final VoyagePlan currentPlan = new VoyagePlan();

		// Calculate voyage plan
		voyageCalculator.calculateVoyagePlan(currentPlan, vessel,
				currentSequence.toArray());

		return currentPlan;
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
	 * Sets the basic voyage plan sequence. This should be {@link IPortSlot}
	 * instances separated by {@link IVoyageOptions} instances implementing
	 * {@link Cloneable}. The {@link IVoyageOptions} objects will be modified
	 * during optimisation.
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
	 */
	@Override
	public void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	/**
	 * Once optimised, returns the best {@link IVoyagePlan} cost.
	 * 
	 * @return
	 */
	@Override
	public long getBestCost() {
		return bestCost;
	}

	/**
	 * Once optimised, returns the best {@link IVoyagePlan}.
	 * 
	 * @return
	 */
	@Override
	public IVoyagePlan getBestPlan() {
		return bestPlan;
	}

	/**
	 * Returns the {@link ILNGVoyageCalculator} used in the
	 * {@link VoyagePlanOptimiser}.
	 * 
	 * @return
	 */
	@Override
	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return voyageCalculator;
	}

	/**
	 * Set the {@link ILNGVoyageCalculator} to use.
	 * 
	 * @param voyageCalculator
	 */
	@Override
	public void setVoyageCalculator(
			final ILNGVoyageCalculator<T> voyageCalculator) {
		this.voyageCalculator = voyageCalculator;
	}

	/**
	 * Add a new choice to the ordered stack of choices. If this choice depends
	 * upon the choice of another {@link IVoyagePlanChoice}, then that object
	 * should have already been added.
	 * 
	 * @param choice
	 */
	@Override
	public void addChoice(final IVoyagePlanChoice choice) {
		choices.add(choice);
	}
}