/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;

/**
 * A {@link IVolumeAllocator} implementation that checks (estimated) P&L and decided whether to full load (normal mode) or min load (for cargoes which are loosing money).
 * 
 * @author Simon Goodall
 *
 */
public class MinMaxUnconstrainedVolumeAllocator extends UnconstrainedVolumeAllocator {

	@Inject
	@NotCaching
	private Provider<IEntityValueCalculator> entityValueCalculatorProvider;

	@Override
	protected @NonNull AllocationAnnotation calculateTransferMode(final AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);

		// Note: Calculations in MMBTU
		final long startVolumeInMMBTu;
		final long vesselCapacityInMMBTu;

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);
		// Assuming a single cargo CV!
		final int defaultCargoCVValue = loadSlot.getCargoCVValue();
		// Convert startVolume and vesselCapacity into MMBTu
		if (defaultCargoCVValue > 0) {
			if (allocationRecord.startVolumeInM3 != Long.MAX_VALUE) {
				startVolumeInMMBTu = Calculator.convertM3ToMMBTu(allocationRecord.startVolumeInM3, defaultCargoCVValue);
			} else {
				startVolumeInMMBTu = Long.MAX_VALUE;
			}
			if (vessel.getCargoCapacity() != Long.MAX_VALUE) {
				vesselCapacityInMMBTu = Calculator.convertM3ToMMBTu(vessel.getCargoCapacity(), defaultCargoCVValue);
			} else {
				vesselCapacityInMMBTu = Long.MAX_VALUE;
			}
		} else {
			startVolumeInMMBTu = 0;
			vesselCapacityInMMBTu = Long.MAX_VALUE;
		}
		final long availableCargoSpaceMMBTu = vesselCapacityInMMBTu - startVolumeInMMBTu;

		long maxTransferVolumeMMBTu = availableCargoSpaceMMBTu;
		long maxTransferVolumeM3 = -1;
		{
			for (int i = 0; i < slots.size(); ++i) {
				final long newMinTransferVolumeMMBTU = capValueWithZeroDefault(allocationRecord.maxVolumesInMMBtu.get(i), maxTransferVolumeMMBTu);
				maxTransferVolumeM3 = getUnroundedM3TransferVolume(allocationRecord, slots, maxTransferVolumeMMBTu, maxTransferVolumeM3, i, newMinTransferVolumeMMBTU, false);
				maxTransferVolumeMMBTu = newMinTransferVolumeMMBTU;
			}
		}
		setTransferVolume(allocationRecord, slots, annotation, maxTransferVolumeMMBTu, maxTransferVolumeM3);
		final long maxTransferPNL = entityValueCalculatorProvider.get()
				.evaluate(EvaluationMode.Estimate, allocationRecord.resourceVoyagePlan, annotation, allocationRecord.vesselAvailability, allocationRecord.vesselStartTime, null, null).getSecond();

		long minTransferVolumeMMBTu = 0;
		long minTransferVolumeM3 = -1;
		{
			for (int i = 0; i < slots.size(); ++i) {
				final long newMinTransferVolumeMMBTU = Math.max(allocationRecord.minVolumesInMMBtu.get(i), minTransferVolumeMMBTu);
				minTransferVolumeM3 = getUnroundedM3TransferVolume(allocationRecord, slots, minTransferVolumeMMBTu, minTransferVolumeM3, i, newMinTransferVolumeMMBTU, true);
				minTransferVolumeMMBTu = newMinTransferVolumeMMBTU;
			}
			// Max sure lower bound is not higher than the upper bound
			minTransferVolumeM3 = Math.min(minTransferVolumeM3, maxTransferVolumeM3);
			minTransferVolumeMMBTu = Math.min(minTransferVolumeMMBTu, maxTransferVolumeMMBTu);
		}

		setTransferVolume(allocationRecord, slots, annotation, minTransferVolumeMMBTu, minTransferVolumeM3);

		final long minTransferPNL = entityValueCalculatorProvider.get()
				.evaluate(EvaluationMode.Estimate, allocationRecord.resourceVoyagePlan, annotation, allocationRecord.vesselAvailability, allocationRecord.vesselStartTime, null, null).getSecond();

		if (maxTransferPNL >= minTransferPNL) {
			setTransferVolume(allocationRecord, slots, annotation, maxTransferVolumeMMBTu, maxTransferVolumeM3);
		} else {
			// No need to re-call this.
			// setTransferVolume(allocationRecord, slots, annotation, minTransferVolumeMMBTu, minTransferVolumeM3);
		}

		return annotation;
	}

	@Override
	protected @NonNull AllocationAnnotation calculateShippedMode(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
		final IEntityValueCalculator entityValueCalculator = entityValueCalculatorProvider.get();

		final AllocationAnnotation minAnnotation = calculateShippedMode_MinVolumes(allocationRecord, slots, vessel);

		final long minTransferPNL = entityValueCalculator
				.evaluate(EvaluationMode.Estimate, allocationRecord.resourceVoyagePlan, minAnnotation, allocationRecord.vesselAvailability, allocationRecord.vesselStartTime, null, null).getSecond();

		final AllocationAnnotation maxAnnotation = calculateShippedMode_MaxVolumes(allocationRecord, slots, vessel);
		final long maxTransferPNL = entityValueCalculator
				.evaluate(EvaluationMode.Estimate, allocationRecord.resourceVoyagePlan, maxAnnotation, allocationRecord.vesselAvailability, allocationRecord.vesselStartTime, null, null).getSecond();

		if (maxTransferPNL >= minTransferPNL) {
			return maxAnnotation;
		} else {
			return minAnnotation;
		}
	}

	protected @NonNull AllocationAnnotation calculateShippedMode_MaxVolumes(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final IVessel vessel) {

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);

		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);

		assert allocationRecord.allocationMode == AllocationMode.Shipped;
		int cargoCV = annotation.getSlotCargoCV(loadSlot);

		// how much room is there in the tanks?
		final long availableCargoSpaceInMMBTU = Calculator.convertM3ToMMBTu(vessel.getCargoCapacity() - allocationRecord.startVolumeInM3, cargoCV);

		// how much fuel will be required over and above what we start with in the tanks?
		// note: this is the fuel consumption plus any heel quantity required at discharge
		final long fuelDeficitInMMBTU = Calculator.convertM3ToMMBTu(allocationRecord.requiredFuelVolumeInM3 - allocationRecord.startVolumeInM3 + allocationRecord.minEndVolumeInM3, cargoCV);

		// greedy assumption: always load as much as possible
		long loadVolumeInMMBTU = capValueWithZeroDefault(allocationRecord.maxVolumesInMMBtu.get(0), availableCargoSpaceInMMBTU);
		// violate maximum load volume constraint when it has to be done to fuel the vessel
		if (loadVolumeInMMBTU < fuelDeficitInMMBTU) {
			loadVolumeInMMBTU = fuelDeficitInMMBTU;
			// we should never be required to load more than the vessel can fit in its tanks
			// assert (loadVolume <= availableCargoSpace);
		}

		// the amount of LNG available for discharge
		long unusedVolumeInMMBTU = loadVolumeInMMBTU
				+ Calculator.convertM3ToMMBTu(allocationRecord.startVolumeInM3 - allocationRecord.minEndVolumeInM3 - allocationRecord.requiredFuelVolumeInM3, cargoCV);

		// available volume is non-negative
		assert (unusedVolumeInMMBTU >= 0);

		// load / discharge case
		// this is subsumed by the LDD* case, but can be done more efficiently by branching instead of looping
		if (slots.size() == 2) {

			final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);

			// greedy assumption: always discharge as much as possible
			final long dischargeVolumeInMMBTU = capValueWithZeroDefault(allocationRecord.maxVolumesInMMBtu.get(1), unusedVolumeInMMBTU);
			annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;

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
				final long minDischargeVolumeInMMBTU = allocationRecord.minVolumesInMMBtu.get(i);

				// assign the minimum amount per discharge slot
				final long dischargeVolumeInMMBTU;
				if (unusedVolumeInMMBTU >= minDischargeVolumeInMMBTU) {
					dischargeVolumeInMMBTU = minDischargeVolumeInMMBTU;
				} else {
					dischargeVolumeInMMBTU = unusedVolumeInMMBTU;
				}
				annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
				unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;

				// more profitable ?
				// if (i > 1 && prices[i] > prices[mostProfitableDischargeIndex]) {
				// mostProfitableDischargeIndex = i;
				// }
			}

			final int nDischargeSlots = slots.size() - 1;

			// now, starting with the most profitable discharge slot, allocate
			// any remaining volume
			for (int i = 0; i < nDischargeSlots && unusedVolumeInMMBTU > 0; i++) {
				// start at the most profitable slot and cycle through them in order
				// TODO: would be better to sort them by profitability, but needs to be done efficiently
				final int index = 1 + ((i + (mostProfitableDischargeIndex - 1)) % nDischargeSlots);

				final IDischargeOption slot = (IDischargeOption) slots.get(index);
				// discharge all remaining volume at this slot, up to the different in slot maximum and minimum
				final long volumeInMMBTU = Math.min(allocationRecord.maxVolumesInMMBtu.get(index) - allocationRecord.minVolumesInMMBtu.get(index), unusedVolumeInMMBTU);
				// reduce the remaining available volume
				unusedVolumeInMMBTU -= volumeInMMBTU;
				final long currentVolumeInMMBTU = annotation.getSlotVolumeInMMBTu(slot);
				annotation.setSlotVolumeInMMBTu(slot, currentVolumeInMMBTU + volumeInMMBTU);
			}

			// Note this currently does nothing as the next() method in the allocator iterator (BaseCargoAllocator) ignores this data and looks directly on the discharge slot.
		}

		/*
		 * Under certain circumstances, the remaining heel may be more than expected: for instance, when a minimum load constraint far exceeds a maximum discharge constraint. This may cause problems
		 * with restrictions on where LNG can be shipped, or with profit share contracts, so at present a conservative assumption in the LNGVoyageCalculator treats it as a capacity violation and
		 * assumes that the load has to be reduced below its min value constraint.
		 */

		// if there is any leftover volume after discharge
		if (allocationRecord.preferShortLoadOverLeftoverHeel && unusedVolumeInMMBTU > 0) {
			// we use a conservative heuristic: load exactly as much as we need, subject to constraints
			final long revisedLoadVolumeInMMBTU = Math.max(0, loadVolumeInMMBTU - unusedVolumeInMMBTU);

			// TODO: report min load constraint violation if necessary

			unusedVolumeInMMBTU -= loadVolumeInMMBTU - revisedLoadVolumeInMMBTU;
			loadVolumeInMMBTU = revisedLoadVolumeInMMBTU;
			/*
			 * TODO: if the max discharge volume would leave excess heel, the correct load volume decision depends on relative prices at this load and the next, and whether carrying over excess heel
			 * is contractually permissible (and CV-compatible with the next load port).
			 */
		}

		annotation.setSlotVolumeInMMBTu(loadSlot, loadVolumeInMMBTU);
		annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(allocationRecord.minEndVolumeInM3 + Calculator.convertMMBTuToM3(unusedVolumeInMMBTU, cargoCV));
		annotation.setFuelVolumeInM3(allocationRecord.requiredFuelVolumeInM3);

		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			// annotation.setSlotVolumeInMMBTu(slot, Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot), annotation.getSlotCargoCV(slot)));
			annotation.setSlotVolumeInM3(slot, Calculator.convertMMBTuToM3(annotation.getSlotVolumeInMMBTu(slot), annotation.getSlotCargoCV(slot)));

		}
		return annotation;
	}

	protected @NonNull AllocationAnnotation calculateShippedMode_MinVolumes(final AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
		assert allocationRecord.allocationMode == AllocationMode.Shipped;

		// Only support LD cargoes
		assert slots.size() == 2;

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);

		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);
		int cargoCV = annotation.getSlotCargoCV(loadSlot);

		// how much room is there in the tanks?
		final long availableCargoSpaceInMMBTU = Calculator.convertM3ToMMBTu(vessel.getCargoCapacity() - allocationRecord.startVolumeInM3, cargoCV);

		// how much fuel will be required over and above what we start with in the tanks?
		// note: this is the fuel consumption plus any heel quantity required at discharge
		final long fuelDeficitInMMBTU = Calculator.convertM3ToMMBTu(allocationRecord.requiredFuelVolumeInM3 - allocationRecord.startVolumeInM3 + allocationRecord.minEndVolumeInM3, cargoCV);

		// greedy assumption: always load as much as possible
		long loadVolumeInMMBTU = Math.min(allocationRecord.minVolumesInMMBtu.get(0), availableCargoSpaceInMMBTU);
		// violate maximum load volume constraint when it has to be done to fuel the vessel
		if (loadVolumeInMMBTU < fuelDeficitInMMBTU) {
			loadVolumeInMMBTU = fuelDeficitInMMBTU;
			// we should never be required to load more than the vessel can fit in its tanks
			// assert (loadVolume <= availableCargoSpace);
		}

		// the amount of LNG available for discharge
		long unusedVolumeInMMBTU = loadVolumeInMMBTU
				+ Calculator.convertM3ToMMBTu(allocationRecord.startVolumeInM3 - allocationRecord.minEndVolumeInM3 - allocationRecord.requiredFuelVolumeInM3, cargoCV);

		// available volume is non-negative
		assert (unusedVolumeInMMBTU >= 0);

		final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);
		{
			// greedy assumption: always discharge as much as possible
			final long dischargeVolumeInMMBTU = allocationRecord.minVolumesInMMBtu.get(1);
			annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;
		}

		// Increase load volume to meet min discharge.
		if (unusedVolumeInMMBTU < 0) {
			loadVolumeInMMBTU += -unusedVolumeInMMBTU;
			unusedVolumeInMMBTU = 0;
		} else {
			// Increase discharge to meet min load
			// greedy assumption: always discharge as much as possible
			long currentDischargeVolumeInMMBTU = allocationRecord.minVolumesInMMBtu.get(1);
			long dischargeVolumeInMMBTU = Math.min(currentDischargeVolumeInMMBTU + unusedVolumeInMMBTU, allocationRecord.maxVolumesInMMBtu.get(1));
			annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			// Adjust unused volume for new discharge volume
			unusedVolumeInMMBTU += currentDischargeVolumeInMMBTU;
			unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;
			loadVolumeInMMBTU -= unusedVolumeInMMBTU;
		}

		// Check load caps - vessel capacity
		if (loadVolumeInMMBTU > availableCargoSpaceInMMBTU) {
			final long diff = loadVolumeInMMBTU - availableCargoSpaceInMMBTU;

			long dischargeVolumeInMMBTU = annotation.getSlotVolumeInMMBTu(dischargeSlot);
			dischargeVolumeInMMBTU -= diff;
			annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			loadVolumeInMMBTU = availableCargoSpaceInMMBTU;
		}
		// Check load caps - max load
		if (loadVolumeInMMBTU > allocationRecord.maxVolumesInMMBtu.get(1)) {
			final long diff = loadVolumeInMMBTU - allocationRecord.maxVolumesInMMBtu.get(1);

			long dischargeVolumeInMMBTU = annotation.getSlotVolumeInMMBTu(dischargeSlot);
			dischargeVolumeInMMBTU -= diff;
			annotation.setSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			loadVolumeInMMBTU = allocationRecord.maxVolumesInMMBtu.get(1);
		}

		// Finally check for negative discharge volumes
		final long dischargeVolumeInMMBTU = annotation.getSlotVolumeInMMBTu(dischargeSlot);
		if (dischargeVolumeInMMBTU < 0) {
			final long diff = -dischargeVolumeInMMBTU;
			loadVolumeInMMBTU += diff;
			annotation.setSlotVolumeInMMBTu(dischargeSlot, 0);
		}

		annotation.setSlotVolumeInMMBTu(loadSlot, loadVolumeInMMBTU);

		annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(allocationRecord.minEndVolumeInM3 + Calculator.convertMMBTuToM3(unusedVolumeInMMBTU, cargoCV));
		annotation.setFuelVolumeInM3(allocationRecord.requiredFuelVolumeInM3);

		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			// annotation.setSlotVolumeInMMBTu(slot, Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot), annotation.getSlotCargoCV(slot)));
			annotation.setSlotVolumeInM3(slot, Calculator.convertMMBTuToM3(annotation.getSlotVolumeInMMBTu(slot), annotation.getSlotCargoCV(slot)));
		}
		return annotation;
	}
}
