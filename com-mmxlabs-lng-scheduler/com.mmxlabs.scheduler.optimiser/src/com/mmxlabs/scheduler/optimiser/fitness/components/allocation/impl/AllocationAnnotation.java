/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * @author hinton
 * @since 5.0
 * 
 */
public class AllocationAnnotation implements IAllocationAnnotation {
	/**
	 * @since 5.0
	 */
	public class SlotAllocationAnnotation {
		public long volumeInM3;
		public int pricePerM3;
		public int pricePerMMBTu;
		public int startTime;
	}

	private final Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<IPortSlot, SlotAllocationAnnotation>();
	private final List<IPortSlot> slots = new ArrayList<IPortSlot>(2);

	private long fuelVolumeInM3;

	private long remainingHeelVolumeInM3;

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		this.fuelVolumeInM3 = fuelVolume;
	}

	/*
	 * @Override public long getLoadVolumeInM3() { return getFuelVolumeInM3() + getDischargeVolumeInM3() + getRemainingHeelVolumeInM3(); }
	 */

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

	/**
	 * @since 5.0
	 */
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

	/**
	 * @since 5.0
	 */
	@Override
	public int getSlotTime(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}

	/**
	 * @since 5.0
	 */
	public void setSlotTime(final IPortSlot slot, final int time) {
		getOrCreateSlotAllocation(slot).startTime = time;
	}

	/**
	 * @since 5.0
	 */
	@Override
	public int getSlotPricePerM3(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.pricePerM3;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}
	
	@Override
	public int getSlotPricePerMMBTu(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.pricePerMMBTu;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}

	/**
	 * @since 5.0
	 */
	public void setSlotPricePerM3(final IPortSlot slot, final int price) {
		getOrCreateSlotAllocation(slot).pricePerM3 = price;
	}
	
	/**
	 * @since 5.0
	 */
	public void setSlotPricePerMMBTu(final IPortSlot slot, final int price) {
		getOrCreateSlotAllocation(slot).pricePerMMBTu = price;
	}

	/**
	 * @since 6.0
	 */
	@Override
	public long getSlotVolumeInM3(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.volumeInM3;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}

	/**
	 * @since 5.0
	 */
	public void setSlotVolumeInM3(final IPortSlot slot, final long volume) {
		getOrCreateSlotAllocation(slot).volumeInM3 = volume;
	}

}
