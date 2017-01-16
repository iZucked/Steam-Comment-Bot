/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class HashMapPortTypeEditor implements IPortTypeProviderEditor {

	private final Map<ISequenceElement, PortType> portTypes = new HashMap<ISequenceElement, PortType>();

	@Override
	public void setPortType(final ISequenceElement sequenceElement, final PortType portType) {
		portTypes.put(sequenceElement, portType);
	}

	@Override
	public PortType getPortType(final ISequenceElement sequenceElement) {

		if (portTypes.containsKey(sequenceElement)) {
			return portTypes.get(sequenceElement);
		}

		return PortType.Unknown;
	}
}
