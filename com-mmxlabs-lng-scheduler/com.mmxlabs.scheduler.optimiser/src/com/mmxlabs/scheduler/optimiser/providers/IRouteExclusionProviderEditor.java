/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Provider for Route exclusions
 * 
 * @author achurchill
 *
 */
@NonNullByDefault
public interface IRouteExclusionProviderEditor extends IRouteExclusionProvider {

	void setExcludedRoutes(IVessel vessel, Set<ERouteOption> routes);

}
