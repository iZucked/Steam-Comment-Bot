/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.core.IResource;

/**
 * A {@link IVessel} is an extended version of the {@link IResource} interface and contains attributes specific to a vessel.
 * 
 * TODO: This should not necessarily extend IResource and should instead come through a data provider linked to a IResource.
 * 
 * @author Simon Goodall
 */
public interface IVessel extends IResource {

	/**
	 * Returns the name of the vessel.
	 * 
	 * @return
	 */
	@Override
	String getName();

	/**
	 * Return the {@link IVesselClass} instance containing generic vessel data.
	 * 
	 * @return
	 */
	IVesselClass getVesselClass();

	/**
	 * Returns the {@link VesselInstanceType} of this {@link IVessel} instance.
	 * 
	 * @return
	 */
	VesselInstanceType getVesselInstanceType();

	/**
	 * Returns the hourly rate at which this vessel can be chartered in .
	 * 
	 * @return hourly charter out price
	 */
	int getHourlyCharterInPrice();

	/**
	 * Returns the hourly rate at which this vessel can be chartered out.
	 * 
	 * @return hourly charter out price
	 */
	int getHourlyCharterOutPrice();
}
