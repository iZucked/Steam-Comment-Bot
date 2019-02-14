/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteExclusionProviderEditor;

@NonNullByDefault
public class HashMapRouteExclusionProvider implements IRouteExclusionProviderEditor {
	private final HashMap<IVessel, Set<ERouteOption>> exclusionsByVessel = new HashMap<>();

	private boolean isVesselExclusionsEmpty = true;
	private static final Set<ERouteOption> EMPTY = Collections.emptySet();

	@Override
	public Set<ERouteOption> getExcludedRoutes(final IVessel vessel) {
		return exclusionsByVessel.getOrDefault(vessel, EMPTY);
	}

	@Override
	public void setExcludedRoutes(final IVessel vessel, final Set<ERouteOption> excludedRoutes) {
		final Set<ERouteOption> vesselExcludedRoutes = new HashSet<>(excludedRoutes);
		exclusionsByVessel.put(vessel, vesselExcludedRoutes);
		if (vesselExcludedRoutes.isEmpty() == false) {
			isVesselExclusionsEmpty = false;
		}
	}

	@Override
	public boolean hasNoExclusions() {
		return isVesselExclusionsEmpty;
	}

	@Override
	public boolean isRouteEnabled(final IVessel vessel, final ERouteOption route) {
		if (hasNoExclusions() || vessel == null || route == ERouteOption.DIRECT) {
			return true;
		} else {
			final Set<ERouteOption> excludedRoutes = getExcludedRoutes(vessel);
			if (excludedRoutes == EMPTY) {
				return true;
			} else if (excludedRoutes.contains(route)) {
				return false;
			}
		}
		return true;
	}
}
