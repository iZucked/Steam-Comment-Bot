/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Record class for allocation constraints per
 * 
 * @author Simon McGregor
 */
public class AllocationRecord {

	public enum AllocationMode {
		/**
		 * Take actuals data directly and infer missing variables
		 */
		Actuals,
		/**
		 * Actuals data, but currently for DES deals where discharge volume is the purchase volume.
		 */
		Actuals_Transfer,
		/**
		 * FOB Purchase to DES Sales. BOG adjusts volumes
		 */
		Shipped,
		/**
		 * Typically DES or FOB deals. BOG ignored in volumes
		 */
		Transfer,
		/**
		 * Custom mode, use the record max values as absolute values. Assume input data has already been checked.
		 */
		Custom 
	}

	/** The LNG volume which the vessel starts with (the start heel) */
	public final long startVolumeInM3;

	public final @NonNull IVesselAvailability vesselAvailability;
	public final @NonNull VoyagePlan resourceVoyagePlan;

	public IVessel nominatedVessel;
	public VoyagePlan nominatedVoyagePlan;

	// /** The capacity of the vessel carrying the cargo */
	// final long vesselCapacityInM3;

	/** The quantity of LNG which <em>must</em> be loaded for a given cargo (for fuel) */
	public long requiredFuelVolumeInM3;

	/** The LNG volume which must remain at the end of the voyage (the remaining heel) */
	public long minEndVolumeInM3;

	// public final List<Integer> slotTimes;
	// public final List<Integer> slotDurations;

	/** Slots in the cargo, excluding the return port slot */
	public final @NonNull List<@NonNull IPortSlot> slots;

	/**
	 * The minimum transfer volume indexed by port slot
	 */
	public final @NonNull List<Long> minVolumesInM3;
	/**
	 * The maximum transfer volume indexed by port slot
	 */
	public final @NonNull List<Long> maxVolumesInM3;

	public final @NonNull List<Long> minVolumesInMMBtu;
	public final @NonNull List<Long> maxVolumesInMMBtu;
	public final @NonNull List<Integer> slotCV;

	// Set to false to maximise load volume and push gas into next loading
	public boolean preferShortLoadOverLeftoverHeel = true;

	public @NonNull AllocationMode allocationMode;

	public int vesselStartTime;

	/**
	 * The {@link IPortSlot} the vessel "returns" to after the loads and discharges.
	 */
	public IPortSlot returnSlot;

	public @NonNull IPortTimesRecord portTimesRecord;

	public AllocationRecord(final @NonNull IVesselAvailability vesselAvailability, final @NonNull VoyagePlan resourceVoyagePlan, final int vesselStartTime, final long startVolumeInM3,
			final long requiredFuelVolumeInM3, final long minEndVolumeInM3, final @NonNull List<@NonNull IPortSlot> slots, final @NonNull IPortTimesRecord portTimesRecord, final IPortSlot returnSlot,
			final @NonNull List<@NonNull Long> minVolumesInM3, final @NonNull List<@NonNull Long> maxVolumesInM3, @NonNull final List<@NonNull Long> minVolumesInMMBtu,
			@NonNull final List<@NonNull Long> maxVolumesInMMBtu, final @NonNull List<@NonNull Integer> slotCV) {
		this.vesselAvailability = vesselAvailability;
		this.resourceVoyagePlan = resourceVoyagePlan;
		this.vesselStartTime = vesselStartTime;
		this.startVolumeInM3 = startVolumeInM3;
		this.requiredFuelVolumeInM3 = requiredFuelVolumeInM3;
		this.minEndVolumeInM3 = minEndVolumeInM3;
		this.slots = slots;
		this.portTimesRecord = portTimesRecord;
		this.returnSlot = returnSlot;
		this.minVolumesInM3 = minVolumesInM3;
		this.maxVolumesInM3 = maxVolumesInM3;
		this.minVolumesInMMBtu = minVolumesInMMBtu;
		this.maxVolumesInMMBtu = maxVolumesInMMBtu;
		this.slotCV = slotCV;
		this.allocationMode = (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE)
				? AllocationMode.Transfer : AllocationMode.Shipped;
	}
}