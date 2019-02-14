/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;

/**
 * @author hinton
 * 
 */
public final class AllocationAnnotation implements IAllocationAnnotation {
	private boolean locked;

	public static class SlotAllocationAnnotation {
		public long commercialVolumeInM3;
		public long commercialVolumeInMMBTu;
		public long physicalVolumeInM3;
		public long physicalVolumeInMMBTu;
		public int cargoCV;
		public int startTime;
		public int duration;
		public int extraIdleTime;
		public IRouteOptionBooking routeOptionBooking;
		public AvailableRouteChoices nextVoyageRouteChoice;
		public PanamaPeriod panamaPeriod;

		@Override
		public boolean equals(final Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof SlotAllocationAnnotation) {
				final SlotAllocationAnnotation other = (SlotAllocationAnnotation) obj;
				return commercialVolumeInM3 == other.commercialVolumeInM3 //
						&& commercialVolumeInMMBTu == other.commercialVolumeInMMBTu //
						&& cargoCV == other.cargoCV //
						&& startTime == other.startTime //
						&& duration == other.duration //
						&& extraIdleTime == other.extraIdleTime //
						&& nextVoyageRouteChoice == other.nextVoyageRouteChoice //
						&& Objects.equal(routeOptionBooking, other.routeOptionBooking);
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

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		assert !locked;
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
			final long volume = getCommercialSlotVolumeInM3(slot);
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
		assert !locked;
		this.remainingHeelVolumeInM3 = remainingHeelVolumeInM3;
	}

	@Override
	public List<@NonNull IPortSlot> getSlots() {
		return slots;
	}

	private SlotAllocationAnnotation getOrCreateSlotAllocation(final @NonNull IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			assert !locked;
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
		assert !locked;
		getOrCreateSlotAllocation(slot).startTime = time;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotTime = time;
			firstPortSlot = slot;
		}
	}

	@Override
	public int getSlotExtraIdleTime(final @NonNull IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.extraIdleTime;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public void setSlotExtraIdleTime(final @NonNull IPortSlot slot, final int extraIdleTime) {
		assert !locked;
		getOrCreateSlotAllocation(slot).extraIdleTime = extraIdleTime;
	}

	public void setReturnSlotTime(final @NonNull IPortSlot slot, final int time) {
		assert !locked;
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
		assert !locked;
		getOrCreateSlotAllocation(slot).duration = duration;
	}

	@Override
	public long getCommercialSlotVolumeInM3(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.commercialVolumeInM3;
		}

		return 0;
	}

	public void setCommercialSlotVolumeInM3(final @NonNull IPortSlot slot, final long volumeInM3) {
		assert !locked;
		getOrCreateSlotAllocation(slot).commercialVolumeInM3 = volumeInM3;
	}

	@Override
	public long getCommercialSlotVolumeInMMBTu(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.commercialVolumeInMMBTu;
		}

		return 0;
	}

	public void setPhysicalSlotVolumeInMMBTu(final @NonNull IPortSlot slot, final long volumeInMMBTu) {
		assert !locked;
		getOrCreateSlotAllocation(slot).physicalVolumeInMMBTu = volumeInMMBTu;
	}

	@Override
	public long getPhysicalSlotVolumeInM3(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.physicalVolumeInM3;
		}

		return 0;
	}

	public void setPhysicalSlotVolumeInM3(final @NonNull IPortSlot slot, final long volumeInM3) {
		assert !locked;
		getOrCreateSlotAllocation(slot).physicalVolumeInM3 = volumeInM3;
	}

	@Override
	public long getPhysicalSlotVolumeInMMBTu(final @NonNull IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.physicalVolumeInMMBTu;
		}

		return 0;
	}

	public void setCommercialSlotVolumeInMMBTu(final @NonNull IPortSlot slot, final long volumeInMMBTu) {
		assert !locked;
		getOrCreateSlotAllocation(slot).commercialVolumeInMMBTu = volumeInMMBTu;
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
		assert !locked;
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
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof AllocationAnnotation) {
			final AllocationAnnotation other = (AllocationAnnotation) obj;
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

	@Override
	public boolean isCacheLocked() {
		return locked;
	}

	@Override
	public void setCacheLocked(final boolean locked) {
		assert !this.locked;
		this.locked = locked;
	}

	@Override
	public @Nullable IRouteOptionBooking getRouteOptionBooking(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.routeOptionBooking;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public void setRouteOptionBooking(final IPortSlot slot, final IRouteOptionBooking routeOptionBooking) {
		getOrCreateSlotAllocation(slot).routeOptionBooking = routeOptionBooking;

	}

	@Override
	public void setSlotNextVoyageOptions(final IPortSlot slot, final AvailableRouteChoices nextVoyageRoute, final PanamaPeriod panamaPeriod) {
		getOrCreateSlotAllocation(slot).nextVoyageRouteChoice = nextVoyageRoute;
		getOrCreateSlotAllocation(slot).panamaPeriod = panamaPeriod;
	}

	@Override
	public AvailableRouteChoices getSlotNextVoyageOptions(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.nextVoyageRouteChoice;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public PanamaPeriod getSlotNextVoyagePanamaPeriod(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.panamaPeriod;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

}
