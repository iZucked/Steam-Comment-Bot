package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.ISequenceElementProviderEditor;

/**
 * Implementation of {@link ISequenceElementProviderEditor}. TODO: What about a
 * cargo with same load and discharge port?
 * 
 * @author Simon Goodall
 * 
 */

public final class HashMapSequenceElementProviderEditor implements
		ISequenceElementProviderEditor {

	private final String name;

	private final List<ISequenceElement> sequenceElements;
	private final List<ISequenceElement> unmodifiableSequenceElements;

	private final Map<ICargo, Map<IPort, ISequenceElement>> cargoPortElementMap;

	public HashMapSequenceElementProviderEditor(final String name) {
		this.name = name;
		sequenceElements = new LinkedList<ISequenceElement>();
		// Create wrapper around real list
		unmodifiableSequenceElements = Collections
				.unmodifiableList(sequenceElements);

		cargoPortElementMap = new HashMap<ICargo, Map<IPort, ISequenceElement>>();
	}

	@Override
	public void setSequenceElement(final ICargo cargo, final IPort port,
			final ISequenceElement element) {

		if (sequenceElements.contains(element)) {
			throw new RuntimeException("Element has already been added");
		}

		if (!cargoPortElementMap.containsKey(cargo)) {
			cargoPortElementMap.put(cargo,
					new HashMap<IPort, ISequenceElement>());
		}

		cargoPortElementMap.get(cargo).put(port, element);

		sequenceElements.add(element);

	}

	@Override
	public ISequenceElement getSequenceElement(final ICargo cargo,
			final IPort port) {

		if (cargoPortElementMap.containsKey(cargo)) {
			final Map<IPort, ISequenceElement> map = cargoPortElementMap
					.get(cargo);
			if (map.containsKey(port)) {
				return map.get(port);
			}
		}

		return null;
	}

	@Override
	public List<ISequenceElement> getSequenceElements() {
		return unmodifiableSequenceElements;
	}

	@Override
	public void dispose() {
		cargoPortElementMap.clear();
		sequenceElements.clear();
	}

	@Override
	public String getName() {
		return name;
	}
}
