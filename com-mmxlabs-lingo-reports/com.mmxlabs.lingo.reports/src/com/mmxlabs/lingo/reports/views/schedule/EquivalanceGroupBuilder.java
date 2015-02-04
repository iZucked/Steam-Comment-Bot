/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.NamedObject;

public class EquivalanceGroupBuilder {

	private Set<EObject> checkElementEquivalence(final EObject referenceElement, final List<EObject> elements) {

		final Set<EObject> equivalents = new HashSet<>();
		String keyB = getElementKey(referenceElement);
		if (referenceElement instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) referenceElement;
			if (slotAllocation.getSlot() instanceof SpotLoadSlot) {
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa == slotAllocation) {
						continue;
					}

					for (final EObject eObject : elements) {
						if (eObject instanceof SlotAllocation) {
							final SlotAllocation eObjectslotAllocation = (SlotAllocation) eObject;
							final CargoAllocation eObjectcargoAllocation = eObjectslotAllocation.getCargoAllocation();
							for (final SlotAllocation eObjectsa : eObjectcargoAllocation.getSlotAllocations()) {
								if (getElementKey(sa).equals(getElementKey(eObjectsa))) {
									equivalents.add(eObject);
								}
							}
						}
					}

				}

				return equivalents;
			} else if (slotAllocation.getSlot() instanceof SpotDischargeSlot) {
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa == slotAllocation) {
						continue;
					}
					for (final EObject eObject : elements) {
						if (eObject instanceof SlotAllocation) {
							final SlotAllocation eObjectslotAllocation = (SlotAllocation) eObject;
							final CargoAllocation eObjectcargoAllocation = eObjectslotAllocation.getCargoAllocation();
							for (final SlotAllocation eObjectsa : eObjectcargoAllocation.getSlotAllocations()) {
								if (getElementKey(sa).equals(getElementKey(eObjectsa))) {
									equivalents.add(eObject);
								}
							}
						}
					}

				}

				return equivalents;
			}
		}

		{
			for (final EObject eObject : elements) {
				if (getElementKey(eObject).equals(keyB)) {
					equivalents.add(eObject);
				}
			}
		}
		return equivalents;
	}

	public Map<String, List<EObject>> generateElementNameGroups(final List<EObject> elements) {
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

					final Row referenceRow = elementToRowMap.get(referenceElement);

					final Collection<EObject> equivalences = checkElementEquivalence(referenceElement, elements);
					if (equivalences != null) {
						updateHashSetMap(equivalancesMap, referenceElement, equivalences);
					}
					if (equivalences != null && !equivalences.isEmpty()) {
						// Row referenceRow = elementToRowMap.get(referenceElement);
						for (final EObject e : equivalences) {
							final Row equivalenceRow = elementToRowMap.get(e);
							if (e instanceof SlotAllocation) {
								if (((SlotAllocation) e).getSlot() instanceof DischargeSlot) {
									if (equivalenceRow.getReferenceRow() != null) {
										continue;
									}
								}
							}
							equivalenceRow.setReferenceRow(referenceRow);
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
		if (element instanceof Row) {
			if (element.eIsSet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_SLOT_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_SLOT_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__TARGET)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__TARGET);
			}
		}
		if (element instanceof SlotVisit) {
			return getElementKey(((SlotVisit) element).getSlotAllocation());
		}
		if (element instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) element;
			String prefix = "";
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				prefix = "load";
			} else {
				prefix = "discharge";
			}
			if (slot instanceof SpotSlot) {
				final SpotMarket market = ((SpotSlot) slot).getMarket();
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
				return String.format("%s-%s-%s-%s", prefix, market.eClass().getName(), market.getName(), df.format(slot.getWindowStart()));

			} else {
				final String baseName = slotAllocation.getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		} else if (element instanceof OpenSlotAllocation) {
			OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
			String prefix = "";
			final Slot slot = openSlotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				prefix = "load";
			} else {
				prefix = "discharge";
			}
			if (slot instanceof SpotSlot) {
				final SpotMarket market = ((SpotSlot) slot).getMarket();
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
				return String.format("%s-%s-%s-%s", prefix, market.eClass().getName(), market.getName(), df.format(slot.getWindowStart()));

			} else {
				final String baseName = openSlotAllocation.getSlot().getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof Event) {
			return ((Event) element).name();
		}
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

}
