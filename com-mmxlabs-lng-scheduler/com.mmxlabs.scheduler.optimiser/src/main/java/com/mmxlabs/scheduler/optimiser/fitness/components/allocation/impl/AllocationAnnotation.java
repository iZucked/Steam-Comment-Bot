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
	//private ILoadOption loadSlot;
	//private IDischargeOption dischargeSlot;
	private final Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<IPortSlot, SlotAllocationAnnotation>();
	private final List<IPortSlot> slots = new ArrayList<IPortSlot>();
	//private List<ILoadOption> loadSlots = new ArrayList<ILoadOption>();
	//private List<IDischargeOption> dischargeSlots = new ArrayList<IDischargeOption>();

	private long fuelVolumeInM3;

	private long remainingHeelVolumeInM3;

	public class SlotAllocationAnnotation {
		public long volumeInM3;
		public int pricePerM3;
		public int startTime;
	}
	
	@Override
	public List<IPortSlot> getSlots() {
		return slots;
	}
	
	/*
	@Override
	public ILoadOption getLoadOption() {
		return loadSlot;
	}

	public void setLoadSlot(final ILoadOption loadSlot) {
		this.loadSlot = loadSlot;
	}

	@Override
	public IDischargeOption getDischargeOption() {
		return dischargeSlot;
	}

	public void setDischargeSlot(final IDischargeOption dischargeSlot) {
		this.dischargeSlot = dischargeSlot;
	}*/

	/* 
	@Override
	public long getDischargeVolumeInM3() {
		return dischargeVolumeInM3;
	}

	public void setDischargeVolumeInM3(final long dischargeVolume) {
		this.dischargeVolumeInM3 = dischargeVolume;
	} */
	
	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		this.fuelVolumeInM3 = fuelVolume;
	}

	/*
	@Override
	public long getLoadVolumeInM3() {
		return getFuelVolumeInM3() + getDischargeVolumeInM3() + getRemainingHeelVolumeInM3();
	}
	*/
	
	/*
	@Override
	public int getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(final int loadTime) {
		this.loadTime = loadTime;
	}

	@Override
	public int getDischargeTime() {
		return dischargeTime;
	}

	public void setDischargeTime(final int dischargeTime) {
		this.dischargeTime = dischargeTime;
	}
	*/
	
	/*
	@Override
	public int getLoadPricePerM3() {
		return loadPricePerM3;
	}

	public void setLoadPricePerM3(final int loadM3Price) {
		this.loadPricePerM3 = loadM3Price;
	}

	@Override
	public int getDischargePricePerM3() {
		return dischargePricePerM3;
	}

	public void setDischargePricePerM3(final int dischargeM3Price) {
		this.dischargePricePerM3 = dischargeM3Price;
	}
	*/

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String slotFormat = "%s@%d (%s %d)";
		boolean firstLoop = true;
		for (IPortSlot slot: slots) {
			SlotAllocationAnnotation slotAllocation = slotAllocations.get(slot);
			if (!firstLoop) {
				builder.append(" to ");				
			}
			else {
				firstLoop = false;
			}
			
			String action = (slot instanceof IDischargeOption) ? "discharged" : (slot instanceof ILoadOption ? "loaded" : "???");
			builder.append(String.format(slotFormat, slot.getId(), slotAllocation.startTime, action, slotAllocation.volumeInM3));
		}
		
		String endFormat = ", used %d for fuel, remaining heel %d";
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
	
	@Override
	public long getSlotVolumeInM3(IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.volumeInM3;
		}
		// TODO: throw an exception instead of returning magic value
		return -1;
	}
	
	public void setSlotVolumeInM3(IPortSlot slot, long volume) {
		getOrCreateSlotAllocation(slot).volumeInM3 = volume;
	}

	@Override
	public ILoadOption getFirstLoadSlot() {
		for (IPortSlot slot: slots) {
			if (slot instanceof ILoadOption) {
				return (ILoadOption) slot;
			}
		}
		return null;
	}

}
