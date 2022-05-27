/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;

/**
 * A {@link IVesselCharter} is a period of time in which an IVessel is available for use.
 * 
 * @author Simon Goodall
 */
public interface IVesselCharter {

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

	IStartRequirement getStartRequirement();

	IEndRequirement getEndRequirement();

	/**
	 * Returns the repositioning fee of the vessel.
	 * 
	 * @return repositioning fee
	 */
	@NonNull
	ILongCurve getRepositioningFee();
	
	/**
	 * Sets the ballast bonus contract of the vessel availability.
	 * 
	 */
	void setCharterContract(@Nullable ICharterContract contract);

	/**
	 * Returns the ballast bonus contract of the vessel availability.
	 * 
	 * @return ballast bonus
	 */
	@Nullable
	ICharterContract getCharterContract();

	/**
	 * Returns charter in cost calculator for this vessel.
	 * 
	 * @return charter cost calculator
	 */	
	@NonNull ICharterCostCalculator getCharterCostCalculator();
	
	/**
	 * Returns the daily rate at which this vessel can be chartered in.
	 * 
	 * @return daily charter in price
	 * @deprecated Use {@link #getCharterCostCalculator()} for calculating charter costs.
	 */
	ILongCurve getDailyCharterInRate();
	
	/**
	 * A flag determining whether the charter is optional or not
	 * 
	 * @return
	 */
	boolean isOptional();


}
