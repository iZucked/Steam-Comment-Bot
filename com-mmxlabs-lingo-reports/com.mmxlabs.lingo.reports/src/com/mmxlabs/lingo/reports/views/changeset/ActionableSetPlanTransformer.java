/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.analytics.ActionableSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ActionableSetPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, ActionableSetPlan plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();

		// Assuming first option is the base.
		for (ActionableSet option : plan.getActionSets()) {
			stages.add(new ScenarioResult(instance, option.getScheduleModel()));
		}

		try {
			monitor.beginTask("Opening action plans", stages.size());
			ScenarioResult prev = null;
			for (final ScenarioResult current : stages) {
				if (prev != null) {
					root.getChangeSets().add(buildChangeSet(stages.get(0), prev, current));
				}
				prev = current;
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

	private ChangeSet buildChangeSet(final ScenarioResult base, final ScenarioResult prev, final ScenarioResult current) {
		final ModelReference baseReference = base.getScenarioInstance().getReference("InsertionPlanTransformer:1");
		final ModelReference prevReference = prev.getScenarioInstance().getReference("InsertionPlanTransformer:2");
		final ModelReference currentReference = current.getScenarioInstance().getReference("InsertionPlanTransformer:3");

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

		generateDifferences(base, current, changeSet, true);
		generateDifferences(prev, current, changeSet, false);

		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isBase) {
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
		final List<EObject> deferredElements = processElementsForWiringPass(fromInterestingElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, true);
		// Process deferred elements - i.e. those with multiple spot market options
		processElementsForWiringPass(deferredElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, false);

		ChangeSetTransformerUtil.mergeSpots(rows);
		ChangeSetTransformerUtil.setRowFlags(rows);
		ChangeSetTransformerUtil.filterRows(rows);
		ChangeSetTransformerUtil.sortRows(rows);

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
			// final Set<EObject> equivalents = equivalancesMap.get(element);
			// if (equivalents == null) {
			// continue;
			// }
			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();

					if (loadSlot.getName().contains("FOB_PNG-2016-12")) {
						int ii = 0;
					}
					//
					// for (final EObject e : equivalents) {
					// if (e instanceof SlotVisit) {
					// final SlotVisit slotVisit2 = slotVisit;
					// final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
					// if (cargoAllocation.getSlotAllocations().size() != 2) {
					// throw new RuntimeException("Complex cargoes are not supported");
					// }
					if (ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit, slotVisit.getSlotAllocation(), false, canDefer)) {
						deferredElements.add(element);
						// }
						// }
					}
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation, false);
			} else if (element instanceof Event) {
				Event event = (Event) element;
				ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, false);
			}
		}
		return deferredElements;

	}

}