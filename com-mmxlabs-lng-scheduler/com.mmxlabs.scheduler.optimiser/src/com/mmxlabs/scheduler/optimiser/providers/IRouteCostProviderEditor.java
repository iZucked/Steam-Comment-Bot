/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Manipulates the toll fees, fuel costs, and travel times associated with travelling by a non-default route.
 * 
 * @author hinton
 * 
 */
public interface IRouteCostProviderEditor extends IRouteCostProvider {
	/**
	 * Sets the cost in dollars incurred by a vessel in state {@code vesselState} travelling via route named {@code route} to {@code price}.
	 * 
	 * @param route
	 * @param vessel
	 * @param costType
	 * @param price
	 */
	public void setRouteCost(@NonNull ERouteOption route, @NonNull IVessel vessel, @NonNull CostType costType, long price);

	/**
	 * Sets the default cost incurred when travelling via the route named {@code route} to {@code price}. This price is overridden for particular vessel classes by
	 * {@link #setRouteCost(String, IVessel, VesselState, int)}.
	 * 
	 * @param route
	 *            the route name
	 * @param price
	 *            price in dollars
	 */
	public void setDefaultRouteCost(@NonNull ERouteOption route, long price);

	/**
	 * 
	 * @param routeName
	 *            the name of the route
	 * @param vessel
	 *            the vessel
	 * @param vesselState
	 *            the vessel laden/ballast state
	 * @param baseFuelInScaledMTPerHour
	 *            the amount of base fuel spent while traversing the canal, in scaled MT / hour
	 * @param baseFuelInScaledMTPerHour
	 *            the amount of NBO while traversing the canal, in scaled M3 / hour
	 */
	void setRouteFuel(@NonNull ERouteOption routeName, @NonNull IVessel vessel, @NonNull VesselState vesselState, long baseFuelInScaledMTPerHour, long nboRateInScaledM3PerHour);

	/**
	 * Sets the extra time incurred by vessels of class {@code vc} when travelling via the route with name {@code routeName}
	 * 
	 * @param routeName
	 *            the name of the route
	 * @param vessel
	 *            the vessel
	 * @param transitTimeInHours
	 *            the amount of extra travel time required to get through the canal, in hours.
	 */
	void setRouteTransitTime(@NonNull ERouteOption routeName, @NonNull IVessel vessel, int transitTimeInHours);
}
