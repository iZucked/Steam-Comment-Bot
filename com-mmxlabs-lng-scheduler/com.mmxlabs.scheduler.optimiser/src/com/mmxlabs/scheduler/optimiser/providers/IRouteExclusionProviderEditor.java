/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 * Provider for Route exclusions
 * @author achurchill
 *
 */
public interface IRouteExclusionProviderEditor extends IRouteExclusionProvider {

	public void setExcludedRoutes(IVessel vessel, Set<ERouteOption> routes);

	public void setExcludedRoutes(IVesselClass vesselClass, Set<ERouteOption> routes);
}
