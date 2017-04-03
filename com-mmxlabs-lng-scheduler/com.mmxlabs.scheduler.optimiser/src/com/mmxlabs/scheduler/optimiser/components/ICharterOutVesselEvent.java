/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface ICharterOutVesselEvent extends IVesselEvent {

	IHeelOptionConsumer getHeelOptionsConsumer();

	IHeelOptionSupplier getHeelOptionsSupplier();

	/**
	 * Returns the repositioning fee for charter out events.
	 * 
	 * @return
	 */
	long getRepositioning();

	/**
	 * Returns the total hire cost for charter out events.
	 * 
	 * @return
	 */
	long getHireOutRevenue();

	/**
	 * Sets the repositioning fee for charter out events.
	 * 
	 */
	void setRepositioning(long repositioning);

	/**
	 * Set the total hire cost for charter out events.
	 * 
	 */
	void setHireOutRevenue(long hireCost);

	long getBallastBonus();

	void setBallastBonus(long ballastBonus);
}
