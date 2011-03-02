/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Manipulates the toll fees, fuel costs, and travel times associated with
 * traveling by a non-default route.
 * 
 * @author hinton
 * 
 */
public interface IRouteCostProviderEditor extends IRouteCostProvider {
	/**
	 * Sets the cost in dollars incurred by a vessel of class
	 * {@code vesselClass} in state {@code vesselState} traveling via route
	 * named {@code route} to {@code price}.
	 * 
	 * @param route
	 * @param vesselClass
	 * @param vesselState
	 * @param price
	 */
	public void setRouteCost(final String route,
			final IVesselClass vesselClass, final VesselState vesselState,
			final int price);

	/**
	 * Sets the default cost incurred when traveling via the route named
	 * {@code route} to {@code price}. This price is overridden for particular
	 * vessel classes by
	 * {@link #setRouteCost(String, IVesselClass, VesselState, int)}.
	 * 
	 * @param route
	 *            the route name
	 * @param price
	 *            price in dollars
	 */
	public void setDefaultRouteCost(final String route, final int price);

	/**
	 * Sets the extra time and fuel costs incurred by vessels of class
	 * {@code vc} when traveling via the route with name {@code routeName}
	 * 
	 * @param routeName
	 *            the name of the route
	 * @param vc
	 *            the vessel class
	 * @param transitTimeInHours
	 *            the amount of extra travel time required to get through the
	 *            canal, in hours.
	 * @param baseFuelInScaledMTPerHour
	 *            the amount of base fuel spent while traversing the canal, in
	 *            scaled MT / hour
	 */
	void setRouteTimeAndFuel(final String routeName, final IVesselClass vc,
			final int transitTimeInHours, final long baseFuelInScaledMTPerHour);
}
