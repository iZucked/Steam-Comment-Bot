/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;

import com.google.common.collect.HashBiMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProviderEditor;

/**
 */
public class HashMapAlternativeElementProviderEditor implements IAlternativeElementProviderEditor {

	private final HashBiMap<ISequenceElement, ISequenceElement> map = HashBiMap.create();

	@Override
	public boolean hasAlternativeElement(final ISequenceElement element) {
		return map.containsKey(element) || map.containsValue(element);
	}

	@Override
	public ISequenceElement getAlternativeElement(final ISequenceElement element) {
		if (map.containsKey(element)) {
			return map.get(element);
		} else {
			return map.inverse().get(element);
		}
	}

	@Override
	public void setAlternativeElements(final ISequenceElement elementA, final ISequenceElement elementB) {
		map.put(elementA, elementB);
	}

	@Override
	public boolean isElementOriginal(final ISequenceElement element) {
		return map.containsValue(element);
	}

	@Override
	public boolean isElementAlternative(final ISequenceElement element) {
		return map.containsValue(element);
	}

	@Override
	public Collection<ISequenceElement> getAllAlternativeElements() {
		return map.values();
	}
}
