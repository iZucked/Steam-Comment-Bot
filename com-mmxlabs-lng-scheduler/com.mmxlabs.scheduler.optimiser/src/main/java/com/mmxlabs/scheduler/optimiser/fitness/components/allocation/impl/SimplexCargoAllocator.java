/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.Set;

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

/**
 * A cargo allocator which uses the simplex algorithm.
 * 
 * @author hinton
 * 
 */
public class SimplexCargoAllocator<T> extends BaseCargoAllocator<T> {
	final LinearOptimizer optimizer = new SimplexSolver();
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
	 * #solve()
	 */
	@Override
	protected long[] allocateSpareVolume() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.mmxlabs.scheduler.optimiser.fitness.components.allocation.
		 * ICargoAllocator#solve()
		 */

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
		// for each gas year independently.

		// set multi-cargo constraints (the real point of this optimiser).
		for (final Pair<Long, Set<IPortSlot>> yearlyLimit : cargoAllocationProvider
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
			double[] point = solution.getPointRef();// this is the
													// load/discharge allocation
													// for each cargo
			
			final long[] result = new long[point.length];
			for (int i = 0; i<point.length; i++) {
				result[i] = (long) point[i];
			}
			return result;
		} catch (OptimizationException e) {
			// presumably we set some impossible constraints. shouldn't happen!
			e.printStackTrace();
			return null;
		}
	}

}
