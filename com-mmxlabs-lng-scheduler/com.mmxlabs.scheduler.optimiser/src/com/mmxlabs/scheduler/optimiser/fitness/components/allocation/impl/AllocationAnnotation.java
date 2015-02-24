/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * @author hinton
 * 
 */
public final class AllocationAnnotation implements IAllocationAnnotation {

	public class SlotAllocationAnnotation {
		public long volumeInM3;
		public long volumeInMMBTu;
		public int cargoCV;
		public int startTime;
		public int duration;
	}

	private final Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<IPortSlot, SlotAllocationAnnotation>();
	private final List<IPortSlot> slots = new ArrayList<IPortSlot>(2);

	private long fuelVolumeInM3;

	private long startHeelVolumeInM3;
	private long remainingHeelVolumeInM3;

	private int firstSlotTime = Integer.MAX_VALUE;
	private IPortSlot firstPortSlot = null;
	private IPortSlot returnPortSlot = null;

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		this.fuelVolumeInM3 = fuelVolume;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final String slotFormat = "%s@%d (%s %d)";
		boolean firstLoop = true;
		for (final IPortSlot slot : slots) {
			final SlotAllocationAnnotation slotAllocation = slotAllocations.get(slot);
			if (!firstLoop) {
				builder.append(" to ");
			} else {
				firstLoop = false;
			}

			final String action = (slot instanceof IDischargeOption) ? "discharged" : (slot instanceof ILoadOption ? "loaded" : "???");
			final long volume = getSlotVolumeInM3(slot);
			// long volume = (slot instanceof IDischargeOption) ? getDischargeVolumeInM3() : (slot instanceof ILoadOption ? getLoadVolumeInM3() : -1);
			builder.append(String.format(slotFormat, slot.getId(), slotAllocation.startTime, action, volume));
		}

		final String endFormat = ", used %d for fuel, remaining heel %d";
		builder.append(String.format(endFormat, getFuelVolumeInM3(), getRemainingHeelVolumeInM3()));
		return builder.toString();
	}

	@Override
	public long getRemainingHeelVolumeInM3() {
		return remainingHeelVolumeInM3;
	}

	public void setRemainingHeelVolumeInM3(final long remainingHeelVolumeInM3) {
		this.remainingHeelVolumeInM3 = remainingHeelVolumeInM3;
	}

	@Override
	public List<IPortSlot> getSlots() {
		return slots;
	}

	private SlotAllocationAnnotation getOrCreateSlotAllocation(final IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			allocation = new SlotAllocationAnnotation();
			slotAllocations.put(slot, allocation);
		}
		return allocation;
	}

	@Override
	public int getSlotTime(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public void setSlotTime(final IPortSlot slot, final int time) {
		getOrCreateSlotAllocation(slot).startTime = time;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotTime = time;
			firstPortSlot = slot;
		}
	}

	public void setReturnSlotTime(final IPortSlot slot, final int time) {
		setSlotTime(slot, time);
		returnPortSlot = slot;
	}

	@Override
	public int getSlotDuration(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.duration;
		}
		return 0;
	}

	@Override
	public void setSlotDuration(final IPortSlot slot, final int duration) {
		getOrCreateSlotAllocation(slot).duration = duration;
	}

	@Override
	public long getSlotVolumeInM3(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.volumeInM3;
		}

		return 0;
	}

	public void setSlotVolumeInM3(final IPortSlot slot, final long volumeInM3) {
		getOrCreateSlotAllocation(slot).volumeInM3 = volumeInM3;
	}

	@Override
	public long getSlotVolumeInMMBTu(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.volumeInMMBTu;
		}

		return 0;
	}

	public void setSlotVolumeInMMBTu(final IPortSlot slot, final long volumeInMMBTu) {
		getOrCreateSlotAllocation(slot).volumeInMMBTu = volumeInMMBTu;
	}

	@Override
	public int getSlotCargoCV(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.cargoCV;
		}

		return 0;
	}

	public void setSlotCargoCV(final IPortSlot slot, final int cargoCV) {
		getOrCreateSlotAllocation(slot).cargoCV = cargoCV;
	}

	@Override
	public long getStartHeelVolumeInM3() {
		return startHeelVolumeInM3;
	}

	public void setStartHeelVolumeInM3(long startHeelVolumeInM3) {
		this.startHeelVolumeInM3 = startHeelVolumeInM3;
	}

	@Override
	public IPortSlot getFirstSlot() {
		return firstPortSlot;
	}

	@Override
	public int getFirstSlotTime() {
		return firstSlotTime;
	}

	@Override
	public IPortSlot getReturnSlot() {
		return returnPortSlot;
	}
}
