/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
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
	public IAllocationAnnotation allocate(final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IPortTimesRecord portTimesRecord) {
		final AllocationRecord allocationRecord = createAllocationRecord(vessel, vesselStartTime, plan, portTimesRecord);
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
	public AllocationRecord createAllocationRecord(final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final IPortTimesRecord portTimesRecord) {

		final long minEndVolumeInM3 = plan.getRemainingHeelInM3();

		// Rough estimate of required array size
		final IDetailsSequenceElement[] sequence = plan.getSequence();
		final int numElements = sequence.length / 2 + 1;
		final List<IPortSlot> slots = new ArrayList<>(numElements);
		final List<Long> minVolumesInM3 = new ArrayList<>(numElements);
		final List<Long> maxVolumesInM3 = new ArrayList<>(numElements);
		final List<Long> minVolumesInMMBtu = new ArrayList<>(numElements);
		final List<Long> maxVolumesInMMBtu = new ArrayList<>(numElements);
		final List<Integer> slotCV = new ArrayList<>(numElements);

		boolean foundALoad = false;

		// These handle current special cases around FOB/DES
		// Long transferVolume = null;
		IVessel nominatedVessel = null;
		final int adjust = plan.isIgnoreEnd() ? 1 : 0;
		// Assume true, unless a slot has said otherwise
		boolean hasActuals = false;

		// how to get this port slot?
		IPortSlot returnSlot = null;
		int cargoCV = 0;
		for (int i = 0; i < sequence.length - adjust; ++i) {
			final IDetailsSequenceElement element = sequence[i];

			if (element instanceof PortDetails) {
				final PortDetails pd = (PortDetails) element;
				final IPortSlot slot = pd.getOptions().getPortSlot();

				// Update each time
				returnSlot = slot;

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

						minVolumesInM3.add(loadOption.getMinLoadVolume());
						maxVolumesInM3.add(loadOption.getMaxLoadVolume());
						minVolumesInMMBtu.add(Calculator.convertMMBTuToM3(loadOption.getMinLoadVolume(), cargoCV));
						maxVolumesInMMBtu.add(Calculator.convertMMBTuToM3(loadOption.getMaxLoadVolume(), cargoCV));
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
						minVolumesInM3.add(dischargeOption.getMinDischargeVolume());
						maxVolumesInM3.add(dischargeOption.getMaxDischargeVolume());
						minVolumesInMMBtu.add(Calculator.convertMMBTuToM3(dischargeOption.getMinDischargeVolume(), cargoCV));
						maxVolumesInMMBtu.add(Calculator.convertMMBTuToM3(dischargeOption.getMaxDischargeVolume(), cargoCV));
					}
					if (!(dischargeOption instanceof IDischargeSlot)) {
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
					}
				} else if (slot instanceof IHeelOptionsPortSlot) {
					slots.add(slot);
					final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) slot;
					final IHeelOptions heelOptions = heelOptionsPortSlot.getHeelOptions();
					cargoCV = heelOptions.getHeelCVValue();

					minVolumesInM3.add(heelOptions.getHeelLimit());
					maxVolumesInM3.add(heelOptions.getHeelLimit());
					if (cargoCV != 0) {
						minVolumesInMMBtu.add(Calculator.convertMMBTuToM3(heelOptions.getHeelLimit(), cargoCV));
						maxVolumesInMMBtu.add(Calculator.convertMMBTuToM3(heelOptions.getHeelLimit(), cargoCV));
					} else {
						minVolumesInMMBtu.add(0L);
						maxVolumesInMMBtu.add(0L);
					}
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

		// TODO: Assert start/end heel match actuals records.
		final AllocationRecord allocationRecord = new AllocationRecord(vessel, plan, vesselStartTime, plan.getStartingHeelInM3(), plan.getLNGFuelVolume(), minEndVolumeInM3, slots, portTimesRecord,
				returnSlot, minVolumesInM3, maxVolumesInM3, minVolumesInMMBtu, maxVolumesInMMBtu, slotCV);

		if (hasActuals) {
			allocationRecord.allocationMode = AllocationMode.Actuals;
		}
		allocationRecord.nominatedVessel = nominatedVessel;

		return allocationRecord;
	}

}
