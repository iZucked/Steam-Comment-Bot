/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Implementation of {@link IPanamaSlotsProviderEditor} using a {@link HashMap} as backing data store.
 * 
 */
public class HashMapPanamaSlotsProviderEditor implements IPanamaSlotsProviderEditor {

	private ImmutableMap<IPort, ImmutableSortedSet<Integer>> panamaSlots;
	
	@Override
	public void setSlots(Map<IPort, SortedSet<Integer>> slots) {
		ImmutableMap.Builder<IPort, ImmutableSortedSet<Integer>> builder = new ImmutableMap.Builder<IPort, ImmutableSortedSet<Integer>>();
		slots.forEach((k,v) -> {
			builder.put(k, ImmutableSortedSet.copyOf(v));
		});
		panamaSlots = builder.build();
	}

	@Override
	public ImmutableMap<IPort, ImmutableSortedSet<Integer>> getSlots() {
		if (panamaSlots == null){
			throw new IllegalStateException("Panama slots not set");
		}
		return panamaSlots;
	}
}
