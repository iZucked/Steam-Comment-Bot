/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
public class AllocationAnnotation implements IAllocationAnnotation {
	public class SlotAllocationAnnotation {
		// public long volumeInM3;
		public int pricePerM3;
		public int startTime;
	}

	private final Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<IPortSlot, SlotAllocationAnnotation>();
	private List<IPortSlot> slots = new ArrayList<IPortSlot>(2);

	private long fuelVolumeInM3;

	private long remainingHeelVolumeInM3;

	private long dischargeVolumeInM3;

	@Override
	public ILoadOption getLoadOption() {
		assert(slots.size() == 2);
		return (ILoadOption) slots.get(0);
	}

	@Override
	public IDischargeOption getDischargeOption() {
		assert(slots.size() == 2);
		return (IDischargeOption) slots.get(1);
	}

	@Override
	public long getDischargeVolumeInM3() {
		return dischargeVolumeInM3;
	}

	public void setDischargeVolumeInM3(final long dischargeVolume) {
		this.dischargeVolumeInM3 = dischargeVolume;
	}

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		this.fuelVolumeInM3 = fuelVolume;
	}

	@Override
	public long getLoadVolumeInM3() {
		return getFuelVolumeInM3() + getDischargeVolumeInM3() + getRemainingHeelVolumeInM3();
	}

	@Override
	public String toString() {
		return getLoadOption().getId() + "@" + getSlotTime(getLoadOption()) + " to " + getDischargeOption().getId() + "@" + getSlotTime(getDischargeOption()) + ", loaded " + getLoadVolumeInM3() + ", used " + getFuelVolumeInM3()
				+ " for fuel, discharged " + getDischargeVolumeInM3() + ", remaining heel " + getRemainingHeelVolumeInM3();
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

	private SlotAllocationAnnotation getOrCreateSlotAllocation(IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			allocation = new SlotAllocationAnnotation();
			slotAllocations.put(slot, allocation);
		}
		return allocation;
	}
	
	@Override
	public int getSlotTime(IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}
	
	public void setSlotTime(IPortSlot slot, int time) {
		getOrCreateSlotAllocation(slot).startTime = time;
	}

	@Override
	public int getSlotPricePerM3(IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.pricePerM3;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}
	
	public void setSlotPricePerM3(IPortSlot slot, int price) {
		getOrCreateSlotAllocation(slot).pricePerM3 = price;
	}
	
}
