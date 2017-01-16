/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

/**
 * A faster than LP cargo allocator. Does a bit of finagling to make it all work, which isn't hugely pretty. Each cargo is assigned an index by the base class; this class maps ports slots to a single
 * load/discharge sum constraint (i.e. presumes that there cannot be two contractual limits applying to a single slot) using the cargoConstraints array.
 * 
 * Cargoes are then sorted by their unit price, and allocated the minimum of (a) vessel capacity, (b) the remaining load limit and (c) the remaining discharge limit for those cargoes.
 * 
 * TODO has no handling of load lower bounds; need to do a first pass allocating LB to every cargo first.
 * 
 * @author hinton
 * 
 */
public abstract class FastVolumeAllocator extends BaseVolumeAllocator {
//	final Map<IPortSlot, Integer> volumeConstraintMap = new HashMap<IPortSlot, Integer>();
//	final ArrayList<Long> initialConstraintValues = new ArrayList<Long>();
//
//	@Override
//	public void init() {
//		super.init();
//	}
//
//	@Override
//	public void reset() {
//		super.reset();
//		cargoConstraints.clear();
//	}

//	@Override
//	public void addCargo(final VoyagePlan plan, final PortDetails loadDetails, final VoyageDetails ladenLeg, final PortDetails dischargeDetails, final VoyageDetails ballastLeg, final int loadTime,
//			final int dischargeTime, final long requiredLoadVolume, final IVessel vessel) {
//		super.addCargo(plan, loadDetails, ladenLeg, dischargeDetails, ballastLeg, loadTime, dischargeTime, requiredLoadVolume, vessel);
//
//		cargoConstraints.add(new Pair<Integer, Integer>(volumeConstraintMap.get(loadDetails.getOptions().getPortSlot()), volumeConstraintMap.get(dischargeDetails.getOptions().getPortSlot())));
//	}
//
//	final ArrayList<Pair<Integer, Integer>> cargoConstraints = new ArrayList<Pair<Integer, Integer>>();
//
//	@Override
//	protected long[] allocateSpareVolume() {
//		prepareConstraintMaps();
//
//		// sort cargoes by unit cost
//		final Integer[] variables = new Integer[cargoCount];
//		for (int i = 0; i < variables.length; i++) {
//			variables[i] = i;
//		}
//		final Comparator<Integer> comparator = new Comparator<Integer>() {
//			@Override
//			public int compare(final Integer arg0, final Integer arg1) {
//				final double d0 = unitPricesPerM3.get(arg0);
//				final double d1 = unitPricesPerM3.get(arg1);
//
//				if (d0 > d1) {
//					return -1;
//				} else if (d1 < d0) {
//					return 1;
//				} else {
//					return 0;
//				}
//			}
//		};
//
//		Arrays.sort(variables, comparator);
//
//		// set up constraint table
//		final long[] remainders = CollectionsUtil.longsToLongArray(initialConstraintValues);
//
//		final long[] allocations = new long[cargoCount];
//
//		// Now allocate whatever minimum quantity is required for each slot
//
//		for (final int variable : variables) {
//			final Pair<Integer, Integer> constraintIndices = cargoConstraints.get(variable);
//
//			// Firstly, remove the forced load volume from consideration
//			// Since every constraintIndices pair has first = load slot
//			// constraint
//			// second = discharge slot constraint
//			// we can just knock this off the remaining volume at the start.
//			final long fuelVolume = forcedLoadVolumeInM3.get(variable) + remainingHeelVolumeInM3.get(variable);
//			if (constraintIndices.getFirst() != null) {
//				remainders[constraintIndices.getFirst()] -= fuelVolume;
//			}
//
//			// Now we find the lower bound for allocation above the fuel
//			// requirement
//			final ILoadOption loadSlot = loadSlots.get(variable);
//			final IDischargeOption dischargeSlot = dischargeSlots.get(variable);
//
//			// It is, the greater of the minimum load LESS FUEL, and minimum
//			// discharge
//			final long lowerLoadBound = Math.max(loadSlot.getMinLoadVolume() - fuelVolume, dischargeSlot.getMinDischargeVolume());
//
//			// so allocate it
//			allocations[variable] = lowerLoadBound;
//
//			// now remove this from any remainders which apply
//			// TODO check feasibility here
//			if (constraintIndices.getFirst() != null) {
//				remainders[constraintIndices.getFirst()] -= lowerLoadBound;
//			}
//
//			if (constraintIndices.getSecond() != null) {
//				remainders[constraintIndices.getSecond()] -= lowerLoadBound;
//			}
//		}
//
//		// Now run through variables (remember they are sorted in decreasing
//		// unit value)
//		for (final int variable : variables) {
//			final long fuelRequired = forcedLoadVolumeInM3.get(variable) + remainingHeelVolumeInM3.get(variable);
//			final Pair<Integer, Integer> constraintIndices = cargoConstraints.get(variable);
//			// This is however much is left in the load-side summed volume
//			// constraint
//			final long slack1 = constraintIndices.getFirst() == null ? Long.MAX_VALUE : remainders[constraintIndices.getFirst()];
//			// this is what is left in the discharge-side summed volume
//			// constraint
//			final long slack2 = constraintIndices.getSecond() == null ? Long.MAX_VALUE : remainders[constraintIndices.getSecond()];
//
//			// this is what is left from the load-side upper bound
//			// (we subtract the allocation which we enforced in the loop above,
//			// and the fuel load)
//			final long loadSlack = loadSlots.get(variable).getMaxLoadVolume() - (fuelRequired + allocations[variable]);
//
//			// and this is what is left on the discharge side
//			final long dischargeSlack = dischargeSlots.get(variable).getMaxDischargeVolume() - allocations[variable];
//
//			// and finally this is what is left of the vessel cargo capacity
//			final long cargoSlack = vesselCapacityInM3.get(variable) - (fuelRequired + allocations[variable]);
//
//			// the maximum we can allocate here is the minimum of all of these
//			final long allocation = Math.min(Math.min(Math.min(Math.min(loadSlack, dischargeSlack), slack1), slack2), cargoSlack);
//
//			// System.err.println("Load SV Slack:" + slack1);
//			// System.err.println("Discharge SV Slack:" + slack2);
//			//
//			// System.err.println("Load Slot Slack:" + loadSlack);
//			// System.err.println("Discharge Slot Slack:" + dischargeSlack);
//			// System.err.println("Vessel Slack:" + cargoSlack);
//			//
//			// System.err.println("Allocated: " + allocation);
//
//			if (constraintIndices.getFirst() != null) {
//				remainders[constraintIndices.getFirst()] -= allocation;
//			}
//			if (constraintIndices.getSecond() != null) {
//				remainders[constraintIndices.getSecond()] -= allocation;
//			}
//
//			allocations[variable] += allocation;
//		}
//		return allocations;
//	}
//
//	/**
//	 * Set up the mapping from slots to their constraints.
//	 */
//	private void prepareConstraintMaps() {
//		volumeConstraintMap.clear();
//		initialConstraintValues.clear();
//		for (final ITotalVolumeLimit limit : cargoAllocationProvider.getTotalVolumeLimits()) {
//			final int index = initialConstraintValues.size();
//			initialConstraintValues.add(limit.getVolumeLimit());
//
//			for (final IPortSlot slot : limit.getPossibleSlots()) {
//				final Integer time = slotTimes.get(slot);
//				if (time == null) {
//					continue;
//				}
//				final ITimeWindow window = limit.getTimeWindow();
//				if ((window.getStart() <= time.intValue()) && (window.getEnd() >= time.intValue())) {
//					volumeConstraintMap.put(slot, index);
//				}
//			}
//		}
//	}
}
