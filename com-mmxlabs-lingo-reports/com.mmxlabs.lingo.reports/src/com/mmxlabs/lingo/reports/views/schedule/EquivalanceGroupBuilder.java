/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Joiner;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.NamedObject;

public class EquivalanceGroupBuilder {

	public Set<EObject> checkElementEquivalence(final EObject referenceElement, final List<EObject> elements) {

		if (referenceElement instanceof GeneratedCharterOut) {
			return Collections.emptySet();
		}

		final Set<EObject> equivalents = new HashSet<>();
		final String keyB = getElementKey(referenceElement);
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

		if (referenceElement instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) referenceElement;
			final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();// (SlotAllocation) referenceElement;
			if (slotAllocation.getSlot() instanceof SpotLoadSlot) {
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa == slotAllocation) {
						continue;
					}

					for (final EObject eObject : elements) {
						if (eObject instanceof SlotVisit) {
							final SlotVisit slotVisit2 = (SlotVisit) eObject;
							final SlotAllocation eObjectslotAllocation = slotVisit2.getSlotAllocation();
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
						if (eObject instanceof SlotVisit) {
							final SlotVisit slotVisit2 = (SlotVisit) eObject;
							final SlotAllocation eObjectslotAllocation = slotVisit2.getSlotAllocation();

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
	public void populateEquivalenceGroups(final List<Map<String, List<EObject>>> maps, final Map<EObject, Set<EObject>> equivalancesMap, final List<EObject> uniqueElements,
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

					final Collection<EObject> equivalences = checkElementEquivalence(referenceElement, elements);
					if (equivalences != null) {
						updateHashSetMap(equivalancesMap, referenceElement, equivalences);
						// TODO: May not need this section as we pass in SlotVisits explicitly.
						if (referenceElement instanceof SlotAllocation) {
							final SlotAllocation referenceAllocation = (SlotAllocation) referenceElement;
							final Set<EObject> equivalentVisits = new HashSet<>();
							for (final EObject e : equivalences) {
								if (e instanceof SlotAllocation) {
									equivalentVisits.add(((SlotAllocation) e).getSlotVisit());
								}
							}
							updateHashSetMap(equivalancesMap, referenceAllocation.getSlotVisit(), equivalentVisits);

						}
					}

					// TODO: Break out into separate function?
					// Here we attempt to link rows together based on equivalence.
					if (elementToRowMap != null) {
						if (equivalences != null && !equivalences.isEmpty()) {

							// Skip SlotVisits as rows are linked by SlotAllocations
							if (referenceElement instanceof SlotVisit) {
								continue;
							}
							// // Skip DischargeSlots references as we link by load slot
							// if (referenceElement instanceof SlotAllocation) {
							// if (((SlotAllocation) referenceElement).getSlot() instanceof DischargeSlot) {
							// continue;
							// }
							// }

							final Row referenceRow = elementToRowMap.get(referenceElement);
							for (final EObject e : equivalences) {
								final Row equivalenceRow = elementToRowMap.get(e);
								if (equivalenceRow != null) {
									// if (e instanceof SlotAllocation) {
									// if (((SlotAllocation) e).getSlot() instanceof DischargeSlot) {
									//
									// if (equivalenceRow.getReferenceRow() != null) {
									// continue;
									// }
									// }
									// }
									if (referenceRow != null) {
										assert referenceRow != equivalenceRow;
										if (e instanceof SlotAllocation && ((SlotAllocation) e).getSlot() instanceof DischargeSlot) {
											equivalenceRow.setRhsLink(referenceRow);
											referenceRow.setRhsLink(equivalenceRow);
										} else if (e instanceof OpenSlotAllocation && ((OpenSlotAllocation) e).getSlot() instanceof DischargeSlot) {
											equivalenceRow.setRhsLink(referenceRow);
											referenceRow.setRhsLink(equivalenceRow);
										} else {
											equivalenceRow.setLhsLink(referenceRow);
											referenceRow.setLhsLink(equivalenceRow);
										}
									}
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
	public static String getElementKey(EObject element) {
		if (element instanceof Row) {
			if (element.eIsSet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_LOAD_SLOT_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_LOAD_SLOT_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__OPEN_DISCHARGE_SLOT_ALLOCATION)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__OPEN_DISCHARGE_SLOT_ALLOCATION);
			} else if (element.eIsSet(ScheduleReportPackage.Literals.ROW__TARGET)) {
				element = (EObject) element.eGet(ScheduleReportPackage.Literals.ROW__TARGET);
			}
		}
		if (element instanceof SlotVisit) {
			return "visit-" + getElementKey(((SlotVisit) element).getSlotAllocation());
		}
		if (element instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) element;
			String prefix = "";
			final Slot slot = slotAllocation.getSlot();
			prefix = "allocation-" + getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);

			} else {
				final String baseName = slotAllocation.getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof CargoAllocation) {
			return "cargo-" + ((CargoAllocation) element).getName();
		} else if (element instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
			String prefix = "";
			final Slot slot = openSlotAllocation.getSlot();
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else {
				final String baseName = openSlotAllocation.getSlot().getName();
				return prefix + "-" + baseName;
			}
		} else if (element instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) element;
			Sequence sequence = startEvent.getSequence();
			VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			final String base = "start-" + sequence.getName() + "-" + (vesselAvailability == null ? "" : Integer.toString(vesselAvailability.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) element;
			Sequence sequence = endEvent.getSequence();
			VesselAvailability vesselAvailability = sequence.getVesselAvailability();
			final String base = "end-" + sequence.getName() + "-" + (vesselAvailability == null ? "" : Integer.toString(vesselAvailability.getCharterNumber()));
			if (sequence.isSetSpotIndex()) {
				return base + "-" + sequence.getSpotIndex();
			}
			return base;
		} else if (element instanceof GeneratedCharterOut) {
			// Add in hash code to keep elements unique.
			// See start of #checkElementEquivalence
			// Equivalence is really overlapping event time on a resource, element name (currently) encode the previous cargo/event ID which may change.
			return element.eClass().getName() + "-" + ((Event) element).name() + "-" + element.hashCode();
		} else if (element instanceof Event) {
			return element.eClass().getName() + "-" + ((Event) element).name();
		}
		if (element instanceof Slot) {
			String prefix = "";
			final Slot slot = (Slot) element;
			prefix = getSlotTypePrefix(slot);
			if (slot instanceof SpotSlot) {
				return prefix + "-" + getSpotSlotSuffix(slot);
			} else {
				final String baseName = slot.getName();
				return prefix + "-" + baseName;
			}

		}
		if (element instanceof NamedObject) {
			return element.getClass().getName() + "-" + ((NamedObject) element).getName();
		}
		return element.toString();
	}

	protected static @NonNull String getSpotSlotSuffix(final Slot slot) {
		final SpotMarket market = ((SpotSlot) slot).getMarket();
		final String id = String.format("%s-%s-%s", market.eClass().getName(), market.getName(), format(slot.getWindowStart()));
		final Cargo c = slot.getCargo();

		if (c != null) {
			final List<String> elements = new LinkedList<>();
			for (final Slot s : c.getSortedSlots()) {
				if (s == slot) {
					elements.add(id);
				} else if (s instanceof SpotSlot) {
					// Avoid recursion
					elements.add("spot");
				} else {
					elements.add(getElementKey(s));
				}
			}
			return Joiner.on("--").join(elements);
		} else {
			return id;
		}
	}

	protected static @NonNull String getSlotTypePrefix(final Slot slot) {
		String prefix;
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isDESPurchase()) {
				prefix = "des-purchase";
			} else {
				prefix = "fob-purchase";
			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				prefix = "fob-sale";
			} else {
				prefix = "des-sale";
			}
		} else {
			prefix = "unknownslottype";
		}
		return prefix;
	}

	private static @NonNull String format(final @Nullable LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());

	}
}
