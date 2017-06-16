/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;

/**
 * A {@link IVolumeAllocator} implementation that checks (estimated) P&L and decided whether to full load (normal mode) or min load (for cargoes which are loosing money).
 * 
 * @author Simon Goodall
 *
 */
public class MinMaxUnconstrainedVolumeAllocator extends UnconstrainedVolumeAllocator {

	@Inject
	private IBoilOffHelper inPortBoilOffHelper;

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

		if (slots.size() > 2) {
			// Fixed discharge volumes, so no decision to make here.
			return calculateShippedMode_MaxVolumes(allocationRecord, slots, vessel);
		}

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

	protected @NonNull AllocationAnnotation calculateShippedMode_MinVolumes(final AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
		assert allocationRecord.allocationMode == AllocationMode.Shipped;

		// Scale factor applied internal mmbtu values *for this method only* to help mitigate m3 <-> mmbtu rounding problems
		final int scaleFactor = 10;
		final long scaleFactorL = 10L;

		// Only support LD cargoes
		assert slots.size() == 2;

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);

		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);
		final int cargoCV = annotation.getSlotCargoCV(loadSlot);

		final long loadBoilOffInMMBTu = Calculator.convertM3ToMMBTu(inPortBoilOffHelper.calculatePortVisitNBOInM3(vessel, slots.get(0), allocationRecord), scaleFactor * cargoCV);

		// how much room is there in the tanks?
		long availableCargoSpaceInMMBTU = Calculator.convertM3ToMMBTu(vessel.getCargoCapacity() - allocationRecord.startVolumeInM3, scaleFactor * cargoCV);

		if (inPortBoilOffHelper.isBoilOffCompensation()) {
			availableCargoSpaceInMMBTU += loadBoilOffInMMBTu;
		}
		// how much fuel will be required over and above what we start with in the tanks?
		// note: this is the fuel consumption plus any heel quantity required at discharge
		final long fuelDeficitInMMBTU = Calculator.convertM3ToMMBTu(allocationRecord.requiredFuelVolumeInM3 - allocationRecord.startVolumeInM3 + allocationRecord.minimumEndVolumeInM3,
				scaleFactor * cargoCV);

		// greedy assumption: always load as much as possible
		long loadVolumeInMMBTU = Math.min(scaleFactorL * allocationRecord.minVolumesInMMBtu.get(0), availableCargoSpaceInMMBTU);
		// violate maximum load volume constraint when it has to be done to fuel the vessel
		if (loadVolumeInMMBTU < fuelDeficitInMMBTU) {
			loadVolumeInMMBTU = fuelDeficitInMMBTU;
			// we should never be required to load more than the vessel can fit in its tanks
			// assert (loadVolume <= availableCargoSpace);
		}

		// the amount of LNG available for discharge
		long unusedVolumeInMMBTU = loadVolumeInMMBTU
				+ Calculator.convertM3ToMMBTu(allocationRecord.startVolumeInM3 - allocationRecord.minimumEndVolumeInM3 - allocationRecord.requiredFuelVolumeInM3, scaleFactor * cargoCV);

		// available volume is non-negative
		assert (unusedVolumeInMMBTU >= 0);

		final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);
		{
			// greedy assumption: always discharge as much as possible
			final long dischargeVolumeInMMBTU = scaleFactorL * allocationRecord.minVolumesInMMBtu.get(1);
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;
		}

		// Increase load volume to meet min discharge.
		if (unusedVolumeInMMBTU < 0) {
			loadVolumeInMMBTU += -unusedVolumeInMMBTU;
			unusedVolumeInMMBTU = 0;
		} else {
			// Increase discharge to meet min load
			// greedy assumption: always discharge as much as possible
			final long currentDischargeVolumeInMMBTU = scaleFactorL * allocationRecord.minVolumesInMMBtu.get(1);
			final long dischargeVolumeInMMBTU = Math.min(currentDischargeVolumeInMMBTU + unusedVolumeInMMBTU,
					allocationRecord.maxVolumesInMMBtu.get(1) == Long.MAX_VALUE ? Long.MAX_VALUE : scaleFactorL * allocationRecord.maxVolumesInMMBtu.get(1));
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			// Adjust unused volume for new discharge volume
			unusedVolumeInMMBTU += currentDischargeVolumeInMMBTU;
			unusedVolumeInMMBTU -= dischargeVolumeInMMBTU;
			if (unusedVolumeInMMBTU > 0 && allocationRecord.preferShortLoadOverLeftoverHeel) {
				// Use up the full heel range before short loading....
				final long additionalEndHeelInMMBtu = allocationRecord.maximumEndVolumeInM3 == Long.MAX_VALUE ? unusedVolumeInMMBTU : Calculator.convertM3ToMMBTu(allocationRecord.maximumEndVolumeInM3 - allocationRecord.minimumEndVolumeInM3, scaleFactor * cargoCV);
				loadVolumeInMMBTU -= Math.max(0, unusedVolumeInMMBTU - additionalEndHeelInMMBtu);
			}
		}

		// Check load caps - vessel capacity
		if (loadVolumeInMMBTU > availableCargoSpaceInMMBTU) {
			final long diff = loadVolumeInMMBTU - availableCargoSpaceInMMBTU;

			long dischargeVolumeInMMBTU = annotation.getCommercialSlotVolumeInMMBTu(dischargeSlot);
			long physicalDischargeVolumeInMMBTU = annotation.getPhysicalSlotVolumeInMMBTu(dischargeSlot);
			dischargeVolumeInMMBTU -= diff;
			physicalDischargeVolumeInMMBTU -= diff;
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, physicalDischargeVolumeInMMBTU);
			loadVolumeInMMBTU = availableCargoSpaceInMMBTU;
		}
		// Check load caps - max load // check we can reach boiloff
		long loadMaxVolInMMBtu = allocationRecord.maxVolumesInMMBtu.get(0);
		loadMaxVolInMMBtu = (loadMaxVolInMMBtu == Long.MAX_VALUE) ? Long.MAX_VALUE : scaleFactorL * loadMaxVolInMMBtu;
		if (loadVolumeInMMBTU > loadMaxVolInMMBtu) {
			final long diff = loadVolumeInMMBTU - loadMaxVolInMMBtu;

			long dischargeVolumeInMMBTU = annotation.getCommercialSlotVolumeInMMBTu(dischargeSlot);
			long physicalDischargeVolumeInMMBTU = annotation.getCommercialSlotVolumeInMMBTu(dischargeSlot);
			dischargeVolumeInMMBTU -= diff;
			physicalDischargeVolumeInMMBTU -= diff;
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, physicalDischargeVolumeInMMBTU);
			loadVolumeInMMBTU = loadMaxVolInMMBtu;
		}

		// Finally check for negative discharge volumes
		final long dischargeVolumeInMMBTU = annotation.getCommercialSlotVolumeInMMBTu(dischargeSlot);
		if (dischargeVolumeInMMBTU < 0) {
			final long diff = -dischargeVolumeInMMBTU;
			loadVolumeInMMBTU += diff;
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, 0);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, 0);
		}

		annotation.setCommercialSlotVolumeInMMBTu(loadSlot, loadVolumeInMMBTU);
		annotation.setPhysicalSlotVolumeInMMBTu(loadSlot, loadVolumeInMMBTU - loadBoilOffInMMBTu);

		annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(allocationRecord.minimumEndVolumeInM3 + (Calculator.convertMMBTuToM3(unusedVolumeInMMBTU, cargoCV) + 5L) / scaleFactorL);
		annotation.setFuelVolumeInM3(allocationRecord.requiredFuelVolumeInM3);

		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			// annotation.setSlotVolumeInMMBTu(slot, Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot), annotation.getSlotCargoCV(slot)));
			annotation.setCommercialSlotVolumeInM3(slot, (Calculator.convertMMBTuToM3(annotation.getCommercialSlotVolumeInMMBTu(slot), annotation.getSlotCargoCV(slot)) + 5L) / scaleFactorL);
			annotation.setPhysicalSlotVolumeInM3(slot, (Calculator.convertMMBTuToM3(annotation.getPhysicalSlotVolumeInMMBTu(slot), annotation.getSlotCargoCV(slot)) + 5L) / scaleFactorL);

			// Reset the mmbtu scale factor
			annotation.setCommercialSlotVolumeInMMBTu(slot, (annotation.getCommercialSlotVolumeInMMBTu(slot) + 5L) / scaleFactorL);
			annotation.setPhysicalSlotVolumeInMMBTu(slot, (annotation.getPhysicalSlotVolumeInMMBTu(slot) + 5L) / scaleFactorL);
		}

		assert annotation.getStartHeelVolumeInM3() >= 0;
		assert annotation.getRemainingHeelVolumeInM3() >= 0;

		return annotation;
	}
}
