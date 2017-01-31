/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil.MappingModel;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ScenarioComparisonTransformer {

	public ChangeSetRoot createDataModel(final ISelectedDataProvider selectedDataProvider, final Map<EObject, Set<EObject>> equivalancesMap, @NonNull final Table table,
			@NonNull final ScenarioResult from, @NonNull final ScenarioResult to, final IProgressMonitor monitor) {
		monitor.beginTask("Opening change sets", 1);
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		assert root != null;

		try (final ModelReference toRef = to.getScenarioInstance().getReference("ScenarioComparisonTransformer:1")) {
			toRef.getInstance();
			try (final ModelReference fromRef = from.getScenarioInstance().getReference("ScenarioComparisonTransformer:2")) {
				fromRef.getInstance();
				final ScheduleModel toScheduleModel = to.getTypedResult(ScheduleModel.class);
				final ScheduleModel fromScheduleModel = from.getTypedResult(ScheduleModel.class);

				if (fromScheduleModel == null) {
					return root;
				}
				if (toScheduleModel == null) {
					return root;
				}

				final Schedule toSchedule = toScheduleModel.getSchedule();
				final Schedule fromSchedule = fromScheduleModel.getSchedule();

				if (fromSchedule == null) {
					return root;
				}
				if (toSchedule == null) {
					return root;
				}
				final List<ChangeSet> changeSets = new LinkedList<>();
				// TODO - fix the comparison logic to support LDD
				// Convert into new data model.
				for (final UserGroup g : table.getUserGroups()) {

					final ChangeSet changeSet = createChangeSet(root, from, to);

					// Before
					Set<EObject> beforeTargets = new LinkedHashSet<EObject>();
					for (final CycleGroup cycleGroup : g.getGroups()) {
						beforeTargets.addAll(processCycleGroup(cycleGroup, 0));
					}
					Set<EObject> afterTargets = new LinkedHashSet<EObject>();
					for (final CycleGroup cycleGroup : g.getGroups()) {
						afterTargets.addAll(processCycleGroup(cycleGroup, 1));
					}

					// Generate the row data
					final MappingModel beforeDifferences = ChangeSetTransformerUtil.generateMappingModel(new LinkedList<>(beforeTargets));
					final MappingModel afterDifferences = ChangeSetTransformerUtil.generateMappingModel(new LinkedList<>(afterTargets));

					final List<ChangeSetRow> rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeDifferences, afterDifferences);

					processRows(toSchedule, fromSchedule, rows, changeSet);
					if (!changeSet.getChangeSetRowsToPrevious().isEmpty()) {
						changeSets.add(changeSet);
					}
				}
				for (final CycleGroup cycleGroup : table.getCycleGroups()) {

					// Before
					Set<EObject> beforeTargets = new LinkedHashSet<EObject>();
					beforeTargets.addAll(processCycleGroup(cycleGroup, 0));
					Set<EObject> afterTargets = new LinkedHashSet<EObject>();
					afterTargets.addAll(processCycleGroup(cycleGroup, 1));

					// Generate the row data
					final MappingModel beforeDifferences = ChangeSetTransformerUtil.generateMappingModel(new LinkedList<>(beforeTargets));
					final MappingModel afterDifferences = ChangeSetTransformerUtil.generateMappingModel(new LinkedList<>(afterTargets));

					final List<ChangeSetRow> rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeDifferences, afterDifferences);

					final ChangeSet changeSet = createChangeSet(root, from, to);
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
	private ChangeSet createChangeSet(@NonNull final ChangeSetRoot root, @NonNull final ScenarioResult prev, @NonNull final ScenarioResult current) {
		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();

		// final ModelReference baseReference = base.getReference();
		final ModelReference prevReference = prev.getScenarioInstance().getReference("ScenarioComparisonTransformer:3");
		final ModelReference currentReference = current.getScenarioInstance().getReference("ScenarioComparisonTransformer:4");

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

		// ChangeSetTransformerUtil.mergeSpots(rows);
		ChangeSetTransformerUtil.setRowFlags(rows);
		// ChangeSetTransformerUtil.filterRows(rows);
		// ChangeSetTransformerUtil.sortRows(rows, null);
		changeSet.getChangeSetRowsToPrevious().addAll(rows);
		calculateMetrics(changeSet, fromSchedule, toSchedule);
	}

	private List<EObject> processCycleGroup(final CycleGroup cycleGroup, final int pass) {

		boolean isBase = pass == 0;

		List<EObject> targets = new LinkedList<>();
		for (final Row r : cycleGroup.getRows()) {

			boolean rBase = isBase && r.isReference();
			boolean rOther = !isBase && !r.isReference();
			if (!rBase && !rOther) {
				continue;
			}

			EObject element = r.getTarget();
			if (element instanceof CargoAllocation) {
				CargoAllocation cargoAllocation = (CargoAllocation) element;
				if (!targets.contains(cargoAllocation)) {
					targets.add(cargoAllocation);
				}
			} else if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
				if (slotAllocation != null) {
					CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
					if (!targets.contains(cargoAllocation)) {
						targets.add(cargoAllocation);
					}
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				targets.add(openSlotAllocation);
			} else if (element instanceof Event) {
				Event event = (Event) element;
				if (event instanceof VesselEventVisit) {
					targets.add(event);
				} else if (event instanceof GeneratedCharterOut) {
					targets.add(event);
				} else {
					Sequence sequence = event.getSequence();
					if (sequence.getSequenceType() == SequenceType.VESSEL //
							|| sequence.getSequenceType() == SequenceType.ROUND_TRIP //
							|| sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
						// Filter events
						if (event instanceof Journey) {
							continue;
						} else if (event instanceof Idle) {
							continue;
						} else if (event instanceof Cooldown) {
							continue;
						}

						else if (event instanceof StartEvent //
								|| event instanceof EndEvent) {
							if (sequence.getSequenceType() == SequenceType.VESSEL) {
								// Only include these for fleet vessels.

							} else {
								continue;
							}
						} else if (event instanceof VesselEventVisit) {
							// Keep going!
						} else if (event instanceof GeneratedCharterOut) {
							// Keep going!
						} else if (event instanceof SlotVisit) {
							// Already processed
							continue;
						} else if (event instanceof PortVisit) {
							if (sequence.getSequenceType() == SequenceType.VESSEL) {
								// Only include these for fleet vessels.

							} else {
								// On a spot vessel "probably" the fake end ports -- TODO; tighten check up
								continue;
							}
						} else {
							// Unknown event!
							assert false;
						}

						targets.add(event);
					}
				}

			}
		}
		return targets;
	}
	//
	// public void a() {
	// if (pass == 0) {
	// ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, isBase, false);
	// } else if (pass == 1) {
	// if (r.isReference()) {
	// ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, false, false);
	//
	// continue;
	// }
	// } else if (pass == 3 && !r.isReference()) {
	// final Set<EObject> equivalents = equivalancesMap.get(element);
	// if (equivalents == null) {
	// continue;
	// }
	//
	// if (element instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) element;
	// if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
	// final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();
	//
	// for (final EObject e : equivalents) {
	// if (e instanceof SlotVisit) {
	// final SlotVisit slotVisit2 = (SlotVisit) e;
	// final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
	// if (cargoAllocation.getSlotAllocations().size() != 2) {
	// throw new RuntimeException("Complex cargoes are not supported");
	// }
	// ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit2,
	// (LoadSlot) slotVisit2.getSlotAllocation().getSlot(), false, false);
	// }
	// }
	// }
	// } else if (element instanceof OpenSlotAllocation) {
	//
	// for (final EObject e : equivalents) {
	// if (e instanceof OpenSlotAllocation) {
	// final OpenSlotAllocation openSlotAllocation2 = (OpenSlotAllocation) e;
	// ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation2, false);
	// }
	// }
	// } else if (element instanceof Event) {
	// final Event event = (Event) element;
	// ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, false);
	// }
	// }
	// }
	// }

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

		for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
			ChangeSetRowDataGroup afterData = row.getAfterData();
			if (afterData != null) {
				pnl += ChangeSetTransformerUtil.getRowProfitAndLossValue(afterData, ScheduleModelKPIUtils::getGroupProfitAndLoss);

				if (afterData.getMembers().size() > 0) {
					ChangeSetRowData rd = afterData.getMembers().get(0);
					EventGrouping eventGrouping = rd.getEventGrouping();
					if (eventGrouping != null) {
						lateness += LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						violations += ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
					}
				}
			}
			ChangeSetRowDataGroup beforeData = row.getBeforeData();
			if (beforeData != null) {
				pnl -= ChangeSetTransformerUtil.getRowProfitAndLossValue(beforeData, ScheduleModelKPIUtils::getGroupProfitAndLoss);

				if (beforeData.getMembers().size() > 0) {
					ChangeSetRowData rd = beforeData.getMembers().get(0);
					EventGrouping eventGrouping = rd.getEventGrouping();
					if (eventGrouping != null) {
						lateness -= LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						violations -= ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
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
