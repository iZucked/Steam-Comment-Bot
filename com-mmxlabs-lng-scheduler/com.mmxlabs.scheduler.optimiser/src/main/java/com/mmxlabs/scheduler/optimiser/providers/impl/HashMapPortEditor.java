/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;

/**
 * Implementation of {@link IPortProviderEditor} using a {@link HashMap} as the
 * backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapPortEditor implements IPortProviderEditor {

	private final String name;

	private final Map<Object, IPort> map = new HashMap<Object, IPort>();

	public HashMapPortEditor(final String name) {
		this.name = name;
	}

	@Override
	public IPort getPortForElement(final Object element) {
		if (map.containsKey(element)) {
			return map.get(element);
		}

		return null;
	}

	@Override
	public void setPortForElement(final IPort port, final Object element) {
		map.put(element, port);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		map.clear();
	}
}
