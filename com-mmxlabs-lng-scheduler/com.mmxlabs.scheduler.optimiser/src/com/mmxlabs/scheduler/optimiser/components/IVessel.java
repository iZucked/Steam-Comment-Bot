/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;

/**
 * A {@link IVessel} is an extended version of the {@link IResource} interface and contains attributes specific to a vessel.
 * 
 * TODO: This should not necessarily extend IResource and should instead come through a data provider linked to a IResource.
 * 
 * @author Simon Goodall
 */
public interface IVessel {

	/**
	 * Returns the name of the vessel.
	 * 
	 * @return
	 */
	@NonNull
	String getName();

	/**
	 * Return the {@link IVesselClass} instance containing generic vessel data.
	 * 
	 * @return
	 */
	@NonNull
	IVesselClass getVesselClass();

	/**
	 * Returns the capacity of this vessel
	 * 
	 * @return
	 */
	long getCargoCapacity();

}
