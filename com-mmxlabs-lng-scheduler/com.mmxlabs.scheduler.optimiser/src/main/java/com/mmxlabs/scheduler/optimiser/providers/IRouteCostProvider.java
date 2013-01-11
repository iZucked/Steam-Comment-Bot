/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Provides information about the tolls, fuel usage, and travel time associated with travelling by a non-default route
 * 
 * @author hinton
 * 
 */
public interface IRouteCostProvider extends IDataComponentProvider {
	/**
	 * Gets the cost in dollars incurred by vessels of class {@code vesselClass} travelling via {@code route} in state {@code vesselState}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vesselClass
	 *            class of travelling vessel
	 * @param vesselState
	 *            vessel state during travel`
	 * @return toll in dollars
	 * @since 2.0
	 */
	public long getRouteCost(final String route, final IVesselClass vesselClass, final VesselState vesselState);

	/**
	 * Gets the extra fuel usage, in base-fuel-equivalent scaled MT per hour (see {@link Calculator#ScaleFactor}), required for vessels of class {@code vesselClass} to travel via {@code route} for the
	 * given {@link VesselState}. The total fuel used is then this value multiplied by {@link #getRouteTransitTime(String, IVesselClass)}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vesselClass
	 *            class of travelling vessel
	 * @return extra fuel used by vessel, in scaled MT BF(E) per hour
	 */
	public long getRouteFuelUsage(final String route, final IVesselClass vesselClass, final VesselState vesselState);

	/**
	 * Gets the NBO rate, in scaled M3 per hour (see {@link Calculator#ScaleFactor}), required for vessels of class {@code vesselClass} to travel via {@code route} for the given {@link VesselState}.
	 * The total NBO used is then this value multiplied by {@link #getRouteTransitTime(String, IVesselClass)}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vesselClass
	 *            class of travelling vessel
	 * @return NBO rate of vessel, in scaled M3 per hour
	 */
	public long getRouteNBORate(final String route, final IVesselClass vesselClass, final VesselState vesselState);

	/**
	 * Gets the extra time, in hours, which vessels of class {@code vesselClass} must spend to travel via {@code route}.
	 * 
	 * @param route
	 *            route travelled
	 * @param vesselClass
	 *            class of travelling vessel
	 * @return Time to pass through canal, in hours. This is independent of the journey time spent getting from point A to canal entrance and canal exit to point B.
	 */
	public int getRouteTransitTime(final String route, final IVesselClass vesselClass);
}
