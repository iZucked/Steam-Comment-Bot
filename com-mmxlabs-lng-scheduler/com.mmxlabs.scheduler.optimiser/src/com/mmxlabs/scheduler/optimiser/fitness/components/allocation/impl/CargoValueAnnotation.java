package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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
		public long additionalPNL;
		public int pricePerMMBTu;
		public IEntity entity;
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
	public long getSlotAdditionalPNL(final IPortSlot slot) {

		final SlotAllocationAnnotation allocation = getOrCreateSlotAllocation(slot);
		if (allocation != null) {
			return allocation.additionalPNL;
		}

		return 0;
	}

	public void setSlotAdditionalPNL(final IPortSlot slot, final long additionalPNL) {
		getOrCreateSlotAllocation(slot).additionalPNL = additionalPNL;
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
	public List<IPortSlot> getSlots() {
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

}
