/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScenarioComparisonTransformer {

	public ChangeSetRoot createDataModel(final ISelectedDataProvider selectedDataProvider, final Map<EObject, Set<EObject>> equivalancesMap, @NonNull final Table table,
			@NonNull final ScenarioInstance from, @NonNull final ScenarioInstance to, final IProgressMonitor monitor) {
		monitor.beginTask("Opening change sets", 1);
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		assert root != null;

		try (final ModelReference toRef = to.getReference("ScenarioComparisonTransformer:1")) {
			toRef.getInstance();
			try (final ModelReference fromRef = from.getReference("ScenarioComparisonTransformer:2")) {
				fromRef.getInstance();
				final Schedule toSchedule = selectedDataProvider.getSchedule(to.getInstance());
				final Schedule fromSchedule = selectedDataProvider.getSchedule(from.getInstance());

				if (fromSchedule == null) {
					return root;
				}
				if (toSchedule == null) {
					return root;
				}

				final List<ChangeSet> changeSets = new LinkedList<>();

				// Convert into new data model.
				for (final UserGroup g : table.getUserGroups()) {
					// final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
					final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
					final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

					final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
					final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

					final ChangeSet changeSet = createChangeSet(root, from, to);
					final List<ChangeSetRow> rows = new LinkedList<>();

					for (final CycleGroup cycleGroup : g.getGroups()) {
						processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 0);
						processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 1);
						processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 2);
					}
					processRows(toSchedule, fromSchedule, rows, changeSet);
					if (!changeSet.getChangeSetRowsToPrevious().isEmpty()) {
						changeSets.add(changeSet);
					}
				}
				for (final CycleGroup cycleGroup : table.getCycleGroups()) {
					final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
					final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

					final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
					final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

					final List<ChangeSetRow> rows = new LinkedList<>();

					final ChangeSet changeSet = createChangeSet(root, from, to);

					processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 0);
					processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 1);
					processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, 2);

					processRows(toSchedule, fromSchedule, rows, changeSet);
					if (!changeSet.getChangeSetRowsToPrevious().isEmpty()) {
						changeSets.add(changeSet);
					}
				}
				Collections.sort(changeSets, new Comparator<ChangeSet>() {

					@Override
					public int compare(final ChangeSet o1, final ChangeSet o2) {
						boolean o1HasStructureChange = false;
						boolean o2HasStructureChange = false;

						for (final ChangeSetRow r : o1.getChangeSetRowsToPrevious()) {
							o1HasStructureChange |= r.isWiringChange();
							o1HasStructureChange |= r.isVesselChange();
						}
						for (final ChangeSetRow r : o2.getChangeSetRowsToPrevious()) {
							o2HasStructureChange |= r.isWiringChange();
							o2HasStructureChange |= r.isVesselChange();
						}

						if (o1HasStructureChange != o2HasStructureChange) {
							if (o1HasStructureChange) {
								return -1;
							} else {
								return 1;
							}
						}

						return Integer.compare(o2.getMetricsToPrevious().getPnlDelta(), o1.getMetricsToPrevious().getPnlDelta());
					}
				});
				root.getChangeSets().addAll(changeSets);
				return root;

			}
		}
	}

	@NonNull
	private ChangeSet createChangeSet(@NonNull final ChangeSetRoot root, @NonNull final ScenarioInstance prev, @NonNull final ScenarioInstance current) {
		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();

		// final ModelReference baseReference = base.getReference();
		final ModelReference prevReference = prev.getReference("ScenarioComparisonTransformer:3");
		final ModelReference currentReference = current.getReference("ScenarioComparisonTransformer:4");

		// Pre-Load
		// baseReference.getInstance();
		prevReference.getInstance();
		currentReference.getInstance();

		// final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		// changeSet.setBaseScenario(base);
		// changeSet.setBaseScenarioRef(baseReference);
		changeSet.setPrevScenario(prev);
		changeSet.setPrevScenarioRef(prevReference);
		changeSet.setCurrentScenario(current);
		changeSet.setCurrentScenarioRef(currentReference);

		return changeSet;
	}

	private void processRows(@NonNull final Schedule toSchedule, @NonNull final Schedule fromSchedule, @NonNull final List<ChangeSetRow> rows, @NonNull final ChangeSet changeSet) {
		ChangeSetTransformerUtil.mergeSpots(rows);
		ChangeSetTransformerUtil.setRowFlags(rows);
		// ChangeSetTransformerUtil.filterRows(rows);
		ChangeSetTransformerUtil.sortRows(rows);
		changeSet.getChangeSetRowsToPrevious().addAll(rows);
		calculateMetrics(changeSet, fromSchedule, toSchedule);
	}

	private void processCycleGroup(final CycleGroup cycleGroup, @NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap,
			@NonNull final Map<String, List<ChangeSetRow>> lhsRowMarketMap, @NonNull final Map<String, List<ChangeSetRow>> rhsRowMarketMap, @NonNull final List<ChangeSetRow> rows,
			final Map<EObject, Set<EObject>> equivalancesMap, final int pass) {
		for (final Row r : cycleGroup.getRows()) {

			final boolean isBase = true;

			if (pass == 0 && r.isReference()) {
				continue;
			}
			if (pass == 2 && !r.isReference()) {
				continue;
			}

			EObject element = r.getTarget();
			if (element instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) element;
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					if (slotAllocation.getSlot() instanceof LoadSlot) {
						final SlotVisit slotVisit = slotAllocation.getSlotVisit();
						element = slotVisit;
						break;
					}
				}
			}
			assert element != null;

			if (pass == 0) {
				ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, isBase, false);
			} else if (pass == 1) {
				if (r.isReference()) {
					// final Set<EObject> equivalents = equivalancesMap.get(element);
					// if (equivalents == null) {
					ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, false, false);

					continue;
				}
				// ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, rows, element, isBase);
			} else if (pass == 3 && !r.isReference()) {
				final Set<EObject> equivalents = equivalancesMap.get(element);
				if (equivalents == null) {
					// ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, rows, element, !r.isReference());

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
								ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit2,
										(LoadSlot) slotVisit2.getSlotAllocation().getSlot(), false, false);
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
					ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, false);
				}
			}
		}
	}

	private static void calculateMetrics(@NonNull final ChangeSet changeSet, @NonNull final Schedule fromSchedule, @NonNull final Schedule toSchedule) {
		final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
		final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();

		long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(fromSchedule);
		long lateness = ScheduleModelKPIUtils.getScheduleLateness(fromSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		long violations = ScheduleModelKPIUtils.getScheduleViolationCount(fromSchedule);

		currentMetrics.setPnl((int) pnl);
		currentMetrics.setCapacity((int) violations);
		currentMetrics.setLateness((int) lateness);
		pnl = 0;
		violations = 0;
		lateness = 0;
		{
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				{
					final ProfitAndLossContainer newGroupProfitAndLoss = row.getNewGroupProfitAndLoss();

					if (newGroupProfitAndLoss != null) {
						final GroupProfitAndLoss groupProfitAndLoss = newGroupProfitAndLoss.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl += groupProfitAndLoss.getProfitAndLoss();
						}
						if (newGroupProfitAndLoss instanceof CargoAllocation) {
							final CargoAllocation cargoAllocation = (CargoAllocation) newGroupProfitAndLoss;
						}
					}
					final EventGrouping newEventGrouping = row.getNewEventGrouping();
					if (newEventGrouping != null) {

						lateness += LatenessUtils.getLatenessExcludingFlex(newEventGrouping);
						violations += ScheduleModelKPIUtils.getCapacityViolationCount(newEventGrouping);
					}

					final ProfitAndLossContainer originalGroupProfitAndLoss = row.getOriginalGroupProfitAndLoss();
					if (originalGroupProfitAndLoss != null) {
						final GroupProfitAndLoss groupProfitAndLoss = originalGroupProfitAndLoss.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl -= groupProfitAndLoss.getProfitAndLoss();
						}
					}
					final EventGrouping originalEventGrouping = row.getOriginalEventGrouping();
					if (originalEventGrouping != null) {
						lateness = LatenessUtils.getLatenessExcludingFlex(originalEventGrouping);
						violations = ScheduleModelKPIUtils.getCapacityViolationCount(originalEventGrouping);
					}
				}
			}
		}
		deltaMetrics.setPnlDelta((int) pnl);
		deltaMetrics.setLatenessDelta((int) lateness);
		deltaMetrics.setCapacityDelta((int) violations);
		changeSet.setMetricsToPrevious(deltaMetrics);

		changeSet.setCurrentMetrics(currentMetrics);
	}
}
