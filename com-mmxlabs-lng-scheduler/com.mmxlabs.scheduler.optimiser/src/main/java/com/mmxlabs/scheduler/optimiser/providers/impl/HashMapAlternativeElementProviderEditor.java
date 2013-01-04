package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;

import com.google.common.collect.HashBiMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProviderEditor;

/**
 * @since 2.0
 */
public class HashMapAlternativeElementProviderEditor implements IAlternativeElementProviderEditor {

	private final HashBiMap<ISequenceElement, ISequenceElement> map = HashBiMap.create();

	private final String name;

	public HashMapAlternativeElementProviderEditor(final String name) {
		this.name = name;
	}

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
	public void dispose() {
		map.clear();
	}

	@Override
	public String getName() {
		return name;
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
