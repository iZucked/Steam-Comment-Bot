/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.RelatedSlotAllocations;
import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class CycleDiffProcessor implements IDiffProcessor {

	private final List<@NonNull ICustomRelatedSlotHandler> customRelatedSlotHandlers;
	private final @NonNull RelatedSlotAllocations relatedSlotAllocations = new RelatedSlotAllocations();

	public CycleDiffProcessor(final List<@NonNull ICustomRelatedSlotHandler> customRelatedSlotHandlers) {
		this.customRelatedSlotHandlers = customRelatedSlotHandlers;
	}

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {

				// Always consider cargoes for permutations, even if filtered out
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
					if (cargoAllocation != null) {
						// TODO: Only required for pin/diff mode really.
						relatedSlotAllocations.updateRelatedSetsFor(cargoAllocation);

						for (final ICustomRelatedSlotHandler h : this.customRelatedSlotHandlers) {
							h.addRelatedSlots(relatedSlotAllocations, schedule, cargoAllocation);
						}
					}
				}

			}
		}
		//
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			assert openSlotAllocation != null;
			// Always consider open positions for permutations, even if filtered out
			for (final ICustomRelatedSlotHandler h : customRelatedSlotHandlers) {
				h.addRelatedSlots(relatedSlotAllocations, schedule, openSlotAllocation);
			}
			relatedSlotAllocations.updateRelatedSetsFor(openSlotAllocation);

		}

	}

	private void generateCycleDiffForElement(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final Map<EObject, Row> elementToRowMap, final EObject referenceElement) {

		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}

		final CargoAllocation referenceRowCargoAllocation = referenceRow.getCargoAllocation();
		if (referenceRowCargoAllocation != null) {

			// Check to see if wiring has changed.
			boolean different = false;
			{
				final String currentWiring = CargoAllocationUtils.getSalesWiringAsString(referenceRowCargoAllocation);
				for (final Row referringRow : referenceRow.getReferringRows()) {
					final CargoAllocation pinnedCargoAllocation = referringRow.getCargoAllocation();
					if (pinnedCargoAllocation != null) {
						// convert this cargo's wiring of slot allocations to a string
						final String result = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);

						different = !currentWiring.equals(result);

					} else {
						different = true;
					}
					if (different) {
						break;
					}
				}
			}
			if (different) {

				final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);

				final CargoAllocation thisCargoAllocation = referenceRowCargoAllocation;

				final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, true);
				final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(thisCargoAllocation, false);

				for (final Slot s : buysSet) {
					Object a = relatedSlotAllocations.getSlotAllocation(s);
					if (a == null) {
						a = relatedSlotAllocations.getOpenSlotAllocation(s);
					}
					if (a == null) {
						continue;
					}
					final Row r = elementToRowMap.get(a);
					if (r == null) {
						continue;
					}
					cycleGroup.getRows().add(r);
				}
				for (final Slot s : sellsSet) {
					Object a = relatedSlotAllocations.getSlotAllocation(s);
					if (a == null) {
						a = relatedSlotAllocations.getOpenSlotAllocation(s);
					}
					if (a == null) {
						continue;
					}
					final Row r = elementToRowMap.get(a);
					if (r == null) {
						continue;
					}
					cycleGroup.getRows().add(r);
				}

				cycleGroup.setChangeType(ChangeType.WIRING);
			}
		} else if (referenceRow.getOpenSlotAllocation() != null) {
			final OpenSlotAllocation openSlotAllocation = referenceRow.getOpenSlotAllocation();
			boolean different = false;
			for (final Row referringRow : referenceRow.getReferringRows()) {
				final OpenSlotAllocation allocation = referringRow.getOpenSlotAllocation();
				if (allocation == null) {
					different = true;
				}
			}
			if (different) {
				final Set<Slot> buysSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, true);
				final Set<Slot> sellsSet = relatedSlotAllocations.getRelatedSetFor(openSlotAllocation, false);

				final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);

				for (final Slot s : buysSet) {
					Object a = relatedSlotAllocations.getSlotAllocation(s);
					if (a == null) {
						a = relatedSlotAllocations.getOpenSlotAllocation(s);
					}
					if (a == null) {
						continue;
					}
					final Row r = elementToRowMap.get(a);
					if (r == null) {
						continue;
					}
					cycleGroup.getRows().add(r);
				}
				for (final Slot s : sellsSet) {
					Object a = relatedSlotAllocations.getSlotAllocation(s);
					if (a == null) {
						a = relatedSlotAllocations.getOpenSlotAllocation(s);
					}
					if (a == null) {
						continue;
					}
					final Row r = elementToRowMap.get(a);
					if (r == null) {
						continue;
					}
					cycleGroup.getRows().add(r);
				}
			}
		} else {
			// final Object target = referenceRow.getTarget();
			//
			// if (referenceRow.getCycleGroup() != null) {
			// return;
			// }
			// CycleGroup cycleGroup = null;
			// final Set<EObject> set = equivalancesMap.get(referenceElement);
			// if (set != null) {
			// for (final EObject equiv : set) {
			// final Row r = elementToRowMap.get(equiv);
			// if (r != null) {
			// if (r.getCycleGroup() != null) {
			// assert (cycleGroup == null || r.getCycleGroup() == cycleGroup);
			// cycleGroup = r.getCycleGroup();
			// }
			// }
			// }
			// }
			// if (cycleGroup != null) {
			// referenceRow.setCycleGroup(cycleGroup);
			// } else {
			// cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
			//
			// if (target instanceof GeneratedCharterOut) {
			// cycleGroup.setDescription("Charter out (Virt)");
			// } else if (target instanceof StartEvent) {
			// cycleGroup.setDescription("Orphan Ballast");
			// } else if (target instanceof VesselEventVisit) {
			// final VesselEventVisit vesselEventVisit = (VesselEventVisit) target;
			// final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			// if (vesselEvent instanceof DryDockEvent) {
			// cycleGroup.setDescription("Drydock - " + vesselEvent.getName());
			// } else if (vesselEvent instanceof MaintenanceEvent) {
			// cycleGroup.setDescription("Maintenance - " + vesselEvent.getName());
			// } else if (vesselEvent instanceof CharterOutEvent) {
			// cycleGroup.setDescription("Charter out - " + vesselEvent.getName());
			// } else {
			// cycleGroup.setDescription("Event - " + vesselEvent.getName());
			// }
			//
			// }
			// }
			// if (set != null) {
			// for (final EObject equiv : set) {
			// final Row r = elementToRowMap.get(equiv);
			// if (r != null) {
			// r.setCycleGroup(cycleGroup);
			// }
			// }
			// }
		}
	}

	@Override
	public void runDiffProcess(final Table table, final List<EObject> referenceElements, final List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap,
			final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, referenceElement);
		}
		for (final EObject element : uniqueElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, element);
		}

		// For cargo based cycle groups, construct a particular diff message
		for (final CycleGroup group : table.getCycleGroups()) {
			final Set<Slot> buysSet = new HashSet<>();
			final Set<Slot> sellsSet = new HashSet<>();

			for (final Row r : group.getRows()) {
				final SlotAllocation l = r.getLoadAllocation();
				if (l != null) {
					buysSet.add(l.getSlot());
				}
				final SlotAllocation d = r.getDischargeAllocation();
				if (d != null) {
					sellsSet.add(d.getSlot());
				}
				final OpenSlotAllocation o = r.getOpenSlotAllocation();
				if (o != null) {
					if (o.getSlot() instanceof LoadSlot) {
						buysSet.add(o.getSlot());
					} else {
						sellsSet.add(o.getSlot());
					}
				}
			}
			if (buysSet.isEmpty() && sellsSet.isEmpty()) {
				continue;
			}
			final Set<String> buysStringsSet = slotToStringsSet(buysSet);
			final Set<String> sellsStringsSet = slotToStringsSet(sellsSet);
			final String buysStr = setToString(buysStringsSet);
			final String sellsStr = setToString(sellsStringsSet);

			// Show all rows in the cycle group.
			// TODO: This should be make visible only if dependency has changed.
			for (final Row r : group.getRows()) {
				r.setVisible(true);
			}

			group.setDescription(String.format("Rewire %d x %d; Buys %s, Sells %s", buysStringsSet.size(), sellsStringsSet.size(), buysStr, sellsStr));
		}
	}

	private String setToString(final Set<String> stringsSet) {
		return "[ " + Joiner.on(", ").skipNulls().join(stringsSet) + " ]";
	}

	private Set<String> slotToStringsSet(final Set<Slot> buysSet) {
		return Sets.newTreeSet(Iterables.transform(buysSet, new SlotToStringFunction()));
	}

}
