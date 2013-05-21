/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.common.curves.ICurve;
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
	 * Returns the capacity of this vessel 
	 * @return
	 * @since 5.0
	 */
	long getCargoCapacity();
	
	/**
	 * Returns the hourly rate at which this vessel can be chartered in.
	 * 
	 * @return hourly charter in price
	 * @since 2.0
	 */
	ICurve getHourlyCharterInPrice();
}
