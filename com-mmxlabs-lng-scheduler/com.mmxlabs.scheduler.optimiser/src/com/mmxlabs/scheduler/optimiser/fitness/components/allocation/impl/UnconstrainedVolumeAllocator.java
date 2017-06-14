/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.DetailedCargoType;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A cargo allocator which presumes that there are no total volume constraints, and so the total remaining capacity should be allocated
 * 
 * @author Tom Hinton
 * 
 */
public class UnconstrainedVolumeAllocator extends BaseVolumeAllocator {

	@Inject
	private IBoilOffHelper inPortBoilOffHelper;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private CargoTypeUtil cargoTypeUtil;

	/**
	 * Returns x, capped by y; if x has the special value 0, it is considered undefined and y is returned.
	 * 
	 * @return
	 */
	protected final static long capValueWithZeroDefault(final long x, final long y) {
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
	public IAllocationAnnotation allocate(final @NonNull AllocationRecord allocationRecord) {
		final @NonNull List<@NonNull IPortSlot> slots = allocationRecord.slots;

		final IVessel vessel = allocationRecord.nominatedVessel != null ? allocationRecord.nominatedVessel : allocationRecord.vesselAvailability.getVessel();

		switch (allocationRecord.allocationMode) {
		case Actuals:
			return calculateActualsShippedMode(allocationRecord, slots, vessel);
		case Actuals_Transfer:
			return calculateActualsTransferMode(allocationRecord, slots);
		case Shipped:
			return calculateShippedMode(allocationRecord, slots, vessel);
		case Transfer:
			return calculateTransferMode(allocationRecord, slots, vessel);
		case Custom:
			return calculateCustomMode(allocationRecord, slots, vessel);
		default:
			throw new IllegalStateException();
		}
	}

	protected @NonNull AllocationAnnotation calculateActualsTransferMode(final AllocationRecord allocationRecord, final List<@NonNull IPortSlot> slots) {

		final AllocationAnnotation annotation = new AllocationAnnotation();

		// Second slot - assume the DES Sale
		final IPortSlot salesSlot = allocationRecord.slots.get(1);
		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			if (actualsDataProvider.hasActuals(slot) == false) {
				throw new IllegalStateException("Actuals Volume Mode, but no actuals specified");
			}
			annotation.getSlots().add(slot);
		
			
			// TODO: This is keyed to E DES sale actuals requirements. Needs further customisability...
			// Actuals mode, take values directly from sale
			annotation.setSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(salesSlot));
			annotation.setSlotDuration(slot, 0);
			annotation.setRouteOptionBooking(slot, allocationRecord.portTimesRecord.getRouteOptionBooking(slot));
			annotation.setSlotNextVoyageOptions(slot, allocationRecord.portTimesRecord.getSlotNextVoyageOptions(slot));

			annotation.setCommercialSlotVolumeInM3(slot, allocationRecord.maxVolumesInM3.get(i));
			annotation.setPhysicalSlotVolumeInM3(slot, allocationRecord.maxVolumesInM3.get(i));
			annotation.setCommercialSlotVolumeInMMBTu(slot, allocationRecord.maxVolumesInMMBtu.get(i));
			annotation.setPhysicalSlotVolumeInMMBTu(slot, allocationRecord.maxVolumesInMMBtu.get(i));
			annotation.setSlotCargoCV(slot, allocationRecord.slotCV.get(i));
		}
		// Copy over the return slot time if present
		{
			final IPortSlot slot = allocationRecord.portTimesRecord.getReturnSlot();
			if (slot != null) {
				annotation.setReturnSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(slot));
			}
		}
		return annotation;
	}

	protected @NonNull AllocationAnnotation calculateActualsShippedMode(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots,
			final @NonNull IVessel vessel) {

		final AllocationAnnotation annotation = new AllocationAnnotation();

		// Work out used fuel volume - adjust for heel positions and transfer volumes
		// Actuals data will give us;
		// * Heel before loading
		// * Volume Loaded
		// * Volume discharged.
		// * Heel after discharge.
		// * If the next load is actualised, take the heel before loading, otherwise assume safety heel.
		long usedFuelVolume = 0;
		IPortSlot lastSlot = null;

		// Pre-process to determine cargo type. Pure FOB or DES has no duration, regardless of actuals
		final DetailedCargoType detailedCargoType = cargoTypeUtil.getDetailedCargoType(slots);
		final boolean isFOBOrDES = detailedCargoType == DetailedCargoType.DES_PURCHASE || detailedCargoType == DetailedCargoType.FOB_SALE;
		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			if (actualsDataProvider.hasActuals(slot) == false) {
				throw new IllegalStateException("Actuals Volume Mode, but no actuals specified");
			}

			annotation.getSlots().add(slot);

			// Scheduler should have made this happen, but lets make sure here
			assert isFOBOrDES || (allocationRecord.portTimesRecord.getSlotTime(slot) == actualsDataProvider.getArrivalTime(slot));
			assert allocationRecord.portTimesRecord.getSlotDuration(slot) == (isFOBOrDES ? 0 : actualsDataProvider.getVisitDuration(slot));
			annotation.setSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(slot));
			annotation.setSlotDuration(slot, allocationRecord.portTimesRecord.getSlotDuration(slot));
			annotation.setRouteOptionBooking(slot, allocationRecord.portTimesRecord.getRouteOptionBooking(slot));
			annotation.setSlotNextVoyageOptions(slot, allocationRecord.portTimesRecord.getSlotNextVoyageOptions(slot));

			// Actuals mode, take values directly
			annotation.setCommercialSlotVolumeInM3(slot, allocationRecord.maxVolumesInM3.get(i));
			annotation.setCommercialSlotVolumeInMMBTu(slot, allocationRecord.maxVolumesInMMBtu.get(i));
			annotation.setPhysicalSlotVolumeInM3(slot, allocationRecord.maxVolumesInM3.get(i));
			annotation.setPhysicalSlotVolumeInMMBTu(slot, allocationRecord.maxVolumesInMMBtu.get(i));
			annotation.setSlotCargoCV(slot, allocationRecord.slotCV.get(i));

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
		assert returnSlot != null;

		final long returnSlotHeelInM3;
		if (lastSlot != null && actualsDataProvider.hasReturnActuals(lastSlot)) {
			returnSlotHeelInM3 = actualsDataProvider.getReturnHeelInM3(lastSlot);
		} else if (actualsDataProvider.hasActuals(returnSlot)) {
			returnSlotHeelInM3 = actualsDataProvider.getStartHeelInM3(returnSlot);
		} else {
			// Use safety heel if not actualised
			returnSlotHeelInM3 = vessel.getVesselClass().getSafetyHeel();
		}

		usedFuelVolume -= returnSlotHeelInM3;

		annotation.setRemainingHeelVolumeInM3(returnSlotHeelInM3);
		annotation.setFuelVolumeInM3(usedFuelVolume);

		// Copy over the return slot time if present
		{
			final IPortSlot slot = allocationRecord.portTimesRecord.getReturnSlot();
			if (slot != null) {
				annotation.setReturnSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(slot));
			}
		}
		return annotation;
	}

	protected @NonNull AllocationAnnotation calculateShippedMode_MaxVolumes(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final IVessel vessel) {
		// Scale factor applied internal mmbtu values *for this method only* to help mitigate m3 <-> mmbtu rounding problems
		final int scaleFactor = 10;
		final long scaleFactorL = 10L;

		final ILoadOption loadSlot = (ILoadOption) slots.get(0);
		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);

		assert allocationRecord.allocationMode == AllocationMode.Shipped;
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
		long loadVolumeInMMBTU = capValueWithZeroDefault(
				allocationRecord.maxVolumesInMMBtu.get(0) == Long.MAX_VALUE ? allocationRecord.maxVolumesInMMBtu.get(0) : scaleFactorL * allocationRecord.maxVolumesInMMBtu.get(0),
				availableCargoSpaceInMMBTU);
		// violate maximum load volume constraint when it has to be done to fuel the vessel
		if (loadVolumeInMMBTU < fuelDeficitInMMBTU) {
			loadVolumeInMMBTU = fuelDeficitInMMBTU;
			// we should never be required to load more than the vessel can fit in its tanks
			// assert (loadVolume <= availableCargoSpace);
		}

		// the amount of LNG available for discharge
		long unusedVolumeInMMBTU = loadVolumeInMMBTU - fuelDeficitInMMBTU;
		// + Calculator.convertM3ToMMBTu(allocationRecord.startVolumeInM3 - allocationRecord.minEndVolumeInM3 - allocationRecord.requiredFuelVolumeInM3, scaleFactor * cargoCV);

		// available volume is non-negative
		assert (unusedVolumeInMMBTU >= 0);

		// load / discharge case
		// this is subsumed by the LDD* case, but can be done more efficiently by branching instead of looping
		if (slots.size() == 2) {

			final IDischargeOption dischargeSlot = (IDischargeOption) slots.get(1);
			// greedy assumption: always discharge as much as possible
			final long dischargeVolumeInMMBTU = allocationRecord.maxVolumesInMMBtu.get(1) == Long.MAX_VALUE ? unusedVolumeInMMBTU
					: capValueWithZeroDefault(scaleFactorL * allocationRecord.maxVolumesInMMBtu.get(1), unusedVolumeInMMBTU);
			assert dischargeVolumeInMMBTU >= 0;
			annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
			annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
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
				final long minDischargeVolumeInMMBTU = scaleFactorL * allocationRecord.minVolumesInMMBtu.get(i);

				// assign the minimum amount per discharge slot
				final long dischargeVolumeInMMBTU;
				if (unusedVolumeInMMBTU >= minDischargeVolumeInMMBTU) {
					dischargeVolumeInMMBTU = minDischargeVolumeInMMBTU;
				} else {
					dischargeVolumeInMMBTU = unusedVolumeInMMBTU;
				}
				annotation.setCommercialSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
				annotation.setPhysicalSlotVolumeInMMBTu(dischargeSlot, dischargeVolumeInMMBTU);
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
				final long volumeInMMBTU = Math.min(scaleFactorL * (allocationRecord.maxVolumesInMMBtu.get(index) - allocationRecord.minVolumesInMMBtu.get(index)), unusedVolumeInMMBTU);
				// reduce the remaining available volume
				unusedVolumeInMMBTU -= volumeInMMBTU;
				final long currentVolumeInMMBTU = annotation.getCommercialSlotVolumeInMMBTu(slot);
				annotation.setCommercialSlotVolumeInMMBTu(slot, currentVolumeInMMBTU + volumeInMMBTU);
				annotation.setPhysicalSlotVolumeInMMBTu(slot, currentVolumeInMMBTU + volumeInMMBTU);
			}

			// Note this currently does nothing as the next() method in the allocator iterator (BaseCargoAllocator) ignores this data and looks directly on the discharge slot.
		}

		// Excess volume, drop down to min load before deciding whether or not to short load any further excess
		// NOTE: We include the #preferShortLoadOverLeftoverHeel for F custom split discharge cargoes. I don't think it should be here otherwise.
		// Perhaps we should make the API explicit over this?
		if (allocationRecord.preferShortLoadOverLeftoverHeel && unusedVolumeInMMBTU > 0) {
			if (loadVolumeInMMBTU > scaleFactorL * allocationRecord.minVolumesInM3.get(0)) {
				long delta = loadVolumeInMMBTU - (scaleFactorL * allocationRecord.minVolumesInM3.get(0));
				delta = Math.min(unusedVolumeInMMBTU, delta);
				unusedVolumeInMMBTU -= delta;
				loadVolumeInMMBTU -= delta;

			}
		}

		/*
		 * Under certain circumstances, the remaining heel may be more than expected: for instance, when a minimum load constraint far exceeds a maximum discharge constraint. This may cause problems
		 * with restrictions on where LNG can be shipped, or with profit share contracts, so at present a conservative assumption in the LNGVoyageCalculator treats it as a capacity violation and
		 * assumes that the load has to be reduced below its min value constraint.
		 */

		// if there is any leftover volume after discharge
		if (allocationRecord.preferShortLoadOverLeftoverHeel && unusedVolumeInMMBTU > 0 && allocationRecord.maximumEndVolumeInM3 != Long.MAX_VALUE) {

			// Use up the full heel range before short loading....
			long additionalEndHeelInMMBtu = Calculator.convertM3ToMMBTu(allocationRecord.maximumEndVolumeInM3 - allocationRecord.minimumEndVolumeInM3, scaleFactor * cargoCV);
			// we use a conservative heuristic: load exactly as much as we need, subject to constraints
			final long revisedLoadVolumeInMMBTU = Math.max(0, loadVolumeInMMBTU - Math.max(0, unusedVolumeInMMBTU - additionalEndHeelInMMBtu));

			// TODO: report min load constraint violation if necessary

			unusedVolumeInMMBTU -= loadVolumeInMMBTU - revisedLoadVolumeInMMBTU;
			loadVolumeInMMBTU = revisedLoadVolumeInMMBTU;
			/*
			 * TODO: if the max discharge volume would leave excess heel, the correct load volume decision depends on relative prices at this load and the next, and whether carrying over excess heel
			 * is contractually permissible (and CV-compatible with the next load port).
			 */
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

	protected @NonNull AllocationAnnotation calculateShippedMode(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {

		return calculateShippedMode_MaxVolumes(allocationRecord, slots, vessel);
	}

	protected @NonNull AllocationAnnotation calculateTransferMode(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
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
		long transferVolumeMMBTu = availableCargoSpaceMMBTu;
		long transferVolumeM3 = -1;
		for (int i = 0; i < slots.size(); ++i) {
			final long newMinTransferVolumeMMBTU = capValueWithZeroDefault(allocationRecord.maxVolumesInMMBtu.get(i), transferVolumeMMBTu);
			transferVolumeM3 = getUnroundedM3TransferVolume(allocationRecord, slots, transferVolumeMMBTu, transferVolumeM3, i, newMinTransferVolumeMMBTU, false);
			transferVolumeMMBTu = newMinTransferVolumeMMBTU;
		}
		setTransferVolume(allocationRecord, slots, annotation, transferVolumeMMBTu, transferVolumeM3);

		return annotation;
	}

	/**
	 * Checks if the maximum transfer volume in MMBTU has changed, and if so, returns the original M3 volume, if the volume came from a slot with input originally set in M3. If not a value of -1 is
	 * returned.
	 * 
	 * @param allocationRecord
	 * @param slots
	 * @param transferVolumeMMBTU
	 * @param transferVolumeM3
	 * @param slotIdx
	 * @param newMinTransferVolumeMMBTU
	 * @return
	 */
	protected long getUnroundedM3TransferVolume(final AllocationRecord allocationRecord, final List<IPortSlot> slots, final long transferVolumeMMBTU, long transferVolumeM3, final int slotIdx,
			final long newMinTransferVolumeMMBTU, final boolean isMin) {
		if (transferVolumeMMBTU != newMinTransferVolumeMMBTU) {
			// updating the min transfer volume, avoid rounding errors for M3 volumes
			if (slots.get(slotIdx) instanceof IDischargeOption) {
				if (((IDischargeOption) slots.get(slotIdx)).isVolumeSetInM3()) {
					if (isMin) {
						transferVolumeM3 = allocationRecord.minVolumesInM3.get(slotIdx);
					} else {
						transferVolumeM3 = allocationRecord.maxVolumesInM3.get(slotIdx);
					}
				} else {
					transferVolumeM3 = -1; // resetting
				}
			} else if (slots.get(slotIdx) instanceof ILoadOption) {
				if (((ILoadOption) slots.get(slotIdx)).isVolumeSetInM3()) {
					if (isMin) {
						transferVolumeM3 = allocationRecord.minVolumesInM3.get(slotIdx);
					} else {
						transferVolumeM3 = allocationRecord.maxVolumesInM3.get(slotIdx);
					}
				} else {
					transferVolumeM3 = -1;
				}
			} else {
				transferVolumeM3 = -1;
			}
		}
		return transferVolumeM3;
	}

	/**
	 * Sets the volume transferred in a non shipped cargo in both M3 and MMBTu
	 * 
	 * @param allocationRecord
	 * @param slots
	 * @param annotation
	 * @param transferVolumeMMBTU
	 * @param transferVolumeM3
	 */
	protected void setTransferVolume(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull AllocationAnnotation annotation,
			final long transferVolumeMMBTU, final long transferVolumeM3) {
		for (int i = 0; i < slots.size(); ++i) {
			final IPortSlot slot = slots.get(i);
			annotation.setCommercialSlotVolumeInMMBTu(slot, transferVolumeMMBTU);
			if (transferVolumeM3 != -1) {
				annotation.setCommercialSlotVolumeInM3(slot, transferVolumeM3);
			} else {
				final int slotCV = allocationRecord.slotCV.get(i);
				if (slotCV > 0) {
					annotation.setCommercialSlotVolumeInM3(slot, Calculator.convertMMBTuToM3(transferVolumeMMBTU, slotCV));
				} else {
					annotation.setCommercialSlotVolumeInM3(slot, 0);
				}
			}
		}
	}

	protected @NonNull AllocationAnnotation createNewAnnotation(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots) {
		final AllocationAnnotation annotation = new AllocationAnnotation();
		final ILoadOption loadSlot = (ILoadOption) slots.get(0);
		// Assuming a single cargo CV!
		final int defaultCargoCVValue = loadSlot.getCargoCVValue();

		// Copy across slot time information
		for (int i = 0; i < slots.size(); i++) {
			final IPortSlot slot = allocationRecord.slots.get(i);
			annotation.getSlots().add(slot);
			annotation.setSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(slot));
			annotation.setSlotDuration(slot, allocationRecord.portTimesRecord.getSlotDuration(slot));
			annotation.setRouteOptionBooking(slot, allocationRecord.portTimesRecord.getRouteOptionBooking(slot));
			annotation.setSlotNextVoyageOptions(slot, allocationRecord.portTimesRecord.getSlotNextVoyageOptions(slot));

			if (actualsDataProvider.hasActuals(slot)) {
				annotation.setSlotCargoCV(slot, actualsDataProvider.getCVValue(slot));
			} else {
				annotation.setSlotCargoCV(slot, defaultCargoCVValue);
			}
		}
		// Copy over the return slot time if present
		{
			final IPortSlot slot = allocationRecord.portTimesRecord.getReturnSlot();
			if (slot != null) {
				annotation.setReturnSlotTime(slot, allocationRecord.portTimesRecord.getSlotTime(slot));
			}
		}
		return annotation;

	}

	/**
	 * Sets the volume transferred in a non shipped cargo in both M3 and MMBTu
	 * 
	 * @param allocationRecord
	 * @param slots
	 * @param annotation
	 * @param transferVolumeMMBTU
	 * @param transferVolumeM3
	 */
	protected void setTransferVolume(final @NonNull AllocationRecord allocationRecord, @NonNull final IPortSlot slot, final @NonNull AllocationAnnotation annotation, final long transferVolumeMMBTU,
			final long transferVolumeM3) {
		annotation.setCommercialSlotVolumeInMMBTu(slot, transferVolumeMMBTU);
		if (transferVolumeM3 != -1) {
			annotation.setCommercialSlotVolumeInM3(slot, transferVolumeM3);
		} else {
			final int slotCV = annotation.getSlotCargoCV(slot);
			if (slotCV > 0) {
				annotation.setCommercialSlotVolumeInM3(slot, Calculator.convertMMBTuToM3(transferVolumeMMBTU, slotCV));
			} else {
				annotation.setCommercialSlotVolumeInM3(slot, 0);
			}
		}
	}

	protected @NonNull AllocationAnnotation calculateCustomMode(final @NonNull AllocationRecord allocationRecord, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IVessel vessel) {
		assert allocationRecord.allocationMode == AllocationMode.Custom;

		final AllocationAnnotation annotation = createNewAnnotation(allocationRecord, slots);

		annotation.setStartHeelVolumeInM3(allocationRecord.startVolumeInM3);
		annotation.setFuelVolumeInM3(allocationRecord.requiredFuelVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(allocationRecord.minimumEndVolumeInM3);
		for (int i = 0; i < slots.size(); ++i) {
			final IPortSlot slot = slots.get(i);

			annotation.setCommercialSlotVolumeInM3(slot, allocationRecord.maxVolumesInM3.get(i));
			annotation.setCommercialSlotVolumeInMMBTu(slot, allocationRecord.maxVolumesInMMBtu.get(i));
		}

		return annotation;
	}
}
