/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;

/**
 * A {@link IVesselAvailability} is a period of time in which an IVessel is available for use.
 * 
 * @author Simon Goodall
 */
public interface IVesselAvailability {

	@NonNull
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
	 * Returns the repositioning fee of the vessel.
	 * 
	 * @return repositioning fee
	 */
	@NonNull
	ILongCurve getRepositioningFee();
	
	/**
	 * Returns the ballast bonus of the vessel.
	 * 
	 * @return ballast bonus
	 */
	@NonNull
	ILongCurve getBallastBonus();
	
	/**
	 * Sets the ballast bonus contract of the vessel availability.
	 * 
	 */
	void setBallastBonusContract(@Nullable IBallastBonusContract contract);

	/**
	 * Returns the ballast bonus contract of the vessel availability.
	 * 
	 * @return ballast bonus
	 */
	@Nullable
	IBallastBonusContract getBallastBonusContract();

	/**
	 * Returns the daily rate at which this vessel can be chartered in.
	 * 
	 * @return daily charter in price
	 */
	ILongCurve getDailyCharterInRate();

	/**
	 * A flag determining whether the charter is optional or not
	 * 
	 * @return
	 */
	boolean isOptional();


}
