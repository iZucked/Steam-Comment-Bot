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
 * are provided in a set order which edit the voyage plan objects.
 * TODO: Extract interface. Develop unit tests. 
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
		for (IVoyagePlanChoice c : choices) {
			if (c.reset() == false) {
				return; //handle error properly.
			}
		}
		final int cc = choices.size();
		if (cc == 0) {
			evaluateVoyagePlan();
			return;
		}
		while (true) {
			evaluateVoyagePlan();
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
	private void runLoop(final int i) {
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

	private void evaluateVoyagePlan() {
		// For each voyage options, calculate new Details.

		final List<Object> currentSequence = new ArrayList<Object>(
				basicSequence.size());

		for (final Object element : basicSequence) {

			// Loop through basic elements, calculating voyage requirements
			// to build up basic voyage plan details.
			if (element instanceof VoyageOptions) {
				final VoyageOptions options = (VoyageOptions) element;

				final VoyageDetails<T> voyageDetails = new VoyageDetails<T>();
				// Clone options so further iterations through choices do
				// not change the options stored in these voyage details.
				// This could be made more efficient by only cloning when
				// the plan is the cheapest found so far.
				try {
					voyageDetails.setOptions((IVoyageOptions) options
							.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

		// long revenue = currentPlan.getSalesRevenue() -
		// currentPlan.getSalesRevenue();
		long cost = 0;
		for (final FuelComponent fuel : FuelComponent.values()) {
			cost += currentPlan.getTotalFuelCost(fuel);
		}

		// Store cheapest cost
		if (cost < bestCost) {
			bestCost = cost;
			bestPlan = currentPlan;
		}
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