/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ScheduleResultListTransformer {

	public ChangeSet buildChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current) {
		return buildChangeSet(base, prev, current, null);
	}

	public ChangeSet buildChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current, @Nullable Slot targetToSortFirst) {
		final ModelReference baseReference = base.getScenarioInstance().getReference("ScheduleResultListTransformer:1");
		final ModelReference prevReference = prev.getScenarioInstance().getReference("ScheduleResultListTransformer:2");
		final ModelReference currentReference = current.getScenarioInstance().getReference("ScheduleResultListTransformer:3");

		// Pre-Load
		baseReference.getInstance();
		prevReference.getInstance();
		currentReference.getInstance();

		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setBaseScenario(base);
		changeSet.setBaseScenarioRef(baseReference);
		changeSet.setPrevScenario(prev);
		changeSet.setPrevScenarioRef(prevReference);
		changeSet.setCurrentScenario(current);
		changeSet.setCurrentScenarioRef(currentReference);

		generateDifferences(base, current, changeSet, true, targetToSortFirst);
		generateDifferences(prev, current, changeSet, false, targetToSortFirst);

		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isBase, @Nullable Slot targetToSortFirst) {
		final EquivalanceGroupBuilder equivalanceGroupBuilder = new EquivalanceGroupBuilder();

		final Set<EObject> fromInterestingElements = new LinkedHashSet<>();
		final Set<EObject> toInterestingElements = new LinkedHashSet<>();
		final Set<EObject> fromAllElements = new LinkedHashSet<>();
		final Set<EObject> toAllElements = new LinkedHashSet<>();

		final ScheduleModel fromScheduleModel = from.getTypedResult(ScheduleModel.class);
		final ScheduleModel toScheduleModel = to.getTypedResult(ScheduleModel.class);
		if (fromScheduleModel == null || toScheduleModel == null) {
			return;
		}

		final Schedule fromSchedule = fromScheduleModel.getSchedule();
		final Schedule toSchedule = toScheduleModel.getSchedule();
		if (fromSchedule == null || toSchedule == null) {
			return;
		}

		ChangeSetTransformerUtil.extractElements(fromSchedule, fromInterestingElements, fromAllElements);
		ChangeSetTransformerUtil.extractElements(toSchedule, toInterestingElements, toAllElements);

		// Generate the element by key map
		final List<Map<String, List<EObject>>> perScenarioElementsByKeyMap = new LinkedList<>();
		// Make the "to" scenario the reference scenario
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(toInterestingElements));
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(fromInterestingElements));

		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
		final List<EObject> uniqueElements = new LinkedList<>();

		equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, null);

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Pass one, construct current wiring.
		for (final EObject element : toInterestingElements) {
			assert element != null;
			ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, true, false);
		}

		for (final EObject element : uniqueElements) {
			assert element != null;
			final boolean isBaseElement = toAllElements.contains(element);
			if (!isBaseElement) {
				ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, isBaseElement, false);
			}
		}

		// Second pass, create the wiring links.
		final List<EObject> deferredElements = processElementsForWiringPass(toInterestingElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, true);
		// Process deferred elements - i.e. those with multiple spot market options
		processElementsForWiringPass(deferredElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, false);

		ChangeSetTransformerUtil.mergeSpots(rows);
		ChangeSetTransformerUtil.setRowFlags(rows);
		ChangeSetTransformerUtil.filterRows(rows);
		ChangeSetTransformerUtil.sortRows(rows, targetToSortFirst);

		// Add to data model
		if (isBase) {
			changeSet.getChangeSetRowsToBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToPrevious().addAll(rows);
		}

		// Build metrics
		ChangeSetTransformerUtil.calculateMetrics(changeSet, fromSchedule, toSchedule, isBase);
	}

	private List<EObject> processElementsForWiringPass(final Collection<EObject> elements, final Map<EObject, Set<EObject>> equivalancesMap, final Map<String, ChangeSetRow> lhsRowMap,
			final Map<String, ChangeSetRow> rhsRowMap, final Map<String, List<ChangeSetRow>> lhsRowMarketMap, final Map<String, List<ChangeSetRow>> rhsRowMarketMap, final List<ChangeSetRow> rows,
			final boolean canDefer) {
		final List<EObject> deferredElements = new LinkedList<>();

		for (final EObject element : elements) {
			final Set<EObject> equivalents = equivalancesMap.get(element);
			if (equivalents == null) {
				continue;
			}
			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();

					for (final EObject e : equivalents) {
						if (e instanceof SlotVisit) {
							final SlotVisit slotVisit2 = (SlotVisit) e;
							final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
							if (cargoAllocation.getSlotAllocations().size() != 2) {
								throw new RuntimeException("Complex cargoes are not supported");
							}
							if (ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit2, slotVisit2.getSlotAllocation(), false,
									canDefer)) {
								deferredElements.add(element);
							}
						}
					}
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				for (final EObject e : equivalents) {
					if (e instanceof OpenSlotAllocation) {
						final OpenSlotAllocation openSlotAllocation2 = (OpenSlotAllocation) e;
						ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation2, false);
					}
				}
			} else if (element instanceof Event) {
				final Event event = (Event) element;
				for (final EObject e : equivalents) {
					if (e instanceof Event) {
						final Event event2 = (Event) e;
						ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event2, false);
					}
				}
			}
		}
		return deferredElements;

	}

}
