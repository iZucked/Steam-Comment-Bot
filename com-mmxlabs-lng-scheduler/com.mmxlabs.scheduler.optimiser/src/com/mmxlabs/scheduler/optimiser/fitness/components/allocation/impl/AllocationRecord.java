package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Record class for allocation constraints per
 * 
 * @author Simon McGregor
 * @since 8.0
 */
public final class AllocationRecord {
	/** The LNG volume which the vessel starts with (the start heel) */
	final long startVolumeInM3;

	/** The capacity of the vessel carrying the cargo */
	final long vesselCapacityInM3;

	/** The quantity of LNG which <em>must</em> be loaded for a given cargo (for fuel) */
	public final long requiredFuelVolumeInM3;

	/** The LNG volume which must remain at the end of the voyage (the remaining heel) */
	public final long minEndVolumeInM3;

	// final long maxEndVolumeInM3;

	/**
	 * Hack: magic number calculated by LNGVoyageCalculator representing the amount of heel which should be present after the load -> discharge voyage but can be discharged after idling. Should be
	 * equal to the minimum heel value minus the idle consumption. There must be a better way of doing this.
	 */
	public final long dischargeHeelInM3;

	/** The LNG volume which will actually remain at the end of the voyage */
	public long allocatedEndVolumeInM3;

	/** Prices of LNG at each load / discharge slot in the cargo */
	public final int[] slotPricesPerM3;
	public final int[] slotPricesPerMMBTu;

	public final int[] slotTimes;

	/** Slots in the cargo */
	public final IPortSlot[] slots;

	public final long[] allocations;

	public final VoyagePlan voyagePlan;

	public IVessel vessel;

	public AllocationRecord(final IVessel vessel, final long capacity, final long forced, final long start, final long heel, final long dischargeHeel, final IPortSlot[] slots,
			final int[] pricesPerM3, final int[] pricesPerMMBTu, final int[] times, final VoyagePlan plan) {
		this.startVolumeInM3 = start;
		this.vessel = vessel;
		this.vesselCapacityInM3 = capacity;
		this.requiredFuelVolumeInM3 = forced;
		this.minEndVolumeInM3 = heel;
		this.dischargeHeelInM3 = dischargeHeel;
		this.slotPricesPerM3 = pricesPerM3;
		this.slotPricesPerMMBTu = pricesPerMMBTu;
		this.slotTimes = times;
		this.slots = slots;
		this.voyagePlan = plan;

		this.allocations = new long[slots.length];
	}

	public AllocationAnnotation createAllocationAnnotation() {
		final AllocationAnnotation annotation = new AllocationAnnotation();

		annotation.setFuelVolumeInM3(requiredFuelVolumeInM3);
		annotation.setRemainingHeelVolumeInM3(allocatedEndVolumeInM3);

		// TODO recompute load price here; this is not necessarily right
		// final int[] prices = priceIterator.next();
		final int[] pricesPerM3 = slotPricesPerM3;
		final int[] pricesPerMMBTu = slotPricesPerMMBTu;

		// annotation.getSlots().clear();
		assert slots.length == pricesPerM3.length;
		for (int i = 0; i < slots.length; i++) {
			annotation.getSlots().add(slots[i]);
			annotation.setSlotPricePerM3(slots[i], pricesPerM3[i]);
			annotation.setSlotPricePerMMBTu(slots[i], pricesPerMMBTu[i]);
			annotation.setSlotTime(slots[i], slotTimes[i]);
		}

		// load/discharge case
		if (slots.length == 2) {
			annotation.setSlotVolumeInM3(slots[0], allocations[0]);
			annotation.setSlotVolumeInM3(slots[1], allocations[1]);
		}
		// LDD case
		else {
			for (int j = 1; j < slots.length; j++) {
				final IDischargeOption discharge = (IDischargeOption) slots[j];
				annotation.setSlotVolumeInM3(discharge, discharge.getMaxDischargeVolume());
			}
		}

		return annotation;
	}

	public AllocationRecord(final IVessel vessel, final long vesselCapacityInM3, final IPortSlot[] slots, final int[] pricesPerM3, final int[] pricesPerMMBTu, final int[] times, final VoyagePlan plan) {
		this(vessel, vesselCapacityInM3, 0l, 0l, 0l, 0l, slots, pricesPerM3, pricesPerMMBTu, times, plan);
	}
}