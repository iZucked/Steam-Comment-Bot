/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.LinearOptimizer;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * A utility class for allocating the limited total load/discharge capacity to
 * different cargoes. Uses the simplex solver from Apache Commons Math.
 * 
 * It may be possible to solve this in a specialised way, rather than as a plain
 * LP - given only non-intersecting constraints on the discharge side, for
 * example, a greedy algorithm would probably work (sort by unit price and
 * allocate to the most valuable cargo first, and so on). However, allowing
 * total load constraints, especially with the possibility of disconnected load
 * and discharge sides, means that the constraints are not orthogonal and the
 * solution becomes less obvious.
 * 
 * @author hinton
 * 
 */
public final class CargoAllocator<T> {
	/**
	 * The linear optimiser
	 */
	final LinearOptimizer optimizer = new SimplexSolver();
	final double[] unitPrices = new double[] {};

	// TODO index these types
	final Map<IPortSlot, Integer> variableTable = new HashMap<IPortSlot, Integer>();

	@SuppressWarnings("unchecked")
	final Pair<ILoadSlot, IDischargeSlot> cargoes[] = new Pair[] {};

	int cargoIndex = 0;

	ICargoAllocationProvider<T> cargoAllocationProvider;
	private long[] forcedLoadVolume;
	private long[] vesselCapacity;

	public CargoAllocator() {
		super();
	}

	public void init() {

		if (cargoAllocationProvider == null) {
			throw new RuntimeException("Cargo allocation provider must be set");
		}
	}

	public void reset() {
		cargoIndex = 0;
		Arrays.fill(unitPrices, 0);
		variableTable.clear();
	}

	/**
	 * Notify the allocator of a cargo to be included in the allocation. This
	 * method will probably be called in a loop iterating over a sequence. It
	 * allows for quite a lot of flexibility in the outer optimisation, so load
	 * and discharge slots can be freely connected up without causing problems.
	 * 
	 * This might loose a little performance in the solver, as no constraints
	 * can be kept between runs.
	 * 
	 * @param loadDetails
	 *            The details of the load slot
	 * @param dischargeDetails
	 *            details of the paired discharge slot
	 * @param loadTime
	 *            time at which loading happens
	 * @param dischargeTime
	 *            time at which discharge happens
	 * @param requiredLoadVolume
	 *            the volume of LNG which must be loaded to meet fuel
	 *            requirements for the voyage
	 * @param vesselCapacity
	 *            the capacity of the vessel to which this cargo has been
	 *            allocated
	 */
	public void addCargo(final PortDetails loadDetails,
			final PortDetails dischargeDetails, final int loadTime,
			final int dischargeTime, final long requiredLoadVolume,
			final long vesselCapacity) {

		final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getPortSlot();
		final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails
				.getPortSlot();

		cargoes[cargoIndex].setBoth(loadSlot, dischargeSlot);

		// store the current cargo index (variable index in the LP) so that we
		// can reverse-lookup from slots to LP variables
		final Integer ci = cargoIndex;
		variableTable.put(loadSlot, ci);
		variableTable.put(dischargeSlot, ci);

		// We have to load this much LNG no matter what
		forcedLoadVolume[cargoIndex] = requiredLoadVolume;

		this.vesselCapacity[cargoIndex] = vesselCapacity;

		// TODO price curve support here. This is what loadTime and
		// dischargeTime are for.
		unitPrices[cargoIndex] = dischargeSlot.getSalesPrice()
				- loadSlot.getPurchasePrice();

		cargoIndex++;
	}

	public void solve() {
		final ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		final int variableCount = cargoIndex;

		// set simple constraints (max/min load/discharge volumes at slots)
		int index = 0;
		for (final Pair<ILoadSlot, IDischargeSlot> cargo : cargoes) {
			final ILoadSlot load = cargo.getFirst();
			final IDischargeSlot discharge = cargo.getSecond();

			// constrain this variable. unfortunately a bit ugly.
			final double[] selector = new double[variableCount];
			selector[index] = 1;

			// the most we can load here (above forced load quantity) is
			// the least of:
			// * the maximum load volume (less forced load)
			// * the maximum discharge volume
			// * the capacity remaining after forced load quantity is loaded

			final double upperBound = Math.min(Math.min(load.getMaxLoadVolume()
					- forcedLoadVolume[index],
					discharge.getMaxDischargeVolume()), vesselCapacity[index]
					- forcedLoadVolume[index]);

			final LinearConstraint upperBoundConstraint = new LinearConstraint(
					selector, Relationship.LEQ, upperBound);

			// the least we can load here (above FLQ) is
			// the most of :
			// * the minimum load above FLQ
			// * the minimum required discharge
			final double lowerBound = Math.max(load.getMinLoadVolume()
					- forcedLoadVolume[index],
					discharge.getMinDischargeVolume());

			final LinearConstraint lowerBoundConstraint = new LinearConstraint(
					selector, Relationship.GEQ, lowerBound);
			constraints.add(upperBoundConstraint);
			constraints.add(lowerBoundConstraint);
			index++;
		}

		// TODO think about how gas-year constraints work; really these should
		// be handled by the next level up, which can just call the allocator
		// for each gas year independently

		// set multi-cargo constraints (the real point of this optimiser).
		for (final Pair<Integer, Collection<IPortSlot>> yearlyLimit : cargoAllocationProvider
				.getCargoAllocationLimits()) {
			final double[] selector = new double[variableCount];

			// this is the upper bound for all these slots
			double quantity = yearlyLimit.getFirst();

			// go through all the slots in this limit
			for (final IPortSlot slot : yearlyLimit.getSecond()) {
				// map the slot to a variable (because the load/discharge
				// quantities are paired up
				final int variable = variableForSlot(slot);
				if (variable == -1)
					continue; // this slot is not included (wrong gas year?)
				selector[variable] = 1;

				if (slot instanceof ILoadSlot) {
					// decrease the right hand side by the amount we have been
					// forced to load for fuel.

					quantity -= forcedLoadVolume[variable];
				}
			}

			final LinearConstraint summedCargoConstraint = new LinearConstraint(
					selector, Relationship.LEQ, quantity);

			constraints.add(summedCargoConstraint);
		}

		final LinearObjectiveFunction objective = new LinearObjectiveFunction(
				unitPrices, 0);

		try {
			final RealPointValuePair solution = optimizer.optimize(objective,
					constraints, GoalType.MAXIMIZE, false);
			@SuppressWarnings("unused")
			double[] point = solution.getPointRef();// this is the
													// load/discharge allocation
													// for each cargo
			// allocations are stored in this solution. Get them out for display
		} catch (OptimizationException e) {
			// presumably we set some impossible constraints. shouldn't happen!
			e.printStackTrace();
		}
	}

	/**
	 * Get the variable associated with this slot (load or discharge) in the
	 * current run.
	 * 
	 * @param slot
	 * @return
	 */
	private final int variableForSlot(final IPortSlot slot) {
		final Integer i = variableTable.get(slot);
		return i == null ? -1 : i.intValue();
	}
}
