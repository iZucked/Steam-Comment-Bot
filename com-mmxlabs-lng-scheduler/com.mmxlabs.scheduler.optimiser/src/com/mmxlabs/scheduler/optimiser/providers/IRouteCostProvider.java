/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Provides information about the tolls, fuel usage, and travel time associated with travelling by a non-default route
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public interface IRouteCostProvider extends IDataComponentProvider {

	enum CostType {
		Laden, Ballast, RoundTripBallast
	}

	/**
	 * Gets the cost in dollars incurred by vessels travelling via {@code route}
	 * 
	 * @param route
	 *            route travelled
	 * @param vessel
	 *            the travelling vessel
	 * @param costType
	 *            the type of cost required
	 * @return toll in dollars
	 */
	long getRouteCost(ERouteOption route, IPort from, IPort to, IVessel vessel, int voyageStartTime, CostType costType);

	/**
	 * Gets the extra fuel usage, in base-fuel-equivalent scaled MT per hour (see {@link Calculator#ScaleFactor}), required for vessels of class {@code vesselClass} to travel via {@code route} for the
	 * given {@link VesselState}. The total fuel used is then this value multiplied by {@link #getRouteTransitTime(String, IVesselClass)}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vessel
	 *            the travelling vessel
	 * @return extra fuel used by vessel, in scaled MT BF(E) per hour
	 */
	long getRouteFuelUsage(ERouteOption route, IVessel vessel, VesselState vesselState);

	/**
	 * Gets the NBO rate, in scaled M3 per hour (see {@link Calculator#ScaleFactor}), required for vessels of class {@code vesselClass} to travel via {@code route} for the given {@link VesselState}.
	 * The total NBO used is then this value multiplied by {@link #getRouteTransitTime(String, IVesselClass)}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vesselClass
	 *            the travelling vessel
	 * @return NBO rate of vessel, in scaled M3 per hour
	 */
	long getRouteNBORate(ERouteOption route, IVessel vessel, VesselState vesselState);

	/**
	 * Gets the extra time, in hours, which vessels of class {@code vesselClass} must spend to travel via {@code route}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vessel
	 *            the travelling vessel
	 * @return Time to pass through canal, in hours. This is independent of the journey time spent getting from point A to canal entrance and canal exit to point B.
	 */
	int getRouteTransitTime(ERouteOption route, IVessel vessel);
}
