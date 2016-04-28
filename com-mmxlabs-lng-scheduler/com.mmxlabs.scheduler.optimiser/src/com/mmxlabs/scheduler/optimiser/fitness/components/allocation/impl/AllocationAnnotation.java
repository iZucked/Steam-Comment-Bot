/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

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

		@Override
		public boolean equals(Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof SlotAllocationAnnotation) {
				final SlotAllocationAnnotation other = (SlotAllocationAnnotation) obj;
				return volumeInM3 == other.volumeInM3 //
						&& volumeInMMBTu == other.volumeInMMBTu //
						&& cargoCV == other.cargoCV //
						&& startTime == other.startTime //
						&& duration == other.duration;

			}

			return false;
		}
	}

	private final @NonNull Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<>();
	private final @NonNull List<@NonNull IPortSlot> slots = new ArrayList<>(2);

	private long fuelVolumeInM3;

	private long startHeelVolumeInM3;
	private long remainingHeelVolumeInM3;

	private int firstSlotTime = Integer.MAX_VALUE;
	private IPortSlot firstPortSlot = null;
	private IPortSlot returnPortSlot = null;

	public AllocationAnnotation() {

	}

	public AllocationAnnotation(@NonNull final IPortTimesRecord portTimesRecord) {
		for (final IPortSlot portSlot : portTimesRecord.getSlots()) {
			assert portSlot != null;
			getSlots().add(portSlot);
			setSlotTime(portSlot, portTimesRecord.getSlotTime(portSlot));
			setSlotTime(portSlot, portTimesRecord.getSlotDuration(portSlot));
		}
		final IPortSlot returnSlot = portTimesRecord.getReturnSlot();
		if (returnSlot != null) {
			setReturnSlotTime(returnSlot, portTimesRecord.getSlotDuration(returnSlot));
		}
	}

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
	public List<@NonNull IPortSlot> getSlots() {
		return slots;
	}

	private SlotAllocationAnnotation getOrCreateSlotAllocation(final @NonNull IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			allocation = new SlotAllocationAnnotation();
			slotAllocations.put(slot, allocation);
		}
		return allocation;
	}

	@Override
	public int getSlotTime(final @NonNull IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public void setSlotTime(final @NonNull IPortSlot slot, final int time) {
		getOrCreateSlotAllocation(slot).startTime = time;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotTime = time;
			firstPortSlot = slot;
		}
	}

	public void setReturnSlotTime(final @NonNull IPortSlot slot, final int time) {
		setSlotTime(slot, time);
		slots.remove(returnPortSlot);
		returnPortSlot = slot;
	}

	@Override
	public int getSlotDuration(final @NonNull IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.duration;
		}
		return 0;
	}

	@Override
	public void setSlotDuration(final @NonNull IPortSlot slot, final int duration) {
		getOrCreateSlotAllocation(slot).duration = duration;
	}

	@Override
	public long getSlotVolumeInM3(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.volumeInM3;
		}

		return 0;
	}

	public void setSlotVolumeInM3(final @NonNull IPortSlot slot, final long volumeInM3) {
		getOrCreateSlotAllocation(slot).volumeInM3 = volumeInM3;
	}

	@Override
	public long getSlotVolumeInMMBTu(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.volumeInMMBTu;
		}

		return 0;
	}

	public void setSlotVolumeInMMBTu(final @NonNull IPortSlot slot, final long volumeInMMBTu) {
		getOrCreateSlotAllocation(slot).volumeInMMBTu = volumeInMMBTu;
	}

	@Override
	public int getSlotCargoCV(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.cargoCV;
		}

		return 0;
	}

	public void setSlotCargoCV(final @NonNull IPortSlot slot, final int cargoCV) {
		getOrCreateSlotAllocation(slot).cargoCV = cargoCV;
	}

	@Override
	public long getStartHeelVolumeInM3() {
		return startHeelVolumeInM3;
	}

	public void setStartHeelVolumeInM3(final long startHeelVolumeInM3) {
		this.startHeelVolumeInM3 = startHeelVolumeInM3;
	}

	@Override
	public IPortSlot getFirstSlot() {
		final IPortSlot pFirstPortSlot = firstPortSlot;
		if (pFirstPortSlot == null) {
			throw new IllegalStateException("#getFirstSlot called before slots have been added");
		}
		return pFirstPortSlot;
	}

	@Override
	public int getFirstSlotTime() {
		return firstSlotTime;
	}

	@Override
	public IPortSlot getReturnSlot() {
		return returnPortSlot;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof AllocationAnnotation) {
			AllocationAnnotation other = (AllocationAnnotation) obj;
			return this.firstSlotTime == other.firstSlotTime //
					&& this.startHeelVolumeInM3 == other.startHeelVolumeInM3 //
					&& this.fuelVolumeInM3 == other.fuelVolumeInM3 //
					&& this.remainingHeelVolumeInM3 == other.remainingHeelVolumeInM3 //
					&& Objects.equal(this.returnPortSlot, other.returnPortSlot) //
					&& Objects.equal(this.slotAllocations, other.slotAllocations)//
					&& Objects.equal(this.slots, other.slots);
		}

		return false;
	}
}
