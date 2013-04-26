/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProviderEditor;

/**
 * Implementation of {@link IPortCVProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public final class HashMapPortCVProviderEditor implements IPortCVProviderEditor {

	private final String name;

	private final Map<IPort, Integer> map = new HashMap<IPort, Integer>();

	public HashMapPortCVProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public int getPortCV(final IPort port) {
		if (map.containsKey(port)) {
			return map.get(port);
		}

		return 0;
	}

	@Override
	public void setPortCV(final IPort port, final int cv) {
		map.put(port, cv);
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
