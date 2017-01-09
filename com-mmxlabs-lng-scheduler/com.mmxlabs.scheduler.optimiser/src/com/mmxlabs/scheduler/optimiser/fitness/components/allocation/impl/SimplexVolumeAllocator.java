/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;


/**
 * A cargo allocator which uses the simplex algorithm.
 * 
 * @author hinton
 * 
 */
public class SimplexVolumeAllocator {// extends BaseCargoAllocator {
// final LinearOptimizer optimizer = new SimplexSolver();
// {
// optimizer.setMaxIterations(1000);
// }
// private final static double SCALE = 10000;
//
// /*
// * (non-Javadoc)
// *
// * @see
// * com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator
// * #solve()
// */
// @Override
// protected long[] allocateSpareVolume() {
// final ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
// // set simple constraints (max/min load/discharge volumes at slots)
//
// for (int index = 0; index < cargoCount; index++) {
// final ILoadSlot load = loadSlots.get(index);
// final IDischargeSlot discharge = dischargeSlots.get(index);
//
// // constrain this variable. unfortunately a bit ugly.
// final double[] selector = new double[cargoCount];
// selector[index] = 1;
//
// // the most we can load here (above forced load quantity) is
// // the least of:
// // * the maximum load volume (less forced load)
// // * the maximum discharge volume
// // * the capacity remaining after forced load quantity is loaded
// final long flv = forcedLoadVolume.get(index);
// final long remainingVesselCapacity = vesselCapacity.get(index)
// - flv;
// final long remainingLoadSlotCapacity = load.getMaxLoadVolume()
// - flv;
//
// final long dischargeCapacity = discharge.getMaxDischargeVolume();
//
// final double upperBound = (double) (Math.min(Math.min(
// remainingLoadSlotCapacity, remainingVesselCapacity),
// dischargeCapacity));
//
// final LinearConstraint upperBoundConstraint = new LinearConstraint(
// selector, Relationship.LEQ, upperBound / SCALE);
//
// // the least we can load here (above FLQ) is
// // the most of :
// // * the minimum load above FLQ
// // * the minimum required discharge
// // final double lowerBound = 1;
// final double lowerBound = Math.max(load.getMinLoadVolume() - flv,
// discharge.getMinDischargeVolume());
//
// final LinearConstraint lowerBoundConstraint = new LinearConstraint(
// selector, Relationship.GEQ, lowerBound / SCALE);
// constraints.add(upperBoundConstraint);
// constraints.add(lowerBoundConstraint);
//
//
// }
//
// // set multi-cargo constraints
// for (final ITotalVolumeLimit limit : cargoAllocationProvider
// .getTotalVolumeLimits()) {
// final double[] selector = new double[cargoCount];
//
// // this is the upper bound for all these slots
// double quantity = limit.getVolumeLimit();
//
// // go through all the slots in this limit
// for (final IPortSlot slot : limit.getPossibleSlots()) {
// // test whether the slot is in the associated period.
// final int time = slotTimes.get(slot);
// final ITimeWindow window = limit.getTimeWindow();
// if (window.getStart() <= time && window.getEnd() >= time) {
// // map the slot to a variable (because the load/discharge
// // quantities are paired up
// final int variable = variableForSlot(slot);
// if (variable == -1)
// continue; // this slot is not included (optional slot,
// // or something)
// selector[variable] = 1;
//
// if (slot instanceof ILoadSlot) {
// // decrease the right hand side by the amount we have
// // been
// // forced to load for fuel.
//
// quantity -= forcedLoadVolume.get(variable);
// }
// }
// }
//
// final LinearConstraint summedCargoConstraint = new LinearConstraint(
// selector, Relationship.LEQ, quantity / SCALE);
//
// constraints.add(summedCargoConstraint);
// }
//
// final double[] unitPricesArray = new double[cargoCount];
// int index = 0;
// for (int unitPrice : unitPrices)
// unitPricesArray[index++] = unitPrice / SCALE;
// final LinearObjectiveFunction objective = new LinearObjectiveFunction(
// unitPricesArray, 0);
//
// try {
// final RealPointValuePair solution = optimizer.optimize(objective,
// constraints, GoalType.MAXIMIZE, true);
// double[] point = solution.getPointRef();// this is the
// // load/discharge allocation
// // for each cargo
//
// final long[] result = new long[point.length];
// for (int i = 0; i < point.length; i++) {
// result[i] = (long) (point[i] * SCALE);
// }
// return result;
// } catch (OptimizationException e) {
// // presumably we set some impossible constraints. shouldn't happen!
// e.printStackTrace();
// return null;
// }
// }

}
