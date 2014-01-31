package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Record class for allocation constraints per
 * 
 * @author Simon McGregor
 * @since 8.0
 */
public final class AllocationRecord {
	/** The LNG volume which the vessel starts with (the start heel) */
	public final long startVolumeInM3;

	public final IVessel vessel;
	// public final VoyagePlan voyagePlan;

	// /** The capacity of the vessel carrying the cargo */
	// final long vesselCapacityInM3;

	/** The quantity of LNG which <em>must</em> be loaded for a given cargo (for fuel) */
	public long requiredFuelVolumeInM3;

	/** The LNG volume which must remain at the end of the voyage (the remaining heel) */
	public  long minEndVolumeInM3;

	/**
	 * Hack: magic number calculated by LNGVoyageCalculator representing the amount of heel which should be present after the load -> discharge voyage but can be discharged after idling. Should be
	 * equal to the minimum heel value minus the idle consumption. There must be a better way of doing this.
	 */
	public  long dischargeHeelInM3;
	//
	// /** The LNG volume which will actually remain at the end of the voyage */
	// public long allocatedEndVolumeInM3;

	// /** Prices of LNG at each load / discharge slot in the cargo */
	// public final int[] slotPricesPerM3;
	// public final int[] slotPricesPerMMBTu;

	public final List<Integer> slotTimes;

	/** Slots in the cargo */
	public final List<IPortSlot> slots;

	// public final long[] allocations;
	public final List<Long> minVolumes;
	public final List<Long> maxVolumes;
	
//	public final List<Long> violationMinVolumes;
//	public final List<Long> violationMaxVolumes;

	// Set to false to maximise load volume and push gas into next loading
	public boolean preferShortLoadOverLeftoverHeel = true;

	public AllocationRecord(final IVessel vessel, final long startVolumeInM3, final long requiredFuelVolumeInM3, final long minEndVolumeInM3, final long dischargeHeelInM3,
			final List<IPortSlot> slots, final List<Integer> times, List<Long> minVolumes, List<Long> maxVolumes) {
		this.vessel = vessel;
		this.startVolumeInM3 = startVolumeInM3;
		this.requiredFuelVolumeInM3 = requiredFuelVolumeInM3;
		this.minEndVolumeInM3 = minEndVolumeInM3;
		this.dischargeHeelInM3 = dischargeHeelInM3;
		this.slotTimes = times;
		this.slots = slots;
		this.minVolumes = minVolumes;
		this.maxVolumes = maxVolumes;
	}

}