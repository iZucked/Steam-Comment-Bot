/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Objects;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;

/**
 * Implementation of {@link ICargoValueAnnotation} wrapping a pre-existing {@link IAllocationAnnotation} instance adding on the {@link ICargoValueAnnotation} specific data items. Internally very
 * similar to {@link AllocationAnnotation}.
 * 
 * @author Simon Goodall.
 * 
 */
public final class CargoValueAnnotation implements ICargoValueAnnotation {

	private final IAllocationAnnotation allocationAnnotation;

	private static class SlotAllocationAnnotation {
		public long value;
		public long additionalOtherPNL;
		public long additionalUpsidePNL;
		public long additionalShippingPNL;
		public long upstreamPNL;
		public int pricePerMMBTu;
		public IEntity entity;

		@Override
		public boolean equals(final Object obj) {

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

	private final Map<IPortSlot, SlotAllocationAnnotation> slotAllocations = new HashMap<IPortSlot, SlotAllocationAnnotation>();

	public CargoValueAnnotation(@NonNull final IAllocationAnnotation allocationAnnotation) {
		this.allocationAnnotation = allocationAnnotation;
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
	public int getSlotPricePerMMBTu(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = slotAllocations.get(slot);
		if (allocation != null) {
			return allocation.pricePerMMBTu;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	public void setSlotPricePerMMBTu(final IPortSlot slot, final int price) {
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
		getOrCreateSlotAllocation(slot).value = value;
	}

	public void setSlotEntity(final IPortSlot slot, final IEntity entity) {
		getOrCreateSlotAllocation(slot).entity = entity;
	}

	@Override
	public IEntity getSlotEntity(final IPortSlot slot) {
		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.entity;
		}

		return null;

	}

	@Override
	public List<@NonNull IPortSlot> getSlots() {
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
	public long getSlotVolumeInM3(final IPortSlot slot) {
		return allocationAnnotation.getSlotVolumeInM3(slot);
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
	public long getSlotVolumeInMMBTu(final IPortSlot slot) {
		return allocationAnnotation.getSlotVolumeInMMBTu(slot);
	}

	@Override
	public int getSlotCargoCV(final IPortSlot slot) {
		return allocationAnnotation.getSlotCargoCV(slot);
	}

	@Override
	public void setSlotTime(final IPortSlot slot, final int time) {
		allocationAnnotation.setSlotTime(slot, time);

	}

	@Override
	public void setSlotDuration(final IPortSlot slot, final int duration) {
		allocationAnnotation.setSlotDuration(slot, duration);

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
	public IPortSlot getReturnSlot() {
		return allocationAnnotation.getReturnSlot();
	}

	@Override
	public boolean equals(final Object obj) {
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
}
