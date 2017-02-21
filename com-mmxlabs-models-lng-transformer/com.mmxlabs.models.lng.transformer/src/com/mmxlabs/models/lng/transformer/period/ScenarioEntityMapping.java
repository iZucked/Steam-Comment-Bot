/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 * @author Simon Goodall
 *
 */
public final class ScenarioEntityMapping implements IScenarioEntityMapping {

	private final BiMap<@NonNull EObject, @NonNull EObject> originalToCopyMap = HashBiMap.create();
	private final Set<@NonNull EObject> removedObjects = new HashSet<>();

	private final Map<Pair<VesselAssignmentType, Integer>, Pair<AssignableElement, AssignableElement>> trimmedElementsMap = new HashMap<>();

	@Override
	public void createMappings(final Map<@NonNull EObject, @NonNull EObject> originalToCopyMap) {
		this.originalToCopyMap.putAll(originalToCopyMap);
	}

	@Override
	public void createMapping(@NonNull final EObject original, @NonNull final EObject copy) {
		originalToCopyMap.put(original, copy);
	}

	@Override
	public void registerRemovedOriginal(@NonNull final EObject original) {
		removedObjects.add(original);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getOriginalFromCopy(final T copy) {
		return (T) originalToCopyMap.inverse().get(copy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getCopyFromOriginal(final T original) {
		return (T) originalToCopyMap.get(original);
	}

	@Override
	public Collection<@NonNull EObject> getUsedOriginalObjects() {
		final Set<@NonNull EObject> set = new HashSet<>();
		// All the original objects
		set.addAll(originalToCopyMap.keySet());

		// Remove object not in the copy
		set.removeAll(removedObjects);

		return set;
	}

	@Override
	public Collection<@NonNull EObject> getUnusedOriginalObjects() {
		return removedObjects;
	}

	private final Map<@NonNull Pair<@NonNull CharterInMarket, @NonNull Integer>, @Nullable Integer> periodToOriginal = new HashMap<>();
	private final Map<@NonNull Pair<@NonNull CharterInMarket, @NonNull Integer>, @Nullable Integer> originalToPeriod = new HashMap<>();

	@Override
	public void setSpotCharterInMapping(@NonNull final CharterInMarket periodCharterInMarket, final int originalSpotIndex, final int periodSpotIndex) {
		periodToOriginal.put(new Pair<>(periodCharterInMarket, periodSpotIndex), originalSpotIndex);
		originalToPeriod.put(new Pair<>(periodCharterInMarket, originalSpotIndex), periodSpotIndex);
	}

	@Override
	public int getSpotCharterInMappingFromPeriod(@NonNull final CharterInMarket periodCharterInMarket, final int periodSpotIndex) {
		final Integer idx = periodToOriginal.get(new Pair<>(periodCharterInMarket, periodSpotIndex));
		if (idx == null) {
			return periodSpotIndex;
		}
		return idx;
	}

	@Override
	public int getSpotCharterInMappingFromOriginal(@NonNull final CharterInMarket periodCharterInMarket, final int originalSpotIndex) {
		final Integer idx = originalToPeriod.get(new Pair<>(periodCharterInMarket, originalSpotIndex));
		if (idx == null) {
			return originalSpotIndex;
		}
		return idx;
	}

	/**
	 * If null then nothing has been trimmed, the whole "before" is available otherwise element is element used as basis for starting conditions. Original scenario elements only
	 * 
	 * @param vesselAssignmentType
	 * @param spotIndex
	 * @return
	 */
	@Nullable
	public AssignableElement getLastTrimmedBefore(final VesselAssignmentType vesselAssignmentType, final int spotIndex) {

		final Pair<AssignableElement, AssignableElement> p = trimmedElementsMap.computeIfAbsent(new Pair<>(vesselAssignmentType, spotIndex), k -> new Pair<>(null, null));
		return p.getFirst();

	}

	/**
	 * If null then nothing has been trimmed, the whole "after" is available otherwise element is element used as basis for ending conditions. Original scenario elements only
	 * 
	 * @param vesselAssignmentType
	 * @param spotIndex
	 * @return
	 */
	@Nullable
	public AssignableElement getLastTrimmedAfter(final VesselAssignmentType vesselAssignmentType, final int spotIndex) {
		final Pair<AssignableElement, AssignableElement> p = trimmedElementsMap.computeIfAbsent(new Pair<>(vesselAssignmentType, spotIndex), k -> new Pair<>(null, null));
		return p.getSecond();
	}
	@Override
	public void setLastTrimmedBefore(final VesselAssignmentType vesselAssignmentType, int spotIndex, AssignableElement e) {
		Pair<AssignableElement, AssignableElement> p = trimmedElementsMap.computeIfAbsent(new Pair<>(vesselAssignmentType, spotIndex), k -> new Pair<>(null, null));
		p.setFirst(e);
	}

	@Override
	public void setLastTrimmedAfter(final VesselAssignmentType vesselAssignmentType, final int spotIndex, final AssignableElement e) {
		final Pair<AssignableElement, AssignableElement> p = trimmedElementsMap.computeIfAbsent(new Pair<>(vesselAssignmentType, spotIndex), k -> new Pair<>(null, null));
		p.setSecond(e);
	}
}
