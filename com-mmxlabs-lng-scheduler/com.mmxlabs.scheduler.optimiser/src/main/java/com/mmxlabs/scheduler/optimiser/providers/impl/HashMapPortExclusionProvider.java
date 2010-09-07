package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;

public class HashMapPortExclusionProvider implements IPortExclusionProviderEditor {
	private HashMap<IVesselClass, Set<IPort>> exclusions = new
		HashMap<IVesselClass, Set<IPort>>();
	
	private final String name;

	private boolean isEmpty = true;
	private static final Set<IPort> EMPTY = Collections.emptySet();
	
	public HashMapPortExclusionProvider(String name) {
		this.name = name;
	}
	
	@Override
	public Set<IPort> getExcludedPorts(IVesselClass vesselClass) {
		Set<IPort> ports = exclusions.get(vesselClass);
		if (ports == null) return EMPTY;
		else return ports;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		exclusions.clear();
		exclusions = null;
	}

	@Override
	public void setExcludedPorts(IVesselClass vesselClass,
			Set<IPort> excludedPorts) {
		exclusions.put(vesselClass, new HashSet<IPort>(excludedPorts));
		if (excludedPorts.isEmpty() == false) isEmpty = false;
		else {
			for (Set<IPort> ex : exclusions.values()) {
				if (ex.isEmpty() == false) {
					isEmpty = false;
					return;
				}
			}
			isEmpty = true;
		}
	}

	@Override
	public boolean hasNoExclusions() {
		return isEmpty;
	}

}
