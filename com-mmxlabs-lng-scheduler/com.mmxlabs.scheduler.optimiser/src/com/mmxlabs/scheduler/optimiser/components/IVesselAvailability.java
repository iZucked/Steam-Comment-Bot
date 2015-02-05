/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;

/**
 * A {@link IVesselAvailability} is a period of time in which an IVessel is available for use.
 * 
 * @author Simon Goodall
 */
public interface IVesselAvailability {

	IVessel getVessel();

	@Nullable
	ISpotCharterInMarket getSpotCharterInMarket();

	/**
	 * If {@link #getSpotCharterInMarket()} is not null, then this is the instance counter for the charter market.
	 * 
	 * @return
	 */
	int getSpotIndex();

	/**
	 * Returns the {@link VesselInstanceType} of this {@link IVessel} instance.
	 * 
	 * @return
	 */
	@NonNull
	VesselInstanceType getVesselInstanceType();

	IStartEndRequirement getStartRequirement();

	IStartEndRequirement getEndRequirement();

	/**
	 * Returns the daily rate at which this vessel can be chartered in.
	 * 
	 * @return daily charter in price
	 */
	ICurve getDailyCharterInRate();

}
