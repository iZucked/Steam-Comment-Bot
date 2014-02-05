package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

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
	public final long startVolumeInM3;

	public final IVessel resourceVessel;
	public final VoyagePlan resourceVoyagePlan;

	public IVessel nominatedVessel;
	public VoyagePlan nominatedVoyagePlan;

	// /** The capacity of the vessel carrying the cargo */
	// final long vesselCapacityInM3;

	/** The quantity of LNG which <em>must</em> be loaded for a given cargo (for fuel) */
	public long requiredFuelVolumeInM3;

	/** The LNG volume which must remain at the end of the voyage (the remaining heel) */
	public long minEndVolumeInM3;

	public final List<Integer> slotTimes;

	/** Slots in the cargo */
	public final List<IPortSlot> slots;

	/**
	 * The minimum transfer volume indexed by port slot
	 */
	public final List<Long> minVolumes;
	/**
	 * The maximum transfer volume indexed by port slot
	 */
	public final List<Long> maxVolumes;

	// Set to false to maximise load volume and push gas into next loading
	public boolean preferShortLoadOverLeftoverHeel = true;

	public AllocationRecord(final IVessel resourceVessel, VoyagePlan resourceVoyagePlan, final long startVolumeInM3, final long requiredFuelVolumeInM3, final long minEndVolumeInM3,
			final List<IPortSlot> slots, final List<Integer> times, List<Long> minVolumes, List<Long> maxVolumes) {
		this.resourceVessel = resourceVessel;
		this.resourceVoyagePlan = resourceVoyagePlan;
		this.startVolumeInM3 = startVolumeInM3;
		this.requiredFuelVolumeInM3 = requiredFuelVolumeInM3;
		this.minEndVolumeInM3 = minEndVolumeInM3;
		this.slotTimes = times;
		this.slots = slots;
		this.minVolumes = minVolumes;
		this.maxVolumes = maxVolumes;
	}

}