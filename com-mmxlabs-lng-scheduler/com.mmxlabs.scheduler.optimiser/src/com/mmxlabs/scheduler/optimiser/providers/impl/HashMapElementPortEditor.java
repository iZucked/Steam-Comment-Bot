/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProviderEditor;

/**
 * Implementation of {@link IElementPortProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapElementPortEditor implements IElementPortProviderEditor {

	private final Map<@NonNull ISequenceElement, @NonNull IPort> map = new HashMap<>();

	@Override
	public @NonNull IPort getPortForElement(final @NonNull ISequenceElement element) {
		if (map.containsKey(element)) {
			return map.get(element);
		}

		throw new IllegalArgumentException();
	}

	@Override
	public void setPortForElement(final @NonNull IPort port, final @NonNull ISequenceElement element) {
		map.put(element, port);
	}
}