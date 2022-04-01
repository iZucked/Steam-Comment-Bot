/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProvider;

/**
 * A {@link IVolumeAllocator} implementation that checks (estimated) P&L and decided whether to full load (normal mode) or min load (for cargoes which are loosing money).
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class MinMaxUnconstrainedVolumeAllocator extends UnconstrainedVolumeAllocator {

	@Inject
	private IBoilOffHelper inPortBoilOffHelper;

	@Inject
	private ICounterPartyVolumeProvider counterPartyVolumeProvider;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	@Named(SchedulerConstants.KEY_DEFAULT_MAX_VOLUME_IN_M3)
	private long defaultMaxVolumeInM3;

	@Override
	protected IAllocationAnnotation calculateTransferMode(final AllocationRecord maxVolumeRecord, final List<IPortSlot> slots, final IVessel vessel, @Nullable IAnnotatedSolution annotatedSolution) {

		// Note: Calculations in MMBTU
		final long startVolumeInMMBTu;
		long vesselCapacityInMMBTu;

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);
		// Assuming a single cargo CV!
		final int defaultCargoCVValue = loadSlot.getCargoCVValue();
		// Convert startVolume and vesselCapacity into MMBTu
		if (defaultCargoCVValue > 0) {
			if (maxVolumeRecord.startVolumeInM3 != Long.MAX_VALUE) {
				startVolumeInMMBTu = Calculator.convertM3ToMMBTu(maxVolumeRecord.startVolumeInM3, defaultCargoCVValue);
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

		// If there are no upper bounds, apply default volume limit
		boolean isAllMaxValue = vesselCapacityInMMBTu == Long.MAX_VALUE;
		if (isAllMaxValue) {
			for (int i = 0; i < slots.size(); ++i) {
				isAllMaxValue &= maxVolumeRecord.maxVolumesInMMBtu.get(i) == Long.MAX_VALUE;
			}
		}
		if (isAllMaxValue) {
			vesselCapacityInMMBTu = Calculator.convertM3ToMMBTu(defaultMaxVolumeInM3, defaultCargoCVValue);
		}

		final long availableCargoSpaceMMBTu = vesselCapacityInMMBTu - startVolumeInMMBTu;

		long maxTransferVolumeMMBTu = availableCargoSpaceMMBTu;
		long maxTransferVolumeM3 = -1;
		{
			for (int i = 0; i < slots.size(); ++i) {
				final long newMinTransferVolumeMMBTU = capValueWithZeroDefault(maxVolumeRecord.maxVolumesInMMBtu.get(i), maxTransferVolumeMMBTu);
				maxTransferVolumeM3 = getUnroundedM3TransferVolume(maxVolumeRecord, slots, maxTransferVolumeMMBTu, maxTransferVolumeM3, i, newMinTransferVolumeMMBTU, false);
				maxTransferVolumeMMBTu = newMinTransferVolumeMMBTU;
			}
		}

		final AllocationAnnotation maxVolumeAnnotation = createNewAnnotation(maxVolumeRecord, slots);

		setTransferVolume(maxVolumeRecord, slots, maxVolumeAnnotation, maxTransferVolumeMMBTu, maxTransferVolumeM3);
		// FCL? Always return max load even if loss making.
		if (maxVolumeRecord.fullCargoLot) {
			return maxVolumeAnnotation;
		}

		Pair<@NonNull CargoValueAnnotation, @NonNull Long> maxVolumePair = entityValueCalculator.evaluate(maxVolumeRecord.resourceVoyagePlan, maxVolumeAnnotation, maxVolumeRecord.vesselAvailability,
				null, null);
		final long maxTransferPNL = maxVolumePair.getSecond();

		AllocationRecord minVolumeRecord = maxVolumeRecord.mutableCopy();
		long minTransferVolumeMMBTu = 0;
		long minTransferVolumeM3 = -1;
		{
			for (int i = 0; i < slots.size(); ++i) {
				final long newMinTransferVolumeMMBTU = Math.max(minVolumeRecord.minVolumesInMMBtu.get(i), minTransferVolumeMMBTu);
				minTransferVolumeM3 = getUnroundedM3TransferVolume(minVolumeRecord, slots, minTransferVolumeMMBTu, minTransferVolumeM3, i, newMinTransferVolumeMMBTU, true);
				minTransferVolumeMMBTu = newMinTransferVolumeMMBTU;
			}
			// Max sure lower bound is not higher than the upper bound
			minTransferVolumeM3 = Math.min(minTransferVolumeM3, maxTransferVolumeM3);
			minTransferVolumeMMBTu = Math.min(minTransferVolumeMMBTu, maxTransferVolumeMMBTu);
		}
		final AllocationAnnotation minVolumeAnnotation = createNewAnnotation(minVolumeRecord, slots);

		setTransferVolume(minVolumeRecord, slots, minVolumeAnnotation, minTransferVolumeMMBTu, minTransferVolumeM3);

		Pair<CargoValueAnnotation, Long> minVolumePair = entityValueCalculator.evaluate(minVolumeRecord.resourceVoyagePlan, minVolumeAnnotation, minVolumeRecord.vesselAvailability, null, null);

		final long minTransferPNL = minVolumePair.getSecond();
		// If annotating, then we need to re-evaluate with the solution so we record the
		// correct output
		final boolean counterPartyVolumeOption = counterPartyVolumeProvider.isCounterPartyVolume(loadSlot);
		// If there's a counterparty option, we would flip the check. Consider the previous setTransferVolume
		final boolean foo = counterPartyVolumeOption != (maxTransferPNL >= minTransferPNL);
		if (foo) {
			if (annotatedSolution != null) {
				return entityValueCalculator.evaluate(maxVolumeRecord.resourceVoyagePlan, maxVolumeAnnotation, maxVolumeRecord.vesselAvailability, null, annotatedSolution).getFirst();
			}
			return maxVolumePair.getFirst();
		} else {
			if (annotatedSolution != null) {
				return entityValueCalculator.evaluate(minVolumeRecord.resourceVoyagePlan, minVolumeAnnotation, minVolumeRecord.vesselAvailability, null, annotatedSolution).getFirst();
			}
			return minVolumePair.getFirst();
		}
	}

	@Override
	protected IAllocationAnnotation calculateShippedMode(final AllocationRecord allocationRecord, final List<IPortSlot> slots, final IVessel vessel, @Nullable IAnnotatedSolution annotatedSolution) {

		if (slots.size() > 2 || allocationRecord.fullCargoLot) {
			// Fixed discharge volumes, so no decision to make here.
			return calculateShippedMode_MaxVolumes(allocationRecord, slots, vessel);
		}

		final IAllocationAnnotation minAnnotation = calculateShippedMode_MinVolumes(allocationRecord, slots, vessel);

		Pair<@NonNull CargoValueAnnotation, @NonNull Long> minVolumePair = entityValueCalculator.evaluate(allocationRecord.resourceVoyagePlan, minAnnotation, allocationRecord.vesselAvailability, null,
				annotatedSolution);
		final long minTransferPNL = minVolumePair.getSecond();

		final IAllocationAnnotation maxAnnotation = calculateShippedMode_MaxVolumes(allocationRecord, slots, vessel);

		Pair<CargoValueAnnotation, Long> maxVolumePair = entityValueCalculator.evaluate(allocationRecord.resourceVoyagePlan, maxAnnotation, allocationRecord.vesselAvailability, null, null);
		final long maxTransferPNL = maxVolumePair.getSecond();

		if (maxTransferPNL >= minTransferPNL) {
			if (annotatedSolution != null) {
				return entityValueCalculator.evaluate(allocationRecord.resourceVoyagePlan, maxAnnotation, allocationRecord.vesselAvailability, null, annotatedSolution).getFirst();
			}
			return maxVolumePair.getFirst();
		} else {
			if (annotatedSolution != null) {
				return entityValueCalculator.evaluate(allocationRecord.resourceVoyagePlan, minAnnotation, allocationRecord.vesselAvailability, null, annotatedSolution).getFirst();
			}
			return minVolumePair.getFirst();
		}
	}

	protected AllocationAnnotation calculateShippedMode_MinVolumes(final AllocationRecord allocationRecord, final List<IPortSlot> slots, final IVessel vessel) {
		assert allocationRecord.allocationMode == AllocationMode.Shipped;

		// Scale factor applied internal mmbtu values *for this method only* to help
		// mitigate m3 <-> mmbtu rounding problems
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

		// greedy assumption: always load as much as possible
		long loadVolumeInMMBTU = Math.min(scaleFactorL * allocationRecord.minVolumesInMMBtu.get(0), availableCargoSpaceInMMBTU);
		{
			// how much fuel will be required over and above what we start with in the
			// tanks?
			// note: this is the fuel consumption plus any heel quantity required at
			// discharge
			final long fuelDeficitInMMBTU = Calculator.convertM3ToMMBTu(allocationRecord.requiredFuelVolumeInM3 - allocationRecord.startVolumeInM3 + allocationRecord.minimumEndVolumeInM3,
					scaleFactor * cargoCV);
			// violate maximum load volume constraint when it has to be done to fuel the
			// vessel
			if (loadVolumeInMMBTU < fuelDeficitInMMBTU) {
				loadVolumeInMMBTU = fuelDeficitInMMBTU;
				// we should never be required to load more than the vessel can fit in its tanks
				// assert (loadVolume <= availableCargoSpace);
			}
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
				final long additionalEndHeelInMMBtu = allocationRecord.maximumEndVolumeInM3 == Long.MAX_VALUE ? unusedVolumeInMMBTU
						: Calculator.convertM3ToMMBTu(allocationRecord.maximumEndVolumeInM3 - allocationRecord.minimumEndVolumeInM3, scaleFactor * cargoCV);
				long excessLoadVolumeInMMBTU = Math.max(0, unusedVolumeInMMBTU - additionalEndHeelInMMBtu);
				if (excessLoadVolumeInMMBTU > loadVolumeInMMBTU) {
					// Can't short load all the volume!
					loadVolumeInMMBTU = 0;
				} else {
					loadVolumeInMMBTU -= excessLoadVolumeInMMBTU;
				}
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
			// annotation.setSlotVolumeInMMBTu(slot,
			// Calculator.convertM3ToMMBTu(annotation.getSlotVolumeInM3(slot),
			// annotation.getSlotCargoCV(slot)));
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
