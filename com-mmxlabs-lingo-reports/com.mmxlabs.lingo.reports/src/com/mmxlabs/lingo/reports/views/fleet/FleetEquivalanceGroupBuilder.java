/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;

public class FleetEquivalanceGroupBuilder {

	private Set<EObject> checkElementEquivalence(final EObject referenceElement, final List<EObject> elements) {

		final Set<EObject> equivalents = new HashSet<>();
		final String keyB = getElementKey(referenceElement);

		{
			for (final EObject eObject : elements) {
				if (getElementKey(eObject).equals(keyB)) {
					equivalents.add(eObject);
				}
			}
		}
		return equivalents;
	}

	public Map<String, List<EObject>> generateElementNameGroups(final Collection<EObject> elements) {
		final Map<String, List<EObject>> scheduleMap = new HashMap<>();
		for (final EObject eObj : elements) {
			final String key = getElementKey(eObj);
			updateLinkedListMap(scheduleMap, key, eObj);
		}
		return scheduleMap;
	}

	/**
	 * Generate a mapping between an element (not necessarily the row key element) and find the equivalent objects in other scenarios.
	 * 
	 * @param maps
	 * @param equivalancesMap
	 * @param uniqueElements
	 * @param elementToRowMap2
	 */
	void populateEquivalenceGroups(final List<Map<String, List<EObject>>> maps, final Map<EObject, Set<EObject>> equivalancesMap, final List<EObject> uniqueElements,
			final Map<EObject, Row> elementToRowMap) {
		if (maps == null) {
			return;
		}
		if (maps.size() < 2) {
			return;
		}

		// Get set of all unique keys
		final Set<String> allKeys = new HashSet<>();
		for (int i = 0; i < maps.size(); ++i) {
			final Map<String, List<EObject>> data = maps.get(i);
			allKeys.addAll(data.keySet());
		}

		// Find all the equivalents from the reference to the other scenarios
		final Map<String, List<EObject>> reference = maps.get(0);
		for (final String key : allKeys) {

			final List<EObject> referenceElements = reference.get(key);
			if (referenceElements == null || referenceElements.isEmpty()) {

				for (int i = 1; i < maps.size(); ++i) {
					final List<EObject> elements = maps.get(i).get(key);

					if (elements == null || elements.isEmpty()) {
						continue;
					}
					uniqueElements.addAll(elements);
				}

				continue;
			}
			boolean foundEquivalent = false;
			for (int i = 1; i < maps.size(); ++i) {
				final List<EObject> elements = maps.get(i).get(key);

				if (elements == null || elements.isEmpty()) {
					continue;
				}

				foundEquivalent = true;
				for (final EObject referenceElement : referenceElements) {
					// Skip these elements types - they are supplemental items.
					if (referenceElement instanceof Journey || referenceElement instanceof Idle || referenceElement instanceof Cooldown) {
						continue;
					}

					final Row referenceRow = elementToRowMap.get(referenceElement);

					final Collection<EObject> equivalences = checkElementEquivalence(referenceElement, elements);
					if (equivalences != null) {
						updateHashSetMap(equivalancesMap, referenceElement, equivalences);
					}

					// TODO: Break out into separate function?
					// Here we attempt to link rows together based on equivalence.
					if (equivalences != null && !equivalences.isEmpty()) {

						// Row referenceRow = elementToRowMap.get(referenceElement);
						for (final EObject e : equivalences) {
							final Row equivalenceRow = elementToRowMap.get(e);
							if (equivalenceRow != null) {
								if (referenceRow != null) {
									equivalenceRow.setLhsLink(referenceRow);
									referenceRow.setLhsLink(equivalenceRow);
								}
							}
						}
					}

				}
			}
			if (!foundEquivalent) {
				uniqueElements.addAll(referenceElements);
			}
		}
	}

	protected <K, V> void updateHashSetMap(final Map<K, Set<V>> map, final K key, final Collection<V> eObj) {
		Set<V> keyElements;
		if (map.containsKey(key)) {
			keyElements = map.get(key);
		} else {
			keyElements = new HashSet<>();
			map.put(key, keyElements);
		}
		keyElements.addAll(eObj);
		// Ensure key is not included
		keyElements.remove(key);

	}

	protected <K, V> void updateLinkedListMap(final Map<K, List<V>> map, final K key, final V eObj) {
		List<V> keyElements;
		if (map.containsKey(key)) {
			keyElements = map.get(key);
		} else {
			keyElements = new LinkedList<>();
			map.put(key, keyElements);
		}
		keyElements.add(eObj);
	}

	//
	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	public String getElementKey(EObject element) {
		if (element instanceof Sequence) {
			Sequence sequence = (Sequence) element;
			return "sequence-" + sequence.getName();
		}
		return element.toString();
	}
}
