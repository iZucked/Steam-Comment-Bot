/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * A cargo allocator which presumes that there are no total volume constraints, and so the total remaining capacity should be allocated
 * 
 * @author Tom Hinton
 * @since 6.0
 * 
 */
public class UnconstrainedVolumeAllocator extends BaseVolumeAllocator {

	/**
	 * Returns x, capped by y; if x has the special value 0, it is considered undefined and y is returned.
	 * 
	 * @return
	 */
	private final static long capValueWithZeroDefault(final long x, final long y) {
		return x == 0 ? y : Math.min(x, y);
	}

	/**
	 * Calculates the load / discharge volumes per slot in a cargo, based on the constraints supplied (the heel which has to remain at the end of the cargo, the amount of LNG required for travel, and
	 * the vessel capacity). There may also be constraints placed on the amount which can be discharged or loaded per slot.
	 * 
	 * Assumes that the maximum amount within available constraints will be loaded or discharged at each slot.
	 * 
	 * Currently only handles LDD* cases for multiple load / discharge cargoes.
	 * 
	 * @param constraint
	 * @return
	 * 
	 * @author Simon McGregor
	 */
	@Override
	public IAllocationAnnotation allocate(final AllocationRecord constraint) {
		final List<IPortSlot> slots = constraint.slots;

		final AllocationAnnotation annotation = new AllocationAnnotation();

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);

		final IVessel vessel = constraint.nominatedVessel != null ? constraint.nominatedVessel : constraint.resourceVessel;

		// how much room is there in the tanks?
		final long availableCargoSpace = vessel.getCargoCapacity() - constraint.startVolumeInM3;

		// how much fuel will be required over and above what we start with in the tanks?
		// note: this is the fuel consumption plus any heel quantity required at discharge
		final long fuelDeficit = constraint.requiredFuelVolumeInM3 - constraint.startVolumeInM3;

		// greedy assumption: always load as much as possible
		long loadVolume = capValueWithZeroDefault(constraint.maxVolumes.get(0), availableCargoSpace);
		// violate maximum load volume constraint when it has to be done to fuel the vessel
		if (loadVolume < fuelDeficit) {
			loadVolume = fuelDeficit;
			// we should never be required to load more than the vessel can fit in its tanks
			assert (loadVolume <= availableCargoSpace);
			// TODO: report max load constraint violation
		}

		// Assuming a single cargo CV!
		int cargoCVValue = loadSlot.getCargoCVValue();

		// the amount of LNG available for discharge
		long unusedVolume = loadVolume + constraint.startVolumeInM3 - constraint.minEndVolumeInM3 - constraint.requiredFuelVolumeInM3;

		// available volume is non-negative
		assert (unusedVolume >= 0);

		// load / discharge case
		// this is subsumed by the LDD* case, but can be done more efficiently by branching instead of looping
		if (slots.size() == 2) {

			final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);

			// greedy assumption: always discharge as much as possible
			final long dischargeVolume = capValueWithZeroDefault(constraint.maxVolumes.get(1), unusedVolume);
			annotation.setSlotVolumeInM3(dischargeSlot, dischargeVolume);
			unusedVolume -= dischargeVolume;

		}
		// multiple load/discharge case
		else {
			// TODO: this only handles LDD* cases

			// track which discharge slot is the most profitable
			// final int[] prices = constraint.slotPricesPerM3;
			final int mostProfitableDischargeIndex = 1;

			// assign the minimum amount per discharge slot
			for (int i = 1; i < slots.size(); i++) {
				final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(i);
				final long minDischargeVolume = constraint.minVolumes.get(i);

				// assign the minimum amount per discharge slot
				final long dischargeVolume;
				if (unusedVolume >= minDischargeVolume) {
					dischargeVolume = minDischargeVolume;
				} else {
					dischargeVolume = unusedVolume;
				}
				annotation.setSlotVolumeInM3(dischargeSlot, dischargeVolume);
				unusedVolume -= dischargeVolume;

				// more profitable ?
				// if (i > 1 && prices[i] > prices[mostProfitableDischargeIndex]) {
				// mostProfitableDischargeIndex = i;
				// }
			}

			final int nDischargeSlots = slots.size() - 1;

			// now, starting with the most profitable discharge slot, allocate
			// any remaining volume
			for (int i = 0; i < nDischargeSlots && unusedVolume > 0; i++) {
				// start at the most profitable slot and cycle through them in order
				// TODO: would be better to sort them by profitability, but needs to be done efficiently
				final int index = 1 + ((i + (mostProfitableDischargeIndex - 1)) % nDischargeSlots);

				final IDischargeOption slot = (IDischargeOption) slots.get(index);
				// discharge all remaining volume at this slot, up to the different in slot maximum and minimum
				final long volume = Math.min(constraint.maxVolumes.get(index) - constraint.minVolumes.get(index), unusedVolume);
				// reduce the remaining available volume
				unusedVolume -= volume;
				final long currentVolumeInM3 = annotation.getSlotVolumeInM3(slot);
				annotation.setSlotVolumeInM3(slot, currentVolumeInM3 + volume);
			}

			// Note this currently does nothing as the next() method in the allocator iterator (BaseCargoAllocator) ignores this data and looks directly on the discharge slot.
		}

		/*
		 * Under certain circumstances, the remaining heel may be more than expected: for instance, when a minimum load constraint far exceeds a maximum discharge constraint. This may cause problems
		 * with restrictions on where LNG can be shipped, or with profit share contracts, so at present a conservative assumption in the LNGVoyageCalculator treats it as a capacity violation and
		 * assumes that the load has to be reduced below its min value constraint.
		 */

		// if there is any leftover volume after discharge
		if (constraint.preferShortLoadOverLeftoverHeel && unusedVolume > 0) {
			// we use a conservative heuristic: load exactly as much as we need, subject to constraints
			final long revisedLoadVolume = Math.max(0, loadVolume - unusedVolume);

			// TODO: report min load constraint violation if necessary

			unusedVolume -= loadVolume - revisedLoadVolume;
			loadVolume = revisedLoadVolume;
			/*
			 * TODO: if the max discharge volume would leave excess heel, the correct load volume decision depends on relative prices at this load and the next, and whether carrying over excess heel
			 * is contractually permissible (and CV-compatible with the next load port).
			 */
		}

		annotation.setSlotVolumeInM3(loadSlot, loadVolume);

		annotation.setStartHeelVolumeInM3(constraint.startVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(constraint.minEndVolumeInM3 + unusedVolume);
		annotation.setFuelVolumeInM3(constraint.requiredFuelVolumeInM3);

		// Copy across slot time information
		for (int i = 0; i < slots.size(); i++) {
			IPortSlot slot = constraint.slots.get(i);
			annotation.getSlots().add(slot);
			annotation.setSlotTime(slot, constraint.slotTimes.get(i));
			annotation.setSlotVolumeInMMBTu(slot, Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot), cargoCVValue));
		}

		return annotation;
	}
}
