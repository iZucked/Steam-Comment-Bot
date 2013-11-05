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

	private final String name;

	private int defaultValue;

	private final Map<IPort, Map<PortType, Integer>> visitDurationsMap = new HashMap<>();

	public HashMapPortVisitDurationProviderEditor(final String name, final int defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public HashMapPortVisitDurationProviderEditor(final String name) {
		this(name, 0);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {

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
		}
		m.put(portType, duration);
	}
}
