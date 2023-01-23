/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Implementation of {@link IPortShipSizeProviderEditor} using {@link HashMap} and {@link HashSet} as backing data store.
 */
public class HashMapPortShipSizeProviderEditor implements IPortShipSizeProviderEditor {

	/**
	 * A map of resources to incompatible sequence element pairs.
	 * Consider making this lookup more efficient perhaps using getIndex() and two dimensional array lookup, if performance is an issue.
	 */
	private final Map<IVessel, Set<ISequenceElement>> incompatibleResourceElementsMap = new HashMap<>();
	
	@Override
	public boolean isCompatible(IVessel vessel, ISequenceElement sequenceElement) {
		Set<ISequenceElement> incompatibleSequenceElements = incompatibleResourceElementsMap.get(vessel);
		if (incompatibleSequenceElements == null) {
			//No incompatible elements.
			return true;
		}
		else {
			//Vessel does not have this sequence element marked as being incompatible.
			return !incompatibleSequenceElements.contains(sequenceElement);
		}
	}

	@Override
	public void addIncompatibleResourceElements(IVessel vessel, Collection<ISequenceElement> elements) {
		incompatibleResourceElementsMap.computeIfAbsent(vessel, p -> new HashSet<>()).addAll(elements);
	}
}
