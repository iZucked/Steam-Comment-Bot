/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedslots;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link IRestrictedSlotsProviderEditor} using a {@link HashMap} as backing data store.
 * 
 * @author Simon Goodall, FM
 */
public class HashMapRestrictedSlotsProviderEditor implements IRestrictedSlotsProviderEditor {

	private final Map<ISequenceElement, Collection<ISequenceElement>> followerMap = new HashMap<>();
	private final Map<ISequenceElement, Collection<ISequenceElement>> precedingMap = new HashMap<>();

	@Override
	public Collection<ISequenceElement> getRestrictedFollowerElements(final ISequenceElement element) {
		if (followerMap.containsKey(element)) {
			return followerMap.get(element);
		}
		return Collections.emptySet();
	}

	@Override
	public Collection<ISequenceElement> getRestrictedPrecedingElements(final ISequenceElement element) {
		if (precedingMap.containsKey(element)) {
			return precedingMap.get(element);
		}
		return Collections.emptySet();
	}

	@Override
	public void addRestrictedElements(final ISequenceElement element, final Collection<ISequenceElement> restrictedPreceders, final Collection<ISequenceElement> restrictedFollowers) {
		if (restrictedFollowers != null) {
			if (followerMap.containsKey(element)) {
				followerMap.get(element).addAll(restrictedFollowers);
			} else {
				followerMap.put(element, new HashSet<ISequenceElement>(restrictedFollowers));
			}
		}
		if (restrictedPreceders != null) {
			if (precedingMap.containsKey(element)) {
				precedingMap.get(element).addAll(restrictedPreceders);
			} else {
				precedingMap.put(element, new HashSet<ISequenceElement>(restrictedPreceders));
			}
		}

	}

}
