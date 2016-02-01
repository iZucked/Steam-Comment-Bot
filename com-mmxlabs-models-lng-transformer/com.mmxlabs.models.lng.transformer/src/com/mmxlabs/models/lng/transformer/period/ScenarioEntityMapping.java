/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

/**
 * @author Simon Goodall
 *
 */
public final class ScenarioEntityMapping implements IScenarioEntityMapping {

	private final BiMap<EObject, EObject> originalToCopyMap = HashBiMap.create();
	private final Set<EObject> removedObjects = new HashSet<>();

	@Override
	public void createMappings(Map<EObject, EObject> originalToCopyMap) {
		this.originalToCopyMap.putAll(originalToCopyMap);
	}

	@Override
	public void createMapping(EObject original, EObject copy) {
		originalToCopyMap.put(original, copy);
	}

	@Override
	public void registerRemovedOriginal(EObject original) {
		removedObjects.add(original);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getOriginalFromCopy(T copy) {
		return (T) originalToCopyMap.inverse().get(copy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getCopyFromOriginal(T original) {
		return (T) originalToCopyMap.get(original);
	}

	@Override
	public Collection<EObject> getUsedOriginalObjects() {
		Set<EObject> set = new HashSet<>();
		// All the original objects
		set.addAll(originalToCopyMap.keySet());

		// Remove object not in the copy
		set.removeAll(removedObjects);

		return set;
	}

	@Override
	public Collection<EObject> getUnusedOriginalObjects() {
		return removedObjects;
	}

	private Map<Pair<CharterInMarket, Integer>, Integer> periodToOriginal = new HashMap<>();
	private Map<Pair<CharterInMarket, Integer>, Integer> originalToPeriod = new HashMap<>();

	@Override
	public void setSpotCharterInMapping(CharterInMarket periodCharterInMarket, int originalSpotIndex, int periodSpotIndex) {
		periodToOriginal.put(new Pair<>(periodCharterInMarket, periodSpotIndex), originalSpotIndex);
		originalToPeriod.put(new Pair<>(periodCharterInMarket, originalSpotIndex), periodSpotIndex);
	}

	@Override
	public int getSpotCharterInMappingFromPeriod(CharterInMarket periodCharterInMarket, int periodSpotIndex) {
		Integer idx = periodToOriginal.get(new Pair<>(periodCharterInMarket, periodSpotIndex));
		if (idx == null) {
			return periodSpotIndex;
		}
		return idx;
	}

	@Override
	public int getSpotCharterInMappingFromOriginal(CharterInMarket periodCharterInMarket, int originalSpotIndex) {
		Integer idx = originalToPeriod.get(new Pair<>(periodCharterInMarket, originalSpotIndex));
		if (idx == null) {
			return originalSpotIndex;
		}
		return idx;
	}

}
