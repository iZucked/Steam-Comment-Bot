/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
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
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan.HeelType;

/**
 * Base class for allocating load/discharge volumes; doesn't implement the solve() method, but does do various book-keeping tasks.
 * 
 * @author hinton
 * @since 6.0
 * 
 */
public abstract class BaseVolumeAllocator implements IVolumeAllocator {
	@Inject(optional = true)
	private ICustomVolumeAllocator allocationRecordModifier;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	public BaseVolumeAllocator() {
		super();
	}

	@Override
	@Nullable
	public IAllocationAnnotation allocate(final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final List<Integer> arrivalTimes) {
		final AllocationRecord allocationRecord = createAllocationRecord(vessel, vesselStartTime, plan, arrivalTimes);
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
	public AllocationRecord createAllocationRecord(final IVessel vessel, final int vesselStartTime, final VoyagePlan plan, final List<Integer> arrivalTimes) {

		final long startVolumeInM3 = plan.getStartingHeelInM3();
		final long requiredFuelVolumeInM3 = plan.getLNGFuelVolume();
		final long minEndVolumeInM3 = plan.getRemainingHeelType() == HeelType.END ? plan.getRemainingHeelInM3() : 0;
		final long dischargeHeelInM3 = plan.getRemainingHeelType() == HeelType.DISCHARGE ? plan.getRemainingHeelInM3() : 0;

		// Rough estimate of required array size
		final IDetailsSequenceElement[] sequence = plan.getSequence();
		final int numElements = sequence.length / 2 + 1;
		final List<IPortSlot> slots = new ArrayList<>(numElements);
		final List<Long> minVolumes = new ArrayList<>(numElements);
		final List<Long> maxVolumes = new ArrayList<>(numElements);

		boolean foundALoad = false;

		// These handle current special cases around FOB/DES
		Integer transferTime = null;
		Long transferVolume = null;
		IVessel nominatedVessel = null;
		for (int i = 0; i < sequence.length - 1; ++i) {
			final IDetailsSequenceElement element = sequence[i];

			if (element instanceof PortDetails) {
				final PortDetails pd = (PortDetails) element;
				final IPortSlot slot = pd.getOptions().getPortSlot();
				// Special case for FOB/DES
				// Need better bit#
				if (slot instanceof StartPortSlot) {
					continue;
				}
				slots.add(slot);
				if (slot instanceof ILoadOption) {
					final ILoadOption loadOption = (ILoadOption) slot;
					minVolumes.add(loadOption.getMinLoadVolume());
					maxVolumes.add(loadOption.getMaxLoadVolume());
					foundALoad = true;
					if (!(loadOption instanceof ILoadSlot)) {
						transferTime = arrivalTimes.get(i);
						if (transferVolume == null) {
							transferVolume = loadOption.getMaxLoadVolume();
						} else {
							transferVolume = Math.min(transferVolume, loadOption.getMaxLoadVolume());
						}
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
					}
				} else if (slot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) slot;
					minVolumes.add(dischargeOption.getMinDischargeVolume());
					maxVolumes.add(dischargeOption.getMaxDischargeVolume());
					if (!(dischargeOption instanceof IDischargeSlot)) {
						transferTime = arrivalTimes.get(i);
						if (transferVolume == null) {
							transferVolume = dischargeOption.getMaxDischargeVolume();
						} else {
							transferVolume = Math.min(transferVolume, dischargeOption.getMaxDischargeVolume());
						}
						nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
					}
				} else if (slot instanceof IHeelOptionsPortSlot) {
					final IHeelOptionsPortSlot heelOptionsPortSlot = (IHeelOptionsPortSlot) slot;
					final IHeelOptions heelOptions = heelOptionsPortSlot.getHeelOptions();
					minVolumes.add(heelOptions.getHeelLimit());
					maxVolumes.add(heelOptions.getHeelLimit());
				} else {
					minVolumes.add(0l);
					maxVolumes.add(0l);
				}
			}
		}

		// Only handle cargoes for now
		if (!foundALoad) {
			return null;
		}

		final List<Integer> slotTimes;
		final IVessel slotVessel = nominatedVessel != null ? nominatedVessel : vessel;
		if (transferTime != null) {
			slotTimes = new ArrayList<Integer>(sequence.length - 1);
			for (int i = 0; i < sequence.length - 1; ++i) {
				slotTimes.add(transferTime);
			}
		} else {
			slotTimes = new ArrayList<Integer>(arrivalTimes);
		}

		final AllocationRecord allocationRecord = new AllocationRecord(slotVessel, startVolumeInM3, requiredFuelVolumeInM3, minEndVolumeInM3, dischargeHeelInM3, slots, slotTimes, minVolumes,
				maxVolumes);
		if (allocationRecordModifier != null) {
			allocationRecordModifier.modifyAllocationRecord(allocationRecord);
		}

		return allocationRecord;
	}

}
