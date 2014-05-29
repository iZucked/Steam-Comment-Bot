/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;

/**
 * A cargo allocator which presumes that there are no total volume constraints, and so the total remaining capacity should be allocated
 * 
 * @author Tom Hinton
 * 
 */
public class UnconstrainedVolumeAllocator extends BaseVolumeAllocator {

	@Inject
	private IActualsDataProvider actualsDataProvider;

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
	 * @param allocationRecord
	 * @return
	 * 
	 * @author Simon McGregor
	 */
	@Override
	public IAllocationAnnotation allocate(final AllocationRecord allocationRecord) {
		final List<IPortSlot> slots = allocationRecord.slots;

		final AllocationAnnotation annotation = new AllocationAnnotation();

		final IVessel vessel = allocationRecord.nominatedVessel != null ? allocationRecord.nominatedVessel : allocationRecord.resourceVessel;
		if (allocationRecord.allocationMode == AllocationMode.Actuals) {

			// Work out used fuel volume - adjust for heel positions and transfer volumes
			// Actuals data will give us;
			// * Heel before loading
			// * Volume Loaded
			// * Volume discharged.
			// * Heel after discharge.
			// * If the next load is actualised, take the heel before loading, otherwise assume safety heel.
			long usedFuelVolume = 0;
			IPortSlot lastSlot = null;
			for (int i = 0; i < slots.size(); i++) {
				final IPortSlot slot = allocationRecord.slots.get(i);
				if (actualsDataProvider.hasActuals(slot) == false) {
					throw new IllegalStateException("Actuals Volume Mode, but no actuals specified");
				}

				annotation.getSlots().add(slot);

				// Scheduler should have made this happen, but lets make sure here
				assert allocationRecord.slotTimes.get(i) == actualsDataProvider.getArrivalTime(slot);
				annotation.setSlotTime(slot, allocationRecord.slotTimes.get(i));

				// Actuals mode, take values directly
				annotation.setSlotVolumeInM3(slot, actualsDataProvider.getVolumeInM3(slot));
				annotation.setSlotVolumeInMMBTu(slot, actualsDataProvider.getVolumeInMMBtu(slot));
				annotation.setSlotCargoCV(slot, actualsDataProvider.getCVValue(slot));

				// First slot
				if (i == 0) {
					// The Volume Planner should have correctly set this value
					assert allocationRecord.startVolumeInM3 == actualsDataProvider.getStartHeelInM3(slot);
					annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
					usedFuelVolume += allocationRecord.startVolumeInM3;
				}
				if (slot instanceof ILoadOption) {
					usedFuelVolume += actualsDataProvider.getVolumeInM3(slot);
				} else if (slot instanceof IDischargeOption) {
					usedFuelVolume -= actualsDataProvider.getVolumeInM3(slot);
					lastSlot = slot;
				}
			}

			final IPortSlot returnSlot = allocationRecord.returnSlot;

			final long returnSlotHeelInM3;
			if (lastSlot != null && actualsDataProvider.hasReturnActuals(lastSlot)) {
				returnSlotHeelInM3 = actualsDataProvider.getReturnHeelInM3(lastSlot);
			} else if (actualsDataProvider.hasActuals(returnSlot)) {
				returnSlotHeelInM3 = actualsDataProvider.getStartHeelInM3(returnSlot);
			} else {
				// Use safety heel if not actualised
				returnSlotHeelInM3 = vessel.getVesselClass().getMinHeel();
			}

			usedFuelVolume -= returnSlotHeelInM3;

			annotation.setRemainingHeelVolumeInM3(returnSlotHeelInM3);
			annotation.setFuelVolumeInM3(usedFuelVolume);

			// break out before we get to the m3 to mmbtu calcs which would overwrite the actuals data
			return annotation;
		}

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);
		// Assuming a single cargo CV!
		final int cargoCVValue = loadSlot.getCargoCVValue();

