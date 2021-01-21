/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Provider for Route exclusions
 * 
 * @author achurchill
 *
 */
@NonNullByDefault
public interface IRouteExclusionProvider extends IDataComponentProvider {

	/**
	 * Get the set of routes which vessels cannot visit.
	 * 
	 * @param vessel
	 * @return
	 */
	public Set<ERouteOption> getExcludedRoutes(IVessel vessel);

	/**
	 * If there are no exclusions set at all, this returns true. Useful for quickly avoiding execution if this is empty.
	 * 
	 * @return
	 */
	public boolean hasNoExclusions();

	/**
	 * Check whether a route is enabled for a vessel. If vessel is null we assume that the route is enabled, and DIRECT is always enabled.
	 * 
	 * @param vessel
	 * @param route
	 * @return
	 */
	boolean isRouteEnabled(IVessel vessel, ERouteOption route);
}
