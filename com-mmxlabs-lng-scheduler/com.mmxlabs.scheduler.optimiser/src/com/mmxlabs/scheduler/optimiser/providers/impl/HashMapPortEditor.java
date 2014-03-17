/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;

/**
 * Implementation of {@link IPortProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapPortEditor implements IPortProviderEditor {

	private final Map<ISequenceElement, IPort> map = new HashMap<ISequenceElement, IPort>();

	@Override
	public IPort getPortForElement(final ISequenceElement element) {
		if (map.containsKey(element)) {
			return map.get(element);
		}

		return null;
	}

	@Override
	public void setPortForElement(final IPort port, final ISequenceElement element) {
		map.put(element, port);
	}
}