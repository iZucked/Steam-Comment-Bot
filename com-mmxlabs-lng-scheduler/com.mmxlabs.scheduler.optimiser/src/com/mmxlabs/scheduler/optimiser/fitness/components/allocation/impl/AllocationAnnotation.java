/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * @author hinton
 * 
 */
@NonNullByDefault
public final class AllocationAnnotation extends AbstractWriteLockable implements IAllocationAnnotation {

	private final IPortTimesRecord portTimesRecord;

	public static class SlotAllocationAnnotation {
		public long commercialVolumeInM3;
		public long commercialVolumeInMMBTu;
		public long physicalVolumeInM3;
		public long physicalVolumeInMMBTu;
		public int cargoCV;

		@Override
		public boolean equals(final @Nullable Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof SlotAllocationAnnotation) {
				final SlotAllocationAnnotation other = (SlotAllocationAnnotation) obj;
				return commercialVolumeInM3 == other.commercialVolumeInM3 //
						&& commercialVolumeInMMBTu == other.commercialVolumeInMMBTu //
						&& cargoCV == other.cargoCV //
				;
			}

			return false;
		}
	}

	private ImmutableMap<IPortSlot, SlotAllocationAnnotation> slotAllocations = ImmutableMap.of();

	private long fuelVolumeInM3;

	private long startHeelVolumeInM3;
	private long remainingHeelVolumeInM3;

	public AllocationAnnotation(IPortTimesRecord portTimesRecord) {
		this.portTimesRecord = portTimesRecord;
		IWriteLockable.writeLock(portTimesRecord);
	}
	
	public static AllocationAnnotation from(IAllocationAnnotation allocationAnnotation) {
		AllocationAnnotation result = new AllocationAnnotation(allocationAnnotation);
		for (final IPortSlot ps : allocationAnnotation.getSlots()) {
			result.setCommercialSlotVolumeInM3(ps, allocationAnnotation.getCommercialSlotVolumeInM3(ps));
			result.setCommercialSlotVolumeInMMBTu(ps, allocationAnnotation.getCommercialSlotVolumeInMMBTu(ps));
			result.setPhysicalSlotVolumeInM3(ps, allocationAnnotation.getPhysicalSlotVolumeInM3(ps));
			result.setPhysicalSlotVolumeInMMBTu(ps, allocationAnnotation.getPhysicalSlotVolumeInMMBTu(ps));
			result.setSlotCargoCV(ps, allocationAnnotation.getSlotCargoCV(ps));
		}
		result.fuelVolumeInM3 = allocationAnnotation.getFuelVolumeInM3();
		result.startHeelVolumeInM3 = allocationAnnotation.getStartHeelVolumeInM3();
		result.remainingHeelVolumeInM3 = allocationAnnotation.getRemainingHeelVolumeInM3();
		return result;
	}

	@Override
	public long getFuelVolumeInM3() {
		return fuelVolumeInM3;
	}

	public void setFuelVolumeInM3(final long fuelVolume) {
		checkWritable();
		this.fuelVolumeInM3 = fuelVolume;
	}

	@Override
	public long getRemainingHeelVolumeInM3() {
		return remainingHeelVolumeInM3;
	}

	public void setRemainingHeelVolumeInM3(final long remainingHeelVolumeInM3) {
		checkWritable();
		this.remainingHeelVolumeInM3 = remainingHeelVolumeInM3;
	}

	@Override
	public ImmutableList<IPortSlot> getSlots() {
		return portTimesRecord.getSlots();
	}

	private SlotAllocationAnnotation getOrCreateSlotAllocation(final IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			checkWritable();
			allocation = new SlotAllocationAnnotation();
			slotAllocations = ImmutableMap.<IPortSlot, SlotAllocationAnnotation>builder().putAll(slotAllocations).put(slot, allocation).build();

		}
		return allocation;
	}

	@Override
	public int getSlotTime(final IPortSlot slot) {
		return portTimesRecord.getSlotTime(slot);
	}

	@Override
	public void setSlotTime(final IPortSlot slot, final int time) {
		throwNotChangableException();
	}

	@Override
	public int getSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type) {
		return portTimesRecord.getSlotExtraIdleTime(slot, type);
	}

	@Override
	public int getSlotTotalExtraIdleTime(final IPortSlot slot) {
		return portTimesRecord.getSlotTotalExtraIdleTime(slot);
	}

	@Override
	public void setSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type, final int extraIdleTime) {
		throwNotChangableException();
	}

	@Override
	public int getSlotDuration(final IPortSlot slot) {
		return portTimesRecord.getSlotDuration(slot);
	}

	@Override
	public void setSlotDuration(final IPortSlot slot, final int duration) {
		throwNotChangableException();

	}

	@Override
	public long getCommercialSlotVolumeInM3(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.commercialVolumeInM3;
		}

		return 0;
	}

	public void setCommercialSlotVolumeInM3(final IPortSlot slot, final long volumeInM3) {
		checkWritable();
		getOrCreateSlotAllocation(slot).commercialVolumeInM3 = volumeInM3;
	}

	@Override
	public long getCommercialSlotVolumeInMMBTu(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.commercialVolumeInMMBTu;
		}

		return 0;
	}

	public void setPhysicalSlotVolumeInMMBTu(final IPortSlot slot, final long volumeInMMBTu) {
		checkWritable();
		getOrCreateSlotAllocation(slot).physicalVolumeInMMBTu = volumeInMMBTu;
	}

	@Override
	public long getPhysicalSlotVolumeInM3(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.physicalVolumeInM3;
		}

		return 0;
	}

	public void setPhysicalSlotVolumeInM3(final IPortSlot slot, final long volumeInM3) {
		checkWritable();
		getOrCreateSlotAllocation(slot).physicalVolumeInM3 = volumeInM3;
	}

	@Override
	public long getPhysicalSlotVolumeInMMBTu(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.physicalVolumeInMMBTu;
		}

		return 0;
	}

	public void setCommercialSlotVolumeInMMBTu(final IPortSlot slot, final long volumeInMMBTu) {
		checkWritable();
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

	public void setSlotCargoCV(final IPortSlot slot, final int cargoCV) {
		checkWritable();
		getOrCreateSlotAllocation(slot).cargoCV = cargoCV;
	}

	@Override
	public long getStartHeelVolumeInM3() {
		return startHeelVolumeInM3;
	}

	public void setStartHeelVolumeInM3(final long startHeelVolumeInM3) {
		checkWritable();
		this.startHeelVolumeInM3 = startHeelVolumeInM3;
	}

	@Override
	public IPortSlot getFirstSlot() {
		return portTimesRecord.getFirstSlot();
	}

	@Override
	public int getFirstSlotTime() {
		return portTimesRecord.getFirstSlotTime();
	}

	@Override
	public @Nullable IPortSlot getReturnSlot() {
		return portTimesRecord.getReturnSlot();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof AllocationAnnotation) {
			final AllocationAnnotation other = (AllocationAnnotation) obj;
			return this.startHeelVolumeInM3 == other.startHeelVolumeInM3 //
					&& this.fuelVolumeInM3 == other.fuelVolumeInM3 //
					&& this.remainingHeelVolumeInM3 == other.remainingHeelVolumeInM3 //
					&& Objects.equal(this.slotAllocations, other.slotAllocations)//
					&& Objects.equal(this.portTimesRecord, other.portTimesRecord);
		}

		return false;
	}

	@Override
	public @Nullable IRouteOptionBooking getRouteOptionBooking(final IPortSlot slot) {
		return portTimesRecord.getRouteOptionBooking(slot);
	}

	@Override
	public void setRouteOptionBooking(final IPortSlot slot, final @Nullable IRouteOptionBooking routeOptionBooking) {
		throwNotChangableException();
	}

	@Override
	public void setSlotNextVoyageOptions(final IPortSlot slot, final AvailableRouteChoices nextVoyageRoute) {
		throwNotChangableException();
	}

	@Override
	public AvailableRouteChoices getSlotNextVoyageOptions(final IPortSlot slot) {
		return portTimesRecord.getSlotNextVoyageOptions(slot);
	}

	@Override
	public void setSlotMaxAvailablePanamaIdleHours(IPortSlot from, int maxIdleTimeAvailable) {
		throwNotChangableException();
	}

	@Override
	public void setSlotAdditionalPanamaIdleHours(IPortSlot from, int additionalPanamaTime) {
		throwNotChangableException();
	}

	@Override
	public int getSlotAdditionaPanamaIdleHours(IPortSlot slot) {
		return portTimesRecord.getSlotAdditionaPanamaIdleHours(slot);
	}

	@Override
	public int getSlotMaxAdditionaPanamaIdleHours(IPortSlot slot) {
		return portTimesRecord.getSlotMaxAdditionaPanamaIdleHours(slot);
	}

	private void throwNotChangableException() {
		throw new IllegalArgumentException("Should not be changing by this stage.");
	}
}
