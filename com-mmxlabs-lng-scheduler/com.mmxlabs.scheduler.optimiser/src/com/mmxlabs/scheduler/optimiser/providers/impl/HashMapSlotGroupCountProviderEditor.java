/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.ISlotGroupCountProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.SlotGroup;

public final class HashMapSlotGroupCountProviderEditor implements ISlotGroupCountProviderEditor {

	private final Map<ISequenceElement, Collection<SlotGroup>> elementToGroups = new HashMap<ISequenceElement, Collection<SlotGroup>>();
	private final List<SlotGroup> slotGroups = new LinkedList<SlotGroup>();

	@Override
	public SlotGroup createSlotGroup(final Collection<ISequenceElement> elements, final int count) {

		final SlotGroup slotGroup = new SlotGroup(elements, count);
		slotGroups.add(slotGroup);

		for (final ISequenceElement element : elements) {
			final Collection<SlotGroup> group;
			if (elementToGroups.containsKey(element)) {
				group = elementToGroups.get(element);
			} else {
				group = new LinkedList<SlotGroup>();
				elementToGroups.put(element, group);
			}
			group.add(slotGroup);

		}

		return slotGroup;
	}

	@Override
	public Collection<SlotGroup> getGroups() {
		return Collections.unmodifiableCollection(slotGroups);
	}

	@Override
	public Collection<SlotGroup> getGroupsForElement(final ISequenceElement element) {
		if (elementToGroups.containsKey(element)) {
			return elementToGroups.get(element);
		}
		return Collections.emptySet();
	}
}
