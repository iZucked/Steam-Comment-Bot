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
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

/**
 * Implementation of {@link ICargoValueAnnotation} wrapping a pre-existing {@link IAllocationAnnotation} instance adding on the {@link ICargoValueAnnotation} specific data items. Internally very
 * similar to {@link AllocationAnnotation}.
 * 
 * @author Simon Goodall.
 * 
 */
@NonNullByDefault
public final class CargoValueAnnotation extends AbstractWriteLockable implements ICargoValueAnnotation {

	private final IAllocationAnnotation allocationAnnotation;
	private long totalProfitAndLoss;

	private static class SlotAllocationAnnotation {
		public long value;
		public long additionalOtherPNL;
		public long additionalUpsidePNL;
		public long additionalShippingPNL;
		public long upstreamPNL;
		public int pricePerMMBTu;
		public @Nullable IEntity entity;

		@Override
		public boolean equals(final @Nullable Object obj) {

			if (obj == this) {
				return true;
			}
			if (obj instanceof SlotAllocationAnnotation) {
				final SlotAllocationAnnotation other = (SlotAllocationAnnotation) obj;
				return this.value == other.value //
						&& this.additionalOtherPNL == other.additionalOtherPNL //
						&& this.additionalUpsidePNL == other.additionalUpsidePNL //
						&& this.additionalShippingPNL == other.additionalShippingPNL //
						&& this.upstreamPNL == other.upstreamPNL //
						&& this.pricePerMMBTu == other.pricePerMMBTu //
						&& Objects.equal(this.entity, other.entity);

			}

			return false;
		}
	}

	private ImmutableMap<IPortSlot, SlotAllocationAnnotation> slotAllocations = ImmutableMap.of();

	public CargoValueAnnotation(final IAllocationAnnotation allocationAnnotation) {
		this.allocationAnnotation = allocationAnnotation;
		IWriteLockable.writeLock(allocationAnnotation);
	}

