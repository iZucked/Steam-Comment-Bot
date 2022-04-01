/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;

public class HashMapPortExclusionProvider implements IPortExclusionProviderEditor {
	private HashMap<IVessel, Set<IPort>> exclusionsByVessel = new HashMap<>();

	private boolean isVesselExclusionsEmpty = true;

	private static final Set<IPort> EMPTY = Collections.emptySet();

	/**
	 */
	@Override
	public Set<IPort> getExcludedPorts(final IVessel vessel) {
		final Set<IPort> ports = exclusionsByVessel.get(vessel);
		if (ports == null) {
			return EMPTY;
		} else {
			return ports;
		}
	}

	@Override
	public void setExcludedPorts(final IVessel vessel, final Set<IPort> excludedPorts) {
		exclusionsByVessel.put(vessel, new HashSet<IPort>(excludedPorts));
		if (excludedPorts.isEmpty() == false) {
			isVesselExclusionsEmpty = false;
		} else {
			for (final Set<IPort> ex : exclusionsByVessel.values()) {
				if (ex.isEmpty() == false) {
					isVesselExclusionsEmpty = false;
					return;
				}
			}
			isVesselExclusionsEmpty = true;
		}
	}

	@Override
	public boolean hasNoExclusions() {
		return isVesselExclusionsEmpty;
	}
}
