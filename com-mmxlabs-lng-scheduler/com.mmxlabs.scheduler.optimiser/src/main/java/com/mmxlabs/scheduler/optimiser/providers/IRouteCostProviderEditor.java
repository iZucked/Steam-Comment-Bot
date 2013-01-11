/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Manipulates the toll fees, fuel costs, and travel times associated with travelling by a non-default route.
 * 
 * @author hinton
 * 
 */
public interface IRouteCostProviderEditor extends IRouteCostProvider {
	/**
	 * Sets the cost in dollars incurred by a vessel of class {@code vesselClass} in state {@code vesselState} travelling via route named {@code route} to {@code price}.
	 * 
	 * @param route
	 * @param vesselClass
	 * @param vesselState
	 * @param price
	 * @since 2.0
	 */
	public void setRouteCost(String route, IVesselClass vesselClass, VesselState vesselState, long price);

	/**
	 * Sets the default cost incurred when travelling via the route named {@code route} to {@code price}. This price is overridden for particular vessel classes by
	 * {@link #setRouteCost(String, IVesselClass, VesselState, int)}.
	 * 
	 * @param route
	 *            the route name
	 * @param price
	 *            price in dollars
	 * @since 2.0
	 */
	public void setDefaultRouteCost(String route, long price);

	/**
	 * Sets the fuel consumption and NBO rate incurred by vessels of class {@code vc} when travelling via the route with name {@code routeName} with the {@link VesselState}
	 * 
	 * @param routeName
	 *            the name of the route
	 * @param vesselClass
	 *            the vessel class
	 * @param vesselState
	 *            the vessel laden/ballast state
	 * @param baseFuelInScaledMTPerHour
	 *            the amount of base fuel spent while traversing the canal, in scaled MT / hour
	 * @param baseFuelInScaledMTPerHour
	 *            the amount of NBO while traversing the canal, in scaled M3 / hour
	 */
	void setRouteFuel(String routeName, IVesselClass vesselClass, VesselState vesselState, long baseFuelInScaledMTPerHour, long nboRateInScaledM3PerHour);

	/**
	 * Sets the extra time incurred by vessels of class {@code vc} when travelling via the route with name {@code routeName}
	 * 
	 * @param routeName
	 *            the name of the route
	 * @param vc
	 *            the vessel class
	 * @param transitTimeInHours
	 *            the amount of extra travel time required to get through the canal, in hours.
	 */
	void setRouteTransitTime(String routeName, IVesselClass vesselClass, int transitTimeInHours);
}
