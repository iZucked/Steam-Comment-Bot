/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProviderEditor;

public class HashMapRouteExclusionProvider implements IRouteExclusionProviderEditor {
	private HashMap<IVesselClass, Set<ERouteOption>> exclusionsByVesselClass = new HashMap<>();
	private HashMap<IVessel, Set<ERouteOption>> exclusionsByVessel = new HashMap<>();

	private boolean isVesselExclusionsEmpty = true;
	private boolean isVesselClassExclusionsEmpty = true;
	private static final Set<ERouteOption> EMPTY = Collections.emptySet();

	@Override
	public Set<ERouteOption> getExcludedRoutes(final IVesselClass vesselClass) {
		final Set<ERouteOption> routes = exclusionsByVesselClass.get(vesselClass);
		if (routes == null) {
			return EMPTY;
		} else {
			return routes;
		}
	}

	/**
	 */
	@Override
	public Set<ERouteOption> getExcludedRoutes(final IVessel vessel) {
		final Set<ERouteOption> routes = exclusionsByVessel.get(vessel);
		if (routes == null) {
			return EMPTY;
		} else {
			return routes;
		}
	}

	@Override
	public void setExcludedRoutes(final IVesselClass vesselClass, final Set<ERouteOption> excludedRoutes) {
		exclusionsByVesselClass.put(vesselClass, new HashSet<ERouteOption>(excludedRoutes));
		if (excludedRoutes.isEmpty() == false) {
			isVesselClassExclusionsEmpty = false;
		} else {
			for (final Set<ERouteOption> ex : exclusionsByVesselClass.values()) {
				if (ex.isEmpty() == false) {
					isVesselClassExclusionsEmpty = false;
					return;
				}
			}
			isVesselClassExclusionsEmpty = true;
		}
	}

	/**
	 */
	@Override
	public void setExcludedRoutes(final IVessel vessel, final Set<ERouteOption> excludedRoutes) {
		HashSet<ERouteOption> vesselExcludedRoutes = new HashSet<ERouteOption>(excludedRoutes);
		exclusionsByVessel.put(vessel, vesselExcludedRoutes);
		if (vesselExcludedRoutes.isEmpty() == false) {
			isVesselExclusionsEmpty = false;
		}
		Set<ERouteOption> excludedRoutesForVesselClass = getExcludedRoutes(vessel.getVesselClass());
		// add all vessel class excluded routes to make lookup more efficient
		vesselExcludedRoutes.addAll(excludedRoutesForVesselClass);
	}

	@Override
	public boolean hasNoExclusions() {
		return isVesselExclusionsEmpty && isVesselClassExclusionsEmpty;
	}
	
	@Override
	public boolean isRouteEnabled(IVessel vessel, ERouteOption route) {
		if (hasNoExclusions() || vessel == null || route == ERouteOption.DIRECT) {
			return true;
		} else {
			Set<ERouteOption> excludedRoutes = getExcludedRoutes(vessel);
			if (excludedRoutes == EMPTY) { 
				return true;
			} else if (excludedRoutes.contains(route)) {
				return false;
			}
		}
		return true;
	}
}
