package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VoyagePlanOptimiser<T> {

	private final List<IVoyagePlanChoice> choices = new ArrayList<IVoyagePlanChoice>();

	private List<Object> basicSequence;

	private IVessel vessel;

	private long bestCost = Long.MAX_VALUE;

	private IVoyagePlan bestPlan = null;

	private ILNGVoyageCalculator<T> voyageCalculator;

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

	public void reset() {
		choices.clear();
		basicSequence = null;
		bestPlan = null;
		bestCost = Long.MAX_VALUE;
	}

	public void dispose() {
		choices.clear();
		vessel = null;
		voyageCalculator = null;
		basicSequence = null;
		bestPlan = null;
	}

	public IVoyagePlan optimise() {

		runLoop(0);

		return bestPlan;
	}

	private void runLoop(final int i) {

		if (i == choices.size()) {
			// Perform voyage calculations and populate plan.

			// For each voyage options, calculate new Details.

			final List<Object> currentSequence = new ArrayList<Object>(
					basicSequence.size());

			for (final Object element : basicSequence) {

				if (element instanceof VoyageOptions) {
					final VoyageOptions options = (VoyageOptions) element;

					final VoyageDetails<T> voyageDetails = new VoyageDetails<T>();
					// voyageDetails.setStartTime(currentTime);
					try {
						voyageDetails.setOptions((IVoyageOptions) options.clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
		} else {

			final IVoyagePlanChoice c = choices.get(i);
			for (int ci = 0; ci < c.numChoices(); ++ci) {
				if (c.apply(ci)) {
					runLoop(i + 1);
				}
			}
		}
	}

	public List<Object> getBasicSequence() {
		return basicSequence;
	}

	public void setBasicSequence(final List<Object> basicSequence) {
		this.basicSequence = basicSequence;
	}

	public IVessel getVessel() {
		return vessel;
	}

	public void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	public long getBestCost() {
		return bestCost;
	}

	public IVoyagePlan getBestPlan() {
		return bestPlan;
	}

	public ILNGVoyageCalculator<T> getVoyageCalculator() {
		return voyageCalculator;
	}

	public void setVoyageCalculator(
			final ILNGVoyageCalculator<T> voyageCalculator) {
		this.voyageCalculator = voyageCalculator;
	}

	public void addChoice(final IVoyagePlanChoice choice) {
		choices.add(choice);
	}
}