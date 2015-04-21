/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 * Interface for custom code to implement to provide an alternative speed for shipping days restrictions time checking.
 * 
 * @author Simon Goodall
 * 
 */
public interface IShippingDaysRestrictionSpeedProvider {

	/**
	 * Return the reference speed to use to check shipping days times for this vessel class.
	 * 
	 * @param vesselClass
	 * @return
	 */
	double getSpeed(@NonNull VesselClass vesselClass, boolean isLaden);
	
	Collection<Route> getValidRoutes(@NonNull PortModel portModel, VesselClass vesselClass);
}