	private SlotAllocationAnnotation getOrCreateSlotAllocation(final IPortSlot slot) {
		SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation == null) {
			checkWritable();
			allocation = new SlotAllocationAnnotation();
			slotAllocations = ImmutableMap.<IPortSlot, SlotAllocationAnnotation> builder().putAll(slotAllocations).put(slot, allocation).build();

		}
		return allocation;
	}

	@Override
	public int getSlotPricePerMMBTu(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.pricePerMMBTu;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	public void setSlotPricePerMMBTu(final IPortSlot slot, final int price) {
		checkWritable();
		getOrCreateSlotAllocation(slot).pricePerMMBTu = price;
	}

	@Override
	public long getSlotAdditionalOtherPNL(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.additionalOtherPNL;
		}

		return 0;
	}

	public void setSlotAdditionalOtherPNL(final IPortSlot slot, final long additionalOtherPNL) {
		checkWritable();
		getOrCreateSlotAllocation(slot).additionalOtherPNL = additionalOtherPNL;
	}

	@Override
	public long getSlotAdditionalShippingPNL(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.additionalShippingPNL;
		}

		return 0;
	}

	public void setSlotAdditionalShippingPNL(final IPortSlot slot, final long additionalShippingPNL) {
		checkWritable();
		getOrCreateSlotAllocation(slot).additionalShippingPNL = additionalShippingPNL;
	}

	@Override
	public long getSlotAdditionalUpsidePNL(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.additionalUpsidePNL;
		}

		return 0;
	}

	public void setSlotUpstreamPNL(final IPortSlot slot, final long upstreamPNL) {
		checkWritable();
		getOrCreateSlotAllocation(slot).upstreamPNL = upstreamPNL;
	}

	@Override
	public long getSlotUpstreamPNL(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.upstreamPNL;
		}

		return 0;
	}

	public void setSlotAdditionalUpsidePNL(final IPortSlot slot, final long additionalUpsidePNL) {
		checkWritable();
		getOrCreateSlotAllocation(slot).additionalUpsidePNL = additionalUpsidePNL;
	}

	@Override
	public long getSlotValue(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.value;
		}

		return 0;
	}

	public void setSlotValue(final IPortSlot slot, final long value) {
		checkWritable();
		getOrCreateSlotAllocation(slot).value = value;
	}

	public void setSlotEntity(final IPortSlot slot, final IEntity entity) {
		checkWritable();
		getOrCreateSlotAllocation(slot).entity = entity;
	}

	@Override
	public @Nullable IEntity getSlotEntity(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.entity;
		}

		return null;
	}

	@Override
	public ImmutableList<IPortSlot> getSlots() {
		return allocationAnnotation.getSlots();
	}

	@Override
	public long getFuelVolumeInM3() {
		return allocationAnnotation.getFuelVolumeInM3();
	}

	@Override
	public long getStartHeelVolumeInM3() {
		return allocationAnnotation.getStartHeelVolumeInM3();
	}

	@Override
	public long getRemainingHeelVolumeInM3() {
		return allocationAnnotation.getRemainingHeelVolumeInM3();
	}

	@Override
	public long getCommercialSlotVolumeInM3(final IPortSlot slot) {
		return allocationAnnotation.getCommercialSlotVolumeInM3(slot);
	}

	@Override
	public int getSlotTime(final IPortSlot slot) {
		return allocationAnnotation.getSlotTime(slot);
	}

	@Override
	public int getSlotDuration(final IPortSlot slot) {
		return allocationAnnotation.getSlotDuration(slot);
	}

	@Override
	public int getSlotExtraIdleTime(final IPortSlot slot) {
		return allocationAnnotation.getSlotExtraIdleTime(slot);
	}

	@Override
	public long getCommercialSlotVolumeInMMBTu(final IPortSlot slot) {
		return allocationAnnotation.getCommercialSlotVolumeInMMBTu(slot);
	}

	@Override
	public int getSlotCargoCV(final IPortSlot slot) {
		return allocationAnnotation.getSlotCargoCV(slot);
	}

	@Override
	public void setSlotTime(final IPortSlot slot, final int time) {
		throwNotChangableException();

	}

	@Override
	public void setSlotDuration(final IPortSlot slot, final int duration) {
		throwNotChangableException();
	}

	@Override
	public void setSlotExtraIdleTime(final IPortSlot slot, final int extraIdleTime) {
		throwNotChangableException();
	}

	@Override
	public int getFirstSlotTime() {
		return allocationAnnotation.getFirstSlotTime();
	}

	@Override
	public IPortSlot getFirstSlot() {
		return allocationAnnotation.getFirstSlot();
	}

	@Override
	public @Nullable IPortSlot getReturnSlot() {
		return allocationAnnotation.getReturnSlot();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof CargoValueAnnotation) {
			final CargoValueAnnotation other = (CargoValueAnnotation) obj;
			return Objects.equal(this.allocationAnnotation, other.allocationAnnotation) //
					&& Objects.equal(this.slotAllocations, other.slotAllocations);
		}

		return false;
	}

	public long getTotalProfitAndLoss() {
		return totalProfitAndLoss;
	}

	public void setTotalProfitAndLoss(long totalProfitAndLoss) {
		checkWritable();

		this.totalProfitAndLoss = totalProfitAndLoss;
	}

	@Override
	public long getPhysicalSlotVolumeInM3(@NonNull final IPortSlot slot) {
		return allocationAnnotation.getPhysicalSlotVolumeInM3(slot);
	}

	@Override
	public long getPhysicalSlotVolumeInMMBTu(@NonNull final IPortSlot slot) {
		return allocationAnnotation.getPhysicalSlotVolumeInMMBTu(slot);
	}

	@Override
	public @Nullable IRouteOptionBooking getRouteOptionBooking(IPortSlot slot) {
		return allocationAnnotation.getRouteOptionBooking(slot);
	}

	@Override
	public void setRouteOptionBooking(IPortSlot slot, @Nullable IRouteOptionBooking routeOptionBooking) {
		throwNotChangableException();
	}

	@Override
	public AvailableRouteChoices getSlotNextVoyageOptions(@NonNull IPortSlot slot) {
		return allocationAnnotation.getSlotNextVoyageOptions(slot);
	}

	@Override
	public void setSlotNextVoyageOptions(@NonNull IPortSlot slot, @NonNull AvailableRouteChoices nextVoyageRoute) {
		throwNotChangableException();
	}

	@Override
	public int getSlotAdditionaPanamaIdleHours(@NonNull IPortSlot slot) {
		return allocationAnnotation.getSlotAdditionaPanamaIdleHours(slot);
	}

	@Override
	public int getSlotMaxAdditionaPanamaIdleHours(@NonNull IPortSlot slot) {
		return allocationAnnotation.getSlotMaxAdditionaPanamaIdleHours(slot);
	}

	@Override
	public void setSlotMaxAvailablePanamaIdleHours(@NonNull IPortSlot from, int maxIdleTimeAvailable) {
		throwNotChangableException();
	}

	@Override
	public void setSlotAdditionalPanamaIdleHours(@NonNull IPortSlot from, int additionalPanamaTime) {
		throwNotChangableException();
	}

	private void throwNotChangableException() {
		throw new IllegalArgumentException("Should not be changing by this stage.");
	}
}
