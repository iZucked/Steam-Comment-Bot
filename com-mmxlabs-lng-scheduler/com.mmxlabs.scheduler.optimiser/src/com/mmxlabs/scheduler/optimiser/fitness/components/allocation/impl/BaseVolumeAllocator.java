/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord.AllocationMode;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the solve() method, but does do various book-keeping tasks.
 * 
 * @author hinton
 * 
 */
public abstract class BaseVolumeAllocator implements IVolumeAllocator {
	@Inject(optional = true)
	private ICustomVolumeAllocator allocationRecordModifier;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	public BaseVolumeAllocator() {
		super();
	}

	@Override
	@Nullable
	public IAllocationAnnotation allocate(final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IPortTimesRecord portTimesRecord) {
		final AllocationRecord allocationRecord = createAllocationRecord(vesselAvailability, vesselStartTime, plan, portTimesRecord);
		if (allocationRecord == null) {
			return null;
		}
		if (allocationRecordModifier != null) {
			allocationRecordModifier.modifyAllocationRecord(allocationRecord);
		}
		return allocate(allocationRecord);
	}

	@Override
	@Nullable
	public AllocationRecord createAllocationRecord(final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan plan, final IPortTimesRecord portTimesRecord) {

		final long minEndVolumeInM3 = plan.getRemainingHeelInM3();
		// Default to min volume, but can be updated later
		long maxEndVolumeInM3 = minEndVolumeInM3;

		// Rough estimate of required array size
		final IDetailsSequenceElement[] sequence = plan.getSequence();
		final int numElements = sequence.length / 2 + 1;
		final List<@NonNull IPortSlot> slots = new ArrayList<>(numElements);
		final List<@NonNull Long> minVolumesInM3 = new ArrayList<>(numElements);
		final List<@NonNull Long> maxVolumesInM3 = new ArrayList<>(numElements);
		final List<@NonNull Long> minVolumesInMMBtu = new ArrayList<>(numElements);
		final List<@NonNull Long> maxVolumesInMMBtu = new ArrayList<>(numElements);
		final List<@NonNull Integer> slotCV = new ArrayList<>(numElements);

		boolean foundALoad = false;

		// These handle current special cases around FOB/DES
		// Long transferVolume = null;
		IVessel nominatedVessel = null;
		final int adjust = plan.isIgnoreEnd() ? 1 : 0;
		// Assume true, unless a slot has said otherwise
		boolean hasActuals = false;

		int cargoCV = 0;
		for (int i = 0; i < sequence.length - adjust; ++i) {
			final IDetailsSequenceElement element = sequence[i];

			if (element instanceof PortDetails) {
				final PortDetails pd = (PortDetails) element;
				final IPortSlot slot = pd.getOptions().getPortSlot();

				// Special case for FOB/DES
				// Need better bit#
				if (slot instanceof StartPortSlot) {
					continue;
				}
				if (slot instanceof ILoadOption) {
					slots.add(slot);
					final ILoadOption loadOption = (ILoadOption) slot;
					if (actualsDataProvider.hasActuals(slot)) {
						// Do not mark has actuals as true here, wait for discharge
						// hasActuals = true;
						cargoCV = actualsDataProvider.getCVValue(slot);
						slotCV.add(cargoCV);

						minVolumesInM3.add(actualsDataProvider.getVolumeInM3(slot));
						maxVolumesInM3.add(actualsDataProvider.getVolumeInM3(slot));
						minVolumesInMMBtu.add(actualsDataProvider.getVolumeInMMBtu(slot));
						maxVolumesInMMBtu.add(actualsDataProvider.getVolumeInMMBtu(slot));
					} else {
						cargoCV = loadOption.getCargoCVValue();
						// the mmbtu and m3 values have been set in the transformer, so no conversion needed here
						minVolumesInMMBtu.add(loadOption.getMinLoadVolumeMMBTU());
						maxVolumesInMMBtu.add(loadOption.getMaxLoadVolumeMMBTU());
						minVolumesInM3.add(loadOption.getMinLoadVolume());
						maxVolumesInM3.add(loadOption.getMaxLoadVolume());
					}

					foundALoad = true;
					if (!(loadOption instanceof ILoadSlot)) {
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
					}
				} else if (slot instanceof IDischargeOption) {
					slots.add(slot);
					final IDischargeOption dischargeOption = (IDischargeOption) slot;
					if (actualsDataProvider.hasActuals(slot)) {
						hasActuals = true;
						cargoCV = actualsDataProvider.getCVValue(slot);

						minVolumesInM3.add(actualsDataProvider.getVolumeInM3(slot));
						maxVolumesInM3.add(actualsDataProvider.getVolumeInM3(slot));
						minVolumesInMMBtu.add(actualsDataProvider.getVolumeInMMBtu(slot));
						maxVolumesInMMBtu.add(actualsDataProvider.getVolumeInMMBtu(slot));

					} else {
						minVolumesInM3.add(dischargeOption.getMinDischargeVolume(cargoCV));
						maxVolumesInM3.add(dischargeOption.getMaxDischargeVolume(cargoCV));
						minVolumesInMMBtu.add(dischargeOption.getMinDischargeVolumeMMBTU(cargoCV));
						maxVolumesInMMBtu.add(dischargeOption.getMaxDischargeVolumeMMBTU(cargoCV));
					}
					if (!(dischargeOption instanceof IDischargeSlot)) {
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
					}
				} else if (slot instanceof IHeelOptionSupplierPortSlot) {
					slots.add(slot);
					final IHeelOptionSupplierPortSlot heelOptionsPortSlot = (IHeelOptionSupplierPortSlot) slot;
					final IHeelOptionSupplier heelOptions = heelOptionsPortSlot.getHeelOptionsSupplier();
					cargoCV = heelOptions.getHeelCVValue();

					minVolumesInM3.add(heelOptions.getMinimumHeelAvailableInM3());
					maxVolumesInM3.add(heelOptions.getMaximumHeelAvailableInM3());

					minVolumesInMMBtu.add(Calculator.convertM3ToMMBTuWithOverflowProtection(heelOptions.getMinimumHeelAvailableInM3(), cargoCV));
					maxVolumesInMMBtu.add(Calculator.convertM3ToMMBTuWithOverflowProtection(heelOptions.getMaximumHeelAvailableInM3(), cargoCV));
				} else {
					minVolumesInM3.add(0l);
					maxVolumesInM3.add(0l);
					minVolumesInMMBtu.add(0l);
					maxVolumesInMMBtu.add(0l);
				}
				slotCV.add(cargoCV);
			}
		}

		// Only handle cargoes for now
		if (!foundALoad) {
			return null;
		}

		IPortSlot returnSlot = null;
		final IDetailsSequenceElement lastElement = sequence[sequence.length - 1];
		if (lastElement instanceof PortDetails) {
			final PortDetails portDetails = (PortDetails) lastElement;
			final IPortSlot slot = portDetails.getOptions().getPortSlot();
			returnSlot = slot;
			if (slot instanceof IHeelOptionConsumerPortSlot) {
				final IHeelOptionConsumerPortSlot heelOptionsSlotSlot = (IHeelOptionConsumerPortSlot) slot;
				final IHeelOptionConsumer heelOptions = heelOptionsSlotSlot.getHeelOptionsConsumer();

				maxEndVolumeInM3 = heelOptions.getMaximumHeelAcceptedInM3();
			}
		}

		// TODO: Assert start/end heel match actuals records.
		final AllocationRecord allocationRecord = new AllocationRecord(vesselAvailability, plan, vesselStartTime, plan.getStartingHeelInM3(), plan.getLNGFuelVolume(), minEndVolumeInM3,
				maxEndVolumeInM3, slots, portTimesRecord, returnSlot, minVolumesInM3, maxVolumesInM3, minVolumesInMMBtu, maxVolumesInMMBtu, slotCV);

		if (hasActuals) {
			allocationRecord.allocationMode = AllocationMode.Actuals;
		}
		allocationRecord.nominatedVessel = nominatedVessel;

		return allocationRecord;
	}
}
