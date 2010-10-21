/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class HashMapPortTypeEditor<T> implements
		IPortTypeProviderEditor<T> {

	private final String name;

	private final Map<T, PortType> portTypes = new HashMap<T, PortType>();

	public HashMapPortTypeEditor(final String name) {
		this.name = name;
	}

	@Override
	public void setPortType(final T sequenceElement, final PortType portType) {
		portTypes.put(sequenceElement, portType);
	}

	@Override
	public PortType getPortType(final T sequenceElement) {

		if (portTypes.containsKey(sequenceElement)) {
			return portTypes.get(sequenceElement);
		}

		return PortType.Unknown;
	}

	@Override
	public void dispose() {
		portTypes.clear();
	}

	@Override
	public String getName() {
		return name;
	}
}
