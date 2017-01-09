/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An {@link IElementDurationProvider} implementation to wrap around a LNG port & type based duration data structure.
 * 
 * @author Simon Goodall
 * 
 */
public class HashMapPortVisitDurationProviderEditor implements IPortVisitDurationProviderEditor {

	private int defaultValue;

	private final Map<IPort, Map<PortType, Integer>> visitDurationsMap = new HashMap<>();

	public HashMapPortVisitDurationProviderEditor(final int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public HashMapPortVisitDurationProviderEditor() {
		this(0);
	}

	@Override
	public int getVisitDuration(@NonNull final IPort port, @NonNull final PortType portType) {
		if (visitDurationsMap.containsKey(port)) {
			final Map<PortType, Integer> m = visitDurationsMap.get(port);
			if (m.containsKey(portType)) {
				return m.get(portType);
			}
		}
		return defaultValue;
	}

	@Override
	public void setVisitDuration(@NonNull final IPort port, @NonNull final PortType portType, final int duration) {
		final Map<PortType, Integer> m;
		if (visitDurationsMap.containsKey(port)) {
			m = visitDurationsMap.get(port);
		} else {
			m = new EnumMap<>(PortType.class);
			visitDurationsMap.put(port, m);
		}
		m.put(portType, duration);
	}
}
