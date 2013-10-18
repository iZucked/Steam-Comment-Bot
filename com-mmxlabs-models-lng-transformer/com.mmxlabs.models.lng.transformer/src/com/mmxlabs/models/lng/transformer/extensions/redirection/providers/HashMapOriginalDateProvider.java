/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.providers;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;

public class HashMapOriginalDateProvider implements IOriginalDateProviderEditor {

	private final String name;

	private final Map<ISequenceElement, Integer> map = new HashMap<ISequenceElement, Integer>();

	public HashMapOriginalDateProvider(final String name) {
		this.name = name;
	}

	@Override
	public int getOriginalDate(final ISequenceElement loadElement) {
		if (map.containsKey(loadElement)) {
			return map.get(loadElement);
		}
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		map.clear();

	}

	@Override
	public void setOriginalDate(final ISequenceElement loadElement, final int time) {
		map.put(loadElement, time);
	}

}
