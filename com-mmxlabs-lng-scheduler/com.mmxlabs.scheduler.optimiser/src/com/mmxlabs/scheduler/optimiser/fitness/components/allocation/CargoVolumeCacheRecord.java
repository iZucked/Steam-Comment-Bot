/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A data storage class used by a cache object to calculate it's data.
 * 
 * @author sg
 *
 */
public final class CargoVolumeCacheRecord {

	public @NonNull IVesselAvailability vesselAvailability;
	public int vesselStartTime;
	public @NonNull VoyagePlan plan;
	public @NonNull IPortTimesRecord portTimesRecord;

	public CargoVolumeCacheRecord(@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @NonNull IPortTimesRecord portTimesRecord) {
		this.vesselAvailability = vesselAvailability;
		this.vesselStartTime = vesselStartTime;
		this.plan = plan;
		this.portTimesRecord = portTimesRecord;
	}

}