		if (allocationRecord.allocationMode == AllocationMode.Transfer) {
			// Transfer, just find the common max and replicate
			final long availableCargoSpace = vessel.getCargoCapacity() - allocationRecord.startVolumeInM3;
			long transferVolume = availableCargoSpace;
			for (int i = 0; i < slots.size(); ++i) {
				transferVolume = capValueWithZeroDefault(allocationRecord.maxVolumes.get(i), transferVolume);

			}
			for (int i = 0; i < slots.size(); ++i) {
				annotation.setSlotVolumeInM3(slots.get(i), transferVolume);
				annotation.setSlotCargoCV(slots.get(i), cargoCVValue);
			}
		} else {
			assert allocationRecord.allocationMode == AllocationMode.Shipped;
			// how much room is there in the tanks?
			final long availableCargoSpace = vessel.getCargoCapacity() - allocationRecord.startVolumeInM3;

			// how much fuel will be required over and above what we start with in the tanks?
			// note: this is the fuel consumption plus any heel quantity required at discharge
			final long fuelDeficit = allocationRecord.requiredFuelVolumeInM3 - allocationRecord.startVolumeInM3;

			// greedy assumption: always load as much as possible
			long loadVolume = capValueWithZeroDefault(allocationRecord.maxVolumes.get(0), availableCargoSpace);
			// violate maximum load volume constraint when it has to be done to fuel the vessel
			if (loadVolume < fuelDeficit) {
				loadVolume = fuelDeficit;
				// we should never be required to load more than the vessel can fit in its tanks
//				assert (loadVolume <= availableCargoSpace);
			}

			// the amount of LNG available for discharge
			long unusedVolume = loadVolume + allocationRecord.startVolumeInM3 - allocationRecord.minEndVolumeInM3 - allocationRecord.requiredFuelVolumeInM3;

			// available volume is non-negative
			assert (unusedVolume >= 0);

			// load / discharge case
			// this is subsumed by the LDD* case, but can be done more efficiently by branching instead of looping
			if (slots.size() == 2) {

				final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);

				// greedy assumption: always discharge as much as possible
				final long dischargeVolume = capValueWithZeroDefault(allocationRecord.maxVolumes.get(1), unusedVolume);
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
					final long minDischargeVolume = allocationRecord.minVolumes.get(i);

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
					final long volume = Math.min(allocationRecord.maxVolumes.get(index) - allocationRecord.minVolumes.get(index), unusedVolume);
					// reduce the remaining available volume
					unusedVolume -= volume;
					final long currentVolumeInM3 = annotation.getSlotVolumeInM3(slot);
					annotation.setSlotVolumeInM3(slot, currentVolumeInM3 + volume);
				}

				// Note this currently does nothing as the next() method in the allocator iterator (BaseCargoAllocator) ignores this data and looks directly on the discharge slot.
			}

			/*
			 * Under certain circumstances, the remaining heel may be more than expected: for instance, when a minimum load constraint far exceeds a maximum discharge constraint. This may cause
			 * problems with restrictions on where LNG can be shipped, or with profit share contracts, so at present a conservative assumption in the LNGVoyageCalculator treats it as a capacity
			 * violation and assumes that the load has to be reduced below its min value constraint.
			 */

			// if there is any leftover volume after discharge
			if (allocationRecord.preferShortLoadOverLeftoverHeel && unusedVolume > 0) {
				// we use a conservative heuristic: load exactly as much as we need, subject to constraints
				final long revisedLoadVolume = Math.max(0, loadVolume - unusedVolume);

				// TODO: report min load constraint violation if necessary

				unusedVolume -= loadVolume - revisedLoadVolume;
				loadVolume = revisedLoadVolume;
				/*
				 * TODO: if the max discharge volume would leave excess heel, the correct load volume decision depends on relative prices at this load and the next, and whether carrying over excess
				 * heel is contractually permissible (and CV-compatible with the next load port).
				 */
			}

			annotation.setSlotVolumeInM3(loadSlot, loadVolume);
			annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
			annotation.setRemainingHeelVolumeInM3(allocationRecord.minEndVolumeInM3 + unusedVolume);
			annotation.setFuelVolumeInM3(allocationRecord.requiredFuelVolumeInM3);
		}

		// Copy across slot time information
		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			annotation.getSlots().add(slot);
			annotation.setSlotTime(slot, allocationRecord.slotTimes.get(i));
			annotation.setSlotVolumeInMMBTu(slot, Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot), cargoCVValue));
		}

		return annotation;
	}
}
